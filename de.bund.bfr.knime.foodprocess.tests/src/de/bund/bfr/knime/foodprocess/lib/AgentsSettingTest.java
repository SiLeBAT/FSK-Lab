package de.bund.bfr.knime.foodprocess.lib;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class AgentsSettingTest {

	private static final String[] stringArray = new String[] { "string", "array" };
	private static final double[] doubleArray = new double[] { 1, 2, 3 };

	@org.junit.Test
	public void testConstructor() {
		AgentsSetting settings = new AgentsSetting();
		assertFalse(settings.isManualDef());
		assertFalse(settings.isRecipeGuess());
		assertNull(settings.getAgentsDef());
		assertNull(settings.getAgentsDef1());
		assertNull(settings.getAgentsDef2());
		assertNull(settings.getAgentsDef3());
		assertNull(settings.getAgentsDef4());
	}

	@Test
	public void testManualRef() {
		AgentsSetting settings = new AgentsSetting();
		settings.setManualDef(true);
		assertTrue(settings.isManualDef());
	}

	@Test
	public void testRecipeGuess() {
		AgentsSetting settings = new AgentsSetting();
		settings.setRecipeGuess(true);
		assertTrue(settings.isRecipeGuess());
	}

	@Test
	public void testAgentsDef() {
		AgentsSetting settings = new AgentsSetting();
		settings.setAgentsDef(stringArray);
		assertArrayEquals(stringArray, settings.getAgentsDef());
	}

	@Test
	public void testAgentsDef1() {
		AgentsSetting settings = new AgentsSetting();
		settings.setAgentsDef1(doubleArray);
		assertArrayEquals(doubleArray, settings.getAgentsDef1(), .0);
	}

	@Test
	public void testAgentsDef2() {
		AgentsSetting settings = new AgentsSetting();
		settings.setAgentsDef2(doubleArray);
		assertArrayEquals(doubleArray, settings.getAgentsDef2(), .0);
	}

	@Test
	public void testAgentsDef3() {
		AgentsSetting settings = new AgentsSetting();
		settings.setAgentsDef3(doubleArray);
		assertArrayEquals(doubleArray, settings.getAgentsDef3(), .0);
	}

	@Test
	public void testAgentsDef4() {
		AgentsSetting settings = new AgentsSetting();
		settings.setAgentsDef4(doubleArray);
		assertArrayEquals(doubleArray, settings.getAgentsDef4(), .0);
	}
	
	@Test
	public void testSaveSettings() throws InvalidSettingsException {
		AgentsSetting settings = new AgentsSetting();
		settings.setManualDef(true);
		settings.setRecipeGuess(true);
		settings.setAgentsDef(stringArray);
		settings.setAgentsDef1(doubleArray);
		settings.setAgentsDef2(doubleArray);
		settings.setAgentsDef3(doubleArray);
		settings.setAgentsDef4(doubleArray);
		
		NodeSettings nodeSettings = new NodeSettings("identifier");
		settings.saveSettings(nodeSettings);

		assertTrue(nodeSettings.getBoolean("recipeGuess"));
		assertTrue(nodeSettings.getBoolean("manualDef"));
		assertArrayEquals(stringArray, nodeSettings.getStringArray("agentsDef"));
		assertArrayEquals(doubleArray, nodeSettings.getDoubleArray("agentsDef1"), .0);
		assertArrayEquals(doubleArray, nodeSettings.getDoubleArray("agentsDef2"), .0);
		assertArrayEquals(doubleArray, nodeSettings.getDoubleArray("agentsDef3"), .0);
		assertArrayEquals(doubleArray, nodeSettings.getDoubleArray("agentsDef4"), .0);
	}
	
	@Test
	public void testSaveEmptySettings() throws InvalidSettingsException {
		AgentsSetting settings = new AgentsSetting();
		NodeSettings nodeSettings = new NodeSettings("identifier");
		settings.saveSettings(nodeSettings);
		
		assertFalse(nodeSettings.getBoolean("recipeGuess"));
		assertFalse(nodeSettings.getBoolean("manualDef"));
		assertFalse(nodeSettings.containsKey("agentsDef"));
		assertFalse(nodeSettings.containsKey("agentsDef1"));
		assertFalse(nodeSettings.containsKey("agentsDef2"));
		assertFalse(nodeSettings.containsKey("agentsDef3"));
		assertFalse(nodeSettings.containsKey("agentsDef4"));
	}
	
	@Test
	public void testLoadEmptySettings() throws InvalidSettingsException {
		NodeSettings nodeSettings = new NodeSettings("identifier");
		nodeSettings.addBoolean("recipeGuess", true);
		nodeSettings.addBoolean("manualDef", true);
		
		AgentsSetting settings = new AgentsSetting();
		settings.loadSettings(nodeSettings);

		assertTrue(settings.isRecipeGuess());
		assertTrue(settings.isManualDef());
		assertNull(settings.getAgentsDef());
		assertNull(settings.getAgentsDef1());
		assertNull(settings.getAgentsDef2());
		assertNull(settings.getAgentsDef3());
		assertNull(settings.getAgentsDef4());
	}
	
	@Test
	public void testLoadSettings() throws InvalidSettingsException {
		NodeSettings nodeSettings = new NodeSettings("identifier");
		nodeSettings.addBoolean("recipeGuess", true);
		nodeSettings.addBoolean("manualDef", true);
		nodeSettings.addStringArray("agentsDef", stringArray);
		nodeSettings.addDoubleArray("agentsDef1", doubleArray);
		nodeSettings.addDoubleArray("agentsDef2", doubleArray);
		nodeSettings.addDoubleArray("agentsDef3", doubleArray);
		nodeSettings.addDoubleArray("agentsDef4", doubleArray);
		
		AgentsSetting agentSettings = new AgentsSetting();
		agentSettings.loadSettings(nodeSettings);
		
		assertTrue(agentSettings.isRecipeGuess());
		assertTrue(agentSettings.isManualDef());
		assertArrayEquals(stringArray, agentSettings.getAgentsDef());
		assertArrayEquals(doubleArray, agentSettings.getAgentsDef1(), .0);
		assertArrayEquals(doubleArray, agentSettings.getAgentsDef2(), .0);
		assertArrayEquals(doubleArray, agentSettings.getAgentsDef3(), .0);
		assertArrayEquals(doubleArray, agentSettings.getAgentsDef4(), .0);
	}
}
