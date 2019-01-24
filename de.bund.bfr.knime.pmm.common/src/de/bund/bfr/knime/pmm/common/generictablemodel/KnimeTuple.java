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
package de.bund.bfr.knime.pmm.common.generictablemodel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLBlobCell;
import org.knime.core.data.xml.XMLCell;

import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;

public class KnimeTuple implements DataRow {

	private KnimeSchema schema;
	private DataCell[] cell;

	public KnimeTuple(KnimeSchema schema) throws PmmException {
		setSchema(schema);
		cell = new DataCell[schema.size()];
		Arrays.fill(cell, CellIO.createMissingCell());
	}

	public KnimeTuple(KnimeSchema schema, DataTableSpec spec, DataRow row) throws PmmException {

		this(schema);

		int i, j;

		for (i = 0; i < schema.size(); i++)
			for (j = 0; j < row.getNumCells(); j++)
				if (schema.getName(i).equals(spec.getColumnSpec(j).getName()))
					cell[i] = row.getCell(j);

		for (i = 0; i < schema.size(); i++) {

			if (cell[i].isMissing())
				continue;

			switch (schema.getType(i)) {

			case KnimeAttribute.TYPE_INT:
				if (!(cell[i] instanceof IntCell))
					throw new PmmException("Expected attribute '" + schema.getName(i) + "' to be IntCell.");
				break;

			case KnimeAttribute.TYPE_DOUBLE:
				if (!(cell[i] instanceof DoubleCell))
					throw new PmmException("Expected attribute '" + schema.getName(i) + "' to be DoubleCell.");
				break;

			case KnimeAttribute.TYPE_XML:
				if (!(cell[i] instanceof StringValue))
					throw new PmmException("Expected attribute '" + schema.getName(i) + "' to be XMLCell.");
				break;

			default:
				if (!(cell[i] instanceof StringCell))
					throw new PmmException("Expected attribute '" + schema.getName(i) + "' to be StringCell.");
			}
		}
	}

	public KnimeTuple(final KnimeSchema commonSchema, final KnimeTuple set1, final KnimeTuple set2)
			throws PmmException {

		this(commonSchema);

		if (!commonSchema.conforms(set1.getSchema()))
			throw new PmmException("Set 1 does not conform common schema.");

		if (!commonSchema.conforms(set2.getSchema()))
			throw new PmmException("Set 2 does not conform common schema.");

		for (int i = 0; i < set1.size(); i++) {
			String name = set1.getName(i);
			setCell(name, set1.getCell(i));
		}

		for (int i = 0; i < set2.size(); i++) {
			String name = set2.getName(i);
			setCell(name, set2.getCell(i));
		}
	}

	public static KnimeTuple merge(final KnimeSchema commonSchema, final KnimeTuple set1, final KnimeTuple set2)
			throws PmmException {
		return new KnimeTuple(commonSchema, set1, set2);
	}

	public Double getDouble(final String attName) throws PmmException {
		return getDouble(getIndex(attName));
	}

	public int getIndex(final String attName) throws PmmException {
		return schema.getIndex(attName);
	}

	public Integer getInt(final String attName) throws PmmException {
		return getInt(getIndex(attName));
	}

	public String getName(final int i) {
		return schema.getName(i);
	}

	public PmmXmlDoc getPmmXml(final String attName) throws PmmException {
		return getPmmXml(getIndex(attName));
	}

	public String getString(final String attName) throws PmmException {
		return getString(getIndex(attName));
	}

	public KnimeSchema getSchema() {
		return schema;
	}

	public boolean isNull(final String attName) throws PmmException {
		return isNull(getIndex(attName));
	}

	public DataCell getCell(final String attName) throws PmmException {
		return cell[getIndex(attName)];
	}

	public void setCell(final String attName, DataCell cell) throws PmmException {
		setCell(getIndex(attName), cell);
	}

	public void setSchema(final KnimeSchema schema) throws PmmException {

		if (schema == null)
			throw new PmmException("Schema must not be null.");

		this.schema = schema;
	}

	public void setValue(final String attName, final Object obj) throws PmmException {
		setValue(getIndex(attName), obj);
	}

	public int size() {
		return schema.size();
	}

	@Override
	public Iterator<DataCell> iterator() {
		return new KnimeTupleIterator(cell);
	}

	@Override
	public int getNumCells() {
		return size();
	}

	@Override
	public RowKey getKey() {
		return new RowKey(String.valueOf(new Random().nextInt()));
	}

	@Override
	public DataCell getCell(int index) {
		return cell[index];
	}

