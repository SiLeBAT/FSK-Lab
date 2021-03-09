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
package de.bund.bfr.knime.fsklab.v2_0.editor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.interactive.ViewRequestHandlingException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.JSONViewRequestHandler;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.editor.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.rakip.vocabularies.data.AccreditationProcedureRepository;
import de.bund.bfr.rakip.vocabularies.data.AvailabilityRepository;
import de.bund.bfr.rakip.vocabularies.data.BasicProcessRepository;
import de.bund.bfr.rakip.vocabularies.data.CollectionToolRepository;
import de.bund.bfr.rakip.vocabularies.data.CountryRepository;
import de.bund.bfr.rakip.vocabularies.data.FishAreaRepository;
import de.bund.bfr.rakip.vocabularies.data.FormatRepository;
import de.bund.bfr.rakip.vocabularies.data.HazardRepository;
import de.bund.bfr.rakip.vocabularies.data.HazardTypeRepository;
import de.bund.bfr.rakip.vocabularies.data.IndSumRepository;
import de.bund.bfr.rakip.vocabularies.data.LaboratoryAccreditationRepository;
import de.bund.bfr.rakip.vocabularies.data.LanguageRepository;
import de.bund.bfr.rakip.vocabularies.data.LanguageWrittenInRepository;
import de.bund.bfr.rakip.vocabularies.data.ModelClassRepository;
import de.bund.bfr.rakip.vocabularies.data.ModelEquationClassRepository;
import de.bund.bfr.rakip.vocabularies.data.ModelSubclassRepository;
import de.bund.bfr.rakip.vocabularies.data.PackagingRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterClassificationRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterDatatypeRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterDistributionRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterSourceRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterSubjectRepository;
import de.bund.bfr.rakip.vocabularies.data.PopulationRepository;
import de.bund.bfr.rakip.vocabularies.data.ProductMatrixRepository;
import de.bund.bfr.rakip.vocabularies.data.ProductTreatmentRepository;
import de.bund.bfr.rakip.vocabularies.data.ProductionMethodRepository;
import de.bund.bfr.rakip.vocabularies.data.PublicationStatusRepository;
import de.bund.bfr.rakip.vocabularies.data.PublicationTypeRepository;
import de.bund.bfr.rakip.vocabularies.data.RegionRepository;
import de.bund.bfr.rakip.vocabularies.data.RightRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingMethodRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingPointRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingProgramRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingStrategyRepository;
import de.bund.bfr.rakip.vocabularies.data.SoftwareRepository;
import de.bund.bfr.rakip.vocabularies.data.SourceRepository;
import de.bund.bfr.rakip.vocabularies.data.StatusRepository;
import de.bund.bfr.rakip.vocabularies.data.TechnologyTypeRepository;
import de.bund.bfr.rakip.vocabularies.data.UnitCategoryRepository;
import de.bund.bfr.rakip.vocabularies.data.UnitRepository;
import de.bund.bfr.rakip.vocabularies.domain.FskmlObject;
import metadata.SwaggerUtil;


/**
 * Fsk Editor JS node model.
 */
