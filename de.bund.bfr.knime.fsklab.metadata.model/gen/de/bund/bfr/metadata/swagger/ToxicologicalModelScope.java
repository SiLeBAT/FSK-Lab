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
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ToxicologicalModelScope
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class ToxicologicalModelScope {
  @SerializedName("hazard")
  private List<Hazard> hazard = null;

  @SerializedName("populationGroup")
  private List<PopulationGroup> populationGroup = null;

  @SerializedName("generalComment")
  private String generalComment = null;

  @SerializedName("temporalInformation")
  private String temporalInformation = null;

  @SerializedName("spatialInformation")
  private List<String> spatialInformation = null;

  public ToxicologicalModelScope hazard(List<Hazard> hazard) {
    this.hazard = hazard;
    return this;
  }

  public ToxicologicalModelScope addHazardItem(Hazard hazardItem) {
    if (this.hazard == null) {
      this.hazard = new ArrayList<Hazard>();
    }
    this.hazard.add(hazardItem);
    return this;
  }

   /**
   * Get hazard
   * @return hazard
  **/
  @ApiModelProperty(value = "")
  public List<Hazard> getHazard() {
    return hazard;
  }

  public void setHazard(List<Hazard> hazard) {
    this.hazard = hazard;
  }

  public ToxicologicalModelScope populationGroup(List<PopulationGroup> populationGroup) {
    this.populationGroup = populationGroup;
    return this;
  }

  public ToxicologicalModelScope addPopulationGroupItem(PopulationGroup populationGroupItem) {
    if (this.populationGroup == null) {
      this.populationGroup = new ArrayList<PopulationGroup>();
    }
    this.populationGroup.add(populationGroupItem);
    return this;
  }

   /**
   * Get populationGroup
   * @return populationGroup
  **/
  @ApiModelProperty(value = "")
  public List<PopulationGroup> getPopulationGroup() {
    return populationGroup;
  }

  public void setPopulationGroup(List<PopulationGroup> populationGroup) {
    this.populationGroup = populationGroup;
  }

  public ToxicologicalModelScope generalComment(String generalComment) {
    this.generalComment = generalComment;
    return this;
  }

   /**
   * Get generalComment
   * @return generalComment
  **/
  @ApiModelProperty(value = "")
  public String getGeneralComment() {
    return generalComment;
  }

  public void setGeneralComment(String generalComment) {
    this.generalComment = generalComment;
  }

  public ToxicologicalModelScope temporalInformation(String temporalInformation) {
    this.temporalInformation = temporalInformation;
    return this;
  }

   /**
   * Get temporalInformation
   * @return temporalInformation
  **/
  @ApiModelProperty(value = "")
  public String getTemporalInformation() {
    return temporalInformation;
  }

  public void setTemporalInformation(String temporalInformation) {
    this.temporalInformation = temporalInformation;
  }

  public ToxicologicalModelScope spatialInformation(List<String> spatialInformation) {
    this.spatialInformation = spatialInformation;
    return this;
  }

  public ToxicologicalModelScope addSpatialInformationItem(String spatialInformationItem) {
    if (this.spatialInformation == null) {
      this.spatialInformation = new ArrayList<String>();
    }
    this.spatialInformation.add(spatialInformationItem);
    return this;
  }

   /**
   * Get spatialInformation
   * @return spatialInformation
  **/
  @ApiModelProperty(value = "")
  public List<String> getSpatialInformation() {
    return spatialInformation;
  }

  public void setSpatialInformation(List<String> spatialInformation) {
    this.spatialInformation = spatialInformation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToxicologicalModelScope toxicologicalModelScope = (ToxicologicalModelScope) o;
    return Objects.equals(this.hazard, toxicologicalModelScope.hazard) &&
        Objects.equals(this.populationGroup, toxicologicalModelScope.populationGroup) &&
        Objects.equals(this.generalComment, toxicologicalModelScope.generalComment) &&
        Objects.equals(this.temporalInformation, toxicologicalModelScope.temporalInformation) &&
        Objects.equals(this.spatialInformation, toxicologicalModelScope.spatialInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hazard, populationGroup, generalComment, temporalInformation, spatialInformation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ToxicologicalModelScope {\n");
    
    sb.append("    hazard: ").append(toIndentedString(hazard)).append("\n");
    sb.append("    populationGroup: ").append(toIndentedString(populationGroup)).append("\n");
    sb.append("    generalComment: ").append(toIndentedString(generalComment)).append("\n");
    sb.append("    temporalInformation: ").append(toIndentedString(temporalInformation)).append("\n");
    sb.append("    spatialInformation: ").append(toIndentedString(spatialInformation)).append("\n");
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
