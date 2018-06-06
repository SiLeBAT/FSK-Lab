package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.rosuda.REngine.REXPMismatchException;
import com.sun.jna.Platform;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.nodes.controller.ConsoleLikeRExecutor;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.LanguagePreferenceInitializer;
import metadata.Parameter;
import metadata.ParameterClassification;

public class NodeUtils {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(NodeUtils.class);
  public static final String DEFAULT_SIMULATION = "defaultSimulation";

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
      final int height, final int pointSize, final String res, final ConsoleLikeRExecutor executor,
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
    String pngCommand = "png('" + path + "', width=" + width + ", height=" + height + ", pointsize="
        + pointSize + ", res='" + res + "')";

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

  public static FskSimulation createDefaultSimulation(String parameterScript) {
    FskSimulation defaultSimulation = new FskSimulation(DEFAULT_SIMULATION);

    for (String line : parameterScript.split("\\r?\\n")) {
      if (line.startsWith("#") || StringUtils.isBlank(line)) {
        continue;
      }

      if (line.contains("<-")) {
        line = line.trim();

        String[] tokens = line.split("<-");
        String name = tokens[0].trim();
        String value = tokens[1].trim();

        // Remove comments from values in the parameters script
        int poundPos = value.indexOf("#");
        if (poundPos != -1) {
          value = value.substring(0, poundPos);
        }

        defaultSimulation.getParameters().put(name, value);
      } else if (line.contains("=")) {
        line = line.trim();

        String[] tokens = line.split("=");
        String name = tokens[0].trim();
        String value = tokens[1].trim();

        // Remove comments from values in the parameters script
        int poundPos = value.indexOf("#");
        if (poundPos != -1) {
          value = value.substring(0, poundPos);
        }

        defaultSimulation.getParameters().put(name, value);
      }
    }

    return defaultSimulation;
  }

  public static FskSimulation createDefaultSimulation(List<Parameter> parameters) {

    FskSimulation defaultSimulation = new FskSimulation(DEFAULT_SIMULATION);

    for (Parameter param : parameters) {

      if (param.getParameterClassification() == ParameterClassification.OUTPUT) {
        continue;
      }

      defaultSimulation.getParameters().put(param.getParameterID(), param.getParameterValue());
    }

    return defaultSimulation;
  }

  /** Builds string with R parameters script out. */
  public static String buildParameterScript(FskSimulation simulation) {

    String paramScript = "";
    for (Map.Entry<String, String> entry : simulation.getParameters().entrySet()) {
      String parameterName = entry.getKey();
      String parameterValue = entry.getValue();

      paramScript += parameterName + " <- " + parameterValue + "\n";
    }

    return paramScript;
  }

  /**
   * @return Locale for the selected language in the preferences or null if not set.
   */
  public static final Locale getLocale() {

    String languageCode = LanguagePreferenceInitializer.getLanguageProvider().getLanguage();
    Locale locale;
    if (languageCode.equals("de")) {
      locale = new Locale("de", "DE");
    } else if (languageCode.equals("es")) {
      locale = new Locale("es", "ES");
    } else if (languageCode.equals("en")) {
      locale = new Locale("en", "EN");
    } else {
      throw new RuntimeException("[NodeUtils] Illegal locale");
    }

    return locale;
  }
}
