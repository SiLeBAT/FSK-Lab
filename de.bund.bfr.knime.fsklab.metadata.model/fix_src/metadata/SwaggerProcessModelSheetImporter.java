package metadata;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.ProcessModelScope;

public class SwaggerProcessModelSheetImporter extends SwaggerSheetImporter {

	public PredictiveModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {
	
		SwaggerPredictiveModelSheetImporter importer = new SwaggerPredictiveModelSheetImporter();
		return importer.retrieveGeneralInformation(sheet);
		
	}
	public PredictiveModelModelMath retrieveModelMath(Sheet sheet) {
		
		
		SwaggerPredictiveModelSheetImporter importer = new SwaggerPredictiveModelSheetImporter();
		return importer.retrieveModelMath(sheet);
		

	}
	public PredictiveModelDataBackground retrieveBackground(Sheet sheet) {

		
		SwaggerPredictiveModelSheetImporter importer = new SwaggerPredictiveModelSheetImporter();
		return importer.retrieveBackground(sheet);
		

	}
	public ProcessModelScope retrieveScope(Sheet sheet) {

		SwaggerGenericSheetImporter importer = new SwaggerGenericSheetImporter();
		ProcessModelScope scope = new ProcessModelScope();

		for (int numrow = 38; numrow <= 49; numrow++) {

			Row row = sheet.getRow(numrow);

			try {
				scope.addProductItem(importer.retrieveProduct(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addHazardItem(retrieveHazard(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

		
		}

		Cell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(I);
		if (generalCommentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(I);
		if (temporalInformationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}


}
