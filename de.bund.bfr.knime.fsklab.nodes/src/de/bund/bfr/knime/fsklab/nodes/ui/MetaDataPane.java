package de.bund.bfr.knime.fsklab.nodes.ui;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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

	private static enum Col {
		Model_Name,
		Model_Id,
		Model_Link,
		Organism_Name,
		Organism_Detail,
		Environment_Name,
		Environment_Detail,
		Model_Creator,
		Model_Family_Name,
		Model_Contact,
		Software,
		Model_Reference_Description,
		Model_Reference_Description_Link,
		Model_Created_Date,
		Model_Modified_Date,
		Model_Rights,
		Model_Notes,
		Model_Curation_Status,
		Model_Type,
		Model_Subject,
		Model_Food_Process,
		Dependent_Variable,
		Dependent_Variable_Unit,
		Dependent_Variable_Type,
		Dependent_Variable_Min,
		Dependent_Variable_Max,
		Independent_Variable,
		Independent_Variable_Units,
		Independent_Variable_Types,
		Independent_Variable_Mins,
		Independent_Variable_Maxs,
		Independent_Variable_Values,
		Has_Data
	}

	public final FskMetaData template;

	public MetaDataPane(FskMetaData template, boolean editable) {
		super(new Table(template, editable));
		this.template = template;
	}

	private static class Table extends JTable {
		private static final long serialVersionUID = 8776004658791577404L;

		public Table(FskMetaData template, boolean editable) {
			super(new TableModel(template, editable));

			// Set columns witdth
			for (int ncol = 0; ncol < getColumnCount(); ncol++) {
				columnModel.getColumn(ncol).setPreferredWidth(150);
			}
			setAutoResizeMode(AUTO_RESIZE_OFF);

			// Set special editors
			columnModel.getColumn(Col.Model_Type.ordinal()).setCellEditor(new ModelTypeEditor());
			columnModel.getColumn(Col.Model_Subject.ordinal()).setCellEditor(new ModelSubjectEditor());
		}
	}

	private static class ModelTypeEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 2923508881330612951L;
		private static JComboBox<String> comboBox;

		static {
			comboBox = new JComboBox<>();
			modelTypeStrings.values().forEach(modelType -> comboBox.addItem(modelType));
			comboBox.addItem(""); // empty string for non defined model types
		}

		public ModelTypeEditor() {
			super(comboBox);
		}
	}

	private static class ModelSubjectEditor extends DefaultCellEditor {

		private static final long serialVersionUID = -3451495357854026436L;
		private static JComboBox<String> comboBox;

		static {
			comboBox = new JComboBox<>();
			Arrays.stream(ModelClass.values()).forEach(modelClass -> comboBox.addItem(modelClass.fullName()));
			comboBox.addItem(""); // empty string for non defined model class
		}

		public ModelSubjectEditor() {
			super(comboBox);
		}
	}

	private static class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 5174052168162089904L;
		private static String[] names;

		static {
			// Populate column names
			names = new String[Col.values().length];
			names[Col.Model_Name.ordinal()] = "Model name";
			names[Col.Model_Id.ordinal()] = "Model id";
			names[Col.Model_Link.ordinal()] = "Model link";
			names[Col.Organism_Name.ordinal()] = "Organism name";
			names[Col.Organism_Detail.ordinal()] = "Organism detail";
			names[Col.Environment_Name.ordinal()] = "Environment name";
			names[Col.Environment_Detail.ordinal()] = "Environment detail";
			names[Col.Model_Creator.ordinal()] = "Model creator";
			names[Col.Model_Family_Name.ordinal()] = "Model family name";
			names[Col.Model_Contact.ordinal()] = "Model contact";
			names[Col.Software.ordinal()] = "Software";
			names[Col.Model_Reference_Description.ordinal()] = "Reference description";
			names[Col.Model_Reference_Description_Link.ordinal()] = "Reference description link";
			names[Col.Model_Created_Date.ordinal()] = "Created date";
			names[Col.Model_Modified_Date.ordinal()] = "Modified date";
			names[Col.Model_Rights.ordinal()] = "Rights";
			names[Col.Model_Notes.ordinal()] = "Notes";
			names[Col.Model_Curation_Status.ordinal()] = "Curation status";
			names[Col.Model_Type.ordinal()] = "Model type";
			names[Col.Model_Subject.ordinal()] = "Model subject";
			names[Col.Model_Food_Process.ordinal()] = "Food process";
			names[Col.Dependent_Variable.ordinal()] = "Dependent variable";
			names[Col.Dependent_Variable_Unit.ordinal()] = "Dependent variable unit";
			names[Col.Dependent_Variable_Type.ordinal()] = "Dependent variable type";
			names[Col.Dependent_Variable_Min.ordinal()] = "Dependent variable minimum value";
			names[Col.Dependent_Variable_Max.ordinal()] = "Dependent variable maximum value";
			names[Col.Independent_Variable.ordinal()] = "Independent variables";
			names[Col.Independent_Variable_Units.ordinal()] = "Independent variable units";
			names[Col.Independent_Variable_Types.ordinal()] = "Independent variable types";
			names[Col.Independent_Variable_Mins.ordinal()] = "Independent variable minimum values";
			names[Col.Independent_Variable_Maxs.ordinal()] = "Independent variable maximum values";
			names[Col.Independent_Variable_Values.ordinal()] = "Independent variable values";
			names[Col.Has_Data.ordinal()] = "Has data?";
		}

		private FskMetaData template;
		private boolean editable;

		public TableModel(FskMetaData template, boolean editable) {
			this.template = template;
			this.editable = editable;
		}

		@Override
		public int getColumnCount() {
			return names.length;
		}

		@Override
		public int getRowCount() {
			return 1; // so far only one template is supported
		}

		@Override
		public String getColumnName(int column) {
			return names[column];
		}

		@Override
		public Object getValueAt(int row, int col) {
			switch (Col.values()[col]) {
			case Model_Name:
				return template.modelName;
			case Model_Id:
				return template.modelId;
			case Model_Link:
				return template.modelLink;
			case Organism_Name:
				return template.organism;
			case Organism_Detail:
				return template.organismDetails;
			case Environment_Name:
				return template.matrix;
			case Environment_Detail:
				return template.matrixDetails;
			case Model_Creator:
				return template.creator;
			case Model_Family_Name:
				return template.familyName;
			case Model_Contact:
				return template.contact;
			case Software:
				return template.software == null ? "" : template.software.name();
			case Model_Reference_Description:
				return template.referenceDescription;
			case Model_Reference_Description_Link:
				return template.referenceDescriptionLink == null ? "" : template.referenceDescriptionLink.toString();
			case Model_Created_Date:
				return template.createdDate == null ? "" : FskMetaData.dateFormat.format(template.createdDate);
			case Model_Modified_Date:
				return template.modifiedDate == null ? "" : FskMetaData.dateFormat.format(template.modifiedDate);
			case Model_Rights:
				return template.rights;
			case Model_Notes:
				return template.notes;
			case Model_Curation_Status:
				return Boolean.toString(template.curated);
			case Model_Type:
				return template.type == null ? "" : modelTypeStrings.get(template.type);
			case Model_Subject:
				return template.subject == null ? "" : template.subject.fullName();
			case Model_Food_Process:
				return template.foodProcess;
			case Dependent_Variable:
				return template.dependentVariable.name;
			case Dependent_Variable_Unit:
				return template.dependentVariable.unit;
			case Dependent_Variable_Type:
				return template.dependentVariable.type == null ? "" : template.dependentVariable.type.name();
			case Dependent_Variable_Min:
				return template.dependentVariable.min;
			case Dependent_Variable_Max:
				return template.dependentVariable.max;
			case Independent_Variable:
				return template.independentVariables.stream().map(v -> v.name).collect(Collectors.joining("||"));
			case Independent_Variable_Units:
				return template.independentVariables.stream().map(v -> v.unit).collect(Collectors.joining("||"));
			case Independent_Variable_Types:
				return template.independentVariables.stream().map(v -> v.type.name()).collect(Collectors.joining("||"));
			case Independent_Variable_Mins:
				return template.independentVariables.stream().map(v -> v.min).collect(Collectors.joining("||"));
			case Independent_Variable_Maxs:
				return template.independentVariables.stream().map(v -> v.max).collect(Collectors.joining("||"));
			case Independent_Variable_Values:
				return template.independentVariables.stream().map(v -> v.value).collect(Collectors.joining("||"));
			case Has_Data:
				return Boolean.toString(template.hasData);
			}
			throw new RuntimeException("Invalid row & col" + row + " " + col);
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

			String stringValue = (String) aValue;

			switch (Col.values()[columnIndex]) {
			case Model_Name:
				template.modelName = stringValue;
				break;
			case Model_Id:
				template.modelId = stringValue;
				break;
			case Model_Link:
				template.modelLink = stringValue;
				break;
			case Organism_Name:
				template.organism = stringValue;
				break;
			case Organism_Detail:
				template.organismDetails = stringValue;
				break;
			case Environment_Name:
				template.matrix = stringValue;
				break;
			case Environment_Detail:
				template.matrixDetails = stringValue;
				break;
			case Model_Creator:
				template.creator = stringValue;
				break;
			case Model_Family_Name:
				template.familyName = stringValue;
				break;
			case Model_Contact:
				template.contact = stringValue;
				break;
			case Software:
				template.software = Software.valueOf(stringValue);
				break;
			case Model_Reference_Description:
				template.referenceDescription = stringValue;
				break;
			case Model_Reference_Description_Link:
				template.referenceDescriptionLink = stringValue;
				break;
			case Model_Created_Date:
				try {
					template.createdDate = FskMetaData.dateFormat.parse(stringValue);
				} catch (ParseException e) {
					LOGGER.warn("Invalid date");
				}
				break;
			case Model_Modified_Date:
				try {
					template.modifiedDate = FskMetaData.dateFormat.parse(stringValue);
				} catch (ParseException e) {
					LOGGER.warn("Invalid date");
				}
				break;
			case Model_Rights:
				template.rights = stringValue;
				break;
			case Model_Notes:
				template.notes = stringValue;
				break;
			case Model_Curation_Status:
				template.curated = Boolean.parseBoolean(stringValue);
				break;
			case Model_Type:
				if (stringValue.isEmpty()) {
					template.type = null;
				} else {
					for (Map.Entry<ModelType, String> entry : modelTypeStrings.entrySet()) {
						if (stringValue.equals(entry.getValue())) {
							template.type = entry.getKey();
							break;
						}
					}
				}
				break;
			case Model_Subject:
				template.subject = stringValue.isEmpty() ? null : ModelClass.fromName(stringValue);
				break;
			case Model_Food_Process:
				template.foodProcess = stringValue;
				break;
			case Dependent_Variable:
				template.dependentVariable.name = stringValue;
				break;
			case Dependent_Variable_Unit:
				template.dependentVariable.unit = stringValue;
				break;
			case Dependent_Variable_Type:
				template.dependentVariable.type = Strings.isNullOrEmpty(stringValue) ? null : DataType.valueOf(stringValue);
				break;
			case Dependent_Variable_Min:
				template.dependentVariable.min = stringValue;
				break;
			case Dependent_Variable_Max:
				template.dependentVariable.max = stringValue;
				break;
			case Independent_Variable:
				{
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).name = tokens[i];
						}
					}
				}
				break;
			case Independent_Variable_Units:
				{
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).unit = tokens[i];
						}
					}
				}
				break;
			case Independent_Variable_Types:
				{
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							DataType dt = DataType.valueOf(tokens[i]);
							template.independentVariables.get(i).type = dt;
						}
					}
				}
				break;
			case Independent_Variable_Mins:
				{
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).min = tokens[i];
						}
					}
				}
				break;
			case Independent_Variable_Maxs:
				{
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).max = tokens[i];
						}
					}
				}
				break;
			case Independent_Variable_Values:
				{
					String[] tokens = stringValue.split("||");
					if (tokens.length == template.independentVariables.size()) {
						for (int i = 0; i < tokens.length; i++) {
							template.independentVariables.get(i).value = tokens[i];
						}
					}
				}
				break;
			case Has_Data:
				template.hasData = Boolean.parseBoolean(stringValue);
				break;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return editable;
		}
	}
}
