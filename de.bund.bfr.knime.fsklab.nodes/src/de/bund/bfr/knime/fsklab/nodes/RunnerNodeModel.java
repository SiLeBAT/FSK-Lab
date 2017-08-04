/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import com.sun.jna.Platform;

import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.rakip.generic.Parameter;
import de.bund.bfr.rakip.generic.ParameterClassification;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskPortObject;

public class RunnerNodeModel extends NodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

	/** Output spec for an FSK object. */
	private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

	/** Output spec for a PNG image. */
	private static final ImagePortObjectSpec PNG_SPEC = new ImagePortObjectSpec(PNGImageContent.TYPE);

	private final InternalSettings internalSettings = new InternalSettings();

	private RunnerNodeSettings settings = new RunnerNodeSettings();

	// Input and output port types
	private static final PortType[] IN_TYPES = { FskPortObject.TYPE, BufferedDataTable.TYPE_OPTIONAL };
	private static final PortType[] OUT_TYPES = { FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL };

	public RunnerNodeModel() {
		super(IN_TYPES, OUT_TYPES);
	}

	// --- internal settings methods ---

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		internalSettings.loadInternals(nodeInternDir);
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		internalSettings.saveInternals(nodeInternDir);
	}

	@Override
	protected void reset() {
		internalSettings.reset();
	}

	// --- node settings methods ---

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		this.settings.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.loadValidatedSettingsFrom(settings);
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FSK_SPEC, PNG_SPEC };
	}

	@Override
	protected PortObject[] execute(PortObject[] inData, ExecutionContext exec) throws Exception {

		FskPortObject fskObj = (FskPortObject) inData[0];

		final List<Parameter> indepVars = fskObj.genericModel.getModelMath().getParameter().stream()
				.filter(it -> it.getClassification() == ParameterClassification.input).collect(Collectors.toList());

		if (!indepVars.isEmpty()) {
			try {
				fskObj.param = loadParameterScript(indepVars);
			} catch (IllegalArgumentException exception) {
				LOGGER.warn(exception.getMessage());
			}
		}

		try (RController controller = new RController()) {
			fskObj = runSnippet(controller, fskObj);
		}

		try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
			final PNGImageContent content = new PNGImageContent(fis);
			internalSettings.plot = content.getImage();
			ImagePortObject imgObj = new ImagePortObject(content, PNG_SPEC);
			return new PortObject[] { fskObj, imgObj };
		} catch (IOException e) {
			LOGGER.warn("There is no image created");
			return new PortObject[] { fskObj };
		}
	}

	/**
	 * Generate a parameter script with parameters names and values from the model
	 * metadata.
	 * 
	 * @param params
	 *            non-empty list of input parameters.
	 * @throw IllegalArgumentException if a parameter is not valid
	 */
	private String loadParameterScript(final List<Parameter> params) {

		String script = "";
		for (final Parameter param : params) {
			final String paramName = param.getName();
			final String paramValue = param.getValue();

			if (StringUtils.isAnyEmpty(paramName, paramValue))
				throw new IllegalArgumentException("Parameter from metadata is not valid: " + param);

			script += paramName + " <- " + paramValue + "\n";
		}

		return script;
	}

	private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj)
			throws IOException, RException, REXPMismatchException {

		// Add path
		LibRegistry libRegistry = LibRegistry.instance();
		String cmd = ".libPaths(c('" + libRegistry.getInstallationPath().toString().replace("\\", "/")
				+ "', .libPaths()))";
		String[] newPaths = controller.eval(cmd).asStrings();

		// Run model
		controller.eval(fskObj.param + "\n" + fskObj.model);

		// Save workspace
		if (fskObj.workspace == null) {
			fskObj.workspace = FileUtil.createTempFile("workspace", ".R");
		}
		controller.eval("save.image('" + fskObj.workspace.getAbsolutePath().replace("\\", "/") + "')");

		// Creates chart into m_imageFile
		try {
			ChartCreator cc = new ChartCreator(controller);
			cc.plot(internalSettings.imageFile.getAbsolutePath().replace("\\", "/"), fskObj.viz);
		} catch (RException e) {
			LOGGER.warn("Visualization script failed");
		}

		// Restore .libPaths() to the original library path which happens to be
		// in the last position
		controller.eval(".libPaths()[" + newPaths.length + "]");

		return fskObj;
	}

	private class ChartCreator {

		final RController controller;

		public ChartCreator(RController controller) throws RException {
			this.controller = controller;

			// initialize necessary R stuff to plot
			if (Platform.isMac()) {
				controller.eval("library('Cairo')");
				controller.eval("options(device='png', bitmapType='cairo')");
			} else {
				controller.eval("options(device='png')");
			}
		}

		public void plot(String path, String vizScript) throws RException {
			// Gets values
			int width = settings.widthModel.getIntValue();
			int height = settings.heightModel.getIntValue();
			String res = settings.resolutionModel.getStringValue();
			int textPointSize = settings.textPointSizeModel.getIntValue();
			Color colour = settings.colourModel.getColorValue();
			String hexColour = String.format("#%02x%02x%02x", colour.getRed(), colour.getGreen(), colour.getBlue());

			String pngCommand = "png('" + path + "', width=" + width + ", height=" + height + ", pointsize="
					+ textPointSize + ", bg='" + hexColour + "', res='" + res + "')";
			controller.eval(pngCommand);

			controller.eval(vizScript);
			controller.eval("dev.off()");

		}
	}

	Image getResultImage() {
		return internalSettings.plot;
	}

	private class InternalSettings {

		private static final String FILE_NAME = "Rplot";

		/**
		 * Non-null image file to use for this current node. Initialized to temp
		 * location.
		 */
		private File imageFile = null;

		private Image plot = null;

		InternalSettings() {
			try {
				imageFile = FileUtil.createTempFile("FskxRunner-", ".png");
			} catch (IOException e) {
				LOGGER.error("Cannot create temporary file.", e);
				throw new RuntimeException(e);
			}
			imageFile.deleteOnExit();
		}

		/** Loads the saved image. */
		void loadInternals(File nodeInternDir) throws IOException {
			final File file = new File(nodeInternDir, FILE_NAME + ".png");

			if (file.exists() && file.canRead()) {
				FileUtil.copy(file, imageFile);
				try (InputStream is = new FileInputStream(imageFile)) {
					plot = new PNGImageContent(is).getImage();
				}
			}
		}

		/** Saves the saved image. */
		protected void saveInternals(File nodeInternDir) throws IOException {
			if (plot != null) {
				final File file = new File(nodeInternDir, FILE_NAME + ".png");
				FileUtil.copy(imageFile, file);
			}
		}

		/** Clear the contents of the image file. */
		protected void reset() {
			plot = null;

			if (imageFile != null) {
				try (OutputStream erasor = new FileOutputStream(imageFile)) {
					erasor.write((new String()).getBytes());
				} catch (final FileNotFoundException e) {
					LOGGER.error("Temporary file is removed.", e);
				} catch (final IOException e) {
					LOGGER.error("Cannot write temporary file.", e);
				}
			}
		}
	}
}
