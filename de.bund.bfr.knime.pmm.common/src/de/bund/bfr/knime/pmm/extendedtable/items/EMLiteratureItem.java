/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.extendedtable.items;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlHelper;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class EMLiteratureItem implements LiteratureItemI, PmmXmlElementConvertable {

	public static final String ELEMENT_LITERATURE = "EstimatedModelLiterature";

	private static final String ATT_AUTHOR = "author";
	private static final String ATT_YEAR = "year";
	private static final String ATT_TITLE = "title";
	private static final String ATT_ABSTRACT = "abstract";
	private static final String ATT_JOURNAL = "journal";
	private static final String ATT_VOLUME = "volume";
	private static final String ATT_ISSUE = "issue";
	private static final String ATT_PAGE = "page";
	private static final String ATT_APPROVAL_MODE = "approvalMode";
	private static final String ATT_WEBSITE = "website";
	private static final String ATT_TYPE = "type";
	private static final String ATT_COMMENT = "comment";
	private static final String ATT_ID = "id";
	private static final String ATT_DBUUID = "dbuuid";

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

	/** Fully parameterized constructor. */
	public EMLiteratureItem(final String author, final Integer year, final String title, final String abstractText,
			final String journal, final String volume, final String issue, final Integer page,
			final Integer approvalMode, final String website, final Integer type, final String comment,
			final Integer id, String dbuuid) {

		this.author = author;
		this.year = year;
		this.title = title;
		this.abstractText = abstractText;
		this.journal = journal;
		this.volume = volume;
		this.issue = issue;
		this.page = page;
		this.approvalMode = approvalMode;
		this.website = website;
		this.type = type;
		this.comment = comment;
		this.id = id;
		this.dbuuid = dbuuid;
	}

	/**
	 * Constructor with author, year, title, abstractText, journal, volume, issue,
	 * page, approvalMode, website, type, comment and id. dbuuid is null.
	 */
	public EMLiteratureItem(final String author, final Integer year, final String title, final String abstractText,
			final String journal, final String volume, final String issue, final Integer page,
			final Integer approvalMode, final String website, final Integer type, final String comment,
			final Integer id) {
		this(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type, comment, id,
				null);
	}

	/**
	 * Constructor with author, year, title, abstractText, journal, volume, issue,
	 * page, approvalMode, website, type and comment.
	 * <ul>
	 * <li>id is given a random negative int.
	 * <li>dbuuid is null.
	 * </ul>
	 */
	public EMLiteratureItem(final String author, final Integer year, final String title, final String abstractText,
			final String journal, final String volume, final String issue, final Integer page,
			final Integer approvalMode, final String website, final Integer type, final String comment) {
		this(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type, comment,
				MathUtilities.getRandomNegativeInt(), null);
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with
	 * properties:
	 * <ul>
	 * <li>String "author"
	 * <li>Integer "year"
	 * <li>String "title"
	 * <li>String "abstract"
	 * <li>String "journal"
	 * <li>String "volume"
	 * <li>String "issue"
	 * <li>Integer "page"
	 * <li>Integer "approvalMode"
	 * <li>String "website"
	 * <li>Integer "type"
	 * <li>String "comment"
	 * <li>Integer "id"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public EMLiteratureItem(final Element el) {
		this(XmlHelper.getString(el, ATT_AUTHOR), XmlHelper.getInt(el, ATT_YEAR), XmlHelper.getString(el, ATT_TITLE),
				XmlHelper.getString(el, ATT_ABSTRACT), XmlHelper.getString(el, ATT_JOURNAL),
				XmlHelper.getString(el, ATT_VOLUME), XmlHelper.getString(el, ATT_ISSUE), XmlHelper.getInt(el, ATT_PAGE),
				XmlHelper.getInt(el, ATT_APPROVAL_MODE), XmlHelper.getString(el, ATT_WEBSITE),
				XmlHelper.getInt(el, ATT_TYPE), XmlHelper.getString(el, ATT_COMMENT), XmlHelper.getInt(el, ATT_ID),
				XmlHelper.getString(el, ATT_DBUUID));
	}

	/** Copy constructor. */
	public EMLiteratureItem(final LiteratureItemI lit) {
		this(lit.getAuthor(), lit.getYear(), lit.getTitle(), lit.getAbstractText(), lit.getJournal(), lit.getVolume(),
				lit.getIssue(), lit.getPage(), lit.getApprovalMode(), lit.getWebsite(), lit.getType(), lit.getComment(),
				lit.getId(), lit.getDbuuid());
	}

	public String getElementName() {
		return "EstimatedModelLiterature";
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "EstimatedModelLiterature" and properties:
	 * <ul>
	 * <li>String "author"
	 * <li>Integer "year"
	 * <li>String "title"
	 * <li>String "abstract"
	 * <li>String "journal"
	 * <li>String "volume"
	 * <li>String "issue"
	 * <li>Integer "page"
	 * <li>Integer "approvalMode"
	 * <li>String "website"
	 * <li>Integer "type"
	 * <li>String "comment"
	 * <li>Integer "id"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_LITERATURE);

		ret.setAttribute(ATT_AUTHOR, XmlHelper.getNonNull(author));
		ret.setAttribute(ATT_YEAR, XmlHelper.getNonNull(year));
		ret.setAttribute(ATT_TITLE, XmlHelper.removeDirt(title));
		ret.setAttribute(ATT_ABSTRACT, XmlHelper.removeDirt(abstractText));
		ret.setAttribute(ATT_JOURNAL, XmlHelper.getNonNull(journal));
		ret.setAttribute(ATT_VOLUME, XmlHelper.getNonNull(volume));
		ret.setAttribute(ATT_ISSUE, XmlHelper.getNonNull(issue));
		ret.setAttribute(ATT_PAGE, XmlHelper.getNonNull(page));
		ret.setAttribute(ATT_APPROVAL_MODE, XmlHelper.getNonNull(approvalMode));
		ret.setAttribute(ATT_WEBSITE, XmlHelper.getNonNull(website));
		ret.setAttribute(ATT_TYPE, XmlHelper.getNonNull(type));
		ret.setAttribute(ATT_COMMENT, XmlHelper.getNonNull(comment));
		ret.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		ret.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return ret;
	}

	@Override
	public String toString() {
		return author + "_" + year + "_" + title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getApprovalMode() {
		return approvalMode;
	}

	public void setApprovalMode(Integer approvalMode) {
		this.approvalMode = approvalMode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}