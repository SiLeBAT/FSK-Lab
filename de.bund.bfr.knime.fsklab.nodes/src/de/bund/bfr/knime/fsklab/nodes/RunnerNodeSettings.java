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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class RunnerNodeSettings {

  private static final String CFG_WIDTH = "width";
  private static final String CFG_HEIGHT = "height";
  private static final String CFG_RESOLUTION = "resolution";
  private static final String CFG_POINT_SIZE = "textPointSize";
  private static final String CFG_SIMULATION = "simulation";

  /** Width of the plot. */
  public int width = 640;

  /** Height of the plot. */
  public int height = 640;

  /** Nominal resolution in ppi. */
  public String res = "NA";

  /** Selected simulation. */
  public String simulation = NodeUtils.DEFAULT_SIMULATION;

  /**
   * The default pointsize of plotted text, interpreted as big points (1/72 inch) at {@link res}
   * ppi.
   */
  public int pointSize = 12;

  public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
    width = settings.getInt(CFG_WIDTH);
    height = settings.getInt(CFG_HEIGHT);
    res = settings.getString(CFG_RESOLUTION);
    pointSize = settings.getInt(CFG_POINT_SIZE);

    /*
     * If CFG_SIMULATION is missing (in case of old workflows) then restore to defaultSimulation.
     * simulation).
     */
    simulation = settings.getString(CFG_SIMULATION, NodeUtils.DEFAULT_SIMULATION);
  }

  public void save(final NodeSettingsWO settings) {
    settings.addInt(CFG_WIDTH, width);
    settings.addInt(CFG_HEIGHT, height);
    settings.addString(CFG_RESOLUTION, res);
    settings.addInt(CFG_POINT_SIZE, pointSize);
    settings.addString(CFG_SIMULATION, simulation);
  }
}
