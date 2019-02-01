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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgImageContent;
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
import org.knime.core.util.FileUtil;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
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
        joinerProxyValue.setModelMath1(FromEOjectToJSON(inObj1.modelMath));
        joinerProxyValue.setModelMath2(FromEOjectToJSON(inObj2.modelMath));
        joinerProxyValue.setGeneralInformation(FromEOjectToJSON(
            combineGeneralInformation(inObj1.generalInformation, inObj2.generalInformation)));
        joinerProxyValue.setScope(FromEOjectToJSON(inObj1.scope));
        joinerProxyValue.setDataBackground(
            FromEOjectToJSON(combineDataBackground(inObj1.dataBackground, inObj2.dataBackground)));
        joinerProxyValue
            .setModelMath(FromEOjectToJSON(combineModelMath(inObj1.modelMath, inObj2.modelMath)));

        if (!(inObj2 instanceof CombinedFskPortObject)) {
          joinerProxyValue.setSecondModelViz(inObj2.viz);

        } else {
          /*
           * extract the visualization script of the second model which may be also an joined
           * object!
           */
          joinerProxyValue.setSecondModelViz(extractSecondObjectVis(inObj2));



        }

        exec.setProgress(1);
      }

      if (joinerProxyValue.getJoinRelations() != null) {
        String relation = joinerProxyValue.getJoinRelations();
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
            jR.setSourceParam(getEObjectFromJson(sourceTargetRelation.get("sourceParam").toString(),
                Parameter.class));
          }
          if (sourceTargetRelation.containsKey("targetParam")) {
            jR.setTargetParam(getEObjectFromJson(sourceTargetRelation.get("targetParam").toString(),
                Parameter.class));
          }


          joinerRelation.add(jR);

        }

      }

      outObj.generalInformation =
          getEObjectFromJson(joinerProxyValue.getGeneralInformation(), GeneralInformation.class);
      outObj.scope = getEObjectFromJson(joinerProxyValue.getScope(), Scope.class);
      outObj.dataBackground =
          getEObjectFromJson(joinerProxyValue.getDataBackground(), DataBackground.class);
      outObj.modelMath = getEObjectFromJson(joinerProxyValue.getModelMath(), ModelMath.class);
      if(joinerProxyValue.getModelScriptTree() != null) {
        JsonArray scriptTree = getScriptArray(joinerProxyValue.getModelScriptTree());
        setScriptBack(inObj1, inObj2, scriptTree);
      }
      // outObj.model = joinerProxyValue.getSecondModelScript();
      inObj2.viz = joinerProxyValue.getSecondModelViz();

      Set<String> packageSet = new HashSet<>();
      packageSet.addAll(inObj1.packages);
      packageSet.addAll(inObj2.packages);
      outObj.packages.addAll(packageSet);
      resolveParameters(joinerRelation, outObj);

      if (outObj.modelMath != null) {
        outObj.simulations.add(NodeUtils.createDefaultSimulation(outObj.modelMath.getParameter()));
      }
      outObj.setJoinerRelation(joinerRelation);
      imagePort = createSVGImagePortObject(joinerProxyValue.getSvgRepresentation());
    }


    return new PortObject[] {outObj, imagePort};
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
    JsonObject obj2 = scriptTree.getJsonObject(1);
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
      jsonObjectBuilder.add("text", "joined Model");
      FskPortObject first = ((CombinedFskPortObject) object).getFirstFskPortObject();
      FskPortObject second = ((CombinedFskPortObject) object).getSecondFskPortObject();
      JsonArrayBuilder array = Json.createArrayBuilder();
      array.add(getModelScriptNode(first));
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
    m_port = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {}

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
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
    combinedModelMath.getParameter().addAll(firstModelMath.getParameter());
    combinedModelMath.getParameter().addAll(secondModelMath.getParameter());

    // quality measures
    combinedModelMath.getQualityMeasures().addAll(firstModelMath.getQualityMeasures());
    combinedModelMath.getQualityMeasures().addAll(secondModelMath.getQualityMeasures());

    // events
    combinedModelMath.getEvent().addAll(firstModelMath.getEvent());
    combinedModelMath.getEvent().addAll(secondModelMath.getEvent());
    return combinedModelMath;

  }

}
