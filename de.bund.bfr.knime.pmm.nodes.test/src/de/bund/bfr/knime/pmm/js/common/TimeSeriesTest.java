package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

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
	public void testName() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getName());

		timeSeries.setName(name);
		assertEquals(name, timeSeries.getName());
	}

	@Test
	public void testTime() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getTime());

		timeSeries.setTime(time);
		assertEquals(time, timeSeries.getTime(), 0.0);
	}

	@Test
	public void testTimeUnit() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getTimeUnit());

		timeSeries.setTimeUnit(timeUnit);
		assertEquals(timeUnit, timeSeries.getTimeUnit());
	}

	@Test
	public void testOrigTimeUnit() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getOrigTimeUnit());

		timeSeries.setOrigTimeUnit(origTimeUnit);
		assertEquals(origTimeUnit, timeSeries.getOrigTimeUnit());
	}

	@Test
	public void testConcentration() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getConcentration());

		timeSeries.setConcentration(concentration);
		assertEquals(concentration, timeSeries.getConcentration(), 0.0);
	}

	@Test
	public void testConcentrationUnit() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getConcentrationUnit());

		timeSeries.setConcentrationUnit(concentrationUnit);
		assertEquals(concentrationUnit, timeSeries.getConcentrationUnit());
	}

	@Test
	public void testConcentrationUnitObjectType() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getConcentrationUnitObjectType());

		timeSeries.setConcentrationUnitObjectType(concentrationUnitObjectType);
		assertEquals(concentrationUnitObjectType, timeSeries.getConcentrationUnitObjectType());
	}

	@Test
	public void testOrigConcentrationUnit() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getOrigConcentrationUnit());

		timeSeries.setOrigConcentrationUnit(origConcentrationUnit);
		assertEquals(origConcentrationUnit, timeSeries.getOrigConcentrationUnit());
	}

	@Test
	public void testConcentrationStdDev() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getConcentrationStdDev());

		timeSeries.setConcentrationStdDev(concentrationStdDev);
		assertEquals(concentrationStdDev, timeSeries.getConcentrationStdDev(), 0.0);
	}

	@Test
	public void testNumberOfMeasurements() {
		TimeSeries timeSeries = new TimeSeries();
		assertNull(timeSeries.getNumberOfMeasurements());

		timeSeries.setNumberOfMeasurements(numberOfMeasurements);
		assertEquals(numberOfMeasurements, timeSeries.getNumberOfMeasurements(), 0.0);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.setName(name);
		timeSeries.setTime(time);
		timeSeries.setTimeUnit(timeUnit);
		timeSeries.setOrigTimeUnit(origTimeUnit);
		timeSeries.setConcentration(concentration);
		timeSeries.setConcentrationUnit(concentrationUnit);
		timeSeries.setConcentrationUnitObjectType(concentrationUnitObjectType);
		timeSeries.setOrigConcentrationUnit(origConcentrationUnit);
		timeSeries.setConcentrationStdDev(concentrationStdDev);
		timeSeries.setNumberOfMeasurements(numberOfMeasurements);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		timeSeries.saveToNodeSettings(settings);

		assertEquals(name, settings.getString(TimeSeries.NAME));
		assertEquals(time, settings.getDouble(TimeSeries.TIME), 0.0);
		assertEquals(timeUnit, settings.getString(TimeSeries.TIME_UNIT));
		assertEquals(origTimeUnit, settings.getString(TimeSeries.ORIG_TIME_UNIT));
		assertEquals(concentration, settings.getDouble(TimeSeries.CONCENTRATION), 0.0);
		assertEquals(concentrationUnit, settings.getString(TimeSeries.CONCENTRATION_UNIT));
		assertEquals(concentrationUnitObjectType, settings.getString(TimeSeries.CONCENTRATION_UNIT_OBJECT_TYPE));
		assertEquals(origConcentrationUnit, settings.getString(TimeSeries.ORIG_CONCENTRATION_UNIT));
		assertEquals(concentrationStdDev, settings.getDouble(TimeSeries.CONCENTRATION_STDDEV), 0.0);
		assertEquals(numberOfMeasurements, settings.getInt(TimeSeries.NUMBER_OF_MEASUREMENTS));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString(TimeSeries.NAME, name);
		settings.addDouble(TimeSeries.TIME, time);
		settings.addString(TimeSeries.TIME_UNIT, timeUnit);
		settings.addString(TimeSeries.ORIG_TIME_UNIT, origTimeUnit);
		settings.addDouble(TimeSeries.CONCENTRATION, concentration);
		settings.addString(TimeSeries.CONCENTRATION_UNIT, concentrationUnit);
		settings.addString(TimeSeries.CONCENTRATION_UNIT_OBJECT_TYPE, concentrationUnitObjectType);
		settings.addString(TimeSeries.ORIG_CONCENTRATION_UNIT, origConcentrationUnit);
		;
		settings.addDouble(TimeSeries.CONCENTRATION_STDDEV, concentrationStdDev);
		settings.addInt(TimeSeries.NUMBER_OF_MEASUREMENTS, numberOfMeasurements);

		TimeSeries timeSeries = new TimeSeries();
		timeSeries.loadFromNodeSettings(settings);

		assertEquals(name, timeSeries.getName());
		assertEquals(time, timeSeries.getTime(), 0.0);
		assertEquals(timeUnit, timeSeries.getTimeUnit());
		assertEquals(origTimeUnit, timeSeries.getOrigTimeUnit());
		assertEquals(concentration, timeSeries.getConcentration(), 0.0);
		assertEquals(concentrationUnit, timeSeries.getConcentrationUnit());
		assertEquals(concentrationUnitObjectType, timeSeries.getConcentrationUnitObjectType());
		assertEquals(origConcentrationUnit, timeSeries.getOrigConcentrationUnit());
		assertEquals(concentrationStdDev, timeSeries.getConcentrationStdDev(), 0.0);
		assertEquals(numberOfMeasurements, timeSeries.getNumberOfMeasurements().intValue());
	}

	@Test
	public void testToTimeSeries() {
		TimeSeriesXml timeSeriesXml = new TimeSeriesXml(name, time, timeUnit, origTimeUnit, concentration,
				concentrationUnit, concentrationUnitObjectType, origConcentrationUnit, concentrationStdDev,
				numberOfMeasurements);
		TimeSeries timeSeries = TimeSeries.toTimeSeries(timeSeriesXml);
		
		assertEquals(name, timeSeries.getName());
		assertEquals(time, timeSeries.getTime(), 0.0);
		assertEquals(timeUnit, timeSeries.getTimeUnit());
		assertEquals(origTimeUnit, timeSeries.getOrigTimeUnit());
		assertEquals(concentration, timeSeries.getConcentration(), 0.0);
		assertEquals(concentrationUnit, timeSeries.getConcentrationUnit());
		assertEquals(concentrationUnitObjectType, timeSeries.getConcentrationUnitObjectType());
		assertEquals(origConcentrationUnit, timeSeries.getOrigConcentrationUnit());
		assertEquals(concentrationStdDev, timeSeries.getConcentrationStdDev(), 0.0);
		assertEquals(numberOfMeasurements, timeSeries.getNumberOfMeasurements().intValue());
	}
	
	@Test
	public void testToTimeSeriesXml() {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.setName(name);
		timeSeries.setTime(time);
		timeSeries.setTimeUnit(timeUnit);
		timeSeries.setOrigTimeUnit(origTimeUnit);
		timeSeries.setConcentration(concentration);
		timeSeries.setConcentrationUnit(concentrationUnit);
		timeSeries.setConcentrationUnitObjectType(concentrationUnitObjectType);
		timeSeries.setOrigConcentrationUnit(origConcentrationUnit);
		timeSeries.setConcentrationStdDev(concentrationStdDev);
		timeSeries.setNumberOfMeasurements(numberOfMeasurements);
		TimeSeriesXml timeSeriesXml = timeSeries.toTimeSeriesXml();

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