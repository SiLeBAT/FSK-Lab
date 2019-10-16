package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

@SuppressWarnings("static-method")
public class TimeSeriesTest {

	static TimeSeries timeSeries;
	static {
		timeSeries = new TimeSeries();
		timeSeries.name = "t0";
		timeSeries.time = 0.0;
		timeSeries.timeUnit = "h";
		timeSeries.origTimeUnit = "h";
		timeSeries.concentration = 6.147902198294102;
		timeSeries.concentrationUnit = "ln(count/g";
		timeSeries.concentrationUnitObjectType = "CFU";
		timeSeries.origConcentrationUnit = "log10(count/g)";
		timeSeries.concentrationStdDev = 0.91025171;
		timeSeries.numberOfMeasurements = 1;
	}

	@Test
	public void testConstructor() {
		final TimeSeries timeSeries = new TimeSeries();
		assertThat(timeSeries.name, is(nullValue()));
		assertThat(timeSeries.time, is(nullValue()));
		assertThat(timeSeries.timeUnit, is(nullValue()));
		assertThat(timeSeries.concentration, is(nullValue()));
		assertThat(timeSeries.concentrationUnit, is(nullValue()));
		assertThat(timeSeries.concentrationUnitObjectType, is(nullValue()));
		assertThat(timeSeries.origConcentrationUnit, is(nullValue()));
		assertThat(timeSeries.concentrationStdDev, is(nullValue()));
		assertThat(timeSeries.numberOfMeasurements, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		timeSeries.saveToNodeSettings(settings);

		assertThat(settings.getString("name"), equalTo(timeSeries.name));
		assertThat(settings.getDouble("time"), equalTo(timeSeries.time));
		assertThat(settings.getString("timeUnit"), equalTo(timeSeries.timeUnit));
		assertThat(settings.getString("origTimeUnit"), equalTo(timeSeries.origTimeUnit));
		assertThat(settings.getDouble("concentration"), equalTo(timeSeries.concentration));
		assertThat(settings.getString("concentrationUnit"), equalTo(timeSeries.concentrationUnit));
		assertThat(settings.getString("concentrationUnitObjectType"), equalTo(timeSeries.concentrationUnitObjectType));
		assertThat(settings.getString("origConcentrationUnit"), equalTo(timeSeries.origConcentrationUnit));
		assertThat(settings.getDouble("concentrationStdDev"), equalTo(timeSeries.concentrationStdDev));
		assertThat(settings.getInt("numberOfMeasurements"), equalTo(timeSeries.numberOfMeasurements));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", timeSeries.name);
		settings.addDouble("time", timeSeries.time);
		settings.addString("timeUnit", timeSeries.timeUnit);
		settings.addString("origTimeUnit", timeSeries.origTimeUnit);
		settings.addDouble("concentration", timeSeries.concentration);
		settings.addString("concentrationUnit", timeSeries.concentrationUnit);
		settings.addString("concentrationUnitObjectType", timeSeries.concentrationUnitObjectType);
		settings.addString("origConcentrationUnit", timeSeries.origConcentrationUnit);
		settings.addDouble("concentrationStdDev", timeSeries.concentrationStdDev);
		settings.addInt("numberOfMeasurements", timeSeries.numberOfMeasurements);

		final TimeSeries obtained = new TimeSeries();
		obtained.loadFromNodeSettings(settings);
		compare(obtained, timeSeries);
	}

	@Test
	public void testToTimeSeries() {
		final TimeSeriesXml timeSeriesXml = new TimeSeriesXml(timeSeries.name, timeSeries.time, timeSeries.timeUnit,
				timeSeries.origTimeUnit, timeSeries.concentration, timeSeries.concentrationUnit,
				timeSeries.concentrationUnitObjectType, timeSeries.origConcentrationUnit,
				timeSeries.concentrationStdDev, timeSeries.numberOfMeasurements);
		final TimeSeries obtained = TimeSeries.toTimeSeries(timeSeriesXml);
		compare(obtained, timeSeries);
	}

	@Test
	public void testToTimeSeriesXml() {
		final TimeSeriesXml obtained = timeSeries.toTimeSeriesXml();

		assertThat(obtained.name, equalTo(timeSeries.name));
		assertThat(obtained.time, equalTo(timeSeries.time));
		assertThat(obtained.timeUnit, equalTo(timeSeries.timeUnit));
		assertThat(obtained.origTimeUnit, equalTo(timeSeries.origTimeUnit));
		assertThat(obtained.concentration, equalTo(timeSeries.concentration));
		assertThat(obtained.concentrationUnit, equalTo(timeSeries.concentrationUnit));
		assertThat(obtained.concentrationUnitObjectType, equalTo(timeSeries.concentrationUnitObjectType));
		assertThat(obtained.origConcentrationUnit, equalTo(timeSeries.origConcentrationUnit));
		assertThat(obtained.concentrationStdDev, equalTo(timeSeries.concentrationStdDev));
		assertThat(obtained.numberOfMeasurements, equalTo(timeSeries.numberOfMeasurements));
	}

	private static void compare(TimeSeries obtained, TimeSeries expected) {
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.timeUnit, equalTo(expected.timeUnit));
		assertThat(obtained.origTimeUnit, equalTo(expected.origTimeUnit));
		assertThat(obtained.concentration, equalTo(expected.concentration));
		assertThat(obtained.concentrationUnit, equalTo(expected.concentrationUnit));
		assertThat(obtained.concentrationUnitObjectType, equalTo(expected.concentrationUnitObjectType));
		assertThat(obtained.origConcentrationUnit, equalTo(expected.origConcentrationUnit));
		assertThat(obtained.concentrationStdDev, equalTo(expected.concentrationStdDev));
		assertThat(obtained.numberOfMeasurements, equalTo(expected.numberOfMeasurements));
	}
}