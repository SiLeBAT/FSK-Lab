package de.bund.bfr.knime.fsklab.nodes;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.io.FilenameUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.rosuda.REngine.REXPMismatchException;
import com.sun.jna.Platform;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.nodes.controller.ConsoleLikeRExecutor;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;

public class NodeUtils {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(NodeUtils.class);

  /**
   * @return the libraries URI for the running platform.
   * @throws InvalidSettingsException if the running platform is not supported.
   */
  public static URI getLibURI() throws InvalidSettingsException {
    if (Platform.isWindows())
      return URIS.zip;
    if (Platform.isMac())
      return URIS.tgz;
    if (Platform.isLinux())
      return URIS.tar_gz;
    throw new InvalidSettingsException("Unsupported platform");
  }

  /**
   * Plots models results and generates a chart using a visualization script.
   * 
   * @param imageFile Chart file
   * @param vizScript Visualization script
   * @param settings Visualization settings
   * @param executor R executor
   * @param monitor KNIME {@link ExecutionMonitor}
   * @throws RException
   * @throws CanceledExecutionException
   * @throws InterruptedException
   */
  public static void plot(final File imageFile, final String vizScript,
      final RunnerNodeSettings settings, final ConsoleLikeRExecutor executor,
      final ExecutionMonitor monitor)
      throws RException, CanceledExecutionException, InterruptedException {

    // initialize necessary R stuff to plot
    if (Platform.isMac()) {
      executor.executeIgnoreResult("library('Cairo')", monitor);
      executor.executeIgnoreResult("options(device='png', bitmapType='cairo')", monitor);
    } else {
      executor.executeIgnoreResult("options(device='png')", monitor);
    }

    // Gets image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(imageFile.getAbsolutePath());

    // Gets values
    int width = settings.widthModel.getIntValue();
    int height = settings.heightModel.getIntValue();
    String res = settings.resolutionModel.getStringValue();
    int textPointSize = settings.textPointSizeModel.getIntValue();
    Color colour = settings.colourModel.getColorValue();
    String hexColour =
        String.format("#%02x%02x%02x", colour.getRed(), colour.getGreen(), colour.getBlue());

    String pngCommand = "png('" + path + "', width=" + width + ", height=" + height + ", pointsize="
        + textPointSize + ", bg='" + hexColour + "', res='" + res + "')";

    executor.executeIgnoreResult(pngCommand, monitor);
    executor.executeIgnoreResult(vizScript, monitor);
    executor.executeIgnoreResult("dev.off()", monitor);
  }

  public static void runSnippet(final RController controller, final ConsoleLikeRExecutor executor,
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
      NodeUtils.plot(imageFile, vizScript, settings, executor, monitor);
    } catch (final RException exception) {
      LOGGER.warn("Visualization script failed", exception);
    }

    monitor.setMessage("Restore library paths");
    controller.restorePackagePath();

    monitor.setMessage("Collecting captured output");
    executor.finishOutputCapturing(monitor);
  }
}
