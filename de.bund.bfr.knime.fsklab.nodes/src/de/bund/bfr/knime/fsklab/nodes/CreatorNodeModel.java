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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
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
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import metadata.Assay;
import metadata.Contact;
import metadata.DataBackground;
import metadata.DietaryAssessmentMethod;
import metadata.GeneralInformation;
import metadata.Hazard;
import metadata.Laboratory;
import metadata.MetadataFactory;
import metadata.ModelCategory;
import metadata.ModelMath;
import metadata.ModificationDate;
import metadata.Parameter;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PopulationGroup;
import metadata.Product;
import metadata.PublicationType;
import metadata.Reference;
import metadata.Scope;
import metadata.StringObject;
import metadata.Study;
import metadata.StudySample;

class CreatorNodeModel extends NoInternalsModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(CreatorNodeModel.class);

  private CreatorNodeSettings nodeSettings = new CreatorNodeSettings();

  // Input and output port types
  private static final PortType[] IN_TYPES = {BufferedDataTable.TYPE_OPTIONAL};
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
    RScript modelRScript = readScript(nodeSettings.modelScript);

    // Read parameters script
    if (StringUtils.isEmpty(nodeSettings.parameterScript)) {
      throw new InvalidSettingsException("Parameter script is not provided");
    }
    RScript paramRScript = readScript(nodeSettings.parameterScript);

    // Create defaultSimulation out of the parameters script
    FskSimulation defaultSimulation = NodeUtils.createDefaultSimulation(paramRScript.getScript());

    // Reads visualization script
    RScript vizRScript;
    if (StringUtils.isNotEmpty(nodeSettings.visualizationScript)) {
      vizRScript = readScript(nodeSettings.visualizationScript);
    } else {
      vizRScript = null;
    }

    final GeneralInformation generalInformation;
    final Scope scope;
    final DataBackground dataBackground;
    final ModelMath modelMath;

    // If an input table is connected then parse the metadata
    if (inData.length == 1 && inData[0] != null) {
      BufferedDataTable metadataTable = (BufferedDataTable) inData[0];

      // parse table
      String[][] values = new String[200][30];
      int i = 0;
      for (DataRow row : metadataTable) {
        if (isRowEmpty(row))
          break;
        int j = 0;
        for (DataCell cell : row) {
          values[i][j] = !cell.isMissing() ? ((StringCell) cell).getStringValue() : "";
          j++;
        }
        i++;
      }

      generalInformation = TableParser.retrieveGeneralInformation(values);
      scope = TableParser.retrieveScope(values);
      dataBackground = TableParser.retrieveDataBackground(values);

      modelMath = MetadataFactory.eINSTANCE.createModelMath();
      for (i = 132; i <= 152; i++) {
        try {
          Parameter param = TableParser.retrieveParameter(values, i);
          modelMath.getParameter().add(param);
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    }

    else {
      // Reads model meta data
      if (StringUtils.isEmpty(nodeSettings.spreadsheet)) {
        throw new InvalidSettingsException("Model metadata is not provided");
      }

      final File metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.spreadsheet));
      try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
        final XSSFSheet sheet = workbook.getSheet(nodeSettings.sheet);

        if (sheet.getPhysicalNumberOfRows() > 29) {
          // Process new RAKIP spreadsheet
          generalInformation = RAKIPSheetImporter.retrieveGeneralInformation(sheet);
          scope = RAKIPSheetImporter.retrieveScope(sheet);
          dataBackground = RAKIPSheetImporter.retrieveDataBackground(sheet);
          modelMath = MetadataFactory.eINSTANCE.createModelMath();
          for (int i = 132; i <= 152; i++) {
            try {
              Parameter param = RAKIPSheetImporter.retrieveParameter(sheet, i);
              modelMath.getParameter().add(param);
            } catch (Exception exception) {
              // ignore exception since it is thrown by empty rows
            }
          }
        } else {
          // Process legacy spreadsheet
          generalInformation = LegacySheetImporter.getGeneralInformation(sheet);
          scope = LegacySheetImporter.getScope(sheet);
          dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
          modelMath = LegacySheetImporter.getModelMath(sheet);

          // Set variable values and types from parameters script
          try (RController controller = new RController()) {
            controller.eval(paramRScript.getScript(), false);

            for (Parameter p : modelMath.getParameter()) {

              if (p.getParameterClassification() == ParameterClassification.INPUT) {
                continue;
              }

              try {
                REXP rexp = controller.eval(p.getParameterName(), true);

                // check the type for a Vector Or Matrix of String or number
                if (rexp.isVector() && rexp.dim() != null) {

                  int nrow = rexp.dim()[0];
                  int ncol = rexp.dim()[1];

                  if (rexp.isNumeric()) {
                    p.setParameterDataType(ParameterType.MATRIX_OF_NUMBERS);
                    p.setParameterValue(buildRMatrix(rexp.asDoubleMatrix(), nrow, ncol));
                  } else {
                    p.setParameterDataType(ParameterType.MATRIX_OF_STRINGS);
                    p.setParameterValue(buildRMatrix(rexp.asStrings(), nrow, ncol));
                  }

                } else if (rexp.isVector() && rexp.length() > 1) {

                  if (rexp.isNumeric()) {
                    p.setParameterDataType(ParameterType.VECTOR_OF_NUMBERS);
                    p.setParameterValue(buildRVector(rexp.asDoubles()));
                  } else {
                    p.setParameterDataType(ParameterType.VECTOR_OF_STRINGS);
                    p.setParameterValue(buildRVector(rexp.asStrings()));
                  }
                } else if (rexp.isNumeric()) {
                  p.setParameterDataType(ParameterType.DOUBLE);
                  p.setParameterValue(Double.toString(rexp.asDouble()));
                }
              } catch (RException | REXPMismatchException exception) {
                // does nothing. Just leave the value blank.
                LOGGER.warn("Could not parse value of parameter " + p.getParameterName(),
                    exception);
              }
            }
          } catch (RException e) {
            // Does nothing
          }
        }
      } catch (IOException | InvalidFormatException e) {
        throw new InvalidSettingsException("Invalid metadata");
      }
    }

    // Copy resources from settings to a working directory
    Path workingDirectory = FileUtil.createTempDir("workingDirectory").toPath();
    for (final Path resource : nodeSettings.resources) {
      final Path targetPath = workingDirectory.resolve(resource.getFileName().toString());
      Files.copy(resource, targetPath);
    }

    String modelScript = modelRScript.getScript();
    String paramScript = paramRScript.getScript();
    String vizScript = vizRScript != null ? vizRScript.getScript() : "";

    final FskPortObject portObj =
        new FskPortObject(modelScript, paramScript, vizScript, generalInformation, scope,
            dataBackground, modelMath, null, new HashSet<>(), workingDirectory);
    if (defaultSimulation != null) {
      portObj.simulations.add(defaultSimulation);
    }

    // libraries
    List<String> libraries = new ArrayList<>();
    libraries.addAll(modelRScript.getLibraries());
    libraries.addAll(paramRScript.getLibraries());
    if (vizRScript != null) {
      libraries.addAll(vizRScript.getLibraries());
    }

    if (!libraries.isEmpty()) {
      try {
        // Install missing libraries
        final LibRegistry libReg = LibRegistry.instance();
        List<String> missingLibs =
            libraries.stream().filter(lib -> !libReg.isInstalled(lib)).collect(Collectors.toList());
        if (!missingLibs.isEmpty()) {
          libReg.installLibs(missingLibs);

          Set<Path> libPaths = libReg.getPaths(libraries);
          libPaths.forEach(l -> portObj.libs.add(l.toFile()));
        }
      } catch (RException | REXPMismatchException e) {
        LOGGER.error(e.getMessage());
      }
    }

    return new PortObject[] {portObj};
  }

  private static String buildRVector(double[] doubles) {
    String stringRepresentation = Arrays.toString(doubles); // String representation: [a, b, c]

    // Remove opening and closing brackets [].
    stringRepresentation = stringRepresentation.substring(0, stringRepresentation.length() - 1);

    // Build R vector
    return "c(" + stringRepresentation + ")";
  }

  private static String buildRVector(String[] strings) {
    String stringRepresentation = Arrays.toString(strings); // String representation: ["a", "b"]

    // Remove opening and closing brackets
    stringRepresentation = stringRepresentation.substring(0, stringRepresentation.length() - 1);

    // Build R vector
    return "c(" + stringRepresentation + ")";
  }

  private static String buildRMatrix(double[][] matrix, int nrow, int ncol) {

    String stringRepresentation = Arrays.deepToString(matrix);
    stringRepresentation.replace("[", "(");
    stringRepresentation.replace("]", ")");

    // Build R matrix
    return "matrix(c" + stringRepresentation + ", nrow=" + nrow + ", ncol=" + ncol
        + ", byrow=TRUE)";
  }

  private static String buildRMatrix(String[] matrix, int nrow, int ncol) {

    String stringRepresentation = Arrays.deepToString(matrix);
    stringRepresentation.replace("[", "(");
    stringRepresentation.replace("]", ")");

    // Build R matrix
    return "matrix(c" + stringRepresentation + ", nrow=" + nrow + ", ncol=" + ncol
        + ", byrow=TRUE)";
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

  private static boolean isRowEmpty(DataRow row) {
    for (DataCell cell : row)
      if (!cell.isMissing())
        return false;
    return true;
  }

  private static class LegacySheetImporter {

    static String getString(final XSSFSheet sheet, final int rowNumber) {
      final XSSFRow row = sheet.getRow(rowNumber);
      if (row == null)
        throw new IllegalArgumentException("Missing row: #" + rowNumber);
      return row.getCell(5).getStringCellValue();
    }

    static GeneralInformation getGeneralInformation(final XSSFSheet sheet)
        throws MalformedURLException, InvalidSettingsException {

      GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();

      final String name = getString(sheet, 1);
      if (!name.isEmpty()) {
        generalInformation.setName(name);
      }

      final String identifier = getString(sheet, 2);
      if (!identifier.isEmpty()) {
        generalInformation.setIdentifier(identifier);
      }

      final Date creationDate = sheet.getRow(9).getCell(5).getDateCellValue();
      if (creationDate != null) {
        generalInformation.setCreationDate(creationDate);
      }

      final String rights = getString(sheet, 11);
      if (!rights.isEmpty()) {
        generalInformation.setRights(rights);
      }

      generalInformation.setAvailable(true);
      generalInformation.setFormat("");

      final Date modificationDate = sheet.getRow(10).getCell(5).getDateCellValue();
      if (modificationDate != null) {
        ModificationDate md = MetadataFactory.eINSTANCE.createModificationDate();
        md.setValue(modificationDate);
        generalInformation.getModificationdate().add(md);
      }

      return generalInformation;
    }

    static Scope getScope(final XSSFSheet sheet) throws InvalidSettingsException {

      final Scope scope = MetadataFactory.eINSTANCE.createScope();

      Hazard hazard = MetadataFactory.eINSTANCE.createHazard();
      hazard.setHazardName(getString(sheet, 3));
      hazard.setHazardDescription(getString(sheet, 4));
      scope.getHazard().add(hazard);

      Product product = MetadataFactory.eINSTANCE.createProduct();
      product.setProductName(getString(sheet, 5));
      product.setProductDescription(getString(sheet, 6));
      scope.getProduct().add(product);

      return scope;
    }

    static ModelMath getModelMath(final XSSFSheet sheet) throws InvalidSettingsException {

      final ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

      // Dependent variables
      final List<String> depNames = Arrays.stream(getString(sheet, 21).split("\\|\\|"))
          .map(String::trim).collect(Collectors.toList());
      final List<String> depUnits = Arrays.stream(getString(sheet, 22).split("\\|\\|"))
          .map(String::trim).collect(Collectors.toList());

      for (int i = 0; i < depNames.size(); i++) {
        final Parameter param = MetadataFactory.eINSTANCE.createParameter();
        param.setParameterID(depNames.get(i));
        param.setParameterClassification(ParameterClassification.OUTPUT);
        param.setParameterName(depNames.get(i));
        param.setParameterUnit(depUnits.get(i));
        param.setParameterUnitCategory("");
        param.setParameterDataType(ParameterType.OTHER);

        modelMath.getParameter().add(param);
      }

      // Independent variables
      final List<String> indepNames = Arrays.stream(getString(sheet, 25).split("\\|\\|"))
          .map(String::trim).collect(Collectors.toList());
      final List<String> indepUnits = Arrays.stream(getString(sheet, 26).split("\\|\\|"))
          .map(String::trim).collect(Collectors.toList());

      for (int i = 0; i < indepNames.size(); i++) {
        final Parameter param = MetadataFactory.eINSTANCE.createParameter();
        param.setParameterID(indepNames.get(i));
        param.setParameterClassification(ParameterClassification.INPUT);
        param.setParameterName(indepNames.get(i));
        param.setParameterUnit(indepUnits.get(i));
        param.setParameterUnitCategory("");
        param.setParameterDataType(ParameterType.OTHER);

        modelMath.getParameter().add(param);
      }

      return modelMath;
    }
  }

  private enum RakipColumn {
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J,
    K,
    L,
    M,
    N,
    O,
    P,
    Q,
    R,
    S,
    T,
    U,
    V,
    W,
    X,
    Y,
    Z,
    AA
  }

  private enum RakipRow {

    GENERAL_INFORMATION_NAME(2),
    GENERAL_INFORMATION_SOURCE(3),
    GENERAL_INFORMATION_IDENTIFIER(4),
    GENERAL_INFORMATION_RIGHTS(8),
    GENERAL_INFORMATION_AVAILABILITY(9),
    GENERAL_INFORMATION_LANGUAGE(24),
    GENERAL_INFORMATION_SOFTWARE(25),
    GENERAL_INFORMATION_LANGUAGE_WRITTEN_IN(26),
    GENERAL_INFORMATION_STATUS(32),
    GENERAL_INFORMATION_OBJECTIVE(33),
    GENERAL_INFORMATION_DESCRIPTION(34),

    MODEL_CATEGORY_CLASS(27),
    MODEL_CATEGORY_SUBCLASS(28),
    MODEL_CATEGORY_COMMENT(29),
    MODEL_CATEGORY_PROCESS(30),

    HAZARD_TYPE(46),
    HAZARD_NAME(47),
    HAZARD_DESCRIPTION(48),
    HAZARD_UNIT(49),
    HAZARD_ADVERSE_EFFECT(50),
    HAZARD_CONTAMINATION_SOURCE(51),
    HAZARD_BMD(52), // benchmark
                    // dose
    HAZARD_MRL(53), // maximum residue limit
    HAZARD_NOAEL(54), // No Observed Adverse Affect Level
    HAZARD_LOAEL(55), // Lowest Observed Adverse Effect Level
    HAZARD_AOEL(56), // Acceptable Operator Exposure Level
    HAZARD_ARFD(57), // Acute Reference Dose
    HAZARD_ADI(58), // Acceptable Daily Intake
    HAZARD_IND_SUM(59),

    POPULATION_GROUP_NAME(60), // Population name
    POPULATION_GROUP_TARGET(61), // Target population
    POPULATION_GROUP_SPAN(62), // Population span (years)
    POPULATION_GROUP_DESCRIPTION(63), // Population description
    POPULATION_GROUP_AGE(64), // Population age
    POPULATION_GROUP_GENDER(65), // Population gender
    POPULATION_GROUP_BMI(66), // Body mass index
    POPULATION_GROUP_DIET(67), // Special diet groups
    POPULATION_GROUP_PATTERN_CONSUMPTION(68),
    POPULATION_GROUP_REGION(69),
    POPULATION_GROUP_COUNTRY(70),
    POPULATION_GROUP_RISK(71), // Risk and population
                               // factors
    POPULATION_GROUP_SEASON(72),

    STUDY_ID(77),
    STUDY_TITLE(78),
    STUDY_DESCRIPTION(79),
    STUDY_DESIGN_TYPE(80),
    STUDY_ASSAY_MEASUREMENTS_TYPE(81),
    STUDY_ASSAY_TECHNOLOGY_TYPE(82),
    STUDY_ASSAY_TECHNOLOGY_PLATFORM(83),
    STUDY_ACCREDITATION_PROCEDURE(84),
    STUDY_PROTOCOL_NAME(85),
    STUDY_PROTOCOL_TYPE(86),
    STUDY_PROTOCOL_DESCRIPTION(87),
    STUDY_PROTOCOL_URI(88),
    STUDY_PROTOCOL_VERSION(89),
    STUDY_PROTOCOL_PARAMETERS_NAME(90),
    STUDY_PROTOCOL_COMPONENTS_NAME(91),
    STUDY_PROTOCOL_COMPONENTS_TYPE(92),

    STUDY_SAMPLE_NAME(93),
    STUDY_SAMPLE_PROTOCOL(94), // Protocol of sample collection
    STUDY_SAMPLE_STRATEGY(95), // Sampling strategy
    STUDY_SAMPLE_TYPE(96), // Type of sampling program
    STUDY_SAMPLE_METHOD(97), // Sampling method
    STUDY_SAMPLE_PLAN(98), // Sampling plan
    STUDY_SAMPLE_WEIGHT(99), // Sampling weight
    STUDY_SAMPLE_SIZE(100), // Sampling size
    STUDY_SAMPLE_SIZE_UNIT(101), // Lot size unit
    STUDY_SAMPLE_POINT(102), // Sampling point

    DIETARY_ASSESSMENT_METHOD_TOOL(103), // Methodological tool to collect data
    DIETARY_ASSESSMENT_METHOD_1DAY(104), // Number of non-consecutive one-day
    DIETARY_ASSESSMENT_METHOD_SOFTWARE_TOOL(105), // Dietary software tool
    DIETARY_ASSESSMENT_METHOD_ITEMS(106), // Number of food items
    DIETARY_ASSESSMENT_METHOD_RECORD_TYPE(107), // Type of records
    DIETARY_ASSESSMENT_METHOD_DESCRIPTORS(108), // Food descriptors

    LABORATORY_ACCREDITATION(109),
    LABORATORY_NAME(110),
    LABORATORY_COUNTRY(111),

    ASSAY_NAME(112), // Assay name
    ASSAY_DESCRIPTION(113), // Assay description
    ASSAY_MOIST_PERC(114), // Percentage of moisture
    ASSAY_FAT_PERC(115), // Percentage of fat
    ASSAY_DETECTION_LIMIT(116), /// Limit of detection
    ASSAY_QUANTIFICATION_LIMIT(117), // Limit of quantification
    ASSAY_LEFT_CENSORED_DATA(118), // Left-censored data
    ASSAY_CONTAMINATION_RANGE(119), // Range of contamination
    ASSAY_UNCERTAINTY_VALUE(120); // Uncertainty value

    /** Actual row number. 0-based. */
    final int num;

    /**
     * @param num 1-based row number in spreadsheet. The actual number stored is 0-based.
     */
    RakipRow(int num) {
      this.num = num - 1;
    }
  }

  private static class RAKIPSheetImporter {

    /**
     * @throws IllegalStateException if the cell contains a string
     * @return 0 for blank cells
     */
    static Double getNumericValue(XSSFSheet sheet, int row, RakipColumn col) {
      XSSFCell cell = sheet.getRow(row).getCell(col.ordinal());
      return cell.getNumericCellValue();
    }

    static Double getNumericValue(XSSFSheet sheet, RakipRow row, RakipColumn col) {
      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      return cell.getNumericCellValue();
    }

    /**
     * @return empty string for blank cells.
     */
    static String getStringValue(XSSFSheet sheet, int row, RakipColumn col) {
      XSSFCell cell = sheet.getRow(row).getCell(col.ordinal());
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        return Double.toString(cell.getNumericCellValue());
      }
      return cell.getStringCellValue();
    }

    static String getStringValue(XSSFSheet sheet, RakipRow row, RakipColumn col) {
      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        return Double.toString(cell.getNumericCellValue());
      }
      return cell.getStringCellValue();
    }

    static List<StringObject> getStringObjectList(XSSFSheet sheet, RakipRow row, RakipColumn col) {

      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      String[] strings = cell.getStringCellValue().split(",");

      List<StringObject> stringObjects = new ArrayList<>(strings.length);
      for (String value : strings) {
        StringObject so = MetadataFactory.eINSTANCE.createStringObject();
        so.setValue(value);
        stringObjects.add(so);
      }

      return stringObjects;
    }

    static GeneralInformation retrieveGeneralInformation(XSSFSheet sheet) {

      GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();

      // name
      final String name = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_NAME, RakipColumn.I);
      if (StringUtils.isNotEmpty(name)) {
        generalInformation.setName(name);
      }

      // source
      final String source =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_SOURCE, RakipColumn.I);
      if (StringUtils.isNotEmpty(source)) {
        generalInformation.setSource(source);
      }

      // identifier
      final String identifier =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_IDENTIFIER, RakipColumn.I);
      if (StringUtils.isNotEmpty(identifier)) {
        generalInformation.setIdentifier(identifier);
      }

      // TODO: creation date

      // rights
      final String rights =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_RIGHTS, RakipColumn.I);
      if (StringUtils.isNotEmpty(rights)) {
        generalInformation.setRights(rights);
      }

      // available
      final String isAvailableString =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_AVAILABILITY, RakipColumn.I);
      if (StringUtils.isNotEmpty(isAvailableString)) {
        boolean isAvailable = isAvailableString.equals("Yes");
        generalInformation.setAvailable(isAvailable);
      }

      // TODO: format

      // language
      final String language =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_LANGUAGE, RakipColumn.I);
      if (StringUtils.isNotEmpty(language)) {
        generalInformation.setLanguage(language);
      }

      // software
      final String software =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_SOFTWARE, RakipColumn.I);
      if (StringUtils.isNotEmpty(software)) {
        generalInformation.setSoftware(software);
      }

      // language written in
      final String languageWrittenIn =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_LANGUAGE_WRITTEN_IN, RakipColumn.I);
      if (StringUtils.isNotEmpty(languageWrittenIn)) {
        generalInformation.setLanguageWrittenIn(languageWrittenIn);
      }

      // status
      final String status =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_STATUS, RakipColumn.I);
      if (StringUtils.isNotEmpty(status)) {
        generalInformation.setStatus(status);
      }

      // objective
      final String objective =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_OBJECTIVE, RakipColumn.I);
      if (StringUtils.isNotEmpty(objective)) {
        generalInformation.setObjective(objective);
      }

      // description
      final String description =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_DESCRIPTION, RakipColumn.I);
      if (StringUtils.isNotEmpty(description)) {
        generalInformation.setDescription(description);
      }

      // TODO: author (1..1)

      // creator (0..n)
      for (int numRow = 3; numRow < 7; numRow++) {
        try {
          Contact contact = retrieveContact(sheet, numRow);
          generalInformation.getCreators().add(contact);
        } catch (Exception exception) {
        }
      }

      // model category (0..n)
      try {
        ModelCategory modelCategory = retrieveModelCategory(sheet);
        generalInformation.getModelCategory().add(modelCategory);
      } catch (Exception exception) {
      }

      // reference (1..n)
      for (int numRow = 14; numRow < 17; numRow++) {
        try {
          Reference reference = retrieveReference(sheet, numRow);
          generalInformation.getReference().add(reference);
        } catch (Exception exception) {
        }
      }

      // TODO: modification date (0..n)

      return generalInformation;
    }

    /**
     * @throw IllegalArgumentException if mail is empty.
     */
    static Contact retrieveContact(XSSFSheet sheet, int row) {

      String email = getStringValue(sheet, row, RakipColumn.R);

      // Check mandatory properties and throw exception if missing
      if (email.isEmpty()) {
        throw new IllegalArgumentException("Missing mail");
      }

      Contact contact = MetadataFactory.eINSTANCE.createContact();
      contact.setTitle(getStringValue(sheet, row, RakipColumn.K));
      contact.setFamilyName(getStringValue(sheet, row, RakipColumn.O));
      contact.setGivenName(getStringValue(sheet, row, RakipColumn.M));
      contact.setEmail(email);
      contact.setTelephone(getStringValue(sheet, row, RakipColumn.Q));
      contact.setStreetAddress(getStringValue(sheet, row, RakipColumn.W));
      contact.setCountry(getStringValue(sheet, row, RakipColumn.S));
      contact.setCity(getStringValue(sheet, row, RakipColumn.T));
      contact.setZipCode(getStringValue(sheet, row, RakipColumn.U));
      contact.setRegion(getStringValue(sheet, row, RakipColumn.Y));
      // time zone not included in spreadsheet ?
      // gender not included in spreadsheet ?
      // note not included in spreadsheet ?
      contact.setOrganization(getStringValue(sheet, row, RakipColumn.P));

      return contact;
    }

    /**
     * Import reference from Excel row.
     * 
     * <ul>
     * <li>Is_reference_description? in the K column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Mandatory. Takes "Yes" or "No".
     * Other strings are discarded.
     *
     * <li>Publication type in the L column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional. Takes the full name of a
     * RIS reference type.
     *
     * <li>Date in the M column. Type {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}.
     * Optional. Format `YYYY/MM/DD/other info` where the fields are optional. Examples:
     * `2017/11/16/noon`, `2017/11/16`, `2017/11`, `2017`.
     *
     * <li>PubMed Id in the N column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC}. Optional. Unique unsigned
     * integer. Example: 20069275
     *
     * <li>DOI in the O column. Type {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}.
     * Mandatory. Example: 10.1056/NEJM199710303371801.
     *
     * <li>Publication author list in the P column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional. The authors are defined
     * with last name, name and joined with semicolons. Example:
     * `Ungaretti-Haberbeck,L;Plaza-Rodr�guez,C;Desvignes,V`
     *
     * <li>Publication title in the Q column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional.
     *
     * <li>Publication abstract in the R column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional.
     *
     * <li>Publication journal/vol/issue in the S column. Type
     * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional.
     *
     * <li>Publication status. // TODO: publication status
     *
     * <li>Publication website. Type {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}.
     * Optional. Invalid urls are discarded.
     *
     * @throws IllegalArgumentException if isReferenceDescription or DOI are missing
     */
    static Reference retrieveReference(XSSFSheet sheet, int row) {

      /*
       * Journal, volume and issue cannot be parsed since there are encoded as free text in the S
       * column.
       * 
       * The date format in Google Drive DD/MM/YYYY does not match the used in the local spreadsheet
       * YYYY/MM/DD. Therefore it cannot be parsed. Column M.
       * 
       * The author list needs a explicitly defined splitter. The Google Drive spreadsheet does not
       * define it and a comma is used in the local spreadsheet. What should we use to parse it?
       */

      String isReferenceDescriptionString = getStringValue(sheet, row, RakipColumn.K);
      String doi = getStringValue(sheet, row, RakipColumn.O);

      // Check mandatory properties and throw exception if missing
      if (isReferenceDescriptionString.isEmpty()) {
        throw new IllegalArgumentException("Missing Is reference description?");
      }
      if (doi.isEmpty()) {
        throw new IllegalArgumentException("Missing DOI");
      }

      Reference reference = MetadataFactory.eINSTANCE.createReference();

      // Is reference description
      if (StringUtils.isNotEmpty(isReferenceDescriptionString)) {
        boolean isReferenceDescription = isReferenceDescriptionString.equals("Yes");
        reference.setIsReferenceDescription(isReferenceDescription);
      }

      // Publication type
      // Get publication type from literal. Get null if invalid literal.
      String publicationTypeLiteral = getStringValue(sheet, row, RakipColumn.L);
      PublicationType publicationType = PublicationType.get(publicationTypeLiteral);
      if (publicationType != null) {
        reference.setPublicationType(publicationType);
      }

      // PMID
      Double pmid = getNumericValue(sheet, row, RakipColumn.N);
      if (pmid != null) {
        reference.setPmid(Double.toString(pmid));
      }

      // DOI
      if (StringUtils.isNotEmpty(doi)) {
        reference.setDoi(doi);
      }

      reference.setAuthorList(getStringValue(sheet, row, RakipColumn.P));
      reference.setPublicationTitle(getStringValue(sheet, row, RakipColumn.Q));
      reference.setPublicationAbstract(getStringValue(sheet, row, RakipColumn.R));

      // TODO: journal
      // TODO: issue

      reference.setPublicationStatus(getStringValue(sheet, row, RakipColumn.T));

      // website
      reference.setPublicationWebsite(getStringValue(sheet, row, RakipColumn.U));

      // TODO: comment

      return reference;
    }

    /**
     * Import [ModelCategory] from Excel sheet.
     *
     * <ul>
     * <li>Model class from H27. Mandatory.
     * <li>Model sub class from H28. Optional.
     * <li>Model class comment from H29. Optional.
     * <li>Basic process from H32. Optional.
     * </ul>
     *
     * @throws IllegalArgumentException if modelClass is missing.
     */
    static ModelCategory retrieveModelCategory(XSSFSheet sheet) {

      // Check mandatory properties and throw exception if missing
      if (sheet.getRow(RakipRow.MODEL_CATEGORY_CLASS.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing model class");
      }

      ModelCategory modelCategory = MetadataFactory.eINSTANCE.createModelCategory();

      modelCategory
          .setModelClass(getStringValue(sheet, RakipRow.MODEL_CATEGORY_CLASS, RakipColumn.I));

      List<StringObject> modelCategorySubclass =
          getStringObjectList(sheet, RakipRow.MODEL_CATEGORY_SUBCLASS, RakipColumn.I);
      modelCategory.getModelSubClass().addAll(modelCategorySubclass);

      modelCategory.setModelClassComment(
          getStringValue(sheet, RakipRow.MODEL_CATEGORY_COMMENT, RakipColumn.I));
      modelCategory
          .setBasicProcess(getStringValue(sheet, RakipRow.MODEL_CATEGORY_PROCESS, RakipColumn.I));

      return modelCategory;
    }

    static Scope retrieveScope(XSSFSheet sheet) {

      Scope scope = MetadataFactory.eINSTANCE.createScope();

      try {
        Hazard hazard = retrieveHazard(sheet);
        scope.getHazard().add(hazard);
      } catch (IllegalArgumentException exception) {
        // ignore exception since the hazard is optional
      }
      try {
        PopulationGroup populationGroup = retrievePopulationGroup(sheet);
        scope.setPopulationGroup(populationGroup);
      } catch (IllegalArgumentException exception) {
        // ignore exception since the population group is optional
      }
      return scope;
    }

    static Hazard retrieveHazard(XSSFSheet sheet) {

      // Check mandatory properties
      if (sheet.getRow(RakipRow.HAZARD_TYPE.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard type is missing");
      }
      if (sheet.getRow(RakipRow.HAZARD_NAME.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard name is missing");
      }
      if (sheet.getRow(RakipRow.HAZARD_UNIT.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard unit is missing");
      }

      Hazard hazard = MetadataFactory.eINSTANCE.createHazard();
      hazard.setHazardType(getStringValue(sheet, RakipRow.HAZARD_TYPE, RakipColumn.I));
      hazard.setHazardName(getStringValue(sheet, RakipRow.HAZARD_NAME, RakipColumn.I));
      hazard
          .setHazardDescription(getStringValue(sheet, RakipRow.HAZARD_DESCRIPTION, RakipColumn.I));
      hazard.setHazardUnit(getStringValue(sheet, RakipRow.HAZARD_UNIT, RakipColumn.I));
      hazard.setAdverseEffect(getStringValue(sheet, RakipRow.HAZARD_ADVERSE_EFFECT, RakipColumn.I));
      hazard.setSourceOfContamination(
          getStringValue(sheet, RakipRow.HAZARD_CONTAMINATION_SOURCE, RakipColumn.I));
      hazard.setBenchmarkDose(getStringValue(sheet, RakipRow.HAZARD_BMD, RakipColumn.I));
      hazard.setMaximumResidueLimit(getStringValue(sheet, RakipRow.HAZARD_MRL, RakipColumn.I));
      hazard.setNoObservedAdverseAffectLevel(
          getStringValue(sheet, RakipRow.HAZARD_NOAEL, RakipColumn.I));
      hazard.setLowestObservedAdverseAffectLevel(
          getStringValue(sheet, RakipRow.HAZARD_LOAEL, RakipColumn.I));
      hazard.setAcceptableOperatorExposureLevel(
          getStringValue(sheet, RakipRow.HAZARD_AOEL, RakipColumn.I));
      hazard.setAcuteReferenceDose(getStringValue(sheet, RakipRow.HAZARD_ARFD, RakipColumn.I));
      hazard.setHazardIndSum(getStringValue(sheet, RakipRow.HAZARD_IND_SUM, RakipColumn.I));

      return hazard;
    }

    static PopulationGroup retrievePopulationGroup(XSSFSheet sheet) {

      // Check mandatory properties
      if (sheet.getRow(RakipRow.POPULATION_GROUP_NAME.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing population name");
      }

      PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();
      populationGroup
          .setPopulationName(getStringValue(sheet, RakipRow.POPULATION_GROUP_NAME, RakipColumn.I));
      populationGroup.setTargetPopulation(
          getStringValue(sheet, RakipRow.POPULATION_GROUP_TARGET, RakipColumn.I));

      List<StringObject> populationSpan =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_SPAN, RakipColumn.I);
      populationGroup.getPopulationSpan().addAll(populationSpan);

      List<StringObject> populationDescription =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_DESCRIPTION, RakipColumn.I);
      populationGroup.getPopulationDescription().addAll(populationDescription);

      List<StringObject> populationAge =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_AGE, RakipColumn.I);
      populationGroup.getPopulationAge().addAll(populationAge);

      populationGroup.setPopulationGender(
          getStringValue(sheet, RakipRow.POPULATION_GROUP_GENDER, RakipColumn.I));

      List<StringObject> bmi =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_BMI, RakipColumn.I);
      populationGroup.getBmi().addAll(bmi);

      List<StringObject> specialDietGroups =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_DIET, RakipColumn.I);
      populationGroup.getSpecialDietGroups().addAll(specialDietGroups);

      List<StringObject> patternConsumption =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_PATTERN_CONSUMPTION, RakipColumn.I);
      populationGroup.getPatternConsumption().addAll(patternConsumption);

      List<StringObject> region =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_REGION, RakipColumn.I);
      populationGroup.getRegion().addAll(region);

      List<StringObject> country =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_COUNTRY, RakipColumn.I);
      populationGroup.getCountry().addAll(country);

      List<StringObject> populationRiskFactor =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_RISK, RakipColumn.I);
      populationGroup.getPopulationRiskFactor().addAll(populationRiskFactor);

      List<StringObject> season =
          getStringObjectList(sheet, RakipRow.POPULATION_GROUP_SEASON, RakipColumn.I);
      populationGroup.getSeason().addAll(season);

      return populationGroup;
    }

    static DataBackground retrieveDataBackground(XSSFSheet sheet) {

      DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();

      dataBackground.setStudy(retrieveStudy(sheet));

      try {
        StudySample studySample = retrieveStudySample(sheet);
        dataBackground.getStudysample().add(studySample);
      } catch (Exception exception) {
        // ignore errors since StudySample is optional
      }

      try {
        DietaryAssessmentMethod dietaryAssessmentMethod = retrieveDAM(sheet);
        dataBackground.setDietaryassessmentmethod(dietaryAssessmentMethod);
      } catch (Exception exception) {
        // ignore errors since DietaryAssessmentMethod is optional
      }

      try {
        Laboratory laboratory = retrieveLaboratory(sheet);
        dataBackground.setLaboratory(laboratory);
      } catch (Exception exception) {
        // ignore errors since Laboratory is optional
      }

      try {
        Assay assay = retrieveAssay(sheet);
        dataBackground.getAssay().add(assay);
      } catch (Exception exception) {
        // ignore errors since Assay is optional
      }

      return dataBackground;
    }

    static Study retrieveStudy(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.STUDY_TITLE.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing study title");
      }

      Study study = MetadataFactory.eINSTANCE.createStudy();
      study.setStudyIdentifier(getStringValue(sheet, RakipRow.STUDY_ID, RakipColumn.I));
      study.setStudyTitle(getStringValue(sheet, RakipRow.STUDY_TITLE, RakipColumn.I));
      study.setStudyDescription(getStringValue(sheet, RakipRow.STUDY_DESCRIPTION, RakipColumn.I));
      study.setStudyDesignType(getStringValue(sheet, RakipRow.STUDY_DESIGN_TYPE, RakipColumn.I));
      study.setStudyAssayMeasurementType(
          getStringValue(sheet, RakipRow.STUDY_ASSAY_MEASUREMENTS_TYPE, RakipColumn.I));
      study.setStudyAssayTechnologyType(
          getStringValue(sheet, RakipRow.STUDY_ASSAY_TECHNOLOGY_TYPE, RakipColumn.I));
      study.setStudyAssayTechnologyPlatform(
          getStringValue(sheet, RakipRow.STUDY_ASSAY_TECHNOLOGY_PLATFORM, RakipColumn.I));
      study.setAccreditationProcedureForTheAssayTechnology(
          getStringValue(sheet, RakipRow.STUDY_ACCREDITATION_PROCEDURE, RakipColumn.I));
      study
          .setStudyProtocolName(getStringValue(sheet, RakipRow.STUDY_PROTOCOL_NAME, RakipColumn.I));
      study
          .setStudyProtocolType(getStringValue(sheet, RakipRow.STUDY_PROTOCOL_TYPE, RakipColumn.I));
      study.setStudyProtocolDescription(
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_DESCRIPTION, RakipColumn.I));
      try {
        study.setStudyProtocolURI(
            new URI(getStringValue(sheet, RakipRow.STUDY_PROTOCOL_URI, RakipColumn.I)));
      } catch (URISyntaxException e) {
      }
      study.setStudyProtocolVersion(
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_VERSION, RakipColumn.I));
      study.setStudyProtocolParametersName(
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_PARAMETERS_NAME, RakipColumn.I));
      study.setStudyProtocolComponentsName(
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_COMPONENTS_NAME, RakipColumn.I));
      study.setStudyProtocolComponentsType(
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_COMPONENTS_TYPE, RakipColumn.I));

      return study;
    }

    static StudySample retrieveStudySample(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.STUDY_SAMPLE_NAME.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sample name");
      }
      if (sheet.getRow(RakipRow.STUDY_SAMPLE_PROTOCOL.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sampling plan");
      }
      if (sheet.getRow(RakipRow.STUDY_SAMPLE_STRATEGY.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sampling weight");
      }

      StudySample studySample = MetadataFactory.eINSTANCE.createStudySample();
      studySample.setSampleName(getStringValue(sheet, RakipRow.STUDY_SAMPLE_NAME, RakipColumn.I));
      studySample.setProtocolOfSampleCollection(
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_PROTOCOL, RakipColumn.I));
      studySample.setSamplingStrategy(
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_STRATEGY, RakipColumn.I));
      studySample.setTypeOfSamplingProgram(
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_TYPE, RakipColumn.I));
      studySample
          .setSamplingMethod(getStringValue(sheet, RakipRow.STUDY_SAMPLE_METHOD, RakipColumn.I));
      studySample.setSamplingPlan(getStringValue(sheet, RakipRow.STUDY_SAMPLE_PLAN, RakipColumn.I));
      studySample
          .setSamplingWeight(getStringValue(sheet, RakipRow.STUDY_SAMPLE_WEIGHT, RakipColumn.I));
      studySample.setSamplingSize(getStringValue(sheet, RakipRow.STUDY_SAMPLE_SIZE, RakipColumn.I));
      studySample
          .setLotSizeUnit(getStringValue(sheet, RakipRow.STUDY_SAMPLE_SIZE_UNIT, RakipColumn.I));
      studySample
          .setSamplingPoint(getStringValue(sheet, RakipRow.STUDY_SAMPLE_POINT, RakipColumn.I));

      return studySample;
    }

    static DietaryAssessmentMethod retrieveDAM(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.DIETARY_ASSESSMENT_METHOD_TOOL.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing methodological tool");
      }
      if (sheet.getRow(RakipRow.DIETARY_ASSESSMENT_METHOD_1DAY.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing non consecutive one day");
      }

      DietaryAssessmentMethod dietaryAssessmentMethod =
          MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();

      dietaryAssessmentMethod.setCollectionTool(
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_TOOL, RakipColumn.I));

      int numberOfNonConsecutiveOneDay =
          getNumericValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_1DAY, RakipColumn.I).intValue();
      dietaryAssessmentMethod.setNumberOfNonConsecutiveOneDay(numberOfNonConsecutiveOneDay);

      String softwareTool =
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_SOFTWARE_TOOL, RakipColumn.I);
      dietaryAssessmentMethod.setSoftwareTool(softwareTool);

      String numberOfFoodItems =
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_ITEMS, RakipColumn.I);
      dietaryAssessmentMethod.setNumberOfFoodItems(numberOfFoodItems);

      String recordTypes =
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_RECORD_TYPE, RakipColumn.I);
      dietaryAssessmentMethod.setRecordTypes(recordTypes);

      String foodDescriptors =
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_DESCRIPTORS, RakipColumn.I);
      dietaryAssessmentMethod.setFoodDescriptors(foodDescriptors);

      return dietaryAssessmentMethod;
    }

    static Laboratory retrieveLaboratory(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.LABORATORY_ACCREDITATION.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing laboratory accreditation");
      }

      Laboratory laboratory = MetadataFactory.eINSTANCE.createLaboratory();
      laboratory.getLaboratoryAccreditation()
          .addAll(getStringObjectList(sheet, RakipRow.LABORATORY_ACCREDITATION, RakipColumn.I));
      laboratory.setLaboratoryName(getStringValue(sheet, RakipRow.LABORATORY_NAME, RakipColumn.I));
      laboratory
          .setLaboratoryCountry(getStringValue(sheet, RakipRow.LABORATORY_COUNTRY, RakipColumn.I));

      return laboratory;
    }

    static Assay retrieveAssay(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.ASSAY_NAME.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing assay name");
      }

      Assay assay = MetadataFactory.eINSTANCE.createAssay();
      assay.setAssayName(getStringValue(sheet, RakipRow.ASSAY_NAME, RakipColumn.I));
      assay.setAssayDescription(getStringValue(sheet, RakipRow.ASSAY_DESCRIPTION, RakipColumn.I));
      assay
          .setPercentageOfMoisture(getStringValue(sheet, RakipRow.ASSAY_MOIST_PERC, RakipColumn.I));
      assay.setPercentageOfFat(getStringValue(sheet, RakipRow.ASSAY_FAT_PERC, RakipColumn.I));
      assay.setLimitOfDetection(
          getStringValue(sheet, RakipRow.ASSAY_DETECTION_LIMIT, RakipColumn.I));
      assay.setLimitOfQuantification(
          getStringValue(sheet, RakipRow.ASSAY_QUANTIFICATION_LIMIT, RakipColumn.I));
      assay.setLeftCensoredData(
          getStringValue(sheet, RakipRow.ASSAY_LEFT_CENSORED_DATA, RakipColumn.I));
      assay.setRangeOfContamination(
          getStringValue(sheet, RakipRow.ASSAY_CONTAMINATION_RANGE, RakipColumn.I));
      assay.setUncertaintyValue(
          getStringValue(sheet, RakipRow.ASSAY_UNCERTAINTY_VALUE, RakipColumn.I));

      return assay;
    }

    static Parameter retrieveParameter(XSSFSheet sheet, int row) {

      // Check first mandatory properties
      if (sheet.getRow(row).getCell(RakipColumn.L.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter id");
      }
      if (sheet.getRow(row).getCell(RakipColumn.M.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter classification");
      }
      if (sheet.getRow(row).getCell(RakipColumn.N.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter name");
      }
      if (sheet.getRow(row).getCell(RakipColumn.Q.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter unit");
      }
      if (sheet.getRow(row).getCell(RakipColumn.S.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing data type");
      }

      Parameter param = MetadataFactory.eINSTANCE.createParameter();
      param.setParameterID(getStringValue(sheet, row, RakipColumn.L));

      String classificationText = getStringValue(sheet, row, RakipColumn.M).toLowerCase();
      if (classificationText.startsWith("input")) {
        param.setParameterClassification(ParameterClassification.INPUT);
      } else if (classificationText.startsWith("constant")) {
        param.setParameterClassification(ParameterClassification.CONSTANT);
      } else if (classificationText.startsWith("output")) {
        param.setParameterClassification(ParameterClassification.OUTPUT);
      }

      param.setParameterName(getStringValue(sheet, row, RakipColumn.N));
      param.setParameterDescription(getStringValue(sheet, row, RakipColumn.O));
      param.setParameterType(getStringValue(sheet, row, RakipColumn.P));
      param.setParameterUnit(getStringValue(sheet, row, RakipColumn.Q));
      param.setParameterUnitCategory(getStringValue(sheet, row, RakipColumn.R));

      try {
        String dataTypeAsString = getStringValue(sheet, row, RakipColumn.S);
        param.setParameterDataType(ParameterType.valueOf(dataTypeAsString));
      } catch (IllegalArgumentException ex) {
        param.setParameterDataType(ParameterType.OTHER);
      }

      param.setParameterSource(getStringValue(sheet, row, RakipColumn.T));
      param.setParameterSubject(getStringValue(sheet, row, RakipColumn.U));
      param.setParameterDistribution(getStringValue(sheet, row, RakipColumn.V));
      param.setParameterValue(getStringValue(sheet, row, RakipColumn.W));
      // reference
      param.setParameterVariabilitySubject(getStringValue(sheet, row, RakipColumn.Y));
      // applicability
      param.setParameterError(getStringValue(sheet, row, RakipColumn.AA));

      return param;
    }
  }

  /** Parses metadata-filled tables imported from a Google Drive spreadsheet. */
  static class TableParser {

    static List<String> getStringListValue(String[][] values, RakipRow row, RakipColumn col) {
      String rawString = values[row.num][col.ordinal()];
      List<String> tokens = Arrays.stream(rawString.split(",")).collect(Collectors.toList());
      return tokens;
    }

    static List<StringObject> getStringObjectList(String[][] values, RakipRow row,
        RakipColumn col) {

      String[] strings = values[row.num][col.ordinal()].split(",");

      List<StringObject> stringObjects = new ArrayList<>(strings.length);
      for (String value : strings) {
        StringObject so = MetadataFactory.eINSTANCE.createStringObject();
        so.setValue(value);
        stringObjects.add(so);
      }

      return stringObjects;
    }

    private static boolean isRowEmpty(DataRow row) {
      for (DataCell cell : row)
        if (!cell.isMissing())
          return false;
      return true;
    }

    static GeneralInformation retrieveGeneralInformation(String[][] values) {

      GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();

      String name = values[RakipRow.GENERAL_INFORMATION_NAME.num][RakipColumn.I.ordinal()];
      String source = values[RakipRow.GENERAL_INFORMATION_SOURCE.num][RakipColumn.I.ordinal()];
      String identifier =
          values[RakipRow.GENERAL_INFORMATION_IDENTIFIER.num][RakipColumn.I.ordinal()];
      String rights = values[RakipRow.GENERAL_INFORMATION_RIGHTS.num][RakipColumn.I.ordinal()];
      boolean isAvailable =
          values[RakipRow.GENERAL_INFORMATION_AVAILABILITY.num][RakipColumn.I.ordinal()]
              .equals("Yes");
      String language = values[RakipRow.GENERAL_INFORMATION_LANGUAGE.num][RakipColumn.I.ordinal()];
      String software = values[RakipRow.GENERAL_INFORMATION_SOFTWARE.num][RakipColumn.I.ordinal()];
      String languageWrittenIn =
          values[RakipRow.GENERAL_INFORMATION_LANGUAGE_WRITTEN_IN.num][RakipColumn.I.ordinal()];
      String status = values[RakipRow.GENERAL_INFORMATION_STATUS.num][RakipColumn.I.ordinal()];
      String objective =
          values[RakipRow.GENERAL_INFORMATION_OBJECTIVE.num][RakipColumn.I.ordinal()];
      String description =
          values[RakipRow.GENERAL_INFORMATION_DESCRIPTION.num][RakipColumn.I.ordinal()];

      generalInformation.setName(name);
      generalInformation.setSource(source);
      generalInformation.setIdentifier(identifier);
      generalInformation.setRights(rights);
      generalInformation.setAvailable(isAvailable);
      generalInformation.setLanguage(language);
      generalInformation.setSoftware(software);
      generalInformation.setLanguageWrittenIn(languageWrittenIn);
      generalInformation.setStatus(status);
      generalInformation.setObjective(objective);
      generalInformation.setDescription(description);

      for (int numRow = 3; numRow < 7; numRow++) {
        try {
          Contact contact = retrieveContact(values, numRow);
          generalInformation.getCreators().add(contact);
        } catch (Exception exception) {
        }
      }

      for (int numRow = 14; numRow < 17; numRow++) {
        try {
          Reference reference = retrieveReference(values, numRow);
          generalInformation.getReference().add(reference);
        } catch (Exception exception) {
        }
      }

      try {
        ModelCategory modelCategory = retrieveModelCategory(values);
        generalInformation.getModelCategory().add(modelCategory);
      } catch (Exception exception) {
      }

      return generalInformation;
    }

    static Contact retrieveContact(String[][] values, int row) {

      String email = values[row][RakipColumn.R.ordinal()];

      // Check mandatory properties and throw exception if missing
      if (email.isEmpty()) {
        throw new IllegalArgumentException("Missing mail");
      }

      Contact contact = MetadataFactory.eINSTANCE.createContact();
      contact.setTitle(values[row][RakipColumn.K.ordinal()]);
      contact.setFamilyName(values[row][RakipColumn.O.ordinal()]);
      contact.setGivenName(values[row][RakipColumn.M.ordinal()]);
      contact.setEmail(email);
      contact.setTelephone(values[row][RakipColumn.Q.ordinal()]);
      contact.setStreetAddress(values[row][RakipColumn.W.ordinal()]);
      contact.setCountry(values[row][RakipColumn.S.ordinal()]);
      contact.setCity(values[row][RakipColumn.T.ordinal()]);
      contact.setZipCode(values[row][RakipColumn.U.ordinal()]);
      contact.setRegion(values[row][RakipColumn.Y.ordinal()]);
      // time zone not included in spreadsheet ?
      // gender not included in spreadsheet ?
      // note not included in spreadsheet ?
      contact.setOrganization(values[row][RakipColumn.P.ordinal()]);

      return contact;
    }

    /**
     * @throw IllegalArgumentException if a mandatory property is missing
     */
    static Reference retrieveReference(String[][] values, int row) {

      final String isReferenceDescriptionString = values[row][RakipColumn.K.ordinal()];
      final String doi = values[row][RakipColumn.O.ordinal()];

      // Check mandatory properties and throw exception if missing
      if (isReferenceDescriptionString.isEmpty()) {
        throw new IllegalArgumentException("Missing Is reference description?");
      }
      if (doi.isEmpty()) {
        throw new IllegalArgumentException("Missing DOI");
      }

      Reference reference = MetadataFactory.eINSTANCE.createReference();

      // Is reference description
      reference.setIsReferenceDescription(isReferenceDescriptionString.equals("Yes"));

      // Publication type
      // Get publication type from literal. Get null if invalid literal.
      String publicationTypeLiteral = values[row][RakipColumn.L.ordinal()];
      PublicationType publicationType = PublicationType.get(publicationTypeLiteral);
      if (publicationType != null) {
        reference.setPublicationType(publicationType);
      }

      // PMID
      String pmid = values[row][RakipColumn.N.ordinal()];
      if (!pmid.isEmpty()) {
        reference.setPmid(pmid);
      }

      // DOI
      if (!doi.isEmpty()) {
        reference.setDoi(doi);
      }

      reference.setAuthorList(values[row][RakipColumn.P.ordinal()]);
      reference.setPublicationTitle(values[row][RakipColumn.Q.ordinal()]);
      reference.setPublicationAbstract(values[row][RakipColumn.R.ordinal()]);
      // TODO: journal
      // TODO: issue
      reference.setPublicationStatus(values[row][RakipColumn.T.ordinal()]);
      // TODO: web site
      reference.setPublicationWebsite(values[row][RakipColumn.U.ordinal()]);
      // TODO: comment

      return reference;
    }

    /**
     * @throw IllegalArgumentException if a mandatory property is missing
     */
    static ModelCategory retrieveModelCategory(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.MODEL_CATEGORY_CLASS.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing model class");
      }

      ModelCategory modelCategory = MetadataFactory.eINSTANCE.createModelCategory();
      modelCategory.setModelClass(values[RakipRow.MODEL_CATEGORY_CLASS.num][columnI]);
      modelCategory.getModelSubClass()
          .addAll(getStringObjectList(values, RakipRow.MODEL_CATEGORY_SUBCLASS, RakipColumn.I));
      modelCategory.setModelClassComment(values[RakipRow.MODEL_CATEGORY_COMMENT.num][columnI]);
      modelCategory.setBasicProcess(values[RakipRow.MODEL_CATEGORY_PROCESS.num][columnI]);

      return modelCategory;
    }

    static Scope retrieveScope(String[][] values) {

      Scope scope = MetadataFactory.eINSTANCE.createScope();
      try {
        Hazard hazard = MetadataFactory.eINSTANCE.createHazard();
        scope.getHazard().add(hazard);
      } catch (IllegalArgumentException exception) {
        // Ignore since hazard is optional
      }
      try {
        PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();
        scope.setPopulationGroup(populationGroup);
      } catch (IllegalArgumentException exception) {
        // Ignore since population group is optional
      }

      return scope;
    }

    /**
     * @throws IllegalArgumentException if a mandatory property is missing
     */
    static Hazard retrieveHazard(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // check mandatory properties
      if (values[RakipRow.HAZARD_TYPE.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing hazard");
      }

      Hazard hazard = MetadataFactory.eINSTANCE.createHazard();

      hazard.setHazardType(values[RakipRow.HAZARD_TYPE.num][columnI]);
      hazard.setHazardName(values[RakipRow.HAZARD_NAME.num][columnI]);
      hazard.setHazardDescription(values[RakipRow.HAZARD_DESCRIPTION.num][columnI]);
      hazard.setHazardUnit(values[RakipRow.HAZARD_UNIT.num][columnI]);
      hazard.setAdverseEffect(values[RakipRow.HAZARD_ADVERSE_EFFECT.num][columnI]);
      hazard.setSourceOfContamination(values[RakipRow.HAZARD_CONTAMINATION_SOURCE.num][columnI]);
      hazard.setBenchmarkDose(values[RakipRow.HAZARD_BMD.num][columnI]);
      hazard.setMaximumResidueLimit(values[RakipRow.HAZARD_MRL.num][columnI]);
      hazard.setNoObservedAdverseAffectLevel(values[RakipRow.HAZARD_NOAEL.num][columnI]);
      hazard.setLowestObservedAdverseAffectLevel(values[RakipRow.HAZARD_LOAEL.num][columnI]);
      hazard.setAcuteReferenceDose(values[RakipRow.HAZARD_ARFD.num][columnI]);
      hazard.setHazardIndSum(values[RakipRow.HAZARD_IND_SUM.num][columnI]);

      return hazard;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static PopulationGroup retrievePopulationGroup(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.POPULATION_GROUP_NAME.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing population name");
      }


      PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();
      populationGroup.setPopulationName(values[RakipRow.POPULATION_GROUP_NAME.num][columnI]);
      populationGroup.setTargetPopulation(values[RakipRow.POPULATION_GROUP_TARGET.num][columnI]);

      List<StringObject> populationSpan =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_SPAN, RakipColumn.I);
      populationGroup.getPopulationSpan().addAll(populationSpan);

      List<StringObject> populationDescription =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_DESCRIPTION, RakipColumn.I);
      populationGroup.getPopulationDescription().addAll(populationDescription);

      List<StringObject> populationAge =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_AGE, RakipColumn.I);
      populationGroup.getPopulationAge().addAll(populationAge);

      populationGroup.setPopulationGender(values[RakipRow.POPULATION_GROUP_GENDER.num][columnI]);

      List<StringObject> bmi =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_BMI, RakipColumn.I);
      populationGroup.getBmi().addAll(bmi);

      List<StringObject> specialDietGroups =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_DIET, RakipColumn.I);
      populationGroup.getSpecialDietGroups().addAll(specialDietGroups);

      List<StringObject> patternConsumption =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_PATTERN_CONSUMPTION, RakipColumn.I);
      populationGroup.getPatternConsumption().addAll(patternConsumption);

      List<StringObject> region =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_REGION, RakipColumn.I);
      populationGroup.getRegion().addAll(region);

      List<StringObject> country =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_COUNTRY, RakipColumn.I);
      populationGroup.getCountry().addAll(country);

      List<StringObject> populationRiskFactor =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_RISK, RakipColumn.I);
      populationGroup.getPopulationRiskFactor().addAll(populationRiskFactor);

      List<StringObject> season =
          getStringObjectList(values, RakipRow.POPULATION_GROUP_SEASON, RakipColumn.I);
      populationGroup.getSeason().addAll(season);

      return populationGroup;
    }

    static DataBackground retrieveDataBackground(String[][] values) {

      DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();

      dataBackground.setStudy(retrieveStudy(values));

      try {
        StudySample studySample = retrieveStudySample(values);
        dataBackground.getStudysample().add(studySample);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the study sample is optional
      }

      try {
        DietaryAssessmentMethod dietaryAssessmentMethod = retrieveDAM(values);
        dataBackground.setDietaryassessmentmethod(dietaryAssessmentMethod);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the dietary assessment method is optional
      }

      try {
        Laboratory laboratory = retrieveLaboratory(values);
        dataBackground.setLaboratory(laboratory);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the laboratory is optional
      }

      try {
        Assay assay = retrieveAssay(values);
        dataBackground.getAssay().add(assay);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the assay is optional
      }

      return dataBackground;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static Study retrieveStudy(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.STUDY_TITLE.num][RakipColumn.I.ordinal()].isEmpty()) {
        throw new IllegalArgumentException("Missing study title");
      }

      Study study = MetadataFactory.eINSTANCE.createStudy();
      study.setStudyIdentifier(values[RakipRow.STUDY_ID.num][columnI]);
      study.setStudyTitle(values[RakipRow.STUDY_TITLE.num][columnI]);
      study.setStudyDescription(values[RakipRow.STUDY_DESCRIPTION.num][columnI]);
      study.setStudyDesignType(values[RakipRow.STUDY_DESIGN_TYPE.num][columnI]);
      study.setStudyAssayMeasurementType(
          values[RakipRow.STUDY_ASSAY_MEASUREMENTS_TYPE.num][columnI]);
      study.setStudyAssayTechnologyType(values[RakipRow.STUDY_ASSAY_TECHNOLOGY_TYPE.num][columnI]);
      study.setStudyAssayTechnologyPlatform(
          values[RakipRow.STUDY_ASSAY_TECHNOLOGY_PLATFORM.num][columnI]);
      study.setAccreditationProcedureForTheAssayTechnology(
          values[RakipRow.STUDY_ACCREDITATION_PROCEDURE.num][columnI]);
      study.setStudyProtocolName(values[RakipRow.STUDY_PROTOCOL_COMPONENTS_NAME.num][columnI]);
      study.setStudyProtocolType(values[RakipRow.STUDY_PROTOCOL_TYPE.num][columnI]);
      study.setStudyProtocolDescription(values[RakipRow.STUDY_PROTOCOL_DESCRIPTION.num][columnI]);

      try {
        study.setStudyProtocolURI(new URI(values[RakipRow.STUDY_PROTOCOL_URI.num][columnI]));
      } catch (URISyntaxException exception) {
        // does nothing
      }

      study.setStudyProtocolVersion(values[RakipRow.STUDY_PROTOCOL_VERSION.num][columnI]);
      study.setStudyProtocolParametersName(
          values[RakipRow.STUDY_PROTOCOL_PARAMETERS_NAME.num][columnI]);
      study.setStudyProtocolComponentsName(
          values[RakipRow.STUDY_PROTOCOL_COMPONENTS_NAME.num][columnI]);
      study.setStudyProtocolComponentsType(
          values[RakipRow.STUDY_PROTOCOL_COMPONENTS_TYPE.num][columnI]);

      return study;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static StudySample retrieveStudySample(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.STUDY_SAMPLE_NAME.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing sample name");
      }
      if (values[RakipRow.STUDY_SAMPLE_PROTOCOL.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing sampling plan");
      }
      if (values[RakipRow.STUDY_SAMPLE_STRATEGY.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing sampling weight");
      }

      StudySample studySample = MetadataFactory.eINSTANCE.createStudySample();
      studySample.setSampleName(values[RakipRow.STUDY_SAMPLE_NAME.num][columnI]);
      studySample
          .setProtocolOfSampleCollection(values[RakipRow.STUDY_SAMPLE_PROTOCOL.num][columnI]);
      studySample.setSamplingStrategy(values[RakipRow.STUDY_SAMPLE_STRATEGY.num][columnI]);
      studySample.setTypeOfSamplingProgram(values[RakipRow.STUDY_SAMPLE_TYPE.num][columnI]);
      studySample.setSamplingMethod(values[RakipRow.STUDY_SAMPLE_METHOD.num][columnI]);
      studySample.setSamplingPlan(values[RakipRow.STUDY_SAMPLE_PLAN.num][columnI]);
      studySample.setSamplingWeight(values[RakipRow.STUDY_SAMPLE_WEIGHT.num][columnI]);
      studySample.setSamplingSize(values[RakipRow.STUDY_SAMPLE_SIZE.num][columnI]);
      studySample.setLotSizeUnit(values[RakipRow.STUDY_SAMPLE_SIZE.num][columnI]);
      studySample.setSamplingPoint(values[RakipRow.STUDY_SAMPLE_POINT.num][columnI]);

      return studySample;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static DietaryAssessmentMethod retrieveDAM(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.DIETARY_ASSESSMENT_METHOD_TOOL.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing methodological tool");
      }
      if (values[RakipRow.DIETARY_ASSESSMENT_METHOD_1DAY.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing non consecutive one day");
      }

      DietaryAssessmentMethod dietaryAssessmentMethod =
          MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();

      String collectionTool = values[RakipRow.DIETARY_ASSESSMENT_METHOD_TOOL.num][columnI];
      dietaryAssessmentMethod.setCollectionTool(collectionTool);

      int numberOfNonConsecutiveOneDay =
          Integer.parseInt(values[RakipRow.DIETARY_ASSESSMENT_METHOD_1DAY.num][columnI]);
      dietaryAssessmentMethod.setNumberOfNonConsecutiveOneDay(numberOfNonConsecutiveOneDay);

      String softwareTool = values[RakipRow.DIETARY_ASSESSMENT_METHOD_SOFTWARE_TOOL.num][columnI];
      dietaryAssessmentMethod.setSoftwareTool(softwareTool);

      String numberOfFoodItems = values[RakipRow.DIETARY_ASSESSMENT_METHOD_ITEMS.num][columnI];
      dietaryAssessmentMethod.setNumberOfFoodItems(numberOfFoodItems);

      String recordTypes = values[RakipRow.DIETARY_ASSESSMENT_METHOD_RECORD_TYPE.num][columnI];
      dietaryAssessmentMethod.setRecordTypes(recordTypes);

      String foodDescriptors = values[RakipRow.DIETARY_ASSESSMENT_METHOD_DESCRIPTORS.num][columnI];
      dietaryAssessmentMethod.setFoodDescriptors(foodDescriptors);

      return dietaryAssessmentMethod;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static Laboratory retrieveLaboratory(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.LABORATORY_ACCREDITATION.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing laboratory accreditation");
      }

      Laboratory laboratory = MetadataFactory.eINSTANCE.createLaboratory();
      laboratory.getLaboratoryAccreditation()
          .addAll(getStringObjectList(values, RakipRow.LABORATORY_ACCREDITATION, RakipColumn.I));
      laboratory.setLaboratoryName(values[RakipRow.LABORATORY_NAME.num][columnI]);
      laboratory.setLaboratoryCountry(values[RakipRow.LABORATORY_COUNTRY.num][columnI]);

      return laboratory;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static Assay retrieveAssay(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      Assay assay = MetadataFactory.eINSTANCE.createAssay();
      assay.setAssayName(values[RakipRow.ASSAY_NAME.num][columnI]);
      assay.setAssayDescription(values[RakipRow.ASSAY_DESCRIPTION.num][columnI]);
      assay.setPercentageOfMoisture(values[RakipRow.ASSAY_MOIST_PERC.num][columnI]);
      assay.setPercentageOfFat(values[RakipRow.ASSAY_FAT_PERC.num][columnI]);
      assay.setLimitOfDetection(values[RakipRow.ASSAY_DETECTION_LIMIT.num][columnI]);
      assay.setLimitOfQuantification(values[RakipRow.ASSAY_QUANTIFICATION_LIMIT.num][columnI]);
      assay.setLeftCensoredData(values[RakipRow.ASSAY_LEFT_CENSORED_DATA.num][columnI]);
      assay.setRangeOfContamination(values[RakipRow.ASSAY_CONTAMINATION_RANGE.num][columnI]);
      assay.setUncertaintyValue(values[RakipRow.ASSAY_UNCERTAINTY_VALUE.num][columnI]);

      return assay;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static Parameter retrieveParameter(String[][] values, int row) {

      // Check mandatory properties
      if (values[row][RakipColumn.L.ordinal()].isEmpty()) {
        throw new IllegalArgumentException("Missing parameter id");
      }

      if (values[row][RakipColumn.M.ordinal()].isEmpty()) {
        throw new IllegalArgumentException("Missing parameter classification");
      }

      if (values[row][RakipColumn.N.ordinal()].isEmpty()) {
        throw new IllegalArgumentException("Missing parameter name");
      }

      if (values[row][RakipColumn.Q.ordinal()].isEmpty()) {
        throw new IllegalArgumentException("Missing parameter unit");
      }

      if (values[row][RakipColumn.S.ordinal()].isEmpty()) {
        throw new IllegalArgumentException("Missing data type");
      }

      Parameter param = MetadataFactory.eINSTANCE.createParameter();
      param.setParameterID(values[row][RakipColumn.L.ordinal()]);

      String classificationText = values[row][RakipColumn.M.ordinal()].toLowerCase();
      if (classificationText.startsWith("input")) {
        param.setParameterClassification(ParameterClassification.INPUT);
      } else if (classificationText.startsWith("constant")) {
        param.setParameterClassification(ParameterClassification.CONSTANT);
      } else if (classificationText.startsWith("output")) {
        param.setParameterClassification(ParameterClassification.OUTPUT);
      }

      param.setParameterName(values[row][RakipColumn.N.ordinal()]);
      param.setParameterDescription(values[row][RakipColumn.O.ordinal()]);
      param.setParameterType(values[row][RakipColumn.P.ordinal()]);
      param.setParameterUnit(values[row][RakipColumn.Q.ordinal()]);
      param.setParameterUnitCategory(values[row][RakipColumn.R.ordinal()]);

      try {
        String dataTypeAsString = values[row][RakipColumn.S.ordinal()];
        param.setParameterDataType(ParameterType.valueOf(dataTypeAsString));
      } catch (IllegalArgumentException ex) {
        param.setParameterDataType(ParameterType.OTHER);
      }

      param.setParameterSource(values[row][RakipColumn.T.ordinal()]);
      param.setParameterSubject(values[row][RakipColumn.U.ordinal()]);
      param.setParameterDistribution(values[row][RakipColumn.V.ordinal()]);
      param.setParameterValue(values[row][RakipColumn.W.ordinal()]);
      // reference
      param.setParameterVariabilitySubject(values[row][RakipColumn.Y.ordinal()]);
      // model applicability
      param.setParameterError(values[row][RakipColumn.AA.ordinal()]);

      return param;
    }
  }
}
