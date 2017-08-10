package com.fasterxml.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import ezvcard.Ezvcard;
import ezvcard.VCard;

public class VCardDeserializer extends JsonDeserializer<VCard> {

  @Override
  public VCard deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {

    final JsonNode node = p.readValueAsTree();
    final String cardString = node.asText();
    return Ezvcard.parseJson(cardString).first();
  }
}
