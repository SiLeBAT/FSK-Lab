package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import metadata.SwaggerUtil;
import org.knime.core.node.ExecutionContext;
import org.knime.core.util.FileUtil;

public class RJsonHandler extends JsonHandler {



  public RJsonHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    super(scriptHandler, exec);

  }

  @Override
  protected void importLibraries() throws Exception {
    try {
      scriptHandler.runScript("library(jsonlite)", exec, false);
    } catch (Exception e) {
    	e.printStackTrace();
//      scriptHandler.runScript("install.packages('jsonlite', type='source', dependencies=TRUE)",
//          exec, true);
//      scriptHandler.runScript("library(jsonlite)", exec, true);
    }
  }

  @Override
  public void saveInputParameters(FskPortObject fskObj) throws Exception {

    String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);
    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() != Parameter.ClassificationEnum.OUTPUT) {
        Boolean isDataFrame = scriptHandler.runScript("class(" + p.getId() + ")", exec, true)[0]
            .contains("data.frame");
        String parameterDataType = isDataFrame ? "DataFrame" : p.getDataType().getValue();

        String[] results =
            scriptHandler.runScript("toJSON(" + p.getId() + ", auto_unbox=TRUE)", exec, true);
        String data = (results != null) ? results[0] : "";

        parameterJson.addParameter(p,
            modelId,
            data,
            parameterDataType,
            SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata));
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

        // we need to differentiate between list and dataframe:
        Boolean isDataFrame = scriptHandler.runScript("class(" + p.getId() + ")", exec, true)[0]
            .contains("data.frame");
        String parameterDataType = isDataFrame ? "DataFrame" : p.getDataType().getValue();

        String[] results =
            scriptHandler.runScript("toJSON(" + p.getId() + ", auto_unbox=TRUE)", exec, true);
        String data = (results != null) ? results[0] : "";
        parameterJson.addParameter(p,
            modelId,
            data,
            parameterDataType,
            SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata));
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
  public void loadParametersIntoWorkspace(String parameterJson, String sourceParam,
      String targetParam) throws Exception {

    ParameterData parameterData = MAPPER.readValue(new File(parameterJson), ParameterData.class);
    
    for (DataArray param : parameterData.getParameters()) {
      if (sourceParam.equals(param.getMetadata().getId())) {
        String language = param.getGeneratorLanguage();
        String type = param.getParameterType();
        loadRawJsonData(param);
        String data = convertRawJson("sourceParam", language, type);
        String script = targetParam + "<-" + data;
        scriptHandler.runScript(script, exec, false);
        scriptHandler.runScript("rm(sourceParam)", exec, false);

      }
    }
  }


  // little helper method that checks if the data-string is small enough to
  // avoid creating a temporary data file for R
  private void loadRawJsonData(DataArray param) throws Exception {

    String rawJsonData = "";
    String type = param.getParameterType();
    if (param.getData().length() > 1000) {
      // store data in temp file because moving big arrays between controller and Java
      // doesn't seem to work properly

      File tempData = FileUtil.createTempFile("data", "json");

      MAPPER.writer().writeValue(tempData, param.getData());
      if (type.contains(DataTypeEnum.OBJECT.getValue()) || type.equals("DataFrame")) {
        rawJsonData = "sourceParam <- fromJSON(read_json('"
            + tempData.getAbsolutePath().replaceAll("\\\\", "/") + "'), simplifyVector=FALSE)";
      } else {
        rawJsonData = "sourceParam <- fromJSON(read_json('"
            + tempData.getAbsolutePath().replaceAll("\\\\", "/") + "'))";
      }
      scriptHandler.runScript(rawJsonData, exec, false);
      FileUtil.deleteRecursively(tempData);
    } else {
      if (type.contains(DataTypeEnum.OBJECT.getValue()) || type.equals("DataFrame")) {

        rawJsonData = "sourceParam <- fromJSON('" + param.getData() + "', simplifyVector=FALSE)";
      } else {
        rawJsonData = "sourceParam <- fromJSON('" + param.getData() + "')";;

      }
      scriptHandler.runScript(rawJsonData, exec, false);
    }



  }

  private String convertRawJson(String data, String language, String type) throws Exception {
    String converted = "";


    String sizes =
        scriptHandler.runScript("length(unique(lapply(" + data + ",length)))", exec, true)[0];
    String classes =
        scriptHandler.runScript("length(unique(lapply(" + data + ",class)))", exec, true)[0];
    if (type.equals("DataFrame")) {
      if (language.startsWith("Python")) {
        // unlist columns to restore their original structure
        converted = "as.data.frame(lapply(lapply(" + data + ",cbind),unlist))";
      } else {
        converted = "do.call(rbind.data.frame, " + data + ")";

      }
    } else {
      converted = data;

    }

    return converted;
  }

  @Override
  protected void addPathToFileParameter(String parameter, String path) throws Exception {
    scriptHandler.runScript(parameter + " <- paste('" + path + "' , " + parameter + ")", exec,
        false);

  }

  @Override
  protected void applyJoinCommand(String parameter, String command) throws Exception {
    scriptHandler.runScript(parameter + " <- " + command, exec, false);

  }
}
