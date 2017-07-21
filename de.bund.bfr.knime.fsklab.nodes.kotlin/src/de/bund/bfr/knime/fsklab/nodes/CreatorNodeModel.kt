package de.bund.bfr.knime.fsklab.nodes

import de.bund.bfr.fskml.RScript
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObject
import de.bund.bfr.rakip.generic.GeneralInformation
import de.bund.bfr.rakip.generic.GenericModel
import de.bund.bfr.rakip.generic.Hazard
import de.bund.bfr.rakip.generic.ModelMath
import de.bund.bfr.rakip.generic.Parameter
import de.bund.bfr.rakip.generic.ParameterClassification
import de.bund.bfr.rakip.generic.Product
import de.bund.bfr.rakip.generic.Scope
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.knime.core.node.ExecutionContext
import org.knime.core.node.InvalidSettingsException
import org.knime.core.node.NoInternalsModel
import org.knime.core.node.NodeLogger
import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType
import org.knime.core.util.FileUtil
import org.rosuda.REngine.REXPMismatchException
import java.io.IOException
import java.net.URL

import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;


private val IN_TYPES = emptyArray<PortType>()
private val OUT_TYPES = arrayOf(FskPortObject.TYPE)

private val LOGGER = NodeLogger.getLogger("CreatorNodeModel")

class CreatorNodeModel : NoInternalsModel(inPortTypes = IN_TYPES, outPortTypes = OUT_TYPES) {

	private val settings = CreatorNodeSettings()

	override fun saveSettingsTo(settings: NodeSettingsWO) = this.settings.saveSettings(settings)

	override fun validateSettings(settings: NodeSettingsRO) = this.settings.validateSettings(settings)

	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = this.settings.loadValidatedSettings(settings)

	override fun reset() = Unit  // does nothing

	override fun execute(inData: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		val portObj = FskPortObject()

		// Reads model script
		if (settings.modelScript.getStringValue().isNullOrEmpty()) {
			throw InvalidSettingsException("Model script is not provided")
		}
		val modelScript = readScript(settings.modelScript.getStringValue())
		portObj.model = modelScript.getScript()

		// Reads parameters script
		if (settings.paramScript.getStringValue().isNullOrEmpty()) {
			portObj.param = ""
		} else {
			portObj.param = readScript(settings.paramScript.getStringValue()).getScript()
		}

		// Reads visualization script
		if (settings.vizScript.getStringValue().isNullOrEmpty()) {
			portObj.viz = ""
		} else {
			portObj.viz = readScript(settings.vizScript.getStringValue()).getScript()
		}

		// Reads model meta data
		if (settings.metaDataDoc.getStringValue().isNotEmpty()) {

			val metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(settings.metaDataDoc.getStringValue()))

			// process metadata
			XSSFWorkbook(metaDataFile).use { book ->

				val sheet = book.getSheetAt(0)
				portObj.genericModel = GenericModel(generalInformation = sheet.getGeneralInformation(), scope = sheet.getScope(), modelMath = sheet.getModelMath())
				portObj.genericModel.generalInformation.software = "R"

				// set variable values and types from parameters script
				val vars = getVariablesFromAssignments(portObj.param)
				val indepParams = portObj.genericModel.modelMath?.parameter?.filter { it.classification == ParameterClassification.input }?.toList()
				indepParams?.forEach { param ->
					vars[param.name.trim()]?.let {
						param.value = it
						param.dataType = getValueType(it)
					}
				}
			}
		}

		if (modelScript.getLibraries().isNotEmpty()) {
			try {
				// Install missing libraries
				val libRegistry = LibRegistry.instance()
				val missingLibs = modelScript.getLibraries().filter { it -> !libRegistry.isInstalled(it) }.toList()
				if (missingLibs.isNotEmpty()) libRegistry.installLibs(missingLibs)

				val libPaths = libRegistry.getPaths(modelScript.getLibraries())
				libPaths.forEach { portObj.libs.add(it.toFile()) }

			} catch (e: RException) {
				TODO("Handle RException")
			} catch (e: REXPMismatchException) {
				TODO("Handle REXPMismatchException")
			}
		}

