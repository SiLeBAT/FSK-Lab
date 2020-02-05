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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.knime.core.node.CanceledExecutionException;
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
import org.knime.core.node.workflow.NativeNodeContainer;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowEvent;
import org.knime.core.node.workflow.WorkflowListener;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import org.knime.core.util.IRemoteFileUtilsService;
import org.knime.js.core.node.AbstractWizardNodeModel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.metadata.swagger.GenericModel;
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

  private final FSKEditorJSNodeSettings nodeSettings = new FSKEditorJSNodeSettings();
  private FskPortObject m_port;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME = new FSKEditorJSNodeFactory().getInteractiveViewName();

  static final AtomicLong TEMP_DIR_UNIFIER = new AtomicLong((int) (100000 * Math.random()));
  String nodeWithId;
  String nodeName;
  String nodeId;

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

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.js.FSKEditorJS";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  // This method is being called just when user click apply or close with saving options
  @Override
  public ValidationError validateViewValue(FSKEditorJSViewValue viewContent) {
    // TODO
    /*
     * Use SchemaFactory to create new Schema and Validator from it and then apply
     * validator.validate() to validate the EMF Object. Any error after the validate can be return
     * back to the javascript view as ValidationError Object which can contains the error message
     * EObject generalInformation; try { generalInformation =
     * getEObjectFromJson(viewContent.getGeneralInformation(), GeneralInformation.class); EObject
     * feed =
     * (EObject)generalInformation.eGet(generalInformation.eClass().getEStructuralFeature("feed"));
     *
     * Diagnostician validator = Diagnostician.INSTANCE;
     *
     * // Validate the feed and inspect the resulting diagnostic.
     * org.eclipse.emf.common.util.Diagnostic diagnostic = validator.validate(feed);
     *
     * return new ValidationError(diagnostic.toString()); } catch (InvalidSettingsException e) {
     * e.printStackTrace(); }
     * 
     */
    return null;

  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public FSKEditorJSViewValue getViewValue() {
    FSKEditorJSViewValue val;
    synchronized (getLock()) {
      val = super.getViewValue();
      if (val == null) {
        val = createEmptyViewValue();
      }
    }

    return val;
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



  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {
    nodeWithId = NodeContext.getContext().getNodeContainer().getNameWithID();
    nodeName = NodeContext.getContext().getNodeContainer().getName();
    nodeId = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];
    FskPortObject inObj1;
    FskPortObject outObj;

    if (inObjects.length > 0 && inObjects[0] != null) {
      inObj1 = (FskPortObject) inObjects[0];
    } else {
      String workingDirectory = "";
      String readme = "";

      // Import readme
      if (!nodeSettings.getReadme().isEmpty()) {
        File readmeFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.getReadme()));
        readme = FileUtils.readFileToString(readmeFile, "UTF-8");
      }

      if (!nodeSettings.getWorkingDirectory().isEmpty()) {
        workingDirectory = nodeSettings.getWorkingDirectory();
      } else {
        // each sub Model has it's own working directory to avoid resource conflict.
        // get current node's and workflow's context
        NodeContext nodeContext = NodeContext.getContext();
        WorkflowManager wfm = nodeContext.getWorkflowManager();
        WorkflowContext workflowContext = wfm.getContext();
        File currentWorkingDirectory =
            new File(workflowContext.getCurrentLocation(),
                nodeContext.getNodeContainer().getNameWithID().toString().replaceAll("\\W", "")
                    .replace(" ", "") + "_" + "workingDirectory"
                    + TEMP_DIR_UNIFIER.getAndIncrement());
        currentWorkingDirectory.mkdir();
        workingDirectory = currentWorkingDirectory.getPath();
      }
      
      inObj1 = new FskPortObject(workingDirectory, readme, new ArrayList<>());
      inObj1.model = "";
      inObj1.viz = "";
      inObj1.modelMetadata = new GenericModel();
      inObj1.modelMetadata.modelType("genericModel");
    }

    // Clone input object
    synchronized (getLock()) {
      FSKEditorJSViewValue fskEditorProxyValue = getViewValue();
      
      if (!StringUtils.isBlank(nodeSettings.modelType)) {
        fskEditorProxyValue.modelType = nodeSettings.modelType;
      } else if(inObj1 != null && inObj1.modelMetadata != null) {
        fskEditorProxyValue.modelType = inObj1.modelMetadata.getModelType();
      } else {
        fskEditorProxyValue.modelType = "GenericModel";
      }
      
      // If not executed
      if (fskEditorProxyValue.getModelMetaData() == null) {
        if (inObjects[0] == null) {
          loadJsonSetting();
        }
        if (fskEditorProxyValue.getModelMetaData() == null) {
          fskEditorProxyValue.setModelMetaData( FromOjectToJSON(inObj1.modelMetadata));
          fskEditorProxyValue.firstModelScript = inObj1.model;
          fskEditorProxyValue.firstModelViz = inObj1.viz;
          fskEditorProxyValue.modelType = inObj1.modelMetadata.getModelType();
          fskEditorProxyValue.readme = inObj1.getReadme();
          
        }
      } else {
        if (fskEditorProxyValue.notCompleted) {
          setWarningMessage("Output Parameters are not configured correctly");
        }
        if (StringUtils.isNotEmpty(fskEditorProxyValue.validationErrors)) {
          setWarningMessage("\n" + (fskEditorProxyValue.validationErrors).replaceAll("\"", "")
              .replaceAll(",,,", "\n"));
        }
      }
      outObj = inObj1;

      Class <? extends Model> modelClass = SwaggerUtil.modelClasses.get(fskEditorProxyValue.modelType);
      outObj.modelMetadata = getObjectFromJson(fskEditorProxyValue.getModelMetaData(), modelClass);

      if (outObj.modelMetadata != null && SwaggerUtil.getModelMath(outObj.modelMetadata) != null) {
        List<Parameter> parametersList = SwaggerUtil.getParameter(outObj.modelMetadata);

        // Create simulation
        if (parametersList != null && parametersList.size() > 0) {
          FskSimulation defaultSimulation = NodeUtils.createDefaultSimulation(parametersList);
          if (outObj.simulations.size() > 0) {
            List<FskSimulation> defaultSim =
                outObj.simulations.stream().filter(sim -> "defaultSimulation".equals(sim.getName()))
                    .collect(Collectors.toList());
            defaultSim.stream().forEach(sim -> {
              outObj.simulations.remove(sim);
            });
          }
          outObj.simulations.add(0, defaultSimulation);
        } else {
          parametersList = new ArrayList<>();
          outObj.simulations.add(0, NodeUtils.createDefaultSimulation(parametersList));
        }
      }
      
      outObj.model = fskEditorProxyValue.firstModelScript;
      outObj.viz = fskEditorProxyValue.firstModelViz;
      outObj.setReadme(fskEditorProxyValue.readme);
      // resources files via fskEditorProxyValue will be available only in online mode of the JS
      // editor
      if (fskEditorProxyValue.resourcesFiles != null
          && fskEditorProxyValue.resourcesFiles.length != 0) {
        for (String fileRequestString : fskEditorProxyValue.resourcesFiles) {
          downloadFileToWorkingDir(fileRequestString, outObj.getWorkingDirectory());
        }
        // delete the parent folder of the uploaded files after moving them to the working
        // directory.
        // parentFolderPath is always uses KNIME protocol
        String firstFile = fskEditorProxyValue.resourcesFiles[0];
        String parentFolderPath = firstFile.substring(0, firstFile.lastIndexOf("/"));
        BundleContext ctx =
            FrameworkUtil.getBundle(IRemoteFileUtilsService.class).getBundleContext();
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
      // Collect R packages
      final Set<String> librariesSet = new HashSet<>();
      librariesSet.addAll(new RScript(outObj.model).getLibraries());
      librariesSet.addAll(new RScript(outObj.viz).getLibraries());
      outObj.packages.clear();
      outObj.packages.addAll(new ArrayList<>(librariesSet));
    }
    NodeContext.getContext().getWorkflowManager().addListener(new WorkflowListener() {

      @Override
      public void workflowChanged(WorkflowEvent event) {
        if (event.getType().equals(WorkflowEvent.Type.NODE_REMOVED)
            && event.getOldValue() instanceof NativeNodeContainer) {
          NativeNodeContainer nnc = (NativeNodeContainer) event.getOldValue();
          File directory =
              nnc.getDirectNCParent().getProjectWFM().getContext().getCurrentLocation();
          String nncnamewithId = nnc.getNameWithID();
          if (nncnamewithId.equals(nodeWithId)) {

            String containerName = buildContainerName();

            String settingFolderPath = directory.getPath().concat("/" + containerName);
            File settingFolder = new File(settingFolderPath);

            try {
              if (settingFolder.exists()) {
                Files.walk(settingFolder.toPath()).sorted(Comparator.reverseOrder())
                    .map(Path::toFile).forEach(File::delete);
              }
            } catch (IOException e) {
              // nothing to do
            }
          }
        }
      }
    });
    return new PortObject[] {outObj};
  }

  private static String FromOjectToJSON(final Object object) throws JsonProcessingException {
    ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
    String jsonStr = objectMapper.writeValueAsString(object);
    return jsonStr;
  }

  private static <T> T getObjectFromJson(String jsonStr, Class<T> valueType)
      throws InvalidSettingsException, JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    Object object = mapper.readValue(jsonStr, valueType);

    return valueType.cast(object);
  }

  @Override
  protected void performReset() {
    m_port = null;
    nodeSettings.modelMetaData = "";
    nodeSettings.setReadme("");
  }

  @Override
  protected void useCurrentValueAsDefault() {}

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    /*
     * nodeSettings.generalInformation = getViewValue().getGeneralInformation(); nodeSettings.scope
     * = getViewValue().getScope(); nodeSettings.dataBackground =
     * getViewValue().getDataBackground(); nodeSettings.modelMath = getViewValue().getModelMath();
     * nodeSettings.model = getViewValue().getFirstModelScript(); nodeSettings.viz =
     * getViewValue().getFirstModelViz(); nodeSettings.save(settings);
     */
    try {
      FSKEditorJSViewValue vv = getViewValue();
      saveJsonSetting(vv.getModelMetaData(), vv.firstModelScript, vv.firstModelViz, vv.readme);
    } catch (IOException | CanceledExecutionException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    try {
      nodeSettings.load(settings);
      loadJsonSetting();
    } catch (IOException | CanceledExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  protected void loadJsonSetting() throws IOException, CanceledExecutionException {
    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    String containerName = buildContainerName();

    String settingFolderPath = directory.getPath().concat("/" + containerName);
    File settingFolder = new File(settingFolderPath);

    // Read configuration strings
    nodeSettings.modelMetaData = NodeUtils.readConfigString(settingFolder, "modelMetaData.json");
    String modelScript = NodeUtils.readConfigString(settingFolder, "modelScript.txt");
    String visualizationScript = NodeUtils.readConfigString(settingFolder, "visualization.txt");
    String readme = NodeUtils.readConfigString(settingFolder, "readme.txt");

    // Update view value
    FSKEditorJSViewValue viewValue = getViewValue();
    viewValue.setModelMetaData( nodeSettings.modelMetaData);
    viewValue.firstModelScript = modelScript;
    viewValue.firstModelViz = visualizationScript;
    viewValue.readme = readme;
  }

  protected void saveJsonSetting(String modelMetaData, String modelScript,
      String visualizationScript, String readme) throws IOException, CanceledExecutionException {
    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    String containerName = buildContainerName();

    String settingFolderPath = directory.getPath().concat("/" + containerName);
    File settingFolder = new File(settingFolderPath);
    if (!settingFolder.exists()) {
      settingFolder.mkdir();
    }

    NodeUtils.writeConfigString(modelMetaData, settingFolder, "modelMetaData.json");
    NodeUtils.writeConfigString(modelScript, settingFolder, "modelScript.txt");
    NodeUtils.writeConfigString(visualizationScript, settingFolder, "visualization.txt");
    NodeUtils.writeConfigString(readme, settingFolder, "readme.txt");
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {m_port};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    m_port = (FskPortObject) portObjects[0];
  }

  public void setHideInWizard(boolean hide) {}
  
  /** @return string with node name and id with format "{name} (#{id}) setting". */
  private static String buildContainerName() {
    final NodeContainer nodeContainer = NodeContext.getContext().getNodeContainer();
    return nodeContainer.getName() + " (#" + nodeContainer.getID().getIndex() + ") setting";
  }
}
