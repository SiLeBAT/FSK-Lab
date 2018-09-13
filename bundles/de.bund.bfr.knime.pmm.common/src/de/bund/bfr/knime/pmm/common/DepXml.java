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

public class DepXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_DEPENDENT = "dependent";

	private static final String ATT_NAME = "name";
	private static final String ATT_ORIGNAME = "origname";
	private static final String ATT_MIN = "min";
	private static final String ATT_MAX = "max";
	private static final String ATT_CATEGORY = "category";
	private static final String ATT_UNIT = "unit";
	private static final String ATT_DESCRIPTION = "description";

	private String name;
	private String origName;
	private Double min;
	private Double max;
	private String category;
	private String unit;
	private String description;

	public DepXml(String name) {
		this(name, null, null);
	}

	public DepXml(String name, String category, String unit) {
		this(name, name, category, unit, null);
	}

	public DepXml(String name, String origName, String category, String unit,
			String description) {
		this.name = name;
		this.origName = origName;
		this.category = category;
		this.unit = unit;
		this.description = description;
	}

	public DepXml(Element el) {
		this(XmlHelper.getString(el, ATT_NAME), XmlHelper.getString(el,
				ATT_ORIGNAME), XmlHelper.getString(el, ATT_CATEGORY), XmlHelper
				.getString(el, ATT_UNIT), XmlHelper.getString(el,
				ATT_DESCRIPTION));
		this.setMin(XmlHelper.getDouble(el, ATT_MIN));
		this.setMax(XmlHelper.getDouble(el, ATT_MAX));
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_DEPENDENT);

		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_ORIGNAME, XmlHelper.getNonNull(origName));
		ret.setAttribute(ATT_MIN, XmlHelper.getNonNull(min));
		ret.setAttribute(ATT_MAX, XmlHelper.getNonNull(max));
		ret.setAttribute(ATT_CATEGORY, XmlHelper.getNonNull(category));
		ret.setAttribute(ATT_UNIT, XmlHelper.getNonNull(unit));
		ret.setAttribute(ATT_DESCRIPTION, XmlHelper.getNonNull(description));

		return ret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
