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

import java.util.Arrays;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.common.MiscXml;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Misc implements ViewValue {

	public Integer id;
	public String name;
	public String description;
	public Double value;
	public String[] categories;
	public String unit;
	public String origUnit;
	public String dbuuid;

	/**
	 * Saves misc properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "description"
	 * <li>Double "value"
	 * <li>StringArray "category"
	 * <li>String "unit"
	 * <li>String "origUnit"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt("id", id, settings);
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("description", description, settings);
		SettingsHelper.addDouble("value", value, settings);
		settings.addStringArray("category", categories);
		SettingsHelper.addString("unit", unit, settings);
		SettingsHelper.addString("origUnit", origUnit, settings);
		SettingsHelper.addString("dbuuid", dbuuid, settings);
	}

	/**
	 * Loads misc properties from a {@link NodeSettingsRO} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "description"
	 * <li>Double "value"
	 * <li>StringArray "category"
	 * <li>String "unit"
	 * <li>String "origUnit"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("id", settings);
		name = SettingsHelper.getString("name", settings);
		description = SettingsHelper.getString("description", settings);
		value = SettingsHelper.getDouble("value", settings);
		try {
			categories = settings.getStringArray("category");
		} catch (final InvalidSettingsException e) {
			categories = null;
		}
		unit = SettingsHelper.getString("unit", settings);
		origUnit = SettingsHelper.getString("origUnit", settings);
		dbuuid = SettingsHelper.getString("dbuuid", settings);
	}

	/**
	 * Creates a Misc from a MiscXml.
	 *
	 * @param miscXml
	 */
	public static Misc toMisc(MiscXml miscXml) {
		final Misc misc = new Misc();
		misc.id = miscXml.id;
		misc.name = miscXml.name;
		misc.description = miscXml.description;
		misc.value = miscXml.value;
		misc.categories = miscXml.categories.toArray(new String[miscXml.categories.size()]);
		misc.unit = miscXml.unit;
		misc.origUnit = miscXml.origUnit;
		misc.dbuuid = miscXml.dbuuid;

		return misc;
	}

	/**
	 * Returns an equivalent MiscXml.
	 *
	 * @return an equivalent MiscXml
	 */
	public MiscXml toMiscXml() {
		return new MiscXml(id, name, description, value, Arrays.asList(categories), unit, origUnit, dbuuid);
	}
}
