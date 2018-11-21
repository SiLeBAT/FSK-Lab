package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class UnitListTest {
	
	static Unit unit;
	
	static {
		unit = new Unit();
		unit.id = UnitTest.id;
		unit.unit = UnitTest.unitString;
		unit.description = UnitTest.description;
		unit.name = UnitTest.name;
		unit.kind_of_property_quantity = UnitTest.kindOfPropertyQuantity;
		unit.notation_case_sensitive = UnitTest.notationCaseSensitive;
		unit.convert_to = UnitTest.convertTo;
		unit.conversion_function_factor = UnitTest.conversionFunctionFactor;
		unit.inverse_conversion_function_factor = UnitTest.inversionConversionFunctionFactor;
		unit.object_type = UnitTest.objectType;
		unit.display_in_GUI_as = UnitTest.displayInGuiAs;
		unit.mathML_string = UnitTest.mathMlString;
		unit.priority_for_display_in_GUI = UnitTest.priorityForDisplayInGui;
	}

	@Test
	public void testUnits() {
		UnitList list = new UnitList();
		assertNull(list.getUnits());
		
		list.setUnits(new Unit[] { unit });
		
		Unit expected = unit;  // expected Unit
		Unit obtained = list.getUnits()[0];  // obtained Unit
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.kind_of_property_quantity, obtained.kind_of_property_quantity);
		assertEquals(expected.notation_case_sensitive, obtained.notation_case_sensitive);
		assertEquals(expected.convert_to, obtained.convert_to);
		assertEquals(expected.conversion_function_factor, obtained.conversion_function_factor);
		assertEquals(expected.inverse_conversion_function_factor, obtained.inverse_conversion_function_factor);
		assertEquals(expected.object_type, obtained.object_type);
		assertEquals(expected.display_in_GUI_as, obtained.display_in_GUI_as);
		assertEquals(expected.mathML_string, obtained.mathML_string);
		assertEquals(expected.priority_for_display_in_GUI, obtained.priority_for_display_in_GUI);
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		UnitList list = new UnitList();
		list.setUnits(new Unit[] { unit });
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);
		
		assertEquals(1, settings.getInt("numUnits"));
		
		Unit expected = unit;  // expected unit
		Unit obtained = new Unit();  // obtained unit
		obtained.loadFromNodeSettings(settings.getNodeSettings("units0"));
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.kind_of_property_quantity, obtained.kind_of_property_quantity);
		assertEquals(expected.notation_case_sensitive, obtained.notation_case_sensitive);
		assertEquals(expected.convert_to, obtained.convert_to);
		assertEquals(expected.conversion_function_factor, obtained.conversion_function_factor);
		assertEquals(expected.inverse_conversion_function_factor, obtained.inverse_conversion_function_factor);
		assertEquals(expected.object_type, obtained.object_type);
		assertEquals(expected.display_in_GUI_as, obtained.display_in_GUI_as);
		assertEquals(expected.mathML_string, obtained.mathML_string);
		assertEquals(expected.priority_for_display_in_GUI, obtained.priority_for_display_in_GUI);
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numUnits", 1);
		unit.saveToNodeSettings(settings.addNodeSettings("units0"));
		
		UnitList list = new UnitList();
		list.loadFromNodeSettings(settings);
		
		Unit expected = unit;  // expected Unit
		Unit obtained = list.getUnits()[0];  // obtained Unit
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.kind_of_property_quantity, obtained.kind_of_property_quantity);
		assertEquals(expected.notation_case_sensitive, obtained.notation_case_sensitive);
		assertEquals(expected.convert_to, obtained.convert_to);
		assertEquals(expected.conversion_function_factor, obtained.conversion_function_factor);
		assertEquals(expected.inverse_conversion_function_factor, obtained.inverse_conversion_function_factor);
		assertEquals(expected.object_type, obtained.object_type);
		assertEquals(expected.display_in_GUI_as, obtained.display_in_GUI_as);
		assertEquals(expected.mathML_string, obtained.mathML_string);
		assertEquals(expected.priority_for_display_in_GUI, obtained.priority_for_display_in_GUI);
	}
}
