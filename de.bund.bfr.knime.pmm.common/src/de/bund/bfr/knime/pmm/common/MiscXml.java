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

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class MiscXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MISC = "misc";

	public Integer id;
	public String name;
	public String description;
	public Double value;
	public List<String> categories;
	public String unit;
	public String origUnit;
	public String dbuuid;

	/** Copy constructor. Take very property from another {@link MiscXml}. */
	public MiscXml(MiscXml misc) {
		this(misc.id, misc.name, misc.description, misc.value, misc.categories, misc.unit, misc.origUnit, misc.dbuuid);
	}

	/**
	 * Constructor with id, name, description, value, categories and unit.
	 * <ul>
	 * <li>origUnit is assigned unit
	 * <li>dbuuid is assigned null
	 * </ul>
	 */
	public MiscXml(Integer id, String name, String description, Double value, List<String> categories, String unit) {
		this(id, name, description, value, categories, unit, unit, null);
	}

	/**
	 * Constructor with id, name, description, value, categories, unit and dbuuid.
	 * origUnit is assigned unit.
	 */
	public MiscXml(Integer id, String name, String description, Double value, List<String> categories, String unit,
			String dbuuid) {
		this(id, name, description, value, categories, unit, unit, dbuuid);
	}

	/** Fully parameterized constructor. */
	public MiscXml(Integer id, String name, String description, Double value, List<String> categories, String unit,
			String origUnit, String dbuuid) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.value = value;
		this.categories = categories;
		this.unit = unit;
		this.origUnit = origUnit;
		this.dbuuid = dbuuid;
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with
	 * properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "description"
	 * <li>String "value"
	 * <li>String "unit"
	 * <li>String "origUnit"
	 * <li>String "dbuuid"
	 * <li>Multiple string properties for categories with prefix "category" and
	 * suffix <number of category>. E.g.: "category0", "category1", etc.
	 * </ul>
	 */
	public MiscXml(Element el) {
		this(XmlHelper.getInt(el, "id"), XmlHelper.getString(el, "name"), XmlHelper.getString(el, "description"),
				XmlHelper.getDouble(el, "value"), null, XmlHelper.getString(el, "unit"),
				XmlHelper.getString(el, "origUnit"), XmlHelper.getString(el, "dbuuid"));
		categories = new ArrayList<>();

		for (int i = 0;; i++) {
			if (el.getAttribute("category" + i) == null) {
				break;
			}

			categories.add(XmlHelper.getString(el, "category" + i));
		}
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "misc" and properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "description"
	 * <li>String "value"
	 * <li>String "unit"
	 * <li>String "origUnit"
	 * <li>String "dbuuid"
	 * <li>Multiple string properties for categories with prefix "category" and
	 * suffix <number of category>. E.g.: "category0", "category1", etc.
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_MISC);

		ret.setAttribute("id", XmlHelper.getNonNull(id));
		ret.setAttribute("name", XmlHelper.getNonNull(name));
		ret.setAttribute("description", XmlHelper.getNonNull(description));
		ret.setAttribute("value", XmlHelper.getNonNull(value));
		ret.setAttribute("unit", XmlHelper.getNonNull(unit));
		ret.setAttribute("origUnit", XmlHelper.getNonNull(origUnit));
		ret.setAttribute("dbuuid", XmlHelper.getNonNull(dbuuid));

		if (categories != null) {
			for (int i = 0; i < categories.size(); i++) {
				ret.setAttribute("category" + i, XmlHelper.getNonNull(categories.get(i)));
			}
		}

		return ret;
	}
}
