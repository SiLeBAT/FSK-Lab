package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.FSKEditorJSNodeDialog.ModelType;

public class FSKEditorJSNodeSettingsTest {

	@Test
	public void testGetModelType_emptyConstructor_shouldReturnGenericModel() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		assertEquals(ModelType.genericModel, settings.getModelType());
	}

	@Test
	public void testSetModelType_null_shouldSetGenericModel() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setModelType(null);
		assertEquals(ModelType.genericModel, settings.getModelType());
	}
	
	@Test
	public void testSetModelType_nonNullType_shouldSetNonNullType() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setModelType(ModelType.toxicologicalModel);
		assertEquals(ModelType.toxicologicalModel, settings.getModelType());
	}
	
	@Test
	public void testGetReadmeFile_emptyConstructor_shouldReturnEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		assertTrue(settings.getReadmeFile().isEmpty());
	}
	
	@Test
	public void testSetReadmeFile_null_shouldSetEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setReadme(null);
		assertTrue(settings.getReadmeFile().isEmpty());
	}
	
	@Test
	public void testSetReadmeFile_emptyString_shouldSetEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setReadme("");
		assertTrue(settings.getReadmeFile().isEmpty());
	}
	
	@Test
	public void testSetReadmeFile_nonEmptyString_shouldSetNonEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setReadme("/Users/doe/README.md");
		assertEquals("/Users/doe/README.md", settings.getReadmeFile());
	}
	
	@Test
	public void testGetWorkingDirectory_emptyConstructor_shouldReturnEmtpyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		assertTrue(settings.getWorkingDirectory().isEmpty());
	}

	@Test
	public void testSetWorkingDirectory_null_shouldSetEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setWorkingDirectory(null);
		assertTrue(settings.getWorkingDirectory().isEmpty());
	}
	
	@Test
	public void testSetWorkingDirectory_emptyString_shouldSetEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setWorkingDirectory("");
		assertTrue(settings.getWorkingDirectory().isEmpty());
	}

	@Test
	public void testSetWorkingDirectory_nonEmptyString_shouldSetNonEmptyString() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setWorkingDirectory("/Users/doe/workingDirectory");
		assertEquals("/Users/doe/workingDirectory", settings.getWorkingDirectory());
	}
	
	@Test
	public void testGetResources_emptyConstructor_shouldReturnEmptyArray() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		assertEquals(0, settings.getResources().length);
	}
	
	@Test
	public void testSetResources_null_shouldReturnEmptyArray() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setResources(null);
		assertEquals(0, settings.getResources().length);
	}

	@Test
	public void testSetResources_emptyArray_shouldReturnEmptyArray() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		settings.setResources(new String[0]);
		assertEquals(0, settings.getResources().length);
	}

	@Test
	public void testSetResources_nonEmptyArray_shouldReturnNonEmptyArray() {
		FSKEditorJSConfig settings = new FSKEditorJSConfig();
		
		String[] ufoArray = new String[] { "ufos.csv" };
		settings.setResources(ufoArray);
		assertArrayEquals(ufoArray, settings.getResources());
	}
}
