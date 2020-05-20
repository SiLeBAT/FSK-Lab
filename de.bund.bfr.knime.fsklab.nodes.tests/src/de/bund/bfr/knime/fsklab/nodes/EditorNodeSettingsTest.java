package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EditorNodeSettingsTest {
	
	@Test
	public void testModel() {
		
		EditorNodeSettings settings = new EditorNodeSettings();
		assertFalse(settings.hasModel());
		assertTrue(settings.getModel().isEmpty());
		
		// setModel ignores null
		settings.setModel(null);
		assertFalse(settings.hasModel());
		assertTrue(settings.getModel().isEmpty());
		
		// setModel ignores empty string
		settings.setModel("");
		assertFalse(settings.hasModel());
		assertTrue(settings.getModel().isEmpty());
		
		// setModel takes other strings
		settings.setModel("a = 2 + 2");
		assertTrue(settings.hasModel());
		assertEquals("a = 2 + 2", settings.getModel());
	}
	
	@Test
	public void testViz() {
		
		EditorNodeSettings settings = new EditorNodeSettings();
		assertFalse(settings.hasViz());
		assertTrue(settings.getViz().isEmpty());
		
		// setViz ignores null
		settings.setViz(null);
		assertFalse(settings.hasViz());
		assertTrue(settings.getViz().isEmpty());
		
		// setViz ignores empty string
		settings.setViz("");
		assertFalse(settings.hasViz());
		assertTrue(settings.getViz().isEmpty());
		
		// setViz takes other strings
		settings.setViz("plot(10)");
		assertTrue(settings.hasViz());
		assertEquals("plot(10)", settings.getViz());
	}

	@Test
	public void testResources() {
		
		EditorNodeSettings settings = new EditorNodeSettings();
		assertFalse(settings.hasResources());
		assertTrue(settings.getResources().isEmpty());
		
		// setResources ignores null
		settings.setResources(null);
		assertFalse(settings.hasResources());
		assertTrue(settings.getResources().isEmpty());
		
		// setResources ignores empty string
		settings.setResources("");
		assertFalse(settings.hasResources());
		assertTrue(settings.getResources().isEmpty());
		
		// setResources takes other strings
		settings.setResources("C:/Temp");
		assertTrue(settings.hasResources());
		assertEquals("C:/Temp", settings.getResources());
	}

	@Test
	public void testReadme() throws Exception {
		
		EditorNodeSettings settings = new EditorNodeSettings();
		assertFalse(settings.hasReadme());
		assertTrue(settings.getReadme().isEmpty());
		
		// setReadme ignores null
		settings.setReadme(null);
		assertFalse(settings.hasReadme());
		assertTrue(settings.getReadme().isEmpty());
		
		// setReadme ignores empty string
		settings.setReadme("");
		assertFalse(settings.hasReadme());
		assertTrue(settings.getReadme().isEmpty());
		
		// setReadme takes other strings
		settings.setReadme("Some readme ...");
		assertTrue(settings.hasReadme());
		assertEquals("Some readme ...", settings.getReadme());
	}
	
	@Test
	public void testWorkingDirectory() throws Exception {
		
		EditorNodeSettings settings = new EditorNodeSettings();
		assertFalse(settings.hasWorkingDirectory());
		assertTrue(settings.getWorkingDirectory().isEmpty());
		
		// setWorkingDirectory ignores null
		settings.setWorkingDirectory("");
		assertFalse(settings.hasWorkingDirectory());
		assertTrue(settings.getWorkingDirectory().isEmpty());
		
		// setWorkingDirectory ignores empty string
		settings.setWorkingDirectory(null);
		assertFalse(settings.hasWorkingDirectory());
		assertTrue(settings.getWorkingDirectory().isEmpty());
		
		// setWorkingDirectory takes other strings
		settings.setWorkingDirectory("/Users/rambo/working_directory");
		assertTrue(settings.hasWorkingDirectory());
		assertEquals("/Users/rambo/working_directory", settings.getWorkingDirectory());
	}
}
