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
		assertEquals("name", param0.name);
		assertEquals("name", param0.origName);
		assertFalse(param0.isStartParam);
		assertEquals(0.0, param0.value, .0);
		assertNull(param0.error);
		assertNull(param0.min);
		assertNull(param0.max);
		assertNull(param0.P);
		assertNull(param0.t);
		assertNull(param0.minGuess);
		assertNull(param0.maxGuess);
		assertNull(param0.category);
		assertNull(param0.unit);
		assertNull(param0.description);
		assertTrue(param0.correlations.isEmpty());

		// Test constructor with name, isStartParam, value, error, min, max, P and t
		ParamXml param1 = new ParamXml("name", false, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0);
		assertEquals("name", param1.name);
		assertEquals("name", param1.origName);
		assertFalse(param1.isStartParam);
		assertEquals(0.0, param1.value, .0);
		assertEquals(0.0, param1.error, .0);
		assertEquals(0.0, param1.min, .0);
		assertEquals(1.0, param1.max, .0);
		assertEquals(2.0, param1.P, .0);
		assertEquals(3.0, param1.t, .0);
		assertNull(param1.minGuess);
		assertNull(param1.maxGuess);
		assertNull(param1.category);
		assertNull(param1.unit);
		assertNull(param1.description);
		assertTrue(param1.correlations.isEmpty());

		// Test constructor with name, isStartParam, value, error, min, max, P, t,
		// category and unit
		ParamXml param2 = new ParamXml("name", false, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0, "category", "unit");
		assertEquals("name", param2.name);
		assertEquals("name", param2.origName);
		assertFalse(param2.isStartParam);
		assertEquals(0.0, param2.value, .0);
		assertEquals(0.0, param2.error, .0);
		assertEquals(0.0, param2.min, .0);
		assertEquals(1.0, param2.max, .0);
		assertEquals(2.0, param2.P, .0);
		assertEquals(3.0, param2.t, .0);
		assertNull(param2.minGuess);
		assertNull(param2.maxGuess);
		assertEquals("category", param2.category);
		assertEquals("unit", param2.unit);
		assertNull(param2.description);
		assertTrue(param2.correlations.isEmpty());

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
		assertEquals("name", param3.name);
		assertEquals("origName", param3.origName);
		assertTrue(param3.isStartParam);
		assertEquals(0.0, param3.value, .0);
		assertEquals(1.0, param3.error, .0);
		assertEquals(-5.0, param3.min, .0);
		assertEquals(5.0, param3.max, .0);
		assertEquals(0.1, param3.P, .0);
		assertEquals(0.2, param3.t, .0);
		assertEquals(1.0, param3.minGuess, .0);
		assertEquals(2.0, param3.maxGuess, .0);
		assertEquals("category", param3.category);
		assertEquals("unit", param3.unit);
		assertEquals("description", param3.description);
		assertEquals(0.0, param3.correlations.get("a"), .0);

		// Test fully parameterized constructor
		ParamXml param4 = new ParamXml("name", "origName", false, 0.0, 0.0, 0.0, 1.0, 0.1, 0.2, 2.0, 3.0, "category",
				"unit", "description", new HashMap<String, Double>());
		assertEquals("name", param4.name);
		assertEquals("origName", param4.origName);
		assertFalse(param4.isStartParam);
		assertEquals(0.0, param4.value, .0);
		assertEquals(0.0, param4.error, .0);
		assertEquals(0.0, param4.min, .0);
		assertEquals(1.0, param4.max, .0);
		assertEquals(0.1, param4.P, .0);
		assertEquals(0.2, param4.t, .0);
		assertEquals(2.0, param4.minGuess, .0);
		assertEquals(3.0, param4.maxGuess, .0);
		assertEquals("category", param4.category);
		assertEquals("unit", param4.unit);
		assertEquals("description", param4.description);
		assertTrue(param4.correlations.isEmpty());
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
