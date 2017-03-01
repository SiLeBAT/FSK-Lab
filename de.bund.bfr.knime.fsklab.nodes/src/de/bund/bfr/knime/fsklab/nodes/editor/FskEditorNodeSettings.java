package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

class FskEditorNodeSettings {

	String originalModelScript;
	String originalParametersScript;
	String originalVisualizationScript;

	String modifiedModelScript;
	String modifiedParametersScript;
	String modifiedVisualizationScript;

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
	}

	/**
	 * Loads the settings from the given node settings object.
	 *
	 * @param settings
	 *            a node settings object
	 * @throws InvalidSettingsException
	 *             if some settings are missing or invalid
	 */
	public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		originalModelScript = settings.getString("originalModelScript");
		originalParametersScript = settings.getString("originalParametersScript");
		originalVisualizationScript = settings.getString("originalVisualizationScript");

		modifiedModelScript = settings.getString("modifiedModelScript");
		modifiedParametersScript = settings.getString("modifiedParametersScript");
		modifiedVisualizationScript = settings.getString("modifiedVisualizationScript");
	}
}
