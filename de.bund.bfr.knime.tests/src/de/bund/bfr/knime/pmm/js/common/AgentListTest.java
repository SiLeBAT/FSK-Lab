package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class AgentListTest {

	static Agent agent;

	static {
		agent = new Agent();
		agent.id = AgentTest.id;
		agent.name = AgentTest.name;
		agent.detail = AgentTest.detail;
		agent.dbuuid = AgentTest.dbuuid;
	}

	@Test
	public void testAgents() {
		AgentList list = new AgentList();
		assertNull(list.getAgents());

		Agent[] agents = new Agent[] { agent };
		list.setAgents(agents);
		assertEquals(agents[0].id, list.getAgents()[0].id);
		assertEquals(agents[0].name, list.getAgents()[0].name);
		assertEquals(agents[0].detail, list.getAgents()[0].detail);
		assertEquals(agents[0].dbuuid, list.getAgents()[0].dbuuid);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Agent[] agents = new Agent[] { agent };
		AgentList list = new AgentList();
		list.setAgents(agents);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);
		
		assertEquals(1, settings.getInt("numAgents"));
		
		Agent[] obtainedAgents = new Agent[1];
		obtainedAgents[0] = new Agent();
		obtainedAgents[0].loadFromNodeSettings(settings.getNodeSettings("agents" + 0));
		
		Agent expected = agents[0];  // expected agent
		Agent obtained = obtainedAgents[0];  // obtained agent
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numAgents", 1);
		agent.saveToNodeSettings(settings.addNodeSettings("agents" + 0));

		AgentList list = new AgentList();
		list.loadFromNodeSettings(settings);
		
		Agent expected = agent;  // expected agent
		Agent obtained = list.getAgents()[0];  // obtained agent
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}
}
