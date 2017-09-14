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

import de.bund.bfr.fskml.RScript
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.NoInternalsModel
import de.bund.bfr.knime.fsklab.nodes.controller.IRController
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.rakip.*
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.knime.core.node.*
import org.knime.core.node.defaultnodesettings.SettingsModelString
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType
import org.knime.core.util.FileUtil
import org.rosuda.REngine.REXPMismatchException
import java.io.IOException
import java.net.URL
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser
import javax.swing.JFileChooser
import org.knime.core.node.NodeDialogPane
import org.knime.core.node.NodeView



private class CreatorNodeSettings {

    // Setting models, with keys and default values
    val modelScript = SettingsModelString("modelScript", "")
    val paramScript = SettingsModelString("paramScript", "")
    val vizScript = SettingsModelString("visualizationScript", "")
    val metaDataDoc = SettingsModelString("spreadsheet", "")

    fun saveSettings(settings: NodeSettingsWO) {
        modelScript.saveSettingsTo(settings)
        paramScript.saveSettingsTo(settings)
        vizScript.saveSettingsTo(settings)
        metaDataDoc.saveSettingsTo(settings)
    }

    fun validateSettings(settings: NodeSettingsRO) {
        modelScript.validateSettings(settings)
        paramScript.validateSettings(settings)
        vizScript.validateSettings(settings)
        metaDataDoc.validateSettings(settings)
    }

    fun loadValidatedSettings(settings: NodeSettingsRO) {
        modelScript.loadSettingsFrom(settings)
        paramScript.loadSettingsFrom(settings)
        vizScript.loadSettingsFrom(settings)
        metaDataDoc.loadSettingsFrom(settings)
    }
}

private val IN_TYPES = emptyArray<PortType>()
private val OUT_TYPES = arrayOf(FskPortObject.TYPE)

private val LOGGER = NodeLogger.getLogger("CreatorNodeModel")

class CreatorNodeModel : NoInternalsModel(inPortTypes = IN_TYPES, outPortTypes = OUT_TYPES) {

    private val settings = CreatorNodeSettings()

    override fun saveSettingsTo(settings: NodeSettingsWO) = this.settings.saveSettings(settings)

    override fun validateSettings(settings: NodeSettingsRO) = this.settings.validateSettings(settings)

    override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = this.settings.loadValidatedSettings(settings)

    override fun reset() = Unit // does nothing

    override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

        // Reads model script
        if (settings.modelScript.stringValue.isNullOrEmpty()) {
            throw InvalidSettingsException("Model script is not provided")
        }
        val modelRScript = readScript(settings.modelScript.stringValue)
        val modelScript = modelRScript.script

        // Reads parameters script
        val paramScript = if (settings.paramScript.stringValue.isNullOrEmpty()) "" else readScript(settings.paramScript.stringValue).script

        // Reads visualization script
        val vizScript = if (settings.vizScript.stringValue.isNullOrEmpty()) "" else readScript(settings.vizScript.stringValue).script

