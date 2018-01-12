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
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.swing.UI;

public class RunnerNodeDialog extends NodeDialogPane {

  private final RunnerNodeSettings settings;

  private final SpinnerNumberModel widthModel;
  private final SpinnerNumberModel heightModel;
  private final JTextField resolutionField;
  private final SpinnerNumberModel textSizeModel;

  public RunnerNodeDialog() {
    settings = new RunnerNodeSettings();

    widthModel = new SpinnerNumberModel(settings.width, null, null, 1);
    heightModel = new SpinnerNumberModel(settings.height, null, null, 1);
    resolutionField = new JTextField(settings.res);
    textSizeModel = new SpinnerNumberModel(settings.pointSize, null, null, 1);

    createUI();
  }

  private void createUI() {
    ResourceBundle bundle = ResourceBundle.getBundle("RunnerNodeBundle");
    String widthLabelText = UIUtils.getUnicodeString(bundle, "width_label");
    String heightLabelText = UIUtils.getUnicodeString(bundle, "height_label");
    String resLabelText = UIUtils.getUnicodeString(bundle, "res_label");
    String textSizeText = UIUtils.getUnicodeString(bundle, "textsize_label");

    String widthTooltip = UIUtils.getUnicodeString(bundle, "width_tooltip");
    String heightTooltip = UIUtils.getUnicodeString(bundle, "height_tooltip");
    String resTooltip = UIUtils.getUnicodeString(bundle, "res_tooltip");
    String textSizeTooltip = UIUtils.getUnicodeString(bundle, "textsize_tooltip");

    FLabel widthLabel = new FLabel(widthLabelText);
    FLabel heightLabel = new FLabel(heightLabelText);
    FLabel resolutionLabel = new FLabel(resLabelText);
    FLabel textSizeLabel = new FLabel(textSizeText);
    List<FLabel> labels = Arrays.asList(widthLabel, heightLabel, resolutionLabel, textSizeLabel);

    JSpinner widthSpinner = new JSpinner(widthModel);
    JSpinner heightSpinner = new JSpinner(heightModel);
    JSpinner textSizeSpinner = new JSpinner(textSizeModel);
    resolutionField.setColumns(5);
    resolutionField.setHorizontalAlignment(JTextField.RIGHT);

    // Set tooltips
    widthSpinner.setToolTipText(widthTooltip);
    heightSpinner.setToolTipText(heightTooltip);
    resolutionField.setToolTipText(resTooltip);
    textSizeSpinner.setToolTipText(textSizeTooltip);

    List<JComponent> fields =
        Arrays.asList(widthSpinner, heightSpinner, resolutionField, textSizeSpinner);

    JPanel formPanel = UIUtils.createFormPanel(labels, fields);
    JPanel northPanel = UI.createNorthPanel(formPanel);
    northPanel.setBackground(UIUtils.WHITE);

    addTab("Options", northPanel);
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {

    try {
      this.settings.load(settings);

      widthModel.setValue(this.settings.width);
      heightModel.setValue(this.settings.height);
      resolutionField.setText(this.settings.res);
      textSizeModel.setValue(this.settings.pointSize);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    this.settings.width = widthModel.getNumber().intValue();
    this.settings.height = heightModel.getNumber().intValue();
    this.settings.res = resolutionField.getText();
    this.settings.pointSize = textSizeModel.getNumber().intValue();

    this.settings.save(settings);
  }
}
