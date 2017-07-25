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