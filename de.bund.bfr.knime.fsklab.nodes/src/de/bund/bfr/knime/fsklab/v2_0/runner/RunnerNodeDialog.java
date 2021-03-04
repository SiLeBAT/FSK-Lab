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
package de.bund.bfr.knime.fsklab.v2_0.runner;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.common.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.swing.UI;

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

    scriptPanel = new ScriptPanel("Preview", "", false, false);

    createUI();
  }

  private void createUI() {

    // Fields
    resolutionField.setColumns(5);
    resolutionField.setHorizontalAlignment(SwingConstants.RIGHT);
    resolutionField.setToolTipText("Nominal resolution in ppi");

    JComboBox<FskSimulation> simulationField = new JComboBox<>(simulationModel);
    // right align simulationField
    ((JTextField) simulationField.getEditor().getEditorComponent())
        .setHorizontalAlignment(SwingConstants.RIGHT);

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
    simulationField.setToolTipText("Selected simulation");

    scriptPanel.setBorder(BorderFactory.createTitledBorder("Preview"));

    JPanel simulationSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    simulationSelectionPanel.add(new JLabel("Simulation:"));
    simulationSelectionPanel.add(simulationField);

    JPanel simulationSettingsPanel = new JPanel();
    simulationSettingsPanel.setLayout(new BoxLayout(simulationSettingsPanel, BoxLayout.Y_AXIS));
    simulationSettingsPanel.add(simulationSelectionPanel);
    simulationSettingsPanel.add(scriptPanel);

    addTab("Simulation settings", simulationSettingsPanel);

    JPanel plotSettingsPanel = new JPanel(new GridBagLayout());

    // Prepare constraints for the first column
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(5, 5, 5, 5);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.EAST;
    plotSettingsPanel.add(new JLabel("Width:"), constraints);

    constraints.gridy++;
    plotSettingsPanel.add(new JLabel("Height:"), constraints);

    constraints.gridy++;
    plotSettingsPanel.add(new JLabel("Resolution:"), constraints);

    constraints.gridy++;
    plotSettingsPanel.add(new JLabel("Text size:"), constraints);

    // Prepare constraints for the second column
    constraints.gridx++;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.weightx = 1.0;
    JSpinner heightSpinner = new JSpinner(heightModel);
    heightSpinner.setToolTipText("Height of the plot");
    plotSettingsPanel.add(heightSpinner, constraints);

    constraints.gridy++;
    JSpinner widthSpinner = new JSpinner(widthModel);
    widthSpinner.setToolTipText("Width of the plot");
    plotSettingsPanel.add(widthSpinner, constraints);

    constraints.gridy++;
    plotSettingsPanel.add(resolutionField, constraints);

    constraints.gridy++;
    JSpinner textSizeSpinner = new JSpinner(textSizeModel);
    textSizeSpinner.setToolTipText("Text size");
    plotSettingsPanel.add(textSizeSpinner, constraints);

    addTab("Plot settings", UI.createNorthPanel(plotSettingsPanel));
  }

  /** Load settings from input ports. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] inputs)
      throws NotConfigurableException {

    FskPortObject inObj = (FskPortObject) inputs[0];

    FskSimulation selectedSimulation;

    String selectedSimulationInSettings = settings.getString("simulation", "");
    if (!selectedSimulationInSettings.isEmpty()) {
      // If a simulation is configured in the settings then pick it
      Optional<FskSimulation> sim = inObj.simulations.stream()
          .filter(it -> it.getName().equals(selectedSimulationInSettings)).findFirst();
      // If not present assign default simulation
      selectedSimulation = sim.orElse(inObj.simulations.get(0));
    } else {
      // If no selected simulation is saved in settings then pick the simulation from the input port
      selectedSimulation = inObj.simulations.get(inObj.selectedSimulationIndex);
    }

    simulationModel.removeAllElements();
    inObj.simulations.forEach(simulationModel::addElement);
    simulationModel.setSelectedItem(selectedSimulation);
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
