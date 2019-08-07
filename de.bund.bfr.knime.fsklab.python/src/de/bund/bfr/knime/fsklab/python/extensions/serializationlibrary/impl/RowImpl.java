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

import java.util.Iterator;

import de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.interfaces.Cell;
import de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.interfaces.Row;
import de.bund.bfr.knime.fsklab.python.extensions.serializationlibrary.interfaces.TableIterator;

/**
 * An iterable row provided by a {@link TableIterator}.
 *
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */

public class RowImpl implements Row {

    private final String m_key;

    private final Cell[] m_cells;

    /**
     * Constructor.
     *
     * @param rowKey a unique key for identifying the row
     * @param numberCells the number of cells in the row
     *
     */
    public RowImpl(final String rowKey, final int numberCells) {
        m_key = rowKey;
        m_cells = new Cell[numberCells];
    }

    @Override
    public void setCell(final Cell cell, final int index) {
        m_cells[index] = cell;
    }

    @Override
    public int getNumberCells() {
        return m_cells.length;
    }

    @Override
    public String getRowKey() {
        return m_key;
    }

    @Override
    public Iterator<Cell> iterator() {
        return new Iterator<Cell>() {
            int m_index = 0;

            @Override
            public boolean hasNext() {
                return m_index < m_cells.length;
            }

            @Override
            public Cell next() {
                return m_cells[m_index++];
            }
        };
    }

    @Override
    public Cell getCell(final int index) {
        return m_cells[index];
    }

}
