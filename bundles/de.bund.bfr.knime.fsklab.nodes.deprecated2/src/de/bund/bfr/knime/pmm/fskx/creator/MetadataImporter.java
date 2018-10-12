package de.bund.bfr.knime.pmm.fskx.creator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class MetadataImporter {

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

	public FskMetaData processSpreadsheet(final XSSFSheet sheet) {

		final String modelId = getStringVal(sheet, ID);
		final String organism = getStringVal(sheet, ORGANISM);
		final String matrix = getStringVal(sheet, MATRIX);
		final String depVar = getStringVal(sheet, DEPVAR);
		final String depVarUnit = getStringVal(sheet, DEPVAR_UNIT);
		final List<String> indepVars = Arrays.stream(getStringVal(sheet, INDEPVAR).split("\\|\\|")).map(String::trim)
				.collect(Collectors.toList());
		final List<String> indepVarUnits = Arrays.stream(getStringVal(sheet, INDEPVAR_UNIT).split("\\|\\|"))
				.map(String::trim).collect(Collectors.toList());

		FskMetaData template = new FskMetaData();
		template.modelId = modelId;
		template.modelName = getStringVal(sheet, NAME);
		template.organism = organism;
		template.organismDetails = getStringVal(sheet, ORGANISM_DETAIL);
		template.matrix = matrix;
		template.matrixDetails = getStringVal(sheet, MATRIX_DETAIL);
		template.creator = getStringVal(sheet, CREATOR);
		// no family name in the spreadsheet
		// no contact in the spreadsheet
		template.referenceDescription = getStringVal(sheet, REFERENCE_DESCRIPTION);
		
		template.createdDate = sheet.getRow(CREATED_DATE).getCell(5).getDateCellValue();
		template.createdDate.setYear(template.createdDate.getYear() + 1900);
		
		template.modifiedDate = sheet.getRow(MODIFIED_DATE).getCell(5).getDateCellValue();
		template.modifiedDate.setYear(template.modifiedDate.getYear() + 1900);
		
		template.rights = getStringVal(sheet, RIGHTS);

		try {
			String modelType = getStringVal(sheet, TYPE);
			template.type = ModelType.valueOf(modelType);
		} catch (IllegalArgumentException e) {
		}

		try {
			String subject = getStringVal(sheet, SUBJECT);
			template.subject = ModelClass.valueOf(subject);
		} catch (IllegalArgumentException e) {
			template.subject = ModelClass.UNKNOWN;
		}

		// model notes
		template.notes = getStringVal(sheet, NOTES);

		// dep var. Type is not in the spreadsheet.
		template.dependentVariable.name = depVar;
		template.dependentVariable.unit = depVarUnit;
		template.dependentVariable.min = getStringVal(sheet, DEPVAR_MIN);
		template.dependentVariable.max = getStringVal(sheet, DEPVAR_MAX);

		// indep vars
		{
			String[] mins = getStringVal(sheet, INDEPVAR_MIN).split("\\|\\|");
			String[] maxs = getStringVal(sheet, INDEPVAR_MAX).split("\\|\\|");

			for (int i = 0; i < mins.length; i++) {
				Variable v = new Variable();
				v.name = indepVars.get(i);
				v.unit = indepVarUnits.get(i);
				v.min = mins[i].trim();
				v.max = maxs[i].trim();
				// no values or types in the spreadsheet
				v.value = "";
				template.independentVariables.add(v);
			}
		}

		template.hasData = false;

		return template;
	}

	/**
	 * Gets the string value for the fifth column which holds the value for that
	 * row.
	 */
	private static String getStringVal(final XSSFSheet sheet, final int rownum) {
		XSSFCell cell = sheet.getRow(rownum).getCell(5);

		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return Double.toString(cell.getNumericCellValue());
		return cell.getStringCellValue();
	}
}
