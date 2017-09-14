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

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.jna.Platform
import de.bund.bfr.fskml.FskMetaDataObject
import de.bund.bfr.fskml.URIS
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.NoInternalsModel
import de.bund.bfr.knime.fsklab.rakip.GenericModel
import de.bund.bfr.knime.fsklab.rakip.RakipModule
import de.unirostock.sems.cbarchive.ArchiveEntry
import de.unirostock.sems.cbarchive.CombineArchive
import org.apache.commons.io.FileUtils
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
import javax.swing.JFileChooser


private val IN_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE)
private val OUT_TYPES: Array<PortType> = emptyArray()

class WriterNodeModel : NoInternalsModel(IN_TYPES, OUT_TYPES) {

    private val LOGGER = NodeLogger.getLogger(javaClass)

    private val filename = SettingsModelString("file", "")

    override fun saveSettingsTo(settings: NodeSettingsWO) = filename.saveSettingsTo(settings)
    override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = filename.loadSettingsFrom(settings)
    override fun validateSettings(settings: NodeSettingsRO) = filename.validateSettings(settings)
    override fun reset() = Unit
    override fun configure(inSpecs: Array<PortObjectSpec>): Array<PortObjectSpec> = emptyArray()

    override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {
        val fskObj = inObjects[0] as FskPortObject

        val archiveFile = FileUtil.getFileFromURL(FileUtil.toURL(filename.stringValue))
        archiveFile.delete()

        try {
            CombineArchive(archiveFile).use { archive ->

                // Adds model script
                val modelEntry = archive.addScript(fskObj.model, "model.r")
                modelEntry.addDescription(FskMetaDataObject(FskMetaDataObject.ResourceType.modelScript).metaDataObject)

                // Adds parameter script
                val paramEntry = archive.addScript(fskObj.param, "param.r")
                paramEntry
                        .addDescription(FskMetaDataObject(FskMetaDataObject.ResourceType.parametersScript).metaDataObject)

                // Adds visualization script
                val vizEntry = archive.addScript(fskObj.viz, "visualization.r")
                vizEntry
                        .addDescription(FskMetaDataObject(FskMetaDataObject.ResourceType.visualizationScript).metaDataObject)

                // Adds R workspace file
                if (fskObj.workspace != null) {
                    val workspaceEntry = archive.addEntry(fskObj.workspace, "workspace.r", URIS.r)
                    workspaceEntry.addDescription(FskMetaDataObject(FskMetaDataObject.ResourceType.workspace).metaDataObject)
                }

                // Adds model metadata
                archive.addMetaData(fskObj.genericModel, "metaData.json")

                // Adds R libraries
                val libUri = when {
                    Platform.isWindows() -> URIS.zip
                    Platform.isMac() -> URIS.tgz
                    Platform.isLinux() -> URIS.tar_gz
                    else -> throw InvalidSettingsException("Unsupported platform")
                }

                for (libFile in fskObj.libs) {
                    archive.addEntry(libFile, libFile.name, libUri)
                }

                archive.pack()
            }
        } catch (e: Exception) {
            FileUtils.deleteQuietly(archiveFile)
            LOGGER.error("File could not be created $e")
        }


        return arrayOf()
    }

    private fun CombineArchive.addScript(script: String, filename: String) : ArchiveEntry {

        val file = File.createTempFile("temp", ".r")
        file.writeText(script)

        val entry = addEntry(file, filename, URIS.r)
        file.delete()

        return entry
    }

    private fun CombineArchive.addMetaData(genericModel: GenericModel, filename: String) : ArchiveEntry {

        val file = File.createTempFile("temp", ".json")
        val objectMapper = ObjectMapper().registerModule(RakipModule())
        objectMapper.writeValue(file, genericModel)

        // TODO: JSON URI should be moved to fskml
        val entry = addEntry(file, filename, URI("http://json.org"))
        file.delete()

        return entry
    }
}

class WriterNodeFactory : NodeFactory<WriterNodeModel>() {

    override fun createNodeModel() = WriterNodeModel()
    override fun getNrNodeViews() = 0
    override fun createNodeView(viewIndex: Int, nodeModel: WriterNodeModel): NodeView<WriterNodeModel> {
        throw UnsupportedOperationException("No views! $viewIndex")
    }
    override fun hasDialog() = true

    override fun createNodeDialogPane(): NodeDialogPane {
        // File dialog chooser
        val fileHistoryId = "fileHistory"
        val dlgType = JFileChooser.SAVE_DIALOG
        val directoryOnly = false
        val validExtensions = ".fskx|.FSKX"

        val filePath = SettingsModelString("file", null)
        val fileDlg = DialogComponentFileChooser(filePath, fileHistoryId, dlgType,
                directoryOnly, validExtensions)
        fileDlg.setBorderTitle("Output file")

        val pane = DefaultNodeSettingsPane()
        pane.addDialogComponent(fileDlg)

        return pane
    }
}