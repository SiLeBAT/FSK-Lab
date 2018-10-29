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
	public void testName() {
		Param param = new Param();
		assertNull(param.getName());

		param.setName(name);
		assertEquals(name, param.getName());
	}

	@Test
	public void testOrigname() {
		Param param = new Param();
		assertNull(param.getOrigName());

		param.setOrigName(origname);
		assertEquals(origname, param.getOrigName());
	}

	@Test
	public void testIsStart() {
		Param param = new Param();
		assertNull(param.isStart());

		param.setIsStart(isStart);
		assertEquals(isStart, param.isStart());
	}

	@Test
	public void testValue() {
		Param param = new Param();
		assertNull(param.getValue());

		param.setValue(value);
		assertEquals(value, param.getValue(), .0);
	}

	@Test
	public void testError() {
		Param param = new Param();
		assertNull(param.getError());

		param.setError(error);
		assertEquals(error, param.getError(), .0);
	}

	@Test
	public void testMin() {
		Param param = new Param();
		assertNull(param.getMin());

		param.setMin(min);
		assertEquals(min, param.getMin(), .0);
	}

	@Test
	public void testMax() {
		Param param = new Param();
		assertNull(param.getMax());

		param.setMax(max);
		assertEquals(max, param.getMax(), .0);
	}

	@Test
	public void testP() {
		Param param = new Param();
		assertNull(param.getP());

		param.setP(p);
		assertEquals(p, param.getP(), .0);
	}

	@Test
	public void testT() {
		Param param = new Param();
		assertNull(param.getT());

		param.setT(t);
		assertEquals(t, param.getT(), .0);
	}

	@Test
	public void testMinGuess() {
		Param param = new Param();
		assertNull(param.getMinGuess());

		param.setMinGuess(minGuess);
		assertEquals(minGuess, param.getMinGuess(), .0);
	}

	@Test
	public void testMaxGuess() {
		Param param = new Param();
		assertNull(param.getMaxGuess());

		param.setMaxGuess(maxGuess);
		assertEquals(maxGuess, param.getMaxGuess(), .0);
	}

	@Test
	public void testCategory() {
		Param param = new Param();
		assertNull(param.getCategory());

		param.setCategory(category);
		assertEquals(category, param.getCategory());
	}

	@Test
	public void testUnit() {
		Param param = new Param();
		assertNull(param.getUnit());

		param.setUnit(unit);
		assertEquals(unit, param.getUnit());
	}

	@Test
	public void testDescription() {
		Param param = new Param();
		assertNull(param.getDescription());

		param.setDescription(description);
		assertEquals(description, param.getDescription());
	}

	@Test
	public void testCorrelations() {
		Param param = new Param();
		assertNull(param.getCorrelationNames());
		assertNull(param.getCorrelationValues());

		param.setCorrelationNames(correlationNames);
		param.setCorrelationValues(correlationValues);

		assertArrayEquals(correlationNames, param.getCorrelationNames());
		assertArrayEquals(correlationValues, param.getCorrelationValues(), 0.0);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Param param = new Param();
		param.setName(name);
		param.setOrigName(origname);
		param.setIsStart(isStart);
		param.setValue(value);
		param.setError(error);
		param.setMin(min);
		param.setMax(max);
		param.setP(p);
		param.setT(t);
		param.setMinGuess(minGuess);
		param.setMaxGuess(maxGuess);
		param.setCategory(category);
		param.setUnit(unit);
		param.setDescription(description);
		param.setCorrelationNames(correlationNames);
		param.setCorrelationValues(correlationValues);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		param.saveToNodeSettings(settings);
		
		assertEquals(name, settings.getString(Param.NAME));
		assertEquals(origname, settings.getString(Param.ORIGNAME));
		assertEquals(isStart, settings.getBoolean(Param.ISSTART));
		assertEquals(value, settings.getDouble(Param.VALUE), 0.0);
		assertEquals(error, settings.getDouble(Param.ERROR), 0.0);
		assertEquals(min, settings.getDouble(Param.MIN), 0.0);
		assertEquals(max, settings.getDouble(Param.MAX), 0.0);
		assertEquals(p, settings.getDouble(Param.P), 0.0);
		assertEquals(t, settings.getDouble(Param.T), 0.0);
		assertEquals(minGuess, settings.getDouble(Param.MINGUESS), 0.0);
		assertEquals(maxGuess, settings.getDouble(Param.MAXGUESS), 0.0);
		assertEquals(category, settings.getString(Param.CATEGORY));
		assertEquals(unit, settings.getString(Param.UNIT));
		assertEquals(description, settings.getString(Param.DESCRIPTION));
		assertArrayEquals(correlationNames, settings.getStringArray(Param.CORRELATION_NAMES));
		assertArrayEquals(correlationValues, settings.getDoubleArray(Param.CORRELATION_VALUES), 0.0);
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString(Param.NAME, name);
		settings.addString(Param.ORIGNAME, origname);
		settings.addBoolean(Param.ISSTART, isStart);
		settings.addDouble(Param.VALUE, value);
		settings.addDouble(Param.ERROR, error);
		settings.addDouble(Param.P, p);
		settings.addDouble(Param.T, t);
		settings.addDouble(Param.MINGUESS, minGuess);
		settings.addDouble(Param.MAXGUESS, maxGuess);
		settings.addString(Param.CATEGORY, category);
		settings.addString(Param.DESCRIPTION, description);
		settings.addStringArray(Param.CORRELATION_NAMES, correlationNames);
		settings.addDoubleArray(Param.CORRELATION_VALUES, correlationValues);
		
		Param param = new Param();
		param.loadFromNodeSettings(settings);
		
		assertEquals(name, param.getName());
		assertEquals(origname, param.getOrigName());
		assertEquals(isStart, param.isStart());
		assertEquals(value, param.getValue(), 0.0);
		assertEquals(error, param.getError(), 0.0);
		assertEquals(p, param.getP(), 0.0);
		assertEquals(t, param.getT(), 0.0);
		assertEquals(minGuess, param.getMinGuess(), 0.0);
		assertEquals(maxGuess, param.getMaxGuess(), 0.0);
		assertEquals(category, param.getCategory());
		assertEquals(description, param.getDescription());
		assertArrayEquals(correlationNames, param.getCorrelationNames());
		assertArrayEquals(correlationValues, param.getCorrelationValues(), 0.0);
	}
	
	@Test
	public void testToParam() {
		
		HashMap<String, Double> correlations = new HashMap<>();
		for (int i = 0; i < correlationNames.length; i++) {
			correlations.put(correlationNames[i], correlationValues[i]);
		}

		ParamXml paramXml = new ParamXml(name, origname, isStart, value, error, min, max, p, t, minGuess, maxGuess, category, unit, description, correlations);
		Param param = Param.toParam(paramXml);

		assertEquals(name, param.getName());
		assertEquals(origname, param.getOrigName());
		assertEquals(isStart, param.isStart());
		assertEquals(value, param.getValue(), 0.0);
		assertEquals(error, param.getError(), 0.0);
		assertEquals(p, param.getP(), 0.0);
		assertEquals(t, param.getT(), 0.0);
		assertEquals(minGuess, param.getMinGuess(), 0.0);
		assertEquals(maxGuess, param.getMaxGuess(), 0.0);
		assertEquals(category, param.getCategory());
		assertEquals(description, param.getDescription());
		assertArrayEquals(correlationNames, param.getCorrelationNames());
		assertArrayEquals(correlationValues, param.getCorrelationValues(), 0.0);
	}
	
	@Test
	public void testToParamXml() {
		Param param = new Param();
		param.setName(name);
		param.setOrigName(origname);
		param.setIsStart(isStart);
		param.setValue(value);
		param.setError(error);
		param.setMin(min);
		param.setMax(max);
		param.setP(p);
		param.setT(t);
		param.setMinGuess(minGuess);
		param.setMaxGuess(maxGuess);
		param.setCategory(category);
		param.setUnit(unit);
		param.setDescription(description);
		param.setCorrelationNames(correlationNames);
		param.setCorrelationValues(correlationValues);
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
