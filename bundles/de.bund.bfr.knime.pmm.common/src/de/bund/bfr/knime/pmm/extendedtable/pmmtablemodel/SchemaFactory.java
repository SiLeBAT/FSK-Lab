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
package de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel;

import org.knime.core.data.DataTableSpec;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class SchemaFactory {
	
	public static KnimeSchema createDataSchema() {
		return new TimeSeriesSchema();
	}
	
	public static KnimeSchema createM1Schema() {
		return new Model1Schema();
	}
	
	public static KnimeSchema createM2Schema() {
		return new Model2Schema();
	}
	
	public static KnimeSchema createM12Schema() {
		return new KnimeSchema(createM1Schema(), createM2Schema());
	}
	
	public static KnimeSchema createM1DataSchema() {
		return new KnimeSchema(createM1Schema(), createDataSchema());
	}
	
	public static KnimeSchema createM12DataSchema() {
		return new KnimeSchema(createM12Schema(), createDataSchema());
	}
	
	public static boolean conformsDataSchema(DataTableSpec spec) {
		return createDataSchema().conforms(spec);
	}
	
	public static boolean conformsM1Schema(DataTableSpec spec) {
		return createM1Schema().conforms(spec);
	}
	
	public static boolean conformsM2Schema(DataTableSpec spec) {
		return createM2Schema().conforms(spec);
	}
	
	public static boolean conformsM12Schema(DataTableSpec spec) {
		return createM12DataSchema().conforms(spec);
	}
	
	public static boolean conformsM1DataSchema(DataTableSpec spec) {
		return createM1DataSchema().conforms(spec);
	}
	
	public static boolean conformsM12DataSchema(DataTableSpec spec) {
		return createM12DataSchema().conforms(spec);
	}
	
	public static boolean isDataSchema(KnimeSchema schema) {
		KnimeSchema dataSchema = createDataSchema();
		return dataSchema.conforms(schema.createSpec()) && schema.conforms(dataSchema.createSpec());
	}
	
	public static boolean isM1Schema(KnimeSchema schema) {
		KnimeSchema m1Schema = createM1Schema();
		return m1Schema.conforms(schema.createSpec()) && schema.conforms(m1Schema.createSpec());
	}
	
	public static boolean isM2Schema(KnimeSchema schema) {
		KnimeSchema m2Schema = createM2Schema();
		return m2Schema.conforms(schema.createSpec()) && schema.conforms(m2Schema.createSpec());
	}
	
	public static boolean isM12Schema(KnimeSchema schema) {
		KnimeSchema m12Schema = createM12Schema();
		return m12Schema.conforms(schema.createSpec()) && schema.conforms(m12Schema.createSpec());
	}

	public static boolean isM1DataSchema(KnimeSchema schema) {
		KnimeSchema m1DataSchema = createM1DataSchema();
		return m1DataSchema.conforms(schema.createSpec()) && schema.conforms(m1DataSchema.createSpec());
	}
	
	public static boolean isM12DataSchema(KnimeSchema schema) {
		KnimeSchema m12DataSchema = createM12DataSchema();
		return m12DataSchema.conforms(schema.createSpec()) && schema.conforms(m12DataSchema.createSpec());
	}

}
