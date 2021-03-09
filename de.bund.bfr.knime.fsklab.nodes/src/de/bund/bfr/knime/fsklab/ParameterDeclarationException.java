package de.bund.bfr.knime.fsklab;

public class ParameterDeclarationException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -8062328246312350530L;
  public ParameterDeclarationException(String parameterDeclaration, Throwable err) {

    // super(err);
    FskErrorMessages.parameterDeclarationError(parameterDeclaration);

  }
}
