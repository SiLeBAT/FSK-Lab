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
		assertEquals("name", indep0.getName());
		assertEquals("name", indep0.getOrigName());
		assertEquals(0.0, indep0.getMin(), .0);
		assertEquals(1.0, indep0.getMax(), .0);
		assertNull(indep0.getCategory());
		assertNull(indep0.getUnit());
		assertNull(indep0.getDescription());
		
		// Test constructor with name, min, max, category and unit
		IndepXml indep1 = new IndepXml("name", 0.0, 1.0, "category", "unit");
		assertEquals("name", indep1.getName());
		assertEquals("name", indep1.getOrigName());
		assertEquals(0.0, indep1.getMin(), .0);
		assertEquals(1.0, indep1.getMax(), .0);
		assertEquals("category", indep1.getCategory());
		assertEquals("unit", indep1.getUnit());
		assertNull(indep1.getDescription());
		
		// Test fully parameterized constructor
		IndepXml indep2 = new IndepXml("name", "origName", 0.0, 1.0, "category", "unit", "description");
		assertEquals("name", indep2.getName());
		assertEquals("origName", indep2.getOrigName());
		assertEquals(0.0, indep2.getMin(), .0);
		assertEquals(1.0, indep2.getMax(), .0);
		assertEquals("category", indep2.getCategory());
		assertEquals("unit", indep2.getUnit());
		assertEquals("description", indep2.getDescription());
		
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
		assertEquals("name", indep3.getName());
		assertEquals("origName", indep3.getOrigName());
		assertEquals(0.0, indep3.getMin(), .0);
		assertEquals(1.0, indep3.getMax(), .0);
		assertEquals("category", indep3.getCategory());
		assertEquals("unit", indep3.getUnit());
		assertEquals("description", indep3.getDescription());
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
