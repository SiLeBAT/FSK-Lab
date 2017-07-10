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
package de.bund.bfr.knime.fsklab.nodes.rakip.runner;

import java.awt.Image;

import javax.swing.JScrollPane;

import de.bund.bfr.knime.fsklab.nodes.ui.RPlotterViewPanel;

/**
 * The view of the for the r nodes with image output.
 *
 * @author Heiko Hofer
 */
class NodeView extends org.knime.core.node.NodeView<NodeModel> {

	private final RPlotterViewPanel m_panel;

	/**
	 * Creates a new instance.
	 *
	 * @param nodeModel
	 *            the model associated with this view.
	 */
	public NodeView(final NodeModel nodeModel) {
		super(nodeModel);
		m_panel = new RPlotterViewPanel();
		super.setComponent(new JScrollPane(m_panel));
	}

	/**
	 * Updates the image to display.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		final NodeModel model = super.getNodeModel();
		final Image image = model.getResultImage();
		m_panel.update(image);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
		// empty
	}
}
