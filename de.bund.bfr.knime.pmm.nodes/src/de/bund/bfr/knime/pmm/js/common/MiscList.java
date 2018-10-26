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
 * List of PmmLab Misc.
 * 
 * @see Misc
 * @author Miguel de Alba
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class MiscList {
	static final String NUM_MISCS = "numMiscs";
	static final String MISCS = "miscs";

	private int numMiscs;
	private Misc[] miscs;

	/**
	 * Returns an array with the miscs in the list.
	 * 
	 * If not set returns null.
	 * 
	 * @return an array with the miscs in the list
	 */
	public Misc[] getMiscs() {
		return miscs;
	}

	/**
	 * Sets the miscs in the list.
	 * 
	 * @param miscs
	 *            array of miscs to be set
	 */
	public void setMiscs(final Misc[] miscs) {
		numMiscs = miscs.length;
		this.miscs = miscs;
	}

	/**
	 * Saves the list of miscs into a {@link NodeSettingsWO}.
	 * 
	 * @param settings
	 *            settings where to save the {@link MiscList} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(NUM_MISCS, numMiscs, settings);
		for (int i = 0; i < numMiscs; i++) {
			miscs[i].saveToNodeSettings(settings.addNodeSettings(MISCS + i));
		}
	}

	/**
	 * Load properties of the matrix list from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            the settings where to load the {@link MatrixList} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numMiscs = settings.getInt(NUM_MISCS);
			miscs = new Misc[numMiscs];
			for (int i = 0; i < numMiscs; i++) {
				miscs[i] = new Misc();
				miscs[i].loadFromNodeSettings(settings.getNodeSettings(MISCS + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
