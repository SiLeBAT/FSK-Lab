package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;
/*
 ***************************************************************************************************
 * Copyright (c) 2021 Federal Institute for Risk Assessment (BfR), Germany
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

import org.knime.core.node.NodeView;


class PMMToFSKExporterNodeView extends NodeView<PMMToFSKExporterNodeModel> {


  /**
   * Creates a new instance.
   *
   * @param nodeModel the model associated with this view.
   */
  public PMMToFSKExporterNodeView(final PMMToFSKExporterNodeModel nodeModel) {
    super(nodeModel);
  }

  /** Updates the image to display. */
  @Override
  protected void modelChanged() {
    final PMMToFSKExporterNodeModel model = super.getNodeModel();
  }

  @Override
  protected void onClose() {}

  @Override
  protected void onOpen() {}
}
