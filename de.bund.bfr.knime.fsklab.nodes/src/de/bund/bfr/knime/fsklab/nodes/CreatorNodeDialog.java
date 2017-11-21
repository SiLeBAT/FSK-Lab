/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.awt.GridLayout;
import java.nio.file.Path;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.swing.FilePanel;
import de.bund.bfr.swing.UI;

public class CreatorNodeDialog extends NodeDialogPane {

	private final FilePanel modelScriptChooser;
	private final FilePanel paramScriptChooser;
	private final FilePanel vizScriptChooser;
	private final FilePanel metaDataChooser;

	private final DefaultListModel<Path> listModel = new DefaultListModel<>();

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
		addTab("Files", UIUtils.createResourcesPanel(getPanel(), listModel));
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		try {
			this.settings.load(settings);

			modelScriptChooser.setFileName(this.settings.modelScript);
			paramScriptChooser.setFileName(this.settings.parameterScript);
			vizScriptChooser.setFileName(this.settings.visualizationScript);
			metaDataChooser.setFileName(this.settings.spreadsheet);

			// load resources
			listModel.clear();
			this.settings.resources.forEach(listModel::addElement);

		} catch (InvalidSettingsException exception) {
			throw new NotConfigurableException(exception.getMessage(), exception);
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

		this.settings.modelScript = modelScriptChooser.getFileName();
		this.settings.parameterScript = paramScriptChooser.getFileName();
		this.settings.visualizationScript = vizScriptChooser.getFileName();
		this.settings.spreadsheet = metaDataChooser.getFileName();

		// save resources
		this.settings.resources.clear();
		for (int i = 0; i < listModel.size(); i++) {
			this.settings.resources.add(listModel.get(i));
		}

		this.settings.save(settings);
	}
}
