package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

@SuppressWarnings("static-method")
public class DepXmlTest {

    @Test
    public void testConstructors() {

        // Test constructor with name
        final DepXml dep0 = new DepXml("name");
        assertEquals("name", dep0.name);
        assertEquals("name", dep0.origName);
        assertNull(dep0.min);
        assertNull(dep0.max);
        assertNull(dep0.category);
        assertNull(dep0.unit);
        assertNull(dep0.description);

        // Test constructor with name, category and unit
        final DepXml dep1 = new DepXml("name", "category", "unit");
        assertEquals("name", dep1.name);
        assertEquals("name", dep1.origName);
        assertNull(dep1.min);
        assertNull(dep1.max);
        assertEquals("category", dep1.category);
        assertEquals("unit", dep1.unit);
        assertNull(dep1.description);

        // Test constructor with name, category, unit and description
        final DepXml dep2 = new DepXml("name", "origName", "category", "unit", "description");
        assertEquals("name", dep2.name);
        assertEquals("origName", dep2.origName);
        assertNull(dep2.min);
        assertNull(dep2.max);
        assertEquals("category", dep2.category);
        assertEquals("unit", dep2.unit);
        assertEquals("description", dep2.description);

        // Test constructor with Element
        final Element element = new Element(DepXml.ELEMENT_DEPENDENT);
        element.setAttribute("name", "name");
        element.setAttribute("origname", "origName");
        element.setAttribute("min", "0.0");
        element.setAttribute("max", "1.0");
        element.setAttribute("category", "category");
        element.setAttribute("unit", "unit");
        element.setAttribute("description", "description");

        final DepXml dep3 = new DepXml(element);
        assertEquals("name", dep3.name);
        assertEquals("origName", dep3.origName);
        assertEquals(0.0, dep3.min, .0);
        assertEquals(1.0, dep3.max, .0);
        assertEquals("category", dep3.category);
        assertEquals("unit", dep3.unit);
        assertEquals("description", dep3.description);
    }

    @Test
    public void testToXmlElement() throws DataConversionException {

        final DepXml dep = new DepXml("name", "origName", "category", "unit", "description");
        dep.min = 0.0;
        dep.max = 1.0;

        final Element element = dep.toXmlElement();
        assertEquals("name", element.getAttributeValue("name"));
        assertEquals("origName", element.getAttributeValue("origname"));
        assertEquals(0.0, element.getAttribute("min").getDoubleValue(), .0);
        assertEquals(1.0, element.getAttribute("max").getDoubleValue(), .0);
        assertEquals("category", element.getAttributeValue("category"));
        assertEquals("unit", element.getAttributeValue("unit"));
        assertEquals("description", element.getAttributeValue("description"));
    }
}
