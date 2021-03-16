package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.ScriptHandler} when a
 * parameter is assigned an improper value (e.g. string instead of number) or a variable that hasn't
 * been declared before.
 */
public class ParameterDeclarationException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -8062328246312350530L;

  /**
   * Constructs an <code>ParameterDeclarationException</code> with the specified detail message.
   * 
   * @param parameterDeclaration assignment command (dependent on script language)
   */
  public ParameterDeclarationException(String parameterDeclaration) {

    // super(err);
    FskErrorMessages.parameterDeclarationError(parameterDeclaration);

  }
}
