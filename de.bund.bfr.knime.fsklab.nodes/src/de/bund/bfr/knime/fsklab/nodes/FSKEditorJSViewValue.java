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

import de.bund.bfr.knime.fsklab.JoinRelation;

import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataFactory;
import metadata.ModelMath;
import metadata.Scope;



class FSKEditorJSViewValue extends JSONViewContent {
  
 
  
  public final int pseudoIdentifier = (new Random()).nextInt();
  
  private GeneralInformation generalInformation ;
  private Scope scope ;
  private DataBackground dataBackground;
  private ModelMath modelMath ;
  private String firstModelScript;
  private String firstModelViz;

  public String getFirstModelScript() {
    return firstModelScript;
  }

  public void setFirstModelScript(String firstModelScript) {
    this.firstModelScript = firstModelScript;
  }

 
  public String getFirstModelViz() {
    return firstModelViz;
  }

  public void setFirstModelViz(String firstModelViz) {
    this.firstModelViz = firstModelViz;
  }
  public GeneralInformation getGeneralInformation() {
    return generalInformation;
  }

  public void setGeneralInformation(GeneralInformation generalInformation) {
    this.generalInformation = generalInformation;
  }

  public Scope getScope() {
    return scope;
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }

  public DataBackground getDataBackground() {
    return dataBackground;
  }

  public void setDataBackground(DataBackground dataBackground) {
    this.dataBackground = dataBackground;
  }

  public ModelMath getModelMath() {
    return modelMath;
  }

  public void setModelMath(ModelMath modelMath) {
    this.modelMath = modelMath;
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
 
}
