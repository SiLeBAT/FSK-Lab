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
	public void testName() {
		Indep indep = new Indep();
		assertNull(indep.getName());
		
		indep.setName(name);
		assertEquals(name, indep.getName());
	}

	@Test
	public void testOrigname() {
		Indep indep = new Indep();
		assertNull(indep.getOrigname());
		
		indep.setOrigname(origname);
		assertEquals(origname, indep.getOrigname());
	}

	@Test
	public void testMin() {
		Indep indep = new Indep();
		assertNull(indep.getMin());
		
		indep.setMin(min);
		assertEquals(min, indep.getMin(), 0.0);
	}

	@Test
	public void testMax() {
		Indep indep = new Indep();
		assertNull(indep.getMax());
		
		indep.setMax(max);
		assertEquals(max, indep.getMax(), 0.0);
	}

	@Test
	public void testCategory() {
		Indep indep = new Indep();
		assertNull(indep.getCategory());
		
		indep.setCategory(category);
		assertEquals(category, indep.getCategory());
	}

	@Test
	public void testUnit() {
		Indep indep = new Indep();
		assertNull(indep.getUnit());
		
		indep.setUnit(unit);
		assertEquals(unit, indep.getUnit());
	}

	@Test
	public void testDescription() {
		Indep indep = new Indep();
		assertNull(indep.getDescription());
		
		indep.setDescription(description);
		assertEquals(description, indep.getDescription());
	}
	
	@Test
	public void testSaveNodeSettings() throws Exception {
		Indep indep = new Indep();
		indep.setName(name);
		indep.setOrigname(origname);
		indep.setMin(min);
		indep.setMax(max);
		indep.setCategory(category);
		indep.setUnit(unit);
		indep.setDescription(description);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		indep.saveToNodeSettings(settings);
		
		assertEquals(name, settings.getString(Indep.NAME));
		assertEquals(origname, settings.getString(Indep.ORIGNAME));
		assertEquals(min, settings.getDouble(Indep.MIN), 0.0);
		assertEquals(max, settings.getDouble(Indep.MAX), 0.0);
		assertEquals(category, settings.getString(Indep.CATEGORY));
		assertEquals(unit, settings.getString(Indep.UNIT));
		assertEquals(description, settings.getString(Indep.DESCRIPTION));
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString(Indep.NAME, name);
		settings.addString(Indep.ORIGNAME, origname);
		settings.addDouble(Indep.MIN, min);
		settings.addDouble(Indep.MAX, max);
		settings.addString(Indep.CATEGORY, category);
		settings.addString(Indep.UNIT, unit);
		settings.addString(Indep.DESCRIPTION, description);
		
		Indep indep = new Indep();
		indep.loadFromNodeSettings(settings);
		
		assertEquals(name, indep.getName());
		assertEquals(origname, indep.getOrigname());
		assertEquals(min, indep.getMin(), 0.0);
		assertEquals(max, indep.getMax(), 0.0);
		assertEquals(category, indep.getCategory());
		assertEquals(unit, indep.getUnit());
		assertEquals(description, indep.getDescription());
	}
	
	@Test
	public void testToIndep() {
		IndepXml indepXml = new IndepXml(name, origname, min, max, category, unit, description);
		Indep indep = Indep.toIndep(indepXml);
		
		assertEquals(name, indep.getName());
		assertEquals(origname, indep.getOrigname());
		assertEquals(min, indep.getMin(), 0.0);
		assertEquals(max, indep.getMax(), 0.0);
		assertEquals(category, indep.getCategory());
		assertEquals(unit, indep.getUnit());
		assertEquals(description, indep.getDescription());
	}
	
	@Test
	public void testToIndepXml() {
		Indep indep = new Indep();
		indep.setName(name);
		indep.setOrigname(origname);
		indep.setMin(min);
		indep.setMax(max);
		indep.setCategory(category);
		indep.setUnit(unit);
		indep.setDescription(description);
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
