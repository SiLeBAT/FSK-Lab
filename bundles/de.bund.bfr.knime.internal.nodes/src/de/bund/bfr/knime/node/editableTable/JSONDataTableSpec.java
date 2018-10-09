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
 * ---------------------------------------------------------------------
 *
 * Created on 19.03.2013 by Christian Albrecht, KNIME.com AG, Zurich, Switzerland
 */
package de.bund.bfr.knime.node.editableTable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Vector;

import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgValue;
import org.knime.core.data.BooleanValue;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.MissingCell;
import org.knime.core.data.StringValue;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.date.DateAndTimeValue;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.image.png.PNGImageCell;
import org.knime.core.data.image.png.PNGImageValue;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland
 * @since 2.9
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JSONDataTableSpec {

    /**
     *
     * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland
     */
    public static enum JSTypes {
        BOOLEAN("boolean"),
        NUMBER("number"),
        STRING("string"),
        /** @since 2.10 */
        PNG("png"),
        /** @since 2.10 */
        SVG("svg"),
        /** @since 2.11 */
        DATE_TIME("dateTime"),
        UNDEFINED("undefined");

        private String name;

        JSTypes(final String name) {
            this.name = name;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return getName();
        }
    }

    /**
     * @param colType the column type to determine.
     * @return The corresponding JS column type.
     */
    static JSTypes getJSONType(final DataType colType) {
        JSTypes type;
        if (colType.isCompatible(SvgValue.class)) {
            type = JSTypes.SVG;
        } else if (colType.isCompatible(PNGImageValue.class)) {
            type = JSTypes.PNG;
        } else if (colType.isCompatible(BooleanValue.class)) {
            type = JSTypes.BOOLEAN;
        } else if (colType.isCompatible(DateAndTimeValue.class)) {
            type = JSTypes.DATE_TIME;
        } else if (colType.isCompatible(DoubleValue.class)) {
            type = JSTypes.NUMBER;
        } else if (colType.isCompatible(StringValue.class)) {
            type = JSTypes.STRING;
        } else {
            type = JSTypes.UNDEFINED;
        }

        return type;
    }

    private int m_numColumns;
    private int m_numRows;
    private ArrayList<String> m_colTypes = new ArrayList<String>();
    private ArrayList<String> m_knimeTypes = new ArrayList<String>();
    private ArrayList<String> m_colNames = new ArrayList<String>();

    private int m_numExtensions;
    private ArrayList<String> m_extensionTypes = new ArrayList<String>();
    private ArrayList<String> m_extensionNames = new ArrayList<String>();

    private Vector<LinkedHashSet<Object>> m_possibleValues;
    private Object[] m_minValues;
    private Object[] m_maxValues;

    private String[] m_rowColorValues;

    /**
     * Empty default constructor for bean initialization.
     */
    public JSONDataTableSpec() {
        // empty creator for bean initialization
    }

    /**
     * @param spec the DataTableSpec for this JSONTable
     * @param numRows the number of rows in the DataTable
     *
     */
    public JSONDataTableSpec(final DataTableSpec spec, final int numRows) {
        this(spec, new String[0], numRows);
    }

    /**
     * @param spec the DataTableSpec for this JSONTable
     * @param numRows the number of rows in the DataTable
     *
     */
    public JSONDataTableSpec(final DataTableSpec spec, final String[] excludeColumns, final int numRows) {

        int numColumns = 0;
        ArrayList<String> colNames = new ArrayList<String>();
        ArrayList<String> colTypes = new ArrayList<String>();
        ArrayList<String> orgTypes = new ArrayList<String>();
        for (int i = 0; i < spec.getNumColumns(); i++) {
            String colName = spec.getColumnNames()[i];
            if (!Arrays.asList(excludeColumns).contains(colName)) {
                colNames.add(colName);
                orgTypes.add(spec.getColumnSpec(i).getType().getName());
                DataType colType = spec.getColumnSpec(i).getType();
                colTypes.add(getJSONType(colType).name());
                numColumns++;
            }
        }

        setNumColumns(numColumns);
        setNumRows(numRows);
        setColNames(colNames.toArray(new String[0]));
        setColTypes(colTypes.toArray(new String[0]));
        setKnimeTypes(orgTypes.toArray(new String[0]));
    }

    /**
     * Creates a new {@link DataTableSpec} from the current settings.
     * @return the generated spec
     */
    public DataTableSpec createDataTableSpec() {
        DataColumnSpec[] columns = new DataColumnSpec[m_numColumns];
        for (int i = 0; i < m_numColumns; i++) {
            JSTypes type = JSTypes.valueOf(m_colTypes.get(i));
            DataType dataType = null;
            switch (type) {
                case BOOLEAN:
                    dataType = DataType.getType(BooleanCell.class);
                    break;
                case NUMBER:
                    dataType = DataType.getType(DoubleCell.class);
                    break;
                case DATE_TIME:
                    dataType = DataType.getType(DateAndTimeCell.class);
                    break;
                case STRING:
                    dataType = DataType.getType(StringCell.class);
                    break;
                case SVG:
                    dataType = DataType.getType(SvgCell.class);
                    break;
                case PNG:
                    dataType = DataType.getType(PNGImageCell.class);
                    break;
                default:
                    dataType = DataType.getType(MissingCell.class);
                    break;
            }
            columns[i] = new DataColumnSpecCreator(m_colNames.get(i), dataType).createSpec();
        }
        return new DataTableSpec(columns);
    }

    /**
     * @return the num_columns
     */
    public int getNumColumns() {
        return m_numColumns;
    }

    /**
     * @param num the num_columns to set
     */
    public void setNumColumns(final int num) {
        this.m_numColumns = num;
    }

    /**
     * @return the num_rows
     */
    public int getNumRows() {
        return m_numRows;
    }

    /**
     * @param num the num_rows to set
     */
    public void setNumRows(final int num) {
        this.m_numRows = num;
    }

    /**
     * @return the colNames
     */
    public String[] getColNames() {
        return m_colNames.toArray(new String[0]);
    }

    /**
     * @param names the colNames to set
     */
    public void setColNames(final String[] names) {
        this.m_colNames = new ArrayList<String>();
        this.m_colNames.addAll(Arrays.asList(names));
    }

    /**
     * @return the column types
     */
    public String[] getColTypes() {
        return m_colTypes.toArray(new String[0]);
    }

    /**
     * @param types the types to set
     */
    public void setColTypes(final String[] types) {
        this.m_colTypes = new ArrayList<String>();
        this.m_colTypes.addAll(Arrays.asList(types));
    }

    /**
     * @return the knimeTypes
     */
    public String[] getKnimeTypes() {
        return m_knimeTypes.toArray(new String[0]);
    }

    /**
     * @param knimeTypes the knimeTypes to set
     */
    public void setKnimeTypes(final String[] knimeTypes) {
        m_knimeTypes = new ArrayList<String>();
        m_knimeTypes.addAll(Arrays.asList(knimeTypes));
    }

    /**
     * @return
     */
    public int getNumExtensions() {
        return m_numExtensions;
    }

    /**
     * @param num
     */
    public void setNumExtensions(final int num) {
        this.m_numExtensions = num;
    }

    /**
     * @return
     */
    public String[] getExtensionTypes() {
        return m_extensionTypes.toArray(new String[0]);
    }

    /**
     * @param types
     */
    public void setExtensionTypes(final String[] types) {
        this.m_extensionTypes = new ArrayList<String>();
        this.m_extensionTypes.addAll(Arrays.asList(types));
    }

    /**
     * @return
     */
    public String[] getExtensionNames() {
        return m_extensionNames.toArray(new String[0]);
    }

    /**
     * @param names
     */
    public void setExtensionNames(final String[] names) {
        this.m_extensionNames = new ArrayList<String>();
        this.m_extensionNames.addAll(Arrays.asList(names));
    }

    /**
     * @param extensionName
     * @param dataType
     */
    public void addExtension(final String extensionName, final JSTypes dataType) {
        this.m_numExtensions++;
        this.m_extensionNames.add(extensionName);
        this.m_extensionTypes.add(dataType.name);
    }

    /**
     * @return the possibleValues
     */
    public Vector<LinkedHashSet<Object>> getPossibleValues() {
        return m_possibleValues;
    }

    /**
     * @param possibleValues the m_possibleValues to set
     */
    public void setPossibleValues(final Vector<LinkedHashSet<Object>> possibleValues) {
        m_possibleValues = possibleValues;
    }

    /**
     * @return the minValues
     */
    public Object[] getMinValues() {
        return m_minValues;
    }

    /**
     * @param minValues the m_minValues to set
     */
    public void setMinValues(final Object[] minValues) {
        m_minValues = minValues;
    }

    /**
     * @return the maxValues
     */
    public Object[] getMaxValues() {
        return m_maxValues;
    }

    /**
     * @param maxValues the m_maxValues to set
     */
    public void setMaxValues(final Object[] maxValues) {
        m_maxValues = maxValues;
    }

    /**
     * @return the rowColorValues as hex strings
     * @since 2.10
     */
    public String[] getRowColorValues() {
        return m_rowColorValues;
    }

    /**
     * @param rowColorValues the rowColorValues as hex strings to set
     * @since 2.10
     */
    public void setRowColorValues(final String[] rowColorValues) {
        m_rowColorValues = rowColorValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_colNames == null) ? 0 : m_colNames.hashCode());
        result = prime * result + ((m_colTypes == null) ? 0 : m_colTypes.hashCode());
        result = prime * result + ((m_extensionNames == null) ? 0 : m_extensionNames.hashCode());
        result = prime * result + ((m_extensionTypes == null) ? 0 : m_extensionTypes.hashCode());
        result = prime * result + Arrays.hashCode(m_maxValues);
        result = prime * result + Arrays.hashCode(m_minValues);
        result = prime * result + m_numColumns;
        result = prime * result + m_numExtensions;
        result = prime * result + m_numRows;
        result = prime * result + ((m_possibleValues == null) ? 0 : m_possibleValues.hashCode());
        result = prime * result + Arrays.hashCode(m_rowColorValues);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JSONDataTableSpec other = (JSONDataTableSpec)obj;
        if (m_colNames == null) {
            if (other.m_colNames != null) {
                return false;
            }
        } else if (!m_colNames.equals(other.m_colNames)) {
            return false;
        }
        if (m_colTypes == null) {
            if (other.m_colTypes != null) {
                return false;
            }
        } else if (!m_colTypes.equals(other.m_colTypes)) {
            return false;
        }
        if (m_extensionNames == null) {
            if (other.m_extensionNames != null) {
                return false;
            }
        } else if (!m_extensionNames.equals(other.m_extensionNames)) {
            return false;
        }
        if (m_extensionTypes == null) {
            if (other.m_extensionTypes != null) {
                return false;
            }
        } else if (!m_extensionTypes.equals(other.m_extensionTypes)) {
            return false;
        }
        if (!Arrays.equals(m_maxValues, other.m_maxValues)) {
            return false;
        }
        if (!Arrays.equals(m_minValues, other.m_minValues)) {
            return false;
        }
        if (m_numColumns != other.m_numColumns) {
            return false;
        }
        if (m_numExtensions != other.m_numExtensions) {
            return false;
        }
        if (m_numRows != other.m_numRows) {
            return false;
        }
        if (m_possibleValues == null) {
            if (other.m_possibleValues != null) {
                return false;
            }
        } else if (!m_possibleValues.equals(other.m_possibleValues)) {
            return false;
        }
        if (!Arrays.equals(m_rowColorValues, other.m_rowColorValues)) {
            return false;
        }
        return true;
    }

}
