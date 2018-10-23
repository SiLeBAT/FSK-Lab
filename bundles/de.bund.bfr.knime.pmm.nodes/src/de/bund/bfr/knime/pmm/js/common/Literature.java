package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * PmmLab literature item. Holds:
 * <ul>
 * <li>author
 * <li>year
 * <li>title
 * <li>abstract
 * <li>journal
 * <li>volume
 * <li>issue
 * <li>page
 * <li>approval mode
 * <li>website
 * <li>type
 * <li>comment
 * <li>id
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Literature implements ViewValue {

	// Configuration keys
	static final String AUTHOR = "author";
	static final String YEAR = "year";
	static final String TITLE = "title";
	static final String ABSTRACT = "abstract";
	static final String JOURNAL = "journal";
	static final String VOLUME = "volume";
	static final String ISSUE = "issue";
	static final String PAGE = "page";
	static final String APPROVAL_MODE = "approvalMode";
	static final String WEBSITE = "website";
	static final String TYPE = "type";
	static final String COMMENT = "comment";
	static final String ID = "id";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String author;
	private String title;
	private String abstractText;
	private Integer year;
	private String journal;
	private String volume;
	private String issue;
	private Integer page;
	private Integer approvalMode;
	private String website;
	private Integer type;
	private String comment;
	private String dbuuid;

	/**
	 * Returns the id of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link Literature}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the author of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the author of this {@link Literature}
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Returns the title of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the title of this {@link Literature}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the abstract text of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the abstract text of this {@link Literature}
	 */
	public String getAbstractText() {
		return abstractText;
	}

	/**
	 * Returns the year of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the year of this {@link Literature}
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * Returns the journal of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the journal of this {@link Literature}
	 */
	public String getJournal() {
		return journal;
	}

	/**
	 * Returns the journal of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the journal of this {@link Literature}
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * Returns the issue of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the issue of this {@link Literature}
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * Returns the page of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the page of this {@link Literature}
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * Returns the approval mode of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the approval mode of this {@link Literature}
	 */
	public Integer getApprovalMode() {
		return approvalMode;
	}

	/**
	 * Returns the website of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the website of this {@link Literature}
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * Returns the type of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the type of this {@link Literature}
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * Returns the comment of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the comment of this {@link Literature}
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the DBUUID of this {@link Literature}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the DBUUID of this {@link Literature}
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/**
	 * Sets the id of this {@link Literature}.
	 * 
	 * @param id the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the author of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param author the author to be set
	 */
	public void setAuthor(final String author) {
		this.author = Strings.emptyToNull(author);
	}

	/**
	 * Sets the title of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param title the title to be set
	 */
	public void setTitle(final String title) {
		this.title = Strings.emptyToNull(title);
	}

	/**
	 * Sets the abstract text of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param abstractText the abstract to be set
	 */
	public void setAbstractText(final String abstractText) {
		this.abstractText = Strings.emptyToNull(abstractText);
	}

	/**
	 * Sets the year of this {@link Literature}.
	 * 
	 * @param year the year to be set
	 */
	public void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * Sets the journal of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param journal the journal to be set
	 */
	public void setJournal(final String journal) {
		this.journal = Strings.emptyToNull(journal);
	}

	/**
	 * Sets the volume of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param volume the volume to be set
	 */
	public void setVolume(final String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/**
	 * Sets the issue of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param issue the issue to be set
	 */
	public void setIssue(final String issue) {
		this.issue = Strings.emptyToNull(issue);
	}

	/**
	 * Sets the page of this {@link Literature}.
	 * 
	 * @param page the page to be set
	 */
	public void setPage(final Integer page) {
		this.page = page;
	}

	/**
	 * Sets the approval mode of this {@link Literature}.
	 * 
	 * @param approvalMode the approval mode to be set
	 */
	public void setApprovalMode(final Integer approvalMode) {
		this.approvalMode = approvalMode;
	}

	/**
	 * Sets the website of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param website the approval mode to be set
	 */
	public void setWebsite(final String website) {
		this.website = Strings.emptyToNull(website);
	}

	/**
	 * Sets the type of this {@link Literature}.
	 * 
	 * @param type the type to be set
	 */
	public void setType(final Integer type) {
		this.type = type;
	}

	/**
	 * Sets the comment of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param comment the approval mode to be set
	 */
	public void setComment(final String comment) {
		this.comment = Strings.emptyToNull(comment);
	}

	/**
	 * Sets the DBUUID of this {@link Literature}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param dbuuid the DBUUID to be set
	 */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.emptyToNull(dbuuid);
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(AUTHOR, author, settings);
		SettingsHelper.addString(TITLE, title, settings);
		SettingsHelper.addString(ABSTRACT, abstractText, settings);
		SettingsHelper.addInt(YEAR, year, settings);
		SettingsHelper.addString(JOURNAL, journal, settings);
		SettingsHelper.addString(VOLUME, volume, settings);
		SettingsHelper.addString(ISSUE, issue, settings);
		SettingsHelper.addInt(PAGE, page, settings);
		SettingsHelper.addInt(APPROVAL_MODE, approvalMode, settings);
		SettingsHelper.addString(WEBSITE, website, settings);
		SettingsHelper.addInt(TYPE, type, settings);
		SettingsHelper.addString(COMMENT, comment, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		author = SettingsHelper.getString(AUTHOR, settings);
		title = SettingsHelper.getString(TITLE, settings);
		abstractText = SettingsHelper.getString(ABSTRACT, settings);
		year = SettingsHelper.getInteger(YEAR, settings);
		journal = SettingsHelper.getString(JOURNAL, settings);
		volume = SettingsHelper.getString(VOLUME, settings);
		issue = SettingsHelper.getString(ISSUE, settings);
		page = SettingsHelper.getInteger(PAGE, settings);
		approvalMode = SettingsHelper.getInteger(APPROVAL_MODE, settings);
		website = SettingsHelper.getString(WEBSITE, settings);
		type = SettingsHelper.getInteger(TYPE, settings);
		comment = SettingsHelper.getString(COMMENT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}
	
	public static Literature toLiterature(LiteratureItem literatureItem) {
		Literature literature = new Literature();
		literature.setId(literatureItem.id);
		literature.setAuthor(literatureItem.author);
		literature.setTitle(literatureItem.title);
		literature.setAbstractText(literatureItem.abstractText);
		literature.setYear(literatureItem.year);
		literature.setJournal(literatureItem.journal);
		literature.setVolume(literatureItem.volume);
		literature.setIssue(literatureItem.issue);
		literature.setPage(literatureItem.page);
		literature.setApprovalMode(literatureItem.approvalMode);
		literature.setWebsite(literatureItem.website);
		literature.setType(literatureItem.type);
		literature.setComment(literatureItem.comment);
		literature.setDbuuid(literatureItem.dbuuid);
		
		return literature;
	}
	
	public LiteratureItem toLiteratureItem() {
		return new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type, comment, id, dbuuid);
	}
}
