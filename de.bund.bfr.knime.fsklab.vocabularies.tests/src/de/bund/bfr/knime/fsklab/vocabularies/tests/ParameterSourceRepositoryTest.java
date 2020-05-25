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

import de.bund.bfr.knime.fsklab.vocabularies.data.ParameterSourceRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ParameterSource;

public class ParameterSourceRepositoryTest {
	
	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ParameterSourceRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE parameter_source ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO parameter_source VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById() throws SQLException {
		
		// Get mocked source
		ParameterSourceRepository repository = new ParameterSourceRepository(connection);
		
		Optional<ParameterSource> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		ParameterSource source = optional.get();
		assertEquals(0, source.getId());
		assertEquals("name", source.getName());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		ParameterSourceRepository repository = new ParameterSourceRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
