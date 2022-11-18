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
package de.bund.bfr.knime.fsklab.v2_0.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang.StringUtils;
import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgImageContent;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.node.workflow.VariableType;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.nodes.DataArray;
import de.bund.bfr.knime.fsklab.nodes.JsonHandler;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
import de.bund.bfr.knime.fsklab.nodes.ParameterJson;
import de.bund.bfr.knime.fsklab.nodes.ScriptHandler;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import de.bund.bfr.knime.fsklab.v2_0.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelationAdvanced;
import de.bund.bfr.knime.fsklab.v2_0.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v2_0.joiner.JoinerNodeUtil;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class RunnerNodeModel extends ExtToolOutputNodeModel implements PortObjectHolder {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for an SVG image. */
  private static final ImagePortObjectSpec SVG_SPEC = new ImagePortObjectSpec(SvgCell.TYPE);

  private final RunnerNodeInternalSettings internalSettings = new RunnerNodeInternalSettings();
  /** Config identifier saveToJson */
  private boolean saveToJsonChecked = false;
  private boolean legacySettingsHaveBeenReset = false;
  private RunnerNodeSettings nodeSettings = new RunnerNodeSettings();
  private FskPortObject fskObj = null;
  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL};

  // isTest field is only used by maven build
  public static boolean isTest = false;
  LinkedHashMap <String, String> modelsToSuffixMap ;
  public RunnerNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  // --- internal settings methods ---

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // No internal settings
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // No internals settings
  }

  private void cleanGeneratedResources(FskPortObject portObject) {
    if (portObject != null && portObject.getGeneratedResourcesDirectory().isPresent()) {
      try {
        if (portObject instanceof CombinedFskPortObject) {
          cleanGeneratedResources(((CombinedFskPortObject) portObject).getFirstFskPortObject());
          cleanGeneratedResources(((CombinedFskPortObject) portObject).getSecondFskPortObject());
          
        } 
        if (portObject.getGeneratedResourcesDirectory().get().exists()) {
          Files.walk(portObject.getGeneratedResourcesDirectory().get().toPath())
              .sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                try {
                  file.delete();
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
              });
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  @Override
  protected void reset() {
    // update the Runner node from legacy
    legacySettingsHaveBeenReset = true;
    // remove simulation from settings to prioritize input
    nodeSettings.simulation = "";
    internalSettings.reset();
    cleanGeneratedResources(fskObj);
    fskObj = null;
  }

  // --- node settings methods ---

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does not validate anything
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FSK_SPEC, SVG_SPEC};
  }

  @Override
  protected PortObject[] execute(PortObject[] inData, ExecutionContext exec) throws Exception {

    this.setInternalPortObjects(inData);

    FskPortObject fskObj = (FskPortObject) inData[0];
    // this.fskObj = fskObj;

    final List<FskSimulation> simulation = fskObj.simulations;
    if (StringUtils.isNotBlank(nodeSettings.simulation) && legacySettingsHaveBeenReset) {
      FskPortObject fskObjk = fskObj;
      IntStream.range(0, simulation.size())
          .filter(index -> simulation.get(index).getName().equals(nodeSettings.simulation))
          .findFirst().ifPresent(index -> reSelectSimulation(fskObjk, index));
    }

    try {

      FskSimulation combinedSim = fskObj.simulations.get(fskObj.selectedSimulationIndex);
      List<JoinRelationAdvanced> joinRelationList = null;
      modelsToSuffixMap = new LinkedHashMap<>();
      if (fskObj instanceof CombinedFskPortObject) {
        List<Parameter> listOfParameter = SwaggerUtil.getParameter(fskObj.modelMetadata);
        joinRelationList = JoinerNodeUtil.generateJoinerRelationAdvanced(fskObj, null, listOfParameter, getJoinRelations((CombinedFskPortObject)fskObj,
            new ArrayList<JoinRelation>()), new AtomicInteger(0), null, modelsToSuffixMap, new ArrayList<JoinRelation>());
        /*joinRelationList = getMapOfSourceParameters(
            fskObj,
            getJoinRelations((CombinedFskPortObject)fskObj, new ArrayList<JoinRelation>()),
            null,
            ""
            );
          */
      }
     
      runFskPortObject(fskObj, combinedSim, exec, joinRelationList, "");
      
    } catch (Exception e) {
      throw new Exception(e.getLocalizedMessage(), e);
    }
    if (fskObj instanceof CombinedFskPortObject) {
      createTopLevelJsonFile((CombinedFskPortObject) fskObj, exec);
    }

    // make path to JSON parameters available by adding a flow-variable
    if (fskObj.getGeneratedResourcesDirectory().isPresent()) {
      this.pushFlowVariableString("generatedResources",
          fskObj.getGeneratedResourcesDirectory().get().getAbsolutePath());
    }
    // Store subplots of Comnbined Model in Flow-Variable
    if (fskObj instanceof CombinedFskPortObject) {
      List<String> subPlotPaths = new ArrayList<String>();
      makeSubPlotsAvailable(fskObj,subPlotPaths);
      this.pushFlowVariable("SubPlots",
          VariableType.StringArrayType.INSTANCE,subPlotPaths.toArray(new String[0]));
    }
    if(isVisScriptEmpty(fskObj)) {
      LOGGER.warn("There is no visualization script");
      String noImage = "<?xml version=\"1.0\"?>\n"
          + "<svg xmlns=\"http://www.w3.org/2000/svg\"\n"
          + "     height=\"300px\" width=\"300px\"\n"
          + "     version=\"1.1\"\n"
          + "     viewBox=\"-300 -300 600 600\"\n"
          + "     font-family=\"Bitstream Vera Sans,Liberation Sans, Arial, sans-serif\"\n"
          + "     font-size=\"72\"\n"
          + "     text-anchor=\"middle\" >\n"
          + "  \n"
          + "  <circle stroke=\"#AAA\" stroke-width=\"10\" r=\"280\" fill=\"#FFF\"/>\n"
          + "  <text style=\"fill:#444;\">\n"
          + "    <tspan x=\"0\" y=\"-8\">NO PLOT</tspan><tspan x=\"0\" y=\"80\">AVAILABLE</tspan>\n"
          + "  </text>\n"
          + "</svg>";
      Files.write(Paths.get(internalSettings.imageFile.getPath()), noImage.getBytes());
      try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
        final SvgImageContent content = new SvgImageContent(fis);
        ImagePortObject imgObj = new ImagePortObject(content, SVG_SPEC);
        fskObj.setPlot(internalSettings.imageFile.getPath());
        return new PortObject[] {fskObj, imgObj};
      }
    }
    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final SvgImageContent content = new SvgImageContent(fis);
      ImagePortObject imgObj = new ImagePortObject(content, SVG_SPEC);
      // create a parameter.json for the top level combined model
      return new PortObject[] {fskObj, imgObj};
    } catch (IOException e) {
      
      LOGGER.warn("There is no image created");
      LOGGER.warn(e.getMessage());
      return new PortObject[] {fskObj};
    }
  }
  private boolean isVisScriptEmpty(FskPortObject fskObject) {
    if (fskObject instanceof CombinedFskPortObject) {
      return isVisScriptEmpty(((CombinedFskPortObject)fskObject).getSecondFskPortObject());
    }else {
      if(StringUtils.isBlank(fskObject.getViz())) {
        return true;
      }else {
        return false;
      }
    }
  }
  
  private void createTopLevelJsonFile(CombinedFskPortObject fskObj,
      ExecutionContext exec) throws Exception {

    File newResourcesDirectory = FileUtil.createTempDir("generatedResources",new File(PreferenceInitializer.getFSKWorkingDirectory()));
    ParameterJson combinedJson = new ParameterJson( new File(newResourcesDirectory, JsonHandler.JSON_FILE_NAME));
    try {
    
    subModelParametersToJson(fskObj, fskObj, combinedJson, "");

    } finally {
      combinedJson.closeOutput();
      fskObj.setGeneratedResourcesDirectory(newResourcesDirectory);
    }

    

  }

  private void subModelParametersToJson(FskPortObject topLevel, FskPortObject fskObj,
      ParameterJson combinedJson, String suffix) throws Exception {


    if (fskObj instanceof CombinedFskPortObject) {

      subModelParametersToJson(topLevel, ((CombinedFskPortObject) fskObj).getFirstFskPortObject(),
          combinedJson, suffix + JoinerNodeModel.SUFFIX_FIRST);

      subModelParametersToJson(topLevel, ((CombinedFskPortObject) fskObj).getSecondFskPortObject(),
          combinedJson, suffix + JoinerNodeModel.SUFFIX_SECOND);
    } else {

      // get json file of single model
      if (!fskObj.getGeneratedResourcesDirectory().isPresent()) {
        return;
      }

      String resourcePath =
          fskObj.getGeneratedResourcesDirectory().get().getAbsolutePath().replaceAll("\\\\", "/")
              + "/";
      String jsonPath = resourcePath + "parameters.json";
      List<Parameter> topLevelParameter = SwaggerUtil.getParameter(topLevel.modelMetadata);

      ParameterJson modelJson = new ParameterJson(new File(jsonPath));

      DataArray botParam = modelJson.getParameter();
      while (botParam != null) {
        // Stream to output JSON file
        for (Parameter topParam : topLevelParameter) {
          if (topParam.getId().startsWith(botParam.getMetadata().getId() + suffix)) {
            combinedJson.addParameter(botParam);
          }
        }
        botParam = modelJson.getParameter();
      }
      modelJson.closeInput();
    }
  }
  
  public void makeSubPlotsAvailable(FskPortObject fskObj, List<String> list) {
    
    if (fskObj instanceof CombinedFskPortObject) {
      makeSubPlotsAvailable(((CombinedFskPortObject) fskObj).getFirstFskPortObject(), list);
      makeSubPlotsAvailable(((CombinedFskPortObject) fskObj).getSecondFskPortObject(), list);
    } else {
      if(fskObj.getGeneratedResourcesDirectory().isPresent()) {
        try {
          list.addAll(Files.walk(fskObj.getGeneratedResourcesDirectory().get().toPath())
              .map(file -> file.toString()).filter(file -> file.endsWith(".svg"))
              .collect(Collectors.toList()));
        } catch(IOException e) {
          String modelID = SwaggerUtil.getModelId(fskObj.modelMetadata);
          String plotNotFoundMsg = (modelID != null) ? modelID + " generated no Subplot" 
              : "no Subplot generated"; 
          LOGGER.warn( plotNotFoundMsg);
        }
      }
        
    }
  }
  
  public void reSelectSimulation(FskPortObject fskObj, int index) {
    fskObj.selectedSimulationIndex = index;
    if (fskObj instanceof CombinedFskPortObject) {
      reSelectSimulation(((CombinedFskPortObject) fskObj).getFirstFskPortObject(), index);
      reSelectSimulation(((CombinedFskPortObject) fskObj).getSecondFskPortObject(), index);
    }
  }

  public FskPortObject getEmbedFSKObject(CombinedFskPortObject comFskObj) {
    FskPortObject embedFSKObject = comFskObj.getFirstFskPortObject();
    if (embedFSKObject instanceof CombinedFskPortObject) {
      embedFSKObject = getEmbedFSKObject((CombinedFskPortObject) embedFSKObject);
    }
    return embedFSKObject;
  }

  public FskPortObject getSecondEmbedFSKObject(CombinedFskPortObject comFskObj) {
    FskPortObject embedFSKObject = comFskObj.getSecondFskPortObject();
    if (embedFSKObject instanceof CombinedFskPortObject) {
      embedFSKObject = getEmbedFSKObject((CombinedFskPortObject) embedFSKObject);
    }
    return embedFSKObject;
  }

  /**
   * Utility function to generate a Map that has the information of which parameter is 
   * joined with which sourceParameter and where that source Parameter is (which model
   * it belongs to). If he join Relation between model1 and model2 is 
   *    in2 <- out1
   * then the Map would append : < in2 , <model1, out >> 
   * 
   * @param portObject FSKX model 
   * @param joinRelations join relations of top-level combined model ( inlude
   * all relations in a combined model) 
   * @param sourceParametersMap Map < Target Parameter , < FSKX_Model, Source Parameter> >  
   * @param suffix of current model
   * @return Map < Target Parameter , < FSKX_Model, Source Parameter> >
   */
  private List<JoinRelationAdvanced> getMapOfSourceParameters(
      FskPortObject portObject,
      List<JoinRelation> joinRelations,
      List<JoinRelationAdvanced> joinRelationList,
      String suffix) {
    if (joinRelationList == null) {
      joinRelationList = new ArrayList<JoinRelationAdvanced>();
    }
    if (portObject instanceof CombinedFskPortObject) {
      joinRelationList =
          getMapOfSourceParameters(((CombinedFskPortObject) portObject).getFirstFskPortObject(),
              joinRelations, joinRelationList, suffix + JoinerNodeModel.SUFFIX_FIRST);
      joinRelationList =
          getMapOfSourceParameters(((CombinedFskPortObject) portObject).getSecondFskPortObject(),
              joinRelations, joinRelationList, suffix + JoinerNodeModel.SUFFIX_SECOND);
    } else {
      List<Parameter> listOfParameter = SwaggerUtil.getParameter(portObject.modelMetadata);
      
      for(JoinRelation joinRelation : joinRelations) {
        for (Parameter param : listOfParameter) {
          if (joinRelation.getSourceParam().startsWith(param.getId() + suffix)) {
            
            JoinRelationAdvanced entry = new JoinRelationAdvanced(joinRelation, portObject, suffix);
            entry.setSourceParam(param.getId());
            
            joinRelationList.add(entry);
          }
        }
      }
    }

    return joinRelationList;
  }
  
  /*
   * helper method to get all join relations for top and sub-models 
   */
  
  private List<JoinRelation> getJoinRelations(CombinedFskPortObject portObject,
      List<JoinRelation> joinRelations) {
    if (portObject.getJoinerRelation() != null) {
      joinRelations.addAll(Arrays.asList(portObject.getJoinerRelation()));
    }
    if (portObject.getFirstFskPortObject() instanceof CombinedFskPortObject) {
      getJoinRelations((CombinedFskPortObject) portObject.getFirstFskPortObject(), joinRelations);
    }
    if (portObject.getSecondFskPortObject() instanceof CombinedFskPortObject) {
      getJoinRelations((CombinedFskPortObject) portObject.getSecondFskPortObject(), joinRelations);
    }

    return joinRelations;
  }
  
  
  // TODO: make a method that just runs a simulation of a portObject runSimulation(fskObj,
  // simulation)
  public FskPortObject runFskPortObject(FskPortObject fskObj,
      FskSimulation combinedSim,
      ExecutionContext exec,
      List<JoinRelationAdvanced> joinRelationList,
      String suffix) throws Exception {
    LOGGER.info("Running Model: " + fskObj);



    if (fskObj instanceof CombinedFskPortObject) {
      
      // enable saving output parameters to JSON file
      saveToJsonChecked = true;
      
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;

      FskSimulation fskSimulationFirst = null;

      FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();

      if (!(firstFskObj instanceof CombinedFskPortObject)) {
        fskSimulationFirst = JoinerNodeUtil.makeIndividualSimulation(combinedSim,null,
             firstFskObj, modelsToSuffixMap.get(SwaggerUtil.getModelId(firstFskObj.modelMetadata)));
      } else {
        firstFskObj = stepIntoSubModel(firstFskObj, fskSimulationFirst, 
            combinedSim, exec, joinRelationList, suffix + JoinerNodeModel.SUFFIX_FIRST);
      }




      ExecutionContext context = exec.createSubExecutionContext(1.0);


      // execute 1 *******

      // run the first model!
      LOGGER.info("Running Snippet of first Model: " + firstFskObj.toString());

      if (!(firstFskObj instanceof CombinedFskPortObject)) {
        firstFskObj = runSnippet(firstFskObj, fskSimulationFirst, context, joinRelationList,
            modelsToSuffixMap.get(SwaggerUtil.getModelId(firstFskObj.modelMetadata)));

      }


      // prepareSimulation 2 *******


      FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();

      FskSimulation fskSimulationSecond = JoinerNodeUtil.makeIndividualSimulation(combinedSim,
          null, secondFskObj, modelsToSuffixMap.get(SwaggerUtil.getModelId(secondFskObj.modelMetadata)));

 
      // execute 2 *******


      LOGGER.info("Running Snippet of second Model: " + secondFskObj);
      // apply join command for complex join
      if (secondFskObj instanceof CombinedFskPortObject) {
        //
        secondFskObj = stepIntoSubModel(secondFskObj, fskSimulationSecond,
            combinedSim, exec, joinRelationList, suffix + JoinerNodeModel.SUFFIX_SECOND);
      } else {
       secondFskObj = runSnippet( secondFskObj, fskSimulationSecond, context, joinRelationList, modelsToSuffixMap.get(SwaggerUtil.getModelId(secondFskObj.modelMetadata)));


        // save output in the proper variable (with suffix)
//TODO:        JoinerNodeUtil.saveOutputVariable(oopSecond, handler, exec);

 
      }

      comFskObj.setWorkspace(secondFskObj.getWorkspace());
      return comFskObj;
    } else {
      LOGGER.info("Running simulation: " + nodeSettings.simulation);

      saveToJsonChecked = nodeSettings.saveToJson;
      
      FskSimulation fskSimulation;
      if (!nodeSettings.simulation.isEmpty() && legacySettingsHaveBeenReset) {
        // If a simulation is configured in the settings then pick it
        Optional<FskSimulation> sim = fskObj.simulations.stream()
            .filter(it -> it.getName().equals(nodeSettings.simulation)).findFirst();
        // If not present assign default simulation
        fskSimulation = sim.orElse(fskObj.simulations.get(0));
      } else {
        // If no selected simulation is saved in settings then pick the simulation from the input
        // port
        fskSimulation = fskObj.simulations.get(fskObj.selectedSimulationIndex);
      }

      ExecutionContext context = exec.createSubExecutionContext(1.0);

      fskObj = runSnippet(fskObj, fskSimulation, context, joinRelationList, suffix);

      return fskObj;
    }
  }

  private FskPortObject stepIntoSubModel(FskPortObject fskObj, FskSimulation fskSimulation,
      FskSimulation combinedSim,
      final ExecutionContext exec,
      List<JoinRelationAdvanced> joinRelationList,
      String suffix) {

    // carry correct simulation values
    int selectIndex = fskObj.selectedSimulationIndex;
    fskObj.selectedSimulationIndex = fskObj.simulations.size();
    fskObj.simulations.add(fskSimulation);
    try {
      fskObj = runFskPortObject(fskObj, combinedSim, exec, joinRelationList, suffix);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // restore original simulation (just so we don't mess with original models)
    fskObj.simulations.remove(fskSimulation);
    fskObj.selectedSimulationIndex = selectIndex;

    return fskObj;

  }

  private FskPortObject runSnippet(
      final FskPortObject fskObj,
      final FskSimulation simulation,
      final ExecutionContext exec,
      List<JoinRelationAdvanced> joinRelationList,
      String suffix) throws Exception {
    
    try (ScriptHandler handler = ScriptHandler
        .createHandler(SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata), fskObj.packages)) {
     
      // push flowvariable of executed simulation
      this.pushFlowVariableString("selectedSimulation", simulation.getName());

      
      // give handler info from checkBox that he needs to save parameter data to JSON 
      handler.setSaveToJsonChecked(saveToJsonChecked);
      handler.runSnippet(fskObj, simulation, exec, LOGGER, internalSettings.imageFile, joinRelationList, suffix);
  
      // process the return value of error capturing and update error and
      // output views accordingly
      if (!handler.getStdOut().isEmpty()) {
        setExternalOutput(getLinkedListFromOutput(handler.getStdOut()));
      }
  
      if (!handler.getStdErr().isEmpty()) {
        final LinkedList<String> output = getLinkedListFromOutput(handler.getStdErr());
        setExternalErrorOutput(output);
  
        for (final String line : output) {
          if (line.startsWith(ScriptExecutor.ERROR_PREFIX)) {
            throw new RException(line, null);
          }
        }
      }
  
      // cleanup temporary variables of output capturing and consoleLikeCommand stuff
      exec.setProgress(0.99, "Cleaning up");
      handler.cleanup(exec);
    } catch(Exception e) {
      throw new Exception(e.getLocalizedMessage(), e);
    }
      return fskObj;
  }

  private static final LinkedList<String> getLinkedListFromOutput(final String output) {
    return Arrays.stream(output.split("\\r?\\n")).collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {fskObj};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    fskObj = (FskPortObject) portObjects[0];
  }
  File getResultImage() {
    return  internalSettings.imageFile;
  }
  
}
