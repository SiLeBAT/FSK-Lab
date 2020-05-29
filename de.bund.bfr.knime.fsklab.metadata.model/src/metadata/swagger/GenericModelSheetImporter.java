package metadata.swagger;

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
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PublicationType;
import metadata.SwaggerUtil;

public class GenericModelSheetImporter implements SheetImporter {

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

	private int QUALITY_MEASURES__SSE = 123;
	private int QUALITY_MEASURES__MSE = 124;
	private int QUALITY_MEASURES__RMSE = 125;
	private int QUALITY_MEASURES__RSQUARE = 126;
	private int QUALITY_MEASURES__AIC = 127;
	private int QUALITY_MEASURES__BIC = 128;

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
	private int BG_DIET_ASSESS_ROW = 103;
	private int BG_LABORATORY_ROW = 110;
	private int BG_ASSAY_ROW = 117;
	private int BG_QUALITY_MEAS_ROW = 123;
	private int BG_EVENT_ROW = 109;
	private int MM_PARAMETER_ROW = 132;
	private int MM_FITTING_PROCEDURE_ROW = 149;

	private GenericModelGeneralInformation retrieveGeneralInformation(Sheet sheet) {

		final GenericModelGeneralInformation information = new GenericModelGeneralInformation();

		final Cell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(I);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			information.setName(nameCell.getStringCellValue());
		}

		final Cell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(I);
		if (sourceCell.getCellTypeEnum() == CellType.STRING) {
			information.setSource(sourceCell.getStringCellValue());
		}

		final Cell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(I);
		if (identifierCell.getCellTypeEnum() == CellType.STRING) {
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
		if (creationDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date creationDate = creationDateCell.getDateCellValue();
			final LocalDate localDate = LocalDate.of(creationDate.getYear() + 1900, creationDate.getMonth() + 1,
					creationDate.getDate());
			information.setCreationDate(localDate);
		}

		// TODO: modificationDate

		final Cell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(I);
		if (rightsCell.getCellTypeEnum() == CellType.STRING) {
			information.setRights(rightsCell.getStringCellValue());
		}

		final Cell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(I);
		if (isAvailableCell.getCellTypeEnum() == CellType.STRING) {
			information.setAvailability(isAvailableCell.getStringCellValue());
		}

		final Cell urlCell = sheet.getRow(GENERAL_INFORMATION__URL).getCell(I);
		if (urlCell.getCellTypeEnum() == CellType.STRING) {
			information.setUrl(urlCell.getStringCellValue());
		}

		final Cell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(I);
		if (formatCell.getCellTypeEnum() == CellType.STRING) {
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
		if (languageCell.getCellTypeEnum() == CellType.STRING) {
			information.setLanguage(languageCell.getStringCellValue());
		}

		final Cell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(I);
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			information.setSoftware(softwareCell.getStringCellValue());
		}

		final Cell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(I);
		if (languageWrittenInCell.getCellTypeEnum() == CellType.STRING) {
			information.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		// model category (0..n)
		try {
			final ModelCategory category = retrieveModelCategory(sheet);
			information.setModelCategory(category);
		} catch (final Exception exception) {
		}

		final Cell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(I);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			information.setStatus(statusCell.getStringCellValue());
		}

		final Cell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(I);
		if (objectiveCell.getCellTypeEnum() == CellType.STRING) {
			information.setObjective(objectiveCell.getStringCellValue());
		}

		final Cell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(I);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			information.setDescription(descriptionCell.getStringCellValue());
		}

