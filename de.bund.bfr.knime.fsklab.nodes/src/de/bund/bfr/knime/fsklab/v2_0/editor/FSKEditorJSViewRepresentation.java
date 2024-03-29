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

import java.util.Map;
import java.util.Objects;
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

  private String modelMetadata;
  private boolean combinedObject;
  private Map<String, String[]> vocabularies;
  private String modelScript;
  private String visScript;
  private String readme;

  public boolean isCombinedObject() {
    return combinedObject;
  }

  public void setCombinedObject(boolean combinedObject) {
    this.combinedObject = combinedObject;
  }

  public String getModelMetadata() {
    return modelMetadata;
  }

  public void setModelMetadata(String modelMetadata) {
    this.modelMetadata = modelMetadata;
  }
  
  public Map<String, String[]> getVocabularies() {
    return vocabularies;
  }
  
  public void setVocabularies(Map<String, String[]> vocabularies) {
    this.vocabularies = vocabularies;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {}

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  public int hashCode() {
    return  Objects.hash(vocabularies);
  }
  public String getModelScript() {
    return modelScript;
  }

  public void setModelScript(String modelScript) {
    this.modelScript = modelScript;
  }

  public String getVisScript() {
    return visScript;
  }

  public void setVisScript(String visScript) {
    this.visScript = visScript;
  }

  public String getReadme() {
    return readme;
  }

  public void setReadme(String readme) {
    this.readme = readme;
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
    
    FSKEditorJSViewRepresentation other = (FSKEditorJSViewRepresentation) obj;
    
    if (vocabularies != null && other.vocabularies != null && vocabularies.equals(other.vocabularies)) {
      return false;
    }
    
    return true;
  }
}
