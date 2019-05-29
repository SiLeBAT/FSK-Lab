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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgImageContent;
import org.knime.core.internal.ReferencedFile;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;
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
import org.knime.core.node.workflow.NativeNodeContainer;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowEvent;
import org.knime.core.node.workflow.WorkflowListener;
import org.knime.core.util.FileUtil;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataPackage;
import metadata.ModelMath;
import metadata.Parameter;
import metadata.Scope;
import metadata.impl.MetadataFactoryImpl;


/**
 * Fsk Joiner node model.
 */

final class JoinerNodeModel extends
    AbstractWizardNodeModel<JoinerViewRepresentation, JoinerViewValue> implements PortObjectHolder {
  private final JoinerNodeSettings nodeSettings = new JoinerNodeSettings();
  private FskPortObject m_port;
  public final static String suffix = "_dup";
  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE, FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {CombinedFskPortObject.TYPE, ImagePortObject.TYPE};
  private static final String VIEW_NAME = new JoinerNodeFactory().getInteractiveViewName();
  String nodeWithId;
  String nodeName;
  String nodeId;
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
    nodeWithId = NodeContext.getContext().getNodeContainer().getNameWithID();
    nodeName = NodeContext.getContext().getNodeContainer().getName();
    nodeId = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];
    FskPortObject inObj1 = (FskPortObject) inObjects[0];
    FskPortObject inObj2 = (FskPortObject) inObjects[1];
    resolveParameterNamesConflict(inObj1, inObj2);
    CombinedFskPortObject outObj = new CombinedFskPortObject(
        FileUtil.createTempDir("combined").getAbsolutePath(), new ArrayList<>(), inObj1, inObj2);
    ImagePortObject imagePort = null;
    List<JoinRelation> joinerRelation = new ArrayList<>();
    // Clone input object
    synchronized (getLock()) {
      JoinerViewValue joinerProxyValue = getViewValue();
      
      // If not executed
      if (joinerProxyValue.getGeneralInformation() == null) {
        joinerProxyValue.setModelScriptTree(buildModelscriptAsTree(inObj1, inObj2));
        joinerProxyValue.setFirstModelName(inObj1.generalInformation.getName());
        joinerProxyValue.setSecondModelName(inObj2.generalInformation.getName());
        joinerProxyValue.setModelMath1(FromEOjectToJSON(inObj1.modelMath));
        joinerProxyValue.setModelMath2(FromEOjectToJSON(inObj2.modelMath));
        loadJsonSetting();
        if(joinerProxyValue.getGeneralInformation() == null) {
          joinerProxyValue.setGeneralInformation(FromEOjectToJSON(
              combineGeneralInformation(inObj1.generalInformation, inObj2.generalInformation)));
          joinerProxyValue.setScope(FromEOjectToJSON(inObj1.scope));
          joinerProxyValue.setDataBackground(
              FromEOjectToJSON(combineDataBackground(inObj1.dataBackground, inObj2.dataBackground)));
          joinerProxyValue
              .setModelMath(FromEOjectToJSON(combineModelMath(inObj1.modelMath, inObj2.modelMath)));
        }
        if(!StringUtils.isNotBlank(joinerProxyValue.getSecondModelViz())) {
          if (!(inObj2 instanceof CombinedFskPortObject)) {
            joinerProxyValue.setSecondModelViz(inObj2.viz);
  
          } else {
            /*
             * extract the visualization script of the second model which may be also an joined
             * object!
             */
            joinerProxyValue.setSecondModelViz(extractSecondObjectVis(inObj2));
          }
        } 

        exec.setProgress(1);
      }
      else {
        if(StringUtils.isNotEmpty(joinerProxyValue.getValidationErrors())) {
          setWarningMessage( "\n"+(joinerProxyValue.getValidationErrors()).replaceAll("\"", "").replaceAll(",,,", "\n"));
        }
      }
      if (joinerProxyValue.getJoinRelations() != null) {
        String relation = joinerProxyValue.getJoinRelations();
        
        //pushFlowVariableString("generatedJoinScript", relation);
        
        creatRelationList(relation, joinerProxyValue, joinerRelation);
 
      }else if(StringUtils.isNotBlank(nodeSettings.joinScript)){
        creatRelationList(nodeSettings.joinScript, joinerProxyValue, joinerRelation);
      }

      outObj.generalInformation =
          getEObjectFromJson(joinerProxyValue.getGeneralInformation(), GeneralInformation.class);
      outObj.scope = getEObjectFromJson(joinerProxyValue.getScope(), Scope.class);
      outObj.dataBackground =
          getEObjectFromJson(joinerProxyValue.getDataBackground(), DataBackground.class);
      outObj.modelMath = getEObjectFromJson(joinerProxyValue.getModelMath(), ModelMath.class);
      if(StringUtils.isNotEmpty(joinerProxyValue.getModelScriptTree())) {
        JsonArray scriptTree = getScriptArray(joinerProxyValue.getModelScriptTree());
        setScriptBack(inObj1, inObj2, scriptTree);
      }else {
        joinerProxyValue.setModelScriptTree(buildModelscriptAsTree(inObj1, inObj2));
      }
      inObj2.viz = joinerProxyValue.getSecondModelViz();

      Set<String> packageSet = new HashSet<>();
      packageSet.addAll(inObj1.packages);
      packageSet.addAll(inObj2.packages);
      outObj.packages.addAll(packageSet);
      resolveParameters(joinerRelation, outObj);

      
      outObj.setJoinerRelation(joinerRelation);
      if (outObj.modelMath != null) {
        createSimulation(outObj);
      }
      imagePort = createSVGImagePortObject(joinerProxyValue.getSvgRepresentation());
    }
   
    NodeContext.getContext().getWorkflowManager().addListener(new WorkflowListener() {
      
      @Override
      public void workflowChanged(WorkflowEvent event) {
       if(event.getType().equals(WorkflowEvent.Type.NODE_REMOVED) && event.getOldValue() instanceof NativeNodeContainer) {
         NativeNodeContainer nnc = (NativeNodeContainer)event.getOldValue();
         File directory = nnc.getDirectNCParent().getProjectWFM().getContext().getCurrentLocation();
         String nncnamewithId = nnc.getNameWithID();
         if(nncnamewithId.equals(nodeWithId)) {
          
           String containerName = nodeName+" (#"+nodeId+") setting";
           
           String settingFolderPath = directory.getPath().concat("/"+containerName);
           File settingFolder = new File(settingFolderPath);
          
           try {
             if(settingFolder.exists()) {
               Files.walk(settingFolder.toPath())
                   .sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
             }
           } catch (IOException e) {
             //nothing to do
           }
       }
       }
      }
    });
    return new PortObject[] {outObj, imagePort};
  }
  private void creatRelationList(String relation, JoinerViewValue joinerProxyValue,List<JoinRelation> joinerRelation ) throws InvalidSettingsException {
    if (StringUtils.isNotBlank(relation)) {
      joinerProxyValue.setJoinRelations(relation);
      JsonReader jsonReader = Json.createReader(new StringReader(relation));
      JsonArray relationJsonArray = jsonReader.readArray();
      jsonReader.close();
      for (JsonValue element : relationJsonArray) {
        JsonObject sourceTargetRelation = ((JsonObject) element);
        JoinRelation jR = new JoinRelation();
        if (sourceTargetRelation.containsKey("command")) {
          jR.setCommand(sourceTargetRelation.getString("command"));
        }
        if (sourceTargetRelation.containsKey("language_written_in")) {
          jR.setLanguage_written_in(sourceTargetRelation.getString("language_written_in"));
        }
        if (sourceTargetRelation.containsKey("sourceParam")) {
          jR.setSourceParam(getEObjectFromJson(
              sourceTargetRelation.get("sourceParam").toString(), Parameter.class));
        }
        if (sourceTargetRelation.containsKey("targetParam")) {
          jR.setTargetParam(getEObjectFromJson(
              sourceTargetRelation.get("targetParam").toString(), Parameter.class));
        }


        joinerRelation.add(jR);

      }
    }
  }
  private void createSimulation(FskPortObject inObj) {

    if (inObj instanceof CombinedFskPortObject) {
        
        inObj.simulations.clear();
        FskSimulation defaultSimulation =
            NodeUtils.createDefaultSimulation(inObj.modelMath.getParameter());
        inObj.simulations.add(defaultSimulation);
       
        createSimulation(((CombinedFskPortObject) inObj).getFirstFskPortObject());
        createSimulation(((CombinedFskPortObject) inObj).getSecondFskPortObject());
     
    } else {
      inObj.simulations.clear();
      FskSimulation defaultSimulation =
          NodeUtils.createDefaultSimulation(inObj.modelMath.getParameter());
      inObj.simulations.add(defaultSimulation);
     
    }
    inObj.selectedSimulationIndex = 0;
  }
  // second visualization script is the script which draw and control the plotting!
  public String extractSecondObjectVis(FskPortObject object) {
    if (!(object instanceof CombinedFskPortObject)) {
      return object.viz;
    } else {
      return extractSecondObjectVis(((CombinedFskPortObject) object).getSecondFskPortObject());
    }
  }

  public void setScriptBack(FskPortObject fskObject1, FskPortObject fskObject2,
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

  public String buildModelscriptAsTree(FskPortObject inObj1, FskPortObject inObj2) {
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

  public JsonArray getScriptArray(String input) {
    JsonReader jsonReader = Json.createReader(new StringReader(input));
    JsonArray array = jsonReader.readArray();
    jsonReader.close();
    return array;
  }

  public JsonObjectBuilder getModelScriptNode(FskPortObject object) {
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    jsonObjectBuilder.add("id", "" + generateRandomUnifier());
    if (object instanceof CombinedFskPortObject) {
      jsonObjectBuilder.add("text", "Joining Script");
      
      StringBuilder joinModel = new StringBuilder();
      if(((CombinedFskPortObject)object).getJoinerRelation() != null){
        ((CombinedFskPortObject)object).getJoinerRelation().stream().forEach(connection -> {joinModel.append(connection.getTargetParam().getParameterID() + " <- " + connection.getCommand()+";\n");});
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
      jsonObjectBuilder.add("text", object.generalInformation.getName());
      jsonObjectBuilder.add("script", object.model);
    }
    return jsonObjectBuilder;
  }
  
 
  
  
  
  public String generateRandomUnifier() {
    return new AtomicLong((int) (100000 * Math.random())).toString();
  }

  public ImagePortObject createSVGImagePortObject(String svgString) {

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

  private static String FromEOjectToJSON(final EObject eObject) throws JsonProcessingException {


    ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
    String jsonStr = objectMapper.writeValueAsString(eObject);
    return jsonStr;

  }

  private static <T> T getEObjectFromJson(String jsonStr, Class<T> valueType)
      throws InvalidSettingsException {
    final ResourceSet resourceSet = new ResourceSetImpl();
    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
        .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory(mapper));
    resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
        MetadataPackage.eINSTANCE);

    Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
    InputStream inStream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
    try {
      resource.load(inStream, null);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return (T) resource.getContents().get(0);
  }

  @Override
  protected void performReset() {
    createEmptyViewValue();
    nodeSettings.generalInformation = "";
    nodeSettings.scope = "";
    nodeSettings.dataBackground = "";
    nodeSettings.modelMath = "";
    nodeSettings.joinScript = "";
    m_port = null;
  }
 
  @Override
  protected void useCurrentValueAsDefault() {
  }
  
 
  protected void loadJsonSetting() throws IOException,
      CanceledExecutionException {
    
    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    String  name = NodeContext.getContext().getNodeContainer().getName();
    String  id = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];
    String containerName = name+" (#"+id+") setting";
    
    String settingFolderPath = directory.getPath().concat("/"+containerName);
    File settingFolder = new File(settingFolderPath);
   
    File joinRelationFile = new File(settingFolder, "JoinRelations.json");
    if (joinRelationFile.exists()) {
      nodeSettings.joinScript =
          FileUtils.readFileToString(joinRelationFile, Charset.defaultCharset());
      getViewValue().setJoinRelations(nodeSettings.joinScript);
    }
    File generalInformationFile = new File(settingFolder, "generalInformation.json");

    if (generalInformationFile.exists()) {
      nodeSettings.generalInformation =
          FileUtils.readFileToString(generalInformationFile, Charset.defaultCharset());
      getViewValue().setGeneralInformation(nodeSettings.generalInformation);
    }

    File scopeFile = new File(settingFolder, "scope.json");
    if (scopeFile.exists()) {
      nodeSettings.scope = FileUtils.readFileToString(scopeFile, Charset.defaultCharset());
      getViewValue().setScope(nodeSettings.scope);
    }

    File dataBackgroundnFile = new File(settingFolder, "dataBackground.json");
    if (dataBackgroundnFile.exists()) {
      nodeSettings.dataBackground =
          FileUtils.readFileToString(dataBackgroundnFile, Charset.defaultCharset());
      getViewValue().setDataBackground(nodeSettings.dataBackground);
    }

    File modelMathFile = new File(settingFolder, "modelMath.json");
    if (modelMathFile.exists()) {
      nodeSettings.modelMath = FileUtils.readFileToString(modelMathFile, Charset.defaultCharset());
      getViewValue().setModelMath(nodeSettings.modelMath);
    }
    
    File sourceTreeFile = new File(settingFolder, "sourceTree.json");
    if (sourceTreeFile.exists()) {
      String sourceTree = FileUtils.readFileToString(sourceTreeFile, Charset.defaultCharset());      
      getViewValue().setModelScriptTree(sourceTree);
    }
    
    File visualizationFile = new File(settingFolder, "visualization.txt");
    if (visualizationFile.exists()) {
      String visualizationScript = FileUtils.readFileToString(visualizationFile, Charset.defaultCharset());      
      getViewValue().setSecondModelViz(visualizationScript);
    }
    
  }

  /**
   * {@inheritDoc}
   */
 
  protected void saveJsonSetting(String joinRelation,String generalInformation,String scope,String dataBackground,String modelMath,String modelScriptTree,String visualizationScript) throws IOException, CanceledExecutionException {
    File directory =
        NodeContext.getContext().getWorkflowManager().getContext().getCurrentLocation();
    String  name = NodeContext.getContext().getNodeContainer().getName();
    String  id = NodeContext.getContext().getNodeContainer().getID().toString().split(":")[1];
    String containerName = name+" (#"+id+") setting";
    
    String settingFolderPath = directory.getPath().concat("/"+containerName);
    File settingFolder = new File(settingFolderPath);
    if (! settingFolder.exists()){
      settingFolder.mkdir();
    }
    File joinRelationFile = new File(settingFolder, "JoinRelations.json");
    if (joinRelation != null) {
      FileUtils.writeStringToFile(joinRelationFile, joinRelation,Charset.defaultCharset(),  false);
    }
    
    File generalInformationFile = new File(settingFolder, "generalInformation.json");
    if (generalInformation != null) {
      FileUtils.writeStringToFile(generalInformationFile, generalInformation,Charset.defaultCharset(),  false);
    }
    
    File scopeFile = new File(settingFolder, "scope.json");
    if (scope != null) {
      FileUtils.writeStringToFile(scopeFile, scope,Charset.defaultCharset(),  false);
    }
    
    File dataBackgroundFile = new File(settingFolder, "dataBackground.json");
    if (dataBackground != null) {
      FileUtils.writeStringToFile(dataBackgroundFile, dataBackground,Charset.defaultCharset(),  false);
    }
    
    File modelMathFile = new File(settingFolder, "modelMath.json");
    if (modelMath != null) {
      FileUtils.writeStringToFile(modelMathFile, modelMath,Charset.defaultCharset(),  false);
    }
    
    File sourceTreeFile = new File(settingFolder, "sourceTree.json");
    if(sourceTreeFile != null) {
      FileUtils.writeStringToFile(sourceTreeFile, modelScriptTree,Charset.defaultCharset(),  false);
    }
    
    File visualizationFile = new File(settingFolder, "visualization.txt");
    if(visualizationFile != null) {
      FileUtils.writeStringToFile(visualizationFile, visualizationScript,Charset.defaultCharset(),  false);
    }
    
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    try {
      JoinerViewValue vv = getViewValue();
      saveJsonSetting(vv.getJoinRelations(),vv.getGeneralInformation(),vv.getScope(),vv.getDataBackground(),vv.getModelMath(),vv.getModelScriptTree(),vv.getSecondModelViz());
    } catch (IOException | CanceledExecutionException e) {
      e.printStackTrace();
    }
  /*  nodeSettings.joinScript = getViewValue().getJoinRelations();
    nodeSettings.generalInformation = getViewValue().getGeneralInformation();
    nodeSettings.scope = getViewValue().getScope();
    nodeSettings.dataBackground = getViewValue().getDataBackground();
    nodeSettings.modelMath = getViewValue().getModelMath();
    
    nodeSettings.save(settings);*/
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    /*nodeSettings.load(settings);
    getViewValue().setJoinRelations(nodeSettings.joinScript);
    getViewValue().setGeneralInformation(nodeSettings.generalInformation);
    getViewValue().setScope(nodeSettings.scope);
    getViewValue().setDataBackground(nodeSettings.dataBackground);
    getViewValue().setModelMath(nodeSettings.modelMath);*/
    try {
      loadJsonSetting();
    } catch (IOException | CanceledExecutionException e) {
      // TODO Auto-generated catch block
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

  public void resolveParameterNamesConflict(FskPortObject fskPort1, FskPortObject fskPort2) {
    for (Parameter firstParam : fskPort1.modelMath.getParameter()) {
      for (Parameter secondParam : fskPort2.modelMath.getParameter()) {
        if (secondParam.getParameterID().equals(firstParam.getParameterID())) {
          firstParam.setParameterName(firstParam.getParameterID() + suffix);
          firstParam.setParameterID(firstParam.getParameterID() + suffix);
        }
      }
    }
    for (Parameter firstParam : fskPort1.modelMath.getParameter()) {
      for (Parameter secondParam : fskPort2.modelMath.getParameter()) {
        if (secondParam.getParameterID().equals(firstParam.getParameterID())) {
          firstParam.setParameterName(firstParam.getParameterID() + suffix);
          firstParam.setParameterID(firstParam.getParameterID() + suffix);
        }
      }
    }
  }

  public void resolveParameters(List<JoinRelation> relations, FskPortObject outfskPort) {

    if (relations != null)
      for (JoinRelation relation : relations) {

        Iterator<Parameter> iter = outfskPort.modelMath.getParameter().iterator();
        while (iter.hasNext()) {
          Parameter p = iter.next();
          // remove output from first model
          // Boolean b1 = p.getParameterID().equals(relation.getSourceParam().getParameterID());

          // remove input from second model
          Boolean b2 = p.getParameterID().equals(relation.getTargetParam().getParameterID());


          if (b2) {
            iter.remove();
          }
        } // while
      } // for
  }// resolveParameters

  public GeneralInformation combineGeneralInformation(GeneralInformation firstGeneralInformation,
      GeneralInformation secondGeneralInformation) {
    // GeneralInformation --------------
    GeneralInformation combinedGeneralInformation =
        MetadataFactoryImpl.eINSTANCE.createGeneralInformation();
    // TODO: Who is author?
    combinedGeneralInformation.setAuthor(firstGeneralInformation.getAuthor());
    // TODO: different Availabilities?
    combinedGeneralInformation.setAvailable(firstGeneralInformation.isAvailable());

    combinedGeneralInformation.setDescription(firstGeneralInformation.getDescription() + "\n"
        + secondGeneralInformation.getDescription());
    combinedGeneralInformation.setFormat(firstGeneralInformation.getFormat());
    combinedGeneralInformation.setIdentifier(
        firstGeneralInformation.getIdentifier() + " | " + secondGeneralInformation.getIdentifier());

    // TODO: different Languages?
    combinedGeneralInformation.setLanguage(firstGeneralInformation.getLanguage());

    // TODO: Language written in?
    combinedGeneralInformation.setLanguageWrittenIn(firstGeneralInformation.getLanguageWrittenIn());

    combinedGeneralInformation
        .setName(firstGeneralInformation.getName() + " | " + secondGeneralInformation.getName());
    combinedGeneralInformation.setObjective(
        firstGeneralInformation.getObjective() + " | " + firstGeneralInformation.getObjective());

    // TODO: different Rights?
    combinedGeneralInformation.setRights(firstGeneralInformation.getRights());

    // TODO: different Software?
    combinedGeneralInformation.setSoftware(firstGeneralInformation.getSoftware());

    // TODO: different Sources?
    combinedGeneralInformation.setSource(firstGeneralInformation.getSource());

    // TODO: different Status?
    combinedGeneralInformation.setStatus(firstGeneralInformation.getStatus());

    // creators
    combinedGeneralInformation.getCreators().addAll(firstGeneralInformation.getCreators());
    combinedGeneralInformation.getCreators().addAll(secondGeneralInformation.getCreators());

    // references
    combinedGeneralInformation.getReference().addAll(firstGeneralInformation.getReference());
    combinedGeneralInformation.getReference().addAll(secondGeneralInformation.getReference());

    // TODO: different modelCategories?
    combinedGeneralInformation.getModelCategory()
        .addAll(firstGeneralInformation.getModelCategory());
    return combinedGeneralInformation;
  }

  public Scope combineScope(Scope firstScope, Scope secondScope) {
    // Scope --------------
    Scope combinedScope = MetadataFactoryImpl.eINSTANCE.createScope();

    combinedScope.setGeneralComment(
        firstScope.getGeneralComment() + " | " + secondScope.getGeneralComment());

    // TODO: different spatial information(region/country)?
    combinedScope.setSpatialInformation(firstScope.getSpatialInformation());

    combinedScope.setTemporalInformation(
        firstScope.getTemporalInformation() + " | " + secondScope.getTemporalInformation());

    // products
    combinedScope.getProduct().addAll(firstScope.getProduct());
    combinedScope.getProduct().addAll(secondScope.getProduct());


    // hazards
    combinedScope.getHazard().addAll(firstScope.getHazard());
    combinedScope.getHazard().addAll(secondScope.getHazard());

    // population groups
    combinedScope.getPopulationGroup().addAll(firstScope.getPopulationGroup());
    combinedScope.getPopulationGroup().addAll(secondScope.getPopulationGroup());

    return combinedScope;
  }

  public DataBackground combineDataBackground(DataBackground firstDataBackground,
      DataBackground secondDataBackground) {
    // DataBackground --------------
    DataBackground combinedDataBackground = MetadataFactoryImpl.eINSTANCE.createDataBackground();
    // TODO: different studies?
    combinedDataBackground.setStudy(firstDataBackground.getStudy());

    // study samples
    combinedDataBackground.getStudySample().addAll(firstDataBackground.getStudySample());
    combinedDataBackground.getStudySample().addAll(secondDataBackground.getStudySample());
    // dietary assessment methods
    combinedDataBackground.getDietaryAssessmentMethod()
        .addAll(firstDataBackground.getDietaryAssessmentMethod());
    combinedDataBackground.getDietaryAssessmentMethod()
        .addAll(secondDataBackground.getDietaryAssessmentMethod());
    // laboratories
    combinedDataBackground.getLaboratory().addAll(firstDataBackground.getLaboratory());
    combinedDataBackground.getLaboratory().addAll(secondDataBackground.getLaboratory());
    // assay
    combinedDataBackground.getAssay().addAll(firstDataBackground.getAssay());
    combinedDataBackground.getAssay().addAll(secondDataBackground.getAssay());

    return combinedDataBackground;
  }

  public ModelMath combineModelMath(ModelMath firstModelMath, ModelMath secondModelMath) {
    // DataBackground --------------
    ModelMath combinedModelMath = MetadataFactoryImpl.eINSTANCE.createModelMath();
    // ModelMath ------------

    // TODO: different Exposures?
    combinedModelMath.setExposure(firstModelMath.getExposure());
    // TODO: different fitting procedures?
    combinedModelMath.setFittingProcedure(firstModelMath.getFittingProcedure());
    // model Equations
    combinedModelMath.getModelEquation().addAll(firstModelMath.getModelEquation());
    combinedModelMath.getModelEquation().addAll(secondModelMath.getModelEquation());

    // parameters
    combinedModelMath.getParameter().addAll(EcoreUtil.copyAll(firstModelMath.getParameter()));
    combinedModelMath.getParameter().addAll(EcoreUtil.copyAll(secondModelMath.getParameter()));

    // quality measures
    combinedModelMath.getQualityMeasures().addAll(firstModelMath.getQualityMeasures());
    combinedModelMath.getQualityMeasures().addAll(secondModelMath.getQualityMeasures());

    // events
    combinedModelMath.getEvent().addAll(firstModelMath.getEvent());
    combinedModelMath.getEvent().addAll(secondModelMath.getEvent());
    return combinedModelMath;

  }

}
