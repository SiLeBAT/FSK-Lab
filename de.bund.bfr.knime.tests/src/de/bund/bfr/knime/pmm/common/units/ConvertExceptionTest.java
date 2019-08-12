package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("static-method")
public class ConvertExceptionTest {

	@Test
	public void test() {
		final ConvertException exception = new ConvertException("fromUnit", "toUnit");
		assertEquals("fromUnit", exception.fromUnit);
		assertEquals("toUnit", exception.toUnit);
	}
}
