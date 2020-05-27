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

import de.bund.bfr.knime.fsklab.vocabularies.data.UnitRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Unit;

public class UnitRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:");
		
		Statement statement = connection.createStatement();
		
		statement.execute("CREATE TABLE unit_category ("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "PRIMARY KEY(id))");
		statement.execute("INSERT INTO unit_category VALUES (0, 'name')");

		statement.execute("CREATE TABLE unit ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(64) NOT NULL,"
				+ "ssd CHAR(5),"
				+ "comment VARCHAR(128),"
				+ "category_id INTEGER,"
				+ "PRIMARY KEY(id),"
				+ "FOREIGN KEY(category_id) REFERENCES unit_category(id))");
		statement.execute("INSERT INTO unit VALUES (0, 'name', 'ssd', 'comment', 0)");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		UnitRepository repository = new UnitRepository(connection);
		
		Optional<Unit> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Unit unit = optional.get();
		assertEquals(0, unit.getId());
		assertEquals("name", unit.getName());
		assertEquals("ssd", unit.getSsd());
		assertEquals("comment", unit.getComment());
		assertEquals(0, unit.getCategory().getId());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		UnitRepository repository = new UnitRepository(connection);
		Optional<Unit> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		UnitRepository repository = new UnitRepository(closedConnection);
		Optional<Unit> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}

	@Test
	public void testGetAll() {
		UnitRepository repository = new UnitRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		UnitRepository repository = new UnitRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
