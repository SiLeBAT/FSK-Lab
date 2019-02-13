package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class NoCategoryTest {

	private static Category noCategory = new NoCategory();

	@Test
	public void testGetName() {
		assertEquals("No Category", noCategory.getName());
	}
	
	@Test
	public void testGetAllUnits() {
		assertEquals(Arrays.asList("No Unit"), noCategory.getAllUnits());
	}
	
	@Test
	public void testGetStandardUnit() {
		assertEquals("No Unit", noCategory.getStandardUnit());
	}
	
	@Test
	public void testConvert() {
		assertEquals(0.0, noCategory.convert(0.0, "", ""), 0.0);
	}
	
	@Test
	public void testGetConversionString() {
		assertEquals("var", noCategory.getConversionString("var", "", ""));
	}
	
	@Test
	public void testGetSBML() {
		assertNull(noCategory.getSBML("unit"));
	}
}
