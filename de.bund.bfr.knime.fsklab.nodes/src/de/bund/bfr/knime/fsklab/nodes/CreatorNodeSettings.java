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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class CreatorNodeSettings {

  private static final String CFG_MODEL_SCRIPT = "modelScript";
  private static final String CFG_PARAMETERS_SCRIPT = "paramScript";
  private static final String CFG_VISUALIZATION_SCRIPT = "visualizationScript";
  private static final String CFG_SPREADSHEET = "spreadsheet";
  private static final String CFG_RESOURCES = "resources";

  /** Path to model script. */
  public String modelScript = "";

  /** Path to parameters script. */
  public String parameterScript = "";

  /** Path to visualization script. */
  public String visualizationScript = "";

  /** Path to spreadsheet. */
  public String spreadsheet = "";

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  public List<Path> resources = new ArrayList<>();

  public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
    modelScript = settings.getString(CFG_MODEL_SCRIPT);
    parameterScript = settings.getString(CFG_PARAMETERS_SCRIPT);
    visualizationScript = settings.getString(CFG_VISUALIZATION_SCRIPT);
    spreadsheet = settings.getString(CFG_SPREADSHEET);

    resources.clear();
    try {
      final String[] resourcesArray = settings.getStringArray(CFG_RESOURCES);
      for (final String resourceAsString : resourcesArray) {
        resources.add(Paths.get(resourceAsString));
      }
    } catch (final InvalidSettingsException exception) {
      // exception is not used. Instead an empty array is assigned if the
      // CFG_RESOURCES key is missing (old nodes).
    }
  }

  public void save(final NodeSettingsWO settings) {
    settings.addString(CFG_MODEL_SCRIPT, modelScript);
    settings.addString(CFG_PARAMETERS_SCRIPT, parameterScript);
    settings.addString(CFG_VISUALIZATION_SCRIPT, visualizationScript);
    settings.addString(CFG_SPREADSHEET, spreadsheet);

    final String[] resourcesArray = resources.stream().map(Path::toString).toArray(String[]::new);
    settings.addStringArray(CFG_RESOURCES, resourcesArray);
  }
}
