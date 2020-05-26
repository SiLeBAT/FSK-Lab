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

import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingMethodRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingMethod;

public class SamplingMethodRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:SamplingMethodRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE sampling_method ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255),"
				+ "sampmd CHAR(5) NOT NULL,"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO sampling_method VALUES(0, 'name', '12345', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		SamplingMethodRepository repository = new SamplingMethodRepository(connection);
		
		Optional<SamplingMethod> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		SamplingMethod method = optional.get();
		assertEquals(0, method.getId());
		assertEquals("name", method.getName());
		assertEquals("12345", method.getSampmd());
		assertEquals("comment", method.getComment());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		SamplingMethodRepository repository = new SamplingMethodRepository(connection);
		Optional<SamplingMethod> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		SamplingMethodRepository repository = new SamplingMethodRepository(closedConnection);
		Optional<SamplingMethod> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		SamplingMethodRepository repository = new SamplingMethodRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		SamplingMethodRepository repository = new SamplingMethodRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
