package de.bund.bfr.knime.fsklab.preferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	     * @param version The Python 3 version, defaulting to 3.8 if empty.
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
	        
	        return "name: " + envName + "\n"
	               + "channels:\n"
	               + "  - knime\n"
	               + "  - conda-forge\n"
	               + "  - defaults\n"
	               + "dependencies:\n"
	               + "  - python=" + version + "\n"
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
	        return "name: " + envName + "\n"
	               + "channels:\n"
	               + "  - conda-forge\n"
	               + "  - defaults\n"
	               + "dependencies:\n"
	               + "  - python=" + version + "\n"
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
	               + "  - pkgs/r\n"
	               + "dependencies:\n"
	               + "  - r-base="+(!StringUtils.isEmpty(version)?version:"3.6.3")+"\n"
	               + "  - r-cairo\n"
	               + "  - r-rserve\n"
	               + "  - r-jsonlite\n"
	               + "  - r-svglite\n"
	               + "  - r-minicran\n";
	    }

	    public static String getR4EnvContent(String envName, String version) {

	        final String rVersion = StringUtils.isBlank(version) ? "4.1.3" : version.trim();

	        boolean cairoSupported = isCairoPrebuiltOnWin(rVersion);

	        StringBuilder yml = new StringBuilder()
	            .append("name: ").append(envName).append('\n')
	            .append("channels:\n")
	            .append("  - conda-forge\n")
	            .append("  - r\n")
	            .append("  - defaults\n")
	            .append("dependencies:\n")
	            .append("  - r-base=").append(rVersion).append('\n');

	        if (cairoSupported) {
	            yml.append("  - r-cairo\n");
	        } else {
	            yml.append("  # r-cairo      # (omitted – no Win‑64 build for R ")
	               .append(rVersion).append(")\n");
	        }

	        yml.append("  - r-rserve\n")
	           .append("  - r-jsonlite\n")
	           .append("  - r-svglite\n")
	           .append("  - r-minicran\n");

	        return yml.toString();
	    }
	    
	    private static boolean isCairoPrebuiltOnWin(String rVersion) {
	        Pattern p = Pattern.compile("^(\\d+)\\.(\\d+)");
	        Matcher m = p.matcher(rVersion);
	        if (!m.find()) return false;           // unknown format → play safe
	        int major = Integer.parseInt(m.group(1));
	        int minor = Integer.parseInt(m.group(2));

	        // anything before R 4 or up to 4.1.x is okay
	        return (major < 4) || (major == 4 && minor <= 1);
	    }
	    
	    public static void deleteEnvironment(String envName, ExecutionContext exec) {
	        List<String> errorList = Collections.synchronizedList(new ArrayList<>());
	        String os = System.getProperty("os.name").toLowerCase();
	        boolean isWindows = os.contains("win");

	        // Update this to your known Conda installation root
	        String condaRoot = "/Users/" + System.getProperty("user.name") + "/opt/anaconda3";
	        String condaSh = condaRoot + "/etc/profile.d/conda.sh";

	        String deleteCommand;
	        ProcessBuilder builder;

	        if (isWindows) {
	            deleteCommand = "conda remove --name " + envName + " --all -y";
	            builder = new ProcessBuilder("cmd.exe", "/c", deleteCommand);
	        } else {
	            builder = new ProcessBuilder();
	            Path condaPath = CondaEnvironmentManager.findConda(); // points to "conda"
	            builder.command(
	                condaPath.toString(), "remove", "--name", envName, "--all", "-y"
	            );
	        }

	        try {
	            Process process = builder.start();

	            Thread outputThread = new Thread(() -> {
	                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        if (exec != null) {
	                            exec.setMessage("[Conda Remove] " + line);
	                            NodeLogger.getLogger(CondaEnvironmentManager.class).info("[Conda Remove] " + line);
	                        } else {
	                            System.out.println("[Conda Remove] " + line);
	                            NodeLogger.getLogger(CondaEnvironmentManager.class).info("[Conda Remove] " + line);
	                        }
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            });

	            Thread errorThread = new Thread(() -> {
	                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
	                    String line;
	                    while ((line = reader.readLine()) != null) {
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

	            if (exitCode == 0) {
	                String msg = " Successfully deleted Conda environment: " + envName;
	                if (exec != null) exec.setMessage(msg);
	                NodeLogger.getLogger(CondaEnvironmentManager.class).info(msg);
	            } else {
	                String msg = " Failed to delete Conda environment: " + envName + " with exit code " + exitCode;
	                if (exec != null) exec.setMessage(msg);
	                NodeLogger.getLogger(CondaEnvironmentManager.class).warn(msg);
	            }

	            if (!errorList.isEmpty()) {
	                String errors = String.join("\n", errorList);
	                System.err.println("Errors during Conda delete:\n" + errors);
	                NodeLogger.getLogger(CondaEnvironmentManager.class).warn("Errors during Conda delete:\n" + errors);
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
	    
	    public static Path findConda() {
		    /* If we’re already in an activated env, CONDA_EXE is exact. */
		    String exe = System.getenv("CONDA_EXE");
		    if (exe != null && Files.isExecutable(Path.of(exe)))
		        return Path.of(exe);

		    /*  If $PATH already points to a real file, use that. */
		    Path onPath = firstExecutableOnPath(isWindows() ? "conda.bat" : "conda");
		    if (onPath != null) return onPath;

		    /*   macOS/Linux: spawn the user’s login shell and ask it. */
		    if (!isWindows()) {
		        String shell = Optional.ofNullable(System.getenv("SHELL"))
		                               .orElse("/bin/bash");
		        String base = runCommand(shell, "-l", "-i", "-c", "conda info --base 2>/dev/null");
		        if (base != null && !base.isBlank()) {
		            Path exe4 = Path.of(base.trim(), "bin", isWindows() ? "conda.bat" : "conda");
		            if (Files.isExecutable(exe4)) return exe4;
		        }

		    }

		    /*   Windows: use the built‑in ‘where’. */
		    if (isWindows()) {
		        String path = runCommand("cmd.exe", "/c", "where conda.bat");
		        if (path != null && Files.isExecutable(Path.of(path)))
		            return Path.of(path);
		    }

		    throw new IllegalStateException(
		        "Conda executable not found – add it to PATH or set CONDA_EXE");
		}
	    private static String runCommand(String... cmd) {
		    try {
		        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
		        try (BufferedReader br = new BufferedReader(
		                 new InputStreamReader(p.getInputStream()))) {
		            String line = br.readLine();          // only need the first
		            if (p.waitFor() == 0 && line != null && !line.isBlank())
		                return line.trim();
		        }
		    } catch (IOException | InterruptedException ignored) {}
		    return null;
		}

		/* ---------- helpers -------------------------------------------------- */
		private static boolean isWindows() {
		    return System.getProperty("os.name").toLowerCase().contains("win");
		}
		private static Path firstExecutableOnPath(String name) {
		    String p = System.getenv("PATH");
		    if (p == null) return null;
		    for (String dir : p.split(File.pathSeparator)) {
		        Path cand = Paths.get(dir, name);
		        if (Files.isExecutable(cand)) return cand;
		    }
		    return null;
		}
	    

		public static Set<String> fetchPythonVersions() {
		    // Arm‑Mac support begins at 3.8
		    return new TreeSet<>(Set.of(
		        "3.8",
		        "3.9",
		        "3.10",
		        "3.11"
		    ));
		}

		public static Set<String> fetchRBaseVersions() {
		    // Smallest set that has binaries for linux, Windows and both macOS variants
		    return new TreeSet<>(Set.of(
		        "3.6.3",  
		        "4.1.3",   
		        "4.3.3",   
		        "4.4.3"    
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
