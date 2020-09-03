package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.workflow.FlowVariable;
import org.knime.core.node.workflow.NodeContext;
import com.sun.jna.Platform;
import de.bund.bfr.fskml.FSKML;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;

public class NodeUtils {

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
