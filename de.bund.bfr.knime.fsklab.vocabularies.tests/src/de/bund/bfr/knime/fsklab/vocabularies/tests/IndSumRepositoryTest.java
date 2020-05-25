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

import de.bund.bfr.knime.fsklab.vocabularies.data.IndSumRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.IndSum;

public class IndSumRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:IndSumRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE ind_sum ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(50),"
				+ "ssd CHAR(5),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO ind_sum VALUES(0, 'name', 'ssd')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {
	
		// Get mocked ind sum
		IndSumRepository repository = new IndSumRepository(connection);
		
		Optional<IndSum> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		IndSum indSum = optional.get();
		assertEquals(0, indSum.getId());
		assertEquals("name", indSum.getName());
		assertEquals("ssd", indSum.getSsd());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		
		// Get mocked ind sums
		IndSumRepository repository = new IndSumRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
