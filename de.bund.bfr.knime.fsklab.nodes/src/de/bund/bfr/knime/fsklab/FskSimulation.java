package de.bund.bfr.knime.fsklab;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FskSimulation implements Serializable {

  private static final long serialVersionUID = -8847610348622517532L;

  private String name;

  private Map<String, Double> params;

  public FskSimulation(String name) {
    this.name = name;
    params = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setSimulationName(String name) {
    this.name = name;
  }

  public Map<String, Double> getParameters() {
    return params;
  }

  @Override
  public String toString() {
    return name;
  }
}
