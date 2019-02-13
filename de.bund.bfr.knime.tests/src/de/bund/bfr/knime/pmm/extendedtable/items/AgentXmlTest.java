package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Test;

import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml.Type;

public class AgentXmlTest {

	@Test
	public void testConstructors() {
		
		// Test empty constructor
		AgentXml agent0 = new AgentXml(Type.MD);
		assertTrue(agent0.id < 0);
		assertNull(agent0.name);
		assertNull(agent0.detail);
		assertNull(agent0.dbuuid);
		
		// Test constructor with id, name and detail
		AgentXml agent1 = new AgentXml(Type.MD, 0, "name", "detail");
		assertTrue(0 == agent1.id);
		assertEquals("name", agent1.name);
		assertEquals("detail", agent1.detail);
		assertNull(agent1.dbuuid);
		
		// Test constructor with id, name, detail and dbuuid
		AgentXml agent2 = new AgentXml(Type.MD, 0, "name", "detail", "dbuuid");
		assertTrue(0 == agent2.id);
		assertEquals("name", agent2.name);
		assertEquals("detail", agent2.detail);
		assertEquals("dbuuid", agent2.dbuuid);
		
		// Test copy constructor
		AgentXml agent3 = new AgentXml(agent2);
		assertTrue(0 == agent3.id);
		assertEquals("name", agent3.name);
		assertEquals("detail", agent3.detail);
		assertEquals("dbuuid", agent3.dbuuid);
		
		// Test constructor with JDOM Element
		Element element = new Element("mdAgent");
		element.setAttribute("id", "0");
		element.setAttribute("name", "name");
		element.setAttribute("detail", "detail");
		element.setAttribute("dbuuid", "dbuuid");
		
		AgentXml agent4 = new AgentXml(element);
		assertTrue(0 == agent4.id);
		assertEquals("name", agent4.name);
		assertEquals("detail", agent4.detail);
		assertEquals("dbuuid", agent4.dbuuid);
	}
	
	@Test
	public void testElementConstructorMd() {
		new AgentXml(new Element("mdAgent"));
	}
	
	@Test
	public void testElementConstructorModel1() {
		new AgentXml(new Element("model1Agent"));
	}
	
	@Test
	public void testElementConstructorModel2() {
		new AgentXml(new Element("model2Agent"));
	}
	
	@Test(expected = RuntimeException.class)
	public void testElementConstructorOther() {
		new AgentXml(new Element("other"));
	}
	
	@Test
	public void testToXmlElement() throws Exception {
		AgentXml agent = new AgentXml(Type.MD, 0, "name", "detail", "dbuuid");
		Element element = agent.toXmlElement();
		
		assertEquals(0, element.getAttribute("id").getIntValue());
		assertEquals("name", element.getAttribute("name").getValue());
		assertEquals("detail", element.getAttribute("detail").getValue());
		assertEquals("dbuuid", element.getAttribute("dbuuid").getValue());
	}
	
	@Test
	public void testGetElementName() throws Exception {
		assertEquals(new AgentXml(AgentXml.Type.MD).getElementName(), "mdAgent");
		assertEquals(new AgentXml(AgentXml.Type.Model1).getElementName(), "model1Agent");
		assertEquals(new AgentXml(AgentXml.Type.Model2).getElementName(), "model2Agent");
	}
}
