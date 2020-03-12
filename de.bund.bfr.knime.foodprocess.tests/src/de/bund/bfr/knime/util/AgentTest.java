package de.bund.bfr.knime.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("static-method")
public class AgentTest {

	@Test
	public void testNameConstructor() {
		Agent agent = new Agent("name");
		assertEquals(-1, agent.getId());
		assertEquals("name", agent.getName());
	}
	
	@Test
	public void testNameIdConstructor() {
		Agent agent = new Agent("name", 0);
		assertEquals(0, agent.getId());
		assertEquals("name", agent.getName());
	}
	
	@Test
	public void testIdConstructor() {
		Agent agent = new Agent(4000);
		assertEquals(4000, agent.getId());
		// DB name is not tested.
	}
}
