package de.bund.bfr.knime.fsklab.nodes

import de.bund.bfr.knime.fsklab.nodes.ui.RPlotterViewPanel
import org.knime.core.node.NodeView
import javax.swing.JScrollPane

/**
 * The view for the R nodes with image output.
 * @author Miguel de Alba, BfR, Berlin.
 */
class RunnerNodeView(nodeModel: RunnerNodeModel) : NodeView<RunnerNodeModel>(nodeModel) {
	
	private val panel = RPlotterViewPanel()
	
	init {
		component = JScrollPane(panel)
	}
	
	/** Updates the image to display. */
	override fun modelChanged() = panel.update(nodeModel.getResultImage())
	
	override fun onClose() = Unit
	
	override fun onOpen() = Unit
}