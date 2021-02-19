package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.metadata.swagger.Parameter;

public class DataArray {


  private final Parameter metadata;
  private final String data;
  private final String modelId;
  private final String parameterType; // this will be removed when FSK-ML 5 is out

  /**
   * dddd
   */
  public DataArray() {
    metadata = new Parameter();
    data = "";
    modelId = "";
    parameterType = "";
  }

  /**
   * 
   * @param parameter
   * @param modelId
   * @param data
   * @param type
   */
  public DataArray(Parameter parameter, String modelId, String data, String type) {
    metadata = parameter;
    this.data = data;
    this.modelId = modelId;
    this.parameterType = type;
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

  public String getParameterType() {
    return parameterType;
  }



}
