package de.bund.bfr.knime.fsklab.vocabularies.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.vocabularies.data.ModelEquationClassRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelEquationClass;

public class ModelEquationClassRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ModelEquationClassRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE model_equation_class ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO model_equation_class VALUES(0, 'name')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws Exception {
		
		// Get mocked class
		ModelEquationClassRepository repository = new ModelEquationClassRepository(connection);
		ModelEquationClass equationClass = repository.getById(0);
		
		assertEquals(0, equationClass.getId());
		assertEquals("name", equationClass.getName());
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		// Get mocked class
		ModelEquationClassRepository repository = new ModelEquationClassRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
