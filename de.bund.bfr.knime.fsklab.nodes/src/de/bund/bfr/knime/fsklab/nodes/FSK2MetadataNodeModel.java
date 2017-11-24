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
 ***************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.io.IOException;
import javax.json.JsonValue;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.json.JSONCell;
import org.knime.core.data.json.JSONCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.StatelessModel;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.json.util.JSONUtil;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;

public class FSK2MetadataNodeModel extends StatelessModel {

  private static PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static PortType[] OUT_TYPES = {BufferedDataTable.TYPE};

  /**
   * Constructor for the node model.
   */
  protected FSK2MetadataNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    // Get model metadata
    final GenericModel model = ((FskPortObject) inObjects[0]).genericModel;

    // Prepare object mapper to generate JSON strings from metadata elements


    // Build container
    // Argument is not used in configure so null can be passed
    final DataTableSpec tableSpec = configure(null)[0];
    final BufferedDataContainer container = exec.createDataContainer(tableSpec);

    // Create general information cell
    final DataCell giCell = createJSONCell(model.generalInformation);

    // Create scope cell
    final DataCell scopeCell = createJSONCell(model.scope);

    // Create data background cell
    final DataCell dbCell = createJSONCell(model.dataBackground);

    // Create model math cell
    final DataCell mathCell = createJSONCell(model.modelMath);

    // Create and add row to container
    final DefaultRow row =
        new DefaultRow(RowKey.createRowKey(0L), giCell, scopeCell, dbCell, mathCell);
    container.addRowToTable(row);

    container.close();

    return new BufferedDataTable[] {container.getTable()};
  }

  private static DataCell createJSONCell(final Object object) {
    JsonValue jsonValue = JsonValue.NULL;

    if (object != null) {
      try {
        final String jsonStr = FskPlugin.getDefault().OBJECT_MAPPER.writeValueAsString(object);
        jsonValue = JSONUtil.parseJSONValue(jsonStr);
      } catch (IOException e) {
      }
    }

    return JSONCellFactory.create(jsonValue);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

    // Column specs
    final DataColumnSpec generalInformationSpec =
        new DataColumnSpecCreator("General information", JSONCell.TYPE).createSpec();
    final DataColumnSpec scopeSpec = new DataColumnSpecCreator("Scope", JSONCell.TYPE).createSpec();
    final DataColumnSpec dataBackgroundSpec =
        new DataColumnSpecCreator("Data background", JSONCell.TYPE).createSpec();
    final DataColumnSpec modelMathSpec =
        new DataColumnSpecCreator("Model math", JSONCell.TYPE).createSpec();

    // table spec
    final DataTableSpecCreator tableSpec = new DataTableSpecCreator()
        .addColumns(generalInformationSpec, scopeSpec, dataBackgroundSpec, modelMathSpec);
    return new DataTableSpec[] {tableSpec.createSpec()};
  }

  @Override
  protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

    // Column specs
    final DataColumnSpec generalInformationSpec =
        new DataColumnSpecCreator("General information", JSONCell.TYPE).createSpec();
    final DataColumnSpec scopeSpec = new DataColumnSpecCreator("Scope", JSONCell.TYPE).createSpec();
    final DataColumnSpec dataBackgroundSpec =
        new DataColumnSpecCreator("Data background", JSONCell.TYPE).createSpec();
    final DataColumnSpec modelMathSpec =
        new DataColumnSpecCreator("Model math", JSONCell.TYPE).createSpec();

    // table spec
    final DataTableSpecCreator tableSpec = new DataTableSpecCreator()
        .addColumns(generalInformationSpec, scopeSpec, dataBackgroundSpec, modelMathSpec);
    return new DataTableSpec[] {tableSpec.createSpec()};
  }
}
