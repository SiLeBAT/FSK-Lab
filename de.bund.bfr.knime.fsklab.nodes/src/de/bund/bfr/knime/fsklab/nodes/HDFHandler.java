package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.knime.core.node.ExecutionContext;
import org.osgi.framework.Bundle;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.metadata.swagger.Parameter;

/**
 * 
 * @author Thomas Schüler
 *
 * HDF5 is used to store model parameters and their values in a
 * standardized way in order to move parameters across
 * different script languages.
 * The HDFHandler provides methods for each supported script 
 * language to store parameters (input & ouput) in a hdf file.
 *
 */
public abstract class HDFHandler {

  // the hdf file where all model parameters are stored
  protected static final String HDF_FILE_NAME = "parameters.h5";  

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
  
  /**
   * Import the required hdf5 library into workspace using the runscript() metod
   * of the scriptHandler. Installs package if no library is available (R).
   * 
   * @throws Exception if an error occurs running the script.
   */
  protected abstract void importHDFLibraries() throws Exception;
  
  /**
   * Method to save input parameters in the hdf files. This needs
   * to be called before the execution of the model script and after
   * execution of the parameter script.
   * 
   * @param FSKPortObject fsk object containing the parameter names
   * @throws Exception if an error occurs running the script.
   */
  public abstract void saveInputParametersToHDF(FskPortObject fskObj)
      throws Exception;
  /**
   * Method to save output parameters in the hdf files. This needs
   * to be called after the execution of the model script.
   * 
   * @param FSKPortObject fsk object containing the parameter names
   * @throws Exception if an error occurs running the script.
   */
  public abstract void saveOutputParametersToHDF(FskPortObject fskObj)
      throws Exception;

  /**
   * Method to load input parameters into the workspace of the currently
   * active (to be executed) model.
   * 
   * Work in progress.
   * 
   * @param FSKPortObject fsk object containing the parameter names
   * @throws Exception if an error occurs running the script.
   */
  public abstract void loadInputParametersFromHDF(FskPortObject fskObj)
      throws Exception;
  
  
  /**
   * @return a string containing script code that adds variables (parameters)
   * to the hdf file in the corresponding script-language.
   * 
   * @param fskObj fsk object containing the parameter names
   * @param classification Parameter.ClassificationEnum either OUTPUT or INPUT 
   */
  protected abstract String compileListOfParameters(FskPortObject fskObj,
      Parameter.ClassificationEnum classification);
 
  /**
   * Creates and returns an HDFHandler instance of the correct language
   * (Python or R).
   * 
   * @param ScriptHandler language appropriate scripthandler 
   * @param ExecutionContext the context of the KNIME workflow
   * @throws Exception if an error occurs running the script.
   */

  public static HDFHandler createHandler(ScriptHandler scriptHandler, ExecutionContext exec)
      throws Exception {

    final HDFHandler handler;

    if (scriptHandler instanceof PythonScriptHandler) {
      handler = new PythonHDFHandler(scriptHandler, exec);
    } else return new RHDFHandler(scriptHandler, exec);
    
    return handler;
  }
  
  /**
   * 
   * @param path to the appropriate script file
   * @return appropriate R or Python script file (in /data) needed to load parameters
   *  from a hdf file into the current workspace
   * @throws IOException
   * @throws URISyntaxException
   */
  protected File getResource(final String path) throws IOException, URISyntaxException {
    Bundle bundle = Platform.getBundle("de.bund.bfr.knime.fsklab.nodes");
    URL fileURL = bundle.getEntry(path);
    URL resolvedFileURL = FileLocator.toFileURL(fileURL);
    URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
    return new File(resolvedURI);
  }
  
}

