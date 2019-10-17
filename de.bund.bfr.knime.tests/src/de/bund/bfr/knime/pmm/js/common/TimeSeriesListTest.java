package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class TimeSeriesListTest {

	static TimeSeries timeSeries = TimeSeriesTest.timeSeries;

	@Test
	public void testTimeSeries() {
		final TimeSeriesList list = new TimeSeriesList();
		assertNull(list.getTimeSeries());

		list.setTimeSeries(new TimeSeries[] { timeSeries });

		final TimeSeries expected = timeSeries; // expected TimeSeries
		final TimeSeries obtained = list.getTimeSeries()[0]; // obtained TimeSeries
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final TimeSeriesList list = new TimeSeriesList();
		list.setTimeSeries(new TimeSeries[] { timeSeries });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numTimeSeries"));

		final TimeSeries expected = timeSeries; // expected TimeSeries
		final TimeSeries obtained = new TimeSeries(); // obtained TimeSeries
		obtained.loadFromNodeSettings(settings.getNodeSettings("timeSeries0"));

		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numTimeSeries", 1);
		timeSeries.saveToNodeSettings(settings.addNodeSettings("timeSeries0"));

		final TimeSeriesList list = new TimeSeriesList();
		list.loadFromNodeSettings(settings);

		final TimeSeries expected = timeSeries; // expected TimeSeries
		final TimeSeries obtained = list.getTimeSeries()[0]; // obtained TimeSeries
		TestUtils.compare(obtained, expected);
	}
}