package de.bund.bfr.knime.fsklab;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.emfjson.jackson.module.EMFModule;
import org.knime.core.node.web.WebViewContent;
import org.knime.core.node.wizard.AbstractWizardNodeView;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import metadata.serializers.FSKEMFModule;
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class EMFJSONViewContent implements WebViewContent{
  /**
   * {@inheritDoc}
   * @throws IOException
   * @throws JsonProcessingException
   */
  @Override
  @JsonIgnore
  public final void loadFromStream(final InputStream viewContentStream) throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new FSKEMFModule());
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      ObjectReader reader = mapper.readerForUpdating(this);
      ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
      try {
          Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
          reader.readValue(viewContentStream);
      } finally {
          Thread.currentThread().setContextClassLoader(oldLoader);
      }
  }

  /**
   * {@inheritDoc}
   *
   * @return An {@link OutputStream} containing the JSON string in UTF-8 format.
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonGenerationException
   */
  @Override
  @JsonIgnore
  public final OutputStream saveToStream() throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new FSKEMFModule());
      String viewContentString = mapper.writeValueAsString(this);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      out.write(viewContentString.getBytes(Charset.forName("UTF-8")));
      out.flush();
      return out;
  }

  //Force equals and hashCode

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract boolean equals(Object obj);

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract int hashCode();
}
