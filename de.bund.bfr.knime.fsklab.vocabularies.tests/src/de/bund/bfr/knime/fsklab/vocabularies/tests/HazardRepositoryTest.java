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

import de.bund.bfr.knime.fsklab.vocabularies.data.HazardRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Hazard;

public class HazardRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE hazard ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(300),"
				+ "ssd CHAR(15),"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO hazard VALUES(0, 'name', 'ssd')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		
		// Get mocked hazard
		HazardRepository repository = new HazardRepository(connection);
		
		Optional<Hazard> optional = repository.getById(0);
		assertTrue(optional.isPresent());

		Hazard hazard = optional.get();
		assertEquals(0, hazard.getId());
		assertEquals("name", hazard.getName());
		assertEquals("ssd", hazard.getSsd());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		HazardRepository repository = new HazardRepository(connection);
		Optional<Hazard> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		HazardRepository repository = new HazardRepository(closedConnection);
		Optional<Hazard> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}

	@Test
	public void testGetAll() {
		// Get mocked hazards
		HazardRepository repository = new HazardRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		HazardRepository repository = new HazardRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
