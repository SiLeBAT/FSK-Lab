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

import de.bund.bfr.knime.fsklab.vocabularies.data.ParameterSubjectRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ParameterSubject;

public class ParameterSubjectRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE parameter_subject ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO parameter_subject VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		
		// Get mocked subject
		ParameterSubjectRepository repository = new ParameterSubjectRepository(connection);
		
		Optional<ParameterSubject> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		ParameterSubject subject = optional.get();
		assertEquals(0, subject.getId());
		assertEquals("name", subject.getName());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		ParameterSubjectRepository repository = new ParameterSubjectRepository(connection);
		Optional<ParameterSubject> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		ParameterSubjectRepository repository = new ParameterSubjectRepository(closedConnection);
		Optional<ParameterSubject> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		ParameterSubjectRepository repository = new ParameterSubjectRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		ParameterSubjectRepository repository = new ParameterSubjectRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
