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
package de.bund.bfr.knime.fsklab.nodes.v1_7_2.writer;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;

public class WriterNodeDialog extends NodeDialogPane {

  private final WriterNodeSettings nodeSettings;

  private final FilesHistoryPanel m_filePanel;

  public WriterNodeDialog() {
    nodeSettings = new WriterNodeSettings();

    m_filePanel = new FilesHistoryPanel(createFlowVariableModel("file", FlowVariable.Type.STRING),
        "fskx_writer", LocationValidation.FileOutput, ".fskx");

    addTab("Options", initLayout());
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    try {
      nodeSettings.load(settings);

      m_filePanel.updateHistory();
      m_filePanel.setSelectedFile(nodeSettings.filePath);

    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    nodeSettings.filePath = m_filePanel.getSelectedFile().trim();
    m_filePanel.addToHistory();
    
    nodeSettings.save(settings);
  }
  
  private JPanel initLayout() {

    final JPanel filePanel = new JPanel(new BorderLayout());
    filePanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Output location:"));
    filePanel.add(m_filePanel, BorderLayout.NORTH);
    filePanel.add(Box.createHorizontalGlue());

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(filePanel);

    return panel;
  }
}
