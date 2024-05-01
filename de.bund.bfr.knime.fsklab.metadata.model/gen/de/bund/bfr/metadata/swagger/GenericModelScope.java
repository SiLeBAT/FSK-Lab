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
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * GenericModelScope
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-04-28T11:01:15.480200615Z[Etc/UTC]")
public class GenericModelScope {
  @SerializedName("spatialInformation")
  private List<String> spatialInformation = null;

  @SerializedName("product")
  private List<Product> product = null;

  @SerializedName("populationGroup")
  private List<PopulationGroup> populationGroup = null;

  @SerializedName("hazard")
  private List<Hazard> hazard = null;

  @SerializedName("generalComment")
  private String generalComment = null;

  @SerializedName("temporalInformation")
  private String temporalInformation = null;

  public GenericModelScope spatialInformation(List<String> spatialInformation) {
    this.spatialInformation = spatialInformation;
    return this;
  }

  public GenericModelScope addSpatialInformationItem(String spatialInformationItem) {
    if (this.spatialInformation == null) {
      this.spatialInformation = new ArrayList<String>();
    }
    this.spatialInformation.add(spatialInformationItem);
    return this;
  }

   /**
   * Spatial information (area) on which the model or data applies
   * @return spatialInformation
  **/
  @ApiModelProperty(value = "Spatial information (area) on which the model or data applies")
  public List<String> getSpatialInformation() {
    return spatialInformation;
  }

  public void setSpatialInformation(List<String> spatialInformation) {
    this.spatialInformation = spatialInformation;
  }

  public GenericModelScope product(List<Product> product) {
    this.product = product;
    return this;
  }

  public GenericModelScope addProductItem(Product productItem) {
    if (this.product == null) {
      this.product = new ArrayList<Product>();
    }
    this.product.add(productItem);
    return this;
  }

   /**
   * Get product
   * @return product
  **/
  @ApiModelProperty(value = "")
  public List<Product> getProduct() {
    return product;
  }

  public void setProduct(List<Product> product) {
    this.product = product;
  }

  public GenericModelScope populationGroup(List<PopulationGroup> populationGroup) {
    this.populationGroup = populationGroup;
    return this;
  }

  public GenericModelScope addPopulationGroupItem(PopulationGroup populationGroupItem) {
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

  public GenericModelScope hazard(List<Hazard> hazard) {
    this.hazard = hazard;
    return this;
  }

  public GenericModelScope addHazardItem(Hazard hazardItem) {
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

  public GenericModelScope generalComment(String generalComment) {
    this.generalComment = generalComment;
    return this;
  }

   /**
   * General comments on the scope of the study, data or model
   * @return generalComment
  **/
  @ApiModelProperty(value = "General comments on the scope of the study, data or model")
  public String getGeneralComment() {
    return generalComment;
  }

  public void setGeneralComment(String generalComment) {
    this.generalComment = generalComment;
  }

  public GenericModelScope temporalInformation(String temporalInformation) {
    this.temporalInformation = temporalInformation;
    return this;
  }

   /**
   * Temporal information on which the model or data applies / An interval of time that is named or defined by its start and end dates (period of study)
   * @return temporalInformation
  **/
  @ApiModelProperty(value = "Temporal information on which the model or data applies / An interval of time that is named or defined by its start and end dates (period of study)")
  public String getTemporalInformation() {
    return temporalInformation;
  }

  public void setTemporalInformation(String temporalInformation) {
    this.temporalInformation = temporalInformation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericModelScope genericModelScope = (GenericModelScope) o;
    return Objects.equals(this.spatialInformation, genericModelScope.spatialInformation) &&
        Objects.equals(this.product, genericModelScope.product) &&
        Objects.equals(this.populationGroup, genericModelScope.populationGroup) &&
        Objects.equals(this.hazard, genericModelScope.hazard) &&
        Objects.equals(this.generalComment, genericModelScope.generalComment) &&
        Objects.equals(this.temporalInformation, genericModelScope.temporalInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialInformation, product, populationGroup, hazard, generalComment, temporalInformation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericModelScope {\n");
    
    sb.append("    spatialInformation: ").append(toIndentedString(spatialInformation)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    populationGroup: ").append(toIndentedString(populationGroup)).append("\n");
    sb.append("    hazard: ").append(toIndentedString(hazard)).append("\n");
    sb.append("    generalComment: ").append(toIndentedString(generalComment)).append("\n");
    sb.append("    temporalInformation: ").append(toIndentedString(temporalInformation)).append("\n");
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
