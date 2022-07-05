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

@Deprecated
public class SwaggerGenericSheetImporter  {
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

	 protected int QUALITY_MEASURES__SSE = 123;
	 protected int QUALITY_MEASURES__MSE = 124;
	 protected int QUALITY_MEASURES__RMSE = 125;
	 protected int QUALITY_MEASURES__RSQUARE = 126;
	 protected int QUALITY_MEASURES__AIC = 127;
	 protected int QUALITY_MEASURES__BIC = 128;

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
	 protected int BG_DIET_ASSESS_ROW = 103;
	 protected int BG_LABORATORY_ROW = 110;
	 protected int BG_ASSAY_ROW = 117;
	 protected int BG_QUALITY_MEAS_ROW = 123;
	 protected int BG_EVENT_ROW = 109;
	 protected int MM_PARAMETER_ROW = 132;
		protected int MM_FITTING_PROCEDURE_ROW = 149;

	public GenericModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		final GenericModelGeneralInformation information = new GenericModelGeneralInformation();

		final Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(I);
		if (nameCell.getCellType() == CellType.STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		final Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(I);
		if (sourceCell.getCellType() == CellType.STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		final Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == CellType.STRING) {
			information.setIdentifier(identifierCell.getStringCellValue());
		}

		try {
			final Contact author = retrieveAuthor(sheet.getRow(3));
			information.addAuthorItem(author);
		} catch (final Exception exception) {
		}

		for (int numRow = GI_CREATOR_ROW; numRow < GI_CREATOR_ROW + 5; numRow++) {
			try {
				final Contact contact = retrieveCreator(sheet.getRow(numRow));
				information.addCreatorItem(contact);
			} catch (final Exception exception) {
			}
		}

		final Cell creationDateCell = sheet.getRow(GENERAL_INFORMATION_CREATION_DATE).getCell(I);
		if (creationDateCell.getCellType() == CellType.NUMERIC) {
			final Date creationDate = creationDateCell.getDateCellValue();
			final LocalDate localDate = LocalDate.of(creationDate.getYear() + 1900, creationDate.getMonth() + 1,
					creationDate.getDate());
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		final Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(I);
		if (rightsCell.getCellType() == CellType.STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		final Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(I);
		if (isAvailableCell.getCellType() == CellType.STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		final Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(I);
		if (urlCell.getCellType() == CellType.STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		final Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(I);
		if (formatCell.getCellType() == CellType.STRING) {
			information.setFormat(formatCell.getStringCellValue());
		}

		// reference (1..n)
		for (int numRow = GI_REFERENCE_ROW; numRow < GI_REFERENCE_ROW + 4; numRow++) {
			try {
				final Reference reference = retrieveReference(sheet.getRow(numRow));
				information.addReferenceItem(reference);
			} catch (final Exception exception) {
			}
		}

		final Cell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(I);
		if (languageCell.getCellType() == CellType.STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}

		final Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(I);
		if (softwareCell.getCellType() == CellType.STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		final Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(I);
		if (languageWrittenInCell.getCellType() == CellType.STRING) {
			information.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		// model category (0..n)
		try {
			final ModelCategory category = retrieveModelCategory(sheet);
			information.setModelCategory(category);
		} catch (final Exception exception) {
		}

		final Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(I);
		if (statusCell.getCellType() == CellType.STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		final Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(I);
		if (objectiveCell.getCellType() == CellType.STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		final Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == CellType.STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}
	public GenericModelModelMath retrieveModelMath(Sheet sheet) {

		final GenericModelModelMath math = new GenericModelModelMath();

		for (int rownum = MM_PARAMETER_ROW; rownum < sheet.getLastRowNum(); rownum++) {
			try {
				final Row row = sheet.getRow(rownum);
				final Parameter param = retrieveParameter(row);
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
	public GenericModelDataBackground retrieveBackground(Sheet sheet) {

		final GenericModelDataBackground background = new GenericModelDataBackground();

		try {
			final Study study = retrieveStudy(sheet);
			background.setStudy(study);
		} catch (final Exception exception) {
		}

		for (int numrow = BG_STUDY_SAMPLE_ROW; numrow < BG_STUDY_SAMPLE_ROW + 3; numrow++) {
			try {
				final StudySample sample = retrieveStudySample(sheet.getRow(numrow));
				background.addStudySampleItem(sample);
			} catch (final Exception exception) {
			}
		}

		for (int numrow = BG_DIET_ASSESS_ROW; numrow < BG_DIET_ASSESS_ROW + 3; numrow++) {
			try {
				final DietaryAssessmentMethod method = retrieveDietaryAssessmentMethod(sheet.getRow(numrow));
				background.addDietaryAssessmentMethodItem(method);
			} catch (final Exception exception) {
			}
		}

		for (int numrow = BG_LABORATORY_ROW; numrow < BG_LABORATORY_ROW + 3; numrow++) {
			try {
				final Laboratory laboratory = retrieveLaboratory(sheet.getRow(numrow));
				background.addLaboratoryItem(laboratory);
			} catch (final Exception exception) {
			}
		}

		for (int numrow = BG_ASSAY_ROW; numrow < BG_ASSAY_ROW + 3; numrow++) {
			try {
				final Assay assay = retrieveAssay(sheet.getRow(numrow));
				background.addAssayItem(assay);
			} catch (final Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return background;
	}
	public GenericModelScope retrieveScope(Sheet sheet) {

		final GenericModelScope scope = new GenericModelScope();

		for (int numrow = SCOPE_PRODHAZPOP_ROW; numrow <= SCOPE_PRODHAZPOP_ROW + 11; numrow++) {

			final Row row = sheet.getRow(numrow);

			try {
				scope.addProductItem(retrieveProduct(row));
			} catch (final IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addHazardItem(retrieveHazard(row));
			} catch (final IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.addPopulationGroupItem(retrievePopulationGroup(row));
			} catch (final IllegalArgumentException exception) {
				// ignore exception since population groups are optional (*)
			}
		}

		final Cell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(I);
		if (generalCommentCell.getCellType() == CellType.STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		final Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(I);
		if (temporalInformationCell.getCellType() == CellType.STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}
	public Product retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(M).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		final Product product = new Product();
		product.setName(row.getCell(K).getStringCellValue());
		product.setUnit(row.getCell(M).getStringCellValue());

		final Cell descriptionCell = row.getCell(L);
		if (descriptionCell.getCellType() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell methodCell = row.getCell(N);
		if (methodCell.getCellType() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		final Cell packagingCell = row.getCell(O);
		if (packagingCell.getCellType() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		final Cell treatmentCell = row.getCell(P);
		if (treatmentCell.getCellType() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		final Cell originCountryCell = row.getCell(Q);
		if (originCountryCell.getCellType() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		final Cell originAreaCell = row.getCell(R);
		if (originAreaCell.getCellType() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		final Cell fisheriesAreaCell = row.getCell(S);
		if (fisheriesAreaCell.getCellType() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		final Cell productionDateCell = row.getCell(T);
		if (productionDateCell.getCellType() == CellType.NUMERIC) {
			final Date date = productionDateCell.getDateCellValue();
			product.setProductionDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		final Cell expiryDateCell = row.getCell(U);
		if (expiryDateCell.getCellType() == CellType.NUMERIC) {
			final Date date = expiryDateCell.getDateCellValue();
			product.setExpiryDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		return product;
	}
	public Contact retrieveCreator(Row row) {
		@SuppressWarnings("serial")
		final
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
		final
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

		String x = row.getCell(L).getStringCellValue();
		x= row.getCell(AA).getStringCellValue();
		x= row.getCell(AE).getStringCellValue();
		// Check mandatory properties and throw exception if missing
		if (row.getCell(columns.get("mail")).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing mail");
		}

		final Contact contact = new Contact();
		contact.setEmail(row.getCell(columns.get("mail")).getStringCellValue());

		final Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellType() == CellType.STRING) {
			contact.setTitle(titleCell.getStringCellValue());
		}

		final Cell familyNameCell = row.getCell(columns.get("familyName"));
		if (familyNameCell.getCellType() == CellType.STRING) {
			contact.setFamilyName(familyNameCell.getStringCellValue());
		}

		final Cell givenNameCell = row.getCell(columns.get("givenName"));
		if (givenNameCell.getCellType() == CellType.STRING) {
			contact.setGivenName(givenNameCell.getStringCellValue());
		}

		final Cell telephoneCell = row.getCell(columns.get("telephone"));
		if (telephoneCell.getCellType() == CellType.STRING) {
			contact.setTelephone(telephoneCell.getStringCellValue());
		}

		final Cell streetAddressCell = row.getCell(columns.get("streetAddress"));
		if (streetAddressCell.getCellType() == CellType.STRING) {
			contact.setStreetAddress(streetAddressCell.getStringCellValue());
		}

		final Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellType() == CellType.STRING) {
			contact.setCountry(countryCell.getStringCellValue());
		}

		final Cell zipCodeCell = row.getCell(columns.get("zipCode"));
		if (zipCodeCell.getCellType() == CellType.STRING) {
			contact.setZipCode(zipCodeCell.getStringCellValue());
		}

		final Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellType() == CellType.STRING) {
			contact.setRegion(regionCell.getStringCellValue());
		}

		// Time zone not included in spreadsheet
		// gender not included in spreadsheet
		// note not included in spreadsheet

		final Cell organizationCell = row.getCell(columns.get("organization"));
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

		final Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(O).getStringCellValue());

		// publication type
		final Cell typeCell = row.getCell(L);
		if (typeCell.getCellType() == CellType.STRING) {
			final PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			if (type != null) {
				reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
			}
		}

		final Cell dateCell = row.getCell(M);
		if (dateCell.getCellType() == CellType.NUMERIC) {
		    reference.setDate(dateCell.getStringCellValue());
		}

		final Cell pmidCell = row.getCell(N);
		if (pmidCell.getCellType() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		final Cell authorListCell = row.getCell(P);
		if (authorListCell.getCellType() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		final Cell titleCell = row.getCell(Q);
		if (titleCell.getCellType() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		final Cell abstractCell = row.getCell(R);
		if (abstractCell.getCellType() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		final Cell statusCell = row.getCell(T);
		if (statusCell.getCellType() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		final Cell websiteCell = row.getCell(U);
		if (websiteCell.getCellType() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		final Cell commentCell = row.getCell(V);
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

		final ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getStringCellValue());

		final Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(I);
		if (subClassCell.getCellType() == CellType.STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		final Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(I);
		if (modelClassCommentCell.getCellType() == CellType.STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		final Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(I);
		if (basicProcessCell.getCellType() == CellType.STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}


	public Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(W).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		final Hazard hazard = new Hazard();
		hazard.setName(row.getCell(W).getStringCellValue());

		final Cell typeCell = row.getCell(V);
		if (typeCell.getCellType() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		final Cell hazardDescriptionCell = row.getCell(X);
		if (hazardDescriptionCell.getCellType() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		final Cell hazardUnitCell = row.getCell(Y);
		if (hazardUnitCell.getCellType() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		final Cell adverseEffect = row.getCell(Z);
		if (adverseEffect.getCellType() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		final Cell sourceOfContaminationCell = row.getCell(AA);
		if (sourceOfContaminationCell.getCellType() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		final Cell bmdCell = row.getCell(AB);
		if (bmdCell.getCellType() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		final Cell maximumResidueLimitCell = row.getCell(AC);
		if (maximumResidueLimitCell.getCellType() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		final Cell noaelCell = row.getCell(AD);
		if (noaelCell.getCellType() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		final Cell loaelCell = row.getCell(AE);
		if (loaelCell.getCellType() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		final Cell aoelCell = row.getCell(AF);
		if (aoelCell.getCellType() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		final Cell arfdCell = row.getCell(AG);
		if (arfdCell.getCellType() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		final Cell adiCell = row.getCell(AH);
		if (adiCell.getCellType() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		final Cell indSumCell = row.getCell(AI);
		if (indSumCell.getCellType() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	public PopulationGroup retrievePopulationGroup(Row row) {

		// Check mandatory properties
		if (row.getCell(AJ).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		final PopulationGroup group = new PopulationGroup();

		final Cell nameCell = row.getCell(AJ);
		if (nameCell.getCellType() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		final Cell targetPopulationCell = row.getCell(AK);
		if (targetPopulationCell.getCellType() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		final Cell spanCell = row.getCell(AL);
		if (spanCell.getCellType() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		final Cell descriptionCell = row.getCell(AM);
		if (descriptionCell.getCellType() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		final Cell ageCell = row.getCell(AN);
		if (ageCell.getCellType() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		final Cell genderCell = row.getCell(AO);
		if (genderCell.getCellType() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		final Cell bmiCell = row.getCell(AP);
		if (bmiCell.getCellType() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		final Cell dietCell = row.getCell(AQ);
		if (dietCell.getCellType() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		final Cell consumptionCell = row.getCell(AR);
		if (consumptionCell.getCellType() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		final Cell regionCell = row.getCell(AS);
		if (regionCell.getCellType() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		final Cell countryCell = row.getCell(AT);
		if (countryCell.getCellType() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		final Cell factorsCell = row.getCell(AU);
		if (factorsCell.getCellType() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		final Cell seasonCell = row.getCell(AV);
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

		final Study study = new Study();

		final Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == CellType.STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getStringCellValue());

		final Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == CellType.STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(I);
		if (designTypeCell.getCellType() == CellType.STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		final Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(I);
		if (measurementTypeCell.getCellType() == CellType.STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		final Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(I);
		if (technologyTypeCell.getCellType() == CellType.STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		final Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(I);
		if (technologyPlatformCell.getCellType() == CellType.STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		final Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(I);
		if (accreditationProcedureCell.getCellType() == CellType.STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		final Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(I);
		if (protocolNameCell.getCellType() == CellType.STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		final Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(I);
		if (protocolTypeCell.getCellType() == CellType.STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		final Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(I);
		if (protocolDescriptionCell.getCellType() == CellType.STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		final Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(I);
		if (protocolURICell.getCellType() == CellType.STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		final Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(I);
		if (protocolVersionCell.getCellType() == CellType.STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		final Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(I);
		if (parameterNameCell.getCellType() == CellType.STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		final Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(I);
		if (componentNameCell.getCellType() == CellType.STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		final Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(I);
		if (componentTypeCell.getCellType() == CellType.STRING) {
			study.setProtocolComponentsType(componentTypeCell.getStringCellValue());
		}

		return study;
	}


	public StudySample retrieveStudySample(Row row) {

		// Check mandatory properties
		if (row.getCell(L).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		if (row.getCell(M).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		if (row.getCell(Q).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		if (row.getCell(R).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		if (row.getCell(S).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		final StudySample sample = new StudySample();
		sample.setSampleName(row.getCell(L).getStringCellValue());
		sample.setProtocolOfSampleCollection(row.getCell(M).getStringCellValue());

		final Cell strategyCell = row.getCell(N);
		if (strategyCell.getCellType() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		final Cell samplingProgramCell = row.getCell(O);
		if (samplingProgramCell.getCellType() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		final Cell samplingMethodCell = row.getCell(P);
		if (samplingMethodCell.getCellType() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(Q).getStringCellValue());
		sample.setSamplingWeight(row.getCell(R).getStringCellValue());
		sample.setSamplingSize(row.getCell(S).getStringCellValue());

		final Cell unitCell = row.getCell(T);
		if (unitCell.getCellType() == CellType.STRING) {
			sample.setLotSizeUnit(row.getCell(T).getStringCellValue());
		}

		final Cell pointCell = row.getCell(U);
		if (pointCell.getCellType() == CellType.STRING) {
			sample.setSamplingPoint(row.getCell(U).getStringCellValue());
		}

		return sample;
	}

	public DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(M).getCellType() != CellType.NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		final DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(L).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(Double.toString(row.getCell(M).getNumericCellValue()));

		final Cell softwareCell = row.getCell(N);
		if (softwareCell.getCellType() == CellType.STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		final Cell foodItemsCell = row.getCell(O);
		if (foodItemsCell.getCellType() == CellType.STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		final Cell recordTypesCell = row.getCell(P);
		if (recordTypesCell.getCellType() == CellType.STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		final Cell foodDescriptorsCell = row.getCell(Q);
		if (foodDescriptorsCell.getCellType() == CellType.STRING) {
			method.addFoodDescriptorsItem(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}

	public Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		final Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(L).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		final Cell nameCell = row.getCell(M);
		if (nameCell.getCellType() == CellType.STRING) {
			laboratory.setName(row.getCell(M).getStringCellValue());
		}

		final Cell countryCell = row.getCell(N);
		if (countryCell.getCellType() == CellType.STRING) {
			laboratory.setCountry(row.getCell(N).getStringCellValue());
		}

		return laboratory;
	}

	public Assay retrieveAssay(Row row) {
		// Check first mandatory properties
		if (row.getCell(L).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		final Assay assay = new Assay();
		assay.setName(row.getCell(L).getStringCellValue());

		final Cell descriptionCell = row.getCell(M);
		if (descriptionCell.getCellType() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell moistureCell = row.getCell(N);
		if (moistureCell.getCellType() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		final Cell fatCell = row.getCell(O);
		if (fatCell.getCellType() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		final Cell detectionCell = row.getCell(P);
		if (detectionCell.getCellType() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		final Cell quantificationCell = row.getCell(Q);
		if (quantificationCell.getCellType() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		final Cell dataCell = row.getCell(R);
		if (dataCell.getCellType() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		final Cell contaminationCell = row.getCell(S);
		if (contaminationCell.getCellType() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		final Cell uncertaintyCell = row.getCell(T);
		if (uncertaintyCell.getCellType() == CellType.STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}


	public Parameter retrieveParameter(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		if (row.getCell(M).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		if (row.getCell(N).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		if (row.getCell(P).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(R).getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing data type");
		}

		final Parameter param = new Parameter();
		param.setId(row.getCell(L).getStringCellValue());

		final ParameterClassification pc = ParameterClassification.get(row.getCell(M).getStringCellValue());
		if (pc != null) {
			param.setClassification(SwaggerUtil.CLASSIF.get(pc));
		}

		param.setName(row.getCell(N).getStringCellValue());

		final Cell descriptionCell = row.getCell(O);
		if (descriptionCell.getCellType() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		param.setUnit(row.getCell(P).getStringCellValue());

		final Cell unitCategoryCell = row.getCell(Q);
		if (unitCategoryCell.getCellType() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		final ParameterType parameterType = ParameterType.get(row.getCell(R).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		final Cell sourceCell = row.getCell(S);
		if (sourceCell.getCellType() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		final Cell subjectCell = row.getCell(T);
		if (subjectCell.getCellType() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		final Cell distributionCell = row.getCell(U);
		if (distributionCell.getCellType() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		final Cell valueCell = row.getCell(V);
		if (valueCell.getCellType() != CellType.BLANK) {

			if (valueCell.getCellType() == CellType.NUMERIC) {
				final Double doubleValue = valueCell.getNumericCellValue();
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

		final Cell variabilitySubjectCell = row.getCell(X);
		if (variabilitySubjectCell.getCellType() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		final Cell maxCell = row.getCell(Y);
		if (maxCell.getCellType() != CellType.BLANK) {
			if(maxCell.getCellType() != CellType.STRING) {
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			} else {
				param.setMaxValue(maxCell.getStringCellValue());
			}

		}



		final Cell minCell = row.getCell(Z);
		if (minCell.getCellType() != CellType.BLANK) {
			if(minCell.getCellType() != CellType.STRING) {
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			} else {
				param.setMinValue(minCell.getStringCellValue());
			}
		}

		final Cell errorCell = row.getCell(AA);
		if (errorCell.getCellType() != CellType.BLANK) {
			if(errorCell.getCellType() != CellType.STRING) {
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			} else {
				param.setError(errorCell.getStringCellValue());
			}
		}

		return param;
	}

	public QualityMeasures retrieveQualityMeasures(Sheet sheet) {
		final QualityMeasures measures = new QualityMeasures();

		final Cell sseCell = sheet.getRow(QUALITY_MEASURES__SSE).getCell(M);
		if (sseCell.getCellType() == CellType.NUMERIC) {
			measures.setSse(BigDecimal.valueOf(sseCell.getNumericCellValue()));
		}

		final Cell mseCell = sheet.getRow(QUALITY_MEASURES__MSE).getCell(M);
		if (mseCell.getCellType() == CellType.NUMERIC) {
			measures.setMse(BigDecimal.valueOf(mseCell.getNumericCellValue()));
		}

		final Cell rmseCell = sheet.getRow(QUALITY_MEASURES__RMSE).getCell(M);
		if (rmseCell.getCellType() == CellType.NUMERIC) {
			measures.setRmse(BigDecimal.valueOf(rmseCell.getNumericCellValue()));
		}

		final Cell rsquareCell = sheet.getRow(QUALITY_MEASURES__RSQUARE).getCell(M);
		if (rsquareCell.getCellType() == CellType.NUMERIC) {
			measures.setRsquared(BigDecimal.valueOf(rsquareCell.getNumericCellValue()));
		}

		final Cell aicCell = sheet.getRow(QUALITY_MEASURES__AIC).getCell(M);
		if (aicCell.getCellType() == CellType.NUMERIC) {
			measures.setAic(BigDecimal.valueOf(aicCell.getNumericCellValue()));
		}

		final Cell bicCell = sheet.getRow(QUALITY_MEASURES__BIC).getCell(M);
		if (bicCell.getCellType() == CellType.NUMERIC) {
			measures.setBic(BigDecimal.valueOf(bicCell.getNumericCellValue()));
		}

		return measures;
	}

}
