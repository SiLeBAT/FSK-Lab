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
import org.rosuda.REngine.REXPMismatchException;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
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
import metadata.RAKIPSheetImporter;
import metadata.RakipColumn;
import metadata.RakipRow;
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
          RAKIPSheetImporter importer = new RAKIPSheetImporter();
          generalInformation = importer.retrieveGeneralInformation(sheet);
          scope = importer.retrieveScope(sheet);
          dataBackground = importer.retrieveDataBackground(sheet);
          modelMath = importer.retrieveModelMath(sheet);
        } else {
          // Process legacy spreadsheet
          generalInformation = LegacySheetImporter.getGeneralInformation(sheet);
          scope = LegacySheetImporter.getScope(sheet);
          dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
          modelMath = LegacySheetImporter.getModelMath(sheet);
        }
      } catch (IOException | InvalidFormatException e) {
        throw new InvalidSettingsException("Invalid metadata");
      }
    }

    String modelScript = modelRScript.getScript();
    String vizScript = vizRScript != null ? vizRScript.getScript() : "";

    String workingDirectory = nodeSettings.getWorkingDirectory();
    final FskPortObject portObj =
        new FskPortObject(modelScript, "", vizScript, generalInformation, scope,
            dataBackground, modelMath, null, new HashSet<>(), workingDirectory);
    if (modelMath != null) {
      portObj.simulations.add(NodeUtils.createDefaultSimulation(modelMath.getParameter()));
    }

    // libraries
    List<String> libraries = new ArrayList<>();
    libraries.addAll(modelRScript.getLibraries());
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

    static GeneralInformation retrieveGeneralInformation(String[][] values) {

      GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();

      String name = values[RakipRow.GENERAL_INFORMATION__NAME.num][RakipColumn.I.ordinal()];
      String source = values[RakipRow.GENERAL_INFORMATION__SOURCE.num][RakipColumn.I.ordinal()];
      String identifier =
          values[RakipRow.GENERAL_INFORMATION__IDENTIFIER.num][RakipColumn.I.ordinal()];
      String rights = values[RakipRow.GENERAL_INFORMATION__RIGHTS.num][RakipColumn.I.ordinal()];
      boolean isAvailable =
          values[RakipRow.GENERAL_INFORMATION__AVAILABILITY.num][RakipColumn.I.ordinal()]
              .equals("Yes");
      String language = values[RakipRow.GENERAL_INFORMATION__LANGUAGE.num][RakipColumn.I.ordinal()];
      String software = values[RakipRow.GENERAL_INFORMATION__SOFTWARE.num][RakipColumn.I.ordinal()];
      String languageWrittenIn =
          values[RakipRow.GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN.num][RakipColumn.I.ordinal()];
      String status = values[RakipRow.GENERAL_INFORMATION__STATUS.num][RakipColumn.I.ordinal()];
      String objective =
          values[RakipRow.GENERAL_INFORMATION__OBJECTIVE.num][RakipColumn.I.ordinal()];
      String description =
          values[RakipRow.GENERAL_INFORMATION__DESCRIPTION.num][RakipColumn.I.ordinal()];

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
      if (values[RakipRow.MODEL_CATEGORY__MODEL_CLASS.num][columnI].isEmpty()) {
        throw new IllegalArgumentException("Missing model class");
      }

      ModelCategory modelCategory = MetadataFactory.eINSTANCE.createModelCategory();
      modelCategory.setModelClass(values[RakipRow.MODEL_CATEGORY__MODEL_CLASS.num][columnI]);
      modelCategory.getModelSubClass()
          .addAll(getStringObjectList(values, RakipRow.MODEL_CATEGORY__MODEL_SUB_CLASS, RakipColumn.I));
      modelCategory.setModelClassComment(values[RakipRow.MODEL_CATEGORY__MODEL_CLASS_COMMENT.num][columnI]);
      modelCategory.setBasicProcess(values[RakipRow.MODEL_CATEGORY__BASIC_PROCESS.num][columnI]);

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
        scope.getPopulationGroup().add(populationGroup);
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
        DietaryAssessmentMethod method = retrieveDAM(values);
        dataBackground.getDietaryassessmentmethod().add(method);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the dietary assessment method is optional
      }

      try {
        Laboratory laboratory = retrieveLaboratory(values);
        dataBackground.getLaboratory().add(laboratory);
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
