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
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		
		ProductionMethodRepository repository = new ProductionMethodRepository(connection);
		
		Optional<ProductionMethod> optional = repository.getById(0);
		assertTrue(optional.isPresent());

		ProductionMethod method = optional.get();
		assertEquals(0, method.getId());
		assertEquals("name", method.getName());
		assertEquals("ssd", method.getSsd());
		assertEquals("comment", method.getComment());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		ProductionMethodRepository repository = new ProductionMethodRepository(connection);
		Optional<ProductionMethod> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		ProductionMethodRepository repository = new ProductionMethodRepository(closedConnection);
		Optional<ProductionMethod> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		ProductionMethodRepository repository = new ProductionMethodRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
	
	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyArray() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		ProductionMethodRepository repository = new ProductionMethodRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
