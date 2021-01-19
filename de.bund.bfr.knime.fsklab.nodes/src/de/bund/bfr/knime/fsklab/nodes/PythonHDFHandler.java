package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.Platform;
import org.knime.core.node.ExecutionContext;
import org.osgi.framework.Bundle;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class PythonHDFHandler extends HDFHandler {

  
  public PythonHDFHandler(ScriptHandler scriptHandler, ExecutionContext exec) {
    super(scriptHandler, exec);
  }
  
  @Override
  protected void importHDFLibraries() throws Exception {
    
    // load h5py library
    scriptHandler.runScript("import h5py", exec, false);

  }
  
  @Override
  public void saveInputParametersToHDF(FskPortObject fskObj)
      throws Exception {
 // create script to store variables in hdf5 file
    String script = "with h5py.File('"+ HDF_FILE_NAME +"', 'w') as f:\n";
    script += compileListOfParameters(fskObj, Parameter.ClassificationEnum.INPUT);
   
    scriptHandler.runScript(script, exec, false);

  }

  @Override
  public void saveOutputParametersToHDF(FskPortObject fskObj)
      throws Exception {
    
    // create script to add variables to hdf5 file, 
    String script = "with h5py.File('"+ HDF_FILE_NAME +"', 'a') as f:\n";
    script += compileListOfParameters(fskObj, Parameter.ClassificationEnum.OUTPUT);
   
    scriptHandler.runScript(script, exec, false);
  }

  @Override
  protected
  String compileListOfParameters(FskPortObject fskObj, Parameter.ClassificationEnum classification) {
    String script = "";
    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : paras) {
      if (p.getClassification() == classification) {
        
        script += "\tf.create_dataset('output/" + p.getId() + "', data=" + p.getId() + ")\n";
        
      }
    }
    return script;
  }

  @Override
  public void loadInputParametersFromHDF(FskPortObject fskObj) throws Exception {
    
    String script = "";
    script += "with h5py.File('" + HDF_FILE_NAME + "', 'r') as f:\n";
    
    Bundle bundle = Platform.getBundle("de.bund.bfr.knime.fsklab.nodes");
    File file = getResource(bundle, "data/loadHDF.py");
    script += FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    
    scriptHandler.runScript(script, exec, false);
    
  }



}
