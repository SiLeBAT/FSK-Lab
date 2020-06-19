package de.bund.bfr.knime.fsklab.service;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bfr.knime.fsklab.vocabularies.VocabulariesDatabase;
import de.bund.bfr.knime.fsklab.vocabularies.data.AvailabilityRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.BasicRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.CollectionToolRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.CountryRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.FishAreaRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.FormatRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.HazardRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.HazardTypeRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.IndSumRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.LaboratoryAccreditationRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.LanguageRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.LanguageWrittenInRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ModelClassRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ModelEquationClassRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ModelSubclassRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.PackagingRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ParameterDistributionRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ParameterSourceRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ParameterSubjectRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.PopulationRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ProductMatrixRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ProductTreatmentRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.ProductionMethodRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.PublicationStatusRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.PublicationTypeRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.RegionRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.RightRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingMethodRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingPointRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingProgramRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingStrategyRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.SoftwareRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.SourceRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.StatusRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.UnitCategoryRepository;
import de.bund.bfr.knime.fsklab.vocabularies.data.UnitRepository;

public class FskService implements Runnable {
	
	private Server server;
	
	public FskService() {
		server = new Server(0);
	}
	
	public int getPort() {
		return server.getURI() != null ? server.getURI().getPort() : -1;
	}

	@Override
	public void run() {


		Connection connection;
		try {
			connection = new VocabulariesDatabase().getConnection();
		} catch (Exception err) {
			err.printStackTrace();
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
		public void handle(String target, Request baseRequest, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {

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
}
