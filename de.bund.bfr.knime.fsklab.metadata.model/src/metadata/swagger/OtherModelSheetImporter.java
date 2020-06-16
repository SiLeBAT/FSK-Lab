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
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.OtherModelDataBackground;
import de.bund.bfr.metadata.swagger.OtherModelGeneralInformation;
import de.bund.bfr.metadata.swagger.OtherModelModelMath;
import de.bund.bfr.metadata.swagger.OtherModelScope;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

public class OtherModelSheetImporter implements SheetImporter {

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

	protected int QUALITY_MEASURES__SSE = 117;
	protected int QUALITY_MEASURES__MSE = 118;
	protected int QUALITY_MEASURES__RMSE = 119;
	protected int QUALITY_MEASURES__RSQUARE = 120;
	protected int QUALITY_MEASURES__AIC = 121;
	protected int QUALITY_MEASURES__BIC = 122;
	protected int SCOPE__GENERAL_COMMENT = 73;
	protected int SCOPE__TEMPORAL_INFORMATION = 74;

	protected int STUDY__STUDY_IDENTIFIER = 77;
	protected int STUDY__STUDY_TITLE = 78;
	protected int STUDY__STUDY_DESCRIPTION = 79;
	protected int STUDY__STUDY_DESIGN_TYPE = 80;
	protected int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 81;
	protected int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 82;
	protected int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 83;
	protected int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 84;
	protected int STUDY__STUDY_PROTOCOL_NAME = 85;
	protected int STUDY__STUDY_PROTOCOL_TYPE = 86;
	protected int STUDY__STUDY_PROTOCOL_DESCRIPTION = 87;
	protected int STUDY__STUDY_PROTOCOL_URI = 88;
	protected int STUDY__STUDY_PROTOCOL_VERSION = 89;
	protected int STUDY__STUDY_PROTOCOL_PARAMETERS_NAME = 90;
	protected int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 91;
	protected int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 92;

	protected int GI_CREATOR_ROW = 3;
	protected int GI_REFERENCE_ROW = 14;
	protected int SCOPE_PRODHAZPOP_ROW = 38;
	protected int BG_STUDY_SAMPLE_ROW = 96;
	protected int BG_DIET_ASSESS_ROW = 102;
	protected int BG_EVENT_ROW = 108;
	protected int BG_LABORATORY_ROW = 103;
	protected int BG_ASSAY_ROW = 109;
	protected int BG_QUALITY_MEAS_ROW = 117;
	protected int BG_Model_EQ_ROW = 117;

	protected int MM_PARAMETER_ROW = 127;
	protected int MM_FITTING_PROCEDURE_ROW = 132;

	/** Columns for each of the properties of Creator. */
	private final HashMap<String, Integer> creatorColumns;

	/** Columns for each of the properties of Creator. */
	private final HashMap<String, Integer> authorColumns;

	/** Columns for each of the properties of Reference. */
	private final HashMap<String, Integer> referenceColumns;

	/** Columns for each of the properties of Parameter. */
	private final HashMap<String, Integer> parameterColumns;

	/** Columns for each of the properties of StudySample. */
	private final HashMap<String, Integer> sampleColumns;
	
	/** Columns for each of the properties of Product. */
	private final HashMap<String, Integer> productColumns;
	
	/** Columns for each of the properties of Hazard. */
	private final HashMap<String, Integer> hazardColumns;
	
