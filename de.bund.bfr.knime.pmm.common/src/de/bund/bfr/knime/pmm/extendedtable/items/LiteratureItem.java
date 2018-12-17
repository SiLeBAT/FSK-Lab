/*******************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
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

public class LiteratureItem implements PmmXmlElementConvertable {

	public enum Type {
		EM, MD, M
	}

	public final Type litType;

	public Integer id;
	public String author;
	public String title;
	public String abstractText;
	public Integer year;
	public String journal;
	public String volume;
	public String issue;
	public Integer page;
	public Integer approvalMode;
	public String website;
	public Integer type;
	public String comment;
	public String dbuuid;

	/** Fully parameterized constructor. */
	public LiteratureItem(Type libType, final String author, final Integer year, final String title,
			final String abstractText, final String journal, final String volume, final String issue,
			final Integer page, final Integer approvalMode, final String website, final Integer type,
			final String comment, final Integer id, String dbuuid) {
		
		this.litType = libType;

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
	public LiteratureItem(Type litType, final String author, final Integer year, final String title,
			final String abstractText, final String journal, final String volume, final String issue,
			final Integer page, final Integer approvalMode, final String website, final Integer type,
			final String comment, final Integer id) {
		this(litType, author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type,
				comment, id, null);
	}

	/**
	 * Constructor with author, year, title, abstractText, journal, volume, issue,
	 * page, approvalMode, website, type and comment.
	 * <ul>
	 * <li>id is given a random negative int.
	 * <li>dbuuid is null.
	 * </ul>
	 */
	public LiteratureItem(Type libType, final String author, final Integer year, final String title,
			final String abstractText, final String journal, final String volume, final String issue,
			final Integer page, final Integer approvalMode, final String website, final Integer type,
			final String comment) {
		this(libType, author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type,
				comment, MathUtilities.getRandomNegativeInt(), null);
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
	public LiteratureItem(final Element el) {
		
		String name = el.getName();
		if (name.equals("EstimatedModelLiterature")) {
			this.litType = Type.EM;
		} else if (name.equals("MDLiteratureItem")) {
			this.litType = Type.MD;
		} else if (name.equals("MLiteratureItem")) {
			this.litType = Type.M;
		} else {
			throw new RuntimeException("Invalid LiteratureItem " + name);
		}
		
		this.id = XmlHelper.getInt(el, "id");
		this.author = XmlHelper.getString(el, "author");
		this.title = XmlHelper.getString(el, "title");
		this.abstractText = XmlHelper.getString(el, "abstract");
		this.year = XmlHelper.getInt(el, "year");
		this.journal = XmlHelper.getString(el, "journal");
		this.volume = XmlHelper.getString(el, "volume");
		this.issue = XmlHelper.getString(el, "issue");
		this.page = XmlHelper.getInt(el, "page");
		this.approvalMode = XmlHelper.getInt(el, "approvalMode");
		this.website = XmlHelper.getString(el, "website");
		this.type = XmlHelper.getInt(el, "type");
		this.comment = XmlHelper.getString(el, "comment");
		this.dbuuid = XmlHelper.getString(el, "dbuuid");
	}

	public LiteratureItem(final LiteratureItem lit) {
		this(lit.litType, lit.author, lit.year, lit.title, lit.abstractText, lit.journal, lit.volume, lit.issue, lit.page,
				lit.approvalMode, lit.website, lit.type, lit.comment, lit.id, lit.dbuuid);
	}

	public String getElementName() {
		String name = "";
		if (litType == Type.EM) {
			name = "EstimatedModelLiterature";
		} else if (litType == Type.MD) {
			name = "MDLiteratureItem";
		} else if (litType == Type.M) {
			name = "MLiteratureItem";
		}
		
		return name;
	}

	/**
	 * Generate a {@link org.jdom2.Element} with properties:
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
	 * 
	 * <p>
	 * The element name depends of the type:
	 * <ul>
	 * <li>{@link Type#EM} -> "EstimatedModelLiterature"
	 * <li>{@link Type#MD} -> "MDLiteratureItem"
	 * <li>{@link Type#M} -> "MLiteratureItem"
	 * </ul>
	 * </p>
	 */
	public Element toXmlElement() {
		Element ret = new Element(getElementName());

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
