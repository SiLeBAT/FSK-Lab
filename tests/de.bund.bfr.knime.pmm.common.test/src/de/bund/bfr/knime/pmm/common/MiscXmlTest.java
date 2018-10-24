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
		assertTrue(0 == misc0.id);
		assertEquals("name", misc0.name);
		assertEquals("description", misc0.description);
		assertEquals(.0, misc0.value, .0);
		assertEquals(Arrays.asList("category"), misc0.categories);
		assertEquals("unit", misc0.unit);
		assertEquals("unit", misc0.origUnit);
		assertNull(misc0.dbuuid);
		
		// Test constructor with id, name, description, value, categories*, unit and dbuuid
		MiscXml misc1 = new MiscXml(0, "name", "description", .0, Arrays.asList("category"), "unit", "dbuuid");
		assertTrue(0 == misc1.id);
		assertEquals("name", misc1.name);
		assertEquals("description", misc1.description);
		assertEquals(.0, misc1.value, .0);
		assertEquals(Arrays.asList("category"), misc1.categories);
		assertEquals("unit", misc1.unit);
		assertEquals("unit", misc1.origUnit);
		assertEquals("dbuuid", misc1.dbuuid);
		
		// Test fully parameterized constructor
		MiscXml misc2 = new MiscXml(0, "name", "description", .0, Arrays.asList("category"), "unit", "origUnit", "dbuuid");
		assertTrue(0 == misc2.id);
		assertEquals("name", misc2.name);
		assertEquals("description", misc2.description);
		assertEquals(.0, misc2.value, .0);
		assertEquals(Arrays.asList("category"), misc2.categories);
		assertEquals("unit", misc2.unit);
		assertEquals("origUnit", misc2.origUnit);
		assertEquals("dbuuid", misc2.dbuuid);
		
		// Test copy constructor (MatrixXml)
		MiscXml misc3 = new MiscXml(misc2);
		assertTrue(0 == misc3.id);
		assertEquals("name", misc3.name);
		assertEquals("description", misc3.description);
		assertEquals(.0, misc3.value, .0);
		assertEquals(Arrays.asList("category"), misc3.categories);
		assertEquals("unit", misc3.unit);
		assertEquals("origUnit", misc3.origUnit);
		assertEquals("dbuuid", misc3.dbuuid);
		
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
		assertTrue(0 == misc4.id);
		assertEquals("name", misc4.name);
		assertEquals("description", misc4.description);
		assertEquals(.0, misc4.value, .0);
		assertEquals(Arrays.asList("category"), misc4.categories);
		assertEquals("unit", misc4.unit);
		assertEquals("origUnit", misc4.origUnit);
		assertEquals("dbuuid", misc4.dbuuid);
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
