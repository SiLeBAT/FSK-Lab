package de.bund.bfr.knime.fsklab.nodes.v1_9;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXP;

import de.bund.bfr.knime.fsklab.nodes.plot.v1_9.BasePlotter;
import de.bund.bfr.knime.fsklab.nodes.plot.v1_9.Ggplot2Plotter;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeSettings;
import metadata.SwaggerUtil;
import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.bund.bfr.knime.fsklab.r.client.RController;
import de.bund.bfr.knime.fsklab.r.client.RprofileManager;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import de.bund.bfr.metadata.swagger.Parameter;

public class RScriptHandler extends ScriptHandler {

  ScriptExecutor executor;
  RController controller;

  public RScriptHandler() throws RException, IOException {
    this(new ArrayList<String>(0));
  }

  public RScriptHandler(List<String> packages) throws RException, IOException {
    RprofileManager.subscribe();
    // initialize LibRegistry before Controller to avoid errors on switching R in preferences
    LibRegistry.instance(); 
    this.controller = new RController();
    this.executor = new ScriptExecutor(controller);
    
    if (packages.contains("ggplot2")) {
      plotter = new Ggplot2Plotter(controller);
    } else {
      plotter = new BasePlotter(controller);
    }
  }

  public void setWorkingDirectory(Path workingDirectory, ExecutionContext exec) throws Exception {
    controller.setWorkingDirectory(workingDirectory);
  }

  @Override
  public String[] runScript(String script, ExecutionContext exec, Boolean showErrors)
      throws Exception {
    if (showErrors) {
      REXP c = executor.execute(script, exec);
      String[] execResult = c.asStrings();

      return execResult;
    } else {
      executor.executeIgnoreResult(script, exec);
    }
    return null;
  }

  public void convertToKnimeDataTable(FskPortObject fskObj, ExecutionContext exec) throws Exception {
    LibRegistry.instance().install(Arrays.asList("data.table"));

    String DT = "library(data.table)\n";
    // "install.packages(\"data.table\")\n"

    // +"knime.out <-list(x,Value)\n"
    // +"knime.out <- setDT(lapply(knime.out,\"length<-\",max(length(knime.out))))[]";

    FskSimulation fskSimulation = fskObj.simulations.get(fskObj.selectedSimulationIndex);
    String input_lst = "";
    for (Map.Entry<String, String> entry : fskSimulation.getParameters().entrySet()) {
      String parameterName = entry.getKey();
      // String parameterValue = entry.getValue();

      input_lst += parameterName + "= " + parameterName + ",";

    }

    String output_lst = "";
    List<Parameter> paras = SwaggerUtil.getParameter(fskObj.modelMetadata);
    for (Parameter p : paras) {
      if (p.getClassification() == Parameter.ClassificationEnum.OUTPUT) {
        output_lst += p.getName() + "= " + p.getName() + ",";
      }
    }

    String lst = input_lst + output_lst;
    lst = lst.substring(0, lst.lastIndexOf(","));

    DT += "knime.out <- list(" + lst + ")\n";
    DT += "knime.out <- setDT(lapply(knime.out,\"length<-\",max(length(knime.out))))[]";

    // lst[lst.lastIndexOf(",")] = ")";
    controller.eval(DT, false);
    BufferedDataTable out = controller.importBufferedDataTable("knime.out", false, exec);

    // ----------------CONVERT KNIME TABLE BACK TO R

    // controller.exportFlowVariables(inFlowVariables, name, exec);
    // controller.importDataFromPorts(inData, exec, batchSize, rType, sendRowNames);
    controller.monitoredAssign("knime.in", out, exec, 100, "String", true);
    // monitoredAssign("knime.in", out, exec.createSubProgress(0.5),batchSize, rType, true);

  }

  @Override
  public void installLibs(final FskPortObject fskObj, ExecutionContext exec, NodeLogger LOGGER)
      throws Exception {
    // Install needed libraries
    if (!fskObj.packages.isEmpty()) {
      LibRegistry.instance().install(fskObj.packages);
    }

    exec.setProgress(0.71, "Add paths to libraries");
    controller.addPackagePath(LibRegistry.instance().getInstallationPath());
  }

  @Override
  public String buildParameterScript(final FskSimulation simulation) {
    return NodeUtils.buildParameterScript(simulation);
  }

  @Override
  public void plotToImageFile(final RunnerNodeInternalSettings internalSettings,
      RunnerNodeSettings nodeSettings, final FskPortObject fskObj, ExecutionContext exec)
      throws Exception {
    plotter.plotPng(internalSettings.imageFile, fskObj.getViz());
  }

  @Override
  public void saveWorkspace(final FskPortObject fskObj, ExecutionContext exec) throws Exception {
    if (fskObj.getWorkspace() == null) {
      fskObj.setWorkspace(FileUtil.createTempFile("workspace", ".RData").toPath());
    }
    controller.saveWorkspace(fskObj.getWorkspace(), exec);
  }

  @Override
  public void restoreDefaultLibrary() throws Exception {
    controller.restorePackagePath();
  }

  @Override
  public String getStdOut() {
    return executor.getStdOut();
  }

  @Override
  public String getStdErr() {
    return executor.getStdErr();
  }

  public void cleanup(ExecutionContext exec) throws Exception {
    executor.cleanup(exec);
  }

  @Override
  public void setupOutputCapturing(ExecutionContext exec) throws Exception {
    executor.setupOutputCapturing(exec);
  }

  @Override
  public void finishOutputCapturing(ExecutionContext exec) throws Exception {
    executor.finishOutputCapturing(exec);
  }

  @Override
  public String getPackageVersionCommand(String pkg_name) {
    String command = "packageDescription(\"" + pkg_name + "\")$Version";
    return command;
  }

  @Override
  public String getPackageVersionCommand(List<String> pkg_names) {
    String command =
        "available.packages(contriburl = contrib.url(c(\"https://cloud.r-project.org/\"), \"both\"))[c('"
            + pkg_names.stream().collect(Collectors.joining("','")) + "'),]";
    return command;
  }

  @Override
  public String getFileExtension() {
    return "r";
  }

  @Override
  public void close() throws Exception {
    RprofileManager.unSubscribe();
    controller.close();

  }
  
  @Override
  protected String createVectorQuery(List<String> variableNames) {
    return "c(" +  String.join(", ", variableNames) + ")";
  }
}
