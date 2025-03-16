package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.util.Version;
import de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator.FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus;

public class EnvironmentManager {
    private static final String ENV_DIR_PATH = System.getProperty("user.home") + "/.fsk";
    private static final String ENV_FILE_PATH = ENV_DIR_PATH + "/.fskx_envs.yaml";
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
               + "  - knime\n"
               + "  - conda-forge\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=" + pythonVersion + "\n"
               + "  - knime-python-scripting\n"
               + "  - descartes\n"
               + "  - geopandas\n"
               + "  - networkx\n";
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
     * CondaEnvironmentCreationStatus
     *   - **Utilities**:
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
    

    public static EnvironmentStatus createEnvironment(String environmentName, String languageWrittenIn, String[] additionalDependencies, DefaultTableModel tableModel, JPanel panel, FSKEnvironmentCreatorNodeDialog instance, CondaEnvironmentCreationStatus m_status, String version,ExecutionContext exec) {
      File tempYamlFile = null;
      EnvironmentStatus envStatus = new EnvironmentStatus(environmentName, false);
      try {
          StringBuilder yamlContent = new StringBuilder();
          CondaEnvVersion condaVersion = CondaEnvVersion.R4;
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

          Set<String> requiredPackages = new HashSet<>();
          if (additionalDependencies != null) {
              requiredPackages.addAll(Arrays.asList(additionalDependencies));
          }

          Map<String, Set<String>> existingEnvs = loadExistingEnvironments();
          String matchedEnv = findMatchingEnvironment(existingEnvs, languageWrittenIn, version, requiredPackages);
          if(matchedEnv != null && existingEnvs.get(matchedEnv.replace("PARTIAL_MATCH:", "")) != null)
            requiredPackages.addAll(existingEnvs.get(matchedEnv.replace("PARTIAL_MATCH:", "")));
          if (matchedEnv != null) {
              if (matchedEnv.startsWith("PARTIAL_MATCH:")) {
                  String partialEnv = matchedEnv.replace("PARTIAL_MATCH:", "");
                  deleteEnvironment(partialEnv, exec);
                  existingEnvs.remove(partialEnv);
                  environmentName = partialEnv;
                  envStatus.setEnvExist(false);
                  envStatus.setEnvironmentName(partialEnv);
              } else {
                  envStatus.setEnvExist(true);
                  envStatus.setEnvironmentName(matchedEnv);
                  return envStatus;
              }
          }

          updateEnvironmentFile(existingEnvs, environmentName, languageWrittenIn, version, requiredPackages);
          tempYamlFile = File.createTempFile("conda_env_", ".yaml");
          try (FileWriter writer = new FileWriter(tempYamlFile)) {
              writer.write(yamlContent.toString());
          }

          FSKCondaEnvironmentCreationObserver obs = new FSKCondaEnvironmentCreationObserver(condaVersion);
          obs.startEnvironmentCreation(environmentName, tempYamlFile.getAbsolutePath(), new Version(majorVersion, 0, 0), instance != null ? instance.m_status : m_status);

      } catch (IOException ex) {
          JOptionPane.showMessageDialog(panel, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
      }
      return envStatus;
  }

    private static void deleteEnvironment(String envName, ExecutionContext exec) {
      try {
          ProcessBuilder builder = new ProcessBuilder("conda", "remove", "--name", envName, "--all", "-y");
          builder.redirectErrorStream(true); // Merge error and output streams
          Process process = builder.start();

          // Read and log output from the process
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
              String line;
              while ((line = reader.readLine()) != null) {
                if(exec!=null)  
                  exec.setMessage("[Conda Remove] " + line); 
                else
                  System.out.println("[Conda Remove] " + line);
              }
          }

          int exitCode = process.waitFor();
          if (exitCode == 0) {
            if(exec!=null)  
              exec.setMessage("Successfully deleted Conda environment: " + envName);
            else
              System.out.println("Successfully deleted Conda environment: " + envName);
          } else {
            if(exec!=null)  
              exec.setMessage("Failed to delete Conda environment: " + envName + " with exit code " + exitCode);
            else
              System.out.println("Failed to delete Conda environment: " + envName + " with exit code " + exitCode);
          }
      } catch (IOException | InterruptedException e) {
          e.printStackTrace();
      }
  }


    private static Map<String, Set<String>> loadExistingEnvironments() {
        Map<String, Set<String>> envs = new HashMap<>();
        File envDir = new File(ENV_DIR_PATH);
        File envFile = new File(ENV_FILE_PATH);

        if (!envDir.exists()) {
            envDir.mkdirs();
        }
        if (!envFile.exists()) {
            try {
                envFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return envs;
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(ENV_FILE_PATH));
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue;
                String envName = parts[0];
                String language = parts[1];
                Set<String> packages = new HashSet<>(Arrays.asList(parts[2].split(",")));
                envs.put(envName, packages);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return envs;
    }

    private static String findMatchingEnvironment(Map<String, Set<String>> existingEnvs, String language, String version, Set<String> requiredPackages) {
      for (Map.Entry<String, Set<String>> entry : existingEnvs.entrySet()) {
          if (entry.getValue().containsAll(requiredPackages)) {
              return entry.getKey(); // Exact match found
          } else if (!Collections.disjoint(entry.getValue(), requiredPackages)) {
              return "PARTIAL_MATCH:" + entry.getKey(); // Partial match found
          }
      }
      return null;
  }

    private static void updateEnvironmentFile(Map<String, Set<String>> existingEnvs, String envName, String language, String version, Set<String> requiredPackages) {
        existingEnvs.put(envName, requiredPackages);
        try (FileWriter writer = new FileWriter(ENV_FILE_PATH)) {
            for (Map.Entry<String, Set<String>> entry : existingEnvs.entrySet()) {
                writer.write(entry.getKey() + ";" + language + ";" + String.join(",", entry.getValue()) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
