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

import de.bund.bfr.knime.fsklab.vocabularies.data.PopulationRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Population;

public class PopulationRepositoryTest {
	
	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:PopulationRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE population ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255),"
				+ "foodon CHAR(10),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO population VALUES(0, 'name', 'foodon')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {
		
		// Get mocked population
		PopulationRepository repository = new PopulationRepository(connection);
		
		Optional<Population> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Population population = optional.get();
		assertEquals(0, population.getId());
		assertEquals("name", population.getName());
		assertEquals("foodon", population.getFoodon());
	}
	
	@Test
	public void testGetAll() throws Exception {
		PopulationRepository repository = new PopulationRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
