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
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

@SuppressWarnings("deprecation")
public class RAKIPSheetImporter {

	private int A = 0;
	private int B = 1;
	private int C = 2;
	private int D = 3;
	private int E = 4;
	private int F = 5;
	private int G = 6;
	private int H = 7;
	private int I = 8;
	private int J = 9;
	private int K = 10;
	private int L = 11;
	private int M = 12;
	private int N = 13;
	private int O = 14;
	private int P = 15;
	private int Q = 16;
	private int R = 17;
	private int S = 18;
	private int T = 19;
	private int U = 20;
	private int V = 21;
	private int W = 22;
	private int X = 23;
	private int Y = 24;
	private int Z = 25;
	private int AA = 26;
	private int AB = 27;
	private int AC = 28;
	private int AD = 29;
	private int AE = 30;
	private int AF = 31;
	private int AG = 32;
	private int AH = 33;
	private int AI = 34;
	private int AJ = 35;
	private int AK = 36;
	private int AL = 37;
	private int AM = 38;
	private int AN = 39;
	private int AO = 40;
	private int AP = 41;
	private int AQ = 42;
	private int AR = 43;
	private int AS = 44;
	private int AT = 45;
	private int AU = 46;
	private int AV = 47;

	private final int GENERAL_INFORMATION__NAME = 1;
	private final int GENERAL_INFORMATION__SOURCE = 2;
	private final int GENERAL_INFORMATION__IDENTIFIER = 3;
	private final int GENERAL_INFORMATION_CREATION_DATE = 6;
	private final int GENERAL_INFORMATION__RIGHTS = 8;
	private final int GENERAL_INFORMATION__AVAILABLE = 9;
	private final int GENERAL_INFORMATION__URL = 10;
	private final int GENERAL_INFORMATION__FORMAT = 11;
	private final int GENERAL_INFORMATION__LANGUAGE = 24;
	private final int GENERAL_INFORMATION__SOFTWARE = 25;
	private final int GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN = 26;
	private final int GENERAL_INFORMATION__STATUS = 32;
	private final int GENERAL_INFORMATION__OBJECTIVE = 33;
	private final int GENERAL_INFORMATION__DESCRIPTION = 34;

	private final int MODEL_CATEGORY__MODEL_CLASS = 27;
	private final int MODEL_CATEGORY__MODEL_SUB_CLASS = 28;
	private final int MODEL_CATEGORY__CLASS_COMMENT = 29;
	private final int MODEL_CATEGORY__BASIC_PROCESS = 30;

	private final int QUALITY_MEASURES__SSE = 123;
	private final int QUALITY_MEASURES__MSE = 124;
	private final int QUALITY_MEASURES__RMSE = 125;
	private final int QUALITY_MEASURES__RSQUARE = 126;
	private final int QUALITY_MEASURES__AIC = 127;
	private final int QUALITY_MEASURES__BIC = 128;

	private final int SCOPE__GENERAL_COMMENT = 73;
	private final int SCOPE__TEMPORAL_INFORMATION = 74;

	private final int STUDY__STUDY_IDENTIFIER = 77;
	private final int STUDY__STUDY_TITLE = 78;
	private final int STUDY__STUDY_DESCRIPTION = 79;
	private final int STUDY__STUDY_DESIGN_TYPE = 80;
	private final int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 81;
	private final int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 82;
	private final int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 83;
	private final int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 84;
	private final int STUDY__STUDY_PROTOCOL_NAME = 85;
	private final int STUDY__STUDY_PROTOCOL_TYPE = 86;
	private final int STUDY__STUDY_PROTOCOL_DESCRIPTION = 87;
	private final int STUDY__STUDY_PROTOCOL_URI = 88;
	private final int STUDY__STUDY_PROTOCOL_VERSION = 89;
	private final int STUDY__STUDY_PROTOCOL_PARAMETERS_NAME = 90;
	private final int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 91;
	private final int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 92;

