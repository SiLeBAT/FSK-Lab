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

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_DESCRIPTION = "description";
	private static final String ATT_VALUE = "value";
	private static final String ATT_CATEGORY = "category";
	private static final String ATT_UNIT = "unit";
	private static final String ATT_ORIGUNIT = "origUnit";
	private static final String ATT_DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String description;
	private Double value;
	private List<String> categories;
	private String unit;
	private String origUnit;
	private String dbuuid;

	public MiscXml(MiscXml misc) {
		this(misc.getId(), misc.getName(), misc.getDescription(), misc
				.getValue(), misc.getCategories(), misc.getUnit(), misc
				.getOrigUnit(), misc.getDbuuid());
	}

	public MiscXml(Integer id, String name, String description, Double value,
			List<String> categories, String unit) {
		this(id, name, description, value, categories, unit, unit, null);
	}

	public MiscXml(Integer id, String name, String description, Double value,
			List<String> categories, String unit, String dbuuid) {
		this(id, name, description, value, categories, unit, unit, dbuuid);
	}

	public MiscXml(Integer id, String name, String description, Double value,
			List<String> categories, String unit, String origUnit, String dbuuid) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.value = value;
		this.categories = categories;
		this.unit = unit;
		this.origUnit = origUnit;
		this.dbuuid = dbuuid;
	}

	public MiscXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME),
				XmlHelper.getString(el, ATT_DESCRIPTION), XmlHelper.getDouble(
						el, ATT_VALUE), null,
				XmlHelper.getString(el, ATT_UNIT), XmlHelper.getString(el,
						ATT_ORIGUNIT), XmlHelper.getString(el, ATT_DBUUID));
		categories = new ArrayList<>();

		for (int i = 0;; i++) {
			if (el.getAttribute(ATT_CATEGORY + i) == null) {
				break;
			}

			categories.add(XmlHelper.getString(el, ATT_CATEGORY + i));
		}
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_MISC);

		ret.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_DESCRIPTION, XmlHelper.getNonNull(description));
		ret.setAttribute(ATT_VALUE, XmlHelper.getNonNull(value));
		ret.setAttribute(ATT_UNIT, XmlHelper.getNonNull(unit));
		ret.setAttribute(ATT_ORIGUNIT, XmlHelper.getNonNull(origUnit));
		ret.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		if (categories != null) {
			for (int i = 0; i < categories.size(); i++) {
				ret.setAttribute(ATT_CATEGORY + i,
						XmlHelper.getNonNull(categories.get(i)));
			}
		}

		return ret;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOrigUnit() {
		return origUnit;
	}

	public void setOrigUnit(String origUnit) {
		this.origUnit = origUnit;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}
