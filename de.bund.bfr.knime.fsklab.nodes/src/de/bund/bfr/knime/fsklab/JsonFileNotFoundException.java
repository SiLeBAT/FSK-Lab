package de.bund.bfr.knime.fsklab;

public class JsonFileNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -8537547056019262472L;

  public JsonFileNotFoundException(String fileName, Throwable err) {

    // super(err);
    FskErrorMessages.jsonFileNotFoundWarning(fileName);

  }
}
