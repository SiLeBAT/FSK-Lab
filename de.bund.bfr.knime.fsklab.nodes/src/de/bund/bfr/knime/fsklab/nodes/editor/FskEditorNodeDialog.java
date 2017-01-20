package de.bund.bfr.knime.fsklab.nodes.editor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

public class FskEditorNodeDialog extends DataAwareNodeDialogPane {

	private ScriptPanel modelScriptPanel = new ScriptPanel("Model script", "", true);
	private ScriptPanel paramScriptPanel = new ScriptPanel("Parameters script", "", true);
	private ScriptPanel vizScriptPanel = new ScriptPanel("Visualization script", "", true);

	private FskEditorNodeSettings settings = new FskEditorNodeSettings();

	public FskEditorNodeDialog() {
		addTab("Model script", modelScriptPanel);
		addTab("Parameters script", paramScriptPanel);
		addTab("Visualization script", vizScriptPanel);

		settings.modelScript.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				modelScriptPanel.getTextArea().setText(settings.modelScript.getStringValue());
			}
		});

		settings.paramScript.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				paramScriptPanel.getTextArea().setText(settings.paramScript.getStringValue());
			}
		});

		settings.vizScript.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				vizScriptPanel.getTextArea().setText(settings.vizScript.getStringValue());
			}
		});
	}

	// --- settings methods ---
	/** Loads settings from input port. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {		
		FskEditorNodeSettings newSettings = new FskEditorNodeSettings();
		newSettings.loadValidatedSettingsFrom(settings);

		FskPortObject inObj = (FskPortObject) input[0];
		
		int connectedModel = inObj.objectNum;
		int oldConnectedModel = newSettings.objectNumber.getIntValue();
		
		// if input model has changed
		if (connectedModel != oldConnectedModel) {

			// Discard settings and replace them with input model
			this.settings.objectNumber.setIntValue(inObj.objectNum);
			this.settings.modelScript.setStringValue(inObj.model);
			this.settings.paramScript.setStringValue(inObj.param);
			this.settings.vizScript.setStringValue(inObj.viz);

		} else {
			// Return model from settings
			this.settings = newSettings;
		}
	}
	
	/** Loads settings from saved settings. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		this.settings.loadValidatedSettingsFrom(settings);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		this.settings.modelScript.setStringValue(modelScriptPanel.getTextArea().getText().trim());
		this.settings.paramScript.setStringValue(paramScriptPanel.getTextArea().getText().trim());
		this.settings.vizScript.setStringValue(vizScriptPanel.getTextArea().getText().trim());

		this.settings.saveSettingsTo(settings);
	}
}
