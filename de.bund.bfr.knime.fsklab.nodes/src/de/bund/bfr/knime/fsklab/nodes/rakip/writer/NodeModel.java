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
package de.bund.bfr.knime.fsklab.nodes.rakip.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import org.apache.commons.lang3.SystemUtils;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;

import de.bund.bfr.fskml.OmexMetaDataHandler;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObject;
import de.bund.bfr.rakip.generic.RakipModule;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

class NodeModel extends NoInternalsModel {

	// Configuration keys
	public static final String CFG_FILE = "file";

	private final SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

	// Input and output port types
	private static final PortType[] IN_TYPES = { FskPortObject.TYPE };
	private static final PortType[] OUT_TYPES = {};

	public NodeModel() {
		super(IN_TYPES, OUT_TYPES);
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

			OmexMetaDataHandler omexMd = new OmexMetaDataHandler();

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
			if (portObject.genericModel != null) {

				File metaDataFile = FileUtil.createTempFile("metaData", ".json");
				ObjectMapper objectMapper = ExtensionsKt.jacksonObjectMapper().registerModule(new RakipModule());
				objectMapper.writeValue(metaDataFile, portObject.genericModel);

				URI jsonURI = new URI("http://www.json.org");
				archive.addEntry(metaDataFile, "metaData.json", jsonURI);
			}

			// Adds R libraries
			URI libUri = null;
			if (SystemUtils.IS_OS_WINDOWS) {
				libUri = URIS.zip;
			} else if (SystemUtils.IS_OS_MAC) {
				libUri = URIS.tgz;
			} else if (SystemUtils.IS_OS_LINUX) {
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
