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
package de.bund.bfr.knime.foodprocess.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlCursor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.knime.pcml.node.pcmltotable.PCMLDataTable;
import de.bund.bfr.knime.pmm.common.chart.ColorAndShapeCreator;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.util.FormulaEvaluator;
import de.bund.bfr.knime.util.NameAndDbId;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.ProcessDataDocument.ProcessData;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;
import de.bund.bfr.pcml10.RowDocument.Row;

public class MyChartCreator {

	private List<ProcessLegendElement> processLegend;
	private ViewUi vui;

	public MyChartCreator(ViewUi vui) {
		this.vui = vui;
	}

	public List<ProcessLegendElement> getProcessLegend() {
		return processLegend;
	}

	public JFreeChart createChart(PCMLDocument pcmlDoc, List<String> usedParameters, String xUnits) {
		return createChart(pcmlDoc, usedParameters, xUnits, false, false, false);
	}

	public JFreeChart createChart(PCMLDocument pcmlDoc, List<String> usedParameters, String xUnits,
			boolean equidistantProcesses, boolean plotLines, boolean plotPoints) {
		Map<NameAndDbId, Integer> columns = PCMLDataTable.createColumnMap(pcmlDoc);
		Map<String, ProcessNode> processNodes = PCMLDataTable.createProcessNodeMap(pcmlDoc);
		DataTableSpec spec = PCMLDataTable.createOutSpec(columns);

		Double timeDiff = null;
		List<DataRow> rowList = new ArrayList<DataRow>();
		for (ProcessData data : pcmlDoc.getPCML().getProcessChainData().getProcessDataArray()) {
			ProcessNode processNode = processNodes.get(data.getRef());
			double time = data.getTime();
			if (timeDiff == null)
				timeDiff = time;
			time = time - timeDiff;
			Map<String, NameAndDbId> columnList = PCMLDataTable.createColumnList(data.getDataTable().getColumnList());
			NameAndDbId c0 = columnList.get("c0");
			NameAndDbId timeColumn = (c0 != null && c0.getName().startsWith(AttributeUtilities.TIME)) ? c0 : null;
			double curTime = 0;
			for (Row row : data.getDataTable().getInlineTable().getRowArray()) {
				XmlCursor cursor = row.newCursor();
				cursor.toFirstChild();
				Map<NameAndDbId, String> rowData = new HashMap<NameAndDbId, String>();
				for (int i = 0; i < columnList.size(); i++) {
					NameAndDbId column = columnList.get(cursor.getName().getLocalPart());
					if (timeColumn == null || !timeColumn.equals(column))
						rowData.put(column, cursor.getTextValue());
					else
						curTime = (cursor.getTextValue() != null && !cursor.getTextValue().equalsIgnoreCase("null"))
								? Double.valueOf(cursor.getTextValue()) : 0.0;
					cursor.toNextSibling();
				}
				cursor.dispose();
				double theTime = timeColumn != null ? curTime : time;
				DataCell[] cells = PCMLDataTable.createDataCells(theTime, rowData, processNode, columns);
				DataRow dataRow = new DefaultRow(theTime + "_" + processNode.getId(), cells); // RowKey.createRowKey(counter)
				rowList.add(dataRow);

				if (timeColumn == null) {
					// increment time by the time step of the process node
					double stepWidth = processNode.getParameters().getDuration()
							/ processNode.getParameters().getNumberComputations();
					time = time + stepWidth;
				}
			}
		}
		return createChart(spec, rowList, null, usedParameters, xUnits, equidistantProcesses, plotLines, plotPoints);
	}

	public JFreeChart createChart(DataTable table, List<String> usedParameters, String xUnits,
			boolean equidistantProcesses, boolean plotLines, boolean plotPoints) {
		return createChart(table.getDataTableSpec(), null, table, usedParameters, xUnits, equidistantProcesses, plotLines, plotPoints);
	}

	private Iterator<DataRow> getIterator(List<DataRow> rowList, DataTable table) {
		if (rowList != null)
			return rowList.iterator();
		else if (table != null)
			return table.iterator();
		else
			return null;
	}

