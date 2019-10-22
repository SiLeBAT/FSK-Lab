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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.util.FileUtil;
import de.bund.bfr.fskml.Script;
import de.bund.bfr.fskml.ScriptFactory;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import metadata.Assay;
import metadata.Contact;
import metadata.DataBackground;
import metadata.DietaryAssessmentMethod;
import metadata.GeneralInformation;
import metadata.Hazard;
import metadata.Laboratory;
import metadata.MetadataFactory;
import metadata.ModelCategory;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PopulationGroup;
import metadata.PreRakipSheetImporter;
import metadata.PublicationType;
import metadata.RAKIPSheetImporter;
import metadata.RakipColumn;
import metadata.RakipRow;
import metadata.Reference;
import metadata.Scope;
import metadata.StringObject;
import metadata.Study;
import metadata.StudySample;
import metadata.SwaggerConsumptionSheetImporter;
import metadata.SwaggerDataModelSheetImporter;
import metadata.SwaggerDoseResponseSheetImporter;
import metadata.SwaggerExposureSheetImporter;
import metadata.SwaggerGenericSheetImporter;
import metadata.SwaggerHealthModelSheetImporter;
import metadata.SwaggerOtherModelSheetImporter;
import metadata.SwaggerPredictiveModelSheetImporter;
import metadata.SwaggerProcessModelSheetImporter;
import metadata.SwaggerQraModelSheetImporter;
import metadata.SwaggerRiskModelSheetImporter;
import metadata.SwaggerSheetImporter;
import metadata.SwaggerToxicologicalSheetImporter;
import metadata.SwaggerUtil;

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

    CheckUtils.checkSourceFile(settings.getString("modelScript"));
    CheckUtils.checkSourceFile(settings.getString("spreadsheet"));

    // Sheet
    CheckUtils.checkArgument(StringUtils.isNotEmpty(settings.getString("sheet")), "Missing sheet");
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected void reset() {
  }

  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
      throws InvalidSettingsException, IOException, CanceledExecutionException,
      InvalidFormatException {

    // Reads model script
    Script modelRScript = readScript(nodeSettings.modelScript);
    
    // Reads visualization script
    Script vizRScript;
    if (StringUtils.isNotEmpty(nodeSettings.visualizationScript)) {
      vizRScript = readScript(nodeSettings.visualizationScript);
    } else {
      vizRScript = null;
    }

    Model modelMetadata = null;

    // If an input table is connected then parse the metadata
    if (inData.length == 1 && inData[0] != null) {
      // TODO: Need to update input table parser: TableParser
      // BufferedDataTable metadataTable = (BufferedDataTable) inData[0];
      //
      // // parse table
      // String[][] values = new String[200][50];
      // int i = 0;
      // for (DataRow row : metadataTable) {
      // if (isRowEmpty(row))
      // break;
      // int j = 0;
      // for (DataCell cell : row) {
      // values[i][j] = !cell.isMissing() ? ((StringCell) cell).getStringValue() : "";
      // j++;
      // }
      // i++;
      // }
      //
      // generalInformation = TableParser.retrieveGeneralInformation(values);
      // scope = TableParser.retrieveScope(values);
      // dataBackground = TableParser.retrieveDataBackground(values);
      //
      // modelMath = MetadataFactory.eINSTANCE.createModelMath();
      // for (i = 132; i <= 152; i++) {
      // try {
      // Parameter param = TableParser.retrieveParameter(values, i);
      // modelMath.getParameter().add(param);
      // } catch (Exception exception) {
      // exception.printStackTrace();
      // }
      // }
    }

    else {
      exec.checkCanceled();

      // Reads model meta data
      Workbook workbook = getWorkbook(nodeSettings.spreadsheet);
      workbook.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
      Sheet sheet = workbook.getSheet(nodeSettings.sheet);
      if(sheet.getPhysicalNumberOfRows() > 29) {
        // 1.0.3 RAKIP spreadsheet has "parameter type" P:131 (or 130 if index starts at 0), 1.04 doesn't
        
          if(sheet.getSheetName().equals("Generic Metadata Schema") && sheet.getRow(130).getCell(15).getStringCellValue().equals("Parameter type")) { //SWAGGER 1.04
            // Process 1.0.3 RAKIP spreadsheet has "parameter type" P:131 (or 130 if index starts at 0)
            RAKIPSheetImporter importer = new RAKIPSheetImporter();
            GenericModel gm = new GenericModel();
            gm.setModelType("genericModel");
            gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
            gm.setScope(importer.retrieveScope(sheet));
            gm.setDataBackground(importer.retrieveBackground(sheet));
            gm.setModelMath(importer.retrieveModelMath(sheet));

            modelMetadata = gm;
               
              
            
          }else { 
            //SWAGGER 1.0.4
            if(sheet.getSheetName().equals("Generic Metadata Schema") ) {
              SwaggerGenericSheetImporter importer = new SwaggerGenericSheetImporter();
              GenericModel gm = new GenericModel();
              gm.setModelType("genericModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }//end if generic
            
            //Dose Response Model
            if(sheet.getSheetName().equals("Dose-response Model") ) {
              SwaggerDoseResponseSheetImporter importer = new SwaggerDoseResponseSheetImporter();
              DoseResponseModel gm = new DoseResponseModel();
              gm.setModelType("doseResponseModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }//end if dose response
            
            // Predictive Model
            if(sheet.getSheetName().equals("Predictive Model") ) {
              SwaggerPredictiveModelSheetImporter importer = new SwaggerPredictiveModelSheetImporter();
              PredictiveModel gm = new PredictiveModel();
              gm.setModelType("predictiveModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Exposure Model
            if(sheet.getSheetName().equals("Exposure Model") ) {
              SwaggerExposureSheetImporter importer = new SwaggerExposureSheetImporter();
              ExposureModel gm = new ExposureModel();
              gm.setModelType("exposureModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Process Model
            if(sheet.getSheetName().equals("Process Model") ) {
              SwaggerProcessModelSheetImporter importer = new SwaggerProcessModelSheetImporter();
              ProcessModel gm = new ProcessModel();
              gm.setModelType("processModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Toxicological Model
            if(sheet.getSheetName().startsWith("Toxic") ) {
              SwaggerToxicologicalSheetImporter importer = new SwaggerToxicologicalSheetImporter();
              ToxicologicalModel gm = new ToxicologicalModel();
              gm.setModelType("toxicologicalModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // QRA Model
            if(sheet.getSheetName().equals("QRA Models") ) {
              SwaggerQraModelSheetImporter importer = new SwaggerQraModelSheetImporter();
              QraModel gm = new QraModel();
              gm.setModelType("qraModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Risk Model
            if(sheet.getSheetName().startsWith("Risk") ) {
              SwaggerRiskModelSheetImporter importer = new SwaggerRiskModelSheetImporter();
              RiskModel gm = new RiskModel();
              gm.setModelType("riskModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Other Model
            if(sheet.getSheetName().equals("Other Empirical Model") ) {
              SwaggerOtherModelSheetImporter importer = new SwaggerOtherModelSheetImporter();
              OtherModel gm = new OtherModel();
              gm.setModelType("otherModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Consumption Model
            if(sheet.getSheetName().startsWith("Consumption") ) {
              SwaggerConsumptionSheetImporter importer = new SwaggerConsumptionSheetImporter();
              ConsumptionModel gm = new ConsumptionModel();
              gm.setModelType("consumptionModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if         
            // Health Model
            if(sheet.getSheetName().startsWith("Health") ) {
              SwaggerHealthModelSheetImporter importer = new SwaggerHealthModelSheetImporter();
              HealthModel gm = new HealthModel();
              gm.setModelType("healthModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if     
            
            // Data Model
            if(sheet.getSheetName().equals("(Data)") ) {
              SwaggerDataModelSheetImporter importer = new SwaggerDataModelSheetImporter();
              DataModel gm = new DataModel();
              gm.setModelType("dataModel");
              gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
              gm.setScope(importer.retrieveScope(sheet));
              gm.setDataBackground(importer.retrieveBackground(sheet));
              gm.setModelMath(importer.retrieveModelMath(sheet));

              modelMetadata = gm;

            }  // end if     
          }//else RAKIP
         
         }//end if newer than 1.03
         else {
           // Process legacy spreadsheet: prior RAKIP
           PreRakipSheetImporter importer = new PreRakipSheetImporter();
           GenericModel gm = new GenericModel();
           gm.setModelType("genericModel");
           gm.setGeneralInformation(importer.retrieveGeneralInformation(sheet));
           gm.setScope(importer.retrieveScope(sheet));
           gm.setModelMath(importer.retrieveModelMath(sheet));
       
           modelMetadata = gm;
         }
       }//end if check version and create modelmetadata



    String modelScript = modelRScript.getScript();
    String vizScript = vizRScript != null ? vizRScript.getScript() : "";

    String workingDirectory = nodeSettings.getWorkingDirectory();

    // The creator imports a non-execute model without plot, thus the path to
    // the plot is an empty string.
    String plotPath = "";

    // Retrieve used libraries in scripts. A set is used to avoid duplication.
    exec.checkCanceled();
    Set<String> librariesSet = new HashSet<>();
    librariesSet.addAll(modelRScript.getLibraries());
    if (vizRScript != null) {
      librariesSet.addAll(vizRScript.getLibraries());
    }
    List<String> librariesList = new ArrayList<>(librariesSet);

    // Import readme
    exec.checkCanceled();
    String readmePath = nodeSettings.getReadme();
    String readme;
    if (readmePath.isEmpty()) {
      readme = "";
    } else {
      File readmeFile = FileUtil.getFileFromURL(FileUtil.toURL(readmePath));
      readme = FileUtils.readFileToString(readmeFile, "UTF-8");
    }

    final FskPortObject portObj = new FskPortObject(modelScript, vizScript, modelMetadata, null,
        librariesList, workingDirectory, plotPath, readme, nodeSettings.spreadsheet);

    List<Parameter> parameters = SwaggerUtil.getParameter(modelMetadata);
    if (SwaggerUtil.getModelMath(modelMetadata) != null) {
      portObj.simulations.add(NodeUtils.createDefaultSimulation(parameters));
    }

    // Validate parameters from spreadsheet
    exec.checkCanceled();
    try (ScriptHandler handler = ScriptHandler
        .createHandler(SwaggerUtil.getLanguageWrittenIn(modelMetadata), portObj.packages)) {
      if (!workingDirectory.isEmpty()) {
        Path workingDirectoryPath =
            FileUtil.getFileFromURL(FileUtil.toURL(workingDirectory)).toPath();
        handler.setWorkingDirectory(workingDirectoryPath, exec);
      }

      FskSimulation simulation = NodeUtils.createDefaultSimulation(parameters);
      String script = handler.buildParameterScript(simulation);
      // ScriptExecutor executor = new ScriptExecutor(controller);
      
        Arrays.stream(modelScript.split("\\r?\\n")).filter(id -> id.startsWith("import")).forEach(line -> {
          try {
            handler.runScript(line,exec,false);
            
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });//.map(id -> id.split(" ")[1]).forEach(libraries::add);
      
      handler.setupOutputCapturing(exec);
      handler.runScript(script, exec, false);
      handler.finishOutputCapturing(exec);

      if (!handler.getStdErr().isEmpty()) {
        throw new InvalidSettingsException("Invalid parameters:\n" + handler.getStdErr());
      }
    } catch (Exception exception) {
      throw new InvalidSettingsException(
          "Parameters could not be validate. Please try again. " + exception.getMessage(),
          exception);
    }

    return new PortObject[] {portObj};
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

    // Check spreadsheet and sheet in no input table is connected
    if (inSpecs.length == 1 && inSpecs[0] == null) {
      CheckUtils.checkArgument(
          nodeSettings.spreadsheet != null && !nodeSettings.spreadsheet.isEmpty(),
          "No spreadsheet provided! Please enter a valid location.");
      CheckUtils.checkSourceFile(nodeSettings.spreadsheet);
      CheckUtils.checkArgument(nodeSettings.sheet != null && !nodeSettings.sheet.isEmpty(),
          "No sheet provided");
    }

    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  /**
   * Reads R script.
   * 
   * @param path File path to R model script. If is assured not to be null or empty.
   * @throws InvalidSettingsException if {@link path} is null or whitespace.
   * @throws IOException if the file cannot be read.
   */
  private static Script readScript(final String path) throws InvalidSettingsException, IOException {
    String trimmedPath = StringUtils.trimToNull(path.trim());

    // path is not null or whitespace, thus try to read it
    try {
      // may throw IOException
      File fScript = FileUtil.getFileFromURL(FileUtil.toURL(trimmedPath));
      Script script = ScriptFactory.createScript(fScript);
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
      modelCategory.getModelSubClass().addAll(
          getStringObjectList(values, RakipRow.MODEL_CATEGORY__MODEL_SUB_CLASS, RakipColumn.I));
      modelCategory
          .setModelClassComment(values[RakipRow.MODEL_CATEGORY__MODEL_CLASS_COMMENT.num][columnI]);
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
        dataBackground.getStudySample().add(studySample);
      } catch (IllegalArgumentException exception) {
        // Ignore exception since the study sample is optional
      }

      try {
        DietaryAssessmentMethod method = retrieveDAM(values);
        dataBackground.getDietaryAssessmentMethod().add(method);
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
    static metadata.Parameter retrieveParameter(String[][] values, int row) {

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

      metadata.Parameter param = MetadataFactory.eINSTANCE.createParameter();
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

  /**
   * Taken from {@link org.knime.ext.poi.node.read2.XLSUserSettings}.
   * 
   * Opens and returns a new buffered input stream on the passed location. The location could either
   * be a filename or a URL.
   *
   * @param location a filename or a URL
   * @return a new opened buffered input stream.
   * @throws IOException
   * 
   */
  public static BufferedInputStream getBufferedInputStream(final String location)
      throws IOException {
    InputStream in;
    try {
      URL url = new URL(location);
      in = FileUtil.openStreamWithTimeout(url);
    } catch (MalformedURLException mue) {
      // then try a file
      in = new FileInputStream(location);
    }
    return new BufferedInputStream(in);

  }

  /**
   * Taken from {@link org.knime.ext.poi.node.read2.XLSTableSettings}.
   * 
   * Loads a workbook from the file system.
   *
   * @param path Path to the workbook
   * @return The workbook or null if it could not be loaded
   * @throws IOException
   * @throws InvalidFormatException
   * @throws RuntimeException the underlying POI library also throws other kind of exceptions
   */
  public static Workbook getWorkbook(final String path) throws IOException, InvalidFormatException {
    Workbook workbook = null;
    InputStream in = null;
    try {
      in = getBufferedInputStream(path);
      // This should be the only place in the code where a workbook gets loaded
      workbook = WorkbookFactory.create(in);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e2) {
          // ignore
        }
      }
    }
    return workbook;
  }
}
