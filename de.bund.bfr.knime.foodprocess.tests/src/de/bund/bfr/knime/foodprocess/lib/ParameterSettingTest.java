package de.bund.bfr.knime.foodprocess.lib;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class ParameterSettingTest {

	@Test
	public void testConstructor() {
		ParametersSetting parameterSetting = new ParametersSetting();
		assertNull(parameterSetting.getTemperatureUnit());
		assertNull(parameterSetting.getVolume());
		assertNull(parameterSetting.getVolumeUnit());
		assertNull(parameterSetting.getAw());
		assertNull(parameterSetting.getAwAlt());
		assertNull(parameterSetting.getPh());
		assertNull(parameterSetting.getPhAlt());
		assertNull(parameterSetting.getPressure());
		assertNull(parameterSetting.getPressureUnit());
	}
	
	@Test
	public void testTemperatureUnit() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setTemperatureUnit("temperatureUnit");
		assertEquals("temperatureUnit", parameterSetting.getTemperatureUnit());
	}
	
	@Test
	public void testVolume() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setVolume("volume");
		assertEquals("volume", parameterSetting.getVolume());
	}
	
	@Test
	public void testVolumeUnit() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setVolumeUnit("volumeUnit");
		assertEquals("volumeUnit", parameterSetting.getVolumeUnit());
	}
	
	@Test
	public void testTemperature() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setTemperature("temperature");
		assertEquals("temperature", parameterSetting.getTemperature());
	}

	@Test
	public void testAw() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setAw("aw");
		assertEquals("aw", parameterSetting.getAw());
	}

	@Test
	public void testAwAlt() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setAwAlt("awAlt");
		assertEquals("awAlt", parameterSetting.getAwAlt());
	}

	@Test
	public void testPh() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setPh("pH");
		assertEquals("pH", parameterSetting.getPh());
	}

	@Test
	public void testPhAlt() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setPhAlt("pHAlt");
		assertEquals("pHAlt", parameterSetting.getPhAlt());
	}

	@Test
	public void testPressure() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setPressure("pressure");
		assertEquals("pressure", parameterSetting.getPressure());
	}

	@Test
	public void testPresAlt() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setPresAlt("presAlt");
		assertEquals("presAlt", parameterSetting.getPresAlt());
	}

	@Test
	public void testPressureUnit() {
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.setPressureUnit("pressureUnit");
		assertEquals("pressureUnit", parameterSetting.getPressureUnit());
	}
	
	@Test
	public void testSaveEmptySettings() throws InvalidSettingsException {
		ParametersSetting parameterSetting = new ParametersSetting();
		
		NodeSettings nodeSettings = new NodeSettings("identifier");
		parameterSetting.saveSettings(nodeSettings);

		assertFalse(nodeSettings.containsKey("volume"));
		assertNull(nodeSettings.getString("volumeUnit"));
		assertFalse(nodeSettings.containsKey("temperature"));
		assertFalse(nodeSettings.containsKey("tempAlt"));
		assertNull(nodeSettings.getString("temperatureUnit"));
		assertFalse(nodeSettings.containsKey("ph"));
		assertFalse(nodeSettings.containsKey("phAlt"));
		assertFalse(nodeSettings.containsKey("aw"));
		assertFalse(nodeSettings.containsKey("awAlt"));
		assertFalse(nodeSettings.containsKey("pressure"));
		assertFalse(nodeSettings.containsKey("presAlt"));
		assertNull(nodeSettings.getString("pressureUnit"));
	}
	
	@Test
	public void testSaveSettings() throws InvalidSettingsException {
		ParametersSetting parametersSetting = new ParametersSetting();
		parametersSetting.setVolume("volume");
		parametersSetting.setVolumeUnit("volumeUnit");
		parametersSetting.setTemperature("temperature");
		parametersSetting.setTempAlt("tempAlt");
		parametersSetting.setTemperatureUnit("temperatureUnit");
		parametersSetting.setPh("ph");
		parametersSetting.setPhAlt("phAlt");
		parametersSetting.setAw("aw");
		parametersSetting.setAwAlt("awAlt");
		parametersSetting.setPressure("pressure");
		parametersSetting.setPresAlt("presAlt");
		parametersSetting.setPressureUnit("pressureUnit");
		
		NodeSettings nodeSettings = new NodeSettings("identifier");
		parametersSetting.saveSettings(nodeSettings);
		
		assertEquals("volume", nodeSettings.getString("volume"));
		assertEquals("volumeUnit", nodeSettings.getString("volumeUnit"));
		assertEquals("temperature", nodeSettings.getString("temperature"));
		assertEquals("tempAlt", nodeSettings.getString("temperatureAlt"));
		assertEquals("temperatureUnit", nodeSettings.getString("temperatureUnit"));
		assertEquals("ph", nodeSettings.getString("ph"));
		assertEquals("phAlt", nodeSettings.getString("phAlt"));
		assertEquals("aw", nodeSettings.getString("aw"));
		assertEquals("awAlt", nodeSettings.getString("awAlt"));
		assertEquals("pressure", nodeSettings.getString("pressure"));
		assertEquals("presAlt", nodeSettings.getString("pressureAlt"));
		assertEquals("pressureUnit", nodeSettings.getString("pressureUnit"));
	}

	@Test
	public void testLoadEmptySettings() throws Exception {
		NodeSettings nodeSettings = new NodeSettings("identifier");
		nodeSettings.addString("volumeUnit", "volumeUnit");
		nodeSettings.addString("temperatureUnit", "temperatureUnit");
		nodeSettings.addString("pressureUnit", "pressureUnit");
		
		ParametersSetting parametersSetting = new ParametersSetting();
		parametersSetting.loadSettings(nodeSettings);
		
		assertNull(parametersSetting.getVolume());
		assertEquals("volumeUnit", parametersSetting.getVolumeUnit());
		assertNull(parametersSetting.getTemperature());
		assertNull(parametersSetting.getTempAlt());
		assertEquals("temperatureUnit", parametersSetting.getTemperatureUnit());
		assertNull(parametersSetting.getPh());
		assertNull(parametersSetting.getPhAlt());
		assertNull(parametersSetting.getAw());
		assertNull(parametersSetting.getAwAlt());
		assertNull(parametersSetting.getPressure());
		assertNull(parametersSetting.getPresAlt());
		assertEquals("pressureUnit", parametersSetting.getPressureUnit());
	}

	@Test
	public void testLoadSettings() throws Exception {
		NodeSettings nodeSettings = new NodeSettings("identifier");
		nodeSettings.addString("volume", "volume");
		nodeSettings.addString("volumeUnit", "volumeUnit");
		nodeSettings.addString("temperature", "temperature");
		nodeSettings.addString("temperatureAlt", "temperatureAlt");
		nodeSettings.addString("temperatureUnit", "temperatureUnit");
		nodeSettings.addString("ph", "ph");
		nodeSettings.addString("phAlt", "phAlt");
		nodeSettings.addString("aw", "aw");
		nodeSettings.addString("awAlt", "awAlt");
		nodeSettings.addString("pressure", "pressure");
		nodeSettings.addString("pressureAlt", "pressureAlt");
		nodeSettings.addString("pressureUnit", "pressureUnit");
		
		ParametersSetting parameterSetting = new ParametersSetting();
		parameterSetting.loadSettings(nodeSettings);
		
		assertEquals("volume", parameterSetting.getVolume());
		assertEquals("volumeUnit", parameterSetting.getVolumeUnit());
		assertEquals("temperature", parameterSetting.getTemperature());
		assertEquals("temperatureAlt", parameterSetting.getTempAlt());
		assertEquals("temperatureUnit", parameterSetting.getTemperatureUnit());
		assertEquals("ph", parameterSetting.getPh());
		assertEquals("phAlt", parameterSetting.getPhAlt());
		assertEquals("aw", parameterSetting.getAw());
		assertEquals("awAlt", parameterSetting.getAwAlt());
		assertEquals("pressure", parameterSetting.getPressure());
		assertEquals("pressureAlt", parameterSetting.getPresAlt());
		assertEquals("pressureUnit", parameterSetting.getPressureUnit());
	}
}
