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
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
import de.bund.bfr.swing.UI;

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
        
        Optional<FlowVariable> var = m_spreadsheetVariable.getVariableValue();
        if (var.isPresent()) {
          String spreadsheetPath = var.get().getStringValue();
          new SheetFieldTask(spreadsheetPath).execute();
        }
      }
    });
    
    List<JComponent> labels = new ArrayList<>(4);
    List<JComponent> panels = new ArrayList<>(4);
    
    // model script
    labels.add(new JLabel("Model script:"));
    panels.add(m_modelScriptPanel);

    // visualization script
    labels.add(new JLabel("Visualization script:"));
    panels.add(m_visualizationScriptPanel);

    // readme
    labels.add(new JLabel("Readme:"));
    panels.add(m_readmePanel);
    
    // working directory
    labels.add(new JLabel("Working directory:"));
    panels.add(m_workingDirectoryPanel);
    
    JPanel filesPanel = UI.createOptionsPanel("Input files", labels, panels);

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
    
    labels.clear();
    panels.clear();
    
    labels.add(new JLabel("Spreadsheet:"));
    panels.add(m_spreadsheetPanel);
    
    labels.add(new JLabel("Sheet:"));
    panels.add(new JComboBox<>(sheetModel));
    
    JPanel metadataForm = UI.createOptionsPanel("Metadata", labels, panels);
    
    JPanel northPanel = UI.createNorthPanel(filesPanel);
    northPanel.add(UI.createNorthPanel(metadataForm));

    addTab("Options", northPanel);
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
