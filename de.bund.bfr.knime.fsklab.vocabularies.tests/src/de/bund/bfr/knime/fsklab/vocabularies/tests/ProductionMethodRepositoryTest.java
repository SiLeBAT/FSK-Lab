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

import de.bund.bfr.knime.fsklab.vocabularies.data.ProductionMethodRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ProductionMethod;

public class ProductionMethodRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ProductionMethodRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE prodmeth ("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "ssd CHAR(5) not NULL,"
				+ "comment VARCHAR(400),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO prodmeth VALUES(0, 'name', 'ssd', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws Exception {
		
		ProductionMethodRepository repository = new ProductionMethodRepository(connection);
		ProductionMethod method = repository.getById(0);
		
		assertEquals(0, method.getId());
		assertEquals("name", method.getName());
		assertEquals("ssd", method.getSsd());
		assertEquals("comment", method.getComment());
	}
	
	@Test
	public void testGetAll() throws Exception {
		ProductionMethodRepository repository = new ProductionMethodRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
