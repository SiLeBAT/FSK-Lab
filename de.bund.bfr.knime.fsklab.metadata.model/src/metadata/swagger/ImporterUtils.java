package metadata.swagger;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.StudySample;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.SwaggerUtil;

/** Utility methods for importers. */
public class ImporterUtils {

	private ImporterUtils() {
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>mail
	 *                <li>title
	 *                <li>familyName
	 *                <li>givenName
	 *                <li>telephone
	 *                <li>streetAddress
	 *                <li>country
	 *                <li>zipCode
	 *                <li>region
	 *                <li>organization
	 *                </ul>
	 */
	public static Contact retrieveContact(Row row, Map<String, Integer> columns) {

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
		if (zipCodeCell.getCellTypeEnum() == CellType.NUMERIC) {
			double zipCodeAsDouble = zipCodeCell.getNumericCellValue();
			contact.setZipCode(Integer.toString((int) zipCodeAsDouble));
		} else if (zipCodeCell.getCellTypeEnum() == CellType.STRING) {
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

	public static Product retrieveProduct(Row row, Map<String, Integer> columns) {

		// Check first mandatory properties
		if (row.getCell(columns.get("name")).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(columns.get("unit")).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		final Product product = new Product();
		product.setName(row.getCell(columns.get("name")).getStringCellValue());
		product.setUnit(row.getCell(columns.get("unit")).getStringCellValue());

		final Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell methodCell = row.getCell(columns.get("productionMethod"));
		if (methodCell.getCellTypeEnum() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		final Cell packagingCell = row.getCell(columns.get("packaging"));
		if (packagingCell.getCellTypeEnum() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		final Cell treatmentCell = row.getCell(columns.get("treatment"));
		if (treatmentCell.getCellTypeEnum() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		final Cell originCountryCell = row.getCell(columns.get("originCountry"));
		if (originCountryCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		final Cell originAreaCell = row.getCell(columns.get("originArea"));
		if (originAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		final Cell fisheriesAreaCell = row.getCell(columns.get("fisheriesArea"));
		if (fisheriesAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		final Cell productionDateCell = row.getCell(columns.get("productionDate"));
		if (productionDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			product.setProductionDate(retrieveDate(productionDateCell));
		}

		final Cell expiryDateCell = row.getCell(columns.get("expiryDate"));
		if (expiryDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			product.setExpiryDate(retrieveDate(expiryDateCell));
		}

		return product;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>type
	 *                <li>name
	 *                <li>description
	 *                <li>unit
	 *                <li>adverseEffect
	 *                <li>sourceOfContamination
	 *                <li>benchmarkDose
	 *                <li>maximumResidueLimit
	 *                <li>noObservedAdverseAffectLevel
	 *                <li>lowestObservedAdverseAffectLevel
	 *                <li>acceptableOperatorsExposureLevel
	 *                <li>acuteReferenceDose
	 *                <li>acceptableDailyIntake
	 *                <li>indSum
	 *                </ul>
	 */
	public static Hazard retrieveHazard(Row row, Map<String, Integer> columns) {
		
		// Check mandatory properties
		final Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell.getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		final Hazard hazard = new Hazard();
		hazard.setName(nameCell.getStringCellValue());

		final Cell typeCell = row.getCell(columns.get("type"));
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		final Cell hazardDescriptionCell = row.getCell(columns.get("description"));
		if (hazardDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		final Cell hazardUnitCell = row.getCell(columns.get("unit"));
		if (hazardUnitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		final Cell adverseEffect = row.getCell(columns.get("adverseEffect"));
		if (adverseEffect.getCellTypeEnum() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		final Cell sourceOfContaminationCell = row.getCell(columns.get("sourceOfContamination"));
		if (sourceOfContaminationCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		final Cell bmdCell = row.getCell(columns.get("benchmarkDose"));
		if (bmdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		final Cell maximumResidueLimitCell = row.getCell(columns.get("maximumResidueLimit"));
		if (maximumResidueLimitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		final Cell noaelCell = row.getCell(columns.get("noObservedAdverseAffectLevel"));
		if (noaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		final Cell loaelCell = row.getCell(columns.get("lowestObservedAdverseAffectLevel"));
		if (loaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		final Cell aoelCell = row.getCell(columns.get("acceptableOperatorsExposureLevel"));
		if (aoelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		final Cell arfdCell = row.getCell(columns.get("acuteReferenceDose"));
		if (arfdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		final Cell adiCell = row.getCell(columns.get("acceptableDailyIntake"));
		if (adiCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		final Cell indSumCell = row.getCell(columns.get("indSum"));
		if (indSumCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>name
	 *                <li>targetPopulation
	 *                <li>span
	 *                <li>description
	 *                <li>age
	 *                <li>gender
	 *                <li>bmi
	 *                <li>diet
	 *                <li>consumption
	 *                <li>region
	 *                <li>country
	 *                <li>risk
	 *                <li>season
	 *                </ul>
	 */
	public static PopulationGroup retrievePopulationGroup(Row row, Map<String, Integer> columns) {

		// Check mandatory properties
		Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell.getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();
		group.setName(nameCell.getStringCellValue());

		Cell targetPopulationCell = row.getCell(columns.get("targetPopulation"));
		if (targetPopulationCell.getCellTypeEnum() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(columns.get("span"));
		if (spanCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(columns.get("age"));
		if (ageCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(columns.get("gender"));
		if (genderCell.getCellTypeEnum() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(columns.get("bmi"));
		if (bmiCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(columns.get("diet"));
		if (dietCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(columns.get("consumption"));
		if (consumptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(columns.get("risk"));
		if (factorsCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(columns.get("season"));
		if (seasonCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>referenceDescription
	 *                <li>type
	 *                <li>date
	 *                <li>year
	 *                <li>pmid
	 *                <li>doi
	 *                <li>author
	 *                <li>title
	 *                <li>abstract
	 *                <li>status
	 *                <li>website
	 *                <li>comment
	 *                </ul>
	 */
	public static Reference retrieveReference(Row row, Map<String, Integer> columns) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(columns.get("referenceDescription")).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(columns.get("doi")).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		final Reference reference = new Reference();
		reference.setIsReferenceDescription(
				row.getCell(columns.get("referenceDescription")).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(columns.get("doi")).getStringCellValue());

		// TODO: the types in the spreadsheet are not compatible with the RIS types.
		// The annotation template must be updated or the values must be mapped here.

		final Cell dateCell = row.getCell(columns.get("date"));
		if (dateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final LocalDate localDate = retrieveDate(dateCell);
			reference.setDate(localDate);
		}

		final Cell pmidCell = row.getCell(columns.get("pmid"));
		if (pmidCell.getCellTypeEnum() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		final Cell authorListCell = row.getCell(columns.get("author"));
		if (authorListCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		final Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		final Cell abstractCell = row.getCell(columns.get("abstract"));
		if (abstractCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		final Cell statusCell = row.getCell(columns.get("status"));
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		final Cell websiteCell = row.getCell(columns.get("website"));
		if (websiteCell.getCellTypeEnum() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		final Cell commentCell = row.getCell(columns.get("comment"));
		if (commentCell.getCellTypeEnum() == CellType.STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>sample
	 *                <li>protocolOfSampleCollection
	 *                <li>samplingStrategy
	 *                <li>samplingProgramType
	 *                <li>samplingMethod
	 *                <li>samplingPlan
	 *                <li>samplingWeight
	 *                <li>samplingSize
	 *                <li>lotSizeUnit
	 *                <li>samplingPoint
	 *                </ul>
	 */
	public static StudySample retrieveStudySample(Row row, Map<String, Integer> columns) {

		// Check mandatory properties
		final Cell sampleNameCell = row.getCell(columns.get("sample"));
		if (sampleNameCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		
		final Cell protocolCell = row.getCell(columns.get("protocolOfSampleCollection"));
		if (protocolCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		
		final Cell methodCell = row.getCell(columns.get("samplingMethod"));
		if (methodCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		
		final Cell weightCell = row.getCell(columns.get("samplingWeight"));
		if (weightCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		
		final Cell sizeCell = row.getCell(columns.get("samplingSize"));
		if (sizeCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		final StudySample sample = new StudySample();
		sample.setSampleName(sampleNameCell.getStringCellValue());
		sample.setProtocolOfSampleCollection(protocolCell.getStringCellValue());

		final Cell strategyCell = row.getCell(columns.get("samplingStrategy"));
		if (strategyCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		final Cell samplingProgramCell = row.getCell(columns.get("samplingProgramType"));
		if (samplingProgramCell.getCellTypeEnum() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		final Cell samplingMethodCell = row.getCell(columns.get("samplingMethod"));
		if (samplingMethodCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(columns.get("samplingPlan")).getStringCellValue());
		sample.setSamplingWeight(row.getCell(columns.get("samplingWeight")).getStringCellValue());
		sample.setSamplingSize(row.getCell(columns.get("samplingSize")).getStringCellValue());

		final Cell unitCell = row.getCell(columns.get("lotSizeUnit"));
		if (unitCell.getCellTypeEnum() == CellType.STRING) {
			sample.setLotSizeUnit(unitCell.getStringCellValue());
		}

		final Cell pointCell = row.getCell(columns.get("samplingPoint"));
		if (pointCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingPoint((pointCell).getStringCellValue());
		}

		return sample;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>collectionTool
	 *                <li>numberOfNonConsecutiveOneDay
	 *                <li>softwareTool
	 *                <li>numberOfFoodItems
	 *                <li>recordTypes
	 *                <li>foodDescriptors
	 *                </ul>
	 */
	public static DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row, Map<String, Integer> columns) {

		// Check first mandatory properties
		if (row.getCell(columns.get("collectionTool")).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(columns.get("numberOfNonConsecutiveOneDay")).getCellTypeEnum() != CellType.NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		final DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(columns.get("collectionTool")).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(
				Double.toString(row.getCell(columns.get("numberOfNonConsecutiveOneDay")).getNumericCellValue()));

		final Cell softwareCell = row.getCell(columns.get("softwareTool"));
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		final Cell foodItemsCell = row.getCell(columns.get("numberOfFoodItems"));
		if (foodItemsCell.getCellTypeEnum() == CellType.STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		final Cell recordTypesCell = row.getCell(columns.get("recordTypes"));
		if (recordTypesCell.getCellTypeEnum() == CellType.STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		final Cell foodDescriptorsCell = row.getCell(columns.get("foodDescriptors"));
		if (foodDescriptorsCell.getCellTypeEnum() == CellType.STRING) {
			method.addFoodDescriptorsItem(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>accreditation
	 *                <li>name
	 *                <li>country
	 *                </ul>
	 */
	public static Laboratory retrieveLaboratory(Row row, HashMap<String, Integer> columns) {

		// Check first mandatory properties
		if (row.getCell(columns.get("accreditation")).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(columns.get("accreditation")).getStringCellValue().split(","))
				.forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setName(nameCell.getStringCellValue());
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setCountry(countryCell.getStringCellValue());
		}

		return laboratory;
	}
	
	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>name
	 *                <li>description
	 *                <li>moisturePercentage
	 *                <li>fatPercentage
	 *                <li>detectionLimit
	 *                <li>quantificationLimit
	 *                <li>leftCensoredData
	 *                <li>contaminationRange
	 *                <li>uncertaintyValue
	 *                </ul>
	 */
	public static Assay retrieveAssay(Row row, Map<String, Integer> columns) {

		// Check first mandatory properties
		final Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell.getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		final Assay assay = new Assay();
		assay.setName(nameCell.getStringCellValue());

		final Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell moistureCell = row.getCell(columns.get("moisturePercentage"));
		if (moistureCell.getCellTypeEnum() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		final Cell fatCell = row.getCell(columns.get("fatPercentage"));
		if (fatCell.getCellTypeEnum() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		final Cell detectionCell = row.getCell(columns.get("detectionLimit"));
		if (detectionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		final Cell quantificationCell = row.getCell(columns.get("quantificationLimit"));
		if (quantificationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		final Cell dataCell = row.getCell(columns.get("leftCensoredData"));
		if (dataCell.getCellTypeEnum() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		final Cell contaminationCell = row.getCell(columns.get("contaminationRange"));
		if (contaminationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		final Cell uncertaintyCell = row.getCell(columns.get("uncertaintyValue"));
		if (uncertaintyCell.getCellTypeEnum() == CellType.STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}

	/**
	 * @param row     Spreadsheet row
	 * @param columns Column numbers for the columns with keys:
	 *                <ul>
	 *                <li>id
	 *                <li>classification
	 *                <li>name
	 *                <li>description
	 *                <li>unit
	 *                <li>unitCategory
	 *                <li>dataType
	 *                <li>source
	 *                <li>subject
	 *                <li>distribution
	 *                <li>reference
	 *                <li>variability
	 *                <li>max
	 *                <li>min
	 *                <li>error
	 *                </ul>
	 */
	public static Parameter retrieveParameter(Row row, Map<String, Integer> columns) {

		// Check first mandatory properties
		final Cell idCell = row.getCell(columns.get("id"));
		if (idCell == null || idCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		final Cell classificationCell = row.getCell(columns.get("classification"));
		if (classificationCell == null || classificationCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		final Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell == null || nameCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		final Cell unitCell = row.getCell(columns.get("unit"));
		if (unitCell == null || unitCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		final Cell dataTypeCell = row.getCell(columns.get("dataType"));
		if (dataTypeCell == null || dataTypeCell.getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing data type");
		}

		final Parameter param = new Parameter();
		param.setId(idCell.getStringCellValue());
		param.setName(nameCell.getStringCellValue());
		param.setUnit(unitCell.getStringCellValue());

		final ParameterClassification pc = ParameterClassification.get(classificationCell.getStringCellValue());
		if (pc != null) {
			param.setClassification(SwaggerUtil.CLASSIF.get(pc));
		}

		final Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell != null && descriptionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell unitCategoryCell = row.getCell(columns.get("unitCategory"));
		if (unitCategoryCell != null && unitCategoryCell.getCellTypeEnum() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		final ParameterType parameterType = ParameterType.get(dataTypeCell.getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		final Cell sourceCell = row.getCell(columns.get("source"));
		if (sourceCell != null && sourceCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		final Cell subjectCell = row.getCell(columns.get("subject"));
		if (subjectCell != null && subjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		final Cell distributionCell = row.getCell(columns.get("distribution"));
		if (distributionCell != null && distributionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		final Cell valueCell = row.getCell(columns.get("value"));
		if (valueCell != null && valueCell.getCellTypeEnum() != CellType.BLANK) {

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

		final Cell variabilitySubjectCell = row.getCell(columns.get("variability"));
		if (variabilitySubjectCell != null && variabilitySubjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		final Cell maxCell = row.getCell(columns.get("max"));
		if (maxCell != null && maxCell.getCellTypeEnum() != CellType.BLANK) {
			if (maxCell.getCellTypeEnum() != CellType.STRING) {
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			} else {
				param.setMaxValue(maxCell.getStringCellValue());
			}

		}

		final Cell minCell = row.getCell(columns.get("min"));
		if (minCell != null && minCell.getCellTypeEnum() != CellType.BLANK) {
			if (minCell.getCellTypeEnum() != CellType.STRING) {
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			} else {
				param.setMinValue(minCell.getStringCellValue());
			}
		}

		final Cell errorCell = row.getCell(columns.get("error"));
		if (errorCell != null && errorCell.getCellTypeEnum() != CellType.BLANK) {
			if (errorCell.getCellTypeEnum() != CellType.STRING) {
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			} else {
				param.setError(errorCell.getStringCellValue());
			}
		}

		return param;
	}

	@SuppressWarnings("deprecation")
	public static LocalDate retrieveDate(Cell dateCell) {
		Date date = dateCell.getDateCellValue();
		return LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
	}
}
