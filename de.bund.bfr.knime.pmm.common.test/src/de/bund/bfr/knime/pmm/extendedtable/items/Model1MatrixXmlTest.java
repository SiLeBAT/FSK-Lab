package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

public class Model1MatrixXmlTest {

	@Test
	public void testConstructors() {
		
		// Empty constructor
		Model1MatrixXml matrix0 = new Model1MatrixXml ();
		assertTrue(matrix0.id < 0);
		assertNull(matrix0.name);
		assertNull(matrix0.detail);
		assertNull(matrix0.dbuuid);
		
		// Constructor with id, name and detail
		Model1MatrixXml  matrix1 = new Model1MatrixXml (0, "name", "detail");
		assertTrue(0 == matrix1.id);
		assertEquals("name", matrix1.name);
		assertEquals("detail", matrix1.detail);
		assertNull(matrix1.dbuuid);

		// Fully parameterized constructor
		Model1MatrixXml  matrix2 = new Model1MatrixXml (0, "name", "detail", "dbuuid");
		assertTrue(0 == matrix2.id);
		assertEquals("name", matrix2.name);
		assertEquals("detail", matrix2.detail);
		assertEquals("dbuuid", matrix2.dbuuid);
		
		// Copy constructor (Element)
		Element element = new Element(Model1MatrixXml .ELEMENT_MATRIX);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		Model1MatrixXml  matrix3 = new Model1MatrixXml (element);
		assertTrue(0 == matrix3.id);
		assertEquals("name", matrix3.name);
		assertEquals("detail", matrix3.detail);
		assertEquals("dbuuid", matrix3.dbuuid);
		
		// Copy constructor
		Model1MatrixXml  matrix4 = new Model1MatrixXml (matrix3);
		assertTrue(0 == matrix4.id);
		assertEquals("name", matrix4.name);
		assertEquals("detail", matrix4.detail);
		assertEquals("dbuuid", matrix4.dbuuid);
	}
	
	@Test
	public void testToXmlElement() throws DataConversionException {
		Model1MatrixXml  matrix = new Model1MatrixXml (0, "name", "detail", "dbuuid");
		Element element = matrix.toXmlElement();
		
		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("detail", element.getAttributeValue("detail"));
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}
}
