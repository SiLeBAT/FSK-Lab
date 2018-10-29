package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

public class MDAgentXmlTest {

	@Test
	public void testConstructors() {
		
		// Test empty constructor
		MDAgentXml agent0 = new MDAgentXml();
		assertTrue(agent0.getId() < 0);
		assertNull(agent0.getName());
		assertNull(agent0.getDetail());
		assertNull(agent0.getDbuuid());
		
		// Test constructor with id, name and detail
		MDAgentXml agent1 = new MDAgentXml(0, "name", "detail");
		assertTrue(0 == agent1.getId());
		assertEquals("name", agent1.getName());
		assertEquals("detail", agent1.getDetail());
		assertNull(agent1.getDbuuid());
		
		// Test constructor with id, name, detail and dbuuid
		MDAgentXml agent2 = new MDAgentXml(0, "name", "detail", "dbuuid");
		assertTrue(0 == agent2.getId());
		assertEquals("name", agent2.getName());
		assertEquals("detail", agent2.getDetail());
		assertEquals("dbuuid", agent2.getDbuuid());
		
		// Test copy constructor
		MDAgentXml agent3 = new MDAgentXml(agent2);
		assertTrue(0 == agent3.getId());
		assertEquals("name", agent3.getName());
		assertEquals("detail", agent3.getDetail());
		assertEquals("dbuuid", agent3.getDbuuid());
		
		// Test constructor with JDOM Element
		Element element = new Element(MDAgentXml.ELEMENT_AGENT);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		MDAgentXml agent4 = new MDAgentXml(element);
		assertTrue(0 == agent4.getId());
		assertEquals("name", agent4.getName());
		assertEquals("detail", agent4.getDetail());
		assertEquals("dbuuid", agent4.getDbuuid());
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
