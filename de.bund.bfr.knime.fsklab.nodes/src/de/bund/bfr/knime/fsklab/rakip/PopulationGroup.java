package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class PopulationGroup {

  public String populationName;
  public String targetPopulation;
  public final List<String> populationSpan = new ArrayList<>();
  public final List<String> populationDescription = new ArrayList<>();
  public final List<String> populationAge = new ArrayList<>();
  public String populationGender;
  public final List<String> bmi = new ArrayList<>();
  public final List<String> specialDietGroups = new ArrayList<>();
  public final List<String> patternConsumption = new ArrayList<>();
  public final List<String> region = new ArrayList<>();
  public final List<String> country = new ArrayList<>();
  public final List<String> populationRiskFactor = new ArrayList<>();
  public final List<String> season = new ArrayList<>();
}
