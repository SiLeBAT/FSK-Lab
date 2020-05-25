package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.vocabularies.data.LanguageRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Language;

public class LanguageRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:LanguageRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE language ("
				+ "id INTEGER NOT NULL,"
				+ "code CHAR(2) NOT NULL,"
				+ "name VARCHAR(50) NOT NULL,"
				+ "PRIMARY KEY(code))");
		
		statement.execute("INSERT INTO language VALUES(0, 'ES', 'Spanish')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}


	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() throws SQLException {
		
		// Get mocked language
		LanguageRepository repository = new LanguageRepository(connection);
		
		Optional<Language> optional = repository.getById(0);
		assertTrue(optional.isPresent());

		Language language = optional.get();
		assertEquals(0, language.getId());
		assertEquals("ES", language.getCode());
		assertEquals("Spanish", language.getName());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() throws SQLException {
		LanguageRepository repository = new LanguageRepository(connection);
		Optional<Language> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		
		// Get mocked languages
		LanguageRepository repository = new LanguageRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
