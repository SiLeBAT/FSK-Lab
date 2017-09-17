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
 ***************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.io.IOException;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;

public class EditorNodeSettings {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(EditorNodeSettings.class);

  String originalModelScript;
  String originalParametersScript;
  String originalVisualizationScript;

  String modifiedModelScript;
  String modifiedParametersScript;
  String modifiedVisualizationScript;

  GenericModel genericModel;

  /**
   * Saves the settings into the given node settings object.
   * 
   * @param settings a node settings object
   * @throws JsonProcessingException
   */
  void saveSettings(final NodeSettingsWO settings) {

    settings.addString("originalModelScript", originalModelScript);
    settings.addString("originalParametersScript", originalParametersScript);
    settings.addString("originalVisualizationScript", originalVisualizationScript);

    settings.addString("modifiedModelScript", modifiedModelScript);
    settings.addString("modifiedParametersScript", modifiedParametersScript);
    settings.addString("modifiedVisualizationScript", modifiedVisualizationScript);

    // save meta data
    if (genericModel != null) {
      final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      try {
        String stringVal = objectMapper.writeValueAsString(genericModel);
        settings.addString("metaData", stringVal);
      } catch (JsonProcessingException exception) {
        LOGGER.warn("Error saving meta data", exception);
      }
    }
  }

  /**
   * Loads the settings from the given node settings object.
   * 
   * @param settings a node settings object.
   * @throws InvalidSettingsException
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

    originalModelScript = settings.getString("originalModelScript");
    originalParametersScript = settings.getString("originalParametersScript");
    originalVisualizationScript = settings.getString("originalVisualizationScript");

    modifiedModelScript = settings.getString("modifiedModelScript");
    modifiedParametersScript = settings.getString("modifiedParametersScript");
    modifiedVisualizationScript = settings.getString("modifiedVisualizationScript");

    // load meta data
    if (settings.containsKey("metaData")) {
      final String stringVal = settings.getString("metaData");
      final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      try {
        genericModel = objectMapper.readValue(stringVal, GenericModel.class);
      } catch (IOException e) {
        throw new InvalidSettingsException(e);
      }
    }
  }
}
