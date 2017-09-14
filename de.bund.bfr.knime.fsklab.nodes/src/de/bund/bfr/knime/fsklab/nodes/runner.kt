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

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.nodes.controller.RController
import de.bund.bfr.knime.fsklab.nodes.ui.RPlotterViewPanel
import de.bund.bfr.knime.fsklab.rakip.Parameter
import org.knime.core.data.image.png.PNGImageContent
import org.knime.core.node.*
import org.knime.core.node.BufferedDataTable
import org.knime.core.node.defaultnodesettings.SettingsModelColor
import org.knime.core.node.defaultnodesettings.SettingsModelInteger
import org.knime.core.node.defaultnodesettings.SettingsModelString
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType
import org.knime.core.node.port.image.ImagePortObject
import org.knime.core.node.port.image.ImagePortObjectSpec
import org.knime.core.util.FileUtil
import java.awt.Color
import java.awt.Image
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.swing.JScrollPane
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit
import org.knime.core.node.defaultnodesettings.DialogComponentColorChooser
import org.knime.core.node.defaultnodesettings.DialogComponentString
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane


/**
 * View of the runner node with image output.
 * @param nodeModel the model associated with this view.
 *
 * @author Miguel de Alba
 */
class RunnerNodeView(nodeModel: RunnerNodeModel) : NodeView<RunnerNodeModel>(nodeModel) {

	private val panel = RPlotterViewPanel()

	init {
		component = JScrollPane(panel)
	}

	/** Updates the image to display. */
	override fun modelChanged() {
		panel.update(nodeModel.getResultImage())
	}

	override fun onClose() = Unit
	override fun onOpen() = Unit
}

class RunnerNodeSettings {

	// Setting models
	internal val widthModel = SettingsModelInteger("width", 640)
	internal val heightModel = SettingsModelInteger("height", 640)
	internal val resolutionModel = SettingsModelString("resolution", "NA")
	internal val colourModel = SettingsModelColor("colour", Color.white)
	internal val textPointSizeModel = SettingsModelInteger("textPointSize", 12)

	private val settingsModels = listOf(widthModel, heightModel, resolutionModel, colourModel, textPointSizeModel)

	internal fun saveSettingsTo(settings: NodeSettingsWO) {
		settingsModels.forEach { it -> it.saveSettingsTo(settings) }
	}

	internal fun validateSettings(settings: NodeSettingsRO) {
		settingsModels.forEach { it -> it.validateSettings(settings) }
	}

	internal fun loadValidatedSettingsFrom(settings: NodeSettingsRO) {
		settingsModels.forEach { it -> it.loadSettingsFrom(settings) }
	}
}

/** Output spec for an FSK object.  */
private val FSK_SPEC = FskPortObjectSpec.INSTANCE

/** Output spec for a PNG image.  */
private val PNG_SPEC = ImagePortObjectSpec(PNGImageContent.TYPE)

// Input and output port types
private val IN_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE, BufferedDataTable.TYPE_OPTIONAL)
private val OUT_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL)


class RunnerNodeModel : NodeModel(IN_TYPES, OUT_TYPES) {

	private val LOGGER = NodeLogger.getLogger("Fskx Runner Node Model")

	private val internalSettings = InternalSettings()

	private val nodeSettings = RunnerNodeSettings()

	// --- internal settings methods ---
	override fun loadInternals(nodeInternDir: File, exec: ExecutionMonitor) = internalSettings.loadInternals(nodeInternDir)

	override fun saveInternals(nodeInternDir: File, exec: ExecutionMonitor) = internalSettings.saveInternals(nodeInternDir)

	override fun reset() = internalSettings.reset()

	// --- node settings methods ---
	override fun saveSettingsTo(settings: NodeSettingsWO) = nodeSettings.saveSettingsTo(settings)

	override fun validateSettings(settings: NodeSettingsRO) = nodeSettings.validateSettings(settings)
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = nodeSettings.loadValidatedSettingsFrom(settings)

	override fun configure(inSpecs: Array<PortObjectSpec>): Array<PortObjectSpec> {
		return arrayOf(FSK_SPEC, PNG_SPEC)
	}

	override fun execute(inData: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		var fskObj = inData[0] as FskPortObject

		val indepVars: List<Parameter> = fskObj.genericModel.modelMath.parameter
				.filter { it.classification == Parameter.Classification.input }.toList()

		if (!indepVars.isEmpty()) {
			try {
				fskObj.param = loadParameterScript(indepVars)
			} catch (exception: IllegalArgumentException) {
				LOGGER.warn(exception.message)
			}

		}

		RController().let { fskObj = runSnippet(it, fskObj) }

		return try {
			val imgObj = internalSettings.imageFile.inputStream().use { fis ->
				val content = PNGImageContent(fis)
				internalSettings.plot = content.image
				ImagePortObject(content, PNG_SPEC)
			}
			arrayOf(fskObj, imgObj)
		} catch (exception: IOException) {
			LOGGER.warn("There is no image created")
			arrayOf(fskObj)
		}
	}

	/**
	 * Generate a parameter script with parameters names and values from the model metadata.
	 */
	private fun loadParameterScript(params: List<Parameter>): String {

		var script = ""
		params.forEach {
			if (it.parameterName.isEmpty() || it.value.isEmpty()) {
				throw IllegalArgumentException("Parameter from metadata is not valid: $it")
			}

			script += "${it.parameterName} <- ${it.value}\n"
		}

		return script
	}

