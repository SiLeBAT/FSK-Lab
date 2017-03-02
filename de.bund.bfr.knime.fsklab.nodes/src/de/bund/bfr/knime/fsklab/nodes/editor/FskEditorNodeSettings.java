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
package de.bund.bfr.knime.fsklab.nodes.editor;

import java.util.Date;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.common.base.Strings;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

class FskEditorNodeSettings {

	String originalModelScript;
	String originalParametersScript;
	String originalVisualizationScript;

	String modifiedModelScript;
	String modifiedParametersScript;
	String modifiedVisualizationScript;

	FskMetaData metaData = new FskMetaData();

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
		settings.addString("modelName", metaData.modelName);
		settings.addString("modelId", metaData.modelId);
		settings.addString("modelLink", metaData.modelLink);
		settings.addString("organism", metaData.organism);
		settings.addString("organismDetails", metaData.organismDetails);
		settings.addString("matrix", metaData.matrix);
		settings.addString("matrixDetails", metaData.matrixDetails);
		settings.addString("creator", metaData.creator);
		settings.addString("familyName", metaData.familyName);
		settings.addString("contact", metaData.contact);
		settings.addString("software", metaData.software == null ? "" : metaData.software.name());
		settings.addString("referenceDescription", metaData.referenceDescription);
		settings.addString("referenceDescriptionLink", metaData.referenceDescriptionLink);
		settings.addLong("createdDate", metaData.createdDate == null ? 0 : metaData.createdDate.getTime());
		settings.addLong("modifiedDate", metaData.modifiedDate == null ? 0 : metaData.modifiedDate.getTime());
		settings.addString("notes", metaData.notes);
		settings.addBoolean("curated", metaData.curated);
		settings.addString("type", metaData.type == null ? "" : metaData.type.name());
		settings.addString("subject", metaData.subject == null ? "" : metaData.subject.name());
		settings.addString("foodProcess", metaData.foodProcess);
		settings.addBoolean("hasData", metaData.hasData);
	}

	/**
	 * Loads the settings from the given node settings object.
	 *
	 * @param settings
	 *            a node settings object
	 */
	public void loadSettings(final NodeSettingsRO settings){
		originalModelScript = settings.getString("originalModelScript", "");
		originalParametersScript = settings.getString("originalParametersScript", "");
		originalVisualizationScript = settings.getString("originalVisualizationScript", "");

		modifiedModelScript = settings.getString("modifiedModelScript", "");
		modifiedParametersScript = settings.getString("modifiedParametersScript", "");
		modifiedVisualizationScript = settings.getString("modifiedVisualizationScript", "");
		
		// Load meta data
		metaData.modelName = settings.getString("modelName", "");
		metaData.modelId = settings.getString("modelId", "");
		metaData.modelLink = settings.getString("modelLink", "");
		metaData.organism = settings.getString("organism", "");
		metaData.organismDetails = settings.getString("organismDetails", "");
		metaData.matrix = settings.getString("matrix", "");
		metaData.matrixDetails = settings.getString("matrixDetails", "");
		metaData.creator = settings.getString("creator", "");
		metaData.familyName = settings.getString("familyName", "");
		metaData.contact = settings.getString("contact", "");
		String softwareString = settings.getString("software", "");
		metaData.software = Strings.isNullOrEmpty(softwareString) ?
				null : FskMetaData.Software.valueOf(softwareString);
		metaData.referenceDescription = settings.getString("referenceDescription", "");
		metaData.referenceDescriptionLink = settings.getString("referenceDescriptionLink", "");
		metaData.createdDate = new Date(settings.getInt("createdDate", 0));
		metaData.modifiedDate = new Date(settings.getInt("modifiedDate", 0));
		metaData.notes = settings.getString("notes", "");
		metaData.curated = settings.getBoolean("curated", false);
		
		String typeString = settings.getString("type", "");
		metaData.type = typeString.isEmpty() ? null : ModelType.valueOf(typeString);
		
		String subjectString = settings.getString("subject", "");
		metaData.subject = subjectString.isEmpty() ? null : ModelClass.valueOf(subjectString);
		
		metaData.foodProcess = settings.getString("foodProcess", "");
		metaData.hasData = settings.getBoolean("hasData", false);
	}
}
