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
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.Reference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;

/**
 * DataModelGeneralInformation
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class DataModelGeneralInformation {
  @SerializedName("name")
  private String name = null;

  @SerializedName("source")
  private String source = null;

  @SerializedName("identifier")
  private String identifier = null;

  @SerializedName("author")
  private List<Contact> author = null;

  @SerializedName("creator")
  private List<Contact> creator = null;

  @SerializedName("creationDate")
  private LocalDate creationDate = null;

  @SerializedName("modificationDate")
  private List<LocalDate> modificationDate = null;

  @SerializedName("rights")
  private String rights = null;

  @SerializedName("availability")
  private String availability = null;

  @SerializedName("url")
  private String url = null;

  @SerializedName("format")
  private String format = null;

  @SerializedName("reference")
  private List<Reference> reference = new ArrayList<Reference>();

  @SerializedName("language")
  private String language = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("objective")
  private String objective = null;

  @SerializedName("description")
  private String description = null;

  public DataModelGeneralInformation name(String name) {
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

  public DataModelGeneralInformation source(String source) {
    this.source = source;
    return this;
  }

   /**
   * Get source
   * @return source
  **/
  @ApiModelProperty(value = "")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public DataModelGeneralInformation identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

   /**
   * Get identifier
   * @return identifier
  **/
  @ApiModelProperty(required = true, value = "")
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public DataModelGeneralInformation author(List<Contact> author) {
    this.author = author;
    return this;
  }

  public DataModelGeneralInformation addAuthorItem(Contact authorItem) {
    if (this.author == null) {
      this.author = new ArrayList<Contact>();
    }
    this.author.add(authorItem);
    return this;
  }

   /**
   * Get author
   * @return author
  **/
  @ApiModelProperty(value = "")
  public List<Contact> getAuthor() {
    return author;
  }

  public void setAuthor(List<Contact> author) {
    this.author = author;
  }

  public DataModelGeneralInformation creator(List<Contact> creator) {
    this.creator = creator;
    return this;
  }

  public DataModelGeneralInformation addCreatorItem(Contact creatorItem) {
    if (this.creator == null) {
      this.creator = new ArrayList<Contact>();
    }
    this.creator.add(creatorItem);
    return this;
  }

   /**
   * Get creator
   * @return creator
  **/
  @ApiModelProperty(value = "")
  public List<Contact> getCreator() {
    return creator;
  }

  public void setCreator(List<Contact> creator) {
    this.creator = creator;
  }

  public DataModelGeneralInformation creationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
    return this;
  }

   /**
   * Get creationDate
   * @return creationDate
  **/
  @ApiModelProperty(required = true, value = "")
  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public DataModelGeneralInformation modificationDate(List<LocalDate> modificationDate) {
    this.modificationDate = modificationDate;
    return this;
  }

  public DataModelGeneralInformation addModificationDateItem(LocalDate modificationDateItem) {
    if (this.modificationDate == null) {
      this.modificationDate = new ArrayList<LocalDate>();
    }
    this.modificationDate.add(modificationDateItem);
    return this;
  }

   /**
   * Get modificationDate
   * @return modificationDate
  **/
  @ApiModelProperty(value = "")
  public List<LocalDate> getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(List<LocalDate> modificationDate) {
    this.modificationDate = modificationDate;
  }

  public DataModelGeneralInformation rights(String rights) {
    this.rights = rights;
    return this;
  }

   /**
   * Get rights
   * @return rights
  **/
  @ApiModelProperty(required = true, value = "")
  public String getRights() {
    return rights;
  }

  public void setRights(String rights) {
    this.rights = rights;
  }

  public DataModelGeneralInformation availability(String availability) {
    this.availability = availability;
    return this;
  }

   /**
   * Get availability
   * @return availability
  **/
  @ApiModelProperty(value = "")
  public String getAvailability() {
    return availability;
  }

  public void setAvailability(String availability) {
    this.availability = availability;
  }

  public DataModelGeneralInformation url(String url) {
    this.url = url;
    return this;
  }

   /**
   * Get url
   * @return url
  **/
  @ApiModelProperty(value = "")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public DataModelGeneralInformation format(String format) {
    this.format = format;
    return this;
  }

   /**
   * Get format
   * @return format
  **/
  @ApiModelProperty(value = "")
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public DataModelGeneralInformation reference(List<Reference> reference) {
    this.reference = reference;
    return this;
  }

  public DataModelGeneralInformation addReferenceItem(Reference referenceItem) {
    this.reference.add(referenceItem);
    return this;
  }

   /**
   * Get reference
   * @return reference
  **/
  @ApiModelProperty(required = true, value = "")
  public List<Reference> getReference() {
    return reference;
  }

  public void setReference(List<Reference> reference) {
    this.reference = reference;
  }

  public DataModelGeneralInformation language(String language) {
    this.language = language;
    return this;
  }

   /**
   * Get language
   * @return language
  **/
  @ApiModelProperty(value = "")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public DataModelGeneralInformation status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public DataModelGeneralInformation objective(String objective) {
    this.objective = objective;
    return this;
  }

   /**
   * Get objective
   * @return objective
  **/
  @ApiModelProperty(value = "")
  public String getObjective() {
    return objective;
  }

  public void setObjective(String objective) {
    this.objective = objective;
  }

  public DataModelGeneralInformation description(String description) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataModelGeneralInformation dataModelGeneralInformation = (DataModelGeneralInformation) o;
    return Objects.equals(this.name, dataModelGeneralInformation.name) &&
        Objects.equals(this.source, dataModelGeneralInformation.source) &&
        Objects.equals(this.identifier, dataModelGeneralInformation.identifier) &&
        Objects.equals(this.author, dataModelGeneralInformation.author) &&
        Objects.equals(this.creator, dataModelGeneralInformation.creator) &&
        Objects.equals(this.creationDate, dataModelGeneralInformation.creationDate) &&
        Objects.equals(this.modificationDate, dataModelGeneralInformation.modificationDate) &&
        Objects.equals(this.rights, dataModelGeneralInformation.rights) &&
        Objects.equals(this.availability, dataModelGeneralInformation.availability) &&
        Objects.equals(this.url, dataModelGeneralInformation.url) &&
        Objects.equals(this.format, dataModelGeneralInformation.format) &&
        Objects.equals(this.reference, dataModelGeneralInformation.reference) &&
        Objects.equals(this.language, dataModelGeneralInformation.language) &&
        Objects.equals(this.status, dataModelGeneralInformation.status) &&
        Objects.equals(this.objective, dataModelGeneralInformation.objective) &&
        Objects.equals(this.description, dataModelGeneralInformation.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, source, identifier, author, creator, creationDate, modificationDate, rights, availability, url, format, reference, language, status, objective, description);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataModelGeneralInformation {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    modificationDate: ").append(toIndentedString(modificationDate)).append("\n");
    sb.append("    rights: ").append(toIndentedString(rights)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    objective: ").append(toIndentedString(objective)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
