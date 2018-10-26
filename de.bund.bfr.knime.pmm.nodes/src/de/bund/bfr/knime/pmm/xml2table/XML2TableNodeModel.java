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
package de.bund.bfr.knime.pmm.xml2table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.PmmXmlDoc;

/**
 * This is the model implementation of XML2Table.
 * 
 * 
 * @author BfR
 */
public class XML2TableNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected XML2TableNodeModel() {
		super(1, 1);
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		BufferedDataTable table = inData[0];
		DataTableSpec inSpec = table.getSpec();
		int columnIndex = inSpec.findColumnIndex(set.getColumn());

		int n = 0;

		for (DataRow row : table) {
			PmmXmlDoc xml = createXml(row.getCell(columnIndex));

			if (xml != null) {
				n = Math.max(n, xml.size());
			}
		}

		DataTableSpec outSpec = createSpec(inSpec, n,
				Arrays.asList(set.getXmlElements()));
		BufferedDataContainer container = exec.createDataContainer(outSpec);
		int index = 0;

		for (DataRow row : table) {
			DataCell[] cells = new DataCell[outSpec.getNumColumns()];

			for (String column : inSpec.getColumnNames()) {
				cells[outSpec.findColumnIndex(column)] = row.getCell(inSpec
						.findColumnIndex(column));
			}

			PmmXmlDoc xml = createXml(row.getCell(columnIndex));

			for (int i = 0; i < n; i++) {
				Element e = null;

				if (xml != null && i < xml.size()) {
					e = xml.getElementSet().get(i).toXmlElement();
				}

				for (String element : set.getXmlElements()) {
					String column = createColumnName(element, i, n);
					
					if (e != null) {
						cells[outSpec.findColumnIndex(column)] = new StringCell(
								e.getAttributeValue(element));
					} else {
						cells[outSpec.findColumnIndex(column)] = DataType
								.getMissingCell();
					}
				}
			}

			exec.checkCanceled();
			exec.setProgress((double) index / (double) table.size());
			container.addRowToTable(new DefaultRow(row.getKey(), cells));
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
		// Spec at output is not known in advance. It depends on the XML.
		return null;
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

	protected static PmmXmlDoc createXml(DataCell cell) {
		if (cell instanceof StringValue) {
			try {
				return new PmmXmlDoc(((StringValue) cell).getStringValue());
			} catch (IOException e) {
			} catch (JDOMException e) {
			}
		}

		return null;
	}

	private DataTableSpec createSpec(DataTableSpec spec, int n,
			List<String> elements) {
		List<DataColumnSpec> columns = new ArrayList<>();

		for (DataColumnSpec column : spec) {
			columns.add(column);
		}

		for (int i = 0; i < n; i++) {
			for (String element : elements) {
				columns.add(new DataColumnSpecCreator(createColumnName(element,
						i, n), StringCell.TYPE).createSpec());
			}
		}

		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

	private String createColumnName(String element, int i, int n) {
		String column = set.getColumn() + "_" + element;

		if (n > 1) {
			column += "_" + (i + 1);
		}

		return column;
	}

}
