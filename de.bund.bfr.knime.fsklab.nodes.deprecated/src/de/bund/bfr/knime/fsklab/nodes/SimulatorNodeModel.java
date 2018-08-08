package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
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
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.SimulationEntity;

public class SimulatorNodeModel extends ExtToolOutputNodeModel {

  private SimulatorNodeSettings nodeSettings = new SimulatorNodeSettings();

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

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
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    FskPortObject inObj = (FskPortObject) inObjects[0];
    if(nodeSettings.getListOfSimulation() != null) {
      inObj.simulations.clear();
      
      // Convert SimulationEntities from the settings to FskSimulation and add them to the port object
      for (SimulationEntity simulationEntity : nodeSettings.getListOfSimulation()) {
        FskSimulation fskSimulation = new FskSimulation(simulationEntity.getSimulationName());
  
        simulationEntity.getSimulationParameters()
            .forEach(it -> fskSimulation.getParameters().put(it.getParameterID(), it.getParameterValue()));
  
        inObj.simulations.add(fskSimulation);
      }
    }
    return new PortObject[] {inObj};
  }
}
