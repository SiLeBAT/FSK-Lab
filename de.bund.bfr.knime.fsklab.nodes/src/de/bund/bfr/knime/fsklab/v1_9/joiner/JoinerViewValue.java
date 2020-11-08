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
package de.bund.bfr.knime.fsklab.v1_9.joiner;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.metadata.swagger.Parameter;

class JoinerViewValue extends JSONViewContent {

  // Configuration keys
  private static final String CFG_MODEL_METADATA = "ModelMetaData";
  private static final String CFG_JOINER_RELATION = "joinRelation";
  private static final String CFG_JSON_REPRESENTATION = "JSONRepresentation";
  private static final String CFG_MODELSCRIPT_TREE = "ModelScriptTree";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT = "originalVisualizationScript";

  private final int pseudoIdentifier = (new Random()).nextInt();
  private final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  public String modelMetaData;
  public JoinRelation[] joinRelations;
  public String jsonRepresentation;
  public String svgRepresentation;
  public String modelScriptTree;
  private String visualizationScript;
  public String different;

  public JoinerViewValue() {
    visualizationScript = "";
  }
  
  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {

    // Add joinRelations as string
    if (joinRelations != null) {
      try {
        String relationsAsString = MAPPER.writeValueAsString(joinRelations);
        settings.addString(CFG_JOINER_RELATION, relationsAsString);
      } catch (JsonProcessingException err) {
        // do nothing
      }
    }

    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, visualizationScript);
    settings.addString(CFG_JSON_REPRESENTATION, jsonRepresentation);
    settings.addString(CFG_MODELSCRIPT_TREE, modelScriptTree);
    settings.addString(CFG_MODEL_METADATA, modelMetaData);
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    
    // Read relations as string
    String relationsAsString = settings.getString(CFG_JOINER_RELATION);
    if (relationsAsString != null) {
      try {
        joinRelations = MAPPER.readValue(relationsAsString, JoinRelation[].class);
      } catch (IOException err) {
        // do nothing
      }
    }
    
    jsonRepresentation = settings.getString(CFG_JSON_REPRESENTATION);
    modelScriptTree = settings.getString(CFG_MODELSCRIPT_TREE);
    modelMetaData = settings.getString(CFG_MODEL_METADATA);
    visualizationScript = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);

  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return false;
  }

  @Override
  public int hashCode() {
    return pseudoIdentifier;
  }
  
  public String getVisualizationScript() {
    return visualizationScript;
  }

  public void setVisualizationScript(String visualizationScript) {
    this.visualizationScript = visualizationScript;
  }

  /**
   * a helper method for migrating parameter id in workflows written in older version
   * 
   * @param newFirstModelParameters new parameters with suffix
   * @param newSecondModelParameters new parameters with suffix
   */
  void updateParameters(List<Parameter> newFirstModelParameters,
      List<Parameter> newSecondModelParameters) {
    if (joinRelations != null) {
      newFirstModelParameters.stream().forEach(newParam -> {
        for (JoinRelation relation : joinRelations) {
          if (newParam.getId().startsWith(relation.getSourceParam())) {
            relation.setCommand(
                relation.getCommand().replaceAll(relation.getSourceParam(), newParam.getId()));
            relation.setSourceParam(newParam.getId());
          }
        }
      });

      newSecondModelParameters.stream().forEach(newParam -> {
        for (JoinRelation relation : joinRelations) {
          if (newParam.getId().startsWith(relation.getTargetParam())) {
            relation.setTargetParam(newParam.getId());
          }
        }
      });
    }
  }
}
