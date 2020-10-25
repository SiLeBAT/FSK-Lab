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
  private static final String CFG_MODEL3_PARAMETERS = "params3";
  private static final String CFG_MODEL4_PARAMETERS = "params4";
  private static final String CFG_FIRST_MODEL_NAME = "firstModelName";
  private static final String CFG_SECOND_MODEL_NAME = "secondModelName";
  private static final String CFG_THIRD_MODEL_NAME = "thirdModelName";
  private static final String CFG_FOURTH_MODEL_NAME = "fourthModelName";
  private static final String CFG_FIRST_MODEL = "FirstModel";
  private static final String CFG_SECOND_MODEL = "SecondModel";
  private static final String CFG_THIRD_MODEL = "ThirdModel";
  private static final String CFG_FOURTH_MODEL = "FourthModel";
  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  private Parameter[] firstModelParameters;
  private Parameter[] secondModelParameters;
  private Parameter[] thirdModelParameters;
  private Parameter[] fourthModelParameters;

  private String firstModelName;
  private String secondModelName;
  private String thirdModelName;
  private String fourthModelName;

  public String[] firstModel;
  public String[] secondModel;
  public String[] thirdModel;
  public String[] fourthModel;

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

  public Parameter[] getThirdModelParameters() {
    return thirdModelParameters;
  }

  public void setThirdModelParameters(Parameter[] thirdModelParameters) {
    this.thirdModelParameters = thirdModelParameters;
  }

  public Parameter[] getFourthModelParameters() {
    return fourthModelParameters;
  }

  public void setFourthModelParameters(Parameter[] fourthModelParameters) {
    this.fourthModelParameters = fourthModelParameters;
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

  public String getThirdModelName() {
    return thirdModelName;
  }

  public void setThirdModelName(String thridModelName) {
    this.thirdModelName = thridModelName;
  }

  public String getFourthModelName() {
    return fourthModelName;
  }

  public void setFourthModelName(String fourthModelName) {
    this.fourthModelName = fourthModelName;
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

    if (thirdModelParameters != null && thirdModelParameters.length > 0) {
      try {
        String parametersAsString = MAPPER.writeValueAsString(thirdModelParameters);
        settings.addString(CFG_MODEL3_PARAMETERS, parametersAsString);
      } catch (JsonProcessingException err) {
        // do nothing
      }
    }

    if (fourthModelParameters != null && fourthModelParameters.length > 0) {
      try {
        String parametersAsString = MAPPER.writeValueAsString(fourthModelParameters);
        settings.addString(CFG_MODEL4_PARAMETERS, parametersAsString);
      } catch (JsonProcessingException err) {
        // do nothing
      }
    }

    settings.addString(CFG_FIRST_MODEL_NAME, firstModelName);
    settings.addString(CFG_SECOND_MODEL_NAME, secondModelName);
    settings.addString(CFG_THIRD_MODEL_NAME, thirdModelName);
    settings.addString(CFG_FOURTH_MODEL_NAME, fourthModelName);
    try {
      String firstModelAsString = MAPPER.writeValueAsString(firstModel);
      settings.addString(CFG_FIRST_MODEL, firstModelAsString);

      String secondModelAsString = MAPPER.writeValueAsString(secondModel);
      settings.addString(CFG_SECOND_MODEL, secondModelAsString);

      String thirdModelAsString = MAPPER.writeValueAsString(thirdModel);
      settings.addString(CFG_THIRD_MODEL, thirdModelAsString);

      String fourthModelAsString = MAPPER.writeValueAsString(firstModel);
      settings.addString(CFG_FOURTH_MODEL, fourthModelAsString);
    } catch (JsonProcessingException err) {
      // do nothing
    }
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

    if (settings.containsKey(CFG_MODEL3_PARAMETERS)) {
      try {
        thirdModelParameters =
            MAPPER.readValue(settings.getString(CFG_MODEL3_PARAMETERS), Parameter[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    if (settings.containsKey(CFG_MODEL4_PARAMETERS)) {
      try {
        fourthModelParameters =
            MAPPER.readValue(settings.getString(CFG_MODEL4_PARAMETERS), Parameter[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    firstModelName = settings.getString(CFG_FIRST_MODEL_NAME);
    secondModelName = settings.getString(CFG_SECOND_MODEL_NAME);
    thirdModelName = settings.getString(CFG_THIRD_MODEL_NAME);
    fourthModelName = settings.getString(CFG_FOURTH_MODEL_NAME);

    try {
      firstModel = MAPPER.readValue(settings.getString(CFG_FIRST_MODEL), String[].class);
      secondModel = MAPPER.readValue(settings.getString(CFG_SECOND_MODEL), String[].class);
      thirdModel = MAPPER.readValue(settings.getString(CFG_THIRD_MODEL), String[].class);
      fourthModel = MAPPER.readValue(settings.getString(CFG_FOURTH_MODEL), String[].class);
    } catch (IOException err) {
      // do nothing
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(firstModelParameters, secondModelParameters, thirdModelParameters,
        fourthModelParameters, firstModelName, secondModelName, thirdModelName, fourthModelName,
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
        && Arrays.deepEquals(thirdModelParameters, other.thirdModelParameters)
        && Arrays.deepEquals(fourthModelParameters, other.fourthModelParameters)
        && firstModelName.equals(other.firstModelName)
        && secondModelName.equals(other.secondModelName)
        && thirdModelName.equals(other.thirdModelName)
        && fourthModelName.equals(other.fourthModelName) && modelType.equals(other.modelType);
  }
}
