package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.vocabularies.data.LanguageWrittenInRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.LanguageWrittenIn;

public class LanguageWrittenInTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:LanguageWrittenInRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE language_written_in ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(50) NOT NULL,"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO language_written_in VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws Exception {
		
		// Get mocked language written in
		LanguageWrittenInRepository repository = new LanguageWrittenInRepository(connection);
		
		Optional<LanguageWrittenIn> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		LanguageWrittenIn language = optional.get();
		assertEquals(0, language.getId());
		assertEquals("name", language.getName());
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		// Get mocked languages
		LanguageWrittenInRepository repository = new LanguageWrittenInRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
