/*
 ***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.nodes.runner;

import java.awt.Color;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelColor;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class FskRunnerNodeSettings {
	
	// Setting models, with keys and default values
	SettingsModelInteger widthModel = new SettingsModelInteger("width", 640);
	SettingsModelInteger heightModel = new SettingsModelInteger("height", 640);
	SettingsModelString resolutionModel = new SettingsModelString("resolution", "NA");
	SettingsModelColor colourModel = new SettingsModelColor("colour", Color.white);
	SettingsModelInteger textPointSizeModel = new SettingsModelInteger("textPointSize", 12);
	
	public void saveSettingsTo(NodeSettingsWO settings) {
		widthModel.saveSettingsTo(settings);
		heightModel.saveSettingsTo(settings);
		resolutionModel.saveSettingsTo(settings);
		colourModel.saveSettingsTo(settings);
		textPointSizeModel.saveSettingsTo(settings);
	}

	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		widthModel.validateSettings(settings);
		heightModel.validateSettings(settings);
		resolutionModel.validateSettings(settings);
		colourModel.validateSettings(settings);
		textPointSizeModel.validateSettings(settings);
	}

	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		widthModel.loadSettingsFrom(settings);
		heightModel.loadSettingsFrom(settings);
		resolutionModel.loadSettingsFrom(settings);
		colourModel.loadSettingsFrom(settings);
		textPointSizeModel.loadSettingsFrom(settings);
	}
}
