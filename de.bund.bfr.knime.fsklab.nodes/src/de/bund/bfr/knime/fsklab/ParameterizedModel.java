/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
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
package de.bund.bfr.knime.fsklab;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
/**
 *  A model object that contains only few informations from the original one. 
 *  this Object is describibg the FSK Model to the JAVASCRIPT View.
 *  
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */

public class ParameterizedModel {
  private String modelID;
  private List<Parameter> listOfParameter ;
  public ParameterizedModel() {
    super();
    
  }
  public ParameterizedModel(String modelID, List<Parameter> listOfParameter) {
    super();
    this.modelID = modelID;
    this.listOfParameter = listOfParameter;
  }
  
  public String getModelID() {
    return modelID;
  }
  public void setModelID(String modelID) {
    this.modelID = modelID;
  }
  public List<Parameter> getListOfParameter() {
    return listOfParameter;
  }
  public void setListOfParameter(List<Parameter> listOfParameter) {
    this.listOfParameter = listOfParameter;
  }
  
  
}
