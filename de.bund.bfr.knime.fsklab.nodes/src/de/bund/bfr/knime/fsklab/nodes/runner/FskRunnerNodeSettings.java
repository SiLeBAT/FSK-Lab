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
