package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.ExecutionContext;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        String parameterType = scriptHandler.runScript("class(" + p.getId() + ")\n", exec, true)[0];
        String [] results = scriptHandler.runScript("toJSON(" + p.getId() + ", auto_unbox=TRUE)\n", exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p, modelId , data, parameterType);
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
        
        String parameterDataType = scriptHandler.runScript("class(" + p.getId() + ")\n", exec, true)[0];
        String [] results = scriptHandler.runScript("toJSON(" + p.getId() + ", auto_unbox=TRUE)\n", exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p, modelId , data, parameterDataType);
      } 
    }
    
    String path = workingDirectory.toString() + File.separator + JSON_FILE_NAME;
    MAPPER.writer().writeValue(new File(path), parameterJson);
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
    
    ParameterData parameterData = MAPPER.readValue(new File(parameterJson), ParameterData.class);
    String language = parameterData.getGeneratorLanguage();
    for(DataArray param : parameterData.getParameters()) {
      if(sourceParam.equals(param.getMetadata().getId())) {
        String type = param.getParameterType();
        String rawJsonData = "sourceParam <- fromJSON('"+ param.getData() +"', simplifyVector=FALSE)";
        scriptHandler.runScript(rawJsonData , exec, false);
        String data = convertRawJson("sourceParam", language, type);
        String script = targetParam + "<-" + data;
        scriptHandler.runScript(script, exec, false);
        scriptHandler.runScript("rm(sourceParam)", exec, false);
        
      }
    }
   

  }

  private String convertRawJson(String data, String language, String type) throws Exception {
    String converted ="";
    
    
    String sizes = scriptHandler.runScript("length(unique(lapply("+ data + ",length)))", exec, true)[0];
    String classes = scriptHandler.runScript("length(unique(lapply("+ data + ",class)))", exec, true)[0];
    if(type.equals("DataFrame") || type.equals("data.frame")) {
      if(language.startsWith("Python")) {
        //unlist columns to restore their original structure
        converted = "as.data.frame(lapply(lapply("+ data +",cbind),unlist))";
      } else {
        converted = "do.call(rbind.data.frame, " + data + ")";
        
      }
    } 
    else {
     
      if ((type.equals("ndarray") || type.equals("list")) && classes.equals("1") && sizes.equals("1")) {
        
        converted = "matrix(unlist(" + data + "), nrow=length(" + data + "))";
        
      } else {
        converted = data;
      }
    }
      
    return converted;
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
