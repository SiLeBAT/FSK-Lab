package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class ParamXmlTest {

	@Test
	public void testConstructors() {

		// Test constructor with name, isStartParam and value
		ParamXml param0 = new ParamXml("name", false, 0.0);
		assertEquals("name", param0.getName());
		assertEquals("name", param0.getOrigName());
		assertFalse(param0.isStartParam());
		assertEquals(0.0, param0.getValue(), .0);
		assertNull(param0.getError());
		assertNull(param0.getMin());
		assertNull(param0.getMax());
		assertNull(param0.getP());
		assertNull(param0.getT());
		assertNull(param0.getMinGuess());
		assertNull(param0.getMaxGuess());
		assertNull(param0.getCategory());
		assertNull(param0.getUnit());
		assertNull(param0.getDescription());
		assertTrue(param0.getAllCorrelations().isEmpty());

		// Test constructor with name, isStartParam, value, error, min, max, P and t
		ParamXml param1 = new ParamXml("name", false, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0);
		assertEquals("name", param1.getName());
		assertEquals("name", param1.getOrigName());
		assertFalse(param1.isStartParam());
		assertEquals(0.0, param1.getValue(), .0);
		assertEquals(0.0, param1.getError(), .0);
		assertEquals(0.0, param1.getMin(), .0);
		assertEquals(1.0, param1.getMax(), .0);
		assertEquals(2.0, param1.getP(), .0);
		assertEquals(3.0, param1.getT(), .0);
		assertNull(param1.getMinGuess());
		assertNull(param1.getMaxGuess());
		assertNull(param1.getCategory());
		assertNull(param1.getUnit());
		assertNull(param1.getDescription());
		assertTrue(param1.getAllCorrelations().isEmpty());

		// Test constructor with name, isStartParam, value, error, min, max, P, t,
		// category and unit
		ParamXml param2 = new ParamXml("name", false, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0, "category", "unit");
		assertEquals("name", param2.getName());
		assertEquals("name", param2.getOrigName());
		assertFalse(param2.isStartParam());
		assertEquals(0.0, param2.getValue(), .0);
		assertEquals(0.0, param2.getError(), .0);
		assertEquals(0.0, param2.getMin(), .0);
		assertEquals(1.0, param2.getMax(), .0);
		assertEquals(2.0, param2.getP(), .0);
		assertEquals(3.0, param2.getT(), .0);
		assertNull(param2.getMinGuess());
		assertNull(param2.getMaxGuess());
		assertEquals("category", param2.getCategory());
		assertEquals("unit", param2.getUnit());
		assertNull(param2.getDescription());
		assertTrue(param2.getAllCorrelations().isEmpty());

		// Test copy constructor (Element)
		Element element = new Element(ParamXml.ELEMENT_PARAM);
		element.setAttribute("name", "name");
		element.setAttribute("origname", "origName");
		element.setAttribute("isStart", "true");
		element.setAttribute("value", "0.0");
		element.setAttribute("error", "1.0");
		element.setAttribute("min", "-5.0");
		element.setAttribute("max", "5.0");
		element.setAttribute("P", "0.1");
		element.setAttribute("t", "0.2");
		element.setAttribute("minGuess", "1.0");
		element.setAttribute("maxGuess", "2.0");
		element.setAttribute("category", "category");
		element.setAttribute("unit", "unit");
		element.setAttribute("description", "description");

		Element correlation = new Element("correlation");
		correlation.setAttribute("origname", "a");
		correlation.setAttribute("value", "0.0");
		element.addContent(correlation);

		ParamXml param3 = new ParamXml(element);
		assertEquals("name", param3.getName());
		assertEquals("origName", param3.getOrigName());
		assertTrue(param3.isStartParam());
		assertEquals(0.0, param3.getValue(), .0);
		assertEquals(1.0, param3.getError(), .0);
		assertEquals(-5.0, param3.getMin(), .0);
		assertEquals(5.0, param3.getMax(), .0);
		assertEquals(0.1, param3.getP(), .0);
		assertEquals(0.2, param3.getT(), .0);
		assertEquals(1.0, param3.getMinGuess(), .0);
		assertEquals(2.0, param3.getMaxGuess(), .0);
		assertEquals("category", param3.getCategory());
		assertEquals("unit", param3.getUnit());
		assertEquals("description", param3.getDescription());
		assertEquals(0.0, param3.getCorrelation("a"), .0);

		// Test fully parameterized constructor
		ParamXml param4 = new ParamXml("name", "origName", false, 0.0, 0.0, 0.0, 1.0, 0.1, 0.2, 2.0, 3.0, "category",
				"unit", "description", new HashMap<String, Double>());
		assertEquals("name", param4.getName());
		assertEquals("origName", param4.getOrigName());
		assertFalse(param4.isStartParam());
		assertEquals(0.0, param4.getValue(), .0);
		assertEquals(0.0, param4.getError(), .0);
		assertEquals(0.0, param4.getMin(), .0);
		assertEquals(1.0, param4.getMax(), .0);
		assertEquals(0.1, param4.getP(), .0);
		assertEquals(0.2, param4.getT(), .0);
		assertEquals(2.0, param4.getMinGuess(), .0);
		assertEquals(3.0, param4.getMaxGuess(), .0);
		assertEquals("category", param4.getCategory());
		assertEquals("unit", param4.getUnit());
		assertEquals("description", param4.getDescription());
		assertTrue(param4.getAllCorrelations().isEmpty());
	}

	@Test
	public void testToXmlElement() throws DataConversionException {

		HashMap<String, Double> correlations = new HashMap<>();
		correlations.put("a", 0.0);
		ParamXml param = new ParamXml("name", "origName", false, 0.0, 0.0, 0.0, 1.0, 0.1, 0.2, 2.0, 3.0, "category",
				"unit", "description", correlations);
		
		Element element = param.toXmlElement();
		
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("origName", element.getAttributeValue("origname"));
		assertFalse(element.getAttribute("isStart").getBooleanValue());
		assertEquals(0.0, element.getAttribute("value").getDoubleValue(), .0);
		assertEquals(0.0, element.getAttribute("error").getDoubleValue(), .0);
		assertEquals(0.0, element.getAttribute("min").getDoubleValue(), .0);
		assertEquals(1.0, element.getAttribute("max").getDoubleValue(), .0);
		assertEquals(0.1, element.getAttribute("P").getDoubleValue(), .0);
		assertEquals(0.2, element.getAttribute("t").getDoubleValue(), .0);
		assertEquals(2.0, element.getAttribute("minGuess").getDoubleValue(), .0);
		assertEquals(3.0, element.getAttribute("maxGuess").getDoubleValue(), .0);
		assertEquals("category", element.getAttributeValue("category"));
		assertEquals("unit", element.getAttributeValue("unit"));
		assertEquals("description", element.getAttributeValue("description"));
	
		assertTrue(element.getChildren().size() == 1);
		Element childElement = element.getChild("correlation");
		assertEquals("a", childElement.getAttributeValue("origname"));
		assertEquals(0.0, childElement.getAttribute("value").getDoubleValue(), .0);
	}
}
