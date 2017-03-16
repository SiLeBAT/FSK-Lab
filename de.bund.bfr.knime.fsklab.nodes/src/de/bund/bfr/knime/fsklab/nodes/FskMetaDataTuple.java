/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Strings;

import de.bund.bfr.pmfml.ModelClass;

public class FskMetaDataTuple implements DataRow {

	public enum Key {
		name,
		id,
		model_link,
		species,
		species_details,
		matrix,
		matrix_details,
		creator,
		family_name,
		contact,
		software,
		reference_description,
		reference_description_link,
		created_date,
		modified_date,
		rights,
		notes,
		curation_status,
		model_type,
		subject,
		food_process,
		depvars,
		depvars_units,
		depvars_types,
		depvars_mins,
		depvars_maxs,
		indepvars,
		indepvars_units,
		indepvars_types,
		indepvars_mins,
		indepvars_maxs,
		indepvars_values,
		has_data
	}

	private DataCell[] cell;
	private final RowKey rowKey;

	public FskMetaDataTuple(final FskMetaData template) {
		cell = new DataCell[Key.values().length];

		cell[Key.name.ordinal()] = new StringCell(Strings.nullToEmpty(template.modelName));
		cell[Key.id.ordinal()] = new StringCell(Strings.nullToEmpty(template.modelId));
		cell[Key.model_link.ordinal()] = new StringCell(
				template.modelLink == null ? "" : template.modelLink.toString());

		cell[Key.species.ordinal()] = new StringCell(Strings.nullToEmpty(template.organism));
		cell[Key.species_details.ordinal()] = new StringCell(Strings.nullToEmpty(template.organismDetails));

		cell[Key.matrix.ordinal()] = new StringCell(Strings.nullToEmpty(template.matrix));
		cell[Key.matrix_details.ordinal()] = new StringCell(Strings.nullToEmpty(template.matrixDetails));

		cell[Key.creator.ordinal()] = new StringCell(Strings.nullToEmpty(template.creator));
		cell[Key.family_name.ordinal()] = new StringCell(Strings.nullToEmpty(template.familyName));
		cell[Key.contact.ordinal()] = new StringCell(Strings.nullToEmpty(template.contact));
		cell[Key.software.ordinal()] = new StringCell(template.software == null ? "" : template.software.name());
		cell[Key.reference_description.ordinal()] = new StringCell(Strings.nullToEmpty(template.referenceDescription));
		cell[Key.reference_description_link.ordinal()] = new StringCell(
				template.referenceDescriptionLink == null ? "" : template.referenceDescriptionLink.toString());
		cell[Key.created_date.ordinal()] = new StringCell(
				template.createdDate == null ? "" : FskMetaData.dateFormat.format(template.createdDate));
		cell[Key.modified_date.ordinal()] = new StringCell(
				template.modifiedDate == null ? "" : FskMetaData.dateFormat.format(template.modifiedDate));
		cell[Key.rights.ordinal()] = new StringCell(Strings.nullToEmpty(template.rights));
		cell[Key.notes.ordinal()] = new StringCell(Strings.nullToEmpty(template.notes));
		cell[Key.curation_status.ordinal()] = new StringCell(Boolean.toString(template.curated));
		cell[Key.model_type.ordinal()] = new StringCell(template.type == null ? "" : template.type.toString());
		cell[Key.subject.ordinal()] = new StringCell(
				template.subject == null ? ModelClass.UNKNOWN.fullName() : template.subject.fullName());
		cell[Key.food_process.ordinal()] = new StringCell(Strings.nullToEmpty(template.foodProcess));

		// Dependent variable
		{
			ArrayList<String> nameList = new ArrayList<>(template.dependentVariables.size());
			ArrayList<String> unitList = new ArrayList<>(template.dependentVariables.size());
			ArrayList<String> typeList = new ArrayList<>(template.dependentVariables.size());
			ArrayList<String> minList = new ArrayList<>(template.dependentVariables.size());
			ArrayList<String> maxList = new ArrayList<>(template.dependentVariables.size());
			
			for (Variable var : template.dependentVariables) {
				nameList.add(var.name);
				unitList.add(var.unit);
				typeList.add(var.type == null ? "" : var.type.name());
				minList.add(var.min);
				maxList.add(var.max);
			}
			
			cell[Key.depvars.ordinal()] = new StringCell(String.join("||", nameList));
			cell[Key.depvars_units.ordinal()] = new StringCell(String.join("||", unitList));
			cell[Key.depvars_types.ordinal()] = new StringCell(String.join("||", typeList));
			cell[Key.depvars_mins.ordinal()] = new StringCell(String.join("||", minList));
			cell[Key.depvars_maxs.ordinal()] = new StringCell(String.join("||", maxList));
		}

		// Independent variables
		{
			ArrayList<String> nameList = new ArrayList<>(template.independentVariables.size());
			ArrayList<String> unitList = new ArrayList<>(template.independentVariables.size());
			ArrayList<String> typeList = new ArrayList<>(template.independentVariables.size());
			ArrayList<String> minList = new ArrayList<>(template.independentVariables.size());
			ArrayList<String> maxList = new ArrayList<>(template.independentVariables.size());
			ArrayList<String> valList = new ArrayList<>(template.independentVariables.size());
			
			for (Variable var : template.independentVariables) {
				nameList.add(var.name);
				unitList.add(var.unit);
				typeList.add(var.type == null ? "" : var.type.name());
				minList.add(var.min);
				maxList.add(var.max);
				valList.add(var.value);
			}
			
			cell[Key.indepvars.ordinal()] = new StringCell(String.join("||", nameList));
			cell[Key.indepvars_units.ordinal()] = new StringCell(String.join("||", unitList));
			cell[Key.indepvars_types.ordinal()] = new StringCell(String.join("||", typeList));
			cell[Key.indepvars_mins.ordinal()] = new StringCell(String.join("||", minList));
			cell[Key.indepvars_maxs.ordinal()] = new StringCell(String.join("||", maxList));
			cell[Key.indepvars_values.ordinal()] = new StringCell(String.join("||", valList));
		}

		cell[Key.has_data.ordinal()] = new StringCell(Boolean.toString(template.hasData));

		rowKey = new RowKey("0");
	}

