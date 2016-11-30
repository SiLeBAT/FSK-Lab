package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

class FskEditorNodeSettings {
	
	SettingsModelString modelScript = new SettingsModelString("model script", "");
	SettingsModelString paramScript = new SettingsModelString("parameters script", "");
	SettingsModelString vizScript = new SettingsModelString("visualization script", "");
	
	void saveSettingsTo(final NodeSettingsWO settings) {
		modelScript.saveSettingsTo(settings);
		paramScript.saveSettingsTo(settings);
		vizScript.saveSettingsTo(settings);
	}
	
	void validateSettings(final NodeSettingsRO settings) {
		try {
			modelScript.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			modelScript.setStringValue("");
		}
		try {
			paramScript.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			paramScript.setStringValue("");
		}
		try {
			vizScript.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			vizScript.setStringValue("");
		}
	}
	
	void loadValidatedSettingsFrom(final NodeSettingsRO settings) {
		try {
			modelScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			modelScript.setStringValue("");
		}
		try {
			paramScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			paramScript.setStringValue("");
		}
		try {
			vizScript.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			vizScript.setStringValue("");
		}
	}
}
