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
package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.conda.Conda;
import org.knime.conda.CondaPackageSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.util.Version;

class FSKEnvironmentCreatorNodeDialog extends NodeDialogPane {

  private final JPanel m_panel = new JPanel(new CardLayout());
  private List<String> m_environmentsList;
  private boolean firstLoad = true;  // A flag to track if it's the first time loading settings
  private List<String[]> m_packagesList;  // Each String[] will hold package name and version.
  private DefaultTableModel tableModel;
  private JLabel orLabel;
  private JTextArea logTextArea;  // JTextArea for logs
  private JScrollPane logScrollPane;
  private JTextField envNameTextField;  // To get environment name input from the user
  private volatile FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus m_status = new FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus();
  private String languageWrittenIn;
  private String[] additionalDependencies;
  private Conda conda;
  private String proposedEnvName;
  public FSKEnvironmentCreatorNodeDialog() {
      try {
          conda = new Conda();
          m_environmentsList = conda.getEnvironmentNames();
      } catch (IOException e) {
          e.printStackTrace();
      }

      m_packagesList = new ArrayList<>(); 
      final JPanel panel = new JPanel(new GridBagLayout());
      final GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.insets = new Insets(5, 5, 5, 5);

      // Step 1: Center the JComboBox
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      JPanel comboBoxPanel = new JPanel(new GridBagLayout());
      JComboBox<String> environmentsComboBox = new JComboBox<>();
      environmentsComboBox.addItem("NEW Environment (Proposed)");  // Add the default item for creating a new environment
      for (String env : m_environmentsList) {
          environmentsComboBox.addItem(env);
      }
      // Add an ActionListener to handle item selection
      // Add the ActionListener to the JComboBox to update the table model when a new environment is selected
      

      environmentsComboBox.addActionListener(e -> {
          // Get the selected environment from the combo box
          String selectedEnv = (String) environmentsComboBox.getSelectedItem();
          if(selectedEnv.equals("NEW Environment (Proposed)")) {
            tableModel.setRowCount(0);
              for (int i = 0; i < additionalDependencies.length; i++) {
                tableModel.addRow(new String[] {additionalDependencies[i],"","",""});
              }
          }else if (selectedEnv != null) {
              // Fetch the package list for the selected environment
              List<CondaPackageSpec> packages;
              try {
                packages = conda.getPackages(selectedEnv);
                // Clear the current content of the table model
                tableModel.setRowCount(0);  // This clears all rows from the model

                // Loop through the packages and add them to the table model
                for (CondaPackageSpec packageSpec : packages) {
                    // Convert CondaPackageSpec to an array of Strings (columns for the table)
                    String[] packageRow = {
                        packageSpec.getName(),
                        packageSpec.getVersion(),
                        packageSpec.getBuild(),
                        packageSpec.getChannel()
                    };
                    tableModel.addRow(packageRow);  // Add the new row to the table model
                }
              } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
              }

              

              // Optionally, force the table to redraw
              // table.revalidate(); // Refreshes the table's structure
              // table.repaint();    // Forces a repaint of the table
          }
      });
      GridBagConstraints comboBoxGbc = new GridBagConstraints();
      comboBoxGbc.anchor = GridBagConstraints.CENTER;
      comboBoxGbc.gridx = 0;
      comboBoxGbc.gridy = 0;
      comboBoxPanel.add(environmentsComboBox, comboBoxGbc);
      panel.add(comboBoxPanel, gbc);

      
      // 2. Add a separator with "OR" label
      gbc.gridy++;
      gbc.gridwidth = 2;
      JPanel separatorPanel = new JPanel(new GridBagLayout());
      JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
      orLabel = new JLabel("OR CREATE A NEW ENVIRONMENT");
      
      GridBagConstraints separatorGbc = new GridBagConstraints();
      separatorGbc.fill = GridBagConstraints.HORIZONTAL;
      separatorGbc.gridx = 0;
      separatorGbc.weightx = 1;
      separatorPanel.add(separator, separatorGbc);  // Add the separator

      separatorGbc.gridx = 1;
      separatorGbc.weightx = 0;
      separatorGbc.insets = new Insets(0, 5, 0, 5);  // Add padding around the label
      separatorPanel.add(orLabel, separatorGbc);  // Add the "OR" label
      
