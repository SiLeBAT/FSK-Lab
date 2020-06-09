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
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.PredictiveModelScope;
import de.bund.bfr.metadata.swagger.PredictiveModelScopeProduct;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.SwaggerUtil;

public class PredictiveModelSheetImporter implements SheetImporter {

	protected int GENERAL_INFORMATION__NAME = 1;
	protected int GENERAL_INFORMATION__SOURCE = 2;
	protected int GENERAL_INFORMATION__IDENTIFIER = 3;
	protected int GENERAL_INFORMATION_CREATION_DATE = 6;
	protected int GENERAL_INFORMATION__RIGHTS = 8;
	protected int GENERAL_INFORMATION__AVAILABLE = 9;
	protected int GENERAL_INFORMATION__URL = 10;
	protected int GENERAL_INFORMATION__FORMAT = 11;
	protected int GENERAL_INFORMATION__LANGUAGE = 24;
	protected int GENERAL_INFORMATION__SOFTWARE = 25;
	protected int GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN = 26;
	protected int GENERAL_INFORMATION__STATUS = 32;
	protected int GENERAL_INFORMATION__OBJECTIVE = 33;
	protected int GENERAL_INFORMATION__DESCRIPTION = 34;

	protected int MODEL_CATEGORY__MODEL_CLASS = 27;
	protected int MODEL_CATEGORY__MODEL_SUB_CLASS = 28;
	protected int MODEL_CATEGORY__CLASS_COMMENT = 29;
	protected int MODEL_CATEGORY__BASIC_PROCESS = 30;

	protected int QUALITY_MEASURES__SSE = 104;
	protected int QUALITY_MEASURES__MSE = 105;
	protected int QUALITY_MEASURES__RMSE = 106;
	protected int QUALITY_MEASURES__RSQUARE = 107;
	protected int QUALITY_MEASURES__AIC = 108;
	protected int QUALITY_MEASURES__BIC = 109;

	protected int SCOPE__GENERAL_COMMENT = 60;
	protected int SCOPE__TEMPORAL_INFORMATION = 61;

	protected int STUDY__STUDY_IDENTIFIER = 64;
	protected int STUDY__STUDY_TITLE = 65;
	protected int STUDY__STUDY_DESCRIPTION = 66;
	protected int STUDY__STUDY_DESIGN_TYPE = 67;
	protected int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 68;
	protected int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 69;
	protected int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 70;
	protected int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 71;
	protected int STUDY__STUDY_PROTOCOL_NAME = 72;
	protected int STUDY__STUDY_PROTOCOL_TYPE = 73;
	protected int STUDY__STUDY_PROTOCOL_DESCRIPTION = 74;
	protected int STUDY__STUDY_PROTOCOL_URI = 75;
	protected int STUDY__STUDY_PROTOCOL_VERSION = 76;
	protected int STUDY__STUDY_PROTOCOL_PARAMETERS_NAME = 77;
	protected int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 78;
	protected int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 79;

	protected int GI_CREATOR_ROW = 3;
	protected int GI_REFERENCE_ROW = 14;
	protected int SCOPE_PRODHAZPOP_ROW = 38;
	protected int BG_STUDY_SAMPLE_ROW = 83;

	protected int BG_LABORATORY_ROW = 90;
	protected int BG_ASSAY_ROW = 96;
	protected int BG_QUALITY_MEAS_ROW = 104;
	protected int BG_Model_EQ_ROW = 104;

	protected int MM_PARAMETER_ROW = 114;
	protected int MM_FITTING_PROCEDURE_ROW = 130;

	/** Columns for each of the properties of Creator. */
	private final HashMap<String, Integer> creatorColumns;

	/** Columns for each of the properties of Author. */
	private final HashMap<String, Integer> authorColumns;
	
	/** Columns for each of the properties of Reference. */
	private final HashMap<String, Integer> referenceColumns;

