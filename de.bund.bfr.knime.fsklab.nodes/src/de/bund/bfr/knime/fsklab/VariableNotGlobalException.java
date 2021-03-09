package de.bund.bfr.knime.fsklab;

public class VariableNotGlobalException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1080961947247596013L;

  public VariableNotGlobalException(String variableName, String modelId, Throwable err) {
    
    //super(err);
    FskErrorMessages.variableNotGlobalWarning(variableName, modelId);
    
  }

}
