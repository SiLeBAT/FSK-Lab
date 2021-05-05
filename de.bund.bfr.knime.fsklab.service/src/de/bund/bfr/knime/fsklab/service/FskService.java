package de.bund.bfr.knime.fsklab.service;

import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.eclipse.core.runtime.Platform;
import org.h2.tools.DeleteDbFiles;
import org.knime.core.node.NodeLogger;
import org.osgi.framework.Bundle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;

import de.bund.bfr.metadata.swagger.Model;
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
import metadata.ConversionUtils;
import metadata.ConversionUtils.ModelClass;
import spark.ResponseTransformer;

public class FskService implements Runnable {

	private static final String MIME_JSON = "application/json";

	private static final NodeLogger LOGGER = NodeLogger.getLogger(FskService.class);

	private static final JsonTransformer jsonTransformer = new JsonTransformer();

	private final ConversionUtils utils = new ConversionUtils();

	private int port;
	
	private static FskService service = new FskService();

	private FskService() {
	}

	public static FskService instance() {
		return service;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {

		try {
			initDatabase();
		} catch (ClassNotFoundException err) {
			LOGGER.error("Initializing DB", err);
			return;
		} catch (SQLException | IOException err) {
			// These two exceptions are already logged in initDatabase
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
				res.type(MIME_JSON);
				BasicRepository<?> repository = getRepository(req.params(":vocabulary"), connection);
				int id = Integer.parseInt(req.params(":id"));
				return repository.getById(id);
			}
		}, jsonTransformer);

		get("/getAll/:vocabulary", (req, res) -> {
			try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {
				res.type(MIME_JSON);
				BasicRepository<?> repository = getRepository(req.params(":vocabulary"), connection);
				return repository.getAll();
			}
		}, jsonTransformer);

		get("/getAllNames/:vocabulary", (req, res) -> {
			try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies")) {
				res.type(MIME_JSON);
				BasicRepository<?> repository = getRepository(req.params(":vocabulary"), connection);
				return repository.getAllNames();
			}
		}, jsonTransformer);

		// input metadata as body parameter
		post("convertMetadata/:targetModelClass", (req, res) -> {
			res.type("application/json");

			try {
				JsonNode inputMetadata = jsonTransformer.MAPPER.readTree(req.body());
				ModelClass targetClass = ModelClass.valueOf(req.params("targetModelClass"));
				Model convertedMetadata = utils.convertModel(inputMetadata, targetClass);
				res.status(200);
				return convertedMetadata;
			} catch (Exception err) {
				res.status(400);
				return err;
			}
		}, jsonTransformer);

		post("joinMetadata", (req, res) -> {
			// The body keeps two JSON models in an array.
			res.type("application/json");

			try {
				JsonNode models = jsonTransformer.MAPPER.readTree(req.body());
				JsonNode firstModel = models.get(0);
				JsonNode secondModel = models.get(1);
				Model joinedModel = utils.joinModels(firstModel, secondModel, ModelClass.genericModel);
				res.status(200);
				return joinedModel;
			} catch (Exception err) {
				res.status(500);
				return err;
			}
		}, jsonTransformer);

		// After initializing the service, get the randomly picked port by Spark.
		awaitInitialization();
		port = port();
	}

	private static class JsonTransformer implements ResponseTransformer {

		ObjectMapper MAPPER = new ObjectMapper().registerModule(new ThreeTenModule())
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		@Override
		public String render(Object model) {
			try {
			    return MAPPER.writeValueAsString(model);
			} catch (JsonProcessingException err) {
				return "";
			}
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
	 * Import database from SQL files in bundle to local DB in ~/.fsk/vocabularies.
	 * <p>
	 * Import stops if the database could not be created. Individual tables can fail
	 * and the import will continue with the rest of tables.
	 * 
	 * @throws SQLException If connection to local DB could not be opened.
	 * @throws ClassNotFoundException If H2 DB driver is missing.
	 * @throws IOException If the database or its structure could not be created.
	 */
	private void initDatabase() throws SQLException, ClassNotFoundException, IOException {

		final Properties fastImportProperties = new Properties();
		fastImportProperties.put("LOG", 0);
		fastImportProperties.put("CACHE_SIZE", 65536);
		fastImportProperties.put("LOCK_MODE", 0);
		fastImportProperties.put("UNDO_LOG", 0);

		Class.forName("org.h2.Driver");
		DeleteDbFiles.execute("~/.fsk", "vocabularies", true); // Delete DB if it exists
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies",
				fastImportProperties)) {
			
			// Load tables
			Bundle bundle = Platform.getBundle("de.bund.bfr.knime.fsklab.service");
			
			File temporaryFile = File.createTempFile("vocabularies", ".jar");
			URL bundleUrl = bundle.getEntry("/vocabularies-2.0.2.jar");
			
			try {
				// Extract vocabularies jar from bundle to a temporary file
				try (InputStream inputStream = bundleUrl.openStream()) {
					FileUtils.copyToFile(inputStream, temporaryFile);
				}
				
				try (JarFile jarFile = new JarFile(temporaryFile)) {
										
					// Read tables.sql
					String tableSqlScript;
					JarEntry tablesEntry = jarFile.getJarEntry("data/tables.sql");
					try (InputStream inputStream = jarFile.getInputStream(tablesEntry)) {
						tableSqlScript = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					} catch (IOException err) {
						LOGGER.error("RAKIP vocabularies could not be imported", err);
						throw err;
					}
					
					// Import tables.sql
					try (Statement statement = connection.createStatement()) {
						statement.execute(tableSqlScript);
					} catch (SQLException err) {
						LOGGER.error("RAKIP vocabularies could not be imported", err);
						throw err;
					}
					
					// Get and import SQL data files
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()) {
						JarEntry nextEntry = entries.nextElement();
						
						if (nextEntry.getName().startsWith("data/initialdata/") && nextEntry.getName().endsWith(".sql")) {
							try (InputStream is = jarFile.getInputStream(nextEntry);
									LineIterator lineIterator = IOUtils.lineIterator(is, StandardCharsets.UTF_8)) {
								while (lineIterator.hasNext()) {
									try (Statement statement = connection.createStatement()) {
										statement.execute(lineIterator.nextLine());
									}
								}
							} catch (IOException | SQLException err) {
								// Log error and continue with other vocabularies
								LOGGER.warn("SQL file could not be imported: " + nextEntry.getName(), err);
							}
						}
					}
				}
				
			} finally {
				if (temporaryFile.exists()) {
					temporaryFile.delete();
				}
			}
		}
	}
}
