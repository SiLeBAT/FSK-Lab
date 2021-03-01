package de.bund.bfr.knime.fsklab;

import org.knime.core.node.NodeLogger;

public class FskErrorMessages {

  public static void variableNotGlobal(String variableName) {
    NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");
    LOGGER.warn("WARNING: OUTPUT parameter '" + variableName + "' not available in workspace after execution. Make sure"
      + " all output variables are global in the model script.");
  }
}
