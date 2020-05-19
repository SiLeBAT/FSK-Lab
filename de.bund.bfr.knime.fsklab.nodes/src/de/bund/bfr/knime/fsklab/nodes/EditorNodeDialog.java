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
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.EditorNodeSettings.ModelType;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FBrowseButton;

class EditorNodeDialog extends DataAwareNodeDialogPane {

  private EditorNodeSettings settings;

  private final DefaultComboBoxModel<ModelType> modelType;

  private final FilesHistoryPanel m_readmePanel;

  private final FilesHistoryPanel m_workingDirectoryPanel;

  private FileTableModel fileModel = new FileTableModel();

  private JTable fileTable = new JTable(fileModel);

  private File currentWorkingDirectory;

  private WorkingDirectoryChangeListener changeListener = new WorkingDirectoryChangeListener();
  
  public EditorNodeDialog() {
    settings = new EditorNodeSettings();

    modelType = new DefaultComboBoxModel<>(ModelType.values());

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

  /** Loads settings from saved settings. */

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    try {
      this.settings.load(settings);
      
      // Select the model type in settings
      if (StringUtils.isNotEmpty(this.settings.modelType)) {
        modelType.setSelectedItem(ModelType.valueOf(this.settings.modelType));
      }
      
      m_readmePanel.setSelectedFile(this.settings.getReadme());
      m_workingDirectoryPanel.setSelectedFile(this.settings.getWorkingDirectory());

      if (!m_workingDirectoryPanel.getSelectedFile().isEmpty()) {
        try {
          URL url = FileUtil.toURL(m_workingDirectoryPanel.getSelectedFile());
          Path localPath = FileUtil.resolveToPath(url);
          if (localPath != null) {

            // Clear and load file names from directory localPath
            fileModel.filenames.clear();
            Files.walk(localPath).filter(Files::isRegularFile).map(Path::toString)
                .forEach(fileModel.filenames::add);

            fileTable.revalidate();
            currentWorkingDirectory = new File(this.settings.getWorkingDirectory());
          }

        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  /** Loads settings from input port. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    final EditorNodeSettings editorSettings = new EditorNodeSettings();
    try {
      editorSettings.load(settings);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException("InvalidSettingsException", exception);
    }

    final FskPortObject inObj = (FskPortObject) input[0];

    /*
     * If input model has not changed (the original scripts stored in settings match the input
     * model).
     */
    if (Objects.equals(editorSettings.getWorkingDirectory(), inObj.getWorkingDirectory())) {
      // Updates settings
      this.settings = editorSettings;
    } else {
      // Discard settings and replace them with input model
      this.settings.setReadme(inObj.getReadme());
      this.settings.setWorkingDirectory(inObj.getWorkingDirectory());
    }

    if (!inObj.getReadme().isEmpty()) {
      m_readmePanel.getParent().setVisible(false);
    }

    m_readmePanel.updateHistory();
    m_readmePanel.setSelectedFile(this.settings.getReadme());
    m_workingDirectoryPanel.removeChangeListener(changeListener);
    m_workingDirectoryPanel.updateHistory();
    m_workingDirectoryPanel.setSelectedFile(this.settings.getWorkingDirectory());
    m_workingDirectoryPanel.addChangeListener(changeListener);

    try {
      URL url = FileUtil.toURL(m_workingDirectoryPanel.getSelectedFile());
      Path localPath = FileUtil.resolveToPath(url);

      // Clear and load file names from directory localPath
      fileModel.filenames.clear();
      Files.walk(localPath).filter(Files::isRegularFile).map(Path::toString)
          .forEach(fileModel.filenames::add);

      fileTable.revalidate();
      currentWorkingDirectory = new File(this.settings.getWorkingDirectory());
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
    
    this.settings.modelType = ((ModelType) modelType.getSelectedItem()).name();
    this.settings = editorSettings;
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    this.settings.setReadme(m_readmePanel.getSelectedFile().trim());
    m_readmePanel.addToHistory();

    this.settings.setWorkingDirectory(m_workingDirectoryPanel.getSelectedFile().trim());
    m_workingDirectoryPanel.addToHistory();

    this.settings.setResources(String.join(";", fileModel.filenames));

    this.settings.save(settings);
  }

  private void createUI() {

    final NodeContext nodeContext = NodeContext.getContext();
    final WorkflowContext workflowContext = nodeContext.getWorkflowManager().getContext();

    // Model type panel
    final JPanel modelTypePanel = new JPanel(new BorderLayout());
    modelTypePanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Model Type:"));

    JComboBox<ModelType> combo = new JComboBox<>(modelType);
    combo.addItemListener(event -> {
      if (event.getStateChange() == ItemEvent.SELECTED) {
        settings.modelType = ((ModelType) event.getItem()).name();
      }
    });
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

          fileModel.filenames.addAll(
              Arrays.stream(files).map(p -> p.toPath().toString()).collect(Collectors.toList()));

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

  class FileTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 7828275630230509962L;

    protected List<String> filenames;

    private final String[] columnNames =
        {"Name", "Size", "Last modified", "Readable?", "Writable?"};

    // This table model works for any one given directory
    public FileTableModel() {
      filenames = new ArrayList<>();
    }

    @Override
    public int getColumnCount() {
      return 5;
    }

    @Override
    public int getRowCount() {
      return filenames.size();
    } // # of files in dir

    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }

    // The method that must actually return the value of each cell.
    @Override
    public Object getValueAt(int row, int col) {
      File f = new File(filenames.get(row));
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
          fileModel.filenames.clear();
          for (File file : newDirectory.listFiles()) {
            fileModel.filenames.add(file.getAbsolutePath());
          }
          
          // Update currentWorkingDirectory
          currentWorkingDirectory = newDirectory;

        } catch (InvalidPathException | IOException err) {
          err.printStackTrace();
        }
      }
    }
  }
}
