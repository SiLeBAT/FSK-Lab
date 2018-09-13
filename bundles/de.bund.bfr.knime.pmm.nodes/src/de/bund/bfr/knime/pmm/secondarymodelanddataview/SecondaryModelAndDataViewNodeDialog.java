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
package de.bund.bfr.knime.pmm.secondarymodelanddataview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.knime.core.data.DataTable;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

/**
 * <code>NodeDialog</code> for the "SecondaryModelAndDataView" Node.
 * 
 * @author Christian Thoens
 */
public class SecondaryModelAndDataViewNodeDialog extends
		DataAwareNodeDialogPane implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener,
		ChartCreator.ZoomListener {

	private TableReader reader;
	private SettingsHelper set;
	private boolean containsData;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;

	/**
	 * New pane for configuring the SecondaryModelAndDataView node.
	 */
	protected SecondaryModelAndDataViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel, false);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		set = new SettingsHelper();
		set.loadSettings(settings);

		DataTable table = input[0];

		if (SchemaFactory.createDataSchema().conforms(table)) {
			reader = new TableReader(table, true);

			if (Collections.max(reader.getColorCounts()) == 0) {
				reader = new TableReader(table, false);
				containsData = false;
			} else {
				containsData = true;
			}
		} else {
			reader = new TableReader(table, false);
			containsData = false;
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
		selectionPanel.selectFirstRow();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!selectionPanel.getSelectedIDs().isEmpty()) {
			set.setSelectedID(selectionPanel.getSelectedIDs().get(0));
		} else if (configPanel.isDisplayFocusedRow()) {
			set.setSelectedID(selectionPanel.getFocusedID());
		} else {
			set.setSelectedID(null);
		}

		if (containsData) {
			set.setColors(new LinkedHashMap<String, Color>());
			set.setShapes(new LinkedHashMap<String, Shape>());
			set.setColorLists(selectionPanel.getColorLists());
			set.setShapeLists(selectionPanel.getShapeLists());
		} else {
			set.setColors(selectionPanel.getColors());
			set.setShapes(selectionPanel.getShapes());
			set.setColorLists(new LinkedHashMap<String, List<Color>>());
			set.setShapeLists(new LinkedHashMap<String, List<Shape>>());
		}

		set.setCurrentParamX(configPanel.getParamX());
		set.setParamXValues(configPanel.getParamXValues());
		set.setSelectedValuesX(configPanel.getSelectedValuesX());
		set.setManualRange(configPanel.isUseManualRange());
		set.setMinX(configPanel.getMinX());
		set.setMaxX(configPanel.getMaxX());
		set.setMinY(configPanel.getMinY());
		set.setMaxY(configPanel.getMaxY());
		set.setDrawLines(configPanel.isDrawLines());
		set.setShowLegend(configPanel.isShowLegend());
		set.setAddLegendInfo(configPanel.isAddInfoInLegend());
		set.setDisplayHighlighted(configPanel.isDisplayFocusedRow());
		set.setExportAsSvg(configPanel.isExportAsSvg());
		set.setShowConfidence(configPanel.isShowConfidenceInterval());
		set.setUnitX(configPanel.getUnitX());
		set.setUnitY(configPanel.getUnitY());
		set.setTransformX(configPanel.getTransformX());
		set.setTransformY(configPanel.getTransformY());
		set.setStandardVisibleColumns(false);
		set.setVisibleColumns(selectionPanel.getVisibleColumns());
		set.setColumnWidths(selectionPanel.getColumnWidths());
		set.setFittedFilter(selectionPanel.getFilter(ChartConstants.STATUS));
		set.saveSettings(settings);
	}

	private JComponent createMainComponent() {
		if (set.isStandardVisibleColumns()) {
			set.setVisibleColumns(reader.getStandardVisibleColumns());
		}

		if (containsData) {
			configPanel = new ChartConfigPanel(
					ChartConfigPanel.PARAMETER_BOXES, true, null, true);
			selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
					reader.getStringColumns(), reader.getDoubleColumns(),
					reader.getConditions(), null,
					reader.getConditionMinValues(),
					reader.getConditionMaxValues(), reader.getConditionUnits(),
					set.getVisibleColumns(),
					reader.getFilterableStringColumns(), null,
					reader.getParameterData(), null, reader.getFormulas(),
					reader.getColorCounts());
		} else {
			configPanel = new ChartConfigPanel(
					ChartConfigPanel.PARAMETER_FIELDS, true, null, true);
			selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
					reader.getStringColumns(), reader.getDoubleColumns(),
					reader.getConditions(), null,
					reader.getConditionMinValues(),
					reader.getConditionMaxValues(), reader.getConditionUnits(),
					set.getVisibleColumns(),
					reader.getFilterableStringColumns(), null,
					reader.getParameterData(), null, reader.getFormulas());
		}

		if (set.getSelectedID() != null
				&& reader.getPlotables().get(set.getSelectedID()) != null) {
			Plotable plotable = reader.getPlotables().get(set.getSelectedID());

			configPanel.setParameters(plotable.getFunctionValue(), plotable
					.getPossibleArgumentValues(containsData, !containsData),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					plotable.getCategories(), plotable.getUnits(), null);
			configPanel.setParamX(set.getCurrentParamX());
			configPanel.setUnitX(set.getUnitX());
			configPanel.setUnitY(set.getUnitY());

			if (containsData) {
				configPanel.setSelectedValuesX(set.getSelectedValuesX());
			} else {
				configPanel.setParamXValues(set.getParamXValues());
			}
		}

		if (containsData) {
			selectionPanel.setColorLists(set.getColorLists());
			selectionPanel.setShapeLists(set.getShapeLists());
		} else {
			selectionPanel.setColors(set.getColors());
			selectionPanel.setShapes(set.getShapes());
		}

		configPanel.setUseManualRange(set.isManualRange());
		configPanel.setMinX(set.getMinX());
		configPanel.setMaxX(set.getMaxX());
		configPanel.setMinY(set.getMinY());
		configPanel.setMaxY(set.getMaxY());
		configPanel.setDrawLines(set.isDrawLines());
		configPanel.setShowLegend(set.isShowLegend());
		configPanel.setAddInfoInLegend(set.isAddLegendInfo());
		configPanel.setDisplayFocusedRow(set.isDisplayHighlighted());
		configPanel.setExportAsSvg(set.isExportAsSvg());
		configPanel.setShowConfidenceInterval(set.isShowConfidence());
		configPanel.setTransformX(set.getTransformX());
		configPanel.setTransformY(set.getTransformY());
		configPanel.addConfigListener(this);
		selectionPanel.setFilter(ChartConstants.STATUS, set.getFittedFilter());
		selectionPanel.setColumnWidths(set.getColumnWidths());
		selectionPanel.setSelectedIDs(Arrays.asList(set.getSelectedID()));
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		chartCreator.addZoomListener(this);

		if (!set.isDisplayHighlighted()) {
			createChart();
		}

		return new ChartAllPanel(chartCreator, selectionPanel, configPanel);
	}

	private void createChart() {
		String selectedID = null;

		if (configPanel.isDisplayFocusedRow()) {
			selectedID = selectionPanel.getFocusedID();
		} else {
			if (!selectionPanel.getSelectedIDs().isEmpty()) {
				selectedID = selectionPanel.getSelectedIDs().get(0);
			}
		}

		if (selectedID != null) {
			Plotable plotable = chartCreator.getPlotables().get(selectedID);

			configPanel.setParameters(plotable.getFunctionValue(), plotable
					.getPossibleArgumentValues(containsData, !containsData),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					plotable.getCategories(), plotable.getUnits(), null);
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setUnitX(configPanel.getUnitX());
			chartCreator.setUnitY(configPanel.getUnitY());
			chartCreator.setTransformX(configPanel.getTransformX());
			chartCreator.setTransformY(configPanel.getTransformY());
			plotable.setFunctionArguments(configPanel.getParamsX());

			try {
				plotable.convertToUnit(configPanel.getParamX(), 1.0,
						configPanel.getUnitX());
			} catch (ConvertException e) {
				configPanel.setUnitX(plotable.getUnits().get(
						configPanel.getParamX()));
			}

			try {
				plotable.convertToUnit(configPanel.getParamY(), 1.0,
						configPanel.getUnitY());
			} catch (ConvertException e) {
				configPanel.setUnitY(plotable.getUnits().get(
						configPanel.getParamY()));
			}
		} else {
			configPanel.setParameters(null, null, null, null, null, null, null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setUnitX(null);
			chartCreator.setUnitY(null);
			chartCreator.setTransformY(null);
		}

		if (containsData) {
			chartCreator.setColorLists(selectionPanel.getColorLists());
			chartCreator.setShapeLists(selectionPanel.getShapeLists());
		} else {
			chartCreator.setColors(selectionPanel.getColors());
			chartCreator.setShapes(selectionPanel.getShapes());
		}

		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.setShowConfidenceInterval(configPanel
				.isShowConfidenceInterval());
		chartCreator.createChart(selectedID);
	}

	@Override
	public void configChanged() {
		createChart();
	}

	@Override
	public void selectionChanged() {
		createChart();
	}

	@Override
	public void focusChanged() {
		if (configPanel.isDisplayFocusedRow()) {
			createChart();
		}
	}

	@Override
	public void zoomChanged() {
		configPanel.removeConfigListener(this);
		configPanel.setUseManualRange(true);
		configPanel.setMinX(chartCreator.getMinX());
		configPanel.setMaxX(chartCreator.getMaxX());
		configPanel.setMinY(chartCreator.getMinY());
		configPanel.setMaxY(chartCreator.getMaxY());
		configPanel.addConfigListener(this);
		createChart();
	}

}
