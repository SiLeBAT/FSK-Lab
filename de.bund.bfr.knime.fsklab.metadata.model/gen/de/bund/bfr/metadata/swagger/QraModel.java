/*
 * RAKIP Generic model
 * TODO
 *
 * OpenAPI spec version: 1.0.4
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package de.bund.bfr.metadata.swagger;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * QraModel
 */
@jakarta.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class QraModel extends Model {
  @SerializedName("generalInformation")
  private PredictiveModelGeneralInformation generalInformation = null;

  @SerializedName("scope")
  private ExposureModelScope scope = null;

  @SerializedName("dataBackground")
  private GenericModelDataBackground dataBackground = null;

  @SerializedName("modelMath")
  private GenericModelModelMath modelMath = null;

  public QraModel generalInformation(PredictiveModelGeneralInformation generalInformation) {
    this.generalInformation = generalInformation;
    return this;
  }

   /**
   * Get generalInformation
   * @return generalInformation
  **/
  @ApiModelProperty(value = "")
  public PredictiveModelGeneralInformation getGeneralInformation() {
    return generalInformation;
  }

  public void setGeneralInformation(PredictiveModelGeneralInformation generalInformation) {
    this.generalInformation = generalInformation;
  }

  public QraModel scope(ExposureModelScope scope) {
    this.scope = scope;
    return this;
  }

   /**
   * Get scope
   * @return scope
  **/
  @ApiModelProperty(value = "")
  public ExposureModelScope getScope() {
    return scope;
  }

  public void setScope(ExposureModelScope scope) {
    this.scope = scope;
  }

  public QraModel dataBackground(GenericModelDataBackground dataBackground) {
    this.dataBackground = dataBackground;
    return this;
  }

   /**
   * Get dataBackground
   * @return dataBackground
  **/
  @ApiModelProperty(value = "")
  public GenericModelDataBackground getDataBackground() {
    return dataBackground;
  }

  public void setDataBackground(GenericModelDataBackground dataBackground) {
    this.dataBackground = dataBackground;
  }

  public QraModel modelMath(GenericModelModelMath modelMath) {
    this.modelMath = modelMath;
    return this;
  }

   /**
   * Get modelMath
   * @return modelMath
  **/
  @ApiModelProperty(value = "")
  public GenericModelModelMath getModelMath() {
    return modelMath;
  }

  public void setModelMath(GenericModelModelMath modelMath) {
    this.modelMath = modelMath;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QraModel qraModel = (QraModel) o;
    return Objects.equals(this.generalInformation, qraModel.generalInformation) &&
        Objects.equals(this.scope, qraModel.scope) &&
        Objects.equals(this.dataBackground, qraModel.dataBackground) &&
        Objects.equals(this.modelMath, qraModel.modelMath) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generalInformation, scope, dataBackground, modelMath, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QraModel {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    generalInformation: ").append(toIndentedString(generalInformation)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    dataBackground: ").append(toIndentedString(dataBackground)).append("\n");
    sb.append("    modelMath: ").append(toIndentedString(modelMath)).append("\n");
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

