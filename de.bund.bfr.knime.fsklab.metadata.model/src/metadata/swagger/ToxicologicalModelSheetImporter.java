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
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModelScope;

public class ToxicologicalModelSheetImporter implements SheetImporter {

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

	protected int QUALITY_MEASURES__SSE = 106;
	protected int QUALITY_MEASURES__MSE = 107;
	protected int QUALITY_MEASURES__RMSE = 108;
	protected int QUALITY_MEASURES__RSQUARE = 109;
	protected int QUALITY_MEASURES__AIC = 110;
	protected int QUALITY_MEASURES__BIC = 111;

	protected int SCOPE__GENERAL_COMMENT = 62;
	protected int SCOPE__TEMPORAL_INFORMATION = 63;

	protected int STUDY__STUDY_IDENTIFIER = 66;
	protected int STUDY__STUDY_TITLE = 67;
	protected int STUDY__STUDY_DESCRIPTION = 68;
	protected int STUDY__STUDY_DESIGN_TYPE = 69;
	protected int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 70;
	protected int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 71;
	protected int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 72;
	protected int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 73;
	protected int STUDY__STUDY_PROTOCOL_NAME = 74;
	protected int STUDY__STUDY_PROTOCOL_TYPE = 75;
	protected int STUDY__STUDY_PROTOCOL_DESCRIPTION = 76;
	protected int STUDY__STUDY_PROTOCOL_URI = 77;
	protected int STUDY__STUDY_PROTOCOL_VERSION = 78;
	protected int STUDY__STUDY_PROTOCOL_PARAMETERS_NAME = 79;
	protected int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 80;
	protected int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 81;

	protected int GI_CREATOR_ROW = 3;
	protected int GI_REFERENCE_ROW = 14;
	protected int SCOPE_PRODHAZPOP_ROW = 38;
	protected int BG_STUDY_SAMPLE_ROW = 85;
	protected int BG_EVENT_ROW = 91;
	protected int BG_LABORATORY_ROW = 92;
	protected int BG_ASSAY_ROW = 98;
	protected int BG_QUALITY_MEAS_ROW = 106;
	protected int BG_Model_EQ_ROW = 106;

	protected int MM_PARAMETER_ROW = 116;
	protected int MM_FITTING_PROCEDURE_ROW = 132;

	/** Columns for each of the properties of Creator. */
	private final HashMap<String, Integer> creatorColumns;

	/** Columns for each of the properties of Author. */
	private final HashMap<String, Integer> authorColumns;

	/** Columns for each of the properties of Reference. */
	private final HashMap<String, Integer> referenceColumns;
	
	/** Columns for each of the properties of Parameter. */
	private final HashMap<String, Integer> parameterColumns;

	/** Columns for each of the properties of StudySample. */
	private final HashMap<String, Integer> sampleColumns;
	
	/** Columns for each of the properties of Hazard. */
	private final HashMap<String, Integer> hazardColumns;
	
	/** Columns for each of the properties of PopulationGroup. */
	private final HashMap<String, Integer> populationColumns;
	
	/** Columns for each of the properties of Assay. */
	private final HashMap<String, Integer> assayColumns;
	
	public ToxicologicalModelSheetImporter() {

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
		
		hazardColumns = new HashMap<>();
		hazardColumns.put("type", L);
		hazardColumns.put("name", M);
		hazardColumns.put("description", N);
		hazardColumns.put("unit", O);
		hazardColumns.put("adverseEffect", P);
		hazardColumns.put("sourceOfContamination", Q);
		hazardColumns.put("benchmarkDose", R);
		hazardColumns.put("maximumResidueLimit", S);
		hazardColumns.put("noObservedAdverseAffectLevel", T);
		hazardColumns.put("lowestObservedAdverseAffectLevel", U);
		hazardColumns.put("acceptableOperatorsExposureLevel", V);
		hazardColumns.put("acuteReferenceDose", W);
		hazardColumns.put("acceptableDailyIntake", X);
		hazardColumns.put("indSum", Y);
		
		assayColumns = new HashMap<>();
		assayColumns.put("name", L);
		assayColumns.put("description", M);
		assayColumns.put("moisturePercentage", N);
		assayColumns.put("fatPercentage", O);
		assayColumns.put("detectionLimit", P);
		assayColumns.put("quantificationLimit", Q);
		assayColumns.put("leftCensoredData", R);
		assayColumns.put("contaminationRange", S);
		assayColumns.put("uncertaintyValue", T);
		
		populationColumns = new HashMap<>();
		populationColumns.put("name", Z);
		populationColumns.put("targetPopulation", AA);
		populationColumns.put("span", AB);
		populationColumns.put("description", AC);
		populationColumns.put("age", AD);
		populationColumns.put("gender", AE);
		populationColumns.put("bmi", AF);
		populationColumns.put("diet", AG);
		populationColumns.put("consumption", AH);
		populationColumns.put("region", AI);
		populationColumns.put("country", AJ);
		populationColumns.put("risk", AK);
		populationColumns.put("season", AL);
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
				Row row = sheet.getRow(numrow);
				Assay assay = ImporterUtils.retrieveAssay(row, assayColumns);
				background.addAssayItem(assay);
			} catch (Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return background;
	}

