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
package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class PMMToFSKExporterNodeDialog extends DataAwareNodeDialogPane implements ActionListener {

  private PMMToFSKNodeSettings set;

  private JComboBox<String> models;
  private JPanel panel;
  PMModelReader pmmodelReader = new PMModelReader();

  /**
   * New pane for configuring the ModelEstimation node.
   */
  protected PMMToFSKExporterNodeDialog() {
    models = new JComboBox<>();

    JPanel modelsPanel = new JPanel();

    modelsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    modelsPanel.add(models);


    panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(modelsPanel, BorderLayout.CENTER);

    addTab("Options", panel);
  }

  protected void loadSettingsFrom(NodeSettingsRO settings, BufferedDataTable[] input)
      throws NotConfigurableException {
    if (SchemaFactory.conformsM1Schema(input[0].getSpec())) {
      pmmodelReader.readPrimaryTable(input[0]);
    } else if (SchemaFactory.conformsM2Schema(input[0].getSpec())) {
      pmmodelReader.readSecondaryTable(input[0]);
    } else {
      System.out.println("was");
    }

    if (models.getItemCount() == 0)
      for (Entry<String, String> e : pmmodelReader.modelNames.entrySet()) {
        String key = e.getKey();
        models.addItem(key);
      }


    set = new PMMToFSKNodeSettings();
    try {
      set.load(settings);
    } catch (InvalidSettingsException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    models.setSelectedItem(set.getSelectedModel());
    models.addActionListener(this);

    initGUI();
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    set.setSelectedModel((String) models.getSelectedItem());
    set.save(settings);
  }


  private void initGUI() {
    Dimension preferredSize = panel.getPreferredSize();

    panel.setPreferredSize(preferredSize);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {

  }



}
