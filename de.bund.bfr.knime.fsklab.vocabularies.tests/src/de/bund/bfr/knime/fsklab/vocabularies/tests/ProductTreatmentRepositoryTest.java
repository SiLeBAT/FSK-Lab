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

import de.bund.bfr.knime.fsklab.vocabularies.data.ProductTreatmentRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.ProductTreatment;

public class ProductTreatmentRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:ProductTreatmentRepositoryTest");

		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE prodTreat (" + "id INTEGER not NULL," + "name VARCHAR(255) not NULL,"
				+ "ssd CHAR(5) not NULL," + "comment VARCHAR(255)," + "PRIMARY KEY(id))");

		statement.execute("INSERT INTO prodTreat VALUES(0, 'name', 'ssd', 'comment')");
	}

	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	public void testGetById_ExistingId_ShouldReturnPresentOptional() {
		ProductTreatmentRepository repository = new ProductTreatmentRepository(connection);

		Optional<ProductTreatment> optional = repository.getById(0);
		assertTrue(optional.isPresent());

		ProductTreatment treatment = optional.get();
		assertEquals(0, treatment.getId());
		assertEquals("name", treatment.getName());
		assertEquals("ssd", treatment.getSsd());
		assertEquals("comment", treatment.getComment());
	}

	@Test
	public void testGetById_MissingId_ShouldReturnEmptyOptional() {
		ProductTreatmentRepository repository = new ProductTreatmentRepository(connection);
		Optional<ProductTreatment> optional = repository.getById(-1);
		assertFalse(optional.isPresent());
	}

	@Test
	public void testGetById_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		ProductTreatmentRepository repository = new ProductTreatmentRepository(closedConnection);
		Optional<ProductTreatment> optional = repository.getById(0);
		assertFalse(optional.isPresent());
	}

	@Test
	public void testGetAll() {
		ProductTreatmentRepository repository = new ProductTreatmentRepository(connection);
		assertTrue(repository.getAll().length > 0);
	}

	@Test
	public void testGetAll_ClosedConnection_ShouldReturnEmptyOptional() throws SQLException {
		Connection closedConnection = TestUtils.mockClosedConnection();
		ProductTreatmentRepository repository = new ProductTreatmentRepository(closedConnection);
		assertEquals(0, repository.getAll().length);
	}
}
