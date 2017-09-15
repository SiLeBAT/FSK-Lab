package de.bund.bfr.knime.fsklab.rakip;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class GenericModel {

  public GeneralInformation generalInformation;
  public Scope scope;
  public DataBackground dataBackground;
  public ModelMath modelMath;
  public String visualizationScript;
}
