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

import de.bund.bfr.knime.fsklab.vocabularies.data.StatusRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Status;

public class StatusRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:StatusRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE status ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(10) NOT NULL,"
				+ "comment VARCHAR(128),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO status VALUES(0, 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {
		StatusRepository repository = new StatusRepository(connection);
		
		Optional<Status> optional = repository.getById(0);
		assertTrue(optional.isPresent());

		Status status = optional.get();
		assertEquals(0, status.getId());
		assertEquals("name", status.getName());
		assertEquals("comment", status.getComment());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		StatusRepository repository = new StatusRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
