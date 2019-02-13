package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

public class XmlHelperTest {

	@Test
	public void testGetString() {
		
		// null Element
		assertNull(XmlHelper.getString(null, ""));
		
		// Element without attribute
		Element element = new Element("element");
		assertNull(XmlHelper.getString(element, ""));
		
		// Element with empty attribute
		element.setAttribute("drink", "");
		assertNull(XmlHelper.getString(element, "drink"));
		
		// Element with non empty attribute
		element.setAttribute("drink", "martini");
		assertEquals("martini", XmlHelper.getString(element, "drink"));
	}
	
	@Test
	public void testGetInt() {
		
		// null Element
		assertNull(XmlHelper.getInt(null, ""));
		
		// Element without attribute
		Element element = new Element("element");
		assertNull(XmlHelper.getInt(element, ""));
		
		// Element with empty attribute
		element.setAttribute("age", "");
		assertNull(XmlHelper.getInt(element, "age"));
		
		// Element with non empty attribute
		element.setAttribute("age", "18");
		assertEquals(18, XmlHelper.getInt(element, "age").intValue());
		
		// Element with non empty attribute that is not an integer
		element.setAttribute("age", "not a number");
		assertNull(XmlHelper.getInt(element, "age"));
	}
	
	@Test
	public void testGetDouble() {
		
		// null Element
		assertNull(XmlHelper.getDouble(null, ""));
		
		// Element without attribute
		Element element = new Element("element");
		assertNull(XmlHelper.getDouble(element, "price"));
		
		// Element with empty attribute
		element.setAttribute("price", "");
		assertNull(XmlHelper.getDouble(element, "price"));
		
		// Element with non empty attribute
		element.setAttribute("price", "0.99");
		assertEquals(0.99, XmlHelper.getDouble(element, "price"), 0.0);
		
		// Element with non empty attribute that is not a double
		element.setAttribute("price", "not a number");
		assertNull(XmlHelper.getDouble(element, "price"));
	}
	
	@Test
	public void testGetBoolean() {
		
		// null Element
		assertNull(XmlHelper.getBoolean(null, ""));
		
		// Element without attribute
		Element element = new Element("element");
		assertNull(XmlHelper.getBoolean(element, "available"));
		
		// Element with empty attribute
		element.setAttribute("available", "");
		assertNull(XmlHelper.getBoolean(element, "available"));
		
		// Element with non empty attribute
		element.setAttribute("available", "false");
		assertFalse(XmlHelper.getBoolean(element, "available"));
	}
	
	@Test
	public void testGetNonNull() {
		assertTrue(XmlHelper.getNonNull(null).isEmpty());
		assertTrue(XmlHelper.getNonNull(Double.NaN).isEmpty());
		assertEquals("hello", XmlHelper.getNonNull("hello"));
	}
}
