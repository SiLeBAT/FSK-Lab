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

import de.bund.bfr.knime.fsklab.vocabularies.data.PublicationTypeRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.PublicationType;

public class PublicationTypeRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:PublicationTypeRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE publication_type("
				+ "id INTEGER not NULL,"
				+ "shortName VARCHAR(10) not NULL,"
				+ "fullName VARCHAR(255) not NULL,"
				+ "PRIMARY KEY (id))");
		
		statement.execute("INSERT INTO publication_type VALUES(0, 'shortName', 'fullName')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() throws SQLException {
		PublicationTypeRepository repository = new PublicationTypeRepository(connection);
		
		Optional<PublicationType> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		PublicationType publicationType = optional.get();
		assertEquals(0, publicationType.getId());
		assertEquals("shortName", publicationType.getShortName());
		assertEquals("fullName", publicationType.getFullName());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() throws SQLException {
		PublicationTypeRepository repository = new PublicationTypeRepository(connection);
		Optional<PublicationType> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() throws SQLException {
		PublicationTypeRepository repository = new PublicationTypeRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
