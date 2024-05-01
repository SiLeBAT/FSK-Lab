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
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Reference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;
/**
 * DoseResponseModelGeneralInformation
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-04-28T11:01:15.480200615Z[Etc/UTC]")
public class DoseResponseModelGeneralInformation {
  @SerializedName("identifier")
  private String identifier = null;

  @SerializedName("creator")
  private List<Contact> creator = null;

  @SerializedName("software")
  private String software = null;

  @SerializedName("author")
  private List<Contact> author = null;

  @SerializedName("format")
  private String format = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("language")
  private String language = null;

  @SerializedName("source")
  private String source = null;

  @SerializedName("availability")
  private String availability = null;

  @SerializedName("creationDate")
  private LocalDate creationDate = null;

  @SerializedName("url")
  private String url = null;

  @SerializedName("objective")
  private String objective = null;

  @SerializedName("reference")
  private List<Reference> reference = null;

  @SerializedName("modificationDate")
  private List<LocalDate> modificationDate = null;

  @SerializedName("rights")
  private String rights = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("languageWrittenIn")
  private String languageWrittenIn = null;

  @SerializedName("modelCategory")
  private ModelCategory modelCategory = null;

  @SerializedName("status")
  private String status = null;

  public DoseResponseModelGeneralInformation identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

   /**
   * An unambiguous ID given to the model or data. This can also be created automatically by a software tool
   * @return identifier
  **/
  @ApiModelProperty(required = true, value = "An unambiguous ID given to the model or data. This can also be created automatically by a software tool")
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public DoseResponseModelGeneralInformation creator(List<Contact> creator) {
    this.creator = creator;
    return this;
  }

  public DoseResponseModelGeneralInformation addCreatorItem(Contact creatorItem) {
    if (this.creator == null) {
      this.creator = new ArrayList<Contact>();
    }
    this.creator.add(creatorItem);
    return this;
  }

   /**
   * The person(s) that created this FSK file including all metadata
   * @return creator
  **/
  @ApiModelProperty(value = "The person(s) that created this FSK file including all metadata")
  public List<Contact> getCreator() {
    return creator;
  }

  public void setCreator(List<Contact> creator) {
    this.creator = creator;
  }

  public DoseResponseModelGeneralInformation software(String software) {
    this.software = software;
    return this;
  }

   /**
   * The program or software language in which the model has been implemented
   * @return software
  **/
  @ApiModelProperty(value = "The program or software language in which the model has been implemented")
  public String getSoftware() {
    return software;
  }

  public void setSoftware(String software) {
    this.software = software;
  }

  public DoseResponseModelGeneralInformation author(List<Contact> author) {
    this.author = author;
    return this;
  }

  public DoseResponseModelGeneralInformation addAuthorItem(Contact authorItem) {
    if (this.author == null) {
      this.author = new ArrayList<Contact>();
    }
    this.author.add(authorItem);
    return this;
  }

   /**
   * Person(s) who generated the model code or generated the data set originally
   * @return author
  **/
  @ApiModelProperty(value = "Person(s) who generated the model code or generated the data set originally")
  public List<Contact> getAuthor() {
    return author;
  }

  public void setAuthor(List<Contact> author) {
    this.author = author;
  }

  public DoseResponseModelGeneralInformation format(String format) {
    this.format = format;
    return this;
  }

   /**
   * File extension of the model or data file (including version number of format if applicable)
   * @return format
  **/
  @ApiModelProperty(value = "File extension of the model or data file (including version number of format if applicable)")
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public DoseResponseModelGeneralInformation description(String description) {
    this.description = description;
    return this;
  }

   /**
   * General description of the study, data or model
   * @return description
  **/
  @ApiModelProperty(value = "General description of the study, data or model")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DoseResponseModelGeneralInformation language(String language) {
    this.language = language;
    return this;
  }

   /**
   * A language of the resource (some data or reports can be available in French language for example)
   * @return language
  **/
  @ApiModelProperty(value = "A language of the resource (some data or reports can be available in French language for example)")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public DoseResponseModelGeneralInformation source(String source) {
    this.source = source;
    return this;
  }

   /**
   * A source from which the model/data is derived
   * @return source
  **/
  @ApiModelProperty(value = "A source from which the model/data is derived")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public DoseResponseModelGeneralInformation availability(String availability) {
    this.availability = availability;
    return this;
  }

   /**
   * Availability of data or model, i.e. if the annotated model code / data is included in this FSK file
   * @return availability
  **/
  @ApiModelProperty(value = "Availability of data or model, i.e. if the annotated model code / data is included in this FSK file")
  public String getAvailability() {
    return availability;
  }

  public void setAvailability(String availability) {
    this.availability = availability;
  }

  public DoseResponseModelGeneralInformation creationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
    return this;
  }

   /**
   * Creation date/time of the FSK file
   * @return creationDate
  **/
  @ApiModelProperty(required = true, value = "Creation date/time of the FSK file")
  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public DoseResponseModelGeneralInformation url(String url) {
    this.url = url;
    return this;
  }

   /**
   * Web address referencing the resource location (data for example)
   * @return url
  **/
  @ApiModelProperty(value = "Web address referencing the resource location (data for example)")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public DoseResponseModelGeneralInformation objective(String objective) {
    this.objective = objective;
    return this;
  }

   /**
   * Objective of the model or data
   * @return objective
  **/
  @ApiModelProperty(value = "Objective of the model or data")
  public String getObjective() {
    return objective;
  }

  public void setObjective(String objective) {
    this.objective = objective;
  }

  public DoseResponseModelGeneralInformation reference(List<Reference> reference) {
    this.reference = reference;
    return this;
  }

  public DoseResponseModelGeneralInformation addReferenceItem(Reference referenceItem) {
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

  public DoseResponseModelGeneralInformation modificationDate(List<LocalDate> modificationDate) {
    this.modificationDate = modificationDate;
    return this;
  }

  public DoseResponseModelGeneralInformation addModificationDateItem(LocalDate modificationDateItem) {
    if (this.modificationDate == null) {
      this.modificationDate = new ArrayList<LocalDate>();
    }
    this.modificationDate.add(modificationDateItem);
    return this;
  }

   /**
   * Date/time of the last version of the FSK file
   * @return modificationDate
  **/
  @ApiModelProperty(value = "Date/time of the last version of the FSK file")
  public List<LocalDate> getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(List<LocalDate> modificationDate) {
    this.modificationDate = modificationDate;
  }

  public DoseResponseModelGeneralInformation rights(String rights) {
    this.rights = rights;
    return this;
  }

   /**
   * Rights granted for usage, distribution and modification of this FSK file
   * @return rights
  **/
  @ApiModelProperty(required = true, value = "Rights granted for usage, distribution and modification of this FSK file")
  public String getRights() {
    return rights;
  }

  public void setRights(String rights) {
    this.rights = rights;
  }

  public DoseResponseModelGeneralInformation name(String name) {
    this.name = name;
    return this;
  }

   /**
   * A name given to the model or data
   * @return name
  **/
  @ApiModelProperty(required = true, value = "A name given to the model or data")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DoseResponseModelGeneralInformation languageWrittenIn(String languageWrittenIn) {
    this.languageWrittenIn = languageWrittenIn;
    return this;
  }

   /**
   * Software language used to write the model, e.g. R or MatLab
   * @return languageWrittenIn
  **/
  @ApiModelProperty(value = "Software language used to write the model, e.g. R or MatLab")
  public String getLanguageWrittenIn() {
    return languageWrittenIn;
  }

  public void setLanguageWrittenIn(String languageWrittenIn) {
    this.languageWrittenIn = languageWrittenIn;
  }

  public DoseResponseModelGeneralInformation modelCategory(ModelCategory modelCategory) {
    this.modelCategory = modelCategory;
    return this;
  }

   /**
   * Get modelCategory
   * @return modelCategory
  **/
  @ApiModelProperty(value = "")
  public ModelCategory getModelCategory() {
    return modelCategory;
  }

  public void setModelCategory(ModelCategory modelCategory) {
    this.modelCategory = modelCategory;
  }

  public DoseResponseModelGeneralInformation status(String status) {
    this.status = status;
    return this;
  }

   /**
   * The curation status of the model
   * @return status
  **/
  @ApiModelProperty(value = "The curation status of the model")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoseResponseModelGeneralInformation doseResponseModelGeneralInformation = (DoseResponseModelGeneralInformation) o;
    return Objects.equals(this.identifier, doseResponseModelGeneralInformation.identifier) &&
        Objects.equals(this.creator, doseResponseModelGeneralInformation.creator) &&
        Objects.equals(this.software, doseResponseModelGeneralInformation.software) &&
        Objects.equals(this.author, doseResponseModelGeneralInformation.author) &&
        Objects.equals(this.format, doseResponseModelGeneralInformation.format) &&
        Objects.equals(this.description, doseResponseModelGeneralInformation.description) &&
        Objects.equals(this.language, doseResponseModelGeneralInformation.language) &&
        Objects.equals(this.source, doseResponseModelGeneralInformation.source) &&
        Objects.equals(this.availability, doseResponseModelGeneralInformation.availability) &&
        Objects.equals(this.creationDate, doseResponseModelGeneralInformation.creationDate) &&
        Objects.equals(this.url, doseResponseModelGeneralInformation.url) &&
        Objects.equals(this.objective, doseResponseModelGeneralInformation.objective) &&
        Objects.equals(this.reference, doseResponseModelGeneralInformation.reference) &&
        Objects.equals(this.modificationDate, doseResponseModelGeneralInformation.modificationDate) &&
        Objects.equals(this.rights, doseResponseModelGeneralInformation.rights) &&
        Objects.equals(this.name, doseResponseModelGeneralInformation.name) &&
        Objects.equals(this.languageWrittenIn, doseResponseModelGeneralInformation.languageWrittenIn) &&
        Objects.equals(this.modelCategory, doseResponseModelGeneralInformation.modelCategory) &&
        Objects.equals(this.status, doseResponseModelGeneralInformation.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, creator, software, author, format, description, language, source, availability, creationDate, url, objective, reference, modificationDate, rights, name, languageWrittenIn, modelCategory, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DoseResponseModelGeneralInformation {\n");
    
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    software: ").append(toIndentedString(software)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    objective: ").append(toIndentedString(objective)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("    modificationDate: ").append(toIndentedString(modificationDate)).append("\n");
    sb.append("    rights: ").append(toIndentedString(rights)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    languageWrittenIn: ").append(toIndentedString(languageWrittenIn)).append("\n");
    sb.append("    modelCategory: ").append(toIndentedString(modelCategory)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
