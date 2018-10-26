package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

public class CatalogModelXmlTest {

	@Test
	public void testConstructors() throws Exception {
	
		// test constructor with id, name, formula and model class
		CatalogModelXml catalog0 = new CatalogModelXml(0, "name", "formula", 0);
		assertTrue(0 == catalog0.id);
		assertEquals("name", catalog0.name);
		assertEquals("formula", catalog0.formula);
		assertTrue(0 == catalog0.modelClass);
		assertNull(catalog0.comment);
		assertNull(catalog0.dbuuid);
		
		// test constructor with id, name, formula, model class and dbuuid
		CatalogModelXml catalog1 = new CatalogModelXml(0, "name", "formula", 0, "dbuuid");
		assertTrue(0 == catalog1.id);
		assertEquals("name", catalog1.name);
		assertEquals("formula", catalog1.formula);
		assertTrue(0 == catalog1.modelClass);
		assertNull(catalog1.comment);
		assertEquals("dbuuid", catalog1.dbuuid);
		
		// test copy constructor
		Element element = new Element(CatalogModelXml.ELEMENT_CATALOGMODEL);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("formula", "formula");
		element.setAttribute("modelClass", "0");
		element.setAttribute("comment", "comment");
		element.setAttribute("dbuuid", "dbuuid");
		
		CatalogModelXml catalog2 = new CatalogModelXml(element);
		assertTrue(0 == catalog2.id);
		assertEquals("name", catalog2.name);
		assertEquals("formula", catalog2.formula);
		assertTrue(0 == catalog2.modelClass);
		assertEquals("comment", catalog2.comment);
		assertEquals("dbuuid", catalog2.dbuuid);
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		CatalogModelXml catalog = new CatalogModelXml(0, "name", "formula", 0, "dbuuid");
		catalog.comment = "comment";
		Element element = catalog.toXmlElement();
		
		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttribute("name").getValue());
		assertEquals("formula", element.getAttribute("formula").getValue());
		assertTrue(0 == element.getAttribute("modelClass").getIntValue());
		assertEquals("comment", element.getAttribute("comment").getValue());
		assertEquals("dbuuid", element.getAttribute("dbuuid").getValue());
	}
}
