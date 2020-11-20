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
package de.bund.bfr.knime.fsklab.v1_9.fsk2metadata;

import java.io.IOException;
import javax.json.JsonValue;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import metadata.SwaggerUtil;

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
    FskPortObject inObj = (FskPortObject) inObjects[0];
    Object generalInformation = SwaggerUtil.getGeneralInformation(inObj.modelMetadata);
    Object scope = SwaggerUtil.getScope(inObj.modelMetadata);
    Object dataBackground = SwaggerUtil.getDataBackground(inObj.modelMetadata);
    Object modelMath = SwaggerUtil.getModelMath(inObj.modelMetadata);

    // Build container
    final DataTableSpec tableSpec = createTableSpec()[0];
    final BufferedDataContainer container = exec.createDataContainer(tableSpec);

    // Create JSON cells
    final DataCell giCell = createJSONCell(generalInformation);
    final DataCell scopeCell = createJSONCell(scope);
    final DataCell dbCell = createJSONCell(dataBackground);
    final DataCell mathCell = createJSONCell(modelMath);
    final DataCell scriptCell = StringCellFactory.create(inObj.getModel());
    final DataCell visCell = StringCellFactory.create(inObj.getViz());

    // Create and add row to container
    final DefaultRow row =
        new DefaultRow(RowKey.createRowKey(0L), giCell, scopeCell, dbCell, mathCell, scriptCell, visCell);
    container.addRowToTable(row);

    container.close();

    return new BufferedDataTable[] {container.getTable()};
  }

  private static DataCell createJSONCell(final Object object) {
    JsonValue jsonValue = JsonValue.NULL;

    if (object != null) {
      try {
        ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
        final String jsonStr = mapper.writeValueAsString(object);
        jsonValue = JSONUtil.parseJSONValue(jsonStr);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return JSONCellFactory.create(jsonValue);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return createTableSpec();
  }

  private DataTableSpec[] createTableSpec() {
    final DataColumnSpec generalInformationSpec =
        new DataColumnSpecCreator("generalInformation", JSONCell.TYPE).createSpec();
    final DataColumnSpec scopeSpec = new DataColumnSpecCreator("scope", JSONCell.TYPE).createSpec();
    final DataColumnSpec dataBackgroundSpec =
        new DataColumnSpecCreator("dataBackground", JSONCell.TYPE).createSpec();
    final DataColumnSpec modelMathSpec =
        new DataColumnSpecCreator("modelMath", JSONCell.TYPE).createSpec();
    final DataColumnSpec scriptSpec =
        new DataColumnSpecCreator("modelscript", StringCell.TYPE).createSpec();
    final DataColumnSpec visSpec =
        new DataColumnSpecCreator("visualization", StringCell.TYPE).createSpec();

    // table spec
    final DataTableSpecCreator tableSpec = new DataTableSpecCreator()
        .addColumns(generalInformationSpec, scopeSpec, dataBackgroundSpec, modelMathSpec, scriptSpec, visSpec);
    return new DataTableSpec[] {tableSpec.createSpec()};
  }
}
