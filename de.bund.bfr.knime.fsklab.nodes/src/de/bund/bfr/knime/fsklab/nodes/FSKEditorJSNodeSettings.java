/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import de.bund.bfr.knime.fsklab.nodes.FSKEditorJSNodeDialog.ModelType;

class FSKEditorJSNodeSettings {

  private static final String CFG_README = "readme";
  private static final String CFG_RESOURCES = "resources";
  private static final String CFG_WORKING_DIRECTORY = "workingDirectory";
  private static final String CFG_MODELTYPE = "modelType";

  private ModelType modelType;

  private String readmeFile;

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  private String workingDirectory;

  /** Resources that will be load into the working directory */
  private String[] resources;

  /** @return {@link ModelType#genericModel} if not set. */
  public ModelType getModelType() {
    return modelType != null ? modelType : ModelType.genericModel;
  }

  public void setModelType(ModelType modelType) {
    this.modelType = modelType;
  }

  /** @return empty string if not set. */
  public String getReadmeFile() {
    return readmeFile != null ? readmeFile : "";
  }

  public void setReadme(String readmeFile) {
    if (readmeFile != null && !readmeFile.isEmpty()) {
      this.readmeFile = readmeFile;
    }
  }

  /** @return empty string if not set. */
  public String getWorkingDirectory() {
    return workingDirectory != null ? workingDirectory : "";
  }

  public void setWorkingDirectory(String workingDirectory) {
    if (workingDirectory != null && !workingDirectory.isEmpty()) {
      this.workingDirectory = workingDirectory;
    }
  }
  
  /** @return emtpy array if not set. */
  public String[] getResources() {
    return resources != null ? resources : new String[0];
  }
  
  public void setResources(String[] resources) {
    if (resources != null && resources.length > 0) {
      this.resources = resources;
    }
  }

  void load(final NodeSettingsRO settings) {
    modelType = ModelType.valueOf(settings.getString(CFG_MODELTYPE, "genericModel"));
    readmeFile = settings.getString(CFG_README, "");
    workingDirectory = settings.getString(CFG_WORKING_DIRECTORY, "");
    resources = settings.getStringArray(CFG_RESOURCES, new String[0]);
  }

  void save(final NodeSettingsWO settings) {
    settings.addString(CFG_MODELTYPE, getModelType().name());
    settings.addString(CFG_README, getReadmeFile());
    settings.addString(CFG_WORKING_DIRECTORY, getWorkingDirectory());
    settings.addStringArray(CFG_RESOURCES, getResources());
  }
}
