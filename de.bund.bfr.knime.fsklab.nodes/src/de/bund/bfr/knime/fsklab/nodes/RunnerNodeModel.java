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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
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
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class RunnerNodeModel extends ExtToolOutputNodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for an SVG image. */
  private static final ImagePortObjectSpec SVG_SPEC = new ImagePortObjectSpec(SvgCell.TYPE);

  private final RunnerNodeInternalSettings internalSettings = new RunnerNodeInternalSettings();

  private RunnerNodeSettings nodeSettings = new RunnerNodeSettings();

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
    internalSettings.loadInternals(nodeInternDir);
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    internalSettings.saveInternals(nodeInternDir);
  }

  @Override
  protected void reset() {
    internalSettings.reset();
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

    FskPortObject fskObj = (FskPortObject) inData[0];
    final List<FskSimulation> simulation = fskObj.simulations;
    if (StringUtils.isNotBlank(nodeSettings.simulation)) {
      FskPortObject fskObjk = fskObj;
      IntStream.range(0, simulation.size())
          .filter(index -> simulation.get(index).getName().equals(nodeSettings.simulation))
          .findFirst().ifPresent(index -> reSelectSimulation(fskObjk, index));
    }

    try (ScriptHandler handler =
        ScriptHandler.createHandler(SwaggerUtil.getLanguageWrittenIn(fskObj.modelMetadata), fskObj.packages)) {
      runFskPortObject(handler, fskObj, exec);
    } catch (Exception e) {
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

  public FskPortObject runFskPortObject(ScriptHandler handler, FskPortObject fskObj,
      ExecutionContext exec) throws Exception {
    LOGGER.info("Running Model: " + fskObj);
    if (fskObj instanceof CombinedFskPortObject) {
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;
      List<JoinRelation> joinRelations = comFskObj.getJoinerRelation();
      FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();
      if (firstFskObj instanceof CombinedFskPortObject) {
        firstFskObj = runFskPortObject(handler, firstFskObj, exec);
      }
      FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();

      LOGGER.info(" recieving '" + firstFskObj.selectedSimulationIndex
          + "' as the selected simulation index!");

      // get the index of the selected simulation saved by the JavaScript FSK Simulation
      // Configurator the default value is 0 which is the the default simulation
      ExecutionContext context = exec.createSubExecutionContext(1.0);

      FskSimulation fskSimulation =
          firstFskObj.simulations.get(firstFskObj.selectedSimulationIndex);
      // recreate the INPUT or CONSTANT parameters which cause parameterId conflicts
      List<Parameter> alternativeParams = SwaggerUtil.getParameter(firstFskObj.modelMetadata).stream()
          .filter(p -> p.getId().endsWith(JoinerNodeModel.suffix))
          .collect(Collectors.toList());
      for (Parameter param : alternativeParams) {
        if (param.getClassification().equals(Parameter.ClassificationEnum.INPUT)
            || param.getClassification().equals(Parameter.ClassificationEnum.CONSTANT)) {
          // cut out the old Parameter ID
          String oldId = param.getId().substring(0,
              param.getId().indexOf(JoinerNodeModel.suffix));
          // make the old parameter available for the Model script
          if (fskSimulation.getParameters().get(param.getId()) != null) {
            handler.runScript(
                oldId + " <- " + fskSimulation.getParameters().get(param.getId()), exec,
                false);
          }
        }
      }

      // make a map of file name and its last modification date to observe any changes which
      // means file overwriting or generating new one
      String wd1 = firstFskObj.getWorkingDirectory();
      String wd2 = secondFskObj.getWorkingDirectory();

      Map<String, Long> fileModifacationMap = new HashMap<>();
      if (!wd1.isEmpty() && !wd2.isEmpty() && !wd1.equals(wd2)) {
        try (Stream<Path> paths =
            Files.walk(FileUtil.getFileFromURL(FileUtil.toURL(wd1)).toPath())) {
          paths.filter(Files::isRegularFile).forEach(currentFile -> {
            fileModifacationMap.put(currentFile.toFile().getName(),
                currentFile.toFile().lastModified());
          });
        }
      }

      // run the first model!
      LOGGER.info("Running Snippet of first Model: " + firstFskObj);
      if (!(firstFskObj instanceof CombinedFskPortObject)) {
        firstFskObj = runSnippet(handler, firstFskObj, fskSimulation, context);
      }
      // move the generated files to the working
      // directory of the second model
      if (!wd1.isEmpty() && !wd2.isEmpty() && !wd1.equals(wd2)) {
        Path targetDirectory = FileUtil.getFileFromURL(FileUtil.toURL(wd2)).toPath();
        try (Stream<Path> paths =
            Files.walk(FileUtil.getFileFromURL(FileUtil.toURL(wd1)).toPath())) {
          paths.filter(Files::isRegularFile).forEach(currentFile -> {
            // move new and modified files
            Long fileLastModified = fileModifacationMap.get(currentFile.toFile().getName());
            if (fileLastModified == null
                || currentFile.toFile().lastModified() != fileLastModified) {
              try {
                FileUtils.copyFileToDirectory(currentFile.toFile(), targetDirectory.toFile());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          });
        }
      }


      // assign the value of parameters which are causing parameterId conflicts to alternative
      // Parameter which is (maybe) used later in the joining

      for (Parameter param : alternativeParams) {
        // if (!(param.getParameterClassification().equals(ParameterClassification.INPUT)
        // || param.getParameterClassification().equals(ParameterClassification.CONSTANT))) {
        String alternativeId = param.getId();
        String oldId = param.getId().substring(0,
            param.getId().indexOf(JoinerNodeModel.suffix));
        handler.runScript(alternativeId + " <- " + oldId, exec, false);
        // controller.eval("rm(" + oldId + ")", false);
        // }
      }

      // apply join command
      if (joinRelations != null) {

        for (JoinRelation joinRelation : joinRelations) {
          for (Parameter sourceParameter : SwaggerUtil.getParameter(firstFskObj.modelMetadata)) {

            if (joinRelation.getSourceParam().getId()
                .equals(sourceParameter.getId())) {
              // override the value of the target parameter with the value generated by the
              // command
              for (FskSimulation sim : secondFskObj.simulations) {
                sim.getParameters().put(joinRelation.getTargetParam().getId(),
                    joinRelation.getCommand());
              }
            }
          }
        }
      }

      // get the index of the selected simulation saved by the JavaScript FSK Simulation
      // Configurater the default value is 0 which is the the default simulation
      FskSimulation secondfskSimulation =
          secondFskObj.simulations.get(secondFskObj.selectedSimulationIndex);
      LOGGER.info("Running Snippet of second Model: " + secondFskObj);
      // apply join command for complex join
      if (secondFskObj instanceof CombinedFskPortObject) {
        FskPortObject embedFSKObject = getEmbedFSKObject((CombinedFskPortObject) secondFskObj);
        FskPortObject secondEmbedFSKObject =
            getSecondEmbedFSKObject((CombinedFskPortObject) secondFskObj);

        if (joinRelations != null) {
          List<Parameter> alternativeParamsx = SwaggerUtil.getParameter(firstFskObj.modelMetadata).stream()
              .filter(p -> p.getId().endsWith(JoinerNodeModel.suffix))
              .collect(Collectors.toList());
          for (Parameter param : alternativeParamsx) {
            // if (!(param.getParameterClassification().equals(ParameterClassification.INPUT)
            // || param.getParameterClassification().equals(ParameterClassification.CONSTANT))) {
            String alternativeId = param.getId();
            String oldId = param.getId().substring(0,
                param.getId().indexOf(JoinerNodeModel.suffix));
            handler.runScript(alternativeId + " <- " + oldId, exec, false);
            // controller.eval("rm(" + oldId + ")", false);
            // }
          }
          for (JoinRelation joinRelation : joinRelations) {
            for (Parameter sourceParameter : SwaggerUtil.getParameter(firstFskObj.modelMetadata)) {

              if (joinRelation.getSourceParam().getId()
                  .equals(sourceParameter.getId())) {
                // override the value of the target parameter with the value generated by the
                // command
                for (FskSimulation sim : embedFSKObject.simulations) {
                  final String embedParametername = joinRelation.getTargetParam().getId();
                  List<Parameter> params = SwaggerUtil.getParameter(embedFSKObject.modelMetadata).stream()
                      .filter(p -> embedParametername.startsWith(p.getId()))
                      .collect(Collectors.toList());
                  if (params.size() > 0) {
                    sim.getParameters().put(params.get(0).getId(),
                        joinRelation.getCommand());
                  } else {
                    params = SwaggerUtil.getParameter(secondEmbedFSKObject.modelMetadata).stream()
                        .filter(p -> embedParametername.startsWith(p.getId()))
                        .collect(Collectors.toList());
                    if (params.size() > 0) {
                      sim.getParameters().put(params.get(0).getId(),
                          joinRelation.getCommand());
                    }
                  }
                }

              }
            }
          }
        }

        secondFskObj = runFskPortObject(handler, secondFskObj, exec);
      } else {
        secondFskObj = runSnippet(handler, secondFskObj, secondfskSimulation, context);
      }
      fskObj.workspace = secondFskObj.workspace;

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
}
