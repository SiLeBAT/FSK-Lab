/*
 * 
 * 
 *
 * 
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package de.bund.bfr.metadata.swagger;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.bund.bfr.metadata.swagger.Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * DataModelModelMath
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-04-28T11:01:15.480200615Z[Etc/UTC]")
public class DataModelModelMath {
  @SerializedName("parameter")
  private List<Parameter> parameter = new ArrayList<Parameter>();

  public DataModelModelMath parameter(List<Parameter> parameter) {
    this.parameter = parameter;
    return this;
  }

  public DataModelModelMath addParameterItem(Parameter parameterItem) {
    this.parameter.add(parameterItem);
    return this;
  }

   /**
   * Get parameter
   * @return parameter
  **/
  @ApiModelProperty(required = true, value = "")
  public List<Parameter> getParameter() {
    return parameter;
  }

  public void setParameter(List<Parameter> parameter) {
    this.parameter = parameter;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataModelModelMath dataModelModelMath = (DataModelModelMath) o;
    return Objects.equals(this.parameter, dataModelModelMath.parameter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameter);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataModelModelMath {\n");
    
    sb.append("    parameter: ").append(toIndentedString(parameter)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
