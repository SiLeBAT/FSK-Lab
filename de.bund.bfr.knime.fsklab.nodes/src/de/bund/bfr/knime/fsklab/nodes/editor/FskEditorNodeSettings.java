package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

class FskEditorNodeSettings {
	
	SettingsModelString originalModelScript;
	SettingsModelString originalParametersScript;
	SettingsModelString originalVisualizationScript;
	
	SettingsModelString modifiedModelScript;
	SettingsModelString modifiedParametersScript;
	SettingsModelString modifiedVisualizationScript;

	FskEditorNodeSettings() {
		originalModelScript = new SettingsModelString("originalModelScript", "");
		originalParametersScript = new SettingsModelString("originalParametersScript", "");
		originalVisualizationScript = new SettingsModelString("originalVisualizationScript", "");
		
		modifiedModelScript = new SettingsModelString("modifiedModelScript", "");
		modifiedParametersScript = new SettingsModelString("modifiedParametersScript", "");
		modifiedVisualizationScript = new SettingsModelString("modifiedVisualizationScript", "");
	}
	
	void saveSettingsTo(final NodeSettingsWO settings) {
		originalModelScript.saveSettingsTo(settings);
		originalParametersScript.saveSettingsTo(settings);
		originalVisualizationScript.saveSettingsTo(settings);
		
		modifiedModelScript.saveSettingsTo(settings);
		modifiedParametersScript.saveSettingsTo(settings);
		modifiedVisualizationScript.saveSettingsTo(settings);
	}
	
	void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		originalModelScript.validateSettings(settings);
		originalParametersScript.validateSettings(settings);
		originalVisualizationScript.validateSettings(settings);
		
		modifiedModelScript.validateSettings(settings);
		modifiedParametersScript.validateSettings(settings);
		modifiedVisualizationScript.validateSettings(settings);
	}
	
	void loadValidatedSettingsFrom(final NodeSettingsRO settings)  {
		try {
			originalModelScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			originalModelScript.setStringValue("");
		}
		
		try {
			originalParametersScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			originalParametersScript.setStringValue("");
		}

		try {
			originalVisualizationScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			originalVisualizationScript.setStringValue("");
		}

		try {
			modifiedModelScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			modifiedModelScript.setStringValue("");
		}
		
		try {
			modifiedParametersScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			modifiedParametersScript.setStringValue("");
		}

		try {
			modifiedVisualizationScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			modifiedVisualizationScript.setStringValue("");
		}
	}
}