	public void setCell(int key, String value) {
		cell[key] = new StringCell(value);
	}

	// --- DataRow methods ---

	@Override
	public int getNumCells() {
		return cell.length;
	}

	@Override
	public RowKey getKey() {
		return rowKey;
	}

	@Override
	public DataCell getCell(final int index) {
		return cell[index];
	}

	@Override
	public Iterator<DataCell> iterator() {
		return new MetaDataTupleIterator();
	}

	class MetaDataTupleIterator implements Iterator<DataCell> {

		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < cell.length;
		}

		@Override
		public DataCell next() {
			return cell[i++];
		}
	}

	// other utility methods
	public static DataTableSpec createSpec() {

		int numKeys = Key.values().length;

		String[] names = new String[numKeys];
		names[Key.name.ordinal()] = "Model name";
		names[Key.id.ordinal()] = "Model id";
		names[Key.model_link.ordinal()] = "Model link";
		
		names[Key.species.ordinal()] = "Organism";
		names[Key.species_details.ordinal()] = "Organism details";
		
		names[Key.matrix.ordinal()] = "Environment";
		names[Key.matrix_details.ordinal()] = "Environment details";
		
		names[Key.creator.ordinal()] = "Model creator";
		names[Key.family_name.ordinal()] = "Model family name";
		names[Key.contact.ordinal()] = "Model contact";
		names[Key.software.ordinal()] = "Software";
		names[Key.reference_description.ordinal()] = "Model reference description";
		names[Key.reference_description_link.ordinal()] = "Model reference description link";
		
		names[Key.created_date.ordinal()] = "Created date";
		names[Key.modified_date.ordinal()] = "Modified date";
		
		names[Key.rights.ordinal()] = "Rights";
		names[Key.notes.ordinal()] = "Notes";
		names[Key.curation_status.ordinal()] = "Curation status";
		names[Key.model_type.ordinal()] = "Model type";
		names[Key.subject.ordinal()] = "Subject";
		names[Key.food_process.ordinal()] = "Food process";
		
		names[Key.depvars.ordinal()] = "Dependent variables";
		names[Key.depvars_units.ordinal()] = "Dependent variables units";
		names[Key.depvars_types.ordinal()] = "Dependent variables types";
		names[Key.depvars_mins.ordinal()] = "Dependent variables mins";
		names[Key.depvars_maxs.ordinal()] = "Dependent variables maxs";
		
		names[Key.indepvars.ordinal()] = "Independent variables";
		names[Key.indepvars_units.ordinal()] = "Independent variable units";
		names[Key.indepvars_types.ordinal()] = "Independent variable types";
		names[Key.indepvars_mins.ordinal()] = "Independent variable mins";
		names[Key.indepvars_maxs.ordinal()] = "Independent variable maxs";
		names[Key.indepvars_values.ordinal()] = "Independent variable values";
		
		names[Key.has_data.ordinal()] = "Has data?";

		DataType[] types = new DataType[numKeys];
		Arrays.fill(types, StringCell.TYPE);

		return new DataTableSpec(DataTableSpec.createColumnSpecs(names, types));
	}
}