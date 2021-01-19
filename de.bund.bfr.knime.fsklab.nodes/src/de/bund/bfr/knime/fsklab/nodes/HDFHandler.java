package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.knime.core.node.ExecutionContext;
import org.osgi.framework.Bundle;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.metadata.swagger.Parameter;

public abstract class HDFHandler {

  public static String HDF_FILE_NAME = "parameters.h5";  

  protected ScriptHandler scriptHandler;
  protected ExecutionContext exec;
  
  public HDFHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    this.scriptHandler = scriptHandler;
    this.exec = exec;
    
    try {
      importHDFLibraries();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  protected abstract void importHDFLibraries() throws Exception;
  
  public abstract void saveInputParametersToHDF(FskPortObject fskObj)
      throws Exception;
  
  public abstract void saveOutputParametersToHDF(FskPortObject fskObj)
      throws Exception;

  public abstract void loadInputParametersFromHDF(FskPortObject fskObj)
      throws Exception;
  
  
  
  protected abstract String compileListOfParameters(FskPortObject fskObj, Parameter.ClassificationEnum classification);
 
  public static HDFHandler createHandler(ScriptHandler scriptHandler, ExecutionContext exec)
      throws Exception {

    final HDFHandler handler;

    if (scriptHandler instanceof PythonScriptHandler) {
      handler = new PythonHDFHandler(scriptHandler, exec);
    } else return new RHDFHandler(scriptHandler, exec);
    
    return handler;
  }
  
  protected File getResource(final Bundle bundle, final String path) throws IOException, URISyntaxException {
    URL fileURL = bundle.getEntry(path);
    URL resolvedFileURL = FileLocator.toFileURL(fileURL);
    URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
    return new File(resolvedURI);
}
  
}

