package de.bund.bfr.knime.fsklab.v1_9.editor;

import org.knime.js.core.JSONViewRequest;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FSKEditorJSViewRequest extends JSONViewRequest {

  private String vocabulary;
  
  public String getVocabulary() {
    return vocabulary;
  }
  
  public void setVocabulary(String vocabulary) {
    this.vocabulary = vocabulary;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    
    FSKEditorJSViewRequest other = (FSKEditorJSViewRequest)obj;
    return vocabulary.equals(other.vocabulary);
  }
  
  @Override
  public int hashCode() {
    return vocabulary.hashCode();
  }
}
