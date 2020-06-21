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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FBrowseButton;

class FSKEditorJSNodeDialog extends DataAwareNodeDialogPane {

  private static final String MODEL_TYPE = "modelType";
  private static final String README_FILE = "readmeFile";

  private ModelType m_modelType;
  
  /** Absolute path to readme file. */
  private String m_readmeFile;
  
  private final FSKEditorJSConfig m_config;

  private final DefaultComboBoxModel<ModelType> modelTypeComboBoxModel;
  private final FilesHistoryPanel m_readmePanel;
  private final FilesHistoryPanel m_workingDirectoryPanel;
  private FileTableModel fileModel = new FileTableModel();
  private JTable fileTable = new JTable(fileModel);
  private File currentWorkingDirectory;

  private WorkingDirectoryChangeListener changeListener = new WorkingDirectoryChangeListener();

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

    FlowVariableModel directoryVariable =
        createFlowVariableModel("workingDirectory", FlowVariable.Type.STRING);
    m_workingDirectoryPanel =
        new FilesHistoryPanel(directoryVariable, "directory", LocationValidation.None);
    m_workingDirectoryPanel.getComponent(0).setEnabled(false);

    createUI();
  }

  /** @return {@link ModelType#genericModel} if not set. */
  public ModelType getModelType() {
    return m_modelType != null ? m_modelType : ModelType.genericModel;
  }

  public void setModelType(ModelType modelType) {
    this.m_modelType = modelType;
  }
  
  /**
   * Update the dialog with the passed data.
   * 
   * @param modelType Non null model type.
   * @param readmeFile Empty string if not set.
   * @param workingDirectory Empty string if not set.
   */
  private void updateDialog(ModelType modelType, String readmeFile, String workingDirectory) {

    modelTypeComboBoxModel.setSelectedItem(modelType);

    m_readmePanel.setSelectedFile(!readmeFile.isEmpty() ? readmeFile : "");

    if (!workingDirectory.isEmpty()) {

      try {
        m_workingDirectoryPanel.setSelectedFile(workingDirectory);

        // Populate resources table with all the files in the working directory
        URL url = FileUtil.toURL(m_workingDirectoryPanel.getSelectedFile());
        Path localPath = FileUtil.resolveToPath(url);

        if (localPath != null) {
          // Clear and local file names from directory localPath
          fileModel.filenames = Files.walk(localPath).filter(Files::isRegularFile)
              .map(Path::toString).toArray(String[]::new);
          fileTable.revalidate();
          currentWorkingDirectory = new File(workingDirectory);
        }
      } catch (Exception err) {
        m_workingDirectoryPanel.setSelectedFile("");
        fileModel.filenames = new String[0];
        currentWorkingDirectory = null;
      }
    } else {
      m_workingDirectoryPanel.setSelectedFile("");
    }
  }

  /** Loads settings from saved settings. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    
    m_config.loadSettingsForDialog(settings, specs[0]);

    ModelType modelType = ModelType.valueOf(settings.getString(MODEL_TYPE, "genericModel"));
    String readmeFile = settings.getString(README_FILE, "");
    String workingDirectory = m_config.getWorkingDirectory();
    
    updateDialog(modelType, readmeFile, workingDirectory);
  }

//  /** Loads settings from input port. */
//  @Override
//  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
//      throws NotConfigurableException {
//
//    FSKEditorJSViewRepresentation repr = new FSKEditorJSNodeModel().getViewRepresentation();
//    FskPortObject inputPort = (FskPortObject) input[0];
//
//    // If the node id stored in representation is the same as the one from the input port, then
//    // the settings described the same model connected to this editor
//    if (inputPort.id == repr.getConnectedNodeId()) {
//      // Keep settings
//      FSKEditorJSConfig editorSettings = new FSKEditorJSConfig();
//      editorSettings.load(settings);
//
//      // Fill dialog with settings
//      updateDialog(getModelType(), editorSettings.getReadmeFile(),
//          editorSettings.getWorkingDirectory());
//    }
//
//    // Editor has been connected to another model.
//    else {
//      ModelType modelType = ModelType.valueOf(inputPort.modelMetadata.getModelType());
//
//      // Readme cannot be assigned for now as the dialog uses a file (readme file)
//      // and the port object has a string (raw readme). In this case, an empty string is assigned
//      // and the full readme from the port object will be read in the node model.
//      String readme = "";
//
//      String workingDirectory = inputPort.getWorkingDirectory();
//      updateDialog(modelType, readme, workingDirectory);
//
//      // And update the connected node id in the representation
//      repr.setConnectedNodeId(inputPort.id);
//    }
//  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    m_readmePanel.addToHistory();
    m_workingDirectoryPanel.addToHistory();

    settings.addString(MODEL_TYPE, m_modelType.name());
    settings.addString(README_FILE, m_readmeFile);
    
    m_config.saveSettings(settings);
  }

  private void createUI() {

    final NodeContext nodeContext = NodeContext.getContext();
    final WorkflowContext workflowContext = nodeContext.getWorkflowManager().getContext();

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

    // Working directory panel
    final JPanel workingDirectoryPanel = new JPanel(new BorderLayout());
    workingDirectoryPanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Working directory:"));
    workingDirectoryPanel.add(m_workingDirectoryPanel, BorderLayout.NORTH);
    workingDirectoryPanel.add(Box.createHorizontalGlue());

    m_workingDirectoryPanel.addChangeListener(changeListener);

    FBrowseButton resourcesButton = new FBrowseButton("Browse");
    resourcesButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int response = fc.showOpenDialog(resourcesButton);
        if (response == JFileChooser.APPROVE_OPTION) {

          File[] files = fc.getSelectedFiles();

          // Get working directory
          if (StringUtils.isNotEmpty(m_workingDirectoryPanel.getSelectedFile())) {
            try {
              currentWorkingDirectory = FileUtil
                  .getFileFromURL(FileUtil.toURL(m_workingDirectoryPanel.getSelectedFile()));
            } catch (InvalidPathException | MalformedURLException err) {
              err.printStackTrace();
            }
          } else {
            currentWorkingDirectory = new File(workflowContext.getCurrentLocation(),
                nodeContext.getNodeContainer().getNameWithID().toString().replaceAll("\\W", "")
                    .replace(" ", "") + "_" + "workingDirectory"
                    + FSKEditorJSNodeModel.TEMP_DIR_UNIFIER.getAndIncrement());
            currentWorkingDirectory.mkdir();
            m_workingDirectoryPanel.setSelectedFile(currentWorkingDirectory.getAbsolutePath());
          }

          resourcesButton.setEnabled(false);
          new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws IOException {

              for (File oneFile : files) {
                File targetFile = new File(currentWorkingDirectory, oneFile.getName());
                FileUtils.copyFile(oneFile, targetFile);
              }

              return null;
            }

            protected void done() {
              resourcesButton.setEnabled(true);
            }

          }.execute();

          fileModel.filenames =
              Arrays.stream(files).map(p -> p.toPath().toString()).toArray(String[]::new);
          fileTable.revalidate();
        }
      }
    });

    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(modelTypePanel);
    container.add(readmePanel);
    container.add(workingDirectoryPanel);

    JPanel fileSelector = new JPanel(new BorderLayout());

    TitledBorder border = new TitledBorder("Select Resource(s) - to move to the working directory");
    border.setTitleJustification(TitledBorder.CENTER);
    border.setTitlePosition(TitledBorder.TOP);

    fileSelector.add(resourcesButton, BorderLayout.NORTH);
    fileSelector.add(new JScrollPane(fileTable), BorderLayout.CENTER);
    fileSelector.setBorder(border);

    container.add(fileSelector);


    addTab("Options", container);
  }

  private class FileTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 7828275630230509962L;

    private String[] filenames;

    private final String[] columnNames =
        {"Name", "Size", "Last modified", "Readable?", "Writable?"};

    // This table model works for any one given directory
    public FileTableModel() {
      filenames = new String[0];
    }

    @Override
    public int getColumnCount() {
      return 5;
    }

    @Override
    public int getRowCount() {
      return filenames.length;
    } // # of files in dir

    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }

    // The method that must actually return the value of each cell.
    @Override
    public Object getValueAt(int row, int col) {
      File f = new File(filenames[row]);
      switch (col) {
        case 0:
          return f.getName();
        case 1:
          return Long.valueOf(f.length());
        case 2:
          return new Date(f.lastModified());
        case 3:
          return f.canRead();
        case 4:
          return f.canWrite();
        default:
          return null;
      }
    }
  }

  private class WorkingDirectoryChangeListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent e) {

      // Disable text field
      m_workingDirectoryPanel.getComponent(0).setEnabled(false);

      // If selected director is not null or empty
      if (StringUtils.isNotEmpty(m_workingDirectoryPanel.getSelectedFile())) {

        // Get selected directory
        String selectedDirectory = m_workingDirectoryPanel.getSelectedFile();
        try {
          File newDirectory = FileUtil.getFileFromURL(FileUtil.toURL(selectedDirectory));

          // Create new working directory
          if (!newDirectory.exists()) {
            newDirectory.mkdir();
          }

          // If old working directory is set, copy the old directory to the new one.
          if (!newDirectory.equals(currentWorkingDirectory)) {
            int choice = JOptionPane.showConfirmDialog(null, "Working directory is already set to "
                + currentWorkingDirectory + " \nAre you sure you want to change?");
            if (choice == JOptionPane.YES_OPTION) {
              FileUtils.copyDirectory(currentWorkingDirectory, newDirectory);
            }
          }

          // Update fileModel.filenames with the files in newDirectory
          fileModel.filenames = Arrays.stream(newDirectory.listFiles()).map(File::getAbsolutePath)
              .toArray(String[]::new);

          // Update currentWorkingDirectory
          currentWorkingDirectory = newDirectory;

        } catch (InvalidPathException | IOException err) {
          err.printStackTrace();
        }
      }
    }
  }
}
