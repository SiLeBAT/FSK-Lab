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
package de.bund.bfr.knime.fsklab.nodes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgImageContent;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.node.web.ValidationError;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.util.FileUtil;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

/**
 * Fsk Joiner node model.
 */
final class JoinerNodeModel extends
    AbstractWizardNodeModel<JoinerViewRepresentation, JoinerViewValue> implements PortObjectHolder {

  private final JoinerNodeSettings nodeSettings = new JoinerNodeSettings();

  private FskPortObject m_port;

  public final static String SUFFIX = "_dup";

  private final static ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE, FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {CombinedFskPortObject.TYPE, ImagePortObject.TYPE};

  private static final String VIEW_NAME = new JoinerNodeFactory().getInteractiveViewName();

  public JoinerNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public JoinerViewRepresentation createEmptyViewRepresentation() {
    return new JoinerViewRepresentation();
  }

  @Override
  public JoinerViewValue createEmptyViewValue() {
    return new JoinerViewValue();
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.js.joiner";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(JoinerViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public JoinerViewValue getViewValue() {
    JoinerViewValue val;
    synchronized (getLock()) {
      val = super.getViewValue();
      if (val == null) {
        val = createEmptyViewValue();
      }

    }
    return val;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    ImagePortObjectSpec imageSpec = new ImagePortObjectSpec(SvgCell.TYPE);
    return new PortObjectSpec[] {CombinedFskPortObjectSpec.INSTANCE, imageSpec};
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {

    final String nodeWithId = NodeContext.getContext().getNodeContainer().getNameWithID();
    NodeContext.getContext().getWorkflowManager()
        .addListener(new NodeRemovedListener(nodeWithId, buildContainerName()));

    FskPortObject inObj1 = (FskPortObject) inObjects[0];
    FskPortObject inObj2 = (FskPortObject) inObjects[1];

    resolveParameterNamesConflict(inObj1, inObj2);

    CombinedFskPortObject outObj = new CombinedFskPortObject(
        FileUtil.createTempDir("combined").getAbsolutePath(), new ArrayList<>(), inObj1, inObj2);

    ImagePortObject imagePort = null;
    JoinRelation[] connections = new JoinRelation[0];

    // Clone input object
    synchronized (getLock()) {
      JoinerViewValue joinerProxyValue = getViewValue();

      // If not executed
      if (joinerProxyValue.modelMetaData == null) {
        joinerProxyValue.modelScriptTree = buildModelscriptAsTree(inObj1, inObj2);

        joinerProxyValue.firstModelName = SwaggerUtil.getModelName(inObj1.modelMetadata);
        joinerProxyValue.secondModelName = SwaggerUtil.getModelName(inObj2.modelMetadata);
        loadJsonSetting();
        if (joinerProxyValue.modelMetaData == null) {
          loadFromPorts(inObj1, inObj2, joinerProxyValue);
        }

        joinerProxyValue.firstModelScript = inObj1.model;
        joinerProxyValue.firstModelViz = inObj1.viz;
        joinerProxyValue.secondModelScript = inObj2.model;

        if (!StringUtils.isNotBlank(joinerProxyValue.secondModelViz)) {
          if (!(inObj2 instanceof CombinedFskPortObject)) {
            joinerProxyValue.secondModelViz = inObj2.viz;

          } else {
            /*
             * extract the visualization script of the second model which may be also an joined
             * object!
             */
            joinerProxyValue.secondModelViz = extractSecondObjectVis(inObj2);
          }
        }

        exec.setProgress(1);
      }

      if (joinerProxyValue.joinRelations != null) {
        connections = joinerProxyValue.joinRelations;
      } else if (nodeSettings.connections != null) {
        connections = nodeSettings.connections;
      }

      // Consider Here that the model type is the same as the second model
      outObj.modelMetadata = getObjectFromJson(joinerProxyValue.modelMetaData,
          SwaggerUtil.modelClasses.get(inObj2.modelMetadata.getModelType()));
      joinerProxyValue.modelType = inObj2.modelMetadata.getModelType();

      if (StringUtils.isNotEmpty(joinerProxyValue.modelScriptTree)) {
        JsonArray scriptTree = getScriptArray(joinerProxyValue.modelScriptTree);
        setScriptBack(inObj1, inObj2, scriptTree);
      } else {
        joinerProxyValue.modelScriptTree = buildModelscriptAsTree(inObj1, inObj2);
      }

      inObj2.viz = joinerProxyValue.secondModelViz;

      Set<String> packageSet = new HashSet<>();
      packageSet.addAll(inObj1.packages);
      packageSet.addAll(inObj2.packages);
      outObj.packages.addAll(packageSet);
      resolveParameters(connections, outObj);

      outObj.setJoinerRelation(connections);

      // Create default simulation out of parameters metadata
      if (SwaggerUtil.getModelMath(outObj.modelMetadata) != null) {
        List<Parameter> params = SwaggerUtil.getParameter(outObj.modelMetadata);
        FskSimulation defaultSimulation = NodeUtils.createDefaultSimulation(params);
        outObj.simulations.add(defaultSimulation);
        outObj.selectedSimulationIndex = 0;
      }

      imagePort = createSVGImagePortObject(joinerProxyValue.svgRepresentation);
    }

    return new PortObject[] {outObj, imagePort};
  }

  private void loadFromPorts(FskPortObject inObj1, FskPortObject inObj2,
      JoinerViewValue joinerProxyValue) throws JsonProcessingException {

    SwaggerUtil.setParameter(inObj2.modelMetadata,
        combineParameters(SwaggerUtil.getParameter(inObj1.modelMetadata),
            SwaggerUtil.getParameter(inObj2.modelMetadata)));
    joinerProxyValue.modelMetaData = FromOjectToJSON(inObj2.modelMetadata);
    joinerProxyValue.modelMath1 = FromOjectToJSON(SwaggerUtil.getModelMath(inObj1.modelMetadata));
    joinerProxyValue.modelMath2 = FromOjectToJSON(SwaggerUtil.getModelMath(inObj2.modelMetadata));
  }

  // second visualization script is the script which draw and control the plotting!
  private String extractSecondObjectVis(FskPortObject object) {
    if (!(object instanceof CombinedFskPortObject)) {
      return object.viz;
    } else {
      return extractSecondObjectVis(((CombinedFskPortObject) object).getSecondFskPortObject());
    }
  }

  private void setScriptBack(FskPortObject fskObject1, FskPortObject fskObject2,
      JsonArray scriptTree) {
    JsonObject obj1 = scriptTree.getJsonObject(0);
    if (obj1.containsKey("script")) {
      fskObject1.model = obj1.getString("script");
    } else {
      setScriptBack(((CombinedFskPortObject) fskObject1).getFirstFskPortObject(),
          ((CombinedFskPortObject) fskObject1).getSecondFskPortObject(),
          obj1.getJsonArray("nodes"));
    }
    JsonObject obj2 = scriptTree.getJsonObject(2);
    if (obj2.containsKey("script")) {
      fskObject2.model = obj2.getString("script");
    } else {
      setScriptBack(((CombinedFskPortObject) fskObject2).getFirstFskPortObject(),
          ((CombinedFskPortObject) fskObject2).getSecondFskPortObject(),
          obj2.getJsonArray("nodes"));
    }
  }

  private String buildModelscriptAsTree(FskPortObject inObj1, FskPortObject inObj2) {
    JsonArrayBuilder array = Json.createArrayBuilder();
    array.add(getModelScriptNode(inObj1).build());
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    jsonObjectBuilder.add("id", "" + generateRandomUnifier());
    jsonObjectBuilder.add("text", "Joining Script");
    StringBuilder joinModel = new StringBuilder();
    jsonObjectBuilder.add("script", joinModel.toString());
    array.add(jsonObjectBuilder.build());
    array.add(getModelScriptNode(inObj2).build());
    return array.build().toString();
  }

  private JsonArray getScriptArray(String input) {
    JsonReader jsonReader = Json.createReader(new StringReader(input));
    JsonArray array = jsonReader.readArray();
    jsonReader.close();
    return array;
  }

  private JsonObjectBuilder getModelScriptNode(FskPortObject object) {
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    jsonObjectBuilder.add("id", "" + generateRandomUnifier());
    if (object instanceof CombinedFskPortObject) {
      jsonObjectBuilder.add("text", "Joining Script");

      StringBuilder joinModel = new StringBuilder();
      if (((CombinedFskPortObject) object).getJoinerRelation() != null) {
        Arrays.stream(((CombinedFskPortObject) object).getJoinerRelation()).forEach(connection -> {
          joinModel.append(connection.getTargetParam() + " <- " + connection.getCommand() + ";\n");
        });
      }
      jsonObjectBuilder.add("script", joinModel.toString());
      FskPortObject first = ((CombinedFskPortObject) object).getFirstFskPortObject();
      FskPortObject second = ((CombinedFskPortObject) object).getSecondFskPortObject();
      JsonArrayBuilder array = Json.createArrayBuilder();
      array.add(getModelScriptNode(first));
      array.add(jsonObjectBuilder.build());
      array.add(getModelScriptNode(second));
      jsonObjectBuilder.add("nodes", array);
    } else {
      jsonObjectBuilder.add("text", SwaggerUtil.getModelName(object.modelMetadata));
      jsonObjectBuilder.add("script", object.model);
    }
    return jsonObjectBuilder;
  }

  private String generateRandomUnifier() {
    return new AtomicLong((int) (100000 * Math.random())).toString();
  }

  private ImagePortObject createSVGImagePortObject(String svgString) {

    ImagePortObject imagePort = null;
    if (svgString == null || svgString.equals("")) {
      svgString = "<svg xmlns=\"http://www.w3.org/2000/svg\"/>";
    }
    String xmlPrimer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    String svgPrimer = xmlPrimer + "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" "
        + "\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">";
    String xmlString = null;
    xmlString = svgPrimer + svgString;
    try {
      InputStream is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
      ImagePortObjectSpec imageSpec = new ImagePortObjectSpec(SvgCell.TYPE);

      imagePort = new ImagePortObject(new SvgImageContent(is), imageSpec);
    } catch (IOException e) {
      // LOGGER.error("Creating SVG port object failed: " + e.getMessage(), e);
    }

    return imagePort;

  }

  private static String FromOjectToJSON(final Object Object) throws JsonProcessingException {


    ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
    String jsonStr = objectMapper.writeValueAsString(Object);
    return jsonStr;

  }

  private static <T> T getObjectFromJson(String jsonStr, Class<T> valueType)
      throws InvalidSettingsException, JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    Object object = mapper.readValue(jsonStr, valueType);

    return valueType.cast(object);
  }

  @Override
  protected void performReset() {
    createEmptyViewValue();
    nodeSettings.modelMetaData = "";

    nodeSettings.connections = null;
    m_port = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {}


  protected void loadJsonSetting() throws IOException, CanceledExecutionException {

    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    File settingFolder = new File(directory, buildContainerName());

    // Get flow variables
    Map<String, FlowVariable> flowVariables;
    if (NodeContext.getContext().getNodeContainer().getFlowObjectStack() != null) {
      flowVariables = NodeContext.getContext().getNodeContainer().getFlowObjectStack()
          .getAvailableFlowVariables();
    } else {
      flowVariables = Collections.emptyMap();
    }

    if (flowVariables.containsKey("JoinRelations.json")) {
      String connectionString = flowVariables.get("JoinRelations.json").getStringValue();
      nodeSettings.connections = MAPPER.readValue(connectionString, JoinRelation[].class);
    } else {
      File configFile = new File(settingFolder, "JoinRelation.json");
      if (configFile.exists()) {
        nodeSettings.connections = MAPPER.readValue(configFile, JoinRelation[].class);
      }
    }

    if (flowVariables.containsKey("modelMetaData.json")) {
      nodeSettings.modelMetaData = flowVariables.get("modelMetaData.json").getStringValue();
    } else {
      File configFile = new File(settingFolder, "modelMetaData.json");
      if (configFile.exists()) {
        nodeSettings.modelMetaData = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
      }
    }

    if (flowVariables.containsKey("modelMath1.json")) {
      nodeSettings.modelMath1 = flowVariables.get("modelMath1.json").getStringValue();
    } else {
      File configFile = new File(settingFolder, "modelMath1.json");
      if (configFile.exists()) {
        nodeSettings.modelMath1 = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
      }
    }

    if (flowVariables.containsKey("modelMath2.json")) {
      nodeSettings.modelMath2 = flowVariables.get("modelMath2.json").getStringValue();
    } else {
      File configFile = new File(settingFolder, "modelMath2.json");
      if (configFile.exists()) {
        nodeSettings.modelMath2 = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
      }
    }

    String sourceTree;
    if (flowVariables.containsKey("sourceTree.json")) {
      sourceTree = flowVariables.get("sourceTree.json").getStringValue();
    } else {
      File configFile = new File(settingFolder, "sourceTree.json");
      if (configFile.exists()) {
        sourceTree = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
      } else {
        sourceTree = null;
      }
    }

    String visualizationScript;
    if (flowVariables.containsKey("visualization.txt")) {
      visualizationScript = flowVariables.get("visualization.txt").getStringValue();
    } else {
      File configFile = new File(settingFolder, "visualization.txt");
      if (configFile.exists()) {
        visualizationScript = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
      } else {
        visualizationScript = null;
      }
    }

    JoinerViewValue viewValue = getViewValue();
    viewValue.joinRelations = nodeSettings.connections;
    viewValue.modelMetaData = nodeSettings.modelMetaData;

    if (nodeSettings.modelMath1 != null)
      viewValue.modelMath1 = nodeSettings.modelMath1;
    if (nodeSettings.modelMath1 != null)
      viewValue.modelMath2 = nodeSettings.modelMath2;
    viewValue.modelScriptTree = sourceTree;
    viewValue.secondModelViz = visualizationScript;
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {

    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    File settingsFolder = new File(directory, buildContainerName());
    if (!settingsFolder.exists()) {
      settingsFolder.mkdir();
    }

    JoinerViewValue viewValue = getViewValue();

    if (viewValue.joinRelations != null && viewValue.joinRelations.length > 0) {
      File configFile = new File(settingsFolder, "JoinRelations.json");
      try {
        MAPPER.writeValue(configFile, viewValue.joinRelations);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (viewValue.modelMetaData != null && !viewValue.modelMetaData.isEmpty()) {
      File configFile = new File(settingsFolder, "modelMetaData.json");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.modelMetaData, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (viewValue.modelMath1 != null && !viewValue.modelMath1.isEmpty()) {
      File configFile = new File(settingsFolder, "modelMath1.json");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.modelMath1, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (viewValue.modelMath2 != null && !viewValue.modelMath2.isEmpty()) {
      File configFile = new File(settingsFolder, "modelMath2.json");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.modelMath2, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (viewValue.modelScriptTree != null && !viewValue.modelScriptTree.isEmpty()) {
      File configFile = new File(settingsFolder, "sourceTree.json");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.modelScriptTree, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (viewValue.secondModelViz != null && !viewValue.secondModelViz.isEmpty()) {
      File configFile = new File(settingsFolder, "visualization.txt");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.secondModelViz, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    try {
      loadJsonSetting();
    } catch (IOException | CanceledExecutionException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {m_port};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    m_port = (FskPortObject) portObjects[0];
  }

  public void setHideInWizard(boolean hide) {}

  private void resolveParameterNamesConflict(FskPortObject fskPort1, FskPortObject fskPort2) {
    for (Parameter firstParam : SwaggerUtil.getParameter(fskPort1.modelMetadata)) {
      for (Parameter secondParam : SwaggerUtil.getParameter(fskPort2.modelMetadata)) {
        if (secondParam.getId().equals(firstParam.getId())) {
          firstParam.setName(firstParam.getId() + SUFFIX);
          firstParam.setId(firstParam.getId() + SUFFIX);
        }
      }
    }
  }

  private void resolveParameters(JoinRelation[] relations, FskPortObject outfskPort) {

    if (relations != null)
      for (JoinRelation relation : relations) {

        Iterator<Parameter> iter = SwaggerUtil.getParameter(outfskPort.modelMetadata).iterator();
        while (iter.hasNext()) {
          Parameter p = iter.next();
          // remove output from first model
          // Boolean b1 = p.getParameterID().equals(relation.getSourceParam().getParameterID());

          // remove input from second model
          Boolean b2 = p.getId().equals(relation.getTargetParam());


          if (b2) {
            iter.remove();
          }
        } // while
      } // for
  }// resolveParameters

  // TODO: finalize joining meta data after 1.04
  private List<Parameter> combineParameters(List<Parameter> firstParameterList,
      List<Parameter> secondParameterList) {

    // parameters
    List<Parameter> combinedList = Stream.of(firstParameterList, secondParameterList)
        .flatMap(x -> x.stream()).collect(Collectors.toList());

    return combinedList;
  }

  /** @return string with node name and id with format "{name} (#{id}) setting". */
  private static String buildContainerName() {
    final NodeContainer nodeContainer = NodeContext.getContext().getNodeContainer();
    return nodeContainer.getName() + " (#" + nodeContainer.getID().getIndex() + ") setting";
  }
}
