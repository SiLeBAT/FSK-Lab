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
package de.bund.bfr.knime.fsklab.nodes.runner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColorChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeSettings;

@Deprecated
public class FskRunnerNodeFactory extends NodeFactory<FskRunnerNodeModel> {

  @Override
  public FskRunnerNodeModel createNodeModel() {
    return new FskRunnerNodeModel();
  }

  @Override
  protected int getNrNodeViews() {
    return 1;
  }

  @Override
  public NodeView<FskRunnerNodeModel> createNodeView(int viewIndex, FskRunnerNodeModel nodeModel) {
    return new FskRunnerNodeView(nodeModel);
  }

  @Override
  protected boolean hasDialog() {
    return true;
  }

  @Override
  protected NodeDialogPane createNodeDialogPane() {
    // return new DefaultNodeSettingsPane();
    DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();
    pane.createNewGroup("Options");

    RunnerNodeSettings settings = new RunnerNodeSettings();

    // Width component
    DialogComponentNumberEdit widthComp =
        new DialogComponentNumberEdit(settings.widthModel, "Width");
    widthComp.setToolTipText("Width of the plot");
    pane.addDialogComponent(widthComp);

    // Height component
    DialogComponentNumberEdit heightComp =
        new DialogComponentNumberEdit(settings.heightModel, "Height");
    heightComp.setToolTipText("Height of the plot");
    pane.addDialogComponent(heightComp);

    // Resolution component
    DialogComponentString resolutionComp =
        new DialogComponentString(settings.resolutionModel, "Resolution");
    resolutionComp.setToolTipText("Nominal resolution in ppi which will be recorded "
        + "in the bitmap file, if a positive integer.");
    pane.addDialogComponent(resolutionComp);

    // Background colour component
    DialogComponentColorChooser colorComp =
        new DialogComponentColorChooser(settings.colourModel, "Background colour", true);
    colorComp.setToolTipText("Background colour");
    pane.addDialogComponent(colorComp);

    // Text point size component
    DialogComponentNumberEdit textPointSizeComp =
        new DialogComponentNumberEdit(settings.textPointSizeModel, "Text point size");
    textPointSizeComp.setToolTipText(
        "Point size of plotted text, interpreted as big points (1/72 inch) at res ppi.");

    pane.addDialogComponent(textPointSizeComp);

    return pane;
  }
}
