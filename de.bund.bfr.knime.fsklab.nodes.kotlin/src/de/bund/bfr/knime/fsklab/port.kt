package de.bund.bfr.knime.fsklab

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel
import de.bund.bfr.rakip.createTree
import de.bund.bfr.rakip.editor.DataBackgroundPanel
import de.bund.bfr.rakip.editor.GeneralInformationPanel
import de.bund.bfr.rakip.editor.ModelMathPanel
import de.bund.bfr.rakip.editor.ScopePanel
import de.bund.bfr.rakip.generic.GenericModel
import de.bund.bfr.rakip.generic.RakipModule
import org.apache.commons.io.IOUtils
import org.knime.core.node.ExecutionMonitor
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObject.PortObjectSerializer
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortObjectSpec.PortObjectSpecSerializer
import org.knime.core.node.port.PortObjectSpecZipInputStream
import org.knime.core.node.port.PortObjectSpecZipOutputStream
import org.knime.core.node.port.PortObjectZipInputStream
import org.knime.core.node.port.PortObjectZipOutputStream
import org.knime.core.node.port.PortTypeRegistry
import org.knime.core.util.FileUtil
import java.awt.BorderLayout
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.ListSelectionModel
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * A port object spec for R model port.
 * @author Miguel de Alba, BfR, Berlin.
 */
class FskPortObjectSpec : PortObjectSpec {
	
	companion object {
		val INSTANCE = FskPortObjectSpec()
	}
	override fun getViews() = emptyArray<JComponent>()

	class Serializer : PortObjectSpecSerializer<FskPortObjectSpec>() {

		override fun loadPortObjectSpec(stream: PortObjectSpecZipInputStream) = INSTANCE

		override fun savePortObjectSpec(spec: FskPortObjectSpec, stream: PortObjectSpecZipOutputStream) = Unit
	}
}

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * @property model Model script
 * @property param Parameters script
 * @property viz Visualization script
 * @property genericModel Model meta data
 * @property workspace  R workspace file
 * @property libs R library files

 * @author Miguel de Alba, BfR, Berlin.
 */
class FskPortObject : PortObject {

	companion object {
		val TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject::class.java)
		val TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FskPortObject::class.java, true)

		private var numOfInstances = 0
	}

	var model = ""
	var param = ""
	var viz = ""
	var genericModel: GenericModel? = null
	var workspace: File? = null
	val libs = mutableSetOf<File>()

	val objectNum = numOfInstances

	init {
		numOfInstances++
	}

	override fun getSpec() = FskPortObjectSpec.INSTANCE

	override fun getSummary() = "FSK Object"
	
	override fun getViews() : Array<JComponent> {
		
		val modelScriptPanel = ScriptPanel("Model script", model, false)
		val paramScriptPanel = ScriptPanel("Param script", param, false)
		val vizScriptPanel = ScriptPanel("Visualization script", viz, false)
		
		val generalInformationPanel = GeneralInformationPanel(genericModel?.generalInformation)
		generalInformationPanel.name = "General information"
		
		val scopePanel = ScopePanel(genericModel?.scope)
		scopePanel.name = "Scope"
		
		val dataBackgroundPanel = DataBackgroundPanel(genericModel?.dataBackground)
		dataBackgroundPanel.name = "Data background"
		
		val modelMathPanel = ModelMathPanel(genericModel?.modelMath)
		modelMathPanel.name = "Model math"
		
		val metaDataPane = JScrollPane()
		genericModel?.let { metaDataPane.add(createTree(genericModel = it)) }
		metaDataPane.name = "Meta data"
		
		return arrayOf(modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane, LibrariesPanel())
	}
	
	/** JPanel with list of R libraries. */
	private inner class LibrariesPanel : JPanel(BorderLayout()) {
		
		init {
			name = "Libraries list"
			
			val libNames = libs.map { it.name }.toTypedArray()
			
			val list = JList<String>(libNames)
			list.layoutOrientation = JList.VERTICAL
			list.selectionMode = ListSelectionModel.SINGLE_SELECTION
			
			add(JScrollPane(list))
		}
	}
}

/**
 * Serializer used to save this port object.
 * @return a {@link FskPortObject}.
 */
class FskPortObjectSerializer : PortObjectSerializer<FskPortObject>() {

	val MODEL = "model.R"
	val PARAM = "param.R"
	val VIZ = "viz.R"
	val META_DATA = "metaData"
	val WORKSPACE = "workspace"

	val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(RakipModule())

	override fun savePortObject(portObject: FskPortObject, stream: PortObjectZipOutputStream, exec: ExecutionMonitor) {
		
		// model entry (file with model script)
		stream.putNextEntry(ZipEntry(MODEL))
		IOUtils.write(portObject.model, stream)
		stream.closeEntry()

		// param entry (file with param script)
		stream.putNextEntry(ZipEntry(PARAM))
		IOUtils.write(portObject.param, stream)
		stream.closeEntry()

		// viz entry (file with visualization script)
		stream.putNextEntry(ZipEntry(VIZ))
		IOUtils.write(portObject.viz, stream)
		stream.closeEntry()

		// template entry (file with model meta data)
		stream.putNextEntry(ZipEntry(META_DATA))
		portObject.genericModel?.let {
			val stringVal = objectMapper.writeValueAsString(it)
			IOUtils.write(stringVal, stream)
		}
		stream.closeEntry()
	}

	override fun loadPortObject(stream: PortObjectZipInputStream, spec: PortObjectSpec, exec: ExecutionMonitor): FskPortObject {

		val portObj = FskPortObject()

		// model entry
		stream.getNextEntry()
		portObj.model = IOUtils.toString(stream, "UTF-8")

		// parameters script
		stream.getNextEntry()
		portObj.param = IOUtils.toString(stream, "UTF-8")

		// visualization script
		stream.getNextEntry()
		portObj.viz = IOUtils.toString(stream, "UTF-8")

		// workspace
		stream.getNextEntry()
		portObj.workspace = FileUtil.createTempFile("workspace", ".r")
		FileOutputStream(portObj.workspace).use { FileUtil.copy(stream, it) }

		// libraries
		stream.getNextEntry()
		val libNames = IOUtils.readLines(stream, "UTF-8")

		val libRegistry = LibRegistry.instance()

		// Install missing libraries
		val missingLibs = libNames.filter { !libRegistry.isInstalled(it) }.toList()
		if (missingLibs.isNotEmpty()) libRegistry.installLibs(missingLibs)

		// Adds libs to the paths of the libraries converted to Files
		libRegistry.getPaths(libNames).map { it.toFile() }.forEach { portObj.libs.add(it) }

		stream.close()

		return portObj;
	}
}