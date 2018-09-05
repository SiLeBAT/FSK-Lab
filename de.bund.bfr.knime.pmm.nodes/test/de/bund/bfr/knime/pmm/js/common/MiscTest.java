package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.*;

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
		assertTrue(id == misc.getId());
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
		
		assertTrue(id == settings.getInt(Misc.ID));
		assertEquals(name, settings.getString(Misc.NAME));
		assertEquals(description, settings.getString(Misc.DESCRIPTION));
		assertEquals(value, settings.getDouble(Misc.VALUE), 0.0);
		assertEquals(unit, settings.getString(Misc.UNIT));
		assertEquals(origUnit, settings.getString(Misc.ORIGUNIT));
		assertEquals(dbuuid, settings.getString(Misc.DBUUID));
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(Misc.ID, id);
		settings.addString(Misc.NAME, name);
		settings.addString(Misc.DESCRIPTION, description);
		settings.addDouble(Misc.VALUE, value);
		settings.addStringArray(Misc.CATEGORY, categories);
		settings.addString(Misc.UNIT, unit);
		settings.addString(Misc.ORIGUNIT, origUnit);
		settings.addString(Misc.DBUUID, dbuuid);
		
		Misc misc = new Misc();
		misc.loadFromNodeSettings(settings);
		
		assertTrue(id == misc.getId());
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
		
		assertTrue(id == misc.getId());
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
		
		assertTrue(id == miscXml.getId());
		assertEquals(name, miscXml.getName());
		assertEquals(description, miscXml.getDescription());
		assertEquals(value, miscXml.getValue(), 0.0);
		assertEquals(Arrays.asList(categories), miscXml.getCategories());
		assertEquals(unit, miscXml.getUnit());
		assertEquals(origUnit, miscXml.getOrigUnit());
		assertEquals(dbuuid, miscXml.getDbuuid());
	}
}
