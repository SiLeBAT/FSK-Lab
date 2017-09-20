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

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColor;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class RunnerNodeSettings {

	// Setting models
	public final SettingsModelInteger widthModel = new SettingsModelInteger("width", 640);
	public final SettingsModelInteger heightModel = new SettingsModelInteger("height", 640);
	public final SettingsModelString resolutionModel = new SettingsModelString("resolution", "NA");
	public final SettingsModelColor colourModel = new SettingsModelColor("colour", Color.white);
	public final SettingsModelInteger textPointSizeModel = new SettingsModelInteger("textPointSize", 12);

	private final List<SettingsModel> settingsModels = Arrays.asList(widthModel, heightModel, resolutionModel,
			colourModel, textPointSizeModel);

	public void saveSettingsTo(final NodeSettingsWO settings) {
		settingsModels.forEach(it -> it.saveSettingsTo(settings));
	}

	public void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		for (SettingsModel settingsModel : settingsModels) {
			settingsModel.validateSettings(settings);
		}
	}

	public void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		for (SettingsModel settingsModel : settingsModels) {
			settingsModel.loadSettingsFrom(settings);
		}
	}
}
