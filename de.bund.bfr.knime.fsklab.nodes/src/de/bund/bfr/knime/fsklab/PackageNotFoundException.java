package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.ScriptHandler} when a package
 * or library is not available for the model execution.
 */
public class PackageNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -5969333578267090733L;
  
  /**
   * Constructs an <code>PackageNotFoundException</code> with the specified detail message.
   * 
   * @param pkg the package or library that was not available
   */
  public PackageNotFoundException(String pkg) {

    // super(err);
    super(FskErrorMessages.packageNotFoundError(pkg));

  }
}