final class FSKEditorJSNodeModel
    extends AbstractWizardNodeModel<FSKEditorJSViewRepresentation, FSKEditorJSViewValue>
    implements JSONViewRequestHandler<FSKEditorJSViewRequest, FSKEditorJSViewResponse>, PortObjectHolder {

  private final FSKEditorJSConfig m_config = new FSKEditorJSConfig();
  private FskPortObject m_port;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME = new FSKEditorJSNodeFactory().getInteractiveViewName();

  static final AtomicLong TEMP_DIR_UNIFIER = new AtomicLong((int) (100000 * Math.random()));
  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  public FSKEditorJSNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public FSKEditorJSViewRepresentation createEmptyViewRepresentation() {
    FSKEditorJSViewRepresentation representation = super.getViewRepresentation();
    if (representation == null) {
      representation = new FSKEditorJSViewRepresentation();
      representation.setServicePort(FskPlugin.getDefault().fskService.getPort());
      representation.setControlledVocabularyURL(PreferenceInitializer.getControlledVocabularyURL());
    }
    return representation;
  }

  @Override
  public FSKEditorJSViewValue createEmptyViewValue() {
    return new FSKEditorJSViewValue();
  }

  @Override
  public FSKEditorJSViewValue getViewValue() {

    FSKEditorJSViewValue value = super.getViewValue();

    synchronized (getLock()) {

      final String connectedNodeId = getTableId(0);
      
      if (value.isEmpty()) {
        copyConfigToView(value);
      }
      
      if (value.isEmpty() && m_port != null) {
        copyConnectedNodeToView(connectedNodeId, value);
      }
      
      if (value.getModelMetaData() == null && value.getModelType() == null) {
        Model metadata = NodeUtils.initializeModel(ModelType.genericModel);
        try {
          m_config.setModelMetaData(MAPPER.writeValueAsString(metadata));
          m_config.setModelType(ModelType.genericModel.name());
        } catch (JsonProcessingException e) {
        }
      }
      
      if (value.getModelScript() == null) {
        value.setModelScript("");
      }
      
      if (value.getVisualizationScript() == null) {
        value.setVisualizationScript("");
      }
    }

    return value;
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.v2.0.editor.component";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(FSKEditorJSViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {

    if (inObjects[0] != null) {
      setInternalPortObjects(inObjects);
    }

    // Create port object
    Model metadata = null;
    Model originalMetadata = null;
    String readme = "";
    List<String> packages = Collections.emptyList();
    List<FskSimulation> simulations = Collections.emptyList();
    String modelScript = "";
    String visualizationScript = "";

    Optional<EnvironmentManager> environmentManager;   
    if (m_config.getEnvironmentManager() != null) {
      environmentManager = Optional.of(m_config.getEnvironmentManager());
    } else if (inObjects[0] != null) {
      environmentManager = ((FskPortObject) inObjects[0]).getEnvironmentManager();
    } else {
      environmentManager = Optional.empty();
    }

    synchronized (getLock()) {
      FSKEditorJSViewValue viewValue = getViewValue();
      FSKEditorJSViewRepresentation viewRep = createEmptyViewRepresentation();

      // If executed
      if (viewValue.isCompleted()) {
        setWarningMessage("Output Parameters are not configured correctly");
      }

      final String[] validationErrors = viewValue.getValidationErrors();
      if (validationErrors != null && validationErrors.length > 0) {
        for (String error : validationErrors) {
          setWarningMessage(error);
        }
      }

      String jsonMetadata = viewValue.getModelMetaData();
      if (jsonMetadata != null && !jsonMetadata.isEmpty()) {
        // Get model type from metadata
        JsonNode metadataNode = MAPPER.readTree(viewValue.getModelMetaData());
        String modelType = metadataNode.get("modelType").asText("genericModel");

        // Deserialize metadata to concrete class according to modelType
        Class<? extends Model> modelClass = SwaggerUtil.modelClasses.get(modelType);
        metadata = MAPPER.readValue(viewValue.getModelMetaData(), modelClass);
        if(!StringUtils.isEmpty(viewRep.getModelMetadata()))
          originalMetadata = MAPPER.readValue(viewRep.getModelMetadata(), modelClass);
        else
          viewRep.setModelMetadata(jsonMetadata);
      }
      if(StringUtils.isEmpty(viewRep.getModelMetadata())) {
          viewRep.setModelMetadata("");
      }
      boolean regenerateSimulation = false;
      // Take parameters from view value (metadata)
      List<Parameter> parameters = new ArrayList<>();
      if(metadata != null)
        parameters = SwaggerUtil.getParameter(metadata);
      // Take original parameters from view representation (metadata)
      List<Parameter> originalParameters = new ArrayList<>();
      if(originalMetadata != null)
        originalParameters = SwaggerUtil.getParameter(originalMetadata);
      
      if(!parameters.equals(originalParameters) || m_port == null){
        regenerateSimulation = true;
      }
      
      // Take simulation from view value otherwise from input port (if connected)
      if (metadata != null && SwaggerUtil.getModelMath(metadata) != null
          && SwaggerUtil.getParameter(metadata) != null && regenerateSimulation)  {

        // 1. Create new default simulation out of the view value
        FskSimulation newDefaultSimulation = NodeUtils.createDefaultSimulation(parameters);

        // 2. Assign newDefaultSimulation
        simulations = Arrays.asList(newDefaultSimulation);
      } else if (m_port != null) {
        simulations = m_port.simulations;
      }

      modelScript = StringUtils.defaultString(viewValue.getModelScript(), "");
      visualizationScript = StringUtils.defaultString(viewValue.getVisualizationScript(), "");
      readme = StringUtils.defaultString(viewValue.getReadme(), "");

      // resources files via fskEditorProxyValue will be available only in online mode of the
      // editor
//      if (viewValue.getResourcesFiles() != null && viewValue.getResourcesFiles().length > 0) {
//        resources = viewValue.getResourcesFiles();
//      }

      // Collect R packages
      final Set<String> librariesSet = new HashSet<>();
      librariesSet.addAll(new RScript(modelScript).getLibraries());
      librariesSet.addAll(new RScript(visualizationScript).getLibraries());
      packages = new ArrayList<>(librariesSet);
    }

    // TODO: Support resources upload
//    if (resources.length > 0) {
//      for (String fileRequestString : resources) {
//        downloadFileToWorkingDir(fileRequestString, workingDirectory);
//      }
//      // delete the parent folder of the uploaded files after moving them to the working
//      // directory. parentFolderPath is always uses KNIME protocol
//      String firstFile = resources[0];
//      String parentFolderPath = firstFile.substring(0, firstFile.lastIndexOf("/"));
//      BundleContext ctx = FrameworkUtil.getBundle(IRemoteFileUtilsService.class).getBundleContext();
//      ServiceReference<IRemoteFileUtilsService> ref =
//          ctx.getServiceReference(IRemoteFileUtilsService.class);
//      if (ref != null) {
//        try {
//          ctx.getService(ref).delete(new URL(parentFolderPath));
//        } finally {
//          ctx.ungetService(ref);
//        }
//      }
//    }

    FskPortObject outputPort = new FskPortObject(environmentManager, readme, packages);
    outputPort.setModel(modelScript);
    outputPort.setViz(visualizationScript);
    if (!simulations.isEmpty()) {
      outputPort.simulations.addAll(simulations);
    }

    outputPort.modelMetadata =
        metadata != null ? metadata : NodeUtils.initializeModel(ModelType.genericModel);

    return new PortObject[] {outputPort};
  }

  @Override
  protected void performReset() {
    m_port = null;
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    m_config.saveSettings(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    new FSKEditorJSConfig().loadSettings(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    m_config.loadSettings(settings);
  }

  @Override
  protected void useCurrentValueAsDefault() {
    synchronized (getLock()) {
      copyValueToConfig();
    }
  }

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {m_port};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    m_port = (FskPortObject) portObjects[0];
  }

  public void setHideInWizard(boolean hide) {
  }

  private void copyConfigToView(FSKEditorJSViewValue value) {
    value.setModelMetaData(m_config.getModelMetaData());
    value.setModelScript(m_config.getModelScript());
    value.setVisualizationScript(m_config.getVisualizationScript());
    value.setReadme(m_config.getReadme());
    value.setEnvironment(m_config.getEnvironmentManager());
    value.setServerName(m_config.getServerName());
    value.setCompleted(m_config.isCompleted());
    value.setValidationErrors(m_config.getValidationErrors());
    value.setModelType(m_config.getModelType());
  }

  private void copyValueToConfig() {
    FSKEditorJSViewValue value = getViewValue();
    m_config.setModelMetaData(value.getModelMetaData());
    m_config.setModelScript(value.getModelScript());
    m_config.setVisualizationScript(value.getVisualizationScript());
    m_config.setReadmeFile(value.getReadme());
    m_config.setEnvironmentManager(value.getEnvironment());
    m_config.setServerName(value.getServerName());
    m_config.setCompleted(value.isCompleted());
    m_config.setValidationErrors(value.getValidationErrors());
    m_config.setModelType(value.getModelType());
  }

  /**
   * Copy the model information from a connected node to the view.
   * 
   * @param connectedNodeId UUID of the connected node
   */
  private void copyConnectedNodeToView(String connectedNodeId, FSKEditorJSViewValue value) {

    try {
      String jsonMetadata = MAPPER.writeValueAsString(m_port.modelMetadata);
      value.setModelMetaData(jsonMetadata);
    } catch (JsonProcessingException e) {
    }

    value.setModelScript(m_port.getModel());
    value.setVisualizationScript(m_port.getViz());
    value.setReadme(m_port.getReadme());
    value.setModelType(m_port.modelMetadata.getModelType());
    m_port.getEnvironmentManager().ifPresent(value::setEnvironment);
    
    // Cannot assign server name, completed and validation errors
  }
  
  @Override
  public FSKEditorJSViewResponse handleRequest(FSKEditorJSViewRequest request, ExecutionMonitor exec)
          throws ViewRequestHandlingException, InterruptedException, CanceledExecutionException {

      final FSKEditorJSViewResponse response = new FSKEditorJSViewResponse(request);
      
      try {
        Class.forName("org.h2.Driver");
      } catch (ClassNotFoundException exception) {
        return response;
      }
      
      try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {
        if (!StringUtils.isEmpty(request.getVocabularyName())) {
          
          FskmlObject[] items;
          switch (request.getVocabularyName()) {
            case "accreditation_procedure":
              items = new AccreditationProcedureRepository(connection).getAll();
              break;
            case "availability":
              items = new AvailabilityRepository(connection).getAll();
              break;
            case "basic_process":
              items = new BasicProcessRepository(connection).getAll();
              break;
            case "collection_tool":
              items = new CollectionToolRepository(connection).getAll();
              break;
            case "country":
              items = new CountryRepository(connection).getAll();
              break;
            case "fish_area":
              items = new FishAreaRepository(connection).getAll();
              break;
            case "format":
              items = new FormatRepository(connection).getAll();
              break;
            case "hazard":
              items = new HazardRepository(connection).getAll();
              break;
            case "hazard_type":
              items = new HazardTypeRepository(connection).getAll();
              break;
            case "ind_sum":
              items = new IndSumRepository(connection).getAll();
              break;
            case "laboratory_accreditation":
              items = new LaboratoryAccreditationRepository(connection).getAll();
              break;
            case "language":
              items = new LanguageRepository(connection).getAll();
              break;
            case "language_written_in":
              items = new LanguageWrittenInRepository(connection).getAll();
              break;
            case "model_class":
              items = new ModelClassRepository(connection).getAll();
              break;
            case "model_equation_class":
              items = new ModelEquationClassRepository(connection).getAll();
              break;
            case "model_subclass":
              items = new ModelSubclassRepository(connection).getAll();
              break;
            case "packaging":
              items = new PackagingRepository(connection).getAll();
              break;
            case "parameter_classification":
              items = new ParameterClassificationRepository(connection).getAll();
              break;
            case "parameter_datatype":
              items = new ParameterDatatypeRepository(connection).getAll();
              break;
            case "parameter_distribution":
              items = new ParameterDistributionRepository(connection).getAll();
              break;
            case "parameter_source":
              items = new ParameterSourceRepository(connection).getAll();
              break;
            case "parameter_subject":
              items = new ParameterSubjectRepository(connection).getAll();
              break;
            case "population":
              items = new PopulationRepository(connection).getAll();
              break;
            case "product_matrix":
              items = new ProductMatrixRepository(connection).getAll();
              break;
            case "product_treatment":
              items = new ProductTreatmentRepository(connection).getAll();
              break;
            case "production_method":
              items = new ProductionMethodRepository(connection).getAll();
              break;
            case "publication_status":
              items = new PublicationStatusRepository(connection).getAll();
              break;
            case "publication_type":
              items = new PublicationTypeRepository(connection).getAll();
              break;
            case "region":
              items = new RegionRepository(connection).getAll();
              break;
            case "right":
              items = new RightRepository(connection).getAll();
              break;
            case "sampling_method":
              items = new SamplingMethodRepository(connection).getAll();
              break;
            case "sampling_point":
              items = new SamplingPointRepository(connection).getAll();
              break;
            case "sampling_program":
              items = new SamplingProgramRepository(connection).getAll();
              break;
            case "sampling_strategy":
              items = new SamplingStrategyRepository(connection).getAll();
              break;
            case "software":
              items = new SoftwareRepository(connection).getAll();
              break;
            case "source":
              items = new SourceRepository(connection).getAll();
              break;
            case "status":
              items = new StatusRepository(connection).getAll();
              break;
            case "unit":
              items = new UnitRepository(connection).getAll();
              break;
            case "unit_category":
              items = new UnitCategoryRepository(connection).getAll();
              break;
            case "technology_type":
              items = new TechnologyTypeRepository(connection).getAll();
              break;
            default:
              items = new FskmlObject[0];
          } // end-switch
          response.setVocabularyItems(items);
        } // end-if
      } // end-try
      catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return response;
  }

  @Override
  public FSKEditorJSViewRequest createEmptyViewRequest() {
      return new FSKEditorJSViewRequest();
  }
}
