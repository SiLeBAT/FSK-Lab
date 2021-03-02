package de.bund.bfr.knime.fsklab.v2_0.editor;

import java.util.Arrays;
import org.knime.js.core.JSONViewResponse;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.bund.bfr.rakip.vocabularies.domain.FskmlObject;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FSKEditorJSViewResponse extends JSONViewResponse<FSKEditorJSViewRequest> {
  
  private FskmlObject[] vocabularyItems;
  
  public FSKEditorJSViewResponse(FSKEditorJSViewRequest request) {
    super(request);
  }
  
  public FskmlObject[] getVocabularyItems() {
    return vocabularyItems;
  }
  
  public void setVocabularyItems(FskmlObject[] vocabularyItems) {
    this.vocabularyItems = vocabularyItems;
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

      FSKEditorJSViewResponse other = (FSKEditorJSViewResponse) obj;
      return Arrays.equals(vocabularyItems, other.vocabularyItems);
  }

  @Override
  public int hashCode() {
      return Arrays.hashCode(vocabularyItems);
  }
}