	public OtherModelSheetImporter() {

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
		authorColumns.put("title", AB);
		authorColumns.put("name", AC);
		authorColumns.put("givenName", AD);
		authorColumns.put("additionalName", AE);
		authorColumns.put("familyName", AF);
		authorColumns.put("organization", AG);
		authorColumns.put("telephone", AH);
		authorColumns.put("mail", AI);
		authorColumns.put("country", AJ);
		authorColumns.put("city", AK);
		authorColumns.put("zipCode", AL);
		authorColumns.put("postOfficeBox", AM);
		authorColumns.put("streetAddress", AN);
		authorColumns.put("extendedAddress", AO);
		authorColumns.put("region", AP);

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

		parameterColumns = new HashMap<>();
		parameterColumns.put("id", L);
		parameterColumns.put("classification", M);
		parameterColumns.put("name", N);
		parameterColumns.put("description", O);
		parameterColumns.put("unit", P);
		parameterColumns.put("unitCategory", Q);
		parameterColumns.put("dataType", R);
		parameterColumns.put("source", S);
		parameterColumns.put("subject", T);
		parameterColumns.put("distribution", U);
		parameterColumns.put("value", V);
		parameterColumns.put("reference", W);
		parameterColumns.put("variability", X);
		parameterColumns.put("max", Y);
		parameterColumns.put("min", Z);
		parameterColumns.put("error", AA);
		
		sampleColumns = new HashMap<>();
		sampleColumns.put("sample", L);
		sampleColumns.put("protocolOfSampleCollection", M);
		sampleColumns.put("samplingStrategy", N);
		sampleColumns.put("samplingProgramType", O);
		sampleColumns.put("samplingMethod", P);
		sampleColumns.put("samplingPlan", Q);
		sampleColumns.put("samplingWeight", R);
		sampleColumns.put("samplingSize", S);
		sampleColumns.put("lotSizeUnit", T);
		sampleColumns.put("samplingPoint", U);
		
		productColumns = new HashMap<>();
		productColumns.put("name", L);
		productColumns.put("description", M);
		productColumns.put("unit", N);
		productColumns.put("productionMethod", O);
		productColumns.put("packaging", P);
		productColumns.put("treatment", Q);
		productColumns.put("originCountry", R);
		productColumns.put("originArea", S);
		productColumns.put("fisheriesArea", T);
		productColumns.put("productionDate", U);
		productColumns.put("expiryDate", V);
		
		hazardColumns = new HashMap<>();
		hazardColumns.put("type", W);
		hazardColumns.put("name", X);
		hazardColumns.put("description", Y);
		hazardColumns.put("unit", Z);
		hazardColumns.put("adverseEffect", AA);
		hazardColumns.put("sourceOfContamination", AB);
		hazardColumns.put("benchmarkDose", AC);
		hazardColumns.put("maximumResidueLimit", AD);
		hazardColumns.put("noObservedAdverseAffectLevel", AE);
		hazardColumns.put("lowestObservedAdverseAffectLevel", AF);
		hazardColumns.put("acceptableOperatorsExposureLevel", AG);
		hazardColumns.put("acuteReferenceDose", AH);
		hazardColumns.put("acceptableDailyIntake", AI);
		hazardColumns.put("indSum", AJ);
	}

	private OtherModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		OtherModelGeneralInformation information = new OtherModelGeneralInformation();

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
				Contact author = ImporterUtils.retrieveContact(row, authorColumns);
				information.addAuthorItem(author);
			} catch (Exception exception) {
			}

			try {
				Contact creator = ImporterUtils.retrieveContact(row, creatorColumns);
				information.addCreatorItem(creator);
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
		for (int numRow = this.GI_REFERENCE_ROW; numRow < (GI_REFERENCE_ROW + 3); numRow++) {
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

	private OtherModelModelMath retrieveModelMath(Sheet sheet) {

		OtherModelModelMath math = new OtherModelModelMath();

		for (int rownum = this.MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
			Row row = sheet.getRow(rownum);
			try {
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

		Cell fittingProcedureCell = sheet.getRow(MM_FITTING_PROCEDURE_ROW).getCell(J);
		if (fittingProcedureCell.getCellTypeEnum() == CellType.STRING) {
			math.setFittingProcedure(fittingProcedureCell.getStringCellValue());
		}

		return math;
	}

	private OtherModelDataBackground retrieveBackground(Sheet sheet) {

		OtherModelDataBackground background = new OtherModelDataBackground();

		try {
			Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (Exception exception) {
		}

		for (int numrow = this.BG_STUDY_SAMPLE_ROW; numrow < (BG_STUDY_SAMPLE_ROW + 3); numrow++) {
			try {
				Row row = sheet.getRow(numrow);
				StudySample sample = ImporterUtils.retrieveStudySample(row, sampleColumns);
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

	private OtherModelScope retrieveScope(Sheet sheet) {

		OtherModelScope scope = new OtherModelScope();

		for (int numrow = SCOPE_PRODHAZPOP_ROW; numrow <= (SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

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
				scope.addPopulationGroupItem(retrievePopulationGroup(row));
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

	private PopulationGroup retrievePopulationGroup(Row row) {

		// Check mandatory properties
		if (row.getCell(AK).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();

		Cell nameCell = row.getCell(AK);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		Cell targetPopulationCell = row.getCell(AL);
		if (targetPopulationCell.getCellTypeEnum() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(AM);
		if (spanCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(AN);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(AO);
		if (ageCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(AP);
		if (genderCell.getCellTypeEnum() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(AQ);
		if (bmiCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(AR);
		if (dietCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(AS);
		if (consumptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(AT);
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(AU);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(AV);
		if (factorsCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(AW);
		if (seasonCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}

	@Override
	public Model retrieveModel(Sheet sheet) {
		OtherModel model = new OtherModel();
		model.setModelType("otherModel");
		model.setGeneralInformation(retrieveGeneralInformation(sheet));
		model.setScope(retrieveScope(sheet));
		model.setDataBackground(retrieveBackground(sheet));
		model.setModelMath(retrieveModelMath(sheet));

		return model;
	}
}
