package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MiscXml;

public class MiscTest {

	static int id = -1;
	static String name = "Temperature";
	static String description = "Temperature";
	static double value = 10.0;
	static String[] categories = new String[] { "Temperature" };
	static String unit = "°C";
	static String origUnit = "°C";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";

	@Test
	public void testId() {
		Misc misc = new Misc();
		assertNull(misc.getId());

		misc.setId(id);
		assertEquals(id, misc.getId().intValue());
	}

	@Test
	public void testName() {
		Misc misc = new Misc();
		assertNull(misc.getName());
		
		misc.setName(name);
		assertEquals(name, misc.getName());
	}

	@Test
	public void testDescription() {
		Misc misc = new Misc();
		assertNull(misc.getDescription());
		
		misc.setDescription(description);
		assertEquals(description, misc.getDescription());
	}

	@Test
	public void testValue() {
		Misc misc = new Misc();
		assertNull(misc.getValue());
		
		misc.setValue(value);
		assertEquals(value, misc.getValue(), 0.0);
	}

	@Test
	public void testCategories() {
		Misc misc = new Misc();
		assertNull(misc.getCategories());
		
		misc.setCategories(categories);
		assertArrayEquals(categories, misc.getCategories());
	}

	@Test
	public void testUnit() {
		Misc misc = new Misc();
		assertNull(misc.getUnit());
		
		misc.setUnit(unit);
		assertEquals(unit, misc.getUnit());
	}

	@Test
	public void testOrigUnit() {
		Misc misc = new Misc();
		assertNull(misc.getOrigUnit());
		
		misc.setOrigUnit(origUnit);
		assertEquals(origUnit, misc.getOrigUnit());
	}

	@Test
	public void testDbuuid() {
		Misc misc = new Misc();
		assertNull(misc.getDbuuid());
		
		misc.setDbuuid(dbuuid);
		assertEquals(dbuuid, misc.getDbuuid());
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Misc misc = new Misc();
		misc.setId(id);
		misc.setName(name);
		misc.setDescription(description);
		misc.setValue(value);
		misc.setCategories(categories);
		misc.setUnit(unit);
		misc.setOrigUnit(origUnit);
		misc.setDbuuid(dbuuid);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		misc.saveToNodeSettings(settings);
		
		assertEquals(id, settings.getInt("id"));
		assertEquals(name, settings.getString("name"));
		assertEquals(description, settings.getString("description"));
		assertEquals(value, settings.getDouble("value"), 0.0);
		assertEquals(unit, settings.getString("unit"));
		assertEquals(origUnit, settings.getString("origUnit"));
		assertEquals(dbuuid, settings.getString("dbuuid"));
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("name", name);
		settings.addString("description", description);
		settings.addDouble("value", value);
		settings.addStringArray("category", categories);
		settings.addString("unit", unit);
		settings.addString("origUnit", origUnit);
		settings.addString("dbuuid", dbuuid);
		
		Misc misc = new Misc();
		misc.loadFromNodeSettings(settings);
		
		assertEquals(id, misc.getId().intValue());
		assertEquals(id, misc.getId().intValue());
		assertEquals(name, misc.getName());
		assertEquals(description, misc.getDescription());
		assertEquals(value, misc.getValue(), 0.0);
		assertArrayEquals(categories, misc.getCategories());
		assertEquals(unit, misc.getUnit());
		assertEquals(origUnit, misc.getOrigUnit());
		assertEquals(dbuuid, misc.getDbuuid());
	}
	
	@Test
	public void testToMisc() {
		MiscXml miscXml = new MiscXml(id, name, description, value, Arrays.asList(categories), unit, origUnit, dbuuid);
		Misc misc = Misc.toMisc(miscXml);
		
		assertEquals(id, misc.getId().intValue());
		assertEquals(name, misc.getName());
		assertEquals(description, misc.getDescription());
		assertEquals(value, misc.getValue(), 0.0);
		assertArrayEquals(categories, misc.getCategories());
		assertEquals(unit, misc.getUnit());
		assertEquals(origUnit, misc.getOrigUnit());
		assertEquals(dbuuid, misc.getDbuuid());
	}
	
	@Test
	public void testToMiscXml() {
		Misc misc = new Misc();
		misc.setId(id);
		misc.setName(name);
		misc.setDescription(description);
		misc.setValue(value);
		misc.setCategories(categories);
		misc.setUnit(unit);
		misc.setOrigUnit(origUnit);
		misc.setDbuuid(dbuuid);
		MiscXml miscXml = misc.toMiscXml();
		
		assertEquals(id, miscXml.id.intValue());
		assertEquals(name, miscXml.name);
		assertEquals(description, miscXml.description);
		assertEquals(value, miscXml.value, 0.0);
		assertEquals(Arrays.asList(categories), miscXml.categories);
		assertEquals(unit, miscXml.unit);
		assertEquals(origUnit, miscXml.origUnit);
		assertEquals(dbuuid, miscXml.dbuuid);
	}
}
