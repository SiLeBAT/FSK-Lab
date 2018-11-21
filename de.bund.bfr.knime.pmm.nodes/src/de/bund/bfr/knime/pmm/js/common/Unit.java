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

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Unit implements ViewValue {

	// variables
	public Integer id;
	public String unit;
	public String description;
	public String name;
	public String kind_of_property_quantity;
	public String notation_case_sensitive;
	public String convert_to;
	public String conversion_function_factor;
	public String inverse_conversion_function_factor;
	public String object_type;
	public String display_in_GUI_as;
	public String mathML_string;
	public String priority_for_display_in_GUI;

	/**
	 * Saves unit properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "unit"
	 * <li>String "description"
	 * <li>String "name"
	 * <li>String "kindOfPropertyQuantity"
	 * <li>String "notationCaseSensitive"
	 * <li>String "convertTo"
	 * <li>String "conversionFunctionFactor"
	 * <li>String "inversionConversionFunctionFactor"
	 * <li>String "objectType"
	 * <li>String "displayInGuiAs"
	 * <li>String "mathmlString"
	 * <li>String "priorityForDisplayInGui"
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link Unit} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt("id", id, settings);
		SettingsHelper.addString("unit", unit, settings);
		SettingsHelper.addString("description", description, settings);
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("kindOfPropertyQuantity", kind_of_property_quantity, settings);
		SettingsHelper.addString("notationCaseSensitive", notation_case_sensitive, settings);
		SettingsHelper.addString("convertTo", convert_to, settings);
		SettingsHelper.addString("conversionFunctionFactor", conversion_function_factor, settings);
		SettingsHelper.addString("inversionConversionFunctionFactor", inverse_conversion_function_factor, settings);
		SettingsHelper.addString("objectType", object_type, settings);
		SettingsHelper.addString("displayInGuiAs", display_in_GUI_as, settings);
		SettingsHelper.addString("mathmlString", mathML_string, settings);
		SettingsHelper.addString("priorityForDisplayInGui", priority_for_display_in_GUI, settings);
	}

	/**
	 * Loads unit properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            The settings where to load the {@link Unit} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("id", settings);
		unit = SettingsHelper.getString("unit", settings);
		description = SettingsHelper.getString("description", settings);
		name = SettingsHelper.getString("name", settings);
		kind_of_property_quantity = SettingsHelper.getString("kindOfPropertyQuantity", settings);
		notation_case_sensitive = SettingsHelper.getString("notationCaseSensitive", settings);
		convert_to = SettingsHelper.getString("convertTo", settings);
		conversion_function_factor = SettingsHelper.getString("conversionFunctionFactor", settings);
		inverse_conversion_function_factor = SettingsHelper.getString("inversionConversionFunctionFactor", settings);
		object_type = SettingsHelper.getString("objectType", settings);
		display_in_GUI_as = SettingsHelper.getString("displayInGuiAs", settings);
		mathML_string = SettingsHelper.getString("mathmlString", settings);
		priority_for_display_in_GUI = SettingsHelper.getString("priorityForDisplayInGui", settings);
	}
}
