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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.jna.Platform
import de.bund.bfr.fskml.FskMetaDataObject
import de.bund.bfr.fskml.URIS
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.NoInternalsModel
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.nodes.controller.RController
import de.bund.bfr.knime.fsklab.rakip.GenericModel
import de.bund.bfr.knime.fsklab.rakip.RakipModule
import de.unirostock.sems.cbarchive.CombineArchive
import org.knime.core.node.*
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser
import org.knime.core.node.defaultnodesettings.SettingsModelString
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType
import org.knime.core.util.FileUtil
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.util.*
import javax.swing.JFileChooser

private val IN_TYPES: Array<PortType> = emptyArray()
private val OUT_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE)

class ReaderNodeModel : NoInternalsModel(IN_TYPES, OUT_TYPES) {

	private val filename = SettingsModelString("filename", "")

	/** @throws [InvalidSettingsException] */
	override fun saveSettingsTo(settings: NodeSettingsWO?) = filename.saveSettingsTo(settings)

	/**@throws [InvalidSettingsException] */
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO?) = filename.loadSettingsFrom(settings)

	override fun validateSettings(settings: NodeSettingsRO?) = filename.validateSettings(settings)

	override fun reset() {}

	override fun configure(inSpecs: Array<PortObjectSpec>?): Array<PortObjectSpec> {
		return arrayOf(FskPortObjectSpec.INSTANCE)
	}

	override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		val libNames = ArrayList<String>()

		val file = FileUtil.getFileFromURL(FileUtil.toURL(filename.stringValue))

		val fskObj = FskPortObject()

		CombineArchive(file).use { archive ->
			for (entry in archive.getEntriesWithFormat(URIS.r)) {

				val fmdo = FskMetaDataObject(entry.descriptions[0])

				when (fmdo.resourceType) {
					FskMetaDataObject.ResourceType.modelScript -> {
						val modelScriptFile = createTempFile("modelScript")
						entry.extractFile(modelScriptFile)
						fskObj.model = modelScriptFile.readText()
						modelScriptFile.delete()
					}
					FskMetaDataObject.ResourceType.parametersScript -> {
						val paramScriptFile = createTempFile("paramScript")
						entry.extractFile(paramScriptFile)
						fskObj.param = paramScriptFile.readText()
						paramScriptFile.delete()
					}
					FskMetaDataObject.ResourceType.visualizationScript -> {
						val visualizationScriptFile = createTempFile("visualizationScript")
						entry.extractFile(visualizationScriptFile)
						fskObj.viz = visualizationScriptFile.readText()
						visualizationScriptFile.delete()
					}
					FskMetaDataObject.ResourceType.workspace -> {
						fskObj.workspace = FileUtil.createTempFile("workspace", ".r")
					}
					else -> {
						// RAKIP JSON metadata is not supported currently by fskml.
					}
				}
			}

			// Gets metadata file
			val jsonURI = URI("http://json.org")
			val metaDataFile = createTempFile("metaData", ".json")
			archive.getEntriesWithFormat(jsonURI)[0].extractFile(metaDataFile)
			fskObj.genericModel = metaDataFile.loadMetaData()
			metaDataFile.delete()

			// Gets library names
			val libUri: URI = when {
				Platform.isWindows() -> URIS.zip
				Platform.isMac() -> URIS.tgz
				Platform.isLinux() -> URIS.tar_gz
				else -> throw InvalidSettingsException("Unsupported platform")
			}

			archive.getEntriesWithFormat(libUri).mapTo(libNames) { it.fileName.split("_".toRegex()).toTypedArray()[0] }
		}

		if (!libNames.isEmpty()) {

			val libRegistry = LibRegistry.instance

			// Filters and installs missing libraries
			val missingLibs = libNames.filter { !libRegistry.isInstalled(it) }.toList()
			if (!missingLibs.isEmpty()) {
				libRegistry.installLibs(missingLibs)
			}

			// Converts and return set of Paths returned from plugin to set
			libRegistry.getPaths(libNames).map(Path::toFile).forEach { fskObj.libs.add(it) }
		}

		// validate model
		RController().let { controller ->

			// Add path
			val installationPath = LibRegistry.instance.installationPath.toString().replace("\\", "/")
			val cmd = ".libPaths(c('$installationPath', .libPaths()))"
			val newPaths = controller.eval(cmd).asStrings()

			// TODO: validate model with parameter values from parameter script

			// Restore .libPaths() to the original library path which is in the last position
			controller.eval(".libPaths()[${newPaths.size}]")
		}

		return arrayOf(fskObj)
	}

	private fun File.loadMetaData(): GenericModel {
		val objectMapper = ObjectMapper().registerModule(RakipModule())
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
		return objectMapper.readValue(this, GenericModel::class.java)
	}
}

class ReaderNodeFactory: NodeFactory<ReaderNodeModel>() {

    override fun createNodeModel() = ReaderNodeModel()

    override fun getNrNodeViews() = 0

    override fun createNodeView(viewIndex: Int, nodeModel: ReaderNodeModel?): NodeView<ReaderNodeModel> {
        throw UnsupportedOperationException("No views! $viewIndex")
    }

    override fun hasDialog() = true

    override fun createNodeDialogPane(): NodeDialogPane {

        val filename = SettingsModelString("filename", "")
        val filechooser = DialogComponentFileChooser(filename, "filename-history", JFileChooser.OPEN_DIALOG, ".fskx")

        // Add widget
        val pane = DefaultNodeSettingsPane()
        pane.createNewGroup("Data source")
        pane.addDialogComponent(filechooser)

        return pane
    }
}