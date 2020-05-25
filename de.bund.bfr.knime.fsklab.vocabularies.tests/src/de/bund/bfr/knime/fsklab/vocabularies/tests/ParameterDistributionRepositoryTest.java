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

import de.bund.bfr.knime.fsklab.vocabularies.data.ParameterDistributionRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ParameterDistribution;

public class ParameterDistributionRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ParameterDistributionRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE parameter_distribution ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(50),"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO parameter_distribution VALUES(0, 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() throws SQLException {
		
		// Get mocked parameter distribution
		ParameterDistributionRepository repository = new ParameterDistributionRepository(connection);
		
		Optional<ParameterDistribution> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		ParameterDistribution distribution = optional.get();
		assertEquals(0, distribution.getId());
		assertEquals("name", distribution.getName());
		assertEquals("comment", distribution.getComment());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnMissingOptional() throws SQLException {
		ParameterDistributionRepository repository = new ParameterDistributionRepository(connection);
		Optional<ParameterDistribution> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() throws Exception {
		ParameterDistributionRepository repository = new ParameterDistributionRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
