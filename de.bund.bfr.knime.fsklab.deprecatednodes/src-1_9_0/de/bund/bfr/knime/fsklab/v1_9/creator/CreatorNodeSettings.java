package de.bund.bfr.knime.fsklab.v1_9.creator;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class CreatorNodeSettings {

  private static final String CFG_MODEL_SCRIPT = "modelScript";
  private static final String CFG_VISUALIZATION_SCRIPT = "visualizationScript";
  private static final String CFG_README = "readme";
  private static final String CFG_SPREADSHEET = "spreadsheet";
  private static final String CFG_SHEET = "sheet";
  private static final String CFG_WORKING_DIRECTORY = "workingDirectory";

  /** Path to model script. */
  public String modelScript = "";

  /** Path to visualization script. */
  public String visualizationScript = "";

  /** Path to spreadsheet. */
  public String spreadsheet = "";

  /** Name of the selected sheet in the spreadsheet. */
  public String sheet = "";

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  private String workingDirectory = "";

  /** README. */
  private String readme = "";

  /** @return empty string if not set. */
  public String getWorkingDirectory() {
    return workingDirectory != null ? workingDirectory : "";
  }

  public void setWorkingDirectory(String workingDirectory) {
    if (workingDirectory != null) {
      this.workingDirectory = workingDirectory;
    }
  }

  /** @return empty string if not set. */
  public String getReadme() {
    return readme != null ? readme : "";
  }

  public void setReadme(String readme) {
    if (readme != null) {
      this.readme = readme;
    }
  }

  public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
    modelScript = settings.getString(CFG_MODEL_SCRIPT);
    visualizationScript = settings.getString(CFG_VISUALIZATION_SCRIPT, "");
    workingDirectory = settings.getString(CFG_WORKING_DIRECTORY);
    readme = settings.getString(CFG_README, "");
    spreadsheet = settings.getString(CFG_SPREADSHEET);
    sheet = settings.getString(CFG_SHEET, "");
  }

  public void save(final NodeSettingsWO settings) {
    settings.addString(CFG_MODEL_SCRIPT, modelScript);
    settings.addString(CFG_VISUALIZATION_SCRIPT, visualizationScript);
    settings.addString(CFG_WORKING_DIRECTORY, workingDirectory);
    settings.addString(CFG_README, readme);
    settings.addString(CFG_SPREADSHEET, spreadsheet);
    settings.addString(CFG_SHEET, sheet);
  }
}
