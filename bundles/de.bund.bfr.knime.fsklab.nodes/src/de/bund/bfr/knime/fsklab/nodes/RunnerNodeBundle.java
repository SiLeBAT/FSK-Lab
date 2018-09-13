package de.bund.bfr.knime.fsklab.nodes;

import java.util.Locale;
import java.util.ResourceBundle;
import de.bund.bfr.knime.fsklab.util.UTF8Control;

/**
 * Wrapper for resource bundles of the creator node. This class is tested by
 * {@link de.bund.bfr.knime.fsklab.nodes.RunnerNodeBundleTest} to avoid runtime exceptions. The
 * tests should run before making a new build.
 * 
 * @author Miguel de Alba
 */
public class RunnerNodeBundle {

  private final ResourceBundle bundle;

  public RunnerNodeBundle(Locale locale) {
    bundle = ResourceBundle.getBundle("RunnerNodeBundle", locale, new UTF8Control());
  }

  public String getWidthLabel() {
    return bundle.getString("width_label");
  }

  public String getWidthTooltip() {
    return bundle.getString("width_tooltip");
  }

  public String getHeightLabel() {
    return bundle.getString("height_label");
  }

  public String getHeightTooltip() {
    return bundle.getString("height_tooltip");
  }

  public String getResolutionLabel() {
    return bundle.getString("res_label");
  }

  public String getResolutionTooltip() {
    return bundle.getString("res_tooltip");
  }

  public String getTextSizeLabel() {
    return bundle.getString("textsize_label");
  }

  public String getTextSizeTooltip() {
    return bundle.getString("textsize_tooltip");
  }

  public String getSimulationLabel() {
    return bundle.getString("simulation_label");
  }

  public String getSimulationTooltip() {
    return bundle.getString("simulation_tooltip");
  }
}
