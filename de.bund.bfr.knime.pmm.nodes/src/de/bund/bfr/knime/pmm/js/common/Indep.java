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

	public String name;
	public String origname;
	public Double min;
	public Double max;
	public String category;
	public String unit;
	public String description;

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
		indep.name = indepXml.name;
		indep.origname = indepXml.origName;
		indep.min = indepXml.min;
		indep.max = indepXml.max;
		indep.category = indepXml.category;
		indep.unit = indepXml.unit;
		indep.description = indepXml.description;

		return indep;
	}

	public IndepXml toIndepXml() {
		return new IndepXml(name, origname, min, max, category, unit, description);
	}
}
