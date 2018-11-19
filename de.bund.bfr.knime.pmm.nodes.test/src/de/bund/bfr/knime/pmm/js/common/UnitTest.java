package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class UnitTest {

	static int id = 1;
	static String unitString = "log10(Anzahl pro g)";
	static String description = "log10 Anzahl (Zellen, Partikel, ...) pro Gramm (log10 Anzahl/g)";
	static String name = "log10(number of objects per g)";
	static String kindOfPropertyQuantity = "Number Content (count/mass)";
	static String notationCaseSensitive = "log10(1/g)";
	static String convertTo = "1/g";
	static String conversionFunctionFactor = "10^x";
	static String inversionConversionFunctionFactor = "log10(x)";
	static String objectType = "not_CFU";
	static String displayInGuiAs = "log10(count/g)";
	static String mathMlString = "<unitDefinition id=\"log10_item_g\"><annotation><transformation name=\"log10\" xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"/></annotation><listOfUnits><unit kind=\"item\"/><unit exponent=\"-1\" kind=\"gram\"/></listOfUnits></unitDefinition>";
	static String priorityForDisplayInGui = "false";

	@Test
	public void testId() {
		Unit unit = new Unit();
		assertNull(unit.getId());

		unit.setId(id);
		assertEquals(id, unit.getId().intValue());
	}

	@Test
	public void testUnit() {
		Unit unit = new Unit();
		assertNull(unit.getUnit());

		unit.setUnit(unitString);
		assertEquals(unitString, unit.getUnit());
	}

	@Test
	public void testDescription() {
		Unit unit = new Unit();
		assertNull(unit.getDescription());

		unit.setDescription(description);
		assertEquals(description, unit.getDescription());
	}

	@Test
	public void testName() {
		Unit unit = new Unit();
		assertNull(unit.getName());

		unit.setName(name);
		assertEquals(name, unit.getName());
	}

	@Test
	public void testKindOfProperty() {
		Unit unit = new Unit();
		assertNull(unit.getKindOfPropertyQuantity());

		unit.setKindOfPropertyQuantity(kindOfPropertyQuantity);
		assertEquals(kindOfPropertyQuantity, unit.getKindOfPropertyQuantity());
	}

	@Test
	public void testNotationCaseSensitive() {
		Unit unit = new Unit();
		assertNull(unit.getNotationCaseSensitive());

		unit.setNotationCaseSensitive(notationCaseSensitive);
		assertEquals(notationCaseSensitive, unit.getNotationCaseSensitive());
	}

	@Test
	public void testConvertTo() {
		Unit unit = new Unit();
		assertNull(unit.getConvertTo());

		unit.setConvertTo(convertTo);
		assertEquals(convertTo, unit.getConvertTo());
	}

	@Test
	public void testConversionFunctionFactor() {
		Unit unit = new Unit();
		assertNull(unit.getConversionFunctionFactor());

		unit.setConversionFunctionFactor(conversionFunctionFactor);
		assertEquals(conversionFunctionFactor, unit.getConversionFunctionFactor());
	}

	@Test
	public void testInverseConversionFunction() {
		Unit unit = new Unit();
		assertNull(unit.getInverseConversionFunctionFactor());

		unit.setInverseConversionFunctionFactor(inversionConversionFunctionFactor);
		assertEquals(inversionConversionFunctionFactor, unit.getInverseConversionFunctionFactor());
	}

	@Test
	public void testObjectType() {
		Unit unit = new Unit();
		assertNull(unit.getObjectType());

		unit.setObjectType(objectType);
		assertEquals(objectType, unit.getObjectType());
	}

	@Test
	public void testDisplayInGuiAs() {
		Unit unit = new Unit();
		assertNull(unit.getDisplayInGuiAs());

		unit.setDisplayInGuiAs(displayInGuiAs);
		assertEquals(displayInGuiAs, unit.getDisplayInGuiAs());
	}

	@Test
	public void testMahthMlString() {
		Unit unit = new Unit();
		assertNull(unit.getMathMLString());

		unit.setMathMLString(mathMlString);
		assertEquals(mathMlString, unit.getMathMLString());
	}

	@Test
	public void testPriorityForDisplayInGui() {
		Unit unit = new Unit();
		assertNull(unit.getPriorityForDisplayInGui());

		unit.setPriorityForDisplayInGui(priorityForDisplayInGui);
		assertEquals(priorityForDisplayInGui, unit.getPriorityForDisplayInGui());
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Unit unit = new Unit();
		unit.setId(id);
		unit.setUnit(unitString);
		unit.setDescription(description);
		unit.setName(name);
		unit.setKindOfPropertyQuantity(kindOfPropertyQuantity);
		unit.setNotationCaseSensitive(notationCaseSensitive);
		unit.setConvertTo(convertTo);
		unit.setConversionFunctionFactor(conversionFunctionFactor);
		unit.setInverseConversionFunctionFactor(inversionConversionFunctionFactor);
		unit.setObjectType(objectType);
		unit.setDisplayInGuiAs(displayInGuiAs);
		unit.setMathMLString(mathMlString);
		unit.setPriorityForDisplayInGui(priorityForDisplayInGui);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		unit.saveToNodeSettings(settings);
		
		assertEquals(id, settings.getInt("id"));
		assertEquals(unitString, settings.getString("unit"));
		assertEquals(description, settings.getString("description"));
		assertEquals(kindOfPropertyQuantity, settings.getString("kindOfPropertyQuantity"));
		assertEquals(notationCaseSensitive, settings.getString("notationCaseSensitive"));
		assertEquals(convertTo, settings.getString("convertTo"));
		assertEquals(conversionFunctionFactor, settings.getString("conversionFunctionFactor"));
		assertEquals(inversionConversionFunctionFactor, settings.getString("inversionConversionFunctionFactor"));
		assertEquals(objectType, settings.getString("objectType"));
		assertEquals(displayInGuiAs, settings.getString("displayInGuiAs"));
		assertEquals(mathMlString, settings.getString("mathmlString"));
		assertEquals(priorityForDisplayInGui, settings.getString("priorityForDisplayInGui"));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("unit", unitString);
		settings.addString("description", description);
		settings.addString("name", name);
		settings.addString("kindOfPropertyQuantity", kindOfPropertyQuantity);
		settings.addString("notationCaseSensitive", notationCaseSensitive);
		settings.addString("convertTo", convertTo);
		settings.addString("conversionFunctionFactor", conversionFunctionFactor);
		settings.addString("inversionConversionFunctionFactor", inversionConversionFunctionFactor);
		settings.addString("objectType", objectType);
		settings.addString("displayInGuiAs", displayInGuiAs);
		settings.addString("mathmlString", mathMlString);
		settings.addString("priorityForDisplayInGui", priorityForDisplayInGui);
		
		Unit unit = new Unit();
		unit.loadFromNodeSettings(settings);
		
		assertEquals(id, unit.getId().intValue());
		assertEquals(name, unit.getName());
		assertEquals(description, unit.getDescription());
		assertEquals(kindOfPropertyQuantity, unit.getKindOfPropertyQuantity());
		assertEquals(notationCaseSensitive, unit.getNotationCaseSensitive());
		assertEquals(convertTo, unit.getConvertTo());
		assertEquals(conversionFunctionFactor, unit.getConversionFunctionFactor());
		assertEquals(inversionConversionFunctionFactor, unit.getInverseConversionFunctionFactor());
		assertEquals(objectType, unit.getObjectType());
		assertEquals(displayInGuiAs, unit.getDisplayInGuiAs());
		assertEquals(mathMlString, unit.getMathMLString());
		assertEquals(priorityForDisplayInGui, unit.getPriorityForDisplayInGui());
	}
}
