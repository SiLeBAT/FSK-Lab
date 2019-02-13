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
	public void testConstructor() {
		Unit unit = new Unit();
		assertNull(unit.id);
		assertNull(unit.unit);
		assertNull(unit.description);
		assertNull(unit.name);
		assertNull(unit.kind_of_property_quantity);
		assertNull(unit.notation_case_sensitive);
		assertNull(unit.convert_to);
		assertNull(unit.conversion_function_factor);
		assertNull(unit.inverse_conversion_function_factor);
		assertNull(unit.object_type);
		assertNull(unit.display_in_GUI_as);
		assertNull(unit.mathML_string);
		assertNull(unit.priority_for_display_in_GUI);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Unit unit = new Unit();
		unit.id = id;
		unit.unit = unitString;
		unit.description = description;
		unit.name = name;
		unit.kind_of_property_quantity = kindOfPropertyQuantity;
		unit.notation_case_sensitive = notationCaseSensitive;
		unit.convert_to = convertTo;
		unit.conversion_function_factor = conversionFunctionFactor;
		unit.inverse_conversion_function_factor = inversionConversionFunctionFactor;
		unit.object_type = objectType;
		unit.display_in_GUI_as = displayInGuiAs;
		unit.mathML_string = mathMlString;
		unit.priority_for_display_in_GUI = priorityForDisplayInGui;

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
		
		assertEquals(id, unit.id.intValue());
		assertEquals(name, unit.name);
		assertEquals(description, unit.description);
		assertEquals(kindOfPropertyQuantity, unit.kind_of_property_quantity);
		assertEquals(notationCaseSensitive, unit.notation_case_sensitive);
		assertEquals(convertTo, unit.convert_to);
		assertEquals(conversionFunctionFactor, unit.conversion_function_factor);
		assertEquals(inversionConversionFunctionFactor, unit.inverse_conversion_function_factor);
		assertEquals(objectType, unit.object_type);
		assertEquals(displayInGuiAs, unit.display_in_GUI_as);
		assertEquals(mathMlString, unit.mathML_string);
		assertEquals(priorityForDisplayInGui, unit.priority_for_display_in_GUI);
	}
}
