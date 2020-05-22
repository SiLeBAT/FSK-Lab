package de.bund.bfr.knime.fsklab.vocabularies;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bfr.knime.fsklab.vocabularies.data.RightRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Right;

public class VocabulariesDatabase {

	/**
	 * Connection used to query data out of the database. This is not the original
	 * connection used for creating the database.
	 */
	private Connection connection;
	
	private final String version = "0.0.1";
	
	public VocabulariesDatabase() throws IOException, SQLException {
		initialize();
	}
	
	private void initialize() throws IOException, SQLException {
		
		Path userFolder = Paths.get(System.getProperty("user.home"));
		Path vocabulariesFolder = userFolder.resolve(".fsk").resolve("vocabularies");
		
		if (Files.notExists(vocabulariesFolder)) {
			Files.createDirectories(vocabulariesFolder);
		}
		
		// Create version.properties file with *version*
		Path propertiesFile = vocabulariesFolder.resolve("version.properties");
		Properties versionProperties = new Properties();
		
		boolean isDatabaseMissingOrOutdated = false;
		
		if (Files.exists(propertiesFile)) {
			// Check version (if different load DB)
			try (InputStream inputStream = Files.newInputStream(propertiesFile)) {
				versionProperties.load(inputStream);
			}
			
			if (!versionProperties.get("version").equals(version)) {
				isDatabaseMissingOrOutdated = true;
			}
			
		} else {
			// If it does not exist, just import the DB and create version.properties
			isDatabaseMissingOrOutdated = true;
		}
		
		if (isDatabaseMissingOrOutdated) {
			
			// Update version.properties
			versionProperties.put("version", version);
			
			try (OutputStream outputStream = Files.newOutputStream(propertiesFile)) {
				versionProperties.store(outputStream, "");
			}
			
			// Create DB
			Path database = vocabulariesFolder.resolve("vocabularies");
			String connectionUrl = "jdbc:h2:" + database.toAbsolutePath().toString();

			Properties fastImportProperties = new Properties();
			fastImportProperties.put("LOG", 0);
			fastImportProperties.put("CACHE_SIZE", 65536);
			fastImportProperties.put("LOCK_MODE", 0);
			fastImportProperties.put("UNDO_LOG", 0);
			
			final Connection initialConnection = DriverManager.getConnection(connectionUrl, fastImportProperties);
			loadInitialData(initialConnection);
		}
	}
	
	private static void loadInitialData(Connection connection) throws IOException {

		String tableFile = VocabulariesDatabase.class.getClassLoader().getResource("data/tables.sql").getFile();
		try {
			System.out.println("Creating tables");
			String script = FileUtils.readFileToString(new File(tableFile), "UTF-8");

			Statement statement = connection.createStatement();
			statement.execute(script);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
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

			System.out.println("Loading " + filename);

			String filePath = VocabulariesDatabase.class.getClassLoader().getResource("data/initialdata/" + filename).getFile();
			File file = new File(filePath);

			Statement statement;
			try {
				statement = connection.createStatement();
				for (String line : FileUtils.readLines(file, "UTF-8")) {
					statement.execute(line);
				}
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnection() throws SQLException {
		if (connection == null) {
			Path userFolder = Paths.get(System.getProperty("user.home"));
			Path vocabulariesFolder = userFolder.resolve(".fsk").resolve("vocabularies");
			Path database = vocabulariesFolder.resolve("vocabularies");
			
			String connectionUrl = "jdbc:h2:" + database.toAbsolutePath().toString();
			
			connection = DriverManager.getConnection(connectionUrl);
		}
		return connection;
	}
	
	// Example application for a future Jetty server. (To be removed)
	public static void main(String[] args) throws SQLException, IOException, JsonProcessingException {
		
		VocabulariesDatabase vocabulariesDatabase = new VocabulariesDatabase();

		Connection connection = vocabulariesDatabase.getConnection();
		
		RightRepository rightRepository = new RightRepository(connection);
		Right[] rights = rightRepository.getAll();
		
		ObjectMapper objectMapper = new ObjectMapper();
		for (Right right : rights) {
			try {
				System.out.println(objectMapper.writeValueAsString(right));
			} catch (Exception e) {
				// do nothing
			}
		}
	}
}
