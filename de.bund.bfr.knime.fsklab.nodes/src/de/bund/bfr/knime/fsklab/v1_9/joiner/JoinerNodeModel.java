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
package de.bund.bfr.knime.fsklab.v1_9.joiner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.knime.base.data.xml.SvgCell;
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
import org.knime.js.core.node.AbstractSVGWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeRemovedListener;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.OldJoinerRelation;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

/**
 * Fsk Joiner node model.
 */
public final class JoinerNodeModel
    extends AbstractSVGWizardNodeModel<JoinerViewRepresentation, JoinerViewValue>
    implements PortObjectHolder {

  private final JoinerNodeSettings nodeSettings = new JoinerNodeSettings();

  private FskPortObject firstInputPort;
  private FskPortObject secondInputPort;

  // public final static String SUFFIX = "_dup";
  public final static String SUFFIX_FIRST = "1";
  public final static String SUFFIX_SECOND = "2";
  // public final static String SUFFIX = "_";


  Map<String, String> originals = new LinkedHashMap<String, String>();

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
    return "de.bund.bfr.knime.fsklab.v1.9.joiner.component";
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
  public JoinerViewRepresentation getViewRepresentation() {

    JoinerViewRepresentation representation;

    synchronized (getLock()) {
      representation = super.getViewRepresentation();
      if (representation == null) {
        representation = createEmptyViewRepresentation();
      }

      // Set first model parameters
      if (representation.getFirstModelParameters() == null && firstInputPort != null) {
        List<Parameter> firstModelParams = SwaggerUtil.getParameter(firstInputPort.modelMetadata);
        if (firstModelParams != null && !firstModelParams.isEmpty()) {
          representation.setFirstModelParameters(
              firstModelParams.toArray(new Parameter[firstModelParams.size()]));
        }
      }

      // Set second model parameters
      if (representation.getSecondModelParameters() == null && secondInputPort != null) {
        List<Parameter> secondModelParams = SwaggerUtil.getParameter(secondInputPort.modelMetadata);
        if (secondModelParams != null && !secondModelParams.isEmpty()) {
          representation.setSecondModelParameters(
              secondModelParams.toArray(new Parameter[secondModelParams.size()]));
        }
      }

      if (firstInputPort != null) {

        if (representation.getFirstModelName() == null) {
          representation.setFirstModelName(SwaggerUtil.getModelName(firstInputPort.modelMetadata));
        }

        if (representation.getFirstModelScript() == null) {
          representation.setFirstModelScript(firstInputPort.getModel());
        }

        if (representation.getFirstModelViz() == null) {
          representation.setFirstModelViz(firstInputPort.getViz());
        }
      }

      if (secondInputPort != null) {

        if (representation.getSecondModelName() == null) {
          representation
              .setSecondModelName(SwaggerUtil.getModelName(secondInputPort.modelMetadata));
        }

        if (representation.getSecondModelScript() == null) {
          representation.setSecondModelScript(secondInputPort.getModel());
        }

        if (representation.getSecondModelViz() == null) {
          if (secondInputPort instanceof CombinedFskPortObject) {
            representation.setSecondModelViz(extractSecondObjectVis(secondInputPort));
          } else {
            representation.setSecondModelViz(secondInputPort.getViz());
          }
        }

        representation.setModelType(secondInputPort.modelMetadata.getModelType());
      }
    }

    return representation;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    ImagePortObjectSpec imageSpec = new ImagePortObjectSpec(SvgCell.TYPE);
    return new PortObjectSpec[] {CombinedFskPortObjectSpec.INSTANCE, imageSpec};
  }

  private void loadFromPorts(JoinerViewValue joinerProxyValue) throws JsonProcessingException {

    SwaggerUtil.setParameter(secondInputPort.modelMetadata,
        JoinerNodeUtil.combineParameters(SwaggerUtil.getParameter(firstInputPort.modelMetadata),
            SwaggerUtil.getParameter(secondInputPort.modelMetadata)));

    joinerProxyValue.modelMetaData = MAPPER.writeValueAsString(secondInputPort.modelMetadata);
  }

  // second visualization script is the script which draw and control the plotting!
  private String extractSecondObjectVis(FskPortObject object) {
    if (!(object instanceof CombinedFskPortObject)) {
      return object.getViz();
    } else {
      return extractSecondObjectVis(((CombinedFskPortObject) object).getSecondFskPortObject());
    }
  }

  private void setScriptBack(FskPortObject fskObject1, FskPortObject fskObject2,
      JsonArray scriptTree) {

    JsonObject obj1 = scriptTree.getJsonObject(0);
    if (obj1.containsKey("script")) {
      fskObject1.setModel(obj1.getString("script"));
    } else {
      CombinedFskPortObject firstCombinedModel = (CombinedFskPortObject) fskObject1;
      setScriptBack(firstCombinedModel.getFirstFskPortObject(),
          firstCombinedModel.getSecondFskPortObject(), obj1.getJsonArray("nodes"));
    }

    JsonObject obj2 = scriptTree.getJsonObject(2);
    if (obj2.containsKey("script")) {
      fskObject2.setModel(obj2.getString("script"));
    } else {
      CombinedFskPortObject secondCombinedModel = (CombinedFskPortObject) fskObject2;
      setScriptBack(secondCombinedModel.getFirstFskPortObject(),
          secondCombinedModel.getSecondFskPortObject(), obj2.getJsonArray("nodes"));
    }
  }

  private String buildModelscriptAsTree() {

    JsonArrayBuilder array = Json.createArrayBuilder();
    array.add(getModelScriptNode(firstInputPort).build());

    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    jsonObjectBuilder.add("id", "" + generateRandomUnifier());
    jsonObjectBuilder.add("text", "Joining Script");

    StringBuilder joinModel = new StringBuilder();
    jsonObjectBuilder.add("script", joinModel.toString());
    array.add(jsonObjectBuilder.build());
    array.add(getModelScriptNode(secondInputPort).build());

    return array.build().toString();
  }

  private static JsonArray getScriptArray(String input) {
    try (JsonReader jsonReader = Json.createReader(new StringReader(input))) {
      return jsonReader.readArray();
    }
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
      jsonObjectBuilder.add("script", object.getModel());
    }
    return jsonObjectBuilder;
  }

  private static String generateRandomUnifier() {
    return new AtomicLong((int) (100000 * Math.random())).toString();
  }

  @Override
  protected void performReset() {
    createEmptyViewValue();
    setViewRepresentation(null);

    nodeSettings.modelMetaData = null;

    nodeSettings.connections = null;
    firstInputPort = null;
    secondInputPort = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {}

  protected void loadJsonSetting() throws IOException, CanceledExecutionException, ParseException {

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
      try {
        nodeSettings.connections = MAPPER.readValue(connectionString, JoinRelation[].class);
      }catch(Exception ex) {
        OldJoinerRelation[] oldRelations= MAPPER.readValue(connectionString, OldJoinerRelation[].class);
        Function<OldJoinerRelation, JoinRelation> funcMigrateJoinerRelation= (OldJoinerRelation e)-> {return e.getNewJoinRelation();};
        nodeSettings.connections  = Arrays.asList(oldRelations).stream().map(funcMigrateJoinerRelation
        ).collect(Collectors.toList()).toArray(new JoinRelation[oldRelations.length]);
      }
    } else {
      File configFile = new File(settingFolder, "JoinRelations.json");
      if (configFile.exists()) {
        try {
          nodeSettings.connections = MAPPER.readValue(configFile, JoinRelation[].class);
        } catch (Exception ex) {
          OldJoinerRelation[] oldRelations =
              MAPPER.readValue(configFile, OldJoinerRelation[].class);
          nodeSettings.connections =
              Arrays.asList(oldRelations).stream().map((OldJoinerRelation e) -> {
                return e.getNewJoinRelation();
              }).collect(Collectors.toList()).toArray(new JoinRelation[oldRelations.length]);
        }
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
    List <Parameter> firstParams = new ArrayList<Parameter>();
    if (flowVariables.containsKey("firstModelParameters.json")) {
      String parametersString = flowVariables.get("firstModelParameters.json").getStringValue();
      nodeSettings.firstModelParameters = MAPPER.readValue(parametersString, Parameter[].class);
    } else {
      File configFile = new File(settingFolder, "firstModelParameters.json");
      if (configFile.exists()) {
        nodeSettings.firstModelParameters = MAPPER.readValue(configFile, Parameter[].class);
      }
      configFile = new File(settingFolder, "modelMath1.json");
      if (configFile.exists()) {
        firstParams = getModelParametersfromOldFile(configFile);
        nodeSettings.firstModelParameters = firstParams.toArray(new Parameter[firstParams.size()]);
      }
    }

    if (flowVariables.containsKey("secondModelParameters.json")) {
      String parametersString = flowVariables.get("secondModelParameters.json").getStringValue();
      nodeSettings.secondModelParameters = MAPPER.readValue(parametersString, Parameter[].class);
    } else {
      File configFile = new File(settingFolder, "secondModelParameters.json");
      if (configFile.exists()) {
        nodeSettings.secondModelParameters = MAPPER.readValue(configFile, Parameter[].class);
      }
      configFile = new File(settingFolder, "modelMath2.json");
      if (configFile.exists()) {
        List <Parameter> secondParams = getModelParametersfromOldFile(configFile);
        secondParams.removeAll(firstParams);
        nodeSettings.secondModelParameters = secondParams.toArray(new Parameter[secondParams.size()]);
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
    viewValue.modelScriptTree = sourceTree;
    JoinerViewRepresentation representation = getViewRepresentation();
    if (nodeSettings.firstModelParameters != null) {
      representation.setFirstModelParameters(nodeSettings.firstModelParameters);
    }
    if (nodeSettings.secondModelParameters != null) {
      representation.setSecondModelParameters(nodeSettings.secondModelParameters);
    }
    if (firstInputPort != null) {
      List<Parameter> newFirstModelParameters =
          SwaggerUtil.getParameter(firstInputPort.modelMetadata);
      List<Parameter> newSecondModelParameters =
          SwaggerUtil.getParameter(secondInputPort.modelMetadata);
      if (!JoinerNodeUtil.parametersNeedUpdate(newFirstModelParameters,
          JoinerNodeModel.SUFFIX_FIRST, JoinerNodeModel.SUFFIX_SECOND)) {
        representation.updateParameters(newFirstModelParameters, newSecondModelParameters);
        viewValue.updateParameters(newFirstModelParameters, newSecondModelParameters);
      } else {
        viewValue.modelMetaData = nodeSettings.modelMetaData;
      }
    }

    representation.setSecondModelViz(visualizationScript);

  }

  /*
   * A migration method, takes an old setting file and extract the data inside as list of
   * de.bund.bfr.metadata.swagger.parameter
   */
  private static List<Parameter> getModelParametersfromOldFile(File configFile)
      throws FileNotFoundException, IOException, ParseException {
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(configFile));
    JSONObject jsonObject = (JSONObject) obj;
    JSONArray parameterList = (JSONArray) jsonObject.get("parameter");
    return MAPPER.readValue(parameterList.toJSONString(), new TypeReference<List<Parameter>>() {});
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
    JoinerViewRepresentation representation = getViewRepresentation();

    if (ArrayUtils.isNotEmpty(viewValue.joinRelations)) {
      File configFile = new File(settingsFolder, "JoinRelations.json");
      try {
        MAPPER.writeValue(configFile, viewValue.joinRelations);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (StringUtils.isNotEmpty(viewValue.modelMetaData)) {
      File configFile = new File(settingsFolder, "modelMetaData.json");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.modelMetaData, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (ArrayUtils.isNotEmpty(representation.getFirstModelParameters())) {
      File configFile = new File(settingsFolder, "firstModelParameters.json");
      try {
        MAPPER.writeValue(configFile, representation.getFirstModelParameters());
      } catch (IOException e) {
        // do nothing
      }
    }

    if (ArrayUtils.isNotEmpty(representation.getSecondModelParameters())) {
      File configFile = new File(settingsFolder, "secondModelParameters.json");
      try {
        MAPPER.writeValue(configFile, representation.getSecondModelParameters());
      } catch (IOException e) {
        // do nothing
      }
    }

    if (StringUtils.isNotEmpty(viewValue.modelScriptTree)) {
      File configFile = new File(settingsFolder, "sourceTree.json");
      try {
        FileUtils.writeStringToFile(configFile, viewValue.modelScriptTree, StandardCharsets.UTF_8);
      } catch (IOException e) {
        // do nothing
      }
    }

    if (StringUtils.isNotEmpty(viewValue.getVisualizationScript())) {
      File configFile = new File(settingsFolder, "visualization.txt");
      try {

        if (viewValue.getVisualizationScript() != "")
          representation.setSecondModelViz(viewValue.getVisualizationScript());

        FileUtils.writeStringToFile(configFile, representation.getSecondModelViz(),
            StandardCharsets.UTF_8);
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
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {firstInputPort, secondInputPort};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    if (portObjects != null && portObjects.length == 2) {
      firstInputPort = (FskPortObject) portObjects[0];
      secondInputPort = (FskPortObject) portObjects[1];
    }
  }

  public void setHideInWizard(boolean hide) {}


  /** @return string with node name and id with format "{name} (#{id}) setting". */
  private static String buildContainerName() {
    final NodeContainer nodeContainer = NodeContext.getContext().getNodeContainer();
    return nodeContainer.getName() + " (#" + nodeContainer.getID().getIndex() + ") setting";
  }

  @Override
  protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {

    final String nodeWithId = NodeContext.getContext().getNodeContainer().getNameWithID();
    NodeContext.getContext().getWorkflowManager()
        .addListener(new NodeRemovedListener(nodeWithId, buildContainerName()));

    setInternalPortObjects(inObjects);
    JoinerNodeUtil.addIdentifierToParameters(SwaggerUtil.getParameter(firstInputPort.modelMetadata),
        SwaggerUtil.getParameter(secondInputPort.modelMetadata));

    synchronized (getLock()) {

      JoinerViewValue value = getViewValue();
      if (value.modelMetaData == null) {
        value.modelScriptTree = buildModelscriptAsTree();
        loadJsonSetting();

        if (value.modelMetaData == null) {
          loadFromPorts(value);
        }

        exec.setProgress(1);
      }
    }
  }



  @Override
  protected PortObject[] performExecuteCreatePortObjects(PortObject svgImageFromView,
      PortObject[] inObjects, ExecutionContext exec) throws Exception {

    CombinedFskPortObject outObj = new CombinedFskPortObject(Optional.empty(), new ArrayList<>(),
        firstInputPort, secondInputPort);

    JoinRelation[] connections = new JoinRelation[0];

    synchronized (getLock()) {

      JoinerViewValue value = getViewValue();

      if (value.joinRelations != null) {
        connections = value.joinRelations;
      } else if (nodeSettings.connections != null) {
        connections = nodeSettings.connections;
      }
      outObj.setJoinerRelation(connections);

      // Give CombinedModel some metadata (for now: from second portObject)
      // Consider Here that the model type is the same as the second model
      if (StringUtils.isNotEmpty(value.modelMetaData)) {
        outObj.modelMetadata = MAPPER.readValue(value.modelMetaData,
            SwaggerUtil.modelClasses.get(secondInputPort.modelMetadata.getModelType()));

      } else {
        outObj.modelMetadata = secondInputPort.modelMetadata;
      }

      // change default values for CombinedModel to those of the currently selected simulations
      // (model1 & model2)
      JoinerNodeUtil.createDefaultParameterValues(
          firstInputPort.simulations.get(firstInputPort.selectedSimulationIndex),
          secondInputPort.simulations.get(secondInputPort.selectedSimulationIndex),
          SwaggerUtil.getParameter(outObj.modelMetadata));



      // give the new combined model a name:
      // suggestion: model1.name + model2.name

      if (StringUtils.isNotEmpty(value.modelScriptTree)) {
        JsonArray scriptTree = getScriptArray(value.modelScriptTree);
        setScriptBack(firstInputPort, secondInputPort, scriptTree);
      } else {
        value.modelScriptTree = buildModelscriptAsTree();
      }

      Set<String> packageSet = new HashSet<>();
      packageSet.addAll(firstInputPort.packages);
      packageSet.addAll(secondInputPort.packages);
      outObj.packages.addAll(packageSet);

      JoinerNodeUtil.removeJoinedParameters(connections, outObj);


      // Create default simulation out of parameters metadata
      JoinerNodeUtil.createDefaultSimulation(outObj);


      // add all possible simulations to combined object
      JoinerNodeUtil.createAllPossibleSimulations(firstInputPort, secondInputPort, outObj);

      // update second visualization script from the view
      if (value.getVisualizationScript() == "")
        setLastVisualizationScript(outObj, this.getViewRepresentation().getSecondModelViz());
      // outObj.getSecondFskPortObject().viz = this.getViewRepresentation().getSecondModelViz();
      else
        setLastVisualizationScript(outObj, value.getVisualizationScript());
      // outObj.getSecondFskPortObject().viz = value.getVisualizationScript();

      // remove suffix from original parameters since they are needed with their original id for the
      // scripts
      if (value.joinRelations != null) {
        resetParameterIdToOriginal(
            SwaggerUtil.getParameter(outObj.getFirstFskPortObject().modelMetadata));
        resetParameterIdToOriginal(
            SwaggerUtil.getParameter(outObj.getSecondFskPortObject().modelMetadata));
      }

    }

    return new PortObject[] {outObj, svgImageFromView};
  }


  private void setLastVisualizationScript(FskPortObject fskObj, String viz) {
    if (fskObj instanceof CombinedFskPortObject)
      setLastVisualizationScript(((CombinedFskPortObject) fskObj).getSecondFskPortObject(), viz);
    else
      fskObj.setViz(viz);

  }


  private void resetParameterIdToOriginal(List<Parameter> parameter) {

    for (Parameter p : parameter) {
      p.setId(p.getId().substring(0, p.getId().length() - 1));
    }

  }

  @Override
  protected boolean generateImage() {
    return true;
  }
}
