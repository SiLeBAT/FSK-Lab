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

public class EMLiteratureItem extends LiteratureItem implements PmmXmlElementConvertable {

	public static final String ELEMENT_LITERATURE = "EstimatedModelLiterature";

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
		this(XmlHelper.getString(el, "author"), XmlHelper.getInt(el, "year"), XmlHelper.getString(el, "title"),
				XmlHelper.getString(el, "abstract"), XmlHelper.getString(el, "journal"),
				XmlHelper.getString(el, "volume"), XmlHelper.getString(el, "issue"), XmlHelper.getInt(el, "page"),
				XmlHelper.getInt(el, "approvalMode"), XmlHelper.getString(el, "website"),
				XmlHelper.getInt(el, "type"), XmlHelper.getString(el, "comment"), XmlHelper.getInt(el, "id"),
				XmlHelper.getString(el, "dbuuid"));
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
}