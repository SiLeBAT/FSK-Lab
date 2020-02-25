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
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.metadata.swagger.Parameter;

class JoinerNodeSettings {

  private static final String CFG_JOIN_SCRIPT = "JoinScript";
  private static final String CFG_MODEL_METADATA = "modelMetaData";
  private static final String CFG_MODEL_MATH1 = "modelMath1";
  private static final String CFG_MODEL_MATH2 = "modelMath2";

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  String modelMetaData;

  JoinRelation[] connections;
  Parameter[] firstModelParameters;
  Parameter[] secondModelParameters;

  void load(final NodeSettingsRO settings) throws InvalidSettingsException {

    // Load connections from string in settings
    if (settings.containsKey(CFG_JOIN_SCRIPT)) {
      try {
        connections = MAPPER.readValue(settings.getString(CFG_JOIN_SCRIPT), JoinRelation[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    modelMetaData = settings.getString(CFG_MODEL_METADATA, "");

    if (settings.containsKey(CFG_MODEL_MATH1)) {
      try {
        firstModelParameters =
            MAPPER.readValue(settings.getString(CFG_MODEL_MATH1), Parameter[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    if (settings.containsKey(CFG_MODEL_MATH2)) {
      try {
        secondModelParameters =
            MAPPER.readValue(settings.getString(CFG_MODEL_MATH2), Parameter[].class);
      } catch (IOException err) {
        // do nothing
      }
    }
  }

  void save(final NodeSettingsWO settings) {

    // Save connections as a string in settings
    try {
      String connectionString = MAPPER.writeValueAsString(connections);
      settings.addString(CFG_JOIN_SCRIPT, connectionString);
    } catch (JsonProcessingException err) {
      // do nothing
    }

    settings.addString(CFG_MODEL_METADATA, modelMetaData);
    
    try {
      settings.addString(CFG_MODEL_MATH1, MAPPER.writeValueAsString(firstModelParameters));
    } catch (JsonProcessingException err) {
      // do nothing
    }
    
    try {
      settings.addString(CFG_MODEL_MATH2, MAPPER.writeValueAsString(secondModelParameters));
    } catch (JsonProcessingException err) {
      // do nothing
    }
  }
}
