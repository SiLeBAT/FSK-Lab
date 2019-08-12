package metadata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class LegacyMetadataImporter {

	// Rows
	private final int ID = 2;
	private final int NAME = 1;
	private final int ORGANISM = 3;
	private final int ORGANISM_DETAIL = 4;
	private final int MATRIX = 5;
	private final int MATRIX_DETAIL = 6;
	private final int CREATOR = 7;
	private final int REFERENCE_DESCRIPTION = 8;
	private final int CREATED_DATE = 9;
	private final int MODIFIED_DATE = 10;
	private final int RIGHTS = 11;
	private final int TYPE = 13;
	private final int SUBJECT = 14;
	private final int NOTES = 12;
	private final int DEPVAR = 21;
	private final int DEPVAR_UNIT = 22;
	private final int DEPVAR_MIN = 23;
	private final int DEPVAR_MAX = 24;
	private final int INDEPVAR = 25;
	private final int INDEPVAR_UNIT = 26;
	private final int INDEPVAR_MIN = 27;
	private final int INDEPVAR_MAX = 28;

	public FskMetaData processSpreadsheet(final Sheet sheet) {

		final FskMetaData template = new FskMetaData();

		final Cell modelNameCell = sheet.getRow(NAME).getCell(5);
		if (modelNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.modelName = modelNameCell.getStringCellValue();
		}

		final Cell modelIdCell = sheet.getRow(ID).getCell(5);
		if (modelIdCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.modelId = modelIdCell.getStringCellValue();
		}

		// model link

		final Cell organismCell = sheet.getRow(ORGANISM).getCell(5);
		if (organismCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.organism = organismCell.getStringCellValue();
		}

		final Cell organismDetailCell = sheet.getRow(ORGANISM_DETAIL).getCell(5);
		if (organismDetailCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.organismDetails = organismDetailCell.getStringCellValue();
		}

		final Cell matrixCell = sheet.getRow(MATRIX).getCell(5);
		if (matrixCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.matrix = matrixCell.getStringCellValue();
		}

		final Cell matrixDetailCell = sheet.getRow(MATRIX_DETAIL).getCell(5);
		if (matrixDetailCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.matrixDetails = matrixDetailCell.getStringCellValue();
		}

		final Cell creatorCell = sheet.getRow(CREATOR).getCell(5);
		if (creatorCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.creator = creatorCell.getStringCellValue();
		}

		// There is no family name, contact or software in the spreadsheet.

		final Cell referenceDescriptionCell = sheet.getRow(REFERENCE_DESCRIPTION).getCell(5);
		if (referenceDescriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.referenceDescription = referenceDescriptionCell.getStringCellValue();
		}

		// reference description link

		final Cell creationDateCell = sheet.getRow(CREATED_DATE).getCell(5);
		if (creationDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			template.createdDate = creationDateCell.getDateCellValue();
		}

		final Cell modificationCell = sheet.getRow(MODIFIED_DATE).getCell(5);
		if (modificationCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			template.modifiedDate = modificationCell.getDateCellValue();
		}

		final Cell rightsCell = sheet.getRow(RIGHTS).getCell(5);
		if (rightsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.rights = rightsCell.getStringCellValue();
		}

		final Cell notesCell = sheet.getRow(NOTES).getCell(5);
		if (notesCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.notes = notesCell.getStringCellValue();
		}

		// curated

		try {
			final Cell typeCell = sheet.getRow(TYPE).getCell(5);
			if (typeCell.getCellType() == Cell.CELL_TYPE_STRING) {
				template.type = ModelType.valueOf(typeCell.getStringCellValue());
			}
		} catch (final IllegalArgumentException e) {
		}

		try {
			final Cell subjectCell = sheet.getRow(SUBJECT).getCell(5);
			if (subjectCell.getCellType() == Cell.CELL_TYPE_STRING) {
				template.subject = ModelClass.valueOf(subjectCell.getStringCellValue());
			}
		} catch (final IllegalArgumentException e) {
			template.subject = ModelClass.UNKNOWN;
		}

		// food process

		// dependent variable
		final Cell depVarNameCell = sheet.getRow(DEPVAR).getCell(5);
		if (depVarNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.dependentVariable.name = depVarNameCell.getStringCellValue();
		}

		final Cell depVarUnitCell = sheet.getRow(DEPVAR_UNIT).getCell(5);
		if (depVarUnitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.dependentVariable.unit = depVarUnitCell.getStringCellValue();
		}

		final Cell depVarMinCell = sheet.getRow(DEPVAR_MIN).getCell(5);
		if (depVarMinCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.dependentVariable.min = depVarMinCell.getStringCellValue();
		} else if (depVarMinCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			template.dependentVariable.min = Double.toString(depVarMinCell.getNumericCellValue());
		}

		final Cell depVarMaxCell = sheet.getRow(DEPVAR_MAX).getCell(5);
		if (depVarMaxCell.getCellType() == Cell.CELL_TYPE_STRING) {
			template.dependentVariable.max = depVarMaxCell.getStringCellValue();
		} else if (depVarMaxCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			template.dependentVariable.max = Double.toString(depVarMaxCell.getNumericCellValue());
		}

		// independent variables *
		String[] indepVars = null;
		final Cell indepVarCell = sheet.getRow(INDEPVAR).getCell(5);
		if (indepVarCell.getCellType() == Cell.CELL_TYPE_STRING) {
			indepVars = indepVarCell.getStringCellValue().split("\\|\\|");
		}

		String[] indepVarUnits = null;
		final Cell indepVarUnitCell = sheet.getRow(INDEPVAR_UNIT).getCell(5);
		if (indepVarUnitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			indepVarUnits = indepVarUnitCell.getStringCellValue().split("\\|\\|");
		}

		String[] mins = null;
		final Cell indepVarMinCell = sheet.getRow(INDEPVAR_MIN).getCell(5);
		if (indepVarMinCell.getCellType() == Cell.CELL_TYPE_STRING) {
			mins = indepVarMinCell.getStringCellValue().split("\\|\\|");
		}

		String[] maxs = null;
		final Cell indepVarMaxCell = sheet.getRow(INDEPVAR_MAX).getCell(5);
		if (indepVarMaxCell.getCellType() == Cell.CELL_TYPE_STRING) {
			maxs = indepVarMaxCell.getStringCellValue().split("\\|\\|");
		}

		if (indepVars != null && indepVarUnits != null && mins != null && maxs != null) {
			for (int i = 0; i < indepVars.length; i++) {
				final Variable variable = new Variable();
				variable.name = indepVars[i].trim();
				variable.unit = indepVarUnits[i].trim();
				variable.min = mins[i].trim();
				variable.max = maxs[i].trim();
				// no values or types in the spreadsheet
				variable.value = "";
				template.independentVariables.add(variable);
			}
		}

		template.hasData = false;

		return template;
	}
}
