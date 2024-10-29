package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.util.Version;
import de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator.FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus;

public class EnvironmentManager {

    /**
     * Generates the Conda environment YAML content for Python 3.
     * The environment includes libraries for Data Analysis, Machine Learning, Plotting, and Utilities.
     *
     * @param envName The name of the environment.
     * @param version The Python 3 version, defaulting to 3.9 if empty.
     * @return A String representing the YAML configuration for the Conda environment.
     * 
     * ### Types of Libraries Included:
     * - **Data Analysis and Machine Learning**:
     *   - `numpy`: Array computing and mathematical operations.
     *   - `pandas`: Data manipulation and analysis.
     *   - `scikit-learn`: Machine learning algorithms.
     *   - `scipy`: Scientific computing, including functions for optimization and statistics.
     * 
     * - **Plotting**:
     *   - `matplotlib-base`: Basic 2D plotting.
     *   - `plotly`: Interactive graphing and visualization.
     *   - `seaborn`: Statistical data visualization based on matplotlib.
     *   - `statsmodels`: Statistical models and hypothesis tests.
     * 
     * - **Utilities**:
     *   - `requests`: HTTP library for sending HTTP requests.
     *   - `pillow`: Image processing.
     *   - `openpyxl`: Working with Excel files (.xlsx).
     *   - `pyyaml`: YAML file parsing and writing.
     */
    public static String getPython3EnvContent(String envName, String version) {
        String pythonVersion = (version != null && !version.isEmpty()) ? version : "3.9";
        return "name: " + envName + "\n"
               + "channels:\n"
               + "  - conda-forge\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=" + pythonVersion + "\n"
               + "  - numpy\n"
               + "  - pandas\n"
               + "  - scikit-learn\n"
               + "  - scipy\n"
               + "  - matplotlib-base\n"
               + "  - plotly\n"
               + "  - seaborn\n"
               + "  - statsmodels\n"
               + "  - requests\n"
               + "  - pillow\n"
               + "  - openpyxl\n"
               + "  - descartes\n"
               + "  - pyogrio\n"
               + "  - pyyaml\n";
    }
  
    /**
     * Generates the Conda environment YAML content for Python 2.
     * The environment includes libraries for Data Analysis, Machine Learning, Plotting, and Utilities.
     *
     * @param envName The name of the environment.
     * @param version The Python 2 version, defaulting to 2.7 if empty.
     * @return A String representing the YAML configuration for the Conda environment.
     * 
     * ### Types of Libraries Included:
     * - **Data Analysis and Machine Learning**:
     *   - `numpy`: Array computing and mathematical operations.
     *   - `pandas`: Data manipulation and analysis.
     *   - `scikit-learn`: Machine learning algorithms.
     *   - `scipy`: Scientific computing, including functions for optimization and statistics.
     * 
     * - **Plotting**:
     *   - `matplotlib-base`: Basic 2D plotting.
     *   - `plotly`: Interactive graphing and visualization.
     *   - `seaborn`: Statistical data visualization based on matplotlib.
     *   - `statsmodels`: Statistical models and hypothesis tests.
     * 
     * - **Utilities**:
     *   - `requests`: HTTP library for sending HTTP requests.
     *   - `pillow`: Image processing.
     *   - `openpyxl`: Working with Excel files (.xlsx).
     *   - `pyyaml`: YAML file parsing and writing.
     */
    public static String getPython2EnvContent(String envName, String version) {
        String pythonVersion = (version != null && !version.isEmpty()) ? version : "2.7";
        return "name: " + envName + "\n"
               + "channels:\n"
               + "  - conda-forge\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=" + pythonVersion + "\n"
               + "  - numpy\n"
               + "  - pandas\n"
               + "  - scikit-learn\n"
               + "  - scipy\n"
               + "  - matplotlib-base\n"
               + "  - plotly\n"
               + "  - seaborn\n"
               + "  - statsmodels\n"
               + "  - requests\n"
               + "  - pillow\n"
               + "  - openpyxl\n"
               + "  - descartes\n"
               + "  - pyogrio\n"
               + "  - pyyaml\n";
    }

