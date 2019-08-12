package de.bund.bfr.knime.pmm.common.generictablemodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.PmmException;

@SuppressWarnings("static-method")
public class KnimeAttributeTest {

	@Test
	public void testStringAttribute() {

		final KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_STRING);
		assertEquals("attr", attribute.name);
		assertEquals(KnimeAttribute.TYPE_STRING, attribute.type);

		assertFalse(attribute.isDouble());
		assertFalse(attribute.isInt());
	}

	@Test
	public void testIntAttribute() {

		final KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_INT);
		assertEquals("attr", attribute.name);
		assertEquals(KnimeAttribute.TYPE_INT, attribute.type);

		assertFalse(attribute.isDouble());
		assertTrue(attribute.isInt());
	}

	@Test
	public void testDoubleAttribute() {

		final KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_DOUBLE);
		assertEquals("attr", attribute.name);
		assertEquals(KnimeAttribute.TYPE_DOUBLE, attribute.type);

		assertTrue(attribute.isDouble());
		assertFalse(attribute.isInt());
	}

	@Test
	public void testXmlAttribute() {

		final KnimeAttribute attribute = new KnimeAttribute("attr", KnimeAttribute.TYPE_XML);
		assertEquals("attr", attribute.name);
		assertEquals(KnimeAttribute.TYPE_XML, attribute.type);

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
