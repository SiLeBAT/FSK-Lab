package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class ModelCategory {

  /** Type of model. Ultimate goal of the global model. */
  public String modelClass;

  /** Model sub class. Sub-classification of the model given modelClass. */
  public final List<String> modelSubClass = new ArrayList<>();

  public String modelClassComment;

  public final List<String> modelSubSubClass = new ArrayList<>();

  /** Impact of the specific process in the hazard. */
  public final List<String> basicProcess = new ArrayList<>();
}
