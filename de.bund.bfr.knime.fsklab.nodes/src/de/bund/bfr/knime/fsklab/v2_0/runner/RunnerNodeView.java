package de.bund.bfr.knime.fsklab.v2_0.runner;
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

import javax.swing.JScrollPane;
import org.knime.core.node.NodeView;
import de.bund.bfr.knime.fsklab.nodes.common.ui.RPlotterViewPanel;


class RunnerNodeView extends NodeView<RunnerNodeModel> {

  private final RPlotterViewPanel m_panel;

  /**
   * Creates a new instance.
   *
   * @param nodeModel the model associated with this view.
   */
  public RunnerNodeView(final RunnerNodeModel nodeModel) {
    super(nodeModel);
    m_panel = new RPlotterViewPanel();
    super.setComponent(new JScrollPane(m_panel));
  }

  /** Updates the image to display. */
  @Override
  protected void modelChanged() {
    final RunnerNodeModel model = super.getNodeModel();
    m_panel.update(model.getResultImage());
  }

  @Override
  protected void onClose() {}

  @Override
  protected void onOpen() {}
}
