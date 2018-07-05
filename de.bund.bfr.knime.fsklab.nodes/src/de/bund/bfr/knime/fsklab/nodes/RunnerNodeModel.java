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
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jlibsedml.Parameter;
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
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import metadata.ParameterClassification;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.nodes.controller.ScriptExecutor;

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
    if (fskObj instanceof CombinedFskPortObject) {
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;
      FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();
      FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();
      LOGGER.info(" recieving '" + firstFskObj.selectedSimulationIndex
          + "' as the selected simulation index!");

      try (RController controller = new RController()) {

        // get the index of the selected simulation saved by the JavaScript FSK Simulation
        // Configurator the default value is 0 which is the the default simulation
        FskSimulation fskSimulation =
            firstFskObj.simulations.get(firstFskObj.selectedSimulationIndex);

        ExecutionContext context = exec.createSubExecutionContext(1.0);

        firstFskObj = runSnippet(controller, firstFskObj, fskSimulation, context);

        List<JoinRelation> joinRelations = comFskObj.getJoinerRelation();
        for (JoinRelation joinRelation : joinRelations) {
          for (metadata.Parameter sourceParameter : firstFskObj.modelMath.getParameter()) {
            if (joinRelation.getSourceParam().getParameterID()
                .equals(sourceParameter.getParameterID())) {
              for (FskSimulation sim : secondFskObj.simulations) {
                sim.getParameters().put(joinRelation.getTargetParam().getParameterID(),
                    sourceParameter.getParameterValue());
              }
            }
          }
        }

      }
      try (RController controller = new RController()) {

        // get the index of the selected simulation saved by the JavaScript FSK Simulation
        // Configurator the default value is 0 which is the the default simulation
        FskSimulation fskSimulation =
            secondFskObj.simulations.get(secondFskObj.selectedSimulationIndex);

        ExecutionContext context = exec.createSubExecutionContext(1.0);

        secondFskObj = runSnippet(controller, secondFskObj, fskSimulation, context);

      }
      try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
        final PNGImageContent content = new PNGImageContent(fis);
        internalSettings.plot = content.getImage();
        ImagePortObject imgObj = new ImagePortObject(content, PNG_SPEC);
        return new PortObject[] {secondFskObj, imgObj};
      } catch (IOException e) {
        LOGGER.warn("There is no image created");
        return new PortObject[] {secondFskObj};
      }
    } else {
      LOGGER.info(
          " recieving '" + fskObj.selectedSimulationIndex + "' as the selected simulation index!");

      try (RController controller = new RController()) {

        // get the index of the selected simulation saved by the JavaScript FSK Simulation
        // Configurator the default value is 0 which is the the default simulation
        FskSimulation fskSimulation = fskObj.simulations.get(fskObj.selectedSimulationIndex);

        ExecutionContext context = exec.createSubExecutionContext(1.0);

        fskObj = runSnippet(controller, fskObj, fskSimulation, context);
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
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final FskSimulation simulation, final ExecutionMonitor exec) throws Exception {

    final ScriptExecutor executor = new ScriptExecutor(controller);

    // Sets up working directory with resource files. This directory needs to be deleted.
    exec.setMessage("Add resource files");
    {
      String workingDirectoryString = fskObj.getWorkingDirectory();
      if (!workingDirectoryString.isEmpty()) {
        Path workingDirectory =
            FileUtil.getFileFromURL(FileUtil.toURL(workingDirectoryString)).toPath();
        controller.setWorkingDirectory(workingDirectory);
      }
    }

    // START RUNNING MODEL
    exec.setMessage("Setting up output capturing");
    executor.setupOutputCapturing(exec);

    // Install needed libraries
    if (!fskObj.packages.isEmpty()) {
      try {
        // Install missing libraries
        LibRegistry libReg = LibRegistry.instance();
        List<String> missingLibs = fskObj.packages.stream().filter(lib -> !libReg.isInstalled(lib))
            .collect(Collectors.toList());

        if (!missingLibs.isEmpty()) {
          libReg.installLibs(missingLibs);
        }
      } catch (RException | REXPMismatchException e) {
        LOGGER.error(e.getMessage());
      }
    }

    exec.setMessage("Add paths to libraries");
    controller.addPackagePath(LibRegistry.instance().getInstallationPath());

    exec.setMessage("Set parameter values");
    LOGGER.info(" Running with '" + simulation.getName() + "' simulation!");
    String paramScript = NodeUtils.buildParameterScript(simulation);
    executor.execute(paramScript, exec);

    exec.setMessage("Run models script");
    executor.executeIgnoreResult(fskObj.model, exec);

    exec.setMessage("Run visualization script");
    try {
      NodeUtils.plot(internalSettings.imageFile, fskObj.viz, nodeSettings.width,
          nodeSettings.height, nodeSettings.pointSize, nodeSettings.res, executor, exec);

      // Save path of generated plot
      fskObj.setPlot(internalSettings.imageFile.getAbsolutePath());
    } catch (final RException exception) {
      LOGGER.warn("Visualization script failed", exception);
    }

    exec.setMessage("Restore library paths");
    controller.restorePackagePath();

    exec.setMessage("Collecting captured output");
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
    exec.setMessage("Cleaning up");
    try {
      List<metadata.Parameter> outPutParameters = fskObj.modelMath.getParameter().stream()
          .filter(p -> p.getParameterClassification() == ParameterClassification.OUTPUT)
          .collect(Collectors.toList());
      for (metadata.Parameter outputParam : outPutParameters) {
        String outputParamId = outputParam.getParameterID();

        REXP expression = controller.eval(outputParamId, true);
        // check if parameter is MATRIX
        if (expression.dim() != null) {
          // check if parameter is Numeric MATRIX
          if (expression.isNumeric()) {
            double[] elements = expression.asDoubles();
            String outValue = Arrays.toString(elements);
            REXP dimExpr = controller.eval("dim(" + outputParamId + ")", true);
            int[] dims = dimExpr.asIntegers();
            String output = String.format(" matrix( c(%s) , nrow= %d , ncol= %d, byrow = TRUE)",
                outValue.substring(1, outValue.length() - 1), dims[0], dims[1]);
            System.out.println(output);
            outputParam.setParameterValue(output);
          }
          // check if parameter is String MATRIX
          else {
            String[] elements = expression.asStrings();
            String preValue  = "";
            for(String el : elements) {
              preValue += String.format("\"%s\",",el);
            }
            String outValue = String.format("c(%s)", preValue.substring(0,preValue.length()-1));
            REXP dimExpr = controller.eval("dim(" + outputParamId + ")", true);
            int[] dims = dimExpr.asIntegers();
            String output = String.format(" matrix( %s , nrow= %d , ncol= %d, byrow = TRUE)",
                outValue, dims[0], dims[1]);
            System.out.println(output);
            outputParam.setParameterValue(output);
          }

        } 
        // check if parameter is Numeric
        else if (expression.isNumeric()) {
          // check if parameter is Numeric Vector
          if (expression.length() > 1) {
            // check if parameter is Double Vector
            if (!expression.isInteger()) {
              double[] elements = expression.asDoubles();
              String preValue = Arrays.toString(elements);
              String output = String.format("c(%s)", preValue.substring(1,preValue.length()-1));
              outputParam.setParameterValue(output);
            } 
            // check if parameter is Integer Vector
            else {
              int[] elements = expression.asIntegers();
              String preValue = Arrays.toString(elements);
              String output = String.format("c(%s)", preValue.substring(1,preValue.length()-1));
              outputParam.setParameterValue(output);
            }
          } 
          // check if parameter is Double Value
          else if (!expression.isInteger()) {
            Double outputParamVal = expression.asDouble();
            String value = outputParamVal.toString();
            outputParam.setParameterValue(value);
          } 
          // check if parameter is Integer Value
          else {
            Integer outputParamVal = expression.asInteger();
            String value = outputParamVal.toString();
            outputParam.setParameterValue(value);
          }

        } 
        // check if parameter is String
        else if (expression.isString()) {
          // check if parameter is String Vector
          if (expression.length() > 1) {
            String[] elements = expression.asStrings();
            String preValue  = "";
            for(String el : elements) {
              preValue += String.format("\"%s\",",el);
            }
            String output = String.format("c(%s)", preValue.substring(0,preValue.length()-1));
            outputParam.setParameterValue(output);
          }
          // check if parameter is String or Boolean Value
          else {
            String outputParamVal = expression.asString();
            outputParam.setParameterValue(outputParamVal.toString());
          }

        }
      }
    } catch (Exception ex) {
      LOGGER.error(ex);
    }
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
