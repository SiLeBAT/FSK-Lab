package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class DietaryAssessmentMethod {

  public String collectionTool;
  public int numberOfNonConsecutiveOneDay;
  public String softwareTool;

  public final List<String> numberOfFoodItems = new ArrayList<>();
  public final List<String> recordTypes = new ArrayList<>();
  public final List<String> foodDescriptors = new ArrayList<>();
}
