package de.bund.bfr.knime.fsklab;

public class PackageNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -5969333578267090733L;
  public PackageNotFoundException(String pkg, Throwable err) {

    // super(err);
    FskErrorMessages.packageNotFoundError(pkg);

  }
}
