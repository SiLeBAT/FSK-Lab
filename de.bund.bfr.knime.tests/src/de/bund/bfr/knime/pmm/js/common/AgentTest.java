package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.AgentXml;

@SuppressWarnings("static-method")
public class AgentTest {

	static Agent agent;
	static {
		agent = new Agent();
		agent.id = 4024;
		agent.name = "salmonella spp";
		agent.detail = "Salmonella spec";
		agent.dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	}

	@Test
	public void testConstructor() {
		final Agent agent = new Agent();
		assertThat(agent.id, is(nullValue()));
		assertThat(agent.name, is(nullValue()));
		assertThat(agent.detail, is(nullValue()));
		assertThat(agent.dbuuid, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		agent.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(agent.id));
		assertThat(settings.getString("name"), equalTo(agent.name));
		assertThat(settings.getString("detail"), equalTo(agent.detail));
		assertThat(settings.getString("dbuuid"), equalTo(agent.dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", agent.id);
		settings.addString("name", agent.name);
		settings.addString("detail", agent.detail);
		settings.addString("dbuuid", agent.dbuuid);

		final Agent obtained = new Agent();
		obtained.loadFromNodeSettings(settings);

		compare(obtained, agent);
	}

	@Test
	public void testToAgent() {
		final Agent obtained = Agent.toAgent(new AgentXml(agent.id, agent.name, agent.detail, agent.dbuuid));
		compare(obtained, agent);
	}

	@Test
	public void testToAgentXml() {
		final AgentXml agentXml = agent.toAgentXml();

		assertThat(agentXml.id, equalTo(agent.id));
		assertThat(agentXml.name, equalTo(agent.name));
		assertThat(agentXml.detail, equalTo(agent.detail));
		assertThat(agentXml.dbuuid, equalTo(agent.dbuuid));
	}

	private static void compare(final Agent obtained, final Agent expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.detail, equalTo(expected.detail));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}
}
