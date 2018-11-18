package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.AgentXml;

public class AgentTest {
	
	static int id = 4024;
	static String name = "salmonella spp";
	static String detail = "Salmonella spec";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	
	@Test
	public void testId() {
		Agent agent = new Agent();
		assertNull(agent.getId());
		
		agent.setId(id);
		assertEquals(id, agent.getId().intValue());
	}

	@Test
	public void testName() {
		Agent agent = new Agent();
		assertNull(agent.getName());
		
		agent.setName(name);
		assertEquals(name, agent.getName());
	}
	
	@Test
	public void testDetail() {
		Agent agent = new Agent();
		assertNull(agent.getDetail());
		
		agent.setDetail(detail);
		assertEquals(detail, agent.getDetail());
	}
	
	@Test
	public void testDbuuid() {
		Agent agent = new Agent();
		assertNull(agent.getDbuuid());
		
		agent.setDbuuid(dbuuid);
		assertEquals(dbuuid, agent.getDbuuid());
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Agent agent = new Agent();
		agent.setId(id);
		agent.setName(name);
		agent.setDetail(detail);
		agent.setDbuuid(dbuuid);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		agent.saveToNodeSettings(settings);
		
		assertEquals(id, settings.getInt(Agent.ID));
		assertEquals(name, settings.getString(Agent.NAME));
		assertEquals(detail, settings.getString(Agent.DETAIL));
		assertEquals(dbuuid, settings.getString(Agent.DBUUID));
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(Agent.ID, id);
		settings.addString(Agent.NAME, name);
		settings.addString(Agent.DETAIL, detail);
		settings.addString(Agent.DBUUID, dbuuid);
		
		Agent agent = new Agent();
		agent.loadFromNodeSettings(settings);
		
		assertEquals(id, agent.getId().intValue());
		assertEquals(name, agent.getName());
		assertEquals(detail, agent.getDetail());
		assertEquals(dbuuid, agent.getDbuuid());
	}
	
	@Test
	public void testToAgent() {
		Agent agent = Agent.toAgent(new AgentXml(id, name, detail, dbuuid));
		
		assertEquals(id, agent.getId().intValue());
		assertEquals(name, agent.getName());
		assertEquals(detail, agent.getDetail());
		assertEquals(dbuuid, agent.getDbuuid());
	}
	
	@Test
	public void testToAgentXml() {
		Agent agent = new Agent();
		agent.setId(id);
		agent.setName(name);
		agent.setDetail(detail);
		agent.setDbuuid(dbuuid);
		AgentXml agentXml = agent.toAgentXml();
		
		assertEquals(id, agentXml.id.intValue());
		assertEquals(name, agentXml.name);
		assertEquals(detail, agentXml.detail);
		assertEquals(dbuuid, agentXml.dbuuid);
	}
}
