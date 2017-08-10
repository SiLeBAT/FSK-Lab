package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class Parameter {

  public static enum Classification {
    input, output, constant
  }

  public String id;
  public Classification classification;
  public String name;
  public String description;
  public String unit;
  public String unitCategory;
  public String dataType;
  public String source;
  public String subject;
  public String distribution;
  public String value;
  public String reference;
  public String variabilitySubject;
  public final List<String> modelApplicability = new ArrayList<>();
  public Double error;
}
