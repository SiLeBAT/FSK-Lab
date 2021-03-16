package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.ScriptHandler} when an output
 * parameter that was annotated as FILE could not be found in the working directory.
 */
public class ResourceFileNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -7375284310836600181L;

  /**
   * Constructs an <code>ResourceFileNotFoundException</code> with the specified detail message.
   * 
   * @param fileName the missing output file from an executed model
   */
  public ResourceFileNotFoundException(String fileName) {

    // super(err);
    FskErrorMessages.resourceFileNotFoundWarning(fileName);

  }
}
