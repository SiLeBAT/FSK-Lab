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

import de.bund.bfr.knime.fsklab.vocabularies.data.SamplingProgramRepository;
import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingProgram;

public class SamplingProgramRepositoryTest {

	private static Connection connection;

	@BeforeClass
	public static void setUp() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:SamplingProgramRepositoryTest");
		
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE sampling_program ("
				+ "id INTEGER NOT NULL,"
				+ "name VARCHAR(255),"
				+ "progtype CHAR(5),"
				+ "PRIMARY KEY(id))");
		
		statement.execute("INSERT INTO sampling_program VALUES(0, 'name', 'type')");
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testGetById() throws SQLException {
		SamplingProgramRepository repository = new SamplingProgramRepository(connection);
		
		Optional<SamplingProgram> optional = repository.getById(0);
		assertTrue(optional.isPresent());
		
		SamplingProgram program = optional.get();
		assertEquals(0, program.getId());
		assertEquals("name", program.getName());
		assertEquals("type", program.getProgType());
	}

	@Test
	public void testGetAll() throws SQLException {
		SamplingProgramRepository repository = new SamplingProgramRepository(connection);
		assertTrue(repository.getAll().length > 0);	
	}
}
