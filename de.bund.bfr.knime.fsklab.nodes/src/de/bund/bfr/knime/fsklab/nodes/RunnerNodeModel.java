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

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
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
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.data.image.png.PNGImageContent;
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
import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.bund.bfr.knime.fsklab.r.client.RController;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import metadata.Parameter;
import metadata.ParameterClassification;

public class RunnerNodeModel extends ExtToolOutputNodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for a PNG image. */
  private static final ImagePortObjectSpec PNG_SPEC = new ImagePortObjectSpec(PNGImageContent.TYPE);

  private final RunnerNodeInternalSettings internalSettings = new RunnerNodeInternalSettings();

  private RunnerNodeSettings nodeSettings = new RunnerNodeSettings();

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL};

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
    /*
     * try (RController controller = new RController()) { controller.eval("tools::pskill(" + PID +
     * ")", true); } catch (RException e) { e.printStackTrace(); }
     */
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
    return new PortObjectSpec[] {FSK_SPEC, PNG_SPEC};
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
    try (RController controller = new RController()) {
      fskObj = runFskPortObject(fskObj, exec, controller);
    }
    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final PNGImageContent content = new PNGImageContent(fis);
      internalSettings.plot = content.getImage();
      ImagePortObject imgObj = new ImagePortObject(content, PNG_SPEC);
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

  public FskPortObject runFskPortObject(FskPortObject fskObj, ExecutionContext exec,
      RController controller) throws Exception {
    LOGGER.info("Running Model: " + fskObj);
    if (fskObj instanceof CombinedFskPortObject) {
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;
      List<JoinRelation> joinRelations = comFskObj.getJoinerRelation();
      FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();
      if (firstFskObj instanceof CombinedFskPortObject) {
        firstFskObj = runFskPortObject(firstFskObj, exec, controller);
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
      List<Parameter> alternativeParams = firstFskObj.modelMath.getParameter().stream()
          .filter(p -> p.getParameterID().endsWith(JoinerNodeModel.suffix))
          .collect(Collectors.toList());
      for (Parameter param : alternativeParams) {
        if (param.getParameterClassification().equals(ParameterClassification.INPUT)
            || param.getParameterClassification().equals(ParameterClassification.CONSTANT)) {
          // cut out the old Parameter ID
          String oldId = param.getParameterID().substring(0,
              param.getParameterID().indexOf(JoinerNodeModel.suffix));
          // make the old parameter available for the Model script
          if (fskSimulation.getParameters().get(param.getParameterID()) != null) {
            controller.eval(
                oldId + " <- " + fskSimulation.getParameters().get(param.getParameterID()), false);
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
        firstFskObj = runSnippet(controller, firstFskObj, fskSimulation, context);
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
        String alternativeId = param.getParameterID();
        String oldId = param.getParameterID().substring(0,
            param.getParameterID().indexOf(JoinerNodeModel.suffix));
        controller.eval(alternativeId + " <- " + oldId, false);
        // controller.eval("rm(" + oldId + ")", false);
        // }
      }

      // apply join command
      if (joinRelations != null) {

        for (JoinRelation joinRelation : joinRelations) {
          for (metadata.Parameter sourceParameter : firstFskObj.modelMath.getParameter()) {

            if (joinRelation.getSourceParam().getParameterID()
                .equals(sourceParameter.getParameterID())) {
              // override the value of the target parameter with the value generated by the
              // command
              for (FskSimulation sim : secondFskObj.simulations) {
                sim.getParameters().put(joinRelation.getTargetParam().getParameterID(),
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
          List<Parameter> alternativeParamsx = firstFskObj.modelMath.getParameter().stream()
              .filter(p -> p.getParameterID().endsWith(JoinerNodeModel.suffix))
              .collect(Collectors.toList());
          for (Parameter param : alternativeParamsx) {
            // if (!(param.getParameterClassification().equals(ParameterClassification.INPUT)
            // || param.getParameterClassification().equals(ParameterClassification.CONSTANT))) {
            String alternativeId = param.getParameterID();
            String oldId = param.getParameterID().substring(0,
                param.getParameterID().indexOf(JoinerNodeModel.suffix));
            controller.eval(alternativeId + " <- " + oldId, false);
            // controller.eval("rm(" + oldId + ")", false);
            // }
          }
          for (JoinRelation joinRelation : joinRelations) {
            for (metadata.Parameter sourceParameter : firstFskObj.modelMath.getParameter()) {

              if (joinRelation.getSourceParam().getParameterID()
                  .equals(sourceParameter.getParameterID())) {
                // override the value of the target parameter with the value generated by the
                // command
                for (FskSimulation sim : embedFSKObject.simulations) {
                  final String embedParametername = joinRelation.getTargetParam().getParameterID();
                  List<Parameter> params = embedFSKObject.modelMath.getParameter().stream()
                      .filter(p -> embedParametername.startsWith(p.getParameterID()))
                      .collect(Collectors.toList());
                  if (params.size() > 0) {
                    sim.getParameters().put(params.get(0).getParameterID(),
                        joinRelation.getCommand());
                  } else {
                    params = secondEmbedFSKObject.modelMath.getParameter().stream()
                        .filter(p -> embedParametername.startsWith(p.getParameterID()))
                        .collect(Collectors.toList());
                    if (params.size() > 0) {
                      sim.getParameters().put(params.get(0).getParameterID(),
                          joinRelation.getCommand());
                    }
                  }
                }

              }
            }
          }
        }

        secondFskObj = runFskPortObject(secondFskObj, exec, controller);
      } else {
        secondFskObj = runSnippet(controller, secondFskObj, secondfskSimulation, context);
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

      fskObj = runSnippet(controller, fskObj, fskSimulation, context);

      return fskObj;
    }
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final FskSimulation simulation, final ExecutionMonitor exec) throws Exception {

    final ScriptExecutor executor = new ScriptExecutor(controller);

    // Sets up working directory with resource files. This directory needs to be deleted.
    exec.setProgress(0.05, "Add resource files");
    {
      String workingDirectoryString = fskObj.getWorkingDirectory();
      if (!workingDirectoryString.isEmpty()) {
        Path workingDirectory =
            FileUtil.getFileFromURL(FileUtil.toURL(workingDirectoryString)).toPath();
        controller.setWorkingDirectory(workingDirectory);
      }
    }

    // START RUNNING MODEL
    exec.setProgress(0.1, "Setting up output capturing");
    executor.setupOutputCapturing(exec);

    // Install needed libraries
    if (!fskObj.packages.isEmpty()) {
      LibRegistry.instance().install(fskObj.packages);
    }

    exec.setProgress(0.71, "Add paths to libraries");
    controller.addPackagePath(LibRegistry.instance().getInstallationPath());

    exec.setProgress(0.72, "Set parameter values");
    LOGGER.info(" Running with '" + simulation.getName() + "' simulation!");
    String paramScript = NodeUtils.buildParameterScript(simulation);
    executor.execute(paramScript, exec);

    exec.setProgress(0.75, "Run models script");
    executor.executeIgnoreResult(fskObj.model, exec);

    exec.setProgress(0.9, "Run visualization script");
    try {
      NodeUtils.plot(internalSettings.imageFile, fskObj.viz, nodeSettings.width,
          nodeSettings.height, nodeSettings.pointSize, nodeSettings.res, executor, exec);

      // Save path of generated plot
      fskObj.setPlot(internalSettings.imageFile.getAbsolutePath());
    } catch (final RException exception) {
      LOGGER.warn("Visualization script failed", exception);
    }

    exec.setProgress(0.96, "Restore library paths");
    controller.restorePackagePath();

    exec.setProgress(0.98, "Collecting captured output");
    executor.finishOutputCapturing(exec);

    // END RUNNING MODEL

    // Save workspace
    if (fskObj.workspace == null) {
      fskObj.workspace = FileUtil.createTempFile("workspace", ".R").toPath();
    }
    controller.saveWorkspace(fskObj.workspace, exec);

    // process the return value of error capturing and update error and
    // output views accordingly
    if (!executor.getStdOut().isEmpty()) {
      setExternalOutput(getLinkedListFromOutput(executor.getStdOut()));
    }

    if (!executor.getStdErr().isEmpty()) {
      final LinkedList<String> output = getLinkedListFromOutput(executor.getStdErr());
      setExternalErrorOutput(output);

      for (final String line : output) {
        if (line.startsWith(ScriptExecutor.ERROR_PREFIX)) {
          throw new RException(line, null);
        }
      }
    }

    // cleanup temporary variables of output capturing and consoleLikeCommand stuff
    exec.setProgress(0.99, "Cleaning up");
    executor.cleanup(exec);
    return fskObj;
  }

  private static final LinkedList<String> getLinkedListFromOutput(final String output) {
    return Arrays.stream(output.split("\\r?\\n")).collect(Collectors.toCollection(LinkedList::new));
  }

  Image getResultImage() {
    return internalSettings.plot;
  }
}