		return information;
	}

	private GenericModelModelMath retrieveModelMath(Sheet sheet) {

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

	private GenericModelScope retrieveScope(Sheet sheet) {

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
		if (generalCommentCell.getCellTypeEnum() == CellType.STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		final Cell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(I);
		if (temporalInformationCell.getCellTypeEnum() == CellType.STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}

	private Product retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(M).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		final Product product = new Product();
		product.setName(row.getCell(K).getStringCellValue());
		product.setUnit(row.getCell(M).getStringCellValue());

		final Cell descriptionCell = row.getCell(L);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell methodCell = row.getCell(N);
		if (methodCell.getCellTypeEnum() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		final Cell packagingCell = row.getCell(O);
		if (packagingCell.getCellTypeEnum() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		final Cell treatmentCell = row.getCell(P);
		if (treatmentCell.getCellTypeEnum() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		final Cell originCountryCell = row.getCell(Q);
		if (originCountryCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		final Cell originAreaCell = row.getCell(R);
		if (originAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		final Cell fisheriesAreaCell = row.getCell(S);
		if (fisheriesAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		final Cell productionDateCell = row.getCell(T);
		if (productionDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date date = productionDateCell.getDateCellValue();
			product.setProductionDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		final Cell expiryDateCell = row.getCell(U);
		if (expiryDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date date = expiryDateCell.getDateCellValue();
			product.setExpiryDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		return product;
	}

	private Contact retrieveCreator(Row row) {
		@SuppressWarnings("serial")
		final HashMap<String, Integer> columns = new HashMap<String, Integer>() {
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

	private Contact retrieveAuthor(Row row) {

		@SuppressWarnings("serial")
		final HashMap<String, Integer> columns = new HashMap<String, Integer>() {
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
		x = row.getCell(AA).getStringCellValue();
		x = row.getCell(AE).getStringCellValue();
		// Check mandatory properties and throw exception if missing
		if (row.getCell(columns.get("mail")).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing mail");
		}

		final Contact contact = new Contact();
		contact.setEmail(row.getCell(columns.get("mail")).getStringCellValue());

		final Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			contact.setTitle(titleCell.getStringCellValue());
		}

		final Cell familyNameCell = row.getCell(columns.get("familyName"));
		if (familyNameCell.getCellTypeEnum() == CellType.STRING) {
			contact.setFamilyName(familyNameCell.getStringCellValue());
		}

		final Cell givenNameCell = row.getCell(columns.get("givenName"));
		if (givenNameCell.getCellTypeEnum() == CellType.STRING) {
			contact.setGivenName(givenNameCell.getStringCellValue());
		}

		final Cell telephoneCell = row.getCell(columns.get("telephone"));
		if (telephoneCell.getCellTypeEnum() == CellType.STRING) {
			contact.setTelephone(telephoneCell.getStringCellValue());
		}

		final Cell streetAddressCell = row.getCell(columns.get("streetAddress"));
		if (streetAddressCell.getCellTypeEnum() == CellType.STRING) {
			contact.setStreetAddress(streetAddressCell.getStringCellValue());
		}

		final Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			contact.setCountry(countryCell.getStringCellValue());
		}

		final Cell zipCodeCell = row.getCell(columns.get("zipCode"));
		if (zipCodeCell.getCellTypeEnum() == CellType.STRING) {
			contact.setZipCode(zipCodeCell.getStringCellValue());
		}

		final Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			contact.setRegion(regionCell.getStringCellValue());
		}

		// Time zone not included in spreadsheet
		// gender not included in spreadsheet
		// note not included in spreadsheet

		final Cell organizationCell = row.getCell(columns.get("organization"));
		if (organizationCell.getCellTypeEnum() == CellType.STRING) {
			contact.setOrganization(organizationCell.getStringCellValue());
		}

		return contact;
	}

	private Reference retrieveReference(Row row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(O).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		final Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(O).getStringCellValue());

		// publication type
		final Cell typeCell = row.getCell(L);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			final PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			if (type != null) {
				reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
			}
		}

		final Cell dateCell = row.getCell(M);
		if (dateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date date = dateCell.getDateCellValue();
			final LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
			reference.setDate(localDate);
		}

		final Cell pmidCell = row.getCell(N);
		if (pmidCell.getCellTypeEnum() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		final Cell authorListCell = row.getCell(P);
		if (authorListCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		final Cell titleCell = row.getCell(Q);
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		final Cell abstractCell = row.getCell(R);
		if (abstractCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		final Cell statusCell = row.getCell(T);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		final Cell websiteCell = row.getCell(U);
		if (websiteCell.getCellTypeEnum() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		final Cell commentCell = row.getCell(V);
		if (commentCell.getCellTypeEnum() == CellType.STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	private ModelCategory retrieveModelCategory(Sheet sheet) {
		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		final ModelCategory category = new ModelCategory();

		category.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getStringCellValue());

		final Cell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(I);
		if (subClassCell.getCellTypeEnum() == CellType.STRING) {
			category.addModelSubClassItem(subClassCell.getStringCellValue());
		}

		final Cell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(I);
		if (modelClassCommentCell.getCellTypeEnum() == CellType.STRING) {
			category.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		final Cell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(I);
		if (basicProcessCell.getCellTypeEnum() == CellType.STRING) {
			category.addBasicProcessItem(basicProcessCell.getStringCellValue());
		}

		return category;
	}

	private Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(W).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		final Hazard hazard = new Hazard();
		hazard.setName(row.getCell(W).getStringCellValue());

		final Cell typeCell = row.getCell(V);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		final Cell hazardDescriptionCell = row.getCell(X);
		if (hazardDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		final Cell hazardUnitCell = row.getCell(Y);
		if (hazardUnitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		final Cell adverseEffect = row.getCell(Z);
		if (adverseEffect.getCellTypeEnum() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		final Cell sourceOfContaminationCell = row.getCell(AA);
		if (sourceOfContaminationCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		final Cell bmdCell = row.getCell(AB);
		if (bmdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		final Cell maximumResidueLimitCell = row.getCell(AC);
		if (maximumResidueLimitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		final Cell noaelCell = row.getCell(AD);
		if (noaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		final Cell loaelCell = row.getCell(AE);
		if (loaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		final Cell aoelCell = row.getCell(AF);
		if (aoelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		final Cell arfdCell = row.getCell(AG);
		if (arfdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		final Cell adiCell = row.getCell(AH);
		if (adiCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		final Cell indSumCell = row.getCell(AI);
		if (indSumCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	private PopulationGroup retrievePopulationGroup(Row row) {

		// Check mandatory properties
		if (row.getCell(AJ).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		final PopulationGroup group = new PopulationGroup();

		final Cell nameCell = row.getCell(AJ);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		final Cell targetPopulationCell = row.getCell(AK);
		if (targetPopulationCell.getCellTypeEnum() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		final Cell spanCell = row.getCell(AL);
		if (spanCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		final Cell descriptionCell = row.getCell(AM);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		final Cell ageCell = row.getCell(AN);
		if (ageCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		final Cell genderCell = row.getCell(AO);
		if (genderCell.getCellTypeEnum() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		final Cell bmiCell = row.getCell(AP);
		if (bmiCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		final Cell dietCell = row.getCell(AQ);
		if (dietCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		final Cell consumptionCell = row.getCell(AR);
		if (consumptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		final Cell regionCell = row.getCell(AS);
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		final Cell countryCell = row.getCell(AT);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		final Cell factorsCell = row.getCell(AU);
		if (factorsCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		final Cell seasonCell = row.getCell(AV);
		if (seasonCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}

	private Study retrieveStudy(Sheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		final Study study = new Study();

		final Cell identifierCell = sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I);
		if (identifierCell.getCellTypeEnum() == CellType.STRING) {
			study.setIdentifier(identifierCell.getStringCellValue());
		}

		study.setTitle(sheet.getRow(STUDY__STUDY_TITLE).getCell(I).getStringCellValue());

		final Cell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(I);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			study.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(I);
		if (designTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setDesignType(designTypeCell.getStringCellValue());
		}

		final Cell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(I);
		if (measurementTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		final Cell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(I);
		if (technologyTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		final Cell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(I);
		if (technologyPlatformCell.getCellTypeEnum() == CellType.STRING) {
			study.setAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		final Cell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(I);
		if (accreditationProcedureCell.getCellTypeEnum() == CellType.STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		final Cell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(I);
		if (protocolNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolName(protocolNameCell.getStringCellValue());
		}

		final Cell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(I);
		if (protocolTypeCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolType(protocolTypeCell.getStringCellValue());
		}

		final Cell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(I);
		if (protocolDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		final Cell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(I);
		if (protocolURICell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolURI(protocolURICell.getStringCellValue());
		}

		final Cell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(I);
		if (protocolVersionCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		final Cell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(I);
		if (parameterNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		final Cell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(I);
		if (componentNameCell.getCellTypeEnum() == CellType.STRING) {
			study.setProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		final Cell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(I);
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

		final StudySample sample = new StudySample();
		sample.setSampleName(row.getCell(L).getStringCellValue());
		sample.setProtocolOfSampleCollection(row.getCell(M).getStringCellValue());

		final Cell strategyCell = row.getCell(N);
		if (strategyCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		final Cell samplingProgramCell = row.getCell(O);
		if (samplingProgramCell.getCellTypeEnum() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		final Cell samplingMethodCell = row.getCell(P);
		if (samplingMethodCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(Q).getStringCellValue());
		sample.setSamplingWeight(row.getCell(R).getStringCellValue());
		sample.setSamplingSize(row.getCell(S).getStringCellValue());

		final Cell unitCell = row.getCell(T);
		if (unitCell.getCellTypeEnum() == CellType.STRING) {
			sample.setLotSizeUnit(row.getCell(T).getStringCellValue());
		}

		final Cell pointCell = row.getCell(U);
		if (pointCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingPoint(row.getCell(U).getStringCellValue());
		}

		return sample;
	}

	private DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(M).getCellTypeEnum() != CellType.NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		final DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(L).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(Double.toString(row.getCell(M).getNumericCellValue()));

		final Cell softwareCell = row.getCell(N);
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		final Cell foodItemsCell = row.getCell(O);
		if (foodItemsCell.getCellTypeEnum() == CellType.STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		final Cell recordTypesCell = row.getCell(P);
		if (recordTypesCell.getCellTypeEnum() == CellType.STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		final Cell foodDescriptorsCell = row.getCell(Q);
		if (foodDescriptorsCell.getCellTypeEnum() == CellType.STRING) {
			method.addFoodDescriptorsItem(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}

	private Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		final Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(L).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		final Cell nameCell = row.getCell(M);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setName(row.getCell(M).getStringCellValue());
		}

		final Cell countryCell = row.getCell(N);
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

		final Assay assay = new Assay();
		assay.setName(row.getCell(L).getStringCellValue());

		final Cell descriptionCell = row.getCell(M);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell moistureCell = row.getCell(N);
		if (moistureCell.getCellTypeEnum() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		final Cell fatCell = row.getCell(O);
		if (fatCell.getCellTypeEnum() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		final Cell detectionCell = row.getCell(P);
		if (detectionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		final Cell quantificationCell = row.getCell(Q);
		if (quantificationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		final Cell dataCell = row.getCell(R);
		if (dataCell.getCellTypeEnum() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		final Cell contaminationCell = row.getCell(S);
		if (contaminationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		final Cell uncertaintyCell = row.getCell(T);
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

		final Parameter param = new Parameter();
		param.setId(row.getCell(L).getStringCellValue());

		final ParameterClassification pc = ParameterClassification.get(row.getCell(M).getStringCellValue());
		if (pc != null) {
			param.setClassification(SwaggerUtil.CLASSIF.get(pc));
		}

		param.setName(row.getCell(N).getStringCellValue());

		final Cell descriptionCell = row.getCell(O);
		if (descriptionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		param.setUnit(row.getCell(P).getStringCellValue());

		final Cell unitCategoryCell = row.getCell(Q);
		if (unitCategoryCell.getCellTypeEnum() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		final ParameterType parameterType = ParameterType.get(row.getCell(R).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		final Cell sourceCell = row.getCell(S);
		if (sourceCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		final Cell subjectCell = row.getCell(T);
		if (subjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		final Cell distributionCell = row.getCell(U);
		if (distributionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		final Cell valueCell = row.getCell(V);
		if (valueCell.getCellTypeEnum() != CellType.BLANK) {

			if (valueCell.getCellTypeEnum() == CellType.NUMERIC) {
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
		if (variabilitySubjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		final Cell maxCell = row.getCell(Y);
		if (maxCell.getCellTypeEnum() != CellType.BLANK) {
			if (maxCell.getCellTypeEnum() != CellType.STRING) {
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			} else {
				param.setMaxValue(maxCell.getStringCellValue());
			}

		}

		final Cell minCell = row.getCell(Z);
		if (minCell.getCellTypeEnum() != CellType.BLANK) {
			if (minCell.getCellTypeEnum() != CellType.STRING) {
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			} else {
				param.setMinValue(minCell.getStringCellValue());
			}
		}

		final Cell errorCell = row.getCell(AA);
		if (errorCell.getCellTypeEnum() != CellType.BLANK) {
			if (errorCell.getCellTypeEnum() != CellType.STRING) {
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			} else {
				param.setError(errorCell.getStringCellValue());
			}
		}

		return param;
	}

	private QualityMeasures retrieveQualityMeasures(Sheet sheet) {
		final QualityMeasures measures = new QualityMeasures();

		final Cell sseCell = sheet.getRow(QUALITY_MEASURES__SSE).getCell(M);
		if (sseCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setSse(BigDecimal.valueOf(sseCell.getNumericCellValue()));
		}

		final Cell mseCell = sheet.getRow(QUALITY_MEASURES__MSE).getCell(M);
		if (mseCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setMse(BigDecimal.valueOf(mseCell.getNumericCellValue()));
		}

		final Cell rmseCell = sheet.getRow(QUALITY_MEASURES__RMSE).getCell(M);
		if (rmseCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setRmse(BigDecimal.valueOf(rmseCell.getNumericCellValue()));
		}

		final Cell rsquareCell = sheet.getRow(QUALITY_MEASURES__RSQUARE).getCell(M);
		if (rsquareCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setRsquared(BigDecimal.valueOf(rsquareCell.getNumericCellValue()));
		}

		final Cell aicCell = sheet.getRow(QUALITY_MEASURES__AIC).getCell(M);
		if (aicCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setAic(BigDecimal.valueOf(aicCell.getNumericCellValue()));
		}

		final Cell bicCell = sheet.getRow(QUALITY_MEASURES__BIC).getCell(M);
		if (bicCell.getCellTypeEnum() == CellType.NUMERIC) {
			measures.setBic(BigDecimal.valueOf(bicCell.getNumericCellValue()));
		}

		return measures;
	}

	@Override
	public Model retrieveModel(Sheet sheet) {
		GenericModel gm = new GenericModel();
		gm.setModelType("genericModel");
		gm.setGeneralInformation(retrieveGeneralInformation(sheet));
		gm.setScope(retrieveScope(sheet));
		gm.setDataBackground(retrieveBackground(sheet));
		gm.setModelMath(retrieveModelMath(sheet));
		
		return gm;
	}
}
