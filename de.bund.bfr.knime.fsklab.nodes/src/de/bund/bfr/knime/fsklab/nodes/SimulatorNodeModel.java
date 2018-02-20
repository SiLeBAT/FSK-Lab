package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.json.JSONCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.SimulationEntity;
import de.bund.bfr.knime.fsklab.rakip.Parameter;

public class SimulatorNodeModel extends ExtToolOutputNodeModel {

  private SimulatorNodeSettings nodeSettings = new SimulatorNodeSettings();

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static PortType[] OUT_TYPES = {BufferedDataTable.TYPE};

  public SimulatorNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  // --- internal settings methods ---

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  @Override
  protected void reset() {}

  // --- node settings methods ---
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.saveSettings(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.loadSettings(settings);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new DataTableSpec[] {NodeUtils.createSimulationTableSpec()};
  }

  @Override
  protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
    return new DataTableSpec[] {NodeUtils.createSimulationTableSpec()};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    List<SimulationEntity> listOfSimulation = nodeSettings.getListOfSimulation();

    // Prepare object mapper to generate JSON strings from metadata elements

    // Build container
    final BufferedDataContainer container =
        exec.createDataContainer(NodeUtils.createSimulationTableSpec());

    for (SimulationEntity se : listOfSimulation) {

      JsonObjectBuilder builder = Json.createObjectBuilder();
      for (Parameter param : se.getSimulationParameters()) {
        builder.add(param.name, Double.parseDouble(param.value));
      }

      RowKey rowKey = RowKey.createRowKey(container.size());
      StringCell nameCell = new StringCell(se.getSimulationName());
      DataCell paramCell = JSONCellFactory.create(builder.build());

      container.addRowToTable(new DefaultRow(rowKey, nameCell, paramCell));
    }

    container.close();

    return new PortObject[] {container.getTable()};
  }
}
