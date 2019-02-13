package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConvertExceptionTest {

	@Test
	public void test() {
		ConvertException exception = new ConvertException("fromUnit", "toUnit");
		assertEquals("fromUnit", exception.fromUnit);
		assertEquals("toUnit", exception.toUnit);
	}
}
