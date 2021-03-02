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
package de.bund.bfr.knime.fsklab.v2_0.editor;

import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.editor.FSKEditorJSNodeDialog.ModelType;

class FSKEditorJSViewValue extends JSONViewContent {

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  private static final String CFG_MODEL_METADATA = "ModelMetaData";
  private static final String CFG_ORIGINAL_MODEL_SCRIPT = "originalModelScript";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT = "originalVisualizationScript";
  private static final String CFG_ORIGINAL_README = "README";
  private static final String ENVIRONMENT = "environment";
  private static final String CFG_SERVER_NAME = "serverName";
  private static final String CFG_COMPLETED = "completed";
  private static final String CFG_VALIDATION_ERRORS = "validationErrors";
  private static final String MODEL_TYPE = "modelType";

  private String modelMetaData;
  private String modelScript;
  private String visualizationScript;
  private String readme;
  private EnvironmentManager environment;
  private String serverName;
  private boolean isCompleted;
  private String[] validationErrors;
  private String modelType;

  public FSKEditorJSViewValue() {
    modelScript = "";
    visualizationScript = "";
    readme = "";
    environment = null;
    serverName = "";
    isCompleted = false;
    validationErrors = new String[0];
    modelType = ModelType.genericModel.name();
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    settings.addString(CFG_MODEL_METADATA, modelMetaData);
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT, modelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, visualizationScript);
    settings.addString(CFG_ORIGINAL_README, readme);

    if (environment != null) {
      try {
        byte[] environmentBytes = MAPPER.writeValueAsBytes(environment);
        settings.addByteArray(ENVIRONMENT, environmentBytes);
      } catch (JsonProcessingException e) {
      }
    }

    settings.addString(CFG_SERVER_NAME, serverName);
    settings.addBoolean(CFG_COMPLETED, isCompleted);
    settings.addStringArray(CFG_VALIDATION_ERRORS, validationErrors);
    settings.addString(MODEL_TYPE, modelType);
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {

    modelMetaData = settings.getString(CFG_MODEL_METADATA);
    // fix for old broken strings. Previously some strings were escaped and wrapped with
    // extra quotes at the beginning and end.
    if (modelMetaData.startsWith("\"") && modelMetaData.endsWith("\"")) {
      modelMetaData = StringEscapeUtils.unescapeJson(modelMetaData);
      modelMetaData = modelMetaData.substring(1, modelMetaData.length() - 1);
    }

    modelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT);
    visualizationScript = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);
    readme = settings.getString(CFG_ORIGINAL_README);

    if (settings.containsKey(ENVIRONMENT)) {
      try {
        byte[] environmentBytes = settings.getByteArray(ENVIRONMENT);
        environment = MAPPER.readValue(environmentBytes, EnvironmentManager.class);
      } catch (IOException e) {
      }
    }

    serverName = settings.getString(CFG_SERVER_NAME);
    isCompleted = settings.getBoolean(CFG_COMPLETED);
    validationErrors = settings.getStringArray(CFG_VALIDATION_ERRORS);
    modelType = settings.getString(MODEL_TYPE, ModelType.genericModel.name());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    FSKEditorJSViewValue other = (FSKEditorJSViewValue) obj;
    return new EqualsBuilder().append(modelMetaData, other.modelMetaData)
        .append(modelScript, other.modelScript)
        .append(visualizationScript, other.visualizationScript).append(serverName, other.serverName)
        .append(isCompleted, other.isCompleted).append(validationErrors, other.validationErrors)
        .append(modelType, other.modelType).isEquals();
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelMetaData, modelScript, visualizationScript, readme, serverName,
        isCompleted, validationErrors, modelType);
  }

  public String getModelMetaData() {
    return modelMetaData;
  }

  public void setModelMetaData(String modelMetaData) {
    this.modelMetaData = modelMetaData;
  }

  public String getModelScript() {
    return modelScript;
  }

  public void setModelScript(String modelScript) {
    this.modelScript = modelScript;
  }

  public String getVisualizationScript() {
    return visualizationScript;
  }

  public void setVisualizationScript(String visualizationScript) {
    this.visualizationScript = visualizationScript;
  }

  public String getReadme() {
    return readme;
  }

  public void setReadme(String readme) {
    this.readme = readme;
  }

  public EnvironmentManager getEnvironment() {
    return environment;
  }

  public void setEnvironment(EnvironmentManager environment) {
    this.environment = environment;
  }

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public String[] getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(String[] validationErrors) {
    this.validationErrors = validationErrors;
  }

  public String getModelType() {
    return modelType;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  /**
   * Utility check for checking quickly if this view value is empty. A view value with all its
   * mandatory properties not set is considered empty.
   */
  public boolean isEmpty() {
    return StringUtils.isEmpty(modelMetaData) && StringUtils.isEmpty(modelScript)
        && StringUtils.isEmpty(visualizationScript);
  }
}
