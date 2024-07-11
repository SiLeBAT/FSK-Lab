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
import de.bund.bfr.metadata.swagger.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExposureModelScope
 */
@jakarta.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class ExposureModelScope {
  @SerializedName("product")
  private List<Product> product = new ArrayList<Product>();

  @SerializedName("hazard")
  private List<Hazard> hazard = new ArrayList<Hazard>();

  @SerializedName("populationGroup")
  private List<PopulationGroup> populationGroup = new ArrayList<PopulationGroup>();

  @SerializedName("generalComment")
  private String generalComment = null;

  @SerializedName("temporalInformation")
  private String temporalInformation = null;

  @SerializedName("spatialInformation")
  private List<String> spatialInformation = null;

  public ExposureModelScope product(List<Product> product) {
    this.product = product;
    return this;
  }

  public ExposureModelScope addProductItem(Product productItem) {
    this.product.add(productItem);
    return this;
  }

   /**
   * Get product
   * @return product
  **/
  @ApiModelProperty(required = true, value = "")
  public List<Product> getProduct() {
    return product;
  }

  public void setProduct(List<Product> product) {
    this.product = product;
  }

  public ExposureModelScope hazard(List<Hazard> hazard) {
    this.hazard = hazard;
    return this;
  }

  public ExposureModelScope addHazardItem(Hazard hazardItem) {
    this.hazard.add(hazardItem);
    return this;
  }

   /**
   * Get hazard
   * @return hazard
  **/
  @ApiModelProperty(required = true, value = "")
  public List<Hazard> getHazard() {
    return hazard;
  }

  public void setHazard(List<Hazard> hazard) {
    this.hazard = hazard;
  }

  public ExposureModelScope populationGroup(List<PopulationGroup> populationGroup) {
    this.populationGroup = populationGroup;
    return this;
  }

  public ExposureModelScope addPopulationGroupItem(PopulationGroup populationGroupItem) {
    this.populationGroup.add(populationGroupItem);
    return this;
  }

   /**
   * Get populationGroup
   * @return populationGroup
  **/
  @ApiModelProperty(required = true, value = "")
  public List<PopulationGroup> getPopulationGroup() {
    return populationGroup;
  }

  public void setPopulationGroup(List<PopulationGroup> populationGroup) {
    this.populationGroup = populationGroup;
  }

  public ExposureModelScope generalComment(String generalComment) {
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

  public ExposureModelScope temporalInformation(String temporalInformation) {
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

  public ExposureModelScope spatialInformation(List<String> spatialInformation) {
    this.spatialInformation = spatialInformation;
    return this;
  }

  public ExposureModelScope addSpatialInformationItem(String spatialInformationItem) {
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
    ExposureModelScope exposureModelScope = (ExposureModelScope) o;
    return Objects.equals(this.product, exposureModelScope.product) &&
        Objects.equals(this.hazard, exposureModelScope.hazard) &&
        Objects.equals(this.populationGroup, exposureModelScope.populationGroup) &&
        Objects.equals(this.generalComment, exposureModelScope.generalComment) &&
        Objects.equals(this.temporalInformation, exposureModelScope.temporalInformation) &&
        Objects.equals(this.spatialInformation, exposureModelScope.spatialInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(product, hazard, populationGroup, generalComment, temporalInformation, spatialInformation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExposureModelScope {\n");
    
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
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

