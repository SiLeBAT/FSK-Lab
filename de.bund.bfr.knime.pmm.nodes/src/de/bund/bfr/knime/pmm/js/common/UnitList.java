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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * List of PmmLab units
 * 
 * @see Unit
 * @author Miguel de Alba
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class UnitList {

	private int numUnits;
	private Unit[] units;

	/**
	 * Returns an array with the units in the list.
	 * 
	 * If not set returns null.
	 * 
	 * @return an array with the units in the list
	 */
	public Unit[] getUnits() {
		return units;
	}

	/**
	 * Sets the units in the list.
	 * 
	 * @param units
	 *            array of units to be set
	 */
	public void setUnits(final Unit[] units) {
		numUnits = units.length;
		this.units = units;
	}

	/**
	 * Saves the list of units into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "numUnits"
	 * <li>Variable number of settings with key "units" and a suffix. E.g. "units0",
	 * "units1", etc.
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link UnitList} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt("numUnits", numUnits);
		for (int i = 0; i < numUnits; i++) {
			units[i].saveToNodeSettings(settings.addNodeSettings("units" + i));
		}
	}

	/**
	 * Loads properties of the unit list from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>Integer "numUnits"
	 *            <li>Variable number of settings with key "units" and a suffix.
	 *            E.g. "units0", "units1", etc.
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numUnits = settings.getInt("numUnits");
			units = new Unit[numUnits];
			for (int i = 0; i < numUnits; i++) {
				units[i] = new Unit();
				units[i].loadFromNodeSettings(settings.getNodeSettings("units" + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