	private fun runSnippet(controller: RController, fskObj: FskPortObject): FskPortObject {

		// Add path
		val installationPath = LibRegistry.instance.installationPath.toString().replace("\\", "/")
		val cmd = ".libPaths(c('$installationPath', .libPaths()))"
		val newPaths = controller.eval(cmd).asStrings()

		// Run model
		controller.eval(fskObj.param)
		controller.eval(fskObj.model)

		// Save workspace
		val newWorkspaceFile: File = fskObj.workspace ?: FileUtil.createTempFile("workspace", ".R")
		val newWorkspaceFilePath = newWorkspaceFile.absolutePath.replace("\\", "/")
		controller.eval("save.image('$newWorkspaceFilePath')")
		if (fskObj.workspace == null) {
			fskObj.workspace = newWorkspaceFile
		}

		// Creates chart into m_imageFile
		try {
			val cc = ChartCreator(controller)
			val path = internalSettings.imageFile.absolutePath.replace("\\", "/")
			cc.plot(path, fskObj.viz)
		} catch (e: RException) {
			LOGGER.warn("Visualization script failed", e)
		}

		// Restore .libPaths() to the original library path which happens to be in the last position
		controller.eval(".libPaths()[${newPaths.size}]")

		return fskObj
	}

	private inner class ChartCreator(val controller: RController) {

		init {
			// initialize necessary R stuff to plot
			if (Platform.isMac()) {
				controller.eval("library('Cairo')")
				controller.eval("options(device='png', bitmapType='cairo')")
			} else {
				controller.eval("options(device='png')")
			}
		}

		fun plot(path: String, vizScript: String) {

			// Gets values
			val width = nodeSettings.widthModel.intValue
			val height = nodeSettings.heightModel.intValue
			val res = nodeSettings.resolutionModel.stringValue
			val textPointSize = nodeSettings.textPointSizeModel.intValue
			val colour = nodeSettings.colourModel.colorValue
			val hexColour = String.format("#%02x%02x%02x", colour.red, colour.green, colour.blue)

			val pngCmd = "png('$path', width=$width, height=$height, pointsize=$textPointSize, " +
					"bg='$hexColour', res='$res')"
			controller.eval(pngCmd)

			controller.eval(vizScript)
			controller.eval("dev.off()")
		}
	}

	fun getResultImage(): Image? = internalSettings.plot

	private inner class InternalSettings {

		/** Non-null image file to use for this current node. Initialized to temp location. */
		val imageFile: File = FileUtil.createTempFile("FskxRunner-", ".png")

		var plot: Image? = null

		/** Loads the saved image. */
		fun loadInternals(nodeInternDir: File) {

			val file = File(nodeInternDir, "Rplot.png")
			if (file.exists() && file.canRead()) {
				FileUtil.copy(file, imageFile)
				imageFile.inputStream().use { inputStream -> PNGImageContent(inputStream).image }
			}
		}

		/** Saves the saved image. */
		fun saveInternals(nodeInternDir: File) {
			plot?.let { FileUtil.copy(imageFile, File(nodeInternDir, "Rplot.png")) }
		}

		/** Clear the contents of the image file. */
		fun reset() {
			plot = null

			try {
				imageFile.outputStream().use { erasor ->
					erasor.write("".toByteArray())
				}
			} catch (exception: FileNotFoundException) {
				LOGGER.error("Temporary file is removed. $exception")
			} catch (exception: IOException) {
				LOGGER.error("Cannot write temporary file. $exception")
			}
		}
	}
}

class RunnerNodeFactory : NodeFactory<RunnerNodeModel>() {

	override fun createNodeModel() = RunnerNodeModel()
	override fun getNrNodeViews() = 1
	override fun createNodeView(viewIndex: Int, nodeModel: RunnerNodeModel): NodeView<RunnerNodeModel> = RunnerNodeView(nodeModel)
	override fun hasDialog() = true

	override fun createNodeDialogPane(): NodeDialogPane {

		val settings = RunnerNodeSettings()

		// Width component
		val widthComp = DialogComponentNumberEdit(settings.widthModel, "Width")
		widthComp.setToolTipText("Width of the plot")

		// Height component
		val heightComp = DialogComponentNumberEdit(settings.heightModel, "Height")
		heightComp.setToolTipText("Height of the plot")

		// Resolution component
		val resolutionComp = DialogComponentString(settings.resolutionModel, "Resolution")
		resolutionComp.setToolTipText(
				"Nominal resolution in ppi which will be recorded in the bitmap file, if a positive integer.")

		// Background colour component
		val colorComp = DialogComponentColorChooser(settings.colourModel, "Background colour", true)
		colorComp.setToolTipText("Background colour")

		// Text point size component
		val textPointSizeComp = DialogComponentNumberEdit(settings.textPointSizeModel, "Text point size")
		textPointSizeComp.setToolTipText("Point size of plotted text, interpreted as big points (1/72 inch) at res ppi.")

		val pane = DefaultNodeSettingsPane()
		pane.createNewGroup("Options")
		pane.addDialogComponent(widthComp)
		pane.addDialogComponent(heightComp)
		pane.addDialogComponent(resolutionComp)
		pane.addDialogComponent(colorComp)
		pane.addDialogComponent(textPointSizeComp)

		return pane
	}
}
