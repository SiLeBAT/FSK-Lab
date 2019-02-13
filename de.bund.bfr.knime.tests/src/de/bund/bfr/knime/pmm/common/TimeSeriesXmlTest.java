package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class TimeSeriesXmlTest {

	@Test
	public void testConstructors() {

		// Test constructor with name, time, timeUnit, concentration, concentrationUnit,
		// concentrationStdDev, numberOfMeasurements
		TimeSeriesXml series0 = new TimeSeriesXml("name", 0.0, "timeUnit", 1.0, "concentrationUnit", 0.0, 1);
		assertEquals("name", series0.name);
		assertEquals(0.0, series0.time, .0);
		assertEquals("timeUnit", series0.timeUnit);
		assertEquals("timeUnit", series0.origTimeUnit);
		assertEquals(1.0, series0.concentration, .0);
		assertEquals("concentrationUnit", series0.concentrationUnit);
		assertNull(series0.concentrationUnitObjectType);
		assertEquals("concentrationUnit", series0.origConcentrationUnit);
		assertEquals(0.0, series0.concentrationStdDev, .0);
		assertTrue(1 == series0.numberOfMeasurements);

		// Test fully parameterized constructor
		TimeSeriesXml series1 = new TimeSeriesXml("name", 0.0, "timeUnit", "origTimeUnit", 1.0, "concentrationUnit",
				"concentrationUnitObjectType", "origConcentrationUnit", 0.0, 1);
		assertEquals("name", series1.name);
		assertEquals(0.0, series1.time, .0);
		assertEquals("timeUnit", series1.timeUnit);
		assertEquals("origTimeUnit", series1.origTimeUnit);
		assertEquals(1.0, series1.concentration, .0);
		assertEquals("concentrationUnit", series1.concentrationUnit);
		assertEquals("concentrationUnitObjectType", series1.concentrationUnitObjectType);
		assertEquals("origConcentrationUnit", series1.origConcentrationUnit);
		assertEquals(0.0, series1.concentrationStdDev, .0);
		assertTrue(1 == series1.numberOfMeasurements);

		// Test copy constructor with JDOM Element
		Element element = new Element(TimeSeriesXml.ELEMENT_TIMESERIES);
		element.setAttribute("name", "name");
		element.setAttribute("time", "0.0");
		element.setAttribute("timeUnit", "timeUnit");
		element.setAttribute("origTimeUnit", "origTimeUnit");
		element.setAttribute("concentration", "1.0");
		element.setAttribute("concentrationUnit", "concentrationUnit");
		element.setAttribute("concentrationUnitObjectType", "concentrationUnitObjectType");
		element.setAttribute("origConcentrationUnit", "origConcentrationUnit");
		element.setAttribute("concentrationStdDev", "0.0");
		element.setAttribute("numberOfMeasurements", "1");
		
		TimeSeriesXml series2 = new TimeSeriesXml(element);
		assertEquals("name", series2.name);
		assertEquals(0.0, series2.time, .0);
		assertEquals("timeUnit", series2.timeUnit);
		assertEquals("origTimeUnit", series2.origTimeUnit);
		assertEquals(1.0, series2.concentration, .0);
		assertEquals("concentrationUnit", series2.concentrationUnit);
		assertEquals("concentrationUnitObjectType", series2.concentrationUnitObjectType);
		assertEquals("origConcentrationUnit", series2.origConcentrationUnit);
		assertEquals(0.0, series2.concentrationStdDev, .0);
		assertTrue(1 == series2.numberOfMeasurements);
	}

	@Test
	public void testToXmlElement() throws DataConversionException {
		TimeSeriesXml series = new TimeSeriesXml("name", 0.0, "timeUnit", "origTimeUnit", 1.0, "concentrationUnit",
				"concentrationUnitObjectType", "origConcentrationUnit", 0.0, 1);
		Element element = series.toXmlElement();
		
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals(0.0, element.getAttribute("time").getDoubleValue(), .0);
		assertEquals("timeUnit", element.getAttributeValue("timeUnit"));
		assertEquals("origTimeUnit", element.getAttributeValue("origTimeUnit"));
		assertEquals(1.0, element.getAttribute("concentration").getDoubleValue(), .0);
		assertEquals("concentrationUnit", element.getAttributeValue("concentrationUnit"));
		assertEquals("concentrationUnitObjectType", element.getAttributeValue("concentrationUnitObjectType"));
		assertEquals("origConcentrationUnit", element.getAttributeValue("origConcentrationUnit"));
		assertEquals(0.0, element.getAttribute("concentrationStdDev").getDoubleValue(), .0);
		assertTrue(1 == element.getAttribute("numberOfMeasurements").getIntValue());
	}
}
