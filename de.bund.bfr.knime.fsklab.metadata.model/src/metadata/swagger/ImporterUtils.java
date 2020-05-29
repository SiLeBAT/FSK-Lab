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
import metadata.PublicationType;
import metadata.SwaggerUtil;

/** Utility methods for importers. */
public class ImporterUtils {

	private ImporterUtils() {
	}

	public static Contact retrieveCreator(Row row) {
		@SuppressWarnings("serial")
		final HashMap<String, Integer> columns = new HashMap<String, Integer>() {
			{
				put("mail", SheetImporter.R);
				put("title", SheetImporter.K);
				put("familyName", SheetImporter.O);
				put("givenName", SheetImporter.M);
				put("telephone", SheetImporter.Q);
				put("streetAddress", SheetImporter.W);
				put("country", SheetImporter.S);
				put("city", SheetImporter.T);
				put("zipCode", SheetImporter.U);
				put("region", SheetImporter.Y);
				put("organization", SheetImporter.P);
			}
		};
		return retrieveContact(row, columns);
	}

	public static Contact retrieveAuthor(Row row) {

		@SuppressWarnings("serial")
		final HashMap<String, Integer> columns = new HashMap<String, Integer>() {
			{
				put("mail", SheetImporter.AH);
				put("title", SheetImporter.AA);
				put("familyName", SheetImporter.AE);
				put("givenName", SheetImporter.AC);
				put("telephone", SheetImporter.AG);
				put("streetAddress", SheetImporter.AM);
				put("country", SheetImporter.AI);
				put("city", SheetImporter.AJ);
				put("zipCode", SheetImporter.AK);
				put("region", SheetImporter.AO);
				put("organization", SheetImporter.AF);
			}
		};
		return retrieveContact(row, columns);
	}

