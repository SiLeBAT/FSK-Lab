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
package de.bund.bfr.knime.fsklab.v2_0.creator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.util.FileUtil;

public class CreatorNodeDialog extends NodeDialogPane {

  private final CreatorNodeSettings settings;

  private final FilesHistoryPanel m_modelScriptPanel;
  private final FilesHistoryPanel m_visualizationScriptPanel;
  private final FilesHistoryPanel m_readmePanel;
  private final FilesHistoryPanel m_workingDirectoryPanel;
  private final FilesHistoryPanel m_spreadsheetPanel;
  private final DefaultComboBoxModel<String> sheetModel;

  private FlowVariableModel m_spreadsheetVariable;

  public CreatorNodeDialog() {

    FlowVariableModel modelScriptVariable =
        createFlowVariableModel("modelScript", FlowVariable.Type.STRING);
    FlowVariableModel visualizationScriptVariable =
        createFlowVariableModel("visualizationScript", FlowVariable.Type.STRING);
    FlowVariableModel readmeVariable = createFlowVariableModel("readme", FlowVariable.Type.STRING);
    m_spreadsheetVariable = createFlowVariableModel("spreadsheet", FlowVariable.Type.STRING);
    FlowVariableModel directoryVariable =
        createFlowVariableModel("workingDirectory", FlowVariable.Type.STRING);

    settings = new CreatorNodeSettings();

    m_modelScriptPanel = new FilesHistoryPanel(modelScriptVariable, "modelScript",
        LocationValidation.FileInput, ".r",".py");
    m_modelScriptPanel.setSelectMode(JFileChooser.FILES_ONLY);

    m_visualizationScriptPanel = new FilesHistoryPanel(visualizationScriptVariable,
        "visualizationScript", LocationValidation.FileInput, ".r","py");
    m_visualizationScriptPanel.setSelectMode(JFileChooser.FILES_ONLY);

    m_readmePanel =
        new FilesHistoryPanel(readmeVariable, "readme", LocationValidation.FileInput, ".txt");
    m_readmePanel.setSelectMode(JFileChooser.FILES_ONLY);

    m_workingDirectoryPanel =
        new FilesHistoryPanel(directoryVariable, "directory", LocationValidation.DirectoryInput);
    m_workingDirectoryPanel.setSelectMode(JFileChooser.DIRECTORIES_ONLY);

    m_spreadsheetPanel = new FilesHistoryPanel(m_spreadsheetVariable, "spreadsheet",
        LocationValidation.FileInput, ".xlsx");
    m_spreadsheetPanel.setSelectMode(JFileChooser.FILES_ONLY);

    sheetModel = new DefaultComboBoxModel<>();

    createUI();
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
      throws NotConfigurableException {
    try {
      this.settings.load(settings);

      m_modelScriptPanel.updateHistory();
      m_modelScriptPanel.setSelectedFile(this.settings.modelScript);

      m_visualizationScriptPanel.updateHistory();
      m_visualizationScriptPanel.setSelectedFile(this.settings.visualizationScript);

      m_readmePanel.updateHistory();
      m_readmePanel.setSelectedFile(this.settings.getReadme());

      m_workingDirectoryPanel.updateHistory();
      m_workingDirectoryPanel.setSelectedFile(this.settings.getWorkingDirectory());

      m_spreadsheetPanel.updateHistory();
      m_spreadsheetPanel.setSelectedFile(this.settings.spreadsheet);

      // Set selected sheet from settings
      sheetModel.setSelectedItem(this.settings.sheet);

    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    this.settings.modelScript = m_modelScriptPanel.getSelectedFile().trim();
    m_modelScriptPanel.addToHistory();

    this.settings.visualizationScript = m_visualizationScriptPanel.getSelectedFile().trim();
    m_visualizationScriptPanel.addToHistory();

    this.settings.setReadme(m_readmePanel.getSelectedFile().trim());
    m_readmePanel.addToHistory();

    this.settings.setWorkingDirectory(m_workingDirectoryPanel.getSelectedFile().trim());
    m_workingDirectoryPanel.addToHistory();

    this.settings.spreadsheet = m_spreadsheetPanel.getSelectedFile().trim();
    m_spreadsheetPanel.addToHistory();

    // selected sheet may be null if there is no selection
    this.settings.sheet = (String) sheetModel.getSelectedItem();

    this.settings.save(settings);
  }

  private void createUI() {

    m_spreadsheetPanel.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        sheetModel.removeAllElements();

        String stringPath = m_spreadsheetPanel.getSelectedFile();
        if (StringUtils.isNotEmpty(stringPath)) {

          try {
            URL url = FileUtil.toURL(stringPath);
            Path path = FileUtil.resolveToPath(url);
            if (Files.exists(path)) {
              new SheetFieldTask(path.toFile()).execute();
            }
          } catch (Exception exception) {
          }
        }
      }
    });

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints();

    g.gridx = 0;
    g.gridy = 0;
    g.insets = new Insets(0, 5, 22, 5);
    g.anchor = GridBagConstraints.EAST;
    panel.add(new JLabel("Model script:"), g);

    g.gridy++;
    panel.add(new JLabel("Visualization script:"), g);

    g.gridy++;
    panel.add(new JLabel("Readme:"), g);

    g.gridy++;
    panel.add(new JLabel("Working directory:"), g);

    g.gridy++;
    panel.add(new JLabel("Spreadsheet:"), g);

    g.gridy++;
    g.insets = new Insets(5, 5, 5, 5);
    panel.add(new JLabel("Sheet:"), g);

    g.gridy = 0;
    g.gridx++;
    g.insets = new Insets(5, 0, 5, 0);
    g.anchor = GridBagConstraints.WEST;
    g.fill = GridBagConstraints.HORIZONTAL;
    g.weightx = 1.0;
    panel.add(m_modelScriptPanel, g);

    g.gridy++;
    panel.add(m_visualizationScriptPanel, g);

    g.gridy++;
    panel.add(m_readmePanel, g);

    g.gridy++;
    panel.add(m_workingDirectoryPanel, g);

    g.gridy++;
    panel.add(m_spreadsheetPanel, g);

    g.gridy++;
    g.insets = new Insets(5, 5, 5, 5);
    panel.add(new JComboBox<>(sheetModel), g);

    addTab("Options", panel);
  }

  private final Object LOCK = new Object();

  private class SheetFieldTask extends SwingWorker<List<String>, Void> {

    private final File file;

    public SheetFieldTask(File file) {
      this.file = file;
    }

    @Override
    protected List<String> doInBackground() throws Exception {

      List<String> names = new ArrayList<>();

      synchronized (LOCK) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
          for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            names.add(workbook.getSheetName(i));
          }
        }
      }

      return names;
    }

    @Override
    protected void done() {
      try {
        List<String> names = get();
        names.forEach(sheetModel::addElement);
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}
