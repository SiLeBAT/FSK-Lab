package de.bund.bfr.knime.fsklab;

import java.util.HashSet;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.Parameter;

public class FSKJoinRelation {
  private Parameter valueReciever;
  private Parameter valueSource;
  private GenericModel sourceGenericModel;
  
  public Parameter getValueReciever() {
    return valueReciever;
  }
  public void setValueReciever(Parameter valueReciever) {
    this.valueReciever = valueReciever;
  }
  public Parameter getValueSource() {
    return valueSource;
  }
  public void setValueSource(Parameter valueSource) {
    this.valueSource = valueSource;
  }
  public GenericModel getSourceGenericModel() {
    return sourceGenericModel;
  }
  public void setSourceGenericModel(GenericModel sourceGenericModel) {
    this.sourceGenericModel = sourceGenericModel;
  }
  
}
