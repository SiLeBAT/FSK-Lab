package metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;


@SuppressWarnings("unchecked")
public class ConversionUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper()
			.registerModule(new ThreeTenModule());

	/** Definitions in Swagger YAML. */
	private static final Map<String, Object> definitions;
	static {
	     Yaml yaml = new Yaml();
	     try (InputStream stream = ConversionUtils.class.getClassLoader().getResourceAsStream("model.yaml")) {
	       Map<String, Object> yamlContent = yaml.load(stream);
	       definitions = (Map<String, Object>) yamlContent.get("definitions");
	     } catch (IOException err) {
	       throw new IllegalArgumentException(err);
	     }
	}
	
    public enum ModelClass {
      genericModel(GenericModel.class, definitions.get("GenericModel")),
      dataModel(DataModel.class, definitions.get("DataModel")),
      predictiveModel(PredictiveModel.class, definitions.get("PredictiveModel")),
      otherModel(OtherModel.class, definitions.get("OtherModel")),
      exposureModel(ExposureModel.class, definitions.get("ExposureModel")),
      toxicologicalModel(ToxicologicalModel.class, definitions.get("ToxicologicalModel")),
      doseResponseModel(DoseResponseModel.class, definitions.get("DoseResponseModel")),
      processModel(ProcessModel.class, definitions.get("ProcessModel")),
      consumptionModel(ConsumptionModel.class, definitions.get("ConsumptionModel")),
      healthModel(HealthModel.class, definitions.get("HealthModel")),
      riskModel(RiskModel.class, definitions.get("RiskModel")),
      qraModel(QraModel.class, definitions.get("QraModel")),
      
      // TODO: workaround for old models
      GenericModel(GenericModel.class, definitions.get("GenericModel"));
      
      public final Class<? extends Model> javaClass;
      public final Map<String, Object> swaggerDefinition;
      
      private ModelClass(Class<? extends Model> javaClass, Object swaggerDefinition) {
        this.javaClass = javaClass;
        this.swaggerDefinition = (Map<String, Object>) swaggerDefinition;
      }
    }

	// Top components keys
	private static final String GENERAL_INFORMATION = "generalInformation";
	private static final String SCOPE = "scope";
	private static final String DATA_BACKGROUND = "dataBackground";
	private static final String MODEL_MATH = "modelMath";

	// Swagger tags
	private static final String PROPERTIES = "properties";
	private static final String TYPE = "type";
	private static final String REF = "$ref";

	public ConversionUtils() {

		// TODO: workaround for old models
		// modelMapping.put("GenericModel", definitions.get("GenericModel"));
	}

	public Model convertModel(JsonNode originalMetadata, ModelClass targetClass) throws JsonProcessingException {

		String originalClass = originalMetadata.get("modelType").textValue();
		Map<String, Object> originalModelClass = ModelClass.valueOf(originalClass).swaggerDefinition;
		Map<String, Object> targetModelClass = targetClass.swaggerDefinition;

		// Every model class is an Swagger object with properties (2nd object of allOf):
		// GENERAL_INFORMATION, SCOPE, DATA_BACKGROUND and MODEL_MATH
		List<Object> allOf = (List<Object>) originalModelClass.get("allOf");
		Map<String, Object> originalTopComponents = (Map<String, Object>) ((Map<String, Object>) allOf.get(1))
				.get(PROPERTIES);

		allOf = (List<Object>) targetModelClass.get("allOf");
		Map<String, Object> targetTopComponents = (Map<String, Object>) ((Map<String, Object>) allOf.get(1))
				.get(PROPERTIES);

		JsonNode generalInformationNode = convert(originalMetadata.get(GENERAL_INFORMATION),
				(Map<String, Object>) originalTopComponents.get(GENERAL_INFORMATION),
				(Map<String, Object>) targetTopComponents.get(GENERAL_INFORMATION));

		JsonNode scopeNode = convert(originalMetadata.get(SCOPE),
				(Map<String, Object>) originalTopComponents.get(SCOPE),
				(Map<String, Object>) targetTopComponents.get(SCOPE));

		JsonNode backgroundNode = convert(originalMetadata.get(DATA_BACKGROUND),
				(Map<String, Object>) originalTopComponents.get(DATA_BACKGROUND),
				(Map<String, Object>) targetTopComponents.get(DATA_BACKGROUND));

		JsonNode mathNode = convert(originalMetadata.get(MODEL_MATH),
				(Map<String, Object>) originalTopComponents.get(MODEL_MATH),
				(Map<String, Object>) targetTopComponents.get(MODEL_MATH));

		ObjectNode convertedMetadata = MAPPER.createObjectNode();
		convertedMetadata.put("modelType", targetClass.name());
		convertedMetadata.set(GENERAL_INFORMATION, generalInformationNode);
		convertedMetadata.set(SCOPE, scopeNode);
		convertedMetadata.set(DATA_BACKGROUND, backgroundNode);
		convertedMetadata.set(MODEL_MATH, mathNode);
		
		return MAPPER.treeToValue(convertedMetadata, targetClass.javaClass);
	}

	public Model joinModels(JsonNode modelA, JsonNode modelB, ModelClass targetModelType) throws JsonProcessingException {

		String modelAType = modelA.get("modelType").textValue();
		Map<String, Object> modelAClass = ModelClass.valueOf(modelAType).swaggerDefinition;

		String modelBType = modelB.get("modelType").textValue();
		Map<String, Object> modelBClass = ModelClass.valueOf(modelBType).swaggerDefinition;

		Map<String, Object> targetModelClass = targetModelType.swaggerDefinition;

		// Get top components
		List<Object> allOf = (List<Object>) modelAClass.get("allOf");
		Map<String, Object> modelATopComponents = (Map<String, Object>) ((Map<String, Object>) allOf.get(1))
				.get(PROPERTIES);

		allOf = (List<Object>) modelBClass.get("allOf");
		Map<String, Object> modelBTopComponents = (Map<String, Object>) ((Map<String, Object>) allOf.get(1))
				.get(PROPERTIES);

		allOf = (List<Object>) targetModelClass.get("allOf");
		Map<String, Object> targetTopComponents = (Map<String, Object>) ((Map<String, Object>) allOf.get(1))
				.get(PROPERTIES);

		JsonNode generalInformationNode = join(modelA.get(GENERAL_INFORMATION),
				(Map<String, Object>) modelATopComponents.get(GENERAL_INFORMATION), modelB.get(GENERAL_INFORMATION),
				(Map<String, Object>) modelBTopComponents.get(GENERAL_INFORMATION),
				(Map<String, Object>) targetTopComponents.get(GENERAL_INFORMATION));
		JsonNode scopeNode = join(modelA.get(SCOPE), (Map<String, Object>) modelATopComponents.get(SCOPE),
				modelB.get(SCOPE), (Map<String, Object>) modelBTopComponents.get(SCOPE),
				(Map<String, Object>) targetTopComponents.get(SCOPE));
		JsonNode backgroundNode = join(modelA.get(DATA_BACKGROUND),
				(Map<String, Object>) modelATopComponents.get(DATA_BACKGROUND), modelB.get(DATA_BACKGROUND),
				(Map<String, Object>) modelBTopComponents.get(DATA_BACKGROUND),
				(Map<String, Object>) targetTopComponents.get(DATA_BACKGROUND));
		JsonNode mathNode = join(modelA.get(MODEL_MATH), (Map<String, Object>) modelATopComponents.get(MODEL_MATH),
				modelB.get(MODEL_MATH), (Map<String, Object>) modelBTopComponents.get(MODEL_MATH),
				(Map<String, Object>) targetTopComponents.get(MODEL_MATH));

		ObjectNode joinedNode = MAPPER.createObjectNode();
		joinedNode.put("modelType", targetModelType.name());
		joinedNode.set(GENERAL_INFORMATION, generalInformationNode);
		joinedNode.set(SCOPE, scopeNode);
		joinedNode.set(DATA_BACKGROUND, backgroundNode);
		joinedNode.set(MODEL_MATH, mathNode);

		return MAPPER.treeToValue(joinedNode, targetModelType.javaClass);
	}

	private JsonNode convert(JsonNode originalMetadata, Map<String, Object> originalClass,
			Map<String, Object> targetClass) {
  	    if(originalMetadata == null) {
          return null;
        }
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

			if (field.getValue().isNull()) {
				continue;
			}

			Map<String, Object> originalProp = (Map<String, Object>) originalProperties.get(key);
			Map<String, Object> targetProp = (Map<String, Object>) targetProperties.get(key);

			if (originalProp.containsKey(TYPE) && targetProp.containsKey(TYPE)) {
				String originalPropType = (String) originalProp.get(TYPE);
				String targetPropType = (String) targetProp.get(TYPE);

				if (originalPropType.equals(targetPropType)) {
					if (originalPropType.equals("string") || originalPropType.equals("number")) {
						if(!field.getValue().toString().equals("\"\""))
							node.set(key, field.getValue());
					} else if (originalPropType.equals("array")) {
						ArrayNode convertedProperty = MAPPER.createArrayNode();
						for (JsonNode child : field.getValue()) {
		                    if (child.isValueNode() && !child.isNull()) {
		                    	convertedProperty.add(child);
		                    } else if (child.isObject()) {
		                      JsonNode convertedChild = convert(child, originalProp, targetProp);
		                      convertedProperty.add(convertedChild);
		                    }
		                    // if array, add array (only relevant for modification dates)
		                    else if (child.isArray()) {
		                    	convertedProperty.add(child);
			                }
						}
						if (convertedProperty.size() > 0) {
						  node.set(key, convertedProperty);
						}
					}
				}
			} else if (originalProp.containsKey(REF) && targetProp.containsKey(REF)) {
				JsonNode convertChild = convert(field.getValue(), originalProp, targetProp);
				node.set(key, convertChild);
			}
		}

		return node;
	}

	private JsonNode join(JsonNode metadataA, Map<String, Object> originalClassA, JsonNode metadataB,
			Map<String, Object> originalClassB, Map<String, Object> targetClass) {

		Map<String, Object> classAProperties = getProperties(originalClassA);
		Map<String, Object> classBProperties = getProperties(originalClassB);
		Map<String, Object> targetProperties = getProperties(targetClass);

		ObjectNode node = MAPPER.createObjectNode();

		for (Map.Entry<String, Object> field : targetProperties.entrySet()) {

			final String key = field.getKey();
			Map<String, Object> targetProp = (Map<String, Object>) field.getValue();
			Map<String, Object> propA = (Map<String, Object>) classAProperties.getOrDefault(key,
					Collections.emptyMap());
			Map<String, Object> propB = (Map<String, Object>) classBProperties.getOrDefault(key,
					Collections.emptyMap());

			if (targetProp.containsKey(TYPE) && metadataA != null && metadataB != null) {
				String targetPropertyType = (String) targetProp.get(TYPE);
				if (targetPropertyType.equals("string")) {
					if (targetProp.containsKey("format") && targetProp.get("format").equals("date")) {
						JsonNode newValue = combineDateProperties(key, propA, metadataA, propB, metadataB);
						if (newValue != null) {
							node.set(key, newValue);
						}
					} else {
						String newValue = combineStringProperties(key, propA, metadataA, propB, metadataB);
						if (!newValue.isEmpty()) {
							node.put(key, newValue);
						}
					}

				} else if (targetPropertyType.equals("array")) {
					ArrayNode joinedArray = MAPPER.createArrayNode();

					if (metadataA.has(key)) {
						for (JsonNode child : metadataA.get(key)) {
						  if (child.isTextual()) {
						    joinedArray.add(child.asText());
						  } else if (child.isObject()) {
							JsonNode convertedChild = convert(child, propA, targetProp);
							joinedArray.add(convertedChild);
						  }
						}
					}
					
					if (metadataB.has(key)) {
						for (JsonNode child : metadataB.get(key)) {
							if (child.isTextual()) {
							  joinedArray.add(child.asText());
							} else if (child.isObject()) {
							  JsonNode convertedChild = convert(child, propB, targetProp);
							  joinedArray.add(convertedChild);
							}
						}
					}
					if (joinedArray.size() > 0) {
		                 node.set(key, joinedArray);
					}
				}
			} else if (targetProp.containsKey(REF) && propA.containsKey(REF) && propB.containsKey(REF)) {
				JsonNode joinedChild = join(metadataA.get(key), propA, metadataB.get(key), propB, targetProp);
				node.set(key, joinedChild);
			}

		}

		return node;
	}

	/**
	 * Return joined string properties or empty strings if they cannot be joined or
	 * are missing.
	 * 
	 * @param propA empty map if missing
	 * @param propB empty map if missing
	 */
	private static String combineStringProperties(String key, Map<String, Object> propA, JsonNode metadataA,
			Map<String, Object> propB, JsonNode metadataB) {
		String modelAValue = "";
		if (!propA.isEmpty() && ((String) propA.get(TYPE)).equals("string") && metadataA.has(key)
				&& !metadataA.get(key).isNull()) {
			modelAValue = metadataA.get(key).asText();
		}

		String modelBValue = "";
		if (!propB.isEmpty() && ((String) propB.get(TYPE)).equals("string") && metadataB.has(key)
				&& !metadataB.get(key).isNull()) {
			modelBValue = metadataB.get(key).asText();
		}

		if (!modelAValue.isEmpty() || !modelBValue.isEmpty()) {
			return modelAValue + "_" + modelBValue;
		} else {
			return "";
		}
	}
	
	private static JsonNode combineDateProperties(String key, Map<String, Object> propA, JsonNode metadataA,
			Map<String, Object> propB, JsonNode metadataB) {
		
		if (!propA.isEmpty() && ((String) propA.get(TYPE)).equals("string") && ((String) propA.get("format")).equals("date")
				&& metadataA.has(key) && !metadataA.get(key).isNull()) {
			return metadataA.get(key);
		}
		
		if (!propB.isEmpty() && ((String) propB.get(TYPE)).equals("string") && ((String) propB.get("format")).equals("date")
				&& metadataB.has(key) && !metadataB.get(key).isNull()) {
			return metadataA.get(key);
		}
		
		return null;
	}

	private Map<String, Object> getProperties(Map<String, Object> property) {

		if (property.containsKey(TYPE)) {
			if (property.get(TYPE).equals("object")) {
				return (Map<String, Object>) property.get(PROPERTIES);
			} else if (property.get(TYPE).equals("array") && property.containsKey("items")) {
				return getProperties((Map<String, Object>) property.get("items"));
			}
		}

		if (property.containsKey(REF)) {
			String reference = StringUtils.substringAfter((String) property.get(REF), "#/definitions/");
			if (definitions.containsKey(reference)) {
				return getProperties((Map<String, Object>) definitions.get(reference));
			}
		}

		return Collections.emptyMap();
	}
}
