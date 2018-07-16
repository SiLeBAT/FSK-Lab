/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.rakip;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.engine.JRis;
import com.gmail.gcolaianni5.jris.exception.JRisException;
import ezvcard.Ezvcard;
import ezvcard.VCard;

public class RakipModule extends SimpleModule {

  private static final long serialVersionUID = 6106945494093650252L;

  public RakipModule() {
    super("RakipModule", Version.unknownVersion());

    addSerializer(Record.class, new JsonSerializer<Record>() {
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
    });

    addDeserializer(Record.class, new JsonDeserializer<Record>() {
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
    });

    addSerializer(VCard.class, new JsonSerializer<VCard>() {
      @Override
      public void serialize(VCard value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException, JsonProcessingException {

        gen.writeStartObject();
        gen.writeStringField("creator", value.writeJson());
        gen.writeEndObject();
      }
    });

    addDeserializer(VCard.class, new JsonDeserializer<VCard>() {
      @Override
      public VCard deserialize(JsonParser p, DeserializationContext ctxt)
          throws IOException, JsonProcessingException {

        final JsonNode node = p.readValueAsTree();
        final String cardString = node.get("creator").asText();
        return Ezvcard.parseJson(cardString).first();
      }
    });
  }
}
