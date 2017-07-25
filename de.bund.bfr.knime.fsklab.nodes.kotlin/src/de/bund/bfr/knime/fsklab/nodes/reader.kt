package de.bund.bfr.knime.fsklab.nodes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.bund.bfr.fskml.FskMetaDataObject
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType
import de.bund.bfr.fskml.URIS
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.nodes.controller.RController
import de.bund.bfr.rakip.generic.GenericModel
import de.bund.bfr.rakip.generic.RakipModule
import de.unirostock.sems.cbarchive.ArchiveEntry
import de.unirostock.sems.cbarchive.CombineArchive
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.SystemUtils
import org.knime.core.node.ExecutionContext
import org.knime.core.node.InvalidSettingsException
import org.knime.core.node.NoInternalsModel
import org.knime.core.node.NodeDialogPane
import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeLogger
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
import java.io.File
import java.net.URI
import java.nio.file.Path
import javax.swing.JFileChooser


private val IN_TYPES = emptyArray<PortType>()
private val OUT_TYPES = arrayOf(FskPortObject.TYPE)

private val LOGGER = NodeLogger.getLogger("Reader node")

class ReaderNodeModel : NoInternalsModel(IN_TYPES, OUT_TYPES) {

	private val filename = SettingsModelString("filename", "")

	override fun saveSettingsTo(settings: NodeSettingsWO) = filename.saveSettingsTo(settings)
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = filename.loadSettingsFrom(settings)
	override fun validateSettings(settings: NodeSettingsRO) = filename.validateSettings(settings)

	override fun reset() = Unit

	override fun execute(inData: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		val filesMap = mutableMapOf<String, ArchiveEntry>()
		val libNames = mutableListOf<String>()

		val file = FileUtil.getFileFromURL(FileUtil.toURL(filename.stringValue))
		CombineArchive(file).use { archive ->

			archive.getEntriesWithFormat(URIS.r).forEach {
				val type = FskMetaDataObject(it.descriptions[0])
				when (type) {
					ResourceType.modelScript -> filesMap.put("modelScript", it)
					ResourceType.parametersScript -> filesMap.put("paramScript", it)
					ResourceType.visualizationScript -> filesMap.put("visualizationScript", it)
					ResourceType.workspace -> filesMap.put("workspace", it)
				// Does nothing. RAKIP JSON metadata is not supported currently by fskml
					ResourceType.metaData -> Unit
				}
			}

			// Gets metadata file
			val jsonURI = URI("http://www.json.org")
			filesMap.put("metaData", archive.getEntriesWithFormat(jsonURI)[0])

			// Gets library names
			val libUri: URI
			if (SystemUtils.IS_OS_WINDOWS) {
				libUri = URIS.zip
			} else if (SystemUtils.IS_OS_MAC) {
				libUri = URIS.tgz
			} else if (SystemUtils.IS_OS_LINUX) {
				libUri = URIS.tar_gz
			} else throw InvalidSettingsException("Unsupported platform")

			archive.getEntriesWithFormat(libUri).map { it.fileName.split("\\_")[0] }
					.forEach { libNames.add(it) }
		}

		val modelScript = filesMap["modelScript"]?.loadScript() ?: ""
		val paramScript = filesMap["paramScript"]?.loadScript() ?: ""
		val visualizationScript = filesMap["visualizationScript"]?.loadScript() ?: ""
		val workspaceFile: File? = filesMap["workspace"]?.toTempFile()
		val genericModel = filesMap["metaData"]?.loadMetaData() ?:
				throw InvalidSettingsException("Missing model meta data")

		val libFiles: MutableSet<File> = if (libNames.isNotEmpty()) {

			val libRegistry = LibRegistry.instance()

			// Filters and installs missing libraries
			val missingLibs = libNames.filter { !libRegistry.isInstalled(it) }.toList()
			if (missingLibs.isNotEmpty()) {
				libRegistry.installLibs(missingLibs)
			}

			libRegistry.getPaths(libNames).map(Path::toFile).toMutableSet()

		} else mutableSetOf<File>()

		// validate model
		RController().use { controller ->

			// Add path
			val libRegistry = LibRegistry.instance()
			val installationPath = libRegistry.installationPath.toString().replace("\\", "/")
			val cmd = ".libPaths(c('$installationPath', .libPaths()))"
			val newPaths = controller.eval(cmd).asStrings()

			// TODO: validate model with parameter values from parameter script

			// Restore .libPaths() to the original library path which is in the last position
			controller.eval(".libPaths()[${newPaths.size}]")
		}

		val fskObj = FskPortObject(model = modelScript, param = paramScript, viz = visualizationScript,
				genericModel = genericModel, workspace = workspaceFile, libs = libFiles)
		return arrayOf(fskObj)
	}

	override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf(FskPortObjectSpec.INSTANCE)

	private fun ArchiveEntry.toTempFile(): File {
		val file = FileUtil.createTempFile("temp", "")
		extractFile(file)

		return file
	}

	private fun ArchiveEntry.loadScript(): String {

		// Read script from temp file and return script
		return FileUtils.readFileToString(toTempFile(), "UTF-8")
	}

	private fun ArchiveEntry.loadMetaData(): GenericModel {
		val file = toTempFile()
		val objectMapper = jacksonObjectMapper().registerModule(RakipModule())
		return objectMapper.readValue(file, GenericModel::class.java)
	}
}


class ReaderNodeFactory : NodeFactory<ReaderNodeModel>() {

	override fun createNodeModel() = ReaderNodeModel()

	override fun getNrNodeViews() = 0

	override fun createNodeView(viewIndex: Int, nodeModel: ReaderNodeModel): NodeView<ReaderNodeModel>? = null

	override fun hasDialog() = true

	override fun createNodeDialogPane(): NodeDialogPane {

		val filename = SettingsModelString("filename", "")
		val fileChooser = DialogComponentFileChooser(filename, "filename-history", JFileChooser.OPEN_DIALOG, ".fskx")

		// Add widget
		val pane = DefaultNodeSettingsPane()
		pane.createNewGroup("Data source")
		pane.addDialogComponent(fileChooser)

		return pane
	}
}