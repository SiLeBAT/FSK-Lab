/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes.rakip.editor;

import java.util.Objects;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.bund.bfr.knime.fsklab.nodes.rakip.editor.NodeSettings;
import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObjectSpec;

class NodeModel extends NoInternalsModel {

	private final NodeSettings settings;

	// Input and output port types
	private static final PortType[] IN_TYPES = { FskPortObject.TYPE_OPTIONAL };
	private static final PortType[] OUT_TYPES = { FskPortObject.TYPE };

	public NodeModel() {
		super(IN_TYPES, OUT_TYPES);
		settings = new NodeSettings();
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// do nothing
	}

	// --- node settings methods ---
	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.loadSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		(new NodeSettings()).loadSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		this.settings.saveSettings(settings);
	}

	// --- other methods ---
	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FskPortObjectSpec.INSTANCE };
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		FskPortObject outObj;

		// If there is an input model
		if (inObjects.length > 0 && inObjects[0] != null) {
			FskPortObject inObj = (FskPortObject) inObjects[0];

			// if input model has not changed (the original script stored in
			// settings match the input model)
			if (Objects.equals(settings.originalModelScript, inObj.model)
					&& Objects.equals(settings.originalParametersScript, inObj.param)
					&& Objects.equals(settings.originalVisualizationScript, inObj.viz)) {
				outObj = inObj;
				outObj.model = settings.modifiedModelScript;
				outObj.param = settings.modifiedParametersScript;
				outObj.viz = settings.modifiedVisualizationScript;
				outObj.genericModel = settings.genericModel;
			} else {
				settings.originalModelScript = inObj.model;
				settings.originalParametersScript = inObj.param;
				settings.originalVisualizationScript = inObj.viz;

				settings.modifiedModelScript = inObj.model;
				settings.modifiedParametersScript = inObj.param;
				settings.modifiedVisualizationScript = inObj.viz;
				settings.genericModel = inObj.genericModel;

				outObj = inObj;
			}
		}
		// If there is no input model then it will return the model created in
		// the UI
		else {
			outObj = new FskPortObject();
			outObj.model = settings.modifiedModelScript;
			outObj.param = settings.modifiedParametersScript;
			outObj.viz = settings.modifiedVisualizationScript;
		}

		return new PortObject[] { outObj };
	}
}
