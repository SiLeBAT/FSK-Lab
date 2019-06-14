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
package de.bund.bfr.knime.fsklab.nodes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emfjson.jackson.resource.JsonResourceFactory;
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
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowEvent;
import org.knime.core.node.workflow.WorkflowListener;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.JSSimulatorViewValue.JSSimulation;
import metadata.MetadataPackage;
import metadata.ModelMath;
import metadata.Parameter;
import metadata.ParameterClassification;

class JSSimulatorNodeModel
    extends AbstractWizardNodeModel<JSSimulatorViewRepresentation, JSSimulatorViewValue>
    implements PortObjectHolder {
  private static final NodeLogger LOGGER =
      NodeLogger.getLogger("JavaScript FSK Simulation Configurator");
  private FskPortObject port;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME = new JSSimulatorNodeFactory().getInteractiveViewName();
  int index = 0;

  String nodeWithId;
  String nodeName;
  String nodeId;

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
    return "de.bund.bfr.knime.fsklab.nodes.jssimulator";
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
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public JSSimulatorViewValue getViewValue() {
    JSSimulatorViewValue val;
    synchronized (getLock()) {
      val = super.getViewValue();

      if (val == null) {
        val = createEmptyViewValue();
      }

      if (val.simulations == null && port != null && port.simulations != null) {
        // Convert from FskSimulation(s) to JSSimulation(s)
        val.simulations =
            port.simulations.stream().map(it -> toJSSimulation(it, port.modelMath.getParameter()))
                .collect(Collectors.toList());
      }
    }

    return val;
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
        rep.parameters = port.modelMath.getParameter().stream()
            .filter(p -> (p.getParameterClassification() == ParameterClassification.INPUT
                || p.getParameterClassification() == ParameterClassification.CONSTANT))
            .collect(Collectors.toList());
      }
    }

    return rep;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return inSpecs;
  }
  public void deleteSettingFolder(String settingFolderPath) {
    File settingFolder = new File(settingFolderPath);

    try {
      if (settingFolder.exists()) {
        Files.walk(settingFolder.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile)
            .forEach(File::delete);
      }
    } catch (IOException e) {
      // nothing to do
    }
  }
  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws IOException, CanceledExecutionException, InvalidSettingsException {
    nodeWithId = NodeContext.getContext().getNodeContainer().getNameWithID();
    nodeName = NodeContext.getContext().getNodeContainer().getName();
    nodeId = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];

    FskPortObject inObj = (FskPortObject) inObjects[0];

    synchronized (getLock()) {

      JSSimulatorViewValue val = getViewValue();

      // If not executed
      if (val.simulations == null) {
        loadJsonSetting();
        List<JSSimulation> simulations =
            inObj.simulations.stream().map(it -> toJSSimulation(it, inObj.modelMath.getParameter()))
                .collect(Collectors.toList());
        if (val.modelMath != null) {
          ModelMath modelMathFromSetting = getEObjectFromJson(val.modelMath, ModelMath.class);
          if (!EcoreUtil.equals(modelMathFromSetting.getParameter(),
              inObj.modelMath.getParameter())) {
            // Convert FskSimulation(s) to JSSimulation(s)
            val.simulations = simulations;
            File directory = NodeContext.getContext().getWorkflowManager().getProjectWFM()
                .getContext().getCurrentLocation();
            String containerName = nodeName + " (#" + nodeId + ") setting";
            String settingFolderPath = directory.getPath().concat("/" + containerName);
            deleteSettingFolder(settingFolderPath);
          }
        } else {
          val.simulations = simulations;
          val.modelMath = FromEOjectToJSON(inObj.modelMath);
        }
        port = inObj;
      }

      // Takes modified simulations from val

      // Converts JSSimulation(s) back to FskSimulation(s)
      port = inObj; // Needed by getViewRepresentation
      createSimulation(inObj, val);

      LOGGER
          .info(" saving '" + val.selectedSimulationIndex + "' as the selected simulation index!");
    }

    exec.setProgress(1);
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

            String containerName = nodeName + " (#" + nodeId + ") setting";

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
    return new PortObject[] {inObj};
  }

  private void createSimulation(FskPortObject inObj, JSSimulatorViewValue val) {

    if (inObj instanceof CombinedFskPortObject) {
      List<Parameter> inputParams = getViewRepresentation().parameters;
      createSimulation(((CombinedFskPortObject) inObj).getFirstFskPortObject(), val);
      createSimulation(((CombinedFskPortObject) inObj).getSecondFskPortObject(), val);
      inObj.simulations.clear();
      for (JSSimulation jsSimulation : val.simulations) {
        FskSimulation fskSimulation = new FskSimulation(jsSimulation.name);
        for (int i = 0; i < inputParams.size(); i++) {
          String paramName = inputParams.get(i).getParameterID();
          String paramValue = jsSimulation.values.get(i);
          fskSimulation.getParameters().put(paramName, paramValue);
        }
        inObj.simulations.add(fskSimulation);
      }

      inObj.selectedSimulationIndex = val.selectedSimulationIndex;
    } else {
      inObj.simulations.clear();
      List<String> modelMathParameter = inObj.modelMath.getParameter().stream()
          .map(Parameter::getParameterID).collect(Collectors.toList());
      List<Integer> indexes = new ArrayList<Integer>();
      List<Parameter> properInputParam = new ArrayList<Parameter>();
      for (JSSimulation jsSimulation : val.simulations) {
        FskSimulation fskSimulation = new FskSimulation(jsSimulation.name);
        List<Parameter> inputParams = getViewRepresentation().parameters;
        index = 0;
        inputParams.stream().forEach(param -> {
          Parameter paramCopy = EcoreUtil.copy(param);
          String paramWithSuffix = paramCopy.getParameterID();
          String paramWithoutSuffix = paramWithSuffix.replaceAll(JoinerNodeModel.suffix, "");
          if (modelMathParameter.contains(paramWithoutSuffix)
              || modelMathParameter.contains(paramWithSuffix)) {
            paramCopy.setParameterID(paramWithoutSuffix);
            properInputParam.add(paramCopy);
            indexes.add(index);
          }
          index++;

        });
        /*
         * inputParams.stream().filter(param -> modelMathParameter.contains(param.getParameterID()))
         * .collect(Collectors.toList());
         */

        for (int i = 0; i < properInputParam.size(); i++) {
          String paramName = properInputParam.get(i).getParameterID();
          String paramValue = jsSimulation.values.get(indexes.get(i));
          fskSimulation.getParameters().put(paramName, paramValue);
        }
        inObj.simulations.add(fskSimulation);
      }

      inObj.selectedSimulationIndex = val.selectedSimulationIndex;
    }

  }

  @Override
  protected void performReset() {
    port = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {}



  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}


  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {port};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    port = (FskPortObject) portObjects[0];
  }

  public void setHideInWizard(boolean hide) {}

  private static JSSimulation toJSSimulation(FskSimulation fskSim,
      EList<metadata.Parameter> eList) {
    JSSimulation jsSim = new JSSimulation();
    jsSim.name = fskSim.getName();
    jsSim.values = eList.stream()
        .filter(p -> (p.getParameterClassification() == ParameterClassification.INPUT
            || p.getParameterClassification() == ParameterClassification.CONSTANT))
        .map(p -> fskSim.getParameters().get(p.getParameterID().trim()))
        .collect(Collectors.toList());

    return jsSim;
  }



  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {

    try {
      JSSimulatorViewValue vv = getViewValue();
      saveJsonSetting(vv.simulations, vv.modelMath);
    } catch (IOException | CanceledExecutionException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    try {
      loadJsonSetting();
    } catch (IOException | CanceledExecutionException e) {
      e.printStackTrace();
    }
  }


  protected void loadJsonSetting() throws IOException, CanceledExecutionException {
    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    String name = NodeContext.getContext().getNodeContainer().getName();
    String id = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];
    String containerName = name + " (#" + id + ") setting";

    String settingFolderPath = directory.getPath().concat("/" + containerName);
    File settingFolder = new File(settingFolderPath);

    // Read configuration strings
    String simulationString = NodeUtils.readConfigString(settingFolder, "simulations.json");
   
    // Update view value
    if (!StringUtils.isBlank(simulationString)) {
      String selectedIndex = simulationString.split(">>><<<")[0];
      getViewValue().selectedSimulationIndex = Integer.parseInt(selectedIndex);
      simulationString = simulationString.split(">>><<<")[1];
      
      List<JSSimulation> simulations = new ArrayList<JSSimulation>();
      JSSimulatorViewValue viewValue = getViewValue();
      List<String> listOfSimulations = Arrays.asList(simulationString.split("<<<>>>"));

      listOfSimulations.forEach(lineOfSimulation -> {
        String[] oneSimualtion = lineOfSimulation.split("  ,:  ");

        if (oneSimualtion != null && oneSimualtion.length == 2) {
          JSSimulation jsSim = new JSSimulation();
          jsSim.name = oneSimualtion[0];
          jsSim.values = Arrays.asList(oneSimualtion[1].split(":::"));
          simulations.add(jsSim);
        }

      });
      viewValue.simulations = simulations;
      viewValue.modelMath = NodeUtils.readConfigString(settingFolder, "modelMath.json");
    }
  }

  protected void saveJsonSetting(List<JSSimulation> simulationList, String modelMath)
      throws IOException, CanceledExecutionException {
    if (simulationList == null) {
      return;
    }
    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    String name = NodeContext.getContext().getNodeContainer().getName();
    String id = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];
    String containerName = name + " (#" + id + ") setting";

    String settingFolderPath = directory.getPath().concat("/" + containerName);
    File settingFolder = new File(settingFolderPath);
    if (!settingFolder.exists()) {
      settingFolder.mkdir();
    }
    String joinedSimulations = ""+getViewValue().selectedSimulationIndex+">>><<<";
    joinedSimulations += simulationList.stream()
        .map(simulation -> simulation.buildStringValue()).collect(Collectors.joining("<<<>>>"));
    NodeUtils.writeConfigString(joinedSimulations, settingFolder, "simulations.json");
    NodeUtils.writeConfigString(modelMath, settingFolder, "modelMath.json");

  }

  private static <T> T getEObjectFromJson(String jsonStr, Class<T> valueType)
      throws InvalidSettingsException {
    final ResourceSet resourceSet = new ResourceSetImpl();
    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
        .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory(mapper));
    resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
        MetadataPackage.eINSTANCE);

    Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
    InputStream inStream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
    try {
      resource.load(inStream, null);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return (T) resource.getContents().get(0);
  }

  private static String FromEOjectToJSON(final EObject eObject) throws JsonProcessingException {
    ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
    String jsonStr = objectMapper.writeValueAsString(eObject);
    return jsonStr;
  }
}
