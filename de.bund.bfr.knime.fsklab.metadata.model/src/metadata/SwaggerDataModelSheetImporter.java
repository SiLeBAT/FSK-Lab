package metadata;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DataModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DataModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Reference;

public class SwaggerDataModelSheetImporter extends SwaggerSheetImporter {
	public DataModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		DataModelGeneralInformation information = new DataModelGeneralInformation();

		Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(I);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(I);
		if (sourceCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setIdentifier(identifierCell.getStringCellValue());
		}

		try {
			Contact author = retrieveCreator(sheet.getRow(3));
			information.addAuthorItem(author);
		} catch (Exception exception) {
		}

		for (int numRow = 3; numRow < 7; numRow++) {
			try {
				Contact contact = retrieveAuthor(sheet.getRow(numRow));
				information.addCreatorItem(contact);
			} catch (Exception exception) {
			}
		}

		Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(I);
		if (creationDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date creationDate = creationDateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(creationDate.getYear() + 1900, creationDate.getMonth() + 1,
					creationDate.getDate());
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(I);
		if (rightsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(I);
		if (isAvailableCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(I);
		if (urlCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(I);
		if (formatCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setFormat(formatCell.getStringCellValue());
		}

		// reference (1..n)
		for (int numRow = 14; numRow < 17; numRow++) {
			try {
				Reference reference = retrieveReference(sheet.getRow(numRow));
				information.addReferenceItem(reference);
			} catch (Exception exception) {
			}
		}

		Cell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(I);
		if (languageCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}


		// model category (0..n)

		Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(I);
		if (statusCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(I);
		if (objectiveCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}
	public GenericModelScope retrieveScope(Sheet sheet) {
	
		SwaggerGenericSheetImporter importer = new SwaggerGenericSheetImporter();
		return importer.retrieveScope(sheet);
		
	}
	public GenericModelDataBackground retrieveBackground(Sheet sheet) {

		SwaggerGenericSheetImporter importer = new SwaggerGenericSheetImporter();
		return importer.retrieveBackground(sheet);
		
	}
	public DataModelModelMath retrieveModelMath(Sheet sheet) {
		
		DataModelModelMath math = new DataModelModelMath();

		for (int rownum = 132; rownum < sheet.getLastRowNum(); rownum++) {
			try {
				Row row = sheet.getRow(rownum);
				Parameter param = retrieveParameter(row);
				math.addParameterItem(param);
			} catch (Exception exception) {
				// ...
			}
		}


		return math;
	}
}
