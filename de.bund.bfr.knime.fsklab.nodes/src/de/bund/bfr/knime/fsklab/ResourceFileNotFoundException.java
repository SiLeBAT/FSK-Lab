package de.bund.bfr.knime.fsklab;

public class ResourceFileNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -7375284310836600181L;

  public ResourceFileNotFoundException(String fileName, Throwable err) {
    
    //super(err);
    FskErrorMessages.resourceFileNotFoundWarning(fileName); 
    
  }
}
