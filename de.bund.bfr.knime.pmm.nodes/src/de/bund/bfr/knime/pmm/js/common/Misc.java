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
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.MiscXml;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Misc implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String DESCRIPTION = "description";
	static final String VALUE = "value";
	static final String CATEGORY = "category";
	static final String UNIT = "unit";
	static final String ORIGUNIT = "origUnit";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String description;
	private Double value;
	private String[] categories;
	private String unit;
	private String origUnit;
	private String dbuuid;

	/**
	 * Returns the id of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link Misc}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link Misc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the description of this {@link Misc}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the value of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the value of this {@link Misc}
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * Returns the categories of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the categories of this {@link Misc}
	 */
	public String[] getCategories() {
		return categories;
	}

	/**
	 * Returns the unit of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the unit of this {@link Misc}
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the original unit of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the original unit of this {@link Misc}
	 */
	public String getOrigUnit() {
		return origUnit;
	}

	/**
	 * Returns the DBUUID of this {@link Misc}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the DBUUID of this {@link Misc}
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/**
	 * Sets the id of this {@link Misc}.
	 * 
	 * @param id the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the name of this {@link Misc}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param name the name to be set
	 */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the description of this {@link Misc}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param description the description of this {@link Misc}
	 */
	public void setDescription(final String description) {
		this.description = Strings.emptyToNull(description);
	}

	/**
	 * Sets the value of this {@link Misc}.
	 * 
	 * @param value the value of this {@link Misc}
	 */
	public void setValue(final Double value) {
		this.value = value;
	}

	/**
	 * Sets the categories of this {@link Misc}.
	 * 
	 * @param categories the categories to be set
	 */
	public void setCategories(final String[] categories) {
		this.categories = categories;
	}

	/**
	 * Sets the unit of this {@link Misc}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param unit the unit to be set
	 */
	public void setUnit(final String unit) {
		this.unit = Strings.emptyToNull(unit);
	}

	/**
	 * Sets the original unit of this {@link Misc}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param origUnit the original unit to be set
	 */
	public void setOrigUnit(final String origUnit) {
		this.origUnit = Strings.emptyToNull(origUnit);
	}

	/**
	 * Sets the DBUUID of this {@link Misc}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param dbuuid the DBUUID to be set
	 */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = dbuuid;
	}

	/** Saves misc properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(DESCRIPTION, description, settings);
		SettingsHelper.addDouble(VALUE, value, settings);
		settings.addStringArray(CATEGORY, categories);
		SettingsHelper.addString(UNIT, unit, settings);
		SettingsHelper.addString(ORIGUNIT, origUnit, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	/**
	 * Loads misc properties from a {@link NodeSettingsRO}.
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		description = SettingsHelper.getString(DESCRIPTION, settings);
		value = SettingsHelper.getDouble(VALUE, settings);
		try {
			categories = settings.getStringArray(CATEGORY);
		} catch (InvalidSettingsException e) {
			categories = null;
		}
		unit = SettingsHelper.getString(UNIT, settings);
		origUnit = SettingsHelper.getString(ORIGUNIT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}
	
	/**
	 * Creates a Misc from a MiscXml.
	 * 
	 * @param miscXml
	 */
	public static Misc toMisc(MiscXml miscXml) {
		Misc misc = new Misc();
		misc.setId(miscXml.getId());
		misc.setName(miscXml.getName());
		misc.setDescription(miscXml.getDescription());
		misc.setValue(miscXml.getValue());
		misc.setCategories(miscXml.getCategories().toArray(new String[miscXml.getCategories().size()]));
		misc.setUnit(miscXml.getUnit());
		misc.setOrigUnit(miscXml.getOrigUnit());
		misc.setDbuuid(miscXml.getDbuuid());
		
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
