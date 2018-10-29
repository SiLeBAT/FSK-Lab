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

public class MDLiteratureItem implements LiteratureItemI, PmmXmlElementConvertable {

	public static final String ELEMENT_LITERATURE = "MDLiteratureItem";

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
	public MDLiteratureItem(final String author, final Integer year, final String title, final String abstractText,
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
	public MDLiteratureItem(final String author, final Integer year, final String title, final String abstractText,
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
	public MDLiteratureItem(final String author, final Integer year, final String title, final String abstractText,
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
	public MDLiteratureItem(final Element el) {
		this(XmlHelper.getString(el, "author"), XmlHelper.getInt(el, "year"), XmlHelper.getString(el, "title"),
				XmlHelper.getString(el, "abstract"), XmlHelper.getString(el, "journal"),
				XmlHelper.getString(el, "volume"), XmlHelper.getString(el, "issue"), XmlHelper.getInt(el, "page"),
				XmlHelper.getInt(el, "approvalMode"), XmlHelper.getString(el, "website"),
				XmlHelper.getInt(el, "type"), XmlHelper.getString(el, "comment"), XmlHelper.getInt(el, "id"),
				XmlHelper.getString(el, "dbuuid"));
	}

	public MDLiteratureItem(final LiteratureItemI lit) {
		this(lit.getAuthor(), lit.getYear(), lit.getTitle(), lit.getAbstractText(), lit.getJournal(), lit.getVolume(),
				lit.getIssue(), lit.getPage(), lit.getApprovalMode(), lit.getWebsite(), lit.getType(), lit.getComment(),
				lit.getId(), lit.getDbuuid());
	}

	public String getElementName() {
		return "MDLiteratureItem";
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "MDLiteratureItem" and properties:
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
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_LITERATURE);

		ret.setAttribute("author", XmlHelper.getNonNull(author));
		ret.setAttribute("year", XmlHelper.getNonNull(year));
		ret.setAttribute("title", XmlHelper.removeDirt(title));
		ret.setAttribute("abstract", XmlHelper.removeDirt(abstractText));
		ret.setAttribute("journal", XmlHelper.getNonNull(journal));
		ret.setAttribute("volume", XmlHelper.getNonNull(volume));
		ret.setAttribute("issue", XmlHelper.getNonNull(issue));
		ret.setAttribute("page", XmlHelper.getNonNull(page));
		ret.setAttribute("approvalMode", XmlHelper.getNonNull(approvalMode));
		ret.setAttribute("website", XmlHelper.getNonNull(website));
		ret.setAttribute("type", XmlHelper.getNonNull(type));
		ret.setAttribute("comment", XmlHelper.getNonNull(comment));
		ret.setAttribute("id", XmlHelper.getNonNull(id));
		ret.setAttribute("dbuuid", XmlHelper.getNonNull(dbuuid));

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
