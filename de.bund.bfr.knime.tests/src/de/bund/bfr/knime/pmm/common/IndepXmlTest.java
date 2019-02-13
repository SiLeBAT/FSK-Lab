package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class IndepXmlTest {

	@Test
	public void testConstructors() {
		
		// Test constructor with name, min and max
		IndepXml indep0 = new IndepXml("name", 0.0, 1.0);
		assertEquals("name", indep0.name);
		assertEquals("name", indep0.origName);
		assertEquals(0.0, indep0.min, .0);
		assertEquals(1.0, indep0.max, .0);
		assertNull(indep0.category);
		assertNull(indep0.unit);
		assertNull(indep0.description);
		
		// Test constructor with name, min, max, category and unit
		IndepXml indep1 = new IndepXml("name", 0.0, 1.0, "category", "unit");
		assertEquals("name", indep1.name);
		assertEquals("name", indep1.origName);
		assertEquals(0.0, indep1.min, .0);
		assertEquals(1.0, indep1.max, .0);
		assertEquals("category", indep1.category);
		assertEquals("unit", indep1.unit);
		assertNull(indep1.description);
		
		// Test fully parameterized constructor
		IndepXml indep2 = new IndepXml("name", "origName", 0.0, 1.0, "category", "unit", "description");
		assertEquals("name", indep2.name);
		assertEquals("origName", indep2.origName);
		assertEquals(0.0, indep2.min, .0);
		assertEquals(1.0, indep2.max, .0);
		assertEquals("category", indep2.category);
		assertEquals("unit", indep2.unit);
		assertEquals("description", indep2.description);
		
		// Test copy constructor with Element
		Element element = new Element(IndepXml.ELEMENT_INDEP);
		element.setAttribute("name", "name");
		element.setAttribute("origname", "origName");
		element.setAttribute("min", "0.0");
		element.setAttribute("max", "1.0");
		element.setAttribute("category", "category");
		element.setAttribute("unit", "unit");
		element.setAttribute("description", "description");
		
		IndepXml indep3 = new IndepXml(element);
		assertEquals("name", indep3.name);
		assertEquals("origName", indep3.origName);
		assertEquals(0.0, indep3.min, .0);
		assertEquals(1.0, indep3.max, .0);
		assertEquals("category", indep3.category);
		assertEquals("unit", indep3.unit);
		assertEquals("description", indep3.description);
	}
	
	@Test
	public void testToXmlElement() throws DataConversionException {
		
		IndepXml indep = new IndepXml("name", "origName", 0.0, 1.0, "category", "unit", "description");
		Element element = indep.toXmlElement();
		
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("origName", element.getAttributeValue("origname"));
		assertEquals(0.0, element.getAttribute("min").getDoubleValue(), .0);
		assertEquals(1.0, element.getAttribute("max").getDoubleValue(), .0);
		assertEquals("category", element.getAttributeValue("category"));
		assertEquals("unit", element.getAttributeValue("unit"));
		assertEquals("description", element.getAttributeValue("description"));
	}
}
