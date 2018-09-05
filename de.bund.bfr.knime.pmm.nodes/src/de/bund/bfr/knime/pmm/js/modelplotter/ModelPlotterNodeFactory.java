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
package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;

/**
 * Factory that bundles model, dialog and view creation.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
public final class ModelPlotterNodeFactory extends NodeFactory<ModelPlotterNodeModel>
		implements
		WizardNodeFactoryExtension<ModelPlotterNodeModel, ModelPlotterViewRepresentation, ModelPlotterViewValue> {

	@Override
	public ModelPlotterNodeModel createNodeModel() {
		return new ModelPlotterNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<ModelPlotterNodeModel> createNodeView(int viewIndex,
			ModelPlotterNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new ModelPlotterNodeDialogPane();
	}

}
