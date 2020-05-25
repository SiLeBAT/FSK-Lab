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

import de.bund.bfr.knime.fsklab.vocabularies.data.ModelSubclassRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelSubclass;

public class ModelSubClassRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ModelSubClassRepositoryTest");
		
		Statement statement = connection.createStatement();
		
		statement.execute("CREATE TABLE model_class ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(64) NOT NULL,"
				+ "PRIMARY KEY(id))");
		statement.execute("INSERT INTO model_class (id, name) VALUES (0, 'model')");

		statement.execute("CREATE TABLE model_subclass ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(128) NOT NULL,"
				+ "comment VARCHAR(256),"
				+ "class_id INTEGER NOT NULL,"
				+ "PRIMARY KEY(id),"
				+ "FOREIGN KEY(class_id) REFERENCES model_class(id))");
		
		statement.execute("INSERT INTO model_subclass VALUES(0, 'submodel', 'comment', 0)");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() throws SQLException {
		
		// Get mocked class
		ModelSubclassRepository repository = new ModelSubclassRepository(connection);
		
		Optional<ModelSubclass> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		ModelSubclass subClass = optional.get();
		assertEquals(0, subClass.getId());
		assertEquals("submodel", subClass.getName());
		assertEquals(0, subClass.getClassCategory().getId());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() throws SQLException {
		ModelSubclassRepository repository = new ModelSubclassRepository(connection);
		Optional<ModelSubclass> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		
		// Get mocked classes
		ModelSubclassRepository repository = new ModelSubclassRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
