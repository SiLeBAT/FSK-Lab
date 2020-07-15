package de.bund.bfr.knime.fsklab.service;

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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.h2.tools.DeleteDbFiles;
import org.knime.core.node.NodeLogger;
import org.osgi.framework.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bfr.rakip.vocabularies.data.AvailabilityRepository;
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
import de.bund.bfr.rakip.vocabularies.data.UnitCategoryRepository;
import de.bund.bfr.rakip.vocabularies.data.UnitRepository;

public class FskService implements Runnable {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(FskService.class);

	private Server server;

	public FskService() {
		server = new Server(8080);
	}

	public int getPort() {
		return server.getURI() != null ? server.getURI().getPort() : -1;
	}

	@Override
	public void run() {

		Connection connection;
		try {
			initDatabase();

			connection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies");
		} catch (SQLException | ClassNotFoundException e1) {
			LOGGER.error("Initializing DB", e1);
			return;
		}

		server.getConnectors()[0].getConnectionFactory(HttpConnectionFactory.class);

		HashMap<String, BasicRepository<?>> repositories = new HashMap<>();
		repositories.put("/api/availability", new AvailabilityRepository(connection));
		repositories.put("/api/collection_tool", new CollectionToolRepository(connection));
		repositories.put("/api/country", new CountryRepository(connection));
		repositories.put("/api/fish_area", new FishAreaRepository(connection));
		repositories.put("/api/format", new FormatRepository(connection));
		repositories.put("/api/hazard", new HazardRepository(connection));
		repositories.put("/api/hazard_type", new HazardTypeRepository(connection));
		repositories.put("/api/ind_sum", new IndSumRepository(connection));
		repositories.put("/api/laboratory_accreditation", new LaboratoryAccreditationRepository(connection));
		repositories.put("/api/language", new LanguageRepository(connection));
		repositories.put("/api/language_written_in", new LanguageWrittenInRepository(connection));
		repositories.put("/api/model_class", new ModelClassRepository(connection));
		repositories.put("/api/model_equation_class", new ModelEquationClassRepository(connection));
		repositories.put("/api/model_subclass", new ModelSubclassRepository(connection));
		repositories.put("/api/packaging", new PackagingRepository(connection));
		repositories.put("/api/parameter_distribution", new ParameterDistributionRepository(connection));
		repositories.put("/api/parameter_source", new ParameterSourceRepository(connection));
		repositories.put("/api/parameter_subject", new ParameterSubjectRepository(connection));
		repositories.put("/api/population", new PopulationRepository(connection));
		repositories.put("/api/product_matrix", new ProductMatrixRepository(connection));
		repositories.put("/api/product_treatment", new ProductTreatmentRepository(connection));
		repositories.put("/api/production_method", new ProductionMethodRepository(connection));
		repositories.put("/api/publication_status", new PublicationStatusRepository(connection));
		repositories.put("/api/publication_type", new PublicationTypeRepository(connection));
		repositories.put("/api/region", new RegionRepository(connection));
		repositories.put("/api/right", new RightRepository(connection));
		repositories.put("/api/sampling_method", new SamplingMethodRepository(connection));
		repositories.put("/api/sampling_point", new SamplingPointRepository(connection));
		repositories.put("/api/sampling_program", new SamplingProgramRepository(connection));
		repositories.put("/api/sampling_strategy", new SamplingStrategyRepository(connection));
		repositories.put("/api/software", new SoftwareRepository(connection));
		repositories.put("/api/source", new SourceRepository(connection));
		repositories.put("/api/status", new StatusRepository(connection));
		repositories.put("/api/unit", new UnitRepository(connection));
		repositories.put("/api/unit_category", new UnitCategoryRepository(connection));

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		ObjectMapper mapper = new ObjectMapper();
		repositories.entrySet().forEach(entry -> {
			ContextHandler contextHandler = new ContextHandler(entry.getKey());
			contextHandler.setHandler(new BasicHandler(mapper, entry.getValue()));
			contexts.addHandler(contextHandler);
		});

		server.setHandler(contexts);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			LOGGER.error(server, e);
			e.printStackTrace();
		}
	}

	private static class BasicHandler extends AbstractHandler {

		private final ObjectMapper mapper;
		private final BasicRepository<?> repository;

		BasicHandler(ObjectMapper mapper, BasicRepository<?> repository) {
			this.mapper = mapper;
			this.repository = repository;
		}

		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {

			response.setContentType("application/json; charset=UTF-8");

			if (request.getParameter("id") != null) {
				int id = Integer.parseInt(request.getParameter("id"));
				Optional<?> item = repository.getById(id);
				if (item.isPresent()) {
					response.setStatus(HttpServletResponse.SC_OK);
					mapper.writeValue(response.getWriter(), item.get());
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
				mapper.writeValue(response.getWriter(), repository.getAll());
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new FskService()).start();
	}

	private void initDatabase() throws SQLException, ClassNotFoundException {

		final Properties fastImportProperties = new Properties();
		fastImportProperties.put("LOG", 0);
		fastImportProperties.put("CACHE_SIZE", 65536);
		fastImportProperties.put("LOCK_MODE", 0);
		fastImportProperties.put("UNDO_LOG", 0);

		Class.forName("org.h2.Driver");
		DeleteDbFiles.execute("~/.fsk", "vocabularies", true); // Delete DB if it exists
		final Connection initialConnection = DriverManager.getConnection("jdbc:h2:~/.fsk/vocabularies",
				fastImportProperties);

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
		List<String> filenames = Arrays.asList("availability.sql", "collection_tool.sql", "country.sql",
				"fish_area.sql", "format.sql", "hazard_type.sql", "hazard.sql", "ind_sum.sql",
				"laboratory_accreditation.sql", "language_written_in.sql", "language.sql", "model_class.sql",
				"model_equation_class.sql", "packaging.sql", "parameter_distribution.sql", "parameter_source.sql",
				"parameter_subject.sql", "population.sql", "prodmeth.sql", "prodTreat.sql", "product_matrix.sql",
				"publication_status.sql", "region.sql", "rights.sql", "sampling_method.sql", "sampling_point.sql",
				"sampling_program.sql", "software.sql", "sources.sql", "status.sql", "unit.sql");

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

	private static File getResource(final Bundle bundle, final String path) throws IOException, URISyntaxException {
		URL fileURL = bundle.getEntry(path);
		URL resolvedFileURL = FileLocator.toFileURL(fileURL);
		URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
		return new File(resolvedURI);
	}
}
