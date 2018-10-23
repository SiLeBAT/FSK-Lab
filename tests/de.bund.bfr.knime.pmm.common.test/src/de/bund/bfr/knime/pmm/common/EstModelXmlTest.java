package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Element;
import org.junit.Test;

public class EstModelXmlTest {

	@Test
	public void testConstructors() {
		
		// Test constructor with id, name, sse, rms, r2, aic, bic, dof
		EstModelXml model0 = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 1);
		assertTrue(0 == model0.getId());
		assertEquals("name", model0.getName());
		assertEquals(0.0, model0.getSse(), .0);
		assertEquals(0.0, model0.getRms(), .0);
		assertEquals(0.0, model0.getR2(), .0);
		assertEquals(0.0, model0.getAic(), .0);
		assertEquals(0.0, model0.getBic(), .0);
		assertTrue(1 == model0.getDof());
		assertNull(model0.getQualityScore());
		assertNull(model0.getChecked());
		assertNull(model0.getDbuuid());
		
		// Test constructor with id, name, sse, rms, r2, aic, bic, dof, checked and qualityScore
		EstModelXml model1 = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 1, true, 1);
		assertTrue(0 == model1.getId());
		assertEquals("name", model1.getName());
		assertEquals(0.0, model1.getSse(), .0);
		assertEquals(0.0, model1.getRms(), .0);
		assertEquals(0.0, model1.getR2(), .0);
		assertEquals(0.0, model1.getAic(), .0);
		assertEquals(0.0, model1.getBic(), .0);
		assertTrue(1 == model1.getDof());
		assertTrue(1 == model1.getQualityScore());
		assertTrue(model1.getChecked());
		assertNull(model1.getDbuuid());

		// Test full parameterized constructor
		EstModelXml model2 = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 1, true, 1, "dbuuid");
		assertTrue(0 == model2.getId());
		assertEquals("name", model2.getName());
		assertEquals(0.0, model2.getSse(), .0);
		assertEquals(0.0, model2.getRms(), .0);
		assertEquals(0.0, model2.getR2(), .0);
		assertEquals(0.0, model2.getAic(), .0);
		assertEquals(0.0, model2.getBic(), .0);
		assertTrue(1 == model2.getDof());
		assertTrue(1 == model2.getQualityScore());
		assertTrue(model2.getChecked());
		assertEquals("dbuuid", model2.getDbuuid());
		
		// Test constructor with Element
		Element element = new Element(EstModelXml.ELEMENT_ESTMODEL);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("sse", "0.0");
		element.setAttribute("rms", "0.0");
		element.setAttribute("r2", "0.0");
		element.setAttribute("aic", "0.0");
		element.setAttribute("bic", "0.0");
		element.setAttribute("dof", "1");
		element.setAttribute("checked", "true");
		element.setAttribute("dbuuid", "dbuuid");
		
		EstModelXml model3 = new EstModelXml(element);
		assertTrue(0 == model3.getId());
		assertEquals("name", model3.getName());
		assertEquals(0.0, model3.getSse(), .0);
		assertEquals(0.0, model3.getRms(), .0);
		assertEquals(0.0, model3.getR2(), .0);
		assertEquals(0.0, model3.getAic(), .0);
		assertEquals(0.0, model3.getBic(), .0);
		assertTrue(1 == model3.getDof());
		assertTrue(model3.getChecked());
		assertEquals("dbuuid", model3.getDbuuid());
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		
		EstModelXml model = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 1, true, 1, "dbuuid");
		Element element = model.toXmlElement();
		
		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttributeValue("name"));
		assertEquals(0.0, element.getAttribute("sse").getDoubleValue(), .0);
		assertEquals(0.0, element.getAttribute("rms").getDoubleValue(), .0);
		assertEquals(0.0, element.getAttribute("r2").getDoubleValue(), .0);
		assertEquals(0.0, element.getAttribute("aic").getDoubleValue(), .0);
		assertEquals(0.0, element.getAttribute("bic").getDoubleValue(), .0);
		assertTrue(1 == element.getAttribute("dof").getDoubleValue());
		assertTrue(element.getAttribute("checked").getBooleanValue());
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}

}
