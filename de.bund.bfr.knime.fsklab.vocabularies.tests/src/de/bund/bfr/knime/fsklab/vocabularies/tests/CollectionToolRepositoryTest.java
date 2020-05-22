package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.vocabularies.data.CollectionToolRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.CollectionTool;

public class CollectionToolRepositoryTest {
	
	private static Connection connection;
	
	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:CollectionToolRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE collection_tool ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(50),"
				+ "PRIMARY KEY(id));");
		statement.execute("INSERT INTO collection_tool VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById() throws SQLException {
		
		// Get mocked collection tool
		CollectionToolRepository repository = new CollectionToolRepository(connection);
		CollectionTool collectionTool = repository.getById(0);
		
		assertEquals(0, collectionTool.getId());
		assertEquals("name", collectionTool.getName());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		
		// Get mocked collection tools
		CollectionToolRepository repository = new CollectionToolRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}

}
