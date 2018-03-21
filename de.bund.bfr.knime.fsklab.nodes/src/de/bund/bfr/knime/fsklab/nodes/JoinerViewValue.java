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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.knime.core.node.*;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.ParameterizedModel;



class JoinerViewValue extends JSONViewContent {
  
 
  
  public final int pseudoIdentifier = (new Random()).nextInt();
  
  private ParameterizedModel firstModel;
  private ParameterizedModel secondModel;
  private List<JoinRelation> joinRelations = new ArrayList<JoinRelation>();
  private String jsonRepresentation;

  public String getJsonRepresentation() {
    return jsonRepresentation;
  }

  public void setJsonRepresentation(String jsonRepresentation) {
    this.jsonRepresentation = jsonRepresentation;
  }

  public ParameterizedModel getFirstModel() {
    return firstModel;
  }

  public void setFirstModel(ParameterizedModel firstModel) {
    this.firstModel = firstModel;
  }

  public ParameterizedModel getSecondModel() {
    return secondModel;
  }

  public void setSecondModel(ParameterizedModel secondModel) {
    this.secondModel = secondModel;
  }

  
  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {}

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

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
  public List<JoinRelation> getJoinRelations() {
    return joinRelations;
  }

  public void setJoinRelations(List<JoinRelation> joinRelations) {
    this.joinRelations = joinRelations;
  }
  public void addJoinRelation(JoinRelation jr) {
    joinRelations.add(jr);
  }
}
