package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.metadata.swagger.Parameter;

public class ParameterJson {
  private List<DataArray> parameters;
  private JsonGenerator jsonGenerator;
  private OutputStream outputStream;
  private ObjectMapper mapper = FskPlugin.getDefault().MAPPER104;
  private boolean isOutputClosed = true;
  private boolean isInputClosed = true;
  private File jsonFile;
  private JsonParser jsonParser;
  private InputStream inputStream;
  public ParameterJson(File jsonFile)  {

    try {
      this.jsonFile = jsonFile;
      outputStream = new FileOutputStream(jsonFile);
      
      jsonGenerator = mapper.getFactory().createGenerator(outputStream);
      jsonGenerator.useDefaultPrettyPrinter();
      jsonGenerator.writeStartObject();      
      jsonGenerator.writeFieldName("parameters");
      isOutputClosed = false;
    } catch (IOException e) {
      
      closeOutput();
      e.printStackTrace();
    }
  }

  public void closeOutput() {
    try {
      // Write the end object token of parameters
      jsonGenerator.writeEndObject();
      if (!jsonGenerator.isClosed())
        jsonGenerator.close();
      
      outputStream.close();  
      
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      isOutputClosed = true;
    }
  }
  public void closeInput() {
    try {
      // Write the end object token of parameters
      
      if (!jsonParser.isClosed())
        jsonParser.close();
      
      inputStream.close();  
      
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      isInputClosed = true;
    }
  }
  private DataArray getNextParameter(JsonParser jsonParser) throws JsonMappingException, IOException {
    
      return mapper.readValue(jsonParser, DataArray.class);
    
  }
  
  public DataArray getParameter() {
    try {
      if (isOutputClosed) {
        // open Json file stream if it isn't already open
        if (isInputClosed) {
          inputStream  = new FileInputStream(jsonFile);
          isInputClosed = false;
          jsonParser = mapper.getFactory().createParser(inputStream);
          if (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            if (fieldName.equals("parameters")) {
              if (jsonParser.nextToken() == JsonToken.START_ARRAY) {
                if (jsonParser.nextToken() != JsonToken.END_ARRAY) {

                  return getNextParameter(jsonParser);

                }
              }
            }
          }
        } else {
          JsonToken token = jsonParser.nextToken();
          if (token != JsonToken.END_OBJECT) {
            if (token != JsonToken.END_ARRAY) {

              return getNextParameter(jsonParser);

            }
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public void setParameters(List<DataArray> parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates a DataArray containing parameter data and adds it to this ParameterData object.
   * 
   * @param parameter metadata {@link de.bund.bfr.metadata.swagger.Parameter}
   * @param modelId model id
   * @param data JSON data
   * @param parameterType data-type of parameter value
   */
  public void addParameter(Parameter parameter, String modelId, String data, String parameterType,
      String language) {
    
    // Write the start object "parameters:"
    try {
      // write Array of DataArray (Parameters)
      jsonGenerator.writeStartArray();
      mapper.writeValue(jsonGenerator, new DataArray(parameter, modelId, data.replaceAll("\\r?\\n", ""), parameterType, language));
      jsonGenerator.writeEndArray();
      
    } catch(Exception e) {
      e.printStackTrace();
    }

    
  }
}
