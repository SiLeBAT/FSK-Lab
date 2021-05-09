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
package de.bund.bfr.knime.fsklab.v1_9;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * An object that describe the relation between two FSK Objects.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
@JsonAutoDetect
public class JoinRelation {

  private String sourceParam;
  private String targetParam;
  private String command;
  private String language_written_in;

  public JoinRelation() {
    this("", "", "", "");
  }

  public JoinRelation(String sourceParam, String targetParam, String command,
      String languageWrittenIn) {
    this.sourceParam = sourceParam;
    this.targetParam = targetParam;
    this.command = command;
    this.language_written_in = languageWrittenIn;
  }

  public String getLanguage_written_in() {
    return language_written_in;
  }

  public String getCommand() {
    return command;
  }

  public String getSourceParam() {
    return sourceParam;
  }

  public String getTargetParam() {
    return targetParam;
  }

  public void setSourceParam(String sourceParam) {
    this.sourceParam = sourceParam;
  }

  public void setTargetParam(String targetParam) {
    this.targetParam = targetParam;
  }

  public void setCommand(String command) {
    this.command = command;
  }
}
