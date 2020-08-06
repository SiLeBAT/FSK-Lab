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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.environment.ArchivedEnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.ExistingEnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.FilesEnvironmentManager;
import de.bund.bfr.metadata.swagger.Model;

class FSKEditorJSNodeDialog extends DataAwareNodeDialogPane {

  private static final String README_FILE = "readmeFile";

  /** Absolute path to readme file. */
  private String m_readmeFile;

  private final FSKEditorJSConfig m_config;

  private final DefaultComboBoxModel<ModelType> modelTypeComboBoxModel;
  private final FilesHistoryPanel m_readmePanel;

  private final JTextField m_workingDirectoryField;

  private final JRadioButton m_archivedEnvironmentButton;
  private final JRadioButton m_directoryEnvironmentButton;
  private final JRadioButton m_filesEnvironmentButton;

  private final DefaultTableModel m_filesTableModel;

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  enum ModelType {

    genericModel("Generic model"),
    dataModel("Data model"),
    consumptionModel("Consumption model"),
    doseResponseModel("Dose-response model"),
    exposureModel("Exposure model"),
    healthModel("Health metrics model"),
    otherModel("Other Empirical models"),
    predictiveModel("Predictive model"),
    processModel("Process model"),
    qraModel("QRA model"),
    riskModel("Risk characterization model"),
    toxicologicalModel("Toxicological reference value model");

    private final String displayString;

    private ModelType(String displayString) {
      this.displayString = displayString;
    }

    @Override
    public String toString() {
      return displayString;
    }
  }

  public FSKEditorJSNodeDialog() {

    m_config = new FSKEditorJSConfig();

    modelTypeComboBoxModel = new DefaultComboBoxModel<>(ModelType.values());

    FlowVariableModel readmeVariable = createFlowVariableModel("readme", FlowVariable.Type.STRING);
    m_readmePanel =
        new FilesHistoryPanel(readmeVariable, "readme", LocationValidation.None, ".txt");

    m_workingDirectoryField = new JTextField(80);
    m_workingDirectoryField.setEditable(false);

    // Radio buttons
    m_archivedEnvironmentButton = new JRadioButton("Archived environment");
    m_directoryEnvironmentButton = new JRadioButton("Directory environment");
    m_filesEnvironmentButton = new JRadioButton("Files environment");

    m_filesTableModel = new DefaultTableModel(0, 1);

    createUI();
  }

  /**
   * Update the dialog with the passed data.
   * 
   * @param modelType Non null model type.
   * @param readmeFile Empty string if not set.
   * @param environment Null if not set.
   */
  private void updateDialog(ModelType modelType, String readmeFile,
      EnvironmentManager environment) {
    modelTypeComboBoxModel.setSelectedItem(modelType);
    m_readmePanel.setSelectedFile(readmeFile);
    clearEnvironmentPanel();

    if (environment != null) {

      if (environment instanceof ArchivedEnvironmentManager) {
        ArchivedEnvironmentManager archivedEnvironment = (ArchivedEnvironmentManager) environment;

        m_archivedEnvironmentButton.setSelected(true);

        // Update m_workingDirectoryField
        m_workingDirectoryField.setEnabled(false);
        m_workingDirectoryField.setText(archivedEnvironment.getArchivePath());

        // Update m_filesTableModel
        for (String entry : archivedEnvironment.getEntries()) {
          String[] row = {entry};
          m_filesTableModel.addRow(row);
        }
      } else if (environment instanceof ExistingEnvironmentManager) {
        ExistingEnvironmentManager directoryManager = (ExistingEnvironmentManager) environment;

        m_archivedEnvironmentButton.setEnabled(false);
        m_directoryEnvironmentButton.setSelected(true);

        // Update m_workingDirectoryField
        m_workingDirectoryField.setEnabled(true);
        m_workingDirectoryField.setText(directoryManager.getEnvironmentPath());

        // Update m_filesTableModel
        File existingWorkingDirectory = new File(directoryManager.getEnvironmentPath());
        File[] files = existingWorkingDirectory.listFiles(File::isFile);
        if (files != null) {
          for (File file : files) {
            String[] row = {file.getAbsolutePath()};
            m_filesTableModel.addRow(row);
          }
        }

      } else if (environment instanceof FilesEnvironmentManager) {
        FilesEnvironmentManager filesManager = (FilesEnvironmentManager) environment;

        m_archivedEnvironmentButton.setEnabled(false);
        m_filesEnvironmentButton.setSelected(true);

        // Update m_workingDirectoryField
        m_workingDirectoryField.setEnabled(false);

        // Update m_filesTableModel
        String[] files = filesManager.getFiles();
        if (files != null) {
          for (String file : files) {
            String[] row = {file};
            m_filesTableModel.addRow(row);
          }
        }
      }
    }
  }

