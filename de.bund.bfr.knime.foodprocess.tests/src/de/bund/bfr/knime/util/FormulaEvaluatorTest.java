package de.bund.bfr.knime.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormulaEvaluatorTest {

	@Test
	public void testIsNumber() {
		assertFalse(FormulaEvaluator.isNumber(null));
		assertTrue(FormulaEvaluator.isNumber("3.14"));
		assertFalse(FormulaEvaluator.isNumber("Not a number"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetDoubleNull() {
		FormulaEvaluator.getDouble(null);
	}
	
	@Test(expected = NumberFormatException.class)
	public void testGetDoubleNA() {
		FormulaEvaluator.getDouble("Not a number");
	}
	
	@Test
	public void testGetDouble() {
		assertEquals(3.14, FormulaEvaluator.getDouble("3.14"), .0);
	}
	
	@Test
	public void testGetSeconds() {
		assertEquals(1, (int)FormulaEvaluator.getSeconds(null));
		assertEquals(60, (int)FormulaEvaluator.getSeconds("min"));
		assertEquals(3600, (int)FormulaEvaluator.getSeconds("h"));
		assertEquals(86400, (int)FormulaEvaluator.getSeconds("d"));
		assertEquals(2592000, (int)FormulaEvaluator.getSeconds("m"));
		assertEquals(31536000, (int)FormulaEvaluator.getSeconds("y"));
	}
	
	@Test
	public void testGetGrams() {
		assertEquals(1, (int)FormulaEvaluator.getGrams(null));
		
		assertEquals(1000, (int)FormulaEvaluator.getGrams("kg"));
		assertEquals(1000, (int)FormulaEvaluator.getGrams("KG"));
		
		assertEquals(1000000, (int)FormulaEvaluator.getGrams("t"));
		assertEquals(1000000, (int)FormulaEvaluator.getGrams("T"));
		
		assertEquals(Math.pow(10, -12), FormulaEvaluator.getGrams("cfu"), .0);
		assertEquals(Math.pow(10, -12), FormulaEvaluator.getGrams("CFU"), .0);
	}
}
