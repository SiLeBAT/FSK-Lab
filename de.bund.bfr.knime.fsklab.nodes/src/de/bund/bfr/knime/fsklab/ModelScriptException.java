package de.bund.bfr.knime.fsklab;

public class ModelScriptException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -101624099037987169L;

  public ModelScriptException(String msg, Throwable err) {

    // super(err);
    FskErrorMessages.modelScriptError(msg);

  }
  
}
