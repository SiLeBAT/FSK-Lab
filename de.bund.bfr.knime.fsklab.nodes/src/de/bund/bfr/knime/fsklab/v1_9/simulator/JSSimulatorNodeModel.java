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

import java.io.IOException;
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
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.simulator.JSSimulatorViewValue.JSSimulation;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
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
    return "de.bund.bfr.knime.fsklab.v1.9.simulator.component";
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

    if (inObjects[0] != null) {
      setInternalPortObjects(inObjects);
    }

    synchronized (getLock()) {

      final JSSimulatorViewValue value = getViewValue();

      createSimulation(value);

      LOGGER.info(
          " saving '" + value.getSelectedSimulationIndex() + "' as the selected simulation index!");
    }

    exec.setProgress(1);

    return new PortObject[] {port};
  }


  private void createSimulation(JSSimulatorViewValue val) {

    final List<Parameter> inputParams = getViewRepresentation().parameters;
    port.simulations.clear();

    for (final JSSimulation jsSimulation : val.getSimulations()) {
      final FskSimulation fskSimulation = new FskSimulation(jsSimulation.name);
      for (int i = 0; i < inputParams.size(); i++) {
        final String paramName = inputParams.get(i).getId();
        final String paramValue = jsSimulation.values.get(i);
        fskSimulation.getParameters().put(paramName, paramValue);
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

}
