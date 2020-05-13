package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class AgentListTest {

	static Agent agent = AgentTest.agent;

	@Test
	public void testAgents() {
		final AgentList list = new AgentList();
		assertThat(list.getAgents(), is(nullValue()));

		final Agent[] agents = new Agent[] { agent };
		list.setAgents(agents);

		TestUtils.compare(list.getAgents()[0], agent);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final Agent[] agents = new Agent[] { agent };
		final AgentList list = new AgentList();
		list.setAgents(agents);

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);
		assertThat(settings.getInt("numAgents"), is(1));

		final Agent[] obtainedAgents = new Agent[1];
		obtainedAgents[0] = new Agent();
		obtainedAgents[0].loadFromNodeSettings(settings.getNodeSettings("agents" + 0));

		TestUtils.compare(obtainedAgents[0], agents[0]);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numAgents", 1);
		agent.saveToNodeSettings(settings.addNodeSettings("agents" + 0));

		final AgentList list = new AgentList();
		list.loadFromNodeSettings(settings);

		TestUtils.compare(list.getAgents()[0], agent);
	}
}
