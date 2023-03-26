package de.bund.bfr.codegenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.nio.file.Paths;
import java.nio.file.Files;
public class Generator {
  static Map<String, JsonNode> refrences = new HashMap<String, JsonNode>();
  private static Map<String, JsonNode> primaryRefrences = new HashMap<String, JsonNode>();

  private static Map<String, JsonNode> complexRefrences = new HashMap<String, JsonNode>();
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static SchemaDescriptor schemaDescriptor = new SchemaDescriptor();
  private static Map <String, ObjectDescriptor> resolvedObjectDescriptorMap = new HashMap<String, ObjectDescriptor>();
  public static void main(String[] args) throws IOException {

    URL inputJson = new URL(
        "https://raw.githubusercontent.com/RakipInitiative/Model-Metadata-Schema/main/modelMetadataSchema104.json");

    Path newFolderPath = Paths.get("../de.bund.bfr.knime.js/src/js/app/generatedclasses");
  	if(!newFolderPath.toFile().exists())
  		Files.createDirectory(newFolderPath);
  	FileUtils.cleanDirectory(newFolderPath.toFile()); 	
    JsonNode rootNode = objectMapper.readTree(inputJson);
    // System.out.printf("root: %s type=%s%n", rootNode.toPrettyString(), rootNode.getNodeType());
    traverse(rootNode, 1);
    extractReferences(rootNode);
    MustacheFactory mf = new DefaultMustacheFactory();
    Path classTemplate = Paths.get("./src/main/resources/ClassTemplate.mustache");
    Mustache mustache = mf.compile(classTemplate.toAbsolutePath().toString());
    createPrimaryRefsUISchema(mustache);
    
    Path complexClassTemplate = Paths.get("./src/main/resources/ComplexClassTemplate.mustache");
    Mustache complexMustache = mf.compile(complexClassTemplate.toAbsolutePath().toString());
    createComplexRefsUISchema(complexMustache);
    
    Path JSHelperTemplate = Paths.get("./src/main/resources/JSHelper.mustache");
    Mustache JSHelperMustache = mf.compile(JSHelperTemplate.toAbsolutePath().toString());
    Path jSHelperPath = Paths.get("../de.bund.bfr.knime.js/src/js/app/generatedclasses/JSHelper.js");
  	Files.createFile(jSHelperPath);
    JSHelperMustache.execute(new FileWriter(jSHelperPath.toFile().getAbsolutePath()),new Object()).flush();

    
    /*Map<String, JsonNode> convertedPrimaryReferences = convertPrimaryReferencesAsUISchema();
    convertedPrimaryReferences.forEach((k, v) -> {
      System.out.println((k + ":" + v.toPrettyString()));
    });
    Map<String, JsonNode> convertedComplexReferences = convertComplexReferencesAsUISchema();
    complexRefrences.forEach((k, v) -> {
      System.out.println((k + ":" + v.toPrettyString()));
    });*/
    
    
  }

  private static Map<String, JsonNode> convertPrimaryReferencesAsUISchema() {
    Map<String, JsonNode> convertedPrimaryReferences = new HashMap<String, JsonNode>();
    primaryRefrences.forEach((k, v) -> {
      JsonNode convertedValue = convertPrimaryValue(k, v);
      convertedPrimaryReferences.put(k, convertedValue);
    });
    return convertedPrimaryReferences;
  }
  
