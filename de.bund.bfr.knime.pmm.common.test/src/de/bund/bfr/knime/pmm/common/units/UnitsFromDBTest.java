package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnitsFromDBTest {

	@Test
	public void testConstructor() {
		UnitsFromDB unit = new UnitsFromDB();
		
		assertEquals(0, unit.getId());
		assertNull(unit.getUnit());
		assertNull(unit.getDescription());
		assertNull(unit.getName());
		assertNull(unit.getKind_of_property_quantity());
		assertNull(unit.getNotation_case_sensitive());
		assertNull(unit.getConvert_to());
		assertNull(unit.getConversion_function_factor());
		assertNull(unit.getInverse_conversion_function_factor());
		assertNull(unit.getObject_type());
		assertNull(unit.getDisplay_in_GUI_as());
		assertNull(unit.getMathML_string());
		assertNull(unit.getPriority_for_display_in_GUI());
	}

}
