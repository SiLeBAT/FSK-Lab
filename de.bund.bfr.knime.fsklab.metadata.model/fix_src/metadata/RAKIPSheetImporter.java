package metadata;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

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
	private final int GENERAL_INFORMATION__RIGHTS = 8;
	private final int GENERAL_INFORMATION__AVAILABLE = 9;
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

	public GeneralInformation retrieveGeneralInformation(XSSFSheet sheet) {

		GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();

		XSSFCell nameCell = sheet.getRow(GENERAL_INFORMATION__NAME).getCell(I);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setName(nameCell.getStringCellValue());
		}

		XSSFCell sourceCell = sheet.getRow(GENERAL_INFORMATION__SOURCE).getCell(I);
		if (sourceCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setSource(sourceCell.getStringCellValue());
		}

		XSSFCell identifierCell = sheet.getRow(GENERAL_INFORMATION__IDENTIFIER).getCell(I);
		if (identifierCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setIdentifier(identifierCell.getStringCellValue());
		}

		// TODO: creation date

		XSSFCell rightsCell = sheet.getRow(GENERAL_INFORMATION__RIGHTS).getCell(I);
		if (rightsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setRights(rightsCell.getStringCellValue());
		}

		XSSFCell isAvailableCell = sheet.getRow(GENERAL_INFORMATION__AVAILABLE).getCell(I);
		if (isAvailableCell.getCellType() == Cell.CELL_TYPE_STRING) {
			String isAvailableString = isAvailableCell.getStringCellValue();
			generalInformation.setAvailable(isAvailableString.equals("Yes"));
		}

		XSSFCell formatCell = sheet.getRow(GENERAL_INFORMATION__FORMAT).getCell(I);
		if (formatCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setFormat(formatCell.getStringCellValue());
		}

		XSSFCell languageCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE).getCell(I);
		if (languageCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setLanguage(languageCell.getStringCellValue());
		}

		XSSFCell softwareCell = sheet.getRow(GENERAL_INFORMATION__SOFTWARE).getCell(I);
		if (softwareCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setSoftware(softwareCell.getStringCellValue());
		}

		XSSFCell languageWrittenInCell = sheet.getRow(GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN).getCell(I);
		if (languageWrittenInCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setLanguageWrittenIn(languageWrittenInCell.getStringCellValue());
		}

		XSSFCell statusCell = sheet.getRow(GENERAL_INFORMATION__STATUS).getCell(I);
		if (statusCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setStatus(statusCell.getStringCellValue());
		}

		XSSFCell objectiveCell = sheet.getRow(GENERAL_INFORMATION__OBJECTIVE).getCell(I);
		if (objectiveCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setObjective(objectiveCell.getStringCellValue());
		}

		XSSFCell descriptionCell = sheet.getRow(GENERAL_INFORMATION__DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			generalInformation.setDescription(descriptionCell.getStringCellValue());
		}

		// TODO: author (1..1)

		// creator (0..n)
		for (int numRow = 3; numRow < 7; numRow++) {
			try {
				Contact contact = retrieveContact(sheet.getRow(numRow));
				generalInformation.getCreators().add(contact);
			} catch (Exception exception) {
			}
		}

		// model category (0..n)
		try {
			ModelCategory modelCategory = retrieveModelCategory(sheet);
			generalInformation.getModelCategory().add(modelCategory);
		} catch (Exception exception) {
		}

		// reference (1..n)
		for (int numRow = 14; numRow < 17; numRow++) {
			try {
				Reference reference = retrieveReference(sheet.getRow(numRow));
				generalInformation.getReference().add(reference);
			} catch (Exception exception) {
			}
		}

		// TODO: modification date (0..n)

		return generalInformation;
	}

	/**
	 * @throw IllegalArgumentException if mail is empty.
	 */
	public Contact retrieveContact(XSSFRow row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(R).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing mail");
		}

		Contact contact = MetadataFactory.eINSTANCE.createContact();
		contact.setEmail(row.getCell(R).getStringCellValue());

		XSSFCell titleCell = row.getCell(K);
		if (titleCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setTitle(titleCell.getStringCellValue());
		}

		XSSFCell familyNameCell = row.getCell(O);
		if (familyNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setFamilyName(familyNameCell.getStringCellValue());
		}

		XSSFCell givenNameCell = row.getCell(M);
		if (givenNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setGivenName(givenNameCell.getStringCellValue());
		}

		XSSFCell telephoneCell = row.getCell(Q);
		if (telephoneCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setTelephone(telephoneCell.getStringCellValue());
		}

		XSSFCell streetAddressCell = row.getCell(W);
		if (streetAddressCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setStreetAddress(streetAddressCell.getStringCellValue());
		}

		XSSFCell countryCell = row.getCell(S);
		if (countryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setCountry(countryCell.getStringCellValue());
		}

		XSSFCell cityCell = row.getCell(T);
		if (cityCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setCity(cityCell.getStringCellValue());
		}

		XSSFCell zipCodeCell = row.getCell(U);
		if (zipCodeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setZipCode(zipCodeCell.getStringCellValue());
		}

		XSSFCell regionCell = row.getCell(Y);
		if (regionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setRegion(regionCell.getStringCellValue());
		}

		// time zone not included in spreadsheet ?
		// gender not included in spreadsheet ?
		// note not included in spreadsheet ?

		XSSFCell organizationCell = row.getCell(P);
		if (organizationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			contact.setOrganization(organizationCell.getStringCellValue());
		}

		return contact;
	}

	/**
	 * Import reference from Excel row.
	 * 
	 * <ul>
	 * <li>Is_reference_description? in the K column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Mandatory. Takes
	 * "Yes" or "No". Other strings are discarded.
	 *
	 * <li>Publication type in the L column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional. Takes
	 * the full name of a RIS reference type.
	 *
	 * <li>Date in the M column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional. Format
	 * `YYYY/MM/DD/other info` where the fields are optional. Examples:
	 * `2017/11/16/noon`, `2017/11/16`, `2017/11`, `2017`.
	 *
	 * <li>PubMed Id in the N column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC}. Optional. Unique
	 * unsigned integer. Example: 20069275
	 *
	 * <li>DOI in the O column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Mandatory.
	 * Example: 10.1056/NEJM199710303371801.
	 *
	 * <li>Publication author list in the P column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional. The
	 * authors are defined with last name, name and joined with semicolons. Example:
	 * `Ungaretti-Haberbeck,L;Plaza-Rodr�guez,C;Desvignes,V`
	 *
	 * <li>Publication title in the Q column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional.
	 *
	 * <li>Publication abstract in the R column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional.
	 *
	 * <li>Publication journal/vol/issue in the S column. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional.
	 *
	 * <li>Publication status. // TODO: publication status
	 *
	 * <li>Publication website. Type
	 * {@link org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING}. Optional. Invalid
	 * urls are discarded.
	 *
	 * @throws IllegalArgumentException
	 *             if isReferenceDescription or DOI are missing
	 */
	public Reference retrieveReference(XSSFRow row) {

		/*
		 * Journal, volume and issue cannot be parsed since there are encoded as free
		 * text in the S column.
		 * 
		 * The date format in Google Drive DD/MM/YYYY does not match the used in the
		 * local spreadsheet YYYY/MM/DD. Therefore it cannot be parsed. Column M.
		 * 
		 * The author list needs a explicitly defined splitter. The Google Drive
		 * spreadsheet does not define it and a comma is used in the local spreadsheet.
		 * What should we use to parse it?
		 */

		// Check mandatory properties and throw exception if missing
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(O).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		Reference reference = MetadataFactory.eINSTANCE.createReference();

		reference.setIsReferenceDescription(row.getCell(K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(O).getStringCellValue());

		XSSFCell publicationTypeCell = row.getCell(L);
		if (publicationTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			String publicationTypeLiteral = publicationTypeCell.getStringCellValue();
			PublicationType publicationType = PublicationType.get(publicationTypeLiteral);
			if (publicationType != null) {
				reference.setPublicationType(publicationType);
			}
		}

		XSSFCell publicationDateCell = row.getCell(M);
		if (publicationDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date publicationDate = publicationDateCell.getDateCellValue();
			publicationDate.setYear(publicationDate.getYear() + 1900);
			reference.setPublicationDate(publicationDate);
		}

		XSSFCell pmidCell = row.getCell(N);
		if (pmidCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		XSSFCell authorListCell = row.getCell(P);
		if (authorListCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		XSSFCell publicationTitleCell = row.getCell(Q);
		if (publicationTitleCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setPublicationTitle(publicationTitleCell.getStringCellValue());
		}

		XSSFCell publicationAbstractCell = row.getCell(R);
		if (publicationAbstractCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setPublicationAbstract(publicationAbstractCell.getStringCellValue());
		}

		// TODO: journal
		// TODO: issue

		XSSFCell publicationStatusCell = row.getCell(T);
		if (publicationStatusCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setPublicationStatus(publicationStatusCell.getStringCellValue());
		}

		XSSFCell websiteCell = row.getCell(U);
		if (websiteCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setPublicationWebsite(websiteCell.getStringCellValue());
		}

		XSSFCell commentCell = row.getCell(V);
		if (commentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	/**
	 * Import [ModelCategory] from Excel sheet.
	 *
	 * <ul>
	 * <li>Model class from H27. Mandatory.
	 * <li>Model sub class from H28. Optional.
	 * <li>Model class comment from H29. Optional.
	 * <li>Basic process from H32. Optional.
	 * </ul>
	 *
	 * @throws IllegalArgumentException
	 *             if modelClass is missing.
	 */
	public ModelCategory retrieveModelCategory(XSSFSheet sheet) {

		// Check mandatory properties and throw exception if missing
		if (sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing model class");
		}

		ModelCategory modelCategory = MetadataFactory.eINSTANCE.createModelCategory();

		modelCategory.setModelClass(sheet.getRow(MODEL_CATEGORY__MODEL_CLASS).getCell(I).getStringCellValue());

		XSSFCell subClassCell = sheet.getRow(MODEL_CATEGORY__MODEL_SUB_CLASS).getCell(I);
		if (subClassCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(subClassCell.getStringCellValue());
			modelCategory.getModelSubClass().addAll(stringObjects);
		}

		XSSFCell modelClassCommentCell = sheet.getRow(MODEL_CATEGORY__CLASS_COMMENT).getCell(I);
		if (modelClassCommentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			modelCategory.setModelClassComment(modelClassCommentCell.getStringCellValue());
		}

		XSSFCell basicProcessCell = sheet.getRow(MODEL_CATEGORY__BASIC_PROCESS).getCell(I);
		if (basicProcessCell.getCellType() == Cell.CELL_TYPE_STRING) {
			modelCategory.setBasicProcess(basicProcessCell.getStringCellValue());
		}

		return modelCategory;
	}

	public Scope retrieveScope(XSSFSheet sheet) {

		Scope scope = MetadataFactory.eINSTANCE.createScope();

		for (int numrow = 38; numrow <= 49; numrow++) {

			XSSFRow row = sheet.getRow(38);

			try {
				scope.getProduct().add(retrieveProduct(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.getHazard().add(retrieveHazard(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since products are optional (*)
			}

			try {
				scope.getPopulationGroup().add(retrievePopulationGroup(row));
			} catch (IllegalArgumentException exception) {
				// ignore exception since population groups are optional (*)
			}
		}

		XSSFCell generalCommentCell = sheet.getRow(SCOPE__GENERAL_COMMENT).getCell(I);
		if (generalCommentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			scope.setGeneralComment(generalCommentCell.getStringCellValue());
		}

		XSSFCell temporalInformationCell = sheet.getRow(SCOPE__TEMPORAL_INFORMATION).getCell(I);
		if (temporalInformationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			scope.setTemporalInformation(temporalInformationCell.getStringCellValue());
		}

		// TODO: Spatial information

		return scope;
	}

	public DataBackground retrieveDataBackground(XSSFSheet sheet) {

		DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();

		dataBackground.setStudy(retrieveStudy(sheet));

		for (int numrow = 96; numrow < 99; numrow++) {
			try {
				StudySample studySample = retrieveStudySample(sheet.getRow(numrow));
				dataBackground.getStudysample().add(studySample);
			} catch (Exception exception) {
			}
		}

		for (int numrow = 103; numrow < 106; numrow++) {
			try {
				DietaryAssessmentMethod method = retrieveDietaryAssessmentMethod(sheet.getRow(numrow));
				dataBackground.getDietaryassessmentmethod().add(method);
			} catch (Exception exception) {
			}
		}

		for (int numrow = 110; numrow < 112; numrow++) {
			try {
				Laboratory laboratory = retrieveLaboratory(sheet.getRow(numrow));
				dataBackground.getLaboratory().add(laboratory);
			} catch (Exception exception) {
			}
		}

		for (int numrow = 117; numrow < 120; numrow++) {
			try {
				Assay assay = retrieveAssay(sheet.getRow(numrow));
				dataBackground.getAssay().add(assay);
			} catch (Exception exception) {
				// ignore errors since Assay is optional
			}
		}

		return dataBackground;
	}

	public Study retrieveStudy(XSSFSheet sheet) {

		// Check first mandatory properties
		if (sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing study title");
		}

		Study study = MetadataFactory.eINSTANCE.createStudy();

		study.setStudyIdentifier(sheet.getRow(STUDY__STUDY_IDENTIFIER).getCell(I).getStringCellValue());

		XSSFCell titleCell = sheet.getRow(STUDY__STUDY_TITLE).getCell(I);
		if (titleCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyTitle(titleCell.getStringCellValue());
		}

		XSSFCell descriptionCell = sheet.getRow(STUDY__STUDY_DESCRIPTION).getCell(I);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyDescription(descriptionCell.getStringCellValue());
		}

		XSSFCell designTypeCell = sheet.getRow(STUDY__STUDY_DESIGN_TYPE).getCell(I);
		if (designTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyDesignType(designTypeCell.getStringCellValue());
		}

		XSSFCell measurementTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_MEASUREMENT_TYPE).getCell(I);
		if (measurementTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyAssayMeasurementType(measurementTypeCell.getStringCellValue());
		}

		XSSFCell technologyTypeCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE).getCell(I);
		if (technologyTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyAssayTechnologyType(technologyTypeCell.getStringCellValue());
		}

		XSSFCell technologyPlatformCell = sheet.getRow(STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM).getCell(I);
		if (technologyPlatformCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyAssayTechnologyPlatform(technologyPlatformCell.getStringCellValue());
		}

		XSSFCell accreditationProcedureCell = sheet.getRow(STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY)
				.getCell(I);
		if (accreditationProcedureCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setAccreditationProcedureForTheAssayTechnology(accreditationProcedureCell.getStringCellValue());
		}

		XSSFCell protocolNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_NAME).getCell(I);
		if (protocolNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolName(protocolNameCell.getStringCellValue());
		}

		XSSFCell protocolTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_TYPE).getCell(I);
		if (protocolTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolType(protocolTypeCell.getStringCellValue());
		}

		XSSFCell protocolDescriptionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_DESCRIPTION).getCell(I);
		if (protocolDescriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolDescription(protocolDescriptionCell.getStringCellValue());
		}

		XSSFCell protocolURICell = sheet.getRow(STUDY__STUDY_PROTOCOL_URI).getCell(I);
		if (protocolURICell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolURI(URI.create(protocolURICell.getStringCellValue()));
		}

		XSSFCell protocolVersionCell = sheet.getRow(STUDY__STUDY_PROTOCOL_VERSION).getCell(I);
		if (protocolVersionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolVersion(protocolVersionCell.getStringCellValue());
		}

		XSSFCell parameterNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_PARAMETERS_NAME).getCell(I);
		if (parameterNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolParametersName(parameterNameCell.getStringCellValue());
		}

		XSSFCell componentNameCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_NAME).getCell(I);
		if (componentNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolComponentsName(componentNameCell.getStringCellValue());
		}

		XSSFCell componentTypeCell = sheet.getRow(STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE).getCell(I);
		if (componentTypeCell.getCellType() == Cell.CELL_TYPE_STRING) {
			study.setStudyProtocolComponentsType(componentTypeCell.getStringCellValue());
		}

		return study;
	}

	public StudySample retrieveStudySample(XSSFRow row) {

		// Check mandatory properties
		if (row.getCell(L).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		if (row.getCell(M).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		if (row.getCell(Q).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		if (row.getCell(R).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		if (row.getCell(S).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		StudySample studySample = MetadataFactory.eINSTANCE.createStudySample();
		studySample.setSampleName(getString(row.getCell(L)));
		studySample.setProtocolOfSampleCollection(getString(row.getCell(M)));

		XSSFCell strategyCell = row.getCell(N);
		if (strategyCell.getCellType() == Cell.CELL_TYPE_STRING) {
			studySample.setSamplingStrategy(getString(strategyCell));
		}

		XSSFCell samplingProgramCell = row.getCell(O);
		if (samplingProgramCell.getCellType() == Cell.CELL_TYPE_STRING) {
			studySample.setTypeOfSamplingProgram(getString(samplingProgramCell));
		}

		XSSFCell samplingMethodCell = row.getCell(P);
		if (samplingMethodCell.getCellType() == Cell.CELL_TYPE_STRING) {
			studySample.setSamplingMethod(getString(samplingMethodCell));
		}

		studySample.setSamplingPlan(getString(row.getCell(Q)));
		studySample.setSamplingWeight(getString(row.getCell(R)));
		studySample.setSamplingSize(getString(row.getCell(S)));

		XSSFCell unitCell = row.getCell(T);
		if (unitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			studySample.setLotSizeUnit(getString(unitCell));
		}

		XSSFCell pointCell = row.getCell(U);
		if (pointCell.getCellType() == Cell.CELL_TYPE_STRING) {
			studySample.setSamplingPoint(getString(pointCell));
		}

		return studySample;
	}

	public Parameter retrieveParameter(XSSFRow row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		if (row.getCell(M).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		if (row.getCell(N).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		if (row.getCell(Q).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(S).getCellType() == Cell.CELL_TYPE_BLANK) {
			throw new IllegalArgumentException("Missing data type");
		}

		Parameter param = MetadataFactory.eINSTANCE.createParameter();
		param.setParameterID(row.getCell(L).getStringCellValue());

		String clasifLiteral = row.getCell(M).getStringCellValue();
		ParameterClassification clasif = ParameterClassification.get(clasifLiteral);
		if (clasif != null) {
			param.setParameterClassification(clasif);
		}

		param.setParameterName(row.getCell(N).getStringCellValue());

		XSSFCell descriptionCell = row.getCell(O);
		if (descriptionCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterDescription(getString(descriptionCell));
		}

		XSSFCell typeCell = row.getCell(P);
		if (typeCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterType(getString(typeCell));
		}

		param.setParameterUnit(row.getCell(Q).getStringCellValue());

		XSSFCell unitCategoryCell = row.getCell(R);
		if (unitCategoryCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterUnitCategory(getString(unitCategoryCell));
		}

		String dataTypeLiteral = row.getCell(S).getStringCellValue();
		ParameterType parameterType = ParameterType.get(dataTypeLiteral);
		if (parameterType != null) {
			param.setParameterDataType(parameterType);
		}

		XSSFCell parameterSourceCell = row.getCell(T);
		if (parameterSourceCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterSource(getString(parameterSourceCell));
		}

		XSSFCell parameterSubjectCell = row.getCell(U);
		if (parameterSubjectCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterSubject(getString(parameterSubjectCell));
		}

		XSSFCell parameterDistributionCell = row.getCell(V);
		if (parameterDistributionCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterDistribution(getString(parameterDistributionCell));
		}

		XSSFCell parameterValueCell = row.getCell(W);
		if (parameterValueCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterValue(getString(parameterValueCell));
		}

		// TODO: reference

		XSSFCell variabilitySubjectCell = row.getCell(Y);
		if (variabilitySubjectCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterVariabilitySubject(getString(variabilitySubjectCell));
		}

		XSSFCell maxCell = row.getCell(Z);
		if (maxCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterValueMax(getString(maxCell));
		}

		XSSFCell minCell = row.getCell(AA);
		if (minCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterValueMin(getString(minCell));
		}

		XSSFCell errorCell = row.getCell(AB);
		if (errorCell.getCellType() != Cell.CELL_TYPE_BLANK) {
			param.setParameterError(getString(errorCell));
		}

		return param;
	}

	public Product retrieveProduct(XSSFRow row) {

		// Check first mandatory properties
		if (row.getCell(K).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(M).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		Product product = MetadataFactory.eINSTANCE.createProduct();

		// Product name does not need to be check for emptiness since it was already
		// checked with the cell type: CELL_TYPE_BLANK
		product.setProductName(row.getCell(K).getStringCellValue());

		// product description
		XSSFCell productDescriptionCell = row.getCell(L);
		if (productDescriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setProductDescription(productDescriptionCell.getStringCellValue());
		}

		// product unit
		product.setProductUnit(row.getCell(M).getStringCellValue());

		// production method
		XSSFCell productionMethodCell = row.getCell(N);
		if (productionMethodCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setProductionMethod(productionMethodCell.getStringCellValue());
		}

		// packaging
		XSSFCell packagingCell = row.getCell(O);
		if (packagingCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setPackaging(packagingCell.getStringCellValue());
		}

		// product treatment
		XSSFCell productTreatmentCell = row.getCell(P);
		if (productTreatmentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setProductTreatment(productTreatmentCell.getStringCellValue());
		}

		// origin country
		XSSFCell originCountryCell = row.getCell(Q);
		if (originCountryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		// origin area
		XSSFCell originAreaCell = row.getCell(R);
		if (originAreaCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		// fisheries area
		XSSFCell fisheriesAreaCell = row.getCell(S);
		if (fisheriesAreaCell.getCellType() == Cell.CELL_TYPE_STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		// production date
		XSSFCell productionDateCell = row.getCell(T);
		if (productionDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date productionDate = productionDateCell.getDateCellValue();
			productionDate.setYear(productionDate.getYear() + 1900);
			product.setProductionDate(productionDate);
		}

		// expiry date U
		XSSFCell expiryDateCell = row.getCell(U);
		if (expiryDateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			Date expiryDate = expiryDateCell.getDateCellValue();
			expiryDate.setYear(expiryDate.getYear() + 1900);
			product.setExpiryDate(expiryDate);
		}

		return product;
	}

	public Hazard retrieveHazard(XSSFRow row) {

		// Check mandatory properties
		if (row.getCell(V).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Hazard type is missing");
		}
		if (row.getCell(W).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}
		if (row.getCell(Y).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Hazard unit is missing");
		}

		Hazard hazard = MetadataFactory.eINSTANCE.createHazard();
		hazard.setHazardType(row.getCell(V).getStringCellValue());
		hazard.setHazardName(row.getCell(W).getStringCellValue());
		hazard.setHazardUnit(row.getCell(Y).getStringCellValue());

		XSSFCell hazardDescriptionCell = row.getCell(X);
		if (hazardDescriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setHazardDescription(hazardDescriptionCell.getStringCellValue());
		}

		XSSFCell hazardUnitCell = row.getCell(Y);
		if (hazardUnitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setHazardUnit(hazardUnitCell.getStringCellValue());
		}

		XSSFCell adverseEffect = row.getCell(Z);
		if (adverseEffect.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		XSSFCell sourceOfContaminationCell = row.getCell(AA);
		if (sourceOfContaminationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		XSSFCell bmdCell = row.getCell(AB);
		if (bmdCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		XSSFCell maximumResidueLimitCell = row.getCell(AC);
		if (maximumResidueLimitCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		XSSFCell noaelCell = row.getCell(AD);
		if (noaelCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		XSSFCell loaelCell = row.getCell(AE);
		if (loaelCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		XSSFCell aoelCell = row.getCell(AF);
		if (aoelCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAcceptableOperatorExposureLevel(aoelCell.getStringCellValue());
		}

		XSSFCell arfdCell = row.getCell(AG);
		if (arfdCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		XSSFCell adiCell = row.getCell(AH);
		if (adiCell.getCellType() == Cell.CELL_TYPE_STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		return hazard;
	}

	public PopulationGroup retrievePopulationGroup(XSSFRow row) {

		// Check mandatory properties
		if (row.getCell(AU).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing Risk and population factors");
		}

		PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();

		XSSFCell nameCell = row.getCell(AJ);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			populationGroup.setPopulationName(nameCell.getStringCellValue());
		}

		XSSFCell targetPopulationCell = row.getCell(AK);
		if (targetPopulationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			populationGroup.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		XSSFCell spanCell = row.getCell(AL);
		if (spanCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(spanCell.getStringCellValue());
			populationGroup.getPopulationSpan().addAll(stringObjects);
		}

		XSSFCell descriptionCell = row.getCell(AM);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(descriptionCell.getStringCellValue());
			populationGroup.getPopulationDescription().addAll(stringObjects);
		}

		XSSFCell ageCell = row.getCell(AN);
		if (ageCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(ageCell.getStringCellValue());
			populationGroup.getPopulationAge().addAll(stringObjects);
		}

		XSSFCell genderCell = row.getCell(AO);
		if (genderCell.getCellType() == Cell.CELL_TYPE_STRING) {
			populationGroup.setPopulationGender(genderCell.getStringCellValue());
		}

		XSSFCell bmiCell = row.getCell(AP);
		if (bmiCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(bmiCell.getStringCellValue());
			populationGroup.getBmi().addAll(stringObjects);
		}

		XSSFCell dietCell = row.getCell(AQ);
		if (dietCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(dietCell.getStringCellValue());
			populationGroup.getSpecialDietGroups().addAll(stringObjects);
		}

		XSSFCell consumptionCell = row.getCell(AR);
		if (consumptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(consumptionCell.getStringCellValue());
			populationGroup.getPatternConsumption().addAll(stringObjects);
		}

		XSSFCell regionCell = row.getCell(AS);
		if (regionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(regionCell.getStringCellValue());
			populationGroup.getRegion().addAll(stringObjects);
		}

		XSSFCell countryCell = row.getCell(AT);
		if (countryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(countryCell.getStringCellValue());
			populationGroup.getCountry().addAll(stringObjects);
		}

		XSSFCell factorsCell = row.getCell(AU);
		if (factorsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(factorsCell.getStringCellValue());
			populationGroup.getPopulationRiskFactor().addAll(stringObjects);
		}

		XSSFCell seasonCell = row.getCell(AV);
		if (factorsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			List<StringObject> stringObjects = toStringObject(seasonCell.getStringCellValue());
			populationGroup.getSeason().addAll(stringObjects);
		}

		return populationGroup;
	}

	public DietaryAssessmentMethod retrieveDietaryAssessmentMethod(XSSFRow row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(M).getCellType() != Cell.CELL_TYPE_NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		DietaryAssessmentMethod method = MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(L).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(new Double(row.getCell(M).getNumericCellValue()).intValue());

		XSSFCell softwareCell = row.getCell(N);
		if (softwareCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		XSSFCell foodItemsCell = row.getCell(O);
		if (foodItemsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.setNumberOfFoodItems(foodItemsCell.getStringCellValue());
		}

		XSSFCell recordTypesCell = row.getCell(P);
		if (recordTypesCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.setRecordTypes(recordTypesCell.getStringCellValue());
		}

		XSSFCell foodDescriptorsCell = row.getCell(Q);
		if (foodDescriptorsCell.getCellType() == Cell.CELL_TYPE_STRING) {
			method.setFoodDescriptors(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}

	public Laboratory retrieveLaboratory(XSSFRow row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = MetadataFactory.eINSTANCE.createLaboratory();
		laboratory.getLaboratoryAccreditation().addAll(toStringObject(row.getCell(L).getStringCellValue()));

		XSSFCell nameCell = row.getCell(M);
		if (nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
			laboratory.setLaboratoryName(row.getCell(M).getStringCellValue());
		}

		XSSFCell countryCell = row.getCell(N);
		if (countryCell.getCellType() == Cell.CELL_TYPE_STRING) {
			laboratory.setLaboratoryCountry(row.getCell(N).getStringCellValue());
		}

		return laboratory;
	}

	public Assay retrieveAssay(XSSFRow row) {

		// Check first mandatory properties
		if (row.getCell(L).getCellType() != Cell.CELL_TYPE_STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		Assay assay = MetadataFactory.eINSTANCE.createAssay();
		assay.setAssayName(row.getCell(L).getStringCellValue());

		XSSFCell descriptionCell = row.getCell(M);
		if (descriptionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setAssayDescription(descriptionCell.getStringCellValue());
		}

		XSSFCell moistureCell = row.getCell(N);
		if (moistureCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setPercentageOfMoisture(moistureCell.getStringCellValue());
		}

		XSSFCell fatCell = row.getCell(O);
		if (fatCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setPercentageOfFat(fatCell.getStringCellValue());
		}

		XSSFCell detectionCell = row.getCell(P);
		if (detectionCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setLimitOfDetection(detectionCell.getStringCellValue());
		}

		XSSFCell quantificationCell = row.getCell(Q);
		if (quantificationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setLimitOfQuantification(quantificationCell.getStringCellValue());
		}

		XSSFCell dataCell = row.getCell(R);
		if (dataCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		XSSFCell contaminationCell = row.getCell(S);
		if (contaminationCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setRangeOfContamination(contaminationCell.getStringCellValue());
		}

		XSSFCell uncertaintyCell = row.getCell(T);
		if (uncertaintyCell.getCellType() == Cell.CELL_TYPE_STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}

	public JsonObject retrieveQualityMeasures(XSSFSheet sheet) {

		XSSFCell sseCell = sheet.getRow(QUALITY_MEASURES__SSE).getCell(M);
		double sse = sseCell.getCellType() == Cell.CELL_TYPE_NUMERIC ? sseCell.getNumericCellValue() : 0;

		XSSFCell mseCell = sheet.getRow(QUALITY_MEASURES__MSE).getCell(M);
		double mse = mseCell.getCellType() == Cell.CELL_TYPE_NUMERIC ? mseCell.getNumericCellValue() : 0;

		XSSFCell rmseCell = sheet.getRow(QUALITY_MEASURES__RMSE).getCell(M);
		double rmse = rmseCell.getCellType() == Cell.CELL_TYPE_NUMERIC ? rmseCell.getNumericCellValue() : 0;

		XSSFCell rsquareCell = sheet.getRow(QUALITY_MEASURES__RSQUARE).getCell(M);
		double rsquare = rsquareCell.getCellType() == Cell.CELL_TYPE_NUMERIC ? rsquareCell.getNumericCellValue() : 0;

		XSSFCell aicCell = sheet.getRow(QUALITY_MEASURES__AIC).getCell(M);
		double aic = aicCell.getCellType() == Cell.CELL_TYPE_NUMERIC ? aicCell.getNumericCellValue() : 0;

		XSSFCell bicCell = sheet.getRow(QUALITY_MEASURES__BIC).getCell(M);
		double bic = bicCell.getCellType() == Cell.CELL_TYPE_NUMERIC ? bicCell.getNumericCellValue() : 0;

		return Json.createObjectBuilder().add("SSE", sse).add("MSE", mse).add("RMSE", rmse).add("Rsquared", rsquare)
				.add("AIC", aic).add("BIC", bic).build();
	}

	public ModelMath retrieveModelMath(XSSFSheet sheet) {

		ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

		RAKIPSheetImporter importer = new RAKIPSheetImporter();

		for (int rownum = 132; rownum < 161; rownum++) {
			try {
				XSSFRow row = sheet.getRow(rownum);
				Parameter param = importer.retrieveParameter(row);
				modelMath.getParameter().add(param);
			} catch (Exception exception) {
				// ...
			}
		}

		try {
			JsonObject measures = retrieveQualityMeasures(sheet);
			
			StringObject so = MetadataFactory.eINSTANCE.createStringObject();
			so.setValue(measures.toString());
			modelMath.getQualityMeasures().add(so);
		} catch (Exception exception) {
			// ...
		}

		return modelMath;
	}

	static List<StringObject> toStringObject(String string) {

		List<StringObject> stringObjects = new ArrayList<>();

		for (String token : string.split(",")) {
			StringObject so = MetadataFactory.eINSTANCE.createStringObject();
			so.setValue(token);
			stringObjects.add(so);
		}

		return stringObjects;
	}

	private String getString(XSSFCell cell) {

		int cellType = cell.getCellType();

		if (cellType == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}

		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			return Double.toString(cell.getNumericCellValue());
		}

		if (cellType == Cell.CELL_TYPE_FORMULA) {
			try {
				return cell.getStringCellValue();
			} catch (RuntimeException exception) {
				// An exception is thrown for non-string formulas
				return "";
			}
		}
		return "";
	}
}