package de.bund.bfr.knime.fsklab.rakip;

import java.io.IOException;
import java.io.StringReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.engine.JRis;
import com.gmail.gcolaianni5.jris.exception.JRisException;

public class RisReferenceDeserializer extends JsonDeserializer<Record> {

  @Override
  public Record deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {

    final JsonNode node = p.readValueAsTree();
    final String referenceString = node.get("reference").asText();

    try (final StringReader reader = new StringReader(referenceString)) {
      return JRis.parse(reader).get(0);
    } catch (JRisException e) {
      throw new IOException(e);
    }
  }
}
