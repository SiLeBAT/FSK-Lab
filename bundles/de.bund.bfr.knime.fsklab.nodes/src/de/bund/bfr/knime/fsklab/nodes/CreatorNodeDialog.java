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

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
        LocationValidation.FileInput, ".r");
    m_visualizationScriptPanel = new FilesHistoryPanel(visualizationScriptVariable,
        "visualizationScript", LocationValidation.FileInput, ".r");
    m_readmePanel =
        new FilesHistoryPanel(readmeVariable, "readme", LocationValidation.None, ".txt");
    m_workingDirectoryPanel =
        new FilesHistoryPanel(directoryVariable, "directory", LocationValidation.None);
    m_spreadsheetPanel = new FilesHistoryPanel(m_spreadsheetVariable, "spreadsheet",
        LocationValidation.FileInput, ".xlsx");
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

      // Populate sheetField with sheet names from the spreadsheet in settings
      sheetModel.removeAllElements();
      new SheetFieldTask(this.settings.spreadsheet).execute();
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

    m_spreadsheetVariable.addChangeListener(new ChangeListener() {
      
      @Override
      public void stateChanged(ChangeEvent e) {
        sheetModel.removeAllElements();
        new SheetFieldTask(m_spreadsheetVariable.getVariableValue().get().getStringValue()).execute();
      }
    });

    final JPanel modelScriptPanel = new JPanel(new BorderLayout());
    modelScriptPanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Model script:"));
    modelScriptPanel.add(m_modelScriptPanel, BorderLayout.NORTH);
    modelScriptPanel.add(Box.createHorizontalGlue());

    final JPanel visualizationScriptPanel = new JPanel(new BorderLayout());
    visualizationScriptPanel.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createEtchedBorder(), "Visualization script:"));
    visualizationScriptPanel.add(m_visualizationScriptPanel, BorderLayout.NORTH);
    visualizationScriptPanel.add(Box.createHorizontalGlue());

    final JPanel readmePanel = new JPanel(new BorderLayout());
    readmePanel
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Readme:"));
    readmePanel.add(m_readmePanel, BorderLayout.NORTH);
    readmePanel.add(Box.createHorizontalGlue());

    final JPanel workingDirectoryPanel = new JPanel(new BorderLayout());
    workingDirectoryPanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Working directory:"));
    workingDirectoryPanel.add(m_workingDirectoryPanel, BorderLayout.NORTH);
    workingDirectoryPanel.add(Box.createHorizontalGlue());

    final JPanel spreadsheetPanel = new JPanel(new BorderLayout());
    spreadsheetPanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Spreadsheet:"));
    spreadsheetPanel.add(m_spreadsheetPanel, BorderLayout.NORTH);
    spreadsheetPanel.add(Box.createHorizontalGlue());
    
    final JPanel sheetPanel = new JPanel(new BorderLayout());
    sheetPanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sheet:"));
    sheetPanel.add(new JComboBox<>(sheetModel), BorderLayout.NORTH);
    sheetPanel.add(Box.createHorizontalGlue());
    
    final JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(modelScriptPanel);
    panel.add(visualizationScriptPanel);
    panel.add(readmePanel);
    panel.add(workingDirectoryPanel);
    panel.add(spreadsheetPanel);
    panel.add(sheetPanel);

    addTab("Options", panel);
  }

  private final Object LOCK = new Object();

  private class SheetFieldTask extends SwingWorker<List<String>, Void> {

    private final String path;

    public SheetFieldTask(String path) {
      this.path = path;
    }

    @Override
    protected List<String> doInBackground() throws Exception {

      List<String> names = new ArrayList<>();

      synchronized (LOCK) {

        if (!path.isEmpty()) {
          try {
            File file = FileUtil.getFileFromURL(FileUtil.toURL(path));
            if (file.exists()) {
              try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                  names.add(workbook.getSheetName(i));
                }
              }
            }
          } catch (InvalidPathException | InvalidFormatException | IOException exception) {
            exception.printStackTrace();
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
