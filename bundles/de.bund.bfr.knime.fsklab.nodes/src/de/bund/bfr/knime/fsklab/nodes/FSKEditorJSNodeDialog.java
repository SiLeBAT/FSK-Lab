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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FBrowseButton;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.common.ui.UIUtils;
import de.bund.bfr.swing.UI;

class FSKEditorJSNodeDialog extends NodeDialogPane {

  private final FSKEditorJSNodeSettings settings;


  private final JTextField readmeField;
  private final JTextField workingDirectoryField;
  private FileTableModel fileModel = new FileTableModel();
  JTable fileTable = new JTable(fileModel);
  JScrollPane centerPane;
  JPanel container;
  File currentWorkingDirectory;

  public FSKEditorJSNodeDialog() {
    settings = new FSKEditorJSNodeSettings();
    readmeField = new FTextField();
    workingDirectoryField = new FTextField();
    createUI();
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
      throws NotConfigurableException {
    try {
      this.settings.load(settings);


      readmeField.setText(this.settings.getReadme());
      workingDirectoryField.setText(this.settings.getWorkingDirectory());

      String fileHolder = this.settings.getResources();
      List<String> fileback = new LinkedList<String>(Arrays.asList(fileHolder.split(";", -1)));
      fileback.remove(0);
      if (fileback.size() > 0) {
        fileModel.filenames = fileback;
      }


    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    this.settings.setReadme(readmeField.getText());
    this.settings.setWorkingDirectory(workingDirectoryField.getText());
    String fileHolder = "";
    for (String oneFile : fileModel.filenames) {
      fileHolder = fileHolder + ";" + oneFile;
    }
    this.settings.setResources(fileHolder);

    this.settings.save(settings);
  }

  private void createUI() {

    final NodeContext nodeContext = NodeContext.getContext();
    final WorkflowManager wfm = nodeContext.getWorkflowManager();
    final WorkflowContext workflowContext = wfm.getContext();

    FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Plain text", "txt");

    // Build locale with the selected language in the preferences
    CreatorNodeBundle bundle = new CreatorNodeBundle(NodeUtils.getLocale());

    // buttons
    String buttonText = bundle.getBrowseButton();


    JButton readmeButton =
        UIUtils.createBrowseButton(buttonText, readmeField, JFileChooser.OPEN_DIALOG, txtFilter);

    FBrowseButton workingDirectoryButton = new FBrowseButton(buttonText);
    workingDirectoryButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        JFileChooser fc;
        try {
          File file = FileUtil.getFileFromURL(FileUtil.toURL(workingDirectoryField.getText()));
          fc = new JFileChooser(file);
        } catch (Exception ex) {
          fc = new JFileChooser();
        }

        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);

        int response = fc.showOpenDialog(workingDirectoryButton);
        if (response == JFileChooser.APPROVE_OPTION) {

          String selectedDirectory = fc.getSelectedFile().getAbsolutePath();
          if (currentWorkingDirectory == null) {
            workingDirectoryField.setText(selectedDirectory);
          } else {
            int choise = JOptionPane.showConfirmDialog(null, "Working directory is already set to "
                + currentWorkingDirectory + " \n Are you sure you want to change?");
            switch (choise) {
              case 0:
                try {

                  // moving the file to the new working directory
                  Files
                      .walk(NodeContext.getContext().getWorkflowManager().getContext()
                          .getCurrentLocation().toPath())
                      .filter(path -> path.toString()
                          .contains(nodeContext.getNodeContainer().getNameWithID().toString()
                              .replaceAll("\\W", "").replace(" ", "") + "_" + "workingDirectory"))
                      .sorted(Comparator.comparing(Path::toString).reversed()).map(Path::toFile).forEach(file -> {
                        System.out.println("looping over this File : "+file);
                        SwingWorker worker = new SwingWorker() {
                          @Override
                          protected Object doInBackground() throws Exception {

                            String toPath = selectedDirectory + File.separator + file.getName();
                            if(file.isFile()) {
                              copyFile(file, toPath);
                              
                            }else {
                              System.out.println(file);
                            }
                            return null;
                          }

                          @Override
                          protected void done() {
                            file.delete();
                            //try to delete parent folder
                            boolean b  = file.getParentFile().delete();
                          }

                        };

                        worker.execute();

                      });
                  workingDirectoryField.setText(selectedDirectory);

                
                } catch (IOException e1) {

                  e1.printStackTrace();
                }
                break;
              default:
                break;

            }
          }
        }
      }
    });



    FBrowseButton resourcesButton = new FBrowseButton(buttonText);
    resourcesButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        JFileChooser fc;

        fc = new JFileChooser();

        fc.setMultiSelectionEnabled(true);
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int response = fc.showOpenDialog(resourcesButton);
        if (response == JFileChooser.APPROVE_OPTION) {

          File[] files = fc.getSelectedFiles();

          if (workingDirectoryField.getText() != null
              && !workingDirectoryField.getText().equals("")) {
            currentWorkingDirectory = new File(workingDirectoryField.getText());
          } else {
            currentWorkingDirectory = new File(workflowContext.getCurrentLocation(),
                nodeContext.getNodeContainer().getNameWithID().toString().replaceAll("\\W", "")
                    .replace(" ", "") + "_" + "workingDirectory"
                    + FSKEditorJSNodeModel.TEMP_DIR_UNIFIER.getAndIncrement());
            currentWorkingDirectory.mkdir();
            workingDirectoryField.setText(currentWorkingDirectory.toString());

          }
          for (File oneFile : files) {
            SwingWorker worker = new SwingWorker() {
              @Override
              protected Object doInBackground() throws Exception {
                resourcesButton.setEnabled(false);
                String toPath = currentWorkingDirectory.toPath().toString() + File.separator
                    + oneFile.getName();
                copyFile(oneFile, toPath);
                return null;
              }

              @Override
              protected void done() {
                resourcesButton.setEnabled(true);
              }

            };

            worker.execute();



          }
          fileModel.filenames.addAll(
              Arrays.stream(files).map(p -> p.toPath().toString()).collect(Collectors.toList()));

          fileTable.revalidate();
        }
      }
    });


    // labels
    FLabel workingDirectoryLabel = new FLabel("Working directory");
    FLabel readmeLabel = new FLabel("Readme");

    // formPanel
    List<FLabel> labels = Arrays.asList(readmeLabel, workingDirectoryLabel);
    List<JTextField> fields = Arrays.asList(readmeField, workingDirectoryField);
    List<JButton> buttons = Arrays.asList(readmeButton, workingDirectoryButton);

    FPanel formPanel = UIUtils.createFormPanel(labels, fields, buttons);
    JPanel northPanel = UI.createNorthPanel(formPanel);
    northPanel.setBackground(UIUtils.WHITE);
    container = new JPanel(new BorderLayout());
    container.add(northPanel, BorderLayout.NORTH);
    JPanel fileSelector = new JPanel(new BorderLayout());

    TitledBorder border = new TitledBorder("Select Resource(s) - to move to the working directory");
    border.setTitleJustification(TitledBorder.CENTER);
    border.setTitlePosition(TitledBorder.TOP);

    centerPane = new JScrollPane(fileTable);
    fileSelector.add(resourcesButton, BorderLayout.NORTH);
    fileSelector.add(centerPane, BorderLayout.CENTER);

    fileSelector.setBorder(border);

    container.add(fileSelector, BorderLayout.CENTER);

    addTab("Options", container);
  }

  private void copyFile(File file, String toPath) {
    BufferedInputStream bis;
    BufferedOutputStream baos;
    try {
      bis = new BufferedInputStream(new FileInputStream(file));
      ProgressMonitorInputStream pmis =
          new ProgressMonitorInputStream(null, "Reading... " + file.getAbsolutePath(), bis);

      pmis.getProgressMonitor().setMillisToPopup(10);

      baos = new BufferedOutputStream(new FileOutputStream(toPath));

      byte[] buffer = new byte[2048];
      int nRead = 0;

      while ((nRead = pmis.read(buffer)) != -1) {
        baos.write(buffer, 0, nRead);
      }

      pmis.close();
      baos.flush();
      baos.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  class FileTableModel extends AbstractTableModel {
    protected List<String> filenames;

    protected String[] columnNames =
        new String[] {"name", "size", "last modified", "readable?", "writable?"};

    protected Class[] columnClasses =
        new Class[] {String.class, Long.class, Date.class, Boolean.class, Boolean.class};

    // This table model works for any one given directory
    public FileTableModel() {
      filenames = new ArrayList<String>();
    }

    public FileTableModel(List<String> files) {
      this.filenames = files; // Store a list of files in the directory
    }

    // These are easy methods.
    public int getColumnCount() {
      return 5;
    } // A constant for this model

    public int getRowCount() {
      return filenames.size();
    } // # of files in dir

    // Information about each column.
    public String getColumnName(int col) {
      return columnNames[col];
    }

    public Class getColumnClass(int col) {
      return columnClasses[col];
    }


    // The method that must actually return the value of each cell.
    public Object getValueAt(int row, int col) {
      File f = new File(filenames.get(row));
      switch (col) {
        case 0:
          return new File(filenames.get(row)).getName();
        case 1:
          return new Long(f.length());
        case 2:
          return new Date(f.lastModified());
        case 3:
          return f.canRead() ? Boolean.TRUE : Boolean.FALSE;
        case 4:
          return f.canWrite() ? Boolean.TRUE : Boolean.FALSE;
        default:
          return null;
      }
    }
  }

}