      separatorGbc.gridx = 2;
      separatorGbc.weightx = 1;
      separatorPanel.add(new JSeparator(SwingConstants.HORIZONTAL), separatorGbc);  // Another separator after the label

      panel.add(separatorPanel, gbc);
      // Step 3: Add the label and text field for "Environment Name"
      gbc.gridy++;
      gbc.gridwidth = 1;
      JLabel envNameLabel = new JLabel("Name:");
      panel.add(envNameLabel, gbc);

      gbc.gridx = 1;  // Positioning next to the label
      envNameTextField = new JTextField(20);  // Text field for environment name input
      panel.add(envNameTextField, gbc);
      
      // Step 4: Add the packages table (name, version)
      gbc.gridx = 0;
      gbc.gridy++;
      gbc.gridwidth = 2;
      gbc.weighty = 1;
      String[] columnNames = {"Package Name", "Version", "Channel", "Build"};
      tableModel = new DefaultTableModel(columnNames, 0);
      for (String[] packageRow : m_packagesList) {
          tableModel.addRow(packageRow);
      }

      JTable packagesTable = new JTable(tableModel);
      panel.add(new JScrollPane(packagesTable), gbc);

      // Step 4: Add the button to create the environment
      gbc.gridy++;
      gbc.weighty = 0;
      gbc.gridwidth = 2;
      JButton createEnvButton = new JButton("Create Environment");
      panel.add(createEnvButton, gbc);

      // Step 5: Add an ActionListener to the button

      createEnvButton.addActionListener(e -> {
          File tempYamlFile = null;
          try {
              // Choose the YAML content dynamically based on user input (or some other condition)
              StringBuilder yamlContent = new StringBuilder();
              CondaEnvVersion condaVersion = CondaEnvVersion.R4;
              String environmentName = envNameTextField.getText();
              int majorVersion = 4;
              if (languageWrittenIn.toLowerCase().startsWith("python 2")) {
                  yamlContent.append(EnvironmentYAML.getPython2EnvContent(environmentName));
                  condaVersion = CondaEnvVersion.PYTHON2;
                  majorVersion = 2;
              } else if (languageWrittenIn.toLowerCase().startsWith("python 3")) {
                  yamlContent.append(EnvironmentYAML.getPython3EnvContent(environmentName));
                  condaVersion = CondaEnvVersion.PYTHON3;
                  majorVersion = 3;
              } else if (languageWrittenIn.toLowerCase().startsWith("r 3")) {
                  yamlContent.append(EnvironmentYAML.getR3EnvContent(environmentName));
                  condaVersion = CondaEnvVersion.R3;
                  majorVersion = 3;
              } else if (languageWrittenIn.toLowerCase().startsWith("r 4")) {
                  yamlContent.append(EnvironmentYAML.getR4EnvContent(environmentName));
                  condaVersion = CondaEnvVersion.R4;
                  majorVersion = 4;
              }
              
              if (additionalDependencies != null && additionalDependencies.length > 0) {
                  for (String dependency : additionalDependencies) {
                    if(!StringUtils.isBlank(dependency)) {
                      if(languageWrittenIn.toLowerCase().startsWith("r "))
                        dependency = "r-"+dependency;
                        yamlContent.append("  - ").append(dependency).append("\n");
                    }
                  }
              }
              
              // Create a temporary YAML file for the selected content
              tempYamlFile = File.createTempFile("conda_env_", ".yaml");
  
              // Write the YAML content to the temporary file
              try (FileWriter writer = new FileWriter(tempYamlFile)) {
                  writer.write(yamlContent.toString());
              }
  
              // Collect packages from the table model
              List<CondaPackageSpec> packages = new ArrayList<>();
              for (int i = 0; i < tableModel.getRowCount(); i++) {
                  String packageName = (String) tableModel.getValueAt(i, 0);
                  String packageVersion = (String) tableModel.getValueAt(i, 1);
                  String packageChannel = (String) tableModel.getValueAt(i, 2);
                  String packageBuild = (String) tableModel.getValueAt(i, 3);
                  
                  CondaPackageSpec packageSpec = new CondaPackageSpec(packageName, packageVersion, packageBuild, packageChannel);
                  packages.add(packageSpec);
              }
  
              
              registerExternalHooks();
  
              // Use the temporary YAML file path for environment creation
              FSKCondaEnvironmentCreationObserver obs = new FSKCondaEnvironmentCreationObserver(condaVersion); 
              proposedEnvName = obs.getDefaultEnvironmentName(environmentName);
              obs.startEnvironmentCreation(proposedEnvName, tempYamlFile.getAbsolutePath(),
                  new Version(majorVersion, 0, 0), m_status);
  
              
          } catch (IOException ex) {
              JOptionPane.showMessageDialog(panel, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              ex.printStackTrace();
          }
      });

      
      // Initialize the log text area
      logTextArea = new JTextArea(10, 50);  // 10 rows, 50 columns
      logTextArea.setEditable(false);  // Make the log area non-editable
      logScrollPane = new JScrollPane(logTextArea);  // Add the text area to a scroll pane
      
      gbc.gridx = 0;
      gbc.gridy++;
      gbc.gridwidth = 2;
      gbc.weighty = 1;
      panel.add(new JLabel("Log Output:"), gbc);  // Add a label for the log area
      
      gbc.gridy++;
      gbc.weighty = 1;  // Allow the log area to grow vertically
      panel.add(logScrollPane, gbc);  // Add the log scroll pane to the panel

      
      m_panel.add(panel);
      addTab("Options", m_panel, false);
  }

