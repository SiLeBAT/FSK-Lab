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

import de.bund.bfr.knime.fsklab.vocabularies.data.RightRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Right;

public class RightRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE rights("
				+ "id INTEGER not NULL,"
				+ "shortname VARCHAR(50) not NULL,"
				+ "fullname VARCHAR(255) not NULL,"
				+ "url VARCHAR(255),"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO rights VALUES(0, 'shortname', 'fullname', 'url')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		RightRepository repository = new RightRepository(connection);
		
		Optional<Right> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Right right = optional.get();
		assertEquals(0, right.getId());
		assertEquals("shortname", right.getShortname());
		assertEquals("fullname", right.getFullname());
		assertEquals("url", right.getUrl());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		RightRepository repository = new RightRepository(connection);
		Optional<Right> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		RightRepository repository = new RightRepository(closedConnection);
		Optional<Right> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		RightRepository repository = new RightRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		RightRepository repository = new RightRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
