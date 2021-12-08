package metadata;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DoseResponseModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

@Deprecated
public class SwaggerDoseResponseSheetImporter{
	
	 protected int A = 0;
	 protected int B = 1;
	 protected int C = 2;
	 protected int D = 3;
	 protected int E = 4;
	 protected int F = 5;
	 protected int G = 6;
	 protected int H = 7;
	 protected int I = 8;
	 protected int J = 9;
	 protected int K = 10;
	 protected int L = 11;
	 protected int M = 12;
	 protected int N = 13;
	 protected int O = 14;
	 protected int P = 15;
	 protected int Q = 16;
	 protected int R = 17;
	 protected int S = 18;
	 protected int T = 19;
	 protected int U = 20;
	 protected int V = 21;
	 protected int W = 22;
	 protected int X = 23;
	 protected int Y = 24;
	 protected int Z = 25;
	 protected int AA = 26;
	 protected int AB = 27;
	 protected int AC = 28;
	 protected int AD = 29;
	 protected int AE = 30;
	 protected int AF = 31;
	 protected int AG = 32;
	 protected int AH = 33;
	 protected int AI = 34;
	 protected int AJ = 35;
	 protected int AK = 36;
	 protected int AL = 37;
	 protected int AM = 38;
	 protected int AN = 39;
	 protected int AO = 40;
	 protected int AP = 41;
	 protected int AQ = 42;
	 protected int AR = 43;
	 protected int AS = 44;
	 protected int AT = 45;
	 protected int AU = 46;
	 protected int AV = 47;

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

	 protected int QUALITY_MEASURES__SSE = 107;
	 protected int QUALITY_MEASURES__MSE = 108;
	 protected int QUALITY_MEASURES__RMSE = 109;
	 protected int QUALITY_MEASURES__RSQUARE = 110;
	 protected int QUALITY_MEASURES__AIC = 111;
	 protected int QUALITY_MEASURES__BIC = 112;

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
	 protected int BG_ASSAY_ROW = 99;
	 protected int BG_QUALITY_MEAS_ROW = 107;
	 protected int BG_Model_EQ_ROW = 107;

	 protected int MM_PARAMETER_ROW = 116;
	
	
	public DoseResponseModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		DoseResponseModelGeneralInformation information = new DoseResponseModelGeneralInformation();

		Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(I);
		if (nameCell.getCellType() == CellType.STRING) {
			information.setModelName(nameCell.getStringCellValue());
		}

		Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(I);
		if (sourceCell.getCellType() == CellType.STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == CellType.STRING) {
			information.setIdentifier(identifierCell.getStringCellValue());
		}

		
		try {
			Contact author = retrieveAuthor(sheet.getRow(GI_CREATOR_ROW));
			information.addAuthorItem(author);
		} catch (Exception exception) {
		}

