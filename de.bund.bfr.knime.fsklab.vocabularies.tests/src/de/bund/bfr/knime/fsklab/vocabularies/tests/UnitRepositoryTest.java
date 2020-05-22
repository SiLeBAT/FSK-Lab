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

import de.bund.bfr.knime.fsklab.vocabularies.data.UnitRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Unit;

public class UnitRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:UnitRepositoryTest");
		
		Statement statement = connection.createStatement();
		
		statement.execute("CREATE TABLE unit_category ("
				+ "id INTEGER not NULL,"
				+ "name VARCHAR(255) not NULL,"
				+ "PRIMARY KEY(id))");
		statement.execute("INSERT INTO unit_category VALUES (0, 'name')");

		statement.execute("CREATE TABLE unit ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(64) NOT NULL,"
				+ "ssd CHAR(5),"
				+ "comment VARCHAR(128),"
				+ "category_id INTEGER,"
				+ "PRIMARY KEY(id),"
				+ "FOREIGN KEY(category_id) REFERENCES unit_category(id))");
		statement.execute("INSERT INTO unit VALUES (0, 'name', 'ssd', 'comment', 0)");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {
		UnitRepository repository = new UnitRepository(connection);
		Unit unit = repository.getById(0);
		
		assertEquals(0, unit.getId());
		assertEquals("name", unit.getName());
		assertEquals("ssd", unit.getSsd());
		assertEquals("comment", unit.getComment());
		assertEquals(0, unit.getCategory().getId());
	}

	@Test
	public void testGetAll() throws SQLException {
		UnitRepository repository = new UnitRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
