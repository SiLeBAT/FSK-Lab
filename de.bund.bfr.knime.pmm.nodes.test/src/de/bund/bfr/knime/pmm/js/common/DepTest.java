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
	public void testName() {
		Dep dep = new Dep();
		assertNull(dep.getName());

		dep.setName(name);
		assertEquals(name, dep.getName());
	}

	@Test
	public void testOrigname() {
		Dep dep = new Dep();
		assertNull(dep.getOrigname());

		dep.setOrigname(origname);
		assertEquals(origname, dep.getOrigname());
	}

	@Test
	public void testMin() {
		Dep dep = new Dep();
		assertNull(dep.getMin());

		dep.setMin(min);
		assertEquals(min, dep.getMin(), 0.0);
	}

	@Test
	public void testMax() {
		Dep dep = new Dep();
		assertNull(dep.getMax());

		dep.setMax(max);
		assertEquals(max, dep.getMax(), 0.0);
	}

	@Test
	public void testCategory() {
		Dep dep = new Dep();
		assertNull(dep.getCategory());
		
		dep.setCategory(category);
		assertEquals(category, dep.getCategory());
	}

	@Test
	public void testUnit() {
		Dep dep = new Dep();
		assertNull(dep.getUnit());
		
		dep.setUnit(unit);
		assertEquals(unit, dep.getUnit());
	}

	@Test
	public void testDescription() {
		Dep dep = new Dep();
		assertNull(dep.getDescription());
		
		dep.setDescription(description);
		assertEquals(description, dep.getDescription());
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		Dep dep = new Dep();
		dep.setName(name);
		dep.setOrigname(origname);
		dep.setMin(min);
		dep.setMax(max);
		dep.setCategory(category);
		dep.setUnit(unit);
		dep.setDescription(description);
		
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
		
		assertEquals(name, dep.getName());
		assertEquals(origname, dep.getOrigname());
		assertEquals(min, dep.getMin(), 0.0);
		assertEquals(max, dep.getMax(), 0.0);
		assertEquals(category, dep.getCategory());
		assertEquals(unit, dep.getUnit());
		assertEquals(description, dep.getDescription());
	}
	
	@Test
	public void testToDep() {
		DepXml depXml = new DepXml(name, origname, category, unit, description);
		depXml.min = min;
		depXml.max = max;
		
		Dep dep = Dep.toDep(depXml);
		assertEquals(name, dep.getName());
		assertEquals(origname, dep.getOrigname());
		assertEquals(min, dep.getMin(), 0.0);
		assertEquals(max, dep.getMax(), 0.0);
		assertEquals(category, dep.getCategory());
		assertEquals(unit, dep.getUnit());
		assertEquals(description, dep.getDescription());
	}
	
	@Test
	public void testToDepXml() {
		Dep dep = new Dep();
		dep.setName(name);
		dep.setOrigname(origname);
		dep.setMin(min);
		dep.setMax(max);
		dep.setCategory(category);
		dep.setUnit(unit);
		dep.setDescription(description);
		
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
