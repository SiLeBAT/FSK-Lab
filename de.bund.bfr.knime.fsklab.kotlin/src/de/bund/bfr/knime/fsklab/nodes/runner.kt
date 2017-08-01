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
import de.bund.bfr.rakip.generic.ParameterClassification
import org.knime.core.data.image.png.PNGImageContent
import org.knime.core.node.ExecutionContext
import org.knime.core.node.ExecutionMonitor
import org.knime.core.node.InvalidSettingsException
import org.knime.core.node.NodeDialogPane
import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeLogger
import org.knime.core.node.NodeModel
import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.NodeView
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.defaultnodesettings.DialogComponentColorChooser
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit
import org.knime.core.node.defaultnodesettings.DialogComponentString
import org.knime.core.node.defaultnodesettings.SettingsModelColor
import org.knime.core.node.defaultnodesettings.SettingsModelInteger
import org.knime.core.node.defaultnodesettings.SettingsModelString
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.image.ImagePortObject
import org.knime.core.node.port.image.ImagePortObjectSpec
import org.knime.core.util.FileUtil
import java.awt.Color
import java.awt.Image
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.swing.JScrollPane

private class RunnerNodeSettings {

	companion object {
		val WIDTH = 640
		val HEIGHT = 640
		val RES = "NA"
		val COLOUR = Color.white
		val TEXT_POINT_SIZE = 12
	}

	// Setting models, with keys and default values
	val widthModel = SettingsModelInteger("width", WIDTH)
	val heightModel = SettingsModelInteger("height", HEIGHT)
	val resolutionModel = SettingsModelString("resolution", RES)
	val colourModel = SettingsModelColor("colour", COLOUR)
	val textPointSizeModel = SettingsModelInteger("textPointSize", TEXT_POINT_SIZE)

	fun saveSettingsTo(settings: NodeSettingsWO) {
		widthModel.saveSettingsTo(settings)
		heightModel.saveSettingsTo(settings)
		resolutionModel.saveSettingsTo(settings)
		colourModel.saveSettingsTo(settings)
		textPointSizeModel.saveSettingsTo(settings)
	}

	fun validateSettings(settings: NodeSettingsRO) {
		widthModel.validateSettings(settings = settings, defaultValue = WIDTH)
		heightModel.validateSettings(settings = settings, defaultValue = HEIGHT)
		resolutionModel.validateSettings(settings = settings, defaultValue = RES)
		colourModel.validateSettings(settings = settings, defaultValue = COLOUR)
		textPointSizeModel.validateSettings(settings = settings, defaultValue = TEXT_POINT_SIZE)
	}

	fun loadValidatedSettingsFrom(settings: NodeSettingsRO) {
		widthModel.loadSettingsFrom(settings = settings, defaultValue = WIDTH)
		heightModel.loadSettingsFrom(settings = settings, defaultValue = HEIGHT)
		resolutionModel.loadSettingsFrom(settings = settings, defaultValue = RES)
		colourModel.loadSettingsFrom(settings = settings, defaultValue = COLOUR)
		textPointSizeModel.loadSettingsFrom(settings = settings, defaultValue = TEXT_POINT_SIZE)
	}

	/**
	 * Reads the expected values from the settings object, without assigning them to the internal variable.
	 *
	 * @param settings the object to read the value from
	 * @param defaultValue value to be assigned if the value in the settings object is invalid
	 */
	private fun SettingsModelInteger.validateSettings(settings: NodeSettingsRO, defaultValue: Int) {
		try {
			validateSettings(settings)
		} catch (_: InvalidSettingsException) {
			setIntValue(defaultValue)
		}
	}

	/**
	 * Reads the expected values from the settings object, without assigning them to the internal variable.
	 *
	 * @param settings the object to read the value from
	 * @param defaultValue value to be assigned if the value in the settings object is invalid
	 */
	private fun SettingsModelString.validateSettings(settings: NodeSettingsRO, defaultValue: String) {
		try {
			validateSettings(settings)
		} catch (_: InvalidSettingsException) {
			setStringValue(defaultValue)
		}
	}

