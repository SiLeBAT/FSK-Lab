package de.bund.bfr.knime.fsklab.nodes.environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ExistingEnvironmentManagerTest {
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void testGetEnvironment_nullEnvironment_shouldReturnEmptyOptional() {
		ExistingEnvironmentManager environmentManager = new ExistingEnvironmentManager(null);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}
	
	@Test
	public void testGetEnvironment_emptyEnvironment_shouldReturnEmptyOptional() {
		ExistingEnvironmentManager environmentManager = new ExistingEnvironmentManager("");
		assertFalse(environmentManager.getEnvironment().isPresent());
	}
	
	@Test
	public void testGetEnvironment_nonExistingEnvironment_shouldReturnEmptyOptional() {
		ExistingEnvironmentManager environmentManager = new ExistingEnvironmentManager("@@NonExistingPath");
		assertFalse(environmentManager.getEnvironment().isPresent());
	}
	
	@Test
	public void testGetEnvironment_emptyExistingEnvironment_shouldReturnEmptyOptional() throws IOException {
		String emptyFolder = temporaryFolder.newFolder("emptyFolder").getAbsolutePath();
		ExistingEnvironmentManager environmentManager = new ExistingEnvironmentManager(emptyFolder);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}
	
	@Test
	public void testGetEnvironment_nonEmptyExistingEnvironment_shouldReturnPresentOptional() throws IOException {
		String userFolder = System.getProperty("user.home");
		ExistingEnvironmentManager environmentManager = new ExistingEnvironmentManager(userFolder);
		
		assertTrue(environmentManager.getEnvironment().isPresent());
		Path environment = environmentManager.getEnvironment().get();

		assertTrue(Files.exists(environment));
		assertTrue(Files.isDirectory(environment));
		assertTrue(Files.list(environment).findAny().isPresent());
	}
}
