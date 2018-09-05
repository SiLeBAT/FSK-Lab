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
		timeSeries.setName(TimeSeriesTest.name);
		timeSeries.setTimeUnit(TimeSeriesTest.timeUnit);
		timeSeries.setOrigTimeUnit(TimeSeriesTest.origTimeUnit);
		timeSeries.setConcentration(TimeSeriesTest.concentration);
		timeSeries.setConcentrationUnit(TimeSeriesTest.concentrationUnit);
		timeSeries.setConcentrationUnitObjectType(TimeSeriesTest.concentrationUnitObjectType);
		timeSeries.setOrigConcentrationUnit(TimeSeriesTest.origConcentrationUnit);
		timeSeries.setConcentrationStdDev(TimeSeriesTest.concentrationStdDev);
		timeSeries.setNumberOfMeasurements(TimeSeriesTest.numberOfMeasurements);
	}

	@Test
	public void testTimeSeries() {
		TimeSeriesList  list = new TimeSeriesList();
		assertNull(list.getTimeSeries());

		list.setTimeSeries(new TimeSeries[] { timeSeries });

		TimeSeries expected = timeSeries;  // expected TimeSeries
		TimeSeries obtained = list.getTimeSeries()[0];  // obtained TimeSeries

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getTimeUnit(), obtained.getTimeUnit());
		assertEquals(expected.getOrigTimeUnit(), obtained.getOrigTimeUnit());
		assertEquals(expected.getConcentration(), obtained.getConcentration(), 0.0);
		assertEquals(expected.getConcentrationUnit(), obtained.getConcentrationUnit());
		assertEquals(expected.getConcentrationUnitObjectType(), obtained.getConcentrationUnitObjectType());
		assertEquals(expected.getOrigConcentrationUnit(), obtained.getOrigConcentrationUnit());
		assertEquals(expected.getConcentrationStdDev(), obtained.getConcentrationStdDev(), 0.0);
		assertEquals(expected.getNumberOfMeasurements(), obtained.getNumberOfMeasurements());
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		TimeSeriesList list = new TimeSeriesList();
		list.setTimeSeries(new TimeSeries[] { timeSeries });

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt(TimeSeriesList.NUM_TIMESERIES));

		TimeSeries expected = timeSeries;  // expected TimeSeries
		TimeSeries obtained = new TimeSeries();  // obtained TimeSeries
		obtained.loadFromNodeSettings(settings.getNodeSettings(TimeSeriesList.TIMESERIES + 0));

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getTimeUnit(), obtained.getTimeUnit());
		assertEquals(expected.getOrigTimeUnit(), obtained.getOrigTimeUnit());
		assertEquals(expected.getConcentration(), obtained.getConcentration(), 0.0);
		assertEquals(expected.getConcentrationUnit(), obtained.getConcentrationUnit());
		assertEquals(expected.getConcentrationUnitObjectType(), obtained.getConcentrationUnitObjectType());
		assertEquals(expected.getOrigConcentrationUnit(), obtained.getOrigConcentrationUnit());
		assertEquals(expected.getConcentrationStdDev(), obtained.getConcentrationStdDev(), 0.0);
		assertEquals(expected.getNumberOfMeasurements(), obtained.getNumberOfMeasurements());
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(TimeSeriesList.NUM_TIMESERIES, 1);
		timeSeries.saveToNodeSettings(settings.addNodeSettings(TimeSeriesList.TIMESERIES + 0));

		TimeSeriesList list = new TimeSeriesList();
		list.loadFromNodeSettings(settings);

		TimeSeries expected = timeSeries;  // expected TimeSeries
		TimeSeries obtained = list.getTimeSeries()[0];  // obtained TimeSeries

		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getTimeUnit(), obtained.getTimeUnit());
		assertEquals(expected.getOrigTimeUnit(), obtained.getOrigTimeUnit());
		assertEquals(expected.getConcentration(), obtained.getConcentration(), 0.0);
		assertEquals(expected.getConcentrationUnit(), obtained.getConcentrationUnit());
		assertEquals(expected.getConcentrationUnitObjectType(), obtained.getConcentrationUnitObjectType());
		assertEquals(expected.getOrigConcentrationUnit(), obtained.getOrigConcentrationUnit());
		assertEquals(expected.getConcentrationStdDev(), obtained.getConcentrationStdDev(), 0.0);
		assertEquals(expected.getNumberOfMeasurements(), obtained.getNumberOfMeasurements());
	}
}