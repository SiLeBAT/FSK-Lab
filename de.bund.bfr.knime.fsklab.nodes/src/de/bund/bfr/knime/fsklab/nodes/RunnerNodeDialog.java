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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;

public class RunnerNodeDialog extends DataAwareNodeDialogPane {

  private final RunnerNodeSettings settings;

  private final SpinnerNumberModel widthModel;
  private final SpinnerNumberModel heightModel;
  private final JTextField resolutionField;
  private final SpinnerNumberModel textSizeModel;
  private final DefaultComboBoxModel<String> simulationModel;

  public RunnerNodeDialog() {
    settings = new RunnerNodeSettings();

    widthModel = new SpinnerNumberModel(settings.width, null, null, 1);
    heightModel = new SpinnerNumberModel(settings.height, null, null, 1);
    resolutionField = new JTextField(settings.res);
    textSizeModel = new SpinnerNumberModel(settings.pointSize, null, null, 1);
    simulationModel = new DefaultComboBoxModel<String>();

    createUI();
  }

  private void createUI() {

    ResourceBundle bundle = ResourceBundle.getBundle("RunnerNodeBundle", new UTF8Control());

    FLabel widthLabel = new FLabel(bundle.getString("width_label"));
    FLabel heightLabel = new FLabel(bundle.getString("height_label"));
    FLabel resolutionLabel = new FLabel(bundle.getString("res_label"));
    FLabel textSizeLabel = new FLabel(bundle.getString("textsize_label"));
    FLabel simulationLabel = new FLabel(bundle.getString("simulation_label"));
    List<FLabel> labels =
        Arrays.asList(widthLabel, heightLabel, resolutionLabel, textSizeLabel, simulationLabel);

    JSpinner widthSpinner = new JSpinner(widthModel);
    JSpinner heightSpinner = new JSpinner(heightModel);
    JSpinner textSizeSpinner = new JSpinner(textSizeModel);
    resolutionField.setColumns(5);
    resolutionField.setHorizontalAlignment(JTextField.RIGHT);

    JComboBox<String> simulationField = new JComboBox<>(simulationModel);
    // right align simulationField
    ((JTextField) simulationField.getEditor().getEditorComponent())
        .setHorizontalAlignment(JTextField.RIGHT);

    // Set tooltips
    widthSpinner.setToolTipText(bundle.getString("width_tooltip"));
    heightSpinner.setToolTipText(bundle.getString("height_tooltip"));
    resolutionField.setToolTipText(bundle.getString("res_tooltip"));
    textSizeSpinner.setToolTipText(bundle.getString("textsize_tooltip"));
    simulationField.setToolTipText(bundle.getString("simulation_tooltip"));

    List<JComponent> fields = Arrays.asList(widthSpinner, heightSpinner, resolutionField,
        textSizeSpinner, simulationField);

    FPanel formPanel = UIUtils.createFormPanel(labels, fields);
    FPanel northPanel = UIUtils.createNorthPanel(formPanel);

    addTab("Options", northPanel);
  }

  /** Load settings from saved settings. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {

    try {
      this.settings.load(settings);

      widthModel.setValue(this.settings.width);
      heightModel.setValue(this.settings.height);
      resolutionField.setText(this.settings.res);
      textSizeModel.setValue(this.settings.pointSize);

      // Remove selected simulation if not contained in new list of simulations. Otherwise set value
      // from settings as selected.
      boolean isContained = false;
      for (int i = 0; i < simulationModel.getSize(); i++) {
        if (this.settings.simulation.equals(simulationModel.getElementAt(i))) {
          isContained = true;
          break;
        }
      }
      simulationModel.setSelectedItem(isContained ? this.settings.simulation : null);

    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  /** Load settings from input ports. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] inputs)
      throws NotConfigurableException {

    simulationModel.removeAllElements();

    if (inputs.length == 1 && inputs[0] != null) {
      FskPortObject inObj = (FskPortObject) inputs[0];
      for (FskSimulation fskSimulation : inObj.simulations) {
        simulationModel.addElement(fskSimulation.getName());
      }

      // Remove selected simulation if not contained in new list of simulations. Otherwise set value
      // from settings as selected.
      boolean isContained = false;
      for (int i = 0; i < simulationModel.getSize(); i++) {
        if (this.settings.simulation.equals(simulationModel.getElementAt(i))) {
          isContained = true;
          break;
        }
      }
      simulationModel.setSelectedItem(isContained ? this.settings.simulation : null);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    this.settings.width = widthModel.getNumber().intValue();
    this.settings.height = heightModel.getNumber().intValue();
    this.settings.res = resolutionField.getText();
    this.settings.pointSize = textSizeModel.getNumber().intValue();

    // selectedSimulation may be null if there is no selection
    String selectedSimulation = (String) simulationModel.getSelectedItem();

    // this.settings.simulation is set "" if selectedSimulation is null
    this.settings.simulation = StringUtils.defaultString(selectedSimulation);

    this.settings.save(settings);
  }
}
