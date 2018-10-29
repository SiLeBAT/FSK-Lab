package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class MDMatrixXmlTest {

	@Test
	public void testConstructors() {
		
		// Empty constructor
		MDMatrixXml matrix0 = new MDMatrixXml();
		assertTrue(matrix0.getId() < 0);
		assertNull(matrix0.getName());
		assertNull(matrix0.getDetail());
		assertNull(matrix0.getDbuuid());
		
		// Constructor with id, name and detail
		MDMatrixXml matrix1 = new MDMatrixXml(0, "name", "detail");
		assertTrue(0 == matrix1.getId());
		assertEquals("name", matrix1.getName());
		assertEquals("detail", matrix1.getDetail());
		assertNull(matrix1.getDbuuid());

		// Fully parameterized constructor
		MDMatrixXml matrix2 = new MDMatrixXml(0, "name", "detail", "dbuuid");
		assertTrue(0 == matrix2.getId());
		assertEquals("name", matrix2.getName());
		assertEquals("detail", matrix2.getDetail());
		assertEquals("dbuuid", matrix2.getDbuuid());
		
		// Copy constructor (Element)
		Element element = new Element(MDMatrixXml.ELEMENT_MATRIX);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		MDMatrixXml matrix3 = new MDMatrixXml(element);
		assertTrue(0 == matrix3.getId());
		assertEquals("name", matrix3.getName());
		assertEquals("detail", matrix3.getDetail());
		assertEquals("dbuuid", matrix3.getDbuuid());
		
		// Copy constructor
		MDMatrixXml matrix4 = new MDMatrixXml(matrix3);
		assertTrue(0 == matrix4.getId());
		assertEquals("name", matrix4.getName());
		assertEquals("detail", matrix4.getDetail());
		assertEquals("dbuuid", matrix4.getDbuuid());
	}
	
	@Test
	public void testToXmlElement() throws DataConversionException {
		MDMatrixXml matrix = new MDMatrixXml(0, "name", "detail", "dbuuid");
		Element element = matrix.toXmlElement();
		
		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("detail", element.getAttributeValue("detail"));
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}
}
