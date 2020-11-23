package metadata;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("static-method")
public class ConversionUtilsTest {
  
  private static ObjectMapper MAPPER;
  
  @BeforeClass
  public static void doSetup() {
    MAPPER = new ObjectMapper();
  }

  @Test
  public void testConvertModel_genericModel_toGenericModel() throws Exception {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "genericModel");
    
    assertEquals("genericModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toDataModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "dataModel");
    
    assertEquals("dataModel", convertedMetadata.get("modelType").asText());
  }
  
  
  @Test
  public void testConvertModel_genericModel_toPredictiveModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "predictiveModel");
    
    assertEquals("predictiveModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toOtherModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "otherModel");
    
    assertEquals("otherModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toExposureModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "exposureModel");
    
    assertEquals("exposureModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toToxicologicalModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "toxicologicalModel");
    
    assertEquals("toxicologicalModel", convertedMetadata.get("modelType").asText());
  }
  
  
  @Test
  public void testConvertModel_genericModel_toProcessModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "processModel");
    
    assertEquals("processModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toConsumptionModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "consumptionModel");
    
    assertEquals("consumptionModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toHealthModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "healthModel");
    
    assertEquals("healthModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toRiskModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "riskModel");
    
    assertEquals("riskModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testConvertModel_genericModel_toQraModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode convertedMetadata = utils.convertModel(inputMetadata, "qraModel");
    
    assertEquals("qraModel", convertedMetadata.get("modelType").asText());
  }
  
  @Test
  public void testJoin() throws JsonProcessingException, IOException  {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    JsonNode joined = utils.joinModels(inputMetadata, inputMetadata, "genericModel");
    
    assertEquals("genericModel", joined.get("modelType").asText());
  }
}
