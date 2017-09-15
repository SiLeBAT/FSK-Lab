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
package de.bund.bfr.knime.fsklab.nodes.runner;

import java.awt.Color;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelColor;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

@Deprecated
class FskRunnerNodeSettings {
	
	// Default values
	private static final int WIDTH = 640;
	private static final int HEIGHT = 640;
	private static final String RES = "NA";
	private static final Color COLOUR = Color.white;
	private static final int TEXT_POINT_SIZE = 12;

	// Setting models, with keys and default values
	SettingsModelInteger widthModel = new SettingsModelInteger("width", WIDTH);
	SettingsModelInteger heightModel = new SettingsModelInteger("height", HEIGHT);
	SettingsModelString resolutionModel = new SettingsModelString("resolution", RES);
	SettingsModelColor colourModel = new SettingsModelColor("colour", COLOUR);
	SettingsModelInteger textPointSizeModel = new SettingsModelInteger("textPointSize", TEXT_POINT_SIZE);

	public void saveSettingsTo(NodeSettingsWO settings) {
		widthModel.saveSettingsTo(settings);
		heightModel.saveSettingsTo(settings);
		resolutionModel.saveSettingsTo(settings);
		colourModel.saveSettingsTo(settings);
		textPointSizeModel.saveSettingsTo(settings);
	}

	protected void validateSettings(NodeSettingsRO settings) {
		try {
			widthModel.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			widthModel.setIntValue(WIDTH);
		}
		try {
			heightModel.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			heightModel.setIntValue(HEIGHT);
		}
		try {
			resolutionModel.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			resolutionModel.setStringValue(RES);
		}
		try {
			colourModel.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			colourModel.setColorValue(COLOUR);
		}
		try {
			textPointSizeModel.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			textPointSizeModel.setIntValue(TEXT_POINT_SIZE);
		}
	}

	/**
	 * Loads settings. If a setting is missing (like in an old node) then it
	 * loads the default value for that setting.
	 */
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) {
		try {
			widthModel.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			widthModel.setIntValue(WIDTH);
		}
		try {
			heightModel.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			heightModel.setIntValue(HEIGHT);
		}
		try {
			resolutionModel.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			resolutionModel.setStringValue(RES);
		}
		try {
			colourModel.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			colourModel.setColorValue(COLOUR);
		}
		try {
			textPointSizeModel.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			textPointSizeModel.setIntValue(TEXT_POINT_SIZE);
		}
	}
}
