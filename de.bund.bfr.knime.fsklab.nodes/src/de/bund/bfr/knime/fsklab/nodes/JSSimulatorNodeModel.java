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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.nodes.JSSimulatorViewValue.JSSimulation;
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

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws IOException {

    FskPortObject inObj = (FskPortObject) inObjects[0];

    synchronized (getLock()) {

      JSSimulatorViewValue val = getViewValue();

      // If not executed
      if (val.simulations == null) {

        // Convert FskSimulation(s) to JSSimulation(s)
        val.simulations =
            inObj.simulations.stream().map(it -> toJSSimulation(it, inObj.modelMath.getParameter()))
                .collect(Collectors.toList());

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

    if (inObj instanceof CombinedFskPortObject) {
      if (((CombinedFskPortObject) inObj).getJoinerRelation() != null
          && !((CombinedFskPortObject) inObj).getJoinerRelation().isEmpty()) {
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
          Parameter paramCopy =  EcoreUtil.copy(param);          
          String paramWithSuffix = paramCopy.getParameterID();
          String paramWithoutSuffix = paramWithSuffix.replaceAll(JoinerNodeModel.suffix, "");
          if (modelMathParameter.contains(paramWithoutSuffix) || modelMathParameter.contains(paramWithSuffix)) {
            paramCopy.setParameterID(paramWithoutSuffix);
            properInputParam.add(paramCopy);
            indexes.add(index);
          }
          index++;

        });
        /*inputParams.stream().filter(param -> modelMathParameter.contains(param.getParameterID()))
            .collect(Collectors.toList());*/

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
  protected void saveSettingsTo(NodeSettingsWO settings) {}

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {}

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
}