	public static Contact retrieveContact(Row row, Map<String, Integer> columns) {

		String x = row.getCell(SheetImporter.L).getStringCellValue();
		x = row.getCell(SheetImporter.AA).getStringCellValue();
		x = row.getCell(SheetImporter.AE).getStringCellValue();
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

	public static Product retrieveProduct(Row row) {

		// Check first mandatory properties
		if (row.getCell(SheetImporter.K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product name");
		}
		if (row.getCell(SheetImporter.M).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing product unit");
		}

		final Product product = new Product();
		product.setName(row.getCell(SheetImporter.K).getStringCellValue());
		product.setUnit(row.getCell(SheetImporter.M).getStringCellValue());

		final Cell descriptionCell = row.getCell(SheetImporter.L);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			product.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell methodCell = row.getCell(SheetImporter.N);
		if (methodCell.getCellTypeEnum() == CellType.STRING) {
			product.addMethodItem(methodCell.getStringCellValue());
		}

		final Cell packagingCell = row.getCell(SheetImporter.O);
		if (packagingCell.getCellTypeEnum() == CellType.STRING) {
			product.addPackagingItem(packagingCell.getStringCellValue());
		}

		final Cell treatmentCell = row.getCell(SheetImporter.P);
		if (treatmentCell.getCellTypeEnum() == CellType.STRING) {
			product.addTreatmentItem(treatmentCell.getStringCellValue());
		}

		final Cell originCountryCell = row.getCell(SheetImporter.Q);
		if (originCountryCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginCountry(originCountryCell.getStringCellValue());
		}

		final Cell originAreaCell = row.getCell(SheetImporter.R);
		if (originAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setOriginArea(originAreaCell.getStringCellValue());
		}

		final Cell fisheriesAreaCell = row.getCell(SheetImporter.S);
		if (fisheriesAreaCell.getCellTypeEnum() == CellType.STRING) {
			product.setFisheriesArea(fisheriesAreaCell.getStringCellValue());
		}

		final Cell productionDateCell = row.getCell(SheetImporter.T);
		if (productionDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date date = productionDateCell.getDateCellValue();
			product.setProductionDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		final Cell expiryDateCell = row.getCell(SheetImporter.U);
		if (expiryDateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date date = expiryDateCell.getDateCellValue();
			product.setExpiryDate(LocalDate.of(date.getYear() + 1900, date.getMonth(), date.getDate()));
		}

		return product;
	}

	public static Hazard retrieveHazard(Row row) {
		// Check mandatory properties
		if (row.getCell(SheetImporter.W).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Hazard name is missing");
		}

		final Hazard hazard = new Hazard();
		hazard.setName(row.getCell(SheetImporter.W).getStringCellValue());

		final Cell typeCell = row.getCell(SheetImporter.V);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setType(typeCell.getStringCellValue());
		}

		final Cell hazardDescriptionCell = row.getCell(SheetImporter.X);
		if (hazardDescriptionCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setDescription(hazardDescriptionCell.getStringCellValue());
		}

		final Cell hazardUnitCell = row.getCell(SheetImporter.Y);
		if (hazardUnitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setUnit(hazardUnitCell.getStringCellValue());
		}

		final Cell adverseEffect = row.getCell(SheetImporter.Z);
		if (adverseEffect.getCellTypeEnum() == CellType.STRING) {
			hazard.setAdverseEffect(adverseEffect.getStringCellValue());
		}

		final Cell sourceOfContaminationCell = row.getCell(SheetImporter.AA);
		if (sourceOfContaminationCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setSourceOfContamination(sourceOfContaminationCell.getStringCellValue());
		}

		final Cell bmdCell = row.getCell(SheetImporter.AB);
		if (bmdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setBenchmarkDose(bmdCell.getStringCellValue());
		}

		final Cell maximumResidueLimitCell = row.getCell(SheetImporter.AC);
		if (maximumResidueLimitCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setMaximumResidueLimit(maximumResidueLimitCell.getStringCellValue());
		}

		final Cell noaelCell = row.getCell(SheetImporter.AD);
		if (noaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setNoObservedAdverseAffectLevel(noaelCell.getStringCellValue());
		}

		final Cell loaelCell = row.getCell(SheetImporter.AE);
		if (loaelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setLowestObservedAdverseAffectLevel(loaelCell.getStringCellValue());
		}

		final Cell aoelCell = row.getCell(SheetImporter.AF);
		if (aoelCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableOperatorsExposureLevel(aoelCell.getStringCellValue());
		}

		final Cell arfdCell = row.getCell(SheetImporter.AG);
		if (arfdCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcuteReferenceDose(arfdCell.getStringCellValue());
		}

		final Cell adiCell = row.getCell(SheetImporter.AH);
		if (adiCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setAcceptableDailyIntake(adiCell.getStringCellValue());
		}

		final Cell indSumCell = row.getCell(SheetImporter.AI);
		if (indSumCell.getCellTypeEnum() == CellType.STRING) {
			hazard.setIndSum(indSumCell.getStringCellValue());
		}

		return hazard;
	}

	public static PopulationGroup retrievePopulationGroup(Row row) {

		// Check mandatory properties
		if (row.getCell(SheetImporter.W).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing population name");
		}

		PopulationGroup group = new PopulationGroup();

		Cell nameCell = row.getCell(SheetImporter.W);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			group.setName(nameCell.getStringCellValue());
		}

		Cell targetPopulationCell = row.getCell(SheetImporter.X);
		if (targetPopulationCell.getCellTypeEnum() == CellType.STRING) {
			group.setTargetPopulation(targetPopulationCell.getStringCellValue());
		}

		Cell spanCell = row.getCell(SheetImporter.Y);
		if (spanCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(spanCell.getStringCellValue().split(",")).forEach(group::addPopulationSpanItem);
		}

		Cell descriptionCell = row.getCell(SheetImporter.Z);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(descriptionCell.getStringCellValue().split(",")).forEach(group::addPopulationDescriptionItem);
		}

		Cell ageCell = row.getCell(SheetImporter.AA);
		if (ageCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(ageCell.getStringCellValue().split(",")).forEach(group::addPopulationAgeItem);
		}

		Cell genderCell = row.getCell(SheetImporter.AB);
		if (genderCell.getCellTypeEnum() == CellType.STRING) {
			group.setPopulationGender(genderCell.getStringCellValue());
		}

		Cell bmiCell = row.getCell(SheetImporter.AC);
		if (bmiCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(bmiCell.getStringCellValue().split(",")).forEach(group::addBmiItem);
		}

		Cell dietCell = row.getCell(SheetImporter.AD);
		if (dietCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(dietCell.getStringCellValue().split(",")).forEach(group::addSpecialDietGroupsItem);
		}

		Cell consumptionCell = row.getCell(SheetImporter.AE);
		if (consumptionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(consumptionCell.getStringCellValue().split(",")).forEach(group::addPatternConsumptionItem);
		}

		Cell regionCell = row.getCell(SheetImporter.AF);
		if (regionCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(regionCell.getStringCellValue().split(",")).forEach(group::addRegionItem);
		}

		Cell countryCell = row.getCell(SheetImporter.AG);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(countryCell.getStringCellValue().split(",")).forEach(group::addCountryItem);
		}

