package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;


public class TimeSeriesListTest {

	static TimeSeries timeSeries;

	static {
		timeSeries = new TimeSeries();
		timeSeries.name = TimeSeriesTest.name;
		timeSeries.timeUnit = TimeSeriesTest.timeUnit;
		timeSeries.origTimeUnit = TimeSeriesTest.origTimeUnit;
		timeSeries.concentration = TimeSeriesTest.concentration;
		timeSeries.concentrationUnit = TimeSeriesTest.concentrationUnit;
		timeSeries.concentrationUnitObjectType = TimeSeriesTest.concentrationUnitObjectType;
		timeSeries.origConcentrationUnit = TimeSeriesTest.origConcentrationUnit;
		timeSeries.concentrationStdDev = TimeSeriesTest.concentrationStdDev;
		timeSeries.numberOfMeasurements = TimeSeriesTest.numberOfMeasurements;
	}

	@Test
	public void testTimeSeries() {
		TimeSeriesList  list = new TimeSeriesList();
		assertNull(list.getTimeSeries());

		list.setTimeSeries(new TimeSeries[] { timeSeries });

		TimeSeries expected = timeSeries;  // expected TimeSeries
		TimeSeries obtained = list.getTimeSeries()[0];  // obtained TimeSeries

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.timeUnit, obtained.timeUnit);
		assertEquals(expected.origTimeUnit, obtained.origTimeUnit);
		assertEquals(expected.concentration, obtained.concentration, 0.0);
		assertEquals(expected.concentrationUnit, obtained.concentrationUnit);
		assertEquals(expected.concentrationUnitObjectType, obtained.concentrationUnitObjectType);
		assertEquals(expected.origConcentrationUnit, obtained.origConcentrationUnit);
		assertEquals(expected.concentrationStdDev, obtained.concentrationStdDev, 0.0);
		assertEquals(expected.numberOfMeasurements, obtained.numberOfMeasurements);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		TimeSeriesList list = new TimeSeriesList();
		list.setTimeSeries(new TimeSeries[] { timeSeries });

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numTimeSeries"));

		TimeSeries expected = timeSeries;  // expected TimeSeries
		TimeSeries obtained = new TimeSeries();  // obtained TimeSeries
		obtained.loadFromNodeSettings(settings.getNodeSettings("timeSeries0"));

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.timeUnit, obtained.timeUnit);
		assertEquals(expected.origTimeUnit, obtained.origTimeUnit);
		assertEquals(expected.concentration, obtained.concentration, 0.0);
		assertEquals(expected.concentrationUnit, obtained.concentrationUnit);
		assertEquals(expected.concentrationUnitObjectType, obtained.concentrationUnitObjectType);
		assertEquals(expected.origConcentrationUnit, obtained.origConcentrationUnit);
		assertEquals(expected.concentrationStdDev, obtained.concentrationStdDev, 0.0);
		assertEquals(expected.numberOfMeasurements, obtained.numberOfMeasurements);
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numTimeSeries", 1);
		timeSeries.saveToNodeSettings(settings.addNodeSettings("timeSeries0"));

		TimeSeriesList list = new TimeSeriesList();
		list.loadFromNodeSettings(settings);

		TimeSeries expected = timeSeries;  // expected TimeSeries
		TimeSeries obtained = list.getTimeSeries()[0];  // obtained TimeSeries

		assertEquals(expected.name, obtained.name);
		assertEquals(expected.timeUnit, obtained.timeUnit);
		assertEquals(expected.origTimeUnit, obtained.origTimeUnit);
		assertEquals(expected.concentration, obtained.concentration, 0.0);
		assertEquals(expected.concentrationUnit, obtained.concentrationUnit);
		assertEquals(expected.concentrationUnitObjectType, obtained.concentrationUnitObjectType);
		assertEquals(expected.origConcentrationUnit, obtained.origConcentrationUnit);
		assertEquals(expected.concentrationStdDev, obtained.concentrationStdDev, 0.0);
		assertEquals(expected.numberOfMeasurements, obtained.numberOfMeasurements);
	}
}