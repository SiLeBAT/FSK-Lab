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

import de.bund.bfr.knime.fsklab.vocabularies.data.FishAreaRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.FishArea;

public class FishAreaRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:FishAreaRepositoryTest");
		
		Statement statement = connection.createStatement();

		statement.execute("CREATE TABLE fish_area ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(250) NOT NULL,"
				+ "ssd CHAR(10) NOT NULL);");
		
		statement.execute("INSERT INTO fish_area VALUES(0, 'name', 'ssd')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById() throws SQLException {
		
		// Get mocked fish area
		FishAreaRepository repository = new FishAreaRepository(connection);
		
		Optional<FishArea> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		FishArea fishArea = optional.get();
		assertEquals(0, fishArea.getId());
		assertEquals("name", fishArea.getName());
		assertEquals("ssd", fishArea.getSsd());
	}
	
	@Test
	public void testGetAll() throws SQLException {
	
		// Get mocked fish areas
		FishAreaRepository repository = new FishAreaRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
