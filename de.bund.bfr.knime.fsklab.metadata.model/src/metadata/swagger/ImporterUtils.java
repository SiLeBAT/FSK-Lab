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
		if (zipCodeCell.getCellType() == CellType.NUMERIC) {
			double zipCodeAsDouble = zipCodeCell.getNumericCellValue();
			contact.setZipCode(Integer.toString((int) zipCodeAsDouble));
		} else if (zipCodeCell.getCellType() == CellType.STRING) {
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

	public static Product retrieveProduct(Row row, Map<String, Integer> columns) {

		// Check first mandatory properties
		if (row.getCell(columns.get("name")).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(columns.get("unit")).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		final Product product = new Product();
		product.setName(row.getCell(columns.get("name")).getStringCellValue());
		product.setUnit(row.getCell(columns.get("unit")).getStringCellValue());

		final Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell.getCellType() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell methodCell = row.getCell(columns.get("productionMethod"));
		if (methodCell.getCellType() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		final Cell packagingCell = row.getCell(columns.get("packaging"));
		if (packagingCell.getCellType() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		final Cell treatmentCell = row.getCell(columns.get("treatment"));
		if (treatmentCell.getCellType() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		final Cell originCountryCell = row.getCell(columns.get("originCountry"));
		if (originCountryCell.getCellType() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		final Cell originAreaCell = row.getCell(columns.get("originArea"));
		if (originAreaCell.getCellType() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		final Cell fisheriesAreaCell = row.getCell(columns.get("fisheriesArea"));
		if (fisheriesAreaCell.getCellType() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		final Cell productionDateCell = row.getCell(columns.get("productionDate"));
		if (productionDateCell.getCellType() == CellType.NUMERIC) {
			product.setProductionDate(retrieveDate(productionDateCell));
		}

		final Cell expiryDateCell = row.getCell(columns.get("expiryDate"));
		if (expiryDateCell.getCellType() == CellType.NUMERIC) {
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
		if (nameCell.getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		final Hazard hazard = new Hazard();
		hazard.setName(nameCell.getStringCellValue());

		final Cell typeCell = row.getCell(columns.get("type"));
		if (typeCell.getCellType() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		final Cell hazardDescriptionCell = row.getCell(columns.get("description"));
		if (hazardDescriptionCell.getCellType() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		final Cell hazardUnitCell = row.getCell(columns.get("unit"));
		if (hazardUnitCell.getCellType() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		final Cell adverseEffect = row.getCell(columns.get("adverseEffect"));
		if (adverseEffect.getCellType() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		final Cell sourceOfContaminationCell = row.getCell(columns.get("sourceOfContamination"));
		if (sourceOfContaminationCell.getCellType() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		final Cell bmdCell = row.getCell(columns.get("benchmarkDose"));
		if (bmdCell.getCellType() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		final Cell maximumResidueLimitCell = row.getCell(columns.get("maximumResidueLimit"));
		if (maximumResidueLimitCell.getCellType() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		final Cell noaelCell = row.getCell(columns.get("noObservedAdverseAffectLevel"));
		if (noaelCell.getCellType() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		final Cell loaelCell = row.getCell(columns.get("lowestObservedAdverseAffectLevel"));
		if (loaelCell.getCellType() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		final Cell aoelCell = row.getCell(columns.get("acceptableOperatorsExposureLevel"));
		if (aoelCell.getCellType() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		final Cell arfdCell = row.getCell(columns.get("acuteReferenceDose"));
		if (arfdCell.getCellType() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		final Cell adiCell = row.getCell(columns.get("acceptableDailyIntake"));
		if (adiCell.getCellType() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		final Cell indSumCell = row.getCell(columns.get("indSum"));
		if (indSumCell.getCellType() == CellType.STRING) {
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
		if (nameCell.getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();
		group.setName(nameCell.getStringCellValue());

		Cell targetPopulationCell = row.getCell(columns.get("targetPopulation"));
		if (targetPopulationCell.getCellType() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(columns.get("span"));
		if (spanCell.getCellType() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell.getCellType() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(columns.get("age"));
		if (ageCell.getCellType() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(columns.get("gender"));
		if (genderCell.getCellType() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(columns.get("bmi"));
		if (bmiCell.getCellType() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(columns.get("diet"));
		if (dietCell.getCellType() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(columns.get("consumption"));
		if (consumptionCell.getCellType() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(columns.get("region"));
		if (regionCell.getCellType() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellType() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(columns.get("risk"));
		if (factorsCell.getCellType() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(columns.get("season"));
		if (seasonCell.getCellType() == CellType.STRING) {
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
		if (row.getCell(columns.get("referenceDescription")).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(columns.get("doi")).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		final Reference reference = new Reference();
		reference.setIsReferenceDescription(
				row.getCell(columns.get("referenceDescription")).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(columns.get("doi")).getStringCellValue());

		// TODO: the types in the spreadsheet are not compatible with the RIS types.
		// The annotation template must be updated or the values must be mapped here.

		final Cell dateCell = row.getCell(columns.get("date"));
		if (dateCell.getCellType() == CellType.NUMERIC) {
		  reference.setDate(""+((Double)dateCell.getNumericCellValue()).intValue());
		}

		final Cell pmidCell = row.getCell(columns.get("pmid"));
		if (pmidCell.getCellType() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		final Cell authorListCell = row.getCell(columns.get("author"));
		if (authorListCell.getCellType() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		final Cell titleCell = row.getCell(columns.get("title"));
		if (titleCell.getCellType() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		final Cell abstractCell = row.getCell(columns.get("abstract"));
		if (abstractCell.getCellType() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		final Cell statusCell = row.getCell(columns.get("status"));
		if (statusCell.getCellType() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		final Cell websiteCell = row.getCell(columns.get("website"));
		if (websiteCell.getCellType() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		final Cell commentCell = row.getCell(columns.get("comment"));
		if (commentCell.getCellType() == CellType.STRING) {
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
		if (sampleNameCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		
		final Cell protocolCell = row.getCell(columns.get("protocolOfSampleCollection"));
		if (protocolCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		
		final Cell methodCell = row.getCell(columns.get("samplingMethod"));
		if (methodCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		
		final Cell weightCell = row.getCell(columns.get("samplingWeight"));
		if (weightCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		
		final Cell sizeCell = row.getCell(columns.get("samplingSize"));
		if (sizeCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		final StudySample sample = new StudySample();
		sample.setSampleName(sampleNameCell.getStringCellValue());
		sample.setProtocolOfSampleCollection(protocolCell.getStringCellValue());

		final Cell strategyCell = row.getCell(columns.get("samplingStrategy"));
		if (strategyCell.getCellType() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		final Cell samplingProgramCell = row.getCell(columns.get("samplingProgramType"));
		if (samplingProgramCell.getCellType() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		final Cell samplingMethodCell = row.getCell(columns.get("samplingMethod"));
		if (samplingMethodCell.getCellType() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(columns.get("samplingPlan")).getStringCellValue());
		sample.setSamplingWeight(row.getCell(columns.get("samplingWeight")).getStringCellValue());
		sample.setSamplingSize(row.getCell(columns.get("samplingSize")).getStringCellValue());

		final Cell unitCell = row.getCell(columns.get("lotSizeUnit"));
		if (unitCell.getCellType() == CellType.STRING) {
			sample.setLotSizeUnit(unitCell.getStringCellValue());
		}

		final Cell pointCell = row.getCell(columns.get("samplingPoint"));
		if (pointCell.getCellType() == CellType.STRING) {
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
		if (row.getCell(columns.get("collectionTool")).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(columns.get("numberOfNonConsecutiveOneDay")).getCellType() != CellType.NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		final DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(columns.get("collectionTool")).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(
				Double.toString(row.getCell(columns.get("numberOfNonConsecutiveOneDay")).getNumericCellValue()));

		final Cell softwareCell = row.getCell(columns.get("softwareTool"));
		if (softwareCell.getCellType() == CellType.STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		final Cell foodItemsCell = row.getCell(columns.get("numberOfFoodItems"));
		if (foodItemsCell.getCellType() == CellType.STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		final Cell recordTypesCell = row.getCell(columns.get("recordTypes"));
		if (recordTypesCell.getCellType() == CellType.STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		final Cell foodDescriptorsCell = row.getCell(columns.get("foodDescriptors"));
		if (foodDescriptorsCell.getCellType() == CellType.STRING) {
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
		if (row.getCell(columns.get("accreditation")).getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(columns.get("accreditation")).getStringCellValue().split(","))
				.forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell.getCellType() == CellType.STRING) {
			laboratory.setName(nameCell.getStringCellValue());
		}

		Cell countryCell = row.getCell(columns.get("country"));
		if (countryCell.getCellType() == CellType.STRING) {
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
		if (nameCell.getCellType() != CellType.STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		final Assay assay = new Assay();
		assay.setName(nameCell.getStringCellValue());

		final Cell descriptionCell = row.getCell(columns.get("description"));
		if (descriptionCell.getCellType() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell moistureCell = row.getCell(columns.get("moisturePercentage"));
		if (moistureCell.getCellType() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		final Cell fatCell = row.getCell(columns.get("fatPercentage"));
		if (fatCell.getCellType() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		final Cell detectionCell = row.getCell(columns.get("detectionLimit"));
		if (detectionCell.getCellType() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		final Cell quantificationCell = row.getCell(columns.get("quantificationLimit"));
		if (quantificationCell.getCellType() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		final Cell dataCell = row.getCell(columns.get("leftCensoredData"));
		if (dataCell.getCellType() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		final Cell contaminationCell = row.getCell(columns.get("contaminationRange"));
		if (contaminationCell.getCellType() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		final Cell uncertaintyCell = row.getCell(columns.get("uncertaintyValue"));
		if (uncertaintyCell.getCellType() == CellType.STRING) {
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
		if (idCell == null || idCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		final Cell classificationCell = row.getCell(columns.get("classification"));
		if (classificationCell == null || classificationCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		final Cell nameCell = row.getCell(columns.get("name"));
		if (nameCell == null || nameCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		final Cell unitCell = row.getCell(columns.get("unit"));
		if (unitCell == null || unitCell.getCellType() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		final Cell dataTypeCell = row.getCell(columns.get("dataType"));
		if (dataTypeCell == null || dataTypeCell.getCellType() == CellType.BLANK) {
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
		if (descriptionCell != null && descriptionCell.getCellType() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell unitCategoryCell = row.getCell(columns.get("unitCategory"));
		if (unitCategoryCell != null && unitCategoryCell.getCellType() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		final ParameterType parameterType = ParameterType.get(dataTypeCell.getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		final Cell sourceCell = row.getCell(columns.get("source"));
		if (sourceCell != null && sourceCell.getCellType() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		final Cell subjectCell = row.getCell(columns.get("subject"));
		if (subjectCell != null && subjectCell.getCellType() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		final Cell distributionCell = row.getCell(columns.get("distribution"));
		if (distributionCell != null && distributionCell.getCellType() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		final Cell valueCell = row.getCell(columns.get("value"));
		if (valueCell != null && valueCell.getCellType() != CellType.BLANK) {

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

		final Cell variabilitySubjectCell = row.getCell(columns.get("variability"));
		if (variabilitySubjectCell != null && variabilitySubjectCell.getCellType() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		final Cell maxCell = row.getCell(columns.get("max"));
		if (maxCell != null && maxCell.getCellType() != CellType.BLANK) {
			if (maxCell.getCellType() != CellType.STRING) {
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			} else {
				param.setMaxValue(maxCell.getStringCellValue());
			}

		}

		final Cell minCell = row.getCell(columns.get("min"));
		if (minCell != null && minCell.getCellType() != CellType.BLANK) {
			if (minCell.getCellType() != CellType.STRING) {
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			} else {
				param.setMinValue(minCell.getStringCellValue());
			}
		}

		final Cell errorCell = row.getCell(columns.get("error"));
		if (errorCell != null && errorCell.getCellType() != CellType.BLANK) {
			if (errorCell.getCellType() != CellType.STRING) {
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