  private void registerExternalHooks() {
    m_status.getStatusMessage().addChangeListener(this::updateStatusMessage);
    m_status.getProgress().addChangeListener(this::updateProgress);
    m_status.getErrorLog().addChangeListener(this::updateErrorLog);
  }
  

  private void updateStatusMessage(final ChangeEvent e) {
    logTextArea.append("Status: " + m_status.getStatusMessage().getStringValue() + "\n");
    if(m_status.getStatusMessage().getStringValue().startsWith("Environment creation finished.")) {
      // Regular expression pattern to match content between single quotes
      Pattern pattern = Pattern.compile("'(.*?)'");
      Matcher matcher = pattern.matcher(m_status.getStatusMessage().getStringValue());

      // Extract and print the content between the quotes
      if (matcher.find()) {
          String extractedText = matcher.group(1);  // Group 1 contains the content between the quotes
          System.out.println("Extracted text: " + extractedText);
      }
    }
      
    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());  // Auto-scroll to the bottom
  }

  private void updateProgress(final ChangeEvent e) {
    final int progress = m_status.getProgress().getIntValue();
    logTextArea.append("Progress: " + progress + "%\n");
    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
  }

  private void updateErrorLog(final ChangeEvent e) {
    logTextArea.append("Error: " + m_status.getErrorLog().getStringValue() + "\n");
    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
          throws NotConfigurableException {
      Map<String, FlowVariable> flowVars = getAvailableFlowVariables();
      flowVars.forEach((key, value) -> {
          if (key.equals("packages") && firstLoad) {
              addNewPackage(convertCommaSeparatedStringToArray(value.getStringValue()));
              additionalDependencies = getPackages(value.getStringValue());
              firstLoad = false;
          } else if (key.equals("LanguageWrittenIn")) {
              languageWrittenIn = value.getStringValue();
              orLabel.setText("OR CREATE A NEW ENVIRONMENT FOR (" + languageWrittenIn + ")");
          }
      });
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
      // Save settings code (if necessary)
  }

  public String[][] convertCommaSeparatedStringToArray(String input) {
      String[] tokens = input.split("\\s*,\\s*");
      String[][] result = new String[tokens.length][2];
      for (int i = 0; i < tokens.length; i++) {
          result[i][0] = tokens[i];
          result[i][1] = "";
      }
      return result;
  }
  public String[] getPackages(String input) {
    String[] tokens = input.split("\\s*,\\s*");
    String[] result = new String[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
        result[i] = tokens[i];
    }
    return result;
}

  public void addNewPackage(String[][] packageData) {
      for (int i = 0; i < packageData.length; i++) {
          tableModel.addRow(packageData[i]);
      }
  }
}