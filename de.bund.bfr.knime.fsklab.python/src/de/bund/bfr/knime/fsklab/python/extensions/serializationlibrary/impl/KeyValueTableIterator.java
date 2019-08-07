/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 */

package de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.impl;

import de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.interfaces.Row;
import de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.interfaces.TableIterator;
import de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.interfaces.TableSpec;

/**
 * Used for iterating over a table with a single row. Each column can be seen as a Key/Value pair, with the column name
 * being the key and the cell being the value.
 *
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */

public class KeyValueTableIterator implements TableIterator {

    private final TableSpec m_spec;

    private Row m_row;

    /**
     * Constructor.
     *
     * @param spec a python integration specific table spec
     * @param row the row containing the values
     */
    public KeyValueTableIterator(final TableSpec spec, final Row row) {
        m_spec = spec;
        m_row = row;
    }

    @Override
    public Row next() {
        final Row next = m_row;
        m_row = null;
        return next;
    }

    @Override
    public boolean hasNext() {
        return m_row != null;
    }

    @Override
    public int getNumberRemainingRows() {
        return m_row != null ? 1 : 0;
    }

    @Override
    public TableSpec getTableSpec() {
        return m_spec;
    }

}
