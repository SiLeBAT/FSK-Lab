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
import java.util.Set;
import java.util.stream.Collectors;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;

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

    final ArrayList<String> libNames = new ArrayList<>();
    final Path workingDirectory = FileUtil.createTempDir("workingDirectory").toPath();

    final File file = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.filePath));

    String modelScript = "";
    String paramScript = "";
    String visualizationScript = "";
    File workspace = null; // null if missing
    GenericModel genericModel = null; // null if missing
    List<FskSimulation> simulations = new ArrayList<>();

    try (final CombineArchive archive = new CombineArchive(file)) {
      for (final ArchiveEntry entry : archive.getEntriesWithFormat(URIS.r)) {

        final FskMetaDataObject fmdo = new FskMetaDataObject(entry.getDescriptions().get(0));
        final ResourceType resourceType = fmdo.getResourceType();

        if (resourceType.equals(ResourceType.modelScript)) {
          modelScript = loadScript(entry);
        } else if (resourceType.equals(ResourceType.parametersScript)) {
          paramScript = loadScript(entry);
        } else if (resourceType.equals(ResourceType.visualizationScript)) {
          visualizationScript = loadScript(entry);
        } else if (resourceType.equals(ResourceType.workspace)) {
          workspace = FileUtil.createTempFile("workspace", ".r");
          entry.extractFile(workspace);
        }
      }

      // Gets resources
      List<ArchiveEntry> resourceEntries = new ArrayList<>();
      resourceEntries.addAll(archive.getEntriesWithFormat(URIS.plainText));
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
        ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
        genericModel = objectMapper.readValue(temp.toFile(), GenericModel.class);

        Files.delete(temp); // Deletes temporary file
      }

      // Gets library URI for the running platform
      final URI libUri = NodeUtils.getLibURI();

      // Gets library names
      for (final ArchiveEntry entry : archive.getEntriesWithFormat(libUri)) {
        final String name = entry.getFileName().split("\\_")[0];
        libNames.add(name);
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
    }

    if (genericModel == null) {
      throw new InvalidSettingsException("Missing model meta data");
    }

    final Set<File> libFiles = new HashSet<>();
    if (!libNames.isEmpty()) {

      LibRegistry libRegistry = LibRegistry.instance();

      // Filters and installs missing libraries
      List<String> missingLibs = libNames.stream().filter(lib -> !libRegistry.isInstalled(lib))
          .collect(Collectors.toList());
      if (!missingLibs.isEmpty()) {
        libRegistry.installLibs(missingLibs);
      }

      // Converts and return set of Paths returned from plugin to set
      Set<File> libs =
          libRegistry.getPaths(libNames).stream().map(Path::toFile).collect(Collectors.toSet());
      libFiles.addAll(libs);
    }

    // validate model
    try (final RController controller = new RController()) {

      // Add path
      final LibRegistry libRegistry = LibRegistry.instance();
      controller.addPackagePath(libRegistry.getInstallationPath());

      // TODO: validate model with parameter values from parameter script

      controller.restorePackagePath();
    }

    Path workspacePath = workspace == null ? null : workspace.toPath();
    final FskPortObject fskObj = new FskPortObject(modelScript, paramScript, visualizationScript,
        genericModel, workspacePath, libFiles, workingDirectory);
    fskObj.simulations.addAll(simulations);

    return new PortObject[] {fskObj};
  }

  private static String loadScript(final ArchiveEntry entry) throws IOException {

    // Create temporary file with script
    Path temp = Files.createTempFile("temp", ".r");
    entry.extractFile(temp.toFile());

    String script = new String(Files.readAllBytes(temp), "UTF-8"); // Read script

    Files.delete(temp); // Delete temporary file

    return script;
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
