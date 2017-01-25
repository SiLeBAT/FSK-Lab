package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

public class FskEditorNodeDialog extends DataAwareNodeDialogPane {

	private ScriptPanel modelScriptPanel;
	private ScriptPanel paramScriptPanel;
	private ScriptPanel vizScriptPanel;

	private FskEditorNodeSettings settings;

	public FskEditorNodeDialog() {
		// Create panels
		modelScriptPanel = new ScriptPanel("Model script", "", true);
		paramScriptPanel = new ScriptPanel("Parameters script", "", true);
		vizScriptPanel = new ScriptPanel("Visualization script", "", true);

		// Initialize settings (values are garbage, need to be loaded from
		// settings/input port)
		settings = new FskEditorNodeSettings();

		// Add ScriptPanels
		addTab("Model script", modelScriptPanel);
		addTab("Parameters script", paramScriptPanel);
		addTab("Visualization script", vizScriptPanel);

		updatePanels();
	}

	// Update the scripts in the ScriptPanels
	private void updatePanels() {
		modelScriptPanel.getTextArea().setText(settings.modifiedModelScript.getStringValue());
		paramScriptPanel.getTextArea().setText(settings.modifiedParametersScript.getStringValue());
		vizScriptPanel.getTextArea().setText(settings.modifiedVisualizationScript.getStringValue());
	}

	// --- settings methods ---
	/** Loads settings from input port. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {
		FskEditorNodeSettings editorSettings = new FskEditorNodeSettings();
		editorSettings.loadValidatedSettingsFrom(settings);

		FskPortObject inObj = (FskPortObject) input[0];

		// if input model has not changed (the original scripts stored in
		// settings match the input model)
		if (Objects.equal(editorSettings.originalModelScript.getStringValue(), inObj.model)
				&& Objects.equal(editorSettings.originalParametersScript.getStringValue(), inObj.param)
				&& Objects.equal(editorSettings.originalVisualizationScript.getStringValue(), inObj.viz)) {
			// Updates settings
			this.settings = editorSettings;

		} else {
			// Discard settings and replace them with input model
			this.settings.originalModelScript.setStringValue(inObj.model);
			this.settings.originalParametersScript.setStringValue(inObj.param);
			this.settings.originalVisualizationScript.setStringValue(inObj.viz);

			this.settings.modifiedModelScript.setStringValue(inObj.model);
			this.settings.modifiedParametersScript.setStringValue(inObj.param);
			this.settings.modifiedVisualizationScript.setStringValue(inObj.viz);
		}
		
		updatePanels();
	}

	/** Loads settings from saved settings. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		this.settings.loadValidatedSettingsFrom(settings);
		updatePanels();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		// Get scripts from ScriptPanels
		String modifiedModelScript = modelScriptPanel.getTextArea().getText();
		String modifiedParametersScript = paramScriptPanel.getTextArea().getText();
		String modifiedVisualizationScript = vizScriptPanel.getTextArea().getText();
		
		// Trim scripts if not empty
		if (!Strings.isNullOrEmpty(modifiedModelScript)) {
			modifiedModelScript = modifiedModelScript.trim();
		}
		if (!Strings.isNullOrEmpty(modifiedParametersScript)) {
			modifiedParametersScript = modifiedParametersScript.trim();
		}
		if (!Strings.isNullOrEmpty(modifiedVisualizationScript)) {
			modifiedVisualizationScript = modifiedVisualizationScript.trim();
		}
		
		// Save modified scripts to settings
		this.settings.modifiedModelScript.setStringValue(modifiedModelScript);
		this.settings.modifiedParametersScript.setStringValue(modifiedParametersScript);
		this.settings.modifiedVisualizationScript.setStringValue(modifiedVisualizationScript);
		this.settings.saveSettingsTo(settings);
	}
}
