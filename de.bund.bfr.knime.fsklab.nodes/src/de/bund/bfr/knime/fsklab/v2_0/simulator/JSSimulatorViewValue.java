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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;

/**
 * Value of the JavaScript simulator node.
 *
 * It contains the simulation name and the parameter values.
 */

public class JSSimulatorViewValue extends JSONViewContent {

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  private static final String CFG_MODEL_MATH = "modelMath";
  private static final String CFG_SIMULATIONS = "simulations";
  private static final String CFG_SIMULATION_INDEX = "selectedSimulationIndex";

  private JSSimulation[] simulations;
  private int selectedSimulationIndex;
  private String modelMath;
  private LinkedHashMap<String, Object[]> parametersMap;
  
  public JSSimulatorViewValue() {
    simulations = new JSSimulation[0];
    modelMath = "";
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    settings.addInt(CFG_SIMULATION_INDEX, selectedSimulationIndex);
    settings.addString(CFG_MODEL_MATH, modelMath);

    if (simulations != null && simulations.length != 0) {
      try {
        String simulationStrings = MAPPER.writeValueAsString(simulations);
        settings.addString(CFG_SIMULATIONS, simulationStrings);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }

  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    selectedSimulationIndex = settings.getInt(CFG_SIMULATION_INDEX);
    modelMath = settings.getString(CFG_MODEL_MATH);

    if (settings.containsKey(CFG_SIMULATIONS)) {
      try {
        String simulationStrings = settings.getString(CFG_SIMULATIONS);
        simulations = MAPPER.readValue(simulationStrings, JSSimulation[].class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    final JSSimulatorViewValue other = (JSSimulatorViewValue) obj;
    return simulations.equals(other.simulations)
        && selectedSimulationIndex == other.selectedSimulationIndex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(simulations, selectedSimulationIndex, modelMath);
  }

  public String getModelMath() {
    return modelMath;
  }

  public void setModelMath(String modelMath) {
    this.modelMath = modelMath;
  }

  public int getSelectedSimulationIndex() {
    return selectedSimulationIndex;
  }

  public void setSelectedSimulationIndex(int selectedSimulationIndex) {
    this.selectedSimulationIndex = selectedSimulationIndex;
  }

  public JSSimulation[] getSimulations() {
    return simulations;
  }

  public void setSimulations(JSSimulation[] simulations) {
    this.simulations = simulations;
  }
  
  public LinkedHashMap<String, Object[]> getParametersMap() {
    return parametersMap;
  }

  public void setParametersMap(LinkedHashMap<String, Object[]> parametersMap) {
    this.parametersMap = parametersMap;
  }


  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class JSSimulation {

    public String name;

    public List<String> values = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }

      final JSSimulation other = (JSSimulation) obj;
      return Objects.equals(name, other.name) && Objects.equals(values, other.values);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, values);
    }
  }
}
