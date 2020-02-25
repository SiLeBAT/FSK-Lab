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
  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  private Parameter[] firstModelParameters;
  private Parameter[] secondModelParameters;
  
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
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstModelParameters, secondModelParameters);
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
    
    JoinerViewRepresentation other = (JoinerViewRepresentation)obj;
    return Arrays.deepEquals(firstModelParameters, other.firstModelParameters) &&
        Arrays.deepEquals(secondModelParameters, other.secondModelParameters);
  }
}
