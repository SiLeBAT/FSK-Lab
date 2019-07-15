package metadata;


import org.apache.poi.ss.usermodel.Sheet;

import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;

public class SwaggerQraModelSheetImporter extends SwaggerSheetImporter {
	public GenericModelDataBackground retrieveBackground(Sheet sheet) {
		SwaggerGenericSheetImporter importer = new SwaggerGenericSheetImporter();
		return importer.retrieveBackground(sheet);	
	}
	public GenericModelModelMath retrieveModelMath(Sheet sheet) {
		
		SwaggerGenericSheetImporter importer = new SwaggerGenericSheetImporter();
		return importer.retrieveModelMath(sheet);
		
	}

	public PredictiveModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {
		
		SwaggerPredictiveModelSheetImporter importer = new SwaggerPredictiveModelSheetImporter();
		return importer.retrieveGeneralInformation(sheet);	
	
	}
	public ExposureModelScope retrieveScope(Sheet sheet) {
		
		SwaggerExposureSheetImporter importer = new SwaggerExposureSheetImporter();
		return importer.retrieveScope(sheet);
		
	}

}
