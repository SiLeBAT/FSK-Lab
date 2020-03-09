/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.foodprocess.view;

import org.jfree.chart.JFreeChart;
import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "View" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class ViewNodeView extends NodeView<ViewNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link ViewNodeModel})
	 */
	protected ViewNodeView(final ViewNodeModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
		JFreeChart jfc = getNodeModel().createChart();
		MyChartPanel cp = new MyChartPanel(jfc);
		setComponent(cp);
	}

}
