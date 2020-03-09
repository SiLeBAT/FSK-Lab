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
package de.bund.bfr.knime.pcml.node.xls2pcml;

import org.knime.core.node.NodeView;

public class XLS2PCMLNodeView extends NodeView<XLS2PCMLNodeModel> {

	protected XLS2PCMLNodeView(final XLS2PCMLNodeModel nodeModel) {
		super(nodeModel);
	}
	
	protected void modelChanged() {
		XLS2PCMLNodeModel nodeModel = (XLS2PCMLNodeModel) getNodeModel();
		assert nodeModel != null;
	}
	
	@Override
	protected void onClose() {
	}
	
	@Override
	protected void onOpen() {
	}
}
