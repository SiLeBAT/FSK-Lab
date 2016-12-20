/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.fsklab.nodes.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import javax.xml.stream.XMLStreamException;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.xml.stax.SBMLWriter;

import com.sun.jna.Platform;

import de.bund.bfr.fskml.DCOmexMetaDataHandler;
import de.bund.bfr.knime.fsklab.nodes.MetadataDocument;
import de.bund.bfr.knime.fsklab.nodes.URIS;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 */
public class FskxWriterNodeModel extends NoInternalsModel {

	// Configuration keys
	public static final String CFG_FILE = "file";

	private final SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

	public FskxWriterNodeModel() {
		super(new PortType[] { FskPortObject.TYPE}, null);  // input and output ports
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec) throws Exception {

		FskPortObject portObject = (FskPortObject) inData[0];

		File archiveFile = FileUtil.getFileFromURL(FileUtil.toURL(filePath.getStringValue()));

		try {
			Files.deleteIfExists(archiveFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Previous file with same name could not be overwritten");
		}

		// try to create CombineArchive
		try (CombineArchive archive = new CombineArchive(archiveFile)) {

			DCOmexMetaDataHandler omexMd = new DCOmexMetaDataHandler();

			// Adds model script
			if (portObject.model != null) {
				archive.addEntry(createScriptFile(portObject.model), "model.r", URIS.r);
				omexMd.setModelScript("model.r");
			}

			// Adds parameters script
			if (portObject.param != null) {
				archive.addEntry(createScriptFile(portObject.param), "param.r", URIS.r);
				omexMd.setParametersScript("param.r");
			}

			// Adds visualization script
			if (portObject.viz != null) {
				archive.addEntry(createScriptFile(portObject.viz), "visualization.r", URIS.r);
				omexMd.setVisualizationScript("visualization.r");
			}

			// Adds R workspace file
			if (portObject.workspace != null) {
				archive.addEntry(portObject.workspace, "workspace.r", URIS.r);
				omexMd.setWorkspaceFile("workspace.r");
			}

			// Add descriptions to archive
			omexMd.getElements().stream().map(DefaultMetaDataObject::new).forEach(archive::addDescription);

			// Adds model meta data
			if (portObject.template != null) {
				SBMLDocument doc = new MetadataDocument(portObject.template).doc;

				File metadataFile = FileUtil.createTempFile("metaData", ".pmf");
				try {
					new SBMLWriter().write(doc, metadataFile);
					archive.addEntry(metadataFile, "metaData.pmf", URIS.pmf);
				} catch (SBMLException | XMLStreamException e) {
					e.printStackTrace();
				}
			}

			// Adds R libraries
			URI libUri = null;
			if (Platform.isWindows()) {
				libUri = URIS.zip;
			} else if (Platform.isMac()) {
				libUri = URIS.tgz;
			} else if (Platform.isLinux()) {
				libUri = URIS.tar_gz;
			}
			for (File lib : portObject.libs) {
				archive.addEntry(lib, lib.getName(), libUri);
			}

			archive.pack();
		} catch (Exception e) {
			try {
				Files.delete(archiveFile.toPath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new Exception("File could not be created", e.getCause());
		}

		return new PortObject[] {};
	}

	private static File createScriptFile(String script) throws IOException {
		File f = FileUtil.createTempFile("script", ".r");
		try (FileWriter fw = new FileWriter(f)) {
			fw.write(script);
		}

		return f;
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// does nothing
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] {};
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filePath.saveSettingsTo(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.loadSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.validateSettings(settings);
	}
}