	protected void setCell(final int i, final StringCell c) throws PmmException {

		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_INT:
		case KnimeAttribute.TYPE_DOUBLE:
			throw new PmmException("String cell cannot be put into int/double attributes");

		default:
			cell[i] = c;

		}
	}

	protected void setCell(final int i, final DoubleCell c) throws PmmException {

		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_INT:
			throw new PmmException("Int cell cannot be put into double attribute.");

		case KnimeAttribute.TYPE_DOUBLE:
			cell[i] = c;
			break;

		default:
			throw new PmmException("Only string cells can be put into this attribute.");
		}
	}

	protected void setCell(final int i, final IntCell c) throws PmmException {

		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_INT:
			cell[i] = c;
			break;

		case KnimeAttribute.TYPE_DOUBLE:
			throw new PmmException("Only double cells can be put into this attribute.");

		default:
			throw new PmmException("Only string cells can be put into this attribute.");
		}
	}

	protected void setCell(final int i, final XMLCell c) throws PmmException {
		switch (schema.getType(i)) {
		case KnimeAttribute.TYPE_XML:
			cell[i] = c;
			break;

		default:
			throw new PmmException("Some cells are not allowed for XML Types");
		}
	}

	protected void setCell(final int i, final XMLBlobCell c) throws PmmException {
		switch (schema.getType(i)) {
		case KnimeAttribute.TYPE_XML:
			cell[i] = c;
			break;

		default:
			throw new PmmException("Some cells are not allowed for XML Types");
		}
	}

	protected void setCell(final int i, final DataCell c) throws PmmException {

		if (c instanceof IntCell)
			setCell(i, (IntCell) c);
		else if (c instanceof DoubleCell)
			setCell(i, (DoubleCell) c);
		else if (c instanceof StringCell)
			setCell(i, (StringCell) c);
		else if (c instanceof XMLCell)
			setCell(i, (XMLCell) c);
		else if (c instanceof XMLBlobCell)
			setCell(i, (XMLBlobCell) c);
		else if (c.isMissing())
			cell[i] = CellIO.createMissingCell();
		else
			throw new PmmException("Only Int/Double/String/XML/Missing cells are allowed.");
	}

	private Double getDouble(final int i) throws PmmException {

		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_INT:
			return Double.valueOf(CellIO.getInt(cell[i]));

		case KnimeAttribute.TYPE_DOUBLE:
			return CellIO.getDouble(cell[i]);

		case KnimeAttribute.TYPE_STRING:
			throw new PmmException("Will not cast string to double.");

		default:
			throw new PmmException("Comma separated type cannot be cast to double.");
		}
	}

	private Integer getInt(final int i) throws PmmException {

		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_INT:
			return CellIO.getInt(cell[i]);

		case KnimeAttribute.TYPE_DOUBLE:
			throw new PmmException("Will not cast double to int.");

		case KnimeAttribute.TYPE_STRING:
			throw new PmmException("Will not cast string to double.");

		default:
			throw new PmmException("Comma separated type cannot be cast to int.");
		}
	}

	private PmmXmlDoc getPmmXml(final int i) throws PmmException {
		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_XML:

			if (cell[i] instanceof StringValue || cell[i].isMissing())
				return CellIO.getPmmXml(cell[i]);

		default:
			throw new PmmException("Type cannot be cast to XML.");
		}
	}

	private String getString(final int i) {

		switch (schema.getType(i)) {

		case KnimeAttribute.TYPE_INT:
			return String.valueOf(CellIO.getInt(cell[i]));

		case KnimeAttribute.TYPE_DOUBLE:
			return String.valueOf(CellIO.getDouble(cell[i]));

		default:
			return CellIO.getString(cell[i]);
		}
	}

	private boolean isNull(final int i) {
		return cell[i].isMissing();
	}

	private void setValue(final int i, final Object obj) throws PmmException {
		if (obj == null)
			cell[i] = CellIO.createMissingCell();
		else
			switch (schema.getType(i)) {
			case KnimeAttribute.TYPE_INT:
				if (obj instanceof Integer) {
					cell[i] = CellIO.createCell((Integer) obj);
					break;
				}
				if (obj instanceof String) {
					cell[i] = CellIO.createIntCell((String) obj);
					break;
				}
				throw new PmmException("Value must be integer.");
			case KnimeAttribute.TYPE_DOUBLE:
				if (obj instanceof String) {
					cell[i] = CellIO.createDoubleCell((String) obj);
					break;
				}
				if (obj instanceof Double) {
					cell[i] = CellIO.createCell((Double) obj);
					break;
				}
				throw new PmmException("Value must be Double or parsable String.");
			case KnimeAttribute.TYPE_STRING:
				if (!(obj instanceof String))
					throw new PmmException("Value must be string.");
				cell[i] = CellIO.createCell((String) obj);
				break;
			case KnimeAttribute.TYPE_XML:
				if (obj instanceof PmmXmlDoc) {
					cell[i] = CellIO.createXmlCell((PmmXmlDoc) obj);
					break;
				}
				throw new PmmException("Bad value type");
			default:
				throw new PmmException("Unknown datatype.");
			}
	}

	public class KnimeTupleIterator implements Iterator<DataCell> {

		private int i;
		private DataCell[] cell;

		public KnimeTupleIterator(final DataCell[] cell) {
			i = 0;
			this.cell = cell;
		}

		@Override
		public boolean hasNext() {
			return i < cell.length;
		}

		@Override
		public DataCell next() {
			return cell[i++];
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
	}

}
