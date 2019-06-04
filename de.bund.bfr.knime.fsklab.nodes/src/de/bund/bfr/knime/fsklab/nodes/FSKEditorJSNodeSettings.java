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

class FSKEditorJSNodeSettings {

  private static final String JSON_REPRESENTATION = "jsonRepresentation";
  private static final String CFG_README = "readme";
  private static final String CFG_RESOURCES = "resources";
  private static final String CFG_WORKING_DIRECTORY = "workingDirectory";

  private static final String CFG_GENERAL_INFORMATION = "generalInformation";
  private static final String CFG_SCOPE = "scope";
  private static final String CFG_DATA_BACKGROUND = "dataBackground";
  private static final String CFG_MODEL_MATH = "modelMath";

  public String generalInformation;
  public String scope;
  public String dataBackground;
  public String modelMath;
  public String model;
  public String viz;
  String jsonRepresentation = "";

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  private String workingDirectory = "";
  /** Resources that will be load into the working directory */
  private String resources = "";

  /** README. */
  private String readme = "";

  /** @return empty string if not set. */
  public String getResources() {
    return resources != null ? resources : "";
  }

  public void setResources(String resources) {
    if (resources != null) {
      this.resources = resources;
    }
  }

  /** @return empty string if not set. */
  public String getReadme() {
    return readme != null ? readme : "";
  }

  public void setReadme(String readme) {
    if (readme != null) {
      this.readme = readme;
    }
  }

  /** @return empty string if not set. */
  public String getWorkingDirectory() {
    return workingDirectory != null ? workingDirectory : "";
  }

  public void setWorkingDirectory(String workingDirectory) {
    if (workingDirectory != null) {
      this.workingDirectory = workingDirectory;
    }
  }

  void load(final NodeSettingsRO settings) throws InvalidSettingsException {
    jsonRepresentation = settings.getString(JSON_REPRESENTATION, "");
    resources = settings.getString(CFG_RESOURCES, "");
    readme = settings.getString(CFG_README, "");
    workingDirectory = settings.getString(CFG_WORKING_DIRECTORY, "");
    try {
      generalInformation = settings.getString(CFG_GENERAL_INFORMATION);
      scope = settings.getString(CFG_SCOPE);
      dataBackground = settings.getString(CFG_DATA_BACKGROUND);
      modelMath = settings.getString(CFG_MODEL_MATH);
    } catch (Exception e) {
      generalInformation = "";
      scope = "";
      dataBackground = "";
      modelMath = "";
    }
  }

  void save(final NodeSettingsWO settings) {
    settings.addString(JSON_REPRESENTATION, jsonRepresentation);
    settings.addString(CFG_RESOURCES, resources);
    settings.addString(CFG_README, readme);
    settings.addString(CFG_WORKING_DIRECTORY, workingDirectory);
    settings.addString(CFG_GENERAL_INFORMATION, generalInformation);
    settings.addString(CFG_SCOPE, scope);
    settings.addString(CFG_DATA_BACKGROUND, dataBackground);
    settings.addString(CFG_MODEL_MATH, modelMath);
  }
}
