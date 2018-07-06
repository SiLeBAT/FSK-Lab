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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.jlibsedml.Change;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLTags;
import org.jlibsedml.SedML;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.RakipUtil;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataFactory;
import metadata.ModelMath;
import metadata.Scope;

class ReaderNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private final ReaderNodeSettings nodeSettings = new ReaderNodeSettings();

  public ReaderNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does nothing
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    final Path workingDirectory = FileUtil.createTempDir("workingDirectory").toPath();

    final File file = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.filePath));

    String modelScript = "";
    String visualizationScript = "";
    File workspace = null; // null if missing

    String spreadsheetPath = "";

    GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();
    Scope scope = MetadataFactory.eINSTANCE.createScope();
    DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
    ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

    List<FskSimulation> simulations = new ArrayList<>();

    String readme = "";

    try (final CombineArchive archive = new CombineArchive(file)) {
      for (final ArchiveEntry entry : archive.getEntriesWithFormat(URIS.r)) {

        List<MetaDataObject> descriptions = entry.getDescriptions();

        if (descriptions.size() > 0) {
          final FskMetaDataObject fmdo = new FskMetaDataObject(descriptions.get(0));
          final ResourceType resourceType = fmdo.getResourceType();

          if (resourceType.equals(ResourceType.modelScript)) {
            modelScript = loadTextEntry(entry);
          } else if (resourceType.equals(ResourceType.visualizationScript)) {
            visualizationScript = loadTextEntry(entry);
          } else if (resourceType.equals(ResourceType.workspace)) {
            workspace = FileUtil.createTempFile("workspace", ".r");
            entry.extractFile(workspace);
          }
        }
      }

      // Gets resources
      List<ArchiveEntry> resourceEntries = new ArrayList<>();

      // Take README.txt and leave other txt as resources.
      List<ArchiveEntry> txtEntries = archive.getEntriesWithFormat(URIS.plainText);
      for (ArchiveEntry entry : txtEntries) {

        // If a txt entry has a description then it must be a README.
        if (entry.getDescriptions().size() > 0) {
          readme = loadTextEntry(entry);
        } else {
          resourceEntries.add(entry);
        }
      }

      resourceEntries.addAll(archive.getEntriesWithFormat(URIS.csv));
      resourceEntries.addAll(archive.getEntriesWithFormat(URIS.rData));

      for (final ArchiveEntry entry : resourceEntries) {
        Path targetPath = workingDirectory.resolve(entry.getFileName());
        Files.createFile(targetPath);
        entry.extractFile(targetPath.toFile());
      }

      // Gets metadata
      {
        // Create temporary file with metadata
        Path temp = Files.createTempFile("metadata", ".json");
        ArchiveEntry jsonEntry = archive.getEntriesWithFormat(URIS.json).get(0);
        jsonEntry.extractFile(temp.toFile());

        // Loads metadata from temporary file
        final ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
        
        JsonNode modelNode = mapper.readTree(temp.toFile());
        Object version = modelNode.get("version");
        
        if (version != null) {
          generalInformation =
              mapper.treeToValue(modelNode.get("generalInformation"), GeneralInformation.class);
          scope = mapper.treeToValue(modelNode.get("scope"), Scope.class);
          dataBackground =
              mapper.treeToValue(modelNode.get("dataBackground"), DataBackground.class);
          modelMath = mapper.treeToValue(modelNode.get("modelMath"), ModelMath.class);
        } else {
          modelNode = mapper.readTree(temp.toFile());
          GenericModel genericModel = mapper.readValue(temp.toFile(), GenericModel.class);
          generalInformation = RakipUtil.convert(genericModel.generalInformation);
          scope = RakipUtil.convert(genericModel.scope);
          dataBackground = RakipUtil.convert(genericModel.dataBackground);
          modelMath = RakipUtil.convert(genericModel.modelMath);
        }

        Files.delete(temp); // Deletes temporary file
      }

      // Get simulations
      if (archive.getNumEntriesWithFormat(URIS.sedml) > 0) {
        File simulationsFile = FileUtil.createTempFile("sim", ".sedml");
        ArchiveEntry simEntry = archive.getEntriesWithFormat(URIS.sedml).get(0);
        simEntry.extractFile(simulationsFile);

        // Loads simulations from temporary file
        SedML sedml = Libsedml.readDocument(simulationsFile).getSedMLModel();
        simulations.addAll(loadSimulations(sedml));
      }

      // Get metadata spreadsheet
      URI xlsxURI = URI.create(WriterNodeModel.EXCEL_URI);
      if (archive.getNumEntriesWithFormat(xlsxURI) > 0) {
        File excelFile = FileUtil.createTempFile("metadata", ".xlsx");

        ArchiveEntry excelEntry = archive.getEntriesWithFormat(xlsxURI).get(0);
        excelEntry.extractFile(excelFile);

        spreadsheetPath = excelFile.getAbsolutePath();
      }
    }

    // Retrieve missing libraries from CRAN
    HashSet<String> packagesSet = new HashSet<>();
    if (!modelScript.isEmpty()) {
      packagesSet.addAll(new RScript(modelScript).getLibraries());
    }
    if (!visualizationScript.isEmpty()) {
      packagesSet.addAll(new RScript(visualizationScript).getLibraries());
    }
    List<String> packagesList = new ArrayList<>(packagesSet);


    Path workspacePath = workspace == null ? null : workspace.toPath();

    // The reader node is not using currently the plot, if present. Therefore an
    // empty string is used.
    String plotPath = "";

    final FskPortObject fskObj = new FskPortObject(modelScript, visualizationScript,
        generalInformation, scope, dataBackground, modelMath, workspacePath, packagesList,
        workingDirectory.toString(), plotPath, readme);
    fskObj.simulations.addAll(simulations);
    fskObj.setSpreadsheet(spreadsheetPath);

    return new PortObject[] {fskObj};
  }

  private static String loadTextEntry(final ArchiveEntry entry) throws IOException {
    
    // Create temporary file with script
    File temp = File.createTempFile("temp", null);
    entry.extractFile(temp);

    // Read contents
    String contents = FileUtils.readFileToString(temp, "UTF-8");

    // Delete temporary file
    temp.delete();
    
    return contents;
  }

  /**
   * Load simulations from SedML to a BufferedDataContainer.
   */
  private static List<FskSimulation> loadSimulations(SedML sedml) {

    List<FskSimulation> simulations = new ArrayList<>(sedml.getModels().size());

    for (org.jlibsedml.Model model : sedml.getModels()) {

      FskSimulation fskSimulation = new FskSimulation(model.getId());
      for (Change change : model.getListOfChanges()) {
        if (change.getChangeKind().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND)) {
          ChangeAttribute ca = (ChangeAttribute) change;

          String variable = ca.getTargetXPath().toString();
          String value = ca.getNewValue();
          fskSimulation.getParameters().put(variable, value);
        }
      }

      simulations.add(fskSimulation);
    }

    return simulations;
  }
}
