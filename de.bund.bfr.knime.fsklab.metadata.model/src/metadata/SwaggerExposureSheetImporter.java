package metadata;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

public class SwaggerExposureSheetImporter  {

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

	protected int QUALITY_MEASURES__SSE = 124;
	protected int QUALITY_MEASURES__MSE = 125;
	protected int QUALITY_MEASURES__RMSE = 126;
	protected int QUALITY_MEASURES__RSQUARE = 127;
	protected int QUALITY_MEASURES__AIC = 128;
	protected int QUALITY_MEASURES__BIC = 129;

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
	protected int BG_LABORATORY_ROW = 109;
	protected int BG_ASSAY_ROW = 115;
	protected int BG_QUALITY_MEAS_ROW = 124;
	protected int BG_Model_EQ_ROW = 124;

	protected int MM_PARAMETER_ROW = 133;



	public GenericModelDataBackground retrieveBackground(Sheet sheet) {

		GenericModelDataBackground background = new GenericModelDataBackground();

		try {
			Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (Exception exception) {
		}

		for (int numrow = BG_STUDY_SAMPLE_ROW; numrow < (BG_STUDY_SAMPLE_ROW + 3); numrow++) {
			try {
				StudySample sample = retrieveStudySample(sheet.getRow(numrow));
				background.addStudySampleItem(sample);
			} catch (Exception exception) {
			}
		}

		for (int numrow = BG_DIET_ASSESS_ROW; numrow < (BG_DIET_ASSESS_ROW + 3); numrow++) {
			try {
				DietaryAssessmentMethod method = retrieveDietaryAssessmentMethod(sheet.getRow(numrow));
				background.addDietaryAssessmentMethodItem(method);
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

	public GenericModelModelMath retrieveModelMath(Sheet sheet) {

		GenericModelModelMath math = new GenericModelModelMath();

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


	public PredictiveModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();

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
		for (int numRow = this.GI_REFERENCE_ROW; numRow < (this.GI_REFERENCE_ROW + 3); numRow++) {
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

		Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(I);
		if (softwareCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(I);
		if (languageWrittenInCell.getCellType() == Cell.CELL_TYPE_STRING) {
			information.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		// model category (0..n)
		try {
			ModelCategory category = retrieveModelCategory(sheet);
			information.setModelCategory(category);
		} catch (Exception exception) {
		}

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

	public ExposureModelScope retrieveScope(Sheet sheet) {


		ExposureModelScope scope = new ExposureModelScope();

		for (int numrow = this.SCOPE_PRODHAZPOP_ROW; numrow <= (this.SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

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
	public Product retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(M).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		Product product = new Product();
		product.setName(row.getCell(K).getStringCellValue());
		product.setUnit(row.getCell(M).getStringCellValue());

		Cell descriptionCell = row.getCell(L);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		Cell methodCell = row.getCell(N);
		if (methodCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		Cell packagingCell = row.getCell(O);
		if (packagingCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		Cell treatmentCell = row.getCell(P);
		if (treatmentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		Cell originCountryCell = row.getCell(Q);
		if (originCountryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		Cell originAreaCell = row.getCell(R);
		if (originAreaCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		Cell fisheriesAreaCell = row.getCell(S);
		if (fisheriesAreaCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		Cell productionDateCell = row.getCell(T);
		if (productionDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date date = productionDateCell.getDateCellValue();
			product.setProductionDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		Cell expiryDateCell = row.getCell(U);
		if (expiryDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date date = expiryDateCell.getDateCellValue();
			product.setExpiryDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		return product;
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
		if (row.getCell(columns.get("mail")).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing mail");
		}

		Contact contact = new Contact();
		contact.setEmail(row.getCell(columns.get("mail")).getStringCellValue());

		Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setTitle(titleCell.getStringCellValue());
		}

		Cell familyNameCell = row.getCell(columns.get("familyName"));
		if (familyNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setFamilyName(familyNameCell.getStringCellValue());
		}

		Cell givenNameCell = row.getCell(columns.get("givenName"));
		if (givenNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setGivenName(givenNameCell.getStringCellValue());
		}

		Cell telephoneCell = row.getCell(columns.get("telephone"));
		if (telephoneCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setTelephone(telephoneCell.getStringCellValue());
		}

		Cell streetAddressCell = row.getCell(columns.get("streetAddress"));
		if (streetAddressCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setStreetAddress(streetAddressCell.getStringCellValue());
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setCountry(countryCell.getStringCellValue());
		}

		Cell zipCodeCell = row.getCell(columns.get("zipCode"));
		if (zipCodeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setZipCode(zipCodeCell.getStringCellValue());
		}

		Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setRegion(regionCell.getStringCellValue());
		}

		// Time zone not included in spreadsheet
		// gender not included in spreadsheet
		// note not included in spreadsheet

		Cell organizationCell = row.getCell(columns.get("organization"));
		if (organizationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setOrganization(organizationCell.getStringCellValue());
		}

		return contact;
	}

	public Reference retrieveReference(Row row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(O).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(O).getStringCellValue());

		// publication type
		Cell typeCell = row.getCell(L);
		if (typeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			if (type != null) {
				reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
			}
		}

		Cell dateCell = row.getCell(M);
		if (dateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date date = dateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
			reference.setDate(localDate);
		}

		Cell pmidCell = row.getCell(N);
		if (pmidCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		Cell authorListCell = row.getCell(P);
		if (authorListCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		Cell titleCell = row.getCell(Q);
		if (titleCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		Cell abstractCell = row.getCell(R);
		if (abstractCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		Cell statusCell = row.getCell(T);
		if (statusCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		Cell websiteCell = row.getCell(U);
		if (websiteCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		Cell commentCell = row.getCell(V);
		if (commentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	public ModelCategory retrieveModelCategory(Sheet sheet) {
		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getStringCellValue());

		Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(I);
		if (subClassCell.getCellType() == Cell.CELL_TYPE_STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(I);
		if (modelClassCommentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(I);
		if (basicProcessCell.getCellType() == Cell.CELL_TYPE_STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}


	public Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(W).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		Hazard hazard = new Hazard();
		hazard.setName(row.getCell(W).getStringCellValue());

		Cell typeCell = row.getCell(V);
		if (typeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		Cell hazardDescriptionCell = row.getCell(X);
		if (hazardDescriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		Cell hazardUnitCell = row.getCell(Y);
		if (hazardUnitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		Cell adverseEffect = row.getCell(Z);
		if (adverseEffect.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		Cell sourceOfContaminationCell = row.getCell(AA);
		if (sourceOfContaminationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		Cell bmdCell = row.getCell(AB);
		if (bmdCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		Cell maximumResidueLimitCell = row.getCell(AC);
		if (maximumResidueLimitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		Cell noaelCell = row.getCell(AD);
		if (noaelCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		Cell loaelCell = row.getCell(AE);
		if (loaelCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		Cell aoelCell = row.getCell(AF);
		if (aoelCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		Cell arfdCell = row.getCell(AG);
		if (arfdCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		Cell adiCell = row.getCell(AH);
		if (adiCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		Cell indSumCell = row.getCell(AI);
		if (indSumCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	public PopulationGroup retrievePopulationGroup(Row row) {
		
		// Check mandatory properties
		if (row.getCell(AJ).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();

		Cell nameCell = row.getCell(AJ);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		Cell targetPopulationCell = row.getCell(AK);
		if (targetPopulationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(AL);
		if (spanCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(AM);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(AN);
		if (ageCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(AO);
		if (genderCell.getCellType() == Cell.CELL_TYPE_STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(AP);
		if (bmiCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(AQ);
		if (dietCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(AR);
		if (consumptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(AS);
		if (regionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(AT);
		if (countryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(AU);
		if (factorsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(AV);
		if (seasonCell.getCellType() == Cell.CELL_TYPE_STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}


	public Study retrieveStudy(Sheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		Study study = new Study();

		Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getStringCellValue());

		Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(I);
		if (designTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(I);
		if (measurementTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(I);
		if (technologyTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(I);
		if (technologyPlatformCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(I);
		if (accreditationProcedureCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(I);
		if (protocolNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(I);
		if (protocolTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(I);
		if (protocolDescriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(I);
		if (protocolURICell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(I);
		if (protocolVersionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(I);
		if (parameterNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(I);
		if (componentNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(I);
		if (componentTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setProtocolComponentsType(componentTypeCell.getStringCellValue());
		}

		return study;
	}


	public StudySample retrieveStudySample(Row row) {

		// Check mandatory properties
		if (row.getCell(K).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		if (row.getCell(N).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		if (row.getCell(O).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		if (row.getCell(Q).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		if (row.getCell(R).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		StudySample sample = new StudySample();
		sample.setSampleName(row.getCell(K).getStringCellValue());
		sample.setProtocolOfSampleCollection(row.getCell(L).getStringCellValue());

		Cell strategyCell = row.getCell(M);
		if (strategyCell.getCellType() == Cell.CELL_TYPE_STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		Cell samplingProgramCell = row.getCell(N);
		if (samplingProgramCell.getCellType() == Cell.CELL_TYPE_STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		Cell samplingMethodCell = row.getCell(O);
		if (samplingMethodCell.getCellType() == Cell.CELL_TYPE_STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(P).getStringCellValue());
		sample.setSamplingWeight(row.getCell(Q).getStringCellValue());
		sample.setSamplingSize(row.getCell(R).getStringCellValue());

		Cell unitCell = row.getCell(S);
		if (unitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			sample.setLotSizeUnit(row.getCell(S).getStringCellValue());
		}

		Cell pointCell = row.getCell(T);
		if (pointCell.getCellType() == Cell.CELL_TYPE_STRING) {
			sample.setSamplingPoint(row.getCell(T).getStringCellValue());
		}

		return sample;
	}



	public Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(K).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(L);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			laboratory.setName(row.getCell(L).getStringCellValue());
		}

		Cell countryCell = row.getCell(M);
		if (countryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			laboratory.setCountry(row.getCell(M).getStringCellValue());
		}

		return laboratory;
	}

	public Assay retrieveAssay(Row row) {
		// Check first mandatory properties
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		Assay assay = new Assay();
		assay.setName(row.getCell(K).getStringCellValue());

		Cell descriptionCell = row.getCell(L);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		Cell moistureCell = row.getCell(M);
		if (moistureCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		Cell fatCell = row.getCell(N);
		if (fatCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		Cell detectionCell = row.getCell(O);
		if (detectionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		Cell quantificationCell = row.getCell(P);
		if (quantificationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		Cell dataCell = row.getCell(Q);
		if (dataCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		Cell contaminationCell = row.getCell(R);
		if (contaminationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		Cell uncertaintyCell = row.getCell(S);
		if (uncertaintyCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}


	public Parameter retrieveParameter(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		if (row.getCell(L).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		if (row.getCell(M).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		if (row.getCell(O).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(Q).getCellType() == Cell.CELL_TYPE_BLANK) {
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
		if (descriptionCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		param.setUnit(row.getCell(O).getStringCellValue());

		Cell unitCategoryCell = row.getCell(P);
		if (unitCategoryCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		ParameterType parameterType = ParameterType.get(row.getCell(Q).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		Cell sourceCell = row.getCell(R);
		if (sourceCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		Cell subjectCell = row.getCell(S);
		if (subjectCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		Cell distributionCell = row.getCell(T);
		if (distributionCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		Cell valueCell = row.getCell(U);
		if (valueCell.getCellType() != Cell.CELL_TYPE_BLANK) {

			if (valueCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
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
		if (variabilitySubjectCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		Cell maxCell = row.getCell(X);
		if (maxCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			if(maxCell.getCellType() != Cell.CELL_TYPE_STRING)
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			else param.setMaxValue(maxCell.getStringCellValue());
			
		}
		
		

		Cell minCell = row.getCell(Y);
		if (minCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			if(minCell.getCellType() != Cell.CELL_TYPE_STRING)
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			else param.setMinValue(minCell.getStringCellValue());
		}

		Cell errorCell = row.getCell(Z);
		if (errorCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			if(errorCell.getCellType() != Cell.CELL_TYPE_STRING)
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			else param.setError(errorCell.getStringCellValue());
		}


		return param;
	}

	public QualityMeasures retrieveQualityMeasures(Sheet sheet) {
		QualityMeasures measures = new QualityMeasures();

		Cell sseCell = sheet.getRow(QUALITY_MEASURES__SSE).getCell(M);
		if (sseCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			measures.setSse(BigDecimal.valueOf(sseCell.getNumericCellValue()));
		}

		Cell mseCell = sheet.getRow(QUALITY_MEASURES__MSE).getCell(M);
		if (mseCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			measures.setMse(BigDecimal.valueOf(mseCell.getNumericCellValue()));
		}

		Cell rmseCell = sheet.getRow(QUALITY_MEASURES__RMSE).getCell(M);
		if (rmseCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			measures.setRmse(BigDecimal.valueOf(rmseCell.getNumericCellValue()));
		}

		Cell rsquareCell = sheet.getRow(QUALITY_MEASURES__RSQUARE).getCell(M);
		if (rsquareCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			measures.setRsquared(BigDecimal.valueOf(rsquareCell.getNumericCellValue()));
		}

		Cell aicCell = sheet.getRow(QUALITY_MEASURES__AIC).getCell(M);
		if (aicCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			measures.setAic(BigDecimal.valueOf(aicCell.getNumericCellValue()));
		}

		Cell bicCell = sheet.getRow(QUALITY_MEASURES__BIC).getCell(M);
		if (bicCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			measures.setBic(BigDecimal.valueOf(bicCell.getNumericCellValue()));
		}
		
		return measures;
	}
	public DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(L).getCellType() != Cell.CELL_TYPE_NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(K).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(Double.toString(row.getCell(L).getNumericCellValue()));

		Cell softwareCell = row.getCell(M);
		if (softwareCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		Cell foodItemsCell = row.getCell(N);
		if (foodItemsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		Cell recordTypesCell = row.getCell(O);
		if (recordTypesCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		Cell foodDescriptorsCell = row.getCell(P);
		if (foodDescriptorsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.addFoodDescriptorsItem(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}
	

}
