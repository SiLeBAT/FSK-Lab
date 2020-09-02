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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
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
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.nodes.ScriptHandler;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeUtil;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
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
//    internalSettings.loadInternals(nodeInternDir);
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    //internalSettings.saveInternals(nodeInternDir);
    internalSettings.saveInternals(nodeInternDir,fskObj);
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
//    this.fskObj = fskObj;
    
    final List<FskSimulation> simulation = fskObj.simulations;
    if (StringUtils.isNotBlank(nodeSettings.simulation)) {
      FskPortObject fskObjk = fskObj;
      IntStream.range(0, simulation.size())
          .filter(index -> simulation.get(index).getName().equals(nodeSettings.simulation))
          .findFirst().ifPresent(index -> reSelectSimulation(fskObjk, index));
    }

    try (ScriptHandler handler = ScriptHandler
        .createHandler(SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata), fskObj.packages)) {
      LinkedHashMap<String,String> originalOutputParameters = new LinkedHashMap<String,String>();
      List<Parameter> p = SwaggerUtil.getParameter(fskObj.modelMetadata);
      p.forEach(item -> originalOutputParameters.put(item.getId(), item.getId() ));
      runFskPortObject(handler, fskObj, originalOutputParameters, exec);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final SvgImageContent content = new SvgImageContent(fis);
      ImagePortObject imgObj = new ImagePortObject(content, SVG_SPEC);
      return new PortObject[] {fskObj, imgObj};
    } catch (IOException e) {
      LOGGER.warn("There is no image created");
      return new PortObject[] {fskObj};
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

  //TODO: make a method that just runs a simulation of a portObject runSimulation(fskObj, simulation)
  public FskPortObject runFskPortObject(ScriptHandler handler, FskPortObject fskObj, LinkedHashMap<String,String> originalOutputParameters,
      ExecutionContext exec) throws Exception {
    LOGGER.info("Running Model: " + fskObj);
    
    

    if (fskObj instanceof CombinedFskPortObject) {
      
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;
      
      
      
      
      //TODO: What happens if the user adds a parameter during joining process?? ¯\(°_o)/¯
      
      
      // prepareSimulation 1    *******
      FskSimulation fskSimOriginal  = comFskObj.simulations.get(comFskObj.selectedSimulationIndex);
      FskSimulation fskSimulationFirst = JoinerNodeUtil.makeIndividualSimulation(fskSimOriginal, JoinerNodeModel.SUFFIX_FIRST);
      
      // get local parameters to store  
      List<Parameter> p = SwaggerUtil.getParameter(fskObj.modelMetadata);
      p.forEach(item -> originalOutputParameters.put(item.getId(), item.getId() ));
      
      
      // create a mapping for the output parameters so that the true (original) parameter name is preserved but links      
      LinkedHashMap<String,String> oopFirst = JoinerNodeUtil.getOriginalParameterNames(originalOutputParameters, JoinerNodeModel.SUFFIX_FIRST);

      
      
      // if 1 is combined, go deeper
      FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();
      if (firstFskObj instanceof CombinedFskPortObject) {
      
        firstFskObj = stepIntoSubModel(firstFskObj, fskSimulationFirst, oopFirst, handler, exec);

      }
      
      
      
      

      // TODO: prepare files          *******
      ExecutionContext context = exec.createSubExecutionContext(1.0);
      // make a map of file name and its last modification date to observe any changes which
      // means file overwriting or generating new one
      
      Optional<Path> workingDirectory1;
      if (firstFskObj.getEnvironmentManager().isPresent()) {
        workingDirectory1 = firstFskObj.getEnvironmentManager().get().getEnvironment();
      } else {
        workingDirectory1 = Optional.empty();
      }
      
      
      // copy generated resources (output parameters) to working directory of second 
      // model
      for(Path path : comFskObj.generatedResourceFiles.getResourcePaths()) {
        
        FileUtils.copyFileToDirectory(path.toFile(), workingDirectory1.get().toFile());
      }
      
      
      Optional<Path> workingDirectory2;
      if (comFskObj.getSecondFskPortObject().getEnvironmentManager().isPresent()) {
        workingDirectory2 = comFskObj.getSecondFskPortObject().getEnvironmentManager().get().getEnvironment();
      } else {
        workingDirectory2 = Optional.empty();
      }
      
      
      // execute 1              *******
      
      // run the first model!
      LOGGER.info("Running Snippet of first Model: " + firstFskObj.toString());
      
      if (!(firstFskObj instanceof CombinedFskPortObject)) {
        firstFskObj = runSnippet(handler, firstFskObj, fskSimulationFirst, context);
        // save output from simulation by using current output name (with suffixes)
        // save is done by running the command in R
        JoinerNodeUtil.saveOutputVariable( oopFirst ,handler, exec);
        
        // collect paths to generated resource files in combined object
        firstFskObj.generatedResourceFiles.getResourcePaths().forEach(path -> comFskObj.generatedResourceFiles.addResourceFile(path.toFile()));
        
      }
        
      
      // prepareSimulation 2    *******

      
      FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();

      FskSimulation fskSimulationSecond = JoinerNodeUtil.makeIndividualSimulation(fskSimOriginal, JoinerNodeModel.SUFFIX_SECOND);

      // create a mapping for the output parameters so that the true (original) parameter name is preserved but links
      // to the current parameter (from the script)
      LinkedHashMap<String,String> oopSecond = JoinerNodeUtil.getOriginalParameterNames(originalOutputParameters, JoinerNodeModel.SUFFIX_SECOND);
    
      
      // apply join command
      applyJoinCommandToSimulation(comFskObj, fskSimulationSecond, originalOutputParameters);
    
        
      // copy generated resources (output parameters) to working directory of second 
      // model
      for(Path path : firstFskObj.generatedResourceFiles.getResourcePaths()) {
        FileUtils.copyFileToDirectory(path.toFile(), workingDirectory2.get().toFile());
      }
      
   
      // execute 2              ******* 
        

      LOGGER.info("Running Snippet of second Model: " + secondFskObj);
      // apply join command for complex join
      if (secondFskObj instanceof CombinedFskPortObject) {
//      
        secondFskObj = stepIntoSubModel(secondFskObj, fskSimulationSecond, oopSecond, handler, exec);


      } else {
        
         secondFskObj = runSnippet(handler, secondFskObj, fskSimulationSecond, context);


        // save output in the proper variable (with suffix)
        JoinerNodeUtil.saveOutputVariable(oopSecond, handler, exec);
        
        // collect paths to generated resource files in combined object
        secondFskObj.generatedResourceFiles.getResourcePaths().forEach(path -> comFskObj.generatedResourceFiles.addResourceFile(path.toFile()));

      }
//      fskObj.workspace = secondFskObj.workspace;
      comFskObj.workspace = secondFskObj.workspace;
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

      fskObj = runSnippet(handler, fskObj, fskSimulation, context);

      return fskObj;
    }
  }

  
  private void applyJoinCommandToSimulation(CombinedFskPortObject fskObj, FskSimulation fskSimulation, LinkedHashMap<String, String> originalOutputParameters) {
    
    JoinRelation[] joinRelations = fskObj.getJoinerRelation();

    if (joinRelations != null) {
      // originalOutputParameters have the name of the actual, globally unique parameter name.
      // the parameter name is marked (currently with []) e.g. [output_var]
      // therefore, the join command is something like input_var = 3*[output_var]
      // the local parameter name needs to be replaced with the unique one
      // 
      for (JoinRelation joinRelation : joinRelations)
        for(Map.Entry<String,String> pair : originalOutputParameters.entrySet()) {
          if(joinRelation.getSourceParam().equals(pair.getValue())) {
            
            //replace join command with unique identifier of parameter
            String command_new = joinRelation.getCommand().replaceAll("\\[([^<]*)\\]", pair.getKey());
            String target = joinRelation.getTargetParam().substring(0, joinRelation.getTargetParam().length() - 1);
            
            
            fskSimulation.getParameters().put(target, command_new);

          }
        }
      
    }
  }
  
  private FskPortObject stepIntoSubModel(FskPortObject fskObj,
      FskSimulation fskSimulation,
      LinkedHashMap<String, String> orignialParameters,
      ScriptHandler handler,
      final ExecutionContext exec ) {
    
    //carry correct simulation values
    int selectIndex = fskObj.selectedSimulationIndex;
    fskObj.selectedSimulationIndex = fskObj.simulations.size();
    fskObj.simulations.add(fskSimulation);
    try {
      fskObj = runFskPortObject(handler, fskObj, orignialParameters, exec);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //restore original simulation (just so we don't mess with original models)
    fskObj.simulations.remove(fskSimulation);
    fskObj.selectedSimulationIndex = selectIndex;
    
    return fskObj;

  }
  private FskPortObject runSnippet(ScriptHandler handler, final FskPortObject fskObj,
      final FskSimulation simulation, final ExecutionContext exec) throws Exception {

    handler.runSnippet(fskObj, simulation, exec, LOGGER, internalSettings, nodeSettings);

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
