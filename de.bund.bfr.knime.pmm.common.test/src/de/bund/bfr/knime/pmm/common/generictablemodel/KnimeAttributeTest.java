package de.bund.bfr.knime.pmm.common.generictablemodel;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.PmmException;

public class KnimeAttributeTest {

	@Test
	public void testStringAttribute() {
		
		KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_STRING);
		assertEquals("attr", attribute.getName());
		assertEquals(KnimeAttribute.TYPE_STRING, attribute.getType());
		
		assertFalse(attribute.isDouble());
		assertFalse(attribute.isInt());
	}
	
	@Test
	public void testIntAttribute() {
	
		KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_INT);
		assertEquals("attr", attribute.getName());
		assertEquals(KnimeAttribute.TYPE_INT, attribute.getType());
		
		assertFalse(attribute.isDouble());
		assertTrue(attribute.isInt());
	}

	@Test
	public void testDoubleAttribute() {
	
		KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_DOUBLE);
		assertEquals("attr", attribute.getName());
		assertEquals(KnimeAttribute.TYPE_DOUBLE, attribute.getType());
		
		assertTrue(attribute.isDouble());
		assertFalse(attribute.isInt());
	}

	@Test
	public void testXmlAttribute() {
	
		KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_XML);
		assertEquals("attr", attribute.getName());
		assertEquals(KnimeAttribute.TYPE_XML, attribute.getType());
		
		assertFalse(attribute.isDouble());
		assertFalse(attribute.isInt());
	}
	
	@Test(expected = PmmException.class)
	public void testNullName() {
		new KnimeAttribute(null, KnimeAttribute.TYPE_INT);
	}
	
	@Test(expected = PmmException.class)
	public void testEmptyName() {
		new KnimeAttribute("", KnimeAttribute.TYPE_INT);
	}
	
	@Test(expected = PmmException.class)
	public void testWrongType1() {
		new KnimeAttribute("attr", -1);
	}

	@Test(expected = PmmException.class)
	public void testWrongType2() {
		new KnimeAttribute("attr", 8);
	}
}
