package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.JsonHandler} when an output
 * parameter is not available in the global scope of the working directory.
 */
public class VariableNotGlobalException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1080961947247596013L;

  /**
   * Constructs an <code>VariableNotGlobalException</code> with the specified detail message.
   * 
   * @param variableName name of the output parameter
   * @param modelId ID of the model that created the parameter
   */
  public VariableNotGlobalException(String variableName, String modelId) {

    // super(err);
    super(FskErrorMessages.variableNotGlobalWarning(variableName, modelId));

  }

}