	public PredictiveModelSheetImporter() {

		creatorColumns = new HashMap<>();
		creatorColumns.put("mail", S);
		creatorColumns.put("title", L);
		creatorColumns.put("familyName", P);
		creatorColumns.put("givenName", N);
		creatorColumns.put("telephone", R);
		creatorColumns.put("streetAddress", X);
		creatorColumns.put("country", T);
		creatorColumns.put("city", U);
		creatorColumns.put("zipCode", V);
		creatorColumns.put("region", Z);
		creatorColumns.put("organization", Q);

		authorColumns = new HashMap<>();
		authorColumns.put("mail", AI);
		authorColumns.put("title", AB);
		authorColumns.put("familyName", AF);
		authorColumns.put("givenName", AD);
		authorColumns.put("telephone", AH);
		authorColumns.put("streetAddress", AN);
		authorColumns.put("country", AJ);
		authorColumns.put("city", AK);
		authorColumns.put("zipCode", AL);
		authorColumns.put("region", AP);
		authorColumns.put("organization", AG);
		
		referenceColumns = new HashMap<>();
		referenceColumns.put("referenceDescription", L);
		referenceColumns.put("type", M);
		referenceColumns.put("date", N);
		referenceColumns.put("pmid", O);
		referenceColumns.put("doi", P);
		referenceColumns.put("author", Q);
		referenceColumns.put("title", R);
		referenceColumns.put("abstract", S);
		referenceColumns.put("status", U);
		referenceColumns.put("website", V);
		referenceColumns.put("comment", W);
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

		for (int numRow = GI_CREATOR_ROW; numRow < GI_CREATOR_ROW + 6; numRow++) {
			Row row = sheet.getRow(numRow);
			try {
				Contact contact = ImporterUtils.retrieveContact(row, creatorColumns);
				information.addCreatorItem(contact);
			} catch (Exception exception) {
			}

			try {
				Contact author = ImporterUtils.retrieveContact(row, authorColumns);
				information.addAuthorItem(author);
			} catch (Exception exception) {
			}
		}

		Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(J);
		if (creationDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			LocalDate localDate = ImporterUtils.retrieveDate(creationDateCell);
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
			Row row = sheet.getRow(numRow);
			try {
				Reference reference = ImporterUtils.retrieveReference(row, referenceColumns);
				information.addReferenceItem(reference);
			} catch (Exception exception) {
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

	private PredictiveModelModelMath retrieveModelMath(Sheet sheet) {

		PredictiveModelModelMath math = new PredictiveModelModelMath();

		for (int rownum = this.MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
			try {
				Row row = sheet.getRow(rownum);
				Parameter param = retrieveParameter(row);
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

	private PredictiveModelDataBackground retrieveBackground(Sheet sheet) {

		PredictiveModelDataBackground background = new PredictiveModelDataBackground();
		try {
			Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (Exception exception) {
		}

		for (int numrow = this.BG_STUDY_SAMPLE_ROW; numrow < (this.BG_STUDY_SAMPLE_ROW + 3); numrow++) {
			try {
				StudySample sample = retrieveStudySample(sheet.getRow(numrow));
				background.addStudySampleItem(sample);
			} catch (Exception exception) {
			}
		}

		for (int numrow = BG_LABORATORY_ROW; numrow < (BG_LABORATORY_ROW + 3); numrow++) {
			try {
				Laboratory laboratory = retrieveLaboratory(sheet.getRow(numrow));
				background.addLaboratoryItem(laboratory);
			} catch (Exception exception) {
			}
		}

		for (int numrow = BG_ASSAY_ROW; numrow < (BG_ASSAY_ROW + 3); numrow++) {
			try {
				Assay assay = retrieveAssay(sheet.getRow(numrow));
				background.addAssayItem(assay);
			} catch (Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return background;
	}

	private PredictiveModelScope retrieveScope(Sheet sheet) {

		PredictiveModelScope scope = new PredictiveModelScope();

		for (int numrow = SCOPE_PRODHAZPOP_ROW; numrow <= (SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

			Row row = sheet.getRow(numrow);

			try {
				scope.addProductItem(retrieveProduct(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addHazardItem(retrieveHazard(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
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

	private PredictiveModelScopeProduct retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(N).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		PredictiveModelScopeProduct product = new PredictiveModelScopeProduct();
		product.setName(row.getCell(L).getStringCellValue());
		product.setUnit(row.getCell(N).getStringCellValue());

		Cell descriptionCell = row.getCell(M);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		Cell methodCell = row.getCell(O);
		if (methodCell.getCellTypeEnum() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		Cell packagingCell = row.getCell(P);
		if (packagingCell.getCellTypeEnum() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		Cell treatmentCell = row.getCell(Q);
		if (treatmentCell.getCellTypeEnum() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		Cell originCountryCell = row.getCell(R);
		if (originCountryCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		Cell originAreaCell = row.getCell(S);
		if (originAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		Cell fisheriesAreaCell = row.getCell(T);
		if (fisheriesAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		Cell productionDateCell = row.getCell(U);
		if (productionDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			LocalDate date = ImporterUtils.retrieveDate(productionDateCell);
			product.setProductionDate(date);
		}

		Cell expiryDateCell = row.getCell(V);
		if (expiryDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			LocalDate date = ImporterUtils.retrieveDate(expiryDateCell);
			product.setExpiryDate(date);
		}

		return product;
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

	private Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(X).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		Hazard hazard = new Hazard();
		hazard.setName(row.getCell(X).getStringCellValue());

		Cell typeCell = row.getCell(W);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		Cell hazardDescriptionCell = row.getCell(Y);
		if (hazardDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		Cell hazardUnitCell = row.getCell(Z);
		if (hazardUnitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		Cell adverseEffect = row.getCell(AA);
		if (adverseEffect.getCellTypeEnum() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		Cell sourceOfContaminationCell = row.getCell(AB);
		if (sourceOfContaminationCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		Cell bmdCell = row.getCell(AC);
		if (bmdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		Cell maximumResidueLimitCell = row.getCell(AD);
		if (maximumResidueLimitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		Cell noaelCell = row.getCell(AE);
		if (noaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		Cell loaelCell = row.getCell(AF);
		if (loaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		Cell aoelCell = row.getCell(AG);
		if (aoelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		Cell arfdCell = row.getCell(AH);
		if (arfdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		Cell adiCell = row.getCell(AI);
		if (adiCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		Cell indSumCell = row.getCell(AJ);
		if (indSumCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
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

	private StudySample retrieveStudySample(Row row) {

		// Check mandatory properties
		if (row.getCell(L).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		if (row.getCell(M).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		if (row.getCell(Q).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		if (row.getCell(R).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		if (row.getCell(S).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		StudySample sample = new StudySample();
		sample.setSampleName(row.getCell(L).getStringCellValue());
		sample.setProtocolOfSampleCollection(row.getCell(M).getStringCellValue());

		Cell strategyCell = row.getCell(N);
		if (strategyCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		Cell samplingProgramCell = row.getCell(O);
		if (samplingProgramCell.getCellTypeEnum() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		Cell samplingMethodCell = row.getCell(P);
		if (samplingMethodCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(Q).getStringCellValue());
		sample.setSamplingWeight(row.getCell(R).getStringCellValue());
		sample.setSamplingSize(row.getCell(S).getStringCellValue());

		Cell unitCell = row.getCell(T);
		if (unitCell.getCellTypeEnum() == CellType.STRING) {
			sample.setLotSizeUnit(row.getCell(T).getStringCellValue());
		}

		Cell pointCell = row.getCell(U);
		if (pointCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingPoint(row.getCell(U).getStringCellValue());
		}

		return sample;
	}

	private Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(L).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(M);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setName(row.getCell(M).getStringCellValue());
		}

		Cell countryCell = row.getCell(N);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setCountry(row.getCell(N).getStringCellValue());
		}

		return laboratory;
	}

	private Assay retrieveAssay(Row row) {
		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		Assay assay = new Assay();
		assay.setName(row.getCell(L).getStringCellValue());

		Cell descriptionCell = row.getCell(M);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		Cell moistureCell = row.getCell(N);
		if (moistureCell.getCellTypeEnum() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		Cell fatCell = row.getCell(O);
		if (fatCell.getCellTypeEnum() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		Cell detectionCell = row.getCell(P);
		if (detectionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		Cell quantificationCell = row.getCell(Q);
		if (quantificationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		Cell dataCell = row.getCell(R);
		if (dataCell.getCellTypeEnum() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		Cell contaminationCell = row.getCell(S);
		if (contaminationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		Cell uncertaintyCell = row.getCell(T);
		if (uncertaintyCell.getCellTypeEnum() == CellType.STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}

	private Parameter retrieveParameter(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		if (row.getCell(M).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		if (row.getCell(N).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		if (row.getCell(P).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(R).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing data type");
		}

		Parameter param = new Parameter();
		param.setId(row.getCell(L).getStringCellValue());

		ParameterClassification pc = ParameterClassification.get(row.getCell(M).getStringCellValue());
		if (pc != null) {
			param.setClassification(SwaggerUtil.CLASSIF.get(pc));
		}

		param.setName(row.getCell(N).getStringCellValue());

		Cell descriptionCell = row.getCell(O);
		if (descriptionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		param.setUnit(row.getCell(P).getStringCellValue());

		Cell unitCategoryCell = row.getCell(Q);
		if (unitCategoryCell.getCellTypeEnum() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		ParameterType parameterType = ParameterType.get(row.getCell(R).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		Cell sourceCell = row.getCell(S);
		if (sourceCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		Cell subjectCell = row.getCell(T);
		if (subjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		Cell distributionCell = row.getCell(U);
		if (distributionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		Cell valueCell = row.getCell(V);
		if (valueCell.getCellTypeEnum() != CellType.BLANK) {

			if (valueCell.getCellTypeEnum() == CellType.NUMERIC) {
				Double doubleValue = valueCell.getNumericCellValue();
				if (parameterType == ParameterType.INTEGER) {
					param.setValue(Integer.toString(doubleValue.intValue()));
				} else if (parameterType == ParameterType.DOUBLE || parameterType == ParameterType.NUMBER) {
					param.setValue(Double.toString(doubleValue));
				}
			} else {
				param.setValue(valueCell.getStringCellValue());
			}
		}

		// TODO: reference

		Cell variabilitySubjectCell = row.getCell(X);
		if (variabilitySubjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		Cell maxCell = row.getCell(Y);
		if (maxCell.getCellTypeEnum() != CellType.BLANK) {
			if (maxCell.getCellTypeEnum() != CellType.STRING)
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			else
				param.setMaxValue(maxCell.getStringCellValue());

		}

		Cell minCell = row.getCell(Z);
		if (minCell.getCellTypeEnum() != CellType.BLANK) {
			if (minCell.getCellTypeEnum() != CellType.STRING)
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			else
				param.setMinValue(minCell.getStringCellValue());
		}

		Cell errorCell = row.getCell(AA);
		if (errorCell.getCellTypeEnum() != CellType.BLANK) {
			if (errorCell.getCellTypeEnum() != CellType.STRING)
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			else
				param.setError(errorCell.getStringCellValue());
		}
		return param;
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
		PredictiveModel model = new PredictiveModel();
		model.setModelType("predictiveModel");
		model.setGeneralInformation(retrieveGeneralInformation(sheet));
		model.setScope(retrieveScope(sheet));
		model.setDataBackground(retrieveBackground(sheet));
		model.setModelMath(retrieveModelMath(sheet));

		return model;
	}
}
