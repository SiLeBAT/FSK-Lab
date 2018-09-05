package org.sbml.jsbml.ext.pmf;


import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * @author Miguel Alba
 */
public class Reference extends AbstractSBase {

  private static final long serialVersionUID = -4222919106019745845L;
  
  private String  author;
  private boolean isSetAuthor = false;
  
  private Integer year;
  private boolean isSetYear = false;
  
  private String  title;
  private boolean isSetTitle = false;
  
  private String  abstractText;
  private boolean isSetAbstractText = false;
  
  private String  journal;
  private boolean isSetJournal = false;
  
  private String  volume;
  private boolean isSetVolume = false;
  
  private String  issue;
  private boolean isSetIssue = false;
  
  private Integer page;
  private boolean isSetPage = false;
  
  private Integer approvalMode;
  private boolean isSetApprovalMode = false;
  
  private String  website;
  private boolean isSetWebsite = false;
  
  private Integer referenceType; // TODO: should use a ReferenceType enum
  private boolean isSetReferenceType = false;
  
  private String  comment;
  private boolean isSetComment = false;


  /** Creates a {@link Reference} instance. */
  public Reference() {
    super();
    this.packageName = PMFConstants.shortLabel;
  }


  /** Creates a {@link Reference} instance with a level and version. */
  public Reference(int level, int version) {
    super(level, version);
    this.packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor. */
  public Reference(Reference reference) {
    super(reference);
  }


  /** Clones this class. */
  @Override
  public Reference clone() {
    return new Reference(this);
  }


  // *** author ***
  /**
   * Returns author.
   *
   * @return author.
   */
  public String getAuthor() {
    return author;
  }


  /**
   * Returns whether author is set.
   *
   * @return whether author is set.
   */
  public boolean isSetAuthor() {
    return isSetAuthor;
  }


  /**
   * Sets author.
   *
   * @param author
   */
  public void setAuthor(String author) {
    String oldAuthor = this.author;
    this.author = author;
    firePropertyChange("author", oldAuthor, this.author);
    isSetAuthor = true;
  }


  /**
   * Unsets the variable author.
   *
   * @return {@code true}, if author was set before, otherwise {@code false}.
   */
  public boolean unsetAuthor() {
    if (isSetAuthor()) {
      String oldAuthor = author;
      author = null;
      firePropertyChange("author", oldAuthor, author);
      isSetAuthor = false;
      return true;
    }
    return false;
  }


  // *** year ***
  /**
   * Returns year.
   *
   * @return year.
   */
  public int getYear() {
    if (isSetYear()) {
      return year.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("year", this);
  }


  /**
   * Returns whether year is set.
   *
   * @return whether year is set.
   */
  public boolean isSetYear() {
    return isSetYear;
  }


  /**
   * Sets year.
   *
   * @param year.
   */
  public void setYear(int year) {
    Integer oldYear = this.year;
    this.year = Integer.valueOf(year);
    firePropertyChange("year", oldYear, this.year);
    isSetYear = true;
  }


  /**
   * Unsets year.
   *
   * @return {@code true}, if year was set before, otherwise {@code false}.
   */
  public boolean unsetYear() {
    if (isSetYear()) {
      Integer oldYear = year;
      year = null;
      firePropertyChange("year", oldYear, year);
      isSetYear = false;
      return true;
    }
    return false;
  }


  // *** title ***
  /**
   * Returns title.
   *
   * @return title.
   */
  public String getTitle() {
    return title;
  }


  /**
   * Returns whether title is set.
   *
   * @return whether title is set.
   */
  public boolean isSetTitle() {
    return isSetTitle;
  }


  /**
   * Sets title.
   *
   * @param title.
   */
  public void setTitle(String title) {
    String oldTitle = this.title;
    this.title = title;
    firePropertyChange("title", oldTitle, this.title);
    isSetTitle = true;
  }


  /**
   * Unsets the variable title.
   *
   * @return {@code true}, if title was set before, otherwise {@code false}.
   */
  public boolean unsetTitle() {
    if (isSetTitle()) {
      String oldTitle = title;
      title = null;
      firePropertyChange("title", oldTitle, title);
      isSetTitle = false;
      return true;
    }
    return false;
  }


  // *** abstract ***
  /**
   * Returns abstract.
   *
   * @return abstract.
   */
  public String getAbstractText() {
    return abstractText;
  }


  /**
   * Returns whether abstractText is set.
   *
   * @return whether abstractText is set.
   */
  public boolean isSetAbstractText() {
    return isSetAbstractText;
  }


  /**
   * Sets abstractText.
   *
   * @param abstractText.
   */
  public void setAbstractText(String abstractText) {
    String oldAbstractText = this.abstractText;
    this.abstractText = abstractText;
    firePropertyChange("abstractText", oldAbstractText, this.abstractText);
    isSetAbstractText = true;
  }


  /**
   * Unsets the abstractText.
   *
   * @return {@code true}, if abstractText was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetAbstractText() {
    if (isSetAbstractText()) {
      String oldAbstractText = abstractText;
      abstractText = null;
      firePropertyChange("abstractText", oldAbstractText, abstractText);
      isSetAbstractText = false;
      return true;
    }
    return false;
  }


  // *** journal ***
  /**
   * Returns journal.
   *
   * @return journal.
   */
  public String getJournal() {
    return journal;
  }


  /**
   * Returns whether journal is set.
   *
   * @return whether journal is set.
   */
  public boolean isSetJournal() {
    return isSetJournal;
  }


  /**
   * Sets journal.
   *
   * @param journal.
   */
  public void setJournal(String journal) {
    String oldJournal = this.journal;
    this.journal = journal;
    firePropertyChange("journal", oldJournal, this.journal);
    isSetJournal = true;
  }


  /**
   * Unsets the journal.
   *
   * @return {@code true}, if journal was set before, otherwise {@code false}.
   */
  public boolean unsetJournal() {
    if (isSetJournal()) {
      String oldJournal = journal;
      journal = null;
      firePropertyChange("journal", oldJournal, journal);
      isSetJournal = false;
      return true;
    }
    return false;
  }


  // *** volume ***
  /**
   * Returns volume.
   *
   * @return volume.
   */
  public String getVolume() {
    return volume;
  }


  /**
   * Returns whether volume is set.
   *
   * @return whether volume is set.
   */
  public boolean isSetVolume() {
    return isSetVolume;
  }


  /**
   * Sets volume.
   *
   * @param volume.
   */
  public void setVolume(String volume) {
    String oldVolume = this.volume;
    this.volume = volume;
    firePropertyChange("volume", oldVolume, this.volume);
    isSetVolume = true;
  }


  /**
   * Unsets the volume.
   *
   * @return {@code true}, if volume was set before, otherwise {@code false}.
   */
  public boolean unsetVolume() {
    if (isSetVolume()) {
      String oldVolume = volume;
      volume = null;
      firePropertyChange("volume", oldVolume, volume);
      isSetVolume = false;
      return true;
    }
    return false;
  }


  // *** issue ***
  /**
   * Returns issue.
   *
   * @return issue.
   */
  public String getIssue() {
    return issue;
  }


  /**
   * Returns whether issue is set.
   *
   * @return whether issue is set.
   */
  public boolean isSetIssue() {
    return isSetIssue;
  }


  /**
   * Sets issue.
   *
   * @param issue.
   */
  public void setIssue(String issue) {
    String oldIssue = this.issue;
    this.issue = issue;
    firePropertyChange("issue", oldIssue, this.issue);
    isSetIssue = true;
  }


  /**
   * Unsets the issue.
   *
   * @return {@code true}, if issue was set before, otherwise {@code false}.
   */
  public boolean unsetIssue() {
    if (isSetIssue()) {
      String oldIssue = issue;
      issue = null;
      firePropertyChange("issue", oldIssue, issue);
      isSetIssue = false;
      return true;
    }
    return false;
  }


  // *** page ***
  /**
   * Returns page.
   *
   * @return page.
   */
  public int getPage() {
    if (isSetPage()) {
      return page.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("page", this);
  }


  /**
   * Returns whether page is set.
   *
   * @return whether page is set.
   */
  public boolean isSetPage() {
    return isSetPage;
  }


  /**
   * Sets year.
   *
   * @param year.
   */
  public void setPage(int page) {
    Integer oldPage = this.page;
    this.page = Integer.valueOf(page);
    firePropertyChange("page", oldPage, this.page);
    isSetPage = true;
  }


  /**
   * Unsets page.
   *
   * @return {@code true}, if page was set before, otherwise {@code false}.
   */
  public boolean unsetPage() {
    if (isSetPage()) {
      Integer oldPage = page;
      page = null;
      firePropertyChange("page", oldPage, page);
      isSetPage = false;
      return true;
    }
    return false;
  }


  // *** approvalMode ***
  /**
   * Returns approval mode.
   *
   * @return approval mode.
   */
  public int getApprovalMode() {
    if (isSetApprovalMode()) {
      return approvalMode.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("approvalMode", this);
  }


  /**
   * Returns whether approvalMode is set.
   *
   * @return whether approvalMode is set.
   */
  public boolean isSetApprovalMode() {
    return isSetApprovalMode;
  }


  /**
   * Sets approval mode.
   *
   * @param approval
   *        mode.
   */
  public void setApprovalMode(int approvalMode) {
    Integer oldApprovalMode = this.approvalMode;
    this.approvalMode = Integer.valueOf(approvalMode);
    firePropertyChange("approvalMode", oldApprovalMode, this.approvalMode);
    isSetApprovalMode = true;
  }


  /**
   * Unsets approval mode.
   *
   * @return {@code true}, if approval mode was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetApprovalMode() {
    if (isSetApprovalMode()) {
      Integer oldApprovalMode = approvalMode;
      approvalMode = null;
      firePropertyChange("approvalMode", oldApprovalMode, approvalMode);
      isSetApprovalMode = false;
      return true;
    }
    return false;
  }


  // *** website ***
  /**
   * Returns website.
   *
   * @return website.
   */
  public String getWebsite() {
    return website;
  }


  /**
   * Returns whether website is set.
   *
   * @return whether website is set.
   */
  public boolean isSetWebsite() {
    return isSetWebsite;
  }


  /**
   * Sets website.
   *
   * @param website.
   */
  public void setWebsite(String website) {
    String oldWebsite = this.website;
    this.website = website;
    firePropertyChange("website", oldWebsite, this.website);
    isSetWebsite = true;
  }


  /**
   * Unsets the website.
   *
   * @return {@code true}, if website was set before, otherwise {@code false}.
   */
  public boolean unsetWebsite() {
    if (isSetWebsite()) {
      String oldWebsite = website;
      website = null;
      firePropertyChange("website", oldWebsite, website);
      isSetWebsite = false;
      return true;
    }
    return false;
  }


  // *** reference type ***
  /**
   * Returns reference type.
   *
   * @return reference type.
   */
  public int getReferenceType() {
    if (isSetReferenceType()) {
      return referenceType.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("referenceType", this);
  }


  /**
   * Returns whether reference type is set.
   *
   * @return whether reference type is set.
   */
  public boolean isSetReferenceType() {
    return isSetReferenceType;
  }


  /**
   * Sets reference type.
   *
   * @param referenceType
   */
  public void setReferenceType(int referenceType) {
    Integer oldReferenceType = this.referenceType;
    this.referenceType = Integer.valueOf(referenceType);
    firePropertyChange("referenceType", oldReferenceType, this.referenceType);
    isSetReferenceType = true;
  }


  /**
   * Unsets reference type.
   *
   * @return {@code true}, if reference type was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetReferenceType() {
    if (isSetReferenceType()) {
      Integer oldReferenceType = referenceType;
      referenceType = null;
      firePropertyChange("referenceType", oldReferenceType, referenceType);
    isSetReferenceType = false;
      return true;
    }
    return false;
  }


  // *** comment ***
  /**
   * Returns comment.
   *
   * @return comment.
   */
  public String getComment() {
    return comment;
  }


  /**
   * Returns whether comment is set.
   *
   * @return whether comment is set.
   */
  public boolean isSetComment() {
    return isSetComment;
  }


  /**
   * Sets comment.
   *
   * @param comment.
   */
  public void setComment(String comment) {
    String oldComment = this.comment;
    this.comment = comment;
    firePropertyChange("comment", oldComment, this.comment);
    isSetComment = true;
  }


  /**
   * Unsets the comment.
   *
   * @return {@code true}, if comment was set before, otherwise {@code false}.
   */
  public boolean unsetComment() {
    if (isSetComment()) {
      String oldComment = comment;
      comment = null;
      firePropertyChange("comment", oldComment, comment);
      isSetComment = false;
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String,
   * javal.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    switch (attributeName) {
      case "AU": setAuthor(value); return true;
      case "PY": setYear(StringTools.parseSBMLInt(value)); return true;
      case "TI": setTitle(value); return true;
      case "AB": setAbstractText(value); return true;
      case "T2": setJournal(value); return true;
      case "VL": setVolume(value); return true;
      case "IS": setIssue(value); return true;
      case "SP": setPage(StringTools.parseSBMLInt(value)); return true;
      case "LB": setApprovalMode(StringTools.parseSBMLInt(value)); return true;
      case "UR": setWebsite(value); return true;
      case "M3": setReferenceType(StringTools.parseSBMLInt(value)); return true;
      case "N1": setComment(value); return true;
      default: return false;
    }
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();

    if (isSetAuthor()) {
      attributes.put("AU", author);
    }
    if (isSetYear()) {
      attributes.put("PY", StringTools.toString(Locale.ENGLISH, year.intValue()));
    }
    if (isSetTitle()) {
      attributes.put("TI", title);
    }
    if (isSetAbstractText()) {
      attributes.put("AB", abstractText);
    }
    if (isSetJournal()) {
      attributes.put("T2", journal);
    }
    if (isSetVolume()) {
      attributes.put("VL", volume);
    }
    if (isSetIssue()) {
      attributes.put("IS", issue);
    }
    if (isSetPage()) {
      attributes.put("SP", StringTools.toString(Locale.ENGLISH, page.intValue()));
    }
    if (isSetApprovalMode()) {
      attributes.put("LB", StringTools.toString(Locale.ENGLISH, approvalMode.intValue()));
    }
    if (isSetWebsite()) {
      attributes.put("UR", website);
    }
    if (isSetReferenceType()) {
      attributes.put("M3", StringTools.toString(Locale.ENGLISH, referenceType.intValue()));
    }
    if (isSetComment()) {
      attributes.put("UR", comment);
    }

    return attributes;
  }


  @Override
  public String toString() {
    return String.format("%s_%s_%s", author, year, title);
  }
}
