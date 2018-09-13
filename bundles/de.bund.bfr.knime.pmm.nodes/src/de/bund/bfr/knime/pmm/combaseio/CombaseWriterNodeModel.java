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
package de.bund.bfr.knime.pmm.combaseio;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.combaseio.lib.CombaseWriter;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of CombaseWriter.
 * 
 * 
 * @author Jorgen Brandt
 */
public class CombaseWriterNodeModel extends NodeModel {

	protected static final String PARAM_FILENAME = "filename";
	protected static final String PARAM_OVERWRITE = "overwrite";
	protected static final String DEFAULT_FILENAME = "";

	private String filename;
	private boolean overwrite;

	/**
	 * Constructor for the node model.
	 */
	protected CombaseWriterNodeModel() {

		super(1, 0);

		filename = DEFAULT_FILENAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		File f = new File(filename);

		if (f.exists() && !overwrite) {
			throw new IOException(f.getAbsolutePath() + " already exists");
		}

		long n = inData[0].size();

		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		int j = 0;
		CombaseWriter cbw = new CombaseWriter(filename);
		while (reader.hasMoreElements()) {
			exec.setProgress((double) j++ / n);

			KnimeTuple row = reader.nextElement();

			PmmTimeSeries ts = new PmmTimeSeries(row);
			cbw.add(ts);
		}
		cbw.flush();

		return null;
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
		if (filename.isEmpty())
			throw new InvalidSettingsException("Filename must be specified.");
		File f = new File(filename);
		if (f.exists() && overwrite)
			this.setWarningMessage("Selected output file exists and will be overwritten!");
		getInSchema(inSpecs[0]);
		return null;// new DataTableSpec[]{};
	}

	private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
		KnimeSchema result = null;
		String errorMsg = "Unexpected format - Microbial data is not present in the columns of the incoming table";
		KnimeSchema inSchema = new TimeSeriesSchema();
		try {
			if (inSchema.conforms(inSpec)) {
				result = inSchema;
			}
		} catch (PmmException e) {
		}
		if (result == null) {
			throw new InvalidSettingsException(errorMsg);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(PARAM_FILENAME, filename);
		settings.addBoolean(PARAM_OVERWRITE, overwrite);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename = settings.getString(PARAM_FILENAME);
		overwrite = settings.getBoolean(PARAM_OVERWRITE);
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
