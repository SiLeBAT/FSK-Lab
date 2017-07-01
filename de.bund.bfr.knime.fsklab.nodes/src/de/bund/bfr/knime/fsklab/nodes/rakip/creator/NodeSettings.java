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
package de.bund.bfr.knime.fsklab.nodes.rakip.creator;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

class NodeSettings {

	// Setting models, with keys and default values
	SettingsModelString modelScript = new SettingsModelString("modelScript", "");
	SettingsModelString paramScript = new SettingsModelString("paramScript", "");
	SettingsModelString vizScript = new SettingsModelString("visualizationScript", "");
	SettingsModelString metaDataDoc = new SettingsModelString("spreadsheet", "");

	public void saveSettings(final NodeSettingsWO settings) {
		modelScript.saveSettingsTo(settings);
		paramScript.saveSettingsTo(settings);
		vizScript.saveSettingsTo(settings);
		metaDataDoc.saveSettingsTo(settings);
	}

	public void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		modelScript.validateSettings(settings);
		paramScript.validateSettings(settings);
		vizScript.validateSettings(settings);
		metaDataDoc.validateSettings(settings);
	}

	public void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		modelScript.loadSettingsFrom(settings);
		paramScript.loadSettingsFrom(settings);
		vizScript.loadSettingsFrom(settings);
		metaDataDoc.loadSettingsFrom(settings);
	}
}