	/**
	 * Reads the expected values from the settings object, without assigning them to the internal variable.
	 *
	 * @param settings the object to read the value from
	 * @param defaultValue value to be assigned if the value in the settings object is invalid
	 */
	private fun SettingsModelColor.validateSettings(settings: NodeSettingsRO, defaultValue: Color) {
		try {
			validateSettings(settings)
		} catch (_: InvalidSettingsException) {
			setColorValue(defaultValue)
		}
	}

	/**
	 * Read value of this component model from configuration object.
	 *
	 * @param settings the {@link org.knime.core.node.NodeSettings} to read from
	 * @param defaultValue value to be assigned if the value is missing in the settings object
	 */
	private fun SettingsModelInteger.loadSettingsFrom(settings: NodeSettingsRO, defaultValue: Int) {
		try {
			loadSettingsFrom(settings)
		} catch (_: InvalidSettingsException) {
			setIntValue(defaultValue)
		}
	}

	/**
	 * Read value of this component model from configuration object.
	 *
	 * @param settings the {@link org.knime.core.node.NodeSettings} to read from
	 * @param defaultValue value to be assigned if the value is missing in the settings object
	 */
	private fun SettingsModelString.loadSettingsFrom(settings: NodeSettingsRO, defaultValue: String) {
		try {
			loadSettingsFrom(settings)
		} catch (_: InvalidSettingsException) {
			setStringValue(defaultValue)
		}
	}

	/**
	 * Read value of this component model from configuration object.
	 *
	 * @param settings the {@link org.knime.core.node.NodeSettings} to read from
	 * @param defaultValue value to be assigned if the value is missing in the settings object
	 */
	private fun SettingsModelColor.loadSettingsFrom(settings: NodeSettingsRO, defaultValue: Color) {
		try {
			loadSettingsFrom(settings)
		} catch (_: InvalidSettingsException) {
			setColorValue(defaultValue)
		}
	}
}

private val LOGGER = NodeLogger.getLogger("Runner node")

// Output for an FSK object
private val FSK_SPEC = FskPortObjectSpec.INSTANCE

// Output for a PNG image
private val PNG_SPEC = ImagePortObjectSpec(PNGImageContent.TYPE)

// Input and output types
private val IN_TYPES = arrayOf(FskPortObject.TYPE)
private val OUT_TYPES = arrayOf(FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL)

class RunnerNodeModel : NodeModel(IN_TYPES, OUT_TYPES) {

	private val settings = RunnerNodeSettings()

	// --- internal settings methods ---
	override fun loadInternals(nodeInternDir: File, exec: ExecutionMonitor) {
		InternalSettings.loadInternals(nodeInternDir)
	}

	override fun saveInternals(nodeInternDir: File, exec: ExecutionMonitor) {
		InternalSettings.saveInternals(nodeInternDir)
	}

	override fun reset() = InternalSettings.reset()

	// --- node settings methods ---
	override fun saveSettingsTo(settings: NodeSettingsWO) = this.settings.saveSettingsTo(settings)

	override fun validateSettings(settings: NodeSettingsRO) = this.settings.validateSettings(settings)
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = this.settings.loadValidatedSettingsFrom(settings)

	override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf(FSK_SPEC, PNG_SPEC)

	override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		var fskObj = inObjects[0] as FskPortObject

		var newScript = ""
		var onError = false

		val indepVars = fskObj.genericModel.modelMath?.parameter
				?.filter { it.classification == ParameterClassification.input }?.toList() ?: emptyList()

		for (indepVar in indepVars) {
			if (indepVar.name.isNullOrBlank() || indepVar.value.isNullOrBlank()) {
				onError = true
				break
			}
			newScript += "$indepVar.name <- $indepVar.value\n"
		}

		if (!onError) fskObj.param = newScript

