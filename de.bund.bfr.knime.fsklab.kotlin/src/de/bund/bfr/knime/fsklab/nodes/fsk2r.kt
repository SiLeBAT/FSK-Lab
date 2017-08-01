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
import org.knime.core.node.ExecutionContext
import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeModel
import org.knime.core.node.NodeView
import org.knime.core.node.StatelessModel
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.ext.r.node.local.port.RPortObject
import org.knime.ext.r.node.local.port.RPortObjectSpec

private val IN_TYPES = arrayOf(FskPortObject.TYPE)
private val OUT_TYPES = arrayOf(RPortObject.TYPE)

private val nodeModel = object : StatelessModel(inPortTypes = IN_TYPES, outPortTypes = OUT_TYPES) {
	
	override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf(RPortObjectSpec.INSTANCE)
	
	override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {
		val workspaceFile = (inObjects[0] as FskPortObject).workspace
		return arrayOf(RPortObject(workspaceFile))
	}
}

class Fsk2RNodeFactory : NodeFactory<NodeModel>() {
	
	override fun createNodeModel() = nodeModel
	override fun getNrNodeViews() = 0
	override fun createNodeView(viewIndex: Int, nodeModel: NodeModel): NodeView<NodeModel>? = null
	override fun hasDialog() = false
	override fun createNodeDialogPane() = DefaultNodeSettingsPane()
}