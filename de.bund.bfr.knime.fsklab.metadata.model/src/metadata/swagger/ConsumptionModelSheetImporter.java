package metadata.swagger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.ConsumptionModelScope;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

public class ConsumptionModelSheetImporter implements SheetImporter {

	private int GENERAL_INFORMATION__NAME = 1;
	private int GENERAL_INFORMATION__SOURCE = 2;
	private int GENERAL_INFORMATION__IDENTIFIER = 3;
	private int GENERAL_INFORMATION_CREATION_DATE = 6;
	private int GENERAL_INFORMATION__RIGHTS = 8;
	private int GENERAL_INFORMATION__AVAILABLE = 9;
	private int GENERAL_INFORMATION__URL = 10;
	private int GENERAL_INFORMATION__FORMAT = 11;
	private int GENERAL_INFORMATION__LANGUAGE = 24;
	private int GENERAL_INFORMATION__SOFTWARE = 25;
	private int GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN = 26;
	private int GENERAL_INFORMATION__STATUS = 32;
	private int GENERAL_INFORMATION__OBJECTIVE = 33;
	private int GENERAL_INFORMATION__DESCRIPTION = 34;

	private int MODEL_CATEGORY__MODEL_CLASS = 27;
	private int MODEL_CATEGORY__MODEL_SUB_CLASS = 28;
	private int MODEL_CATEGORY__CLASS_COMMENT = 29;
	private int MODEL_CATEGORY__BASIC_PROCESS = 30;

	private int QUALITY_MEASURES__SSE = 109;
	private int QUALITY_MEASURES__MSE = 110;
	private int QUALITY_MEASURES__RMSE = 111;
	private int QUALITY_MEASURES__RSQUARE = 112;
	private int QUALITY_MEASURES__AIC = 113;
	private int QUALITY_MEASURES__BIC = 114;

	private int SCOPE__GENERAL_COMMENT = 62;
	private int SCOPE__TEMPORAL_INFORMATION = 63;

	private int STUDY__STUDY_IDENTIFIER = 66;
	private int STUDY__STUDY_TITLE = 67;
	private int STUDY__STUDY_DESCRIPTION = 68;
	private int STUDY__STUDY_DESIGN_TYPE = 69;
	private int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 70;
	private int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 71;
	private int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 72;
	private int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 73;
	private int STUDY__STUDY_PROTOCOL_NAME = 74;
	private int STUDY__STUDY_PROTOCOL_TYPE = 75;
	private int STUDY__STUDY_PROTOCOL_DESCRIPTION = 76;
	private int STUDY__STUDY_PROTOCOL_URI = 77;
	private int STUDY__STUDY_PROTOCOL_VERSION = 78;
	private int STUDY__STUDY_PROTOCOL_PARAMETERS_NAME = 79;
	private int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 80;
	private int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 81;

	private int GI_CREATOR_ROW = 3;
	private int GI_REFERENCE_ROW = 14;
	private int SCOPE_PRODHAZPOP_ROW = 38;
	private int BG_STUDY_SAMPLE_ROW = 82;
	private int BG_DIET_ASSESS_ROW = 88;
	private int BG_EVENT_ROW = 94;
	private int BG_LABORATORY_ROW = 95;
	private int BG_ASSAY_ROW = 101;
	private int BG_QUALITY_MEAS_ROW = 109;
	private int BG_Model_EQ_ROW = 109;

	private int MM_PARAMETER_ROW = 119;
	private int MM_FITTING_PROCEDURE_ROW = 135;
	
	/** Columns for each of the properties of DietaryAssessmentMethod. */
	private final HashMap<String, Integer> methodColumns;
	
	/** Columns for each of the properties of Laboratory. */
	private final HashMap<String, Integer> laboratoryColumns;
	
	public ConsumptionModelSheetImporter() {
		
		methodColumns = new HashMap<>();
		methodColumns.put("collectionTool", L);
		methodColumns.put("numberOfNonConsecutiveOneDay", M);
		methodColumns.put("softwareTool", N);
		methodColumns.put("numberOfFoodItems", O);
		methodColumns.put("recordTypes", P);
		methodColumns.put("foodDescriptors", Q);
		
		laboratoryColumns = new HashMap<>();
		laboratoryColumns.put("accreditation", L);
		laboratoryColumns.put("name", M);
		laboratoryColumns.put("country", N);
	}

