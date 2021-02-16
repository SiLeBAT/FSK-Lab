package de.bund.bfr.knime.fsklab.nodes;

import java.util.ArrayList;
import java.util.List;
import de.bund.bfr.metadata.swagger.Parameter;

public class ParameterData {

  private String generatorLanguage;

  private List<DataArray> parameters;
  
  public ParameterData() {
    
    this.parameters = new ArrayList<DataArray>();
    
  }
  public String getGeneratorLanguage() {
    return generatorLanguage;
  }

  public void setGeneratorLanguage(String generatorLanguage) {
    this.generatorLanguage = generatorLanguage;
  }
  public List<DataArray> getParameters() {
    return parameters;
  }
  public void setParameters(List<DataArray> parameters) {
    this.parameters = parameters;
  }
  
  public void addParameter(Parameter parameter, String modelId, String data) {
    DataArray arr = new DataArray(parameter, modelId, data);
    this.parameters.add(arr);
  }
  
  
}
