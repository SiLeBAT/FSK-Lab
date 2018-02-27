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
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
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

    // Reads model meta data
    if (StringUtils.isEmpty(nodeSettings.spreadsheet)) {
      throw new InvalidSettingsException("Model metadata is not provided");
    }

    final GenericModel genericModel;
    final File metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.spreadsheet));
    try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
      final XSSFSheet sheet = workbook.getSheetAt(0);

      if (sheet.getPhysicalNumberOfRows() > 29) {
        // Process new RAKIP spreadsheet
        genericModel = new GenericModel();
        genericModel.generalInformation = RAKIPSheetImporter.retrieveGeneralInformation(sheet);
        genericModel.scope = RAKIPSheetImporter.retrieveScope(sheet);
        genericModel.dataBackground = RAKIPSheetImporter.retrieveDataBackground(sheet);
        // TODO: ModelMath
        for (int i = 132; i <= 152; i++) {
          try {
            Parameter param = RAKIPSheetImporter.retrieveParameter(sheet, i);
            genericModel.modelMath.parameter.add(param);
          } catch (Exception exception) {
            exception.printStackTrace();
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

          Set<Path> libPaths = libReg.getPaths(libraries);
          libPaths.forEach(l -> portObj.libs.add(l.toFile()));
        }
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

  private static class RAKIPSheetImporter {

    enum Column {
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

    private enum Row {

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
      HAZARD_BMD(52), // benchmark dose
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
      POPULATION_GROUP_RISK(71), // Risk and population factors
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
      Row(int num) {
        this.num = num - 1;
      }
    }

    /**
     * @throws IllegalStateException if the cell contains a string
     * @return 0 for blank cells
     */
    static Double getNumericValue(XSSFSheet sheet, int row, Column col) {
      XSSFCell cell = sheet.getRow(row).getCell(col.ordinal());
      return cell.getNumericCellValue();
    }

    static Double getNumericValue(XSSFSheet sheet, Row row, Column col) {
      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      return cell.getNumericCellValue();
    }

    /**
     * @return empty string for blank cells.
     */
    static String getStringValue(XSSFSheet sheet, int row, Column col) {
      XSSFCell cell = sheet.getRow(row).getCell(col.ordinal());
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        return Double.toString(cell.getNumericCellValue());
      }
      return cell.getStringCellValue();
    }

    static String getStringValue(XSSFSheet sheet, Row row, Column col) {
      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      return cell.getStringCellValue();
    }

    static List<String> getStringListValue(XSSFSheet sheet, Row row, Column col) {
      XSSFCell cell = sheet.getRow(row.num).getCell(col.ordinal());
      return Arrays.stream(cell.getStringCellValue().split(",")).collect(Collectors.toList());
    }

    static GeneralInformation retrieveGeneralInformation(XSSFSheet sheet) {

      GeneralInformation gi = new GeneralInformation();
      gi.name = getStringValue(sheet, Row.GENERAL_INFORMATION_NAME, Column.I);
      gi.source = getStringValue(sheet, Row.GENERAL_INFORMATION_SOURCE, Column.I);
      gi.identifier = getStringValue(sheet, Row.GENERAL_INFORMATION_IDENTIFIER, Column.I);
      gi.rights = getStringValue(sheet, Row.GENERAL_INFORMATION_RIGHTS, Column.I);
      gi.isAvailable =
          getStringValue(sheet, Row.GENERAL_INFORMATION_AVAILABILITY, Column.I) == "Yes";
      gi.language = getStringValue(sheet, Row.GENERAL_INFORMATION_LANGUAGE, Column.I);
      gi.software = getStringValue(sheet, Row.GENERAL_INFORMATION_SOFTWARE, Column.I);
      gi.languageWrittenIn =
          getStringValue(sheet, Row.GENERAL_INFORMATION_LANGUAGE_WRITTEN_IN, Column.I);
      gi.status = getStringValue(sheet, Row.GENERAL_INFORMATION_STATUS, Column.I);
      gi.objective = getStringValue(sheet, Row.GENERAL_INFORMATION_OBJECTIVE, Column.I);
      gi.description = getStringValue(sheet, Row.GENERAL_INFORMATION_DESCRIPTION, Column.I);

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
      String honorific = getStringValue(sheet, row, Column.K); // honorific prefix
      String givenName = getStringValue(sheet, row, Column.M);
      String additionalName = getStringValue(sheet, row, Column.N);
      String familyName = getStringValue(sheet, row, Column.O);
      String organization = getStringValue(sheet, row, Column.P);
      String telephone = getStringValue(sheet, row, Column.Q);
      String email = getStringValue(sheet, row, Column.R);
      String country = getStringValue(sheet, row, Column.S);
      String city = getStringValue(sheet, row, Column.T);
      String zipCode = getStringValue(sheet, row, Column.U);
      String postOfficeBox = getStringValue(sheet, row, Column.V);
      String streetAddress = getStringValue(sheet, row, Column.W);
      String extendedAddress = getStringValue(sheet, row, Column.X);
      String region = getStringValue(sheet, row, Column.Y);

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
     * @param string Whole string with the type. E.g. "Abstract" or "Audiovisual material".
     * @return the RIS type corresponding to the given string. If null, empty or non-valid string
     *         return null.
     */
    private static com.gmail.gcolaianni5.jris.bean.Type getRisType(String string) {

      Type type = null;

      if (string.equals("Abstract")) {
        type = Type.ABST;
      } else if (string.equals("Audiovisual material")) {
        type = Type.ADVS;
      } else if (string.equals("Aggregated Database")) {
        type = Type.AGGR;
      } else if (string.equals("Ancient Text")) {
        type = Type.ANCIENT;
      } else if (string.equals("Art Work")) {
        type = Type.ART;
      } else if (string.equals("Bill")) {
        type = Type.BILL;
      } else if (string.equals("Blog")) {
        type = Type.BLOG;
      } else if (string.equals("Whole book")) {
        type = Type.BOOK;
      } else if (string.equals("Case")) {
        type = Type.CASE;
      } else if (string.equals("Book chapter")) {
        type = Type.CHAP;
      } else if (string.equals("Chart")) {
        type = Type.CHART;
      } else if (string.equals("Classical work")) {
        type = Type.CLSWK;
      } else if (string.equals("Computer program")) {
        type = Type.COMP;
      } else if (string.equals("Conference proceeding")) {
        type = Type.CONF;
      } else if (string.equals("Conference paper")) {
        type = Type.CPAPER;
      } else if (string.equals("Catalog")) {
        type = Type.CTLG;
      } else if (string.equals("Data file")) {
        type = Type.DATA;
      } else if (string.equals("Online Database")) {
        type = Type.DBASE;
      } else if (string.equals("Dictionary")) {
        type = Type.DICT;
      } else if (string.equals("Electronic Book")) {
        type = Type.EBOOK;
      } else if (string.equals("Electronic Book Section")) {
        type = Type.ECHAP;
      } else if (string.equals("Edited Book")) {
        type = Type.EDBOOK;
      } else if (string.equals("Electronic Article")) {
        type = Type.EJOUR;
      } else if (string.equals("Web Page")) {
        type = Type.ELEC;
      } else if (string.equals("Encyclopedia")) {
        type = Type.ENCYC;
      } else if (string.equals("Equation")) {
        type = Type.EQUA;
      } else if (string.equals("Figure")) {
        type = Type.FIGURE;
      } else if (string.equals("Generic")) {
        type = Type.GEN;
      } else if (string.equals("Government Document")) {
        type = Type.GOVDOC;
      } else if (string.equals("Grant")) {
        type = Type.GRANT;
      } else if (string.equals("Hearing")) {
        type = Type.HEAR;
      } else if (string.equals("Internet Communication")) {
        type = Type.ICOMM;
      } else if (string.equals("In Press")) {
        type = Type.INPR;
      } else if (string.equals("Journal (full)")) {
        type = Type.JFULL;
      } else if (string.equals("Journal")) {
        type = Type.JOUR;
      } else if (string.equals("Legal Rule or Regulation")) {
        type = Type.LEGAL;
      } else if (string.equals("Manuscript")) {
        type = Type.MANSCPT;
      } else if (string.equals("Map")) {
        type = Type.MAP;
      } else if (string.equals("Magazine article")) {
        type = Type.MGZN;
      } else if (string.equals("Motion picture")) {
        type = Type.MPCT;
      } else if (string.equals("Online Multimedia")) {
        type = Type.MULTI;
      } else if (string.equals("Music score")) {
        type = Type.MUSIC;
      } else if (string.equals("Newspaper")) {
        type = Type.NEWS;
      } else if (string.equals("Pamphlet")) {
        type = Type.PAMP;
      } else if (string.equals("Patent")) {
        type = Type.PAT;
      } else if (string.equals("Personal communication")) {
        type = Type.PCOMM;
      } else if (string.equals("Report")) {
        type = Type.RPRT;
      } else if (string.equals("Serial publication")) {
        type = Type.SER;
      } else if (string.equals("Slide")) {
        type = Type.SLIDE;
      } else if (string.equals("Sound recording")) {
        type = Type.SOUND;
      } else if (string.equals("Standard")) {
        type = Type.STAND;
      } else if (string.equals("Statute")) {
        type = Type.STAT;
      } else if (string.equals("Thesis/Dissertation")) {
        type = Type.THES;
      } else if (string.equals("Unpublished work")) {
        type = Type.UNPB;
      } else if (string.equals("Video recording")) {
        type = Type.VIDEO;
      }

      return type;
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

      String isReferenceDescription = getStringValue(sheet, row, Column.K);
      String type = getStringValue(sheet, row, Column.L);
      Double pmid = getNumericValue(sheet, row, Column.N);
      String doi = getStringValue(sheet, row, Column.O);
      String author = getStringValue(sheet, row, Column.P);
      String title = getStringValue(sheet, row, Column.Q);
      String abstractText = getStringValue(sheet, row, Column.R);
      String status = getStringValue(sheet, row, Column.T);
      String website = getStringValue(sheet, row, Column.U);

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

      com.gmail.gcolaianni5.jris.bean.Type risType = getRisType(type);
      if (risType != null) {
        record.setType(risType);
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
      if (sheet.getRow(Row.MODEL_CATEGORY_CLASS.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing model class");
      }

      ModelCategory modelCategory = new ModelCategory();
      modelCategory.modelClass = getStringValue(sheet, Row.MODEL_CATEGORY_CLASS, Column.I);
      modelCategory.modelSubClass
          .addAll(getStringListValue(sheet, Row.MODEL_CATEGORY_SUBCLASS, Column.I));
      modelCategory.modelClassComment = getStringValue(sheet, Row.MODEL_CATEGORY_COMMENT, Column.I);
      modelCategory.basicProcess
          .addAll(getStringListValue(sheet, Row.MODEL_CATEGORY_PROCESS, Column.I));

      return modelCategory;
    }

    static Scope retrieveScope(XSSFSheet sheet) {

      Scope scope = new Scope();
      scope.hazard = retrieveHazard(sheet);
      scope.populationGroup = retrievePopulationGroup(sheet);
      return scope;
    }

    static Hazard retrieveHazard(XSSFSheet sheet) {

      // Check mandatory properties
      if (sheet.getRow(Row.HAZARD_TYPE.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard type is missing");
      }

      XSSFCell nameCell = sheet.getRow(Row.HAZARD_NAME.num).getCell(Column.I.ordinal());
      if (sheet.getRow(Row.HAZARD_NAME.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        System.out.println(nameCell.getRawValue());
        throw new IllegalArgumentException("Hazard name is missing");
      }
      if (sheet.getRow(Row.HAZARD_UNIT.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard unit is missing");
      }

      Hazard hazard = new Hazard();
      hazard.hazardType = getStringValue(sheet, Row.HAZARD_TYPE, Column.I);
      hazard.hazardName = getStringValue(sheet, Row.HAZARD_NAME, Column.I);
      hazard.hazardDescription = getStringValue(sheet, Row.HAZARD_DESCRIPTION, Column.I);
      hazard.hazardUnit = getStringValue(sheet, Row.HAZARD_UNIT, Column.I);
      hazard.adverseEffect = getStringValue(sheet, Row.HAZARD_ADVERSE_EFFECT, Column.I);
      hazard.sourceOfContamination =
          getStringValue(sheet, Row.HAZARD_CONTAMINATION_SOURCE, Column.I);
      hazard.bmd = getStringValue(sheet, Row.HAZARD_BMD, Column.I);
      hazard.mrl = getStringValue(sheet, Row.HAZARD_MRL, Column.I);
      hazard.noael = getStringValue(sheet, Row.HAZARD_NOAEL, Column.I);
      hazard.loael = getStringValue(sheet, Row.HAZARD_LOAEL, Column.I);
      hazard.aoel = getStringValue(sheet, Row.HAZARD_AOEL, Column.I);
      hazard.ard = getStringValue(sheet, Row.HAZARD_ARFD, Column.I);
      hazard.hazardIndSum = getStringValue(sheet, Row.HAZARD_IND_SUM, Column.I);

      return hazard;
    }

    static PopulationGroup retrievePopulationGroup(XSSFSheet sheet) {

      // Check mandatory properties
      if (sheet.getRow(Row.POPULATION_GROUP_NAME.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing population name");
      }

      PopulationGroup pg = new PopulationGroup();
      pg.populationName = getStringValue(sheet, Row.POPULATION_GROUP_NAME, Column.I);
      pg.targetPopulation = getStringValue(sheet, Row.POPULATION_GROUP_TARGET, Column.I);
      pg.populationSpan.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_SPAN, Column.I));
      pg.populationDescription
          .addAll(getStringListValue(sheet, Row.POPULATION_GROUP_DESCRIPTION, Column.I));
      pg.populationAge.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_AGE, Column.I));
      pg.populationGender = getStringValue(sheet, Row.POPULATION_GROUP_GENDER, Column.I);
      pg.bmi.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_BMI, Column.I));
      pg.specialDietGroups.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_DIET, Column.I));
      pg.patternConsumption
          .addAll(getStringListValue(sheet, Row.POPULATION_GROUP_PATTERN_CONSUMPTION, Column.I));
      pg.region.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_REGION, Column.I));
      pg.country.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_COUNTRY, Column.I));
      pg.populationRiskFactor
          .addAll(getStringListValue(sheet, Row.POPULATION_GROUP_RISK, Column.I));
      pg.season.addAll(getStringListValue(sheet, Row.POPULATION_GROUP_SEASON, Column.I));

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
      if (sheet.getRow(Row.STUDY_ID.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing study identifier");
      }
      if (sheet.getRow(Row.STUDY_TITLE.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing study title");
      }

      Study study = new Study();
      study.id = getStringValue(sheet, Row.STUDY_ID, Column.I);
      study.title = getStringValue(sheet, Row.STUDY_TITLE, Column.I);
      study.description = getStringValue(sheet, Row.STUDY_DESCRIPTION, Column.I);
      study.designType = getStringValue(sheet, Row.STUDY_DESIGN_TYPE, Column.I);
      study.measurementType = getStringValue(sheet, Row.STUDY_ASSAY_MEASUREMENTS_TYPE, Column.I);
      study.technologyType = getStringValue(sheet, Row.STUDY_ASSAY_TECHNOLOGY_TYPE, Column.I);
      study.technologyPlatform =
          getStringValue(sheet, Row.STUDY_ASSAY_TECHNOLOGY_PLATFORM, Column.I);
      study.accreditationProcedure =
          getStringValue(sheet, Row.STUDY_ACCREDITATION_PROCEDURE, Column.I);
      study.protocolName = getStringValue(sheet, Row.STUDY_PROTOCOL_NAME, Column.I);
      study.protocolType = getStringValue(sheet, Row.STUDY_PROTOCOL_TYPE, Column.I);
      study.protocolDescription = getStringValue(sheet, Row.STUDY_PROTOCOL_DESCRIPTION, Column.I);
      try {
        study.protocolUri = new URI(getStringValue(sheet, Row.STUDY_PROTOCOL_URI, Column.I));
      } catch (URISyntaxException e) {
      }
      study.protocolVersion = getStringValue(sheet, Row.STUDY_PROTOCOL_VERSION, Column.I);
      study.parametersName = getStringValue(sheet, Row.STUDY_PROTOCOL_PARAMETERS_NAME, Column.I);
      study.componentsName = getStringValue(sheet, Row.STUDY_PROTOCOL_COMPONENTS_NAME, Column.I);
      study.componentsType = getStringValue(sheet, Row.STUDY_PROTOCOL_COMPONENTS_TYPE, Column.I);

      return study;
    }

    static StudySample retrieveStudySample(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(Row.STUDY_SAMPLE_NAME.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sample name");
      }
      if (sheet.getRow(Row.STUDY_SAMPLE_PROTOCOL.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sampling plan");
      }
      if (sheet.getRow(Row.STUDY_SAMPLE_STRATEGY.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sampling weight");
      }

      StudySample studySample = new StudySample();
      studySample.sample = getStringValue(sheet, Row.STUDY_SAMPLE_NAME, Column.I);
      studySample.collectionProtocol = getStringValue(sheet, Row.STUDY_SAMPLE_PROTOCOL, Column.I);
      studySample.samplingStrategy = getStringValue(sheet, Row.STUDY_SAMPLE_STRATEGY, Column.I);
      studySample.samplingProgramType = getStringValue(sheet, Row.STUDY_SAMPLE_TYPE, Column.I);
      studySample.samplingMethod = getStringValue(sheet, Row.STUDY_SAMPLE_METHOD, Column.I);
      studySample.samplingPlan = getStringValue(sheet, Row.STUDY_SAMPLE_PLAN, Column.I);
      studySample.samplingWeight = getStringValue(sheet, Row.STUDY_SAMPLE_WEIGHT, Column.I);
      studySample.samplingSize = getStringValue(sheet, Row.STUDY_SAMPLE_SIZE, Column.I);
      studySample.lotSizeUnit = getStringValue(sheet, Row.STUDY_SAMPLE_SIZE_UNIT, Column.I);
      studySample.samplingPoint = getStringValue(sheet, Row.STUDY_SAMPLE_POINT, Column.I);


      return studySample;
    }

    static DietaryAssessmentMethod retrieveDAM(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(Row.DIETARY_ASSESSMENT_METHOD_TOOL.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing methodological tool");
      }
      if (sheet.getRow(Row.DIETARY_ASSESSMENT_METHOD_1DAY.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing non consecutive one day");
      }

      DietaryAssessmentMethod dam = new DietaryAssessmentMethod();
      dam.collectionTool = getStringValue(sheet, Row.DIETARY_ASSESSMENT_METHOD_TOOL, Column.I);
      dam.numberOfNonConsecutiveOneDay =
          getNumericValue(sheet, Row.DIETARY_ASSESSMENT_METHOD_1DAY, Column.I).intValue();
      dam.softwareTool =
          getStringValue(sheet, Row.DIETARY_ASSESSMENT_METHOD_SOFTWARE_TOOL, Column.I);
      dam.numberOfFoodItems
          .addAll(getStringListValue(sheet, Row.DIETARY_ASSESSMENT_METHOD_ITEMS, Column.I));
      dam.recordTypes
          .addAll(getStringListValue(sheet, Row.DIETARY_ASSESSMENT_METHOD_RECORD_TYPE, Column.I));
      dam.foodDescriptors
          .addAll(getStringListValue(sheet, Row.DIETARY_ASSESSMENT_METHOD_DESCRIPTORS, Column.I));

      return dam;
    }

    static Laboratory retrieveLaboratory(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(Row.LABORATORY_ACCREDITATION.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing laboratory accreditation");
      }

      Laboratory laboratory = new Laboratory();
      laboratory.accreditation = getStringValue(sheet, Row.LABORATORY_ACCREDITATION, Column.I);
      laboratory.name = getStringValue(sheet, Row.LABORATORY_NAME, Column.I);
      laboratory.country = getStringValue(sheet, Row.LABORATORY_COUNTRY, Column.I);

      return laboratory;
    }

    static Assay retrieveAssay(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(Row.ASSAY_NAME.num).getCell(Column.I.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing assay name");
      }

      Assay assay = new Assay();
      assay.name = getStringValue(sheet, Row.ASSAY_NAME, Column.I);
      assay.description = getStringValue(sheet, Row.ASSAY_DESCRIPTION, Column.I);
      assay.moisturePercentage = getStringValue(sheet, Row.ASSAY_MOIST_PERC, Column.I);
      assay.detectionLimit = getStringValue(sheet, Row.ASSAY_DETECTION_LIMIT, Column.I);
      assay.quantificationLimit = getStringValue(sheet, Row.ASSAY_QUANTIFICATION_LIMIT, Column.I);
      assay.leftCensoredData = getStringValue(sheet, Row.ASSAY_LEFT_CENSORED_DATA, Column.I);
      assay.contaminationRange = getStringValue(sheet, Row.ASSAY_CONTAMINATION_RANGE, Column.I);
      assay.uncertaintyValue = getStringValue(sheet, Row.ASSAY_UNCERTAINTY_VALUE, Column.I);

      return assay;
    }

    static Parameter retrieveParameter(XSSFSheet sheet, int row) {

      // Check first mandatory properties
      if (sheet.getRow(row).getCell(Column.L.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter id");
      }
      if (sheet.getRow(row).getCell(Column.M.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter classification");
      }
      if (sheet.getRow(row).getCell(Column.N.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter name");
      }
      if (sheet.getRow(row).getCell(Column.Q.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter unit");
      }
      if (sheet.getRow(row).getCell(Column.S.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing data type");
      }

      Parameter param = new Parameter();
      param.id = getStringValue(sheet, row, Column.L);

      String classificationText = getStringValue(sheet, row, Column.M).toLowerCase();
      if (classificationText.startsWith("input")) {
        param.classification = Parameter.Classification.input;
      } else if (classificationText.startsWith("constant")) {
        param.classification = Parameter.Classification.constant;
      } else if (classificationText.startsWith("output")) {
        param.classification = Parameter.Classification.output;
      }

      param.name = getStringValue(sheet, row, Column.N);
      param.description = getStringValue(sheet, row, Column.O);
      param.type = getStringValue(sheet, row, Column.P);
      param.unit = getStringValue(sheet, row, Column.Q);
      param.unitCategory = getStringValue(sheet, row, Column.R);
      param.dataType = getStringValue(sheet, row, Column.S);
      param.source = getStringValue(sheet, row, Column.T);
      param.subject = getStringValue(sheet, row, Column.U);
      param.distribution = getStringValue(sheet, row, Column.V);
      param.value = getStringValue(sheet, row, Column.W);
      param.reference = getStringValue(sheet, row, Column.X);
      param.variabilitySubject = getStringValue(sheet, row, Column.Y);
      param.modelApplicability.add(getStringValue(sheet, row, Column.Z));
      param.error = getNumericValue(sheet, row, Column.AA);

      return param;
    }
  }
}
