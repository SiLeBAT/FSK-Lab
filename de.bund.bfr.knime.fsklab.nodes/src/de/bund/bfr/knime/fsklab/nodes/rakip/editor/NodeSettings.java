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
package de.bund.bfr.knime.fsklab.nodes.rakip.editor;

import java.io.IOException;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;

import de.bund.bfr.rakip.generic.GeneralInformation;
import de.bund.bfr.rakip.generic.GenericModel;
import de.bund.bfr.rakip.generic.RakipModule;

class NodeSettings {

	String originalModelScript;
	String originalParametersScript;
	String originalVisualizationScript;

	String modifiedModelScript;
	String modifiedParametersScript;
	String modifiedVisualizationScript;
	
	// JSON mapper to process metadata
	static ObjectMapper objectMapper = ExtensionsKt.jacksonObjectMapper().registerModule(new RakipModule());

	GenericModel genericModel;

	/**
	 * Saves the settings into the given node settings object.
	 *
	 * @param settings
	 *            a node settings object
	 */
	public void saveSettings(final NodeSettingsWO settings) {
		settings.addString("originalModelScript", originalModelScript);
		settings.addString("originalParametersScript", originalParametersScript);
		settings.addString("originalVisualizationScript", originalVisualizationScript);

		settings.addString("modifiedModelScript", modifiedModelScript);
		settings.addString("modifiedParametersScript", modifiedParametersScript);
		settings.addString("modifiedVisualizationScript", modifiedVisualizationScript);

		// Save meta data
		String jsonString;
		try {
			jsonString = objectMapper.writeValueAsString(genericModel);
			settings.addString("genericModel", jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the settings from the given node settings object.
	 *
	 * @param settings
	 *            a node settings object
	 */
	public void loadSettings(final NodeSettingsRO settings) {
		originalModelScript = settings.getString("originalModelScript", "");
		originalParametersScript = settings.getString("originalParametersScript", "");
		originalVisualizationScript = settings.getString("originalVisualizationScript", "");

		modifiedModelScript = settings.getString("modifiedModelScript", "");
		modifiedParametersScript = settings.getString("modifiedParametersScript", "");
		modifiedVisualizationScript = settings.getString("modifiedVisualizationScript", "");

		// Load meta data
		String jsonString;
		try {
			jsonString = settings.getString("genericModel");
			objectMapper.readValue(jsonString, GeneralInformation.class);
		} catch (InvalidSettingsException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
