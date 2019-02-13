package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.DepXml;

public class DepTest {

	static String name = "Value";
	static String origname = "Value";
	static double min = 0.0;
	static double max = 10.0;
	static String category = "Number Content (count/mass)";
	static String unit = "ln(count/g)";
	static String description = "bacterial population at time t -ln() transformed";

	@Test
	public void testConstructor() {
		Dep dep = new Dep();
		assertNull(dep.name);
		assertNull(dep.origname);
		assertNull(dep.min);
		assertNull(dep.max);
		assertNull(dep.category);
		assertNull(dep.unit);
		assertNull(dep.description);
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		Dep dep = new Dep();
		dep.name = name;
		dep.origname = origname;
		dep.min = min;
		dep.max = max;
		dep.category = category;
		dep.unit = unit;
		dep.description = description;
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		dep.saveToNodeSettings(settings);
		
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
		
		Dep dep = new Dep();
		dep.loadFromNodeSettings(settings);
		
		assertEquals(name, dep.name);
		assertEquals(origname, dep.origname);
		assertEquals(min, dep.min, 0.0);
		assertEquals(max, dep.max, 0.0);
		assertEquals(category, dep.category);
		assertEquals(unit, dep.unit);
		assertEquals(description, dep.description);
	}
	
	@Test
	public void testToDep() {
		DepXml depXml = new DepXml(name, origname, category, unit, description);
		depXml.min = min;
		depXml.max = max;
		
		Dep dep = Dep.toDep(depXml);
		assertEquals(name, dep.name);
		assertEquals(origname, dep.origname);
		assertEquals(min, dep.min, 0.0);
		assertEquals(max, dep.max, 0.0);
		assertEquals(category, dep.category);
		assertEquals(unit, dep.unit);
		assertEquals(description, dep.description);
	}
	
	@Test
	public void testToDepXml() {
		Dep dep = new Dep();
		dep.name = name;
		dep.origname = origname;
		dep.min = min;
		dep.max = max;
		dep.category = category;
		dep.unit = unit;
		dep.description = description;
		
		DepXml depXml = dep.toDepXml();
		assertEquals(name, depXml.name);
		assertEquals(origname, depXml.origName);
		assertEquals(min, depXml.min, 0.0);
		assertEquals(max, depXml.max, 0.0);
		assertEquals(category, depXml.category);
		assertEquals(unit, depXml.unit);
		assertEquals(description, depXml.description);
	}
}
