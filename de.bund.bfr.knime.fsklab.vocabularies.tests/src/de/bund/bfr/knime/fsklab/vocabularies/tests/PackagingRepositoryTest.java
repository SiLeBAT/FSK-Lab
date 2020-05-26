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

import de.bund.bfr.knime.fsklab.vocabularies.data.PackagingRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Packaging;

public class PackagingRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:PackagingRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE packaging ("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "ssd CHAR(5) not NULL,"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO packaging VALUES(0, 'name', 'ssd', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		
		// Get mocked packaging
		PackagingRepository repository = new PackagingRepository(connection);
		
		Optional<Packaging> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Packaging packaging = optional.get();
		assertEquals(0, packaging.getId());
		assertEquals("name", packaging.getName());
		assertEquals("ssd", packaging.getSsd());
		assertEquals("comment", packaging.getComment());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		PackagingRepository repository = new PackagingRepository(connection);
		Optional<Packaging> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		PackagingRepository repository = new PackagingRepository(closedConnection);
		Optional<Packaging> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		PackagingRepository repository = new PackagingRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		PackagingRepository repository = new PackagingRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
