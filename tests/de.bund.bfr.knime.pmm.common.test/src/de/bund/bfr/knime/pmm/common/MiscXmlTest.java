package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jdom2.Element;
import org.junit.Test;

public class MiscXmlTest {

	@Test
	public void testConstructors() throws Exception {
		
		// Test constructor with id, name, description, value, categories* and unit
		MiscXml misc0 = new MiscXml(0, "name", "description", .0, Arrays.asList("category"), "unit");
		assertTrue(0 == misc0.getId());
		assertEquals("name", misc0.getName());
		assertEquals("description", misc0.getDescription());
		assertEquals(.0, misc0.getValue(), .0);
		assertEquals(Arrays.asList("category"), misc0.getCategories());
		assertEquals("unit", misc0.getUnit());
		assertEquals("unit", misc0.getOrigUnit());
		assertNull(misc0.getDbuuid());
		
		// Test constructor with id, name, description, value, categories*, unit and dbuuid
		MiscXml misc1 = new MiscXml(0, "name", "description", .0, Arrays.asList("category"), "unit", "dbuuid");
		assertTrue(0 == misc1.getId());
		assertEquals("name", misc1.getName());
		assertEquals("description", misc1.getDescription());
		assertEquals(.0, misc1.getValue(), .0);
		assertEquals(Arrays.asList("category"), misc1.getCategories());
		assertEquals("unit", misc1.getUnit());
		assertEquals("unit", misc1.getOrigUnit());
		assertEquals("dbuuid", misc1.getDbuuid());
		
		// Test fully parameterized constructor
		MiscXml misc2 = new MiscXml(0, "name", "description", .0, Arrays.asList("category"), "unit", "origUnit", "dbuuid");
		assertTrue(0 == misc2.getId());
		assertEquals("name", misc2.getName());
		assertEquals("description", misc2.getDescription());
		assertEquals(.0, misc2.getValue(), .0);
		assertEquals(Arrays.asList("category"), misc2.getCategories());
		assertEquals("unit", misc2.getUnit());
		assertEquals("origUnit", misc2.getOrigUnit());
		assertEquals("dbuuid", misc2.getDbuuid());
		
		// Test copy constructor (MatrixXml)
		MiscXml misc3 = new MiscXml(misc2);
		assertTrue(0 == misc3.getId());
		assertEquals("name", misc3.getName());
		assertEquals("description", misc3.getDescription());
		assertEquals(.0, misc3.getValue(), .0);
		assertEquals(Arrays.asList("category"), misc3.getCategories());
		assertEquals("unit", misc3.getUnit());
		assertEquals("origUnit", misc3.getOrigUnit());
		assertEquals("dbuuid", misc3.getDbuuid());
		
		// Test copy constructor (Element)
		Element element = new Element(MiscXml.ELEMENT_MISC);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("description", "description");
		element.setAttribute("value", "0.0");
		element.setAttribute("category0", "category");
		element.setAttribute("unit", "unit");
		element.setAttribute("origUnit", "origUnit");
		element.setAttribute("dbuuid", "dbuuid");
		
		MiscXml misc4 = new MiscXml(element);
		assertTrue(0 == misc4.getId());
		assertEquals("name", misc4.getName());
		assertEquals("description", misc4.getDescription());
		assertEquals(.0, misc4.getValue(), .0);
		assertEquals(Arrays.asList("category"), misc4.getCategories());
		assertEquals("unit", misc4.getUnit());
		assertEquals("origUnit", misc4.getOrigUnit());
		assertEquals("dbuuid", misc4.getDbuuid());
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		MiscXml misc = new MiscXml(0, "name", "description", .0, Arrays.asList("category"), "unit", "origUnit", "dbuuid");
		Element element = misc.toXmlElement();
		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("description", element.getAttributeValue("description"));
		assertEquals(.0, element.getAttribute("value").getDoubleValue(), .0);
		assertEquals("category", element.getAttributeValue("category0"));
		assertEquals("unit", element.getAttributeValue("unit"));
		assertEquals("origUnit", element.getAttributeValue("origUnit"));
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}
}
