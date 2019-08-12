package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;


@SuppressWarnings("static-method")
public class MdInfoXmlTest {

	@Test
	public void testConstructors() {

		// Test fully parameterized constructor
		final MdInfoXml info0 = new MdInfoXml(0, "name", "comment", 0, true);
		assertTrue(0 == info0.id);
		assertEquals("name", info0.name);
		assertEquals("comment", info0.comment);
		assertTrue(0 == info0.qualityScore);
		assertTrue(info0.checked);

		// Test copy constructor
		final Element element = new Element(MdInfoXml.ELEMENT_MDINFO);
		element.setAttribute("ID", "0");
		element.setAttribute("Name", "name");
		element.setAttribute("Comment", "comment");
		element.setAttribute("QualityScore", "0");
		element.setAttribute("Checked", "true");

		final MdInfoXml info1 = new MdInfoXml(element);
		assertTrue(0 == info1.id);
		assertEquals("name", info1.name);
		assertEquals("comment", info1.comment);
		assertTrue(0 == info1.qualityScore);
		assertTrue(info1.checked);
	}

	@Test
	public void testToXmlElement() throws DataConversionException {
		final MdInfoXml info = new MdInfoXml(0, "name", "comment", 0, true);
		assertTrue(0 == info.id);
		assertEquals("name", info.name);
		assertEquals("comment", info.comment);
		assertTrue(0 == info.qualityScore);
		assertTrue(info.checked);

		final Element element = info.toXmlElement();
		assertTrue(0 == element.getAttribute("ID").getIntValue());
		assertEquals("name", element.getAttributeValue("Name"));
		assertEquals("comment", element.getAttributeValue("Comment"));
		assertTrue(0 == element.getAttribute("QualityScore").getIntValue());
		assertTrue(element.getAttribute("Checked").getBooleanValue());
	}
}
