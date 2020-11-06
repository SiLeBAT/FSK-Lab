package de.bund.bfr.knime.fsklab.service;

import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.options;
import static spark.Spark.port;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.h2.tools.DeleteDbFiles;
import org.knime.core.node.NodeLogger;
import org.osgi.framework.Bundle;
import com.google.gson.Gson;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.ConsumptionModelScope;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DataModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DataModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DoseResponseModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModelScope;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.HealthModelScope;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.OtherModelDataBackground;
import de.bund.bfr.metadata.swagger.OtherModelGeneralInformation;
import de.bund.bfr.metadata.swagger.OtherModelModelMath;
import de.bund.bfr.metadata.swagger.OtherModelScope;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.PredictiveModelScope;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.ProcessModelScope;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModelScope;
import de.bund.bfr.rakip.vocabularies.data.AccreditationProcedureRepository;
import de.bund.bfr.rakip.vocabularies.data.AvailabilityRepository;
import de.bund.bfr.rakip.vocabularies.data.BasicProcessRepository;
import de.bund.bfr.rakip.vocabularies.data.BasicRepository;
import de.bund.bfr.rakip.vocabularies.data.CollectionToolRepository;
import de.bund.bfr.rakip.vocabularies.data.CountryRepository;
import de.bund.bfr.rakip.vocabularies.data.FishAreaRepository;
import de.bund.bfr.rakip.vocabularies.data.FormatRepository;
import de.bund.bfr.rakip.vocabularies.data.HazardRepository;
import de.bund.bfr.rakip.vocabularies.data.HazardTypeRepository;
import de.bund.bfr.rakip.vocabularies.data.IndSumRepository;
import de.bund.bfr.rakip.vocabularies.data.LaboratoryAccreditationRepository;
import de.bund.bfr.rakip.vocabularies.data.LanguageRepository;
import de.bund.bfr.rakip.vocabularies.data.LanguageWrittenInRepository;
import de.bund.bfr.rakip.vocabularies.data.ModelClassRepository;
import de.bund.bfr.rakip.vocabularies.data.ModelEquationClassRepository;
import de.bund.bfr.rakip.vocabularies.data.ModelSubclassRepository;
import de.bund.bfr.rakip.vocabularies.data.PackagingRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterClassificationRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterDatatypeRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterDistributionRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterSourceRepository;
import de.bund.bfr.rakip.vocabularies.data.ParameterSubjectRepository;
import de.bund.bfr.rakip.vocabularies.data.PopulationRepository;
import de.bund.bfr.rakip.vocabularies.data.ProductMatrixRepository;
import de.bund.bfr.rakip.vocabularies.data.ProductTreatmentRepository;
import de.bund.bfr.rakip.vocabularies.data.ProductionMethodRepository;
import de.bund.bfr.rakip.vocabularies.data.PublicationStatusRepository;
import de.bund.bfr.rakip.vocabularies.data.PublicationTypeRepository;
import de.bund.bfr.rakip.vocabularies.data.RegionRepository;
import de.bund.bfr.rakip.vocabularies.data.RightRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingMethodRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingPointRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingProgramRepository;
import de.bund.bfr.rakip.vocabularies.data.SamplingStrategyRepository;
import de.bund.bfr.rakip.vocabularies.data.SoftwareRepository;
import de.bund.bfr.rakip.vocabularies.data.SourceRepository;
import de.bund.bfr.rakip.vocabularies.data.StatusRepository;
import de.bund.bfr.rakip.vocabularies.data.TechnologyTypeRepository;
import de.bund.bfr.rakip.vocabularies.data.UnitCategoryRepository;
import de.bund.bfr.rakip.vocabularies.data.UnitRepository;
import spark.ResponseTransformer;

