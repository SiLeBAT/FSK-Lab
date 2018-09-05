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

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ModelList {
	private static final String NUM_MODELS = "numModels";
	private static final String MODELS = "models";

	private int numModels;
	private Model1DataTuple[] models;

	public Model1DataTuple[] getModels() {
		return models;
	}

	public void setModels(final Model1DataTuple[] models) {
		numModels = models.length;
		this.models = models;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(NUM_MODELS, numModels);
		for (int i = 0; i < numModels; i++) {
			models[i].saveToNodeSettings(settings.addNodeSettings(MODELS + i));
		}
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numModels = settings.getInt(NUM_MODELS);
			models = new Model1DataTuple[numModels];
			for (int i = 0; i < numModels; i++) {
				models[i] = new Model1DataTuple();
				models[i].loadFromNodeSettings(settings.getNodeSettings(MODELS + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
