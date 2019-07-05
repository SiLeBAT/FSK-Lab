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

/**
 * DietaryAssessmentMethod
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class DietaryAssessmentMethod {
  @SerializedName("collectionTool")
  private String collectionTool = null;

  @SerializedName("numberOfNonConsecutiveOneDay")
  private String numberOfNonConsecutiveOneDay = null;

  @SerializedName("softwareTool")
  private String softwareTool = null;

  @SerializedName("numberOfFoodItems")
  private List<String> numberOfFoodItems = new ArrayList<String>();

  @SerializedName("recordTypes")
  private List<String> recordTypes = new ArrayList<String>();

  @SerializedName("foodDescriptors")
  private List<String> foodDescriptors = new ArrayList<String>();

  public DietaryAssessmentMethod collectionTool(String collectionTool) {
    this.collectionTool = collectionTool;
    return this;
  }

   /**
   * Get collectionTool
   * @return collectionTool
  **/
  @ApiModelProperty(required = true, value = "")
  public String getCollectionTool() {
    return collectionTool;
  }

  public void setCollectionTool(String collectionTool) {
    this.collectionTool = collectionTool;
  }

  public DietaryAssessmentMethod numberOfNonConsecutiveOneDay(String numberOfNonConsecutiveOneDay) {
    this.numberOfNonConsecutiveOneDay = numberOfNonConsecutiveOneDay;
    return this;
  }

   /**
   * Get numberOfNonConsecutiveOneDay
   * @return numberOfNonConsecutiveOneDay
  **/
  @ApiModelProperty(required = true, value = "")
  public String getNumberOfNonConsecutiveOneDay() {
    return numberOfNonConsecutiveOneDay;
  }

  public void setNumberOfNonConsecutiveOneDay(String numberOfNonConsecutiveOneDay) {
    this.numberOfNonConsecutiveOneDay = numberOfNonConsecutiveOneDay;
  }

  public DietaryAssessmentMethod softwareTool(String softwareTool) {
    this.softwareTool = softwareTool;
    return this;
  }

   /**
   * Get softwareTool
   * @return softwareTool
  **/
  @ApiModelProperty(value = "")
  public String getSoftwareTool() {
    return softwareTool;
  }

  public void setSoftwareTool(String softwareTool) {
    this.softwareTool = softwareTool;
  }

  public DietaryAssessmentMethod numberOfFoodItems(List<String> numberOfFoodItems) {
    this.numberOfFoodItems = numberOfFoodItems;
    return this;
  }

  public DietaryAssessmentMethod addNumberOfFoodItemsItem(String numberOfFoodItemsItem) {
    this.numberOfFoodItems.add(numberOfFoodItemsItem);
    return this;
  }

   /**
   * Get numberOfFoodItems
   * @return numberOfFoodItems
  **/
  @ApiModelProperty(required = true, value = "")
  public List<String> getNumberOfFoodItems() {
    return numberOfFoodItems;
  }

  public void setNumberOfFoodItems(List<String> numberOfFoodItems) {
    this.numberOfFoodItems = numberOfFoodItems;
  }

  public DietaryAssessmentMethod recordTypes(List<String> recordTypes) {
    this.recordTypes = recordTypes;
    return this;
  }

  public DietaryAssessmentMethod addRecordTypesItem(String recordTypesItem) {
    this.recordTypes.add(recordTypesItem);
    return this;
  }

   /**
   * Get recordTypes
   * @return recordTypes
  **/
  @ApiModelProperty(required = true, value = "")
  public List<String> getRecordTypes() {
    return recordTypes;
  }

  public void setRecordTypes(List<String> recordTypes) {
    this.recordTypes = recordTypes;
  }

  public DietaryAssessmentMethod foodDescriptors(List<String> foodDescriptors) {
    this.foodDescriptors = foodDescriptors;
    return this;
  }

  public DietaryAssessmentMethod addFoodDescriptorsItem(String foodDescriptorsItem) {
    this.foodDescriptors.add(foodDescriptorsItem);
    return this;
  }

   /**
   * Get foodDescriptors
   * @return foodDescriptors
  **/
  @ApiModelProperty(required = true, value = "")
  public List<String> getFoodDescriptors() {
    return foodDescriptors;
  }

  public void setFoodDescriptors(List<String> foodDescriptors) {
    this.foodDescriptors = foodDescriptors;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DietaryAssessmentMethod dietaryAssessmentMethod = (DietaryAssessmentMethod) o;
    return Objects.equals(this.collectionTool, dietaryAssessmentMethod.collectionTool) &&
        Objects.equals(this.numberOfNonConsecutiveOneDay, dietaryAssessmentMethod.numberOfNonConsecutiveOneDay) &&
        Objects.equals(this.softwareTool, dietaryAssessmentMethod.softwareTool) &&
        Objects.equals(this.numberOfFoodItems, dietaryAssessmentMethod.numberOfFoodItems) &&
        Objects.equals(this.recordTypes, dietaryAssessmentMethod.recordTypes) &&
        Objects.equals(this.foodDescriptors, dietaryAssessmentMethod.foodDescriptors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(collectionTool, numberOfNonConsecutiveOneDay, softwareTool, numberOfFoodItems, recordTypes, foodDescriptors);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DietaryAssessmentMethod {\n");
    
    sb.append("    collectionTool: ").append(toIndentedString(collectionTool)).append("\n");
    sb.append("    numberOfNonConsecutiveOneDay: ").append(toIndentedString(numberOfNonConsecutiveOneDay)).append("\n");
    sb.append("    softwareTool: ").append(toIndentedString(softwareTool)).append("\n");
    sb.append("    numberOfFoodItems: ").append(toIndentedString(numberOfFoodItems)).append("\n");
    sb.append("    recordTypes: ").append(toIndentedString(recordTypes)).append("\n");
    sb.append("    foodDescriptors: ").append(toIndentedString(foodDescriptors)).append("\n");
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
