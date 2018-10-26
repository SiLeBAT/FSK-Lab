package de.bund.bfr.knime.fsklab.features;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;

public enum Features implements Feature {

  @Label("First Feature")
  FEATURE_ONE,

  @Label("Second Feature")
  FEATURE_TWO;

  public boolean isActive() {
    return new SingletonFeatureManagerProvider().getFeatureManager().isActive(this);
  }
}
