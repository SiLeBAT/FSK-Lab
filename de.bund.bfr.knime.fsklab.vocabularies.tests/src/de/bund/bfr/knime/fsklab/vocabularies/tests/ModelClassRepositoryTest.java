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

import de.bund.bfr.knime.fsklab.vocabularies.data.ModelClassRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelClass;

public class ModelClassRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ModelClassRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE model_class ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(64) NOT NULL,"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO model_class VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() throws Exception {
		
		// Get mocked class
		ModelClassRepository repository = new ModelClassRepository(connection);
		
		Optional<ModelClass> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		ModelClass modelClass = optional.get();
		assertEquals(0, modelClass.getId());
		assertEquals("name", modelClass.getName());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() throws Exception {
		ModelClassRepository repository = new ModelClassRepository(connection);
		Optional<ModelClass> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		// Get mocked class
		ModelClassRepository repository = new ModelClassRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
