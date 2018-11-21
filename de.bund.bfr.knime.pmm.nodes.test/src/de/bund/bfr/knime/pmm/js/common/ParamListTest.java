package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;


public class ParamListTest {

	static Param param;
	static {
		param = new Param();	
		param.name = ParamTest.name;
		param.origName = ParamTest.origname;
		param.isStart = ParamTest.isStart;
		param.value = ParamTest.value;
		param.error = ParamTest.error;
		param.min = ParamTest.min;
		param.max = ParamTest.max;
		param.p = ParamTest.p;
		param.t = ParamTest.t;
		param.minGuess = ParamTest.minGuess;
		param.maxGuess = ParamTest.maxGuess;
		param.category = ParamTest.category;
		param.unit = ParamTest.unit;
		param.description = ParamTest.description;
		param.correlationNames = ParamTest.correlationNames;
		param.correlationValues = ParamTest.correlationValues;
	}

	@Test
	public void testParams() {
		ParamList list = new ParamList();
		assertNull(list.getParams());

		list.setParams(new Param[] { param} );

		Param expected = param;  // expected Param
		Param obtained = list.getParams()[0];  // obtained Param

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.origName, obtained.origName);
		assertEquals(expected.isStart, obtained.isStart);
		assertEquals(expected.value, obtained.value);
		assertEquals(expected.error, obtained.error);
		assertEquals(expected.min, obtained.min);
		assertEquals(expected.max, obtained.max);
		assertEquals(expected.p, obtained.p);
		assertEquals(expected.t, obtained.t);
		assertEquals(expected.minGuess, obtained.minGuess);
		assertEquals(expected.maxGuess, obtained.maxGuess);
		assertEquals(expected.category, obtained.category);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
		assertArrayEquals(expected.correlationNames, obtained.correlationNames);
		assertArrayEquals(expected.correlationValues, obtained.correlationValues, 0.0);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		ParamList list = new ParamList();
		list.setParams(new Param[] { param });

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numParams"));

		Param expected = param;  // expected Param
		Param obtained = new Param();  // obtained Param
		obtained.loadFromNodeSettings(settings.getNodeSettings("params0"));

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.origName, obtained.origName);
		assertEquals(expected.isStart, obtained.isStart);
		assertEquals(expected.value, obtained.value);
		assertEquals(expected.error, obtained.error);
		assertEquals(expected.min, obtained.min);
		assertEquals(expected.max, obtained.max);
		assertEquals(expected.p, obtained.p);
		assertEquals(expected.t, obtained.t);
		assertEquals(expected.minGuess, obtained.minGuess);
		assertEquals(expected.maxGuess, obtained.maxGuess);
		assertEquals(expected.category, obtained.category);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
		assertArrayEquals(expected.correlationNames, obtained.correlationNames);
		assertArrayEquals(expected.correlationValues, obtained.correlationValues, 0.0);
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numParams", 1);
		param.saveToNodeSettings(settings.addNodeSettings("params0"));

		ParamList list = new ParamList();
		list.loadFromNodeSettings(settings);

		Param expected = param;  // expected Param
		Param obtained = list.getParams()[0];  // obtained Param

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.origName, obtained.origName);
		assertEquals(expected.isStart, obtained.isStart);
		assertEquals(expected.value, obtained.value);
		assertEquals(expected.error, obtained.error);
		assertEquals(expected.min, obtained.min);
		assertEquals(expected.max, obtained.max);
		assertEquals(expected.p, obtained.p);
		assertEquals(expected.t, obtained.t);
		assertEquals(expected.minGuess, obtained.minGuess);
		assertEquals(expected.maxGuess, obtained.maxGuess);
		assertEquals(expected.category, obtained.category);
		assertEquals(expected.unit, obtained.unit);
		assertEquals(expected.description, obtained.description);
		assertArrayEquals(expected.correlationNames, obtained.correlationNames);
		assertArrayEquals(expected.correlationValues, obtained.correlationValues, 0.0);
	}
}