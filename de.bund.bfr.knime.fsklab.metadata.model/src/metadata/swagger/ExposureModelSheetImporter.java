package metadata.swagger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

public class ExposureModelSheetImporter implements SheetImporter {

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

	private int QUALITY_MEASURES__SSE = 124;
	private int QUALITY_MEASURES__MSE = 125;
	private int QUALITY_MEASURES__RMSE = 126;
	private int QUALITY_MEASURES__RSQUARE = 127;
	private int QUALITY_MEASURES__AIC = 128;
	private int QUALITY_MEASURES__BIC = 129;

	private int SCOPE__GENERAL_COMMENT = 73;
	private int SCOPE__TEMPORAL_INFORMATION = 74;

	private int STUDY__STUDY_IDENTIFIER = 77;
	private int STUDY__STUDY_TITLE = 78;
	private int STUDY__STUDY_DESCRIPTION = 79;
	private int STUDY__STUDY_DESIGN_TYPE = 80;
	private int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 81;
	private int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 82;
	private int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 83;
	private int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 84;
	private int STUDY__STUDY_PROTOCOL_NAME = 85;
	private int STUDY__STUDY_PROTOCOL_TYPE = 86;
	private int STUDY__STUDY_PROTOCOL_DESCRIPTION = 87;
	private int STUDY__STUDY_PROTOCOL_URI = 88;
	private int STUDY__STUDY_PROTOCOL_VERSION = 89;
	private int STUDY__STUDY_PROTOCOL_PARAMETERS_NAME = 90;
	private int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 91;
	private int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 92;

	private int GI_CREATOR_ROW = 3;
	private int GI_REFERENCE_ROW = 14;
	private int SCOPE_PRODHAZPOP_ROW = 38;
	private int BG_STUDY_SAMPLE_ROW = 96;
	private int BG_DIET_ASSESS_ROW = 102;
	private int BG_LABORATORY_ROW = 109;
	private int BG_ASSAY_ROW = 115;

	private int MM_PARAMETER_ROW = 133;
	
	/** Columns for each of the properties of Creator. */
	private final HashMap<String, Integer> creatorColumns;

	/** Columns for each of the properties of Author. */
	private final HashMap<String, Integer> authorColumns;
	
	/** Columns for each of the properties of DietaryAssessmentMethod. */
	private final HashMap<String, Integer> methodColumns;
	
	/** Columns for each of the properties of Laboratory. */
	private final HashMap<String, Integer> laboratoryColumns;
	
	/** Columns for each of the properties of Reference. */
	private final HashMap<String, Integer> referenceColumns;
	
	/** Columns for each of the properties of Product. */
	private final HashMap<String, Integer> productColumns;
	
	/** Columns for each of the properties of Parameter. */
	private final HashMap<String, Integer> parameterColumns;
	
	/** Columns for each of the properties of StudySample. */
	private final HashMap<String, Integer> sampleColumns;
	
	/** Columns for each of the properties of Hazard. */
	private final HashMap<String, Integer> hazardColumns;
	
