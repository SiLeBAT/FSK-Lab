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

import de.bund.bfr.knime.fsklab.vocabularies.data.HazardTypeRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.HazardType;

public class HazardTypeRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:HazardTypeRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE hazard_type ("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO hazard_type VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		
		// Get mocked hazard type
		HazardTypeRepository repository = new HazardTypeRepository(connection);
		
		Optional<HazardType> optional = repository.getById(0);
		assertTrue(optional.isPresent());

		HazardType hazardType = optional.get();
		assertEquals(0, hazardType.getId());
		assertEquals("name", hazardType.getName());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		HazardTypeRepository repository = new HazardTypeRepository(connection);
		Optional<HazardType> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		HazardTypeRepository repository = new HazardTypeRepository(closedConnection);
		Optional<HazardType> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		// Get mocked hazard types
		HazardTypeRepository repository = new HazardTypeRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		HazardTypeRepository repository = new HazardTypeRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
