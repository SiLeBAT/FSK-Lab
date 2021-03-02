package de.bund.bfr.knime.fsklab.v2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bfr.knime.fsklab.nodes.FskPortObjectUtil;
import de.bund.bfr.knime.fsklab.nodes.environment.ArchivedEnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;

public class FskPortObjectUtilTest {

	@Test
	public void testDeserialize_CorrectClass_shouldReturnTrue() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = getClass().getClassLoader();
		
		try (InputStream inputStream = new FileInputStream(new File("files/example_environment.json"))){
			EnvironmentManager manager = FskPortObjectUtil.deserializeAfterClassloaderReset(classLoader, mapper, inputStream, EnvironmentManager.class);
			assertTrue(manager instanceof ArchivedEnvironmentManager);
		}
	}
	
	@Test
	public void testDeserialize_CorrectValues_shouldReturnEqual() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = getClass().getClassLoader();
		
		try (InputStream inputStream = new FileInputStream(new File("files/example_environment.json"))){
			EnvironmentManager manager = FskPortObjectUtil.deserializeAfterClassloaderReset(classLoader, mapper, inputStream, EnvironmentManager.class);
			ArchivedEnvironmentManager actualManager = (ArchivedEnvironmentManager) manager;
			String archivePath = actualManager.getArchivePath();
			assertEquals("C:\\Users\\schuelet\\Documents\\Arbeit\\test_model1.fskx", archivePath);
			
			String[] entries = actualManager.getEntries();
			assertNotNull(entries );
			assertEquals(2, entries.length);
			assertEquals("/colors.csv", entries[0]);
			assertEquals("/README.txt", entries[1]);
		}
	}
}
