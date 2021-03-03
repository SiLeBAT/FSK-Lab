package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import metadata.SwaggerUtil;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.ExecutionContext;

public class PythonJsonHandler extends JsonHandler {

  public PythonJsonHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    super(scriptHandler, exec);
    // TODO Auto-generated constructor stub
  }


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

   String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);

    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() != Parameter.ClassificationEnum.OUTPUT) {
        StringBuilder script = new StringBuilder();
        script.append("#JSON_PARAMETER_OUTUT\n");
        convertParamToJsonSerializable(p.getId(), Parameter.ClassificationEnum.INPUT);
        script.append("print(toSerializable)\n"); // important to get a return from controller
        String[] results = scriptHandler.runScript(script.toString(), exec, true);
        String data = (results != null) ? results[0] : "";
        
        Boolean isDataFrame = scriptHandler.runScript("print(type(" + p.getId() + ").__name__)", exec, true)[0]
            .contains("DataFrame");
        String parameterDataType = isDataFrame ? "DataFrame" : p.getDataType().getValue();
        
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
    // create script to store variables in hdf5 file

    String modelId = SwaggerUtil.getModelId(fskObj.modelMetadata);
    List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : parameters) {
      if (p.getClassification() == Parameter.ClassificationEnum.OUTPUT) {
        StringBuilder script = new StringBuilder();
        script.append("#JSON_PARAMETER_OUTUT\n");
        convertParamToJsonSerializable(p.getId(), Parameter.ClassificationEnum.OUTPUT);
        script.append("print(toSerializable)"); // important to get a return from controller
        String[] results = scriptHandler.runScript(script.toString(), exec, true);
        String data = (results != null) ? results[0] : "";
        Boolean isDataFrame = scriptHandler.runScript("print(type(" + p.getId() + ").__name__)", exec, true)[0]
            .contains("DataFrame");
        String parameterDataType = isDataFrame ? "DataFrame" : p.getDataType().getValue();
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

    // StringBuilder script = new StringBuilder();

    // load source and target into workspace as strings
    ParameterData parameterData = MAPPER.readValue(new File(parameterJson), ParameterData.class);
    
    for (DataArray param : parameterData.getParameters()) {
      if (sourceParam.equals(param.getMetadata().getId())) {
        String language = param.getGeneratorLanguage();
        String type = param.getParameterType();
        String rawJsonData = "sourceParam = json.loads('" + param.getData() + "')";
        scriptHandler.runScript(rawJsonData, exec, false);
        String data = convertRawJson("sourceParam", language, type);
        String script = targetParam + "=" + data;
        scriptHandler.runScript(script, exec, false);
        scriptHandler.runScript("del sourceParam", exec, false);

      }
    }
  }

  private String convertRawJson(String data, String language, String type) throws Exception {
    String converted = "";

    if (type.equals("DataFrame") ) {

      converted = "pandas.DataFrame.from_records(" + data + ")";

    } else {
      converted = data;
    }
    return converted;
  }



  private void convertParamToJsonSerializable(String parameter, ClassificationEnum classification)
      throws Exception {
    StringBuilder script = new StringBuilder();

    if (!classification.equals(Parameter.ClassificationEnum.OUTPUT)) {
      // deep copy input parameters in case their values change during script execution
      script.append("toSerializable = copy.deepcopy(" + parameter + ")\n"); // deep copy is
                                                                            // expensive, so only do
                                                                            // it for input
                                                                            // parameters
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
    scriptHandler.runScript(parameter + " = '" + path + "' + " + parameter, exec, false);
  }


  @Override
  protected void applyJoinCommand(String parameter, String command) throws Exception {
    scriptHandler.runScript(parameter + " = " + command, exec, false);

  }



}
