package de.bund.bfr.knime.pmm.predictorview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class SettingsHelperTest {
	
	private static KnimeTuple exampleTuple;
	
	@BeforeClass
	public static void setUp() {
		KnimeSchema schema = new KnimeSchema();
		schema.addIntAttribute("Entero");
		schema.addDoubleAttribute("Real");
		schema.addStringAttribute("Cadena");
		schema.addXmlAttribute("XML");	
		
		exampleTuple = new KnimeTuple(schema);
	}

	@Test
	public void testEmptyConstructor() {
		
		SettingsHelper helper = new SettingsHelper();
		assertTrue(helper.getSelectedIDs().isEmpty());
		assertTrue(helper.getParamXValues().isEmpty());
		assertTrue(helper.getTimeValues().isEmpty());
		assertTrue(helper.getColors().isEmpty());
		assertTrue(helper.getShapes().isEmpty());
		assertFalse(helper.isManualRange());
		assertEquals(0.0, helper.getMinX(), .0);
		assertEquals(100.0, helper.getMaxX(), .0);
		assertFalse(helper.isDrawLines());
		assertTrue(helper.isShowLegend());
		assertFalse(helper.isAddLegendInfo());
		assertTrue(helper.isDisplayHighlighted());
		assertFalse(helper.isExportAsSvg());
		assertNull(helper.getUnitX());
		assertNull(helper.getUnitY());
		assertTrue(helper.getTransformX().isEmpty());
		assertTrue(helper.getTransformY().isEmpty());
		assertTrue(helper.isStandardVisibleColumns());
		assertTrue(helper.getVisibleColumns().isEmpty());
		assertNull(helper.getFittedFilter());
		assertTrue(helper.getConcentrationParameters().isEmpty());
		assertTrue(helper.getLagParameters().isEmpty());
		assertTrue(helper.getSelectedTuples().isEmpty());
		assertTrue(helper.getSelectedOldTuples().isEmpty());
		assertTrue(helper.getConcentrationParameters().isEmpty());
		assertTrue(helper.getLagParameters().isEmpty());
		assertTrue(helper.getColumnWidths().isEmpty());
		assertFalse(helper.isSampleInverse());
	}
	
	@Test
	public void testSelectedIDs() {
		final List<String> selectedIds = Arrays.asList("1", "2", "3");

		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedIDs(selectedIds);
		assertEquals(selectedIds, helper.getSelectedIDs());
	}

	@Test
	public void testParamXValues() {
		final Map<String, Double> xValues = new HashMap<>();
		xValues.put("time", 15.0);
		
		SettingsHelper helper = new SettingsHelper();
		helper.setParamXValues(xValues);
		assertEquals(xValues, helper.getParamXValues());
	}

	@Test
	public void testTimeValues() {
		final List<Double> timeValues = Arrays.asList(15.0);
		
		SettingsHelper helper = new SettingsHelper();
		helper.setTimeValues(timeValues);
		assertEquals(timeValues, helper.getTimeValues());
	}

	@Test
	public void testColors() {
		final Map<String, Color> colorMap = new HashMap<>();
		colorMap.put("red", Color.red);
		
		SettingsHelper helper = new SettingsHelper();
		helper.setColors(colorMap);
		assertEquals(colorMap, helper.getColors());
	}
	
	@Test
	public void testShapes() {
		final Map<String, Shape> shapeMap = new HashMap<>();
		shapeMap.put("rectangle", new Rectangle());
		
		SettingsHelper helper = new SettingsHelper();
		helper.setShapes(shapeMap);
		assertEquals(shapeMap, helper.getShapes());
	}
	
	@Test
	public void testName() {
		SettingsHelper helper = new SettingsHelper();
		helper.setManualRange(true);
		assertTrue(helper.isManualRange());
	}

	@Test
	public void testMinX() {
		SettingsHelper helper = new SettingsHelper();
		helper.setMinX(1.0);
		assertEquals(1.0, helper.getMinX(), .0);
	}

	@Test
	public void testMaxX() {
		SettingsHelper helper = new SettingsHelper();
		helper.setMaxX(1.0);
		assertEquals(1.0, helper.getMaxX(), .0);
	}

	@Test
	public void testMinY() {
		SettingsHelper helper = new SettingsHelper();
		helper.setMinY(1.0);
		assertEquals(1.0, helper.getMinY(), .0);
	}

	@Test
	public void testMaxY() {
		SettingsHelper helper = new SettingsHelper();
		helper.setMaxY(1.0);
		assertEquals(1.0, helper.getMaxY(), .0);
	}
	
	@Test
	public void testDrawLines() {
		SettingsHelper helper = new SettingsHelper();
		helper.setDrawLines(true);
		assertTrue(helper.isDrawLines());
	}
	
	@Test
	public void testShowLegend() {
		SettingsHelper helper = new SettingsHelper();
		helper.setShowLegend(false);
		assertFalse(helper.isShowLegend());
	}
	
	@Test
	public void testAddLegendInfo() {
		SettingsHelper helper = new SettingsHelper();
		helper.setAddLegendInfo(true);
		assertTrue(helper.isAddLegendInfo());
	}
	
	@Test
	public void testDisplayHighlighted() {
		SettingsHelper helper = new SettingsHelper();
		helper.setDisplayHighlighted(false);
		assertFalse(helper.isDisplayHighlighted());
	}
	
	@Test
	public void testExportAsSvg() {
		SettingsHelper helper = new SettingsHelper();
		helper.setExportAsSvg(true);
		assertTrue(helper.isExportAsSvg());
	}

	@Test
	public void testUnitX() {
		SettingsHelper helper = new SettingsHelper();
		helper.setUnitX("unitX");
		assertEquals("unitX", helper.getUnitX());
	}

	@Test
	public void testUnitY() {
		SettingsHelper helper = new SettingsHelper();
		helper.setUnitY("unitY");
		assertEquals("unitY", helper.getUnitY());
	}

	@Test
	public void testTransformX() {
		SettingsHelper helper = new SettingsHelper();
		helper.setTransformX("transformX");
		assertEquals("transformX", helper.getTransformX());
	}
	
	@Test
	public void testTransformY() {
		SettingsHelper helper = new SettingsHelper();
		helper.setTransformY("transformY");
		assertEquals("transformY", helper.getTransformY());
	}

	@Test
	public void testStandardVisibleColumns() {
		SettingsHelper helper = new SettingsHelper();
		helper.setStandardVisibleColumns(false);
		assertFalse(helper.isStandardVisibleColumns());
	}

	@Test
	public void testVisibleColumns() {
		final List<String> visibleColumns = Arrays.asList("pH", "wa");
		
		SettingsHelper helper = new SettingsHelper();
		helper.setVisibleColumns(visibleColumns);
		assertEquals(visibleColumns, helper.getVisibleColumns());
	}

	@Test
	public void testFittedFilter() {
		SettingsHelper helper = new SettingsHelper();
		helper.setFittedFilter("fitted filter");
		assertEquals("fitted filter", helper.getFittedFilter());
	}

	@Test
	public void testConcentrationParameters() {
		final Map<String, String> concentrationParameters = new HashMap<>();
		concentrationParameters.put("concentration", "parameter");
		
		SettingsHelper helper = new SettingsHelper();
		helper.setConcentrationParameters(concentrationParameters);
		assertEquals(concentrationParameters, helper.getConcentrationParameters());
	}

	@Test
	public void testLagParameters() {
		final Map<String, String> lagParameter = new HashMap<>();
		lagParameter.put("lag", "parameter");
		
		SettingsHelper helper = new SettingsHelper();
		helper.setLagParameters(lagParameter);
		assertEquals(lagParameter, helper.getLagParameters());
	}

	@Test
	public void testSelectedTuples() {
		final List<KnimeTuple> selectedTuple = Arrays.asList(exampleTuple);
		
		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedTuples(selectedTuple);
		assertEquals(selectedTuple, helper.getSelectedTuples());
	}

	@Test
	public void testSelectedOldTuples() {
		final List<KnimeTuple> selectedTuple = Arrays.asList(exampleTuple);
		
		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedOldTuples(selectedTuple);
		assertEquals(selectedTuple, helper.getSelectedOldTuples());
	}
	
	@Test
	public void testNewConcentrationParameters() {
		final Map<String, String> concentrationParameters = new HashMap<>();
		concentrationParameters.put("concentration", "parameter");
		
		SettingsHelper helper = new SettingsHelper();
		helper.setNewConcentrationParameters(concentrationParameters);
		assertEquals(concentrationParameters, helper.getNewConcentrationParameters());
	}

	@Test
	public void testNewLagParameters() {
		final Map<String, String> lagParameter = new HashMap<>();
		lagParameter.put("lag", "parameter");
		
		SettingsHelper helper = new SettingsHelper();
		helper.setNewLagParameters(lagParameter);
		assertEquals(lagParameter, helper.getNewLagParameters());
	}

	@Test
	public void testColumnWidths() {
		final Map<String, Integer> columnWidth = new HashMap<>();
		columnWidth.put("col", 5);
		
		SettingsHelper helper = new SettingsHelper();
		helper.setColumnWidths(columnWidth);
		assertEquals(columnWidth, helper.getColumnWidths());
	}
	
	@Test
	public void testSampleInverse() {
		SettingsHelper helper = new SettingsHelper();
		helper.setSampleInverse(true);
		assertTrue(helper.isSampleInverse());
	}
}