		RController().use { controller -> fskObj = runSnippet(controller, fskObj) }

		var stream: FileInputStream? = null
		return try {
			stream = FileInputStream(InternalSettings.imgFile)
			val content = PNGImageContent(stream)
			InternalSettings.plot = content.image
			arrayOf(fskObj, ImagePortObject(content, PNG_SPEC))
		} catch (_: IOException) {
			LOGGER.warn("There is no image created")
			arrayOf(fskObj)
		} finally {
			stream?.close()
		}
	}
	
	fun getResultImage() = InternalSettings.plot

	private fun runSnippet(controller: RController, fskObj: FskPortObject): FskPortObject {

		// Add path
		val libRegistry = LibRegistry.instance()
		val installationPath = libRegistry.installationPath.toString().replace("\\", "/")
		val cmd = ".libPaths(c('$installationPath', .libPaths()))"
		val newPaths = controller.eval(cmd).asStrings()

		// Run model
		controller.eval(fskObj.param + "\n" + fskObj.model)

		// Save workspace
		if (fskObj.workspace == null)
			fskObj.workspace = FileUtil.createTempFile("workspace", ".R")
		val workspacePath = fskObj.workspace!!.absolutePath.replace("\\", "/")
		controller.eval("save.image('$workspacePath')")

		// Creates chart into m_imageFile
		try {
			val cc = ChartCreator(controller)
			cc.plot(InternalSettings.imgFile.absolutePath.replace("\\", "/"), fskObj.viz)
		} catch (_: RException) {
			LOGGER.warn("Visualization script failed")
		}

		// Restore .libPaths() to the original library path which is in the last position
		controller.eval(".libPaths()[$newPaths.size]")

		return fskObj
	}

	private inner class ChartCreator(val controller: RController) {

		init {
			// initialize necessary R stuff to plot
			if (Platform.isMac()) {
				controller.eval("library('Cairo')");
				controller.eval("options(device='png', bitmapType='cairo')");
			} else {
				controller.eval("options(device='png')");
			}
		}

		fun plot(path: String, vizScript: String) {

			// gets values
			val width = settings.widthModel.intValue
			val height = settings.heightModel.intValue
			val res = settings.resolutionModel.stringValue
			val textPointSize = settings.textPointSizeModel.intValue
			val colour = settings.colourModel.colorValue

			val hexColour = String.format("#%02x%02x%02x", colour.red, colour.green, colour.blue)
			
			val pngCommand = "png(path='$path', width='$width', height='$height', " +
					"pointSize='$textPointSize', bg='$hexColour', res='$res')"
			controller.eval(pngCommand)
			
			controller.eval(vizScript)
			controller.eval("dev.off()")		
		}
	}

	/**
	 * @property imgFile image file to use for this current node. Initialized to temp location.
	 */
	private object InternalSettings {

		val FILE_NAME = "Rplot"
		val imgFile: File
		var plot: Image? = null

		// TODO: throws IOException (move to docstring)
		init {
			imgFile = FileUtil.createTempFile("RunnerNode-", ".png")
		}

		/**
		 * Loads the saved image.
		 * @throws IOException
		 */
		fun loadInternals(nodeInternDir: File) {
			val file = File(nodeInternDir, FILE_NAME + ".png")

			if (file.exists() && file.canRead()) {
				FileUtil.copy(file, imgFile)
				FileInputStream(imgFile).use { stream -> plot = PNGImageContent(stream).getImage() }
			}
		}

		/**
		 * Saves the saved image.
		 * @throws IOException
		 */
		fun saveInternals(nodeInternDir: File) {
			if (plot != null) {
				val file = File(nodeInternDir, FILE_NAME + ".png")
				FileUtil.copy(imgFile, file)
			}
		}

		/**
		 * Clear the contents of the image file.
		 * @throws IOException
		 */
		fun reset() {
			plot = null
			FileOutputStream(imgFile).use { erasor -> erasor.write("".toByteArray()) }
		}
	}
}

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