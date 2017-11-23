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
package de.bund.bfr.knime.fsklab.nodes.creator;

import java.awt.GridLayout;
import javax.swing.JPanel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NodeView;
import org.knime.core.node.NotConfigurableException;
import de.bund.bfr.knime.fsklab.nodes.CreatorNodeSettings;
import de.bund.bfr.swing.FilePanel;
import de.bund.bfr.swing.UI;

@Deprecated
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
  public NodeView<FskCreatorNodeModel> createNodeView(final int viewIndex,
      final FskCreatorNodeModel nodeModel) {
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
    return new NodeDialog();
  }

  class NodeDialog extends NodeDialogPane {

    private final FilePanel modelScriptChooser;
    private final FilePanel paramScriptChooser;
    private final FilePanel visualizationScriptChooser;
    private final FilePanel spreadsheetChooser;

    private final CreatorNodeSettings settings = new CreatorNodeSettings();

    NodeDialog() {

      modelScriptChooser = new FilePanel("Model script (*)", FilePanel.OPEN_DIALOG, 50);
      modelScriptChooser
          .setToolTipText("Script that calculates the values of the model (Mandatory).");
      modelScriptChooser.setAcceptAllFiles(false);
      modelScriptChooser.addFileFilter(".r", "R file (*.r)");

      paramScriptChooser = new FilePanel("Parameters script", FilePanel.OPEN_DIALOG, 50);
      paramScriptChooser
          .setToolTipText("Script with the parameter values of the model (Optional).");
      paramScriptChooser.setAcceptAllFiles(false);
      paramScriptChooser.addFileFilter(".r", "R file (*.r)");

      visualizationScriptChooser = new FilePanel("Visualization script", FilePanel.OPEN_DIALOG, 50);
      visualizationScriptChooser.setToolTipText("Script with a number of commands to create "
          + "plots or charts using the simulation results (Optional).");
      visualizationScriptChooser.setAcceptAllFiles(false);
      visualizationScriptChooser.addFileFilter(".r", "R file (*.r)");

      spreadsheetChooser = new FilePanel("XLSX spreadsheet", FilePanel.OPEN_DIALOG, 50);
      spreadsheetChooser.setToolTipText("XLSX file with model metadata (Optional).");
      spreadsheetChooser.setAcceptAllFiles(false);
      spreadsheetChooser.addFileFilter(".xlsx", "XLSX spreadsheet (*.xlsx)");

      final JPanel gridPanel = new JPanel(new GridLayout(4, 1, 5, 5));
      gridPanel.add(modelScriptChooser);
      gridPanel.add(paramScriptChooser);
      gridPanel.add(visualizationScriptChooser);
      gridPanel.add(spreadsheetChooser);

      addTab("Options", UI.createNorthPanel(gridPanel));
    }

    @Override
    protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
        throws NotConfigurableException {
      try {
        this.settings.load(settings);

        modelScriptChooser.setFileName(this.settings.modelScript);
        paramScriptChooser.setFileName(this.settings.parameterScript);
        visualizationScriptChooser.setFileName(this.settings.visualizationScript);
        spreadsheetChooser.setFileName(this.settings.spreadsheet);
      } catch (InvalidSettingsException exception) {
        throw new NotConfigurableException(exception.getMessage(), exception);
      }
    }

    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
      this.settings.modelScript = modelScriptChooser.getFileName();
      this.settings.parameterScript = paramScriptChooser.getFileName();
      this.settings.visualizationScript = visualizationScriptChooser.getFileName();
      this.settings.spreadsheet = spreadsheetChooser.getFileName();

      this.settings.save(settings);
    }
  }
}
