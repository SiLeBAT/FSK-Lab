package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
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
  protected void importLibraries() throws Exception {
    // load h5py library
    scriptHandler.runScript("import pandas", exec, false);
    scriptHandler.runScript("import copy", exec, false);
    scriptHandler.runScript("import json", exec, false);
    scriptHandler.runScript("import numpy", exec, false);
  }

  @Override
  public void saveInputParameters(FskPortObject fskObj) throws Exception {
    // create script to store variables in hdf5 file

    
    parameterJson.setGeneratorLanguage(SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata));
    
    String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);
    
    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() != Parameter.ClassificationEnum.OUTPUT) {
        StringBuilder script = new StringBuilder();
        script.append("#JSON_PARAMETER_OUTUT\n");
        convertParamToJsonSerializable(p.getId(), Parameter.ClassificationEnum.INPUT);
        script.append("print(toSerializable)\n");
        String [] results = scriptHandler.runScript(script.toString(), exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p, modelId , data);
      } 
    }
    
  }

  @Override
  public void saveOutputParameters(FskPortObject fskObj, Path workingDirectory) throws Exception {
    // create script to store variables in hdf5 file
    
    String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);
    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() == Parameter.ClassificationEnum.OUTPUT) {
        StringBuilder script = new StringBuilder();
        script.append("#JSON_PARAMETER_OUTUT\n");
        convertParamToJsonSerializable(p.getId(), Parameter.ClassificationEnum.OUTPUT);
        script.append("print(toSerializable)\n");
        String [] results = scriptHandler.runScript(script.toString(), exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p, modelId , data);
      } 
    }
    String path = workingDirectory.toString() + File.separator + JSON_FILE_NAME;
    MAPPER.writeValue(new File(path), parameterJson);
  }


  
  /**
   * 
   * @param sourceParam : parameter of first script 
   * @param targetParam : parameter of second script to be overwritten
   * @throws Exception
   */
  @Override
  public void loadParametersIntoWorkspace(String parameterJson, 
      String sourceParam, String targetParam) throws Exception {
    
    StringBuilder script = new StringBuilder();
    
    //load source and target into workspace as strings
    script.append("sourceParam = '" + sourceParam + "'\n");
    script.append("targetParam = '" + targetParam + "'\n");
    script.append("JSON_FILE_NAME = '" + parameterJson + "'\n");
    // load JSON file into json_params
    File file = getResource("data/loadJsonIntoPython.py");
    script.append(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    
    
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
  
    if (!classification.equals(Parameter.ClassificationEnum.OUTPUT)) {
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


  @Override
  protected void addPathToFileParameter(String parameter, String path) throws Exception {
    // parameter = 'path/to/resource' + parameter
    scriptHandler.runScript(parameter + " = '" + path + "' + " + parameter , exec, false);
  }


  @Override
  protected void applyJoinCommand(String parameter, String command) throws Exception {
    scriptHandler.runScript(parameter + " = " + command , exec, false);
    
  }
  
  

}
