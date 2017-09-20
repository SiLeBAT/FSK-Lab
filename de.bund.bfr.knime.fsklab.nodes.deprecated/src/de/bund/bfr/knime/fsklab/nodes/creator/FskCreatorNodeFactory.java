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
package de.bund.bfr.knime.fsklab.nodes.creator;

import javax.swing.JFileChooser;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

import de.bund.bfr.knime.fsklab.nodes.CreatorNodeSettings;

@Deprecated
public class FskCreatorNodeFactory extends NodeFactory<FskCreatorNodeModel> {

	/** {@inheritDoc} */
	@Override
	public FskCreatorNodeModel createNodeModel() {
		return new FskCreatorNodeModel();
	}

	/** {@inheritDoc} */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public NodeView<FskCreatorNodeModel> createNodeView(final int viewIndex, final FskCreatorNodeModel nodeModel) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public NodeDialogPane createNodeDialogPane() {

		CreatorNodeSettings settings = new CreatorNodeSettings();

		// Create components
		final int dlgType = JFileChooser.OPEN_DIALOG;
		final String rFilters = ".r|.R"; // Extension filters for the R script

		DialogComponentFileChooser modelScriptChooser = new DialogComponentFileChooser(settings.modelScript,
				"modelScript-history", dlgType, rFilters);
		modelScriptChooser.setBorderTitle("Model script (*)");
		modelScriptChooser.setToolTipText("Script that calculates the values of the model (Mandatory).");

		DialogComponentFileChooser paramScriptChooser = new DialogComponentFileChooser(settings.paramScript,
				"paramScript-history", dlgType, rFilters);
		paramScriptChooser.setBorderTitle("Parameters script");
		paramScriptChooser.setToolTipText("Script with the parameter values of the model (Optional).");

		DialogComponentFileChooser vizScriptChooser = new DialogComponentFileChooser(settings.vizScript,
				"vizScript-history", dlgType, rFilters);
		vizScriptChooser.setBorderTitle("Visualization script");
		vizScriptChooser.setToolTipText(
				"Script with a number of commands to create plots or charts using the simulation results (Optional).");

		DialogComponentFileChooser metaDataChooser = new DialogComponentFileChooser(settings.metaDataDoc,
				"metaData-history", dlgType);
		metaDataChooser.setBorderTitle("XLSX spreadsheet");
		metaDataChooser.setToolTipText("XLSX file with model metadata (Optional).");

		// Create pane and add components
		DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();
		pane.addDialogComponent(modelScriptChooser);
		pane.addDialogComponent(paramScriptChooser);
		pane.addDialogComponent(vizScriptChooser);
		pane.addDialogComponent(metaDataChooser);

		return pane;
	}
}
