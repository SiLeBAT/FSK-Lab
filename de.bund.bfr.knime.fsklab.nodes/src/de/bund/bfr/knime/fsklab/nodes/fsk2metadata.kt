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
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.StatelessModel
import de.bund.bfr.knime.fsklab.rakip.RakipModule
import org.knime.core.data.*
import org.knime.core.data.def.DefaultRow
import org.knime.core.data.json.JSONCell
import org.knime.core.data.json.JSONCellFactory
import org.knime.core.node.*
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortType
import org.knime.json.util.JSONUtil
import java.io.IOException
import javax.json.JsonValue

class FSK2MetadataNodeFactory : NodeFactory<FSK2MetadataNodeModel>() {

    override fun createNodeModel() = FSK2MetadataNodeModel()
    override fun getNrNodeViews() = 0
    override fun createNodeView(viewIndex: Int, p1: FSK2MetadataNodeModel): NodeView<FSK2MetadataNodeModel> {
        throw UnsupportedOperationException("No views! $viewIndex")
    }
    override fun hasDialog() = false
    override fun createNodeDialogPane() = DefaultNodeSettingsPane()
}

private val IN_TYPES: Array<PortType> = arrayOf(FskPortObject.TYPE)
private val OUT_TYPES: Array<PortType> = arrayOf(BufferedDataTable.TYPE)

private val LOGGER = NodeLogger.getLogger("FSK to Metadata node")

class FSK2MetadataNodeModel : StatelessModel(IN_TYPES, OUT_TYPES) {

    companion object {
        val objectMapper: ObjectMapper = ObjectMapper().registerModule(RakipModule())
    }

    override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

        // Get model metadata
        val model = (inObjects[0] as FskPortObject).genericModel

        // Build container
        val tableSpec = configure(emptyArray())[0]
        val container = exec.createDataContainer(tableSpec)

        // Create cells
        val generalInformationCell = createJSONCell(model.generalInformation)
        val scopeCell = createJSONCell(model.scope)
        val dataBackgroundCell = createJSONCell(model.dataBackground)
        val modelMathCell = createJSONCell(model.modelMath)

        // Create and add row to container
        container.addRowToTable(DefaultRow(RowKey.createRowKey(0L), generalInformationCell, scopeCell,
                dataBackgroundCell, modelMathCell))
        container.close()

        return arrayOf(container.table)
    }

    override fun configure(inSpecs: Array<out PortObjectSpec>?): Array<PortObjectSpec> {
        val tableSpec = DataTableSpecCreator().addColumns(
                DataColumnSpecCreator("General information", JSONCell.TYPE).createSpec(),
                DataColumnSpecCreator("Scope", JSONCell.TYPE).createSpec(),
                DataColumnSpecCreator("Data background", JSONCell.TYPE).createSpec(),
                DataColumnSpecCreator("Model math", JSONCell.TYPE).createSpec())

        return arrayOf(tableSpec.createSpec())
    }

    override fun configure(inSpecs: Array<out DataTableSpec>?): Array<DataTableSpec> {
        val tableSpec = DataTableSpecCreator().addColumns(
                DataColumnSpecCreator("General information", JSONCell.TYPE).createSpec(),
                DataColumnSpecCreator("Scope", JSONCell.TYPE).createSpec(),
                DataColumnSpecCreator("Data background", JSONCell.TYPE).createSpec(),
                DataColumnSpecCreator("Model math", JSONCell.TYPE).createSpec())

        return arrayOf(tableSpec.createSpec())
    }

    private fun createJSONCell(obj: Any): DataCell {

        val jsonValue = try {
            val jsonString = objectMapper.writeValueAsString(obj)
            JSONUtil.parseJSONValue(jsonString)
        } catch (exception: IOException) {
            LOGGER.warn("Cannot parse $obj", exception)
            JsonValue.NULL
        }

        return JSONCellFactory.create(jsonValue)
    }
}