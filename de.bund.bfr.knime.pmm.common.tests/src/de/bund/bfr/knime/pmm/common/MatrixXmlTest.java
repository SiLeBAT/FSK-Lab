package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.junit.Test;

@SuppressWarnings("static-method")
public class MatrixXmlTest {

	@Test
	public void testConstructors() {

		// Empty constructor
		final MatrixXml matrix0 = new MatrixXml();
		assertTrue(matrix0.id < 0);
		assertNull(matrix0.name);
		assertNull(matrix0.detail);
		assertNull(matrix0.dbuuid);

		// Constructor with id, name and detail
		final MatrixXml matrix1 = new MatrixXml(0, "name", "detail");
		assertTrue(0 == matrix1.id);
		assertEquals("name", matrix1.name);
		assertEquals("detail", matrix1.detail);
		assertNull(matrix1.dbuuid);

		// Fully parameterized constructor
		final MatrixXml matrix2 = new MatrixXml(0, "name", "detail", "dbuuid");
		assertTrue(0 == matrix2.id);
		assertEquals("name", matrix2.name);
		assertEquals("detail", matrix2.detail);
		assertEquals("dbuuid", matrix2.dbuuid);

		// Copy constructor (Element)
		final Element element = new Element(MatrixXml.ELEMENT_MATRIX);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");

		final MatrixXml matrix3 = new MatrixXml(element);
		assertTrue(0 == matrix3.id);
		assertEquals("name", matrix3.name);
		assertEquals("detail", matrix3.detail);
		assertEquals("dbuuid", matrix3.dbuuid);

		// Copy constructor
		final MatrixXml matrix4 = new MatrixXml(matrix3);
		assertTrue(0 == matrix4.id);
		assertEquals("name", matrix4.name);
		assertEquals("detail", matrix4.detail);
		assertEquals("dbuuid", matrix4.dbuuid);
	}

	@Test
	public void testToXmlElement() throws DataConversionException {
		final MatrixXml matrix = new MatrixXml(0, "name", "detail", "dbuuid");
		final Element element = matrix.toXmlElement();

		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals("detail", element.getAttributeValue("detail"));
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}
}
