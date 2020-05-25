package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.vocabularies.data.FormatRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Format;

public class FormatRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:FormatRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE format("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO format VALUES(0, 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws Exception {
		
		// Get mocked format
		FormatRepository repository = new FormatRepository(connection);
		
		Optional<Format> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Format format = optional.get();
		assertEquals(0, format.getId());
		assertEquals("name", format.getName());
		assertEquals("comment", format.getComment());
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		// Get mocked formats
		FormatRepository repository = new FormatRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
