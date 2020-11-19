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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeRemovedListener;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
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

  private final JoinerNodeSettings m_config = new JoinerNodeSettings();

  private FskPortObject firstInputPort;
  private FskPortObject secondInputPort;
  private FskPortObject thirdInputPort;
  private FskPortObject fourthInputPort;

  // public final static String SUFFIX = "_dup";
  public static final String SUFFIX_FIRST = "1";
  public static final String SUFFIX_SECOND = "2";
  // public static final String SUFFIX = "_";
  private Map<String, FskPortObject> fskID_to_fskObject = new HashMap<>();
  private Map<String, Map<String, String>> modelsParamsOriginalNames = new HashMap<>();

  Map<String, String> originals = new LinkedHashMap<String, String>();

  private int inputModelNumber;
  private CombinedFskPortObject outObj;
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
      } else if (val.isEmpty()) {
        copyConfigToView(val);
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

      JoinerModelsData joinerModelsData = representation.joinerModelsData;

      // Set first model parameters
      if (joinerModelsData.firstModelParameters == null && firstInputPort != null) {
        List<Parameter> firstModelParams = SwaggerUtil.getParameter(firstInputPort.modelMetadata);
        if (firstModelParams != null && !firstModelParams.isEmpty()) {
          joinerModelsData.firstModelParameters =
              firstModelParams.toArray(new Parameter[firstModelParams.size()]);
        }
      }

      // Set second model parameters
      if (joinerModelsData.secondModelParameters == null && secondInputPort != null) {
        List<Parameter> secondModelParams = SwaggerUtil.getParameter(secondInputPort.modelMetadata);
        if (secondModelParams != null && !secondModelParams.isEmpty()) {
          joinerModelsData.secondModelParameters =
              secondModelParams.toArray(new Parameter[secondModelParams.size()]);
        }
      }

      // Set third model parameters
      if (joinerModelsData.thirdModelParameters == null && thirdInputPort != null) {
        List<Parameter> thirdModelParams = SwaggerUtil.getParameter(thirdInputPort.modelMetadata);
        if (thirdModelParams != null && !thirdModelParams.isEmpty()) {
          joinerModelsData.thirdModelParameters =
              thirdModelParams.toArray(new Parameter[thirdModelParams.size()]);
        }
      }

      // Set fourth model parameters
      if (joinerModelsData.fourthModelParameters == null && fourthInputPort != null) {
        List<Parameter> fourthModelParams = SwaggerUtil.getParameter(fourthInputPort.modelMetadata);
        if (fourthModelParams != null && !fourthModelParams.isEmpty()) {
          joinerModelsData.fourthModelParameters =
              fourthModelParams.toArray(new Parameter[fourthModelParams.size()]);
        }
      }
      if (firstInputPort != null) {
        if (joinerModelsData.firstModelName == null) {
          joinerModelsData.firstModelName = SwaggerUtil.getModelName(firstInputPort.modelMetadata);
          joinerModelsData.firstModelType = firstInputPort.modelMetadata.getModelType();
        }
        if (joinerModelsData.firstModel == null) {
          joinerModelsData.firstModel = getFSKObjectAsStringArray(firstInputPort);
          joinerModelsData.firstModelType = "GenericModel";
        }
      }
      if (secondInputPort != null) {
        if (joinerModelsData.secondModelName == null) {
          joinerModelsData.secondModelName =
              SwaggerUtil.getModelName(secondInputPort.modelMetadata);
          // TODO To be changed to a generic type
          joinerModelsData.secondModelType = secondInputPort.modelMetadata.getModelType();
        }
        if (joinerModelsData.secondModel == null) {
          joinerModelsData.secondModel = getFSKObjectAsStringArray(secondInputPort);
          joinerModelsData.secondModelType = "GenericModel";
        }
      }

      if (thirdInputPort != null) {
        if (joinerModelsData.thirdModelName == null) {
          joinerModelsData.thirdModelName = SwaggerUtil.getModelName(thirdInputPort.modelMetadata);
          joinerModelsData.thirdModelType = thirdInputPort.modelMetadata.getModelType();
        }
        if (joinerModelsData.thirdModel == null) {
          joinerModelsData.thirdModel = getFSKObjectAsStringArray(thirdInputPort);
          joinerModelsData.thirdModelType = "GenericModel";
        }
      }
      if (fourthInputPort != null) {
        if (joinerModelsData.fourthModelName == null) {
          joinerModelsData.fourthModelName =
              SwaggerUtil.getModelName(fourthInputPort.modelMetadata);
          joinerModelsData.fourthModelType = fourthInputPort.modelMetadata.getModelType();
        }
        if (joinerModelsData.fourthModel == null) {
          joinerModelsData.fourthModel = getFSKObjectAsStringArray(fourthInputPort);
          joinerModelsData.fourthModelType = "GenericModel";
        }
      }
    }
    if(!modelsParamsOriginalNames.containsKey(null)) {
      representation.joinerModelsData.modelsParamsOriginalNames = this.modelsParamsOriginalNames;
    }
    return representation;
  }



  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    ImagePortObjectSpec imageSpec = new ImagePortObjectSpec(SvgCell.TYPE);
    return new PortObjectSpec[] {CombinedFskPortObjectSpec.INSTANCE, imageSpec};
  }

  private FskPortObject mergeParameterForJoinedObject(CombinedFskPortObject portObject)
      throws JsonProcessingException, IOException {
    FskPortObject first = portObject.getFirstFskPortObject();
    FskPortObject second = portObject.getSecondFskPortObject();
    if (first instanceof CombinedFskPortObject) {
      first = mergeParameterForJoinedObject((CombinedFskPortObject) first);
    }
    if (second instanceof CombinedFskPortObject) {
      second = mergeParameterForJoinedObject((CombinedFskPortObject) second);
    }
    // SwaggerUtil.setParameter(portObject.modelMetadata,
    // JoinerNodeUtil.combineParameters(SwaggerUtil.getParameter(first.modelMetadata),
    // SwaggerUtil.getParameter(second.modelMetadata)));

    SwaggerUtil.setParameter(portObject.modelMetadata, JoinerNodeUtil.combineParameters(
        MAPPER.readValue(MAPPER.writeValueAsString(SwaggerUtil.getParameter(first.modelMetadata)),
            new TypeReference<List<Parameter>>() {}),
        MAPPER.readValue(MAPPER.writeValueAsString(SwaggerUtil.getParameter(second.modelMetadata)),
            new TypeReference<List<Parameter>>() {})));
    // TODO join metadata instead of code here
    return portObject;
  }

  @Override
  protected void performReset() {
    createEmptyViewValue();
    setViewRepresentation(null);

    firstInputPort = null;
    secondInputPort = null;
    thirdInputPort = null;
    fourthInputPort = null;
  }

  private void copyValueToConfig() {
    JoinerViewValue value = getViewValue();
    m_config.modelMetaData = value.modelMetaData;
    m_config.connections = value.joinRelations;
    m_config.jsonRepresentation = value.jsonRepresentation;
    m_config.joinerModelsData = value.joinerModelsData;

  }

  private void copyConfigToView(JoinerViewValue value) {
    value.modelMetaData = m_config.modelMetaData;
    value.joinRelations = m_config.connections;
    value.jsonRepresentation = m_config.jsonRepresentation;
    JoinerViewRepresentation representation = getViewRepresentation();
    representation.joinerModelsData =
        m_config.joinerModelsData != null ? m_config.joinerModelsData : new JoinerModelsData();
    value.joinerModelsData = representation.joinerModelsData;

  }


  @Override
  protected void useCurrentValueAsDefault() {
    synchronized (getLock()) {
      copyValueToConfig();
    }
  }

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
      m_config.connections = MAPPER.readValue(connectionString, JoinRelation[].class);
    } else {
      File configFile = new File(settingFolder, "JoinRelations.json");
      if (configFile.exists()) {
        m_config.connections = MAPPER.readValue(configFile, JoinRelation[].class);
      }
    }

    if (flowVariables.containsKey("modelMetaData.json")) {
      m_config.modelMetaData = flowVariables.get("modelMetaData.json").getStringValue();
    } else {
      File configFile = new File(settingFolder, "modelMetaData.json");
      if (configFile.exists()) {
        m_config.modelMetaData = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
      }
    }

    if (flowVariables.containsKey("firstModelParameters.json")) {
      String parametersString = flowVariables.get("firstModelParameters.json").getStringValue();
      m_config.joinerModelsData.firstModelParameters =
          MAPPER.readValue(parametersString, Parameter[].class);
    } else {
      File configFile = new File(settingFolder, "firstModelParameters.json");
      if (configFile.exists()) {
        m_config.joinerModelsData.firstModelParameters =
            MAPPER.readValue(configFile, Parameter[].class);
      }
    }

    if (flowVariables.containsKey("secondModelParameters.json")) {
      String parametersString = flowVariables.get("secondModelParameters.json").getStringValue();
      m_config.joinerModelsData.secondModelParameters =
          MAPPER.readValue(parametersString, Parameter[].class);
    } else {
      File configFile = new File(settingFolder, "secondModelParameters.json");
      if (configFile.exists()) {
        m_config.joinerModelsData.secondModelParameters =
            MAPPER.readValue(configFile, Parameter[].class);
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
    viewValue.joinRelations = m_config.connections;
    viewValue.modelMetaData = m_config.modelMetaData;

    JoinerViewRepresentation representation = getViewRepresentation();
    if (m_config.joinerModelsData != null
        && m_config.joinerModelsData.firstModelParameters != null) {
      representation.joinerModelsData.firstModelParameters =
          m_config.joinerModelsData.firstModelParameters;
    }
    if (m_config.joinerModelsData != null
        && m_config.joinerModelsData.secondModelParameters != null) {
      representation.joinerModelsData.firstModelParameters =
          m_config.joinerModelsData.secondModelParameters;
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    m_config.save(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    m_config.load(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    m_config.load(settings);
  }

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

  private static Map<String, List<String>> getParameterMap(FskPortObject jFirstInputPort,
      FskPortObject jSecondInputPort, FskPortObject jThirdInputPort,
      FskPortObject jFourthInputPort) {
    Map<String, List<String>> tempCopyOfParams = new HashMap<>();
    String firstModelName = SwaggerUtil.getModelName(jFirstInputPort.modelMetadata);
    String secondModelName = SwaggerUtil.getModelName(jSecondInputPort.modelMetadata);
    String thirdModelName = SwaggerUtil.getModelName(jThirdInputPort.modelMetadata);
    String fourthModelName = SwaggerUtil.getModelName(jFourthInputPort.modelMetadata);
    if (StringUtils.isNotBlank(firstModelName))
      tempCopyOfParams.put(firstModelName, SwaggerUtil.getParameter(jFirstInputPort.modelMetadata)
          .stream().map(param -> param.getId()).collect(Collectors.toList()));
    if (StringUtils.isNotBlank(secondModelName))
      tempCopyOfParams.put(secondModelName, SwaggerUtil.getParameter(jSecondInputPort.modelMetadata)
          .stream().map(param -> param.getId()).collect(Collectors.toList()));
    if (StringUtils.isNotBlank(thirdModelName))
      tempCopyOfParams.put(thirdModelName, SwaggerUtil.getParameter(jThirdInputPort.modelMetadata)
          .stream().map(param -> param.getId()).collect(Collectors.toList()));
    if (StringUtils.isNotBlank(fourthModelName))
      tempCopyOfParams.put(fourthModelName, SwaggerUtil.getParameter(jFourthInputPort.modelMetadata)
          .stream().map(param -> param.getId()).collect(Collectors.toList()));

    return tempCopyOfParams;
  }

  @Override
  protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {

    final String nodeWithId = NodeContext.getContext().getNodeContainer().getNameWithID();
    NodeContext.getContext().getWorkflowManager()
        .addListener(new NodeRemovedListener(nodeWithId, buildContainerName()));

    setInternalPortObjects(inObjects);
    outObj = createCombinedFskPortObject(firstInputPort, secondInputPort, thirdInputPort,
        fourthInputPort);
    Map<String, List<String>> unModifiedParamsNames =
        getParameterMap(firstInputPort, secondInputPort, thirdInputPort, fourthInputPort);
    JoinerNodeUtil.addIdentifierToParametersForCombinedObject(outObj, "", unModifiedParamsNames,
        modelsParamsOriginalNames);
    synchronized (getLock()) {

      JoinerViewValue value = getViewValue();
      if (value.modelMetaData == null) {
        loadJsonSetting();

        if (value.modelMetaData == null && fixNullPortsToDefault(inObjects).length >= 2) {
          value.modelMetaData =
              MAPPER.writeValueAsString(mergeParameterForJoinedObject(outObj).modelMetadata);
        }
        exec.setProgress(1);
      }
    }
  }

  private PortObject[] fixNullPortsToDefault(PortObject[] inObjects) throws IOException {
    PortObject[] inPorts =
        Arrays.stream(inObjects).filter(s -> (s != null)).toArray(PortObject[]::new);
    inputModelNumber = inPorts.length;
    switch (inputModelNumber) {
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

  private static CombinedFskPortObject createSimpleCombinedFskPortObject(FskPortObject first,
      FskPortObject second) throws IOException {
    CombinedFskPortObject out =
        new CombinedFskPortObject(Optional.empty(), new ArrayList<>(), first, second);
    out.modelMetadata = MAPPER.readValue(MAPPER.writeValueAsString(second.modelMetadata),
        SwaggerUtil.modelClasses.get(second.modelMetadata.getModelType()));
    SwaggerUtil.setParameter(out.modelMetadata, JoinerNodeUtil.combineParameters(
        MAPPER.readValue(MAPPER.writeValueAsString(SwaggerUtil.getParameter(first.modelMetadata)),
            new TypeReference<List<Parameter>>() {}),
        MAPPER.readValue(MAPPER.writeValueAsString(SwaggerUtil.getParameter(second.modelMetadata)),
            new TypeReference<List<Parameter>>() {})));
    Set<String> packageSet = new HashSet<>();
    packageSet.addAll(first.packages);
    packageSet.addAll(second.packages);
    out.packages.addAll(packageSet);
    return out;
  }

  private CombinedFskPortObject createCombinedFskPortObject(FskPortObject jFirstInputPort,
      FskPortObject jSecondInputPort, FskPortObject jThirdInputPort, FskPortObject jFourthInputPort)
      throws IOException {
    CombinedFskPortObject outObj;
    switch (inputModelNumber) {
      case 1:
      case 2:
        outObj = createSimpleCombinedFskPortObject(jFirstInputPort, jSecondInputPort);
        break;
      case 3:
        CombinedFskPortObject case3OutObj =
            createSimpleCombinedFskPortObject(jFirstInputPort, jSecondInputPort);
        outObj = createSimpleCombinedFskPortObject(case3OutObj, jThirdInputPort);
        break;
      default:
        CombinedFskPortObject defaultOutObj1 =
            createSimpleCombinedFskPortObject(jFirstInputPort, jSecondInputPort);
        CombinedFskPortObject defaultOutObj2 =
            createSimpleCombinedFskPortObject(defaultOutObj1, jThirdInputPort);
        outObj = createSimpleCombinedFskPortObject(defaultOutObj2, jFourthInputPort);
        break;
    }
    return outObj;
  }

  private static FskPortObject createEmptyFSKObject() throws IOException {
    
    FskPortObject fskPortObject = new FskPortObject(Optional.empty(), "", Collections.emptyList());
    fskPortObject.setModel("");
    fskPortObject.setViz("");
    fskPortObject.modelMetadata = NodeUtils.initializeModel(ModelType.genericModel);
    return fskPortObject;
  }

  @Override
  protected PortObject[] performExecuteCreatePortObjects(PortObject svgImageFromView,
      PortObject[] inObjects, ExecutionContext exec) throws Exception {

    JoinRelation[] connections = new JoinRelation[0];

    synchronized (getLock()) {

      JoinerViewValue value = getViewValue();

      if (value.joinRelations != null) {
        connections = value.joinRelations;
      } else if (m_config.connections != null) {
        connections = m_config.connections;
      }



      // Give CombinedModel some metadata (for now: from second portObject)
      // Consider Here that the model type is the same as the second model
      // if (value.modelMetaData != null) {
      // outObj.modelMetadata = MAPPER.readValue(value.modelMetaData,
      // SwaggerUtil.modelClasses.get(firstInputPort.modelMetadata.getModelType()));
      // }
      JoinerModelsData joinerModelsData = value.joinerModelsData;
      if (joinerModelsData.firstModel != null && joinerModelsData.firstModel.length > 0
          && StringUtils.isNotEmpty(joinerModelsData.firstModel[0])) {
        
        
        FskPortObject jFirstInputPort = getFSKObjectFromStringArray(firstInputPort.getEnvironmentManager(),
            joinerModelsData.firstModel, joinerModelsData.firstModelType);

        FskPortObject jSecondInputPort;
        FskPortObject jThirdInputPort;
        FskPortObject jFourthInputPort;
        if (joinerModelsData.secondModel != null && joinerModelsData.secondModel.length > 0
            && StringUtils.isNotEmpty(joinerModelsData.secondModel[0])) {
          jSecondInputPort = getFSKObjectFromStringArray(secondInputPort.getEnvironmentManager(),
              joinerModelsData.secondModel, joinerModelsData.secondModelType);
        } else {
          jSecondInputPort = createEmptyFSKObject();
        }
        if (joinerModelsData.thirdModel != null && joinerModelsData.thirdModel.length > 0
            && StringUtils.isNotEmpty(joinerModelsData.thirdModel[0])) {
          jThirdInputPort = getFSKObjectFromStringArray(thirdInputPort.getEnvironmentManager(),
              joinerModelsData.thirdModel, joinerModelsData.thirdModelType);
        } else {
          jThirdInputPort = createEmptyFSKObject();
        }

        if (joinerModelsData.fourthModel != null && joinerModelsData.fourthModel.length > 0
            && StringUtils.isNotEmpty(joinerModelsData.fourthModel[0])) {
          jFourthInputPort = getFSKObjectFromStringArray(fourthInputPort.getEnvironmentManager(),
              joinerModelsData.fourthModel, joinerModelsData.fourthModelType);
        } else {
          jFourthInputPort = createEmptyFSKObject();
        }
        fskID_to_fskObject.put(SwaggerUtil.getModelName(jFirstInputPort.modelMetadata),
            jFirstInputPort);
        fskID_to_fskObject.put(SwaggerUtil.getModelName(jSecondInputPort.modelMetadata),
            jSecondInputPort);
        fskID_to_fskObject.put(SwaggerUtil.getModelName(jThirdInputPort.modelMetadata),
            jThirdInputPort);
        fskID_to_fskObject.put(SwaggerUtil.getModelName(jFourthInputPort.modelMetadata),
            jFourthInputPort);
        
        outObj = createCombinedFskPortObject(jFirstInputPort, jSecondInputPort, jThirdInputPort, jFourthInputPort);
        if(value.modelMetaData != null) {
          outObj.modelMetadata = MAPPER.readValue(value.modelMetaData,
            SwaggerUtil.modelClasses.get(firstInputPort.modelMetadata.getModelType()));
        }else {
          outObj.modelMetadata = jFirstInputPort.modelMetadata;
        }
        resetParameterIdForObjectsFromJSON(outObj, 0);
        outObj = createCombinedFskPortObject(jFirstInputPort, jSecondInputPort, jThirdInputPort,
            jFourthInputPort);
        Map<String, List<String>> unModifiedParamsNames =
            getParameterMap(jFirstInputPort, jSecondInputPort, jThirdInputPort, jFourthInputPort);

        JoinerNodeUtil.addIdentifierToParametersForCombinedObject(outObj, "", unModifiedParamsNames,
            modelsParamsOriginalNames);
        mergeParameterForJoinedObject(outObj);



      }

      // outObj.setJoinerRelation(connections);
      setBackJoinConnection(outObj, connections);
      // change default values for CombinedModel to those of the currently selected simulations

      createDefaultParameterValues(outObj, outObj, 0);


      // give the new combined model a name:
      // suggestion: model1.name + model2.name

      JoinerNodeUtil.removeJoinedParameters(connections, outObj);


      // Create default simulation out of parameters metadata
      JoinerNodeUtil.createDefaultSimulation(outObj);


      // add all possible simulations to combined object
      JoinerNodeUtil.createAllPossibleSimulations(firstInputPort, secondInputPort, outObj);

      // remove suffix from original parameters since they are needed with their original id for the
      // scripts
      // remove the suffix for four models joining
      if (value.joinRelations != null) {
        resetParameterId(outObj, 0);
      }

    }

    return new PortObject[] {outObj, svgImageFromView};
  }

  /** A helper method to set the join information to the suitable level */
  private static void setBackJoinConnection(FskPortObject outObj, JoinRelation[] connections) {
    FskPortObject firstModel = ((CombinedFskPortObject) outObj).getFirstFskPortObject();
    Set<JoinRelation> subConnections = new HashSet<>();
    SwaggerUtil.getParameter(firstModel.modelMetadata).forEach((parameter) -> {
      for (JoinRelation connection : connections) {
        if (connection.getSourceParam().startsWith(parameter.getId())) {
          subConnections.add(connection);
        }
      }
    });
    ((CombinedFskPortObject) outObj)
        .setJoinerRelation(subConnections.stream().toArray(JoinRelation[]::new));
    if (firstModel instanceof CombinedFskPortObject) {
      setBackJoinConnection(firstModel, connections);
    }

  }

  private static void createDefaultParameterValues(FskPortObject outObj, FskPortObject subObj,
      int suffixIndex) {
    if (subObj instanceof CombinedFskPortObject) {
      ++suffixIndex;
      createDefaultParameterValues(outObj,
          ((CombinedFskPortObject) subObj).getSecondFskPortObject(), suffixIndex);
      createDefaultParameterValues(outObj, ((CombinedFskPortObject) subObj).getFirstFskPortObject(),
          suffixIndex);


    } else {
      if (subObj.simulations.size() > 0) {
        JoinerNodeUtil.createDefaultParameterValues(
            subObj.simulations.get(subObj.selectedSimulationIndex),
            SwaggerUtil.getParameter(subObj.modelMetadata), suffixIndex);
      }
    }
  }

  /** reset the parameters ID of all simple models to their original name without suffix */
  private void resetParameterId(FskPortObject outObj1, int suffixIndex) {
    if (outObj1 instanceof CombinedFskPortObject) {
      ++suffixIndex;
      resetParameterId(((CombinedFskPortObject) outObj1).getFirstFskPortObject(), suffixIndex);
      resetParameterId(((CombinedFskPortObject) outObj1).getSecondFskPortObject(), suffixIndex);
    } else {
      resetParameterIdToOriginal(SwaggerUtil.getParameter(outObj1.modelMetadata), suffixIndex);
    }
  }

  /** reset the parameters ID of all simple models to their original name without suffix */
  private void resetParameterIdForObjectsFromJSON(FskPortObject outObj1, int suffixIndex) {
    if (outObj1 instanceof CombinedFskPortObject) {
      ++suffixIndex;
      resetParameterIdForObjectsFromJSON(((CombinedFskPortObject) outObj1).getFirstFskPortObject(),
          suffixIndex);
      resetParameterIdForObjectsFromJSON(((CombinedFskPortObject) outObj1).getSecondFskPortObject(),
          suffixIndex);
    } else {
      resetParameterIdToOriginal(SwaggerUtil.getParameter(
          fskID_to_fskObject.get(SwaggerUtil.getModelName(outObj1.modelMetadata)).modelMetadata),
          suffixIndex);
    }
  }

  /**
   * Parameters of individual model must be reverted to original id values without suffix
   * 
   * @param parameter list of parameters
   * @param suffixIndex
   */
  private void resetParameterIdToOriginal(List<Parameter> parameters, int suffixIndex) {
    for (Parameter p : parameters) {
      p.setId(p.getId().substring(0, p.getId().length() - suffixIndex));
    }
  }

  @Override
  protected boolean generateImage() {
    return true;
  }

  /**
   * converts Object into JSON string to be passed to the JS View used together
   * with @getFSKObjectFromStringArray for the opposite conversion.
   * 
   * @param object
   * @return JSON String
   */
  private static String getObjectAsJSONString(Object object) {
    String jsonStr = "";

    if (object != null) {
      try {
        ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
        jsonStr = mapper.writeValueAsString(object);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return jsonStr;
  }

  private static String[] getFSKObjectAsStringArray(FskPortObject portObject) {
    return new String[] {getObjectAsJSONString(portObject.modelMetadata),
        getObjectAsJSONString(portObject.getModel()), getObjectAsJSONString(portObject.getViz()),
        getObjectAsJSONString(portObject.simulations), getObjectAsJSONString(portObject.packages)};
  }

  private static FskPortObject getFSKObjectFromStringArray(Optional<EnvironmentManager> manager, String[] model,
      String modelType) throws JsonMappingException, JsonProcessingException, IOException {
    FskPortObject portObject = new FskPortObject(manager, "", MAPPER.readValue(model[4], new TypeReference<List<String>>() {}));
    portObject.modelMetadata = MAPPER.readValue(model[0], SwaggerUtil.modelClasses.get(modelType));
    if(StringUtils.isNotEmpty(model[1]))
      portObject.setModel(MAPPER.readValue(model[1], String.class));
    if(StringUtils.isNotEmpty(model[2]))
      portObject.setViz(MAPPER.readValue(model[2], String.class));
    portObject.simulations.clear();
    portObject.packages.clear();
    portObject.simulations
        .addAll(MAPPER.readValue(model[3], new TypeReference<List<FskSimulation>>() {}));

    return portObject;
  }
}
