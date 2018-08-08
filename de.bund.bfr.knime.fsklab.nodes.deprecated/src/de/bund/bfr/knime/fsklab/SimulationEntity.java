/*
 ***************************************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab;

import java.util.List;
import metadata.Parameter;

/**
 * An Object holds the basic information of a simulation configuration.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class SimulationEntity {

  private String simulationName;

  private List<Parameter> simulationParameters;

  public String getSimulationName() {
    return simulationName;
  }

  public void setSimulationName(String simulationName) {
    this.simulationName = simulationName;
  }

  public List<Parameter> getSimulationParameters() {
    return simulationParameters;
  }

  public void setSimulationParameters(List<Parameter> simulationParameters) {
    this.simulationParameters = simulationParameters;
  }

  @Override
  public String toString() {
    return simulationName;
  }
}