		for (int numRow = GI_CREATOR_ROW; numRow < (GI_CREATOR_ROW + 4); numRow++) {
			try {
				Contact contact = retrieveCreator(sheet.getRow(numRow));
				information.addCreatorItem(contact);
			} catch (Exception exception) {
			}
		}

		Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(I);
		if (creationDateCell.getCellType() == CellType.NUMERIC) {
			Date creationDate = creationDateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(creationDate.getYear() + 1900, creationDate.getMonth() + 1,
					creationDate.getDate());
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(I);
		if (rightsCell.getCellType() == CellType.STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(I);
		if (isAvailableCell.getCellType() == CellType.STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(I);
		if (urlCell.getCellType() == CellType.STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(I);
		if (formatCell.getCellType() == CellType.STRING) {
			information.setFormat(formatCell.getStringCellValue());
		}

		// reference (1..n)
		for (int numRow = GI_REFERENCE_ROW; numRow < (GI_REFERENCE_ROW + 3); numRow++) {
			try {
				Reference reference = retrieveReference(sheet.getRow(numRow));
				information.addReferenceItem(reference);
			} catch (Exception exception) {
			}
		}

		Cell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(I);
		if (languageCell.getCellType() == CellType.STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}

		Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(I);
		if (softwareCell.getCellType() == CellType.STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(I);
		if (languageWrittenInCell.getCellType() == CellType.STRING) {
			information.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		// model category (0..n)
		try {
			ModelCategory category = retrieveModelCategory(sheet);
			information.setModelCategory(category);
		} catch (Exception exception) {
		}

		Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(I);
		if (statusCell.getCellType() == CellType.STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(I);
		if (objectiveCell.getCellType() == CellType.STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == CellType.STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}
	public PredictiveModelDataBackground retrieveBackground(Sheet sheet) {
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

		

		for (int numrow = this.BG_LABORATORY_ROW; numrow < (this.BG_LABORATORY_ROW + 3); numrow++) {
			try {
				Laboratory laboratory = retrieveLaboratory(sheet.getRow(numrow));
				background.addLaboratoryItem(laboratory);
			} catch (Exception exception) {
			}
		}

		for (int numrow = this.BG_ASSAY_ROW; numrow < (this.BG_ASSAY_ROW +3); numrow++) {
			try {
				Assay assay = retrieveAssay(sheet.getRow(numrow));
				background.addAssayItem(assay);
			} catch (Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return background;
	}
	public DoseResponseModelScope retrieveScope(Sheet sheet) {

		DoseResponseModelScope scope = new DoseResponseModelScope();
		//col_offset = 12;
		
		for (int numrow = SCOPE_PRODHAZPOP_ROW; numrow <= (SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

			Row row = sheet.getRow(numrow);

			

			try {
				scope.addHazardItem(retrieveHazard(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addPopulationGroupItem(retrievePopulationGroup(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since population groups are optional (*)
			}
		}
		

		Cell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(I);
		if (generalCommentCell.getCellType() == CellType.STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(I);
		if (temporalInformationCell.getCellType() == CellType.STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}
	
	public DoseResponseModelModelMath retrieveModelMath(Sheet sheet) {
		
	
		DoseResponseModelModelMath math = new DoseResponseModelModelMath();
		
		for (int rownum = MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
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

		return math;
	}

	public Contact retrieveCreator(Row row) {
		@SuppressWarnings("serial")
		HashMap<String, Integer> columns = new HashMap<String, Integer>() {
			{
				put("mail", R);
				put("title", K);
				put("familyName", O);
				put("givenName", M);
				put("telephone", Q);
				put("streetAddress", W);
				put("country", S);
				put("city", T);
				put("zipCode", U);
				put("region", Y);
				put("organization", P);
			}
		};
		return retrieveContact(row, columns);
	}

	public Contact retrieveAuthor(Row row) {

		@SuppressWarnings("serial")
		HashMap<String, Integer> columns = new HashMap<String, Integer>() {
			{
				put("mail", AH);
				put("title", AA);
				put("familyName", AE);
				put("givenName", AC);
				put("telephone", AG);
				put("streetAddress", AM);
				put("country", AI);
				put("city", AJ);
				put("zipCode", AK);
				put("region", AO);
				put("organization", AF);
			}
		};
		return retrieveContact(row, columns);
	}

	private Contact retrieveContact(Row row, Map<String, Integer> columns) {

	    // Check mandatory properties and throw exception if missing
		if (row.getCell(columns.get("mail")).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing mail");
		}

		Contact contact = new Contact();
		contact.setEmail(row.getCell(columns.get("mail")).getStringCellValue());

		Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellType() == CellType.STRING) {
			contact.setTitle(titleCell.getStringCellValue());
		}

		Cell familyNameCell = row.getCell(columns.get("familyName"));
		if (familyNameCell.getCellType() == CellType.STRING) {
			contact.setFamilyName(familyNameCell.getStringCellValue());
		}

		Cell givenNameCell = row.getCell(columns.get("givenName"));
		if (givenNameCell.getCellType() == CellType.STRING) {
			contact.setGivenName(givenNameCell.getStringCellValue());
		}

		Cell telephoneCell = row.getCell(columns.get("telephone"));
		if (telephoneCell.getCellType() == CellType.STRING) {
			contact.setTelephone(telephoneCell.getStringCellValue());
		}

		Cell streetAddressCell = row.getCell(columns.get("streetAddress"));
		if (streetAddressCell.getCellType() == CellType.STRING) {
			contact.setStreetAddress(streetAddressCell.getStringCellValue());
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellType() == CellType.STRING) {
			contact.setCountry(countryCell.getStringCellValue());
		}

		Cell zipCodeCell = row.getCell(columns.get("zipCode"));
		if (zipCodeCell.getCellType() == CellType.STRING) {
			contact.setZipCode(zipCodeCell.getStringCellValue());
		}

		Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellType() == CellType.STRING) {
			contact.setRegion(regionCell.getStringCellValue());
		}

		// Time zone not included in spreadsheet
		// gender not included in spreadsheet
		// note not included in spreadsheet

		Cell organizationCell = row.getCell(columns.get("organization"));
		if (organizationCell.getCellType() == CellType.STRING) {
			contact.setOrganization(organizationCell.getStringCellValue());
		}

		return contact;
	}

	public Reference retrieveReference(Row row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(K).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(O).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(O).getStringCellValue());

		// publication type
		Cell typeCell = row.getCell(L);
		if (typeCell.getCellType() == CellType.STRING) {
			PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			if (type != null) {
				reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
			}
		}

		Cell dateCell = row.getCell(M);
		if (dateCell.getCellType() == CellType.NUMERIC) {
		  reference.setDate(dateCell.getStringCellValue());
		}

		Cell pmidCell = row.getCell(N);
		if (pmidCell.getCellType() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		Cell authorListCell = row.getCell(P);
		if (authorListCell.getCellType() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		Cell titleCell = row.getCell(Q);
		if (titleCell.getCellType() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		Cell abstractCell = row.getCell(R);
		if (abstractCell.getCellType() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		Cell statusCell = row.getCell(T);
		if (statusCell.getCellType() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		Cell websiteCell = row.getCell(U);
		if (websiteCell.getCellType() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		Cell commentCell = row.getCell(V);
		if (commentCell.getCellType() == CellType.STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	public ModelCategory retrieveModelCategory(Sheet sheet) {
		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getStringCellValue());

		Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(I);
		if (subClassCell.getCellType() == CellType.STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(I);
		if (modelClassCommentCell.getCellType() == CellType.STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(I);
		if (basicProcessCell.getCellType() == CellType.STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}


	public Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(K).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		Hazard hazard = new Hazard();
		hazard.setName(row.getCell(K).getStringCellValue());

		Cell typeCell = row.getCell(L);
		if (typeCell.getCellType() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		Cell hazardDescriptionCell = row.getCell(M);
		if (hazardDescriptionCell.getCellType() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		Cell hazardUnitCell = row.getCell(N);
		if (hazardUnitCell.getCellType() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		Cell adverseEffect = row.getCell(O);
		if (adverseEffect.getCellType() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		Cell sourceOfContaminationCell = row.getCell(P);
		if (sourceOfContaminationCell.getCellType() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		Cell bmdCell = row.getCell(Q);
		if (bmdCell.getCellType() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		Cell maximumResidueLimitCell = row.getCell(R);
		if (maximumResidueLimitCell.getCellType() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		Cell noaelCell = row.getCell(S);
		if (noaelCell.getCellType() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		Cell loaelCell = row.getCell(T);
		if (loaelCell.getCellType() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		Cell aoelCell = row.getCell(U);
		if (aoelCell.getCellType() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		Cell arfdCell = row.getCell(V);
		if (arfdCell.getCellType() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		Cell adiCell = row.getCell(W);
		if (adiCell.getCellType() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		Cell indSumCell = row.getCell(X);
		if (indSumCell.getCellType() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	public PopulationGroup retrievePopulationGroup(Row row) {
		
		// Check mandatory properties
		if (row.getCell(Y).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();

		Cell nameCell = row.getCell(Y);
		if (nameCell.getCellType() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		Cell targetPopulationCell = row.getCell(Z);
		if (targetPopulationCell.getCellType() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(AA);
		if (spanCell.getCellType() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(AB);
		if (descriptionCell.getCellType() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(AC);
		if (ageCell.getCellType() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(AD);
		if (genderCell.getCellType() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(AE);
		if (bmiCell.getCellType() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(AF);
		if (dietCell.getCellType() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(AG);
		if (consumptionCell.getCellType() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(AH);
		if (regionCell.getCellType() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(AI);
		if (countryCell.getCellType() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(AJ);
		if (factorsCell.getCellType() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(AK);
		if (seasonCell.getCellType() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}


	public Study retrieveStudy(Sheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		Study study = new Study();

		Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == CellType.STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getStringCellValue());

		Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == CellType.STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(I);
		if (designTypeCell.getCellType() == CellType.STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(I);
		if (measurementTypeCell.getCellType() == CellType.STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(I);
		if (technologyTypeCell.getCellType() == CellType.STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(I);
		if (technologyPlatformCell.getCellType() == CellType.STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(I);
		if (accreditationProcedureCell.getCellType() == CellType.STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(I);
		if (protocolNameCell.getCellType() == CellType.STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(I);
		if (protocolTypeCell.getCellType() == CellType.STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(I);
		if (protocolDescriptionCell.getCellType() == CellType.STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(I);
		if (protocolURICell.getCellType() == CellType.STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(I);
		if (protocolVersionCell.getCellType() == CellType.STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(I);
		if (parameterNameCell.getCellType() == CellType.STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(I);
		if (componentNameCell.getCellType() == CellType.STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(I);
		if (componentTypeCell.getCellType() == CellType.STRING) {
			study.setProtocolComponentsType(componentTypeCell.getStringCellValue());
		}

		return study;
	}


	public StudySample retrieveStudySample(Row row) {

		// Check mandatory properties
		if (row.getCell(K).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		if (row.getCell(N).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		if (row.getCell(O).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		if (row.getCell(Q).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		if (row.getCell(R).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		StudySample sample = new StudySample();
		sample.setSampleName(row.getCell(K).getStringCellValue());
		sample.setProtocolOfSampleCollection(row.getCell(L).getStringCellValue());

		Cell strategyCell = row.getCell(M);
		if (strategyCell.getCellType() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		Cell samplingProgramCell = row.getCell(N);
		if (samplingProgramCell.getCellType() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		Cell samplingMethodCell = row.getCell(O);
		if (samplingMethodCell.getCellType() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(P).getStringCellValue());
		sample.setSamplingWeight(row.getCell(Q).getStringCellValue());
		sample.setSamplingSize(row.getCell(R).getStringCellValue());

		Cell unitCell = row.getCell(S);
		if (unitCell.getCellType() == CellType.STRING) {
			sample.setLotSizeUnit(row.getCell(S).getStringCellValue());
		}

		Cell pointCell = row.getCell(T);
		if (pointCell.getCellType() == CellType.STRING) {
			sample.setSamplingPoint(row.getCell(T).getStringCellValue());
		}

		return sample;
	}



	public Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(K).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(L);
		if (nameCell.getCellType() == CellType.STRING) {
			laboratory.setName(row.getCell(L).getStringCellValue());
		}

		Cell countryCell = row.getCell(M);
		if (countryCell.getCellType() == CellType.STRING) {
			laboratory.setCountry(row.getCell(M).getStringCellValue());
		}

		return laboratory;
	}

	public Assay retrieveAssay(Row row) {
		// Check first mandatory properties
		if (row.getCell(K).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		Assay assay = new Assay();
		assay.setName(row.getCell(K).getStringCellValue());

		Cell descriptionCell = row.getCell(L);
		if (descriptionCell.getCellType() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		Cell moistureCell = row.getCell(M);
		if (moistureCell.getCellType() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		Cell fatCell = row.getCell(N);
		if (fatCell.getCellType() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		Cell detectionCell = row.getCell(O);
		if (detectionCell.getCellType() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		Cell quantificationCell = row.getCell(P);
		if (quantificationCell.getCellType() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		Cell dataCell = row.getCell(Q);
		if (dataCell.getCellType() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		Cell contaminationCell = row.getCell(R);
		if (contaminationCell.getCellType() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		Cell uncertaintyCell = row.getCell(S);
		if (uncertaintyCell.getCellType() == CellType.STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}


	public Parameter retrieveParameter(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		if (row.getCell(L).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		if (row.getCell(M).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		if (row.getCell(O).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(Q).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing data type");
		}

		Parameter param = new Parameter();
		param.setId(row.getCell(K).getStringCellValue());

		ParameterClassification pc = ParameterClassification.get(row.getCell(L).getStringCellValue());
		if (pc != null) {
			param.setClassification(SwaggerUtil.CLASSIF.get(pc));
		}

		param.setName(row.getCell(M).getStringCellValue());

		Cell descriptionCell = row.getCell(N);
		if (descriptionCell.getCellType() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		param.setUnit(row.getCell(O).getStringCellValue());

		Cell unitCategoryCell = row.getCell(P);
		if (unitCategoryCell.getCellType() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		ParameterType parameterType = ParameterType.get(row.getCell(Q).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		Cell sourceCell = row.getCell(R);
		if (sourceCell.getCellType() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		Cell subjectCell = row.getCell(S);
		if (subjectCell.getCellType() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		Cell distributionCell = row.getCell(T);
		if (distributionCell.getCellType() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		Cell valueCell = row.getCell(U);
		if (valueCell.getCellType() != CellType.BLANK) {

			if (valueCell.getCellType() == CellType.NUMERIC) {
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

		Cell variabilitySubjectCell = row.getCell(W);
		if (variabilitySubjectCell.getCellType() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		Cell maxCell = row.getCell(X);
		if (maxCell.getCellType() != CellType.BLANK) {
			if(maxCell.getCellType() != CellType.STRING)
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			else param.setMaxValue(maxCell.getStringCellValue());
			
		}
		
		Cell minCell = row.getCell(Y);
		if (minCell.getCellType() != CellType.BLANK) {
			if(minCell.getCellType() != CellType.STRING)
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			else param.setMinValue(minCell.getStringCellValue());
		}

		Cell errorCell = row.getCell(Z);
		if (errorCell.getCellType() != CellType.BLANK) {
			if(errorCell.getCellType() != CellType.STRING)
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			else param.setError(errorCell.getStringCellValue());
		}

		return param;
	}

	public QualityMeasures retrieveQualityMeasures(Sheet sheet) {
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

}
