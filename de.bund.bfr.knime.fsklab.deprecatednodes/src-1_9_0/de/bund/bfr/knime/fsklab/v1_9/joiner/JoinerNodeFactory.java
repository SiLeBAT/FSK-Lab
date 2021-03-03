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
package de.bund.bfr.knime.fsklab.v1_9.joiner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;


public class JoinerNodeFactory extends NodeFactory<JoinerNodeModel> implements
    WizardNodeFactoryExtension<JoinerNodeModel, JoinerViewRepresentation, JoinerViewValue> {

  @Override
  public JoinerNodeModel createNodeModel() {
    return new JoinerNodeModel();
  }

  @Override
  protected int getNrNodeViews() {
    return 0;
  }

  @Override
  public NodeView<JoinerNodeModel> createNodeView(int viewIndex,
      JoinerNodeModel nodeModel) {
    return null;
  }

  @Override
  protected NodeDialogPane createNodeDialogPane() {
    return null;
  }

  @Override
  protected boolean hasDialog() {
    return false;
  }
}
