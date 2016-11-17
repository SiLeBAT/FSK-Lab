package de.bund.bfr.knime.fsklab.nodes.ui;

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

import com.google.common.base.Strings;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.Software;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class MetaDataPane extends JScrollPane {

	private static final long serialVersionUID = -3455056721681075796L;

	private static final NodeLogger LOGGER = NodeLogger.getLogger(MetaDataPane.class);

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

	private static enum Row {

		MODEL_NAME("Model name"),
		MODEL_ID("Model id"),
		MODEL_LINK("Model link"),
		ORGANISM_NAME("Organism name"),
		ORGANISM_DETAIL("Organism detail"),
		ENVIRONMENT_NAME("Environment name"),
		ENVIRONMENT_DETAIL("Environment detail"),
		CREATOR("Creator"),
		FAMILY_NAME("Family name"),
		CONTACT("Contact"),
		SOFTWARE("Software"),
		REFERENCE_DESCRIPTION("Reference description"),
		REFERENCE_DESCRIPTION_LINK("Reference description link"),
		CREATED_DATE("Created date"),
		MODIFIED_DATE("Modified date"),
		RIGHTS("Rights"),
		NOTES("Notes"),
		CURATION_STATUS("Curation status"),
		TYPE("Type"),
		SUBJECT("Subject"),
		FOOD_PROCESS("Food process"),
		DEPENDENT_VARIABLE("Dependent variable"),
		DEPENDENT_VARIABLE_UNIT("Dependent variable unit"),
		DEPENDENT_VARIABLE_TYPE("Dependent variable type"),
		DEPENDENT_VARIABLE_MIN("Dependent variable min"),
		DEPENDENT_VARIABLE_MAX("Dependent variable max"),
		INDEPENDENT_VARIABLE("Independent variable"),
		INDEPENDENT_VARIABLE_UNIT("Independent variable unit"),
		INDEPENDENT_VARIABLE_TYPE("Independent variable type"),
		INDEPENDENT_VARIABLE_MIN("Independent variable min"),
		INDEPENDENT_VARIABLE_MAX("Independent variable max"),
		INDEPENDENT_VARIABLE_VALUE("Independent variable value"),
		HAS_DATA("Has data");

		String name;

		Row(String name) {
			this.name = name;
		}
	}

	public final FskMetaData template;

	public MetaDataPane(FskMetaData template, boolean editable) {
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
			Arrays.stream(ModelClass.values()).map(ModelClass::fullName).forEach(subjectComboBox::addItem);
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
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (row == Row.TYPE.ordinal()) {
				editor = new DefaultCellEditor(typeComboBox);
			} else if (row == Row.SUBJECT.ordinal()) {
				editor = new DefaultCellEditor(subjectComboBox);
			} else if (row == Row.SOFTWARE.ordinal()) {
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
			return Row.values().length; // 1 row per field
		}

		@Override
		public Object getValueAt(int row, int col) {
			if (col == 0)
				return Row.values()[row].name;
			else if (col == 1) {
				switch (Row.values()[row]) {
				case MODEL_NAME:
					return template.modelName;
				case MODEL_ID:
					return template.modelId;
				case MODEL_LINK:
					return template.modelLink;
				case ORGANISM_NAME:
					return template.organism;
				case ORGANISM_DETAIL:
					return template.organismDetails;
				case ENVIRONMENT_NAME:
					return template.matrix;
				case ENVIRONMENT_DETAIL:
					return template.matrixDetails;
				case CREATOR:
					return template.creator;
				case FAMILY_NAME:
					return template.familyName;
				case CONTACT:
					return template.contact;
				case SOFTWARE:
					return template.software == null ? "" : template.software.name();
				case REFERENCE_DESCRIPTION:
					return template.referenceDescription;
				case REFERENCE_DESCRIPTION_LINK:
					return template.referenceDescriptionLink == null ? ""
							: template.referenceDescriptionLink.toString();
				case CREATED_DATE:
					return template.createdDate == null ? "" : FskMetaData.dateFormat.format(template.createdDate);
				case MODIFIED_DATE:
					return template.modifiedDate == null ? "" : FskMetaData.dateFormat.format(template.modifiedDate);
				case RIGHTS:
					return template.rights;
				case NOTES:
					return template.notes;
				case CURATION_STATUS:
					return Boolean.toString(template.curated);
				case TYPE:
					return template.type == null ? "" : modelTypeStrings.get(template.type);
				case SUBJECT:
					return template.subject == null ? "" : template.subject.fullName();
				case FOOD_PROCESS:
					return template.foodProcess;
				case DEPENDENT_VARIABLE:
					return template.dependentVariable.name;
				case DEPENDENT_VARIABLE_UNIT:
					return template.dependentVariable.unit;
				case DEPENDENT_VARIABLE_TYPE:
					return template.dependentVariable.type == null ? "" : template.dependentVariable.type.name();
				case DEPENDENT_VARIABLE_MIN:
					return template.dependentVariable.min;
				case DEPENDENT_VARIABLE_MAX:
					return template.dependentVariable.max;
				case INDEPENDENT_VARIABLE:
					return template.independentVariables.stream().map(v -> v.name).collect(Collectors.joining("||"));
				case INDEPENDENT_VARIABLE_UNIT:
					return template.independentVariables.stream().map(v -> v.unit).collect(Collectors.joining("||"));
				case INDEPENDENT_VARIABLE_TYPE:
					return template.independentVariables.stream().map(v -> v.type.name())
							.collect(Collectors.joining("||"));
				case INDEPENDENT_VARIABLE_MIN:
					return template.independentVariables.stream().map(v -> v.min).collect(Collectors.joining("||"));
				case INDEPENDENT_VARIABLE_MAX:
					return template.independentVariables.stream().map(v -> v.max).collect(Collectors.joining("||"));
				case INDEPENDENT_VARIABLE_VALUE:
					return template.independentVariables.stream().map(v -> v.value).collect(Collectors.joining("||"));
				case HAS_DATA:
					return Boolean.toString(template.hasData);
				}
			}

			throw new RuntimeException("Invalid row & col" + row + " " + col);
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

			if (columnIndex == 1) {
				String stringValue = (String) aValue;

				if (rowIndex == Row.MODEL_NAME.ordinal()) {
					template.modelName = stringValue;
				} else if (rowIndex == Row.MODEL_ID.ordinal()) {
					template.modelId = stringValue;
				} else if (rowIndex == Row.MODEL_LINK.ordinal()) {
					template.modelLink = stringValue;
				} else if (rowIndex == Row.ORGANISM_NAME.ordinal()) {
					template.organism = stringValue;
				} else if (rowIndex == Row.ORGANISM_DETAIL.ordinal()) {
					template.organismDetails = stringValue;
				} else if (rowIndex == Row.ENVIRONMENT_NAME.ordinal()) {
					template.matrix = stringValue;
				} else if (rowIndex == Row.ENVIRONMENT_DETAIL.ordinal()) {
					template.matrixDetails = stringValue;
				} else if (rowIndex == Row.CREATOR.ordinal()) {
					template.creator = stringValue;
				} else if (rowIndex == Row.FAMILY_NAME.ordinal()) {
					template.familyName = stringValue;
				} else if (rowIndex == Row.CONTACT.ordinal()) {
					template.contact = stringValue;
				} else if (rowIndex == Row.SOFTWARE.ordinal()) {
					// stringValue is either "" or a FskMetaData.Software string
					template.software = stringValue.isEmpty() ? null : Software.valueOf(stringValue);
				} else if (rowIndex == Row.REFERENCE_DESCRIPTION.ordinal()) {
					template.referenceDescription = stringValue;
				} else if (rowIndex == Row.REFERENCE_DESCRIPTION_LINK.ordinal()) {
					template.referenceDescriptionLink = stringValue;
				} else if (rowIndex == Row.CREATED_DATE.ordinal()) {
					try {
						template.createdDate = FskMetaData.dateFormat.parse(stringValue);
					} catch (ParseException e) {
						LOGGER.warn("Invalid date");
					}
				} else if (rowIndex == Row.MODIFIED_DATE.ordinal()) {
					try {
						template.modifiedDate = FskMetaData.dateFormat.parse(stringValue);
					} catch (ParseException e) {
						LOGGER.warn("Invalid date");
					}
				} else if (rowIndex == Row.RIGHTS.ordinal()) {
					template.rights = stringValue;
				} else if (rowIndex == Row.NOTES.ordinal()) {
					template.notes = stringValue;
				} else if (rowIndex == Row.CURATION_STATUS.ordinal()) {
					template.curated = Boolean.parseBoolean(stringValue);
				} else if (rowIndex == Row.TYPE.ordinal()) {
					if (!stringValue.isEmpty()) {
						template.type = modelTypeStrings.entrySet().stream()
								.filter(entry -> entry.getValue().equals(stringValue)).findFirst().get().getKey();
					}
				} else if (rowIndex == Row.SUBJECT.ordinal()) {
					if (!stringValue.isEmpty()) {
						template.subject = ModelClass.fromName(stringValue);
					}
				} else if (rowIndex == Row.FOOD_PROCESS.ordinal()) {
					template.foodProcess = stringValue;
				} else if (rowIndex == Row.DEPENDENT_VARIABLE.ordinal()) {
					template.dependentVariable.name = stringValue;
				} else if (rowIndex == Row.DEPENDENT_VARIABLE_UNIT.ordinal()) {
					template.dependentVariable.unit = stringValue;
				} else if (rowIndex == Row.DEPENDENT_VARIABLE_TYPE.ordinal()) {
					if (!Strings.isNullOrEmpty(stringValue)) {
						template.dependentVariable.type = DataType.valueOf(stringValue);
					}
				} else if (rowIndex == Row.DEPENDENT_VARIABLE_MIN.ordinal()) {
					template.dependentVariable.min = stringValue;
				} else if (rowIndex == Row.DEPENDENT_VARIABLE_MAX.ordinal()) {
					template.dependentVariable.max = stringValue;
				} else if (rowIndex == Row.INDEPENDENT_VARIABLE.ordinal()) {
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).name = tokens[i];
						}
					}
				} else if (rowIndex == Row.INDEPENDENT_VARIABLE_UNIT.ordinal()) {
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).unit = tokens[i];
						}
					}
				} else if (rowIndex == Row.INDEPENDENT_VARIABLE_TYPE.ordinal()) {
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							DataType dt = DataType.valueOf(tokens[i]);
							template.independentVariables.get(i).type = dt;
						}
					}
				} else if (rowIndex == Row.INDEPENDENT_VARIABLE_MIN.ordinal()) {
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).min = tokens[i];
						}
					}
				} else if (rowIndex == Row.INDEPENDENT_VARIABLE_MAX.ordinal()) {
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).max = tokens[i];
						}
					}
				} else if (rowIndex == Row.INDEPENDENT_VARIABLE_VALUE.ordinal()) {
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).value = tokens[i];
						}
					}
				} else if (rowIndex == Row.HAS_DATA.ordinal()) {
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
