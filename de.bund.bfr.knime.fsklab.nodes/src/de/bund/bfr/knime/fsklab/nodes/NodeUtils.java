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
import de.bund.bfr.knime.fsklab.nodes.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
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

  /** Initialize and return a model for the passed type. */
  public static Model initializeModel(ModelType modelType) {

    if (modelType == null)
      return new GenericModel().modelType("genericModel");

    switch (modelType) {
      case genericModel:
        return new GenericModel().modelType("genericModel");
      case dataModel:
        return new DataModel().modelType("dataModel");
      case consumptionModel:
        return new ConsumptionModel().modelType("consumptionModel");
      case doseResponseModel:
        return new DoseResponseModel().modelType("doseResponseModel");
      case exposureModel:
        return new ExposureModel().modelType("exposureModel");
      case healthModel:
        return new HealthModel().modelType("healthModel");
      case otherModel:
        return new OtherModel().modelType("otherModel");
      case predictiveModel:
        return new PredictiveModel().modelType("predictiveModel");
      case processModel:
        return new ProcessModel().modelType("processModel");
      case qraModel:
        return new QraModel().modelType("qraModel");
      case riskModel:
        return new RiskModel().modelType("riskModel");
      case toxicologicalModel:
        return new ToxicologicalModel().modelType("toxicologicalModel");
      default:
        return new GenericModel().modelType("genericModel");
    }
  }
}
