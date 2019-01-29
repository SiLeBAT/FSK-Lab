package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnitsFromDBTest {

	@Test
	public void testConstructor() {
		UnitsFromDB unit = new UnitsFromDB();
		
		assertEquals(0, unit.id);
		assertNull(unit.unit);
		assertNull(unit.description);
		assertNull(unit.name);
		assertNull(unit.kindOfPropertyQuantity);
		assertNull(unit.notationCaseSensitive);
		assertNull(unit.convertTo);
		assertNull(unit.conversionFunctionFactor);
		assertNull(unit.inverseConversionFunctionFactor);
		assertNull(unit.objectType);
		assertNull(unit.displayInGuiAs);
		assertNull(unit.mathMlString);
		assertNull(unit.priorityForDisplayInGui);
	}

}
