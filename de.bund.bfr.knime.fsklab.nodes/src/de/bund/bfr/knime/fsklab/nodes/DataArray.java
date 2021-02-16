package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.metadata.swagger.Parameter;

public class DataArray {
  
  
  private final Parameter metadata;
  private final String data;
  private final String modelId;
  
  
  public DataArray(Parameter parameter,String modelId,  String data) {
    metadata = parameter;
    this.data = data;
    this.modelId = modelId;
  }
  public Parameter getMetadata() {
    return metadata;
  }
  public String getData() {
    return data;
  }
  
  public String getModelId() {
    return modelId;
  }
  
  
  
}
