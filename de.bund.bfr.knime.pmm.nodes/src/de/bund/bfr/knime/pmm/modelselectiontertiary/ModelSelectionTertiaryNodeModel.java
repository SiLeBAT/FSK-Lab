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
package de.bund.bfr.knime.pmm.modelselectiontertiary;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of ModelSelectionTertiary.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelSelectionTertiaryNodeModel extends NodeModel {

	private SettingsHelper set;
	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected ModelSelectionTertiaryNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] {
				BufferedDataTable.TYPE, ImagePortObject.TYPE });
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		TableReader reader = new TableReader(table,
				SchemaFactory.isM12DataSchema(schema));
		List<String> ids;

		if (set.isSelectAllIDs()) {
			ids = reader.getIds();
		} else {
			ids = set.getSelectedIDs();
		}

		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		Set<String> idSet = new LinkedHashSet<>(ids);
		int index = 0;

		for (int i = 0; i < reader.getAllTuples().size(); i++) {
			KnimeTuple tuple = reader.getAllTuples().get(i);
			List<KnimeTuple> usedTuples = reader.getTupleCombinations().get(
					tuple);
			String id = reader.getAllIds().get(i);

			if (idSet.contains(id)) {
				for (KnimeTuple t : usedTuples) {
					container.addRowToTable(t);
				}
			}

			exec.checkCanceled();
			exec.setProgress((double) index
					/ (double) reader.getAllTuples().size(), "");
			index++;
		}

		container.close();

		ChartCreator creator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());

		creator.setParamX(AttributeUtilities.TIME);
		creator.setParamY(AttributeUtilities.CONCENTRATION);
		creator.setColors(set.getColors());
		creator.setShapes(set.getShapes());
		creator.setUseManualRange(set.isManualRange());
		creator.setMinX(set.getMinX());
		creator.setMaxX(set.getMaxX());
		creator.setMinY(set.getMinY());
		creator.setMaxY(set.getMaxY());
		creator.setDrawLines(set.isDrawLines());
		creator.setShowLegend(set.isShowLegend());
		creator.setAddInfoInLegend(set.isAddLegendInfo());
		creator.setUnitX(set.getUnitX());
		creator.setUnitY(set.getUnitY());
		creator.setTransformX(set.getTransformX());
		creator.setTransformY(set.getTransformY());

		ImagePortObject image = ChartUtilities.getImage(creator.getChart(ids),
				set.isExportAsSvg());

		return new PortObject[] { container.getTable(), image };
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
		if (SchemaFactory.createM12DataSchema().conforms(
				(DataTableSpec) inSpecs[0])) {
			schema = SchemaFactory.createM12DataSchema();
		} else if (SchemaFactory.createM12Schema().conforms(
				(DataTableSpec) inSpecs[0])) {
			schema = SchemaFactory.createM12Schema();
		} else {
			throw new InvalidSettingsException("Wrong input!");
		}

		return new PortObjectSpec[] { schema.createSpec(),
				ChartUtilities.getImageSpec(set.isExportAsSvg()) };
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