	private GenericModelModelMath retrieveModelMath(Sheet sheet) {

		final GenericModelModelMath math = new GenericModelModelMath();

		for (int rownum = MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
			try {
				final Row row = sheet.getRow(rownum);
				final Parameter param = ImporterUtils.retrieveParameter(row, parameterColumns);
				math.addParameterItem(param);
			} catch (final Exception exception) {
				// ...
			}
		}

		try {
			final QualityMeasures measures = retrieveQualityMeasures(sheet);
			math.addQualityMeasuresItem(measures);
		} catch (final Exception exception) {
			// ...
		}

		Cell fittingProcedureCell = sheet.getRow(MM_FITTING_PROCEDURE_ROW).getCell(J);
		if (fittingProcedureCell.getCellType() == CellType.STRING) {
			math.setFittingProcedure(fittingProcedureCell.getStringCellValue());
		}

		return math;

	}

	private PredictiveModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();

		Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(J);
		if (nameCell.getCellType() == CellType.STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(J);
		if (sourceCell.getCellType() == CellType.STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(J);
		if (identifierCell.getCellType() == CellType.STRING) {
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
				Contact contact = ImporterUtils.retrieveContact(row, creatorColumns);
				information.addCreatorItem(contact);
			} catch (Exception exception) {
			}
		}

		Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(J);
		if (creationDateCell.getCellType() == CellType.NUMERIC) {
			LocalDate localDate = ImporterUtils.retrieveDate(creationDateCell);
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(J);
		if (rightsCell.getCellType() == CellType.STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(J);
		if (isAvailableCell.getCellType() == CellType.STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(J);
		if (urlCell.getCellType() == CellType.STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(J);
		if (formatCell.getCellType() == CellType.STRING) {
			information.setFormat(formatCell.getStringCellValue());
		}

		// reference (1..n)
		for (int numRow = this.GI_REFERENCE_ROW; numRow < (this.GI_REFERENCE_ROW + 3); numRow++) {
			try {
				Row row = sheet.getRow(numRow);
				Reference reference = ImporterUtils.retrieveReference(row, referenceColumns);
				information.addReferenceItem(reference);
			} catch (Exception exception) {
			}
		}

		Cell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(J);
		if (languageCell.getCellType() == CellType.STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}

		Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(J);
		if (softwareCell.getCellType() == CellType.STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(J);
		if (languageWrittenInCell.getCellType() == CellType.STRING) {
			information.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		// model category (0..n)
		try {
			ModelCategory category = retrieveModelCategory(sheet);
			information.setModelCategory(category);
		} catch (Exception exception) {
		}

		Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(J);
		if (statusCell.getCellType() == CellType.STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(J);
		if (objectiveCell.getCellType() == CellType.STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(J);
		if (descriptionCell.getCellType() == CellType.STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}

	private ToxicologicalModelScope retrieveScope(Sheet sheet) {
		ToxicologicalModelScope scope = new ToxicologicalModelScope();

		for (int numrow = this.SCOPE_PRODHAZPOP_ROW; numrow <= (SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

			Row row = sheet.getRow(numrow);

			try {
				scope.addHazardItem(ImporterUtils.retrieveHazard(row, hazardColumns));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addPopulationGroupItem(ImporterUtils.retrievePopulationGroup(row, populationColumns));
			} catch (IllegalArgumentException exception) {
				// ignore exception since population groups are optional (*)
			}
		}

		Cell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(J);
		if (generalCommentCell.getCellType() == CellType.STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(J);
		if (temporalInformationCell.getCellType() == CellType.STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;

	}

	private ModelCategory retrieveModelCategory(Sheet sheet) {
		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(J).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(J).getStringCellValue());

		Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(J);
		if (subClassCell.getCellType() == CellType.STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(J);
		if (modelClassCommentCell.getCellType() == CellType.STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(J);
		if (basicProcessCell.getCellType() == CellType.STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}

	private Study retrieveStudy(Sheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_TITLE).getCell(J).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		Study study = new Study();

		Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(J);
		if (identifierCell.getCellType() == CellType.STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(J).getStringCellValue());

		Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(J);
		if (descriptionCell.getCellType() == CellType.STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(J);
		if (designTypeCell.getCellType() == CellType.STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(J);
		if (measurementTypeCell.getCellType() == CellType.STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(J);
		if (technologyTypeCell.getCellType() == CellType.STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(J);
		if (technologyPlatformCell.getCellType() == CellType.STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(J);
		if (accreditationProcedureCell.getCellType() == CellType.STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(J);
		if (protocolNameCell.getCellType() == CellType.STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(J);
		if (protocolTypeCell.getCellType() == CellType.STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(J);
		if (protocolDescriptionCell.getCellType() == CellType.STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(J);
		if (protocolURICell.getCellType() == CellType.STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(J);
		if (protocolVersionCell.getCellType() == CellType.STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(J);
		if (parameterNameCell.getCellType() == CellType.STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(J);
		if (componentNameCell.getCellType() == CellType.STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(J);
		if (componentTypeCell.getCellType() == CellType.STRING) {
			study.setProtocolComponentsType(componentTypeCell.getStringCellValue());
		}

		return study;
	}

	private Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(L).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(M);
		if (nameCell.getCellType() == CellType.STRING) {
			laboratory.setName(row.getCell(M).getStringCellValue());
		}

		Cell countryCell = row.getCell(N);
		if (countryCell.getCellType() == CellType.STRING) {
			laboratory.setCountry(row.getCell(N).getStringCellValue());
		}

		return laboratory;
	}

	private QualityMeasures retrieveQualityMeasures(Sheet sheet) {
		QualityMeasures measures = new QualityMeasures();

		Cell sseCell = sheet.getRow(QUALITY_MEASURES__SSE).getCell(M);
		if (sseCell.getCellType() == CellType.NUMERIC) {
			measures.setSse(BigDecimal.valueOf(sseCell.getNumericCellValue()));
		}

		Cell mseCell = sheet.getRow(QUALITY_MEASURES__MSE).getCell(M);
		if (mseCell.getCellType() == CellType.NUMERIC) {
			measures.setMse(BigDecimal.valueOf(mseCell.getNumericCellValue()));
		}

		Cell rmseCell = sheet.getRow(QUALITY_MEASURES__RMSE).getCell(M);
		if (rmseCell.getCellType() == CellType.NUMERIC) {
			measures.setRmse(BigDecimal.valueOf(rmseCell.getNumericCellValue()));
		}

		Cell rsquareCell = sheet.getRow(QUALITY_MEASURES__RSQUARE).getCell(M);
		if (rsquareCell.getCellType() == CellType.NUMERIC) {
			measures.setRsquared(BigDecimal.valueOf(rsquareCell.getNumericCellValue()));
		}

		Cell aicCell = sheet.getRow(QUALITY_MEASURES__AIC).getCell(M);
		if (aicCell.getCellType() == CellType.NUMERIC) {
			measures.setAic(BigDecimal.valueOf(aicCell.getNumericCellValue()));
		}

		Cell bicCell = sheet.getRow(QUALITY_MEASURES__BIC).getCell(M);
		if (bicCell.getCellType() == CellType.NUMERIC) {
			measures.setBic(BigDecimal.valueOf(bicCell.getNumericCellValue()));
		}

		return measures;
	}

	@Override
	public Model retrieveModel(Sheet sheet) {
		ToxicologicalModel model = new ToxicologicalModel();
		model.setModelType("toxicologicalModel");
		model.setGeneralInformation(retrieveGeneralInformation(sheet));
		model.setScope(retrieveScope(sheet));
		model.setDataBackground(retrieveBackground(sheet));
		model.setModelMath(retrieveModelMath(sheet));

		return model;
	}
}