  /** Loads settings from saved settings. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {

    m_config.loadSettingsForDialog(settings, specs[0]);

    ModelType modelType;
    try {
      modelType = ModelType.valueOf(m_config.getModelType());
    } catch (Exception err) {
      modelType = ModelType.genericModel;
    }

    String readmeFile = settings.getString(README_FILE, "");

    updateDialog(modelType, readmeFile, m_config.getEnvironmentManager());
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    FskPortObject inputObject = (FskPortObject) input[0];
    try {
      m_config.loadSettings(settings);
    } catch (InvalidSettingsException e) {
      throw new NotConfigurableException(e.getMessage(), e);
    }

    ModelType modelType;
    try {
      if (StringUtils.isNotEmpty(m_config.getModelType())) {
        modelType = ModelType.valueOf(m_config.getModelType());
      } else if (inputObject.modelMetadata != null
          && StringUtils.isNotEmpty(inputObject.modelMetadata.getModelType())) {
        modelType = ModelType.valueOf(inputObject.modelMetadata.getModelType());
      } else {
        modelType = ModelType.genericModel;
      }
    } catch (IllegalArgumentException err) {
      modelType = ModelType.genericModel;
    }

    String readmeFile = settings.getString(README_FILE, "");

    // Environment. First load from settings and if empty try to get from input model.
    EnvironmentManager environment;
    if (m_config.getEnvironmentManager() != null) {
      environment = m_config.getEnvironmentManager();
    } else if (inputObject.getEnvironmentManager().isPresent()) {
      environment = inputObject.getEnvironmentManager().get();
    } else {
      environment = null;
    }

    updateDialog(modelType, readmeFile, environment);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    m_readmePanel.addToHistory();

    String modelType = ((ModelType) modelTypeComboBoxModel.getSelectedItem()).name();

    // If the model class has changed, then discard the metadata for now
    // TODO: the metadata must be converted between schemas
    if (!modelType.equals(m_config.getModelType())) {
      ModelType modelTypeEnum = ModelType.valueOf(modelType);
      Model metadata = NodeUtils.initializeModel(modelTypeEnum);
      try {
        String jsonMetadata = MAPPER.writeValueAsString(metadata);
        m_config.setModelMetaData(jsonMetadata);
      } catch (JsonProcessingException e) {
      }
    }

    m_config.setModelType(modelType);

    m_readmeFile = m_readmePanel.getSelectedFile().trim();
    settings.addString(README_FILE, m_readmeFile);

    // environment
    if (m_archivedEnvironmentButton.isSelected()) {
      // Take archive path
      String archivePath = m_workingDirectoryField.getText();
      // Take entries
      String[] entries = new String[m_filesTableModel.getRowCount()];
      for (int row = 0; row < m_filesTableModel.getRowCount(); row++) {
        entries[row] = (String) m_filesTableModel.getValueAt(row, 0);
      }
      // Create and set environment
      m_config.setEnvironmentManager(new ArchivedEnvironmentManager(archivePath, entries));
    } else if (m_directoryEnvironmentButton.isSelected()) {
      // Take directory path
      String directoryPath = m_workingDirectoryField.getText();
      // Create and set environment
      m_config.setEnvironmentManager(new ExistingEnvironmentManager(directoryPath));
    } else if (m_filesEnvironmentButton.isSelected()) {
      // Take entries
      String[] entries = new String[m_filesTableModel.getRowCount()];
      for (int row = 0; row < m_filesTableModel.getRowCount(); row++) {
        entries[row] = (String) m_filesTableModel.getValueAt(row, 0);
      }
      // Create and set environment
      m_config.setEnvironmentManager(new FilesEnvironmentManager(entries));
    }

    m_config.saveSettings(settings);
  }

  private void createUI() {

    // Model type panel
    final JPanel modelTypePanel = new JPanel(new BorderLayout());
    modelTypePanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Model Type:"));

    JComboBox<ModelType> combo = new JComboBox<>(modelTypeComboBoxModel);
    modelTypePanel.add(combo, BorderLayout.NORTH);

    modelTypePanel.add(Box.createHorizontalGlue());

    // Readme panel
    final JPanel readmePanel = new JPanel(new BorderLayout());
    readmePanel
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Readme:"));
    readmePanel.add(m_readmePanel, BorderLayout.NORTH);
    readmePanel.add(Box.createHorizontalGlue());

    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(modelTypePanel);
    container.add(readmePanel);

    addTab("Options", container);

    // Environment tab

    // Working directory panel
    JButton workingDirectoryButton = new JButton("Browse");
    workingDirectoryButton.addActionListener(new WorkingDirectoryButtonListener());

    JPanel workingDirectoryPanel = new JPanel();
    workingDirectoryPanel.add(new JLabel("Working directory:"));
    workingDirectoryPanel.add(m_workingDirectoryField);
    workingDirectoryPanel.add(workingDirectoryButton);

    // Group the radio buttons.
    ButtonGroup group = new ButtonGroup();
    group.add(m_archivedEnvironmentButton);
    group.add(m_directoryEnvironmentButton);
    group.add(m_filesEnvironmentButton);

    // Register a listener for the radio buttons.
    m_archivedEnvironmentButton.addActionListener(new ArchivedEnvironmentButtonListener());
    m_directoryEnvironmentButton.addActionListener(new DirectoryEnvironmentButtonListener());
    m_filesEnvironmentButton.addActionListener(new FilesEnvironmentButtonListener());

    // Put the radio buttons in a row
    JPanel radioPanel = new JPanel();
    radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));

    radioPanel.add(m_archivedEnvironmentButton);
    radioPanel.add(m_directoryEnvironmentButton);
    radioPanel.add(m_filesEnvironmentButton);

    // Files table: List files in directory
    JTable table = new JTable(m_filesTableModel);

    JPanel environmentContainer = new JPanel();
    environmentContainer.setLayout(new BoxLayout(environmentContainer, BoxLayout.Y_AXIS));
    environmentContainer.add(workingDirectoryPanel);
    environmentContainer.add(radioPanel);
    environmentContainer.add(table);

    addTab("Environment", environmentContainer);
  }

  private class ArchivedEnvironmentButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      clearEnvironmentPanel();
      m_workingDirectoryField.setEnabled(false);
    }
  }

  private class DirectoryEnvironmentButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      m_archivedEnvironmentButton.setEnabled(false);
      clearEnvironmentPanel();
      m_workingDirectoryField.setEnabled(true);
    }
  }

  private class FilesEnvironmentButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      m_archivedEnvironmentButton.setEnabled(false);
      clearEnvironmentPanel();
      m_workingDirectoryField.setEnabled(true);
    }
  }

  private class WorkingDirectoryButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

      if (m_directoryEnvironmentButton.isSelected()) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

          // Update m_workingDirectoryField
          File selectedFile = fileChooser.getSelectedFile();
          m_workingDirectoryField.setText(selectedFile.getAbsolutePath());

          // Update table model
          m_filesTableModel.setRowCount(0);
          File[] files = selectedFile.listFiles(File::isFile);
          if (files != null) {
            for (File file : files) {
              String[] row = {file.getAbsolutePath()};
              m_filesTableModel.addRow(row);
            }
          }
        }
      } else if (m_filesEnvironmentButton.isSelected()) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        // Update table model
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          m_filesTableModel.setRowCount(0);
          for (File file : fileChooser.getSelectedFiles()) {
            String[] row = {file.getAbsolutePath()};
            m_filesTableModel.addRow(row);
          }
        }
      }
    }
  }

  private void clearEnvironmentPanel() {
    m_workingDirectoryField.setText("");
    m_filesTableModel.setRowCount(0); // Clear table
  }
}
