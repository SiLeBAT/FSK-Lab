package de.bund.bfr.knime.fsklab.v1_9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	public void testDeserialize_CorrectClass_shouldReturnEqual() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = getClass().getClassLoader();
		
		try (InputStream inputStream = new FileInputStream(new File("files/example_environment.json"))){
			EnvironmentManager manager = FskPortObjectUtil.deserializeAfterClassloaderReset(classLoader, mapper, inputStream, EnvironmentManager.class);
			assertEquals(ArchivedEnvironmentManager.class,manager.getClass());
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
			assertEquals("C:\\Users\\schuelet\\Documents\\Arbeit\\test_model1.fskx",archivePath);
			
			String[] entries = actualManager.getEntries();
			assertNotNull(entries );
			assertEquals(2, entries.length);
			assertEquals("/colors.csv", entries[0]);
			assertEquals("/README.txt", entries[1]);
		}
	}
}
