package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.util.Version;
import de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator.FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus;

public class EnvironmentManager {

    public static String getPython2EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=2.7\n"
               + "  - pandas\n"
               + "  - matplotlib\n";
    }

    public static String getPython3EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=3.9\n"
               + "  - pandas\n"
               + "  - matplotlib\n";
    }

    public static String getR3EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "  - conda-forge\n"
               + "dependencies:\n"
               + "  - r-base=3.6\n"
               + "  - r-rserve\n"
               + "  - r-cairo\n"
               + "  - r-ggplot2\n"
               + "  - r-jsonlite\n"
               + "  - r-svglite\n"
               + "  - r-minicran\n"
               + "  - r-msm\n"
               + "  - r-mc2d\n"
               + "  - r-tidyverse\n"
               + "  - r-robustbase\n"
               + "  - r-gridextra\n"
               + "  - r-mcmcpack\n"
               + "  - r-gridgraphics\n"
               + "  - r-distr\n"
               + "  - r-hmisc\n"
               + "  - r-reshape2\n"
               + "  - r-gsl\n"
               + "  - r-triangle\n"
               + "  - r-maldiquant\n"
               + "  - r-kernsmooth\n"
               + "  - r-r2openbugs\n";
    }

    public static String getR4EnvContent(String envName) {
        return "name: "+envName+"\n"
              + "channels:\n"
              + "  - conda-forge\n"
              + "  - defaults\n"
              + "dependencies:\n"
              + "  - r-base=4.2.2\n"
              + "  - r-rserve\n"
              + "  - r-cairo\n"
              + "  - r-ggplot2\n"
              + "  - r-jsonlite\n"
              + "  - r-svglite\n"
              + "  - r-minicran\n"
              + "  - r-msm\n"
              + "  - r-mc2d\n"
              + "  - r-tidyverse\n"
              + "  - r-robustbase\n"
              + "  - r-gridextra\n"
              + "  - r-mcmcpack\n"
              + "  - r-gridgraphics\n"
              + "  - r-distr\n"
              + "  - r-hmisc\n"
              + "  - r-reshape2\n"
              + "  - r-gsl\n"
              + "  - r-triangle\n"
              + "  - r-maldiquant\n"
              + "  - r-kernsmooth\n"
              + "  - r-r2openbugs\n";
    }
    public static void createEnvironment(String environmentName, String languageWrittenIn, String[] additionalDependencies, DefaultTableModel tableModel, JPanel panel, FSKEnvironmentCreatorNodeDialog instance, CondaEnvironmentCreationStatus m_status) {
      File tempYamlFile = null;
      try {
          // Choose the YAML content dynamically based on user input (or some other condition)
          StringBuilder yamlContent = new StringBuilder();
          CondaEnvVersion condaVersion = CondaEnvVersion.R4; // Default to R4
          int majorVersion = 4;

          if (languageWrittenIn.toLowerCase().startsWith("python 2")) {
              yamlContent.append(getPython2EnvContent(environmentName));
              condaVersion = CondaEnvVersion.PYTHON2;
              majorVersion = 2;
          } else if (languageWrittenIn.toLowerCase().startsWith("python 3")) {
              yamlContent.append(getPython3EnvContent(environmentName));
              condaVersion = CondaEnvVersion.PYTHON3;
              majorVersion = 3;
          } else if (languageWrittenIn.toLowerCase().startsWith("r 3")) {
              yamlContent.append(getR3EnvContent(environmentName));
              condaVersion = CondaEnvVersion.R3;
              majorVersion = 3;
          } else if (languageWrittenIn.toLowerCase().startsWith("r 4")) {
              yamlContent.append(getR4EnvContent(environmentName));
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
    
    
}
