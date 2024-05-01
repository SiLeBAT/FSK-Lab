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
import de.bund.bfr.metadata.swagger.Exposure;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * DoseResponseModelModelMath
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-04-28T11:01:15.480200615Z[Etc/UTC]")
public class DoseResponseModelModelMath {
  @SerializedName("fittingProcedure")
  private String fittingProcedure = null;

  @SerializedName("modelEquation")
  private List<ModelEquation> modelEquation = null;

  @SerializedName("exposure")
  private Exposure exposure = null;

  @SerializedName("parameter")
  private List<Parameter> parameter = new ArrayList<Parameter>();

  @SerializedName("qualityMeasures")
  private List<QualityMeasures> qualityMeasures = null;

  @SerializedName("event")
  private List<String> event = null;

  public DoseResponseModelModelMath fittingProcedure(String fittingProcedure) {
    this.fittingProcedure = fittingProcedure;
    return this;
  }

   /**
   * Procedure used to fit the data to the model equation
   * @return fittingProcedure
  **/
  @ApiModelProperty(value = "Procedure used to fit the data to the model equation")
  public String getFittingProcedure() {
    return fittingProcedure;
  }

  public void setFittingProcedure(String fittingProcedure) {
    this.fittingProcedure = fittingProcedure;
  }

  public DoseResponseModelModelMath modelEquation(List<ModelEquation> modelEquation) {
    this.modelEquation = modelEquation;
    return this;
  }

  public DoseResponseModelModelMath addModelEquationItem(ModelEquation modelEquationItem) {
    if (this.modelEquation == null) {
      this.modelEquation = new ArrayList<ModelEquation>();
    }
    this.modelEquation.add(modelEquationItem);
    return this;
  }

   /**
   * Get modelEquation
   * @return modelEquation
  **/
  @ApiModelProperty(value = "")
  public List<ModelEquation> getModelEquation() {
    return modelEquation;
  }

  public void setModelEquation(List<ModelEquation> modelEquation) {
    this.modelEquation = modelEquation;
  }

  public DoseResponseModelModelMath exposure(Exposure exposure) {
    this.exposure = exposure;
    return this;
  }

   /**
   * Get exposure
   * @return exposure
  **/
  @ApiModelProperty(value = "")
  public Exposure getExposure() {
    return exposure;
  }

  public void setExposure(Exposure exposure) {
    this.exposure = exposure;
  }

  public DoseResponseModelModelMath parameter(List<Parameter> parameter) {
    this.parameter = parameter;
    return this;
  }

  public DoseResponseModelModelMath addParameterItem(Parameter parameterItem) {
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

  public DoseResponseModelModelMath qualityMeasures(List<QualityMeasures> qualityMeasures) {
    this.qualityMeasures = qualityMeasures;
    return this;
  }

  public DoseResponseModelModelMath addQualityMeasuresItem(QualityMeasures qualityMeasuresItem) {
    if (this.qualityMeasures == null) {
      this.qualityMeasures = new ArrayList<QualityMeasures>();
    }
    this.qualityMeasures.add(qualityMeasuresItem);
    return this;
  }

   /**
   * Get qualityMeasures
   * @return qualityMeasures
  **/
  @ApiModelProperty(value = "")
  public List<QualityMeasures> getQualityMeasures() {
    return qualityMeasures;
  }

  public void setQualityMeasures(List<QualityMeasures> qualityMeasures) {
    this.qualityMeasures = qualityMeasures;
  }

  public DoseResponseModelModelMath event(List<String> event) {
    this.event = event;
    return this;
  }

  public DoseResponseModelModelMath addEventItem(String eventItem) {
    if (this.event == null) {
      this.event = new ArrayList<String>();
    }
    this.event.add(eventItem);
    return this;
  }

   /**
   * Definition of time-dependent parameter changes
   * @return event
  **/
  @ApiModelProperty(value = "Definition of time-dependent parameter changes")
  public List<String> getEvent() {
    return event;
  }

  public void setEvent(List<String> event) {
    this.event = event;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoseResponseModelModelMath doseResponseModelModelMath = (DoseResponseModelModelMath) o;
    return Objects.equals(this.fittingProcedure, doseResponseModelModelMath.fittingProcedure) &&
        Objects.equals(this.modelEquation, doseResponseModelModelMath.modelEquation) &&
        Objects.equals(this.exposure, doseResponseModelModelMath.exposure) &&
        Objects.equals(this.parameter, doseResponseModelModelMath.parameter) &&
        Objects.equals(this.qualityMeasures, doseResponseModelModelMath.qualityMeasures) &&
        Objects.equals(this.event, doseResponseModelModelMath.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fittingProcedure, modelEquation, exposure, parameter, qualityMeasures, event);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DoseResponseModelModelMath {\n");
    
    sb.append("    fittingProcedure: ").append(toIndentedString(fittingProcedure)).append("\n");
    sb.append("    modelEquation: ").append(toIndentedString(modelEquation)).append("\n");
    sb.append("    exposure: ").append(toIndentedString(exposure)).append("\n");
    sb.append("    parameter: ").append(toIndentedString(parameter)).append("\n");
    sb.append("    qualityMeasures: ").append(toIndentedString(qualityMeasures)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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
