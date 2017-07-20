package de.bund.bfr.knime.fsklab.nodes

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

class CreatorNodeSettings {

	// Setting models, with keys and default values
	val modelScript = SettingsModelString("modelScript", "")
	val paramScript = SettingsModelString("paramScript", "")
	val vizScript = SettingsModelString("visualizationScript", "")
	val metaDataDoc = SettingsModelString("spreadsheet", "")
	
	fun saveSettings(settings: NodeSettingsWO) : Unit {
		modelScript.saveSettingsTo(settings)
		paramScript.saveSettingsTo(settings)
		vizScript.saveSettingsTo(settings)
		metaDataDoc.saveSettingsTo(settings)
	}
	
	fun validateSettings(settings: NodeSettingsRO) : Unit {
		modelScript.validateSettings(settings)
		paramScript.validateSettings(settings)
		vizScript.validateSettings(settings)
		metaDataDoc.validateSettings(settings)
	}
	
	fun loadValidatedSettings(settings: NodeSettingsRO) : Unit {
		modelScript.loadSettingsFrom(settings)
		paramScript.loadSettingsFrom(settings)
		vizScript.loadSettingsFrom(settings)
		metaDataDoc.loadSettingsFrom(settings)
	}
}