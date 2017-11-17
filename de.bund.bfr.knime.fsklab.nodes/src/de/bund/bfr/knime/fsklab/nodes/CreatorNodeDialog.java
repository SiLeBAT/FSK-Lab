package de.bund.bfr.knime.fsklab.nodes;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.swing.FilePanel;
import de.bund.bfr.swing.UI;

public class CreatorNodeDialog extends NodeDialogPane {

	private final FilePanel modelScriptChooser;
	private final FilePanel paramScriptChooser;
	private final FilePanel vizScriptChooser;
	private final FilePanel metaDataChooser;

	private final CreatorNodeSettings settings = new CreatorNodeSettings();

	public CreatorNodeDialog() {

		modelScriptChooser = new FilePanel("Model script", FilePanel.OPEN_DIALOG, 50);
		modelScriptChooser.setToolTipText("Script that calculates the values of the model (Mandatory)");
		modelScriptChooser.setAcceptAllFiles(false);
		modelScriptChooser.addFileFilter(".r", "R file (*.r)");

		paramScriptChooser = new FilePanel("Parameters script", FilePanel.OPEN_DIALOG, 50);
		paramScriptChooser.setToolTipText("Script with the parameter values of the model (Optional).");
		paramScriptChooser.setAcceptAllFiles(false);
		paramScriptChooser.addFileFilter(".r", "R file (*.r)");

		vizScriptChooser = new FilePanel("Visualization script", FilePanel.OPEN_DIALOG, 50);
		vizScriptChooser.setToolTipText("Script with a number of commands to create plots or charts "
				+ "using the simulation results (Optional).");
		vizScriptChooser.setAcceptAllFiles(false);
		vizScriptChooser.addFileFilter(".r", "R file (*.r)");

		metaDataChooser = new FilePanel("XLSX spreadsheet", FilePanel.OPEN_DIALOG, 50);
		metaDataChooser.setToolTipText("XLSX file with model metadata");
		metaDataChooser.setAcceptAllFiles(false);
		metaDataChooser.addFileFilter(".xlsx", "XSLX spreadsheet (*.xslx)");

		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		gridPanel.add(modelScriptChooser);
		gridPanel.add(paramScriptChooser);
		gridPanel.add(vizScriptChooser);
		gridPanel.add(metaDataChooser);

		JPanel northPanel = UI.createNorthPanel(gridPanel);

		addTab("Options", northPanel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		try {
			this.settings.loadValidatedSettingsFrom(settings);
			
			modelScriptChooser.setFileName(this.settings.modelScript.getStringValue());
			paramScriptChooser.setFileName(this.settings.paramScript.getStringValue());
			vizScriptChooser.setFileName(this.settings.vizScript.getStringValue());
			metaDataChooser.setFileName(this.settings.metaDataDoc.getStringValue());
		} catch (InvalidSettingsException exception) {
			throw new NotConfigurableException(exception.getMessage(), exception);
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

		this.settings.modelScript.setStringValue(modelScriptChooser.getFileName());
		this.settings.paramScript.setStringValue(paramScriptChooser.getFileName());
		this.settings.vizScript.setStringValue(vizScriptChooser.getFileName());
		this.settings.metaDataDoc.setStringValue(metaDataChooser.getFileName());

		this.settings.saveSettings(settings);
	}

}
