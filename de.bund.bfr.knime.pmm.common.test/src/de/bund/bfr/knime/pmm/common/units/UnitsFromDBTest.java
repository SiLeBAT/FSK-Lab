package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnitsFromDBTest {

	@Test
	public void testConstructor() {
	
		UnitsFromDB unit = new UnitsFromDB(0, "unit", "description", "name", "kindOfPropertyQuantity",
				"notationCaseSensitive", "convertTo", "conversionFunctionFactor", "inverseConversionFunctionFactor",
				"objectType", "displayInGuiAs", "mathMlString", "priorityForDisplayInGui");
		
		assertEquals(0, unit.id);
		assertEquals("unit", unit.unit);
		assertEquals("description", unit.description);
		assertEquals("name", unit.name);
		assertEquals("kindOfPropertyQuantity", unit.kindOfPropertyQuantity);
		assertEquals("notationCaseSensitive", unit.notationCaseSensitive);
		assertEquals("convertTo", unit.convertTo);
		assertEquals("conversionFunctionFactor", unit.conversionFunctionFactor);
		assertEquals("inverseConversionFunctionFactor", unit.inverseConversionFunctionFactor);
		assertEquals("objectType", unit.objectType);
		assertEquals("displayInGuiAs", unit.displayInGuiAs);
		assertEquals("mathMlString", unit.mathMlString);
		assertEquals("priorityForDisplayInGui", unit.priorityForDisplayInGui);
	}
}
