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
	 * Saves indep properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>String"name"
	 * <li>String "origname"
	 * <li>Double "min"
	 * <li>Double "max"
	 * <li>String "category"
	 * <li>String "unit"
	 * <li>String "description"
	 * </ul>
	 * 
	 * @param settings
	 *            where to save the {@link Indep} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("origname", origname, settings);
		SettingsHelper.addDouble("min", min, settings);
		SettingsHelper.addDouble("max", max, settings);
		SettingsHelper.addString("category", category, settings);
		SettingsHelper.addString("unit", unit, settings);
		SettingsHelper.addString("description", description, settings);
	}

	/**
	 * Loads indep properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>String"name"
	 *            <li>String "origname"
	 *            <li>Double "min"
	 *            <li>Double "max"
	 *            <li>String "category"
	 *            <li>String "unit"
	 *            <li>String "description"
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString("name", settings);
		origname = SettingsHelper.getString("origname", settings);
		min = SettingsHelper.getDouble("min", settings);
		max = SettingsHelper.getDouble("max", settings);
		category = SettingsHelper.getString("category", settings);
		unit = SettingsHelper.getString("unit", settings);
		description = SettingsHelper.getString("description", settings);
	}

	/**
	 * Creates an Indep from an IndepXml.
	 * 
	 * @param indepXml
	 */
	public static Indep toIndep(IndepXml indepXml) {
		Indep indep = new Indep();
		indep.setName(indepXml.name);
		indep.setOrigname(indepXml.origName);
		if (indepXml.min != null)
			indep.setMin(indepXml.min);
		if (indepXml.max != null)
			indep.setMax(indepXml.max);
		indep.setCategory(indepXml.category);
		indep.setUnit(indepXml.unit);
		indep.setDescription(indepXml.description);

		return indep;
	}

	public IndepXml toIndepXml() {
		return new IndepXml(name, origname, min, max, category, unit, description);
	}
}
