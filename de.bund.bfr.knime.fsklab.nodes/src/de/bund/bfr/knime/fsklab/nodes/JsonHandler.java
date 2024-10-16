package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.knime.core.node.ExecutionContext;
import org.osgi.framework.Bundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.PackageNotFoundException;
import de.bund.bfr.knime.fsklab.ParameterJsonConversionException;
import de.bund.bfr.knime.fsklab.VariableNotGlobalException;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelationAdvanced;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

/**
 * 
 * @author Thomas Schüler
 *
 *         HDF5 is used to store model parameters and their values in a standardized way in order to
 *         move parameters across different script languages. The HDFHandler provides methods for
 *         each supported script language to store parameters (input & ouput) in a hdf file.
 *
 */
public abstract class JsonHandler {

  // the hdf file where all model parameters are stored
  public static final String JSON_FILE_NAME = "parameters.json";
  protected static final String JSON_PARAMETERS_NAME = "fsk_parameters";

  protected ScriptHandler scriptHandler;
  protected ExecutionContext exec;
  protected ParameterJson parameterJson;
  protected static ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;//new ObjectMapper();

  protected JsonHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    this.scriptHandler = scriptHandler;
    this.exec = exec;
    
    try {
      importLibraries();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  // Method loads parameter data from previous models into workspace of current model
  // according to the list of Joiner-Relations
  public void applyJoinRelation(FskPortObject fskObj, List<JoinRelationAdvanced> joinRelationList,
      String suffix) throws Exception {

    // figure out which parameters from JoinRelations belong to which model to avoid loading
    // a json file twice
    if (joinRelationList != null) {
      // get all relevant parameters -> assign to Map with key=jsonPath
      Map<String, List<JoinerObject>> sourceTargetPathMap = new HashMap<String, List<JoinerObject>>();
      for (JoinRelationAdvanced joinRelation : joinRelationList) {
        if(SwaggerUtil.getParameter(fskObj.modelMetadata) != null) {
          for (Parameter param : SwaggerUtil.getParameter(fskObj.modelMetadata)) {
            if (joinRelation.getTargetParam().equals(param.getId() + suffix)) {
              addEntryToRelationMap(sourceTargetPathMap,joinRelation, param );
    
            }
          }
        }
      }// for joinRelation

//TODO: check what happens if sourceTargetPathMap is empty
      // work through Map, each entry is a parameters.json file, load data into workspace
      for (Map.Entry<String, List<JoinerObject>> entry : sourceTargetPathMap.entrySet()) {
        //ParameterData parameterData = MAPPER.readValue(new File(jsonPath), ParameterData.class);
        //ParameterData parameterData = MAPPER.readValue(new FileInputStream(entry.getKey()), ParameterData.class);
        ParameterJson parameterJson = new ParameterJson(new File(entry.getKey()));
        for (JoinerObject obj : entry.getValue()) {
          loadParametersIntoWorkspace(parameterJson, obj.sourceParameter, obj.targetParameter.getId());

          // if target parameter is of type FILE, add path to generatedResources to sourceParam
          // This should be safe since source and target parameter must have the same type (if
          // File)
          if (obj.targetParameter.getDataType().equals(Parameter.DataTypeEnum.FILE)) {
            addPathToFileParameter(obj.targetParameter.getId(), obj.resourcePath);
          }

          applyJoinCommand(obj.targetParameter.getId(), obj.joinRelation.replaceCommand(obj.targetParameter.getId()));
        }

      }

    }// if     
  }

  // adds jsonPath with corresponding joinReleation information to map
  private void addEntryToRelationMap(Map<String, List<JoinerObject>> sourceTargetPathMap,
      JoinRelationAdvanced joinRelation, Parameter param) {
    if (joinRelation.getModel().getGeneratedResourcesDirectory().isPresent()) {
      String resourcePath = joinRelation.getModel().getGeneratedResourcesDirectory().get()
          .getAbsolutePath().replaceAll("\\\\", "/") + "/";
      String jsonPath = resourcePath + JSON_FILE_NAME;
      if(!sourceTargetPathMap.containsKey(jsonPath)) {
        sourceTargetPathMap.put(jsonPath, new ArrayList<JoinerObject>());
      }

      sourceTargetPathMap.get(jsonPath).add(new JoinerObject(joinRelation.getSourceParam(),param,joinRelation, resourcePath));

    }
  }

  
  // helper class to keep track of which list of Joiner-Relations is related to which parameter.json file
  private class JoinerObject{
    public String sourceParameter;
    public Parameter targetParameter;
    public JoinRelationAdvanced joinRelation;
    public String resourcePath; // path to parameter.json file
    public JoinerObject(String source, Parameter target, JoinRelationAdvanced relation, String path) {
      sourceParameter = source;
      targetParameter = target;
      joinRelation = relation;
      resourcePath = path;
    }
  }
  
  protected abstract void applyJoinCommand(String parameter, String command) throws Exception;

  /**
   * Import the required hdf5 library into workspace using the runscript() metod of the
   * scriptHandler. Installs package if no library is available (R).
   * 
   * @throws Exception if an error occurs running the script.
   */
  protected abstract void importLibraries() throws PackageNotFoundException;

  /**
   * Method to save input parameters in the hdf files. This needs to be called before the execution
   * of the model script and after execution of the parameter script.
   * 
   * @param FSKPortObject fsk object containing the parameter names
   * @throws Exception if an error occurs running the script.
   */
  public abstract void saveInputParameters(FskPortObject fskObj, Path workingDirectory) throws Exception;

  /**
   * Method to save output parameters in the hdf files. This needs to be called after the execution
   * of the model script.
   * 
   * @param FSKPortObject fsk object containing the parameter names
   * @throws Exception if an error occurs running the script.
   */
  public abstract void saveOutputParameters(FskPortObject fskObj, Path workingDirectory)
      throws VariableNotGlobalException, IOException, ParameterJsonConversionException,
      URISyntaxException;

  /**
   * Method to load input parameters into the workspace of the currently active (to be executed)
   * model.
   * 
   * Work in progress.
   * 
   * @param FSKPortObject fsk object containing the parameter names
   * @throws Exception if an error occurs running the script.
   */
  public abstract void loadParametersIntoWorkspace(ParameterJson parameterJson, String sourceParam,
      String targetParam) throws Exception;


  /**
   * Creates and returns an HDFHandler instance of the correct language (Python or R).
   * 
   * @param ScriptHandler language appropriate scripthandler
   * @param ExecutionContext the context of the KNIME workflow
   * @throws Exception if an error occurs running the script.
   */

  public static JsonHandler createHandler(ScriptHandler scriptHandler, ExecutionContext exec)
      throws Exception {

    final JsonHandler handler;

    if (scriptHandler instanceof PythonScriptHandler) {
      handler = new PythonJsonHandler(scriptHandler, exec);
    } else {
      return new RJsonHandler(scriptHandler, exec);
    }

    return handler;
  }

  /**
   * If a FILE parameter is overwritten with another value (file) because of joining, the new file
   * will be located in the generatedResource folder of the other model. This method concatenates
   * the path to the parameter value.
   * 
   * Assume, inFile <- "myFile.csv" will be overwritten with "fileOfModel1.csv", then: inFile <-
   * "fileOfModel1.csv" will become inFile <- "/path/to/generatedResourceOfModel1/fileOfModel1.csv"
   * 
   * 
   * @param parameter
   * @param path
   * @throws Exception
   */
  protected abstract void addPathToFileParameter(String parameter, String path) throws Exception;

  /**
   * 
   * @param path to the appropriate script file
   * @return appropriate R or Python script file (in /data) needed to load parameters from a hdf
   *         file into the current workspace
   * @throws IOException
   * @throws URISyntaxException
   */
  protected File getResource(final String path) throws IOException, URISyntaxException {
    Bundle bundle = Platform.getBundle("de.bund.bfr.knime.fsklab.nodes");
    URL fileUrl = bundle.getEntry(path);
    URL resolvedFileUrl = FileLocator.toFileURL(fileUrl);
    URI resolvedUri = new URI(resolvedFileUrl.getProtocol(), resolvedFileUrl.getPath(), null);
    return new File(resolvedUri);
  }

}

