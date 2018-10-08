/*******************************************************************************
 * Copyright (c) 2016 German Federal Institute for Risk Assessment (BfR)
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
package de.bund.bfr.knime.util.urlencoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.AbstractCellFactory;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnFilter2;
import org.knime.core.node.streamable.simple.SimpleStreamableFunctionNodeModel;
import org.knime.core.node.util.filter.NameFilterConfiguration.FilterResult;

/**
 * This is the model implementation of UrlEncoder.
 * 
 *
 * @author
 */
public class UrlEncoderNodeModel extends SimpleStreamableFunctionNodeModel {

	protected static final String COLUMNS = "Columns";

	private SettingsModelColumnFilter2 columns;

	/**
	 * Constructor for the node model.
	 */
	@SuppressWarnings("unchecked")
	protected UrlEncoderNodeModel() {
		columns = new SettingsModelColumnFilter2(COLUMNS, StringValue.class);
	}

	@Override
	protected ColumnRearranger createColumnRearranger(DataTableSpec spec) throws InvalidSettingsException {
		FilterResult filteredCols = columns.applyTo(spec);

		if (filteredCols.getIncludes().length == 0) {
			throw new InvalidSettingsException("There are no columns containing double values in the input table!");
		}

		String[] unknownCols = filteredCols.getRemovedFromIncludes();

		if (unknownCols.length == 1) {
			setWarningMessage("Column \"" + unknownCols[0] + "\" is not available.");
		} else if (unknownCols.length > 1) {
			setWarningMessage(unknownCols.length + " selected columns are not available anymore.");
		}

		ColumnRearranger rearranger = new ColumnRearranger(spec);

		for (String col : filteredCols.getIncludes()) {
			rearranger.replace(new AbstractCellFactory(new DataColumnSpecCreator(col, StringCell.TYPE).createSpec()) {

				@Override
				public DataCell[] getCells(DataRow row) {
					DataCell cell = row.getCell(spec.findColumnIndex(col));

					if (cell instanceof StringValue) {
						try {
							return new DataCell[] { new StringCell(URLEncoder
									.encode(((StringValue) cell).getStringValue(), StandardCharsets.UTF_8.name())) };
						} catch (UnsupportedEncodingException e) {
							return new DataCell[] { DataType.getMissingCell() };
						}
					} else {
						return new DataCell[] { DataType.getMissingCell() };
					}
				}
			}, col);
		}

		return rearranger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		columns.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		columns.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		columns.validateSettings(settings);
	}
}
