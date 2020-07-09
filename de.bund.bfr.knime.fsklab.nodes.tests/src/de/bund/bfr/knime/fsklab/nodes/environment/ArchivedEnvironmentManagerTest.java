package de.bund.bfr.knime.fsklab.nodes.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ArchivedEnvironmentManagerTest {

	@Test
	public void testGetEnvironment_nullArchivePath_shouldReturnEmptyOptional() {
		ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager(null, null);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}

	@Test
	public void testGetEnvironment_emptyArchivePath_shouldReturnEmptyOptional() {
		ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager("", null);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}

	@Test
	public void testGetEnvironment_nonExistingArchivePath_shouldReturnEmptyOptional() {
		ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager("@@NonExistingPath", null);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}

	@Test
	public void testGetEnvironment_nullEntries_shouldReturnEmptyOptional() {
		ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager(
				"files/model_without_resources.fskx", null);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}

	@Test
	public void testGetEnvironment_emptyEntries_shouldReturnEmptyOptional() {
		ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager(
				"files/model_without_resources.fskx", new String[0]);
		assertFalse(environmentManager.getEnvironment().isPresent());
	}

	@Test
	public void testGetEnvironment_withResources_shouldReturnPresentOptional() throws IOException {
		ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager(
				"files/model_with_resources.fskx", new String[] { "./model-pars.csv", "./campy-obs-censoring.csv" });

		assertTrue(environmentManager.getEnvironment().isPresent());
		Path environment = environmentManager.getEnvironment().get();

		assertTrue(Files.exists(environment));
		assertTrue(Files.isDirectory(environment));

		List<Path> files = Files.list(environment).collect(Collectors.toList());
		assertEquals(2, files.size());
	}
	
	@Test
	public void testName() throws Exception {
		
		ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("C:/Users/de/Desktop/workingDirectory.json");
		ExistingEnvironmentManager deserializeManager = mapper.readValue(file, ExistingEnvironmentManager.class);
		System.out.println(mapper.writeValueAsString(deserializeManager));
		
		Thread.currentThread().setContextClassLoader(originalClassLoader);
	}
}
