package com.fasterxml.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

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
