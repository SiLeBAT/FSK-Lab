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
import org.apache.commons.lang3.StringUtils;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;
import de.bund.bfr.pmfml.ModelClass;

public class FskMetaDataTuple implements DataRow {

  private DataCell[] cell;
  private final RowKey rowKey;

  public FskMetaDataTuple(final FskMetaData template) {
    cell = new DataCell[FskMetaDataFields.values().length];

    cell[FskMetaDataFields.name.ordinal()] =
        new StringCell(StringUtils.defaultString(template.modelName));
    cell[FskMetaDataFields.id.ordinal()] =
        new StringCell(StringUtils.defaultString(template.modelId));
    cell[FskMetaDataFields.model_link.ordinal()] =
        new StringCell(template.modelLink == null ? "" : template.modelLink.toString());

    cell[FskMetaDataFields.species.ordinal()] =
        new StringCell(StringUtils.defaultString(template.organism));
    cell[FskMetaDataFields.species_details.ordinal()] =
        new StringCell(StringUtils.defaultString(template.organismDetails));

    cell[FskMetaDataFields.matrix.ordinal()] =
        new StringCell(StringUtils.defaultString(template.matrix));
    cell[FskMetaDataFields.matrix_details.ordinal()] =
        new StringCell(StringUtils.defaultString(template.matrixDetails));

    cell[FskMetaDataFields.creator.ordinal()] =
        new StringCell(StringUtils.defaultString(template.creator));
    cell[FskMetaDataFields.family_name.ordinal()] =
        new StringCell(StringUtils.defaultString(template.familyName));
    cell[FskMetaDataFields.contact.ordinal()] =
        new StringCell(StringUtils.defaultString(template.contact));
    cell[FskMetaDataFields.software.ordinal()] =
        new StringCell(template.software == null ? "" : template.software.name());
    cell[FskMetaDataFields.reference_description.ordinal()] =
        new StringCell(StringUtils.defaultString(template.referenceDescription));
    cell[FskMetaDataFields.reference_description_link.ordinal()] =
        new StringCell(template.referenceDescriptionLink == null ? ""
            : template.referenceDescriptionLink.toString());
    cell[FskMetaDataFields.created_date.ordinal()] = new StringCell(
        template.createdDate == null ? "" : FskMetaData.dateFormat.format(template.createdDate));
    cell[FskMetaDataFields.modified_date.ordinal()] = new StringCell(
        template.modifiedDate == null ? "" : FskMetaData.dateFormat.format(template.modifiedDate));
    cell[FskMetaDataFields.rights.ordinal()] =
        new StringCell(StringUtils.defaultString(template.rights));
    cell[FskMetaDataFields.notes.ordinal()] =
        new StringCell(StringUtils.defaultString(template.notes));
    cell[FskMetaDataFields.curation_status.ordinal()] =
        new StringCell(Boolean.toString(template.curated));
    cell[FskMetaDataFields.type.ordinal()] =
        new StringCell(template.type == null ? "" : template.type.toString());
    cell[FskMetaDataFields.subject.ordinal()] = new StringCell(
        template.subject == null ? ModelClass.UNKNOWN.fullName() : template.subject.fullName());
    cell[FskMetaDataFields.food_process.ordinal()] =
        new StringCell(StringUtils.defaultString(template.foodProcess));

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

      cell[FskMetaDataFields.depvars.ordinal()] = new StringCell(String.join("||", nameList));
      cell[FskMetaDataFields.depvars_units.ordinal()] = new StringCell(String.join("||", unitList));
      cell[FskMetaDataFields.depvars_types.ordinal()] = new StringCell(String.join("||", typeList));
      cell[FskMetaDataFields.depvars_mins.ordinal()] = new StringCell(String.join("||", minList));
      cell[FskMetaDataFields.depvars_maxs.ordinal()] = new StringCell(String.join("||", maxList));
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

      cell[FskMetaDataFields.indepvars.ordinal()] = new StringCell(String.join("||", nameList));
      cell[FskMetaDataFields.indepvars_units.ordinal()] =
          new StringCell(String.join("||", unitList));
      cell[FskMetaDataFields.indepvars_types.ordinal()] =
          new StringCell(String.join("||", typeList));
      cell[FskMetaDataFields.indepvars_mins.ordinal()] = new StringCell(String.join("||", minList));
      cell[FskMetaDataFields.indepvars_maxs.ordinal()] = new StringCell(String.join("||", maxList));
      cell[FskMetaDataFields.indepvars_values.ordinal()] =
          new StringCell(String.join("||", valList));
    }

    cell[FskMetaDataFields.has_data.ordinal()] = new StringCell(Boolean.toString(template.hasData));

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
    String[] names =
        Arrays.stream(FskMetaDataFields.values()).map(f -> f.fullname).toArray(String[]::new);
    DataType[] types = new DataType[names.length];
    Arrays.fill(types, StringCell.TYPE);

    return new DataTableSpec(DataTableSpec.createColumnSpecs(names, types));
  }
}
