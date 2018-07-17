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
package de.bund.bfr.knime.fsklab.nodes.common.ui;

import java.awt.Component;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import org.knime.core.node.NodeLogger;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.Software;
import de.bund.bfr.knime.fsklab.nodes.FskMetaDataFields;
import de.bund.bfr.knime.fsklab.nodes.Variable.DataType;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

/** Pane for deprecated FSK nodes. */
public class MetaDataPane2 extends JScrollPane {

  private static final long serialVersionUID = -3455056721681075796L;

  private static final NodeLogger LOGGER = NodeLogger.getLogger(MetaDataPane2.class);

  private final static Map<ModelType, String> modelTypeStrings;

  static {
    // model type strings
    modelTypeStrings = new LinkedHashMap<>();
    modelTypeStrings.put(ModelType.EXPERIMENTAL_DATA, "Experimental data");
    modelTypeStrings.put(ModelType.PRIMARY_MODEL_WDATA, "Primary model with data");
    modelTypeStrings.put(ModelType.PRIMARY_MODEL_WODATA, "Primary model without data");
    modelTypeStrings.put(ModelType.TWO_STEP_SECONDARY_MODEL, "Two step secondary model");
    modelTypeStrings.put(ModelType.ONE_STEP_SECONDARY_MODEL, "One step secondary model");
    modelTypeStrings.put(ModelType.MANUAL_SECONDARY_MODEL, "Manual secondary model");
    modelTypeStrings.put(ModelType.TWO_STEP_TERTIARY_MODEL, "Two step tertiary model");
    modelTypeStrings.put(ModelType.ONE_STEP_TERTIARY_MODEL, "One step tertiary model");
    modelTypeStrings.put(ModelType.MANUAL_TERTIARY_MODEL, "Manual tertiary model");
  }

  public final FskMetaData template;

  public MetaDataPane2(FskMetaData template, boolean editable) {
    super(new TransposedTable(template, editable));
    this.template = template;
  }

  private static class TransposedTable extends JTable {

    private static final long serialVersionUID = 3577330711178390516L;

    public TransposedTable(FskMetaData template, boolean editable) {
      super(new TableModel2(template, editable));

      // Set columns width (name and value)
      columnModel.getColumn(0).setPreferredWidth(150);
      columnModel.getColumn(1).setPreferredWidth(150);

      setAutoResizeMode(AUTO_RESIZE_OFF);

      // Set cell editor for specific cells (model type and subject)
      columnModel.getColumn(1).setCellEditor(new CustomTableCellEditor());
    }
  }

  public static class CustomTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 3883278971216889101L;
    private TableCellEditor editor;

    private static JComboBox<String> typeComboBox;
    private static JComboBox<String> subjectComboBox;
    private static JComboBox<String> softwareComboBox;

    static {
      typeComboBox = new JComboBox<>();
      modelTypeStrings.values().forEach(typeComboBox::addItem);
      typeComboBox.addItem(""); // Empty string for non defined model
                                // types

      subjectComboBox = new JComboBox<>();
      Arrays.stream(ModelClass.values()).map(ModelClass::fullName)
          .forEach(subjectComboBox::addItem);
      subjectComboBox.addItem(""); // Empty string for non defined model
                                   // class

      softwareComboBox = new JComboBox<>();
      Arrays.stream(FskMetaData.Software.values()).map(FskMetaData.Software::name)
          .forEach(softwareComboBox::addItem);
      softwareComboBox.addItem("");
    }

    @Override
    public Object getCellEditorValue() {
      if (editor != null) {
        return editor.getCellEditorValue();
      }

      return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
        int row, int column) {
      if (row == FskMetaDataFields.type.ordinal()) {
        editor = new DefaultCellEditor(typeComboBox);
      } else if (row == FskMetaDataFields.subject.ordinal()) {
        editor = new DefaultCellEditor(subjectComboBox);
      } else if (row == FskMetaDataFields.software.ordinal()) {
        editor = new DefaultCellEditor(softwareComboBox);
      } else {
        editor = new DefaultCellEditor(new JTextField());
      }

      return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
  }

