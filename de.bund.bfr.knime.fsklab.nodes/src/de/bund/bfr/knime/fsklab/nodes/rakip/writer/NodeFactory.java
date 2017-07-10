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
package de.bund.bfr.knime.fsklab.nodes.rakip.writer;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class NodeFactory extends org.knime.core.node.NodeFactory<NodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeModel createNodeModel() {
		return new NodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.knime.core.node.NodeView<NodeModel> createNodeView(final int viewIndex, final NodeModel nodeModel) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.knime.core.node.NodeDialogPane createNodeDialogPane() {
		// File dialog chooser
		final String fileHistoryId = "fileHistory";
		final int dlgType = JFileChooser.SAVE_DIALOG;
		final boolean directoryOnly = false;
		final String validExtensions = ".fskx|.FSKX";

		final SettingsModelString filePath = new SettingsModelString(NodeModel.CFG_FILE, null);
		final DialogComponentFileChooser fileDlg = new DialogComponentFileChooser(filePath, fileHistoryId, dlgType,
				directoryOnly, validExtensions);
		fileDlg.setBorderTitle("Output file");

		DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();
		pane.addDialogComponent(fileDlg);

		return pane;
	}
}
