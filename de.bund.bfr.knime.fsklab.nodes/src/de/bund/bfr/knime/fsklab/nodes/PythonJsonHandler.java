package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.ExecutionContext;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

public class PythonJsonHandler extends JsonHandler {

  public PythonJsonHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    super(scriptHandler, exec);
    // TODO Auto-generated constructor stub
  }

  
  // 
  @Override
  protected void importHDFLibraries() throws Exception {
    // load h5py library
    scriptHandler.runScript("import pandas", exec, false);
    scriptHandler.runScript("import copy", exec, false);
    scriptHandler.runScript("import json", exec, false);
    scriptHandler.runScript("import numpy", exec, false);
  }

  @Override
  public void saveInputParametersToHDF(FskPortObject fskObj) throws Exception {
    // create script to store variables in hdf5 file

    scriptHandler.runScript(JSON_PARAMETERS_NAME + " = {}\n", exec, false);
    
    // set script language
    scriptHandler.runScript(JSON_PARAMETERS_NAME + "['script_language'] = 'Python'\n", exec, false);
    scriptHandler.runScript(JSON_PARAMETERS_NAME + "['var_types'] = {}\n", exec, false);
    compileListOfParameters(fskObj, Parameter.ClassificationEnum.INPUT);
    
  }

  @Override
  public void saveOutputParametersToHDF(FskPortObject fskObj) throws Exception {
    // create script to store variables in hdf5 file
    

    compileListOfParameters(fskObj, Parameter.ClassificationEnum.OUTPUT);
    StringBuilder script = new StringBuilder();
    script.append("with open('" + JSON_FILE_NAME +"', 'w') as outfile:\n");
    script.append("\tjson.dump(" + JSON_PARAMETERS_NAME + ", outfile)\n");
    scriptHandler.runScript(script.toString(), exec, false);
  }

  @Override
  public void loadInputParametersFromHDF(FskPortObject fskObj) throws Exception {
    // TODO THIS IS DUMMY, REMOVE OR RENAME
    loadJsonParameterIntoWorkspace("my_source","my_target");

  }
  
  /**
   * 
   * @param sourceParam : parameter of first script 
   * @param targetParam : parameter of second script to be overwritten
   * @throws Exception
   */
  public void loadJsonParameterIntoWorkspace(String sourceParam, String targetParam) throws Exception {
    
    StringBuilder script = new StringBuilder();
    
    //load source and target into workspace as strings
    script.append("sourceParam = " + sourceParam + "\n");
    script.append("targetParam = " + targetParam + "\n");
    script.append("JSON_FILE_NAME = " + JSON_FILE_NAME + "\n");
    // load JSON file into json_params
    File file = getResource("data/loadJsonIntoPython.py");
    script.append(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    
    script.append("del json_params\n"); // delete JSON to free up space
    scriptHandler.runScript(script.toString(), exec, false);
    
  }
  @Override
  protected String compileListOfParameters(FskPortObject fskObj,
      ClassificationEnum classification) {
    
    StringBuilder script = new StringBuilder();
    
    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : paras) {
      if (p.getClassification() == classification) {
        
        try {
          convertParamToJsonSerializable(p.getId(), classification);
          scriptHandler.runScript(JSON_PARAMETERS_NAME + "['" + p.getId() + "'] = toSerializable\n", exec, false);
          
          // save type of parameter
          scriptHandler.runScript(JSON_PARAMETERS_NAME + "['var_types']['" + p.getId() + "'] = type(" + p.getId() + ").__name__\n", exec, false);
         
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    
    return script.toString();
  }
  
  private void convertParamToJsonSerializable(String parameter, ClassificationEnum classification) throws Exception {
    StringBuilder script = new StringBuilder();
  
    if (classification.equals(Parameter.ClassificationEnum.INPUT)) {
      // deep copy input parameters in case their values change during script execution
      script.append("toSerializable = copy.deepcopy(" + parameter + ")\n"); // deep copy is expensive, so only do it for input parameters  
    } else {
      script.append("toSerializable = " + parameter + "\n");
    }
    script.append("toSerializable = " + parameter + "\n");
    File file = getResource("data/convertToJsonSerializable.py");
    script.append(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    
    scriptHandler.runScript(script.toString(), exec, false);
    
  }
  
  

}
