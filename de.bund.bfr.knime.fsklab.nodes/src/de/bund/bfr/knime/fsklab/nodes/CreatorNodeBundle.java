package de.bund.bfr.knime.fsklab.nodes;

import java.util.Locale;
import java.util.ResourceBundle;
import de.bund.bfr.knime.fsklab.util.UTF8Control;

/**
 * Wrapper for resource bundles of the creator node. This class is tested by
 * {@link de.bund.bfr.knime.fsklab.nodes.CreatorNodeBundleTest} to avoid runtime exceptions. The
 * tests should run before making a new build.
 * 
 * @author Miguel de Alba
 */
public final class CreatorNodeBundle {

  private final ResourceBundle bundle;
  
  public CreatorNodeBundle(Locale locale) {
    bundle = ResourceBundle.getBundle("CreatorNodeBundle", locale, new UTF8Control());
  }

  public String getBrowseButton() {
    return bundle.getString("browse_button");
  }

  public String getModelScriptLabel() {
    return bundle.getString("modelscript_label");
  }

  public String getModelScriptTooltip() {
    return bundle.getString("modelscript_tooltip");
  }

  public String getParameterScriptLabel() {
    return bundle.getString("parameterscript_label");
  }

  public String getParameterScriptTooltip() {
    return bundle.getString("parameterscript_tooltip");
  }

  public String getVisualizationScriptLabel() {
    return bundle.getString("visualizationscript_label");
  }

  public String getVisualizationScriptTooltip() {
    return bundle.getString("visualizationscript_tooltip");
  }

  public String getSpreadsheetLabel() {
    return bundle.getString("spreadsheet_label");
  }

  public String getSpreadsheetTooltip() {
    return bundle.getString("spreadsheet_tooltip");
  }

  public String getSheetLabel() {
    return bundle.getString("sheet_label");
  }

  public String getSheetTooltip() {
    return bundle.getString("sheet_tooltip");
  }

  public String getMetadataTitle() {
    return bundle.getString("metadata_title");
  }
}