public class FskService implements Runnable {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FskService.class);

  private static final JsonTransformer jsonTransformer = new JsonTransformer();

  private int port;

  public int getPort() {
    return port;
  }

  @Override
  public void run() {

    try {
      initDatabase();
    } catch (SQLException | ClassNotFoundException e1) {
      LOGGER.error("Initializing DB", e1);
      return;
    }

    port(0);

    // Enable CORS
    options("/*", (request, response) -> {

      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    get("getById/:vocabulary/:id", (req, res) -> {
      try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {
        res.type("application/json");
        BasicRepository<?> repository = getRepository(req.params(":vocabulary"), connection);
        int id = Integer.parseInt(req.params(":id"));
        return repository.getById(id);
      }
    }, jsonTransformer);

    get("/getAll/:vocabulary", (req, res) -> {
      try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {
        res.type("application/json");
        BasicRepository<?> repository = getRepository(req.params(":vocabulary"), connection);
        return repository.getAll();
      }
    }, jsonTransformer);

    get("/getAllNames/:vocabulary", (req, res) -> {
      try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {
        res.type("application/json");
        BasicRepository<?> repository = getRepository(req.params(":vocabulary"), connection);
        return repository.getAllNames();
      }
    }, jsonTransformer);

    // input metadata as body parameter
    get("convertMetadata/:targetModelClass", (req, res) -> {
      String inputMetadata = req.body();
      // System.out.println(inputMetadata);

      res.type("application/json");
      res.status(200);

      return convertMetadata(inputMetadata, req.params(":targetModelClass"));
    }, jsonTransformer);
    
    post("joinMetadata", (req, res) -> {
      // String body = req.body();
      // Do nothing with body yet
      // The body keeps two JSON models in an array.
      
      res.type("application/json");
      res.status(200);
      
      return new GenericModel();
    }, jsonTransformer);

    // After initializing the service, get the randomly picked port by Spark.
    awaitInitialization();
    port = port();
  }

  private static class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
      return gson.toJson(model);
    }
  }

  private BasicRepository<?> getRepository(String vocabulary, Connection connection) {
    switch (vocabulary) {
      case "accreditation_procedure":
        return new AccreditationProcedureRepository(connection);
      case "availability":
        return new AvailabilityRepository(connection);
      case "basic_process":
        return new BasicProcessRepository(connection);
      case "collection_tool":
        return new CollectionToolRepository(connection);
      case "country":
        return new CountryRepository(connection);
      case "fish_area":
        return new FishAreaRepository(connection);
      case "format":
        return new FormatRepository(connection);
      case "hazard":
        return new HazardRepository(connection);
      case "hazard_type":
        return new HazardTypeRepository(connection);
      case "ind_sum":
        return new IndSumRepository(connection);
      case "laboratory_accreditation":
        return new LaboratoryAccreditationRepository(connection);
      case "language":
        return new LanguageRepository(connection);
      case "language_written_in":
        return new LanguageWrittenInRepository(connection);
      case "model_class":
        return new ModelClassRepository(connection);
      case "model_equation_class":
        return new ModelEquationClassRepository(connection);
      case "model_subclass":
        return new ModelSubclassRepository(connection);
      case "packaging":
        return new PackagingRepository(connection);
      case "parameter_classification":
        return new ParameterClassificationRepository(connection);
      case "parameter_datatype":
        return new ParameterDatatypeRepository(connection);
      case "parameter_distribution":
        return new ParameterDistributionRepository(connection);
      case "parameter_source":
        return new ParameterSourceRepository(connection);
      case "parameter_subject":
        return new ParameterSubjectRepository(connection);
      case "population":
        return new PopulationRepository(connection);
      case "product_matrix":
        return new ProductMatrixRepository(connection);
      case "product_treatment":
        return new ProductTreatmentRepository(connection);
      case "production_method":
        return new ProductionMethodRepository(connection);
      case "publication_status":
        return new PublicationStatusRepository(connection);
      case "publication_type":
        return new PublicationTypeRepository(connection);
      case "region":
        return new RegionRepository(connection);
      case "right":
        return new RightRepository(connection);
      case "sampling_method":
        return new SamplingMethodRepository(connection);
      case "sampling_point":
        return new SamplingPointRepository(connection);
      case "sampling_program":
        return new SamplingProgramRepository(connection);
      case "sampling_strategy":
        return new SamplingStrategyRepository(connection);
      case "software":
        return new SoftwareRepository(connection);
      case "source":
        return new SourceRepository(connection);
      case "status":
        return new StatusRepository(connection);
      case "unit":
        return new UnitRepository(connection);
      case "unit_category":
        return new UnitCategoryRepository(connection);
      case "technology_type":
        return new TechnologyTypeRepository(connection);
      default:
        break;
    }

    return null;
  }

  /**
   * Convert model metadata to a different model class.
   * 
   * @param originalMetadata Original metadata
   * @param targetModelClass String representation of the target model class.
   * @return Model or null if targetModelClass is not valid
   */
  private Model convertMetadata(String originalMetadata, String targetModelClass) {
    switch (targetModelClass) {
      case "genericModel":
        return new GenericModel()
            .generalInformation(new GenericModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Generic model")))
            .scope(new GenericModelScope()).dataBackground(new GenericModelDataBackground())
            .modelMath(new GenericModelModelMath()).modelType("genericModel");
      case "dataModel":
        return new DataModel()
            .generalInformation(new DataModelGeneralInformation())
            .scope(new GenericModelScope())
            .dataBackground(new GenericModelDataBackground())
            .modelMath(new DataModelModelMath())
            .modelType("dataModel");
      case "consumptionModel":
        return new ConsumptionModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Consumption model")))
            .scope(new ConsumptionModelScope()).dataBackground(new GenericModelDataBackground())
            .modelMath(new PredictiveModelModelMath()).modelType("consumptionModel");
      case "doseResponseModel":
        return new DoseResponseModel()
            .generalInformation(new DoseResponseModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Dose-response model")))
            .scope(new DoseResponseModelScope())
            .dataBackground(new PredictiveModelDataBackground())
            .modelMath(new DoseResponseModelModelMath())
            .modelType("doseResponseModel");
      case "exposureModel":
        return new ExposureModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Exposure model")))
            .scope(new ExposureModelScope())
            .dataBackground(new GenericModelDataBackground())
            .modelMath(new GenericModelModelMath())
            .modelType("exposureModel");
      case "healthModel":
        return new HealthModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Health metrics model")))
            .scope(new HealthModelScope())
            .dataBackground(new PredictiveModelDataBackground())
            .modelMath(new GenericModelModelMath())
            .modelType("healthModel");
      case "otherModel":
        return new OtherModel()
            .generalInformation(new OtherModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Other empirical model")))
            .scope(new OtherModelScope())
            .dataBackground(new OtherModelDataBackground())
            .modelMath(new OtherModelModelMath())
            .modelType("otherModel");
      case "predictiveModel":
        return new PredictiveModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Predictive model")))
            .scope(new PredictiveModelScope())
            .dataBackground(new PredictiveModelDataBackground())
            .modelMath(new PredictiveModelModelMath())
            .modelType("predictiveModel");
      case "processModel":
        return new ProcessModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Process model")))
            .scope(new ProcessModelScope())
            .dataBackground(new PredictiveModelDataBackground())
            .modelMath(new PredictiveModelModelMath())
            .modelType("processModel");
      case "qraModel":
        return new QraModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Quantitative risk assessment")))
            .scope(new ExposureModelScope())
            .dataBackground(new GenericModelDataBackground())
            .modelMath(new GenericModelModelMath())
            .modelType("qraModel");
      case "riskModel":
        return new RiskModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Risk characterization model")))
            .scope(new ExposureModelScope())
            .dataBackground(new GenericModelDataBackground())
            .modelMath(new GenericModelModelMath())
            .modelType("riskModel");
      case "toxicologicalModel":
        return new ToxicologicalModel()
            .generalInformation(new PredictiveModelGeneralInformation()
                .modelCategory(new ModelCategory().modelClass("Toxicological reference value")))
            .scope(new ToxicologicalModelScope())
            .dataBackground(new PredictiveModelDataBackground())
            .modelMath(new GenericModelModelMath())
            .modelType("toxicologicalModel");
      default:
        return null;
    }
  }

  private void initDatabase() throws SQLException, ClassNotFoundException {

    final Properties fastImportProperties = new Properties();
    fastImportProperties.put("LOG", 0);
    fastImportProperties.put("CACHE_SIZE", 65536);
    fastImportProperties.put("LOCK_MODE", 0);
    fastImportProperties.put("UNDO_LOG", 0);

    Class.forName("org.h2.Driver");
    DeleteDbFiles.execute("~/.fsk", "vocabularies", true); // Delete DB if it exists
    final Connection initialConnection =
        DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies", fastImportProperties);

    // Load tables
    Bundle bundle = Platform.getBundle("de.bund.bfr.knime.fsklab.service");

    try {
      File file = getResource(bundle, "data/tables.sql");
      String script = FileUtils.readFileToString(file, "UTF-8");

      Statement statement = initialConnection.createStatement();
      statement.execute(script);
    } catch (IOException | SQLException | URISyntaxException e) {
      e.printStackTrace();
      LOGGER.error("Fail to create DB", e);
    }

    // Insert data
    List<String> filenames = Arrays.asList("accreditation_procedure.sql", "availability.sql",
        "collection_tool.sql", "country.sql", "fish_area.sql", "format.sql", "hazard_type.sql",
        "hazard.sql", "ind_sum.sql", "laboratory_accreditation.sql", "language_written_in.sql",
        "language.sql", "model_class.sql", "model_equation_class.sql", "packaging.sql",
        "parameter_classification.sql", "parameter_datatype.sql", "parameter_distribution.sql",
        "parameter_source.sql", "parameter_subject.sql", "population.sql", "prodmeth.sql",
        "prodTreat.sql", "product_matrix.sql", "publication_status.sql", "publication_type.sql",
        "region.sql", "rights.sql", "sampling_method.sql", "sampling_point.sql",
        "sampling_program.sql", "sampling_strategy.sql", "software.sql", "sources.sql",
        "status.sql", "technology_type.sql", "unit.sql");

    for (String filename : filenames) {

      try {
        Statement statement = initialConnection.createStatement();

        File file = getResource(bundle, "data/initialdata/" + filename);
        for (String line : FileUtils.readLines(file, "UTF-8")) {
          statement.execute(line);
        }
      } catch (IOException | SQLException | URISyntaxException e) {
        e.printStackTrace();
      }
    }

    initialConnection.close();
  }

  private static File getResource(final Bundle bundle, final String path)
      throws IOException, URISyntaxException {
    URL fileURL = bundle.getEntry(path);
    URL resolvedFileURL = FileLocator.toFileURL(fileURL);
    URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
    return new File(resolvedURI);
  }
}
