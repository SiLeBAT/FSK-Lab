package de.bund.bfr.knime.fsklab.nodes;

import java.util.Locale;
import java.util.ResourceBundle;
import de.bund.bfr.knime.fsklab.util.UTF8Control;

/**
 * * Wrapper for resource bundles of the creator node. This class is tested by
 * {@link de.bund.bfr.knime.fsklab.nodes.ReaderNodeBundleTest} to avoid runtime exceptions. The
 * tests should run before making a new build.
 * 
 * @author Miguel de Alba
 */
public class ReaderNodeBundle {

  private final ResourceBundle bundle;

  public ReaderNodeBundle(Locale locale) {
    bundle = ResourceBundle.getBundle("ReaderNodeBundle", locale, new UTF8Control());
  }

  public String getButton() {
    return bundle.getString("button");
  }

  public String getLabel() {
    return bundle.getString("label");
  }

  public String getTooltip() {
    return bundle.getString("tooltip");
  }
}