	private JFreeChart createChart(DataTableSpec spec, List<DataRow> rowList, DataTable table,
			List<String> usedParameters, String xUnits, boolean equidistantProcesses, boolean plotLines, boolean plotPoints) {
		LinkedList<XYDataset> dataSets = new LinkedList<XYDataset>();
		LinkedList<XYDataset> equiDataSets = new LinkedList<XYDataset>();
		int timeIndex = spec.findColumnIndex(AttributeUtilities.TIME + " [s]");
		int processIndex = spec.findColumnIndex("process");
		int processIdIndex = spec.findColumnIndex("process id");

		LinkedList<Point2D.Double> ranges = new LinkedList<Point2D.Double>();
		LinkedList<String> processNames = new LinkedList<String>();
		double processStart = Double.NaN;
		double time = Double.NaN;
		boolean pFilled = false;

		for (String param : usedParameters) {
			int paramIndex = spec.findColumnIndex(param);
			if (paramIndex >= 0) {
				LinkedHashMap<Double, Double> timeSeries = new LinkedHashMap<Double, Double>();
				boolean paramDone = false;
				String process = null, lastP = null;

				double ds = FormulaEvaluator.getSeconds(xUnits);
				boolean param2Cumulate = !JCheckboxWithObject.isTemperature(param) && !JCheckboxWithObject.isPH(param)
						&& !JCheckboxWithObject.isAW(param) && !JCheckboxWithObject.isPressure(param)
						&& !JCheckboxWithObject.isAgent(param) && !JCheckboxWithObject.isWithUnit(param);
				Iterator<DataRow> iter = getIterator(rowList, table);
				while (iter.hasNext()) {
					DataRow row = iter.next();
					DataCell timeCell = row.getCell(timeIndex);
					DataCell paramCell = row.getCell(paramIndex);
					DataCell processCell = row.getCell(processIndex);
					DataCell processIdCell = row.getCell(processIdIndex);

					if (!timeCell.isMissing()) {
						String p = ((StringCell) processIdCell).getStringValue();
						boolean newP = false;
						if (!p.equals(process)) {
							newP = true;
							if (process != null) {
								if (!pFilled) {
									ranges.add(new Point2D.Double(processStart, time));
									processNames.add(lastP);
								}
							}
							double newPS = ((DoubleCell) timeCell).getDoubleValue() / ds;
							if (newPS <= processStart && timeSeries.size() > 0) {
								// processflow splits into parallel processes
								if (!param2Cumulate) {
									addDataset(dataSets, paramDone ? "" : param, timeSeries);
									addDataset(equiDataSets, paramDone ? "" : param, getEquiSeries(timeSeries, ranges));
									timeSeries = new LinkedHashMap<Double, Double>();
									paramDone = true;
								}
							}
							processStart = newPS;
							process = p;
							lastP = ((StringCell) processCell).getStringValue();
						}
						time = ((DoubleCell) timeCell).getDoubleValue() / ds;

						if (!paramCell.isMissing()) {
							Double val = ((DoubleCell) paramCell).getDoubleValue();
							if (param2Cumulate && !newP) {
								if (timeSeries.containsKey(time))
									timeSeries.put(time, val + timeSeries.get(time));
								else
									timeSeries.put(time, val);
							} else if (!timeSeries.containsKey(time)) {
								// System.err.println(param + "\t" + lastP +
								// "\t" + time + "\t" + val);
								timeSeries.put(time, val);
							} else if (newP) {
								// System.err.println(param + "\t" + lastP +
								// "\t" + time + "\t" + val);
								timeSeries.put(time + 0.00001, val);
							}
						}
					}
				}
				if (!pFilled) {
					ranges.add(new Point2D.Double(processStart, time));
					processNames.add(lastP);
					pFilled = true;
				}
				if (timeSeries.size() > 0) {
					addDataset(dataSets, paramDone ? "" : param, timeSeries);
					addDataset(equiDataSets, paramDone ? "" : param, getEquiSeries(timeSeries, ranges));
				}
			}
		}

		if (processLegend == null || processLegend.size() == 0) {
			processLegend = new ArrayList<ProcessLegendElement>();
			List<Color> colorList = new ColorAndShapeCreator(ranges.size()).getColorList();
			for (int i = 0; i < processNames.size(); i++) {
				String pn = processNames.get(i);
				ProcessLegendElement ple = new ProcessLegendElement(pn, colorList.get(i), vui);
				processLegend.add(ple);
			}
		}

		MyXAxis xaxis = new MyXAxis(AttributeUtilities.TIME + " [" + xUnits + "]", ranges, processNames,
				equidistantProcesses);
		XYPlot plot = new XYPlot(null, xaxis, null, null);
		plot.setDomainPannable(true);
		plot.setRangePannable(true);

		// ColorAndShapeCreator colorCreator = new
		// ColorAndShapeCreator(usedParameters.size());
		int lastI = 0;
		StandardXYItemRenderer renderer = null;
		Color lastBG = null;
		String lastParam = null;
		List<NumberAxis> matrixAxes = new ArrayList<NumberAxis>();
		List<NumberAxis> agentAxes = new ArrayList<NumberAxis>();
		if (equidistantProcesses)
			dataSets = equiDataSets;
		for (int i = 0; i < dataSets.size(); i++) {
			String param = dataSets.get(i).getSeriesKey(0).toString();
			Color color = getColor(param, lastBG, lastParam);
			lastBG = color;
			lastParam = param;
			plot.setDataset(i, dataSets.get(i));
			if (!param.isEmpty()) {
				lastI = i;
				// Only plots points (no lines)
				int rendererType;
				if (plotPoints && plotLines) {
					rendererType = StandardXYItemRenderer.SHAPES_AND_LINES;
				} else if (plotPoints && !plotLines) {
					rendererType = StandardXYItemRenderer.SHAPES;
				} else if (!plotPoints && plotLines) {
					rendererType = StandardXYItemRenderer.LINES;
				} else {
					rendererType = 0;
				}
				renderer = new StandardXYItemRenderer(rendererType);
				renderer.setSeriesPaint(0, color);
				plot.setRenderer(i, renderer);
				NumberAxis rangeAxis = getNumberAxis(param, color);
				plot.setRangeAxis(i, rangeAxis);
				plot.mapDatasetToRangeAxis(i, i);
				if (i > 0) {
					double diffY = Math.random() * (rangeAxis.getUpperBound() - rangeAxis.getLowerBound()) / 5;
					rangeAxis.setRange(rangeAxis.getLowerBound() - diffY, rangeAxis.getUpperBound() + diffY);
				}
				if (JCheckboxWithObject.isAgent(param))
					agentAxes.add(rangeAxis);
				else if (JCheckboxWithObject.isMatrix(param))
					matrixAxes.add(rangeAxis);
			} else {
				plot.setRenderer(i, renderer);
				plot.mapDatasetToRangeAxis(i, lastI);
			}
		}
		setBounds(agentAxes);
		setBounds(matrixAxes);

		JFreeChart jfc = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		return jfc;
	}

