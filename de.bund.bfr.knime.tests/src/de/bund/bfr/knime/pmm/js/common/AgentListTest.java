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
		assertThat(list.getAgents(), is(nullValue()));

		final Agent[] agents = new Agent[] { agent };
		list.setAgents(agents);

		final Agent actualAgent = list.getAgents()[0];

		assertThat(actualAgent.id, equalTo(agent.id));
		assertThat(actualAgent.name, equalTo(agent.name));
		assertThat(actualAgent.detail, equalTo(agent.detail));
		assertThat(actualAgent.dbuuid, equalTo(agent.dbuuid));
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

		final Agent expected = agents[0];  // expected agent
		final Agent obtained = obtainedAgents[0];  // obtained agent

		assertThat(expected.id, equalTo(obtained.id));
		assertThat(expected.name, equalTo(obtained.name));
		assertThat(expected.detail, equalTo(obtained.detail));
		assertThat(expected.dbuuid, equalTo(obtained.dbuuid));
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

		assertThat(expected.id, equalTo(obtained.id));
		assertThat(expected.name, equalTo(obtained.name));
		assertThat(expected.detail, equalTo(obtained.detail));
		assertThat(expected.dbuuid, equalTo(obtained.dbuuid));
	}
}
