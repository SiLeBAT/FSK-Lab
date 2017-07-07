/*
 ***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.nodes.rakip.port;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.apache.commons.io.IOUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.rakip.editor.DataBackgroundPanel;
import de.bund.bfr.rakip.editor.GeneralInformationPanel;
import de.bund.bfr.rakip.editor.ModelMathPanel;
import de.bund.bfr.rakip.editor.ScopePanel;
import de.bund.bfr.rakip.generic.GenericModel;

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * 
 * @author Miguel Alba, BfR, Berlin.
 */
public class FskPortObject implements PortObject {

	/**
	 * Convenience access member for
	 * <code>new PortType(FSKPortObject.class)</code>
	 */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FskPortObject.class, true);

	/** Model script. */
	public String model;

	/** Parameters script. */
	public String param;

	/** Visualization script. */
	public String viz;

	/** Model meta data. */
	public GenericModel genericModel = null;

	/** R workspace file. */
	public File workspace;

	/** R library files. */
	public final Set<File> libs;

	private static int numOfInstances = 0;

	public int objectNum;

	public FskPortObject() {
		libs = new HashSet<>();

		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	public FskPortObject(final String model, final String param, final String viz, final GenericModel genericModel,
			final File workspace, final Set<File> libs) {
		this.model = model;
		this.param = param;
		this.viz = viz;
		this.genericModel = genericModel;
		this.workspace = workspace;
		this.libs = libs;

		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	@Override
	public FskPortObjectSpec getSpec() {
		return FskPortObjectSpec.INSTANCE;
	}

	@Override
	public String getSummary() {
		return "FSK Object";
	}

	/**
	 * Serializer used to save this port object.
	 * 
	 * @return a {@link FskPortObject}.
	 */
	public static final class Serializer extends PortObjectSerializer<FskPortObject> {

		private static final String MODEL = "model.R";
		private static final String PARAM = "param.R";
		private static final String VIZ = "viz.R";
		private static final String META_DATA = "metaData";
		private static final String WORKSPACE = "workspace";

		/** {@inheritDoc} */
		@Override
		public void savePortObject(final FskPortObject portObject, final PortObjectZipOutputStream out,
				final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
			// model entry (file with model script)
			out.putNextEntry(new ZipEntry(MODEL));
			IOUtils.write(portObject.model, out);
			out.closeEntry();

			// param entry (file with param script)
			out.putNextEntry(new ZipEntry(PARAM));
			IOUtils.write(portObject.param, out);
			out.closeEntry();

			// viz entry (file with visualization script)
			out.putNextEntry(new ZipEntry(VIZ));
			IOUtils.write(portObject.viz, out);
			out.closeEntry();

			// template entry (file with model meta data)
			if (portObject.genericModel != null) {
				out.putNextEntry(new ZipEntry(META_DATA));
				ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(portObject.genericModel);
				out.closeEntry();
			}

			// workspace entry
			if (portObject.workspace != null) {
				out.putNextEntry(new ZipEntry(WORKSPACE));
				try (FileInputStream fis = new FileInputStream(portObject.workspace)) {
					FileUtil.copy(fis, out);
				}
				out.closeEntry();
			}

			if (!portObject.libs.isEmpty()) {
				out.putNextEntry(new ZipEntry("library.list"));
				List<String> libNames = portObject.libs.stream().map(f -> f.getName().split("\\_")[0])
						.collect(Collectors.toList());
				IOUtils.writeLines(libNames, "\n", out, "UTF-8");
				out.closeEntry();
			}

			out.close();
		}

		@Override
		public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
				throws IOException, CanceledExecutionException {

			FskPortObject portObj = new FskPortObject();

			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				String entryName = entry.getName();

				if (entryName.equals(MODEL)) {
					portObj.model = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(PARAM)) {
					portObj.param = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(VIZ)) {
					portObj.viz = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(META_DATA)) {
					try {
						ObjectInputStream ois = new ObjectInputStream(in);
						portObj.genericModel = (GenericModel) ois.readObject();
					} catch (ClassNotFoundException e) {
					}
				} else if (entryName.equals(WORKSPACE)) {
					portObj.workspace = FileUtil.createTempFile("workspace", ".r");
					try (FileOutputStream fos = new FileOutputStream(portObj.workspace)) {
						FileUtil.copy(in, fos);
					}
				} else if (entryName.equals("library.list")) {
					List<String> libNames = IOUtils.readLines(in, "UTF-8");

					try {
						LibRegistry libRegistry = LibRegistry.instance();
						// Install missing libraries
						List<String> missingLibs = new LinkedList<>();
						for (String lib : libNames) {
							if (!libRegistry.isInstalled(lib)) {
								missingLibs.add(lib);
							}
						}
						if (!missingLibs.isEmpty()) {
							libRegistry.installLibs(missingLibs);
						}
						// Adds to libs the Paths of the libraries converted to
						// Files
						libRegistry.getPaths(libNames).forEach(p -> portObj.libs.add(p.toFile()));
					} catch (RException | REXPMismatchException error) {
						throw new IOException(error.getMessage());
					}
				}
			}

			in.close();

			return portObj;
		}
	}

	/** {Override} */
	@Override
	public JComponent[] getViews() {
		JPanel modelScriptPanel = new ScriptPanel("Model script", model, false);
		JPanel paramScriptPanel = new ScriptPanel("Param script", param, false);
		JPanel vizScriptPanel = new ScriptPanel("Visualization script", viz, false);
		
		GeneralInformationPanel generalInformationPanel = new GeneralInformationPanel(genericModel.getGeneralInformation());
		generalInformationPanel.setName("General information");
		
		ScopePanel scopePanel = new ScopePanel(genericModel.getScope());
		scopePanel.setName("Scope");
		
		DataBackgroundPanel dataBackgroundPanel = new DataBackgroundPanel(genericModel.getDataBackground());
		dataBackgroundPanel.setName("Data background");
		
		ModelMathPanel modelMathPanel = new ModelMathPanel(genericModel.getModelMath());
		modelMathPanel.setName("Model math");

		return new JComponent[] { modelScriptPanel, paramScriptPanel, vizScriptPanel,
				generalInformationPanel, scopePanel, dataBackgroundPanel, modelMathPanel,
				new LibrariesPanel() };
	}

	/** JPanel with list of R libraries. */
	private class LibrariesPanel extends JPanel {

		private static final long serialVersionUID = -5084804515050256443L;

		LibrariesPanel() {
			super(new BorderLayout());
			setName("Libraries list");

			String[] libNames = new String[libs.size()];
			int i = 0;
			for (File lib : libs) {
				libNames[i] = lib.getName();
				i++;
			}

			JList<String> list = new JList<>(libNames);
			list.setLayoutOrientation(JList.VERTICAL);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			add(new JScrollPane(list));
		}
	}
}
