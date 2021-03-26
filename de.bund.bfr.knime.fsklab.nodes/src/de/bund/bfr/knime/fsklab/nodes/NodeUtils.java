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
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.editor.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.Exposure;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.OtherModelGeneralInformation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;

public class NodeUtils {

  public static final String DEFAULT_SIMULATION = "defaultSimulation";

  // used by Writer & Reader to identify Join-Relations in the SBML File
  public static final String METADATA_COMMAND_VALUE = "commandValue";

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
  public static String readConfigString(File settingsFolder, String filename) throws IOException {
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
  public static void writeConfigString(String configString, File settingsFolder, String filename)
      throws IOException {
    if (configString != null) {
      final File configFile = new File(settingsFolder, filename);
      FileUtils.writeStringToFile(configFile, configString, StandardCharsets.UTF_8);
    }
  }

  /** Initialize and return an empty Generic model using the fluent API of the generated Swagger Models */
  public static Model initializeGenericModell() {
      return new GenericModel()
          .generalInformation(new GenericModelGeneralInformation()
              .modelCategory(new ModelCategory().modelClass("Generic model"))
              .addAuthorItem(new Contact())
              .addCreatorItem(new Contact())
              .addReferenceItem(new Reference()))
          .dataBackground(new GenericModelDataBackground()
              .study(new Study())
              .addAssayItem(new Assay())
              .addDietaryAssessmentMethodItem(new DietaryAssessmentMethod())
              .addLaboratoryItem(new Laboratory())
              .addStudySampleItem(new StudySample()))
          .scope(new GenericModelScope()
              .addHazardItem(new Hazard())
              .addPopulationGroupItem(new PopulationGroup())
              .addProductItem(new Product())
              .addSpatialInformationItem(""))
          .modelMath(new GenericModelModelMath()
              .addEventItem("")
              .addExposureItem(new Exposure())
              .addModelEquationItem(new ModelEquation())
              .addParameterItem(new Parameter())
              .addQualityMeasuresItem(new QualityMeasures()))
          .modelType("genericModel");
  }

  /** Initialize and return a model for the passed type. */
  public static Model initializeModel(ModelType modelType) {

    if (modelType == null)
      return new GenericModel()
          .generalInformation(new GenericModelGeneralInformation()
              .modelCategory(new ModelCategory().modelClass("Generic model")))
          .modelType("genericModel");

    switch (modelType) {
      case genericModel:
        return new GenericModel().modelMath(new GenericModelModelMath())
            .generalInformation(new GenericModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Generic model")))
            .modelType("genericModel");
      case dataModel:
        return new DataModel().modelType("dataModel");
      case consumptionModel:
        return new ConsumptionModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Consumption model")))
            .modelType("consumptionModel");
      case doseResponseModel:
        return new DoseResponseModel()
            .generalInformation(new DoseResponseModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Dose-response model")))
            .modelType("doseResponseModel");
      case exposureModel:
        return new ExposureModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Exposure model")))
            .modelType("exposureModel");
      case healthModel:
        return new HealthModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Health metrics model")))
            .modelType("healthModel");
      case otherModel:
        return new OtherModel()
            .generalInformation(new OtherModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Other empirical models")))
            .modelType("otherModel");
      case predictiveModel:
        return new PredictiveModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Predictive model")))
            .modelType("predictiveModel");
      case processModel:
        return new ProcessModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Process model")))
            .modelType("processModel");
      case qraModel:
        return new QraModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Quantitative risk assessment")))
            .modelType("qraModel");
      case riskModel:
        return new RiskModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Risk characterization model")))
            .modelType("riskModel");
      case toxicologicalModel:
        return new ToxicologicalModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Toxicological reference value")))
            .modelType("toxicologicalModel");
      default:
        return new GenericModel()
            .generalInformation(new GenericModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Generic model")))
            .modelType("genericModel");
    }
  }
}
