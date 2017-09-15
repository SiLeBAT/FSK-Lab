package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

  /** Simulation algorithm. */
  public String algorithm;

  /** URI of the model applies for simulation/prediction. */
  public String simulatedModel;

  public final List<String> parameterSettings = new ArrayList<>();

  /** General description of the simulation. */
  public String description;

  /** Information on the parameters to be plotted. */
  public final List<String> plotSettings = new ArrayList<>();

  /** Pointer to software code (R script). */
  public String visualizationScript;
}
