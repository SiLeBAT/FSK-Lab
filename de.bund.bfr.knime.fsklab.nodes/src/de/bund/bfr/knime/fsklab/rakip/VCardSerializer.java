package de.bund.bfr.knime.fsklab.rakip;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import ezvcard.VCard;

public class VCardSerializer extends JsonSerializer<VCard> {

  @Override
  public void serialize(VCard value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {

    gen.writeStartObject();
    gen.writeStringField("creator", value.writeJson());
    gen.writeEndObject();
  }
}
