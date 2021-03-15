package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.ScriptHandler} when a model
 * script failed to execute.
 * 
 */
public class ModelScriptException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -101624099037987169L;

  /**
   * Constructs an <code>ModelScriptException</code> with the specified detail message.
   * 
   * @param msg a detailed message about the errors from the failed script
   */
  public ModelScriptException(String msg) {

    // super(err);
    FskErrorMessages.modelScriptError(msg);

  }

}
