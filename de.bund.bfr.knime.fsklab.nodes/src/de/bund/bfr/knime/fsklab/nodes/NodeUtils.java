package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.NodeContext;
import org.rosuda.REngine.REXPMismatchException;
import com.sun.jna.Platform;
import de.bund.bfr.fskml.FSKML;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.bund.bfr.knime.fsklab.r.client.RController;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;

public class NodeUtils {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(NodeUtils.class);
  public static final String DEFAULT_SIMULATION = "defaultSimulation";

  /**
   * @return the libraries URI for the running platform.
   * @throws InvalidSettingsException if the running platform is not supported.
   */
  public static URI getLibURI() throws InvalidSettingsException {
    if (Platform.isWindows()) {
      return FSKML.getURIS(1, 0, 12).get("zip");
    }
    if (Platform.isMac()) {
      return FSKML.getURIS(1, 0, 12).get("tgz");
    }
    if (Platform.isLinux()) {
      return FSKML.getURIS(1, 0, 12).get("tar_gz");
    }
    throw new InvalidSettingsException("Unsupported platform");
  }

  /**
   * Plots model results and generates a chart using a visualization script.
   *
   * @param imgFile Chart file
   * @param vizScript Visualization script
   * @param executor R executor
   * @param monitor KNIME {@link ExecutionMonitor}
   * @throws InterruptedException
   * @throws CanceledExecutionException
   * @throws RException
   *
   */
  public static void plot(final File imageFile, final String vizScript, final int width,
      final int height, final int pointSize, final String res, final ScriptExecutor executor,
      final ExecutionMonitor monitor)
      throws RException, CanceledExecutionException, InterruptedException {

    // Initialize necessary R stuff to plot
    if (Platform.isMac()) {
      executor.executeIgnoreResult("library('Cairo')", monitor);
      executor.executeIgnoreResult("options(device='png', bitmapType='cairo')", monitor);
    } else {
      executor.executeIgnoreResult("options(device='png')", monitor);
    }

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(imageFile.getAbsolutePath());

    // Gets values
    final String pngCommand = "png('" + path + "', width=" + width + ", height=" + height + ", pointsize="
        + pointSize + ", res='" + res + "')";

    executor.executeIgnoreResult(pngCommand, monitor);
    executor.executeIgnoreResult(vizScript, monitor);
    executor.executeIgnoreResult("dev.off()", monitor);
  }

  public static void runSnippet(final RController controller, final ScriptExecutor executor,
      final String modelScript, final String paramScript, final String vizScript,
      final ExecutionMonitor monitor, final File imageFile, final RunnerNodeSettings settings)
      throws RException, CanceledExecutionException, InterruptedException, IOException,
      REXPMismatchException {

    monitor.setMessage("Setting up output capturing");
    executor.setupOutputCapturing(monitor);

    monitor.setMessage("Add paths to libraries");
    controller.addPackagePath(LibRegistry.instance().getInstallationPath());

    monitor.setMessage("Run parameters script");
    executor.executeIgnoreResult(paramScript, monitor);

    monitor.setMessage("Run model script");
    executor.executeIgnoreResult(modelScript, monitor);

    monitor.setMessage("Run visualization script");
    try {
      NodeUtils.plot(imageFile, vizScript, settings.width, settings.height, settings.pointSize,
          settings.res, executor, monitor);
    } catch (final RException exception) {
      LOGGER.warn("Visualization script failed", exception);
    }

    monitor.setMessage("Restore library paths");
    controller.restorePackagePath();

    monitor.setMessage("Collecting captured output");
    executor.finishOutputCapturing(monitor);
  }

  public static FskSimulation createDefaultSimulation(List<Parameter> parameters) {

    final FskSimulation simulation = new FskSimulation(DEFAULT_SIMULATION);

    // The parameters to be inserted in order
    parameters.stream().filter(p -> p.getClassification() != ClassificationEnum.OUTPUT)
        .forEach(p -> simulation.getParameters().put(p.getId(), p.getValue()));

    return simulation;
  }

  /** Builds string with R parameters script out. */
  public static String buildParameterScript(FskSimulation simulation) {

    final StringBuilder builder = new StringBuilder();
    for (final Map.Entry<String, String> entry : simulation.getParameters().entrySet()) {
      final String parameterName = entry.getKey();
      final String parameterValue = entry.getValue();

      builder.append(parameterName + " <- " + parameterValue + "\n");
    }

    return builder.toString();
  }

  /**
   * Read a configuration string from a file under a settings folder.
   *
   * @throws IOException
   */
  static String readConfigString(File settingsFolder, String filename) throws IOException {
    FlowVariable aFlowVariable = null;
    if (NodeContext.getContext().getNodeContainer().getFlowObjectStack() != null) {
      aFlowVariable = NodeContext.getContext().getNodeContainer().getFlowObjectStack()
          .getAvailableFlowVariables().get(filename);
    }
    if (aFlowVariable != null) {
      return aFlowVariable.getStringValue();
    } else {
      final File configFile = new File(settingsFolder, filename);
      return configFile.exists() ? FileUtils.readFileToString(configFile, StandardCharsets.UTF_8)
          : null;
    }
  }

  /**
   * Write a configuration string to a file under a settings folder
   *
   * @throws IOException
   */
  static void writeConfigString(String configString, File settingsFolder, String filename)
      throws IOException {
    if (configString != null) {
      final File configFile = new File(settingsFolder, filename);
      FileUtils.writeStringToFile(configFile, configString, StandardCharsets.UTF_8);
    }
  }
}
