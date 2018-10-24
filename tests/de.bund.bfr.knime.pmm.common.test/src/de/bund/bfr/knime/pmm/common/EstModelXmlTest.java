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
		assertTrue(0 == model0.id);
		assertEquals("name", model0.name);
		assertEquals(0.0, model0.sse, .0);
		assertEquals(0.0, model0.rms, .0);
		assertEquals(0.0, model0.r2, .0);
		assertEquals(0.0, model0.aic, .0);
		assertEquals(0.0, model0.bic, .0);
		assertTrue(1 == model0.dof);
		assertNull(model0.qualityScore);
		assertNull(model0.checked);
		assertNull(model0.dbuuid);
		
		// Test constructor with id, name, sse, rms, r2, aic, bic, dof, checked and qualityScore
		EstModelXml model1 = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 1, true, 1);
		assertTrue(0 == model1.id);
		assertEquals("name", model1.name);
		assertEquals(0.0, model1.sse, .0);
		assertEquals(0.0, model1.rms, .0);
		assertEquals(0.0, model1.r2, .0);
		assertEquals(0.0, model1.aic, .0);
		assertEquals(0.0, model1.bic, .0);
		assertTrue(1 == model1.dof);
		assertTrue(1 == model1.qualityScore);
		assertTrue(model1.checked);
		assertNull(model1.dbuuid);

		// Test full parameterized constructor
		EstModelXml model2 = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 1, true, 1, "dbuuid");
		assertTrue(0 == model2.id);
		assertEquals("name", model2.name);
		assertEquals(0.0, model2.sse, .0);
		assertEquals(0.0, model2.rms, .0);
		assertEquals(0.0, model2.r2, .0);
		assertEquals(0.0, model2.aic, .0);
		assertEquals(0.0, model2.bic, .0);
		assertTrue(1 == model2.dof);
		assertTrue(1 == model2.qualityScore);
		assertTrue(model2.checked);
		assertEquals("dbuuid", model2.dbuuid);
		
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
		assertTrue(0 == model3.id);
		assertEquals("name", model3.name);
		assertEquals(0.0, model3.sse, .0);
		assertEquals(0.0, model3.rms, .0);
		assertEquals(0.0, model3.r2, .0);
		assertEquals(0.0, model3.aic, .0);
		assertEquals(0.0, model3.bic, .0);
		assertTrue(1 == model3.dof);
		assertTrue(model3.checked);
		assertEquals("dbuuid", model3.dbuuid);
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
