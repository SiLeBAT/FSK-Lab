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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * StudySample
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class StudySample {
  @SerializedName("sampleName")
  private String sampleName = null;

  @SerializedName("protocolOfSampleCollection")
  private String protocolOfSampleCollection = null;

  @SerializedName("samplingStrategy")
  private String samplingStrategy = null;

  @SerializedName("typeOfSamplingProgram")
  private String typeOfSamplingProgram = null;

  @SerializedName("samplingMethod")
  private String samplingMethod = null;

  @SerializedName("samplingPlan")
  private String samplingPlan = null;

  @SerializedName("samplingWeight")
  private String samplingWeight = null;

  @SerializedName("samplingSize")
  private String samplingSize = null;

  @SerializedName("lotSizeUnit")
  private String lotSizeUnit = null;

  @SerializedName("samplingPoint")
  private String samplingPoint = null;

  public StudySample sampleName(String sampleName) {
    this.sampleName = sampleName;
    return this;
  }

   /**
   * Get sampleName
   * @return sampleName
  **/
  @ApiModelProperty(required = true, value = "")
  public String getSampleName() {
    return sampleName;
  }

  public void setSampleName(String sampleName) {
    this.sampleName = sampleName;
  }

  public StudySample protocolOfSampleCollection(String protocolOfSampleCollection) {
    this.protocolOfSampleCollection = protocolOfSampleCollection;
    return this;
  }

   /**
   * Get protocolOfSampleCollection
   * @return protocolOfSampleCollection
  **/
  @ApiModelProperty(required = true, value = "")
  public String getProtocolOfSampleCollection() {
    return protocolOfSampleCollection;
  }

  public void setProtocolOfSampleCollection(String protocolOfSampleCollection) {
    this.protocolOfSampleCollection = protocolOfSampleCollection;
  }

  public StudySample samplingStrategy(String samplingStrategy) {
    this.samplingStrategy = samplingStrategy;
    return this;
  }

   /**
   * Get samplingStrategy
   * @return samplingStrategy
  **/
  @ApiModelProperty(value = "")
  public String getSamplingStrategy() {
    return samplingStrategy;
  }

  public void setSamplingStrategy(String samplingStrategy) {
    this.samplingStrategy = samplingStrategy;
  }

  public StudySample typeOfSamplingProgram(String typeOfSamplingProgram) {
    this.typeOfSamplingProgram = typeOfSamplingProgram;
    return this;
  }

   /**
   * Get typeOfSamplingProgram
   * @return typeOfSamplingProgram
  **/
  @ApiModelProperty(value = "")
  public String getTypeOfSamplingProgram() {
    return typeOfSamplingProgram;
  }

  public void setTypeOfSamplingProgram(String typeOfSamplingProgram) {
    this.typeOfSamplingProgram = typeOfSamplingProgram;
  }

  public StudySample samplingMethod(String samplingMethod) {
    this.samplingMethod = samplingMethod;
    return this;
  }

   /**
   * Get samplingMethod
   * @return samplingMethod
  **/
  @ApiModelProperty(value = "")
  public String getSamplingMethod() {
    return samplingMethod;
  }

  public void setSamplingMethod(String samplingMethod) {
    this.samplingMethod = samplingMethod;
  }

  public StudySample samplingPlan(String samplingPlan) {
    this.samplingPlan = samplingPlan;
    return this;
  }

   /**
   * Get samplingPlan
   * @return samplingPlan
  **/
  @ApiModelProperty(required = true, value = "")
  public String getSamplingPlan() {
    return samplingPlan;
  }

  public void setSamplingPlan(String samplingPlan) {
    this.samplingPlan = samplingPlan;
  }

  public StudySample samplingWeight(String samplingWeight) {
    this.samplingWeight = samplingWeight;
    return this;
  }

   /**
   * Get samplingWeight
   * @return samplingWeight
  **/
  @ApiModelProperty(required = true, value = "")
  public String getSamplingWeight() {
    return samplingWeight;
  }

  public void setSamplingWeight(String samplingWeight) {
    this.samplingWeight = samplingWeight;
  }

  public StudySample samplingSize(String samplingSize) {
    this.samplingSize = samplingSize;
    return this;
  }

   /**
   * Get samplingSize
   * @return samplingSize
  **/
  @ApiModelProperty(required = true, value = "")
  public String getSamplingSize() {
    return samplingSize;
  }

  public void setSamplingSize(String samplingSize) {
    this.samplingSize = samplingSize;
  }

  public StudySample lotSizeUnit(String lotSizeUnit) {
    this.lotSizeUnit = lotSizeUnit;
    return this;
  }

   /**
   * Get lotSizeUnit
   * @return lotSizeUnit
  **/
  @ApiModelProperty(value = "")
  public String getLotSizeUnit() {
    return lotSizeUnit;
  }

  public void setLotSizeUnit(String lotSizeUnit) {
    this.lotSizeUnit = lotSizeUnit;
  }

  public StudySample samplingPoint(String samplingPoint) {
    this.samplingPoint = samplingPoint;
    return this;
  }

   /**
   * Get samplingPoint
   * @return samplingPoint
  **/
  @ApiModelProperty(value = "")
  public String getSamplingPoint() {
    return samplingPoint;
  }

  public void setSamplingPoint(String samplingPoint) {
    this.samplingPoint = samplingPoint;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudySample studySample = (StudySample) o;
    return Objects.equals(this.sampleName, studySample.sampleName) &&
        Objects.equals(this.protocolOfSampleCollection, studySample.protocolOfSampleCollection) &&
        Objects.equals(this.samplingStrategy, studySample.samplingStrategy) &&
        Objects.equals(this.typeOfSamplingProgram, studySample.typeOfSamplingProgram) &&
        Objects.equals(this.samplingMethod, studySample.samplingMethod) &&
        Objects.equals(this.samplingPlan, studySample.samplingPlan) &&
        Objects.equals(this.samplingWeight, studySample.samplingWeight) &&
        Objects.equals(this.samplingSize, studySample.samplingSize) &&
        Objects.equals(this.lotSizeUnit, studySample.lotSizeUnit) &&
        Objects.equals(this.samplingPoint, studySample.samplingPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sampleName, protocolOfSampleCollection, samplingStrategy, typeOfSamplingProgram, samplingMethod, samplingPlan, samplingWeight, samplingSize, lotSizeUnit, samplingPoint);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudySample {\n");
    
    sb.append("    sampleName: ").append(toIndentedString(sampleName)).append("\n");
    sb.append("    protocolOfSampleCollection: ").append(toIndentedString(protocolOfSampleCollection)).append("\n");
    sb.append("    samplingStrategy: ").append(toIndentedString(samplingStrategy)).append("\n");
    sb.append("    typeOfSamplingProgram: ").append(toIndentedString(typeOfSamplingProgram)).append("\n");
    sb.append("    samplingMethod: ").append(toIndentedString(samplingMethod)).append("\n");
    sb.append("    samplingPlan: ").append(toIndentedString(samplingPlan)).append("\n");
    sb.append("    samplingWeight: ").append(toIndentedString(samplingWeight)).append("\n");
    sb.append("    samplingSize: ").append(toIndentedString(samplingSize)).append("\n");
    sb.append("    lotSizeUnit: ").append(toIndentedString(lotSizeUnit)).append("\n");
    sb.append("    samplingPoint: ").append(toIndentedString(samplingPoint)).append("\n");
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
