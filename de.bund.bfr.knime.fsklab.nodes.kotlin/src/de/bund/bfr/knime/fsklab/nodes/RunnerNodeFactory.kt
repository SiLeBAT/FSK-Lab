package de.bund.bfr.knime.fsklab.nodes

import de.bund.bfr.knime.fsklab.nodes.RunnerNodeModel
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeSettings
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeView
import org.knime.core.node.NodeDialogPane
import org.knime.core.node.NodeFactory
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.defaultnodesettings.DialogComponentColorChooser
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit
import org.knime.core.node.defaultnodesettings.DialogComponentString

class RunnerNodeFactory : NodeFactory<RunnerNodeModel>() {

	override fun createNodeModel() = RunnerNodeModel()

	override fun getNrNodeViews() = 1

	override fun createNodeView(viewIndex: Int, nodeModel: RunnerNodeModel) = RunnerNodeView(nodeModel)

	override fun hasDialog() = true

	override fun createNodeDialogPane(): NodeDialogPane {

		val pane = DefaultNodeSettingsPane()
		pane.createNewGroup("Options")

		val settings = RunnerNodeSettings()

		// Width component
		val widthComp = DialogComponentNumberEdit(settings.widthModel, "Width")
		widthComp.setToolTipText("Width of the plot")
		pane.addDialogComponent(widthComp)

		// Height component
		val heightComp = DialogComponentNumberEdit(settings.heightModel, "Height")
		heightComp.setToolTipText("Height of the plot")
		pane.addDialogComponent(heightComp)

		// Resolution component
		val resolutionComp = DialogComponentString(settings.resolutionModel, "Resolution")
		resolutionComp.setToolTipText("Nominal resolution in ppi which will be recorded in the bitmap file, if a positive integer.")
		pane.addDialogComponent(resolutionComp)

		// Background colour component
		val colorComp = DialogComponentColorChooser(settings.colourModel, "Background colour", true)
		colorComp.setToolTipText("Background colour")
		pane.addDialogComponent(colorComp)

		// Text point size component
		val textPointSizeComp = DialogComponentNumberEdit(settings.textPointSizeModel, "Text point size")
		textPointSizeComp.setToolTipText("Point size of plotted text, interpreted as big points (1/72 inch) at rest ppi.")
		pane.addDialogComponent(textPointSizeComp)

		return pane
	}
}