	public GenericModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		GenericModelGeneralInformation information = new GenericModelGeneralInformation();

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
		if (creationDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date creationDate = creationDateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(creationDate.getYear() + 1900, creationDate.getMonth() + 1,
					creationDate.getDate());
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
		for (int numRow = 14; numRow < 17; numRow++) {
			try {
				Reference reference = retrieveReference(sheet.getRow(numRow));
				information.addReferenceItem(reference);
			} catch (Exception exception) {
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
		if (row.getCell(columns.get("mail")).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing mail");
		}

		Contact contact = new Contact();
		contact.setEmail(row.getCell(columns.get("mail")).getStringCellValue());

		Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			contact.setTitle(titleCell.getStringCellValue());
		}

		Cell familyNameCell = row.getCell(columns.get("familyName"));
		if (familyNameCell.getCellTypeEnum() == CellType.STRING) {
			contact.setFamilyName(familyNameCell.getStringCellValue());
		}

		Cell givenNameCell = row.getCell(columns.get("givenName"));
		if (givenNameCell.getCellTypeEnum() == CellType.STRING) {
			contact.setGivenName(givenNameCell.getStringCellValue());
		}

		Cell telephoneCell = row.getCell(columns.get("telephone"));
		if (telephoneCell.getCellTypeEnum() == CellType.STRING) {
			contact.setTelephone(telephoneCell.getStringCellValue());
		}

		Cell streetAddressCell = row.getCell(columns.get("streetAddress"));
		if (streetAddressCell.getCellTypeEnum() == CellType.STRING) {
			contact.setStreetAddress(streetAddressCell.getStringCellValue());
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			contact.setCountry(countryCell.getStringCellValue());
		}

		Cell zipCodeCell = row.getCell(columns.get("zipCode"));
		if (zipCodeCell.getCellTypeEnum() == CellType.STRING) {
			contact.setZipCode(zipCodeCell.getStringCellValue());
		}

		Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			contact.setRegion(regionCell.getStringCellValue());
		}

		// Time zone not included in spreadsheet
		// gender not included in spreadsheet
		// note not included in spreadsheet

		Cell organizationCell = row.getCell(columns.get("organization"));
		if (organizationCell.getCellTypeEnum() == CellType.STRING) {
			contact.setOrganization(organizationCell.getStringCellValue());
		}

		return contact;
	}

	public Reference retrieveReference(Row row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(O).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(O).getStringCellValue());

