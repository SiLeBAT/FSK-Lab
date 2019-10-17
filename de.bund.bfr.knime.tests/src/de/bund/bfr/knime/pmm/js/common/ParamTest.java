package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.ParamXml;

@SuppressWarnings("static-method")
public class ParamTest {

	static Param param;
	static {
		param = new Param();
		param.name = "mu_max";
		param.origName = "mu_max";
		param.isStart = false;
		param.value = 0.03412808921078558;
		param.error = 9.922557266695599E-4;
		param.min = 0.0;
		param.max = 5.0;
		param.p = 2.220446049250313E-16;
		param.t = 34.39444922664667;
		param.minGuess = -1.0;
		param.maxGuess = 6.0;
		param.category = "a_category";
		param.unit = "log10";
		param.description = "specific growth rate related to ln() transformed data - min/max selected to improve fitting";
		param.correlationNames = new String[] { "mu_max", "Ymax", "Y0", "h0" };
		param.correlationValues = new double[] { 9.845714271085363E-7, -6.137961639952814E-5, 7.843710581862156E-5,
				3.2387019240707804E-4 };
	}

	@Test
	public void testConstructor() {
		final Param param = new Param();
		assertThat(param.name, is(nullValue()));
		assertThat(param.origName, is(nullValue()));
		assertThat(param.isStart, is(nullValue()));
		assertThat(param.value, is(nullValue()));
		assertThat(param.error, is(nullValue()));
		assertThat(param.min, is(nullValue()));
		assertThat(param.max, is(nullValue()));
		assertThat(param.p, is(nullValue()));
		assertThat(param.t, is(nullValue()));
		assertThat(param.minGuess, is(nullValue()));
		assertThat(param.maxGuess, is(nullValue()));
		assertThat(param.category, is(nullValue()));
		assertThat(param.unit, is(nullValue()));
		assertThat(param.description, is(nullValue()));
		assertThat(param.correlationNames, is(nullValue()));
		assertThat(param.correlationValues, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		param.saveToNodeSettings(settings);

		assertThat(settings.getString("name"), equalTo(param.name));
		assertThat(settings.getString("origname"), equalTo(param.origName));
		assertThat(settings.getBoolean("isStart"), equalTo(param.isStart));
		assertThat(settings.getDouble("value"), equalTo(param.value));
		assertThat(settings.getDouble("error"), equalTo(param.error));
		assertThat(settings.getDouble("min"), equalTo(param.min));
		assertThat(settings.getDouble("max"), equalTo(param.max));
		assertThat(settings.getDouble("P"), equalTo(param.p));
		assertThat(settings.getDouble("t"), equalTo(param.t));
		assertThat(settings.getDouble("minGuess"), equalTo(param.minGuess));
		assertThat(settings.getDouble("maxGuess"), equalTo(param.maxGuess));
		assertThat(settings.getString("category"), equalTo(param.category));
		assertThat(settings.getString("unit"), equalTo(param.unit));
		assertThat(settings.getString("description"), equalTo(param.description));
		assertThat(settings.getStringArray("correlationNames"), equalTo(param.correlationNames));
		assertThat(settings.getDoubleArray("correlationValues"), equalTo(param.correlationValues));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", param.name);
		settings.addString("origname", param.origName);
		settings.addBoolean("isStart", param.isStart);
		settings.addDouble("value", param.value);
		settings.addDouble("error", param.error);
		settings.addDouble("min", param.min);
		settings.addDouble("max", param.max);
		settings.addDouble("P", param.p);
		settings.addDouble("t", param.t);
		settings.addDouble("minGuess", param.minGuess);
		settings.addDouble("maxGuess", param.maxGuess);
		settings.addString("category", param.category);
		settings.addString("unit", param.unit);
		settings.addString("description", param.description);
		settings.addStringArray("correlationNames", param.correlationNames);
		settings.addDoubleArray("correlationValues", param.correlationValues);

		final Param obtained = new Param();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, param);
	}

	@Test
	public void testToParam() {

		final HashMap<String, Double> correlations = new HashMap<>();
		for (int i = 0; i < param.correlationNames.length; i++) {
			correlations.put(param.correlationNames[i], param.correlationValues[i]);
		}

		final ParamXml paramXml = new ParamXml(param.name, param.origName, param.isStart, param.value, param.error, param.min, param.max, param.p, param.t, param.minGuess,
				param.maxGuess, param.category, param.unit, param.description, correlations);
		final Param obtained = Param.toParam(paramXml);

		TestUtils.compare(obtained, param);
	}

	@Test
	public void testToParamXml() {
		final ParamXml obtained = param.toParamXml();

		assertThat(obtained.name, equalTo(param.name));
		assertThat(obtained.origName, equalTo(param.origName));
		assertThat(obtained.isStartParam, equalTo(param.isStart));
		assertThat(obtained.value, equalTo(param.value));
		assertThat(obtained.error, equalTo(param.error));
		assertThat(obtained.P, equalTo(param.p));
		assertThat(obtained.t, equalTo(param.t));
		assertThat(obtained.minGuess, equalTo(param.minGuess));
		assertThat(obtained.maxGuess, equalTo(param.maxGuess));
		assertThat(obtained.category, equalTo(param.category));
		assertThat(obtained.description, equalTo(param.description));

		final HashMap<String, Double> correlations = new HashMap<>();
		for (int i = 0; i < param.correlationNames.length; i++) {
			correlations.put(param.correlationNames[i], param.correlationValues[i]);
		}
		assertThat(correlations, equalTo(obtained.correlations));
	}
}
