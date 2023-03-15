/*
 ***************************************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.v2_0.simulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.VariableType;
import org.knime.core.util.FileUtil;
import org.knime.core.util.IRemoteFileUtilsService;
import org.knime.js.core.node.AbstractWizardNodeModel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.ExistingEnvironmentManager;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.joiner.JoinerNodeUtil;
import de.bund.bfr.knime.fsklab.v2_0.simulator.JSSimulatorViewValue.JSSimulation;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum;
import metadata.SwaggerUtil;

class JSSimulatorNodeModel
    extends AbstractWizardNodeModel<JSSimulatorViewRepresentation, JSSimulatorViewValue>
    implements PortObjectHolder {

  private final JSSimulatorConfig m_config = new JSSimulatorConfig();
  private FskPortObject port;

  private static final NodeLogger LOGGER =
      NodeLogger.getLogger("JavaScript FSK Simulation Configurator");

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME = new JSSimulatorNodeFactory().getInteractiveViewName();

  public JSSimulatorNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public JSSimulatorViewRepresentation createEmptyViewRepresentation() {
    return new JSSimulatorViewRepresentation();
  }

  @Override
  public JSSimulatorViewValue createEmptyViewValue() {
    return new JSSimulatorViewValue();
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.v2.0.simulator.component";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(JSSimulatorViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {
  }

  @Override
  public JSSimulatorViewValue getViewValue() {

    JSSimulatorViewValue value = super.getViewValue();
    synchronized (getLock()) {

      // Copy simulation from settings
      if (value.getSimulations() == null || value.getSimulations().length == 0) {
        copyConfigToView(value);
      }

      // If still not initialized, then copy from input model
      if (value.getSimulations() == null || value.getSimulations().length == 0) {
        // Convert from FskSimulation(s) to JSSimulation(s)
        value.setSimulations(convertSimulations(port.modelMetadata));
        value.setSelectedSimulationIndex(port.selectedSimulationIndex);
      }

      if (value.getModelMath() == null) {
        try {
          value.setModelMath(
              MAPPER.writeValueAsString(SwaggerUtil.getModelMath(port.modelMetadata)));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    }

    return value;
  }

  @Override
  public JSSimulatorViewRepresentation getViewRepresentation() {
    JSSimulatorViewRepresentation rep;
    synchronized (getLock()) {
      rep = super.getViewRepresentation();
      if (rep == null) {
        rep = createEmptyViewRepresentation();
      }
      if (rep.parameters == null && port != null) {
        // Take only input parameters from metadata
        rep.parameters = SwaggerUtil.getParameter(port.modelMetadata).stream()
            .filter(p -> p.getClassification() != ClassificationEnum.OUTPUT)
            .collect(Collectors.toList());
      }
    }

    return rep;
  }

  private JSSimulation[] convertSimulations(Model metadata) {
    final List<Parameter> parameters = SwaggerUtil.getParameter(port.modelMetadata);
    final List<JSSimulation> simulations = port.simulations.stream()
        .map(it -> toJSSimulation(it, parameters)).collect(Collectors.toList());
    return (JSSimulation[]) simulations.toArray(new JSSimulation[0]);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return inSpecs;
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws IOException, CanceledExecutionException, InvalidSettingsException {
    VariableType[] variableType = new VariableType[]{VariableType.StringType.INSTANCE};
    Map<String, FlowVariable> variableMap = getAvailableFlowVariables(variableType);
    if (inObjects[0] != null) {
      setInternalPortObjects(inObjects);
    }
    FskSimulation flowVarSimulation = createFlowVarSimulation(variableMap);
    if(flowVarSimulation != null)
      ((FskPortObject) inObjects[0]).simulations.add(flowVarSimulation);
    synchronized (getLock()) {

      final JSSimulatorViewValue value = getViewValue();

      try {
        createSimulation(value, (FskPortObject) inObjects[0]);
      } catch (IOException | URISyntaxException | InvalidSettingsException e) {
        e.printStackTrace();
      }
      // delete the parent folder of the uploaded files after moving them to the working
      // directory.
      // parentFolderPath is always uses KNIME protocol

      if(!StringUtils.isBlank(value.getParentResourcesFolder())){
        BundleContext ctx =
            FrameworkUtil.getBundle(IRemoteFileUtilsService.class).getBundleContext();
        ServiceReference<IRemoteFileUtilsService> ref =
            ctx.getServiceReference(IRemoteFileUtilsService.class);
        if (ref != null) {
          try {
            ctx.getService(ref).delete(new URL(value.getParentResourcesFolder()));
          } finally {
            ctx.ungetService(ref);
          }
        }
      }
      List<Parameter> listOfParameter = SwaggerUtil.getParameter(port.modelMetadata);

      value.setParametersMap(JoinerNodeUtil.generateColorMap(port, null, listOfParameter, new AtomicInteger(0), new AtomicInteger(0)));
      LOGGER.info(
          " saving '" + value.getSelectedSimulationIndex() + "' as the selected simulation index!");
    }

    exec.setProgress(1);

    return new PortObject[] {port};
  }
  private FskSimulation getDefaultSimulation() {
    FskSimulation defaultSimulation = null;
    for (final FskSimulation jsSimulation : port.simulations) {
      if(jsSimulation.getName().equals("defaultSimulation")) {
        defaultSimulation = jsSimulation;
      }
    }
    return defaultSimulation;
  }
  private FskSimulation createFlowVarSimulation( Map<String, FlowVariable> variableMap) {
    JsonNode simulationJson  = getJSONIfValid(variableMap);
    FskSimulation injectedSimulation = null;
    if(simulationJson == null)
      return injectedSimulation;
    else 
      injectedSimulation = new FskSimulation("InjectedSimulation");
    
    FskSimulation defaultSimulation = getDefaultSimulation();
    LinkedHashMap<String, String>  defaultSimulationParams = defaultSimulation.getParameters();
    LinkedHashMap<String, String>  defaultSimulationParamsCopy = (LinkedHashMap<String, String>) defaultSimulationParams.clone();
    injectedSimulation.setParams(defaultSimulationParamsCopy);
    for(String paramName : defaultSimulationParams.keySet()) {
      if(simulationJson.get(paramName)!=null) {
        JsonNode value = simulationJson.get(paramName);
        injectedSimulation.getParameters().put(paramName, value.asText());
      }
    }
    
    return injectedSimulation;
  }

  public JsonNode getJSONIfValid(Map<String, FlowVariable> variableMap) {
    JsonNode simulationJson = null;
    
    ObjectMapper mapper = new ObjectMapper()
        .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
    //TODO: Have a Flow-Variable in Simulator Node Dialog so a simulation can
    // have a custom name
    if(variableMap.keySet().contains("injectedSimulation")) {
      try {
        simulationJson  = mapper.readTree(variableMap.get("injectedSimulation").getStringValue());
      } catch (JsonProcessingException e) {
    	    LOGGER.warn("Invalid JSON Object provided via Flow Variable");
      }
    }
    return simulationJson;
  }
  private void createSimulation(JSSimulatorViewValue val, FskPortObject fskObj)
      throws IOException, URISyntaxException, InvalidSettingsException {

    final List<Parameter> inputParams = getViewRepresentation().parameters;
    JSSimulatorViewValue value = getViewValue();

    port.simulations.clear();
    
    Optional<Path> workingDirectory = null;
    String userPath = PreferenceInitializer.getFSKWorkingDirectory();
    Path directory = Files.createTempDirectory(Paths.get(userPath), "workingDirectory");
    
    workingDirectory = Optional.of(directory);
    if (fskObj.getEnvironmentManager().isPresent()) {
      Optional<Path> oldWorkingDirectory = fskObj.getEnvironmentManager().get().getEnvironment();
      try {
        FileUtils.copyDirectory(oldWorkingDirectory.get().toFile(), directory.toFile());
      } catch (IOException e) {
          e.printStackTrace();
      }
    } 
    
    Optional<EnvironmentManager> environmentManager =
        Optional.of(new ExistingEnvironmentManager(workingDirectory.get().toString()));
    fskObj.setEnvironmentManager(environmentManager);

    for (final JSSimulation jsSimulation : val.getSimulations()) {
      final FskSimulation fskSimulation = new FskSimulation(jsSimulation.name);
      for (int i = 0; i < inputParams.size(); i++) {
        final String paramName = inputParams.get(i).getId();
        String paramValue = jsSimulation.values.get(i);
        if (paramValue != null && inputParams.get(i).getDataType().equals(DataTypeEnum.FILE)) {
          
          
          if (value.getResourcesFiles() != null
              && value.getResourcesFiles().get(paramName)!=null
              && value.getResourcesFiles().get(paramName).length > 0
              && value.getResourcesFiles().get(paramName)[1].equals(fskSimulation.getName())
              && !fskSimulation.getName().equals("defaultSimulation")) {
            
            String fileParam = downloadFileToWorkingDir(value.getResourcesFiles().get(paramName)[0],
                workingDirectory.get().toString());
            fskSimulation.getParameters().put(paramName, "\"" + fileParam + "\"");

          } else {
            if (paramValue.startsWith("\"")) {

              paramValue = paramValue.replaceAll("\"", "");
            }
            File file = new File(paramValue);
            
            URI u = new URI(paramValue);
            if(u.getScheme() != null){
              
              String fileParam = downloadOnlineRecourceToWorkingDir(u,
                  workingDirectory.get().toString());
              fskSimulation.getParameters().put(paramName, "\"" + fileParam + "\"");
            }
            else if (file.exists()) {
              String fileParam = copyFileToWorkingDir(paramValue, workingDirectory.get().toString());
              fskSimulation.getParameters().put(paramName, "\""+fileParam+"\"");
            }else  {
              fskSimulation.getParameters().put(paramName, "\""+paramValue+"\"");
            }
          }
        } else {
          fskSimulation.getParameters().put(paramName, paramValue);
        }
      }
      port.simulations.add(fskSimulation);
    }

    port.selectedSimulationIndex = val.getSelectedSimulationIndex();
  }


  @Override
  protected void performReset() {
    port = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {
    copyValueToConfig();
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    new JSSimulatorConfig().loadSettings(settings);
  }

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {port};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    port = (FskPortObject) portObjects[0];
  }

  @Override
  public void setHideInWizard(boolean hide) {
  }

  private static JSSimulation toJSSimulation(FskSimulation fskSim, List<Parameter> parameters) {
    final JSSimulation jsSim = new JSSimulation();
    jsSim.name = fskSim.getName();

    // Get input and constant parameters
    final List<de.bund.bfr.metadata.swagger.Parameter> nonOutputs =
        parameters.stream().filter(p -> p.getClassification() != ClassificationEnum.OUTPUT)
            .collect(Collectors.toList());

    // Get values of input and constant parameters from the parameters metadata in fskSim
    jsSim.values = nonOutputs.stream().map(Parameter::getId).map(fskSim.getParameters()::get)
        .collect(Collectors.toList());

    return jsSim;
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    m_config.saveSettings(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    m_config.loadSettings(settings);
  }

  private void copyConfigToView(JSSimulatorViewValue value) {
    value.setModelMath(m_config.getModelMath());
    value.setSelectedSimulationIndex(m_config.getSelectedSimulationIndex());
    value.setSimulations(m_config.getSimulations());
  }

  private void copyValueToConfig() {
    JSSimulatorViewValue value = getViewValue();
    m_config.setModelMath(value.getModelMath());
    m_config.setSelectedSimulationIndex(value.getSelectedSimulationIndex());
    m_config.setSimulations(value.getSimulations());
  }
  /**
   * copy a file from a URL.The code here is considering that the fileURL is using KNIME
   * Protocol
   * 
   * @param fileURL HTTP URL of the file to be copied
   * @param workingDir path of the directory to save the file
   * @throws IOException
   * @throws URISyntaxException
   * @throws InvalidSettingsException
   */
  public String copyFileToWorkingDir(String fileURL, String workingDir)
      throws IOException, URISyntaxException, InvalidSettingsException {
    String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
    String destinationPath = workingDir + File.separator + fileName;
    File fileTodownload = new File(destinationPath);
    LOGGER.info("JS Simulator path to write to: " + destinationPath);
    try (InputStream inStream = FileUtil.openInputStream(fileURL);
        OutputStream outStream = new FileOutputStream(fileTodownload)) {
      IOUtils.copy(inStream, outStream);
    }
    return fileName;
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
  public String downloadFileToWorkingDir(String fileURL, String workingDir)
      throws IOException, URISyntaxException, InvalidSettingsException {
    String fileName = fileURL.substring(fileURL.lastIndexOf(File.separator) + 1, fileURL.length());
    String destinationPath = workingDir + File.separator + fileName;
    File fileTodownload = new File(destinationPath);
    LOGGER.warn("downloadFileToWorkingDir JS Simulator path to write to: " + destinationPath);
    try (InputStream inStream = FileUtil.openInputStream(fileURL);
        OutputStream outStream = new FileOutputStream(fileTodownload)) {
      IOUtils.copy(inStream, outStream);
    }
    return fileName;
  }
  
  /**
   * Downloads a file from a URL.The code here is considering that the fileURL defines a file URI scheme
   * https://en.wikipedia.org/wiki/File_URI_scheme
   * @param u URL of the file to be downloaded
   * @param workingDir path of the directory to save the file
   * @throws IOException
   * @throws URISyntaxException
   * @throws InvalidSettingsException
   */
  public String downloadOnlineRecourceToWorkingDir(URI u, String workingDir) throws IOException {
    String fileName = FilenameUtils.getName(u.getPath());
    String destinationPath = workingDir + File.separator + fileName;    
    ReadableByteChannel rbc = Channels.newChannel(u.toURL().openStream());
    try (FileOutputStream fos = new FileOutputStream(destinationPath)) {
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
    return fileName;
  }
}