  private static void createPrimaryRefsUISchema(Mustache mustache) {
    
    primaryRefrences.forEach((k, v) -> {
      ObjectDescriptor convertedValue = convertPrimaryValueToObjectDescriptor(k, v);
      schemaDescriptor.primaryObjects.add(convertedValue);
      try {
    	Path newFilePath = Paths.get("../de.bund.bfr.knime.js/src/js/app/generatedclasses/"+convertedValue.className+".js");
    	Files.createFile(newFilePath);
        mustache.execute(new FileWriter(newFilePath.toFile().getAbsolutePath()),convertedValue).flush();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    });
  }
  private static void createComplexRefsUISchema(Mustache mustache) {
    
    complexRefrences.forEach((k, v) -> {
      ComplexObjectDescriptor convertedValue = convertComplexValueToComplexObjectDescriptor(k, v);
      schemaDescriptor.complexObjects.add(convertedValue);
      try {
    	Path newFilePath = Paths.get("../de.bund.bfr.knime.js/src/js/app/generatedclasses/"+convertedValue.className+".js");
      	Files.createFile(newFilePath);
        mustache.execute(new FileWriter(newFilePath.toFile().getAbsolutePath()),convertedValue).flush();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    });
  }
  private static Map<String, JsonNode> convertComplexReferencesAsUISchema() {
    Map<String, JsonNode> convertedComplexReferences = new HashMap<String, JsonNode>();
    primaryRefrences.forEach((k, v) -> {
      JsonNode convertedValue = convertComplexValue(k, v);
      convertedComplexReferences.put(k, convertedValue);
    });
    return convertedComplexReferences;
  }

  private static JsonNode convertComplexValue(String parentName, JsonNode currentvalue) {
    List<String> requiredList = new ArrayList<String>();
    ArrayNode requiredNodes = (ArrayNode) currentvalue.get("required");
    if(requiredNodes != null)
      requiredNodes.forEach(item -> {requiredList.add(item.asText());});
    JsonNode value= currentvalue.get("properties");
    ArrayNode convertedArraytValue = objectMapper.createArrayNode();
    value.fieldNames().forEachRemaining((String propertyName) -> {
      processPropertyNode(propertyName, value, requiredList, convertedArraytValue);
    });
    return convertedArraytValue;
  }

  private static JsonNode convertPrimaryValue(String parentName, JsonNode currentvalue) {
    List<String> requiredList = new ArrayList<String>();
    ArrayNode requiredNodes = (ArrayNode) currentvalue.get("required");
    if(requiredNodes != null)
      requiredNodes.forEach(item -> {requiredList.add(item.asText());});
    JsonNode value= currentvalue.get("properties");
    ArrayNode convertedArraytValue = objectMapper.createArrayNode();
    value.fieldNames().forEachRemaining((String propertyName) -> {
      processPropertyNode(propertyName, value, requiredList, convertedArraytValue);
    });
    return convertedArraytValue;
  }
  
  private static ObjectDescriptor convertPrimaryValueToObjectDescriptor(String parentName, JsonNode currentvalue) {
    List<String> requiredList = new ArrayList<String>();
    ArrayNode requiredNodes = (ArrayNode) currentvalue.get("required");
    if(requiredNodes != null)
      requiredNodes.forEach(item -> {requiredList.add(item.asText());});
    JsonNode value= currentvalue.get("properties");
    ObjectDescriptor objectDescriptor = new  ObjectDescriptor();
    objectDescriptor.className = parentName;
    
    objectDescriptor.JSONRepresentation =  org.apache.commons.text.StringEscapeUtils.unescapeHtml4(value.toString());
    value.fieldNames().forEachRemaining((String propertyName) -> {
      processPropertyNode(propertyName, value, requiredList, objectDescriptor,parentName);
    });
    resolvedObjectDescriptorMap.put(parentName, objectDescriptor);

    return objectDescriptor;
  }
  
  private static ComplexObjectDescriptor convertComplexValueToComplexObjectDescriptor(String parentName, JsonNode currentvalue) {
    List<String> requiredList = new ArrayList<String>();
    ArrayNode requiredNodes = (ArrayNode) currentvalue.get("required");
    if(requiredNodes != null)
      requiredNodes.forEach(item -> {requiredList.add(item.asText());});
    
    ComplexObjectDescriptor complexObjectDescriptor = new  ComplexObjectDescriptor();
    complexObjectDescriptor.className = parentName;
    
    
    if(!hasPolymorfismSign(currentvalue)) {
      JsonNode value= currentvalue.get("properties");
      complexObjectDescriptor.JSONRepresentation =  org.apache.commons.text.StringEscapeUtils.unescapeHtml4(value.toString());
      value.fieldNames().forEachRemaining((String propertyName) -> {
        if(!hasComplexfield(value.get(propertyName)))
          processPropertyNode(propertyName, value, requiredList, complexObjectDescriptor,parentName);
        else {
          processReferenceNode(propertyName, value, requiredList, complexObjectDescriptor,parentName);
        }

      });
    }else {
      currentvalue.get("allOf").forEach(element -> {
        if(element.has("$ref")) {
          JsonNode node = element.get("$ref");
          complexObjectDescriptor.parentClassName = node.toString().substring("#/$defs/".length()+1) ;

        }
        if(element.has("properties")){
          JsonNode value= element.get("properties");
          complexObjectDescriptor.JSONRepresentation =  org.apache.commons.text.StringEscapeUtils.unescapeHtml4(value.toString());
          value.fieldNames().forEachRemaining((String propertyName) -> {
            if(!hasComplexfield(value.get(propertyName)))
              processPropertyNode(propertyName, value, requiredList, complexObjectDescriptor,parentName);
            else {
              processReferenceNode(propertyName, value, requiredList, complexObjectDescriptor,parentName);
            }

          });
        }
      });
    }
   
    resolvedObjectDescriptorMap.put(parentName, complexObjectDescriptor);

    return complexObjectDescriptor;
  }
  
  private static void processReferenceNode(String propertyName, JsonNode value,
      List<String> requiredList, ComplexObjectDescriptor complexObjectDescriptor,
      String parentName) {
    JsonNode node = value.get(propertyName);
    ComplexObject co = new ComplexObject();
    
    String referencedClassName="";
    if(node.get("$ref") != null) {
      String referrenceValue = node.get("$ref").toString();
      referencedClassName = referrenceValue.substring("#/$defs/".length()+1,referrenceValue.length()-1) ;
      co.type = "Object";
    }else if(node.get("items") != null) {
      //TODO set as array
      String referrenceValue = node.get("items").get("$ref").toString();
      referencedClassName = referrenceValue.substring("#/$defs/".length()+1,referrenceValue.length()-1) ;
      co.type = "Array";
    }
    co.name = referencedClassName;
    
    complexObjectDescriptor.classes.add(co);

    
  }

  public static void processPropertyNode(String propertyName, JsonNode value, List<String> requiredList, ArrayNode convertedArraytValue) {
    FieldDescriptor fieldDescriptor = new FieldDescriptor();
    ObjectNode convertedValue = objectMapper.createObjectNode();
    JsonNode v = value.get(propertyName);
    convertedValue.put("id", propertyName);
    fieldDescriptor.id = propertyName;
    if(requiredList.contains(propertyName)) {
      convertedValue.put("required", true);
      fieldDescriptor.required = true;
    }
    v.fieldNames().forEachRemaining((String fieldName) -> {
      
      if (fieldName.equals("type")) {
        if (v.get("type").asText().equals("string")) {
          JsonNode format = v.get("format");
          if (format != null && format.asText().equals("long-text")) {
            convertedValue.put("type", "long-text");
            fieldDescriptor.type = "long-text";
          }else if (format != null && format.asText().equals("date")) {
            convertedValue.put("type", "date");
            fieldDescriptor.type = "date";
          }else {
            convertedValue.put("type", "text");
            fieldDescriptor.type =  "text";
          }
        }
        if (v.get("type").asText().equals("array")) {
          /*
           * "modificationDate" : { "type" : "array", "description" :
           * "Date/time of the last version of the FSK file", "items" : { "type" : "string",
           * "format" : "date" }, "title" : "Modification date" }
           * 
           * OR
           * 
           * "modelHypothesis" : { "type" : "array", "description" :
           * "Description of the hypothesis of the model", "items" : { "type" : "string" }, "title"
           * : "Hypothesis of the model" }
           */
          JsonNode items = v.get("items");
          if (items.get("type").asText().equals("string") && items.get("format") != null
              && items.get("format").asText().equals("string")) {
            convertedValue.put("type", "date-array");
            fieldDescriptor.type = "date-array";
          } else {
            convertedValue.put("type", "text-array");
            fieldDescriptor.type = "text-array";
          }
        }
      } else if (fieldName.equals("title")) {
        convertedValue.put("label", v.get(fieldName).asText());
        fieldDescriptor.label = v.get(fieldName).asText();
      } else if (fieldName.equals("externalEnum")) {
        convertedValue.put("vocabulary", v.get(fieldName).asText());
        fieldDescriptor.vocabulary = v.get(fieldName).asText();
      } else if (fieldName.equals("description")) {
        convertedValue.put("description", v.get(fieldName).asText());
        fieldDescriptor.description = v.get(fieldName).asText();
      }
    });
    convertedArraytValue.add(convertedValue);
  }
  public static void processPropertyNode(String propertyName, JsonNode value, List<String> requiredList, ObjectDescriptor objectDescriptor, String parentName) {
    FieldDescriptor fieldDescriptor = new FieldDescriptor();
    JsonNode v = value.get(propertyName);
    fieldDescriptor.id = propertyName;
    fieldDescriptor.ownerClass = parentName;
    if(requiredList.contains(propertyName)) {
      fieldDescriptor.required = true;
      objectDescriptor.requiredFieldNum++;
    }
    v.fieldNames().forEachRemaining((String fieldName) -> {
      
      if (fieldName.equals("type")) {
        if (v.get("type").asText().equals("string")) {
          JsonNode format = v.get("format");
          if (format != null && format.asText().equals("long-text")) {
            fieldDescriptor.type = "long-text";
          }else if (format != null && format.asText().equals("date")) {
            fieldDescriptor.type = "date";
          }else {
            fieldDescriptor.type =  "text";
          }
        }
        if (v.get("type").asText().equals("array")) {
          /*
           * "modificationDate" : { "type" : "array", "description" :
           * "Date/time of the last version of the FSK file", "items" : { "type" : "string",
           * "format" : "date" }, "title" : "Modification date" }
           * 
           * OR
           * 
           * "modelHypothesis" : { "type" : "array", "description" :
           * "Description of the hypothesis of the model", "items" : { "type" : "string" }, "title"
           * : "Hypothesis of the model" }
           */
          JsonNode items = v.get("items");
          if (items.get("type").asText().equals("string") && items.get("format") != null
              && items.get("format").asText().equals("string")) {
            fieldDescriptor.type = "date-array";
          } else {
            fieldDescriptor.type = "text-array";
          }
        }
      } else if (fieldName.equals("title")) {
        fieldDescriptor.label = v.get(fieldName).asText();
      } else if (fieldName.equals("externalEnum")) {
        fieldDescriptor.vocabulary = v.get(fieldName).asText();
      } else if (fieldName.equals("description")) {
        fieldDescriptor.description = v.get(fieldName).asText();
      }
    });
    objectDescriptor.fields.add(fieldDescriptor);
  }
  private static void extractReferences(JsonNode rootNode) {
    JsonNode defsNode = rootNode.get("$defs");
    defsNode.fieldNames().forEachRemaining((String fieldName) -> {
      JsonNode childNode = defsNode.get(fieldName);
      try {
        String representation = new ObjectMapper().writeValueAsString(childNode);
        if (representation != null && !representation.contains("\"#/$defs/"))
          primaryRefrences.put(fieldName, childNode);
        else
          complexRefrences.put(fieldName, childNode);
      } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }


    });
    /*System.out.println("++++++++++++++++++++++ primaryRefrences ++++++++++++++++++++");

    primaryRefrences.forEach((k, v) -> {
      System.out.println((k + ":" + v.toPrettyString()));
    });
    System.out.println("++++++++++++++++++++++ complexRefrences ++++++++++++++++++++");

    complexRefrences.forEach((k, v) -> {
      System.out.println((k + ":" + v.toPrettyString()));
    });*/
  }
  
  private static boolean hasComplexfield(JsonNode rootNode) {
    return rootNode.toString().contains("$defs");
  }
  private static boolean hasPolymorfismSign(JsonNode rootNode) {
    return rootNode.toString().contains("allOf");
  }

  private static void traverse(JsonNode node, int level) {
    if (node.getNodeType() == JsonNodeType.ARRAY) {
      traverseArray(node, level);
    } else if (node.getNodeType() == JsonNodeType.OBJECT) {
      traverseObject(node, level);
    } else {
      throw new RuntimeException("Not yet implemented");
    }
  }

  private static void traverseObject(JsonNode node, int level) {
    node.fieldNames().forEachRemaining((String fieldName) -> {
      JsonNode childNode = node.get(fieldName);
      // printNode(childNode, fieldName, level);
      // for nested object or arrays
      if (traversable(childNode)) {
        traverse(childNode, level + 1);
      }
    });
  }

  private static void traverseArray(JsonNode node, int level) {
    for (JsonNode jsonArrayNode : node) {
      // printNode(jsonArrayNode, "arrayElement", level);
      if (traversable(jsonArrayNode)) {
        traverse(jsonArrayNode, level + 1);
      }
    }
  }

  private static boolean traversable(JsonNode node) {
    return node.getNodeType() == JsonNodeType.OBJECT || node.getNodeType() == JsonNodeType.ARRAY;
  }

  private static void printNode(JsonNode node, String keyName, int level) {
    if (traversable(node)) {
      System.out.printf("%" + (level * 4 - 3) + "s|-- %s=%s type=%s%n", "", keyName,
          node.toString(), node.getNodeType());

    } else {
      Object value = null;
      if (node.isTextual()) {
        value = node.textValue();
      } else if (node.isNumber()) {
        value = node.numberValue();
      } // todo add more types
      System.out.printf("%" + (level * 4 - 3) + "s|-- %s=%s type=%s%n", "", keyName, value,
          node.getNodeType());
    }
  }
}
