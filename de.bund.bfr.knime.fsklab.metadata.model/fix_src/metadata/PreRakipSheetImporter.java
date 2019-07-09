package metadata;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Product;

@SuppressWarnings("deprecation")
public class PreRakipSheetImporter {

	public GenericModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		GenericModelGeneralInformation information = new GenericModelGeneralInformation();

		Cell nameCell = sheet.getRow(1).getCell(5);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		Cell idCell = sheet.getRow(2).getCell(5);
		if (idCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setIdentifier(idCell.getStringCellValue());
		}

		Cell creationDateCell = sheet.getRow(9).getCell(5);
		if (creationDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date date = creationDateCell.getDateCellValue();
			information.setCreationDate(LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate()));
		}

		Cell rightsCell = sheet.getRow(11).getCell(5);
		if (rightsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		Cell modificationCell = sheet.getRow(10).getCell(5);
		if (modificationCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date date = creationDateCell.getDateCellValue();
			information
					.addModificationDateItem(LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate()));
		}
		
		return information;
	}

	public GenericModelScope retrieveScope(Sheet sheet) {
		
		Hazard hazard = new Hazard();
		Cell organismCell = sheet.getRow(3).getCell(5);
		if (organismCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setName(organismCell.getStringCellValue());
		}
		
		Cell organismDetailCell = sheet.getRow(4).getCell(5);
		if (organismDetailCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setDescription(organismDetailCell.getStringCellValue());
		}
		
		Product product = new Product();
		Cell matrixCell = sheet.getRow(5).getCell(5);
		if (matrixCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setName(matrixCell.getStringCellValue());
		}

		Cell matrixDetailCell = sheet.getRow(6).getCell(5);
		if (matrixDetailCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setDescription(matrixDetailCell.getStringCellValue());
		}
		
		GenericModelScope scope = new GenericModelScope();
		scope.addHazardItem(hazard);
		scope.addProductItem(product);
		
		return scope;
	}

	public GenericModelModelMath retrieveModelMath(Sheet sheet) {

		GenericModelModelMath math = new GenericModelModelMath();
		
		// Dependent variable
		Parameter dep = new Parameter();
		
		Cell depVarNameCell = sheet.getRow(21).getCell(5);
		if (depVarNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			dep.setId(depVarNameCell.getStringCellValue());
		}

		Cell depVarUnitCell = sheet.getRow(22).getCell(5);
		if (depVarUnitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			dep.setUnit(depVarUnitCell.getStringCellValue());
		}

		Cell depVarMinCell = sheet.getRow(23).getCell(5);
		if (depVarMinCell.getCellType() == Cell.CELL_TYPE_STRING) {
			dep.setMinValue(depVarMinCell.getStringCellValue());
		} else if (depVarMinCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			dep.setMinValue(Double.toString(depVarMinCell.getNumericCellValue()));
		}

		Cell depVarMaxCell = sheet.getRow(24).getCell(5);
		if (depVarMaxCell.getCellType() == Cell.CELL_TYPE_STRING) {
			dep.setMaxValue(depVarMaxCell.getStringCellValue());
		} else if (depVarMaxCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			dep.setMaxValue(Double.toString(depVarMaxCell.getNumericCellValue()));
		}
		
		math.addParameterItem(dep);
		
		// independent variables *
		String[] indepVars = null;
		Cell indepVarCell = sheet.getRow(25).getCell(5);
		if (indepVarCell.getCellType() == Cell.CELL_TYPE_STRING) {
			indepVars = indepVarCell.getStringCellValue().split("\\|\\|");
		}

		String[] indepVarUnits = null;
		Cell indepVarUnitCell = sheet.getRow(26).getCell(5);
		if (indepVarUnitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			indepVarUnits = indepVarUnitCell.getStringCellValue().split("\\|\\|");
		}

		String[] mins = null;
		Cell indepVarMinCell = sheet.getRow(27).getCell(5);
		if (indepVarMinCell.getCellType() == Cell.CELL_TYPE_STRING) {
			mins = indepVarMinCell.getStringCellValue().split("\\|\\|");
		}

		String[] maxs = null;
		Cell indepVarMaxCell = sheet.getRow(28).getCell(5);
		if (indepVarMaxCell.getCellType() == Cell.CELL_TYPE_STRING) {
			maxs = indepVarMaxCell.getStringCellValue().split("\\|\\|");
		}
		
		if (indepVars != null && indepVarUnits != null && mins != null && maxs != null) {
			for (int i = 0; i < indepVars.length; i++) {
				Parameter p = new Parameter();
				p.setId(indepVars[i].trim());
				p.setUnit(indepVarUnits[i].trim());
				p.setMinValue(mins[i].trim());
				p.setMaxValue(maxs[i].trim());
				math.addParameterItem(p);
			}

		}
		return math; 
	}
}