		// publication type
		Cell typeCell = row.getCell(L);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
		}

		Cell dateCell = row.getCell(M);
		if (dateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date date = dateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
			reference.setDate(""+localDate.getYear());
		}

		Cell pmidCell = row.getCell(N);
		if (pmidCell.getCellTypeEnum() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		Cell authorListCell = row.getCell(P);
		if (authorListCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		Cell titleCell = row.getCell(Q);
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		Cell abstractCell = row.getCell(R);
		if (abstractCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		Cell statusCell = row.getCell(T);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		Cell websiteCell = row.getCell(U);
		if (websiteCell.getCellTypeEnum() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		Cell commentCell = row.getCell(V);
		if (commentCell.getCellTypeEnum() == CellType.STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	public ModelCategory retrieveModelCategory(Sheet sheet) {
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

	public Product retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(M).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		Product product = new Product();
		product.setName(row.getCell(K).getStringCellValue());
		product.setUnit(row.getCell(M).getStringCellValue());

		Cell descriptionCell = row.getCell(L);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		Cell methodCell = row.getCell(N);
		if (methodCell.getCellTypeEnum() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		Cell packagingCell = row.getCell(O);
		if (packagingCell.getCellTypeEnum() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		Cell treatmentCell = row.getCell(P);
		if (treatmentCell.getCellTypeEnum() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		Cell originCountryCell = row.getCell(Q);
		if (originCountryCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		Cell originAreaCell = row.getCell(R);
		if (originAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		Cell fisheriesAreaCell = row.getCell(S);
		if (fisheriesAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		Cell productionDateCell = row.getCell(T);
		if (productionDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date date = productionDateCell.getDateCellValue();
			product.setProductionDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		Cell expiryDateCell = row.getCell(U);
		if (expiryDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date date = expiryDateCell.getDateCellValue();
			product.setExpiryDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		return product;
	}

	public Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(W).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		Hazard hazard = new Hazard();
		hazard.setName(row.getCell(W).getStringCellValue());

		Cell typeCell = row.getCell(V);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		Cell hazardDescriptionCell = row.getCell(X);
		if (hazardDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		Cell hazardUnitCell = row.getCell(Y);
		if (hazardUnitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		Cell adverseEffect = row.getCell(Z);
		if (adverseEffect.getCellTypeEnum() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		Cell sourceOfContaminationCell = row.getCell(AA);
		if (sourceOfContaminationCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		Cell bmdCell = row.getCell(AB);
		if (bmdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		Cell maximumResidueLimitCell = row.getCell(AC);
		if (maximumResidueLimitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		Cell noaelCell = row.getCell(AD);
		if (noaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		Cell loaelCell = row.getCell(AE);
		if (loaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		Cell aoelCell = row.getCell(AF);
		if (aoelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		Cell arfdCell = row.getCell(AG);
		if (arfdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		Cell adiCell = row.getCell(AH);
		if (adiCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		Cell indSumCell = row.getCell(AI);
		if (indSumCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	public PopulationGroup retrievePopulationGroup(Row row) {
		// Check mandatory properties
		if (row.getCell(AU).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Risk and population factors");
		}

		PopulationGroup group = new PopulationGroup();

		Cell nameCell = row.getCell(AJ);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		Cell targetPopulationCell = row.getCell(AK);
		if (targetPopulationCell.getCellTypeEnum() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(AL);
		if (spanCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(AM);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(AN);
		if (ageCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(AO);
		if (genderCell.getCellTypeEnum() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(AP);
		if (bmiCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(AQ);
		if (dietCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(AR);
		if (consumptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(AS);
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(AT);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(AU);
		if (factorsCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(AV);
		if (seasonCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}

	public GenericModelScope retrieveScope(Sheet sheet) {

		GenericModelScope scope = new GenericModelScope();

		for (int numrow = 38; numrow <= 49; numrow++) {

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

			try {
				scope.addPopulationGroupItem(retrievePopulationGroup(row));
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

	public Study retrieveStudy(Sheet sheet) {

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

	public GenericModelDataBackground retrieveBackground(Sheet sheet) {

		GenericModelDataBackground background = new GenericModelDataBackground();

		try {
			Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (Exception exception) {
		}

		for (int numrow = 96; numrow < 99; numrow++) {
			try {
				StudySample sample = retrieveStudySample(sheet.getRow(numrow));
				background.addStudySampleItem(sample);
			} catch (Exception exception) {
			}
		}

		for (int numrow = 103; numrow < 106; numrow++) {
			try {
				DietaryAssessmentMethod method = retrieveDietaryAssessmentMethod(sheet.getRow(numrow));
				background.addDietaryAssessmentMethodItem(method);
			} catch (Exception exception) {
			}
		}

		for (int numrow = 110; numrow < 113; numrow++) {
			try {
				Laboratory laboratory = retrieveLaboratory(sheet.getRow(numrow));
				background.addLaboratoryItem(laboratory);
			} catch (Exception exception) {
			}
		}

		for (int numrow = 117; numrow < 120; numrow++) {
			try {
				Assay assay = retrieveAssay(sheet.getRow(numrow));
				background.addAssayItem(assay);
			} catch (Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return background;
	}

	public StudySample retrieveStudySample(Row row) {

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

	public DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(M).getCellTypeEnum() != CellType.NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(L).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(Double.toString(row.getCell(M).getNumericCellValue()));

		Cell softwareCell = row.getCell(N);
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		Cell foodItemsCell = row.getCell(O);
		if (foodItemsCell.getCellTypeEnum() == CellType.STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		Cell recordTypesCell = row.getCell(P);
		if (recordTypesCell.getCellTypeEnum() == CellType.STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		Cell foodDescriptorsCell = row.getCell(Q);
		if (foodDescriptorsCell.getCellTypeEnum() == CellType.STRING) {
			method.addFoodDescriptorsItem(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}

	public Laboratory retrieveLaboratory(Row row) {

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

	public Assay retrieveAssay(Row row) {
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

	public GenericModelModelMath retrieveModelMath(Sheet sheet) {
		
		GenericModelModelMath math = new GenericModelModelMath();

		for (int rownum = 132; rownum < sheet.getLastRowNum(); rownum++) {
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

	public Parameter retrieveParameter(Row row) {

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

		if (row.getCell(Q).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(S).getCellTypeEnum() == CellType.BLANK) {
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

		param.setUnit(row.getCell(Q).getStringCellValue());

		Cell unitCategoryCell = row.getCell(R);
		if (unitCategoryCell.getCellTypeEnum() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		ParameterType parameterType = ParameterType.get(row.getCell(S).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		Cell sourceCell = row.getCell(T);
		if (sourceCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		Cell subjectCell = row.getCell(U);
		if (subjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		Cell distributionCell = row.getCell(V);
		if (distributionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		Cell valueCell = row.getCell(W);
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

		Cell variabilitySubjectCell = row.getCell(Y);
		if (variabilitySubjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		Cell maxCell = row.getCell(Z);
		if (maxCell.getCellTypeEnum() != CellType.BLANK) {
			param.setMaxValue(maxCell.getStringCellValue());
		}

		Cell minCell = row.getCell(AA);
		if (minCell.getCellTypeEnum() != CellType.BLANK) {
			param.setMinValue(minCell.getStringCellValue());
		}

		Cell errorCell = row.getCell(AB);
		if (errorCell.getCellTypeEnum() != CellType.BLANK) {
			param.setError(errorCell.getStringCellValue());
		}

		return param;
	}

	public QualityMeasures retrieveQualityMeasures(Sheet sheet) {
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
}
