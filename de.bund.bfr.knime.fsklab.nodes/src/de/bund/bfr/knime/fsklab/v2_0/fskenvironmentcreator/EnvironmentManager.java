package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.Version;
import de.bund.bfr.knime.fsklab.preferences.CondaEnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator.FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus;

public class EnvironmentManager {
    
    
    public static EnvironmentStatus createEnvironment(String environmentName, String languageWrittenIn, String[] additionalDependencies, DefaultTableModel tableModel, JPanel panel, FSKEnvironmentCreatorNodeDialog instance, CondaEnvironmentCreationStatus m_status, String version, ExecutionContext exec) {
      File tempYamlFile = null;
      EnvironmentStatus envStatus = new EnvironmentStatus(environmentName, false);
      try {
          StringBuilder yamlContent = new StringBuilder();
          CondaEnvVersion condaVersion = CondaEnvVersion.R4;
          int majorVersion = 4;

          if (languageWrittenIn.toLowerCase().startsWith("python 2")) {
              yamlContent.append(CondaEnvironmentManager.getPython2EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.PYTHON2;
              majorVersion = 2;
          } else if (languageWrittenIn.toLowerCase().startsWith("python 3")) {
              yamlContent.append(CondaEnvironmentManager.getPython3EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.PYTHON3;
              majorVersion = 3;
          } else if (languageWrittenIn.toLowerCase().startsWith("r 3")) {
              yamlContent.append(CondaEnvironmentManager.getR3EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.R3;
              majorVersion = 3;
          } else if (languageWrittenIn.toLowerCase().startsWith("r 4")) {
              yamlContent.append(CondaEnvironmentManager.getR4EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.R4;
              majorVersion = 4;
          }

          Set<String> requiredPackages = new HashSet<>();
          if (additionalDependencies != null) {
              requiredPackages.addAll(Arrays.asList(additionalDependencies));
          }

          Map<String, Set<String>> existingEnvs = CondaEnvironmentManager.loadExistingEnvironments();
          String matchedEnv = CondaEnvironmentManager.findMatchingEnvironment(existingEnvs, languageWrittenIn, version, requiredPackages);

          if (matchedEnv != null && existingEnvs.get(matchedEnv.replace("PARTIAL_MATCH:", "")) != null) {
              requiredPackages.addAll(existingEnvs.get(matchedEnv.replace("PARTIAL_MATCH:", "")));
          }
         

          if (matchedEnv != null) {
              if (matchedEnv.startsWith("PARTIAL_MATCH:")) {
                  String partialEnv = matchedEnv.replace("PARTIAL_MATCH:", "");
                  CondaEnvironmentManager.deleteEnvironment(partialEnv, exec);
                  existingEnvs.remove(partialEnv);
                  CondaEnvironmentManager.removeEnvironmentEntry(partialEnv);
                  environmentName = partialEnv;
                  envStatus.setEnvExist(false);
                  envStatus.setEnvironmentName(partialEnv);
              } else {
                  envStatus.setEnvExist(true);
                  envStatus.setEnvironmentName(matchedEnv);
                  return envStatus;
              }
          }else if (environmentName.isEmpty()) {
              JOptionPane.showMessageDialog(panel, "Environment name is required.", "Warning", JOptionPane.WARNING_MESSAGE);
              return null;
          }

          CondaEnvironmentManager.updateEnvironmentFile(existingEnvs, environmentName, languageWrittenIn, version, requiredPackages);

          // ** Step 1: Append Required Packages to YAML Content**
          for (String pkg : requiredPackages) {
              yamlContent.append("  - ").append(languageWrittenIn.toLowerCase().startsWith("r")? "r-"+pkg:pkg).append("\n");
          }

          // **🔹 Step 2: Write the YAML Content to File**
          tempYamlFile = File.createTempFile("conda_env_", ".yaml");
          try (FileWriter writer = new FileWriter(tempYamlFile)) {
              writer.write(yamlContent.toString());
          }

          // **🔹 Step 3: Start Environment Creation**
          FSKCondaEnvironmentCreationObserver obs = new FSKCondaEnvironmentCreationObserver(condaVersion);
          obs.startEnvironmentCreation(environmentName, tempYamlFile.getAbsolutePath(), new Version(majorVersion, 0, 0), instance != null ? instance.m_status : m_status);

      } catch (IOException ex) {
          JOptionPane.showMessageDialog(panel, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
      }
      return envStatus;
  }


    

    

    private static void registerExternalHooksupdateUI( FSKEnvironmentCreatorNodeDialog instance) {
      instance.m_status.getStatusMessage().addChangeListener(instance::updateStatusMessage);
      instance.m_status.getProgress().addChangeListener(instance::updateProgress);
      instance.m_status.getErrorLog().addChangeListener(instance::updateErrorLog);
    }
   

    
    

  public static void loadPythonVersions(String language, JComboBox<String> versionComboBox, Map<String, Set<String>> cachedVersions) {
      if (cachedVersions.containsKey(language)) {
          SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
      } else {
          versionComboBox.removeAllItems();
          versionComboBox.addItem("Loading...");
          new Thread(() -> {
              Set<String> pythonVersions = CondaEnvironmentManager.fetchPythonVersions();
              cachedVersions.put("Python 2", filterVersions(pythonVersions, "2."));
              cachedVersions.put("Python 3", filterVersions(pythonVersions, "3."));
              SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
          }).start();
      }
  }

  public static void loadRVersions(String language, JComboBox<String> versionComboBox, Map<String, Set<String>> cachedVersions) {
      if (cachedVersions.containsKey(language)) {
          SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
      } else {
          versionComboBox.removeAllItems();
          versionComboBox.addItem("Loading...");
          new Thread(() -> {
              Set<String> rVersions = CondaEnvironmentManager.fetchRBaseVersions();
              cachedVersions.put("R 3", filterVersions(rVersions, "3."));
              cachedVersions.put("R 4", filterVersions(rVersions, "4."));
              SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
          }).start();
      }
  }

  private static void populateVersionComboBox(Set<String> versions, JComboBox<String> versionComboBox) {
      versionComboBox.removeAllItems();
      for (String version : versions) {
          versionComboBox.addItem(version);
      }
  }

  private static Set<String> filterVersions(Set<String> versions, String prefix) {
      return versions.stream()
              .filter(version -> version.startsWith(prefix))
              .collect(Collectors.toCollection(TreeSet::new));
  }

}
