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
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.ModelMath;
import metadata.Scope;



class JoinerViewValue extends JSONViewContent {
  
 
  
  public final int pseudoIdentifier = (new Random()).nextInt();
  
  private GeneralInformation firstGeneralInformation ;
  private Scope firstScope ;
  private DataBackground firstDataBackground;
  private ModelMath firstModelMath ;
  private GeneralInformation secondGeneralInformation ;
  private Scope secondScope ;
  private DataBackground secondDataBackground;
  private ModelMath secondModelMath ;
  
  private String firstModelScript;
  private String secondModelScript;
  
  private String firstModelViz;
  private String secondModelViz;
  public String getFirstModelScript() {
    return firstModelScript;
  }

  public void setFirstModelScript(String firstModelScript) {
    this.firstModelScript = firstModelScript;
  }

  public String getSecondModelScript() {
    return secondModelScript;
  }

  public void setSecondModelScript(String secondModelScript) {
    this.secondModelScript = secondModelScript;
  }

  public String getFirstModelViz() {
    return firstModelViz;
  }

  public void setFirstModelViz(String firstModelViz) {
    this.firstModelViz = firstModelViz;
  }

  public String getSecondModelViz() {
    return secondModelViz;
  }

  public void setSecondModelViz(String secondModelViz) {
    this.secondModelViz = secondModelViz;
  }

  
 
  public GeneralInformation getFirstGeneralInformation() {
    return firstGeneralInformation;
  }

  public void setFirstGeneralInformation(GeneralInformation firstGeneralInformation) {
    this.firstGeneralInformation = firstGeneralInformation;
  }

  public Scope getFirstScope() {
    return firstScope;
  }

  public void setFirstScope(Scope firstScope) {
    this.firstScope = firstScope;
  }

  public DataBackground getFirstDataBackground() {
    return firstDataBackground;
  }

  public void setFirstDataBackground(DataBackground firstDataBackground) {
    this.firstDataBackground = firstDataBackground;
  }

  public ModelMath getFirstModelMath() {
    return firstModelMath;
  }

  public void setFirstModelMath(ModelMath firstModelMath) {
    this.firstModelMath = firstModelMath;
  }

  public GeneralInformation getSecondGeneralInformation() {
    return secondGeneralInformation;
  }

  public void setSecondGeneralInformation(GeneralInformation secondGeneralInformation) {
    this.secondGeneralInformation = secondGeneralInformation;
  }

  public Scope getSecondScope() {
    return secondScope;
  }

  public void setSecondScope(Scope secondScope) {
    this.secondScope = secondScope;
  }

  public DataBackground getSecondDataBackground() {
    return secondDataBackground;
  }

  public void setSecondDataBackground(DataBackground secondDataBackground) {
    this.secondDataBackground = secondDataBackground;
  }

  public ModelMath getSecondModelMath() {
    return secondModelMath;
  }

  public void setSecondModelMath(ModelMath secondModelMath) {
    this.secondModelMath = secondModelMath;
  }



  private List<JoinRelation> joinRelations = new ArrayList<JoinRelation>();
  private String jsonRepresentation;
  private String svgRepresentation;
  public String getJsonRepresentation() {
    return jsonRepresentation;
  }

  public void setJsonRepresentation(String jsonRepresentation) {
    this.jsonRepresentation = jsonRepresentation;
  }

 

  public String getSvgRepresentation() {
    return svgRepresentation;
  }

  public void setSvgRepresentation(String svgRepresentation) {
    this.svgRepresentation = svgRepresentation;
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
