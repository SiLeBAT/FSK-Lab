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
 * Reference
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-07-02T16:22:48.194+02:00")
public class Reference {
  @SerializedName("isReferenceDescription")
  private Boolean isReferenceDescription = null;

  /**
   * Gets or Sets publicationType
   */
  @JsonAdapter(PublicationTypeEnum.Adapter.class)
  public enum PublicationTypeEnum {
    ABST("ABST"),
    
    ADVS("ADVS"),
    
    AGGR("AGGR"),
    
    ANCIENT("ANCIENT"),
    
    ART("ART"),
    
    BILL("BILL"),
    
    BLOG("BLOG"),
    
    BOOK("BOOK"),
    
    CASE("CASE"),
    
    CHAP("CHAP"),
    
    CHART("CHART"),
    
    CLSWK("CLSWK"),
    
    COMP("COMP"),
    
    CONF("CONF"),
    
    CPAPER("CPAPER"),
    
    CTLG("CTLG"),
    
    DATA("DATA"),
    
    DBASE("DBASE"),
    
    DICT("DICT"),
    
    EBOOK("EBOOK"),
    
    ECHAP("ECHAP"),
    
    EDBOOK("EDBOOK"),
    
    EJOUR("EJOUR"),
    
    ELECT("ELECT"),
    
    ENCYC("ENCYC"),
    
    EQUA("EQUA"),
    
    FIGURE("FIGURE"),
    
    GEN("GEN"),
    
    GOVDOC("GOVDOC"),
    
    GRANT("GRANT"),
    
    HEAR("HEAR"),
    
    ICOMM("ICOMM"),
    
    INPR("INPR"),
    
    JOUR("JOUR"),
    
    JFULL("JFULL"),
    
    LEGAL("LEGAL"),
    
    MANSCPT("MANSCPT"),
    
    MAP("MAP"),
    
    MGZN("MGZN"),
    
    MPCT("MPCT"),
    
    MULTI("MULTI"),
    
    MUSIC("MUSIC"),
    
    NEW("NEW"),
    
    PAMP("PAMP"),
    
    PAT("PAT"),
    
    PCOMM("PCOMM"),
    
    RPRT("RPRT"),
    
    SER("SER"),
    
    SLIDE("SLIDE"),
    
    SOUND("SOUND"),
    
    STAND("STAND"),
    
    STAT("STAT"),
    
    THES("THES"),
    
    UNPB("UNPB"),
    
    VIDEO("VIDEO");

    private String value;

    PublicationTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static PublicationTypeEnum fromValue(String text) {
      for (PublicationTypeEnum b : PublicationTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<PublicationTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PublicationTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public PublicationTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return PublicationTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("publicationType")
  private List<PublicationTypeEnum> publicationType = null;

  @SerializedName("date")
  private LocalDate date = null;

  @SerializedName("pmid")
  private String pmid = null;

  @SerializedName("doi")
  private String doi = null;

  @SerializedName("authorList")
  private String authorList = null;

  @SerializedName("title")
  private String title = null;

  @SerializedName("abstract")
  private String _abstract = null;

  @SerializedName("journal")
  private String journal = null;

  @SerializedName("volume")
  private String volume = null;

  @SerializedName("issue")
  private String issue = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("website")
  private String website = null;

  @SerializedName("comment")
  private String comment = null;

  public Reference isReferenceDescription(Boolean isReferenceDescription) {
    this.isReferenceDescription = isReferenceDescription;
    return this;
  }

   /**
   * Indicates whether this specific publication serves as the reference description for the model. There has to be at least one reference where this field is set to &#39;True&#39;
   * @return isReferenceDescription
  **/
  @ApiModelProperty(required = true, value = "Indicates whether this specific publication serves as the reference description for the model. There has to be at least one reference where this field is set to 'True'")
  public Boolean isIsReferenceDescription() {
    return isReferenceDescription;
  }

  public void setIsReferenceDescription(Boolean isReferenceDescription) {
    this.isReferenceDescription = isReferenceDescription;
  }

  public Reference publicationType(List<PublicationTypeEnum> publicationType) {
    this.publicationType = publicationType;
    return this;
  }

  public Reference addPublicationTypeItem(PublicationTypeEnum publicationTypeItem) {
    if (this.publicationType == null) {
      this.publicationType = new ArrayList<PublicationTypeEnum>();
    }
    this.publicationType.add(publicationTypeItem);
    return this;
  }

   /**
   * The type of publication, e.g. Report, Journal article, Book, Online database, ...
   * @return publicationType
  **/
  @ApiModelProperty(value = "The type of publication, e.g. Report, Journal article, Book, Online database, ...")
  public List<PublicationTypeEnum> getPublicationType() {
    return publicationType;
  }

  public void setPublicationType(List<PublicationTypeEnum> publicationType) {
    this.publicationType = publicationType;
  }

  public Reference date(LocalDate date) {
    this.date = date;
    return this;
  }

   /**
   * Temporal information on the publication date
   * @return date
  **/
  @ApiModelProperty(value = "Temporal information on the publication date")
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Reference pmid(String pmid) {
    this.pmid = pmid;
    return this;
  }

   /**
   * The PubMed ID related to this publication
   * @return pmid
  **/
  @ApiModelProperty(value = "The PubMed ID related to this publication")
  public String getPmid() {
    return pmid;
  }

  public void setPmid(String pmid) {
    this.pmid = pmid;
  }

  public Reference doi(String doi) {
    this.doi = doi;
    return this;
  }

   /**
   * The DOI related to this publication
   * @return doi
  **/
  @ApiModelProperty(required = true, value = "The DOI related to this publication")
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public Reference authorList(String authorList) {
    this.authorList = authorList;
    return this;
  }

   /**
   * Name and surname of the authors who contributed to this publication
   * @return authorList
  **/
  @ApiModelProperty(value = "Name and surname of the authors who contributed to this publication")
  public String getAuthorList() {
    return authorList;
  }

  public void setAuthorList(String authorList) {
    this.authorList = authorList;
  }

  public Reference title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Title of the publication in which the model or the data has been described
   * @return title
  **/
  @ApiModelProperty(required = true, value = "Title of the publication in which the model or the data has been described")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Reference _abstract(String _abstract) {
    this._abstract = _abstract;
    return this;
  }

   /**
   * Abstract of the publication in which the model or the data has been described
   * @return _abstract
  **/
  @ApiModelProperty(value = "Abstract of the publication in which the model or the data has been described")
  public String getAbstract() {
    return _abstract;
  }

  public void setAbstract(String _abstract) {
    this._abstract = _abstract;
  }

  public Reference journal(String journal) {
    this.journal = journal;
    return this;
  }

   /**
   * Data on the details of the journal in which the model or the data has been described
   * @return journal
  **/
  @ApiModelProperty(value = "Data on the details of the journal in which the model or the data has been described")
  public String getJournal() {
    return journal;
  }

  public void setJournal(String journal) {
    this.journal = journal;
  }

  public Reference volume(String volume) {
    this.volume = volume;
    return this;
  }

   /**
   * Data on the details of the journal in which the model or the data has been described
   * @return volume
  **/
  @ApiModelProperty(value = "Data on the details of the journal in which the model or the data has been described")
  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public Reference issue(String issue) {
    this.issue = issue;
    return this;
  }

   /**
   * Data on the details of the journal in which the model or the data has been described
   * @return issue
  **/
  @ApiModelProperty(value = "Data on the details of the journal in which the model or the data has been described")
  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public Reference status(String status) {
    this.status = status;
    return this;
  }

   /**
   * The status of this publication, e.g. Published, Submitted, etc.
   * @return status
  **/
  @ApiModelProperty(value = "The status of this publication, e.g. Published, Submitted, etc.")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Reference website(String website) {
    this.website = website;
    return this;
  }

   /**
   * A link to the publication website (different from DOI)
   * @return website
  **/
  @ApiModelProperty(value = "A link to the publication website (different from DOI)")
  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public Reference comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Further comments related to the reference description, e.g. which section in there describes the specific model or which figure in there can be reproduced with the visualization script
   * @return comment
  **/
  @ApiModelProperty(value = "Further comments related to the reference description, e.g. which section in there describes the specific model or which figure in there can be reproduced with the visualization script")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reference reference = (Reference) o;
    return Objects.equals(this.isReferenceDescription, reference.isReferenceDescription) &&
        Objects.equals(this.publicationType, reference.publicationType) &&
        Objects.equals(this.date, reference.date) &&
        Objects.equals(this.pmid, reference.pmid) &&
        Objects.equals(this.doi, reference.doi) &&
        Objects.equals(this.authorList, reference.authorList) &&
        Objects.equals(this.title, reference.title) &&
        Objects.equals(this._abstract, reference._abstract) &&
        Objects.equals(this.journal, reference.journal) &&
        Objects.equals(this.volume, reference.volume) &&
        Objects.equals(this.issue, reference.issue) &&
        Objects.equals(this.status, reference.status) &&
        Objects.equals(this.website, reference.website) &&
        Objects.equals(this.comment, reference.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isReferenceDescription, publicationType, date, pmid, doi, authorList, title, _abstract, journal, volume, issue, status, website, comment);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Reference {\n");
    
    sb.append("    isReferenceDescription: ").append(toIndentedString(isReferenceDescription)).append("\n");
    sb.append("    publicationType: ").append(toIndentedString(publicationType)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    pmid: ").append(toIndentedString(pmid)).append("\n");
    sb.append("    doi: ").append(toIndentedString(doi)).append("\n");
    sb.append("    authorList: ").append(toIndentedString(authorList)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    _abstract: ").append(toIndentedString(_abstract)).append("\n");
    sb.append("    journal: ").append(toIndentedString(journal)).append("\n");
    sb.append("    volume: ").append(toIndentedString(volume)).append("\n");
    sb.append("    issue: ").append(toIndentedString(issue)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    website: ").append(toIndentedString(website)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

