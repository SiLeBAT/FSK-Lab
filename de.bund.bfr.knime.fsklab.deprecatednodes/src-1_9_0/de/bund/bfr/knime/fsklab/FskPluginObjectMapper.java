/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab;

import org.emfjson.jackson.module.EMFModule;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;

import de.bund.bfr.knime.fsklab.service.FskService;
import metadata.EmfMetadataModule;

public class FskPluginObjectMapper {

  
  /**
   * Object mapper for 1.0.4 classes generated with Swagger at de.bund.bfr.metadata.swagger
   */
  public static final ObjectMapper MAPPER104;
    
  
  
  static {
    // ObjectMapper defaults to use a JsonFactory that automatically closes
    // the stream. When further entries are added to the archive the stream
    // is closed and fails. The AUTO_CLOSE_TARGET needs to be disabled.
    // ThreeTenModule is added for dates and null fields are skipped.
    JsonFactory jsonFactory = new JsonFactory();
    jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    MAPPER104 = new ObjectMapper(jsonFactory).registerModule(new ThreeTenModule())
        .registerModule(new EMFModule()).registerModule(new EmfMetadataModule());

    MAPPER104.setSerializationInclusion(Include.NON_NULL);
    
    
  }

  
}
