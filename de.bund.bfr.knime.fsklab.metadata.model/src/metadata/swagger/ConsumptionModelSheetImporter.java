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
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.ConsumptionModelScope;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PublicationType;
import metadata.SwaggerUtil;

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
				Reference reference = retrieveReference(sheet.getRow(numRow));
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

	private ConsumptionModelScope retrieveScope(Sheet sheet) {

		ConsumptionModelScope scope = new ConsumptionModelScope();

		for (int numrow = SCOPE_PRODHAZPOP_ROW; numrow <= (SCOPE_PRODHAZPOP_ROW + 11); numrow++) {

			Row row = sheet.getRow(numrow);

			try {
				scope.addProductItem(retrieveProduct(row));
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

	private Product retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(N).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		Product product = new Product();
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

		Cell treatmentCell = row.getCell(P);
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
			Date date = productionDateCell.getDateCellValue();
			product.setProductionDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		Cell expiryDateCell = row.getCell(V);
		if (expiryDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date date = expiryDateCell.getDateCellValue();
			product.setExpiryDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		return product;
	}

	private Contact retrieveCreator(Row row) {
		@SuppressWarnings("serial")
		HashMap<String, Integer> columns = new HashMap<String, Integer>() {
			{
				put("mail", S);
				put("title", L);
				put("familyName", P);
				put("givenName", N);
				put("telephone", R);
				put("streetAddress", X);
				put("country", T);
				put("city", U);
				put("zipCode", V);
				put("region", Z);
				put("organization", Q);
			}
		};
		return retrieveContact(row, columns);
	}

	private Contact retrieveAuthor(Row row) {

		@SuppressWarnings("serial")
		HashMap<String, Integer> columns = new HashMap<String, Integer>() {
			{
				put("mail", AI);
				put("title", AB);
				put("familyName", AF);
				put("givenName", AD);
				put("telephone", AH);
				put("streetAddress", AN);
				put("country", AJ);
				put("city", AK);
				put("zipCode", AL);
				put("region", AP);
				put("organization", AG);
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

	private Reference retrieveReference(Row row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(P).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(L).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(P).getStringCellValue());

		// publication type
		Cell typeCell = row.getCell(M);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			if (type != null) {
				reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
			}
		}

		Cell dateCell = row.getCell(N);
		if (dateCell.getCellTypeEnum() == CellType.NUMERIC) {
			Date date = dateCell.getDateCellValue();
			LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
			reference.setDate(localDate);
		}

		Cell pmidCell = row.getCell(O);
		if (pmidCell.getCellTypeEnum() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		Cell authorListCell = row.getCell(Q);
		if (authorListCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		Cell titleCell = row.getCell(R);
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		Cell abstractCell = row.getCell(S);
		if (abstractCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		Cell statusCell = row.getCell(U);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		Cell websiteCell = row.getCell(V);
		if (websiteCell.getCellTypeEnum() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		Cell commentCell = row.getCell(W);
		if (commentCell.getCellTypeEnum() == CellType.STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
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

	private DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row) {

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

	private PopulationGroup retrievePopulationGroup(Row row) {

		// Check mandatory properties
		if (row.getCell(W).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();

		Cell nameCell = row.getCell(W);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		Cell targetPopulationCell = row.getCell(X);
		if (targetPopulationCell.getCellTypeEnum() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(Y);
		if (spanCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(Z);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(AA);
		if (ageCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(AB);
		if (genderCell.getCellTypeEnum() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(AC);
		if (bmiCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(AD);
		if (dietCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(AE);
		if (consumptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(AF);
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(AG);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(AH);
		if (factorsCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(AI);
		if (seasonCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
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
