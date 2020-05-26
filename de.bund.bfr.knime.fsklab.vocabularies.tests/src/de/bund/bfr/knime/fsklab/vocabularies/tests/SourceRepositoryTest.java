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

import de.bund.bfr.knime.fsklab.vocabularies.data.SourceRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Source;

public class SourceRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:SourceRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE sources("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO sources VALUES(0, 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		SourceRepository repository = new SourceRepository(connection);
		
		Optional<Source> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Source source = optional.get();
		assertEquals(0, source.getId());
		assertEquals("name", source.getName());
		assertEquals("comment", source.getComment());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		SourceRepository repository = new SourceRepository(connection);
		Optional<Source> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		SourceRepository repository = new SourceRepository(closedConnection);
		Optional<Source> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		SourceRepository repository = new SourceRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		SourceRepository repository = new SourceRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
