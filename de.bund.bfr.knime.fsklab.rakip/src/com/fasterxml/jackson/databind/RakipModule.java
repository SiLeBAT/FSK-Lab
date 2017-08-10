package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gmail.gcolaianni5.jris.bean.Record;

import ezvcard.VCard;

public class RakipModule extends SimpleModule {

  private static final long serialVersionUID = 6106945494093650252L;

  public RakipModule() {
    super("RakipModule", Version.unknownVersion());

    addSerializer(Record.class, new RisReferenceSerializer());
    addDeserializer(Record.class, new RisReferenceDeserializer());

    addSerializer(VCard.class, new VCardSerializer());
    addDeserializer(VCard.class, new VCardDeserializer());
  }
}
