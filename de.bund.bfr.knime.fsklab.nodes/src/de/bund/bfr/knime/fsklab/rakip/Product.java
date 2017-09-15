package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {

  public String environmentName;
  public String environmentDescription;
  public String environmentUnit;
  public final List<String> productionMethod = new ArrayList<>();
  public final List<String> packaging = new ArrayList<>();
  public final List<String> productTreatment = new ArrayList<>();
  public String originCountry;
  public String originArea;
  public String fisheriesArea;
  public Date productionDate;
  public Date expirationDate;
}
