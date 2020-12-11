package de.bund.bfr.knime.fsklab.nodes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import org.knime.python2.kernel.PythonKernel;
import org.knime.python2.kernel.PythonKernelOptions;
import de.bund.bfr.knime.fsklab.nodes.plot.PythonPlotter;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeSettings;

public class PythonScriptHandler extends ScriptHandler {
  String std_out = "";
  String std_err = "";
  // controller that communicates with Python Installation
  PythonKernel controller;

  
  public PythonScriptHandler() throws IOException {

    PythonKernelOptions m_kernelOptions = new PythonKernelOptions();
    controller = new PythonKernel(m_kernelOptions);
    
    // set up backend (rendering engine) for matplotlib for image handling:
    controller.execute("import matplotlib");
    controller.execute("matplotlib.use('Agg')");
    
    // Currently only PythonPlotter is assigned as it is the only available for Python
    this.plotter = new PythonPlotter(controller);
  }

  @Override
  public void convertToKnimeDataTable(FskPortObject fskObj, ExecutionContext exec) throws Exception {

  }

  @Override
  public String[] runScript(String script, ExecutionContext exec, Boolean showErrors)
      throws Exception {
    String[] output = controller.execute(script.replaceAll("<-", "="));
    if (!output[0].isEmpty())
      std_out += output[0] + "\n";
    if (!output[1].isEmpty())
      std_err += output[1] + "\n";
    return output[0].replaceAll("[\\[\\]\\'\\n]", "").split(","); // remove these characters: [ ' \n 
  }

  @Override
  public void installLibs(FskPortObject fskObj, ExecutionContext exec, NodeLogger LOGGER)
      throws Exception {
    // TODO Install neccessary packages
  }

  @Override
  public String buildParameterScript(FskSimulation simulation) {
    String paramScript = NodeUtils.buildParameterScript(simulation);
    paramScript = paramScript.replaceAll("<-", "=");
    return paramScript;
  }

  @Override
  public void plotToImageFile(RunnerNodeInternalSettings internalSettings, RunnerNodeSettings nodeSettings,
      FskPortObject fskObj, ExecutionContext exec) throws Exception {
    plotter.plotPng(internalSettings.imageFile, fskObj.getViz());
  }

  @Override
  public void restoreDefaultLibrary() throws Exception {
    // TODO remove previously installed packages and restore the library
    // to its default state
  }

  @Override
  public void saveWorkspace(FskPortObject fskObj, ExecutionContext exec) throws Exception {
    fskObj.setWorkspace(FileUtil.createTempFile("workspace", ".py").toPath());
  }

  @Override
  public String getStdOut() {

    return std_out;
  }

  @Override
  public String getStdErr() {
    return std_err;
  }

  @Override
  public void cleanup(ExecutionContext exec) throws Exception {

    std_out = "";
    std_err = "";
  }

  @Override
  public void setWorkingDirectory(Path workingDirectory, ExecutionContext exec) throws Exception {
    String cmd = "import os\n";
    String directory = workingDirectory.toString().replaceAll("\\\\", "/");

    cmd += "os.chdir('" + directory + "')";

    runScript(cmd, exec, false);
  }

  @Override
  public void setupOutputCapturing(ExecutionContext exec) throws Exception {
    // can be left empty
  }

  @Override
  public void finishOutputCapturing(ExecutionContext exec) throws Exception {
    // can be left empty
  }

  @Override
  public String getPackageVersionCommand(String pkg_name) {
    String command = pkg_name + ".__version__";
    return command;
  }

  @Override
  public String getPackageVersionCommand(List<String> pkg_names) {
    // TODO: find a way to get a list of all available packages
    String command = "";
    return command;
  }

  @Override
  public String getFileExtension() {
    return "py";
  }

  @Override
  public void close() throws Exception {
    controller.close();

  }
  
  @Override
  protected String createVectorQuery(List<String> variableNames) {
    return "print([" + String.join(",", variableNames) + "])";
  }
}
