package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class Scope {

  public Product product;
  public Hazard hazard;
  public PopulationGroup populationGroup;

  /** General comment on the model. */
  public String generalComment;

  /** Temporal information on which the model applies. */
  public String temporalInformation;

  /** Information on which the model applies. */
  public final List<String> region = new ArrayList<>();

  /** Countries on which the model applies. */
  public final List<String> country = new ArrayList<>();
}
