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
		modelScriptPanel.getTextArea().setText(settings.modifiedModelScript);
		paramScriptPanel.getTextArea().setText(settings.modifiedParametersScript);
		vizScriptPanel.getTextArea().setText(settings.modifiedVisualizationScript);
	}

	// --- settings methods ---
	/** Loads settings from input port. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {
		FskEditorNodeSettings editorSettings = new FskEditorNodeSettings();
		try {
			editorSettings.loadSettings(settings);
		} catch (InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage(), e.getCause());
		}

		FskPortObject inObj = (FskPortObject) input[0];

		// if input model has not changed (the original scripts stored in
		// settings match the input model)
		if (Objects.equal(editorSettings.originalModelScript, inObj.model)
				&& Objects.equal(editorSettings.originalParametersScript, inObj.param)
				&& Objects.equal(editorSettings.originalVisualizationScript, inObj.viz)) {
			// Updates settings
			this.settings = editorSettings;

		} else {
			// Discard settings and replace them with input model
			this.settings.originalModelScript = inObj.model;
			this.settings.originalParametersScript = inObj.param;
			this.settings.originalVisualizationScript = inObj.viz;

			this.settings.modifiedModelScript = inObj.model;
			this.settings.modifiedParametersScript = inObj.param;
			this.settings.modifiedVisualizationScript = inObj.viz;
		}
		
		updatePanels();
	}

	/** Loads settings from saved settings. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		try {
			this.settings.loadSettings(settings);
		} catch (InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage(), e.getCause());
		}
		updatePanels();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		// Save modified scripts to settings
		this.settings.modifiedModelScript = modelScriptPanel.getTextArea().getText();
		this.settings.modifiedParametersScript = paramScriptPanel.getTextArea().getText();
		this.settings.modifiedVisualizationScript = vizScriptPanel.getTextArea().getText();
		
		// Trim non-empty scripts
		if (!Strings.isNullOrEmpty(this.settings.modifiedModelScript)) {
			this.settings.modifiedModelScript = this.settings.modifiedModelScript.trim();
		}
		if (!Strings.isNullOrEmpty(this.settings.modifiedParametersScript)) {
			this.settings.modifiedParametersScript = this.settings.modifiedParametersScript.trim();
		}
		if (!Strings.isNullOrEmpty(this.settings.modifiedVisualizationScript)) {
			this.settings.modifiedVisualizationScript = this.settings.modifiedVisualizationScript.trim();
		}
		
		this.settings.saveSettings(settings);
	}
}
