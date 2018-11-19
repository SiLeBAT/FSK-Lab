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
 * List of PmmLab parameters.
 *
 * @see Param
 * @author Miguel de Alba
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ParamList {

	private int numParams;
	private Param[] params;

	/**
	 * Returns an empty array with the parameters in the list.
	 *
	 * If not set returns null.
	 *
	 * @return an array with the parameters in the list
	 */
	public Param[] getParams() {
		return params;
	}

	/**
	 * Sets the parameters in the list.
	 *
	 * @param params
	 *            array of parameters to be set
	 */
	public void setParams(Param[] params) {
		numParams = params.length;
		this.params = params;
	}

	/**
	 * Saves the list of parameters into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "numParams"
	 * <li>Variable number of settings with key "params" and a suffix. E.g.
	 * "params0", "params1", etc.
	 * </ul>
	 *
	 * @param settings
	 *            settings where to save the {@link ParamList} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt("numParams", numParams);
		for (int i = 0; i < numParams; i++) {
			params[i].saveToNodeSettings(settings.addNodeSettings("params" + i));
		}
	}

	/**
	 * Loads properties of the parameters list from a {@link NodeSettingsRO}.
	 *
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>Integer "numParams"
	 *            <li>Variable number of settings with key "params" and a suffix.
	 *            E.g. "params0", "params1", etc.
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numParams = settings.getInt("numParams");
			params = new Param[numParams];
			for (int i = 0; i < numParams; i++) {
				params[i] = new Param();
				params[i].loadFromNodeSettings(settings.getNodeSettings("params" + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
