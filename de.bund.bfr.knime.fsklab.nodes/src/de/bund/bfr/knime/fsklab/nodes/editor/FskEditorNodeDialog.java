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
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {

		FskPortObject fskObj = (FskPortObject) input[0];

		this.settings.loadValidatedSettingsFrom(settings);

		/*
		 * Take data from the inputs if: 1) all the scripts are empty then the
		 * editor is new
		 */
		if (this.settings.modelScript.getStringValue().isEmpty() && this.settings.paramScript.getStringValue().isEmpty()
				&& this.settings.vizScript.getStringValue().isEmpty()) {

			this.settings.modelScript.setStringValue(fskObj.model);
			this.settings.paramScript.setStringValue(fskObj.param);
			this.settings.vizScript.setStringValue(fskObj.viz);
		}
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		// nothing to do - the scripts are already initialized in the constructor
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		this.settings.modelScript.setStringValue(modelScriptPanel.getTextArea().getText().trim());
		this.settings.paramScript.setStringValue(paramScriptPanel.getTextArea().getText().trim());
		this.settings.vizScript.setStringValue(vizScriptPanel.getTextArea().getText().trim());

		this.settings.saveSettingsTo(settings);
	}
}
