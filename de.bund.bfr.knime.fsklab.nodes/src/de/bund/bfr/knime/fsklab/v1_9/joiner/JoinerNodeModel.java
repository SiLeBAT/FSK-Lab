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
import java.io.IOException;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeRemovedListener;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.editor.FSKEditorJSNodeDialog.ModelType;
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
  private FskPortObject thirdInputPort;
  private FskPortObject fourthInputPort;

  // public final static String SUFFIX = "_dup";
  public static final String SUFFIX_FIRST = "1";
  public static final String SUFFIX_SECOND = "2";
  public static final String SUFFIX_THIRD = "3";
  public static final String SUFFIX_FOURTH = "4";
  // public static final String SUFFIX = "_";


  Map<String, String> originals = new LinkedHashMap<String, String>();

  private static final ObjectMapper MAPPER = FskPlugin.getDefault().MAPPER104;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE_OPTIONAL,
      FskPortObject.TYPE_OPTIONAL, FskPortObject.TYPE_OPTIONAL, FskPortObject.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES =
      {CombinedFskPortObject.TYPE_OPTIONAL, ImagePortObject.TYPE_OPTIONAL};

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

      // Set third model parameters
      if (representation.getThirdModelParameters() == null && thirdInputPort != null) {
        List<Parameter> thirdModelParams = SwaggerUtil.getParameter(thirdInputPort.modelMetadata);
        if (thirdModelParams != null && !thirdModelParams.isEmpty()) {
          representation.setThirdModelParameters(
              thirdModelParams.toArray(new Parameter[thirdModelParams.size()]));
        }
      }

      // Set fourth model parameters
      if (representation.getFourthModelParameters() == null && fourthInputPort != null) {
        List<Parameter> fourthModelParams = SwaggerUtil.getParameter(fourthInputPort.modelMetadata);
        if (fourthModelParams != null && !fourthModelParams.isEmpty()) {
          representation.setFourthModelParameters(
              fourthModelParams.toArray(new Parameter[fourthModelParams.size()]));
        }
      }

      if (firstInputPort != null && representation.getFirstModelName() == null) {
        representation.setFirstModelName(SwaggerUtil.getModelName(firstInputPort.modelMetadata));
      }

      if (secondInputPort != null && representation.getSecondModelName() == null) {
        representation.setSecondModelName(SwaggerUtil.getModelName(secondInputPort.modelMetadata));
        // TODO To be changed to a generic type
        representation.setModelType(secondInputPort.modelMetadata.getModelType());
      }

      if (thirdInputPort != null && representation.getThirdModelName() == null) {
        representation.setThirdModelName(SwaggerUtil.getModelName(thirdInputPort.modelMetadata));
      }

      if (fourthInputPort != null && representation.getFourthModelName() == null) {
        representation.setFourthModelName(SwaggerUtil.getModelName(fourthInputPort.modelMetadata));
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

    SwaggerUtil.setParameter(secondInputPort.modelMetadata, JoinerNodeUtil.combineParameters(
        firstInputPort != null ? SwaggerUtil.getParameter(firstInputPort.modelMetadata) : null,
        secondInputPort != null ? SwaggerUtil.getParameter(secondInputPort.modelMetadata) : null,
        thirdInputPort != null ? SwaggerUtil.getParameter(thirdInputPort.modelMetadata) : null,
        fourthInputPort != null ? SwaggerUtil.getParameter(fourthInputPort.modelMetadata) : null));

    joinerProxyValue.modelMetaData = MAPPER.writeValueAsString(secondInputPort.modelMetadata);
  }

  @Override
  protected void performReset() {
    createEmptyViewValue();
    setViewRepresentation(null);

    nodeSettings.modelMetaData = null;

    nodeSettings.connections = null;
    firstInputPort = null;
    secondInputPort = null;
    thirdInputPort = null;
    fourthInputPort = null;
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
      File configFile = new File(settingFolder, "JoinRelations.json");
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

    if (flowVariables.containsKey("firstModelParameters.json")) {
      String parametersString = flowVariables.get("firstModelParameters.json").getStringValue();
      nodeSettings.firstModelParameters = MAPPER.readValue(parametersString, Parameter[].class);
    } else {
      File configFile = new File(settingFolder, "firstModelParameters.json");
      if (configFile.exists()) {
        nodeSettings.firstModelParameters = MAPPER.readValue(configFile, Parameter[].class);
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

    JoinerViewRepresentation representation = getViewRepresentation();
    if (nodeSettings.firstModelParameters != null) {
      representation.setFirstModelParameters(nodeSettings.firstModelParameters);
    }
    if (nodeSettings.secondModelParameters != null) {
      representation.setSecondModelParameters(nodeSettings.secondModelParameters);
    }
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
    return new PortObject[] {firstInputPort, secondInputPort, thirdInputPort, fourthInputPort};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    // remove null port objects to handle a case of first or second port in the node are not fed.
    try {
      fixNullPortsToDefault(portObjects);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (portObjects.length < 2) {
      setWarningMessage("This node created an empty model port object");
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
    JoinerNodeUtil.addIdentifierToParameters(
        firstInputPort != null ? SwaggerUtil.getParameter(firstInputPort.modelMetadata) : null,
        secondInputPort != null ? SwaggerUtil.getParameter(secondInputPort.modelMetadata) : null,
        thirdInputPort != null ? SwaggerUtil.getParameter(thirdInputPort.modelMetadata) : null,
        fourthInputPort != null ? SwaggerUtil.getParameter(fourthInputPort.modelMetadata) : null);

    synchronized (getLock()) {

      JoinerViewValue value = getViewValue();
      if (value.modelMetaData == null) {
        loadJsonSetting();

        if (value.modelMetaData == null && fixNullPortsToDefault(inObjects).length >= 2) {
          loadFromPorts(value);
        }

        exec.setProgress(1);
      }
    }
  }

  private PortObject[] fixNullPortsToDefault(PortObject[] inObjects) throws IOException {
    PortObject[] inPorts =
        Arrays.stream(inObjects).filter(s -> (s != null)).toArray(PortObject[]::new);
    int length = inPorts.length;
    switch (length) {
      case 1:
        firstInputPort = (FskPortObject) inPorts[0];
        secondInputPort = createEmptyFSKObject();
        thirdInputPort = createEmptyFSKObject();
        fourthInputPort = createEmptyFSKObject();
        break;
      case 2:
        firstInputPort = (FskPortObject) inPorts[0];
        secondInputPort = (FskPortObject) inPorts[1];
        thirdInputPort = createEmptyFSKObject();
        fourthInputPort = createEmptyFSKObject();
        break;
      case 3:
        firstInputPort = (FskPortObject) inPorts[0];
        secondInputPort = (FskPortObject) inPorts[1];
        thirdInputPort = (FskPortObject) inPorts[2];
        fourthInputPort = createEmptyFSKObject();
        break;
      case 4:
        firstInputPort = (FskPortObject) inPorts[0];
        secondInputPort = (FskPortObject) inPorts[1];
        thirdInputPort = (FskPortObject) inPorts[2];
        fourthInputPort = (FskPortObject) inPorts[3];
        break;
      default:
        firstInputPort = createEmptyFSKObject();
        secondInputPort = createEmptyFSKObject();
        thirdInputPort = createEmptyFSKObject();
        fourthInputPort = createEmptyFSKObject();
    }

    return inPorts;
  }


  private FskPortObject createEmptyFSKObject() throws IOException {

    FskPortObject fskPortObject = new FskPortObject(Optional.empty(), "", Collections.emptyList());
    fskPortObject.setModel("");
    fskPortObject.setViz("");
    fskPortObject.modelMetadata = NodeUtils.initializeModel(ModelType.genericModel);
    return fskPortObject;
  }

  @Override
  protected PortObject[] performExecuteCreatePortObjects(PortObject svgImageFromView,
      PortObject[] inObjects, ExecutionContext exec) throws Exception {
    // TODO complex joined object to be created, the third and the fourth then with the second then
    // with the first
    // this is related to a task in line 583 related to packages, and the other on line 597 related
    // to removing suffix.
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
          firstInputPort.simulations.size() > 0
              ? firstInputPort.simulations.get(firstInputPort.selectedSimulationIndex)
              : null,
          secondInputPort.simulations.size() > 0
              ? secondInputPort.simulations.get(secondInputPort.selectedSimulationIndex)
              : null,
          thirdInputPort.simulations.size() > 0
              ? thirdInputPort.simulations.get(thirdInputPort.selectedSimulationIndex)
              : null,
          fourthInputPort.simulations.size() > 0
              ? fourthInputPort.simulations.get(fourthInputPort.selectedSimulationIndex)
              : null,
          SwaggerUtil.getParameter(outObj.modelMetadata));



      // give the new combined model a name:
      // suggestion: model1.name + model2.name


      Set<String> packageSet = new HashSet<>();
      packageSet.addAll(firstInputPort.packages);
      packageSet.addAll(secondInputPort.packages);
      // TODO add packages to the corresponding joined Object
      outObj.packages.addAll(packageSet);

      JoinerNodeUtil.removeJoinedParameters(connections, outObj);


      // Create default simulation out of parameters metadata
      JoinerNodeUtil.createDefaultSimulation(outObj);


      // add all possible simulations to combined object
      JoinerNodeUtil.createAllPossibleSimulations(firstInputPort, secondInputPort, outObj);

      // remove suffix from original parameters since they are needed with their original id for the
      // scripts
      // TODO remove the suffix for complex joining for third and fourth model (recursion)
      if (value.joinRelations != null) {
        resetParameterIdToOriginal(
            SwaggerUtil.getParameter(outObj.getFirstFskPortObject().modelMetadata));
        resetParameterIdToOriginal(
            SwaggerUtil.getParameter(outObj.getSecondFskPortObject().modelMetadata));
      }

    }

    return new PortObject[] {outObj, svgImageFromView};
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
