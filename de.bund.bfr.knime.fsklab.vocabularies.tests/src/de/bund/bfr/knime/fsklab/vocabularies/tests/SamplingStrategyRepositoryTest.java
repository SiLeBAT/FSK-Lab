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

import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingStrategyRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingStrategy;

public class SamplingStrategyRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:SamplingStrategyRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE sampling_strategy ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(50),"
				+ "comment VARCHAR(700),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO sampling_strategy VALUES(0, 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {
		SamplingStrategyRepository repository = new SamplingStrategyRepository(connection);
		
		Optional<SamplingStrategy> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		SamplingStrategy strategy = optional.get();
		assertEquals(0, strategy.getId());
		assertEquals("name", strategy.getName());
		assertEquals("comment", strategy.getComment());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		SamplingStrategyRepository repository = new SamplingStrategyRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
