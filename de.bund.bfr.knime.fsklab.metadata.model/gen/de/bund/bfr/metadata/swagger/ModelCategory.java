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
 * ModelCategory
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class ModelCategory {
  @SerializedName("modelClass")
  private String modelClass = null;

  @SerializedName("modelSubClass")
  private List<String> modelSubClass = null;

  @SerializedName("modelClassComment")
  private String modelClassComment = null;

  @SerializedName("basicProcess")
  private List<String> basicProcess = null;

  public ModelCategory modelClass(String modelClass) {
    this.modelClass = modelClass;
    return this;
  }

   /**
   * Type of model according to RAKIP classification
   * @return modelClass
  **/
  @ApiModelProperty(required = true, value = "Type of model according to RAKIP classification")
  public String getModelClass() {
    return modelClass;
  }

  public void setModelClass(String modelClass) {
    this.modelClass = modelClass;
  }

  public ModelCategory modelSubClass(List<String> modelSubClass) {
    this.modelSubClass = modelSubClass;
    return this;
  }

  public ModelCategory addModelSubClassItem(String modelSubClassItem) {
    if (this.modelSubClass == null) {
      this.modelSubClass = new ArrayList<String>();
    }
    this.modelSubClass.add(modelSubClassItem);
    return this;
  }

   /**
   * Get modelSubClass
   * @return modelSubClass
  **/
  @ApiModelProperty(value = "")
  public List<String> getModelSubClass() {
    return modelSubClass;
  }

  public void setModelSubClass(List<String> modelSubClass) {
    this.modelSubClass = modelSubClass;
  }

  public ModelCategory modelClassComment(String modelClassComment) {
    this.modelClassComment = modelClassComment;
    return this;
  }

   /**
   * Empty
   * @return modelClassComment
  **/
  @ApiModelProperty(value = "Empty")
  public String getModelClassComment() {
    return modelClassComment;
  }

  public void setModelClassComment(String modelClassComment) {
    this.modelClassComment = modelClassComment;
  }

  public ModelCategory basicProcess(List<String> basicProcess) {
    this.basicProcess = basicProcess;
    return this;
  }

  public ModelCategory addBasicProcessItem(String basicProcessItem) {
    if (this.basicProcess == null) {
      this.basicProcess = new ArrayList<String>();
    }
    this.basicProcess.add(basicProcessItem);
    return this;
  }

   /**
   * Get basicProcess
   * @return basicProcess
  **/
  @ApiModelProperty(value = "")
  public List<String> getBasicProcess() {
    return basicProcess;
  }

  public void setBasicProcess(List<String> basicProcess) {
    this.basicProcess = basicProcess;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelCategory modelCategory = (ModelCategory) o;
    return Objects.equals(this.modelClass, modelCategory.modelClass) &&
        Objects.equals(this.modelSubClass, modelCategory.modelSubClass) &&
        Objects.equals(this.modelClassComment, modelCategory.modelClassComment) &&
        Objects.equals(this.basicProcess, modelCategory.basicProcess);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelClass, modelSubClass, modelClassComment, basicProcess);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelCategory {\n");
    
    sb.append("    modelClass: ").append(toIndentedString(modelClass)).append("\n");
    sb.append("    modelSubClass: ").append(toIndentedString(modelSubClass)).append("\n");
    sb.append("    modelClassComment: ").append(toIndentedString(modelClassComment)).append("\n");
    sb.append("    basicProcess: ").append(toIndentedString(basicProcess)).append("\n");
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

