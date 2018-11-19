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
 * List of PmmLab matrices.
 * 
 * @see Matrix
 * @author Miguel de Alba
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class MatrixList {

	private int numMatrices;
	private Matrix[] matrices;

	/**
	 * Returns an array with the matrices in the list.
	 * 
	 * If not set returns null.
	 * 
	 * @return an array with the ematrices in the list.
	 */
	public Matrix[] getMatrices() {
		return matrices;
	}

	/**
	 * Sets the matrices in the list.
	 * 
	 * @param matrices
	 *            array of matrices to be set
	 */
	public void setMatrices(Matrix[] matrices) {
		numMatrices = matrices.length;
		this.matrices = matrices;
	}

	/**
	 * Saves the list of matrices into a {@link MatrixList} with properties:
	 * <ul>
	 * <li>Integer "numMatrices"
	 * <li>Variable number of settings with key "matrices" and a suffix. E.g.
	 * "matrices0", "matrices1", ...
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link MatrixList} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt("numMatrices", numMatrices);
		for (int i = 0; i < numMatrices; i++) {
			matrices[i].saveToNodeSettings(settings.addNodeSettings("matrices" + i));
		}
	}

	/**
	 * Load properties of the matrices from a {@link MatrixList}.
	 * 
	 * @param settings
	 *            with properties
	 *            <ul>
	 *            <li>Integer "numMatrices"
	 *            <li>Variable number of settings with key "matrices" and a suffix.
	 *            E.g. "matrices0", "matrices1", ...
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numMatrices = settings.getInt("numMatrices");
			matrices = new Matrix[numMatrices];
			for (int i = 0; i < numMatrices; i++) {
				matrices[i] = new Matrix();
				matrices[i].loadFromNodeSettings(settings.getNodeSettings("matrices" + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
