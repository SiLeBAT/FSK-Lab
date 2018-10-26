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
package de.bund.bfr.knime.pmm.primarymodelviewandselect;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.units.Categories;

/**
 * <code>NodeDialog</code> for the "PrimaryModelViewAndSelect" Node.
 * 
 * @author Christian Thoens
 */
public class PrimaryModelViewAndSelectNodeDialog extends
		DataAwareNodeDialogPane implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener,
		ChartCreator.ZoomListener {

	private TableReader reader;
	private SettingsHelper set;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;

	/**
	 * New pane for configuring the PrimaryModelViewAndSelect node.
	 */
	protected PrimaryModelViewAndSelectNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel, false);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		boolean schemaContainsData;

		if (SchemaFactory.createDataSchema().conforms(input[0])) {
			schemaContainsData = true;
		} else {
			schemaContainsData = false;
		}

		set = new SettingsHelper();
		set.loadSettings(settings);
		reader = new TableReader(input[0], schemaContainsData);
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
		selectionPanel.selectFirstRow();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		set.setSelectedIDs(selectionPanel.getSelectedIDs());
		set.setColors(selectionPanel.getColors());
		set.setShapes(selectionPanel.getShapes());
		set.setStandardVisibleColumns(false);
		set.setVisibleColumns(selectionPanel.getVisibleColumns());
		set.setColumnWidths(selectionPanel.getColumnWidths());
		set.setSelectAllIDs(false);
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
		set.setModelFilter(selectionPanel.getFilter(Model1Schema.FORMULA));
		set.setDataFilter(selectionPanel.getFilter(AttributeUtilities.DATAID));
		set.setFittedFilter(selectionPanel.getFilter(ChartConstants.STATUS));
		set.saveSettings(settings);
	}

	private JComponent createMainComponent() {
		Map<String, List<String>> categories = new LinkedHashMap<>();
		Map<String, String> units = new LinkedHashMap<>();
		Map<String, List<Double>> paramsX = new LinkedHashMap<>();

		categories.put(AttributeUtilities.TIME,
				Arrays.asList(Categories.getTime()));
		categories.put(AttributeUtilities.CONCENTRATION,
				Categories.getConcentrations());
		units.put(AttributeUtilities.TIME, Categories.getTimeCategory()
				.getStandardUnit());
		units.put(AttributeUtilities.CONCENTRATION, Categories
				.getConcentrationCategories().get(0).getStandardUnit());
		paramsX.put(AttributeUtilities.TIME, new ArrayList<Double>());

		if (set.isSelectAllIDs()) {
			set.setSelectedIDs(reader.getIds());
		}

		if (set.isStandardVisibleColumns()) {
			set.setVisibleColumns(reader.getStandardVisibleColumns());
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.NO_PARAMETER_INPUT,
				true, null, false);
		configPanel.setParameters(AttributeUtilities.CONCENTRATION, paramsX,
				null, null, categories, units, null);
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
		configPanel.setUnitX(set.getUnitX());
		configPanel.setUnitY(set.getUnitY());
		configPanel.setTransformX(set.getTransformX());
		configPanel.setTransformY(set.getTransformY());
		configPanel.addConfigListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), false,
				reader.getStringColumns(), reader.getDoubleColumns(),
				reader.getConditions(), reader.getConditionValues(), null,
				null, reader.getConditionUnits(), set.getVisibleColumns(),
				reader.getFilterableStringColumns(), reader.getData(),
				reader.getParameterData(), null, reader.getFormulas());
		selectionPanel.setColors(set.getColors());
		selectionPanel.setShapes(set.getShapes());
		selectionPanel.setColumnWidths(set.getColumnWidths());
		selectionPanel.setFilter(Model1Schema.FORMULA, set.getModelFilter());
		selectionPanel
				.setFilter(AttributeUtilities.DATAID, set.getDataFilter());
		selectionPanel.setFilter(ChartConstants.STATUS, set.getFittedFilter());
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		chartCreator.addZoomListener(this);

		if (set.getSelectedIDs() != null) {
			selectionPanel.setSelectedIDs(set.getSelectedIDs());
		}

		return new ChartAllPanel(chartCreator, selectionPanel, configPanel);
	}

	private void createChart() {
		String id = null;

		if (configPanel.isDisplayFocusedRow()) {
			id = selectionPanel.getFocusedID();
		} else if (!selectionPanel.getSelectedIDs().isEmpty()) {
			id = selectionPanel.getSelectedIDs().get(0);
		}

		if (id != null) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (configPanel.getUnitX() == null) {
				configPanel.removeConfigListener(this);
				configPanel.setUnitX(plotable.getUnits().get(
						AttributeUtilities.TIME));
				configPanel.addConfigListener(this);
			}

			if (configPanel.getUnitY() == null) {
				configPanel.removeConfigListener(this);
				configPanel.setUnitY(plotable.getUnits().get(
						AttributeUtilities.CONCENTRATION));
				configPanel.addConfigListener(this);
			}
		}

		chartCreator.setParamX(configPanel.getParamX());
		chartCreator.setParamY(configPanel.getParamY());
		chartCreator.setUnitX(configPanel.getUnitX());
		chartCreator.setUnitY(configPanel.getUnitY());
		chartCreator.setTransformX(configPanel.getTransformX());
		chartCreator.setTransformY(configPanel.getTransformY());
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
		chartCreator.setColors(selectionPanel.getColors());
		chartCreator.setShapes(selectionPanel.getShapes());

		if (configPanel.isDisplayFocusedRow()) {
			chartCreator.createChart(selectionPanel.getFocusedID());
		} else {
			chartCreator.createChart(selectionPanel.getSelectedIDs());
		}
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
	public void configChanged() {
		createChart();
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
