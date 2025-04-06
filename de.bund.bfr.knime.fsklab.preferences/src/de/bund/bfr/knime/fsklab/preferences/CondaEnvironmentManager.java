package de.bund.bfr.knime.fsklab.preferences;

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
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;

public class CondaEnvironmentManager {

	    public static final String ENV_DIR_PATH = System.getProperty("user.home") + "/.fsk";
	    public static final String ENV_FILE_PATH = ENV_DIR_PATH + "/.fskx_envs.yaml";
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
	    
	   
	    public static void deleteEnvironment(String envName, ExecutionContext exec) {
	      try {
	          ProcessBuilder builder = new ProcessBuilder("conda", "remove", "--name", envName, "--all", "-y");
	          builder.redirectErrorStream(true); // Merge error and output streams
	          Process process = builder.start();

	          // Read and log output from the process
	          try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	              String line;
	              while ((line = reader.readLine()) != null) {
	                if(exec!=null)  {
	                  exec.setMessage("[Conda Remove] " + line); 
	                  NodeLogger.getLogger(CondaEnvironmentManager.class).info("[Conda Remove] " + line);
	                }
	                else {
	                  System.out.println("[Conda Remove] " + line);
	                  NodeLogger.getLogger(CondaEnvironmentManager.class).info("[Conda Remove] " + line);

	                }
	                
	              }
	          }

	          int exitCode = process.waitFor();
	          if (exitCode == 0) {
	            if(exec!=null)  {
	              NodeLogger.getLogger(CondaEnvironmentManager.class).info("Successfully deleted Conda environment: " + envName);
	            }
	            else {
	              NodeLogger.getLogger(CondaEnvironmentManager.class).info("Successfully deleted Conda environment: " + envName);
	            }
	          } else {
	            if(exec!=null)  {
	              exec.setMessage("Failed to delete Conda environment: " + envName + " with exit code " + exitCode);
	              NodeLogger.getLogger(CondaEnvironmentManager.class).info("Failed to delete Conda environment: " + envName + " with exit code " + exitCode);

	            }
	            else {
	              NodeLogger.getLogger(CondaEnvironmentManager.class).info("Failed to delete Conda environment: " + envName + " with exit code " + exitCode);

	            }
	          }
	      } catch (IOException | InterruptedException e) {
	          e.printStackTrace();
	      }
	  }


	    public static Map<String, Set<String>> loadExistingEnvironments() {
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
	                if (parts.length < 4) continue;
	                String envName = parts[0];
	                String language = parts[1];
	                Set<String> packages = new HashSet<>(Arrays.asList(parts[3].split(",")));
	                envs.put(envName, packages);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return envs;
	    }

	    public static String findMatchingEnvironment(Map<String, Set<String>> existingEnvs, String language, String version, Set<String> requiredPackages) {
	      for (Map.Entry<String, Set<String>> entry : existingEnvs.entrySet()) {
	          if (entry.getValue().containsAll(requiredPackages)) {
	              return entry.getKey(); // Exact match found
	          } else if (!Collections.disjoint(entry.getValue(), requiredPackages)) {
	              return "PARTIAL_MATCH:" + entry.getKey(); // Partial match found
	          }
	      }
	      return null;
	  }

	    public static String[] normalizeLanguageAndVersion(String languageInput, String versionInput) {
	        if (languageInput == null || languageInput.trim().isEmpty()) {
	            return new String[]{"unknown", versionInput != null ? versionInput.trim() : "unknown"};
	        }
	  
	        String lang = languageInput.trim();
	        String version = versionInput != null ? versionInput.trim() : null;
	  
	        String[] parts = lang.split("\\s+");
	        if (parts.length == 2 && parts[1].matches("\\d+(\\.\\d+)*")) {
	            // Case: "python 3.6.2" → extract both language and version
	            if (version == null || version.isEmpty()) {
	                version = parts[1];
	            }
	            lang = parts[0] + " " + parts[1].split("\\.")[0];
	        } else if (parts.length == 2) {
	            // Case: "python 3" or "R 4" → already in expected format
	            lang = parts[0] + " " + parts[1];
	        }
	  
	        return new String[]{lang, version != null ? version : "unknown"};
	    }
	  
	    public static void updateEnvironmentFile(Map<String, Set<String>> existingEnvs, String envName, String languageInput, String versionInput, Set<String> requiredPackages) {
	        String[] langAndVer = normalizeLanguageAndVersion(languageInput, versionInput);
	        String language = langAndVer[0];
	        String version = langAndVer[1];
	        existingEnvs.put(envName, requiredPackages);
	        if(language != null && language.startsWith("R"))
	          version =  version.startsWith("3") ? "3.6.3" : "4.1.0";
	        try (FileWriter writer = new FileWriter(ENV_FILE_PATH, true)) { // append mode = true
	            writer.write(envName + ";" + language + ";" + version + ";" + String.join(",", requiredPackages) + "\n");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    
	    public static void removeEnvironmentEntry(String envNameToRemove) {
	        File file = new File(ENV_FILE_PATH);
	        if (!file.exists()) return;
	  
	        try {
	            List<String> updatedLines = new ArrayList<>();
	  
	            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    String[] parts = line.split(";", 2); // split only on the first semicolon
	                    if (!parts[0].equals(envNameToRemove)) {
	                        updatedLines.add(line);
	                    }
	                }
	            }
	  
	            try (FileWriter writer = new FileWriter(file, false)) { // overwrite mode = false
	                for (String line : updatedLines) {
	                    writer.write(line + System.lineSeparator());
	                }
	            }
	  
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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
	    public static Set<String> fetchVersions(String packageName, String filter) {
	      Set<String> versions = Collections.synchronizedSet(new TreeSet<>());
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

	  public static Set<String> fetchPythonVersions() {
	    return new TreeSet<>(Set.of(
	        "2.7",  
	        "3.6",    
	        "3.7",
	        "3.8",
	        "3.9",
	        "3.10",
	        "3.11"
	    ));
	  }

	  public static Set<String> fetchRBaseVersions() {
	    return new TreeSet<>(Set.of(
	        "3.6.3",    
	        "3.3.3",
	        "3.4.4",
	        "3.5.3",
	        "4.0.5",
	        "4.1.3",
	        "4.2.3",
	        "4.3.3"     
	    ));

	  }

	  public static void loadPythonVersions(String language, JComboBox<String> versionComboBox, Map<String, Set<String>> cachedVersions) {
	      if (cachedVersions.containsKey(language)) {
	          SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
	      } else {
	          versionComboBox.removeAllItems();
	          versionComboBox.addItem("Loading...");
	          new Thread(() -> {
	              Set<String> pythonVersions = fetchPythonVersions();
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
	              Set<String> rVersions = fetchRBaseVersions();
	              cachedVersions.put("R 3", filterVersions(rVersions, "3."));
	              cachedVersions.put("R 4", filterVersions(rVersions, "4."));
	              SwingUtilities.invokeLater(() -> populateVersionComboBox(cachedVersions.get(language), versionComboBox));
	          }).start();
	      }
	  }

	  public static void populateVersionComboBox(Set<String> versions, JComboBox<String> versionComboBox) {
	      versionComboBox.removeAllItems();
	      for (String version : versions) {
	          versionComboBox.addItem(version);
	      }
	  }

	  public static Set<String> filterVersions(Set<String> versions, String prefix) {
	      return versions.stream()
	              .filter(version -> version.startsWith(prefix))
	              .collect(Collectors.toCollection(TreeSet::new));
	  }
}
