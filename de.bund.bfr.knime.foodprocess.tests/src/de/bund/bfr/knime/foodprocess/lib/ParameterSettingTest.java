package de.bund.bfr.knime.foodprocess.lib;

import static org.junit.Assert.*;

import org.junit.Test;

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

	// TODO: presAlt
	@Test
	public void testPresAlt() {
		
	}
	// TODO: pressureUnit
}
