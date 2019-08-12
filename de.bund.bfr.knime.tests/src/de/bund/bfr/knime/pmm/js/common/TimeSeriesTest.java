package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

@SuppressWarnings("static-method")
public class TimeSeriesTest {

	static String name = "t0";
	static double time = 0.0;
	static String timeUnit = "h";
	static String origTimeUnit = "h";
	static double concentration = 6.147902198294102;
	static String concentrationUnit = "ln(count/g";
	static String concentrationUnitObjectType = "CFU";
	static String origConcentrationUnit = "log10(count/g)";
	static double concentrationStdDev = 0.91025171;
	static int numberOfMeasurements = 1;

	@Test
	public void testConstructor() {

		final TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.name);
		assertNull(timeSeries.time);
		assertNull(timeSeries.timeUnit);
		assertNull(timeSeries.origTimeUnit);
		assertNull(timeSeries.concentration);
		assertNull(timeSeries.concentrationUnit);
		assertNull(timeSeries.concentrationUnitObjectType);
		assertNull(timeSeries.origConcentrationUnit);
		assertNull(timeSeries.concentrationStdDev);
		assertNull(timeSeries.numberOfMeasurements);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final TimeSeries timeSeries = new TimeSeries();
		timeSeries.name = name;
		timeSeries.time = time;
		timeSeries.timeUnit = timeUnit;
		timeSeries.origTimeUnit = origTimeUnit;
		timeSeries.concentration = concentration;
		timeSeries.concentrationUnit = concentrationUnit;
		timeSeries.concentrationUnitObjectType = concentrationUnitObjectType;
		timeSeries.origConcentrationUnit = origConcentrationUnit;
		timeSeries.concentrationStdDev = concentrationStdDev;
		timeSeries.numberOfMeasurements = numberOfMeasurements;

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		timeSeries.saveToNodeSettings(settings);

		assertEquals(name, settings.getString("name"));
		assertEquals(time, settings.getDouble("time"), 0.0);
		assertEquals(timeUnit, settings.getString("timeUnit"));
		assertEquals(origTimeUnit, settings.getString("origTimeUnit"));
		assertEquals(concentration, settings.getDouble("concentration"), 0.0);
		assertEquals(concentrationUnit, settings.getString("concentrationUnit"));
		assertEquals(concentrationUnitObjectType, settings.getString("concentrationUnitObjectType"));
		assertEquals(origConcentrationUnit, settings.getString("origConcentrationUnit"));
		assertEquals(concentrationStdDev, settings.getDouble("concentrationStdDev"), 0.0);
		assertEquals(numberOfMeasurements, settings.getInt("numberOfMeasurements"));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", name);
		settings.addDouble("time", time);
		settings.addString("timeUnit", timeUnit);
		settings.addString("origTimeUnit", origTimeUnit);
		settings.addDouble("concentration", concentration);
		settings.addString("concentrationUnit", concentrationUnit);
		settings.addString("concentrationUnitObjectType", concentrationUnitObjectType);
		settings.addString("origConcentrationUnit", origConcentrationUnit);
		;
		settings.addDouble("concentrationStdDev", concentrationStdDev);
		settings.addInt("numberOfMeasurements", numberOfMeasurements);

		final TimeSeries timeSeries = new TimeSeries();
		timeSeries.loadFromNodeSettings(settings);

		assertEquals(name, timeSeries.name);
		assertEquals(time, timeSeries.time, 0.0);
		assertEquals(timeUnit, timeSeries.timeUnit);
		assertEquals(origTimeUnit, timeSeries.origTimeUnit);
		assertEquals(concentration, timeSeries.concentration, 0.0);
		assertEquals(concentrationUnit, timeSeries.concentrationUnit);
		assertEquals(concentrationUnitObjectType, timeSeries.concentrationUnitObjectType);
		assertEquals(origConcentrationUnit, timeSeries.origConcentrationUnit);
		assertEquals(concentrationStdDev, timeSeries.concentrationStdDev, 0.0);
		assertEquals(numberOfMeasurements, timeSeries.numberOfMeasurements.intValue());
	}

	@Test
	public void testToTimeSeries() {
		final TimeSeriesXml timeSeriesXml = new TimeSeriesXml(name, time, timeUnit, origTimeUnit, concentration,
				concentrationUnit, concentrationUnitObjectType, origConcentrationUnit, concentrationStdDev,
				numberOfMeasurements);
		final TimeSeries timeSeries = TimeSeries.toTimeSeries(timeSeriesXml);

		assertEquals(name, timeSeries.name);
		assertEquals(time, timeSeries.time, 0.0);
		assertEquals(timeUnit, timeSeries.timeUnit);
		assertEquals(origTimeUnit, timeSeries.origTimeUnit);
		assertEquals(concentration, timeSeries.concentration, 0.0);
		assertEquals(concentrationUnit, timeSeries.concentrationUnit);
		assertEquals(concentrationUnitObjectType, timeSeries.concentrationUnitObjectType);
		assertEquals(origConcentrationUnit, timeSeries.origConcentrationUnit);
		assertEquals(concentrationStdDev, timeSeries.concentrationStdDev, 0.0);
		assertEquals(numberOfMeasurements, timeSeries.numberOfMeasurements.intValue());
	}

	@Test
	public void testToTimeSeriesXml() {
		final TimeSeries timeSeries = new TimeSeries();
		timeSeries.name = name;
		timeSeries.time = time;
		timeSeries.timeUnit = timeUnit;
		timeSeries.origTimeUnit = origTimeUnit;
		timeSeries.concentration = concentration;
		timeSeries.concentrationUnit = concentrationUnit;
		timeSeries.concentrationUnitObjectType = concentrationUnitObjectType;
		timeSeries.origConcentrationUnit = origConcentrationUnit;
		timeSeries.concentrationStdDev = concentrationStdDev;
		timeSeries.numberOfMeasurements = numberOfMeasurements;
		final TimeSeriesXml timeSeriesXml = timeSeries.toTimeSeriesXml();

		assertEquals(name, timeSeriesXml.name);
		assertEquals(time, timeSeriesXml.time, 0.0);
		assertEquals(timeUnit, timeSeriesXml.timeUnit);
		assertEquals(origTimeUnit, timeSeriesXml.origTimeUnit);
		assertEquals(concentration, timeSeriesXml.concentration, 0.0);
		assertEquals(concentrationUnit, timeSeriesXml.concentrationUnit);
		assertEquals(concentrationUnitObjectType, timeSeriesXml.concentrationUnitObjectType);
		assertEquals(origConcentrationUnit, timeSeriesXml.origConcentrationUnit);
		assertEquals(concentrationStdDev, timeSeriesXml.concentrationStdDev, 0.0);
		assertEquals(numberOfMeasurements, timeSeriesXml.numberOfMeasurements.intValue());
	}
}