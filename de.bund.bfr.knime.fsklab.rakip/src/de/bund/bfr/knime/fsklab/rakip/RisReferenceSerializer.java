package de.bund.bfr.knime.fsklab.rakip;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.engine.JRis;
import com.gmail.gcolaianni5.jris.exception.JRisException;

public class RisReferenceSerializer extends JsonSerializer<Record> {

  @Override
  public void serialize(Record value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {

    gen.writeStartObject();

    try (final StringWriter writer = new StringWriter()) {
      JRis.build(Arrays.asList(value), writer);
      gen.writeStringField("reference", writer.toString());
    } catch (JRisException e) {
      throw new IOException(e);
    }

    gen.writeEndObject();
  }
}
