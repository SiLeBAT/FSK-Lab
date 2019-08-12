package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		assertNull(agent.id);
		assertNull(agent.name);
		assertNull(agent.detail);
		assertNull(agent.dbuuid);
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

		assertEquals(id, settings.getInt("id"));
		assertEquals(name, settings.getString("name"));
		assertEquals(detail, settings.getString("detail"));
		assertEquals(dbuuid, settings.getString("dbuuid"));
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

		assertEquals(id, agent.id.intValue());
		assertEquals(name, agent.name);
		assertEquals(detail, agent.detail);
		assertEquals(dbuuid, agent.dbuuid);
	}

	@Test
	public void testToAgent() {
		final Agent agent = Agent.toAgent(new AgentXml(id, name, detail, dbuuid));

		assertEquals(id, agent.id.intValue());
		assertEquals(name, agent.name);
		assertEquals(detail, agent.detail);
		assertEquals(dbuuid, agent.dbuuid);
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
