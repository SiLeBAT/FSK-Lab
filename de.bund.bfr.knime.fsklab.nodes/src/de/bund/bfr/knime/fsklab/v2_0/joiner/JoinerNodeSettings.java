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
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelation;

class JoinerNodeSettings {

  private static final String CFG_JOIN_SCRIPT = "joinRelation";
  private static final String CFG_MODEL_METADATA = "modelMetaData";
  private static final String CFG_JSON_REPRESENTATION = "JSONRepresentation";

  private static final String CFG_MODELS_DATA = "JoinerModelsData";

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  String modelMetaData;

  JoinRelation[] connections;

  JoinerModelsData joinerModelsData;

  String jsonRepresentation;

  void load(final NodeSettingsRO settings) throws InvalidSettingsException {

    // Load connections from string in settings
    if (settings.containsKey(CFG_JOIN_SCRIPT)) {
      try {
        connections = MAPPER.readValue(settings.getString(CFG_JOIN_SCRIPT), JoinRelation[].class);
        joinerModelsData =
            MAPPER.readValue(settings.getString(CFG_MODELS_DATA), JoinerModelsData.class);
      } catch (IOException err) {
        // do nothing
      }
    }

    modelMetaData = settings.getString(CFG_MODEL_METADATA, "");
    jsonRepresentation = settings.getString(CFG_JSON_REPRESENTATION);

  }

  void save(final NodeSettingsWO settings) {

    // Save connections as a string in settings
    try {
      String connectionString = MAPPER.writeValueAsString(connections);
      settings.addString(CFG_JOIN_SCRIPT, connectionString);

      String joinerModelsDataAsString = MAPPER.writeValueAsString(joinerModelsData);
      settings.addString(CFG_MODELS_DATA, joinerModelsDataAsString);
    } catch (JsonProcessingException err) {
      // do nothing
    }

    settings.addString(CFG_MODEL_METADATA, modelMetaData);
    settings.addString(CFG_JSON_REPRESENTATION, jsonRepresentation);

  }
}
