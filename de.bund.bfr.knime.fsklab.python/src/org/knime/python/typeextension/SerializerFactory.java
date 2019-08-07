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
 *
 * History
 *   Sep 25, 2014 (Patrick Winter): created
 */
package org.knime.python.typeextension;

import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;

/**
 * Factory creating a {@link Serializer}.
 * 
 * @author Patrick Winter, KNIME AG, Zurich, Switzerland
 *
 * @param <Value>
 *            The value type that can be handled by the serializer.
 */
public abstract class SerializerFactory<Value extends DataValue> {

    private Class<? extends Value> m_value;

    /**
     * Creates the factory.
     * 
     * @param dataType
     *            The value type that is used for compatibility checks
     */
    public SerializerFactory(final Class<? extends Value> dataValue) {
        m_value = dataValue;
    }

    /**
     * @return The value type that this factories serializer is compatible with
     */
    public final Class<? extends Value> getDataValue() {
        return m_value;
    }

    /**
     * Checks if this factories serializer is compatible with the given type.
     * 
     * @param type
     *            The type to check
     * @return true if the serializer is compatible, false otherwise
     */
    public final boolean isCompatible(final DataType type) {
        return type.isCompatible(m_value);
    }

    /**
     * Creates a serializer.
     * 
     * @return The serializer
     */
    public abstract Serializer<? extends Value> createSerializer();

}
