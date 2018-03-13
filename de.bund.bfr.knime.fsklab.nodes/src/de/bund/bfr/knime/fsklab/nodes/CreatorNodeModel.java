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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
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
import com.gmail.gcolaianni5.jris.bean.Record;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.rakip.Assay;
import de.bund.bfr.knime.fsklab.rakip.DataBackground;
import de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod;
import de.bund.bfr.knime.fsklab.rakip.GeneralInformation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.Hazard;
import de.bund.bfr.knime.fsklab.rakip.Laboratory;
import de.bund.bfr.knime.fsklab.rakip.ModelCategory;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.PopulationGroup;
import de.bund.bfr.knime.fsklab.rakip.Scope;
import de.bund.bfr.knime.fsklab.rakip.Study;
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.StructuredName;

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
    final RScript modelRScript = readScript(nodeSettings.modelScript);
    final String modelScript = modelRScript.getScript();

    // Reads parameters script
    final String paramScript;
    FskSimulation defaultSimulation = null;
    if (StringUtils.isNotEmpty(nodeSettings.parameterScript)) {
      paramScript = readScript(nodeSettings.parameterScript).getScript();

      // Create defaultSimulation out of the parameters script
      defaultSimulation = NodeUtils.createDefaultSimulation(paramScript);
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

    final GenericModel genericModel;

    // If an input table is connected then parse the metadata
    if (inData.length == 1 && inData[0] != null) {
      BufferedDataTable metadataTable = (BufferedDataTable) inData[0];

      // parse table
      genericModel = TableParser.retrieveGenericModel(metadataTable);
    }

    else {
      // Reads model meta data
      if (StringUtils.isEmpty(nodeSettings.spreadsheet)) {
        throw new InvalidSettingsException("Model metadata is not provided");
      }

      final File metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.spreadsheet));
      try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
        final XSSFSheet sheet = workbook.getSheetAt(0);

        if (sheet.getPhysicalNumberOfRows() > 29) {
          // Process new RAKIP spreadsheet
          genericModel = new GenericModel();
          genericModel.generalInformation = RAKIPSheetImporter.retrieveGeneralInformation(sheet);
          genericModel.scope = RAKIPSheetImporter.retrieveScope(sheet);
          genericModel.dataBackground = RAKIPSheetImporter.retrieveDataBackground(sheet);
          for (int i = 132; i <= 152; i++) {
            try {
              Parameter param = RAKIPSheetImporter.retrieveParameter(sheet, i);
              genericModel.modelMath.parameter.add(param);
            } catch (Exception exception) {
              // ignore exception since it is thrown by empty rows
            }
          }
        } else {
          // Process legacy spreadsheet
          genericModel = new GenericModel();
          genericModel.generalInformation = LegacySheetImporter.getGeneralInformation(sheet);
          genericModel.generalInformation.software = "R";
          genericModel.scope = LegacySheetImporter.getScope(sheet);
          genericModel.modelMath = LegacySheetImporter.getModelMath(sheet);

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

    final FskPortObject portObj = new FskPortObject(modelScript, paramScript, visualizationScript,
        genericModel, null, new HashSet<>(), workingDirectory);
    if (defaultSimulation != null) {
      portObj.simulations.add(defaultSimulation);
    }

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

  private static class LegacySheetImporter {

    static String getString(final XSSFSheet sheet, final int rowNumber) {
      final XSSFRow row = sheet.getRow(rowNumber);
      if (row == null)
        throw new IllegalArgumentException("Missing row: #" + rowNumber);
      return row.getCell(5).getStringCellValue();
    }

    static GeneralInformation getGeneralInformation(final XSSFSheet sheet)
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

    static Scope getScope(final XSSFSheet sheet) throws InvalidSettingsException {

      final Scope scope = new Scope();

      scope.hazard.hazardName = getString(sheet, 3);
      scope.hazard.hazardDescription = getString(sheet, 4);

      scope.product.environmentName = getString(sheet, 5);
      scope.product.environmentDescription = getString(sheet, 6);

      return scope;
    }

    static ModelMath getModelMath(final XSSFSheet sheet) throws InvalidSettingsException {

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

    static List<String> getStringListValue(XSSFSheet sheet, RakipRow row, RakipColumn col) {
      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      return Arrays.stream(cell.getStringCellValue().split(",")).collect(Collectors.toList());
    }

    static GeneralInformation retrieveGeneralInformation(XSSFSheet sheet) {

      GeneralInformation gi = new GeneralInformation();
      gi.name = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_NAME, RakipColumn.I);
      gi.source = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_SOURCE, RakipColumn.I);
      gi.identifier = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_IDENTIFIER, RakipColumn.I);
      gi.rights = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_RIGHTS, RakipColumn.I);
      gi.isAvailable =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_AVAILABILITY, RakipColumn.I) == "Yes";
      gi.language = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_LANGUAGE, RakipColumn.I);
      gi.software = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_SOFTWARE, RakipColumn.I);
      gi.languageWrittenIn =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_LANGUAGE_WRITTEN_IN, RakipColumn.I);
      gi.status = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_STATUS, RakipColumn.I);
      gi.objective = getStringValue(sheet, RakipRow.GENERAL_INFORMATION_OBJECTIVE, RakipColumn.I);
      gi.description =
          getStringValue(sheet, RakipRow.GENERAL_INFORMATION_DESCRIPTION, RakipColumn.I);

      // retrieve creators
      for (int numRow = 3; numRow < 7; numRow++) {
        try {
          VCard vCard = retrieveCreator(sheet, numRow);
          gi.creators.add(vCard);
        } catch (Exception exception) {
        }
      }

      // retrieve references
      for (int numRow = 14; numRow < 17; numRow++) {
        try {
          Record record = retrieveReference(sheet, numRow);
          gi.reference.add(record);
        } catch (Exception exception) {
        }
      }

      try {
        gi.modelCategory = retrieveModelCategory(sheet);
      } catch (Exception exception) {
      }

      return gi;
    }

    /**
     * @throw IllegalArgumentException if mail is empty
     */
    static VCard retrieveCreator(XSSFSheet sheet, int row) {


      /*
       * Get values from spreadsheet
       * 
       * Note: name (column L) is redundant and not used. The structured name already contains given
       * family name, given name and additional names.
       */
      String honorific = getStringValue(sheet, row, RakipColumn.K); // honorific prefix
      String givenName = getStringValue(sheet, row, RakipColumn.M);
      String additionalName = getStringValue(sheet, row, RakipColumn.N);
      String familyName = getStringValue(sheet, row, RakipColumn.O);
      String organization = getStringValue(sheet, row, RakipColumn.P);
      String telephone = getStringValue(sheet, row, RakipColumn.Q);
      String email = getStringValue(sheet, row, RakipColumn.R);
      String country = getStringValue(sheet, row, RakipColumn.S);
      String city = getStringValue(sheet, row, RakipColumn.T);
      String zipCode = getStringValue(sheet, row, RakipColumn.U);
      String postOfficeBox = getStringValue(sheet, row, RakipColumn.V);
      String streetAddress = getStringValue(sheet, row, RakipColumn.W);
      String extendedAddress = getStringValue(sheet, row, RakipColumn.X);
      String region = getStringValue(sheet, row, RakipColumn.Y);

      // Check mandatory properties and throw exception if missing
      if (email.isEmpty()) {
        throw new IllegalArgumentException("Missing mail");
      }

      VCard vCard = new VCard();

      // StructuredName <- family, given and additional names with prefixes
      {
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(familyName);
        structuredName.setGiven(givenName);
        structuredName.getAdditionalNames().add(additionalName);
        structuredName.getPrefixes().add(honorific);
        vCard.setStructuredName(structuredName);
      }

      // organization is optional. Ignore empty cell.
      if (!organization.isEmpty()) {
        vCard.setOrganization(organization);
      }

      // telephone is optional. Ignore empty cell.
      if (!telephone.isEmpty()) {
        vCard.addTelephoneNumber(telephone, TelephoneType.VOICE);
      }

      vCard.addEmail(new Email(email));

      // Address <- country, city and postal code
      {
        Address address = new Address();
        address.setCountry(country);
        address.setLocality(city);
        address.setPostalCode(zipCode);
        address.setPoBox(postOfficeBox);
        address.setStreetAddress(streetAddress);
        address.setExtendedAddress(extendedAddress);
        address.setRegion(region);

        vCard.addAddress(address);
      }

      return vCard;
    }

    /**
     * Import reference from Excel row.
     *
     * @throws IllegalArgumentException if isReferenceDescription or DOI are missing
     *
     *         - Is_reference_description? in the K column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING]. Mandatory. Takes "Yes" or "No".
     *         Other strings are discarded.
     *
     *         - Publication type in the L column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING]. Optional. Takes the full name of
     *         a RIS reference type.
     *
     *         - Date in the M column. Type [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING].
     *         Optional. Format `YYYY/MM/DD/other info` where the fields are optional. Examples:
     *         `2017/11/16/noon`, `2017/11/16`, `2017/11`, `2017`.
     *
     *         - PubMed Id in the N column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC]. Optional. Unique unsigned
     *         integer. Example: 20069275
     *
     *         - DOI in the O column. Type [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING].
     *         Mandatory. Example: 10.1056/NEJM199710303371801.
     *
     *         - Publication author list in the P column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING]. Optional. The authors are
     *         defined with last name, name and joined with semicolons. Example:
     *         `Ungaretti-Haberbeck,L;Plaza-Rodríguez,C;Desvignes,V`
     *
     *         - Publication title in the Q column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING]. Optional.
     *
     *         - Publication abstract in the R column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING]. Optional.
     *
     *         - Publication journal/vol/issue in the S column. Type
     *         [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING]. Optional.
     *
     *         - Publication status. // TODO: publication status
     *
     *         - Publication website. Type [org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING].
     *         Optional. Invalid urls are discarded.
     */
    static Record retrieveReference(XSSFSheet sheet, int row) {

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

      String isReferenceDescription = getStringValue(sheet, row, RakipColumn.K);
      String type = getStringValue(sheet, row, RakipColumn.L);
      Double pmid = getNumericValue(sheet, row, RakipColumn.N);
      String doi = getStringValue(sheet, row, RakipColumn.O);
      String author = getStringValue(sheet, row, RakipColumn.P);
      String title = getStringValue(sheet, row, RakipColumn.Q);
      String abstractText = getStringValue(sheet, row, RakipColumn.R);
      String status = getStringValue(sheet, row, RakipColumn.T);
      String website = getStringValue(sheet, row, RakipColumn.U);

      // Check mandatory properties and throw exception if missing
      if (isReferenceDescription.isEmpty()) {
        throw new IllegalArgumentException("Missing Is reference description?");
      }
      if (doi.isEmpty()) {
        throw new IllegalArgumentException("Missing DOI");
      }

      Record record = new Record();

      // Save isReferenceDescription in U1 tag (user definable #1)
      record.setUserDefinable1(isReferenceDescription);

      // Look for RIS type (abbreviation) through the string value
      ResourceBundle risBundle = ResourceBundle.getBundle("ris_types");
      for (String abbreviation : risBundle.keySet()) {
        if (abbreviation.equals(type)) {
          com.gmail.gcolaianni5.jris.bean.Type risType =
              com.gmail.gcolaianni5.jris.bean.Type.valueOf(abbreviation);
          record.setType(risType);
          break;
        }
      }

      // Save PMID in U2 tag (user definable #2)
      record.setUserDefinable2(pmid.toString());

      record.setDoi(doi);

      // Save author list
      Arrays.stream(author.split(";")).forEach(record::addAuthor);

      record.setTitle(title); // Save title in TI tag
      record.setAbstr(abstractText); // Save abstract in AB tag

      // Save status in U3 tag (user definable #3)
      record.setUserDefinable3(status);

      record.setUrl(website);

      return record;
    }

    /**
     * Import [ModelCategory] from Excel sheet.
     *
     * - Model class from H27. Mandatory. - Model sub class from H28. Optional. - Model class
     * comment from H29. Optional. - Basic process from H32. Optional.
     *
     * @throws IllegalArgumentException if modelClass is missing.
     */
    static ModelCategory retrieveModelCategory(XSSFSheet sheet) {

      // Check mandatory properties and throw exception if missing
      if (sheet.getRow(RakipRow.MODEL_CATEGORY_CLASS.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing model class");
      }

      ModelCategory modelCategory = new ModelCategory();
      modelCategory.modelClass =
          getStringValue(sheet, RakipRow.MODEL_CATEGORY_CLASS, RakipColumn.I);
      modelCategory.modelSubClass
          .addAll(getStringListValue(sheet, RakipRow.MODEL_CATEGORY_SUBCLASS, RakipColumn.I));
      modelCategory.modelClassComment =
          getStringValue(sheet, RakipRow.MODEL_CATEGORY_COMMENT, RakipColumn.I);
      modelCategory.basicProcess
          .addAll(getStringListValue(sheet, RakipRow.MODEL_CATEGORY_PROCESS, RakipColumn.I));

      return modelCategory;
    }

    static Scope retrieveScope(XSSFSheet sheet) {

      Scope scope = new Scope();

      try {
        scope.hazard = retrieveHazard(sheet);
      } catch (IllegalArgumentException exception) {
        // ignore exception since the hazard is optional
      }
      try {
        scope.populationGroup = retrievePopulationGroup(sheet);
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

      Hazard hazard = new Hazard();
      hazard.hazardType = getStringValue(sheet, RakipRow.HAZARD_TYPE, RakipColumn.I);
      hazard.hazardName = getStringValue(sheet, RakipRow.HAZARD_NAME, RakipColumn.I);
      hazard.hazardDescription = getStringValue(sheet, RakipRow.HAZARD_DESCRIPTION, RakipColumn.I);
      hazard.hazardUnit = getStringValue(sheet, RakipRow.HAZARD_UNIT, RakipColumn.I);
      hazard.adverseEffect = getStringValue(sheet, RakipRow.HAZARD_ADVERSE_EFFECT, RakipColumn.I);
      hazard.sourceOfContamination =
          getStringValue(sheet, RakipRow.HAZARD_CONTAMINATION_SOURCE, RakipColumn.I);
      hazard.bmd = getStringValue(sheet, RakipRow.HAZARD_BMD, RakipColumn.I);
      hazard.mrl = getStringValue(sheet, RakipRow.HAZARD_MRL, RakipColumn.I);
      hazard.noael = getStringValue(sheet, RakipRow.HAZARD_NOAEL, RakipColumn.I);
      hazard.loael = getStringValue(sheet, RakipRow.HAZARD_LOAEL, RakipColumn.I);
      hazard.aoel = getStringValue(sheet, RakipRow.HAZARD_AOEL, RakipColumn.I);
      hazard.ard = getStringValue(sheet, RakipRow.HAZARD_ARFD, RakipColumn.I);
      hazard.hazardIndSum = getStringValue(sheet, RakipRow.HAZARD_IND_SUM, RakipColumn.I);

      return hazard;
    }

    static PopulationGroup retrievePopulationGroup(XSSFSheet sheet) {

      // Check mandatory properties
      if (sheet.getRow(RakipRow.POPULATION_GROUP_NAME.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing population name");
      }

      PopulationGroup pg = new PopulationGroup();
      pg.populationName = getStringValue(sheet, RakipRow.POPULATION_GROUP_NAME, RakipColumn.I);
      pg.targetPopulation = getStringValue(sheet, RakipRow.POPULATION_GROUP_TARGET, RakipColumn.I);
      pg.populationSpan
          .addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_SPAN, RakipColumn.I));
      pg.populationDescription
          .addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_DESCRIPTION, RakipColumn.I));
      pg.populationAge
          .addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_AGE, RakipColumn.I));
      pg.populationGender = getStringValue(sheet, RakipRow.POPULATION_GROUP_GENDER, RakipColumn.I);
      pg.bmi.addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_BMI, RakipColumn.I));
      pg.specialDietGroups
          .addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_DIET, RakipColumn.I));
      pg.patternConsumption.addAll(
          getStringListValue(sheet, RakipRow.POPULATION_GROUP_PATTERN_CONSUMPTION, RakipColumn.I));
      pg.region.addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_REGION, RakipColumn.I));
      pg.country
          .addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_COUNTRY, RakipColumn.I));
      pg.populationRiskFactor
          .addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_RISK, RakipColumn.I));
      pg.season.addAll(getStringListValue(sheet, RakipRow.POPULATION_GROUP_SEASON, RakipColumn.I));

      return pg;
    }

    static DataBackground retrieveDataBackground(XSSFSheet sheet) {
      DataBackground dataBackground = new DataBackground();
      dataBackground.study = retrieveStudy(sheet);

      try {
        dataBackground.studySample = retrieveStudySample(sheet);
      } catch (Exception exception) {
        // ignore errors since StudySample is optional
      }

      try {
        dataBackground.dietaryAssessmentMethod = retrieveDAM(sheet);
      } catch (Exception exception) {
        // ignore errors since DietaryAssessmentMethod is optional
      }

      try {
        dataBackground.laboratory = retrieveLaboratory(sheet);
      } catch (Exception exception) {
        // ignore errors since Laboratory is optional
      }

      try {
        dataBackground.assay = retrieveAssay(sheet);
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

      Study study = new Study();
      study.id = getStringValue(sheet, RakipRow.STUDY_ID, RakipColumn.I);
      study.title = getStringValue(sheet, RakipRow.STUDY_TITLE, RakipColumn.I);
      study.description = getStringValue(sheet, RakipRow.STUDY_DESCRIPTION, RakipColumn.I);
      study.designType = getStringValue(sheet, RakipRow.STUDY_DESIGN_TYPE, RakipColumn.I);
      study.measurementType =
          getStringValue(sheet, RakipRow.STUDY_ASSAY_MEASUREMENTS_TYPE, RakipColumn.I);
      study.technologyType =
          getStringValue(sheet, RakipRow.STUDY_ASSAY_TECHNOLOGY_TYPE, RakipColumn.I);
      study.technologyPlatform =
          getStringValue(sheet, RakipRow.STUDY_ASSAY_TECHNOLOGY_PLATFORM, RakipColumn.I);
      study.accreditationProcedure =
          getStringValue(sheet, RakipRow.STUDY_ACCREDITATION_PROCEDURE, RakipColumn.I);
      study.protocolName = getStringValue(sheet, RakipRow.STUDY_PROTOCOL_NAME, RakipColumn.I);
      study.protocolType = getStringValue(sheet, RakipRow.STUDY_PROTOCOL_TYPE, RakipColumn.I);
      study.protocolDescription =
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_DESCRIPTION, RakipColumn.I);
      try {
        study.protocolUri =
            new URI(getStringValue(sheet, RakipRow.STUDY_PROTOCOL_URI, RakipColumn.I));
      } catch (URISyntaxException e) {
      }
      study.protocolVersion = getStringValue(sheet, RakipRow.STUDY_PROTOCOL_VERSION, RakipColumn.I);
      study.parametersName =
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_PARAMETERS_NAME, RakipColumn.I);
      study.componentsName =
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_COMPONENTS_NAME, RakipColumn.I);
      study.componentsType =
          getStringValue(sheet, RakipRow.STUDY_PROTOCOL_COMPONENTS_TYPE, RakipColumn.I);

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

      StudySample studySample = new StudySample();
      studySample.sample = getStringValue(sheet, RakipRow.STUDY_SAMPLE_NAME, RakipColumn.I);
      studySample.collectionProtocol =
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_PROTOCOL, RakipColumn.I);
      studySample.samplingStrategy =
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_STRATEGY, RakipColumn.I);
      studySample.samplingProgramType =
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_TYPE, RakipColumn.I);
      studySample.samplingMethod =
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_METHOD, RakipColumn.I);
      studySample.samplingPlan = getStringValue(sheet, RakipRow.STUDY_SAMPLE_PLAN, RakipColumn.I);
      studySample.samplingWeight =
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_WEIGHT, RakipColumn.I);
      studySample.samplingSize = getStringValue(sheet, RakipRow.STUDY_SAMPLE_SIZE, RakipColumn.I);
      studySample.lotSizeUnit =
          getStringValue(sheet, RakipRow.STUDY_SAMPLE_SIZE_UNIT, RakipColumn.I);
      studySample.samplingPoint = getStringValue(sheet, RakipRow.STUDY_SAMPLE_POINT, RakipColumn.I);


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

      DietaryAssessmentMethod dam = new DietaryAssessmentMethod();
      dam.collectionTool =
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_TOOL, RakipColumn.I);
      dam.numberOfNonConsecutiveOneDay =
          getNumericValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_1DAY, RakipColumn.I).intValue();
      dam.softwareTool =
          getStringValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_SOFTWARE_TOOL, RakipColumn.I);
      dam.numberOfFoodItems.addAll(
          getStringListValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_ITEMS, RakipColumn.I));
      dam.recordTypes.addAll(
          getStringListValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_RECORD_TYPE, RakipColumn.I));
      dam.foodDescriptors.addAll(
          getStringListValue(sheet, RakipRow.DIETARY_ASSESSMENT_METHOD_DESCRIPTORS, RakipColumn.I));

      return dam;
    }

    static Laboratory retrieveLaboratory(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.LABORATORY_ACCREDITATION.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing laboratory accreditation");
      }

      Laboratory laboratory = new Laboratory();
      laboratory.accreditation =
          getStringValue(sheet, RakipRow.LABORATORY_ACCREDITATION, RakipColumn.I);
      laboratory.name = getStringValue(sheet, RakipRow.LABORATORY_NAME, RakipColumn.I);
      laboratory.country = getStringValue(sheet, RakipRow.LABORATORY_COUNTRY, RakipColumn.I);

      return laboratory;
    }

    static Assay retrieveAssay(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(RakipRow.ASSAY_NAME.num).getCell(RakipColumn.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing assay name");
      }

      Assay assay = new Assay();
      assay.name = getStringValue(sheet, RakipRow.ASSAY_NAME, RakipColumn.I);
      assay.description = getStringValue(sheet, RakipRow.ASSAY_DESCRIPTION, RakipColumn.I);
      assay.moisturePercentage = getStringValue(sheet, RakipRow.ASSAY_MOIST_PERC, RakipColumn.I);
      assay.fatPercentage = getStringValue(sheet, RakipRow.ASSAY_FAT_PERC, RakipColumn.I);
      assay.detectionLimit = getStringValue(sheet, RakipRow.ASSAY_DETECTION_LIMIT, RakipColumn.I);
      assay.quantificationLimit =
          getStringValue(sheet, RakipRow.ASSAY_QUANTIFICATION_LIMIT, RakipColumn.I);
      assay.leftCensoredData =
          getStringValue(sheet, RakipRow.ASSAY_LEFT_CENSORED_DATA, RakipColumn.I);
      assay.contaminationRange =
          getStringValue(sheet, RakipRow.ASSAY_CONTAMINATION_RANGE, RakipColumn.I);
      assay.uncertaintyValue =
          getStringValue(sheet, RakipRow.ASSAY_UNCERTAINTY_VALUE, RakipColumn.I);

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

      Parameter param = new Parameter();
      param.id = getStringValue(sheet, row, RakipColumn.L);

      String classificationText = getStringValue(sheet, row, RakipColumn.M).toLowerCase();
      if (classificationText.startsWith("input")) {
        param.classification = Parameter.Classification.input;
      } else if (classificationText.startsWith("constant")) {
        param.classification = Parameter.Classification.constant;
      } else if (classificationText.startsWith("output")) {
        param.classification = Parameter.Classification.output;
      }

      param.name = getStringValue(sheet, row, RakipColumn.N);
      param.description = getStringValue(sheet, row, RakipColumn.O);
      param.type = getStringValue(sheet, row, RakipColumn.P);
      param.unit = getStringValue(sheet, row, RakipColumn.Q);
      param.unitCategory = getStringValue(sheet, row, RakipColumn.R);
      param.dataType = getStringValue(sheet, row, RakipColumn.S);
      param.source = getStringValue(sheet, row, RakipColumn.T);
      param.subject = getStringValue(sheet, row, RakipColumn.U);
      param.distribution = getStringValue(sheet, row, RakipColumn.V);
      param.value = getStringValue(sheet, row, RakipColumn.W);
      param.reference = getStringValue(sheet, row, RakipColumn.X);
      param.variabilitySubject = getStringValue(sheet, row, RakipColumn.Y);
      param.modelApplicability.add(getStringValue(sheet, row, RakipColumn.Z));
      param.error = getNumericValue(sheet, row, RakipColumn.AA);

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

    private static boolean isRowEmpty(DataRow row) {
      for (DataCell cell : row)
        if (!cell.isMissing())
          return false;
      return true;
    }

    static GenericModel retrieveGenericModel(BufferedDataTable table) {

      GenericModel model = new GenericModel();

      String[][] values = new String[200][30];
      int i = 0;
      for (DataRow row : table) {
        if (isRowEmpty(row))
          break;
        int j = 0;
        for (DataCell cell : row) {
          values[i][j] = !cell.isMissing() ? ((StringCell) cell).getStringValue() : "";
          j++;
        }
        i++;
      }

      model.generalInformation = retrieveGeneralInformation(values);
      model.scope = retrieveScope(values);
      model.dataBackground = retrieveDataBackground(values);

      for (i = 132; i <= 152; i++) {
        try {
          Parameter param = TableParser.retrieveParameter(values, i);
          model.modelMath.parameter.add(param);
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }

      return model;
    }

    static GeneralInformation retrieveGeneralInformation(String[][] values) {
      GeneralInformation gi = new GeneralInformation();

      gi.name = values[RakipRow.GENERAL_INFORMATION_NAME.num][RakipColumn.I.ordinal()];
      gi.source = values[RakipRow.GENERAL_INFORMATION_SOURCE.num][RakipColumn.I.ordinal()];
      gi.identifier = values[RakipRow.GENERAL_INFORMATION_IDENTIFIER.num][RakipColumn.I.ordinal()];
      gi.rights = values[RakipRow.GENERAL_INFORMATION_RIGHTS.num][RakipColumn.I.ordinal()];
      gi.isAvailable =
          values[RakipRow.GENERAL_INFORMATION_AVAILABILITY.num][RakipColumn.I.ordinal()]
              .equals("Yes");
      gi.language = values[RakipRow.GENERAL_INFORMATION_LANGUAGE.num][RakipColumn.I.ordinal()];
      gi.software = values[RakipRow.GENERAL_INFORMATION_SOFTWARE.num][RakipColumn.I.ordinal()];
      gi.languageWrittenIn =
          values[RakipRow.GENERAL_INFORMATION_LANGUAGE_WRITTEN_IN.num][RakipColumn.I.ordinal()];
      gi.status = values[RakipRow.GENERAL_INFORMATION_STATUS.num][RakipColumn.I.ordinal()];
      gi.objective = values[RakipRow.GENERAL_INFORMATION_OBJECTIVE.num][RakipColumn.I.ordinal()];
      gi.description =
          values[RakipRow.GENERAL_INFORMATION_DESCRIPTION.num][RakipColumn.I.ordinal()];

      for (int numRow = 3; numRow < 7; numRow++) {
        try {
          VCard vCard = retrieveCreator(values, numRow);
          gi.creators.add(vCard);
        } catch (Exception exception) {
        }
      }

      for (int numRow = 14; numRow < 17; numRow++) {
        try {
          Record record = retrieveReference(values, numRow);
          gi.reference.add(record);
        } catch (Exception exception) {
        }
      }

      try {
        gi.modelCategory = retrieveModelCategory(values);
      } catch (Exception exception) {
      }

      return gi;
    }

    /**
     * @throw IllegalArgumentException if mail is missing
     */
    static VCard retrieveCreator(String[][] values, int row) {

      String honorific = values[row][RakipColumn.K.ordinal()]; // honorific prefix
      String givenName = values[row][RakipColumn.M.ordinal()];
      String additionalName = values[row][RakipColumn.N.ordinal()];
      String familyName = values[row][RakipColumn.O.ordinal()];
      String organization = values[row][RakipColumn.P.ordinal()];
      String telephone = values[row][RakipColumn.Q.ordinal()];
      String email = values[row][RakipColumn.R.ordinal()];
      String country = values[row][RakipColumn.S.ordinal()];
      String city = values[row][RakipColumn.T.ordinal()];
      String zipCode = values[row][RakipColumn.U.ordinal()];
      String postOfficeBox = values[row][RakipColumn.V.ordinal()];
      String streetAddress = values[row][RakipColumn.W.ordinal()];
      String extendedAddress = values[row][RakipColumn.X.ordinal()];
      String region = values[row][RakipColumn.Y.ordinal()];

      // Check mandatory properties and throw exception if missing
      if (email.isEmpty()) {
        throw new IllegalArgumentException("Missing mail");
      }

      VCard vCard = new VCard();

      // StructuredName <- family, given and additional names with prefixes
      {
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(familyName);
        structuredName.setGiven(givenName);
        structuredName.getAdditionalNames().add(additionalName);
        structuredName.getPrefixes().add(honorific);
        vCard.setStructuredName(structuredName);
      }

      // organization is optional. Ignore empty cell.
      if (!organization.isEmpty()) {
        vCard.setOrganization(organization);
      }

      // telephone is optional. Ignore empty cell.
      if (!telephone.isEmpty()) {
        vCard.addTelephoneNumber(telephone, TelephoneType.VOICE);
      }

      vCard.addEmail(new Email(email));

      // Address <- country, city and postal code
      {
        Address address = new Address();
        address.setCountry(country);
        address.setLocality(city);
        address.setPostalCode(zipCode);
        address.setPoBox(postOfficeBox);
        address.setStreetAddress(streetAddress);
        address.setExtendedAddress(extendedAddress);
        address.setRegion(region);

        vCard.addAddress(address);
      }

      return vCard;
    }

    /**
     * @throw IllegalArgumentException if a mandatory property is missing
     */
    static Record retrieveReference(String[][] values, int row) {

      String isReferenceDescription = values[row][RakipColumn.K.ordinal()];
      String type = values[row][RakipColumn.L.ordinal()];
      String pmid = values[row][RakipColumn.N.ordinal()];
      String doi = values[row][RakipColumn.O.ordinal()];
      String author = values[row][RakipColumn.P.ordinal()];
      String title = values[row][RakipColumn.Q.ordinal()];
      String abstractText = values[row][RakipColumn.R.ordinal()];
      String status = values[row][RakipColumn.T.ordinal()];
      String website = values[row][RakipColumn.U.ordinal()];

      // Check mandatory properties and throw exception if missing
      if (isReferenceDescription.isEmpty()) {
        throw new IllegalArgumentException("Missing Is reference description?");
      }
      if (doi.isEmpty()) {
        throw new IllegalArgumentException("Missing DOI");
      }

      Record record = new Record();

      // Save isReferenceDescription in U1 tag (user definable #1)
      record.setUserDefinable1(isReferenceDescription);

      // Look for RIS type (abbreviation) through the string value
      ResourceBundle risBundle = ResourceBundle.getBundle("ris_types");
      for (String abbreviation : risBundle.keySet()) {
        if (abbreviation.equals(type)) {
          com.gmail.gcolaianni5.jris.bean.Type risType =
              com.gmail.gcolaianni5.jris.bean.Type.valueOf(abbreviation);
          record.setType(risType);
          break;
        }
      }

      // Save PMID in U2 tag (user definable #2)
      record.setUserDefinable2(pmid);

      record.setDoi(doi);

      // Save author list
      Arrays.stream(author.split(";")).forEach(record::addAuthor);

      record.setTitle(title); // Save title in TI tag
      record.setAbstr(abstractText); // Save abstract in AB tag

      // Save status in U3 tag (user definable #3)
      record.setUserDefinable3(status);

      record.setUrl(website);

      return record;
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

      ModelCategory modelCategory = new ModelCategory();
      modelCategory.modelClass = values[RakipRow.MODEL_CATEGORY_CLASS.num][columnI];
      modelCategory.modelSubClass
          .addAll(getStringListValue(values, RakipRow.MODEL_CATEGORY_SUBCLASS, RakipColumn.I));
      modelCategory.modelClassComment = values[RakipRow.MODEL_CATEGORY_COMMENT.num][columnI];
      modelCategory.basicProcess
          .addAll(getStringListValue(values, RakipRow.MODEL_CATEGORY_PROCESS, RakipColumn.I));

      return modelCategory;
    }

    static Scope retrieveScope(String[][] values) {
      Scope scope = new Scope();
      try {
        scope.hazard = retrieveHazard(values);
      } catch (IllegalArgumentException exception) {
        // Ignore since hazard is optional
      }
      try {
        scope.populationGroup = retrievePopulationGroup(values);
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

      Hazard hazard = new Hazard();
      hazard.hazardType = values[RakipRow.HAZARD_TYPE.num][columnI];
      hazard.hazardName = values[RakipRow.HAZARD_NAME.num][columnI];
      hazard.hazardDescription = values[RakipRow.HAZARD_DESCRIPTION.num][columnI];
      hazard.hazardUnit = values[RakipRow.HAZARD_UNIT.num][columnI];
      hazard.adverseEffect = values[RakipRow.HAZARD_ADVERSE_EFFECT.num][columnI];
      hazard.sourceOfContamination = values[RakipRow.HAZARD_CONTAMINATION_SOURCE.num][columnI];
      hazard.bmd = values[RakipRow.HAZARD_BMD.num][columnI];
      hazard.mrl = values[RakipRow.HAZARD_MRL.num][columnI];
      hazard.noael = values[RakipRow.HAZARD_NOAEL.num][columnI];
      hazard.loael = values[RakipRow.HAZARD_LOAEL.num][columnI];
      hazard.ard = values[RakipRow.HAZARD_ARFD.num][columnI];
      hazard.hazardIndSum = values[RakipRow.HAZARD_IND_SUM.num][columnI];

      return hazard;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static PopulationGroup retrievePopulationGroup(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.POPULATION_GROUP_NAME.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing population name");
      }

      PopulationGroup populationGroup = new PopulationGroup();
      populationGroup.populationName = values[RakipRow.POPULATION_GROUP_NAME.num][columnI];
      populationGroup.targetPopulation = values[RakipRow.POPULATION_GROUP_TARGET.num][columnI];
      populationGroup.populationSpan
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_SPAN, RakipColumn.I));
      populationGroup.populationDescription
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_DESCRIPTION, RakipColumn.I));
      populationGroup.populationAge
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_AGE, RakipColumn.I));
      populationGroup.populationGender = values[RakipRow.POPULATION_GROUP_GENDER.num][columnI];
      populationGroup.bmi
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_BMI, RakipColumn.I));
      populationGroup.specialDietGroups
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_DIET, RakipColumn.I));
      populationGroup.patternConsumption.addAll(
          getStringListValue(values, RakipRow.POPULATION_GROUP_PATTERN_CONSUMPTION, RakipColumn.I));
      populationGroup.region
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_REGION, RakipColumn.I));
      populationGroup.country
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_COUNTRY, RakipColumn.I));
      populationGroup.populationRiskFactor
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_RISK, RakipColumn.I));
      populationGroup.season
          .addAll(getStringListValue(values, RakipRow.POPULATION_GROUP_SEASON, RakipColumn.I));

      return populationGroup;
    }

    static DataBackground retrieveDataBackground(String[][] values) {

      DataBackground dataBackground = new DataBackground();
      dataBackground.study = retrieveStudy(values);

      try {
        dataBackground.studySample = retrieveStudySample(values);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the study sample is optional
      }

      try {
        dataBackground.dietaryAssessmentMethod = retrieveDAM(values);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the dietary assessment method is optional
      }

      try {
        dataBackground.laboratory = retrieveLaboratory(values);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the laboratory is optional
      }

      try {
        dataBackground.assay = retrieveAssay(values);
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

      Study study = new Study();
      study.id = values[RakipRow.STUDY_ID.num][columnI];
      study.title = values[RakipRow.STUDY_TITLE.num][columnI];
      study.description = values[RakipRow.STUDY_DESCRIPTION.num][columnI];
      study.designType = values[RakipRow.STUDY_DESIGN_TYPE.num][columnI];
      study.measurementType = values[RakipRow.STUDY_ASSAY_MEASUREMENTS_TYPE.num][columnI];
      study.technologyType = values[RakipRow.STUDY_ASSAY_TECHNOLOGY_TYPE.num][columnI];
      study.technologyPlatform = values[RakipRow.STUDY_ASSAY_TECHNOLOGY_PLATFORM.num][columnI];
      study.accreditationProcedure = values[RakipRow.STUDY_ACCREDITATION_PROCEDURE.num][columnI];
      study.protocolName = values[RakipRow.STUDY_PROTOCOL_NAME.num][columnI];
      study.protocolType = values[RakipRow.STUDY_PROTOCOL_TYPE.num][columnI];
      study.protocolDescription = values[RakipRow.STUDY_PROTOCOL_DESCRIPTION.num][columnI];
      try {
        study.protocolUri = new URI(values[RakipRow.STUDY_PROTOCOL_URI.num][columnI]);
      } catch (URISyntaxException exception) {
        // does nothing
      }
      study.protocolVersion = values[RakipRow.STUDY_PROTOCOL_VERSION.num][columnI];
      study.parametersName = values[RakipRow.STUDY_PROTOCOL_PARAMETERS_NAME.num][columnI];
      study.componentsName = values[RakipRow.STUDY_PROTOCOL_COMPONENTS_NAME.num][columnI];
      study.componentsType = values[RakipRow.STUDY_PROTOCOL_COMPONENTS_TYPE.num][columnI];

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

      StudySample studySample = new StudySample();
      studySample.sample = values[RakipRow.STUDY_SAMPLE_NAME.num][columnI];
      studySample.collectionProtocol = values[RakipRow.STUDY_SAMPLE_PROTOCOL.num][columnI];
      studySample.samplingStrategy = values[RakipRow.STUDY_SAMPLE_STRATEGY.num][columnI];
      studySample.samplingProgramType = values[RakipRow.STUDY_SAMPLE_TYPE.num][columnI];
      studySample.samplingMethod = values[RakipRow.STUDY_SAMPLE_METHOD.num][columnI];
      studySample.samplingPlan = values[RakipRow.STUDY_SAMPLE_PLAN.num][columnI];
      studySample.samplingWeight = values[RakipRow.STUDY_SAMPLE_WEIGHT.num][columnI];
      studySample.samplingSize = values[RakipRow.STUDY_SAMPLE_SIZE.num][columnI];
      studySample.lotSizeUnit = values[RakipRow.STUDY_SAMPLE_SIZE.num][columnI];
      studySample.samplingPoint = values[RakipRow.STUDY_SAMPLE_POINT.num][columnI];

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

      DietaryAssessmentMethod dam = new DietaryAssessmentMethod();
      dam.collectionTool = values[RakipRow.DIETARY_ASSESSMENT_METHOD_TOOL.num][columnI];
      dam.numberOfNonConsecutiveOneDay =
          Integer.parseInt(values[RakipRow.DIETARY_ASSESSMENT_METHOD_1DAY.num][columnI]);
      dam.softwareTool = values[RakipRow.DIETARY_ASSESSMENT_METHOD_SOFTWARE_TOOL.num][columnI];
      dam.numberOfFoodItems.addAll(
          getStringListValue(values, RakipRow.DIETARY_ASSESSMENT_METHOD_ITEMS, RakipColumn.I));
      dam.recordTypes.addAll(getStringListValue(values,
          RakipRow.DIETARY_ASSESSMENT_METHOD_RECORD_TYPE, RakipColumn.I));
      dam.foodDescriptors.addAll(getStringListValue(values,
          RakipRow.DIETARY_ASSESSMENT_METHOD_DESCRIPTORS, RakipColumn.I));

      return dam;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static Laboratory retrieveLaboratory(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      // Check mandatory properties
      if (values[RakipRow.LABORATORY_ACCREDITATION.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing laboratory accreditation");
      }

      Laboratory laboratory = new Laboratory();
      laboratory.accreditation = values[RakipRow.LABORATORY_ACCREDITATION.num][columnI];
      laboratory.name = values[RakipRow.LABORATORY_ACCREDITATION.num][columnI];
      laboratory.country = values[RakipRow.LABORATORY_COUNTRY.num][columnI];

      return laboratory;
    }

    /** @throws IllegalArgumentException if a mandatory property is missing. */
    static Assay retrieveAssay(String[][] values) {

      int columnI = RakipColumn.I.ordinal();

      Assay assay = new Assay();
      assay.name = values[RakipRow.ASSAY_NAME.num][columnI];
      assay.description = values[RakipRow.ASSAY_DESCRIPTION.num][columnI];
      assay.moisturePercentage = values[RakipRow.ASSAY_MOIST_PERC.num][columnI];
      assay.fatPercentage = values[RakipRow.ASSAY_FAT_PERC.num][columnI];
      assay.detectionLimit = values[RakipRow.ASSAY_DETECTION_LIMIT.num][columnI];
      assay.quantificationLimit = values[RakipRow.ASSAY_QUANTIFICATION_LIMIT.num][columnI];
      assay.leftCensoredData = values[RakipRow.ASSAY_LEFT_CENSORED_DATA.num][columnI];
      assay.contaminationRange = values[RakipRow.ASSAY_CONTAMINATION_RANGE.num][columnI];
      assay.uncertaintyValue = values[RakipRow.ASSAY_UNCERTAINTY_VALUE.num][columnI];

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

      Parameter param = new Parameter();
      param.id = values[row][RakipColumn.L.ordinal()];

      String classificationText = values[row][RakipColumn.M.ordinal()].toLowerCase();
      if (classificationText.startsWith("input")) {
        param.classification = Parameter.Classification.input;
      } else if (classificationText.startsWith("constant")) {
        param.classification = Parameter.Classification.constant;
      } else if (classificationText.startsWith("output")) {
        param.classification = Parameter.Classification.output;
      }

      param.name = values[row][RakipColumn.N.ordinal()];
      param.description = values[row][RakipColumn.O.ordinal()];
      param.type = values[row][RakipColumn.P.ordinal()];
      param.unit = values[row][RakipColumn.Q.ordinal()];
      param.unitCategory = values[row][RakipColumn.R.ordinal()];
      param.dataType = values[row][RakipColumn.S.ordinal()];
      param.source = values[row][RakipColumn.T.ordinal()];
      param.subject = values[row][RakipColumn.U.ordinal()];
      param.distribution = values[row][RakipColumn.V.ordinal()];
      param.value = values[row][RakipColumn.W.ordinal()];
      param.reference = values[row][RakipColumn.X.ordinal()];
      param.variabilitySubject = values[row][RakipColumn.Y.ordinal()];
      param.modelApplicability.add(values[row][RakipColumn.Z.ordinal()]);
      param.error = Double.parseDouble(values[row][RakipColumn.AA.ordinal()]);

      return param;
    }
  }
}
