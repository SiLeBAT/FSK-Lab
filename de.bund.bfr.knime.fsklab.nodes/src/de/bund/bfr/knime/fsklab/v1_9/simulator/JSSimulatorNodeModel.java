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
package de.bund.bfr.knime.fsklab.v1_9.simulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.util.Pair;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.NodeRemovedListener;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.SimulationSetting;
import de.bund.bfr.knime.fsklab.v1_9.simulator.JSSimulatorViewValue.JSSimulation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

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
        final List<Parameter> parameters = SwaggerUtil.getParameter(port.modelMetadata);
        val.simulations = port.simulations.stream().map(it -> toJSSimulation(it, parameters))
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
        rep.parameters = SwaggerUtil.getParameter(port.modelMetadata).stream()
            .filter(p -> p.getClassification() != ClassificationEnum.OUTPUT)
            .collect(Collectors.toList());
      }
    }

    return rep;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return inSpecs;
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws IOException, CanceledExecutionException, InvalidSettingsException {

    final String nameWithID = NodeContext.getContext().getNodeContainer().getNameWithID();
    final String containerName = buildContainerName();
    NodeContext.getContext().getWorkflowManager()
        .addListener(new NodeRemovedListener(nameWithID, containerName));

    final FskPortObject inObj = (FskPortObject) inObjects[0];

    synchronized (getLock()) {

      final JSSimulatorViewValue val = getViewValue();

      // If not executed
      if (val.simulations == null) {
        final List<Parameter> parameters = SwaggerUtil.getParameter(inObj.modelMetadata);
        loadJsonSetting();
        final List<JSSimulation> simulations = inObj.simulations.stream()
            .map(it -> toJSSimulation(it, parameters)).collect(Collectors.toList());

        if (val.modelMath == null) {
          val.simulations = simulations;
          val.modelMath = FromOjectToJSON(SwaggerUtil.getModelMath(inObj.modelMetadata));
          val.selectedSimulationIndex = inObj.selectedSimulationIndex;
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

    return new PortObject[] {inObj};
  }


  private void createSimulation(FskPortObject inObj, JSSimulatorViewValue val) {

    final List<Parameter> inputParams = getViewRepresentation().parameters;
    inObj.simulations.clear();
    
    for (final JSSimulation jsSimulation : val.simulations) {
      final FskSimulation fskSimulation = new FskSimulation(jsSimulation.name);
      for (int i = 0; i < inputParams.size(); i++) {
        final String paramName = inputParams.get(i).getId();
        final String paramValue = jsSimulation.values.get(i);
        fskSimulation.getParameters().put(paramName, paramValue);
      }
      inObj.simulations.add(fskSimulation);
    }

    inObj.selectedSimulationIndex = val.selectedSimulationIndex;
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

  @Override
  public void setHideInWizard(boolean hide) {}

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

    try {
      final JSSimulatorViewValue vv = getViewValue();
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
    final File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    final String containerName = buildContainerName();

    final File settingFolder = new File(directory, containerName);
   
    // Read configuration strings
    final String simulationString = NodeUtils.readConfigString(settingFolder, "simulations.json");

    // Update view value
    final JSSimulatorViewValue viewValue = getViewValue();

    // Parse simulation string and update view Value
    final Pair<Integer, List<JSSimulation>> simulationsInfo =
        SimulationSetting.from(simulationString);
    viewValue.selectedSimulationIndex = simulationsInfo.getFirst();
    viewValue.simulations = simulationsInfo.getSecond();

    viewValue.modelMath = NodeUtils.readConfigString(settingFolder, "modelMath.json");
  }

  protected void saveJsonSetting(List<JSSimulation> simulationList, String modelMath)
      throws IOException, CanceledExecutionException {

    if (simulationList == null) {
      return;
    }

    final File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();

    final String containerName = buildContainerName();

    final File settingFolder = new File(directory, containerName);
    if (!settingFolder.exists()) {
      settingFolder.mkdir();
    }

    final String joinedSimulations = SimulationSetting
        .toString(new Pair<>(getViewValue().selectedSimulationIndex, simulationList));
    NodeUtils.writeConfigString(joinedSimulations, settingFolder, "simulations.json");
    NodeUtils.writeConfigString(modelMath, settingFolder, "modelMath.json");
  }


  private static String FromOjectToJSON(final Object object) throws JsonProcessingException {
    final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
    final String jsonStr = objectMapper.writeValueAsString(object);
    return jsonStr;
  }

  /** @return string with node name and id with format "{name} (#{id}) setting". */
  private static String buildContainerName() {
    final NodeContainer nodeContainer = NodeContext.getContext().getNodeContainer();
    return nodeContainer.getName() + " (#" + nodeContainer.getID().getIndex() + ") setting";
  }
}
