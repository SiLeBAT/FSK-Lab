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

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.FSKEditorJSNodeDialog.ModelType;

class FSKEditorJSConfig {

  private static final String METADATA = "ModelMetaData";
  private static final String MODEL_SCRIPT = "modelScript";
  private static final String VISUALIZATION_SCRIPT = "visualizationScript";
  private static final String README = "readme";
  private static final String RESOURCES = "resources";
  private static final String SERVER_NAME = "serverName";
  private static final String COMPLETED = "completed";
  private static final String ERRORS = "errors";
  private static final String WORKING_DIRECTORY = "workingDirectory";
  private static final String MODEL_TYPE = "modelType";

  private String m_metadata;
  private String m_modelScript;
  private String m_visualizationScript;
  private String m_readme;
  private String[] m_resourceFiles;
  private String m_serverName;
  private boolean m_isCompleted;
  private String[] m_validationErrors;
  private String m_modelType;
  
  /** UUID of the connected node. */
  private String m_connectedNode;
  
  /** Paths to resources: plain text files and R workspace files (.rdata). */
  private String m_workingDirectory;
  
  public String getModelMetaData() {
    return m_metadata;
  }

  public void setModelMetaData(String modelMetaData) {
    this.m_metadata = modelMetaData;
  }

  public String getModelScript() {
    return m_modelScript;
  }

  public void setModelScript(String modelScript) {
    this.m_modelScript = modelScript;
  }

  public String getVisualizationScript() {
    return m_visualizationScript;
  }

  public void setVisualizationScript(String visualizationScript) {
    this.m_visualizationScript = visualizationScript;
  }

  public String[] getResources() {
    return resources;
  }
  
  public void setResources(String[] resources) {
    this.resources = resources;
  }
  
  public String getServerName() {
    return m_serverName;
  }

  public void setServerName(String serverName) {
    this.m_serverName = serverName;
  }

  public boolean isCompleted() {
    return m_isCompleted;
  }

  public void setCompleted(boolean isCompleted) {
    this.m_isCompleted = isCompleted;
  }

  public String[] getValidationErrors() {
    return m_validationErrors;
  }

  public void setValidationErrors(String[] validationErrors) {
    this.m_validationErrors = validationErrors;
  }

  public String getReadme() {
    return m_readme;
  }

  public void setReadmeFile(String readme) {
    this.m_readme = readme;
  }

  /** Resources that will be load into the working directory */
  private String[] resources;

  /** @return empty string if not set. */
  public String getWorkingDirectory() {
    return m_workingDirectory != null ? m_workingDirectory : "";
  }

  public void setWorkingDirectory(String workingDirectory) {
    if (workingDirectory != null && !workingDirectory.isEmpty()) {
      this.m_workingDirectory = workingDirectory;
    }
  }
  
  public String getConnectedNode() {
    return m_connectedNode;
  }
  
  public void setConnectedNode(String connectedNode) {
    this.m_connectedNode = connectedNode;
  }
  
  public String getModelType() {
    return m_modelType;
  }
  
  public void setModelType(String modelType) {
    m_modelType = modelType;
  }

  /**
   * Saves current parameters to settings object.
   * 
   * @param settings To save to.
   */
  public void saveSettings(final NodeSettingsWO settings) {
    settings.addString(METADATA, m_metadata);
    settings.addString(MODEL_SCRIPT, m_modelScript);
    settings.addString(VISUALIZATION_SCRIPT, m_visualizationScript);
    settings.addString(README, m_readme);
    settings.addStringArray(RESOURCES, m_resourceFiles);
    settings.addString(SERVER_NAME, m_serverName);
    settings.addBoolean(COMPLETED, m_isCompleted);
    settings.addStringArray(ERRORS, m_validationErrors);
    settings.addString(MODEL_TYPE, m_modelType);
  }

  /**
   * Loads parameters in NodeModel.
   * 
   * @param settings To load from.
   * @throws InvalidSettingsException If incomplete or wrong.
   */
  public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    m_metadata = settings.getString(METADATA);
    m_modelScript = settings.getString(MODEL_SCRIPT, "");
    m_visualizationScript = settings.getString(VISUALIZATION_SCRIPT, "");
    m_readme = settings.getString(README, "");
    resources = settings.getStringArray(RESOURCES);
    m_serverName = settings.getString(SERVER_NAME, "");
    m_isCompleted = settings.getBoolean(COMPLETED, false);
    m_validationErrors = settings.getStringArray(ERRORS, new String[0]);
    m_workingDirectory = settings.getString(WORKING_DIRECTORY, "");
    m_modelType = settings.getString(MODEL_TYPE, ModelType.genericModel.name());
  }

  /**
   * Loads parameters in Dialog.
   * 
   * @param settings To load from.
   * @param spec the {@link DataTableSpec} to use for loading
   */
  public void loadSettingsForDialog(final NodeSettingsRO settings, final PortObjectSpec spec) {
    m_metadata = settings.getString(METADATA, "");
    m_modelScript = settings.getString(MODEL_SCRIPT, "");
    m_visualizationScript = settings.getString(VISUALIZATION_SCRIPT, "");
    m_readme = settings.getString(README, "");
    resources = settings.getStringArray(RESOURCES, "");
    m_serverName = settings.getString(SERVER_NAME, "");
    m_isCompleted = settings.getBoolean(COMPLETED, false);
    m_validationErrors = settings.getStringArray(ERRORS, "");
    m_workingDirectory = settings.getString(WORKING_DIRECTORY, "");
    m_modelType = settings.getString(MODEL_TYPE, ModelType.genericModel.name());
  }
}
