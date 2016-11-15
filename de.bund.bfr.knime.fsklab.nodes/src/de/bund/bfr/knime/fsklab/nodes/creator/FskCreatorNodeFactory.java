package de.bund.bfr.knime.fsklab.nodes.creator;
/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 **************************************************************************************************/


import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

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
    return new FskCreatorNodeDialog();
  }
}
