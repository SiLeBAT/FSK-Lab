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
		agent.setId(AgentTest.id);
		agent.setName(AgentTest.name);
		agent.setDetail(AgentTest.detail);
		agent.setDbuuid(AgentTest.dbuuid);
	}

	@Test
	public void testAgents() {
		AgentList list = new AgentList();
		assertNull(list.getAgents());

		Agent[] agents = new Agent[] { agent };
		list.setAgents(agents);
		assertEquals(agents[0].getId(), list.getAgents()[0].getId());
		assertEquals(agents[0].getName(), list.getAgents()[0].getName());
		assertEquals(agents[0].getDetail(), list.getAgents()[0].getDetail());
		assertEquals(agents[0].getDbuuid(), list.getAgents()[0].getDbuuid());
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
		
		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getDetail(), obtained.getDetail());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
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
		
		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getDetail(), obtained.getDetail());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}
}
