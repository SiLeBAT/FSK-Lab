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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Value of the JavaScript simulator node.
 *
 * It contains the simulation name and the parameter values.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JSSimulatorViewValue extends JSONViewContent {

  List<JSSimulation> simulations;
  int selectedSimulationIndex = 0;
  String modelMath;
  
  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {}

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

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
    return simulations.hashCode() + selectedSimulationIndex;
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
