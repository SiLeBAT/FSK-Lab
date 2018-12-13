package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

public class MDAgentXmlTest {

	@Test
	public void testConstructors() {
		
		// Test empty constructor
		MDAgentXml agent0 = new MDAgentXml();
		assertTrue(agent0.id < 0);
		assertNull(agent0.name);
		assertNull(agent0.detail);
		assertNull(agent0.dbuuid);
		
		// Test constructor with id, name and detail
		MDAgentXml agent1 = new MDAgentXml(0, "name", "detail");
		assertTrue(0 == agent1.id);
		assertEquals("name", agent1.name);
		assertEquals("detail", agent1.detail);
		assertNull(agent1.dbuuid);
		
		// Test constructor with id, name, detail and dbuuid
		MDAgentXml agent2 = new MDAgentXml(0, "name", "detail", "dbuuid");
		assertTrue(0 == agent2.id);
		assertEquals("name", agent2.name);
		assertEquals("detail", agent2.detail);
		assertEquals("dbuuid", agent2.dbuuid);
		
		// Test copy constructor
		MDAgentXml agent3 = new MDAgentXml(agent2);
		assertTrue(0 == agent3.id);
		assertEquals("name", agent3.name);
		assertEquals("detail", agent3.detail);
		assertEquals("dbuuid", agent3.dbuuid);
		
		// Test constructor with JDOM Element
		Element element = new Element(MDAgentXml.ELEMENT_AGENT);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		MDAgentXml agent4 = new MDAgentXml(element);
		assertTrue(0 == agent4.id);
		assertEquals("name", agent4.name);
		assertEquals("detail", agent4.detail);
		assertEquals("dbuuid", agent4.dbuuid);
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		MDAgentXml agent = new MDAgentXml(0, "name", "detail", "dbuuid");
		Element element = agent.toXmlElement();
		
		assertEquals(0, element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttribute("name").getValue());
		assertEquals("detail", element.getAttribute("detail").getValue());
		assertEquals("dbuuid", element.getAttribute("dbuuid").getValue());
	}
}
