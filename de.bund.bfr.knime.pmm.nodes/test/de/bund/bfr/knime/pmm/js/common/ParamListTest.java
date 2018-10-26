package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;


public class ParamListTest {

	static Param param;
	static {
		param = new Param();	
		param.setName(ParamTest.name);
		param.setOrigName(ParamTest.origname);
		param.setIsStart(ParamTest.isStart);
		param.setValue(ParamTest.value);
		param.setError(ParamTest.error);
		param.setMin(ParamTest.min);
		param.setMax(ParamTest.max);
		param.setP(ParamTest.p);
		param.setT(ParamTest.t);
		param.setMinGuess(ParamTest.minGuess);
		param.setMaxGuess(ParamTest.maxGuess);
		param.setCategory(ParamTest.category);
		param.setUnit(ParamTest.unit);
		param.setDescription(ParamTest.description);
		param.setCorrelationNames(ParamTest.correlationNames);
		param.setCorrelationValues(ParamTest.correlationValues);
	}

	@Test
	public void testParams() {
		ParamList list = new ParamList();
		assertNull(list.getParams());

		list.setParams(new Param[] { param} );

		Param expected = param;  // expected Param
		Param obtained = list.getParams()[0];  // obtained Param

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getOrigName(), obtained.getOrigName());
		assertEquals(expected.isStart(), obtained.isStart());
		assertEquals(expected.getValue(), obtained.getValue());
		assertEquals(expected.getError(), obtained.getError());
		assertEquals(expected.getMin(), obtained.getMin());
		assertEquals(expected.getMax(), obtained.getMax());
		assertEquals(expected.getP(), obtained.getP());
		assertEquals(expected.getT(), obtained.getT());
		assertEquals(expected.getMinGuess(), obtained.getMinGuess());
		assertEquals(expected.getMaxGuess(), obtained.getMaxGuess());
		assertEquals(expected.getCategory(), obtained.getCategory());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertArrayEquals(expected.getCorrelationNames(), obtained.getCorrelationNames());
		assertArrayEquals(expected.getCorrelationValues(), obtained.getCorrelationValues(), 0.0);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		ParamList list = new ParamList();
		list.setParams(new Param[] { param });

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt(ParamList.NUM_PARAMS));

		Param expected = param;  // expected Param
		Param obtained = new Param();  // obtained Param
		obtained.loadFromNodeSettings(settings.getNodeSettings(ParamList.PARAMS + 0));

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getOrigName(), obtained.getOrigName());
		assertEquals(expected.isStart(), obtained.isStart());
		assertEquals(expected.getValue(), obtained.getValue());
		assertEquals(expected.getError(), obtained.getError());
		assertEquals(expected.getMin(), obtained.getMin());
		assertEquals(expected.getMax(), obtained.getMax());
		assertEquals(expected.getP(), obtained.getP());
		assertEquals(expected.getT(), obtained.getT());
		assertEquals(expected.getMinGuess(), obtained.getMinGuess());
		assertEquals(expected.getMaxGuess(), obtained.getMaxGuess());
		assertEquals(expected.getCategory(), obtained.getCategory());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertArrayEquals(expected.getCorrelationNames(), obtained.getCorrelationNames());
		assertArrayEquals(expected.getCorrelationValues(), obtained.getCorrelationValues(), 0.0);
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(ParamList.NUM_PARAMS, 1);
		param.saveToNodeSettings(settings.addNodeSettings(ParamList.PARAMS + 0));

		ParamList list = new ParamList();
		list.loadFromNodeSettings(settings);

		Param expected = param;  // expected Param
		Param obtained = list.getParams()[0];  // obtained Param

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getOrigName(), obtained.getOrigName());
		assertEquals(expected.isStart(), obtained.isStart());
		assertEquals(expected.getValue(), obtained.getValue());
		assertEquals(expected.getError(), obtained.getError());
		assertEquals(expected.getMin(), obtained.getMin());
		assertEquals(expected.getMax(), obtained.getMax());
		assertEquals(expected.getP(), obtained.getP());
		assertEquals(expected.getT(), obtained.getT());
		assertEquals(expected.getMinGuess(), obtained.getMinGuess());
		assertEquals(expected.getMaxGuess(), obtained.getMaxGuess());
		assertEquals(expected.getCategory(), obtained.getCategory());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertArrayEquals(expected.getCorrelationNames(), obtained.getCorrelationNames());
		assertArrayEquals(expected.getCorrelationValues(), obtained.getCorrelationValues(), 0.0);
	}
}