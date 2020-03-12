package de.bund.bfr.knime.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("static-method")
public class MatrixTest {

	@Test
	public void testNameConstructor() {
		Matrix matrix = new Matrix("name");
		assertEquals(-1, matrix.getId());
		assertEquals("name", matrix.getName());
	}
	
	@Test
	public void testNameIdConstructor() {
		Matrix matrix = new Matrix("name", 0);
		assertEquals(0, matrix.getId());
		assertEquals("name", matrix.getName());
	}
	
	@Test
	public void testIdConstructor() {
		Matrix matrix = new Matrix(4000);
		assertEquals(4000, matrix.getId());
		// DB name is not tested.
	}
}
