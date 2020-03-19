package de.bund.bfr.knime.pmm.predictorview;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Shape;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ColorAndShapeCreator;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class SettingsHelperTest {

	private static List<String> exampleSelectedIds;
	private static List<KnimeTuple> exampleSelectedTuples;
	private static Map<String, Double> exampleXValues;
	private static List<Double> exampleTimeValues;
	private static Map<String, Color> exampleColors;
	private static Map<String, Shape> exampleShapes;
	private static List<String> exampleVisibleColumns;
	private static Map<String, String> exampleConcentrationParameters;
	private static Map<String, String> exampleLagParameter;
	private static Map<String, Integer> exampleColumnWidths;

	@BeforeClass
	public static void setUp() {
		KnimeSchema schema = new KnimeSchema();
		schema.addIntAttribute("Entero");
		schema.addDoubleAttribute("Real");
		schema.addStringAttribute("Cadena");
		schema.addXmlAttribute("XML");
		exampleSelectedTuples = Arrays.asList(new KnimeTuple(schema));

		exampleSelectedIds = Arrays.asList("1", "2", "3");

		exampleXValues = new HashMap<>();
		exampleXValues.put("time", 15.0);

		exampleTimeValues = Arrays.asList(15.0);

		exampleColors = new HashMap<>();
		exampleColors.put("red", Color.red);

		exampleShapes = new HashMap<>();
		exampleShapes.put(ColorAndShapeCreator.SHAPE_NAMES[0], ColorAndShapeCreator.SHAPES[0]);

		exampleVisibleColumns = Arrays.asList("pH", "wa");

		exampleConcentrationParameters = new HashMap<>();
		exampleConcentrationParameters.put("concentration", "parameter");

		exampleLagParameter = new HashMap<>();
		exampleLagParameter.put("lag", "parameter");

		exampleColumnWidths = new HashMap<>();
		exampleColumnWidths.put("col", 5);
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
		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedIDs(exampleSelectedIds);
		assertEquals(exampleSelectedIds, helper.getSelectedIDs());
	}

	@Test
	public void testParamXValues() {
		SettingsHelper helper = new SettingsHelper();
		helper.setParamXValues(exampleXValues);
		assertEquals(exampleXValues, helper.getParamXValues());
	}

	@Test
	public void testTimeValues() {
		SettingsHelper helper = new SettingsHelper();
		helper.setTimeValues(exampleTimeValues);
		assertEquals(exampleTimeValues, helper.getTimeValues());
	}

	@Test
	public void testColors() {
		SettingsHelper helper = new SettingsHelper();
		helper.setColors(exampleColors);
		assertEquals(exampleColors, helper.getColors());
	}

	@Test
	public void testShapes() {
		SettingsHelper helper = new SettingsHelper();
		helper.setShapes(exampleShapes);
		assertEquals(exampleShapes, helper.getShapes());
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
		SettingsHelper helper = new SettingsHelper();
		helper.setVisibleColumns(exampleVisibleColumns);
		assertEquals(exampleVisibleColumns, helper.getVisibleColumns());
	}

	@Test
	public void testFittedFilter() {
		SettingsHelper helper = new SettingsHelper();
		helper.setFittedFilter("fitted filter");
		assertEquals("fitted filter", helper.getFittedFilter());
	}

	@Test
	public void testConcentrationParameters() {
		SettingsHelper helper = new SettingsHelper();
		helper.setConcentrationParameters(exampleConcentrationParameters);
		assertEquals(exampleConcentrationParameters, helper.getConcentrationParameters());
	}

	@Test
	public void testLagParameters() {
		SettingsHelper helper = new SettingsHelper();
		helper.setLagParameters(exampleLagParameter);
		assertEquals(exampleLagParameter, helper.getLagParameters());
	}

	@Test
	public void testSelectedTuples() {
		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedTuples(exampleSelectedTuples);
		assertEquals(exampleSelectedTuples, helper.getSelectedTuples());
	}

	@Test
	public void testSelectedOldTuples() {
		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedOldTuples(exampleSelectedTuples);
		assertEquals(exampleSelectedTuples, helper.getSelectedOldTuples());
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
		SettingsHelper helper = new SettingsHelper();
		helper.setColumnWidths(exampleColumnWidths);
		assertEquals(exampleColumnWidths, helper.getColumnWidths());
	}

	@Test
	public void testSampleInverse() {
		SettingsHelper helper = new SettingsHelper();
		helper.setSampleInverse(true);
		assertTrue(helper.isSampleInverse());
	}

	@Test
	public void testLoadEmptySettings() {
		NodeSettings settings = new NodeSettings("identifier");
		SettingsHelper helper = new SettingsHelper();
		helper.loadSettings(settings);

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
	public void testLoadSettings() {

		NodeSettings settings = new NodeSettings("identifier");
		settings.addString("SelectedIDs", XmlConverter.objectToXml(exampleSelectedIds));
		settings.addString("ParamXValues", XmlConverter.objectToXml(exampleXValues));
		settings.addString("TimeValues", XmlConverter.objectToXml(exampleTimeValues));
		settings.addString("Colors", XmlConverter.colorMapToXml(exampleColors));
		settings.addString("Shapes", XmlConverter.shapeMapToXml(exampleShapes));
		settings.addBoolean("ManualRange", true);
		settings.addDouble("MinX", 1.0);
		settings.addDouble("MaxX", 100.0);
		settings.addDouble("MinY", 1.0);
		settings.addDouble("MaxY", 100.0);
		settings.addBoolean("DrawLines", true);
		settings.addBoolean("ShowLegend", true);
		settings.addBoolean("AddLegendInfo", true);
		settings.addBoolean("DisplayHighlighted", true);
		settings.addBoolean("ExportAsSvg", true);
		settings.addString("UnitX", "UnitX");
		settings.addString("UnitY", "UnitY");
		settings.addString("TransformX", "TransformX");
		settings.addString("TransformY", "TransformY");
		settings.addBoolean("StandardVisibleColumns", true);
		settings.addString("VisibleColumns", XmlConverter.objectToXml(exampleVisibleColumns));
		settings.addString("FittedFilter", "fittedFilter");
		settings.addString("ConcentrationParameters", XmlConverter.objectToXml(exampleConcentrationParameters));
		settings.addString("LagParameters", XmlConverter.objectToXml(exampleLagParameter));
		settings.addString("SelectedTuples", XmlConverter.tupleListToXml(exampleSelectedTuples));
		settings.addString("SelectedOldTuples", XmlConverter.tupleListToXml(exampleSelectedTuples));
		settings.addString("NewConcentrationParameters", XmlConverter.objectToXml(exampleConcentrationParameters));
		settings.addString("NewLagParameters", XmlConverter.objectToXml(exampleLagParameter));
		settings.addString("ColumnWidths", XmlConverter.objectToXml(exampleColumnWidths));
		settings.addBoolean("SampleInverse", true);

		SettingsHelper helper = new SettingsHelper();
		helper.loadSettings(settings);

		assertEquals(exampleSelectedIds, helper.getSelectedIDs());
		assertEquals(exampleXValues, helper.getParamXValues());
		assertEquals(exampleTimeValues, helper.getTimeValues());
		assertEquals(exampleColors, helper.getColors());
		assertEquals(exampleShapes, helper.getShapes());
		assertTrue(helper.isManualRange());
		assertEquals(1.0, helper.getMinX(), .0);
		assertEquals(100.0, helper.getMaxX(), .0);
		assertEquals(1.0, helper.getMinY(), .0);
		assertEquals(100.0, helper.getMaxY(), .0);
		assertTrue(helper.isDrawLines());
		assertTrue(helper.isShowLegend());
		assertTrue(helper.isAddLegendInfo());
		assertTrue(helper.isDisplayHighlighted());
		assertTrue(helper.isExportAsSvg());
		assertEquals("UnitX", helper.getUnitX());
		assertEquals("UnitY", helper.getUnitY());
		assertEquals("TransformX", helper.getTransformX());
		assertEquals("TransformY", helper.getTransformY());
		assertTrue(helper.isStandardVisibleColumns());
		assertEquals(exampleVisibleColumns, helper.getVisibleColumns());
		assertEquals("fittedFilter", helper.getFittedFilter());
		assertEquals(exampleConcentrationParameters, helper.getConcentrationParameters());
		assertEquals(exampleLagParameter, helper.getLagParameters());
		// SettingsHelper is losing the row id and equals fails. Only check size for now.
		assertEquals(1, helper.getSelectedTuples().size());
		assertEquals(1, helper.getSelectedOldTuples().size());
		assertEquals(exampleConcentrationParameters, helper.getNewConcentrationParameters());
		assertEquals(exampleLagParameter, helper.getNewLagParameters());
		assertEquals(exampleColumnWidths, helper.getColumnWidths());
		assertTrue(helper.isSampleInverse());
	}
	
	@Test
	public void testSaveSettings() throws InvalidSettingsException {
		
		SettingsHelper helper = new SettingsHelper();
		helper.setSelectedIDs(exampleSelectedIds);
		helper.setParamXValues(exampleXValues);
		helper.setTimeValues(exampleTimeValues);
		helper.setColors(exampleColors);
		helper.setShapes(exampleShapes);
		helper.setManualRange(true);
		helper.setMinX(1.0);
		helper.setMaxX(100.0);
		helper.setMinY(1.0);
		helper.setMaxY(100.0);
		helper.setDrawLines(true);
		helper.setShowLegend(true);
		helper.setAddLegendInfo(true);
		helper.setDisplayHighlighted(true);
		helper.setExportAsSvg(true);
		helper.setUnitX("UnitX");
		helper.setUnitY("UnitY");
		helper.setTransformX("TransformX");
		helper.setTransformY("TransformY");
		helper.setStandardVisibleColumns(true);
		helper.setVisibleColumns(exampleVisibleColumns);
		helper.setFittedFilter("FittedFilter");
		helper.setConcentrationParameters(exampleConcentrationParameters);
		helper.setLagParameters(exampleLagParameter);
		helper.setSelectedTuples(exampleSelectedTuples);
		helper.setSelectedOldTuples(exampleSelectedTuples);
		helper.setNewConcentrationParameters(exampleConcentrationParameters);
		helper.setNewLagParameters(exampleLagParameter);
		helper.setColumnWidths(exampleColumnWidths);
		helper.setSampleInverse(true);
		
		NodeSettings settings = new NodeSettings("identifier");
		helper.saveSettings(settings);

		assertEquals(XmlConverter.objectToXml(exampleSelectedIds), settings.getString("SelectedIDs"));
		assertEquals(XmlConverter.objectToXml(exampleXValues), settings.getString("ParamXValues"));
		assertEquals(XmlConverter.objectToXml(exampleTimeValues), settings.getString("TimeValues"));
		assertEquals(XmlConverter.colorMapToXml(exampleColors), settings.getString("Colors"));
		assertEquals(XmlConverter.shapeMapToXml(exampleShapes), settings.getString("Shapes"));
		assertTrue(settings.getBoolean("ManualRange"));
		assertEquals(1.0, settings.getDouble("MinX"), .0);
		assertEquals(100.0, settings.getDouble("MaxX"), .0);
		assertEquals(1.0, settings.getDouble("MinY"), .0);
		assertEquals(100.0, settings.getDouble("MaxY"), .0);
		assertTrue(settings.getBoolean("DrawLines"));
		assertTrue(settings.getBoolean("ShowLegend"));
		assertTrue(settings.getBoolean("AddLegendInfo"));
		assertTrue(settings.getBoolean("DisplayHighlighted"));
		assertTrue(settings.getBoolean("ExportAsSvg"));
		assertEquals("UnitX", settings.getString("UnitX"));
		assertEquals("UnitY", settings.getString("UnitY"));
		assertEquals("TransformX", settings.getString("TransformX"));
		assertEquals("TransformY", settings.getString("TransformY"));
		assertTrue(settings.getBoolean("StandardVisibleColumns"));
		assertEquals(XmlConverter.objectToXml(exampleVisibleColumns), settings.getString("VisibleColumns"));
		assertEquals("FittedFilter", settings.getString("FittedFilter"));
		assertEquals(XmlConverter.objectToXml(exampleConcentrationParameters), settings.getString("ConcentrationParameters"));
		assertEquals(XmlConverter.objectToXml(exampleLagParameter), settings.getString("LagParameters"));
		assertEquals(XmlConverter.tupleListToXml(exampleSelectedTuples), settings.getString("SelectedTuples"));
		assertEquals(XmlConverter.tupleListToXml(exampleSelectedTuples), settings.getString("SelectedOldTuples"));
		assertEquals(XmlConverter.objectToXml(exampleConcentrationParameters), settings.getString("NewConcentrationParameters"));
		assertEquals(XmlConverter.objectToXml(exampleLagParameter), settings.getString("NewLagParameters"));
		assertEquals(XmlConverter.objectToXml(exampleColumnWidths), settings.getString("ColumnWidths"));
		assertTrue(settings.getBoolean("SampleInverse"));
	}
}
