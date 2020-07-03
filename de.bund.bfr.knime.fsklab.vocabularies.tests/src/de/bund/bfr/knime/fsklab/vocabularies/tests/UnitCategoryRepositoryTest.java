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

import de.bund.bfr.knime.fsklab.vocabularies.data.UnitCategoryRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.UnitCategory;

public class UnitCategoryRepositoryTest {

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
		
		statement.execute("INSERT INTO unit_category VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		UnitCategoryRepository repository = new UnitCategoryRepository(connection);
		
		Optional<UnitCategory> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		UnitCategory category = optional.get();
		assertEquals(0, category.getId());
		assertEquals("name", category.getName());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		UnitCategoryRepository repository = new UnitCategoryRepository(connection);
		Optional<UnitCategory> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		UnitCategoryRepository repository = new UnitCategoryRepository(closedConnection);
		Optional<UnitCategory> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		UnitCategoryRepository repository = new UnitCategoryRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		UnitCategoryRepository repository = new UnitCategoryRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}