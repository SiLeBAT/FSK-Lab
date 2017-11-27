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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.rakip.GeneralInformation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.Scope;

class CreatorNodeModel extends NoInternalsModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(CreatorNodeModel.class);

  private CreatorNodeSettings nodeSettings = new CreatorNodeSettings();

  // Input and output port types
  private static final PortType[] IN_TYPES = {};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  public CreatorNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does not validate anything
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
      throws InvalidSettingsException, IOException {
    // Reads model script
    if (StringUtils.isEmpty(nodeSettings.modelScript)) {
      throw new InvalidSettingsException("Model script is not provided");
    }
    final RScript modelRScript = readScript(nodeSettings.modelScript);
    final String modelScript = modelRScript.getScript();

    // Reads parameters script
    final String paramScript;
    if (StringUtils.isNotEmpty(nodeSettings.parameterScript)) {
      paramScript = readScript(nodeSettings.parameterScript).getScript();
    } else {
      paramScript = "";
    }

    // Reads visualization script
    final String visualizationScript;
    if (StringUtils.isNotEmpty(nodeSettings.visualizationScript)) {
      visualizationScript = readScript(nodeSettings.visualizationScript).getScript();
    } else {
      visualizationScript = "";
    }

    // Reads model meta data
    if (StringUtils.isEmpty(nodeSettings.spreadsheet)) {
      throw new InvalidSettingsException("Model metadata is not provided");
    }

    final GenericModel genericModel;
    final File metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.spreadsheet));
    try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
      final XSSFSheet sheet = workbook.getSheetAt(0);

      // Process metadata
      genericModel = new GenericModel();
      genericModel.generalInformation = getGeneralInformation(sheet);
      genericModel.generalInformation.software = "R";
      genericModel.scope = getScope(sheet);
      genericModel.modelMath = getModelMath(sheet);

      // Set variable values and types from parameters script
      try (RController controller = new RController()) {
        controller.eval(paramScript, false);

        for (int i = 0; i < genericModel.modelMath.parameter.size(); i++) {
          Parameter p = genericModel.modelMath.parameter.get(i);

          if (p.classification != Parameter.Classification.input) {
            continue;
          }

          try {
            REXP rexp = controller.eval(p.name, true);
            if (rexp.isNumeric()) {
              p.value = Double.toString(rexp.asDouble());
              p.dataType = "Double";
            }
          } catch (RException | REXPMismatchException exception) {
            // does nothing. Just leave the value blank.
            LOGGER.warn("Could not parse value of parameter " + p.name, exception);
          }
        }
      } catch (RException e) {
        // Does nothing
      }

    } catch (IOException | InvalidFormatException e) {
      throw new InvalidSettingsException("Invalid metadata");
    }

    // Copy resources from settings to a working directory
    Path workingDirectory = FileUtil.createTempDir("workingDirectory").toPath();
    for (final Path resource : nodeSettings.resources) {
      final Path targetPath = workingDirectory.resolve(resource.getFileName().toString());
      Files.copy(resource, targetPath);
    }

    final FskPortObject portObj = new FskPortObject(modelScript, paramScript, visualizationScript,
        genericModel, null, new HashSet<>(), workingDirectory);

    // libraries
    List<String> libraries = modelRScript.getLibraries();
    if (!libraries.isEmpty()) {
      try {
        // Install missing libraries
        final LibRegistry libReg = LibRegistry.instance();
        List<String> missingLibs =
            libraries.stream().filter(lib -> !libReg.isInstalled(lib)).collect(Collectors.toList());
        if (!missingLibs.isEmpty()) {
          libReg.installLibs(missingLibs);
        }

        Set<Path> libPaths = libReg.getPaths(libraries);
        libPaths.forEach(l -> portObj.libs.add(l.toFile()));
      } catch (RException | REXPMismatchException e) {
        LOGGER.error(e.getMessage());
      }
    }

    return new PortObject[] {portObj};
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  /**
   * Reads R script.
   * 
   * @param path File path to R model script. If is assured not to be null or empty.
   * @throws InvalidSettingsException if {@link path} is null or whitespace.
   * @throws IOException if the file cannot be read.
   */
  private static RScript readScript(final String path)
      throws InvalidSettingsException, IOException {
    String trimmedPath = StringUtils.trimToNull(path.trim());

    // path is not null or whitespace, thus try to read it
    try {
      // may throw IOException
      File fScript = FileUtil.getFileFromURL(FileUtil.toURL(trimmedPath));
      RScript script = new RScript(fScript);
      return script;
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      throw new IOException(trimmedPath + ": cannot be read");
    }
  }

  private static String getString(final XSSFSheet sheet, final int rowNumber) {
    final XSSFRow row = sheet.getRow(rowNumber);
    if (row == null)
      throw new IllegalArgumentException("Missing row: #" + rowNumber);
    return row.getCell(5).getStringCellValue();
  }

  private static GeneralInformation getGeneralInformation(final XSSFSheet sheet)
      throws MalformedURLException, InvalidSettingsException {

    final GeneralInformation gi = new GeneralInformation();
    gi.name = getString(sheet, 1);
    gi.identifier = getString(sheet, 2);
    gi.creationDate = sheet.getRow(9).getCell(5).getDateCellValue();
    gi.rights = getString(sheet, 11);
    gi.isAvailable = true;

    final String urlString = getString(sheet, 16);
    gi.url = new URL(StringUtils.defaultIfEmpty(urlString, "http://bfr.bund.de"));

    gi.format = "";
    gi.modificationDate.add(sheet.getRow(10).getCell(5).getDateCellValue());

    return gi;
  }

  private static Scope getScope(final XSSFSheet sheet) throws InvalidSettingsException {

    final Scope scope = new Scope();

    scope.hazard.hazardName = getString(sheet, 3);
    scope.hazard.hazardDescription = getString(sheet, 4);

    scope.product.environmentName = getString(sheet, 5);
    scope.product.environmentDescription = getString(sheet, 6);

    return scope;
  }

  private static ModelMath getModelMath(final XSSFSheet sheet) throws InvalidSettingsException {

    final ModelMath modelMath = new ModelMath();

    // Dependent variables
    final List<String> depNames = Arrays.stream(getString(sheet, 21).split("\\|\\|"))
        .map(String::trim).collect(Collectors.toList());
    final List<String> depUnits = Arrays.stream(getString(sheet, 22).split("\\|\\|"))
        .map(String::trim).collect(Collectors.toList());

    for (int i = 0; i < depNames.size(); i++) {
      final Parameter param = new Parameter();
      param.id = depNames.get(i);
      param.classification = Parameter.Classification.output;
      param.name = depNames.get(i);
      param.unit = depUnits.get(i);
      param.unitCategory = "";
      param.dataType = "";

      modelMath.parameter.add(param);
    }

    // Independent variables
    final List<String> indepNames = Arrays.stream(getString(sheet, 25).split("\\|\\|"))
        .map(String::trim).collect(Collectors.toList());
    final List<String> indepUnits = Arrays.stream(getString(sheet, 26).split("\\|\\|"))
        .map(String::trim).collect(Collectors.toList());
    for (int i = 0; i < indepNames.size(); i++) {
      final Parameter param = new Parameter();
      param.id = indepNames.get(i);
      param.classification = Parameter.Classification.input;
      param.name = indepNames.get(i);
      param.unit = indepUnits.get(i);
      param.unitCategory = "";
      param.dataType = "";

      modelMath.parameter.add(param);
    }

    return modelMath;
  }
}
