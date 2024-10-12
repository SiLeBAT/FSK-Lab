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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.VariableType;
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
  protected volatile FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus m_status = new FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus();
  private String[] additionalDependencies;
  private Conda conda;
  private String proposedEnvName;
  private String[] languages = { "Python 2", "Python 3", "R 3", "R 4" };
  private JComboBox languageComboBox = new JComboBox<>(languages);  private SettingsModelString condaEnvName;

  public FSKEnvironmentCreatorNodeDialog() {
      condaEnvName = new SettingsModelString(FSKEnvironmentCreatorNodeModel.CFG_FILE, "");

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

              

              proposedEnvName = selectedEnv;

          }
      });
      GridBagConstraints comboBoxGbc = new GridBagConstraints();
      comboBoxGbc.anchor = GridBagConstraints.CENTER;
      comboBoxGbc.gridx = 0;
      comboBoxGbc.gridy = 0;
      comboBoxPanel.add(environmentsComboBox, comboBoxGbc);
      panel.add(comboBoxPanel, gbc);

      
      // 2. Add a separator with "OR" label and a text field on the same line
      gbc.gridy++;  // Move to the next row
      gbc.gridwidth = 3;  // Span across all columns for the separator
      JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
      gbc.fill = GridBagConstraints.HORIZONTAL;  // Make separator span the entire width
      panel.add(separator, gbc);  // Add separator to the panel

      // 2. Add the "Or Create an Environment For" label and the text field on the next line
      gbc.gridy++;  // Move to the next row
      gbc.gridwidth = 1;  // Reset the grid width for the label and text field
      gbc.fill = GridBagConstraints.NONE;  // Reset fill to default

      orLabel = new JLabel("Or Create an Environment For");
      gbc.gridx = 0;  // First column for the label
      gbc.insets = new Insets(5, 0, 5, 5);  // Add some padding
      panel.add(orLabel, gbc);  // Add the label to the panel

      gbc.gridx = 1;  // Move to the second column for the text field
      gbc.gridwidth = 2;  // Span the text field across two columns if needed
      gbc.fill = GridBagConstraints.HORIZONTAL;  // Make the text field expandable
      languageComboBox.setSelectedItem("Python 3");
      panel.add(languageComboBox, gbc);  // Add the text field to the panel

      // Step 3: Add the label and text field for "Environment Name"// 1. Add the label and text field on the same line
      gbc.gridy++;  // Move to the next row
      gbc.gridwidth = 1;  // Ensure the grid width is set to 1 for individual components
      gbc.gridx = 0;  // Set the x position to the first column
      gbc.insets = new Insets(5, 0, 5, 5);  // Add some padding around the label

      JLabel envNameLabel = new JLabel("Environment Name:");
      panel.add(envNameLabel, gbc);  // Add the label to the panel

      gbc.gridx = 1;  // Move to the second column for the text field
      gbc.fill = GridBagConstraints.HORIZONTAL;  // Make the text field expandable horizontally
      envNameTextField = new JTextField(20);  // Text field for environment name input
      panel.add(envNameTextField, gbc);  // Add the text field to the panel

      
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
        String environmentName = envNameTextField.getText();
        proposedEnvName = environmentName;
        String languageWrittenIn = (String) languageComboBox.getSelectedItem();
        
        // Assuming additionalDependencies and tableModel are accessible in this scope
        EnvironmentManager.createEnvironment(environmentName, languageWrittenIn, additionalDependencies, tableModel, panel, FSKEnvironmentCreatorNodeDialog.this, m_status);
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
  
  

  protected void updateStatusMessage(final ChangeEvent e) {
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

  protected void updateProgress(final ChangeEvent e) {
    final int progress = m_status.getProgress().getIntValue();
    logTextArea.append("Progress: " + progress + "%\n");
    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
  }

  protected void updateErrorLog(final ChangeEvent e) {
    logTextArea.append("Error: " + m_status.getErrorLog().getStringValue() + "\n");
    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
          throws NotConfigurableException {
      try {
        condaEnvName.loadSettingsFrom(settings);
      } catch (InvalidSettingsException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      Map<String, FlowVariable> flowVars = getAvailableFlowVariables(new VariableType [] {VariableType.StringType.INSTANCE});

      flowVars.forEach((key, value) -> {
          if (key.equals("packages") && firstLoad) {
              addNewPackage(EnvironmentManager.convertCommaSeparatedStringToArray(value.getStringValue()));
              additionalDependencies = EnvironmentManager.getPackages(value.getStringValue());
              firstLoad = false;
          } else if (key.equals("LanguageWrittenIn")) {
              String language = value.getStringValue();
              String closestLanguage = Arrays.stream(languages)
                  .min((s1, s2) -> Integer.compare(getLevenshteinDistance(s1, language), getLevenshteinDistance(s2, language)))
                  .orElse(languages[0]);
              languageComboBox.setSelectedItem(closestLanguage);
          }
      });
  }
  
  //Utility method to calculate the Levenshtein distance between two strings
  private int getLevenshteinDistance(String s1, String s2) {
      int[][] dp = new int[s1.length() + 1][s2.length() + 1];

      for (int i = 0; i <= s1.length(); i++) {
          for (int j = 0; j <= s2.length(); j++) {
              if (i == 0) {
                  dp[i][j] = j;
              } else if (j == 0) {
                  dp[i][j] = i;
              } else {
                  dp[i][j] = Math.min(
                          dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                          Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                  );
              }
          }
      }

      return dp[s1.length()][s2.length()];
  }
  
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    condaEnvName.setStringValue(proposedEnvName);
    condaEnvName.saveSettingsTo(settings);
  }

  public void addNewPackage(String[][] packageData) {
      for (int i = 0; i < packageData.length; i++) {
          tableModel.addRow(packageData[i]);
      }
  }
}