		Cell factorsCell = row.getCell(SheetImporter.AH);
		if (factorsCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(factorsCell.getStringCellValue().split(",")).forEach(group::addPopulationRiskFactorItem);
		}

		Cell seasonCell = row.getCell(SheetImporter.AI);
		if (seasonCell.getCellTypeEnum() == CellType.STRING) {
			Arrays.stream(seasonCell.getStringCellValue().split(",")).forEach(group::addSeasonItem);
		}

		return group;
	}

	public static Reference retrieveReference(Row row) {

		// Check mandatory properties and throw exception if missing
		if (row.getCell(SheetImporter.K).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing Is reference description?");
		}
		if (row.getCell(SheetImporter.O).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing DOI");
		}

		final Reference reference = new Reference();
		reference.setIsReferenceDescription(row.getCell(SheetImporter.K).getStringCellValue().equals("Yes"));
		reference.setDoi(row.getCell(SheetImporter.O).getStringCellValue());

		// publication type
		final Cell typeCell = row.getCell(SheetImporter.L);
		if (typeCell.getCellTypeEnum() == CellType.STRING) {
			final PublicationType type = PublicationType.get(typeCell.getStringCellValue());
			if (type != null) {
				reference.setPublicationType(SwaggerUtil.PUBLICATION_TYPE.get(type));
			}
		}

		final Cell dateCell = row.getCell(SheetImporter.M);
		if (dateCell.getCellTypeEnum() == CellType.NUMERIC) {
			final Date date = dateCell.getDateCellValue();
			final LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
			reference.setDate(localDate);
		}

		final Cell pmidCell = row.getCell(SheetImporter.N);
		if (pmidCell.getCellTypeEnum() == CellType.STRING) {
			reference.setPmid(pmidCell.getStringCellValue());
		}

		final Cell authorListCell = row.getCell(SheetImporter.P);
		if (authorListCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAuthorList(authorListCell.getStringCellValue());
		}

		final Cell titleCell = row.getCell(SheetImporter.Q);
		if (titleCell.getCellTypeEnum() == CellType.STRING) {
			reference.setTitle(titleCell.getStringCellValue());
		}

		final Cell abstractCell = row.getCell(SheetImporter.R);
		if (abstractCell.getCellTypeEnum() == CellType.STRING) {
			reference.setAbstract(abstractCell.getStringCellValue());
		}
		// journal
		// volume
		// issue

		final Cell statusCell = row.getCell(SheetImporter.T);
		if (statusCell.getCellTypeEnum() == CellType.STRING) {
			reference.setStatus(statusCell.getStringCellValue());
		}

		final Cell websiteCell = row.getCell(SheetImporter.U);
		if (websiteCell.getCellTypeEnum() == CellType.STRING) {
			reference.setWebsite(websiteCell.getStringCellValue());
		}

		final Cell commentCell = row.getCell(SheetImporter.V);
		if (commentCell.getCellTypeEnum() == CellType.STRING) {
			reference.setComment(commentCell.getStringCellValue());
		}

		return reference;
	}

	public static StudySample retrieveStudySample(Row row) {

		// Check mandatory properties
		if (row.getCell(SheetImporter.L).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sample name");
		}
		if (row.getCell(SheetImporter.M).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing protocol of sample collection");
		}
		if (row.getCell(SheetImporter.Q).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling method");
		}
		if (row.getCell(SheetImporter.R).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling weight");
		}
		if (row.getCell(SheetImporter.S).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing sampling size");
		}

		final StudySample sample = new StudySample();
		sample.setSampleName(row.getCell(SheetImporter.L).getStringCellValue());
		sample.setProtocolOfSampleCollection(row.getCell(SheetImporter.M).getStringCellValue());

		final Cell strategyCell = row.getCell(SheetImporter.N);
		if (strategyCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingStrategy(strategyCell.getStringCellValue());
		}

		final Cell samplingProgramCell = row.getCell(SheetImporter.O);
		if (samplingProgramCell.getCellTypeEnum() == CellType.STRING) {
			sample.setTypeOfSamplingProgram(samplingProgramCell.getStringCellValue());
		}

		final Cell samplingMethodCell = row.getCell(SheetImporter.P);
		if (samplingMethodCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingMethod(samplingMethodCell.getStringCellValue());
		}

		sample.setSamplingPlan(row.getCell(SheetImporter.Q).getStringCellValue());
		sample.setSamplingWeight(row.getCell(SheetImporter.R).getStringCellValue());
		sample.setSamplingSize(row.getCell(SheetImporter.S).getStringCellValue());

		final Cell unitCell = row.getCell(SheetImporter.T);
		if (unitCell.getCellTypeEnum() == CellType.STRING) {
			sample.setLotSizeUnit(row.getCell(SheetImporter.T).getStringCellValue());
		}

		final Cell pointCell = row.getCell(SheetImporter.U);
		if (pointCell.getCellTypeEnum() == CellType.STRING) {
			sample.setSamplingPoint(row.getCell(SheetImporter.U).getStringCellValue());
		}

		return sample;
	}
	
	public static DietaryAssessmentMethod retrieveDietaryAssessmentMethod(Row row) {

		// Check first mandatory properties
		if (row.getCell(SheetImporter.L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing methodological tool to collect data");
		}
		if (row.getCell(SheetImporter.M).getCellTypeEnum() != CellType.NUMERIC) {
			throw new IllegalArgumentException("Missing number of non consecutive one day");
		}

		final DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		method.setCollectionTool(row.getCell(SheetImporter.L).getStringCellValue());
		method.setNumberOfNonConsecutiveOneDay(Double.toString(row.getCell(SheetImporter.M).getNumericCellValue()));

		final Cell softwareCell = row.getCell(SheetImporter.N);
		if (softwareCell.getCellTypeEnum() == CellType.STRING) {
			method.setSoftwareTool(softwareCell.getStringCellValue());
		}

		final Cell foodItemsCell = row.getCell(SheetImporter.O);
		if (foodItemsCell.getCellTypeEnum() == CellType.STRING) {
			method.addNumberOfFoodItemsItem(foodItemsCell.getStringCellValue());
		}

		final Cell recordTypesCell = row.getCell(SheetImporter.P);
		if (recordTypesCell.getCellTypeEnum() == CellType.STRING) {
			method.addRecordTypesItem(recordTypesCell.getStringCellValue());
		}

		final Cell foodDescriptorsCell = row.getCell(SheetImporter.Q);
		if (foodDescriptorsCell.getCellTypeEnum() == CellType.STRING) {
			method.addFoodDescriptorsItem(foodDescriptorsCell.getStringCellValue());
		}

		return method;
	}

	public static Laboratory retrieveLaboratory(Row row) {

		// Check first mandatory properties
		if (row.getCell(SheetImporter.L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing laboratory accreditation");
		}

		Laboratory laboratory = new Laboratory();
		Arrays.stream(row.getCell(SheetImporter.L).getStringCellValue().split(",")).forEach(laboratory::addAccreditationItem);

		Cell nameCell = row.getCell(SheetImporter.M);
		if (nameCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setName(row.getCell(SheetImporter.M).getStringCellValue());
		}

		Cell countryCell = row.getCell(SheetImporter.N);
		if (countryCell.getCellTypeEnum() == CellType.STRING) {
			laboratory.setCountry(row.getCell(SheetImporter.N).getStringCellValue());
		}

		return laboratory;
	}

	public static Assay retrieveAssay(Row row) {
		// Check first mandatory properties
		if (row.getCell(SheetImporter.L).getCellTypeEnum() != CellType.STRING) {
			throw new IllegalArgumentException("Missing assay name");
		}

		final Assay assay = new Assay();
		assay.setName(row.getCell(SheetImporter.L).getStringCellValue());

		final Cell descriptionCell = row.getCell(SheetImporter.M);
		if (descriptionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDescription(descriptionCell.getStringCellValue());
		}

		final Cell moistureCell = row.getCell(SheetImporter.N);
		if (moistureCell.getCellTypeEnum() == CellType.STRING) {
			assay.setMoisturePercentage(moistureCell.getStringCellValue());
		}

		final Cell fatCell = row.getCell(SheetImporter.O);
		if (fatCell.getCellTypeEnum() == CellType.STRING) {
			assay.setFatPercentage(fatCell.getStringCellValue());
		}

		final Cell detectionCell = row.getCell(SheetImporter.P);
		if (detectionCell.getCellTypeEnum() == CellType.STRING) {
			assay.setDetectionLimit(detectionCell.getStringCellValue());
		}

		final Cell quantificationCell = row.getCell(SheetImporter.Q);
		if (quantificationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setQuantificationLimit(quantificationCell.getStringCellValue());
		}

		final Cell dataCell = row.getCell(SheetImporter.R);
		if (dataCell.getCellTypeEnum() == CellType.STRING) {
			assay.setLeftCensoredData(dataCell.getStringCellValue());
		}

		final Cell contaminationCell = row.getCell(SheetImporter.S);
		if (contaminationCell.getCellTypeEnum() == CellType.STRING) {
			assay.setContaminationRange(contaminationCell.getStringCellValue());
		}

		final Cell uncertaintyCell = row.getCell(SheetImporter.T);
		if (uncertaintyCell.getCellTypeEnum() == CellType.STRING) {
			assay.setUncertaintyValue(uncertaintyCell.getStringCellValue());
		}

		return assay;
	}

	public static Parameter retrieveParameter(Row row) {

		// Check first mandatory properties
		if (row.getCell(SheetImporter.L).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter id");
		}

		if (row.getCell(SheetImporter.M).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter classification");
		}

		if (row.getCell(SheetImporter.N).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter name");
		}

		if (row.getCell(SheetImporter.P).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing parameter unit");
		}

		if (row.getCell(SheetImporter.R).getCellTypeEnum() == CellType.BLANK) {
			throw new IllegalArgumentException("Missing data type");
		}

		final Parameter param = new Parameter();
		param.setId(row.getCell(SheetImporter.L).getStringCellValue());

		final ParameterClassification pc = ParameterClassification.get(row.getCell(SheetImporter.M).getStringCellValue());
		if (pc != null) {
			param.setClassification(SwaggerUtil.CLASSIF.get(pc));
		}

		param.setName(row.getCell(SheetImporter.N).getStringCellValue());

		final Cell descriptionCell = row.getCell(SheetImporter.O);
		if (descriptionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDescription(descriptionCell.getStringCellValue());
		}

		param.setUnit(row.getCell(SheetImporter.P).getStringCellValue());

		final Cell unitCategoryCell = row.getCell(SheetImporter.Q);
		if (unitCategoryCell.getCellTypeEnum() != CellType.BLANK) {
			param.setUnitCategory(unitCategoryCell.getStringCellValue());
		}

		final ParameterType parameterType = ParameterType.get(row.getCell(SheetImporter.R).getStringCellValue());
		if (parameterType != null) {
			param.setDataType(SwaggerUtil.TYPES.get(parameterType));
		}

		final Cell sourceCell = row.getCell(SheetImporter.S);
		if (sourceCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSource(sourceCell.getStringCellValue());
		}

		final Cell subjectCell = row.getCell(SheetImporter.T);
		if (subjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setSubject(subjectCell.getStringCellValue());
		}

		final Cell distributionCell = row.getCell(SheetImporter.U);
		if (distributionCell.getCellTypeEnum() != CellType.BLANK) {
			param.setDistribution(distributionCell.getStringCellValue());
		}

		final Cell valueCell = row.getCell(SheetImporter.V);
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

		final Cell variabilitySubjectCell = row.getCell(SheetImporter.X);
		if (variabilitySubjectCell.getCellTypeEnum() != CellType.BLANK) {
			param.setVariabilitySubject(variabilitySubjectCell.getStringCellValue());
		}

		final Cell maxCell = row.getCell(SheetImporter.Y);
		if (maxCell.getCellTypeEnum() != CellType.BLANK) {
			if (maxCell.getCellTypeEnum() != CellType.STRING) {
				param.setMaxValue(String.valueOf(maxCell.getNumericCellValue()));
			} else {
				param.setMaxValue(maxCell.getStringCellValue());
			}

		}

		final Cell minCell = row.getCell(SheetImporter.Z);
		if (minCell.getCellTypeEnum() != CellType.BLANK) {
			if (minCell.getCellTypeEnum() != CellType.STRING) {
				param.setMinValue(String.valueOf(minCell.getNumericCellValue()));
			} else {
				param.setMinValue(minCell.getStringCellValue());
			}
		}

		final Cell errorCell = row.getCell(SheetImporter.AA);
		if (errorCell.getCellTypeEnum() != CellType.BLANK) {
			if (errorCell.getCellTypeEnum() != CellType.STRING) {
				param.setError(String.valueOf(errorCell.getNumericCellValue()));
			} else {
				param.setError(errorCell.getStringCellValue());
			}
		}

		return param;
	}
}
