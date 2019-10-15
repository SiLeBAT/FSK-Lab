package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.AgentXml;

@SuppressWarnings("static-method")
public class AgentTest {

	static int id = 4024;
	static String name = "salmonella spp";
	static String detail = "Salmonella spec";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";

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
		final Agent agent = new Agent();
		agent.id = id;
		agent.name = name;
		agent.detail = detail;
		agent.dbuuid = dbuuid;

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		agent.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(id));
		assertThat(settings.getString("name"), equalTo(name));
		assertThat(settings.getString("detail"), equalTo(detail));
		assertThat(settings.getString("dbuuid"), equalTo(dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("name", name);
		settings.addString("detail", detail);
		settings.addString("dbuuid", dbuuid);

		final Agent agent = new Agent();
		agent.loadFromNodeSettings(settings);

		assertThat(agent.id, equalTo(id));
		assertThat(agent.name, equalTo(name));
		assertThat(agent.detail, equalTo(detail));
		assertThat(agent.dbuuid, equalTo(dbuuid));
	}

	@Test
	public void testToAgent() {
		final Agent agent = Agent.toAgent(new AgentXml(id, name, detail, dbuuid));
		assertThat(agent.id, equalTo(id));
		assertThat(agent.name, equalTo(name));
		assertThat(agent.detail, equalTo(detail));
		assertThat(agent.dbuuid, equalTo(dbuuid));
	}

	@Test
	public void testToAgentXml() {
		final Agent agent = new Agent();
		agent.id = id;
		agent.name = name;
		agent.detail = detail;
		agent.dbuuid = dbuuid;
		final AgentXml agentXml = agent.toAgentXml();

		assertEquals(id, agentXml.id.intValue());
		assertEquals(name, agentXml.name);
		assertEquals(detail, agentXml.detail);
		assertEquals(dbuuid, agentXml.dbuuid);
	}
}
