package de.bund.bfr.knime.fsklab;

public class UnsupportedFileExtensionException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 4704704980718608597L;

  /**
   * Constructs an <code>UnsupportedFileExtensionException</code> with the specified detail message.
   * 
   * @param extension the file-extension that is not supported
   */
  public UnsupportedFileExtensionException(String extension) {

    // super(err);
    super(extension + " not supported");

  }
}
