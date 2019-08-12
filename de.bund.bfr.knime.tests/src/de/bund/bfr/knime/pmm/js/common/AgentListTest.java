package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
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
		final AgentList list = new AgentList();
		assertNull(list.getAgents());

		final Agent[] agents = new Agent[] { agent };
		list.setAgents(agents);
		assertEquals(agents[0].id, list.getAgents()[0].id);
		assertEquals(agents[0].name, list.getAgents()[0].name);
		assertEquals(agents[0].detail, list.getAgents()[0].detail);
		assertEquals(agents[0].dbuuid, list.getAgents()[0].dbuuid);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final Agent[] agents = new Agent[] { agent };
		final AgentList list = new AgentList();
		list.setAgents(agents);

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numAgents"));

		final Agent[] obtainedAgents = new Agent[1];
		obtainedAgents[0] = new Agent();
		obtainedAgents[0].loadFromNodeSettings(settings.getNodeSettings("agents" + 0));

		final Agent expected = agents[0];  // expected agent
		final Agent obtained = obtainedAgents[0];  // obtained agent

		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numAgents", 1);
		agent.saveToNodeSettings(settings.addNodeSettings("agents" + 0));

		final AgentList list = new AgentList();
		list.loadFromNodeSettings(settings);

		final Agent expected = agent;  // expected agent
		final Agent obtained = list.getAgents()[0];  // obtained agent

		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}
}
