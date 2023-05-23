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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import org.knime.core.util.IRemoteFileUtilsService;
import org.knime.js.core.node.AbstractWizardNodeModel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.AddedFilesEnvironmentManager;
import de.bund.bfr.knime.fsklab.preferences.PythonPreferences;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.ExistingEnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.editor.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.bund.bfr.metadata.swagger.Reference;
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
import metadata.ConversionUtils;
import metadata.SwaggerUtil;


/**
 * Fsk Editor JS node model.
 */
final class FSKEditorJSNodeModel
    extends AbstractWizardNodeModel<FSKEditorJSViewRepresentation, FSKEditorJSViewValue>
    implements PortObjectHolder {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FSKEditorJSNodeModel.class);
  
  private final FSKEditorJSConfig m_config = new FSKEditorJSConfig();
  private FskPortObject m_port;

  private boolean isRepresentationResetReuired;

  private boolean resetDone;

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
      Map<String, String[]> vocabularies;
      try {
        Class.forName("org.h2.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {

          vocabularies = new HashMap<>();
          vocabularies.put("accreditation_procedure", new AccreditationProcedureRepository(connection).getAllNames());
          vocabularies.put("availability", new AvailabilityRepository(connection).getAllNames());
          vocabularies.put("basic_process", new BasicProcessRepository(connection).getAllNames());
          vocabularies.put("collection_tool", new CollectionToolRepository(connection).getAllNames());
          vocabularies.put("country", new CountryRepository(connection).getAllNames());
          vocabularies.put("fish_area", new FishAreaRepository(connection).getAllNames());
          vocabularies.put("format", new FormatRepository(connection).getAllNames());
          vocabularies.put("hazard", new HazardRepository(connection).getAllNames());
          vocabularies.put("hazard_type", new HazardTypeRepository(connection).getAllNames());
          vocabularies.put("ind_sum", new IndSumRepository(connection).getAllNames());
          vocabularies.put("laboratory_accreditation", new LaboratoryAccreditationRepository(connection).getAllNames());
          vocabularies.put("language", new LanguageRepository(connection).getAllNames());
          vocabularies.put("language_written_in", new LanguageWrittenInRepository(connection).getAllNames());
          vocabularies.put("model_class", new ModelClassRepository(connection).getAllNames());
          vocabularies.put("model_equation_class", new ModelEquationClassRepository(connection).getAllNames());
          vocabularies.put("model_subclass", new ModelSubclassRepository(connection).getAllNames());
          vocabularies.put("packaging", new PackagingRepository(connection).getAllNames());
          vocabularies.put("parameter_classification", new ParameterClassificationRepository(connection).getAllNames());
          vocabularies.put("parameter_datatype", new ParameterDatatypeRepository(connection).getAllNames());
          vocabularies.put("parameter_distribution", new ParameterDistributionRepository(connection).getAllNames());
          vocabularies.put("parameter_source", new ParameterSourceRepository(connection).getAllNames());
          vocabularies.put("parameter_subject", new ParameterSubjectRepository(connection).getAllNames());
          vocabularies.put("population", new PopulationRepository(connection).getAllNames());
          vocabularies.put("product_matrix", new ProductMatrixRepository(connection).getAllNames());
          vocabularies.put("product_treatment", new ProductTreatmentRepository(connection).getAllNames());
          vocabularies.put("production_method", new ProductionMethodRepository(connection).getAllNames());
          vocabularies.put("publication_status", new PublicationStatusRepository(connection).getAllNames());
          vocabularies.put("publication_type", new PublicationTypeRepository(connection).getAllNames());
          vocabularies.put("region", new RegionRepository(connection).getAllNames());
          vocabularies.put("right", new RightRepository(connection).getAllNames());
          vocabularies.put("sampling_method", new SamplingMethodRepository(connection).getAllNames());
          vocabularies.put("sampling_point", new SamplingPointRepository(connection).getAllNames());
          vocabularies.put("sampling_program", new SamplingProgramRepository(connection).getAllNames());
          vocabularies.put("sampling_strategy", new SamplingStrategyRepository(connection).getAllNames());
          vocabularies.put("software", new SoftwareRepository(connection).getAllNames());
          vocabularies.put("source", new SourceRepository(connection).getAllNames());
          vocabularies.put("status", new StatusRepository(connection).getAllNames());
          vocabularies.put("unit", new UnitRepository(connection).getAllNames());
          vocabularies.put("unit_category", new UnitCategoryRepository(connection).getAllNames());
          vocabularies.put("technology_type", new TechnologyTypeRepository(connection).getAllNames());
        } catch (SQLException err) {
          LOGGER.warn("Error accessing vocabularies database", err);
          vocabularies = Collections.emptyMap();
        }
      } catch (ClassNotFoundException err) {
        LOGGER.warn("H2 database driver is missing", err);
        vocabularies = Collections.emptyMap();
      }
      
      representation.setVocabularies(vocabularies);
    } // if (representation == null)
    
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
    if(!StringUtils.isEmpty(value.getModelMetaData())) {
      try {
        value.setModelMetaData(migrateReferenceDateToYear(value.getModelMetaData()));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return value;
  }
  
  public String migrateReferenceDateToYear(String metadataString) throws IOException {
      
      JsonNode metadataNode = MAPPER.readTree(metadataString);
      
      String modelType = metadataNode.get("modelType").asText("genericModel");

      // Deserialize metadata to concrete class according to modelType
      Class<? extends Model> modelClass = SwaggerUtil.modelClasses.get(modelType);
      
      Model metadata = MAPPER.readValue(metadataString, modelClass);
      List<Reference> references = NodeUtils.getReferenceList(modelType, metadata);
      references.forEach(reference -> {
        if(reference.getDate() != null && !reference.getDate().trim().equals("-"))
          reference.setDate(reference.getDate().split("[-,]")[0]);
      });
      return MAPPER.writeValueAsString(metadata);
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
    String portObjectModelType = "";
    if (inObjects[0] != null) {
      setInternalPortObjects(inObjects);
      portObjectModelType = ((FskPortObject)inObjects[0]).modelMetadata.getModelType();
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
    if (m_config.getEnvironmentManager() != null && m_config.getEnvironmentManager().getEnvironment().isPresent()) {
      environmentManager = Optional.of(m_config.getEnvironmentManager());
    } else if (inObjects[0] != null) {
      environmentManager = ((FskPortObject) inObjects[0]).getEnvironmentManager();
    } else {
      environmentManager = Optional.empty();
    }

    synchronized (getLock()) {
      FSKEditorJSViewValue viewValue = getViewValue();
      FSKEditorJSViewRepresentation viewRep = createEmptyViewRepresentation();
      // resources files via fskEditorProxyValue will be available only in the online mode of the JS
      // editor
      if (viewValue.getResourcesFiles() != null
          && viewValue.getResourcesFiles().length != 0) {
        FskPortObject fskObj= ((FskPortObject)inObjects[0]);
        Optional<Path> workingDirectory;
        if (fskObj != null &&fskObj.getEnvironmentManager().isPresent()) {
          workingDirectory = fskObj.getEnvironmentManager().get().getEnvironment();
        } else {
          workingDirectory = Optional.of(Files.createTempDirectory(Paths.get(PythonPreferences.getFSKWorkingDirectoryPath()),"workingDirectory"));
        }
        environmentManager = Optional.of(new ExistingEnvironmentManager(workingDirectory.get().toString()));
        for (String fileRequestString : viewValue.getResourcesFiles()) {
          downloadFileToWorkingDir(fileRequestString, workingDirectory.get().toString());
        }
      }
      // delete the parent folder of the uploaded files after moving them to the working
      // directory.
      // parentFolderPath is always uses KNIME protocol

      if(!StringUtils.isBlank(viewValue.getParentResourcesFolder())){
        BundleContext ctx =
            FrameworkUtil.getBundle(IRemoteFileUtilsService.class).getBundleContext();
        ServiceReference<IRemoteFileUtilsService> ref =
            ctx.getServiceReference(IRemoteFileUtilsService.class);
        if (ref != null) {
          try {
            ctx.getService(ref).delete(new URL(viewValue.getParentResourcesFolder()));
          } finally {
            ctx.ungetService(ref);
          }
        }
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
        //metadata = MAPPER.readValue(viewValue.getModelMetaData(), modelClass);
        metadata =  new ConversionUtils().convertModel(MAPPER.readTree(viewValue.getModelMetaData()),
            ConversionUtils.ModelClass.valueOf(modelType));
        if(!StringUtils.isEmpty(viewRep.getModelMetadata()) && !viewValue.isModeTypeChanged()) {
          JsonNode repMetadataNode = MAPPER.readTree(viewRep.getModelMetadata());
          String repModelType = repMetadataNode.get("modelType").asText("genericModel");
          if(m_config.getModelType() != null &&  !m_config.getModelType().equalsIgnoreCase(repModelType)) {
            
            metadata =  new ConversionUtils().convertModel(MAPPER.readTree(viewRep.getModelMetadata()),
                  ConversionUtils.ModelClass.valueOf(m_config.getModelType()));
         // workaround only for DoseResponseModel because the model name attribute is called
            // modelName unlike other models.
            if (metadata instanceof DoseResponseModel) {
              String modelName = repMetadataNode.get("generalInformation").get("name").asText("");;
              SwaggerUtil.setModelName(metadata, modelName);
            }
            if (repModelType.equalsIgnoreCase("doseResponseModel")) {
              String modelName = repMetadataNode.get("generalInformation").get("modelName").asText("");;
              SwaggerUtil.setModelName(metadata, modelName);
            }
            //update representation metadata
            viewRep.setModelMetadata(MAPPER.writeValueAsString(metadata));
            originalMetadata = metadata;
            //update view value metadata from representation after converting model metadata
            viewValue.setModelMetaData(MAPPER.writeValueAsString(metadata));
            viewValue.setModelScript(viewRep.getModelScript());
            viewValue.setVisualizationScript(viewRep.getVisScript());
            viewValue.setReadme(viewRep.getReadme());
          }
          else {
            //metadata = MAPPER.readValue(viewValue.getModelMetaData(), modelClass);
            
            if(!StringUtils.isEmpty(portObjectModelType) && !portObjectModelType.equalsIgnoreCase(modelType)) {
              originalMetadata = ((FskPortObject)inObjects[0]).modelMetadata;
            }else {
              originalMetadata = MAPPER.readValue(viewRep.getModelMetadata(), modelClass);
            }
            
            if(!viewValue.isCompleted()) {
              viewValue.setModelMetaData(MAPPER.writeValueAsString(metadata));
              viewValue.setModelScript(viewRep.getModelScript());
              viewValue.setVisualizationScript(viewRep.getVisScript());
              viewValue.setReadme(viewRep.getReadme());
            }else {
              //update representation for following model type changes
              viewRep.setModelMetadata(MAPPER.writeValueAsString(metadata));
              viewRep.setModelScript(viewValue.getModelScript());
              viewRep.setVisScript(viewValue.getVisualizationScript());
              viewRep.setReadme(viewValue.getReadme());
            }
          }
        }
        else {
          if(!StringUtils.isEmpty(portObjectModelType)
              && !portObjectModelType.equalsIgnoreCase(modelType) 
              && !viewValue.isCompleted()) {
            String json = MAPPER.writeValueAsString(((FskPortObject)inObjects[0]).modelMetadata);
            JsonNode portMetadata = MAPPER.readTree(json);    
            metadata = new ConversionUtils().convertModel(portMetadata,
                ConversionUtils.ModelClass.valueOf(m_config.getModelType()));
            // workaround only for DoseResponseModel because the model name attribute is called
            // modelName unlike other models.
//            if (metadata instanceof DoseResponseModel) {
              String modelName =
                  SwaggerUtil.getModelName(((FskPortObject) inObjects[0]).modelMetadata);
              SwaggerUtil.setModelName(metadata, modelName);
//            }
            copyConnectedNodeToView("", viewValue);
            viewRep.setModelMetadata(MAPPER.writeValueAsString(metadata));
            viewValue.setModelMetaData(MAPPER.writeValueAsString(metadata));
          }else {
            viewRep.setModelMetadata(MAPPER.writeValueAsString(metadata));
            originalMetadata = metadata;
            viewRep.setModelScript(viewValue.getModelScript());
            viewRep.setVisScript(viewValue.getVisualizationScript());
            viewRep.setReadme(viewValue.getReadme());
          }
        }
      }
      if(StringUtils.isEmpty(viewRep.getModelMetadata())) {
          viewRep.setModelMetadata("");
      }
      // Take parameters from view value (metadata)
      List<Parameter> parameters = new ArrayList<>();
      if(metadata != null)
        parameters = SwaggerUtil.getParameter(metadata);
      // Take original parameters from view representation (metadata)
      List<Parameter> originalParameters = new ArrayList<>();
      if(!viewValue.isModeTypeChanged()) {
         if(originalMetadata != null )
            originalParameters = SwaggerUtil.getParameter(originalMetadata);
      }
      // Take simulation from input port (if connected) otherwise from view value. 
      if (m_port != null) {
        //clone to clean the port object simulations safely later.
        simulations = new ArrayList<>(m_port.simulations);
        if(originalParameters != null)
          checkAndRegenerateSimulation(simulations, originalParameters, parameters);

      } else if (metadata != null && SwaggerUtil.getModelMath(metadata) != null
          && SwaggerUtil.getParameter(metadata) != null)  {

        // 1. Create new default simulation out of the view value
        FskSimulation newDefaultSimulation = NodeUtils.createDefaultSimulation(parameters);

        // 2. Assign newDefaultSimulation
        simulations = Arrays.asList(newDefaultSimulation);
      } else if(metadata == null) {

        // Make sure at least a default simulation is present at any time, even if editor view
        // is not opened
        FskSimulation newDefaultSimulation = NodeUtils.createDefaultSimulation(parameters);
        simulations = Arrays.asList(newDefaultSimulation);

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
      
      if(m_port instanceof CombinedFskPortObject) {
        viewRep.setCombinedObject(true);
      }
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
    
    // Add new files (if there are any) to Model 
    
    Optional<EnvironmentManager> manager = environmentManager;
//    if(m_config.getAddedFiles() != null && m_config.getAddedFiles().length > 0) {
//      if(environmentManager.isPresent())
//        manager = Optional.of(new AddedFilesEnvironmentManager(environmentManager.get(), m_config.getAddedFiles()));
//      else
//        manager = Optional.of(new AddedFilesEnvironmentManager(m_config.getAddedFiles()));
//    }
    FskPortObject outputPort;
    if(inObjects[0] instanceof CombinedFskPortObject) {
      outputPort = (FskPortObject) inObjects[0];
    } else {
      outputPort = new FskPortObject(manager , readme, packages);
      outputPort.setModel(modelScript);
      outputPort.setViz(visualizationScript);
    }
    
    if (!simulations.isEmpty()) {
      outputPort.simulations.clear();
      outputPort.simulations.addAll(simulations);
    }

    outputPort.modelMetadata =
        metadata != null ? metadata : NodeUtils.initializeModel(ModelType.genericModel);
    // Apply UUID to model as an identifier
    try {
      // check if model already has a UUID
      if(SwaggerUtil.getModelId(outputPort.modelMetadata) != null) {
        UUID uuid = UUID.fromString(SwaggerUtil.getModelId(outputPort.modelMetadata));  
      } else {
        UUID uuid = UUID.randomUUID();
        SwaggerUtil.setModelId(outputPort.modelMetadata, uuid.toString());  
      }
    }catch (IllegalArgumentException exception){
      UUID uuid = UUID.randomUUID();
      SwaggerUtil.setModelId(outputPort.modelMetadata, uuid.toString());
    }
    return new PortObject[] {outputPort};
  }

  public void checkAndRegenerateSimulation(List<FskSimulation> simulations,
      List<Parameter> originalParameters, List<Parameter> newParams) {
    List<Parameter> paramsToBeRemoved =
        (List<Parameter>) CollectionUtils.removeAll(originalParameters, newParams);
    List<Parameter> viewParams =  (List<Parameter>) CollectionUtils.removeAll(newParams, originalParameters);
    viewParams.removeIf(p -> p.getClassification().equals(ClassificationEnum.OUTPUT));
    List<String> editedParamsIDs = new ArrayList<String>();
    List<Parameter> copyOforiginalParameters = new ArrayList<>(originalParameters);
    copyOforiginalParameters.removeIf(p -> p.getClassification().equals(ClassificationEnum.OUTPUT));
    List<String> originalParamsIDs = copyOforiginalParameters.stream().map(para -> para.getId()).collect(Collectors.toList());
    
    //find newly added and edited parameters out of the saved setting
    Set<String> simParamIDs = simulations.get(0).getParameters().keySet();
    List<String> differencesToBeAdded = new ArrayList<>(originalParamsIDs  );
    List<String> differencesToBeRemoved = new ArrayList<>(simParamIDs   );
    List<String> retained = new ArrayList<>(originalParamsIDs);
    retained.retainAll(simParamIDs );
    differencesToBeAdded.removeAll(simParamIDs);
    differencesToBeRemoved.removeAll(originalParamsIDs);
    
    if(originalParamsIDs.isEmpty())
      return;
    simulations.forEach(sim -> {
       LinkedHashMap<String, String> simParams = sim.getParameters();
      if (!simParams.isEmpty()) {
        if (!viewParams.isEmpty()) {
          viewParams.forEach(viewParam -> {
            // new parameters will be added to all simulations
            if (!originalParamsIDs.contains(viewParam.getId())) {
              simParams.put(viewParam.getId(), viewParam.getValue());
            }
            // or the changed parameter will be added only to the default simulation
            else if (sim.getName().equals("defaultSimulation")) {
              simParams.put(viewParam.getId(), viewParam.getValue());
            }
            editedParamsIDs.add(viewParam.getId());
          });
        } else {
          copyOforiginalParameters.forEach(originalParam -> {
             if(retained.contains(originalParam.getId()) && sim.getName().equals("defaultSimulation")) {
               simParams.put(originalParam.getId(), originalParam.getValue());
             }else if(differencesToBeAdded.contains(originalParam.getId())){
               simParams.put(originalParam.getId(), originalParam.getValue());
             }
          });
        }
        differencesToBeRemoved.forEach(toBeDeletedParam -> {
            simParams.remove(toBeDeletedParam);
        });
        // remove the parameter from simulation if it is removed from model math
        paramsToBeRemoved.forEach(toBeDeletedParam -> {
          if (!editedParamsIDs.contains(toBeDeletedParam.getId())) {
            simParams.remove(toBeDeletedParam.getId());
          }
        });
      }else {
         newParams.forEach(viewParam -> {
           simParams.put(viewParam.getId(),viewParam.getValue());  
         });
       }
    });
  }
  
  @Override
  protected void performReset() {
    m_port = null;
    if(isRepresentationResetReuired) {
      FSKEditorJSViewRepresentation rep =getViewRepresentation();
      rep.setModelMetadata("");
      rep.setReadme("");
      rep.setModelScript("");
      rep.setVisScript("");
    }
    else {
      isRepresentationResetReuired = true;
    }
    resetDone = true;
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    m_config.saveSettings(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    FSKEditorJSConfig config = new FSKEditorJSConfig();
    config.loadSettings(settings);
    if(!resetDone) {
      isRepresentationResetReuired = config.isRepresentationResetReuired();
    }
    resetDone=false;

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

  @Override
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
  
  
  /**
   * Downloads a file from a URL.The code here is considering that the fileURL is using KNIME
   * Protocol
   * 
   * @param fileURL HTTP URL of the file to be downloaded
   * @param workingDir path of the directory to save the file
   * @throws IOException
   * @throws URISyntaxException
   * @throws InvalidSettingsException
   */
  public void downloadFileToWorkingDir(String fileURL, String workingDir)
      throws IOException, URISyntaxException, InvalidSettingsException {
    String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
    String destinationPath = workingDir + File.separator + fileName;
    File fileTodownload = new File(destinationPath);
    LOGGER.info("JS EDITOR  path to write to: " + destinationPath);
    try (InputStream inStream = FileUtil.openInputStream(fileURL);
        OutputStream outStream = new FileOutputStream(fileTodownload)) {
      IOUtils.copy(inStream, outStream);
    }
  }
}
