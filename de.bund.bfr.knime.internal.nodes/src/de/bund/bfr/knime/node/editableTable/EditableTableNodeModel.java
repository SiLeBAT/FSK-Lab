/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ------------------------------------------------------------------------
 *
 * History
 *   23.04.2014 (Christian Albrecht, KNIME.com AG, Zurich, Switzerland): created
 */
package de.bund.bfr.knime.node.editableTable;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.BufferedDataTableHolder;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

/**
 * @author Miguel de Alba, BfR, Berlin, Germany
 */
public class EditableTableNodeModel
		extends AbstractWizardNodeModel<EditableTableViewRepresentation, EditableTableViewValue>
		implements BufferedDataTableHolder {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(EditableTableNodeModel.class);

	// private JSONDataTable jsonTable;
	private BufferedDataTable m_table;

	/**
	 * @param viewName
	 *            The name of the interactive view
	 */
	protected EditableTableNodeModel(final String viewName) {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE }, viewName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.util.js.editableTable";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveCurrentValue(final NodeSettingsWO content) {
		// Nothing to do.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EditableTableViewRepresentation createEmptyViewRepresentation() {
		return new EditableTableViewRepresentation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EditableTableViewValue createEmptyViewValue() {
		return new EditableTableViewValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHideInWizard() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidationError validateViewValue(final EditableTableViewValue value) {
		return null;
	}

	@Override
	public EditableTableViewValue getViewValue() {
		EditableTableViewValue val = super.getViewValue();
		synchronized (getLock()) {
			if (val.table == null && m_table != null) {
				try {
					val.table = new JSONDataTable(m_table, 1, (int) m_table.size(), null);
				} catch (CanceledExecutionException e) {
					LOGGER.error("Could not create JSON table: " + e.getMessage(), e);
				}
			}
		}

		return val;
	}

	@Override
	protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
			throws CanceledExecutionException {
		BufferedDataTable inTable = (BufferedDataTable) inObjects[0];
		BufferedDataTable outTable;

		synchronized (getLock()) {
			EditableTableViewValue val = getViewValue();

			// if not executed
			if (val.table == null) {
				m_table = inTable;
				val.table = new JSONDataTable(inTable, 1, (int) inTable.size(), exec);
			}

			// Takes modified table from val
			outTable = val.table.createBufferedDataTable(exec);
			// Copies modified table to config
			m_table = outTable;
		}

		exec.setProgress(1);
		return new PortObject[] { outTable };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performReset() {
		m_table = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void useCurrentValueAsDefault() {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		// nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		// Nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		// nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BufferedDataTable[] getInternalTables() {
		return new BufferedDataTable[] { m_table };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInternalTables(final BufferedDataTable[] tables) {
		m_table = tables[0];
	}

	@Override
	public void setHideInWizard(boolean hide) {
	}
}
