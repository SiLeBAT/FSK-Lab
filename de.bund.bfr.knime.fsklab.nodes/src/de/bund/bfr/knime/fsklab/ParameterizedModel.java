package de.bund.bfr.knime.fsklab;

import java.util.List;
import de.bund.bfr.knime.fsklab.rakip.Parameter;

public class ParameterizedModel {
  private String modelID;
  private List<Parameter> listOfParameter ;
  
  public ParameterizedModel(String modelID, List<Parameter> listOfParameter) {
    super();
    this.modelID = modelID;
    this.listOfParameter = listOfParameter;
  }
  
  public String getModelID() {
    return modelID;
  }
  public void setModelID(String modelID) {
    this.modelID = modelID;
  }
  public List<Parameter> getListOfParameter() {
    return listOfParameter;
  }
  public void setListOfParameter(List<Parameter> listOfParameter) {
    this.listOfParameter = listOfParameter;
  }
  
  
}
