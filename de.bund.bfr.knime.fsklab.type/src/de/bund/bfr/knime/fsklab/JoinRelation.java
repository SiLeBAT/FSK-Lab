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
package de.bund.bfr.knime.fsklab;

import org.emfjson.jackson.module.EMFModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import metadata.Parameter;

/**
 * An object that describe the relation between two FSK Objects.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class JoinRelation {
  private Parameter sourceParam;
  private Parameter targetParam;
  private String command;
  private String language_written_in;
  private String Id ;
  
 
  public String getId() {
    return Id;
  }
  public void setId(String id) {
    Id = id;
  }
  public String getLanguage_written_in() {
    return language_written_in;
  }
  public void setLanguage_written_in(String language_written_in) {
    this.language_written_in = language_written_in;
  }
  public String getCommand() {
    return command;
  }
  public void setCommand(String command) {
    this.command = command;
  }
  public Parameter getSourceParam() {
    return sourceParam;
  }
  public void setSourceParam(Parameter sourceParam) {
    this.sourceParam = sourceParam;
  }
  public Parameter getTargetParam() {
    return targetParam;
  }
  public void setTargetParam(Parameter targetParam) {
    this.targetParam = targetParam;
  }
  public String getJsonReresentaion() {
    ObjectMapper mapper = EMFModule.setupDefaultMapper();
    String out = "";
    try {
      String sourceParamAsJSONString = mapper.writeValueAsString(sourceParam);
      String targetParamAsJSONString = mapper.writeValueAsString(targetParam);
      out = "{\"sourceParam\" :"+sourceParamAsJSONString+",\"targetParam\" :"+targetParamAsJSONString+",\"command\" :\""+command+"\"}";
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return out; 
  }
}