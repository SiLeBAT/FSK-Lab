package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class ParamTest {

	static String name = "mu_max";
	static String origname = "mu_max";
	static boolean isStart = false;
	static double value = 0.03412808921078558;
	static double error = 9.922557266695599E-4;
	static double min = 0.0;
	static double max = 5.0;
	static double p = 2.220446049250313E-16;
	static double t = 34.39444922664667;
	static double minGuess = -1.0;
	static double maxGuess = 6.0;
	static String category = "a_category";
	static String unit = "log10";
	static String description = "specific growth rate related to ln() transformed data - min/max selected to improve fitting";
	static String[] correlationNames;
	static double[] correlationValues;
	static {
		correlationNames = new String[] { "mu_max", "Ymax", "Y0", "h0" };
		correlationValues = new double[] { 9.845714271085363E-7, -6.137961639952814E-5, 7.843710581862156E-5,
				3.2387019240707804E-4 };
	}
	
	@Test
	public void testConstructor() {
		Param param = new Param();
		assertNull(param.name);
		assertNull(param.origName);
		assertNull(param.isStart);
		assertNull(param.value);
		assertNull(param.error);
		assertNull(param.min);
		assertNull(param.max);
		assertNull(param.p);
		assertNull(param.t);
		assertNull(param.minGuess);
		assertNull(param.maxGuess);
		assertNull(param.category);
		assertNull(param.unit);
		assertNull(param.description);
		assertNull(param.correlationNames);
		assertNull(param.correlationValues);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Param param = new Param();
		param.name = name;
		param.origName = origname;
		param.isStart = isStart;
		param.value = value;
		param.error = error;
		param.min = min;
		param.max = max;
		param.p = p;
		param.t = t;
		param.minGuess = minGuess;
		param.maxGuess = maxGuess;
		param.category = category;
		param.unit = unit;
		param.description = description;
		param.correlationNames = correlationNames;
		param.correlationValues = correlationValues;
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		param.saveToNodeSettings(settings);
		
		assertEquals(name, settings.getString("name"));
		assertEquals(origname, settings.getString("origname"));
		assertEquals(isStart, settings.getBoolean("isStart"));
		assertEquals(value, settings.getDouble("value"), 0.0);
		assertEquals(error, settings.getDouble("error"), 0.0);
		assertEquals(min, settings.getDouble("min"), 0.0);
		assertEquals(max, settings.getDouble("max"), 0.0);
		assertEquals(p, settings.getDouble("P"), 0.0);
		assertEquals(t, settings.getDouble("t"), 0.0);
		assertEquals(minGuess, settings.getDouble("minGuess"), 0.0);
		assertEquals(maxGuess, settings.getDouble("maxGuess"), 0.0);
		assertEquals(category, settings.getString("category"));
		assertEquals(unit, settings.getString("unit"));
		assertEquals(description, settings.getString("description"));
		assertArrayEquals(correlationNames, settings.getStringArray("correlationNames"));
		assertArrayEquals(correlationValues, settings.getDoubleArray("correlationValues"), 0.0);
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", name);
		settings.addString("origname", origname);
		settings.addBoolean("isStart", isStart);
		settings.addDouble("value", value);
		settings.addDouble("error", error);
		settings.addDouble("P", p);
		settings.addDouble("t", t);
		settings.addDouble("minGuess", minGuess);
		settings.addDouble("maxGuess", maxGuess);
		settings.addString("category", category);
		settings.addString("description", description);
		settings.addStringArray("correlationNames", correlationNames);
		settings.addDoubleArray("correlationValues", correlationValues);
		
		Param param = new Param();
		param.loadFromNodeSettings(settings);
		
		assertEquals(name, param.name);
		assertEquals(origname, param.origName);
		assertEquals(isStart, param.isStart);
		assertEquals(value, param.value, 0.0);
		assertEquals(error, param.error, 0.0);
		assertEquals(p, param.p, 0.0);
		assertEquals(t, param.t, 0.0);
		assertEquals(minGuess, param.minGuess, 0.0);
		assertEquals(maxGuess, param.maxGuess, 0.0);
		assertEquals(category, param.category);
		assertEquals(description, param.description);
		assertArrayEquals(correlationNames, param.correlationNames);
		assertArrayEquals(correlationValues, param.correlationValues, 0.0);
	}
	
	@Test
	public void testToParam() {
		
		HashMap<String, Double> correlations = new HashMap<>();
		for (int i = 0; i < correlationNames.length; i++) {
			correlations.put(correlationNames[i], correlationValues[i]);
		}

		ParamXml paramXml = new ParamXml(name, origname, isStart, value, error, min, max, p, t, minGuess, maxGuess, category, unit, description, correlations);
		Param param = Param.toParam(paramXml);

		assertEquals(name, param.name);
		assertEquals(origname, param.origName);
		assertEquals(isStart, param.isStart);
		assertEquals(value, param.value, 0.0);
		assertEquals(error, param.error, 0.0);
		assertEquals(p, param.p, 0.0);
		assertEquals(t, param.t, 0.0);
		assertEquals(minGuess, param.minGuess, 0.0);
		assertEquals(maxGuess, param.maxGuess, 0.0);
		assertEquals(category, param.category);
		assertEquals(description, param.description);
		assertArrayEquals(correlationNames, param.correlationNames);
		assertArrayEquals(correlationValues, param.correlationValues, 0.0);
	}
	
	@Test
	public void testToParamXml() {
		Param param = new Param();
		param.name = name;
		param.origName = origname;
		param.isStart = isStart;
		param.value = value;
		param.error = error;
		param.min = min;
		param.max = max;
		param.p = p;
		param.t = t;
		param.minGuess = minGuess;
		param.maxGuess = maxGuess;
		param.category = category;
		param.unit = unit;
		param.description = description;
		param.correlationNames = correlationNames;
		param.correlationValues = correlationValues;
		ParamXml paramXml = param.toParamXml();
		
		assertEquals(name, paramXml.name);
		assertEquals(origname, paramXml.origName);
		assertEquals(isStart, paramXml.isStartParam);
		assertEquals(value, paramXml.value, 0.0);
		assertEquals(error, paramXml.error, 0.0);
		assertEquals(p, paramXml.P, 0.0);
		assertEquals(t, paramXml.t, 0.0);
		assertEquals(minGuess, paramXml.minGuess, 0.0);
		assertEquals(maxGuess, paramXml.maxGuess, 0.0);
		assertEquals(category, paramXml.category);
		assertEquals(description, paramXml.description);
		
		HashMap<String, Double> correlations = new HashMap<>();
		for (int i = 0; i < correlationNames.length; i++) {
			correlations.put(correlationNames[i], correlationValues[i]);
		}
		assertEquals(correlations, paramXml.correlations);
	}
}
