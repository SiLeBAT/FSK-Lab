package metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings("unchecked")
public class ConversionUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  /** Definitions in Swagger YAML. */
  private final Map<String, Object> definitions;

  /** Mapping of model keys to Swagger definitions. */
  private final Map<String, Object> modelMapping;

  // Top components keys
  private static final String GENERAL_INFORMATION = "generalInformation";
  private static final String SCOPE = "scope";
  private static final String DATA_BACKGROUND = "dataBackground";
  private static final String MODEL_MATH = "modelMath";

  public ConversionUtils() {

    Yaml yaml = new Yaml();
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream("model.yaml")) {
      Map<String, Object> yamlContent = yaml.load(stream);
      definitions = (Map<String, Object>) yamlContent.get("definitions");
    } catch (IOException err) {
      throw new IllegalArgumentException(err);
    }

    // Initialize model mapping
    modelMapping = new HashMap<>();
    modelMapping.put("genericModel", definitions.get("GenericModel"));
    modelMapping.put("dataModel", definitions.get("DataModel"));
    modelMapping.put("predictiveModel", definitions.get("PredictiveModel"));
    modelMapping.put("otherModel", definitions.get("OtherModel"));
    modelMapping.put("exposureModel", definitions.get("ExposureModel"));
    modelMapping.put("toxicologicalModel", definitions.get("ToxicologicalModel"));
    modelMapping.put("doseResponseModel", definitions.get("DoseResponseModel"));
    modelMapping.put("processModel", definitions.get("ProcessModel"));
    modelMapping.put("consumptionModel", definitions.get("ConsumptionModel"));
    modelMapping.put("healthModel", definitions.get("HealthModel"));
    modelMapping.put("riskModel", definitions.get("RiskModel"));
    modelMapping.put("qraModel", definitions.get("QraModel"));
  }

  public JsonNode convertModel(JsonNode originalMetadata, String targetClass) {
    
    String originalClass = originalMetadata.get("modelType").textValue();
    Map<String, Object> originalModelClass = (Map<String, Object>) modelMapping.get(originalClass);
    Map<String, Object> targetModelClass = (Map<String, Object>) modelMapping.get(targetClass);

    List<Object> allOf = (List<Object>) originalModelClass.get("allOf");
    Map<String, Object> originalTopComponents =
        (Map<String, Object>) ((Map<String, Object>) allOf.get(1)).get("properties");

    allOf = (List<Object>) targetModelClass.get("allOf");
    Map<String, Object> targetTopComponents =
        (Map<String, Object>) ((Map<String, Object>) allOf.get(1)).get("properties");

    JsonNode generalInformationNode = convert(originalMetadata.get(GENERAL_INFORMATION),
        (Map<String, Object>) originalTopComponents.get(GENERAL_INFORMATION),
        (Map<String, Object>) targetTopComponents.get(GENERAL_INFORMATION));

    JsonNode scopeNode =
        convert(originalMetadata.get(SCOPE), (Map<String, Object>) originalTopComponents.get(SCOPE),
            (Map<String, Object>) targetTopComponents.get(SCOPE));

    JsonNode backgroundNode = convert(originalMetadata.get(DATA_BACKGROUND),
        (Map<String, Object>) originalTopComponents.get(DATA_BACKGROUND),
        (Map<String, Object>) targetTopComponents.get(DATA_BACKGROUND));

    JsonNode mathNode = convert(originalMetadata.get(MODEL_MATH),
        (Map<String, Object>) originalTopComponents.get(MODEL_MATH),
        (Map<String, Object>) targetTopComponents.get(MODEL_MATH));

    ObjectNode convertedMetadata = MAPPER.createObjectNode();
    convertedMetadata.put("modelType", targetClass);
    convertedMetadata.set(GENERAL_INFORMATION, generalInformationNode);
    convertedMetadata.set(SCOPE, scopeNode);
    convertedMetadata.set(DATA_BACKGROUND, backgroundNode);
    convertedMetadata.set(MODEL_MATH, mathNode);

    return convertedMetadata;
  }

  private JsonNode convert(JsonNode originalMetadata, Map<String, Object> originalClass,
      Map<String, Object> targetClass) {

    Map<String, Object> originalProperties = getProperties(originalClass);
    Map<String, Object> targetProperties = getProperties(targetClass);

    ObjectNode node = MAPPER.createObjectNode();

    Iterator<Entry<String, JsonNode>> fields = originalMetadata.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> field = fields.next();
      String key = field.getKey();

      if (!originalProperties.containsKey(key) || !targetProperties.containsKey(key)) {
        continue;
      }

      Map<String, Object> originalProp = (Map<String, Object>) originalProperties.get(key);
      Map<String, Object> targetProp = (Map<String, Object>) targetProperties.get(key);

      if (originalProp.containsKey("type") && targetProp.containsKey("type")) {
        String originalPropType = (String) originalProp.get("type");
        String targetPropType = (String) targetProp.get("type");
        if (originalPropType.equals(targetPropType)) {
          node.set(key, originalMetadata.get(key));
        }
      }
    }

    return node;
  }

  private Map<String, Object> getProperties(Map<String, Object> property) {

    if (property.containsKey("type") && property.get("type").equals("object")) {
      return (Map<String, Object>) property.get("properties");
    }

    if (property.containsKey("$ref")) {
      String reference =
          StringUtils.substringAfter((String) property.get("$ref"), "#/definitions/");
      if (definitions.containsKey(reference)) {
        return getProperties((Map<String, Object>) definitions.get(reference));
      }
    }

    return Collections.emptyMap();
  }
}
