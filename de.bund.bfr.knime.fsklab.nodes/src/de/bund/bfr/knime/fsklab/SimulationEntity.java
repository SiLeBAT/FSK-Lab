package de.bund.bfr.knime.fsklab;

import java.util.List;
import de.bund.bfr.knime.fsklab.rakip.Parameter;

public class SimulationEntity {
  private String simulationName;
  private List<Parameter> simulationParameters;

  public String getSimulationName() {
    return simulationName;
  }

  public void setSimulationName(String simulationName) {
    this.simulationName = simulationName;
  }

  public List<Parameter> getSimulationParameters() {
    return simulationParameters;
  }

  public void setSimulationParameters(List<Parameter> simulationParameters) {
    this.simulationParameters = simulationParameters;
  }

  @Override
  public String toString() {
    return simulationName;
  }
}
