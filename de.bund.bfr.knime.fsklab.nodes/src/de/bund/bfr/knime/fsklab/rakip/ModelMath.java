package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class ModelMath {

  public final List<Parameter> parameter = new ArrayList<>();
  public Double sse;
  public Double mse;
  public Double rmse;
  public Double rSquared;
  public Double aic;
  public Double bic;
  public ModelEquation modelEquation;
  public String fittingProcedure;
  public Exposure exposure;
  public final List<String> event = new ArrayList<>();
}