  private static class TableModel2 extends AbstractTableModel {

    private static final long serialVersionUID = -4031679211807376226L;

    private FskMetaData template;
    private boolean editable;

    public TableModel2(FskMetaData template, boolean editable) {
      this.template = template;
      this.editable = editable;
    }

    @Override
    public int getColumnCount() {
      return 2; // name + value
    }

    @Override
    public int getRowCount() {
      return FskMetaDataFields.values().length; // 1 row per field
    }

    @Override
    public Object getValueAt(int row, int col) {
      if (col == 0)
        return FskMetaDataFields.values()[row].fullname;
      else if (col == 1) {
        switch (FskMetaDataFields.values()[row]) {
          case name:
            return template.modelName;
          case id:
            return template.modelId;
          case model_link:
            return template.modelLink;
          case species:
            return template.organism;
          case species_details:
            return template.organismDetails;
          case matrix:
            return template.matrix;
          case matrix_details:
            return template.matrixDetails;
          case creator:
            return template.creator;
          case family_name:
            return template.familyName;
          case contact:
            return template.contact;
          case software:
            return template.software == null ? "" : template.software.name();
          case reference_description:
            return template.referenceDescription;
          case reference_description_link:
            return template.referenceDescriptionLink == null ? ""
                : template.referenceDescriptionLink.toString();
          case created_date:
            return template.createdDate == null ? ""
                : FskMetaData.dateFormat.format(template.createdDate);
          case modified_date:
            return template.modifiedDate == null ? ""
                : FskMetaData.dateFormat.format(template.modifiedDate);
          case rights:
            return template.rights;
          case notes:
            return template.notes;
          case curation_status:
            return Boolean.toString(template.curated);
          case type:
            return template.type == null ? "" : modelTypeStrings.get(template.type);
          case subject:
            return template.subject == null ? "" : template.subject.fullName();
          case food_process:
            return template.foodProcess;
          case depvars:
            return template.dependentVariables.stream().map(v -> v.name)
                .collect(Collectors.joining("||"));
          case depvars_units:
            return template.dependentVariables.stream().map(v -> v.unit)
                .collect(Collectors.joining("||"));
          case depvars_types:
            return template.dependentVariables.stream()
                .map(v -> v.type == null ? "" : v.type.name()).collect(Collectors.joining("||"));
          case depvars_mins:
            return template.dependentVariables.stream().map(v -> v.min)
                .collect(Collectors.joining("||"));
          case depvars_maxs:
            return template.dependentVariables.stream().map(v -> v.max)
                .collect(Collectors.joining("||"));
          case indepvars:
            return template.independentVariables.stream().map(v -> v.name)
                .collect(Collectors.joining("||"));
          case indepvars_units:
            return template.independentVariables.stream().map(v -> v.unit)
                .collect(Collectors.joining("||"));
          case indepvars_types:
            return template.independentVariables.stream()
                .map(v -> v.type == null ? "" : v.type.name()).collect(Collectors.joining("||"));
          case indepvars_mins:
            return template.independentVariables.stream().map(v -> v.min)
                .collect(Collectors.joining("||"));
          case indepvars_maxs:
            return template.independentVariables.stream().map(v -> v.max)
                .collect(Collectors.joining("||"));
          case indepvars_values:
            return template.independentVariables.stream().map(v -> v.value)
                .collect(Collectors.joining("||"));
          case has_data:
            return Boolean.toString(template.hasData);
        }
      }

      throw new RuntimeException("Invalid row & col" + row + " " + col);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

      if (columnIndex == 1) {
        String stringValue = (String) aValue;

        if (rowIndex == FskMetaDataFields.name.ordinal()) {
          template.modelName = stringValue;
        } else if (rowIndex == FskMetaDataFields.id.ordinal()) {
          template.modelId = stringValue;
        } else if (rowIndex == FskMetaDataFields.model_link.ordinal()) {
          template.modelLink = stringValue;
        } else if (rowIndex == FskMetaDataFields.species.ordinal()) {
          template.organism = stringValue;
        } else if (rowIndex == FskMetaDataFields.species_details.ordinal()) {
          template.organismDetails = stringValue;
        } else if (rowIndex == FskMetaDataFields.matrix.ordinal()) {
          template.matrix = stringValue;
        } else if (rowIndex == FskMetaDataFields.matrix_details.ordinal()) {
          template.matrixDetails = stringValue;
        } else if (rowIndex == FskMetaDataFields.creator.ordinal()) {
          template.creator = stringValue;
        } else if (rowIndex == FskMetaDataFields.family_name.ordinal()) {
          template.familyName = stringValue;
        } else if (rowIndex == FskMetaDataFields.contact.ordinal()) {
          template.contact = stringValue;
        } else if (rowIndex == FskMetaDataFields.software.ordinal()) {
          // stringValue is either "" or a FskMetaData.Software string
          template.software = stringValue.isEmpty() ? null : Software.valueOf(stringValue);
        } else if (rowIndex == FskMetaDataFields.reference_description.ordinal()) {
          template.referenceDescription = stringValue;
        } else if (rowIndex == FskMetaDataFields.reference_description_link.ordinal()) {
          template.referenceDescriptionLink = stringValue;
        } else if (rowIndex == FskMetaDataFields.created_date.ordinal()) {
          try {
            template.createdDate = FskMetaData.dateFormat.parse(stringValue);
          } catch (ParseException e) {
            LOGGER.warn("Invalid date");
          }
        } else if (rowIndex == FskMetaDataFields.modified_date.ordinal()) {
          try {
            template.modifiedDate = FskMetaData.dateFormat.parse(stringValue);
          } catch (ParseException e) {
            LOGGER.warn("Invalid date");
          }
        } else if (rowIndex == FskMetaDataFields.rights.ordinal()) {
          template.rights = stringValue;
        } else if (rowIndex == FskMetaDataFields.notes.ordinal()) {
          template.notes = stringValue;
        } else if (rowIndex == FskMetaDataFields.curation_status.ordinal()) {
          template.curated = Boolean.parseBoolean(stringValue);
        } else if (rowIndex == FskMetaDataFields.type.ordinal()) {
          if (!stringValue.isEmpty()) {
            template.type = modelTypeStrings.entrySet().stream()
                .filter(entry -> entry.getValue().equals(stringValue)).findFirst().get().getKey();
          }
        } else if (rowIndex == FskMetaDataFields.subject.ordinal()) {
          if (!stringValue.isEmpty()) {
            template.subject = ModelClass.fromName(stringValue);
          }
        } else if (rowIndex == FskMetaDataFields.food_process.ordinal()) {
          template.foodProcess = stringValue;
        } else if (rowIndex == FskMetaDataFields.depvars.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.dependentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.dependentVariables.get(i).name = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.depvars_units.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.dependentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.dependentVariables.get(i).unit = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.depvars_types.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.dependentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              DataType dt = DataType.valueOf(tokens[i]);
              template.independentVariables.get(i).type = dt;
            }
          }
        } else if (rowIndex == FskMetaDataFields.depvars_mins.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.dependentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.dependentVariables.get(i).min = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.depvars_maxs.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.dependentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.dependentVariables.get(i).max = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.indepvars.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.independentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.independentVariables.get(i).name = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.indepvars_units.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.independentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.independentVariables.get(i).unit = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.indepvars_types.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.independentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              DataType dt = DataType.valueOf(tokens[i]);
              template.independentVariables.get(i).type = dt;
            }
          }
        } else if (rowIndex == FskMetaDataFields.indepvars_mins.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.independentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.independentVariables.get(i).min = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.indepvars_maxs.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.independentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.independentVariables.get(i).max = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.indepvars_values.ordinal()) {
          String[] tokens = stringValue.split("||");
          if (tokens.length == template.independentVariables.size()) {
            for (int i = 0; i < tokens.length; i++) {
              template.independentVariables.get(i).value = tokens[i];
            }
          }
        } else if (rowIndex == FskMetaDataFields.has_data.ordinal()) {
          template.hasData = Boolean.parseBoolean(stringValue);
        }
      }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return editable;
    }
  }
}
