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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;

public class RunnerNodeDialog extends DataAwareNodeDialogPane {

  private final RunnerNodeSettings settings;

  private final SpinnerNumberModel widthModel;
  private final SpinnerNumberModel heightModel;
  private final JTextField resolutionField;
  private final SpinnerNumberModel textSizeModel;
  private final DefaultComboBoxModel<FskSimulation> simulationModel;
  private final ScriptPanel scriptPanel;

  public RunnerNodeDialog() {
    settings = new RunnerNodeSettings();

    widthModel = new SpinnerNumberModel(settings.width, null, null, 1);
    heightModel = new SpinnerNumberModel(settings.height, null, null, 1);
    resolutionField = new JTextField(settings.res);
    textSizeModel = new SpinnerNumberModel(settings.pointSize, null, null, 1);
    simulationModel = new DefaultComboBoxModel<>();

    scriptPanel = new ScriptPanel("Preview", "", false);
    scriptPanel.setBackground(UIUtils.WHITE);

    createUI();
  }

  private void createUI() {

    ResourceBundle bundle = ResourceBundle.getBundle("RunnerNodeBundle", new UTF8Control());

    // Labels
    FLabel widthLabel = new FLabel(bundle.getString("width_label"));
    FLabel heightLabel = new FLabel(bundle.getString("height_label"));
    FLabel resolutionLabel = new FLabel(bundle.getString("res_label"));
    FLabel textSizeLabel = new FLabel(bundle.getString("textsize_label"));
    FLabel simulationLabel = new FLabel(bundle.getString("simulation_label"));
    List<FLabel> labels = Arrays.asList(widthLabel, heightLabel, resolutionLabel, textSizeLabel);

    // Fields
    JSpinner widthSpinner = new JSpinner(widthModel);
    JSpinner heightSpinner = new JSpinner(heightModel);
    JSpinner textSizeSpinner = new JSpinner(textSizeModel);
    resolutionField.setColumns(5);
    resolutionField.setHorizontalAlignment(JTextField.RIGHT);

    JComboBox<FskSimulation> simulationField = new JComboBox<>(simulationModel);
    // right align simulationField
    ((JTextField) simulationField.getEditor().getEditorComponent())
        .setHorizontalAlignment(JTextField.RIGHT);

    simulationField.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        FskSimulation selectedSimulation = (FskSimulation) simulationModel.getSelectedItem();

        // The selectedSimulation is null when the simulationModel is cleared.
        String previewScript =
            selectedSimulation == null ? "" : NodeUtils.buildParameterScript(selectedSimulation);
        scriptPanel.setText(previewScript);
        scriptPanel.revalidate();
        scriptPanel.repaint();
      }
    });

    // Set tooltips
    widthSpinner.setToolTipText(bundle.getString("width_tooltip"));
    heightSpinner.setToolTipText(bundle.getString("height_tooltip"));
    resolutionField.setToolTipText(bundle.getString("res_tooltip"));
    textSizeSpinner.setToolTipText(bundle.getString("textsize_tooltip"));
    simulationField.setToolTipText(bundle.getString("simulation_tooltip"));

    List<JComponent> fields =
        Arrays.asList(widthSpinner, heightSpinner, resolutionField, textSizeSpinner);

    FPanel formPanel = UIUtils.createFormPanel(labels, fields);
    FPanel northPanel = UIUtils.createNorthPanel(formPanel);

    // Simulation panel
    {
      FPanel simulationSelectionPanel =
          UIUtils.createFormPanel(Arrays.asList(simulationLabel), Arrays.asList(simulationField));
      scriptPanel.setBorder(BorderFactory.createTitledBorder("Preview"));

      FPanel simulationPanel = new FPanel();
      simulationPanel.setBorder(BorderFactory.createTitledBorder("Simulation"));
      simulationPanel.setLayout(new BoxLayout(simulationPanel, BoxLayout.Y_AXIS));
      simulationPanel.add(UIUtils.createNorthPanel(simulationSelectionPanel));
      simulationPanel.add(scriptPanel);

      northPanel.add(simulationPanel);
    }

    addTab("Options", northPanel);
  }

  /** Load settings from input ports. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] inputs)
      throws NotConfigurableException {

    simulationModel.removeAllElements();

    if (inputs.length == 1 && inputs[0] != null) {
      FskPortObject inObj = (FskPortObject) inputs[0];

      // Update combobox
      inObj.simulations.forEach(simulationModel::addElement);

      // Look for simulation with the name stored in settings. If the simulation is not found
      // (not contained) in new list of simulations then there is no selection.
      FskSimulation selectedSimulation = null;
      for (int i = 0; i < simulationModel.getSize(); i++) {
        FskSimulation currentSimulation = simulationModel.getElementAt(i);
        if (currentSimulation.getName().equals(this.settings.simulation)) {
          selectedSimulation = currentSimulation;
          break;
        }
      }

      // If there is no selection the default simulation is selected (first simulation)
      if (selectedSimulation != null) {
        simulationModel.setSelectedItem(selectedSimulation);
      } else {
        FskSimulation defaultSimulation = inObj.simulations.get(0);
        simulationModel.setSelectedItem(defaultSimulation);
      }
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    this.settings.width = widthModel.getNumber().intValue();
    this.settings.height = heightModel.getNumber().intValue();
    this.settings.res = resolutionField.getText();
    this.settings.pointSize = textSizeModel.getNumber().intValue();

    // selectedSimulation may be null if there is no selection
    FskSimulation selectedSimulation = (FskSimulation) simulationModel.getSelectedItem();
    this.settings.simulation = selectedSimulation.getName();

    this.settings.save(settings);
  }
}
