package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
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

		compare(list.getAgents()[0], agent);
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

		compare(obtainedAgents[0], agents[0]);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numAgents", 1);
		agent.saveToNodeSettings(settings.addNodeSettings("agents" + 0));

		final AgentList list = new AgentList();
		list.loadFromNodeSettings(settings);

		compare(list.getAgents()[0], agent);
	}

	private static void compare(final Agent obtained, final Agent expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.detail, equalTo(expected.detail));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}
}
