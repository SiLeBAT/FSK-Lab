package de.bund.bfr.knime.foodprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.bund.bfr.knime.pmm.predictorview.SettingsHelper;

public class PredictionValuesTest {

	@Test
	public void testConstructor() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		assertNotNull(predictionValues.getSet());
		assertEquals("log10N0", predictionValues.getLog10N0());
		assertEquals("lag", predictionValues.getLag());
		assertEquals("log10(count/g)", predictionValues.getUnitLog10N0());
		assertEquals("h", predictionValues.getUnitTime());
		assertFalse(predictionValues.getUnitTemp().isEmpty());
		assertEquals("log10(count/g)", predictionValues.getUnitLog10N());
		assertEquals("bar", predictionValues.getUnitPres());
		assertNull(predictionValues.getPvnd());
		assertNull(predictionValues.getChartPanel());
	}
	
	@Test
	public void testLog10N0() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setLog10N0("a string");
		assertEquals("a string", predictionValues.getLog10N0());
	}
	
	@Test
	public void testLag() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setLag("string");
		assertEquals("string", predictionValues.getLag());
	}

	@Test
	public void testUnitLog10N0() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setUnitLog10N0("string");
		assertEquals("string", predictionValues.getUnitLog10N0());
	}

	@Test
	public void testUnitTime() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setUnitTime("string");
		assertEquals("string", predictionValues.getUnitTime());
	}

	@Test
	public void testUnitTemp() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setUnitTemp("string");
		assertEquals("string", predictionValues.getUnitTemp());
	}

	@Test
	public void testUnitLog10N() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setUnitLog10N("string");
		assertEquals("string", predictionValues.getUnitLog10N());
	}

	@Test
	public void testUnitPres() {
		PredictionValues predictionValues = new PredictionValues(new SettingsHelper());
		predictionValues.setUnitPres("string");
		assertEquals("string", predictionValues.getUnitPres());
	}
}
