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

class JoinerNodeSettings {

  private static final String CFG_JOIN_SCRIPT = "JoinScript";
  private static final String CFG_MODEL_METADATA = "modelMetaData";
  private static final String CFG_MODEL_MATH1 = "modelMath1";
  private static final String CFG_MODEL_MATH2 = "modelMath2";

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  String modelMetaData;

  String modelMath1;
  String modelMath2;

  JoinRelation[] connections;

  void load(final NodeSettingsRO settings) throws InvalidSettingsException {

    // Load connections from string in settings
    String connectionString = settings.getString(CFG_JOIN_SCRIPT, "");
    if (!connectionString.isEmpty()) {
      try {
        connections = MAPPER.readValue(connectionString, JoinRelation[].class);
      } catch (IOException err) {
        // do nothing
      }
    }

    modelMetaData = settings.getString(CFG_MODEL_METADATA, "");
    modelMath1 = settings.getString(CFG_MODEL_MATH1, "");
    modelMath2 = settings.getString(CFG_MODEL_MATH2, "");
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
    settings.addString(CFG_MODEL_MATH1, modelMath1);
    settings.addString(CFG_MODEL_MATH2, modelMath2);
  }
}
