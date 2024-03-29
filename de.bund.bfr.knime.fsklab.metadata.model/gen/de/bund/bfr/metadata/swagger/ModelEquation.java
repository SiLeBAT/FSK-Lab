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
import java.util.Arrays;
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
 * ModelEquation
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-25T13:50:49.666Z")
public class ModelEquation {
  @SerializedName("name")
  private String name = null;

  @SerializedName("modelEquationClass")
  private String modelEquationClass = null;

  @SerializedName("reference")
  private List<Reference> reference = null;

  @SerializedName("modelEquation")
  private String modelEquation = null;

  @SerializedName("modelHypothesis")
  private List<String> modelHypothesis = null;

  public ModelEquation name(String name) {
    this.name = name;
    return this;
  }

   /**
   * A name given to the model equation
   * @return name
  **/
  @ApiModelProperty(required = true, value = "A name given to the model equation")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ModelEquation modelEquationClass(String modelEquationClass) {
    this.modelEquationClass = modelEquationClass;
    return this;
  }

   /**
   * Information on that helps to categorize model equations
   * @return modelEquationClass
  **/
  @ApiModelProperty(value = "Information on that helps to categorize model equations")
  public String getModelEquationClass() {
    return modelEquationClass;
  }

  public void setModelEquationClass(String modelEquationClass) {
    this.modelEquationClass = modelEquationClass;
  }

  public ModelEquation reference(List<Reference> reference) {
    this.reference = reference;
    return this;
  }

  public ModelEquation addReferenceItem(Reference referenceItem) {
    if (this.reference == null) {
      this.reference = new ArrayList<Reference>();
    }
    this.reference.add(referenceItem);
    return this;
  }

   /**
   * Get reference
   * @return reference
  **/
  @ApiModelProperty(value = "")
  public List<Reference> getReference() {
    return reference;
  }

  public void setReference(List<Reference> reference) {
    this.reference = reference;
  }

  public ModelEquation modelEquation(String modelEquation) {
    this.modelEquation = modelEquation;
    return this;
  }

   /**
   * The pointer to the file that holds the software code (e.g. R-script)
   * @return modelEquation
  **/
  @ApiModelProperty(required = true, value = "The pointer to the file that holds the software code (e.g. R-script)")
  public String getModelEquation() {
    return modelEquation;
  }

  public void setModelEquation(String modelEquation) {
    this.modelEquation = modelEquation;
  }

  public ModelEquation modelHypothesis(List<String> modelHypothesis) {
    this.modelHypothesis = modelHypothesis;
    return this;
  }

  public ModelEquation addModelHypothesisItem(String modelHypothesisItem) {
    if (this.modelHypothesis == null) {
      this.modelHypothesis = new ArrayList<String>();
    }
    this.modelHypothesis.add(modelHypothesisItem);
    return this;
  }

   /**
   * Get modelHypothesis
   * @return modelHypothesis
  **/
  @ApiModelProperty(value = "")
  public List<String> getModelHypothesis() {
    return modelHypothesis;
  }

  public void setModelHypothesis(List<String> modelHypothesis) {
    this.modelHypothesis = modelHypothesis;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelEquation modelEquation = (ModelEquation) o;
    return Objects.equals(this.name, modelEquation.name) &&
        Objects.equals(this.modelEquationClass, modelEquation.modelEquationClass) &&
        Objects.equals(this.reference, modelEquation.reference) &&
        Objects.equals(this.modelEquation, modelEquation.modelEquation) &&
        Objects.equals(this.modelHypothesis, modelEquation.modelHypothesis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, modelEquationClass, reference, modelEquation, modelHypothesis);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelEquation {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    modelEquationClass: ").append(toIndentedString(modelEquationClass)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("    modelEquation: ").append(toIndentedString(modelEquation)).append("\n");
    sb.append("    modelHypothesis: ").append(toIndentedString(modelHypothesis)).append("\n");
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

