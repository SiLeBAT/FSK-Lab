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

import de.bund.bfr.knime.fsklab.vocabularies.data.AvailabilityRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Availability;

public class AvailabilityRepositoryTest {
	
	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:AvailabilityRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE availability ("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO availability VALUES(0, 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {

		// Get mocked availability
		AvailabilityRepository repository = new AvailabilityRepository(connection);
		
		Optional<Availability> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Availability availability = optional.get();
		assertEquals(0, availability.getId());
		assertEquals("name", availability.getName());
		assertEquals("comment", availability.getComment());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		
		// Get mocked availabilities
		AvailabilityRepository repository = new AvailabilityRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
