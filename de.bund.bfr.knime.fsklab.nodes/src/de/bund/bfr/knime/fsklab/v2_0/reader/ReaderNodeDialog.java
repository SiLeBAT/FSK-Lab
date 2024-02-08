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
package de.bund.bfr.knime.fsklab.v2_0.reader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.knime.base.node.io.table.read.ReadTableNodeModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;

class ReaderNodeDialog extends NodeDialogPane {

  private final SettingsModelString filePath;
  private final SettingsModelString overridePackages ;
  private final FilesHistoryPanel m_filePanel;
  
  private final JCheckBox overrideCheckBock; 

  ReaderNodeDialog() {
   filePath = new SettingsModelString(ReaderNodeModel.CFG_FILE, "");
   overridePackages = new SettingsModelString(ReaderNodeModel.CFG_OVERRIDE, null);
    m_filePanel =
        new FilesHistoryPanel(createFlowVariableModel("filename", FlowVariable.Type.STRING),
            "fskx_reader", LocationValidation.FileInput, ".fskx");
    overrideCheckBock = new JCheckBox();
    
    addTab("Options", initLayout());
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    try {
      filePath.loadSettingsFrom(settings);
      
      
      m_filePanel.updateHistory();
      m_filePanel.setSelectedFile(filePath.getStringValue());
      

      if(settings.containsKey("overridePackages")) {
        overridePackages.loadSettingsFrom(settings);
        overrideCheckBock.setSelected(overridePackages.getStringValue()!=null?Boolean.parseBoolean(overridePackages.getStringValue()):false);
      }
      
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    filePath.setStringValue(m_filePanel.getSelectedFile().trim());
    overridePackages.setStringValue(""+overrideCheckBock.isSelected());

    m_filePanel.addToHistory();

    filePath.saveSettingsTo(settings);
    overridePackages.saveSettingsTo(settings);
  }

  private JPanel initLayout() {

    final JPanel filePanel = new JPanel(new BorderLayout());
    filePanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Input location:"));
    filePanel.add(m_filePanel, BorderLayout.NORTH);
    filePanel.add(Box.createHorizontalGlue());
    
    JPanel overridePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    overridePanel.add(new JLabel("Override Packages Info:"));
    overridePanel.add(overrideCheckBock);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(filePanel);
    panel.add(overridePanel);

    return panel;
  }
}
