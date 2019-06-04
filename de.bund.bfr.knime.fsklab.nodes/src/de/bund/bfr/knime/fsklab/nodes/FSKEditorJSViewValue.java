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
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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

  private static final String CFG_GENERAL_INFORMATION = "generalInformation";
  private static final String CFG_SCOPE = "scope";
  private static final String CFG_DATA_BACKGROUND = "dataBackground";
  private static final String CFG_MODEL_MATH = "modelMath";

  final static ResourceSet resourceSet = new ResourceSetImpl();

  public final int pseudoIdentifier = (new Random()).nextInt();

  private String generalInformation;
  private String scope;
  private String dataBackground;
  private String modelMath;

  private String firstModelScript;
  private String firstModelViz;
  private String readme;

  public String getReadme() {
    return readme;
  }

  public void setReadme(String readme) {
    this.readme = readme;
  }

  private String[] resourcesFiles;
  private String serverName;
  private boolean notCompleted;
  private String validationErrors;

  public String getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(String validationErrors) {
    this.validationErrors = validationErrors;
  }

  public boolean isNotCompleted() {
    return notCompleted;
  }

  public void setNotCompleted(boolean notCompleted) {
    this.notCompleted = notCompleted;
  }

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public String[] getResourcesFiles() {
    return resourcesFiles;
  }

  public void setResourcesFiles(String[] resourcesFiles) {
    this.resourcesFiles = resourcesFiles;
  }

  public String getFirstModelScript() {
    return firstModelScript;
  }

  public void setFirstModelScript(String firstModelScript) {
    this.firstModelScript = firstModelScript;
  }

  public String getFirstModelViz() {
    return firstModelViz;
  }

  public void setFirstModelViz(String firstModelViz) {
    this.firstModelViz = firstModelViz;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT, firstModelScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, firstModelViz);
    settings.addString(CFG_ORIGINAL_README, readme);

    if (generalInformation != null) {
      System.out.println(generalInformation);
      saveSettings(settings, CFG_GENERAL_INFORMATION, generalInformation);
    }

    if (scope != null) {
      saveSettings(settings, CFG_SCOPE, scope);
    }

    if (dataBackground != null) {
      saveSettings(settings, CFG_DATA_BACKGROUND, dataBackground);
    }

    if (modelMath != null) {
      System.out.println(modelMath);
      saveSettings(settings, CFG_MODEL_MATH, modelMath);
    }
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    firstModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT);
    firstModelViz = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);
    readme = settings.getString(CFG_ORIGINAL_README);

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
  }

  private static void saveSettings(final NodeSettingsWO settings, final String key,
      final String eObject) {

    try {
      ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      String jsonStr = objectMapper.writeValueAsString(eObject);
      System.out.println(jsonStr);
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
}
