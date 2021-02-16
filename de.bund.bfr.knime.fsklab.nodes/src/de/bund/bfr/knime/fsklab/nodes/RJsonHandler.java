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

public class RJsonHandler extends JsonHandler {

  
  
  public RJsonHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    super(scriptHandler, exec);
   
  }

  @Override
  protected void importLibraries() throws Exception {
    try {
      scriptHandler.runScript("library(jsonlite)", exec, true);
    } catch (Exception e) {
      scriptHandler.runScript("install.packages('jsonlite', type='source', dependencies=TRUE)", exec, true);
      scriptHandler.runScript("library(jsonlite)", exec, true);
    }
  }

  @Override
  public void saveInputParameters(FskPortObject fskObj) throws Exception {
  
    parameterJson.setGeneratorLanguage(SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata));
    
    String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);
    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() != Parameter.ClassificationEnum.OUTPUT) {
        
        String [] results = scriptHandler.runScript("toJSON(" + p.getId() + ", auto_unbox=TRUE)\n", exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p, modelId , data);
      } 
    }
    
  }

  @Override
  public void saveOutputParameters(FskPortObject fskObj, Path workingDirectory) throws Exception {
    StringBuilder script = new StringBuilder();
    
    
    String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);
    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() == Parameter.ClassificationEnum.OUTPUT) {
        script.append("toJSON(" + p.getId() + ", auto_unbox=TRUE)\n");
        String [] results = scriptHandler.runScript("toJSON(" + p.getId() + ", auto_unbox=TRUE)\n", exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p, modelId , data);
      } 
    }
    
    String path = workingDirectory.toString() + File.separator + JSON_FILE_NAME;
    MAPPER.writeValue(new File(path), parameterJson);
  }

  
  @Override
  protected
  String compileListOfParameters(FskPortObject fskObj, ClassificationEnum classification) {
    StringBuilder script = new StringBuilder();

    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : paras) {
      if (p.getClassification() == classification) {
        script.append(JSON_PARAMETERS_NAME + "$" + p.getId() + " <- "+ p.getId() + "\n");
      }
    }
    
    return script.toString();  
  }
  
  private String addTypesToJson(FskPortObject fskObj){
    StringBuilder script = new StringBuilder();

    script.append("var_types = list();\n");
    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    
    for (Parameter p : paras) {
      
      // because of auto_unboxing, we need to crate a var_types dict
      script.append("var_types$" + p.getId() + " <- class(" + p.getId() + ")\n");
    }

    script.append(JSON_PARAMETERS_NAME + "$var_types = var_types\n");
    return script.toString();

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
    
    //TODO: figure out if libraries are loaded at this point
    StringBuilder script = new StringBuilder();
    script.append("json_params <- read_json( '" + parameterJson + "', simplifyVector = FALSE)\n");
    script.append("sourceParam <- '" + sourceParam + "'\n");
    script.append("targetParam <- '" + targetParam + "'\n");
        
    File file = getResource("data/loadJsonIntoR.r");
    script.append(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    
    
    
    scriptHandler.runScript(script.toString(), exec, false);

  }

  @Override
  protected void addPathToFileParameter(String parameter, String path) throws Exception {
    // parameter <- paste('/path/to/resource/', parameter)
    scriptHandler.runScript(parameter + " <- paste('" + path + "' , " + parameter + ")" , exec, false);
    
  }

  @Override
  protected void applyJoinCommand(String parameter, String command) throws Exception {
    scriptHandler.runScript(parameter + " <- " + command , exec, false);
    
  }
}
