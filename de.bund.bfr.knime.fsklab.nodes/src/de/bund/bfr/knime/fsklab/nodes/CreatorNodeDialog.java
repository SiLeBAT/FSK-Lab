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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;
import de.bund.bfr.swing.UI;

public class CreatorNodeDialog extends NodeDialogPane {

  private final JTextField modelScriptField;
  private final JTextField parametersScriptField;
  private final JTextField visualizationScriptField;
  private final JTextField spreadsheetField;

  private final DefaultListModel<Path> listModel;

  private final CreatorNodeSettings settings;

  public CreatorNodeDialog() {

    modelScriptField = new FTextField();
    parametersScriptField = new FTextField();
    visualizationScriptField = new FTextField();
    spreadsheetField = new FTextField();

    listModel = new DefaultListModel<>();
    settings = new CreatorNodeSettings();

    createUI();
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
      throws NotConfigurableException {
    try {
      this.settings.load(settings);

      modelScriptField.setText(this.settings.modelScript);
      parametersScriptField.setText(this.settings.parameterScript);
      visualizationScriptField.setText(this.settings.visualizationScript);
      spreadsheetField.setText(this.settings.spreadsheet);

      // load resources
      listModel.clear();
      this.settings.resources.forEach(listModel::addElement);

    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    this.settings.modelScript = modelScriptField.getText();
    this.settings.parameterScript = parametersScriptField.getText();
    this.settings.visualizationScript = visualizationScriptField.getText();
    this.settings.spreadsheet = spreadsheetField.getText();

    // save resources
    this.settings.resources.clear();
    for (int i = 0; i < listModel.size(); i++) {
      this.settings.resources.add(listModel.get(i));
    }

    this.settings.save(settings);
  }

  private void createUI() {
    FileFilter rFilter = new FileNameExtensionFilter("R script", "r");
    FileFilter spreadsheetFilter = new FileNameExtensionFilter("Excel spreadsheet", "xlsx");

    ResourceBundle bundle = ResourceBundle.getBundle("CreatorNodeBundle", new UTF8Control());

    // buttons
    String buttonText = bundle.getString("browse_button");

    String modelScriptToolTip = bundle.getString("modelscript_tooltip");
    String parameterScriptToolTip = bundle.getString("parameterscript_tooltip");
    String visualizationScriptToolTip = bundle.getString("visualizationscript_tooltip");
    String spreadsheetScriptToolTip = bundle.getString("spreadsheet_tooltip");

    JButton modelScriptButton =
        UIUtils.createBrowseButton(buttonText, modelScriptField, JFileChooser.OPEN_DIALOG, rFilter);
    JButton parametersScriptButton = UIUtils.createBrowseButton(buttonText, parametersScriptField,
        JFileChooser.OPEN_DIALOG, rFilter);
    JButton visualizationScriptButton = UIUtils.createBrowseButton(buttonText,
        visualizationScriptField, JFileChooser.OPEN_DIALOG, rFilter);
    JButton spreadsheetButton = UIUtils.createBrowseButton(buttonText, spreadsheetField,
        JFileChooser.OPEN_DIALOG, spreadsheetFilter);

    modelScriptButton.setToolTipText(modelScriptToolTip);
    parametersScriptButton.setToolTipText(parameterScriptToolTip);
    visualizationScriptButton.setToolTipText(visualizationScriptToolTip);
    spreadsheetButton.setToolTipText(spreadsheetScriptToolTip);

    // labels
    String modelScriptLabelText = bundle.getString("modelscript_label");
    String paramScriptLabelText = bundle.getString("parameterscript_label");
    String visualizationScriptLabelText = bundle.getString("visualizationscript_label");
    String spreadsheetLabelText = bundle.getString("spreadsheet_label");

    FLabel modelScriptLabel = new FLabel(modelScriptLabelText);
    FLabel parametersScriptLabel = new FLabel(paramScriptLabelText);
    FLabel visualizationScriptLabel = new FLabel(visualizationScriptLabelText);
    FLabel spreadsheetLabel = new FLabel(spreadsheetLabelText);

    // formPanel
    List<FLabel> labels = Arrays.asList(modelScriptLabel, parametersScriptLabel,
        visualizationScriptLabel, spreadsheetLabel);
    List<JTextField> fields = Arrays.asList(modelScriptField, parametersScriptField,
        visualizationScriptField, spreadsheetField);
    List<JButton> buttons = Arrays.asList(modelScriptButton, parametersScriptButton,
        visualizationScriptButton, spreadsheetButton);

    FPanel formPanel = UIUtils.createFormPanel(labels, fields, buttons);
    JPanel northPanel = UI.createNorthPanel(formPanel);
    northPanel.setBackground(UIUtils.WHITE);

    addTab("Options", northPanel);
    addTab("Files", UIUtils.createResourcesPanel(getPanel(), listModel));
  }
}
