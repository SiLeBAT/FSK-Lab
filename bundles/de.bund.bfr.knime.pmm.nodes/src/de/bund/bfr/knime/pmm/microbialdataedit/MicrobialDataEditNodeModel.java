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
package de.bund.bfr.knime.pmm.microbialdataedit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of MicrobialDataEdit.
 * 
 * 
 * @author Christian Thoens
 */
public class MicrobialDataEditNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected MicrobialDataEditNodeModel() {
		super(1, 1);
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), inData[0]);
		List<KnimeTuple> tuples = new ArrayList<>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createDataSchema()
						.createSpec());

		for (KnimeTuple tuple : tuples) {
			String combaseID = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			int condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			String id;

			if (combaseID != null) {
				id = combaseID + " (" + condID + ")";
			} else {
				id = condID + "";
			}

			if (set.getAgents().containsKey(id)) {
				PmmXmlDoc agentXml = new PmmXmlDoc();
				AgentXml agent = set.getAgents().get(id);

				if (agent != null) {
					agentXml.add(agent);
				} else {
					agentXml.add(new AgentXml());
				}

				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			}

			if (set.getAgentDetails().containsKey(id)) {
				PmmXmlDoc agentXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_AGENT);

				((AgentXml) agentXml.get(0)).detail = set.getAgentDetails().get(id);

				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			}

			if (set.getMatrices().containsKey(id)) {
				PmmXmlDoc matrixXml = new PmmXmlDoc();
				MatrixXml matrix = set.getMatrices().get(id);

				if (matrix != null) {
					matrixXml.add(matrix);
				} else {
					matrixXml.add(new MatrixXml());
				}

				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
			}

			if (set.getMatrixDetails().containsKey(id)) {
				PmmXmlDoc matrixXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_MATRIX);

				((MatrixXml) matrixXml.get(0)).detail = set.getMatrixDetails()
						.get(id);

				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
			}

			if (set.getComments().containsKey(id)) {
				PmmXmlDoc infoXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_MDINFO);

				((MdInfoXml) infoXml.get(0)).comment = set.getComments().get(
						id);

				tuple.setValue(TimeSeriesSchema.ATT_MDINFO, infoXml);
			}

			if (set.getQualityScores().containsKey(id)) {
				PmmXmlDoc infoXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_MDINFO);

				((MdInfoXml) infoXml.get(0)).qualityScore = set
						.getQualityScores().get(id);

				tuple.setValue(TimeSeriesSchema.ATT_MDINFO, infoXml);
			}

			if (set.getChecks().containsKey(id)) {
				PmmXmlDoc infoXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_MDINFO);

				((MdInfoXml) infoXml.get(0))
						.checked = set.getChecks().get(id);

				tuple.setValue(TimeSeriesSchema.ATT_MDINFO, infoXml);
			}

			if (set.getTimeSeries().containsKey(id)) {
				PmmXmlDoc timeSeriesXml = new PmmXmlDoc();

				timeSeriesXml.getElementSet().addAll(
						set.getTimeSeries().get(id));
				tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
			}

			if (set.getReferences().containsKey(id)) {
				PmmXmlDoc refXml = new PmmXmlDoc();

				refXml.getElementSet().addAll(set.getReferences().get(id));
				tuple.setValue(TimeSeriesSchema.ATT_LITMD, refXml);
			}

			PmmXmlDoc miscXml = new PmmXmlDoc();

			for (int miscID : set.getConditions().keySet()) {
				MiscXml misc = new MiscXml(set.getConditions().get(miscID));

				misc.setValue(set.getConditionValues().get(miscID).get(id));
				misc.setUnit(set.getConditionUnits().get(miscID).get(id));
				misc.setOrigUnit(set.getConditionUnits().get(miscID).get(id));
				miscXml.add(misc);
			}

			for (int miscID : set.getAddedConditions().keySet()) {
				if (set.getConditions().containsKey(miscID)) {
					continue;
				}

				MiscXml misc = new MiscXml(set.getAddedConditions().get(miscID));

				misc.setValue(set.getAddedConditionValues().get(miscID).get(id));
				misc.setUnit(set.getAddedConditionUnits().get(miscID).get(id));
				miscXml.add(misc);
			}

			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXml);
			container.addRowToTable(tuple);
		}

		container.close();

		return new BufferedDataTable[] { container.getTable() };
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (!SchemaFactory.createDataSchema().conforms(inSpecs[0])
				|| SchemaFactory.createM1Schema().conforms(inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input");
		}

		if (set.getTimeSeries().isEmpty()) {
			throw new InvalidSettingsException("Node has to be configured");
		}

		return new DataTableSpec[] { SchemaFactory.createDataSchema()
				.createSpec() };
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
