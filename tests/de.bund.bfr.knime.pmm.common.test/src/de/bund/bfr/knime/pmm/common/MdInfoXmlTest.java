package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class MdInfoXmlTest {

	@Test
	public void testConstructors() {
		
		// Test fully parameterized constructor
		MdInfoXml info0 = new MdInfoXml(0, "name", "comment", 0, true);
		assertTrue(0 == info0.getId());
		assertEquals("name", info0.getName());
		assertEquals("comment", info0.getComment());
		assertTrue(0 == info0.getQualityScore());
		assertTrue(info0.getChecked());
		
		// Test copy constructor
		Element element = new Element(MdInfoXml.ELEMENT_MDINFO);
		element.setAttribute("ID", "0");
		element.setAttribute("Name", "name");
		element.setAttribute("Comment", "comment");
		element.setAttribute("QualityScore", "0");
		
		MdInfoXml info1 = new MdInfoXml(element);
		assertTrue(0 == info1.getId());
		assertEquals("name", info1.getName());
		assertEquals("comment", info1.getComment());
		assertTrue(0 == info1.getQualityScore());
		assertTrue(info1.getChecked());
	}
	
	@Test
	public void testToXmlElement() throws DataConversionException {
		MdInfoXml info = new MdInfoXml(0, "name", "comment", 0, true);
		assertTrue(0 == info.getId());
		assertEquals("name", info.getName());
		assertEquals("comment", info.getComment());
		assertTrue(0 == info.getQualityScore());
		assertTrue(info.getChecked());
		
		Element element = info.toXmlElement();
		assertTrue(0 == element.getAttribute("ID").getIntValue());
		assertEquals("name", element.getAttributeValue("Name"));
		assertEquals("comment", element.getAttributeValue("Comment"));
		assertTrue(0 == element.getAttribute("QualityScore").getIntValue());
		assertTrue(element.getAttribute("Checked").getBooleanValue());
	}
}
