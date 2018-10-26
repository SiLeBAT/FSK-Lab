/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlreader;

import javax.swing.JFileChooser;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SBMLReaderNodeFactory extends NodeFactory<SBMLReaderNodeModel> {

  @Override
  public SBMLReaderNodeModel createNodeModel() {
    return new SBMLReaderNodeModel();
  }

  @Override
  public int getNrNodeViews() {
    return 0;
  }

  @Override
  public NodeView<SBMLReaderNodeModel> createNodeView(final int viewIndex,
      final SBMLReaderNodeModel nodeModel) {
    return null;
  }

  @Override
  public boolean hasDialog() {
    return true;
  }

  @Override
  public NodeDialogPane createNodeDialogPane() {
    DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();

    SettingsModelString filename = new SettingsModelString(SBMLReaderNodeModel.CFGKEY_FILE, "");
    DialogComponentFileChooser filechooser =
        new DialogComponentFileChooser(filename, "filename-history", JFileChooser.OPEN_DIALOG,
            ".sbml");

    pane.createNewGroup("Data source");
    pane.addDialogComponent(filechooser);

    return pane;
  }
}
