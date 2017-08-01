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

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.bund.bfr.fskml.FskMetaDataObject
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType
import de.bund.bfr.fskml.URIS
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.rakip.generic.GenericModel
import de.bund.bfr.rakip.generic.RakipModule
import de.unirostock.sems.cbarchive.ArchiveEntry
import de.unirostock.sems.cbarchive.CombineArchive
import org.apache.commons.lang3.SystemUtils
import org.knime.core.node.ExecutionContext
import org.knime.core.node.InvalidSettingsException
import org.knime.core.node.NoInternalsModel
import org.knime.core.node.NodeDialogPane
import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.NodeView
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser
import org.knime.core.node.defaultnodesettings.SettingsModelString
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType
import org.knime.core.util.FileUtil
import java.io.FileWriter
import java.net.URI
import javax.swing.JFileChooser

private val CFG_FILE = "file"
private val IN_TYPES = arrayOf(FskPortObject.TYPE)
private val OUT_TYPES = emptyArray<PortType>()

class WriterNodeModel : NoInternalsModel(IN_TYPES, OUT_TYPES) {

	private val filePath = SettingsModelString(CFG_FILE, "")

	override fun saveSettingsTo(settings: NodeSettingsWO) = filePath.saveSettingsTo(settings)
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = filePath.loadSettingsFrom(settings)
	override fun validateSettings(settings: NodeSettingsRO) = filePath.validateSettings(settings)

	override fun reset() = Unit
	override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf<PortObjectSpec>()

	// TODO: execute
	override fun execute(inData: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		val portObject = inData[0] as FskPortObject

		val archiveFile = FileUtil.getFileFromURL(FileUtil.toURL(filePath.stringValue))
		archiveFile.delete()  // Delete if exists

		CombineArchive(archiveFile).use { archive ->

			// Adds model script
			val modelEntry = archive.addRScript(script = portObject.model, filename = "model.r")
			modelEntry.addDescription(FskMetaDataObject(ResourceType.modelScript).metaDataObject)

			// Adds parameters script
			val paramEntry = archive.addRScript(script = portObject.param, filename = "param.r")
			paramEntry.addDescription(FskMetaDataObject(ResourceType.parametersScript).metaDataObject)

			// Adds visualization script
			val vizEntry = archive.addRScript(script = portObject.viz, filename = "visualization.r")
			vizEntry.addDescription(FskMetaDataObject(ResourceType.visualizationScript).metaDataObject)

			// Adds R workspace file
			portObject.workspace?.let {
				val workspaceEntry = archive.addEntry(it, "workspace.r", URIS.r)
				workspaceEntry.addDescription(FskMetaDataObject(ResourceType.workspace).metaDataObject)
			}

			// Adds model metadata
			archive.addMetaData(genericModel = portObject.genericModel, filename = "metaData.json")

			// Adds R libraries
			val libUri = if (SystemUtils.IS_OS_WINDOWS) {
				URIS.zip
			} else if (SystemUtils.IS_OS_MAC) {
				URIS.tgz
			} else if (SystemUtils.IS_OS_LINUX) {
				URIS.tar_gz
			} else throw InvalidSettingsException("Unsupported platform")
			
			portObject.libs.forEach { archive.addEntry(it, it.name, libUri) }
		}
		
		return emptyArray<PortObject>()
	}

	private fun CombineArchive.addRScript(script: String, filename: String): ArchiveEntry {

		val file = createTempFile()
		FileWriter(file).use { writer -> writer.write(script) }

		val entry = addEntry(file, filename, URIS.r)
		file.delete()

		return entry
	}

	private fun CombineArchive.addMetaData(genericModel: GenericModel, filename: String): ArchiveEntry {

		val file = createTempFile()
		val objectMapper = jacksonObjectMapper().registerModule(RakipModule())
		objectMapper.writeValue(file, genericModel)

		val entry = addEntry(file, filename, URI("http://www.json.org"))
		file.delete()

		return entry
	}
}

class WriterNodeFactory: NodeFactory<WriterNodeModel>() {
	
	override fun createNodeModel() = WriterNodeModel()
	
	override fun getNrNodeViews() = 0
	
	override fun createNodeView(viewIndex: Int, nodeModel: WriterNodeModel): NodeView<WriterNodeModel>? = null
	
	override fun hasDialog() = true
	
	override fun createNodeDialogPane(): NodeDialogPane {
		
		val filePath = SettingsModelString(CFG_FILE, "")
		val fileDlg = DialogComponentFileChooser(filePath, "fileHistory", JFileChooser.SAVE_DIALOG, false,
				".fskx|.FSKX")
		
		val pane = DefaultNodeSettingsPane()
		pane.addDialogComponent(fileDlg)
		
		return pane
	}
}