package de.bund.bfr.knime.util;

import static org.junit.Assert.*;

import de.bund.bfr.knime.pmm.common.units.Categories;
import org.junit.Test;

@SuppressWarnings("static-method")
public class ValueAndUnitTest {
	
	@Test
	public void test() throws Exception {
		ValueAndUnit valueAndUnit = new ValueAndUnit(1.0, "unit", "object");
		
		assertEquals(1.0, valueAndUnit.getValue(), .0);
		assertEquals("unit", valueAndUnit.getUnit());
		assertEquals("object", valueAndUnit.getObject());
		assertEquals("unit", valueAndUnit.getObjectUnit());
		assertEquals(Categories.NO_CATEGORY, valueAndUnit.getCategory().getName());
		
		ValueAndUnit other = new ValueAndUnit(1.0, "log10(count/g)", "object");
		assertEquals("log10(object/g)", other.getObjectUnit());
	}
}
