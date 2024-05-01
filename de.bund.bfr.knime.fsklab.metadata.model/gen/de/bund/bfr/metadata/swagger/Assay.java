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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
/**
 * Assay
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-04-28T11:01:15.480200615Z[Etc/UTC]")
public class Assay {
  @SerializedName("contaminationRange")
  private String contaminationRange = null;

  @SerializedName("uncertaintyValue")
  private String uncertaintyValue = null;

  @SerializedName("fatPercentage")
  private String fatPercentage = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("moisturePercentage")
  private String moisturePercentage = null;

  @SerializedName("leftCensoredData")
  private String leftCensoredData = null;

  @SerializedName("detectionLimit")
  private String detectionLimit = null;

  @SerializedName("quantificationLimit")
  private String quantificationLimit = null;

  public Assay contaminationRange(String contaminationRange) {
    this.contaminationRange = contaminationRange;
    return this;
  }

   /**
   * Range of result of the analytical measure reported in the unit specified by the variable Hazard unit
   * @return contaminationRange
  **/
  @ApiModelProperty(value = "Range of result of the analytical measure reported in the unit specified by the variable Hazard unit")
  public String getContaminationRange() {
    return contaminationRange;
  }

  public void setContaminationRange(String contaminationRange) {
    this.contaminationRange = contaminationRange;
  }

  public Assay uncertaintyValue(String uncertaintyValue) {
    this.uncertaintyValue = uncertaintyValue;
    return this;
  }

   /**
   * Indicate the expanded uncertainty (usually 95% confidence interval) value associated with the measurement expressed in the unit reported in the field Hazard unit
   * @return uncertaintyValue
  **/
  @ApiModelProperty(value = "Indicate the expanded uncertainty (usually 95% confidence interval) value associated with the measurement expressed in the unit reported in the field Hazard unit")
  public String getUncertaintyValue() {
    return uncertaintyValue;
  }

  public void setUncertaintyValue(String uncertaintyValue) {
    this.uncertaintyValue = uncertaintyValue;
  }

  public Assay fatPercentage(String fatPercentage) {
    this.fatPercentage = fatPercentage;
    return this;
  }

   /**
   * Percentage of fat in the original sample
   * @return fatPercentage
  **/
  @ApiModelProperty(value = "Percentage of fat in the original sample")
  public String getFatPercentage() {
    return fatPercentage;
  }

  public void setFatPercentage(String fatPercentage) {
    this.fatPercentage = fatPercentage;
  }

  public Assay name(String name) {
    this.name = name;
    return this;
  }

   /**
   * A name given to the assay
   * @return name
  **/
  @ApiModelProperty(required = true, value = "A name given to the assay")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Assay description(String description) {
    this.description = description;
    return this;
  }

   /**
   * General description of the assay. Corresponds to the Protocol REF in ISA
   * @return description
  **/
  @ApiModelProperty(value = "General description of the assay. Corresponds to the Protocol REF in ISA")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Assay moisturePercentage(String moisturePercentage) {
    this.moisturePercentage = moisturePercentage;
    return this;
  }

   /**
   * Percentage of moisture in the original sample
   * @return moisturePercentage
  **/
  @ApiModelProperty(value = "Percentage of moisture in the original sample")
  public String getMoisturePercentage() {
    return moisturePercentage;
  }

  public void setMoisturePercentage(String moisturePercentage) {
    this.moisturePercentage = moisturePercentage;
  }

  public Assay leftCensoredData(String leftCensoredData) {
    this.leftCensoredData = leftCensoredData;
    return this;
  }

   /**
   * Percentage of measures equal to LOQ and/or LOD
   * @return leftCensoredData
  **/
  @ApiModelProperty(value = "Percentage of measures equal to LOQ and/or LOD")
  public String getLeftCensoredData() {
    return leftCensoredData;
  }

  public void setLeftCensoredData(String leftCensoredData) {
    this.leftCensoredData = leftCensoredData;
  }

  public Assay detectionLimit(String detectionLimit) {
    this.detectionLimit = detectionLimit;
    return this;
  }

   /**
   * Limit of detection reported in the unit specified by the variable Hazard unit
   * @return detectionLimit
  **/
  @ApiModelProperty(value = "Limit of detection reported in the unit specified by the variable Hazard unit")
  public String getDetectionLimit() {
    return detectionLimit;
  }

  public void setDetectionLimit(String detectionLimit) {
    this.detectionLimit = detectionLimit;
  }

  public Assay quantificationLimit(String quantificationLimit) {
    this.quantificationLimit = quantificationLimit;
    return this;
  }

   /**
   * Limit of quantification reported in the unit specified by the variable Hazard unit
   * @return quantificationLimit
  **/
  @ApiModelProperty(value = "Limit of quantification reported in the unit specified by the variable Hazard unit")
  public String getQuantificationLimit() {
    return quantificationLimit;
  }

  public void setQuantificationLimit(String quantificationLimit) {
    this.quantificationLimit = quantificationLimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Assay assay = (Assay) o;
    return Objects.equals(this.contaminationRange, assay.contaminationRange) &&
        Objects.equals(this.uncertaintyValue, assay.uncertaintyValue) &&
        Objects.equals(this.fatPercentage, assay.fatPercentage) &&
        Objects.equals(this.name, assay.name) &&
        Objects.equals(this.description, assay.description) &&
        Objects.equals(this.moisturePercentage, assay.moisturePercentage) &&
        Objects.equals(this.leftCensoredData, assay.leftCensoredData) &&
        Objects.equals(this.detectionLimit, assay.detectionLimit) &&
        Objects.equals(this.quantificationLimit, assay.quantificationLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contaminationRange, uncertaintyValue, fatPercentage, name, description, moisturePercentage, leftCensoredData, detectionLimit, quantificationLimit);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Assay {\n");
    
    sb.append("    contaminationRange: ").append(toIndentedString(contaminationRange)).append("\n");
    sb.append("    uncertaintyValue: ").append(toIndentedString(uncertaintyValue)).append("\n");
    sb.append("    fatPercentage: ").append(toIndentedString(fatPercentage)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    moisturePercentage: ").append(toIndentedString(moisturePercentage)).append("\n");
    sb.append("    leftCensoredData: ").append(toIndentedString(leftCensoredData)).append("\n");
    sb.append("    detectionLimit: ").append(toIndentedString(detectionLimit)).append("\n");
    sb.append("    quantificationLimit: ").append(toIndentedString(quantificationLimit)).append("\n");
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
