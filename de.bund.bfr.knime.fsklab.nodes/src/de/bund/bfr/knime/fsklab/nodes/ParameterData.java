package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.metadata.swagger.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterData {



  private List<DataArray> parameters;

  public ParameterData() {

    this.parameters = new ArrayList<>();

  }

  public List<DataArray> getParameters() {
    return parameters;
  }

  public void setParameters(List<DataArray> parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates a DataArray containing parameter data and adds it to this ParameterData object.
   * 
   * @param parameter metadata {@link de.bund.bfr.metadata.swagger.Parameter}
   * @param modelId model id
   * @param data JSON data
   * @param parameterType data-type of parameter value
   */
  public void addParameter(Parameter parameter, String modelId, String data, String parameterType,
      String language) {
    DataArray arr =
        new DataArray(parameter, modelId, data.replaceAll("\\r?\\n", ""), parameterType, language);
    this.parameters.add(arr);
  }


}
