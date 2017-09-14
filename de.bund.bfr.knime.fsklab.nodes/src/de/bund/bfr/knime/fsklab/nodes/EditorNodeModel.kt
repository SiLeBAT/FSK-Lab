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
package de.bund.bfr.knime.fsklab.nodes

import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.NoInternalsModel
import org.knime.core.node.ExecutionContext
import org.knime.core.node.NodeLogger
import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType

private val LOGGER = NodeLogger.getLogger("FSK Editor node")

// Input and output types
private val IN_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE_OPTIONAL)
private val OUT_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE)

class EditorNodeModel : NoInternalsModel(IN_TYPES, OUT_TYPES) {

    private val settings = EditorNodeSettings()

    // --- node settings methods ---
    override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = this.settings.loadSettings(settings)

    override fun validateSettings(settings: NodeSettingsRO) = EditorNodeSettings().loadSettings(settings)
    override fun saveSettingsTo(settings: NodeSettingsWO) = this.settings.saveSettings(settings)

    override fun configure(inSpecs: Array<PortObjectSpec>): Array<PortObjectSpec> {
        return arrayOf(FskPortObjectSpec.INSTANCE)
    }

    override fun reset() = Unit

    override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

        val outObj: FskPortObject

        // If there is an input model
        if (inObjects.isNotEmpty()) {
            val inObj = inObjects[0] as FskPortObject

            // if input model has not changed the original script stored in sttings match the input model
            if (settings.originalModelScript == inObj.model && settings.originalParametersScript == inObj.param &&
                    settings.originalVisualizationScript == inObj.viz) {
                outObj = inObj
                outObj.model = settings.modifiedModelScript
                outObj.param = settings.modifiedParametersScript
                outObj.viz = settings.modifiedVisualizationScript
                outObj.genericModel = settings.genericModel
            } else {
                settings.originalModelScript = inObj.model
                settings.originalParametersScript = inObj.param
                settings.originalVisualizationScript = inObj.viz

                settings.modifiedModelScript = inObj.model
                settings.modifiedParametersScript = inObj.param
                settings.modifiedVisualizationScript = inObj.viz

                settings.genericModel = inObj.genericModel
                outObj = inObj
            }
        }
        // If there is no input model, then it will return the model created in the UI
        else {
            outObj = FskPortObject(model = settings.modifiedModelScript, param = settings.modifiedParametersScript,
                    viz = settings.modifiedVisualizationScript)
        }

        return arrayOf(outObj)
    }
}