	public ExposureModelSheetImporter() {

		methodColumns = new HashMap<>();
		methodColumns.put("collectionTool", K);
		methodColumns.put("numberOfNonConsecutiveOneDay", L);
		methodColumns.put("softwareTool", M);
		methodColumns.put("numberOfFoodItems", N);
		methodColumns.put("recordTypes", O);
		methodColumns.put("foodDescriptors", P);
		
		laboratoryColumns = new HashMap<>();
		laboratoryColumns.put("accreditation", K);
		laboratoryColumns.put("name", L);
		laboratoryColumns.put("country", M);
		
		referenceColumns = new HashMap<>();
		referenceColumns.put("referenceDescription", K);
		referenceColumns.put("type", L);
		referenceColumns.put("date", M);
		referenceColumns.put("pmid", N);
		referenceColumns.put("doi", O);
		referenceColumns.put("author", P);
		referenceColumns.put("title", Q);
		referenceColumns.put("abstract", R);
		referenceColumns.put("status", S);
		referenceColumns.put("website", U);
		referenceColumns.put("comment", V);

		productColumns = new HashMap<>();
		productColumns.put("name", K);
		productColumns.put("description", L);
		productColumns.put("unit", M);
		productColumns.put("productionMethod", N);
		productColumns.put("packaging", O);
		productColumns.put("treatment", P);
		productColumns.put("originCountry", Q);
		productColumns.put("originArea", R);
		productColumns.put("fisheriesArea", S);
		productColumns.put("productionDate", T);
		productColumns.put("expiryDate", U);
		
		creatorColumns = new HashMap<>();
		creatorColumns.put("mail", R);
		creatorColumns.put("title", K);
		creatorColumns.put("familyName", O);
		creatorColumns.put("givenName", M);
		creatorColumns.put("telephone", Q);
		creatorColumns.put("streetAddress", W);
		creatorColumns.put("country", S);
		creatorColumns.put("city", T);
		creatorColumns.put("zipCode", U);
		creatorColumns.put("region", Y);
		creatorColumns.put("organization", P);

		authorColumns = new HashMap<>();
		authorColumns.put("mail", AH);
		authorColumns.put("title", AA);
		authorColumns.put("familyName", AE);
		authorColumns.put("givenName", AC);
		authorColumns.put("telephone", AG);
		authorColumns.put("streetAddress", AM);
		authorColumns.put("country", AI);
		authorColumns.put("city", AJ);
		authorColumns.put("zipCode", AK);
		authorColumns.put("region", AO);
		authorColumns.put("organization", AF);
		
		parameterColumns = new HashMap<>();
		parameterColumns.put("id", K);
		parameterColumns.put("classification", L);
		parameterColumns.put("name", M);
		parameterColumns.put("description", N);
		parameterColumns.put("unit", O);
		parameterColumns.put("unitCategory", P);
		parameterColumns.put("dataType", Q);
		parameterColumns.put("source", R);
		parameterColumns.put("subject", S);
		parameterColumns.put("distribution", T);
		parameterColumns.put("value", U);
		parameterColumns.put("reference", V);
		parameterColumns.put("variability", W);
		parameterColumns.put("max", X);
		parameterColumns.put("min", Y);
		parameterColumns.put("error", Z);
		
		sampleColumns = new HashMap<>();
		sampleColumns.put("sample", K);
		sampleColumns.put("protocolOfSampleCollection", L);
		sampleColumns.put("samplingStrategy", M);
		sampleColumns.put("samplingProgramType", N);
		sampleColumns.put("samplingMethod", O);
		sampleColumns.put("samplingPlan", P);
		sampleColumns.put("samplingWeight", Q);
		sampleColumns.put("samplingSize", R);
		sampleColumns.put("lotSizeUnit", S);
		sampleColumns.put("samplingPoint", T);
		
		hazardColumns = new HashMap<>();
		hazardColumns.put("type", V);
		hazardColumns.put("name", W);
		hazardColumns.put("description", X);
		hazardColumns.put("unit", Y);
		hazardColumns.put("adverseEffect", Z);
		hazardColumns.put("sourceOfContamination", AA);
		hazardColumns.put("benchmarkDose", AB);
		hazardColumns.put("maximumResidueLimit", AC);
		hazardColumns.put("noObservedAdverseAffectLevel", AD);
		hazardColumns.put("lowestObservedAdverseAffectLevel", AE);
		hazardColumns.put("acceptableOperatorsExposureLevel", AF);
		hazardColumns.put("acuteReferenceDose", AG);
		hazardColumns.put("acceptableDailyIntake", AH);
		hazardColumns.put("indSum", AI);
	}

