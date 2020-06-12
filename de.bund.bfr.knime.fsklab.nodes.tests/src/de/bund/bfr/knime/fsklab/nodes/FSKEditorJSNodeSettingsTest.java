package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.FSKEditorJSNodeDialog.ModelType;

public class FSKEditorJSNodeSettingsTest {

	@Test
	public void testGetModelType_emptyConstructor_shouldReturnGenericModel() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		assertEquals(ModelType.genericModel, settings.getModelType());
	}

	@Test
	public void testSetModelType_null_shouldSetGenericModel() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setModelType(null);
		assertEquals(ModelType.genericModel, settings.getModelType());
	}
	
	@Test
	public void testSetModelType_nonNullType_shouldSetNonNullType() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setModelType(ModelType.toxicologicalModel);
		assertEquals(ModelType.toxicologicalModel, settings.getModelType());
	}
	
	@Test
	public void testGetReadmeFile_emptyConstructor_shouldReturnEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		assertTrue(settings.getReadmeFile().isEmpty());
	}
	
	@Test
	public void testSetReadmeFile_null_shouldSetEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setReadme(null);
		assertTrue(settings.getReadmeFile().isEmpty());
	}
	
	@Test
	public void testSetReadmeFile_emptyString_shouldSetEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setReadme("");
		assertTrue(settings.getReadmeFile().isEmpty());
	}
	
	@Test
	public void testSetReadmeFile_nonEmptyString_shouldSetNonEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setReadme("/Users/doe/README.md");
		assertEquals("/Users/doe/README.md", settings.getReadmeFile());
	}
	
	@Test
	public void testGetWorkingDirectory_emptyConstructor_shouldReturnEmtpyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		assertTrue(settings.getWorkingDirectory().isEmpty());
	}

	@Test
	public void testSetWorkingDirectory_null_shouldSetEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setWorkingDirectory(null);
		assertTrue(settings.getWorkingDirectory().isEmpty());
	}
	
	@Test
	public void testSetWorkingDirectory_emptyString_shouldSetEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setWorkingDirectory("");
		assertTrue(settings.getWorkingDirectory().isEmpty());
	}

	@Test
	public void testSetWorkingDirectory_nonEmptyString_shouldSetNonEmptyString() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setWorkingDirectory("/Users/doe/workingDirectory");
		assertEquals("/Users/doe/workingDirectory", settings.getWorkingDirectory());
	}
	
	@Test
	public void testGetResources_emptyConstructor_shouldReturnEmptyArray() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		assertEquals(0, settings.getResources().length);
	}
	
	@Test
	public void testSetResources_null_shouldReturnEmptyArray() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setResources(null);
		assertEquals(0, settings.getResources().length);
	}

	@Test
	public void testSetResources_emptyArray_shouldReturnEmptyArray() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		settings.setResources(new String[0]);
		assertEquals(0, settings.getResources().length);
	}

	@Test
	public void testSetResources_nonEmptyArray_shouldReturnNonEmptyArray() {
		FSKEditorJSNodeSettings settings = new FSKEditorJSNodeSettings();
		
		String[] ufoArray = new String[] { "ufos.csv" };
		settings.setResources(ufoArray);
		assertArrayEquals(ufoArray, settings.getResources());
	}
}
