package metadata;

import java.io.File;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversionUtilsTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testConversion() throws Exception {
    ConversionUtils utils = new ConversionUtils();

    ObjectMapper mapper = new ObjectMapper();

    // Example data
    JsonNode inputMetadata = mapper.readTree(new File("files/metadata.json"));

    // It passes if no exceptions are thrown
    utils.convertModel(inputMetadata, "processModel");
  }
}
