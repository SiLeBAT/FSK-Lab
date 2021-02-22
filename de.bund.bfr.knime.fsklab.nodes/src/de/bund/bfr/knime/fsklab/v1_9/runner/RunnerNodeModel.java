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
package de.bund.bfr.knime.fsklab.v1_9.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
import de.bund.bfr.knime.fsklab.nodes.ScriptHandler;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelationAdvanced;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeUtil;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class RunnerNodeModel extends ExtToolOutputNodeModel implements PortObjectHolder {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for an SVG image. */
  private static final ImagePortObjectSpec SVG_SPEC = new ImagePortObjectSpec(SvgCell.TYPE);

  private final RunnerNodeInternalSettings internalSettings = new RunnerNodeInternalSettings();

  private RunnerNodeSettings nodeSettings = new RunnerNodeSettings();
  private FskPortObject fskObj = null;
  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL};

  // isTest field is only used by maven build
  public static boolean isTest = false;

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

  @Override
  protected void reset() {
    internalSettings.reset();
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
    if (StringUtils.isNotBlank(nodeSettings.simulation)) {
      FskPortObject fskObjk = fskObj;
      IntStream.range(0, simulation.size())
          .filter(index -> simulation.get(index).getName().equals(nodeSettings.simulation))
          .findFirst().ifPresent(index -> reSelectSimulation(fskObjk, index));
    }

    try {
      
      FskSimulation combinedSim = fskObj.simulations.get(fskObj.selectedSimulationIndex);
      List<JoinRelationAdvanced> joinRelationList = null;
      if (fskObj instanceof CombinedFskPortObject) {
        joinRelationList = getMapOfSourceParameters(
            fskObj,
            getJoinRelations((CombinedFskPortObject)fskObj, new ArrayList<JoinRelation>()),
            null,
            ""
            );
          
      }
     
      runFskPortObject(fskObj, combinedSim, exec, joinRelationList, "");
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final SvgImageContent content = new SvgImageContent(fis);
      ImagePortObject imgObj = new ImagePortObject(content, SVG_SPEC);
      // create a parameter.json for the top level combined model
      if (fskObj instanceof CombinedFskPortObject) {
        createTopLevelJsonFile((CombinedFskPortObject)fskObj, null, exec);  
      }
      
      // make path to JSON parameters available by adding a flow-variable
      if (fskObj.getGeneratedResourcesDirectory().isPresent()) {
        this.pushFlowVariableString("generatedResources",
            fskObj.getGeneratedResourcesDirectory().get().getAbsolutePath());
      }
      
      return new PortObject[] {fskObj, imgObj};
    } catch (IOException e) {
      LOGGER.warn("There is no image created");
      return new PortObject[] {fskObj};
    }
  }

  private void createTopLevelJsonFile(CombinedFskPortObject fskObj,
      List<JoinRelationAdvanced> joinRelationList,
      ExecutionContext exec) throws Exception {
    
    joinRelationList = getMapOfTopLevelParameters(fskObj, fskObj, null,"");
    FskSimulation fskSimulation = new FskSimulation("dd");
    fskObj.setViz("");
    runSnippet(fskObj, fskSimulation, exec, joinRelationList, "");
  }

  private List<JoinRelationAdvanced> getMapOfTopLevelParameters(
      FskPortObject topLevel,
      FskPortObject fskObj,
      List<JoinRelationAdvanced> joinRelationList,
      String suffix) {
    if (joinRelationList == null) {
      joinRelationList = new ArrayList<JoinRelationAdvanced>();
    }
    if (fskObj instanceof CombinedFskPortObject) {
      joinRelationList =
          getMapOfTopLevelParameters(topLevel, ((CombinedFskPortObject) fskObj).getFirstFskPortObject(),
              joinRelationList, suffix + JoinerNodeModel.SUFFIX_FIRST);
      joinRelationList =
          getMapOfTopLevelParameters(topLevel, ((CombinedFskPortObject) fskObj).getSecondFskPortObject(),
              joinRelationList, suffix + JoinerNodeModel.SUFFIX_SECOND);
    } else {
      List<Parameter> topLevelParameter = SwaggerUtil.getParameter(topLevel.modelMetadata);
      List<Parameter> botLevelParameter = SwaggerUtil.getParameter(fskObj.modelMetadata);
      for (Parameter topParam : topLevelParameter) {
        for(Parameter botParam : botLevelParameter) {
          if ( topParam.getId().startsWith(botParam.getId() + suffix)) {
            JoinRelation joinRelation = new JoinRelation(
                botParam.getId(),
                topParam.getId(),
                botParam.getId() + suffix,
                SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata));
            JoinRelationAdvanced entry = new JoinRelationAdvanced(joinRelation, fskObj, suffix);
            joinRelationList.add(entry);
          }
        }
      }
    }
    return joinRelationList;
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
      
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;

      FskSimulation fskSimulationFirst = null;

      FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();

      if (!(firstFskObj instanceof CombinedFskPortObject)) {
        fskSimulationFirst = JoinerNodeUtil.makeIndividualSimulation(combinedSim,null,
             firstFskObj,suffix + JoinerNodeModel.SUFFIX_FIRST);
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
            suffix + JoinerNodeModel.SUFFIX_FIRST);

      }


      // prepareSimulation 2 *******


      FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();

      FskSimulation fskSimulationSecond = JoinerNodeUtil.makeIndividualSimulation(combinedSim,
          null, secondFskObj, suffix + JoinerNodeModel.SUFFIX_SECOND);

 
      // execute 2 *******


      LOGGER.info("Running Snippet of second Model: " + secondFskObj);
      // apply join command for complex join
      if (secondFskObj instanceof CombinedFskPortObject) {
        //
        secondFskObj = stepIntoSubModel(secondFskObj, fskSimulationSecond,
            combinedSim, exec, joinRelationList, suffix + JoinerNodeModel.SUFFIX_SECOND);
      } else {
       secondFskObj = runSnippet( secondFskObj, fskSimulationSecond, context, joinRelationList, suffix + JoinerNodeModel.SUFFIX_SECOND);


        // save output in the proper variable (with suffix)
//TODO:        JoinerNodeUtil.saveOutputVariable(oopSecond, handler, exec);

 
      }

      comFskObj.setWorkspace(secondFskObj.getWorkspace());
      return comFskObj;
    } else {
      LOGGER.info("Running simulation: " + nodeSettings.simulation);

      FskSimulation fskSimulation;
      if (!nodeSettings.simulation.isEmpty()) {
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
      e.printStackTrace();
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
}
