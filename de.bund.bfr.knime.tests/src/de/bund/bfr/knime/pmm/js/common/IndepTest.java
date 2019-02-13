package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.IndepXml;

public class IndepTest {
	
	static String name = "Time";
	static String origname = "Time";
	static double min = 0.0;
	static double max = 554.0;
	static String category = "Time";
	static String unit = "h";
	static String description = "time";

	@Test
	public void testConstructors() {
		Indep indep = new Indep();
		assertNull(indep.name);
		assertNull(indep.origname);
		assertNull(indep.min);
		assertNull(indep.max);
		assertNull(indep.category);
		assertNull(indep.unit);
		assertNull(indep.description);
	}
	
	@Test
	public void testSaveNodeSettings() throws Exception {
		Indep indep = new Indep();
		indep.name = name;
		indep.origname = origname;
		indep.min = min;
		indep.max = max;
		indep.category = category;
		indep.unit = unit;
		indep.description = description;
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		indep.saveToNodeSettings(settings);
		
		assertEquals(name, settings.getString("name"));
		assertEquals(origname, settings.getString("origname"));
		assertEquals(min, settings.getDouble("min"), 0.0);
		assertEquals(max, settings.getDouble("max"), 0.0);
		assertEquals(category, settings.getString("category"));
		assertEquals(unit, settings.getString("unit"));
		assertEquals(description, settings.getString("description"));
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", name);
		settings.addString("origname", origname);
		settings.addDouble("min", min);
		settings.addDouble("max", max);
		settings.addString("category", category);
		settings.addString("unit", unit);
		settings.addString("description", description);
		
		Indep indep = new Indep();
		indep.loadFromNodeSettings(settings);
		
		assertEquals(name, indep.name);
		assertEquals(origname, indep.origname);
		assertEquals(min, indep.min, 0.0);
		assertEquals(max, indep.max, 0.0);
		assertEquals(category, indep.category);
		assertEquals(unit, indep.unit);
		assertEquals(description, indep.description);
	}
	
	@Test
	public void testToIndep() {
		IndepXml indepXml = new IndepXml(name, origname, min, max, category, unit, description);
		Indep indep = Indep.toIndep(indepXml);
		
		assertEquals(name, indep.name);
		assertEquals(origname, indep.origname);
		assertEquals(min, indep.min, 0.0);
		assertEquals(max, indep.max, 0.0);
		assertEquals(category, indep.category);
		assertEquals(unit, indep.unit);
		assertEquals(description, indep.description);
	}
	
	@Test
	public void testToIndepXml() {
		Indep indep = new Indep();
		indep.name = name;
		indep.origname = origname;
		indep.min = min;
		indep.max = max;
		indep.category = category;
		indep.unit = unit;
		indep.description = description;
		IndepXml indepXml = indep.toIndepXml();
		
		assertEquals(name, indepXml.name);
		assertEquals(origname, indepXml.origName);
		assertEquals(min, indepXml.min, 0.0);
		assertEquals(max, indepXml.max, 0.0);
		assertEquals(category, indepXml.category);
		assertEquals(unit, indepXml.unit);
		assertEquals(description, indepXml.description);
	}
}