	private void setBounds(List<NumberAxis> axes) {
		Double lb = null;
		Double ub = null;
		for (NumberAxis na : axes) {
			if (lb == null || na.getLowerBound() < lb)
				lb = na.getLowerBound();
			if (ub == null || na.getUpperBound() > ub)
				ub = na.getUpperBound();
		}
		if (lb != null && ub != null) {
			for (NumberAxis na : axes) {
				na.setRange(lb, ub);
			}
		}
	}

	private Color getColor(String param, Color lastBG, String lastParam) {
		Color bg = Color.WHITE;
		@SuppressWarnings("unused")
		Color fg = Color.BLACK;
		if (JCheckboxWithObject.isTemperature(param)) {
			bg = Color.BLUE;
			fg = Color.WHITE;
		} else if (JCheckboxWithObject.isAW(param)) {
			bg = new Color(160, 82, 45);
			fg = Color.WHITE;
		} else if (JCheckboxWithObject.isPH(param)) {
			bg = Color.GREEN;
		} else if (JCheckboxWithObject.isPressure(param)) {
			bg = Color.BLACK;
		} else if (JCheckboxWithObject.isAgent(param)) {
			if (lastParam != null && JCheckboxWithObject.isAgent(lastParam))
				bg = lastBG.darker();
			else
				bg = Color.RED;
			fg = Color.WHITE;
		} else if (JCheckboxWithObject.isMatrix(param)) {
			if (lastParam != null && JCheckboxWithObject.isMatrix(lastParam))
				bg = lastBG.brighter();
			else
				bg = Color.DARK_GRAY;
			fg = Color.WHITE;
		} else if (JCheckboxWithObject.isWithUnit(param)) {
			if (lastParam != null && JCheckboxWithObject.isWithUnit(lastParam))
				bg = lastBG.brighter();
			else
				bg = Color.LIGHT_GRAY;
			fg = Color.BLACK;
		}
		return bg;
	}

	private NumberAxis getNumberAxis(String param, Paint axisColor) {
		NumberAxis rangeAxis = new NumberAxis(param);
		// set colors
		if (axisColor != null) {
			rangeAxis.setAxisLinePaint(axisColor);
			rangeAxis.setLabelPaint(axisColor);
			rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
			// rangeAxis.setTickLabelPaint(axisColor);
			// rangeAxis.setTickMarkPaint(axisColor);
		}
		return rangeAxis;
	}

	private void addDataset(List<XYDataset> dataSets, String param, LinkedHashMap<Double, Double> timeSeries) {
		int n = timeSeries.size();
		double[][] data = new double[2][n];

		int i = 0;
		for (Double time : timeSeries.keySet()) {
			data[0][i] = time;
			data[1][i] = timeSeries.get(time);
			i++;
		}

		DefaultXYDataset dataSet = new DefaultXYDataset();

		dataSet.addSeries(param, data);
		dataSets.add(dataSet);
	}

	private LinkedHashMap<Double, Double> getEquiSeries(LinkedHashMap<Double, Double> timeSeries,
			LinkedList<Point2D.Double> ranges) {
		LinkedHashMap<Double, Double> result = new LinkedHashMap<Double, Double>();
		for (Double time : timeSeries.keySet()) {
			Double value = timeSeries.get(time);
			int p2dLfd = 0;
			for (Point2D.Double p2d : ranges) {
				if (time >= p2d.x && time <= p2d.y) {
					result.put(p2dLfd + (time - p2d.x) / (p2d.y - p2d.x), value);
					/*
					 * if (processLegend != null && processLegend.size() >
					 * p2dLfd) { if (processLegend.get(p2dLfd).isSelected()) {
					 * //System.err.println(p2dLfd + "\t" +
					 * processLegend.get(p2dLfd).isSelected()); } }
					 */
				}
				p2dLfd++;
			}
		}
		return result;
	}
}
