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

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Unit implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String UNIT = "unit";
	static final String DESCRIPTION = "description";
	static final String NAME = "name";
	static final String KIND_OF_PROPERTY_QUANTITY = "kindOfPropertyQuantity";
	static final String NOTATION_CASE_SENSITIVE = "notationCaseSensitive";
	static final String CONVERT_TO = "convertTo";
	static final String CONVERSION_FUNCTION_FACTOR = "conversionFunctionFactor";
	static final String INVERSION_CONVERSION_FUNCTION_FACTOR = "inversionConversionFunctionFactor";
	static final String OBJECT_TYPE = "objectType";
	static final String DISPLAY_IN_GUI_AS = "displayInGuiAs";
	static final String MATHML_STRING = "mathmlString";
	static final String PRIORITY_FOR_DISPLAY_IN_GUI = "priorityForDisplayInGui";

	// variables
	private Integer id;
	private String unit;
	private String description;
	private String name;
	private String kind_of_property_quantity;
	private String notation_case_sensitive;
	private String convert_to;
	private String conversion_function_factor;
	private String inverse_conversion_function_factor;
	private String object_type;
	private String display_in_GUI_as;
	private String mathML_string;
	private String priority_for_display_in_GUI;

	/**
	 * Returns the id of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link Unit}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the unit of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the unit of this {@link Unit}
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the description of this {@link Unit}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the name of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link Unit}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the kind of property/quantity of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the kind of property/quantity of this {@link Unit}
	 */
	public String getKindOfPropertyQuantity() {
		return kind_of_property_quantity;
	}

	/**
	 * Returns the case sensitive notation of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the case sensitive notation of this {@link Unit}
	 */
	public String getNotationCaseSensitive() {
		return notation_case_sensitive;
	}

	/**
	 * Returns the convert_to of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the convert_to of this {@link Unit}
	 */
	public String getConvertTo() {
		return convert_to;
	}

	/**
	 * Returns the conversion function factor of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the conversion function factor of this {@link Unit}
	 */
	public String getConversionFunctionFactor() {
		return conversion_function_factor;
	}

	/**
	 * Returns the inverse conversion function factor of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the inverse conversion function factor of this {@link Unit}
	 */
	public String getInverseConversionFunctionFactor() {
		return inverse_conversion_function_factor;
	}

	/**
	 * Returns the object type of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the object type of this {@link Unit}
	 */
	public String getObjectType() {
		return object_type;
	}

	/**
	 * Returns the display_in_GUI_as of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the display_in_GUI_as of this {@link Unit}
	 */
	public String getDisplayInGuiAs() {
		return display_in_GUI_as;
	}

	/**
	 * Returns the MathML string of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the MathML string of this {@link Unit}
	 */
	public String getMathMLString() {
		return mathML_string;
	}

	/**
	 * Returns the priority for display in GUI of this {@link Unit}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the priority for display in GUI of this {@link Unit}
	 */
	public String getPriorityForDisplayInGui() {
		return priority_for_display_in_GUI;
	}

	/**
	 * Sets the id of this {@link Unit}.
	 * 
	 * @param id
	 *            the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the unit of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param unit
	 *            the unit to be set
	 */
	public void setUnit(final String unit) {
		this.unit = Strings.emptyToNull(unit);
	}

	/**
	 * Sets the description of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param description
	 *            the description to be set
	 */
	public void setDescription(final String description) {
		this.description = Strings.emptyToNull(description);
	}

	/**
	 * Sets the name of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param name
	 *            the name to be set
	 */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the kind of property/quantity of this {@link Unit}.
	 * 
	 * Converts empty strings to null.
	 * 
	 * @param kind_of_property_quantity
	 *            kind of property/quantity to be set
	 */
	public void setKindOfPropertyQuantity(final String kind_of_property_quantity) {
		this.kind_of_property_quantity = Strings.emptyToNull(kind_of_property_quantity);
	}

	/**
	 * Sets the case sensitive notation of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param notation_case_sensitive
	 *            case sensitive notation to be set
	 */
	public void setNotationCaseSensitive(final String notation_case_sensitive) {
		this.notation_case_sensitive = Strings.emptyToNull(notation_case_sensitive);
	}

	/**
	 * Sets the convert_to of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param convert_to
	 *            convert_to to be set
	 */
	public void setConvertTo(final String convert_to) {
		this.convert_to = Strings.emptyToNull(convert_to);
	}

	/**
	 * Sets the conversion function factor of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param conversion_function_factor
	 *            the conversion function factor to be set
	 */
	public void setConversionFunctionFactor(final String conversion_function_factor) {
		this.conversion_function_factor = Strings.emptyToNull(conversion_function_factor);
	}

	/**
	 * Sets the inverse conversion function factor of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param inverse_conversion_function_factor
	 *            the inverse conversion function factor
	 */
	public void setInverseConversionFunctionFactor(final String inverse_conversion_function_factor) {
		this.inverse_conversion_function_factor = Strings.emptyToNull(inverse_conversion_function_factor);
	}

	/**
	 * Sets the object type of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param object_type
	 *            the object type to be set
	 */
	public void setObjectType(final String object_type) {
		this.object_type = Strings.emptyToNull(object_type);
	}

	/**
	 * Sets the display_in_GUI_as of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param display_in_GUI_as
	 *            the display_in_GUI_as to be set
	 */
	public void setDisplayInGuiAs(final String display_in_GUI_as) {
		this.display_in_GUI_as = Strings.emptyToNull(display_in_GUI_as);
	}

	/**
	 * Sets the MahtML string of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param mathML_string
	 *            the MahtML string to be set
	 */
	public void setMathMLString(final String mathML_string) {
		this.mathML_string = Strings.emptyToNull(mathML_string);
	}

	/**
	 * Sets the priority for display in GUI of this {@link Unit}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param priority_for_display_in_GUI
	 *            the priority for display in GUI to be set
	 */
	public void setPriorityForDisplayInGui(final String priority_for_display_in_GUI) {
		this.priority_for_display_in_GUI = Strings.emptyToNull(priority_for_display_in_GUI);
	}

	/**
	 * Saves unit properties into a {@link NodeSettingsWO}.
	 * 
	 * @param settings
	 *            settings where to save the {@link Unit} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(UNIT, unit, settings);
		SettingsHelper.addString(DESCRIPTION, description, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(KIND_OF_PROPERTY_QUANTITY, kind_of_property_quantity, settings);
		SettingsHelper.addString(NOTATION_CASE_SENSITIVE, notation_case_sensitive, settings);
		SettingsHelper.addString(CONVERT_TO, convert_to, settings);
		SettingsHelper.addString(CONVERSION_FUNCTION_FACTOR, conversion_function_factor, settings);
		SettingsHelper.addString(INVERSION_CONVERSION_FUNCTION_FACTOR, inverse_conversion_function_factor, settings);
		SettingsHelper.addString(OBJECT_TYPE, object_type, settings);
		SettingsHelper.addString(DISPLAY_IN_GUI_AS, display_in_GUI_as, settings);
		SettingsHelper.addString(MATHML_STRING, mathML_string, settings);
		SettingsHelper.addString(PRIORITY_FOR_DISPLAY_IN_GUI, priority_for_display_in_GUI, settings);
	}

	/**
	 * Loads unit properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            The settings where to load the {@link Unit} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		unit = SettingsHelper.getString(UNIT, settings);
		description = SettingsHelper.getString(DESCRIPTION, settings);
		name = SettingsHelper.getString(NAME, settings);
		kind_of_property_quantity = SettingsHelper.getString(KIND_OF_PROPERTY_QUANTITY, settings);
		notation_case_sensitive = SettingsHelper.getString(NOTATION_CASE_SENSITIVE, settings);
		convert_to = SettingsHelper.getString(CONVERT_TO, settings);
		conversion_function_factor = SettingsHelper.getString(CONVERSION_FUNCTION_FACTOR, settings);
		inverse_conversion_function_factor = SettingsHelper.getString(INVERSION_CONVERSION_FUNCTION_FACTOR, settings);
		object_type = SettingsHelper.getString(OBJECT_TYPE, settings);
		display_in_GUI_as = SettingsHelper.getString(DISPLAY_IN_GUI_AS, settings);
		mathML_string = SettingsHelper.getString(MATHML_STRING, settings);
		priority_for_display_in_GUI = SettingsHelper.getString(PRIORITY_FOR_DISPLAY_IN_GUI, settings);
	}
}
