package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.ScriptHandler} when a JSON
 * file could not be written to the generated resource folder.
 * 
 */
public class JsonFileNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -8537547056019262472L;

  /**
   * Constructs an <code>JsonFileNotFoundException</code> with the specified detail message.
   * 
   * @param fileName the parameter JSON file that could not be written out
   */
  public JsonFileNotFoundException(String fileName) {

    // super(err);
    FskErrorMessages.jsonFileNotFoundWarning(fileName);

  }
}
