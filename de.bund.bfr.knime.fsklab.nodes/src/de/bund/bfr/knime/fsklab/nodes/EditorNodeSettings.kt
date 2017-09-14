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
package de.bund.bfr.knime.fsklab.nodes

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import de.bund.bfr.knime.fsklab.rakip.GenericModel
import de.bund.bfr.knime.fsklab.rakip.RakipModule
import org.knime.core.node.InvalidSettingsException
import org.knime.core.node.NodeLogger
import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import java.io.IOException

private val LOGGER = NodeLogger.getLogger("EditorNodeSettings")

class EditorNodeSettings {

    var originalModelScript = ""
    var originalParametersScript = ""
    var originalVisualizationScript = ""

    var modifiedModelScript = ""
    var modifiedParametersScript = ""
    var modifiedVisualizationScript = ""

    var genericModel = GenericModel()

    /**
     * Saves the settings into the given node settings object.
     *
     * @param settings a node settings object
     * @throws [JsonProcessingException]
     */
    fun saveSettings(settings: NodeSettingsWO) {

        settings.addString("originalModelScript", originalModelScript)
        settings.addString("originalParametersScript", originalParametersScript)
        settings.addString("originalVisualizationScript", originalVisualizationScript)

        settings.addString("modifiedModelScript", modifiedModelScript)
        settings.addString("modifiedParametersScript", modifiedParametersScript)
        settings.addString("modifiedVisualizationScript", modifiedVisualizationScript)

        // Save meta data
        val objectMapper = ObjectMapper().registerModule(RakipModule())
        try {
            val stringVal = objectMapper.writeValueAsString(genericModel)
            settings.addString("metaData", stringVal)
        } catch (exception: JsonProcessingException) {
            LOGGER.warn("Error saving metadata", exception)
        }
    }

    /**
     * Loads the settings from the given node settings object.
     *
     * @param settings a node settings object
     */
    fun loadSettings(settings: NodeSettingsRO) {

        originalModelScript = settings.getString("originalModelScript")
        originalParametersScript = settings.getString("originalParametersScript")
        originalVisualizationScript = settings.getString("originalVisualizationScript")

        modifiedModelScript = settings.getString("modifiedModelScript")
        modifiedParametersScript = settings.getString("modifiedParametersScript")
        modifiedVisualizationScript = settings.getString("modifiedVisualizationScript")

        // load metadata
        if (settings.containsKey("metaData")) {
            val stringVal: String = settings.getString("metaData")
            val objectMapper: ObjectMapper = ObjectMapper().registerModule(RakipModule())
            genericModel = try {
                objectMapper.readValue(stringVal, GenericModel::class.java)
            } catch (exception: IOException) {
                throw InvalidSettingsException(exception)
            }
        }
    }
}