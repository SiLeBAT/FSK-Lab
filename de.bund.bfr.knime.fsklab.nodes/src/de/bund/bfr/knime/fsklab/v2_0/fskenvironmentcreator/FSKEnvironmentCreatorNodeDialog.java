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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.conda.Conda;
import org.knime.conda.CondaPackageSpec;
import org.knime.core.node.ExecutionContext;
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
  private boolean firstLoad = true;
  private List<String[]> m_packagesList;
  private DefaultTableModel tableModel;
  private JTextArea logTextArea;
  private JScrollPane logScrollPane;
  private JTextField envNameTextField;
  protected volatile FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus m_status = new FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus();
  private String[] additionalDependencies;
  private Conda conda;
  private String proposedEnvName;
  private String[] languages = {"Python 2", "Python 3", "R 3", "R 4"};
  private JComboBox<String> versionComboBox = new JComboBox<>();
  private JComboBox<String> languageComboBox = new JComboBox<>(languages);
  private SettingsModelString condaEnvName;
  private Map<String, List<String>> cachedVersions = new HashMap<>();

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
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.anchor = GridBagConstraints.NORTHWEST;

      // Environment Selection ComboBox
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      JComboBox<String> environmentsComboBox = new JComboBox<>();
      environmentsComboBox.addItem("NEW Environment (Proposed)");
      for (String env : m_environmentsList) {
          environmentsComboBox.addItem(env);
      }
      environmentsComboBox.addActionListener(e -> {
          String selectedEnv = (String) environmentsComboBox.getSelectedItem();
          tableModel.setRowCount(0);

          if ("NEW Environment (Proposed)".equals(selectedEnv)) {
              for (String dep : additionalDependencies) {
                  tableModel.addRow(new String[]{dep, "", "", ""});
              }
          } else {
              try {
                  List<CondaPackageSpec> packages = conda.getPackages(selectedEnv);
                  for (CondaPackageSpec packageSpec : packages) {
                      tableModel.addRow(new String[]{
                              packageSpec.getName(),
                              packageSpec.getVersion(),
                              packageSpec.getBuild(),
                              packageSpec.getChannel()
                      });
                  }
              } catch (IOException e1) {
                  e1.printStackTrace();
              }
              proposedEnvName = selectedEnv;
          }
      });
      panel.add(environmentsComboBox, gbc);

      // "OR" separator
      gbc.gridy++;
      gbc.gridwidth = 2;
      JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
      panel.add(separator, gbc);

      // Language ComboBox
      gbc.gridy++;
      gbc.gridwidth = 1;
      panel.add(new JLabel("Language:"), gbc);

      gbc.gridx = 1;
      languageComboBox.setSelectedItem("Python 3");
      panel.add(languageComboBox, gbc);

      // Version ComboBox (initially hidden)
      gbc.gridy++;
      gbc.gridx = 0;
      panel.add(new JLabel("Version:"), gbc);

      gbc.gridx = 1;
      versionComboBox.setVisible(false);
      panel.add(versionComboBox, gbc);

      // Language selection listener to load specific versions
      languageComboBox.addActionListener(e -> {
          String selectedLanguage = (String) languageComboBox.getSelectedItem();
          versionComboBox.setVisible(true);
          if (selectedLanguage != null && selectedLanguage.startsWith("Python")) {
              EnvironmentManager.loadPythonVersions(selectedLanguage, versionComboBox, cachedVersions);
          } else if (selectedLanguage != null && selectedLanguage.startsWith("R")) {
              EnvironmentManager.loadRVersions(selectedLanguage, versionComboBox, cachedVersions);
          }
      });

      // Environment Name Label and Text Field
      gbc.gridy++;
      gbc.gridx = 0;
      panel.add(new JLabel("Environment Name:"), gbc);

      gbc.gridx = 1;
      envNameTextField = new JTextField(20);
      panel.add(envNameTextField, gbc);

      // Packages Table
      gbc.gridy++;
      gbc.gridx = 0;
      gbc.gridwidth = 2;
      gbc.weighty = 1;
      gbc.fill = GridBagConstraints.BOTH;
      String[] columnNames = {"Package Name", "Version", "Channel", "Build"};
      tableModel = new DefaultTableModel(columnNames, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
              return true; // Allow all cells to be editable
          }
      };
      for (String[] packageRow : m_packagesList) {
          tableModel.addRow(packageRow);
      }
      JTable packagesTable = new JTable(tableModel);
      panel.add(new JScrollPane(packagesTable), gbc);

      // Add and Delete Row Buttons
      gbc.gridy++;
      gbc.gridwidth = 1;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      JButton addRowButton = new JButton("Add Row");
      panel.add(addRowButton, gbc);

      gbc.gridx = 1;
      JButton deleteRowButton = new JButton("Delete Row");
      panel.add(deleteRowButton, gbc);

      addRowButton.addActionListener(e -> {
          tableModel.addRow(new String[]{"", "", "", ""});
      });

      deleteRowButton.addActionListener(e -> {
          int selectedRow = packagesTable.getSelectedRow();
          if (selectedRow != -1) {
              tableModel.removeRow(selectedRow);
          } else {
              JOptionPane.showMessageDialog(panel, "Please select a row to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
          }
      });

      // Create Environment Button
      gbc.gridy++;
      gbc.gridx = 0;
      gbc.gridwidth = 2;
      JButton createEnvButton = new JButton("Create Environment");
      panel.add(createEnvButton, gbc);

      createEnvButton.addActionListener(e -> {
          String environmentName = envNameTextField.getText();
          if (environmentName.isEmpty()) {
              JOptionPane.showMessageDialog(panel, "Environment name is required.", "Warning", JOptionPane.WARNING_MESSAGE);
              return;
          }
          
          String languageWrittenIn = (String) languageComboBox.getSelectedItem();
          String[] depArray = new String[tableModel.getRowCount()];
          for (int i = 0; i < tableModel.getRowCount(); i++) {
              depArray[i] = (String) tableModel.getValueAt(i, 0); // Assuming dep is in the first column
          }
          

          EnvironmentStatus envStatus = EnvironmentManager.createEnvironment(environmentName, languageWrittenIn, depArray, tableModel, panel, this, m_status,((String) versionComboBox.getSelectedItem()), null);
          proposedEnvName = envStatus.getEnvironmentName();
      });

      // Log Text Area
      gbc.gridy++;
      gbc.gridx = 0;
      gbc.gridwidth = 2;
      gbc.weighty = 1;
      logTextArea = new JTextArea(10, 50);
      logTextArea.setEditable(false);
      logScrollPane = new JScrollPane(logTextArea);
      panel.add(new JLabel("Log Output:"), gbc);

      gbc.gridy++;
      gbc.fill = GridBagConstraints.BOTH;
      panel.add(logScrollPane, gbc);

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
    // Check if the proposedEnvName is empty
    if (proposedEnvName == null || proposedEnvName.trim().isEmpty()) {
        // Throw an exception indicating that the environment name is required
        throw new InvalidSettingsException("Environment name is required and cannot be empty.");
    }
    condaEnvName.setStringValue(proposedEnvName);
    condaEnvName.saveSettingsTo(settings);
  }

  public void addNewPackage(String[][] packageData) {
      for (int i = 0; i < packageData.length; i++) {
          tableModel.addRow(packageData[i]);
      }
  }

  

}