	private PredictiveModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();

		Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(J);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(J);
		if (sourceCell.getCellTypeEnum() == CellType.STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(J);
		if (identifierCell.getCellTypeEnum() == CellType.STRING) {
			information.setIdentifier(identifierCell.getStringCellValue());
		}

		try {
			Contact author = ImporterUtils.retrieveAuthor(sheet.getRow(GI_CREATOR_ROW));
			information.addAuthorItem(author);
		} catch (Exception exception) {
			// Skip faulty author and continue
		}

		for (int numRow = GI_CREATOR_ROW; numRow < (GI_CREATOR_ROW + 4); numRow++) {
			try {
				Contact contact = ImporterUtils.retrieveCreator(sheet.getRow(numRow));
				information.addCreatorItem(contact);
			} catch (Exception exception) {
				// Skip faulty contact and continue
			}
		}

		Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(J);
		if (creationDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date creationDate = creationDateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(creationDate.getYear() + 1900, creationDate.getMonth() + 1,
					creationDate.getDate());
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(J);
		if (rightsCell.getCellTypeEnum() == CellType.STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(J);
		if (isAvailableCell.getCellTypeEnum() == CellType.STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(J);
		if (urlCell.getCellTypeEnum() == CellType.STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(J);
		if (formatCell.getCellTypeEnum() == CellType.STRING) {
			information.setFormat(formatCell.getStringCellValue());
		}

		// reference (1..n)
		for (int numRow = this.GI_REFERENCE_ROW; numRow < (this.GI_REFERENCE_ROW + 3); numRow++) {
			try {
				Reference reference = ImporterUtils.retrieveReference(sheet.getRow(numRow));
				information.addReferenceItem(reference);
			} catch (Exception exception) {
				// Skip faulty reference and continue
			}
		}

		Cell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(J);
		if (languageCell.getCellTypeEnum() == CellType.STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}

		Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(J);
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(J);
		if (languageWrittenInCell.getCellTypeEnum() == CellType.STRING) {
			information.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		// model category (0..n)
		try {
			ModelCategory category = retrieveModelCategory(sheet);
			information.setModelCategory(category);
		} catch (Exception exception) {
			// Skip faulty category and continue
		}

		Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(J);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(J);
		if (objectiveCell.getCellTypeEnum() == CellType.STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(J);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}

	private ConsumptionModelScope retrieveScope(Sheet sheet) {

		ConsumptionModelScope scope = new ConsumptionModelScope();

		for (int numrow = SCOPE_PRODHAZPOP_ROW; numrow <= (SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

			Row row = sheet.getRow(numrow);

			try {
				scope.addProductItem(ImporterUtils.retrieveProduct(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addPopulationGroupItem(ImporterUtils.retrievePopulationGroup(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since population groups are optional (*)
			}
		}

		Cell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(J);
		if (generalCommentCell.getCellTypeEnum() == CellType.STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(J);
		if (temporalInformationCell.getCellTypeEnum() == CellType.STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}

	private PredictiveModelModelMath retrieveModelMath(Sheet sheet) {

		PredictiveModelModelMath math = new PredictiveModelModelMath();

		for (int rownum = this.MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
			try {
				Row row = sheet.getRow(rownum);
				Parameter param = ImporterUtils.retrieveParameter(row);
				math.addParameterItem(param);
			} catch (Exception exception) {
				// ...
			}
		}

		try {
			QualityMeasures measures = retrieveQualityMeasures(sheet);
			math.addQualityMeasuresItem(measures);
		} catch (Exception exception) {
			// ...
		}
		Cell fittingProcedureCell = sheet.getRow(MM_FITTING_PROCEDURE_ROW).getCell(J);
		if (fittingProcedureCell.getCellTypeEnum() == CellType.STRING) {
			math.setFittingProcedure(fittingProcedureCell.getStringCellValue());
		}

		return math;
	}

	private GenericModelDataBackground retrieveBackground(Sheet sheet) {

		final GenericModelDataBackground background = new GenericModelDataBackground();

		try {
			final Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (final Exception exception) {
			// Skip faulty study and continue
		}

		for (int numrow = BG_STUDY_SAMPLE_ROW; numrow < BG_STUDY_SAMPLE_ROW + 3; numrow++) {
			try {
				final StudySample sample = ImporterUtils.retrieveStudySample(sheet.getRow(numrow));
				background.addStudySampleItem(sample);
			} catch (final Exception exception) {
				// Skip faulty sample and continue
			}
		}

		for (int numrow = BG_DIET_ASSESS_ROW; numrow < BG_DIET_ASSESS_ROW + 3; numrow++) {
			try {
				final DietaryAssessmentMethod method = ImporterUtils.retrieveDietaryAssessmentMethod(sheet.getRow(numrow), methodColumns);
				background.addDietaryAssessmentMethodItem(method);
			} catch (final Exception exception) {
				// Skip faulty method and continue
			}
		}

		for (int numrow = BG_LABORATORY_ROW; numrow < BG_LABORATORY_ROW + 3; numrow++) {
			try {
				Row row = sheet.getRow(numrow);
				final Laboratory laboratory = ImporterUtils.retrieveLaboratory(row, laboratoryColumns);
				background.addLaboratoryItem(laboratory);
			} catch (final Exception exception) {
				// Skip faulty laboratory and continue
			}
		}

		for (int numrow = BG_ASSAY_ROW; numrow < BG_ASSAY_ROW + 3; numrow++) {
			try {
				final Assay assay = ImporterUtils.retrieveAssay(sheet.getRow(numrow));
				background.addAssayItem(assay);
			} catch (final Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return background;
	}

	private ModelCategory retrieveModelCategory(Sheet sheet) {
		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(J).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(J).getStringCellValue());

		Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(J);
		if (subClassCell.getCellTypeEnum() == CellType.STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(J);
		if (modelClassCommentCell.getCellTypeEnum() == CellType.STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(J);
		if (basicProcessCell.getCellTypeEnum() == CellType.STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}

	private Study retrieveStudy(Sheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_TITLE).getCell(J).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		Study study = new Study();

		Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(J);
		if (identifierCell.getCellTypeEnum() == CellType.STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(J).getStringCellValue());

		Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(J);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(J);
		if (designTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(J);
		if (measurementTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(J);
		if (technologyTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(J);
		if (technologyPlatformCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(J);
		if (accreditationProcedureCell.getCellTypeEnum() == CellType.STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(J);
		if (protocolNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(J);
		if (protocolTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(J);
		if (protocolDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(J);
		if (protocolURICell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(J);
		if (protocolVersionCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(J);
		if (parameterNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(J);
		if (componentNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(J);
		if (componentTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolComponentsType(componentTypeCell.getStringCellValue());
		}

		return study;
	}

	private QualityMeasures retrieveQualityMeasures(Sheet sheet) {
		QualityMeasures measures = new QualityMeasures();

		Cell sseCell = sheet.getRow(QUALITY_MEASURES__SSE).getCell(M);
		if (sseCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setSse(BigDecimal.valueOf(sseCell.getNumericCellValue()));
		}

		Cell mseCell = sheet.getRow(QUALITY_MEASURES__MSE).getCell(M);
		if (mseCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setMse(BigDecimal.valueOf(mseCell.getNumericCellValue()));
		}

		Cell rmseCell = sheet.getRow(QUALITY_MEASURES__RMSE).getCell(M);
		if (rmseCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setRmse(BigDecimal.valueOf(rmseCell.getNumericCellValue()));
		}

		Cell rsquareCell = sheet.getRow(QUALITY_MEASURES__RSQUARE).getCell(M);
		if (rsquareCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setRsquared(BigDecimal.valueOf(rsquareCell.getNumericCellValue()));
		}

		Cell aicCell = sheet.getRow(QUALITY_MEASURES__AIC).getCell(M);
		if (aicCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setAic(BigDecimal.valueOf(aicCell.getNumericCellValue()));
		}

		Cell bicCell = sheet.getRow(QUALITY_MEASURES__BIC).getCell(M);
		if (bicCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setBic(BigDecimal.valueOf(bicCell.getNumericCellValue()));
		}

		return measures;
	}

	@Override
	public Model retrieveModel(Sheet sheet) {
		ConsumptionModel model = new ConsumptionModel();
		model.setModelType("consumptionModel");
		model.setGeneralInformation(retrieveGeneralInformation(sheet));
		model.setScope(retrieveScope(sheet));
		model.setDataBackground(retrieveBackground(sheet));
		model.setModelMath(retrieveModelMath(sheet));
		
		return model;
	}
}
