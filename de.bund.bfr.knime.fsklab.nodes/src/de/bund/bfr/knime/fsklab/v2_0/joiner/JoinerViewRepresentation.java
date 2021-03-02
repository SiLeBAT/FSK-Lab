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
package de.bund.bfr.knime.fsklab.v2_0.joiner;

import java.io.IOException;
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


@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
final class JoinerViewRepresentation extends JSONViewContent {

  private static final String CFG_MODELS_DATA = "JoinerModelsData";
  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  public String modelMetaData;
  public JoinerModelsData joinerModelsData = new JoinerModelsData();
  /**
   * Port number where FSK-Service is running.
   * 
   * <p>
   * The service port number is to not be saved to settings as it depends on the computer. The same
   * port cannot be available in the future. So it is assigned during runtime by the node model.
   * </p>
   */
  private int servicePort;
  
  public int getServicePort() {
    return servicePort;
  }
  
  public void setServicePort(int servicePort) {
    this.servicePort = servicePort;
  }
  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    try {
      String joinerModelsDataAsString = MAPPER.writeValueAsString(joinerModelsData);
      settings.addString(CFG_MODELS_DATA, joinerModelsDataAsString);
    } catch (JsonProcessingException err) {
      // do nothing
    }
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    try {
      joinerModelsData =
          MAPPER.readValue(settings.getString(CFG_MODELS_DATA), JoinerModelsData.class);
    } catch (IOException err) {
      // do nothing
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(joinerModelsData);
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
    return joinerModelsData.equals(other.joinerModelsData);
  }
}
