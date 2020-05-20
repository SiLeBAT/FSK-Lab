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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

class EditorNodeSettings {
  
  enum ModelType {
    genericModel,
    dataModel,
    predictiveModel,
    exposureModel,
    toxicologicalModel,
    doseResponseModel,
    processModel,
    consumptionModel,
    healthModel,
    riskModel,
    qraModel,
    otherModel;
    
    @Override
    public String toString() {
      switch(this) {
        case genericModel: return "Generic model";
        case dataModel: return "Data model";
        case predictiveModel: return "Predictive model";
        case exposureModel: return "Exposure model";
        case toxicologicalModel: return "Toxicological model";
        case doseResponseModel: return "Dose response model";
        case processModel: return "Process model";
        case consumptionModel: return "Consumption model";
        case healthModel: return "Health model";
        case riskModel: return "Risk model";
        case qraModel: return "QRA model";
        case otherModel: return "Other model";
        default: throw new IllegalArgumentException();
      }
    }
  }

  private static final String CFG_README = "readme";
  private static final String CFG_RESOURCES = "resources";
  private static final String CFG_WORKING_DIRECTORY = "workingDirectory";
  private static final String CFG_MODEL_METADATA = "ModelMetaData";
  private static final String CFG_MODELTYPE = "modelType";
  
  public String modelMetaData;

  private String model;
  private String viz;
  
  private ModelType modelType;
  
  /** Paths to resources: plain text files and R workspace files (.rdata). */
  private String workingDirectory;
  
  /** Resources that will be load into the working directory */
  private String resources;

  private String readme;
  
  
  public EditorNodeSettings() {
    this.model = "";
    this.viz = "";
    this.modelType = ModelType.genericModel;
    this.workingDirectory = "";
    this.resources = "";
    this.readme = "";
  }
  
  /** @return empty string if not set. */
  public String getModel() {
    return model;
  }
  
  /** @param model null or empty string are ignored. */
  public void setModel(String model) {
    if (model != null && !model.isEmpty()) {
      this.model = model;
    }
  }
  
  public boolean hasModel() {
    return !model.isEmpty();
  }
  
  /** @return empty string if not set. */
  public String getViz() {
    return viz;
  }
  
  /** @param viz null or empty string are ignored. */
  public void setViz(String viz) {
    if (viz != null && !viz.isEmpty()) {
      this.viz = viz;
    }
  }
  
  public ModelType getModelType() {
    return modelType;
  }
  
  public void setModelType(ModelType anotherType) {
    this.modelType = anotherType;
  }
  
  public boolean hasViz() {
    return !viz.isEmpty();
  }
  
  /** @return empty string if not set. */
  public String getResources() {
    return resources;
  }

  /** @param resource null or empty string are ignored. */
  public void setResources(String resources) {
    if (resources != null && !resources.isEmpty()) {
      this.resources = resources;
    }
  }
  
  public boolean hasResources() {
    return !resources.isEmpty();
  }

  /** @return empty string if not set. */
  public String getReadme() {
    return readme;
  }

  /** @param readme null or empty string are ignored. */
  public void setReadme(String readme) {
    if (readme != null && !readme.isEmpty()) {
      this.readme = readme;
    }
  }
  
  public boolean hasReadme() {
    return !readme.isEmpty();
  }

  /** @return empty string if not set. */
  public String getWorkingDirectory() {
    return workingDirectory != null ? workingDirectory : "";
  }

  /** @param workingDirectory null or empty string are ignored. */
  public void setWorkingDirectory(String workingDirectory) {
    if (workingDirectory != null && !workingDirectory.isEmpty()) {
      this.workingDirectory = workingDirectory;
    }
  }
  
  public boolean hasWorkingDirectory() {
    return !workingDirectory.isEmpty();
  }

  void load(final NodeSettingsRO settings) {
    resources = settings.getString(CFG_RESOURCES, "");
    readme = settings.getString(CFG_README, "");
    workingDirectory = settings.getString(CFG_WORKING_DIRECTORY, "");
    modelMetaData = settings.getString(CFG_MODEL_METADATA, "");
    
    if (settings.containsKey(CFG_MODELTYPE)) {
      try {
        modelType = ModelType.valueOf(settings.getString(CFG_MODELTYPE));
      } catch (InvalidSettingsException e) {
        modelType = ModelType.genericModel;
      }
    } else {
      modelType = ModelType.genericModel;
    }
  }

  void save(final NodeSettingsWO settings) {
    settings.addString(CFG_RESOURCES, resources);
    settings.addString(CFG_README, readme);
    settings.addString(CFG_WORKING_DIRECTORY, workingDirectory);
    settings.addString(CFG_MODEL_METADATA, modelMetaData);
    settings.addString(CFG_MODELTYPE, modelType.name());
  }
}
