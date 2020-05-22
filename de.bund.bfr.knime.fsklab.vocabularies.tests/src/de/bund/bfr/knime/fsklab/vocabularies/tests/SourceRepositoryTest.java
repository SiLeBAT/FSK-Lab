package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
	public void testGetById() throws Exception {
		SourceRepository repository = new SourceRepository(connection);
		Source source = repository.getById(0);
		
		assertEquals(0, source.getId());
		assertEquals("name", source.getName());
		assertEquals("comment", source.getComment());
	}
	
	@Test
	public void testGetAll() throws Exception {
		SourceRepository repository = new SourceRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
