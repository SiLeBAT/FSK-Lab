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
package de.bund.bfr.knime.fsklab.nodes.v1_7_2.joiner;

import java.util.Random;
import org.apache.commons.lang3.StringEscapeUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class JoinerViewValue extends JSONViewContent {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(JoinerViewValue.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();
  
  private static final String CFG_ORIGINAL_MODEL_SCRIPT = "originalModelScript";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT = "originalVisualizationScript";
  private static final String CFG_ORIGINAL_MODEL_SCRIPT2 = "originalModelScript2";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT2 = "originalVisualizationScript2";
  public final int pseudoIdentifier = (new Random()).nextInt();
  private static final String CFG_MODEL_METADATA = "ModelMetaData";

  

  private static final String CFG_MODEL_MATH1 = "modelMath1";
  private static final String CFG_MODEL_MATH2 = "modelMath2";

  private static final String CFG_JOINER_RELATION = "joinRelation";

  private static final String CFG_JSON_REPRESENTATION = "JSONRepresentation";
  private static final String CFG_MODELSCRIPT_TREE = "ModelScriptTree";

  private static final String FIRST_MODEL_NAME = "firstModelName";
  private static final String SECOND_MODEL_NAME = "secondModelName";
  public String firstModelScript;
  public String secondModelScript;

  public String firstModelViz;
  public String secondModelViz;

  public String modelMetaData;

  public String modelMath1;
  public String modelMath2;

  public String joinRelations;
  public String jsonRepresentation;
  public String svgRepresentation;
  public String modelScriptTree;

  public String firstModelName;
  public String secondModelName;
  
  public String validationErrors;
  public String different;
  public String modelType ;

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    settings.addString(FIRST_MODEL_NAME, firstModelName);
    settings.addString(SECOND_MODEL_NAME, secondModelName);
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT, firstModelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, firstModelViz);
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT2, secondModelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT2, secondModelViz);

    settings.addString(CFG_JOINER_RELATION, joinRelations);
    settings.addString(CFG_JSON_REPRESENTATION, jsonRepresentation);
    settings.addString(CFG_MODELSCRIPT_TREE, modelScriptTree);
    if (modelMetaData != null) {
      saveSettings(settings,CFG_MODEL_METADATA, modelMetaData);
    }

    if (modelMath1 != null) {
      saveSettings(settings, CFG_MODEL_MATH1, modelMath1);
    }
    if (modelMath2 != null) {
      saveSettings(settings, CFG_MODEL_MATH2, modelMath2);
    }
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    firstModelName = settings.getString(FIRST_MODEL_NAME);
    secondModelName = settings.getString(SECOND_MODEL_NAME);
    firstModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT);
    firstModelViz = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);
    secondModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT2);
    secondModelViz = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT2);
    joinRelations = settings.getString(CFG_JOINER_RELATION);
    jsonRepresentation = settings.getString(CFG_JSON_REPRESENTATION);
    modelScriptTree = settings.getString(CFG_MODELSCRIPT_TREE);
    // load meta data
    if (settings.containsKey(CFG_MODEL_METADATA)) {
      modelMetaData = getEObject(settings, CFG_MODEL_METADATA);
    }
    
    if (settings.containsKey(CFG_MODEL_MATH1)) {
      modelMath1 = getEObject(settings, CFG_MODEL_MATH1);
    }
    if (settings.containsKey(CFG_MODEL_MATH2)) {
      modelMath2 = getEObject(settings, CFG_MODEL_MATH2);
    }
  }

  private static void saveSettings(final NodeSettingsWO settings, final String key,
      final String eObject) {

    try {
      String jsonStr = MAPPER.writeValueAsString(eObject);
      settings.addString(key, jsonStr);
    } catch (JsonProcessingException exception) {
      LOGGER.warn("Error saving " + key);
    }
  }

  private static String getEObject(NodeSettingsRO settings, String key)
      throws InvalidSettingsException {

    String jsonStr = settings.getString(key);
    jsonStr = StringEscapeUtils.unescapeJson(jsonStr);
    jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
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
}
