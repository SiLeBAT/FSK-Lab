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
package de.bund.bfr.knime.fsklab.nodes;

import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;
import de.bund.bfr.swing.UI;

class WriterNodeDialog extends NodeDialogPane {

  private final JTextField field;

  private final WriterNodeSettings nodeSettings;

  public WriterNodeDialog() {
    field = new FTextField();
    nodeSettings = new WriterNodeSettings();

    createUI();
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    try {
      nodeSettings.load(settings);
      field.setText(nodeSettings.filePath);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    nodeSettings.filePath = field.getText();
    nodeSettings.save(settings);
  }

  private void createUI() {

    // Build locale with the selected language in the preferences
    ResourceBundle bundle =
        ResourceBundle.getBundle("WriterNodeBundle", NodeUtils.getLocale(), new UTF8Control());

    String labelText = bundle.getString("label");
    String toolTipText = bundle.getString("tooltip");
    String buttonText = bundle.getString("button");

    FileNameExtensionFilter filter = new FileNameExtensionFilter("FSKX file", "fskx");
    FLabel label = new FLabel(labelText);
    JButton button =
        UIUtils.createBrowseButton(buttonText, field, JFileChooser.SAVE_DIALOG, filter);
    button.setToolTipText(toolTipText);

    FPanel formPanel =
        UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(field), Arrays.asList(button));
    JPanel northPanel = UI.createNorthPanel(formPanel);
    northPanel.setBackground(UIUtils.WHITE);

    addTab("Options", northPanel);
  }
}
