package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class ParamListTest {

	static Param param = ParamTest.param;

	@Test
	public void testParams() {
		final ParamList list = new ParamList();
		assertThat(list.getParams(), is(nullValue()));

		list.setParams(new Param[] { param });

		final Param expected = param; // expected Param
		final Param obtained = list.getParams()[0]; // obtained Param
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final ParamList list = new ParamList();
		list.setParams(new Param[] { param });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertThat(settings.getInt("numParams"), is(1));

		final Param expected = param; // expected Param
		final Param obtained = new Param(); // obtained Param
		obtained.loadFromNodeSettings(settings.getNodeSettings("params0"));

		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numParams", 1);
		param.saveToNodeSettings(settings.addNodeSettings("params0"));

		final ParamList list = new ParamList();
		list.loadFromNodeSettings(settings);

		final Param expected = param; // expected Param
		final Param obtained = list.getParams()[0]; // obtained Param
		TestUtils.compare(obtained, expected);
	}
}