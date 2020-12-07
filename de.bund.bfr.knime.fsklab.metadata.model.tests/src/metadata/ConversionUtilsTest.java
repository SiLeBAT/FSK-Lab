package metadata;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import de.bund.bfr.metadata.swagger.Model;
import metadata.ConversionUtils.ModelClass;

@SuppressWarnings("static-method")
public class ConversionUtilsTest {
  
  private static ObjectMapper MAPPER;
  
  @BeforeClass
  public static void doSetup() {
    MAPPER = new ObjectMapper().registerModule(new ThreeTenModule());
  }

  @Test
  public void testConvertModel_genericModel_toGenericModel() throws Exception {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.genericModel);
    assertEquals("genericModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toDataModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.dataModel);
    assertEquals("dataModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toPredictiveModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.predictiveModel);
    assertEquals("predictiveModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toOtherModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.otherModel);
    assertEquals("otherModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toExposureModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.exposureModel);
    assertEquals("exposureModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toToxicologicalModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.toxicologicalModel);
    assertEquals("toxicologicalModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toProcessModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.processModel);
    assertEquals("processModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toConsumptionModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.consumptionModel);
    assertEquals("consumptionModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toHealthModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.healthModel);
    assertEquals("healthModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toRiskModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.riskModel);
    assertEquals("riskModel", convertedModel.getModelType());
  }
  
  @Test
  public void testConvertModel_genericModel_toQraModel() throws JsonProcessingException, IOException {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model convertedModel = utils.convertModel(inputMetadata, ModelClass.qraModel);
    assertEquals("qraModel", convertedModel.getModelType());
  }
  
  @Test
  public void testJoin() throws JsonProcessingException, IOException  {
    ConversionUtils utils = new ConversionUtils();

    // Example data
    JsonNode inputMetadata = MAPPER.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    Model joined = utils.joinModels(inputMetadata, inputMetadata, ModelClass.genericModel);
    assertEquals("genericModel", joined.getModelType());
  }
}