        // Reads model meta data
        if (settings.metaDataDoc.stringValue.isNullOrEmpty()) {
            throw InvalidSettingsException("Model metadata is not provided")
        }
        val metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(settings.metaDataDoc.stringValue))

        // process metadata
        val book = XSSFWorkbook(metaDataFile)
        val sheet = book.getSheetAt(0)
        val genericModel = GenericModel(generalInformation = sheet.getGeneralInformation(), scope = sheet.getScope(), modelMath = sheet.getModelMath())
        genericModel.generalInformation.software = "R"

        // set variable values and types from parameters script
        val vars = getVariablesFromAssignments(paramScript)
        val indepParams = genericModel.modelMath.parameter.filter { it.classification == Parameter.Classification.input }.toList()
        indepParams.forEach { param ->
            vars[param.parameterName.trim()]?.let {
                param.value = it
                param.dataType = getValueType(it)
            }
        }

        val portObj = FskPortObject(model = modelScript, param = paramScript, viz = vizScript, genericModel = genericModel)

        if (modelRScript.libraries.isNotEmpty()) {
            try {
                // Install missing libraries
                val libRegistry = LibRegistry.instance
                val missingLibs = modelRScript.libraries.filter { it -> !libRegistry.isInstalled(it) }.toList()
                if (missingLibs.isNotEmpty()) libRegistry.installLibs(missingLibs)

                val libPaths = libRegistry.getPaths(modelRScript.libraries)
                libPaths.forEach { portObj.libs.add(it.toFile()) }

            } catch (e: IRController.RException) {
                TODO("Handle RException")
            } catch (e: REXPMismatchException) {
                TODO("Handle REXPMismatchException")
            }
        }

        return arrayOf(portObj)
    }

    override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf(FskPortObjectSpec.INSTANCE)


    /**
     * Reads R script.
     * @param path File path to R model script
     * @throws IOException if the file cannot be read
     */
    private fun readScript(path: String): RScript {

        val trimmedPath = path.trim()
        return try {
            val fScript = FileUtil.getFileFromURL(FileUtil.toURL(trimmedPath))
            RScript(fScript)
        } catch (exception: IOException) {
            LOGGER.warn(exception.message, exception)
            throw IOException("$trimmedPath cannot be read")
        }
    }

    private fun XSSFSheet.getString(rowNumber: Int): String? {

        val row = getRow(rowNumber) ?: throw kotlin.IllegalArgumentException("Missing row #$rowNumber")
        return row.getCell(5).stringCellValue
    }

    private fun XSSFSheet.getGeneralInformation(): GeneralInformation {

        val gi = GeneralInformation()
        gi.name = getString(1) ?: ""
        gi.identifier = getString(2) ?: ""
        gi.rights = getString(11) ?: ""

        // Creation date
        gi.creationDate = getRow(9).getCell(5).dateCellValue
        gi.modificationDate.add(getRow(10).getCell(5).dateCellValue)

        val urlString = getString(16)
        gi.url = URL(if (urlString.isNullOrEmpty()) "http://bfr.bund.de/en/home/html" else urlString)

        // TODO: process model type
        // TODO: process model subject

        return gi
    }

    private fun XSSFSheet.getScope(): Scope {

        val scope = Scope()
        scope.hazard.hazardName = getString(3) ?: ""
        scope.hazard.hazardDescription = getString(4) ?: ""
        scope.product.environmentName = getString(5) ?: ""
        scope.product.environmentDescription = getString(6) ?: ""

        return scope
    }

    private fun XSSFSheet.getModelMath(): ModelMath {

        val modelMath = ModelMath()

        // Dependent variables
        val depNames = getString(21)?.split("||")?.map(String::trim)?.toList()
        val depUnits = getString(22)?.split("||")?.map(String::trim)?.toList()

        if (depNames != null && depUnits != null) {
            for ((first, second) in depNames.zip(depUnits)) {
                val param = Parameter(classification = Parameter.Classification.output)
                param.parameterName = first
                param.unit = second

                modelMath.parameter.add(param)
            }
        }

        // Independent variables
        val indepNames = getString(25)?.split("||")?.map(String::trim)?.toList()
        val indepUnits = getString(26)?.split("||")?.map(String::trim)?.toList()

        if (indepNames != null && indepUnits != null) {
            for ((first, second) in indepNames.zip(indepUnits)) {
                val param = Parameter(classification = Parameter.Classification.input)
                param.parameterName = first
                param.unit = second

                modelMath.parameter.add(param)
            }
        }

        return modelMath
    }

    private class Assignment(line: String, type: Assignment.Type) {

        enum class Type {
            /** R command with the = assignment operator. E.g. x = value */
            equals,

            /** R command with the <- assignment operator. E.g. x <- value */
            left,

            /** R command with the <<- assignment operator. E.g. x <<- value */
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

        return try {
            value.toInt()
            "Integer"
        } catch (e: NumberFormatException) {
            try {
                value.toDouble()
                "Double"
            } catch (e: NumberFormatException) {
                "Categorical value"
            }
        }
    }

    private fun getVariablesFromAssignments(paramScript: String): Map<String, String> {

        val vars = mutableMapOf<String, String>()

        for (line in paramScript.lines()) {

            val trimmedLine = line.trim()
            if (line.startsWith("#")) continue  // Skip comments

            when {
                trimmedLine.contains("=") -> {
                    val assignment = Assignment(trimmedLine, Assignment.Type.equals)
                    vars.put(assignment.variable, assignment.value)
                }
                trimmedLine.contains("<-") -> {
                    val assignment = Assignment(trimmedLine, Assignment.Type.left)
                    vars.put(assignment.variable, assignment.value)
                }
                trimmedLine.contains("<<-") -> {
                    val assignment = Assignment(trimmedLine, Assignment.Type.super_left)
                    vars.put(assignment.variable, assignment.value)
                }
                trimmedLine.contains("->>") -> {
                    val assignment = Assignment(trimmedLine, Assignment.Type.right)
                    vars.put(assignment.variable, assignment.value)
                }
                trimmedLine.contains("->") -> {
                    val assignment = Assignment(trimmedLine, Assignment.Type.super_right)
                    vars.put(assignment.variable, assignment.value)
                }
            }
        }

        return vars
    }
}

class CreatorNodeFactory : NodeFactory<CreatorNodeModel>() {

    override fun createNodeModel() = CreatorNodeModel()
    override fun getNrNodeViews() = 0
    override fun createNodeView(viewIndex: Int, nodeModel: CreatorNodeModel?): NodeView<CreatorNodeModel> {
        throw UnsupportedOperationException("No views! $viewIndex")
    }
    override fun hasDialog() = true

    /** {@inheritDoc}  */
    override fun createNodeDialogPane(): NodeDialogPane {

        val settings = CreatorNodeSettings()

        // Create components
        val dlgType = JFileChooser.OPEN_DIALOG
        val rFilters = ".r|.R" // Extension filters for the R script

        val modelScriptChooser = DialogComponentFileChooser(
                settings.modelScript, "modelScript-history", dlgType, rFilters)
        modelScriptChooser.setBorderTitle("Model script (*)")
        modelScriptChooser
                .setToolTipText("Script that calculates the values of the model (Mandatory).")

        val paramScriptChooser = DialogComponentFileChooser(
                settings.paramScript, "paramScript-history", dlgType, rFilters)
        paramScriptChooser.setBorderTitle("Parameters script")
        paramScriptChooser.setToolTipText("Script with the parameter values of the model (Optional).")

        val vizScriptChooser = DialogComponentFileChooser(settings.vizScript, "vizScript-history", dlgType, rFilters)
        vizScriptChooser.setBorderTitle("Visualization script")
        vizScriptChooser.setToolTipText(
                "Script with a number of commands to create plots or charts using the simulation results (Optional).")

        val metaDataChooser = DialogComponentFileChooser(settings.metaDataDoc, "metaData-history", dlgType)
        metaDataChooser.setBorderTitle("XLSX spreadsheet (*)")
        metaDataChooser.setToolTipText("XLSX file with model metadata")

        // Create pane and add components
        val pane = DefaultNodeSettingsPane()
        pane.addDialogComponent(modelScriptChooser)
        pane.addDialogComponent(paramScriptChooser)
        pane.addDialogComponent(vizScriptChooser)
        pane.addDialogComponent(metaDataChooser)

        return pane
    }
}