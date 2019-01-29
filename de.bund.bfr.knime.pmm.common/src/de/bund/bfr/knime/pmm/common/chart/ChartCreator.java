/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class ChartCreator extends ChartPanel {

	private static final long serialVersionUID = 1L;

	private List<ZoomListener> zoomListeners;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private Map<String, List<Color>> colorLists;
	private Map<String, List<Shape>> shapeLists;

	private String paramX;
	private String paramY;
	private String unitX;
	private String unitY;
	private String transformX;
	private String transformY;
	private boolean useManualRange;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private boolean drawLines;
	private boolean showLegend;
	private boolean addInfoInLegend;
	private boolean showConfidenceInterval;
	private boolean inverse;

	private List<String> warnings;

	public ChartCreator(Plotable plotable) {
		super(new JFreeChart(new XYPlot()));
		zoomListeners = new ArrayList<>();
		getPopupMenu().remove(10);
		getPopupMenu().remove(9);
		getPopupMenu().remove(8);
		getPopupMenu().remove(7);
		getPopupMenu().remove(6);
		getPopupMenu().remove(3);
		getPopupMenu().add(new DataAndModelChartSaveAsItem(), 3);
		plotables = new LinkedHashMap<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();
		colors = new LinkedHashMap<>();
		shapes = new LinkedHashMap<>();
		colorLists = new LinkedHashMap<>();
		shapeLists = new LinkedHashMap<>();

		plotables.put("", plotable);
		shortLegend.put("", "");
		longLegend.put("", "");
	}

	public ChartCreator(Map<String, Plotable> plotables,
			Map<String, String> shortLegend, Map<String, String> longLegend) {
		super(new JFreeChart(new XYPlot()));
		zoomListeners = new ArrayList<>();
		getPopupMenu().remove(10);
		getPopupMenu().remove(9);
		getPopupMenu().remove(8);
		getPopupMenu().remove(7);
		getPopupMenu().remove(6);
		getPopupMenu().remove(3);
		getPopupMenu().add(new DataAndModelChartSaveAsItem(), 3);
		this.plotables = plotables;
		this.shortLegend = shortLegend;
		this.longLegend = longLegend;
		colors = new LinkedHashMap<>();
		shapes = new LinkedHashMap<>();
		colorLists = new LinkedHashMap<>();
		shapeLists = new LinkedHashMap<>();
	}

	public void addZoomListener(ZoomListener listener) {
		zoomListeners.add(listener);
	}

	public void removeZoomListener(ZoomListener listener) {
		zoomListeners.remove(listener);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ValueAxis domainAxis = ((XYPlot) getChart().getPlot()).getDomainAxis();
		ValueAxis rangeAxis = ((XYPlot) getChart().getPlot()).getRangeAxis();

		Range xRange1 = domainAxis.getRange();
		Range yRange1 = rangeAxis.getRange();
		super.mouseReleased(e);
		Range xRange2 = domainAxis.getRange();
		Range yRange2 = rangeAxis.getRange();

		if (!xRange1.equals(xRange2) || !yRange1.equals(yRange2)) {
			minX = xRange2.getLowerBound();
			maxX = xRange2.getUpperBound();
			minY = yRange2.getLowerBound();
			maxY = yRange2.getUpperBound();
			fireZoomChanged();
		}
	}

	public void createChart() {
		setChart(getChart(new ArrayList<>(plotables.keySet())));
	}

	public void createChart(String idToPaint) {
		setChart(getChart(idToPaint));
	}

	public void createChart(List<String> idsToPaint) {
		setChart(getChart(idsToPaint));
	}

	public JFreeChart getChart(String idToPaint) {
		if (idToPaint != null) {
			return getChart(Arrays.asList(idToPaint));
		} else {
			return getChart(new ArrayList<String>());
		}
	}

	public JFreeChart getChart(List<String> idsToPaint) {
		if (paramX == null || paramY == null) {
			return new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT,
					new XYPlot(), showLegend);
		}

		NumberAxis xAxis = new NumberAxis(AttributeUtilities.getNameWithUnit(
				paramX, unitX, transformX));
		NumberAxis yAxis = new NumberAxis(AttributeUtilities.getNameWithUnit(
				paramY, unitY, transformY));
		XYPlot plot = new XYPlot(null, xAxis, yAxis, null);
		double usedMinX = Double.POSITIVE_INFINITY;
		double usedMaxX = Double.NEGATIVE_INFINITY;
		int index = 0;
		ColorAndShapeCreator colorAndShapeCreator = new ColorAndShapeCreator(
				idsToPaint.size());

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			try {
				if (plotable != null) {
					if (plotable.getType() == Plotable.BOTH
							|| plotable.getType() == Plotable.BOTH_STRICT) {
						Double minArg = Plotable.transform(plotable
								.convertToUnit(paramX, plotable
										.getMinArguments().get(paramX), unitX),
								transformX);
						Double maxArg = Plotable.transform(plotable
								.convertToUnit(paramX, plotable
										.getMaxArguments().get(paramX), unitX),
								transformX);

						if (isValid(minArg)) {
							usedMinX = Math.min(usedMinX, minArg);
						}

						if (isValid(maxArg)) {
							usedMaxX = Math.max(usedMaxX, maxArg);
						}

						for (Map<String, Integer> choice : plotable
								.getAllChoices()) {
							double[][] points = plotable.getPoints(paramX,
									paramY, unitX, unitY, transformX,
									transformY, choice);

							if (points != null) {
								for (int i = 0; i < points[0].length; i++) {
									if (isValid(points[0][i])) {
										usedMinX = Math.min(usedMinX,
												points[0][i]);
										usedMaxX = Math.max(usedMaxX,
												points[0][i]);
									}
								}
							}
						}
					} else if (plotable.getType() == Plotable.DATASET
							|| plotable.getType() == Plotable.DATASET_STRICT) {
						double[][] points = plotable.getPoints(paramX, paramY,
								unitX, unitY, transformX, transformY);

						if (points != null) {
							for (int i = 0; i < points[0].length; i++) {
								if (isValid(points[0][i])) {
									usedMinX = Math.min(usedMinX, points[0][i]);
									usedMaxX = Math.max(usedMaxX, points[0][i]);
								}
							}
						}
					} else if (plotable.getType() == Plotable.FUNCTION) {
						Double minArg = Plotable.transform(plotable
								.convertToUnit(paramX, plotable
										.getMinArguments().get(paramX), unitX),
								transformX);
						Double maxArg = Plotable.transform(plotable
								.convertToUnit(paramX, plotable
										.getMaxArguments().get(paramX), unitX),
								transformX);

						if (isValid(minArg)) {
							usedMinX = Math.min(usedMinX, minArg);
						}

						if (isValid(maxArg)) {
							usedMaxX = Math.max(usedMaxX, maxArg);
						}
					} else if (plotable.getType() == Plotable.FUNCTION_SAMPLE) {
						Double minArg = Plotable.transform(plotable
								.convertToUnit(paramX, plotable
										.getMinArguments().get(paramX), unitX),
								transformX);
						Double maxArg = Plotable.transform(plotable
								.convertToUnit(paramX, plotable
										.getMaxArguments().get(paramX), unitX),
								transformX);

						if (isValid(minArg)) {
							usedMinX = Math.min(usedMinX, minArg);
						}

						if (isValid(maxArg)) {
							usedMaxX = Math.max(usedMaxX, maxArg);
						}

						for (Double x : plotable.getSamples()) {
							Double xx = Plotable.transform(
									plotable.convertToUnit(paramX, x, unitX),
									transformX);

							if (isValid(xx)) {
								usedMinX = Math.min(usedMinX, xx);
								usedMaxX = Math.max(usedMaxX, xx);
							}
						}
					}
				}
			} catch (ConvertException e) {
			}
		}

		if (Double.isInfinite(usedMinX)) {
			usedMinX = 0.0;
		}

		if (Double.isInfinite(usedMaxX)) {
			usedMaxX = 100.0;
		}

		if (paramX.equals(AttributeUtilities.TIME)
				|| paramX.equals(AttributeUtilities.CONCENTRATION)) {
			usedMinX = Math.min(usedMinX, 0.0);
			xAxis.setAutoRangeIncludesZero(true);
		} else {
			xAxis.setAutoRangeIncludesZero(false);
		}

		if (paramY.equals(AttributeUtilities.TIME)
				|| paramY.equals(AttributeUtilities.CONCENTRATION)) {
			yAxis.setAutoRangeIncludesZero(true);
		} else {
			yAxis.setAutoRangeIncludesZero(false);
		}

		if (usedMinX == usedMaxX) {
			usedMinX -= 1.0;
			usedMaxX += 1.0;
		}

		if (useManualRange && minX < maxX && minY < maxY) {
			usedMinX = minX;
			usedMaxX = maxX;
			xAxis.setRange(new Range(minX, maxX));
			yAxis.setRange(new Range(minY, maxY));
		}

		Set<ConvertException> convertExceptions = new LinkedHashSet<>();

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.DATASET) {
				try {
					plotDataSet(plot, plotable, id, colorAndShapeCreator
							.getColorList().get(index), colorAndShapeCreator
							.getShapeList().get(index));
					index++;
				} catch (ConvertException e) {
					convertExceptions.add(e);
				}
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null
					&& plotable.getType() == Plotable.DATASET_STRICT) {
				try {
					plotDataSetStrict(plot, plotable, id);
					index++;
				} catch (ConvertException e) {
					convertExceptions.add(e);
				}
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.FUNCTION) {
				try {
					plotFunction(plot, plotable, id, colorAndShapeCreator
							.getColorList().get(index), colorAndShapeCreator
							.getShapeList().get(index), usedMinX, usedMaxX);
					index++;
				} catch (ConvertException e) {
					convertExceptions.add(e);
				}
			}
		}

		warnings = new ArrayList<>();

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null
					&& plotable.getType() == Plotable.FUNCTION_SAMPLE) {
				try {
					plotFunctionSample(plot, plotable, id, colorAndShapeCreator
							.getColorList().get(index), colorAndShapeCreator
							.getShapeList().get(index), usedMinX, usedMaxX,
							warnings);
					index++;
				} catch (ConvertException e) {
					convertExceptions.add(e);
				}
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.BOTH) {
				try {
					plotBoth(plot, plotable, id, colorAndShapeCreator
							.getColorList().get(index), colorAndShapeCreator
							.getShapeList().get(index), usedMinX, usedMaxX);
					index++;
				} catch (ConvertException e) {
					convertExceptions.add(e);
				}
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.BOTH_STRICT) {
				try {
					plotBothStrict(plot, plotable, id, usedMinX, usedMaxX);
					index++;
				} catch (ConvertException e) {
					convertExceptions.add(e);
				}
			}
		}

		if (!convertExceptions.isEmpty()) {
			String warning = "Some datasets/functions cannot be converted to the desired unit\n";

			warning += "Uncovertable units: ";

			for (ConvertException e : convertExceptions) {
				warning += e.fromUnit + "->" + e.toUnit + ", ";
			}

			warning = warning.substring(0, warning.length() - 2);

			JOptionPane.showMessageDialog(this, warning, "Warning",
					JOptionPane.WARNING_MESSAGE);
		}

		return new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot,
				showLegend);
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public String getParamX() {
		return paramX;
	}

	public void setParamX(String paramX) {
		this.paramX = paramX;
	}

	public String getParamY() {
		return paramY;
	}

	public void setParamY(String paramY) {
		this.paramY = paramY;
	}

	public String getUnitX() {
		return unitX;
	}

	public void setUnitX(String unitX) {
		this.unitX = unitX;
	}

	public String getUnitY() {
		return unitY;
	}

	public void setUnitY(String unitY) {
		this.unitY = unitY;
	}

	public String getTransformX() {
		return transformX;
	}

	public void setTransformX(String transformX) {
		this.transformX = transformX;
	}

	public String getTransformY() {
		return transformY;
	}

	public void setTransformY(String transformY) {
		this.transformY = transformY;
	}

	public boolean isUseManualRange() {
		return useManualRange;
	}

	public void setUseManualRange(boolean useManualRange) {
		this.useManualRange = useManualRange;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public boolean isDrawLines() {
		return drawLines;
	}

	public void setDrawLines(boolean drawLines) {
		this.drawLines = drawLines;
	}

	public boolean isShowLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	public boolean isAddInfoInLegend() {
		return addInfoInLegend;
	}

	public void setAddInfoInLegend(boolean addInfoInLegend) {
		this.addInfoInLegend = addInfoInLegend;
	}

	public boolean isShowConfidenceInterval() {
		return showConfidenceInterval;
	}

	public void setShowConfidenceInterval(boolean showConfidenceInterval) {
		this.showConfidenceInterval = showConfidenceInterval;
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

	public void setColors(Map<String, Color> colors) {
		this.colors = colors;
	}

	public Map<String, Shape> getShapes() {
		return shapes;
	}

	public void setShapes(Map<String, Shape> shapes) {
		this.shapes = shapes;
	}

	public Map<String, List<Color>> getColorLists() {
		return colorLists;
	}

	public void setColorLists(Map<String, List<Color>> colorLists) {
		this.colorLists = colorLists;
	}

	public Map<String, List<Shape>> getShapeLists() {
		return shapeLists;
	}

	public void setShapeLists(Map<String, List<Shape>> shapeLists) {
		this.shapeLists = shapeLists;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	private void fireZoomChanged() {
		for (ZoomListener listener : zoomListeners) {
			listener.zoomChanged();
		}
	}

	private void plotDataSet(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape) throws ConvertException {
		double[][] points = plotable.getPoints(paramX, paramY, unitX, unitY,
				transformX, transformY);
		String legend = shortLegend.get(id);
		Color color = colors.get(id);
		Shape shape = shapes.get(id);

		if (addInfoInLegend) {
			legend = longLegend.get(id);
		}

		if (color == null) {
			color = defaultColor;
		}

		if (shape == null) {
			shape = defaultShape;
		}

		if (points != null) {
			DefaultXYDataset dataset = new DefaultXYDataset();
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(
					drawLines, true);

			dataset.addSeries(legend, points);
			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
			renderer.setSeriesPaint(0, color);
			renderer.setSeriesShape(0, shape);

			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			plot.setDataset(i, dataset);
			plot.setRenderer(i, renderer);
		}
	}

	private void plotDataSetStrict(XYPlot plot, Plotable plotable, String id)
			throws ConvertException {
		String legend = shortLegend.get(id);
		List<Color> colorList = colorLists.get(id);
		List<Shape> shapeList = shapeLists.get(id);
		ColorAndShapeCreator creator = new ColorAndShapeCreator(
				plotable.getNumberOfCombinations());
		int index = 0;

		if (addInfoInLegend) {
			legend = longLegend.get(id);
		}

		if (colorList == null || colorList.isEmpty()) {
			colorList = creator.getColorList();
		}

		if (shapeList == null || shapeList.isEmpty()) {
			shapeList = creator.getShapeList();
		}

		for (Map<String, Integer> choiceMap : plotable.getAllChoices()) {
			double[][] dataPoints = plotable.getPoints(paramX, paramY, unitX,
					unitY, transformX, transformY, choiceMap);

			if (dataPoints != null) {
				DefaultXYDataset dataSet = new DefaultXYDataset();
				XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
						drawLines, true);
				String addLegend = "";

				for (String arg : choiceMap.keySet()) {
					if (!arg.equals(paramX)) {
						addLegend += " ("
								+ arg
								+ "="
								+ plotable.getFunctionArguments().get(arg)
										.get(choiceMap.get(arg)) + ")";
					}
				}

				dataSet.addSeries(legend + addLegend, dataPoints);
				dataRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				dataRenderer.setSeriesPaint(0, colorList.get(index));
				dataRenderer.setSeriesShape(0, shapeList.get(index));

				int i;

				if (plot.getDataset(0) == null) {
					i = 0;
				} else {
					i = plot.getDatasetCount();
				}

				plot.setDataset(i, dataSet);
				plot.setRenderer(i, dataRenderer);
			}

			index++;
		}
	}

	private void plotFunction(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape, double minX, double maxX)
			throws ConvertException {
		double[][] points = plotable.getFunctionPoints(paramX, paramY, unitX,
				unitY, transformX, transformY, minX, maxX,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		double[][] functionErrors = null;
		String legend = shortLegend.get(id);
		Color color = colors.get(id);
		Shape shape = shapes.get(id);

		if (showConfidenceInterval) {
			functionErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
					unitY, transformX, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}

		if (addInfoInLegend) {
			legend = longLegend.get(id);
		}

		if (color == null) {
			color = defaultColor;
		}

		if (shape == null) {
			shape = defaultShape;
		}

		if (points != null) {
			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			if (functionErrors != null) {
				YIntervalSeriesCollection functionDataset = new YIntervalSeriesCollection();
				DeviationRenderer functionRenderer = new DeviationRenderer(
						true, false);
				YIntervalSeries series = new YIntervalSeries(legend);

				for (int j = 0; j < points[0].length; j++) {
					double error = Double.isNaN(functionErrors[1][j]) ? 0.0
							: functionErrors[1][j];

					series.add(points[0][j], points[1][j],
							points[1][j] - error, points[1][j] + error);
				}

				functionDataset.addSeries(series);
				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				functionRenderer.setSeriesPaint(0, color);
				functionRenderer.setSeriesFillPaint(0, color);
				functionRenderer.setSeriesShape(0, shape);

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			} else {
				DefaultXYDataset functionDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer(
						true, false);

				functionDataset.addSeries(legend, points);
				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				functionRenderer.setSeriesPaint(0, color);
				functionRenderer.setSeriesShape(0, shape);

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			}
		}
	}

	private void plotFunctionSample(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape, double minX, double maxX,
			List<String> warnings) throws ConvertException {
		double[][] functionPoints = plotable.getFunctionPoints(paramX, paramY,
				unitX, unitY, transformX, transformY, minX, maxX,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		double[][] samplePoints;

		if (!inverse) {
			samplePoints = plotable.getFunctionSamplePoints(paramX, paramY,
					unitX, unitY, transformX, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
					warnings);
		} else {
			samplePoints = plotable.getInverseFunctionSamplePoints(paramX,
					paramY, unitX, unitY, transformX, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
					warnings);
		}

		double[][] functionErrors = null;
		String legend = shortLegend.get(id);
		Color color = colors.get(id);
		Shape shape = shapes.get(id);

		if (showConfidenceInterval) {
			functionErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
					unitY, transformX, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}

		if (addInfoInLegend) {
			legend = longLegend.get(id);
		}

		if (color == null) {
			color = defaultColor;
		}

		if (shape == null) {
			shape = defaultShape;
		}

		if (functionPoints != null) {
			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			if (functionErrors != null) {
				YIntervalSeriesCollection functionDataset = new YIntervalSeriesCollection();
				DeviationRenderer functionRenderer = new DeviationRenderer(
						true, false);
				YIntervalSeries series = new YIntervalSeries(legend);

				for (int j = 0; j < functionPoints[0].length; j++) {
					double error = Double.isNaN(functionErrors[1][j]) ? 0.0
							: functionErrors[1][j];

					series.add(functionPoints[0][j], functionPoints[1][j],
							functionPoints[1][j] - error, functionPoints[1][j]
									+ error);
				}

				functionDataset.addSeries(series);
				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				functionRenderer.setSeriesPaint(0, color);
				functionRenderer.setSeriesFillPaint(0, color);
				functionRenderer.setSeriesShape(0, shape);

				if (samplePoints != null) {
					functionRenderer.setBaseSeriesVisibleInLegend(false);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			} else {
				DefaultXYDataset functionDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer(
						true, false);

				functionDataset.addSeries(legend, functionPoints);
				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				functionRenderer.setSeriesPaint(0, color);
				functionRenderer.setSeriesShape(0, shape);

				if (samplePoints != null) {
					functionRenderer.setBaseSeriesVisibleInLegend(false);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			}

			if (samplePoints != null) {
				DefaultXYDataset sampleDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer sampleRenderer = new XYLineAndShapeRenderer(
						false, true);

				sampleDataset.addSeries(legend, samplePoints);
				sampleRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				sampleRenderer.setSeriesPaint(0, color);
				sampleRenderer.setSeriesShape(0, shape);

				plot.setDataset(i + 1, sampleDataset);
				plot.setRenderer(i + 1, sampleRenderer);
			}
		}
	}

	private void plotBoth(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape, double minX, double maxX)
			throws ConvertException {
		double[][] modelPoints = plotable.getFunctionPoints(paramX, paramY,
				unitX, unitY, transformX, transformY, minX, maxX,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		double[][] dataPoints = plotable.getPoints(paramX, paramY, unitX,
				unitY, transformX, transformY);
		double[][] functionErrors = null;
		String legend = shortLegend.get(id);
		Color color = colors.get(id);
		Shape shape = shapes.get(id);

		if (showConfidenceInterval) {
			functionErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
					unitY, transformX, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}

		if (addInfoInLegend) {
			legend = longLegend.get(id);
		}

		if (color == null) {
			color = defaultColor;
		}

		if (shape == null) {
			shape = defaultShape;
		}

		if (modelPoints != null) {
			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			if (functionErrors != null) {
				YIntervalSeriesCollection functionDataset = new YIntervalSeriesCollection();
				DeviationRenderer functionRenderer = new DeviationRenderer(
						true, false);
				YIntervalSeries series = new YIntervalSeries(legend);

				for (int j = 0; j < modelPoints[0].length; j++) {
					double error = Double.isNaN(functionErrors[1][j]) ? 0.0
							: functionErrors[1][j];

					series.add(modelPoints[0][j], modelPoints[1][j],
							modelPoints[1][j] - error, modelPoints[1][j]
									+ error);
				}

				functionDataset.addSeries(series);
				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				functionRenderer.setSeriesPaint(0, color);
				functionRenderer.setSeriesFillPaint(0, color);
				functionRenderer.setSeriesShape(0, shape);

				if (dataPoints != null) {
					functionRenderer.setBaseSeriesVisibleInLegend(false);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			} else {
				DefaultXYDataset functionDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer(
						true, false);

				functionDataset.addSeries(legend, modelPoints);
				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				functionRenderer.setSeriesPaint(0, color);
				functionRenderer.setSeriesShape(0, shape);

				if (dataPoints != null) {
					functionRenderer.setBaseSeriesVisibleInLegend(false);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			}
		}

		if (dataPoints != null) {
			DefaultXYDataset dataSet = new DefaultXYDataset();
			XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
					drawLines, true);

			dataSet.addSeries(legend, dataPoints);
			dataRenderer
					.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
			dataRenderer.setSeriesPaint(0, color);
			dataRenderer.setSeriesShape(0, shape);

			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			plot.setDataset(i, dataSet);
			plot.setRenderer(i, dataRenderer);
		}
	}

	private void plotBothStrict(XYPlot plot, Plotable plotable, String id,
			double minX, double maxX) throws ConvertException {
		String legend = shortLegend.get(id);
		List<Color> colorList = colorLists.get(id);
		List<Shape> shapeList = shapeLists.get(id);
		ColorAndShapeCreator creator = new ColorAndShapeCreator(
				plotable.getNumberOfCombinations());
		int index = 0;

		if (addInfoInLegend) {
			legend = longLegend.get(id);
		}

		if (colorList == null || colorList.isEmpty()) {
			colorList = creator.getColorList();
		}

		if (shapeList == null || shapeList.isEmpty()) {
			shapeList = creator.getShapeList();
		}

		for (Map<String, Integer> choiceMap : plotable.getAllChoices()) {
			double[][] dataPoints = plotable.getPoints(paramX, paramY, unitX,
					unitY, transformX, transformY, choiceMap);

			if (dataPoints == null) {
				continue;
			}

			double[][] modelPoints = plotable.getFunctionPoints(paramX, paramY,
					unitX, unitY, transformX, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
					choiceMap);

			if (modelPoints == null) {
				continue;
			}

			double[][] modelErrors = null;

			if (showConfidenceInterval) {
				modelErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
						unitY, transformX, transformY, minX, maxX,
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
						choiceMap);
			}

			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			String addLegend = "";

			for (String arg : choiceMap.keySet()) {
				if (!arg.equals(paramX)) {
					addLegend += " ("
							+ arg
							+ "="
							+ plotable.getFunctionArguments().get(arg)
									.get(choiceMap.get(arg)) + ")";
				}
			}

			if (modelErrors != null) {
				YIntervalSeriesCollection modelSet = new YIntervalSeriesCollection();
				DeviationRenderer modelRenderer = new DeviationRenderer(true,
						false);
				YIntervalSeries series = new YIntervalSeries(legend);

				for (int j = 0; j < modelPoints[0].length; j++) {
					double error = Double.isNaN(modelErrors[1][j]) ? 0.0
							: modelErrors[1][j];

					series.add(modelPoints[0][j], modelPoints[1][j],
							modelPoints[1][j] - error, modelPoints[1][j]
									+ error);
				}

				modelSet.addSeries(series);
				modelRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				modelRenderer.setSeriesPaint(0, colorList.get(index));
				modelRenderer.setSeriesFillPaint(0, colorList.get(index));
				modelRenderer.setSeriesShape(0, shapeList.get(index));

				if (dataPoints != null) {
					modelRenderer.setBaseSeriesVisibleInLegend(false);
				}

				plot.setDataset(i, modelSet);
				plot.setRenderer(i, modelRenderer);
			} else {
				DefaultXYDataset modelSet = new DefaultXYDataset();
				XYLineAndShapeRenderer modelRenderer = new XYLineAndShapeRenderer(
						true, false);

				modelSet.addSeries(legend + addLegend, modelPoints);
				modelRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				modelRenderer.setBaseSeriesVisibleInLegend(false);
				modelRenderer.setSeriesPaint(0, colorList.get(index));
				modelRenderer.setSeriesShape(0, shapeList.get(index));

				plot.setDataset(i, modelSet);
				plot.setRenderer(i, modelRenderer);
			}

			DefaultXYDataset dataSet = new DefaultXYDataset();
			XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
					drawLines, true);

			dataSet.addSeries(legend + addLegend, dataPoints);
			dataRenderer
					.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
			dataRenderer.setSeriesPaint(0, colorList.get(index));
			dataRenderer.setSeriesShape(0, shapeList.get(index));
			plot.setDataset(i + 1, dataSet);
			plot.setRenderer(i + 1, dataRenderer);

			index++;
		}
	}

	private boolean isValid(Double value) {
		return value != null && !value.isInfinite() && !value.isNaN();
	}

	private class DataAndModelChartSaveAsItem extends JMenuItem implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public DataAndModelChartSaveAsItem() {
			super("Save as...");

			addActionListener(this);
		}

		private void fireSaveAsButtonClicked(String fileName) {
			ChartCreator chartPanel = ChartCreator.this;

			ChartUtilities.saveChartAs(chartPanel.getChart(), fileName,
					chartPanel.getWidth(), chartPanel.getHeight());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter svgFilter = new FileFilter() {

				@Override
				public String getDescription() {
					return "SVG Vector Graphic (*.svg)";
				}

				@Override
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getName().toLowerCase().endsWith(".svg");
				}
			};
			FileFilter pngFilter = new FileFilter() {

				@Override
				public String getDescription() {
					return "Portable Network Graphics (*.png)";
				}

				@Override
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getName().toLowerCase().endsWith(".png");
				}
			};

			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(pngFilter);
			fileChooser.addChoosableFileFilter(svgFilter);

			if (fileChooser.showSaveDialog(ChartCreator.this) == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getName();
				String path = fileChooser.getSelectedFile().getAbsolutePath();

				if (fileChooser.getFileFilter() == svgFilter) {
					if (fileName.toLowerCase().endsWith(".svg")) {
						fireSaveAsButtonClicked(path);
					} else {
						fireSaveAsButtonClicked(path + ".svg");
					}
				} else if (fileChooser.getFileFilter() == pngFilter) {
					if (fileName.toLowerCase().endsWith(".png")) {
						fireSaveAsButtonClicked(path);
					} else {
						fireSaveAsButtonClicked(path + ".png");
					}
				}
			}
		}

	}

	public static interface ZoomListener {

		public void zoomChanged();
	}

}
