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
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;

/**
 * PredictiveModelScopeProduct
 */
@jakarta.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class PredictiveModelScopeProduct {
  @SerializedName("name")
  private String name = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("unit")
  private String unit = null;

  @SerializedName("method")
  private List<String> method = null;

  @SerializedName("packaging")
  private List<String> packaging = null;

  @SerializedName("treatment")
  private List<String> treatment = null;

  @SerializedName("originCountry")
  private String originCountry = null;

  @SerializedName("originArea")
  private String originArea = null;

  @SerializedName("fisheriesArea")
  private String fisheriesArea = null;

  @SerializedName("productionDate")
  private LocalDate productionDate = null;

  @SerializedName("expiryDate")
  private LocalDate expiryDate = null;

  public PredictiveModelScopeProduct name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(required = true, value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PredictiveModelScopeProduct description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PredictiveModelScopeProduct unit(String unit) {
    this.unit = unit;
    return this;
  }

   /**
   * Get unit
   * @return unit
  **/
  @ApiModelProperty(required = true, value = "")
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public PredictiveModelScopeProduct method(List<String> method) {
    this.method = method;
    return this;
  }

  public PredictiveModelScopeProduct addMethodItem(String methodItem) {
    if (this.method == null) {
      this.method = new ArrayList<String>();
    }
    this.method.add(methodItem);
    return this;
  }

   /**
   * Get method
   * @return method
  **/
  @ApiModelProperty(value = "")
  public List<String> getMethod() {
    return method;
  }

  public void setMethod(List<String> method) {
    this.method = method;
  }

  public PredictiveModelScopeProduct packaging(List<String> packaging) {
    this.packaging = packaging;
    return this;
  }

  public PredictiveModelScopeProduct addPackagingItem(String packagingItem) {
    if (this.packaging == null) {
      this.packaging = new ArrayList<String>();
    }
    this.packaging.add(packagingItem);
    return this;
  }

   /**
   * Get packaging
   * @return packaging
  **/
  @ApiModelProperty(value = "")
  public List<String> getPackaging() {
    return packaging;
  }

  public void setPackaging(List<String> packaging) {
    this.packaging = packaging;
  }

  public PredictiveModelScopeProduct treatment(List<String> treatment) {
    this.treatment = treatment;
    return this;
  }

  public PredictiveModelScopeProduct addTreatmentItem(String treatmentItem) {
    if (this.treatment == null) {
      this.treatment = new ArrayList<String>();
    }
    this.treatment.add(treatmentItem);
    return this;
  }

   /**
   * Get treatment
   * @return treatment
  **/
  @ApiModelProperty(value = "")
  public List<String> getTreatment() {
    return treatment;
  }

  public void setTreatment(List<String> treatment) {
    this.treatment = treatment;
  }

  public PredictiveModelScopeProduct originCountry(String originCountry) {
    this.originCountry = originCountry;
    return this;
  }

   /**
   * Get originCountry
   * @return originCountry
  **/
  @ApiModelProperty(value = "")
  public String getOriginCountry() {
    return originCountry;
  }

  public void setOriginCountry(String originCountry) {
    this.originCountry = originCountry;
  }

  public PredictiveModelScopeProduct originArea(String originArea) {
    this.originArea = originArea;
    return this;
  }

   /**
   * Get originArea
   * @return originArea
  **/
  @ApiModelProperty(value = "")
  public String getOriginArea() {
    return originArea;
  }

  public void setOriginArea(String originArea) {
    this.originArea = originArea;
  }

  public PredictiveModelScopeProduct fisheriesArea(String fisheriesArea) {
    this.fisheriesArea = fisheriesArea;
    return this;
  }

   /**
   * Get fisheriesArea
   * @return fisheriesArea
  **/
  @ApiModelProperty(value = "")
  public String getFisheriesArea() {
    return fisheriesArea;
  }

  public void setFisheriesArea(String fisheriesArea) {
    this.fisheriesArea = fisheriesArea;
  }

  public PredictiveModelScopeProduct productionDate(LocalDate productionDate) {
    this.productionDate = productionDate;
    return this;
  }

   /**
   * Get productionDate
   * @return productionDate
  **/
  @ApiModelProperty(value = "")
  public LocalDate getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(LocalDate productionDate) {
    this.productionDate = productionDate;
  }

  public PredictiveModelScopeProduct expiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

   /**
   * Get expiryDate
   * @return expiryDate
  **/
  @ApiModelProperty(value = "")
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PredictiveModelScopeProduct predictiveModelScopeProduct = (PredictiveModelScopeProduct) o;
    return Objects.equals(this.name, predictiveModelScopeProduct.name) &&
        Objects.equals(this.description, predictiveModelScopeProduct.description) &&
        Objects.equals(this.unit, predictiveModelScopeProduct.unit) &&
        Objects.equals(this.method, predictiveModelScopeProduct.method) &&
        Objects.equals(this.packaging, predictiveModelScopeProduct.packaging) &&
        Objects.equals(this.treatment, predictiveModelScopeProduct.treatment) &&
        Objects.equals(this.originCountry, predictiveModelScopeProduct.originCountry) &&
        Objects.equals(this.originArea, predictiveModelScopeProduct.originArea) &&
        Objects.equals(this.fisheriesArea, predictiveModelScopeProduct.fisheriesArea) &&
        Objects.equals(this.productionDate, predictiveModelScopeProduct.productionDate) &&
        Objects.equals(this.expiryDate, predictiveModelScopeProduct.expiryDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, unit, method, packaging, treatment, originCountry, originArea, fisheriesArea, productionDate, expiryDate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PredictiveModelScopeProduct {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    packaging: ").append(toIndentedString(packaging)).append("\n");
    sb.append("    treatment: ").append(toIndentedString(treatment)).append("\n");
    sb.append("    originCountry: ").append(toIndentedString(originCountry)).append("\n");
    sb.append("    originArea: ").append(toIndentedString(originArea)).append("\n");
    sb.append("    fisheriesArea: ").append(toIndentedString(fisheriesArea)).append("\n");
    sb.append("    productionDate: ").append(toIndentedString(productionDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
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

