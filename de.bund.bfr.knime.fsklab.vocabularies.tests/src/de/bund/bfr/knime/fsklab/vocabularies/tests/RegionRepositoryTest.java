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

import de.bund.bfr.knime.fsklab.vocabularies.data.RegionRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.Region;

public class RegionRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:RegionRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE region ("
				+ "id INT not NULL,"
				+ "name VARCHAR(255),"
				+ "nuts CHAR(5) not NULL,"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO region VALUES(0, 'name', 'nuts')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() throws SQLException {
		RegionRepository repository = new RegionRepository(connection);
		
		Optional<Region> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		Region region = optional.get();
		assertEquals(0, region.getId());
		assertEquals("name", region.getName());
		assertEquals("nuts", region.getSsd());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() throws SQLException {
		RegionRepository repository = new RegionRepository(connection);
		Optional<Region> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		RegionRepository repository = new RegionRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
