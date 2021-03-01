package de.bund.bfr.knime.fsklab;

import org.knime.core.node.NodeLogger;

public class FskErrorMessages {

  // Warning message when a variable that should be available globally in the workspace
  // is only created and used locally e.g. in a function.
  // Usage: JsonHandler.saveOutputVariables
  public static void variableNotGlobal(String variableName) {
    NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");
    LOGGER.warn("WARNING: OUTPUT parameter '" + variableName + "' not available in workspace after execution. Make sure"
      + " all output variables are global in the model script.");
  }
}
