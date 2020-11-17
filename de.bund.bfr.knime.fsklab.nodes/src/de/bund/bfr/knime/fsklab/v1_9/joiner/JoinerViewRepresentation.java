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
package de.bund.bfr.knime.fsklab.v1_9.joiner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.metadata.swagger.Parameter;


@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
final class JoinerViewRepresentation extends JSONViewContent {

  private static final String CFG_MODEL1_PARAMETERS = "params1";
  private static final String CFG_MODEL2_PARAMETERS = "params2";
  private static final String CFG_FIRST_MODEL_NAME = "firstModelName";
  private static final String CFG_FIRST_MODEL_SCRIPT = "firstModelScript";
  private static final String CFG_FIRST_MODEL_VISUALIZATION = "firstModelViz";
  private static final String CFG_SECOND_MODEL_NAME = "secondModelName";
  private static final String CFG_SECOND_MODEL_SCRIPT = "secondModelScript";
  private static final String CFG_SECOND_MODEL_VISUALIZATION = "secondModelViz";
  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  private Parameter[] firstModelParameters;
  private Parameter[] secondModelParameters;
  private String firstModelName;
  private String firstModelScript;
  private String firstModelViz;
  private String secondModelName;
  private String secondModelScript;
  private String secondModelViz;
  private String modelType;

  public Parameter[] getFirstModelParameters() {
    return firstModelParameters;
  }

  public void setFirstModelParameters(Parameter[] firstModelParameters) {
    this.firstModelParameters = firstModelParameters;
  }

  public Parameter[] getSecondModelParameters() {
    return secondModelParameters;
  }

  public void setSecondModelParameters(Parameter[] secondModelParameters) {
    this.secondModelParameters = secondModelParameters;
  }

  public String getFirstModelName() {
    return firstModelName;
  }

  public void setFirstModelName(String firstModelName) {
    this.firstModelName = firstModelName;
  }

  public String getSecondModelName() {
    return secondModelName;
  }

  public void setSecondModelName(String secondModelName) {
    this.secondModelName = secondModelName;
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

  public String getSecondModelScript() {
    return secondModelScript;
  }

  public void setSecondModelScript(String secondModelScript) {
    this.secondModelScript = secondModelScript;
  }

  public String getSecondModelViz() {
    return secondModelViz;
  }

  public void setSecondModelViz(String secondModelViz) {
    this.secondModelViz = secondModelViz;
  }

  public String getModelType() {
    return modelType;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {

    if (firstModelParameters != null && firstModelParameters.length > 0) {
      try {
        String parametersAsString = MAPPER.writeValueAsString(firstModelParameters);
        settings.addString(CFG_MODEL1_PARAMETERS, parametersAsString);
      } catch (JsonProcessingException err) {
        // do nothing
      }
    }

    if (secondModelParameters != null && secondModelParameters.length > 0) {
      try {
        String parametersAsString = MAPPER.writeValueAsString(secondModelParameters);
        settings.addString(CFG_MODEL2_PARAMETERS, parametersAsString);
      } catch (JsonProcessingException err) {
        // do nothing
      }
    }

    settings.addString(CFG_FIRST_MODEL_NAME, firstModelName);
    settings.addString(CFG_FIRST_MODEL_SCRIPT, firstModelScript);
    settings.addString(CFG_FIRST_MODEL_VISUALIZATION, firstModelViz);
    settings.addString(CFG_SECOND_MODEL_NAME, secondModelName);
    settings.addString(CFG_SECOND_MODEL_SCRIPT, secondModelScript);
    settings.addString(CFG_SECOND_MODEL_VISUALIZATION, secondModelViz);
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {

    if (settings.containsKey(CFG_MODEL1_PARAMETERS)) {
      try {
        firstModelParameters =
            MAPPER.readValue(settings.getString(CFG_MODEL1_PARAMETERS), Parameter[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    if (settings.containsKey(CFG_MODEL2_PARAMETERS)) {
      try {
        secondModelParameters =
            MAPPER.readValue(settings.getString(CFG_MODEL2_PARAMETERS), Parameter[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    firstModelName = settings.getString(CFG_FIRST_MODEL_NAME);
    firstModelScript = settings.getString(CFG_FIRST_MODEL_SCRIPT);
    firstModelViz = settings.getString(CFG_FIRST_MODEL_VISUALIZATION);
    secondModelName = settings.getString(CFG_SECOND_MODEL_NAME);
    secondModelScript = settings.getString(CFG_SECOND_MODEL_SCRIPT);
    secondModelViz = settings.getString(CFG_SECOND_MODEL_VISUALIZATION);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstModelParameters, secondModelParameters, firstModelName,
        firstModelScript, firstModelViz, secondModelName, secondModelScript, secondModelViz,
        modelType);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }

    JoinerViewRepresentation other = (JoinerViewRepresentation) obj;
    return Arrays.deepEquals(firstModelParameters, other.firstModelParameters)
        && Arrays.deepEquals(secondModelParameters, other.secondModelParameters)
        && firstModelName.equals(other.firstModelName)
        && firstModelScript.equals(other.firstModelScript)
        && firstModelViz.equals(other.firstModelViz)
        && secondModelName.equals(other.secondModelName)
        && secondModelScript.equals(other.secondModelScript)
        && secondModelViz.equals(other.secondModelViz) && modelType.equals(other.modelType);
  }

  /**
   * a helper method for migrating parameter id in workflows written in older version
   * 
   * @param newFirstModelParameters new parameters with suffix
   * @param newSecondModelParameters new parameters with suffix
   */
  void updateParameters(List<Parameter> newFirstModelParameters,
      List<Parameter> newSecondModelParameters) {
    newFirstModelParameters.stream().forEach(newParam -> {
      for (Parameter paramTobeMigrated : firstModelParameters) {
        if (newParam.getId().startsWith(paramTobeMigrated.getId())) {
          paramTobeMigrated.setId(newParam.getId());
        }
      }
    });

    newSecondModelParameters.stream().forEach(newParam -> {
      for (Parameter paramTobeMigrated : secondModelParameters) {
        if (newParam.getId().startsWith(paramTobeMigrated.getId())) {
          paramTobeMigrated.setId(newParam.getId());
        }
      }
    });
  }
}
