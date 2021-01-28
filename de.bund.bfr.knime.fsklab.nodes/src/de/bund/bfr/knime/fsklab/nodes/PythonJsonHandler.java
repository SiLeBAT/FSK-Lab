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

public class PythonJsonHandler extends HDFHandler {

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
    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : paras) {
      if (p.getClassification() == Parameter.ClassificationEnum.INPUT) {
        
        try {
          convertParamToJsonSerializable(p.getId(), Parameter.ClassificationEnum.INPUT);
          scriptHandler.runScript(JSON_PARAMETERS_NAME + "['" + p.getId() + "'] = toSerializable\n", exec, false);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    
  }

  @Override
  public void saveOutputParametersToHDF(FskPortObject fskObj) throws Exception {
    // create script to store variables in hdf5 file
    

    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : paras) {
      if (p.getClassification() == Parameter.ClassificationEnum.OUTPUT) {
        
        try {
          convertParamToJsonSerializable(p.getId(), Parameter.ClassificationEnum.OUTPUT);
          scriptHandler.runScript(JSON_PARAMETERS_NAME + "['" + p.getId() + "'] = toSerializable\n", exec, false);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    StringBuilder script = new StringBuilder();
    script.append("with open('" + HDF_FILE_NAME +"', 'w') as outfile:\n");
    script.append("\tjson.dump(" + JSON_PARAMETERS_NAME + ", outfile)\n");
    scriptHandler.runScript(script.toString(), exec, false);
  }

  @Override
  public void loadInputParametersFromHDF(FskPortObject fskObj) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  protected String compileListOfParameters(FskPortObject fskObj,
      ClassificationEnum classification) {
    
    StringBuilder script = new StringBuilder();
    
   
    return script.toString();
  }
  
  private void convertParamToJsonSerializable(String parameter, ClassificationEnum classification) throws Exception {
    StringBuilder script = new StringBuilder();
  
    if (classification.equals(Parameter.ClassificationEnum.INPUT)) {
      script.append("toSerializable = copy.deepcopy(" + parameter + ")\n"); // deep copy is expensive, so only do it for input parameters  
    } else {
      script.append("toSerializable = " + parameter + "\n");
    }
    
    File file = getResource("data/convertToJsonSerializable.py");
    script.append(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    
    scriptHandler.runScript(script.toString(), exec, false);
    
  }
  
  

}
