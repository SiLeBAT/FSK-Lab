package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

public class Model2AgentXmlTest2 {

	@Test
	public void testConstructors() {
		
		// Test empty constructor
		Model2AgentXml agent0 = new Model2AgentXml();
		assertTrue(agent0.id < 0);
		assertNull(agent0.name);
		assertNull(agent0.detail);
		assertNull(agent0.dbuuid);
		
		// Test constructor with id, name and detail
		Model2AgentXml agent1 = new Model2AgentXml(0, "name", "detail");
		assertTrue(0 == agent1.id);
		assertEquals("name", agent1.name);
		assertEquals("detail", agent1.detail);
		assertNull(agent1.dbuuid);
		
		// Test constructor with id, name, detail and dbuuid
		Model2AgentXml agent2 = new Model2AgentXml(0, "name", "detail", "dbuuid");
		assertTrue(0 == agent2.id);
		assertEquals("name", agent2.name);
		assertEquals("detail", agent2.detail);
		assertEquals("dbuuid", agent2.dbuuid);
		
		// Test copy constructor
		Model2AgentXml agent3 = new Model2AgentXml(agent2);
		assertTrue(0 == agent3.id);
		assertEquals("name", agent3.name);
		assertEquals("detail", agent3.detail);
		assertEquals("dbuuid", agent3.dbuuid);
		
		// Test constructor with JDOM Element
		Element element = new Element(Model2AgentXml.ELEMENT_AGENT);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		Model2AgentXml agent4 = new Model2AgentXml(element);
		assertTrue(0 == agent4.id);
		assertEquals("name", agent4.name);
		assertEquals("detail", agent4.detail);
		assertEquals("dbuuid", agent4.dbuuid);
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		Model2AgentXml agent = new Model2AgentXml(0, "name", "detail", "dbuuid");
		Element element = agent.toXmlElement();
		
		assertEquals(0, element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttribute("name").getValue());
		assertEquals("detail", element.getAttribute("detail").getValue());
		assertEquals("dbuuid", element.getAttribute("dbuuid").getValue());
	}
}
