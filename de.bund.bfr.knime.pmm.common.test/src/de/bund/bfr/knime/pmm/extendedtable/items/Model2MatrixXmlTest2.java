package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class Model2MatrixXmlTest2 {

	@Test
	public void testConstructors() {
		
		// Empty constructor
		Model2MatrixXml matrix0 = new Model2MatrixXml ();
		assertTrue(matrix0.id < 0);
		assertNull(matrix0.name);
		assertNull(matrix0.detail);
		assertNull(matrix0.dbuuid);
		
		// Constructor with id, name and detail
		Model2MatrixXml  matrix1 = new Model2MatrixXml (0, "name", "detail");
		assertTrue(0 == matrix1.id);
		assertEquals("name", matrix1.name);
		assertEquals("detail", matrix1.detail);
		assertNull(matrix1.dbuuid);

		// Fully parameterized constructor
		Model2MatrixXml  matrix2 = new Model2MatrixXml (0, "name", "detail", "dbuuid");
		assertTrue(0 == matrix2.id);
		assertEquals("name", matrix2.name);
		assertEquals("detail", matrix2.detail);
		assertEquals("dbuuid", matrix2.dbuuid);
		
		// Copy constructor (Element)
		Element element = new Element(Model2MatrixXml .ELEMENT_MATRIX);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		Model2MatrixXml  matrix3 = new Model2MatrixXml (element);
		assertTrue(0 == matrix3.id);
		assertEquals("name", matrix3.name);
		assertEquals("detail", matrix3.detail);
		assertEquals("dbuuid", matrix3.dbuuid);
		
		// Copy constructor
		Model2MatrixXml  matrix4 = new Model2MatrixXml (matrix3);
		assertTrue(0 == matrix4.id);
		assertEquals("name", matrix4.name);
		assertEquals("detail", matrix4.detail);
		assertEquals("dbuuid", matrix4.dbuuid);
	}
	
	@Test
	public void testToXmlElement() throws DataConversionException {
		Model2MatrixXml  matrix = new Model2MatrixXml (0, "name", "detail", "dbuuid");
		Element element = matrix.toXmlElement();
		
		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("detail", element.getAttributeValue("detail"));
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}
}
