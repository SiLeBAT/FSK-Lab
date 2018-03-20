package de.bund.bfr.knime.fsklab;

import de.bund.bfr.knime.fsklab.rakip.Parameter;

public class JoinRelation {
  Parameter sourceParam;
  Parameter targetParam;
  
  public Parameter getSourceParam() {
    return sourceParam;
  }
  public void setSourceParam(Parameter sourceParam) {
    this.sourceParam = sourceParam;
  }
  public Parameter getTargetParam() {
    return targetParam;
  }
  public void setTargetParam(Parameter targetParam) {
    this.targetParam = targetParam;
  }
  
  
}
