package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

public class AgentXmlTest {

	@Test
	public void testConstructors() {
		
		// Test empty constructor
		AgentXml agent0 = new AgentXml();
		assertTrue(agent0.getId() < 0);
		assertNull(agent0.getName());
		assertNull(agent0.getDetail());
		assertNull(agent0.getDbuuid());
		
		// Test constructor with id, name and detail
		AgentXml agent1 = new AgentXml(0, "name", "detail");
		assertTrue(0 == agent1.getId());
		assertEquals("name", agent1.getName());
		assertEquals("detail", agent1.getDetail());
		assertNull(agent1.getDbuuid());
		
		// Test constructor with id, name, detail and dbuuid
		AgentXml agent2 = new AgentXml(0, "name", "detail", "dbuuid");
		assertTrue(0 == agent2.getId());
		assertEquals("name", agent2.getName());
		assertEquals("detail", agent2.getDetail());
		assertEquals("dbuuid", agent2.getDbuuid());
		
		// Test copy constructor
		AgentXml agent3 = new AgentXml(agent2);
		assertTrue(0 == agent3.getId());
		assertEquals("name", agent3.getName());
		assertEquals("detail", agent3.getDetail());
		assertEquals("dbuuid", agent3.getDbuuid());
		
		// Test constructor with JDOM Element
		Element element = new Element(AgentXml.ELEMENT_AGENT);
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		AgentXml agent4 = new AgentXml(element);
		assertTrue(0 == agent4.getId());
		assertEquals("name", agent4.getName());
		assertEquals("detail", agent4.getDetail());
		assertEquals("dbuuid", agent4.getDbuuid());
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		AgentXml agent = new AgentXml(0, "name", "detail", "dbuuid");
		Element element = agent.toXmlElement();
		
		assertEquals(0, element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttribute("name").getValue());
		assertEquals("detail", element.getAttribute("detail").getValue());
		assertEquals("dbuuid", element.getAttribute("dbuuid").getValue());
	}
}
