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
package de.bund.bfr.knime.fsklab.v2_0.editor;

import java.util.Random;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
final class FSKEditorJSViewRepresentation extends JSONViewContent {

  public final int pseudoIdentifier = (new Random()).nextInt();

  /**
   * Port number where FSK-Service is running.
   * 
   * <p>
   * The service port number is to not be saved to settings as it depends on the computer. The same
   * port cannot be available in the future. So it is assigned during runtime by the node model.
   * </p>
   */
  private int servicePort;
  private String controlledVocabularyURL;
  private String modelMetadata;
  
  public String getModelMetadata() {
    return modelMetadata;
  }

  public void setModelMetadata(String modelMetadata) {
    this.modelMetadata = modelMetadata;
  }

  public String getControlledVocabularyURL() {
    return controlledVocabularyURL;
  }

  public void setControlledVocabularyURL(String controlledVocabularyURL) {
    this.controlledVocabularyURL = controlledVocabularyURL;
  }

  public FSKEditorJSViewRepresentation() {
    servicePort = 0;
  }
  
  public int getServicePort() {
    return servicePort;
  }
  
  public void setServicePort(int servicePort) {
    this.servicePort = servicePort;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {}

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  public int hashCode() {
    return servicePort;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    return servicePort == ((FSKEditorJSViewRepresentation)obj).servicePort;
  }
}
