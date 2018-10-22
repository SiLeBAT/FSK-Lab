package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class DepXmlTest {

    @Test
    public void testConstructors() {

        // Test constructor with name
        DepXml dep0 = new DepXml("name");
        assertEquals("name", dep0.getName());
        assertEquals("name", dep0.getOrigName());
        assertNull(dep0.getMin());
        assertNull(dep0.getMax());
        assertNull(dep0.getCategory());
        assertNull(dep0.getUnit());
        assertNull(dep0.getDescription());

        // Test constructor with name, category and unit
        DepXml dep1 = new DepXml("name", "category", "unit");
        assertEquals("name", dep1.getName());
        assertEquals("name", dep1.getOrigName());
        assertNull(dep1.getMin());
        assertNull(dep1.getMax());
        assertEquals("category", dep1.getCategory());
        assertEquals("unit", dep1.getUnit());
        assertNull(dep1.getDescription());

        // Test constructor with name, category, unit and description
        DepXml dep2 = new DepXml("name", "origName", "category", "unit", "description");
        assertEquals("name", dep2.getName());
        assertEquals("origName", dep2.getOrigName());
        assertNull(dep2.getMin());
        assertNull(dep2.getMax());
        assertEquals("category", dep2.getCategory());
        assertEquals("unit", dep2.getUnit());
        assertEquals("description", dep2.getDescription());

        // Test constructor with Element
        Element element = new Element(DepXml.ELEMENT_DEPENDENT);
        element.setAttribute("name", "name");
        element.setAttribute("origname", "origName");
        element.setAttribute("min", "0.0");
        element.setAttribute("max", "1.0");
        element.setAttribute("category", "category");
        element.setAttribute("unit", "unit");
        element.setAttribute("description", "description");

        DepXml dep3 = new DepXml(element);
        assertEquals("name", dep3.getName());
        assertEquals("origName", dep3.getOrigName());
        assertEquals(0.0, dep3.getMin(), .0);
        assertEquals(1.0, dep3.getMax(), .0);
        assertEquals("category", dep3.getCategory());
        assertEquals("unit", dep3.getUnit());
        assertEquals("description", dep3.getDescription());
    }

    @Test
    public void testToXmlElement() throws DataConversionException {

        DepXml dep = new DepXml("name", "origName", "category", "unit", "description");
        dep.setMin(0.0);
        dep.setMax(1.0);

        Element element = dep.toXmlElement();
        assertEquals("name", element.getAttributeValue("name"));
        assertEquals("origName", element.getAttributeValue("origname"));
        assertEquals(0.0, element.getAttribute("min").getDoubleValue(), .0);
        assertEquals(1.0, element.getAttribute("max").getDoubleValue(), .0);
        assertEquals("category", element.getAttributeValue("category"));
        assertEquals("unit", element.getAttributeValue("unit"));
        assertEquals("description", element.getAttributeValue("description"));
    }
}
