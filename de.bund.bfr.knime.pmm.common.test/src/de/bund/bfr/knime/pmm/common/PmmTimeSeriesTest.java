package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class PmmTimeSeriesTest {

	@Test
	public void testConstructors() {

		PmmTimeSeries series0 = new PmmTimeSeries();
		assertTrue(series0.getCondId() < 0);
		assertTrue(series0.getMatrixId() < 0);
		assertTrue(series0.getAgentId() < 0);
		assertNotNull(series0.getMdInfo());

		PmmTimeSeries series1 = new PmmTimeSeries(1);
		assertEquals(1, series1.getCondId().intValue());
		assertTrue(series1.getMatrixId() < 0);
		assertTrue(series1.getAgentId() < 0);
		assertNotNull(series1.getMdInfo());

		PmmTimeSeries series2 = new PmmTimeSeries("id");
		assertTrue(series2.getCondId() < 0);
		assertTrue(series2.getMatrixId() < 0);
		assertTrue(series2.getAgentId() < 0);
		assertNotNull(series2.getMdInfo());
		assertEquals("id", series2.getCombaseId());

		PmmTimeSeries series3 = new PmmTimeSeries(1, "id");
		assertEquals(1, series3.getCondId().intValue());
		assertTrue(series3.getMatrixId() < 0);
		assertTrue(series3.getAgentId() < 0);
		assertNotNull(series3.getMdInfo());
		assertEquals("id", series3.getCombaseId());
	}

	@Test
	public void testAdd1() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.add("name", 0.0, "timeUnit", 0.0, "concentrationUnit");

		TimeSeriesXml ts = (TimeSeriesXml) series.getTimeSeries().get(0);
		assertEquals("name", ts.name);
		assertEquals(0.0, ts.time, .0);
		assertEquals("timeUnit", ts.timeUnit);
		assertEquals(0.0, ts.concentration, .0);
		assertEquals("concentrationUnit", ts.concentrationUnit);
		assertNull(ts.concentrationStdDev);
		assertNull(ts.numberOfMeasurements);
	}

	@Test
	public void testAdd2() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.add(0.0, "timeUnit", 0.0, "concentrationUnit");

		TimeSeriesXml ts = (TimeSeriesXml) series.getTimeSeries().get(0);
		assertTrue(ts.name.startsWith("t"));
		assertEquals(0.0, ts.time, .0);
		assertEquals("timeUnit", ts.timeUnit);
		assertEquals(0.0, ts.concentration, .0);
		assertEquals("concentrationUnit", ts.concentrationUnit);
		assertNull(ts.concentrationStdDev);
		assertNull(ts.numberOfMeasurements);
	}

	@Test
	public void testAdd3() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.add("0.0", "timeUnit", "0.0", "concentrationUnit");

		TimeSeriesXml ts = (TimeSeriesXml) series.getTimeSeries().get(0);
		assertTrue(ts.name.startsWith("t"));
		assertEquals(0.0, ts.time, .0);
		assertEquals("timeUnit", ts.timeUnit);
		assertEquals(0.0, ts.concentration, .0);
		assertEquals("concentrationUnit", ts.concentrationUnit);
		assertNull(ts.concentrationStdDev);
		assertNull(ts.numberOfMeasurements);

		// When non numbers are passed, the exception is caught and
		// no time series is added.
		series.add("Not a number", "timeUnit", "Not a number", "concentrationUnit");
		// It should only have the time series added before
		assertEquals(1, series.getTimeSeries().size());
	}

	@Test
	public void testSetLiterature() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setLiterature(null);
		assertTrue(series.getCell("MD_Literatur").isMissing());
	}

	@Test
	public void testMisc() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setMisc(null);
		assertTrue(series.getCell("Misc").isMissing());
	}

	@Test
	public void testIsEmpty() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(series.isEmpty());

		series.add(0.0, "timeUnit", 1.0, "concentrationUnit");
		assertFalse(series.isEmpty());
	}

	@Test
	public void testGetDbuuid() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertNull(series.getDbuuid());
	}

	@Test
	public void testGetCondId() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(series.getCondId() < 0);
	}

	@Test
	public void testGetAgentName() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setAgentName("agent");
		assertEquals("agent", series.getAgentName());
	}

	@Test
	public void testGetMatrixName() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setMatrixName("matrix");
		assertEquals("matrix", series.getMatrixName());
	}

	@Test
	public void testGetTemperature() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(Double.isNaN(series.getTemperature()));

		series.addMisc(0, "Temperature", "description", 0.0, new ArrayList<>(), "unit");
		assertEquals(0.0, series.getTemperature(), 0.0);
	}

	@Test
	public void testGetTemperatureUnit() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals("°C", series.getTemperatureUnit());

		series.addMisc(0, "Temperature", "description", 0.0, new ArrayList<>(), "unit");
		assertEquals("unit", series.getTemperatureUnit());
	}

	@Test
	public void testGetPh() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(Double.isNaN(series.getPh()));

		series.addMisc(0, "pH", "description", 0.0, new ArrayList<>(), "unit");
		assertEquals(0.0, series.getPh(), 0.0);
	}

	@Test
	public void testGetWaterActivity() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(Double.isNaN(series.getWaterActivity()));

		series.addMisc(0, "aw", "description", 0.0, new ArrayList<>(), "unit");
		assertEquals(0.0, series.getWaterActivity(), 0.0);
	}

	@Test
	public void testGetMisc() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals(0, series.getMisc().size());
	}

	@Test
	public void testGetMatrix() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals(1, series.getMatrix().size());
	}

	@Test
	public void testGetAgent() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals(1, series.getAgent().size());
	}

	@Test
	public void testGetLiterature() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals(0, series.getLiterature().size());
	}

	@Test
	public void testGetMdInfo() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals(1, series.getMdInfo().size());
	}

	@Test
	public void testGetTimeSeries() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertEquals(0, series.getTimeSeries().size());
	}

	@Test
	public void testGetAgentId() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(series.getAgentId() < 0);
	}

	@Test
	public void testGetMatrixId() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertTrue(series.getMatrixId() < 0);
	}

	@Test
	public void testGetAgentDetail() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertNull(series.getAgentDetail());
	}

	@Test
	public void testGetMatrixDetail() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertNull(series.getMatrixDetail());
	}

	@Test
	public void testGetComment() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setMdInfo(new PmmXmlDoc(new MdInfoXml(0, "name", "comment", 0, false)));
		assertEquals("comment", series.getComment());

		// Set PmmXmlDoc without model information
		series.setMdInfo(new PmmXmlDoc());
		assertNull(series.getComment());
	}

	@Test
	public void testGetQualityScore() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setMdInfo(new PmmXmlDoc(new MdInfoXml(0, "name", "comment", 0, false)));
		assertEquals(0, series.getQualityScore().intValue());

		// Set PmmXmlDoc without model information
		series.setMdInfo(new PmmXmlDoc());
		assertNull(series.getQualityScore());
	}

	@Test
	public void testGetChecked() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setMdInfo(new PmmXmlDoc(new MdInfoXml(0, "name", "comment", 0, false)));
		assertFalse(series.getChecked());

		// Set PmmXmlDoc without model information
		series.setMdInfo(new PmmXmlDoc());
		assertNull(series.getChecked());
	}
	
	@Test
	public void testGetWarning() {
		assertNull(new PmmTimeSeries().getWarning());
	}
	
	@Test
	public void testSetWarning() {
		PmmTimeSeries series = new PmmTimeSeries();
		series.setWarning("Danger zone");
		assertEquals("Danger zone", series.getWarning());
	}
	
	@Test
	public void testHasCombaseId() {
		PmmTimeSeries series = new PmmTimeSeries();
		assertFalse(series.hasCombaseId());
		
		series.setCombaseId("id");
		assertTrue(series.hasCombaseId());
	}
}