	private GenericModelDataBackground retrieveBackground(Sheet sheet) {

		GenericModelDataBackground background = new GenericModelDataBackground();

		try {
			Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (Exception exception) {
			// Skip faulty study and continue
		}

		for (int numrow = BG_STUDY_SAMPLE_ROW; numrow < (BG_STUDY_SAMPLE_ROW + 3); numrow++) {
			try {
				Row row = sheet.getRow(numrow);
				StudySample sample = ImporterUtils.retrieveStudySample(row, sampleColumns);
				background.addStudySampleItem(sample);
			} catch (Exception exception) {
				// Skip faulty sample and continue
			}
		}

		for (int numrow = BG_DIET_ASSESS_ROW; numrow < (BG_DIET_ASSESS_ROW + 3); numrow++) {
			try {
				Row row = sheet.getRow(numrow);
				DietaryAssessmentMethod method = ImporterUtils.retrieveDietaryAssessmentMethod(row, methodColumns);
				background.addDietaryAssessmentMethodItem(method);
			} catch (Exception exception) {
				// Skip faulty method and continue
			}
		}

		for (int numrow = BG_LABORATORY_ROW; numrow < (BG_LABORATORY_ROW + 3); numrow++) {
			try {
				Laboratory laboratory = retrieveLaboratory(sheet.getRow(numrow));
				background.addLaboratoryItem(laboratory);
			} catch (Exception exception) {
				// Skip faulty laboratory and continue
			}
		}

		for (int numrow = BG_ASSAY_ROW; numrow < (BG_ASSAY_ROW + 3); numrow++) {
			try {
				Assay assay = ImporterUtils.retrieveAssay(sheet.getRow(numrow));
				background.addAssayItem(assay);
			} catch (Exception exception) {
				// Skip faulty assay and continue
			}
		}

		return background;
	}

	private GenericModelModelMath retrieveModelMath(Sheet sheet) {

		GenericModelModelMath math = new GenericModelModelMath();

		for (int rownum = MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
			try {
				Row row = sheet.getRow(rownum);
				Parameter param = ImporterUtils.retrieveParameter(row, parameterColumns);
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

		return math;
	}

	private PredictiveModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();

		Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(I);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(I);
		if (sourceCell.getCellTypeEnum() == CellType.STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(I);
		if (identifierCell.getCellTypeEnum() == CellType.STRING) {
			information.setIdentifier(identifierCell.getStringCellValue());
		}

		for (int numRow = GI_CREATOR_ROW; numRow < GI_CREATOR_ROW + 6; numRow++) {
			
			Row row = sheet.getRow(numRow);
			try {
				Contact contact = ImporterUtils.retrieveContact(row, creatorColumns);
				information.addCreatorItem(contact);
			} catch (Exception exception) {
				// Skip faulty contact and continue
			}
			
			try {
				Contact author = ImporterUtils.retrieveContact(row, authorColumns);
				information.addAuthorItem(author);
			} catch (Exception exception) {
				// Skip faulty author and continue
			}
		}

		Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(I);
		if (creationDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			LocalDate localDate = ImporterUtils.retrieveDate(creationDateCell);
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(I);
		if (rightsCell.getCellTypeEnum() == CellType.STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(I);
		if (isAvailableCell.getCellTypeEnum() == CellType.STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(I);
		if (urlCell.getCellTypeEnum() == CellType.STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(I);
		if (formatCell.getCellTypeEnum() == CellType.STRING) {
			information.setFormat(formatCell.getStringCellValue());
		}

		// reference (1..n)
		for (int numRow = this.GI_REFERENCE_ROW; numRow < (this.GI_REFERENCE_ROW + 3); numRow++) {
			try {
				Row row = sheet.getRow(numRow);
				Reference reference = ImporterUtils.retrieveReference(row, referenceColumns);
				information.addReferenceItem(reference);
			} catch (Exception exception) {
				// Skip faulty reference and continue
			}
		}

		Cell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(I);
		if (languageCell.getCellTypeEnum() == CellType.STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}

		Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(I);
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(I);
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

		Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(I);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(I);
		if (objectiveCell.getCellTypeEnum() == CellType.STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(I);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}

	private ExposureModelScope retrieveScope(Sheet sheet) {

		ExposureModelScope scope = new ExposureModelScope();

		for (int numrow = this.SCOPE_PRODHAZPOP_ROW; numrow <= (this.SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

			Row row = sheet.getRow(numrow);

			try {
				scope.addProductItem(ImporterUtils.retrieveProduct(row, productColumns));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addHazardItem(ImporterUtils.retrieveHazard(row, hazardColumns));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addPopulationGroupItem(ImporterUtils.retrievePopulationGroup(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since population groups are optional (*)
			}
		}

		Cell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(I);
		if (generalCommentCell.getCellTypeEnum() == CellType.STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(I);
		if (temporalInformationCell.getCellTypeEnum() == CellType.STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}

	private ModelCategory retrieveModelCategory(Sheet sheet) {
		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getStringCellValue());

		Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(I);
		if (subClassCell.getCellTypeEnum() == CellType.STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(I);
		if (modelClassCommentCell.getCellTypeEnum() == CellType.STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(I);
		if (basicProcessCell.getCellTypeEnum() == CellType.STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}

	private Study retrieveStudy(Sheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		Study study = new Study();

		Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I);
		if (identifierCell.getCellTypeEnum() == CellType.STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getStringCellValue());

		Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(I);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(I);
		if (designTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(I);
		if (measurementTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(I);
		if (technologyTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(I);
		if (technologyPlatformCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(I);
		if (accreditationProcedureCell.getCellTypeEnum() == CellType.STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(I);
		if (protocolNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(I);
		if (protocolTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(I);
		if (protocolDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(I);
		if (protocolURICell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(I);
		if (protocolVersionCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(I);
		if (parameterNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(I);
		if (componentNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(I);
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
	
	// ImporterUtils.retrieveDietaryAssessmentMethod is failing somehow
	// TODO: compare!
	private static Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(K).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(L);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setName(row.getCell(L).getStringCellValue());
		}

		Cell countryCell = row.getCell(M);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setCountry(row.getCell(M).getStringCellValue());
		}

		return laboratory;
	}

	@Override
	public Model retrieveModel(Sheet sheet) {
		ExposureModel exposureModel = new ExposureModel();
		exposureModel.setModelType("exposureModel");
		exposureModel.setGeneralInformation(retrieveGeneralInformation(sheet));
		exposureModel.setScope(retrieveScope(sheet));
		exposureModel.setDataBackground(retrieveBackground(sheet));
		exposureModel.setModelMath(retrieveModelMath(sheet));

		return exposureModel;
	}
}
