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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.io.IOUtils;
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
import org.knime.core.node.workflow.WorkflowContext;
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
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;


/**
 * Fsk Editor JS node model.
 */
final class FSKEditorJSNodeModel
    extends AbstractWizardNodeModel<FSKEditorJSViewRepresentation, FSKEditorJSViewValue>
    implements PortObjectHolder {
  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx JS Editor Model");

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
    return new FSKEditorJSViewRepresentation();
  }

  @Override
  public FSKEditorJSViewValue createEmptyViewValue() {
    return new FSKEditorJSViewValue();
  }

  // @Override
  // public FSKEditorJSViewValue getViewValue() {
  //
  // FSKEditorJSViewValue val;
  // synchronized (getLock()) {
  // val = super.getViewValue();
  // if (val == null) {
  // val = new FSKEditorJSViewValue();
  // }

  // // If val has empty (default) values then initialize it
  // if (val.isEmpty()) {
  // if (m_port != null) {
  // // Load from input port
  // try {
  // String jsonMetadata = MAPPER.writeValueAsString(m_port.modelMetadata);
  // val.setModelMetaData(jsonMetadata);
  // } catch (Exception e) {
  // try {
  // Model model = NodeUtils.initializeModel(ModelType.genericModel);
  // val.setModelMetaData(MAPPER.writeValueAsString(model));
  // } catch (JsonProcessingException e1) {
  // LOGGER.error(e1);
  // }
  // }
  //
  // val.setModelScript(m_port.model);
  // val.setVisualizationScript(m_port.viz);
  // val.setReadme(m_port.getReadme());
  // } else {
  // // Load from stored settings
  //
  // String jsonMetadata = nodeSettings.getMetadata();
  // if (jsonMetadata.isEmpty()) {
  // Model model = NodeUtils.initializeModel(nodeSettings.getModelType());
  // try {
  // jsonMetadata = MAPPER.writeValueAsString(model);
  // } catch (JsonProcessingException err) {
  // LOGGER.error(err);
  // }
  // }
  // val.setModelMetaData(jsonMetadata);
  //
  //
  // Model model = NodeUtils.initializeModel(nodeSettings.getModelType());
  // try {
  // val.setModelMetaData(MAPPER.writeValueAsString(model));
  // } catch (JsonProcessingException e1) {
  // LOGGER.error(e1);
  // }
  //
  // if (!nodeSettings.getReadmeFile().isEmpty()) {
  // try {
  // File file = new File(nodeSettings.getReadmeFile());
  // String readme = FileUtils.readFileToString(file, "UTF-8");
  // val.setReadme(readme);
  // } catch (IOException e) {
  // // If IOException then leave the default empty string from the constructor
  // }
  // }
  //
  // if (nodeSettings.getResources().length > 0) {
  // val.setResourcesFiles(nodeSettings.getResources());
  // }
  // }
  // }
  // }
  //
  // return val;
  // }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.js.FSKEditorJS";
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
  private void downloadFileToWorkingDir(String fileURL, String workingDir)
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

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {

    if (inObjects.length == 1) {
      setInternalPortObjects(inObjects);
    }

    // Create view
    final String connectedNodeId = getTableId(0);

    synchronized (getLock()) {

      FSKEditorJSViewRepresentation representation = getViewRepresentation();
      if (representation.getConnectedNodeId() == null) {
        representation.setConnectedNodeId(m_config.getConnectedNode());
      }

      FSKEditorJSViewValue value = getViewValue();

      if (representation.getConnectedNodeId() != null) {
        if (!representation.getConnectedNodeId().equals(connectedNodeId)) {
          // If we are editing another model (editor connected to a different model
          // than it was connected to before), then
          copyConnectedNodeToView(connectedNodeId);
        } else {
          // We are still editing the same model and the config is valid.
          // Copy the config to the view.
          copyConfigToView();
        }
      } else if (value.isEmpty()) {
        // If there is an input node (model), copy it to view (initialize view).
        if (inObjects.length == 1 && inObjects[0] != null) {
          copyConnectedNodeToView(connectedNodeId);
        }
      }
    }

    // Create port object

    Model metadata = null;
    String readme = "";
    String workingDirectory = "";
    List<String> packages = Collections.emptyList();
    List<FskSimulation> simulations = Collections.emptyList();
    String modelScript = "";
    String visualizationScript = "";
    String[] resources = new String[0];

    if (inObjects.length == 0) {

      // Create working directory if not set in settings
      if (!m_config.getWorkingDirectory().isEmpty()) {
        workingDirectory = m_config.getWorkingDirectory();
      } else {
        // Create a folder named after the node that will be used as working
        // directory if the node does not have a working directory
        final NodeContext nodeContext = NodeContext.getContext();
        WorkflowContext workflowContext = nodeContext.getWorkflowManager().getContext();

        // The new working directory will be named as the node's name with id and no spaces,
        // followed by _workingDirectory<COUNTER>.
        final String newWorkingDirectoryName =
            nodeContext.getNodeContainer().getNameWithID().replaceAll("\\W", "").replace(" ", "")
                + "_workingDirectory" + TEMP_DIR_UNIFIER.getAndIncrement();

        // This folder is placed in the workflow.
        File newWorkingDirectory =
            new File(workflowContext.getCurrentLocation(), newWorkingDirectoryName);
        newWorkingDirectory.mkdir();

        workingDirectory = newWorkingDirectory.getPath();
      }
    }

    synchronized (getLock()) {
      FSKEditorJSViewValue viewValue = getViewValue();

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

        metadata = MAPPER.treeToValue(metadataNode, modelClass);
      }

      // Take simulation from input port (if connected) or view value otherwise
      if (m_port != null) {
        simulations = m_port.simulations;
      } else if (metadata != null && SwaggerUtil.getModelMath(metadata) != null
          && SwaggerUtil.getParameter(metadata) != null) {
        // Take parameters from view value (metadata)
        List<Parameter> parameters = SwaggerUtil.getParameter(metadata);

        // 2. Create new default simulation out of the view value
        FskSimulation newDefaultSimulation = NodeUtils.createDefaultSimulation(parameters);

        // 3. Assign newDefaultSimulation
        simulations = Arrays.asList(newDefaultSimulation);
      }

      modelScript = viewValue.getModelScript();
      visualizationScript = viewValue.getVisualizationScript();
      readme = viewValue.getReadme();

      // resources files via fskEditorProxyValue will be available only in online mode of the
      // editor
      if (viewValue.getResourcesFiles() != null && viewValue.getResourcesFiles().length > 0) {
        resources = viewValue.getResourcesFiles();
      }

      // Collect R packages
      final Set<String> librariesSet = new HashSet<>();
      librariesSet.addAll(new RScript(modelScript).getLibraries());
      librariesSet.addAll(new RScript(visualizationScript).getLibraries());
      packages = new ArrayList<>(librariesSet);
    }

    if (resources.length > 0) {
      for (String fileRequestString : resources) {
        downloadFileToWorkingDir(fileRequestString, workingDirectory);
      }
      // delete the parent folder of the uploaded files after moving them to the working
      // directory. parentFolderPath is always uses KNIME protocol
      String firstFile = resources[0];
      String parentFolderPath = firstFile.substring(0, firstFile.lastIndexOf("/"));
      BundleContext ctx = FrameworkUtil.getBundle(IRemoteFileUtilsService.class).getBundleContext();
      ServiceReference<IRemoteFileUtilsService> ref =
          ctx.getServiceReference(IRemoteFileUtilsService.class);
      if (ref != null) {
        try {
          ctx.getService(ref).delete(new URL(parentFolderPath));
        } finally {
          ctx.ungetService(ref);
        }
      }
    }

    FskPortObject outputPort = new FskPortObject(workingDirectory, readme, packages);
    outputPort.model = modelScript;
    outputPort.viz = visualizationScript;
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

  private void copyConfigToView() {

    FSKEditorJSViewRepresentation representation = getViewRepresentation();
    representation.setConnectedNodeId(m_config.getConnectedNode());

    FSKEditorJSViewValue value = getViewValue();
    if (isViewValueEmpty()) {
      value.setModelMetaData(m_config.getModelMetaData());
      value.setModelScript(m_config.getModelScript());
      value.setVisualizationScript(m_config.getVisualizationScript());
      value.setReadme(m_config.getReadme());
      value.setResourcesFiles(m_config.getResources());
      value.setServerName(m_config.getServerName());
      value.setCompleted(m_config.isCompleted());
      value.setValidationErrors(m_config.getValidationErrors());
    }
  }
  
  private void copyValueToConfig() {
    
    FSKEditorJSViewRepresentation representation = getViewRepresentation();
    m_config.setConnectedNode(representation.getConnectedNodeId());
    
    FSKEditorJSViewValue value = getViewValue();
    m_config.setModelMetaData(value.getModelMetaData());
    m_config.setModelScript(value.getModelScript());
    m_config.setVisualizationScript(value.getVisualizationScript());
    m_config.setReadmeFile(value.getReadme());
    m_config.setResources(value.getResourcesFiles());
    m_config.setServerName(value.getServerName());
    m_config.setCompleted(value.isCompleted());
    m_config.setValidationErrors(value.getValidationErrors());
  }

  /**
   * Copy the model information from a connected node to the view.
   * 
   * @param connectedNodeId UUID of the connected node
   */
  private void copyConnectedNodeToView(String connectedNodeId) {

    FSKEditorJSViewRepresentation representation = getViewRepresentation();
    representation.setConnectedNodeId(connectedNodeId);

    FSKEditorJSViewValue value = getViewValue();
    if (isViewValueEmpty()) {

      try {
        String jsonMetadata = MAPPER.writeValueAsString(m_port.modelMetadata);
        value.setModelMetaData(jsonMetadata);
      } catch (JsonProcessingException e) {
      }

      value.setModelScript(m_port.model);
      value.setVisualizationScript(m_port.viz);
      value.setReadme(m_port.getReadme());
      // Cannot assign resource files, server name, completed and validation errors
    }
  }
}
