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

import de.bund.bfr.knime.fsklab.vocabularies.data.ProductMatrixRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ProductMatrix;

public class ProductMatrixRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:AvailabilityRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE product_matrix ("
				+ "id INTEGER not NULL,"
				+ "ssd CHAR(20) not NULL,"
				+ "name VARCHAR(250),"
				+ "comment VARCHAR(255),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO product_matrix VALUES(0, 'ssd', 'name', 'comment')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		ProductMatrixRepository repository = new ProductMatrixRepository(connection);
		
		Optional<ProductMatrix> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		ProductMatrix matrix = optional.get();
		assertEquals(0, matrix.getId());
		assertEquals("ssd", matrix.getSsd());
		assertEquals("name", matrix.getName());
		assertEquals("comment", matrix.getComment());
	}
	
	@Test
	public void testGetById_MissingId_ShouldReturnMissingOptional() {
		ProductMatrixRepository repository = new ProductMatrixRepository(connection);
		Optional<ProductMatrix> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}
	
	@Test
	public void testGetAll() {
		ProductMatrixRepository repository = new ProductMatrixRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}
}
