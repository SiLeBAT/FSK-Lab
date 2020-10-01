package de.bund.bfr.knime.fsklab.v1_9.simulator;

import java.io.IOException;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.v1_9.simulator.JSSimulatorViewValue.JSSimulation;

public class JSSimulatorConfig {
  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  private static final String CFG_MODEL_MATH = "modelMath";
  private static final String CFG_SIMULATIONS = "simulations";
  private static final String SIMULATION_INDEX = "selectedSimulationIndex";

  private JSSimulation[] m_simulations;
  private int m_selectedSimulationIndex;
  private String m_modelMath;

  public String getModelMath() {
    return m_modelMath;
  }

  public void setModelMath(String modelMath) {
    this.m_modelMath = modelMath;
  }

  public int getSelectedSimulationIndex() {
    return m_selectedSimulationIndex;
  }

  public void setSelectedSimulationIndex(int selectedSimulationIndex) {
    this.m_selectedSimulationIndex = selectedSimulationIndex;
  }

  public JSSimulation[] getSimulations() {
    return m_simulations;
  }

  public void setSimulations(JSSimulation[] simulations) {
    this.m_simulations = simulations;
  }

  /**
   * Saves current parameters to settings object.
   * 
   * @param settings To save to.
   */
  public void saveSettings(final NodeSettingsWO settings) {
    settings.addInt(SIMULATION_INDEX, m_selectedSimulationIndex);
    settings.addString(CFG_MODEL_MATH, m_modelMath);

    if (m_simulations != null && m_simulations.length != 0) {
      try {
        String simulationStrings = MAPPER.writeValueAsString(m_simulations);
        settings.addString(CFG_SIMULATIONS, simulationStrings);
      } catch (JsonProcessingException e) {
      }
    }
  }

  /**
   * Loads parameters in NodeModel.
   * 
   * @param settings To load from.
   * @throws InvalidSettingsException If incomplete or wrong.
   */
  public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

    m_selectedSimulationIndex = settings.getInt(SIMULATION_INDEX);
    m_modelMath = settings.getString(CFG_MODEL_MATH);

    if (settings.containsKey(CFG_SIMULATIONS)) {
      try {
        String simulationStrings = settings.getString(CFG_SIMULATIONS);
        m_simulations = MAPPER.readValue(simulationStrings, JSSimulation[].class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
