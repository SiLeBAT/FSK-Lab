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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;

import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.rakip.generic.GenericModel;
import de.bund.bfr.rakip.generic.RakipModule;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;

public class WriterNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {};
  
  private static final NodeLogger LOGGER = NodeLogger.getLogger("Writer node");

  private final SettingsModelString filename = new SettingsModelString("file", "");

  public WriterNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    filename.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    filename.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    filename.validateSettings(settings);
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    final FskPortObject fskObj = (FskPortObject) inObjects[0];

    final File archiveFile = FileUtil.getFileFromURL(FileUtil.toURL(filename.getStringValue()));
    archiveFile.delete();

    try (final CombineArchive archive = new CombineArchive(archiveFile)) {

      // Adds model script
      final ArchiveEntry modelEntry = addRScript(archive, fskObj.model, "model.r");
      modelEntry.addDescription(new FskMetaDataObject(ResourceType.modelScript).metaDataObject);

      // Adds parameter script
      final ArchiveEntry paramEntry = addRScript(archive, fskObj.param, "param.r");
      paramEntry
          .addDescription(new FskMetaDataObject(ResourceType.parametersScript).metaDataObject);

      // Adds visualization script
      final ArchiveEntry vizEntry = addRScript(archive, fskObj.viz, "visualization.r");
      vizEntry
          .addDescription(new FskMetaDataObject(ResourceType.visualizationScript).metaDataObject);

      // Adds R workspace file
      if (fskObj.workspace != null) {
        final ArchiveEntry workspaceEntry =
            archive.addEntry(fskObj.workspace, "workspace.r", URIS.r);
        workspaceEntry.addDescription(new FskMetaDataObject(ResourceType.workspace).metaDataObject);
      }

      // Adds model metadata
      addMetaData(archive, fskObj.genericModel, "metaData.json");

      // Adds R libraries
      // TODO: replace with JNA
      final URI libUri;
      if (SystemUtils.IS_OS_WINDOWS) {
        libUri = URIS.zip;
      } else if (SystemUtils.IS_OS_MAC) {
        libUri = URIS.tgz;
      } else if (SystemUtils.IS_OS_LINUX) {
        libUri = URIS.tar_gz;
      } else {
        throw new InvalidSettingsException("Unsupported platform");
      }

      for (final File libFile : fskObj.libs) {
        archive.addEntry(libFile, libFile.getName(), libUri);
      }
      
      archive.pack();
    } catch (Exception e) {
      FileUtils.deleteQuietly(archiveFile);
      LOGGER.error("File could not be created", e);
    }
    
    return new PortObject[] {};
  }


  private static ArchiveEntry addRScript(final CombineArchive archive, final String script,
      final String filename) throws IOException, URISyntaxException {

    final File file = File.createTempFile("temp", ".r");
    FileUtils.writeStringToFile(file, script, "UTF-8");

    final ArchiveEntry entry = archive.addEntry(file, filename, URIS.r);
    file.delete();

    return entry;
  }

  private static ArchiveEntry addMetaData(final CombineArchive archive,
      final GenericModel genericModel, final String filename)
      throws IOException, URISyntaxException {

    final File file = File.createTempFile("temp", ".json");
    final ObjectMapper objectMapper =
        ExtensionsKt.jacksonObjectMapper().registerModule(new RakipModule());
    objectMapper.writeValue(file, genericModel);

    // TODO: JSON uri should be moved to fskml
    final ArchiveEntry entry = archive.addEntry(file, filename, new URI("http://json.org"));
    file.delete();

    return entry;
  }
}
