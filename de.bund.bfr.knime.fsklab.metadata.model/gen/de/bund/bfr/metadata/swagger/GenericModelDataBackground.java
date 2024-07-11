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
import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GenericModelDataBackground
 */
@jakarta.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class GenericModelDataBackground {
  @SerializedName("study")
  private Study study = null;

  @SerializedName("studySample")
  private List<StudySample> studySample = null;

  @SerializedName("dietaryAssessmentMethod")
  private List<DietaryAssessmentMethod> dietaryAssessmentMethod = null;

  @SerializedName("laboratory")
  private List<Laboratory> laboratory = null;

  @SerializedName("assay")
  private List<Assay> assay = null;

  public GenericModelDataBackground study(Study study) {
    this.study = study;
    return this;
  }

   /**
   * Get study
   * @return study
  **/
  @ApiModelProperty(required = true, value = "")
  public Study getStudy() {
    return study;
  }

  public void setStudy(Study study) {
    this.study = study;
  }

  public GenericModelDataBackground studySample(List<StudySample> studySample) {
    this.studySample = studySample;
    return this;
  }

  public GenericModelDataBackground addStudySampleItem(StudySample studySampleItem) {
    if (this.studySample == null) {
      this.studySample = new ArrayList<StudySample>();
    }
    this.studySample.add(studySampleItem);
    return this;
  }

   /**
   * Get studySample
   * @return studySample
  **/
  @ApiModelProperty(value = "")
  public List<StudySample> getStudySample() {
    return studySample;
  }

  public void setStudySample(List<StudySample> studySample) {
    this.studySample = studySample;
  }

  public GenericModelDataBackground dietaryAssessmentMethod(List<DietaryAssessmentMethod> dietaryAssessmentMethod) {
    this.dietaryAssessmentMethod = dietaryAssessmentMethod;
    return this;
  }

  public GenericModelDataBackground addDietaryAssessmentMethodItem(DietaryAssessmentMethod dietaryAssessmentMethodItem) {
    if (this.dietaryAssessmentMethod == null) {
      this.dietaryAssessmentMethod = new ArrayList<DietaryAssessmentMethod>();
    }
    this.dietaryAssessmentMethod.add(dietaryAssessmentMethodItem);
    return this;
  }

   /**
   * Get dietaryAssessmentMethod
   * @return dietaryAssessmentMethod
  **/
  @ApiModelProperty(value = "")
  public List<DietaryAssessmentMethod> getDietaryAssessmentMethod() {
    return dietaryAssessmentMethod;
  }

  public void setDietaryAssessmentMethod(List<DietaryAssessmentMethod> dietaryAssessmentMethod) {
    this.dietaryAssessmentMethod = dietaryAssessmentMethod;
  }

  public GenericModelDataBackground laboratory(List<Laboratory> laboratory) {
    this.laboratory = laboratory;
    return this;
  }

  public GenericModelDataBackground addLaboratoryItem(Laboratory laboratoryItem) {
    if (this.laboratory == null) {
      this.laboratory = new ArrayList<Laboratory>();
    }
    this.laboratory.add(laboratoryItem);
    return this;
  }

   /**
   * Get laboratory
   * @return laboratory
  **/
  @ApiModelProperty(value = "")
  public List<Laboratory> getLaboratory() {
    return laboratory;
  }

  public void setLaboratory(List<Laboratory> laboratory) {
    this.laboratory = laboratory;
  }

  public GenericModelDataBackground assay(List<Assay> assay) {
    this.assay = assay;
    return this;
  }

  public GenericModelDataBackground addAssayItem(Assay assayItem) {
    if (this.assay == null) {
      this.assay = new ArrayList<Assay>();
    }
    this.assay.add(assayItem);
    return this;
  }

   /**
   * Get assay
   * @return assay
  **/
  @ApiModelProperty(value = "")
  public List<Assay> getAssay() {
    return assay;
  }

  public void setAssay(List<Assay> assay) {
    this.assay = assay;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericModelDataBackground genericModelDataBackground = (GenericModelDataBackground) o;
    return Objects.equals(this.study, genericModelDataBackground.study) &&
        Objects.equals(this.studySample, genericModelDataBackground.studySample) &&
        Objects.equals(this.dietaryAssessmentMethod, genericModelDataBackground.dietaryAssessmentMethod) &&
        Objects.equals(this.laboratory, genericModelDataBackground.laboratory) &&
        Objects.equals(this.assay, genericModelDataBackground.assay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(study, studySample, dietaryAssessmentMethod, laboratory, assay);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericModelDataBackground {\n");
    
    sb.append("    study: ").append(toIndentedString(study)).append("\n");
    sb.append("    studySample: ").append(toIndentedString(studySample)).append("\n");
    sb.append("    dietaryAssessmentMethod: ").append(toIndentedString(dietaryAssessmentMethod)).append("\n");
    sb.append("    laboratory: ").append(toIndentedString(laboratory)).append("\n");
    sb.append("    assay: ").append(toIndentedString(assay)).append("\n");
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

