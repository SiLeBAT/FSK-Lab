package de.bund.bfr.knime.fsklab.nodes.creator;

import javax.swing.Box;
import javax.swing.JFileChooser;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class FskCreatorNodeDialog extends NodeDialogPane {

	// models
	private final SettingsModelString m_modelScript;
	private final SettingsModelString m_paramScript;
	private final SettingsModelString m_visualizationScript;
	private final SettingsModelString m_metaDataDoc;

	public FskCreatorNodeDialog() {
		m_modelScript = new SettingsModelString(FskCreatorNodeModel.CFGKEY_MODEL_SCRIPT, "");
		m_paramScript = new SettingsModelString(FskCreatorNodeModel.CFGKEY_PARAM_SCRIPT, "");
		m_visualizationScript = new SettingsModelString(FskCreatorNodeModel.CFGKEY_VISUALIZATION_SCRIPT, "");
		m_metaDataDoc = new SettingsModelString(FskCreatorNodeModel.CFGKEY_SPREADSHEET, "");

		// Creates the GUI
		Box box = Box.createHorizontalBox();
		box.add(createFilesSelection());

		addTab("Selection", box);
		removeTab("Options");
	}

	/** Creates Box to select R scripts and spreadsheet with meta data. */
	private Box createFilesSelection() {
		final int dialogType = JFileChooser.OPEN_DIALOG;
		
		String rFilters = ".r|.R"; // Extension filters for the R scripts

		DialogComponentFileChooser modelScriptChooser = new DialogComponentFileChooser(m_modelScript,
				"modelScript-history", dialogType, rFilters);
		modelScriptChooser.setBorderTitle("Model script (*)");
		modelScriptChooser.setToolTipText("Script that calculates the values of the model (Mandatory).");

		DialogComponentFileChooser paramScriptChooser = new DialogComponentFileChooser(m_paramScript,
				"paramScript-history", dialogType, rFilters);
		paramScriptChooser.setBorderTitle("Parameters script");
		paramScriptChooser.setToolTipText("Script with the parameter values of the model (Optional).");

		DialogComponentFileChooser vizScriptChooser = new DialogComponentFileChooser(m_visualizationScript,
				"vizScript-history", dialogType, rFilters);
		vizScriptChooser.setBorderTitle("Visualization script");
		vizScriptChooser.setToolTipText(
				"Script with a number of commands to create plots or charts using the simulation results (Optional).");

		DialogComponentFileChooser metaDataChooser = new DialogComponentFileChooser(m_metaDataDoc, "metaData-history",
				dialogType);
		metaDataChooser.setBorderTitle("XLSX spreadsheet");
		metaDataChooser.setToolTipText("XLSX file with model metadata (Optional).");
		

		Box box = Box.createVerticalBox();
		box.add(modelScriptChooser.getComponentPanel());
		box.add(paramScriptChooser.getComponentPanel());
		box.add(vizScriptChooser.getComponentPanel());
		box.add(metaDataChooser.getComponentPanel());

		return box;
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		try {
			m_modelScript.loadSettingsFrom(settings);
			m_paramScript.loadSettingsFrom(settings);
			m_visualizationScript.loadSettingsFrom(settings);
			m_metaDataDoc.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		m_modelScript.saveSettingsTo(settings);
		m_paramScript.saveSettingsTo(settings);
		m_visualizationScript.saveSettingsTo(settings);
		m_metaDataDoc.saveSettingsTo(settings);
	}
}
