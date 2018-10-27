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
package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

public class MdInfoXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MDINFO = "mdinfoxml";

	public Integer id;
	public String name;
	public String comment;
	public Integer qualityScore;
	public Boolean checked;

	/** Fully parameterized constructor. */
	public MdInfoXml(Integer id, String name, String comment, Integer qualityScore, Boolean checked) {
		this.id = id;
		this.name = name;
		this.comment = comment;
		this.qualityScore = qualityScore;
		this.checked = checked;
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with
	 * properties:
	 * <ul>
	 * <li>Integer "ID"
	 * <li>String "Name"
	 * <li>String "Comment"
	 * <li>Integer "QualityScore"
	 * <li>Boolean "Checked"
	 * </ul>
	 */
	public MdInfoXml(Element el) {
		this(XmlHelper.getInt(el, "ID"), XmlHelper.getString(el, "Name"), XmlHelper.getString(el, "Comment"),
				XmlHelper.getInt(el, "QualityScore"), XmlHelper.getBoolean(el, "Checked"));
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "mdinfoxml" and properties:
	 * <ul>
	 * <li>Integer "ID"
	 * <li>String "Name"
	 * <li>String "Comment"
	 * <li>Integer "QualityScore"
	 * <li>Boolean "Checked"
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_MDINFO);

		modelElement.setAttribute("ID", XmlHelper.getNonNull(id));
		modelElement.setAttribute("Name", XmlHelper.getNonNull(name));
		modelElement.setAttribute("Comment", XmlHelper.getNonNull(comment));
		modelElement.setAttribute("QualityScore", XmlHelper.getNonNull(qualityScore));
		modelElement.setAttribute("Checked", XmlHelper.getNonNull(checked));

		return modelElement;
	}
}
