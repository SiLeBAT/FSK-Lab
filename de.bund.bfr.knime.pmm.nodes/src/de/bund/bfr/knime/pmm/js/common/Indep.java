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
package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.IndepXml;

/**
 * PmmLab independent variable. Holds:
 * <ul>
 * <li>name
 * <li>original name
 * <li>min
 * <li>max
 * <li>category
 * <li>unit
 * <li>description
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Indep implements ViewValue {

	// Configuration keys
	static final String NAME = "name";
	static final String ORIGNAME = "origname";
	static final String MIN = "min";
	static final String MAX = "max";
	static final String CATEGORY = "category";
	static final String UNIT = "unit";
	static final String DESCRIPTION = "description";

	private String name;
	private String origname;
	private Double min;
	private Double max;
	private String category;
	private String unit;
	private String description;

	/**
	 * Returns the name of this {@link Indep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link Indep}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the original name of this {@link Indep}.
	 * 
	 * If not set returns null.
	 *
	 * @return the original name of this {@link Indep}
	 */
	public String getOrigname() {
		return origname;
	}

	/**
	 * Returns the minimum value of this {@link Indep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the minimum value of this {@link Indep}
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Returns the maximum value of this {@link Indep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the maximum value of this {@link Indep}
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Returns the category of this {@link Indep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the category of this {@link Indep}
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the unit of this {@link Indep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the unit of this {@link Indep}
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Indep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the description of this {@link Indep}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the name of this {@link Indep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param name
	 *            the name to be set
	 */
	public void setName(String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the original name of this {@link Indep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param origname
	 *            the original name to be set
	 */
	public void setOrigname(String origname) {
		this.origname = Strings.emptyToNull(origname);
	}

	/**
	 * Sets the minimum value of this {@link Indep}.
	 * 
	 * @param min
	 *            the minimum value to be set
	 */
	public void setMin(Double min) {
		this.min = min;
	}

	/**
	 * Sets the maximum value of this {@link Indep}.
	 * 
	 * @param max
	 *            the maximum value to be set
	 */
	public void setMax(Double max) {
		this.max = max;
	}

	/**
	 * Sets the category of this {@link Indep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param category
	 *            the category to be set
	 */
	public void setCategory(String category) {
		this.category = Strings.emptyToNull(category);
	}

	/**
	 * Sets the unit of this {@link Indep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param unit
	 *            the category to be set
	 */
	public void setUnit(String unit) {
		this.unit = Strings.emptyToNull(unit);
	}

	/**
	 * Sets the description of this {@link Indep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param description
	 *            the description to be set
	 */
	public void setDescription(String description) {
		this.description = Strings.emptyToNull(description);
	}

	/**
	 * Saves indep properties into a {@link NodeSettingsWO}.
	 * 
	 * @param settings
	 *            where to save the {@link Indep} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(ORIGNAME, origname, settings);
		SettingsHelper.addDouble(MIN, min, settings);
		SettingsHelper.addDouble(MAX, max, settings);
		SettingsHelper.addString(CATEGORY, category, settings);
		SettingsHelper.addString(UNIT, unit, settings);
		SettingsHelper.addString(DESCRIPTION, description, settings);
	}

	/**
	 * Loads indep properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            the settings where to load the {@link Indep} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString(NAME, settings);
		origname = SettingsHelper.getString(ORIGNAME, settings);
		min = SettingsHelper.getDouble(MIN, settings);
		max = SettingsHelper.getDouble(MAX, settings);
		category = SettingsHelper.getString(CATEGORY, settings);
		unit = SettingsHelper.getString(UNIT, settings);
		description = SettingsHelper.getString(DESCRIPTION, settings);
	}

	/**
	 * Creates an Indep from an IndepXml.
	 * 
	 * @param indepXml
	 */
	public static Indep toIndep(IndepXml indepXml) {
		Indep indep = new Indep();
		indep.setName(indepXml.getName());
		indep.setOrigname(indepXml.getOrigName());
		if (indepXml.getMin() != null)
			indep.setMin(indepXml.getMin());
		if (indepXml.getMax() != null)
			indep.setMax(indepXml.getMax());
		indep.setCategory(indepXml.getCategory());
		indep.setUnit(indepXml.getUnit());
		indep.setDescription(indepXml.getDescription());

		return indep;
	}

	public IndepXml toIndepXml() {
		return new IndepXml(name, origname, min, max, category, unit, description);
	}
}
