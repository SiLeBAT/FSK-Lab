package de.bund.bfr.knime.fsklab.v1_9;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Simulation name and parameters. The parameters are ordered by insertion order.
 */
public class FskSimulation implements Serializable {

  private static final long serialVersionUID = -8847610348622517532L;

  private String name;

  private LinkedHashMap<String, String> params;

  public FskSimulation(String name) {
    this.name = name;
    params = new LinkedHashMap<>();
  }

  public FskSimulation() {
    this.name = "";
    params = new LinkedHashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LinkedHashMap<String, String> getParameters() {
    return params;
  }

  @Override
  public String toString() {
    return name;
  }
}