		return arrayOf(portObj)
	}

	override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf(FskPortObjectSpec)

	/**
	 * Reads R script.
	 *
	 * @param path File path to R model script.
	 * @throws IOException if the file cannot be read
	 */
	private fun readScript(path: String): RScript {

		val trimmedPath = path.trim()

		try {
			val fScript = FileUtil.getFileFromURL(FileUtil.toURL(trimmedPath))
			val script = RScript(fScript)
			return script
		} catch (e: IOException) {
			LOGGER.warn(e.message)
			throw IOException(trimmedPath + ": cannot be read")
		}
	}

	private fun XSSFSheet.getString(rowNumber: Int): String {

		val row = getRow(rowNumber)
		if (row == null)
			throw IllegalArgumentException("Missing row: #$rowNumber")

		return row.getCell(5).getStringCellValue()
	}

	private fun XSSFSheet.getGeneralInformation(): GeneralInformation {

		val modelName = getString(1)
		val modelId = getString(2)
		val modelRights = getString(11)

		// Creation date
		val creationDate = getRow(9).getCell(5).getDateCellValue()
		val modifiedDate = getRow(10).getCell(5).getDateCellValue()
		val modelUrl = URL(getString(16))

		// TODO: process model type
		// TODO: process model subject

		return GeneralInformation(name = modelName, identifier = modelId, creationDate = creationDate,
				modificationDate = mutableListOf(modifiedDate), rights = modelRights,
				isAvailable = true, url = modelUrl)
	}

	private fun XSSFSheet.getScope(): Scope {

		val hazardName = getString(3)
		val hazardDetails = getString(4)
		val hazard = Hazard(hazardType = "", hazardName = hazardName, hazardDescription = hazardDetails, hazardUnit = "")

		val envName = getString(5)
		val envDetails = getString(6)
		val product = Product(environmentName = envName, environmentDescription = envDetails, environmentUnit = "")

		return Scope(product = product, hazard = hazard)
	}

	private fun XSSFSheet.getModelMath(): ModelMath {

		val modelMath = ModelMath()

		// Dependent variables
		val depNames = getString(21).split("\\|\\|")
		val depUnits = getString(22).split("\\|\\|")

		for (i in 0..depNames.size) {
			val name = depNames[i].trim()
			val unit = depUnits[i].trim()

			val param = Parameter(id = "", classification = ParameterClassification.output, name = name, unit = unit, unitCategory = "", dataType = "")
			modelMath.parameter.add(param)
		}

		// Independent variables
		val indepNames = getString(25).split("\\|\\|")
		val indepUnits = getString(26).split("\\|\\|")

		for (i in 0..indepNames.size) {
			val name = indepNames[i].trim()
			val unit = indepUnits[i].trim()

			val param = Parameter(id = "", classification = ParameterClassification.input, name = name, unit = unit, unitCategory = "", dataType = "")
			modelMath.parameter.add(param)
		}


		return modelMath
	}

	private class Assignment(line: String, type: Assignment.Type) {

		enum class Type {
			/** R command with the = assignment operator. E.g. x = value */
			equals,
			/** R command with the <- assignment operator. E.g. x <- value */
			left,
			/**
			 * R command with the <<- scoping assignment operator. E.g. x <<-
			 * value
			 */
			super_left,
			/** R command with the -> assignment operator. E.g. value -> x */
			right,
			/** R command with the ->> assignment operator. E.g. value ->> x */
			super_right
		}

		val variable: String
		val value: String

		init {

			when (type) {
				Type.equals -> {
					val tokens = line.split("||")
					variable = tokens[0]
					value = tokens[1]
				}
				Type.left -> {
					val tokens = line.split("<-")
					variable = tokens[0]
					value = tokens[1]
				}
				Type.super_left -> {
					val tokens = line.split("<<-")
					variable = tokens[0]
					value = tokens[1]
				}
				Type.right -> {
					val tokens = line.split("->")
					variable = tokens[0]
					value = tokens[1]
				}
				Type.super_right -> {
					val tokens = line.split("->>")
					variable = tokens[0]
					value = tokens[1]
				}
			}
		}
	}

	/**
	 * Returns the {@link FskMetaData.DataType} of a value.
	 *
	 * <ul>
	 * <li>"Array of size x,y,z" for Matlab like arrays, c(0, 1, 2, ...)
	 * <li>"Double" for real numbers.
	 * <li>"Integer" for integer numbers.
	 * <li>"Categorical value" for any other variable. E.g. "zero", "eins",
	 * "dos".
	 * </ul>
	 */
	private fun getValueType(value: String): String {

		if (value.startsWith("c(") && value.endsWith(")"))
			return "Array of size x,y,z"

		try {
			value.toInt()
			return "Integer"
		} catch (e: NumberFormatException) {
			try {
				value.toDouble()
				return "Double"
			} catch (e: NumberFormatException) {
				return "Categorical value"
			}
		}
	}

	private fun getVariablesFromAssignments(paramScript: String): Map<String, String> {

		var vars = mutableMapOf<String, String>()

		for (line in paramScript.lines()) {

			val trimmedLine = line.trim()
			if (line.startsWith("#")) continue  // Skip comments

			if (trimmedLine.contains("=")) {
				val assignment = Assignment(trimmedLine, Assignment.Type.equals)
				vars.put(assignment.variable, assignment.value)
			} else if (trimmedLine.contains("<-")) {
				val assignment = Assignment(trimmedLine, Assignment.Type.left)
				vars.put(assignment.variable, assignment.value)
			} else if (trimmedLine.contains("<<-")) {
				val assignment = Assignment(trimmedLine, Assignment.Type.super_left)
				vars.put(assignment.variable, assignment.value)
			} else if (trimmedLine.contains("->>")) {
				val assignment = Assignment(trimmedLine, Assignment.Type.right)
				vars.put(assignment.variable, assignment.value)
			} else if (trimmedLine.contains("->")) {
				val assignment = Assignment(trimmedLine, Assignment.Type.super_right)
				vars.put(assignment.variable, assignment.value)
			}
		}

		return vars;
	}
}