    public static String getR3EnvContent(String envName, String version) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "  - conda-forge\n"
               + "dependencies:\n"
               + "  - r-base="+(!StringUtils.isEmpty(version)?version:"3.6.3")+"\n"
               + "  - r-cairo\n"
               + "  - r-rserve\n"
               + "  - r-jsonlite\n"
               + "  - r-svglite\n"
               + "  - r-minicran\n";
    }

    public static String getR4EnvContent(String envName, String version) {
        return "name: "+envName+"\n"
              + "channels:\n"
              + "  - conda-forge\n"
              + "  - defaults\n"
              + "dependencies:\n"
              + "  - r-base="+(!StringUtils.isEmpty(version)?version:"4.1.0")+"\n"
              + "  - r-cairo\n"
              + "  - r-rserve\n"
              + "  - r-jsonlite\n"
              + "  - r-svglite\n"
              + "  - r-minicran\n";
    }
    public static void createEnvironment(String environmentName, String languageWrittenIn, String[] additionalDependencies, DefaultTableModel tableModel, JPanel panel, FSKEnvironmentCreatorNodeDialog instance, CondaEnvironmentCreationStatus m_status, String version) {
      File tempYamlFile = null;
      try {
          // Choose the YAML content dynamically based on user input (or some other condition)
          StringBuilder yamlContent = new StringBuilder();
          CondaEnvVersion condaVersion = CondaEnvVersion.R4; // Default to R4
          int majorVersion = 4;

          if (languageWrittenIn.toLowerCase().startsWith("python 2")) {
              yamlContent.append(getPython2EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.PYTHON2;
              majorVersion = 2;
          } else if (languageWrittenIn.toLowerCase().startsWith("python 3")) {
              yamlContent.append(getPython3EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.PYTHON3;
              majorVersion = 3;
          } else if (languageWrittenIn.toLowerCase().startsWith("r 3")) {
              yamlContent.append(getR3EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.R3;
              majorVersion = 3;
          } else if (languageWrittenIn.toLowerCase().startsWith("r 4")) {
              yamlContent.append(getR4EnvContent(environmentName, version));
              condaVersion = CondaEnvVersion.R4;
              majorVersion = 4;
          }

          // Add additional dependencies
          if (additionalDependencies != null && additionalDependencies.length > 0) {
              for (String dependency : additionalDependencies) {
                  if (!StringUtils.isBlank(dependency)) {
                      if (languageWrittenIn.toLowerCase().startsWith("r ")) {
                          dependency = "r-" + dependency;
                      }
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

         

          // Register any external hooks

          if(instance != null) {
            registerExternalHooksupdateUI(instance);
            // Use the temporary YAML file path for environment creation
            FSKCondaEnvironmentCreationObserver obs = new FSKCondaEnvironmentCreationObserver(condaVersion);
            obs.startEnvironmentCreation(environmentName, tempYamlFile.getAbsolutePath(), new Version(majorVersion, 0, 0), instance.m_status);
          }else {
            // Use the temporary YAML file path for environment creation
            FSKCondaEnvironmentCreationObserver obs = new FSKCondaEnvironmentCreationObserver(condaVersion);
            obs.startEnvironmentCreation(environmentName, tempYamlFile.getAbsolutePath(), new Version(majorVersion, 0, 0), m_status);
          
          }

          
          
      } catch (IOException ex) {
          JOptionPane.showMessageDialog(panel, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
      }
  }

    private static void registerExternalHooksupdateUI( FSKEnvironmentCreatorNodeDialog instance) {
      instance.m_status.getStatusMessage().addChangeListener(instance::updateStatusMessage);
      instance.m_status.getProgress().addChangeListener(instance::updateProgress);
      instance.m_status.getErrorLog().addChangeListener(instance::updateErrorLog);
    }
   

    
    public static String[][] convertCommaSeparatedStringToArray(String input) {
      String[] tokens = input.split("\\s*,\\s*");
      String[][] result = new String[tokens.length][2];
      for (int i = 0; i < tokens.length; i++) {
          result[i][0] = tokens[i];
          result[i][1] = "";
      }
      return result;
    }
    public static String[] getPackages(String input) {
      String[] tokens = input.split("\\s*,\\s*");
      String[] result = new String[tokens.length];
      for (int i = 0; i < tokens.length; i++) {
          result[i] = tokens[i];
      }
      return result;
    }
 // Generic method to fetch available versions from Conda
    public static List<String> fetchVersions(String packageName, String filter) {
        List<String> versions = Collections.synchronizedList(new ArrayList<>());
        List<String> errorList = Collections.synchronizedList(new ArrayList<>());

        ProcessBuilder builder = new ProcessBuilder();
        String os = System.getProperty("os.name").toLowerCase();
        String command = "conda search " + packageName + " -c conda-forge";

        // Command configuration for cross-platform compatibility
        if (os.contains("win")) {
            builder.command("cmd.exe", "/c", command);
        } else {
            builder.command("/bin/bash", "-c", command);
        }

        try {
            final Process process = builder.start();

            // Read output in a separate thread
            Thread outputThread = new Thread(() -> {
                try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = outputReader.readLine()) != null) {
                        if (line.startsWith(filter)) {
                            String[] columns = line.trim().split("\\s+");
                            if (columns.length > 1) {
                                versions.add(columns[1]);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Read errors in a separate thread
            Thread errorThread = new Thread(() -> {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorList.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputThread.start();
            errorThread.start();
            int exitCode = process.waitFor();
            outputThread.join();
            errorThread.join();

            System.out.println(packageName + " version fetch process finished with exit code: " + exitCode);

            if (!errorList.isEmpty()) {
                System.err.println("Errors from Conda process: " + String.join("\n", errorList));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return versions;
    }

    public static List<String> fetchPythonVersions() {
        return fetchVersions("python", "python");
    }

    public static List<String> fetchRBaseVersions() {
        return fetchVersions("r-base", "r-base");
    }

    public static void loadPythonVersions(String language, JComboBox<String> versionComboBox, Map<String, List<String>> cachedVersions) {
        if (cachedVersions.containsKey(language)) {
            SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
        } else {
            versionComboBox.removeAllItems();
            versionComboBox.addItem("Loading...");
            new Thread(() -> {
                List<String> pythonVersions = fetchPythonVersions();
                cachedVersions.put("Python 2", filterVersions(pythonVersions, "2."));
                cachedVersions.put("Python 3", filterVersions(pythonVersions, "3."));
                SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
            }).start();
        }
    }

    public static void loadRVersions(String language, JComboBox<String> versionComboBox, Map<String, List<String>> cachedVersions) {
        if (cachedVersions.containsKey(language)) {
            SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
        } else {
            versionComboBox.removeAllItems();
            versionComboBox.addItem("Loading...");
            new Thread(() -> {
                List<String> rVersions = fetchRBaseVersions();
                cachedVersions.put("R 3", filterVersions(rVersions, "3."));
                cachedVersions.put("R 4", filterVersions(rVersions, "4."));
                SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
            }).start();
        }
    }

    private static void populateVersionComboBox(List<String> versions, JComboBox<String> versionComboBox) {
        versionComboBox.removeAllItems();
        for (String version : versions) {
            versionComboBox.addItem(version);
        }
    }

    private static List<String> filterVersions(List<String> versions, String prefix) {
        return versions.stream()
                .filter(version -> version.startsWith(prefix))
                .collect(Collectors.toList());
    }
    
}
