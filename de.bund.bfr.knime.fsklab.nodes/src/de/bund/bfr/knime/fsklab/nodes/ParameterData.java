package de.bund.bfr.knime.fsklab.nodes;

import java.util.ArrayList;
import java.util.List;
import de.bund.bfr.metadata.swagger.Parameter;

public class ParameterData {

  private String generatorLanguage;

  private List<Parameter> parameters;
  
  public ParameterData(String language) {
    setGeneratorLanguage(language);
    parameters = new ArrayList<Parameter>();
  }
  public String getGeneratorLanguage() {
    return generatorLanguage;
  }

  public void setGeneratorLanguage(String generatorLanguage) {
    this.generatorLanguage = generatorLanguage;
  }
  public List<Parameter> getParameters() {
    return parameters;
  }
  public void addParameters(Parameter parameters) {
    this.parameters.add(parameters);
  }
  
  
  
}
