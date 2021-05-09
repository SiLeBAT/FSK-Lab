package de.bund.bfr.knime.fsklab;

import java.util.Objects;
import org.knime.core.node.NodeLogger;

public class FskErrorMessages {
  static NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /**
   * Warning message when a variable that should be available globally in the workspace is only
   * created and used locally e.g. in a function. Usage:
   * {@link de.bund.bfr.knime.fsklab.VariableNotGlobalException}
   * 
   * @param variableName name of the missing variable
   * @param modelId ID of model currently executed
   * @return error/warning message
   */
  public static String variableNotGlobalWarning(String variableName, String modelId) {
    if (isNotNull("Variable", variableName) && isNotNull("Model ID", modelId)) {
      String msg ="WARNING: OUTPUT parameter '" + variableName + "' of model '" + modelId
          + "' not available in workspace after execution. Make sure"
          + " all output variables are global in the model script.";
      LOGGER.warn(msg);
      return msg;
    } else {
      if (isNotNull("Variable", variableName)) {
        return objectNullMessage(modelId);
      }
    }
    return objectNullMessage(variableName);
  }
  
  /**
   * Warning message when a variable that should be accessible as a file but is either 
   * not available in the workspace or just a script command reading 
   * a file, e.g. "read.csv("file.csv") 
   * 
   *  
   * @param variableName name of the file variable
   * @param modelId ID of model currently executed
   * @return error/warning message
   */
  public static String variableAccessWarning(String variableName, String modelId) {
    if (isNotNull("Variable", variableName) && isNotNull("Model ID", modelId)) {
      String msg ="WARNING: FILE parameter '" + variableName + "' of model '" + modelId
          + "' is not accessible as a filename. Make sure the variable is global and check "
          + "its value. File parameters should only have their file name + extension as a value";
      LOGGER.warn(msg);
      return msg;
    } else {
      if (isNotNull("Variable", variableName)) {
        return objectNullMessage(modelId);
      }
    }
    return objectNullMessage(variableName);
  }
  

  /**
   * Warning message when a generated resource (OUTPUT FILE) was not found. Usage:
   * {@link de.bund.bfr.knime.fsklab.ResourceFileNotFoundException}
   * 
   * @param fileName name of resource file that is not found
   */
  public static String resourceFileNotFoundWarning(String fileName) {
    
    if (isNotNull("Resource File", fileName)) {
      
      String msg = "WARNING: OUTPUT parameter declared as FILE was not found in working directory: '"
          + fileName + "'";
      LOGGER.warn(msg);
      return msg;
    }
    return objectNullMessage(fileName);
  }


  /**
   * Warning message when a JSON file could not be written. Usage:
   * {@link de.bund.bfr.knime.fsklab.VariableNotGlobalException}
   * 
   * @param workingDirectory name (path) of the working directory that doesn't contain the
   *        parameters.json file.
   */
  public static void jsonFileNotFoundWarning(String workingDirectory) {
    if (isNotNull("JSON File", workingDirectory)) {
      LOGGER.error("Error: output JSON file was not found in working directory: '"
          + workingDirectory + "'");
    }
  }

  /**
   * Warning message if there is a problem with the vizualization script Usage:
   * {@link de.bund.bfr.knime.fsklab.VariableNotGlobalException}
   * 
   * @param vizScript the visualization script that failed.
   */
  public static void visualizationFailedWarning(String vizScript) {
    if (isNotNull("Visualization Script", vizScript)) {
      LOGGER.warn("WARNING: visualization script failed: \n");
      LOGGER.warn(">> " + vizScript);
    }
  }

  /**
   * Error if declaration of simulation parameter cant be executed. Usage:
   * {@link de.bund.bfr.knime.fsklab.ParameterDeclarationException}
   * 
   * @param parameterDeclaration the failed command to assign a parameter.
   * @return error message
   */
  public static String parameterDeclarationError(String parameterDeclaration) {
    if (isNotNull("Simulation Parameter", parameterDeclaration)) {
      String msg =     "ERROR: Simulation Parameter could not be loaded. Remove dependencies by checking the parameter order in the simulation.\n";
      msg += ">> " + parameterDeclaration;
      LOGGER.error(msg);
      return msg;
    }
    return objectNullMessage("Parameter declaration");
  }

  /**
  * Error if serialization of parameter to JSON failed.
  * {@link de.bund.bfr.knime.fsklab.ParameterJsonConversionException}
  * 
  * @param parameter name of the parameter that could not be serialized
  * @return error message 
  */
 public static String parameterJsonConversionError(String parameter) {
   if (isNotNull("Simulation Parameter", parameter)) {
     String msg = "ERROR: Simulation Parameter cant be serialized to JSON. Make sure the data type is supported by FSK-Lab.\n";
     msg += ">> " + parameter;
     LOGGER.error(msg);
     return msg;
   }
   return objectNullMessage("Parameter");
 }
  
  /**
   * Error if package is missing and cant be installed automatically. Usage:
   * {@link de.bund.bfr.knime.fsklab.PackageNotFoundException}
   * 
   * @param pkg name of package that was not found.
   */
  public static String packageNotFoundError(String pkg) {
    if (isNotNull("Package", pkg)) {
      String msg = "ERROR: there is no package called '" + pkg
          + "' nor could it be installed automatically"
          + "\n>> " + pkg;
      LOGGER.error(msg);
      return msg;
    }
    return objectNullMessage("Package");
  }

  /**
   * Error if model script fails Usage: {@link de.bund.bfr.knime.fsklab.ModelScriptException}
   * 
   * @param msg detailed error message containing the lines of the model script that failed.
   * @return error message
   */ 
  public static String modelScriptError(String msg) {
    if (isNotNull("Evaluation Message", msg)) {
      LOGGER.error("ERROR: model script failed: \n");
      LOGGER.warn(">> " + msg);
      return msg;
    } else {
      return objectNullMessage(msg);
    }
    
    
  }

  private static boolean isNotNull(String errorType, Object obj) {
    if (Objects.isNull(obj)) {
      LOGGER.warn(errorType + " cant be accessed. The object is undefined (null)");
      return false;
    }
    return true;
  }
  private static String objectNullMessage(String errorType) {
    return errorType + " cant be accessed. The object is undefined (null)";
  }
}
