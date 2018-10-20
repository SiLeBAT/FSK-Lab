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
package de.bund.bfr.knime.pmm.xlsmodelreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of XLSModelReader.
 * 
 * 
 * @author Christian Thoens
 */
public class XLSModelReaderNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected XLSModelReaderNodeModel() {
		super(0, 1);
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		KnimeTuple modelTuple = new KnimeTuple(set.getModelTuple().getSchema(),
				set.getModelTuple().getSchema().createSpec(), set.getModelTuple());
		Map<String, KnimeTuple> secModelTuples = new LinkedHashMap<>();

		for (String key : set.getSecModelTuples().keySet()) {
			KnimeTuple tuple = set.getSecModelTuples().get(key);

			secModelTuples.put(key, new KnimeTuple(tuple.getSchema(), tuple.getSchema().createSpec(), tuple));
		}

		PmmXmlDoc modelXml = modelTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
		PmmXmlDoc depVar = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
		PmmXmlDoc indepVar = modelTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);

		if (depVar.size() == 1) {
			formula = MathUtilities.replaceVariable(formula, ((DepXml) depVar.get(0)).getName(),
					AttributeUtilities.CONCENTRATION);
			((DepXml) depVar.get(0)).setName(AttributeUtilities.CONCENTRATION);
		}

		if (indepVar.size() == 1) {
			formula = MathUtilities.replaceVariable(formula, ((IndepXml) indepVar.get(0)).getName(),
					AttributeUtilities.TIME);
			((IndepXml) indepVar.get(0)).setName(AttributeUtilities.TIME);
		}

		((CatalogModelXml) modelXml.get(0)).setFormula(formula);
		modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
		modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depVar);
		modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepVar);

		XLSReader xlsReader = new XLSReader();
		List<KnimeTuple> tuples = new ArrayList<>(xlsReader.getModelTuples(KnimeUtils.getFile(set.getFileName()),
				set.getSheetName(), set.getColumnMappings(), set.getAgentColumn(), set.getAgentMappings(),
				set.getMatrixColumn(), set.getMatrixMappings(), modelTuple, set.getModelMappings(),
				set.getModelParamErrors(), set.getModelDepMin(), set.getModelDepMax(), set.getModelDepUnit(),
				set.getModelIndepMin(), set.getModelIndepMax(), set.getModelIndepUnit(), set.getModelRmse(),
				set.getModelR2(), set.getModelAic(), set.getModelDataPoints(), secModelTuples,
				set.getSecModelMappings(), set.getSecModelParamErrors(), set.getSecModelIndepMins(),
				set.getSecModelIndepMaxs(), set.getSecModelIndepCategories(), set.getSecModelIndepUnits(),
				set.getSecModelRmse(), set.getSecModelR2(), set.getSecModelAic(), set.getSecModelDataPoints(),
				set.isPreserveIds(), set.getUsedIds(), set.getSecUsedIds(), set.getGlobalUsedIds()).values());

		for (String warning : xlsReader.getWarnings()) {
			setWarningMessage(warning);
		}

		if (set.getAgentColumn() == null && set.getAgent() != null) {
			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc agentXml = tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT);

				((AgentXml) agentXml.get(0)).id = set.getAgent().id;
				((AgentXml) agentXml.get(0)).name = set.getAgent().name;
				((AgentXml) agentXml.get(0)).dbuuid = set.getAgent().dbuuid;
				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			}
		}

		if (set.getMatrixColumn() == null && set.getMatrix() != null) {
			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc matrixXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX);

				((MatrixXml) matrixXml.get(0)).setId(set.getMatrix().getId());
				((MatrixXml) matrixXml.get(0)).setName(set.getMatrix().getName());
				((MatrixXml) matrixXml.get(0)).setDbuuid(set.getMatrix().getDbuuid());
				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
			}
		}

		PmmXmlDoc literatureXML = new PmmXmlDoc();

		for (LiteratureItem item : set.getLiterature()) {
			literatureXML.add(item);
		}

		for (KnimeTuple tuple : tuples) {
			tuple.setValue(Model1Schema.ATT_EMLIT, literatureXML);
		}

		BufferedDataContainer container;

		if (secModelTuples.isEmpty()) {
			container = exec.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
		} else {
			container = exec.createDataContainer(SchemaFactory.createM12DataSchema().createSpec());
		}

		for (KnimeTuple tuple : tuples) {
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		if (set.getFileName() == null) {
			throw new InvalidSettingsException("");
		}

		KnimeSchema schema;

		if (set.getSecModelTuples().isEmpty()) {
			schema = SchemaFactory.createM1DataSchema();
		} else {
			schema = SchemaFactory.createM12DataSchema();
		}

		return new DataTableSpec[] { schema.createSpec() };
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
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		set.loadSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

}
