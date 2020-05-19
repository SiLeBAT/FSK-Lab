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

import java.util.Random;
import org.apache.commons.lang3.StringEscapeUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;

class FSKEditorJSViewValue extends JSONViewContent {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FSKEditorJSViewValue.class);
  private static final String CFG_ORIGINAL_MODEL_SCRIPT = "originalModelScript";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT = "originalVisualizationScript";
  private static final String CFG_ORIGINAL_README = "README";
  private static final String CFG_MODEL_METADATA = "ModelMetaData";

  public final int pseudoIdentifier = (new Random()).nextInt();

  private String modelMetaData;

  public String firstModelScript;
  public String firstModelViz;
  public String readme;

  public String[] resourcesFiles;
  public String serverName;
  public boolean notCompleted;
  public String[] validationErrors;
  public String modelType ;

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT, firstModelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, firstModelViz);
    settings.addString(CFG_ORIGINAL_README, readme);

    saveSettings(settings, CFG_MODEL_METADATA, getModelMetaData());
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    firstModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT);
    firstModelViz = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);
    readme = settings.getString(CFG_ORIGINAL_README);

    // load meta data
    setModelMetaData(getEObject(settings, CFG_MODEL_METADATA));
  }

  private static void saveSettings(final NodeSettingsWO settings, final String key,
      final String eObject) {

    if (eObject != null) {
      try {
        ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
        String jsonStr = objectMapper.writeValueAsString(eObject);
        settings.addString(key, jsonStr);
      } catch (JsonProcessingException exception) {
        LOGGER.warn("Error saving " + key);
      }
    }
  }

  private static String getEObject(NodeSettingsRO settings, String key)
      throws InvalidSettingsException {
    
    // If entry is missing return null.
    String jsonStr = settings.getString(key, null);
    if (jsonStr != null) {
      jsonStr = StringEscapeUtils.unescapeJson(jsonStr);
      jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
    }
    return jsonStr;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return false;
  }

  @Override
  public int hashCode() {
    return pseudoIdentifier;
  }

  public String getModelMetaData() {
    return modelMetaData;
  }

  public void setModelMetaData(String modelMetaData) {
    this.modelMetaData = modelMetaData;
  }
}
