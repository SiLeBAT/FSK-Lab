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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang.StringUtils;
import org.knime.conda.Conda;
import org.knime.conda.CondaEnvironmentIdentifier;
import org.knime.conda.CondaEnvironmentPropagation.CondaEnvironmentSpec;
import org.knime.conda.CondaEnvironmentPropagation.CondaEnvironmentType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.flowvariable.FlowVariablePortObject;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.VariableType;
import de.binfalse.bflog.LOGGER;
import de.bund.bfr.knime.fsklab.preferences.CondaEnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObjectSpec;


public class FSKEnvironmentCreatorNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {FlowVariablePortObject.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES = {FlowVariablePortObject.TYPE};

  static final String CFG_FILE = "condaEnvName";
  String[] additionalDependencies;
  String languageWrittenIn;
  String modelId;
  private final SettingsModelString condaEnvName = new SettingsModelString(CFG_FILE, null);
  private static final ReentrantLock envCreationLock = new ReentrantLock();
  
  public FSKEnvironmentCreatorNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    condaEnvName.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    condaEnvName.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    condaEnvName.validateSettings(settings);
  }

  @Override
  protected void reset() {
    condaEnvName.setStringValue(null);
  }


  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
      // Retrieve flow variables
      Map<String, FlowVariable> flowVars = getAvailableFlowVariables(new VariableType[] {VariableType.StringType.INSTANCE});
      
      flowVars.forEach((key, value) -> {
          if (key.equals("packages")) {
              additionalDependencies = CondaEnvironmentManager.getPackages(value.getStringValue());
          } else if (key.equals("LanguageWrittenIn")) {
              languageWrittenIn = value.getStringValue();
          } else if (key.equals("modelId")) {
              modelId = value.getStringValue();
          }
      });
      
      String environmentName = condaEnvName.getStringValue();
      
      // If environment name is not set, create a new one
      if (environmentName == null || environmentName.isEmpty()) {
        
          envCreationLock.lock();
          try {
              if (environmentName == null || environmentName.isEmpty()) {
                  FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus m_status = new FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus();

                  EnvironmentStatus envStatus = EnvironmentManager.createEnvironment(
                      modelId, languageWrittenIn, additionalDependencies, null, null, null, m_status, null, exec);

                  if (!envStatus.isEnvExist()) {
                      boolean success = waitForEnvironmentCreation(m_status, exec);
                      if (!success) {
                        List<String> versionFallbacks = generateVersionFallbacks(envStatus.version);

                        for (String fallbackVersion : versionFallbacks) {
                           CondaEnvironmentManager.removeEnvironmentEntry(modelId);
                           m_status = new FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus();

                           envStatus = EnvironmentManager.createEnvironment(
                              modelId, languageWrittenIn, additionalDependencies, null, null, null, m_status, fallbackVersion, exec);
                           success = waitForEnvironmentCreation(m_status, exec);
                           if (success) {
                             break;
                           }
                        }
                        if (!success)
                          throw new IllegalStateException("An issue occurred during creating environment: " + environmentName);
                      }
                  }
                  environmentName = envStatus.getEnvironmentName();
              }
          } finally {
              envCreationLock.unlock();
          }
        
      }

      
      // Get the environment path and push the flow variable
      String environmentPath = findEnvironmentPath(environmentName);
      if (environmentPath != null) {
          if(!StringUtils.isEmpty(condaEnvName.getStringValue()))
            pushEnvironmentFlowVariable(condaEnvName.getStringValue(), environmentPath);
          else
            pushEnvironmentFlowVariable(environmentName, environmentPath);

      } else {
          throw new IllegalStateException("Environment path not found for: " + environmentName);
      }

      return new PortObject[]{FlowVariablePortObject.INSTANCE};
  }
  
  /**
   * Generate version fallback options: full version -> major.minor.* -> major.*
   * @param version Original version string
   * @return List of fallback versions
   */
  private static List<String> generateVersionFallbacks(String version) {
      List<String> fallbacks = new ArrayList<>();
  
      if (version != null && !version.isBlank()) {
          String[] parts = version.trim().split("\\.");
  
          // Step 1: Try exact major.minor version with wildcard (e.g., 3.6 -> 3.6.*)
          if (parts.length >= 2) {
              fallbacks.add(parts[0] + "." + parts[1] + ".*");
          }
  
          // Step 2: Try major version only with wildcard (e.g., 3 -> 3.*)
          if (parts.length >= 1) {
              fallbacks.add(parts[0] + ".*");
          }
      }
  
      // Step 3: Fallback with no version specified
      fallbacks.add(""); 
  
      return fallbacks;
  }

  
  private boolean waitForEnvironmentCreation(FSKCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus m_status, ExecutionContext exec) throws InterruptedException {
      int timeout = 1800000; // Set a 30 Minutes timeout
      int elapsed = 0;
      int interval = 500;  // 500ms sleep interval
      
      while (elapsed < timeout) {
          // Get the current status message
          String statusMessage = m_status.getStatusMessage().getStringValue();
          exec.setMessage(statusMessage);
          NodeLogger.getLogger(FSKCondaEnvironmentCreationObserver.class).debug(statusMessage);

          LOGGER.info(statusMessage);
          System.out.println(statusMessage);
          // Check if the environment creation is finished
          if (statusMessage.contains("New environment's name") || statusMessage.contains("already exists. Please use a different, unique name")) {
              return true;
          }else if (statusMessage.contains("Environment creation failed")) {
              return false;
          }
          
          // Sleep for 500ms and increment elapsed time
          Thread.sleep(interval);
          elapsed += interval;
      }
      
      // If we reach the timeout, throw an exception
      throw new IllegalStateException("Environment creation timed out after 30 Minutes.");
  }

  private String findEnvironmentPath(String environmentName) throws IOException {
      Conda conda = new Conda();
      StringBuilder environmentPath = new StringBuilder();
      
      // Search for the environment by name
      conda.getEnvironments().forEach(env -> {
          if (env.getName().equals(environmentName)) {
              environmentPath.append(env.getDirectoryPath());
          }
      });
      
      // Return the environment path or null if not found
      return environmentPath.length() > 0 ? environmentPath.toString() : null;
  }

  private void pushEnvironmentFlowVariable(final String environmentName, final String environmentDirectoryPath) {
    pushFlowVariable("Conda.environment", CondaEnvironmentType.INSTANCE,
        new CondaEnvironmentSpec(new CondaEnvironmentIdentifier(environmentName, environmentDirectoryPath)));
}
  
 
}
