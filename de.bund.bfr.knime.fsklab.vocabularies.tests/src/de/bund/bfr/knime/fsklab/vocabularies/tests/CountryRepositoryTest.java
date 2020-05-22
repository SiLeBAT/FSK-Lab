package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.vocabularies.data.CountryRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Country;

public class CountryRepositoryTest {
	
	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:CountryRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE country ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255) NOT NULL,"
				+ "iso CHAR(2) NOT NULL,"
				+ "PRIMARY KEY(id));");
		
		statement.execute("INSERT INTO country VALUES(0, 'name', 'ES')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById() throws SQLException {
		
		// Get mocked country
		CountryRepository repository = new CountryRepository(connection);
		Country country = repository.getById(0);
		
		assertEquals(0, country.getId());
		assertEquals("name", country.getName());
		assertEquals("ES", country.getIso());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		
		// Get mocked countries
		CountryRepository repository = new CountryRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
