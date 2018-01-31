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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import ezvcard.property.Organization;
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

      if (sheet.getPhysicalNumberOfRows() > 29) {
        // Process new RAKIP spreadsheet
        genericModel = new GenericModel();
        genericModel.generalInformation = RAKIPSheetImporter.retrieveGeneralInformation(sheet);
        genericModel.scope = RAKIPSheetImporter.retrieveScope(sheet);
        genericModel.dataBackground = RAKIPSheetImporter.retrieveDataBackground(sheet);
        // TODO: ModelMath
        for (int i = 124; i < 130; i++) {
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
      A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    }

    enum GeneralInformationRow {

      NAME(1), SOURCE(2), IDENTIFIER(3), RIGHTS(7), AVAILABILITY(8), LANGUAGE(23), SOFTWARE(
          24), LANGUAGE_WRITTEN_IN(25), STATUS(33), OBJECTIVE(34), DESCRIPTION(35);

      final int num;

      GeneralInformationRow(int num) {
        this.num = num;
      }
    }

    enum ModelCategoryRow {

      MODEL_CLASS(26), MODEL_SUBCLASS(27), MODEL_CLASS_COMMENT(28), BASIC_PROCESS(31);

      final int num;

      ModelCategoryRow(int num) {
        this.num = num;
      }
    }

    enum HazardRow {

      TYPE(47), NAME(48), DESCRIPTION(49), UNIT(50), ADVERSE_EFFECT(51), SOURCE_OF_CONTAMINATION(
          52), BMD(53), MRL(54), NOAEL(55), LOAEL(56), AOEL(57), ARD(58), ADI(59), IND_SUM(60);

      final int num;

      HazardRow(int num) {
        this.num = num;
      }
    }

    enum PopulationGroupRow {

      NAME(61), TARGET(62), SPAN(63), DESCRIPTION(64), AGE(65), GENDER(66), BMI(
          67), SPECIAL_DIET_GROUPS(
              68), PATTERN_CONSUMPTION(69), REGION(70), COUNTRY(71), RISK_FACTOR(72), SEASON(73);

      final int num;

      PopulationGroupRow(int num) {
        this.num = num;
      }
    }

    enum StudyRow {

      ID(78), TITLE(79), DESCRIPTION(80), DESIGN_TYPE(81), ASSAY_MEASUREMENT_TYPE(
          82), ASSAY_TECHNOLOGY_TYPE(83), ASSAY_TECHNOLOGY_PLATFORM(84), ACCREDITATION_PROCEDURE(
              85), PROTOCOL_NAME(86), PROTOCOL_TYPE(87), PROTOCOL_DESCRIPTION(88), PROTOCOL_URI(
                  89), PROTOCOL_VERSION(90), PROTOCOL_PARAMETERS_NAME(
                      91), PROTOCOL_COMPONENTS_NAME(92), PROTOCOL_COMPONENTS_TYPE(93);

      final int num;

      StudyRow(int num) {
        this.num = num;
      }
    }

    enum StudySampleRow {

      NAME(94), MOIST_PERC(95), SAMPLING_STRATEGY(96), SAMPLING_PROGRAM_TYPE(97), SAMPLING_METHOD(
          98), SAMPLING_PLAN(99), SAMPLING_WEIGHT(
              100), SAMPLING_SIZE(101), LOT_SIZE_UNIT(102), SAMPLING_POINT(103);

      final int num;

      StudySampleRow(int num) {
        this.num = num;
      }
    }

    enum DAMRow {

      METHOD_TOOL(104), NON_CONSECUTIVE_1DAY(105), DIETARY_SOFTWARE_TOOL(106), FOOD_ITEMS(
          107), RECORD_TYPE(108), FOOD_DESCRIPTORS(109);

      final int num;

      DAMRow(int num) {
        this.num = num;
      }
    }

    enum LaboratoryRow {

      ACCREDITATION(110), NAME(111), COUNTRY(112);

      final int num;

      LaboratoryRow(int num) {
        this.num = num;
      }
    }

    enum AssayRow {

      NAME(113), DESCRIPTION(114), MOIST_PERC(115), FAT_PERC(116), DETECTION_LIMIT(
          117), QUANTIFICATION_LIMIT(
              118), LEFT_CENSORED_DATA(119), CONTAMINATION_RANGE(120), UNCERTAINTY_VALUE(121);

      final int num;

      AssayRow(int num) {
        this.num = num;
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

    /**
     * @return empty string for blank cells.
     */
    static String getStringValue(XSSFSheet sheet, int row, Column col) {
      XSSFCell cell = sheet.getRow(row).getCell(col.ordinal());
      return cell.getStringCellValue();
    }

    /**
     * Get strings from a cell with multiple values separated with commas.
     */
    static List<String> getStringListValue(XSSFSheet sheet, int row, Column col) {
      XSSFCell cell = sheet.getRow(row).getCell(col.ordinal());
      return Arrays.stream(cell.getStringCellValue().split(",")).collect(Collectors.toList());
    }

    static GeneralInformation retrieveGeneralInformation(XSSFSheet sheet) {

      GeneralInformation gi = new GeneralInformation();
      gi.name = getStringValue(sheet, GeneralInformationRow.NAME.num, Column.H);
      gi.source = getStringValue(sheet, GeneralInformationRow.SOURCE.num, Column.H);
      gi.identifier = getStringValue(sheet, GeneralInformationRow.IDENTIFIER.num, Column.H);
      gi.rights = getStringValue(sheet, GeneralInformationRow.RIGHTS.num, Column.H);
      gi.isAvailable =
          getStringValue(sheet, GeneralInformationRow.AVAILABILITY.num, Column.H) == "Yes";
      gi.language = getStringValue(sheet, GeneralInformationRow.LANGUAGE.num, Column.H);
      gi.software = getStringValue(sheet, GeneralInformationRow.SOFTWARE.num, Column.H);
      gi.languageWrittenIn =
          getStringValue(sheet, GeneralInformationRow.LANGUAGE_WRITTEN_IN.num, Column.H);
      gi.status = getStringValue(sheet, GeneralInformationRow.STATUS.num, Column.H);
      gi.objective = getStringValue(sheet, GeneralInformationRow.OBJECTIVE.num, Column.H);
      gi.description = getStringValue(sheet, GeneralInformationRow.DESCRIPTION.num, Column.H);

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

      VCard vCard = new VCard();

      String nameText = getStringValue(sheet, row, Column.K);
      String organizationText = getStringValue(sheet, row, Column.L);
      String telephoneText = getStringValue(sheet, row, Column.M);
      String mailText = getStringValue(sheet, row, Column.N);
      String countryText = getStringValue(sheet, row, Column.O);
      String cityText = getStringValue(sheet, row, Column.P);
      Integer postalCodeInt = getNumericValue(sheet, row, Column.Q).intValue();

      // throw exception if mail is missing
      if (mailText.isEmpty()) {
        throw new IllegalArgumentException("Missing mail");
      }

      // name is optional. Ignore empty cell.
      if (!nameText.isEmpty()) {
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(nameText.split(",")[0]); // Assign family name
        structuredName.setGiven(nameText.split(",")[1]); // Assign given name
        vCard.setStructuredName(structuredName);
      }

      // organization is optional. Ignore empty cell.
      if (!organizationText.isEmpty()) {
        Organization organization = new Organization();
        organization.getValues().add(organizationText);
        vCard.setOrganization(organization);
      }

      // telephone is optional. Ignore empty cell.
      if (!telephoneText.isEmpty()) {
        vCard.addTelephoneNumber(telephoneText, TelephoneType.VOICE);
      }

      vCard.addEmail(new Email(mailText));

      // import country, city and postal code
      if (!countryText.isEmpty() && !cityText.isEmpty() && postalCodeInt != 0) {
        Address address = new Address();
        address.setCountry(countryText);
        address.setLocality(cityText);
        address.setPostalCode(postalCodeInt.toString());
        vCard.addAddress(address);
      }

      return vCard;
    }

    static Map<String, com.gmail.gcolaianni5.jris.bean.Type> RIS_TYPES =
        new HashMap<String, com.gmail.gcolaianni5.jris.bean.Type>() {

          private static final long serialVersionUID = 9216631877569612872L;

          {
            put("Abstract", Type.ABST);
            put("Audiovisual material", Type.ADVS);
            put("Aggregated Database", Type.AGGR);
            put("Ancient Text", Type.ANCIENT);
            put("Art Work", Type.ART);
            put("Bill", Type.BILL);
            put("Blog", Type.BLOG);
            put("Whole book", Type.BOOK);
            put("Case", Type.CASE);
            put("Book chapter", Type.CHAP);
            put("Chart", Type.CHART);
            put("Classical Work", Type.CLSWK);
            put("Computer program", Type.COMP);
            put("Conference proceeding", Type.CONF);
            put("Conference paper", Type.CPAPER);
            put("Catalog", Type.CTLG);
            put("Data file", Type.DATA);
            put("Online Database", Type.DBASE);
            put("Dictionary", Type.DICT);
            put("Electronic Book", Type.EBOOK);
            put("Electronic Book Section", Type.ECHAP);
            put("Edited Book", Type.EDBOOK);
            put("Electronic Article", Type.EJOUR);
            put("Web Page", Type.ELEC);
            put("Encyclopedia", Type.ENCYC);
            put("Equation", Type.EQUA);
            put("Figure", Type.FIGURE);
            put("Generic", Type.GEN);
            put("Government Document", Type.GOVDOC);
            put("Grant", Type.GRANT);
            put("Hearing", Type.HEAR);
            put("Internet Communication", Type.ICOMM);
            put("In Press", Type.INPR);
            put("Journal (full)", Type.JFULL);
            put("Journal", Type.JOUR);
            put("Legal Rule or Regulation", Type.LEGAL);
            put("Manuscript", Type.MANSCPT);
            put("Map", Type.MAP);
            put("Magazine article", Type.MGZN);
            put("Motion picture", Type.MPCT);
            put("Online Multimedia", Type.MULTI);
            put("Music score", Type.MUSIC);
            put("Newspaper", Type.NEWS);
            put("Pamphlet", Type.PAMP);
            put("Patent", Type.PAT);
            put("Personal communication", Type.PCOMM);
            put("Report", Type.RPRT);
            put("Serial publication", Type.SER);
            put("Slide", Type.SLIDE);
            put("Sound recording", Type.SOUND);
            put("Standard", Type.STAND);
            put("Statute", Type.STAT);
            put("Thesis/Dissertation", Type.THES);
            put("Unpublished work", Type.UNPB);
            put("Video recording", Type.VIDEO);
          }
        };

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

      String isReferenceDescriptionText = getStringValue(sheet, row, Column.K);
      String typeText = getStringValue(sheet, row, Column.L);
      Double dateText = getNumericValue(sheet, row, Column.M);
      Double pmidText = getNumericValue(sheet, row, Column.N);
      String doiText = getStringValue(sheet, row, Column.O);
      String authorListText = getStringValue(sheet, row, Column.P);
      String titleText = getStringValue(sheet, row, Column.Q);
      String abstractText = getStringValue(sheet, row, Column.R);
      String journalVolumeIssueText = getStringValue(sheet, row, Column.S);
      String statusText = getStringValue(sheet, row, Column.T);
      String websiteText = getStringValue(sheet, row, Column.U);

      // throw exception if isReferenceDescription or doi are missing
      if (isReferenceDescriptionText.isEmpty()) {
        throw new IllegalArgumentException("Missing Is reference description?");
      }
      if (doiText.isEmpty()) {
        throw new IllegalArgumentException("Missing DOI");
      }

      Record record = new Record();

      // Save isReferenceDescription in U1 tag (user definable #1)
      record.setUserDefinable1(isReferenceDescriptionText);
      record.setType(RIS_TYPES.get(typeText));

      // TODO: save date

      // Save PMID in U2 tag (user definable #2)
      record.setUserDefinable2(pmidText.toString());

      record.setDoi(doiText);

      // Save author list
      Arrays.stream(authorListText.split(";")).forEach(record::addAuthor);

      record.setTitle(titleText); // Save title in TI tag
      record.setAbstr(abstractText); // Save abstract in AB tag

      // TODO: save journal

      // Save status in U3 tag (user definable #3)
      record.setUserDefinable3(statusText);

      record.setUrl(websiteText);

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

      if (sheet.getRow(ModelCategoryRow.MODEL_CLASS.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing model class");
      }

      ModelCategory modelCategory = new ModelCategory();
      modelCategory.modelClass = getStringValue(sheet, ModelCategoryRow.MODEL_CLASS.num, Column.H);
      modelCategory.modelSubClass
          .addAll(getStringListValue(sheet, ModelCategoryRow.MODEL_SUBCLASS.num, Column.H));
      modelCategory.modelClassComment =
          getStringValue(sheet, ModelCategoryRow.MODEL_CLASS_COMMENT.num, Column.H);
      modelCategory.basicProcess
          .addAll(getStringListValue(sheet, ModelCategoryRow.BASIC_PROCESS.num, Column.H));

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
      if (sheet.getRow(HazardRow.TYPE.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard type is missing");
      }
      if (sheet.getRow(HazardRow.NAME.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard name is missing");
      }
      if (sheet.getRow(HazardRow.UNIT.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Hazard unit is missing");
      }

      Hazard hazard = new Hazard();
      hazard.hazardType = getStringValue(sheet, HazardRow.TYPE.num, Column.H);
      hazard.hazardName = getStringValue(sheet, HazardRow.NAME.num, Column.H);
      hazard.hazardDescription = getStringValue(sheet, HazardRow.DESCRIPTION.num, Column.H);
      hazard.hazardUnit = getStringValue(sheet, HazardRow.UNIT.num, Column.H);
      hazard.adverseEffect = getStringValue(sheet, HazardRow.ADVERSE_EFFECT.num, Column.H);
      // TODO: source of contamination
      hazard.bmd = getStringValue(sheet, HazardRow.BMD.num, Column.H);
      hazard.noael = getStringValue(sheet, HazardRow.NOAEL.num, Column.H);
      hazard.loael = getStringValue(sheet, HazardRow.LOAEL.num, Column.H);
      hazard.aoel = getStringValue(sheet, HazardRow.AOEL.num, Column.H);
      hazard.ard = getStringValue(sheet, HazardRow.ARD.num, Column.H);
      hazard.hazardIndSum = getStringValue(sheet, HazardRow.IND_SUM.num, Column.H);

      return hazard;
    }

    static PopulationGroup retrievePopulationGroup(XSSFSheet sheet) {

      // Check mandatory properties
      if (sheet.getRow(PopulationGroupRow.NAME.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing population name");
      }

      PopulationGroup pg = new PopulationGroup();
      pg.populationName = getStringValue(sheet, PopulationGroupRow.NAME.num, Column.H);
      pg.targetPopulation = getStringValue(sheet, PopulationGroupRow.TARGET.num, Column.H);
      pg.populationSpan.addAll(getStringListValue(sheet, PopulationGroupRow.SPAN.num, Column.H));
      pg.populationDescription
          .addAll(getStringListValue(sheet, PopulationGroupRow.DESCRIPTION.num, Column.H));
      pg.populationAge.addAll(getStringListValue(sheet, PopulationGroupRow.AGE.num, Column.H));
      pg.populationGender = getStringValue(sheet, PopulationGroupRow.GENDER.num, Column.H);
      pg.bmi.addAll(getStringListValue(sheet, PopulationGroupRow.BMI.num, Column.H));
      pg.specialDietGroups
          .addAll(getStringListValue(sheet, PopulationGroupRow.SPECIAL_DIET_GROUPS.num, Column.H));
      pg.patternConsumption
          .addAll(getStringListValue(sheet, PopulationGroupRow.PATTERN_CONSUMPTION.num, Column.H));
      pg.region.addAll(getStringListValue(sheet, PopulationGroupRow.REGION.num, Column.H));
      pg.country.addAll(getStringListValue(sheet, PopulationGroupRow.COUNTRY.num, Column.H));
      pg.populationRiskFactor
          .addAll(getStringListValue(sheet, PopulationGroupRow.RISK_FACTOR.num, Column.H));
      pg.season.addAll(getStringListValue(sheet, PopulationGroupRow.SEASON.num, Column.H));

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
      if (sheet.getRow(StudyRow.ID.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing study identifier");
      }
      if (sheet.getRow(StudyRow.TITLE.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing study title");
      }

      Study study = new Study();
      study.id = getStringValue(sheet, StudyRow.ID.num, Column.H);
      study.title = getStringValue(sheet, StudyRow.TITLE.num, Column.H);
      study.description = getStringValue(sheet, StudyRow.DESCRIPTION.num, Column.H);
      study.designType = getStringValue(sheet, StudyRow.DESIGN_TYPE.num, Column.H);
      study.measurementType = getStringValue(sheet, StudyRow.ASSAY_MEASUREMENT_TYPE.num, Column.H);
      study.technologyType = getStringValue(sheet, StudyRow.ASSAY_TECHNOLOGY_TYPE.num, Column.H);
      study.technologyPlatform =
          getStringValue(sheet, StudyRow.ASSAY_TECHNOLOGY_PLATFORM.num, Column.H);
      study.accreditationProcedure =
          getStringValue(sheet, StudyRow.ACCREDITATION_PROCEDURE.num, Column.H);
      study.protocolName = getStringValue(sheet, StudyRow.PROTOCOL_NAME.num, Column.H);
      study.protocolType = getStringValue(sheet, StudyRow.PROTOCOL_TYPE.num, Column.H);
      study.protocolVersion = getStringValue(sheet, StudyRow.PROTOCOL_VERSION.num, Column.H);
      study.parametersName = getStringValue(sheet, StudyRow.PROTOCOL_PARAMETERS_NAME.num, Column.H);
      study.componentsName = getStringValue(sheet, StudyRow.PROTOCOL_COMPONENTS_NAME.num, Column.H);
      study.componentsType = getStringValue(sheet, StudyRow.PROTOCOL_COMPONENTS_TYPE.num, Column.H);

      return study;
    }

    static StudySample retrieveStudySample(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(StudySampleRow.NAME.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sample name");
      }
      if (sheet.getRow(StudySampleRow.SAMPLING_PLAN.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sampling plan");
      }
      if (sheet.getRow(StudySampleRow.SAMPLING_WEIGHT.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing sampling weight");
      }

      StudySample studySample = new StudySample();
      studySample.sample = getStringValue(sheet, StudySampleRow.NAME.num, Column.H);
      studySample.samplingStrategy =
          getStringValue(sheet, StudySampleRow.SAMPLING_STRATEGY.num, Column.H);
      studySample.samplingProgramType =
          getStringValue(sheet, StudySampleRow.SAMPLING_STRATEGY.num, Column.H);
      studySample.samplingMethod =
          getStringValue(sheet, StudySampleRow.SAMPLING_METHOD.num, Column.H);
      studySample.samplingWeight =
          getStringValue(sheet, StudySampleRow.SAMPLING_WEIGHT.num, Column.H);
      studySample.samplingSize = getStringValue(sheet, StudySampleRow.SAMPLING_SIZE.num, Column.H);
      studySample.lotSizeUnit = getStringValue(sheet, StudySampleRow.LOT_SIZE_UNIT.num, Column.H);
      studySample.samplingPoint =
          getStringValue(sheet, StudySampleRow.SAMPLING_POINT.num, Column.H);

      return studySample;
    }

    static DietaryAssessmentMethod retrieveDAM(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(DAMRow.METHOD_TOOL.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing methodological tool");
      }
      if (sheet.getRow(DAMRow.NON_CONSECUTIVE_1DAY.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing non consecutive one day");
      }

      DietaryAssessmentMethod dam = new DietaryAssessmentMethod();
      dam.collectionTool = getStringValue(sheet, DAMRow.METHOD_TOOL.num, Column.H);
      dam.numberOfNonConsecutiveOneDay =
          getNumericValue(sheet, DAMRow.NON_CONSECUTIVE_1DAY.num, Column.H).intValue();
      dam.softwareTool = getStringValue(sheet, DAMRow.DIETARY_SOFTWARE_TOOL.num, Column.H);
      dam.numberOfFoodItems.addAll(getStringListValue(sheet, DAMRow.FOOD_ITEMS.num, Column.H));
      dam.recordTypes.addAll(getStringListValue(sheet, DAMRow.RECORD_TYPE.num, Column.H));
      dam.foodDescriptors.addAll(getStringListValue(sheet, DAMRow.FOOD_DESCRIPTORS.num, Column.H));

      return dam;
    }

    static Laboratory retrieveLaboratory(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(LaboratoryRow.ACCREDITATION.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing laboratory accreditation");
      }

      Laboratory laboratory = new Laboratory();
      laboratory.accreditation = getStringValue(sheet, LaboratoryRow.ACCREDITATION.num, Column.H);
      laboratory.name = getStringValue(sheet, LaboratoryRow.NAME.num, Column.H);
      laboratory.country = getStringValue(sheet, LaboratoryRow.COUNTRY.num, Column.H);

      return laboratory;
    }

    static Assay retrieveAssay(XSSFSheet sheet) {

      // Check first mandatory properties
      if (sheet.getRow(AssayRow.NAME.num).getCell(Column.H.ordinal())
          .getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing assay name");
      }

      Assay assay = new Assay();
      assay.name = getStringValue(sheet, AssayRow.NAME.num, Column.H);
      assay.description = getStringValue(sheet, AssayRow.DESCRIPTION.num, Column.H);
      assay.moisturePercentage = getStringValue(sheet, AssayRow.MOIST_PERC.num, Column.H);
      assay.detectionLimit = getStringValue(sheet, AssayRow.DETECTION_LIMIT.num, Column.H);
      assay.quantificationLimit =
          getStringValue(sheet, AssayRow.QUANTIFICATION_LIMIT.num, Column.H);
      assay.leftCensoredData = getStringValue(sheet, AssayRow.LEFT_CENSORED_DATA.num, Column.H);
      assay.contaminationRange = getStringValue(sheet, AssayRow.CONTAMINATION_RANGE.num, Column.H);
      assay.uncertaintyValue = getStringValue(sheet, AssayRow.UNCERTAINTY_VALUE.num, Column.H);

      return assay;
    }

    static Parameter retrieveParameter(XSSFSheet sheet, int row) {

      // Check first mandatory properties
      if (sheet.getRow(row).getCell(Column.K.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter id");
      }
      if (sheet.getRow(row).getCell(Column.L.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter classification");
      }
      if (sheet.getRow(row).getCell(Column.M.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter name");
      }
      if (sheet.getRow(row).getCell(Column.P.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing parameter unit");
      }
      if (sheet.getRow(row).getCell(Column.Q.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing unit category");
      }
      if (sheet.getRow(row).getCell(Column.R.ordinal()).getCellType() == Cell.CELL_TYPE_BLANK) {
        throw new IllegalArgumentException("Missing data type");
      }

      Parameter param = new Parameter();
      param.id = getStringValue(sheet, row, Column.K);
      try {
        param.classification =
            Parameter.Classification.valueOf(getStringValue(sheet, row, Column.L));
      } catch (Exception exception) {
        throw new IllegalArgumentException(
            "Invalid parameter classification: " + getStringValue(sheet, row, Column.L));
      }
      param.name = getStringValue(sheet, row, Column.M);
      param.description = getStringValue(sheet, row, Column.N);
      param.type = getStringValue(sheet, row, Column.O);
      param.unit = getStringValue(sheet, row, Column.P);
      param.unitCategory = getStringValue(sheet, row, Column.Q);
      param.dataType = getStringValue(sheet, row, Column.R);
      param.source = getStringValue(sheet, row, Column.S);
      param.subject = getStringValue(sheet, row, Column.T);
      param.distribution = getStringValue(sheet, row, Column.U);
      param.value = getStringValue(sheet, row, Column.V);
      param.reference = getStringValue(sheet, row, Column.W);
      param.variabilitySubject = getStringValue(sheet, row, Column.X);
      param.modelApplicability.add(getStringValue(sheet, row, Column.Y));
      param.error = getNumericValue(sheet, row, Column.Z);

      return param;
    }
  }
}
