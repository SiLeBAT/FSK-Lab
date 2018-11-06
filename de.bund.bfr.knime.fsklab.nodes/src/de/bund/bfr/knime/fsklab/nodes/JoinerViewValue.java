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



class JoinerViewValue extends JSONViewContent {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(JoinerViewValue.class);
  private static final String CFG_ORIGINAL_MODEL_SCRIPT = "originalModelScript";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT = "originalVisualizationScript";
  private static final String CFG_ORIGINAL_MODEL_SCRIPT2 = "originalModelScript2";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT2 = "originalVisualizationScript2";
  public final int pseudoIdentifier = (new Random()).nextInt();
  private static final String CFG_GENERAL_INFORMATION = "generalInformation";
  private static final String CFG_SCOPE = "scope";
  private static final String CFG_DATA_BACKGROUND = "dataBackground";
  private static final String CFG_MODEL_MATH = "modelMath";

  private static final String CFG_MODEL_MATH1 = "modelMath1";
  private static final String CFG_MODEL_MATH2 = "modelMath2";

  private static final String CFG_JOINER_RELATION = "joinRelation";

  private static final String CFG_JSON_REPRESENTATION = "JSONRepresentation";

  private String firstModelScript;
  private String secondModelScript;

  private String firstModelViz;
  private String secondModelViz;



  private String generalInformation;
  private String scope;
  private String dataBackground;
  private String modelMath;

  private String modelMath1;


  private String modelMath2;

  private String joinRelations;
  private String jsonRepresentation;
  private String svgRepresentation;
  private String modelScriptTree;

  public String getModelScriptTree() {
    return modelScriptTree;
  }

  public void setModelScriptTree(String modelScriptTree) {
    this.modelScriptTree = modelScriptTree;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT, firstModelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, firstModelViz);
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT2, secondModelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT2, secondModelViz);

    settings.addString(CFG_JOINER_RELATION, joinRelations);
    settings.addString(CFG_JSON_REPRESENTATION, jsonRepresentation);

    if (generalInformation != null) {
      saveSettings(settings, CFG_GENERAL_INFORMATION, generalInformation);
    }

    if (scope != null) {
      saveSettings(settings, CFG_SCOPE, scope);
    }

    if (dataBackground != null) {
      saveSettings(settings, CFG_DATA_BACKGROUND, dataBackground);
    }

    if (modelMath != null) {
      saveSettings(settings, CFG_MODEL_MATH, modelMath);
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
    firstModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT);
    firstModelViz = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);
    secondModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT2);
    secondModelViz = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT2);
    joinRelations = settings.getString(CFG_JOINER_RELATION);
    jsonRepresentation = settings.getString(CFG_JSON_REPRESENTATION);
    // load meta data
    if (settings.containsKey(CFG_GENERAL_INFORMATION)) {
      generalInformation = getEObject(settings, CFG_GENERAL_INFORMATION);
    }
    if (settings.containsKey(CFG_SCOPE)) {
      scope = getEObject(settings, CFG_SCOPE);
    }
    if (settings.containsKey(CFG_DATA_BACKGROUND)) {
      dataBackground = getEObject(settings, CFG_DATA_BACKGROUND);
    }
    if (settings.containsKey(CFG_MODEL_MATH)) {
      modelMath = getEObject(settings, CFG_MODEL_MATH);
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
      ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      String jsonStr = objectMapper.writeValueAsString(eObject);
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

  public String getFirstModelScript() {
    return firstModelScript;
  }

  public void setFirstModelScript(String firstModelScript) {
    this.firstModelScript = firstModelScript;
  }

  public String getSecondModelScript() {
    return secondModelScript;
  }

  public void setSecondModelScript(String secondModelScript) {
    this.secondModelScript = secondModelScript;
  }

  public String getFirstModelViz() {
    return firstModelViz;
  }

  public void setFirstModelViz(String firstModelViz) {
    this.firstModelViz = firstModelViz;
  }

  public String getSecondModelViz() {
    return secondModelViz;
  }

  public void setSecondModelViz(String secondModelViz) {
    this.secondModelViz = secondModelViz;
  }


  public String getGeneralInformation() {
    return generalInformation;
  }

  public void setGeneralInformation(String generalInformation) {
    this.generalInformation = generalInformation;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getDataBackground() {
    return dataBackground;
  }

  public void setDataBackground(String dataBackground) {
    this.dataBackground = dataBackground;
  }

  public String getModelMath() {
    return modelMath;
  }

  public void setModelMath(String modelMath) {
    this.modelMath = modelMath;
  }

  public String getModelMath1() {
    return modelMath1;
  }

  public void setModelMath1(String modelMath1) {
    this.modelMath1 = modelMath1;
  }

  public String getModelMath2() {
    return modelMath2;
  }

  public void setModelMath2(String modelMath2) {
    this.modelMath2 = modelMath2;
  }

  public String getJsonRepresentation() {
    return jsonRepresentation;
  }

  public String getJoinRelations() {
    return joinRelations;
  }

  public void setJoinRelations(String joinRelations) {
    this.joinRelations = joinRelations;
  }

  public void setJsonRepresentation(String jsonRepresentation) {
    this.jsonRepresentation = jsonRepresentation;
  }



  public String getSvgRepresentation() {
    return svgRepresentation;
  }

  public void setSvgRepresentation(String svgRepresentation) {
    this.svgRepresentation = svgRepresentation;
  }



}
