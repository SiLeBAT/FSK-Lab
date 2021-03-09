package de.bund.bfr.knime.fsklab;

import java.util.Objects;
import org.knime.core.node.NodeLogger;

public class FskErrorMessages {
  static NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  // Warning message when a variable that should be available globally in the workspace
  // is only created and used locally e.g. in a function.
  // Usage: JsonHandler.saveOutputVariables
  public static void variableNotGlobalWarning(String variableName, String modelId) {
    if (isNotNull("Variable", variableName) && isNotNull("Model ID", modelId)) {
      LOGGER.warn("WARNING: OUTPUT parameter '" + variableName + "' of model '" + modelId
          + "' not available in workspace after execution. Make sure"
          + " all output variables are global in the model script.");
    }
  }

  // Warning message when a generated resource (OUTPUT FILE) was not found.
  // Usage: ScriptHandler.saveGeneratedResources
  public static void resourceFileNotFoundWarning(String fileName) {
    if (isNotNull("Resource File", fileName)) {
      LOGGER.warn("WARNING: OUTPUT parameter declared as FILE was not found in working directory: '"
          + fileName + "'");
    }
  }

  // Warning message when a JSON file could not be written.
  // Usage: ScriptHandler.saveGeneratedResources
  public static void jsonFileNotFoundWarning(String workingDirectory) {
    if (isNotNull("JSON File", workingDirectory)) {
      LOGGER.warn("WARNING: output JSON file was not found in working directory: '"
          + workingDirectory + "'");
    }
  }

  // Warning message if there is a problem with the vizualization script
  // Usage: ScriptHandler.runSnippet
  public static void visualizationFailedWarning(String vizScript) {
    if (isNotNull("Visualization Script", vizScript)) {
      LOGGER.warn("WARNING: visualization script failed: \n");
      LOGGER.warn(">> " + vizScript);
    }
  }

  // Error if declaration of simulation parameter cant be executed.
  // Usage: ScriptHandler.runSnippet
  public static void parameterDeclarationError(String parameterDeclaration) {
    if (isNotNull("Simulation Parameter", parameterDeclaration)) {
      LOGGER.error(
          "ERROR: Simulation Parameter could not be loaded. Remove dependencies by checking the parameter order in the simulation.\n");
      LOGGER.warn(">> " + parameterDeclaration);
    }
  }

  // Error if package is missing and cant be installed automatically.
  public static void packageNotFoundError(String pkg) {
    if (isNotNull("Package", pkg)) {
      LOGGER.error("ERROR: there is no package called '" + pkg
          + "' nor could it be installed automatically");
      LOGGER.warn(">> " + pkg);
    }
  }

  // Error if model script fails
  public static void modelScriptError(String msg) {
    if (isNotNull("Evaluation Message", msg)) {
      LOGGER.error("ERROR: model script failed: \n");
      LOGGER.warn(">> " + msg);
    }
  }

  private static boolean isNotNull(String errorType, Object obj) {
    if (Objects.isNull(obj)) {
      LOGGER.warn(errorType + " cant be accessed. The object is undefined (null)");
      return false;
    }
    return true;
  }
}
