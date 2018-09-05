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
package de.bund.bfr.knime.pmm.secondarypredictorview;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;

import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of SecondaryPredictorView.
 * 
 * 
 * @author Christian Thoens
 */
public class SecondaryPredictorViewNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected SecondaryPredictorViewNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE },
				new PortType[] { ImagePortObject.TYPE });
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		DataTable table = (DataTable) inObjects[0];
		TableReader reader;

		if (SchemaFactory.createDataSchema().conforms(table)) {
			reader = new TableReader(table, true);
		} else {
			reader = new TableReader(table, false);
		}

		ChartCreator creator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());

		if (set.getSelectedID() != null
				&& reader.getPlotables().get(set.getSelectedID()) != null) {
			Plotable plotable = reader.getPlotables().get(set.getSelectedID());
			Map<String, List<Double>> arguments = new LinkedHashMap<>();

			for (Map.Entry<String, Double> entry : set.getParamXValues()
					.entrySet()) {
				arguments.put(entry.getKey(), Arrays.asList(entry.getValue()));
			}

			plotable.setFunctionArguments(arguments);
			plotable.setSamples(set.getTimeValues());
			creator.setParamX(set.getCurrentParamX());
			creator.setParamY(plotable.getFunctionValue());
			creator.setUseManualRange(set.isManualRange());
			creator.setMinX(set.getMinX());
			creator.setMaxX(set.getMaxX());
			creator.setMinY(set.getMinY());
			creator.setMaxY(set.getMaxY());
			creator.setDrawLines(set.isDrawLines());
			creator.setShowLegend(set.isShowLegend());
			creator.setAddInfoInLegend(set.isAddLegendInfo());
			creator.setShowConfidenceInterval(set.isShowConfidence());
			creator.setUnitX(set.getUnitX());
			creator.setUnitY(set.getUnitY());
			creator.setTransformX(set.getTransformX());
			creator.setTransformY(set.getTransformY());
			creator.setColors(set.getColors());
			creator.setShapes(set.getShapes());
			creator.setInverse(set.isSampleInverse());
		}

		return new PortObject[] { ChartUtilities.getImage(
				creator.getChart(set.getSelectedID()), set.isExportAsSvg()) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		if (!SchemaFactory.createM2Schema()
				.conforms((DataTableSpec) inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input!");
		}

		return new PortObjectSpec[] { ChartUtilities.getImageSpec(set
				.isExportAsSvg()) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		set.saveSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		set.loadSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

}
