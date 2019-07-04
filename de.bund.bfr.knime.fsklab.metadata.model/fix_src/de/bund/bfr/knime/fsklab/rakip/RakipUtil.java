package de.bund.bfr.knime.fsklab.rakip;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;

import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;

import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.Gender;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Timezone;
import metadata.MetadataFactory;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PublicationType;
import metadata.SpatialInformation;
import metadata.StringObject;

public class RakipUtil {

	/**
	 * Convert deprecated RAKIP
	 * {@link de.bund.bfr.knime.fsklab.rakip.GeneralInformation} to an EMF
	 * {@link metadata.GeneralInformation}.
	 */
	public static metadata.GeneralInformation convert(GeneralInformation deprecatedGeneralInformation) {

		metadata.GeneralInformation emfGeneralInformation = MetadataFactory.eINSTANCE.createGeneralInformation();

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.name)) {
			emfGeneralInformation.setName(deprecatedGeneralInformation.name);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.source)) {
			emfGeneralInformation.setSource(deprecatedGeneralInformation.source);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.identifier)) {
			emfGeneralInformation.setIdentifier(deprecatedGeneralInformation.identifier);
		}

		if (deprecatedGeneralInformation.creationDate != null) {
			emfGeneralInformation.setCreationDate(deprecatedGeneralInformation.creationDate);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.rights)) {
			emfGeneralInformation.setRights(deprecatedGeneralInformation.rights);
		}

		emfGeneralInformation.setAvailable(deprecatedGeneralInformation.isAvailable);

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.format)) {
			emfGeneralInformation.setFormat(deprecatedGeneralInformation.format);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.language)) {
			emfGeneralInformation.setLanguage(deprecatedGeneralInformation.language);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.software)) {
			emfGeneralInformation.setSoftware(deprecatedGeneralInformation.software);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.languageWrittenIn)) {
			emfGeneralInformation.setLanguageWrittenIn(deprecatedGeneralInformation.languageWrittenIn);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.status)) {
			emfGeneralInformation.setStatus(deprecatedGeneralInformation.status);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.objective)) {
			emfGeneralInformation.setFormat(deprecatedGeneralInformation.format);
		}

		if (StringUtils.isNotEmpty(deprecatedGeneralInformation.description)) {
			emfGeneralInformation.setDescription(deprecatedGeneralInformation.description);
		}

		// TODO: creators

		if (deprecatedGeneralInformation.modelCategory != null) {
			metadata.ModelCategory modelCategory = convert(deprecatedGeneralInformation.modelCategory);
			emfGeneralInformation.getModelCategory().add(modelCategory);
		}

		// TODO: modification date

		return emfGeneralInformation;
	}

	/**
	 * Convert VCard to an EMF {@link metadata.Contact}.
	 */
	public static metadata.Contact convert(VCard vcard) {

		metadata.Contact contact = MetadataFactory.eINSTANCE.createContact();

		if (!vcard.getStructuredNames().isEmpty()) {

			StructuredName structuredName = vcard.getStructuredName();

			// title
			if (!structuredName.getPrefixes().isEmpty()) {
				contact.setTitle(structuredName.getPrefixes().get(0));
			}

			// family name
			String family = structuredName.getFamily();
			if (StringUtils.isNotEmpty(family)) {
				contact.setFamilyName(family);
			}

			// given name
			String givenName = structuredName.getGiven();
			if (StringUtils.isNotEmpty(givenName)) {
				contact.setGivenName(givenName);
			}
		}

		// email
		List<Email> emails = vcard.getEmails();
		if (emails != null && !emails.isEmpty()) {
			contact.setEmail(emails.get(0).getValue());
		}

		// telephone
		List<Telephone> telephones = vcard.getTelephoneNumbers();
		if (telephones != null && !telephones.isEmpty()) {
			contact.setTelephone(telephones.get(0).getText());
		}

		if (!vcard.getAddresses().isEmpty()) {

			Address address = vcard.getAddresses().get(0);

			String streetAddress = address.getStreetAddress();
			if (streetAddress != null) {
				contact.setStreetAddress(streetAddress);
			}

			String country = address.getCountry();
			if (country != null) {
				contact.setCountry(country);
			}

			String city = address.getLocality();
			if (city != null) {
				contact.setCity(city);
			}

			String zipCode = address.getPostalCode();
			if (zipCode != null) {
				contact.setZipCode(zipCode);
			}

			String region = address.getRegion();
			if (region != null) {
				contact.setRegion(region);
			}
		}

		// timezone
		Timezone timezone = vcard.getTimezone();
		if (timezone != null) {
			contact.setTimeZone(timezone.getText());
		}

		// gender
		Gender gender = vcard.getGender();
		if (gender != null) {
			contact.setGender(gender.getGender());
		}

		// note
		if (!vcard.getNotes().isEmpty()) {
			String note = vcard.getNotes().get(0).getValue();
			contact.setNote(note);
		}

		// organization
		Organization organization = vcard.getOrganization();
		if (organization != null) {
			contact.setOrganization(organization.getValues().get(0));
		}

		return contact;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.ModelCategory}
	 * to an EMF {@link metadata.ModelCategory}.
	 */
	public static metadata.ModelCategory convert(ModelCategory deprecatedModelCategory) {

		metadata.ModelCategory modelCategory = MetadataFactory.eINSTANCE.createModelCategory();

		if (StringUtils.isNotEmpty(deprecatedModelCategory.modelClass)) {
			modelCategory.setModelClass(deprecatedModelCategory.modelClass);
		}

		if (deprecatedModelCategory.modelSubClass != null) {
			modelCategory.getModelSubClass().addAll(toStringObjectList(deprecatedModelCategory.modelSubClass));
		}

		if (StringUtils.isNotEmpty(deprecatedModelCategory.modelClassComment)) {
			modelCategory.setModelClassComment(deprecatedModelCategory.modelClassComment);
		}

		if (deprecatedModelCategory.basicProcess != null && deprecatedModelCategory.basicProcess.size() > 0) {
			String basicProcess = deprecatedModelCategory.basicProcess.get(0);
			modelCategory.setBasicProcess(basicProcess);
		}

		return modelCategory;
	}

	/**
	 * Convert RIS reference, {@link com.gmail.gcolaianni5.jris.bean.Record} to an
	 * EMF {@link metadata.Reference}.
	 */
	public static metadata.Reference convert(com.gmail.gcolaianni5.jris.bean.Record record) {

		metadata.Reference reference = MetadataFactory.eINSTANCE.createReference();

		// Is reference description is not included in RIS
		reference.setIsReferenceDescription(false);

		if (record.getType() != null) {
			PublicationType publicationType = PublicationType.valueOf(record.getType().name());
			reference.setPublicationType(publicationType);
		}

		final String publicationDate = record.getPrimaryDate();
		if (StringUtils.isNotEmpty(publicationDate)) {
			try {
				String dateFormatStr = "yyyy-MM-dd";
				SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
				Date date = dateFormat.parse(publicationDate);
				reference.setPublicationDate(date);
			} catch (ParseException e) {
			}
		}

		// PMID is not included in RIS

		final String doi = record.getDoi();
		if (StringUtils.isNotEmpty(doi)) {
			reference.setDoi(doi);
		}

		List<String> firstAuthors = record.getFirstAuthors();
		if (firstAuthors != null && !firstAuthors.isEmpty()) {
			String contatenatedAuthors = String.join(";", firstAuthors);
			reference.setAuthorList(contatenatedAuthors);
		}

		final String title = record.getTitle();
		if (StringUtils.isNotEmpty(title)) {
			reference.setPublicationTitle(title);
		}

		final String abstr = record.getAbstr();
		if (StringUtils.isNotEmpty(abstr)) {
			reference.setPublicationAbstract(abstr);
		}

		final String journal = record.getSecondaryTitle();
		if (StringUtils.isNotEmpty(journal)) {
			reference.setPublicationJournal(journal);
		}

		final String volumeNumber = record.getVolumeNumber();
		if (StringUtils.isNotEmpty(volumeNumber)) {
			try {
				int volume = Integer.parseInt(volumeNumber);
				reference.setPublicationVolume(volume);
			} catch (Exception exception) {
			}
		}

		final Integer issueNumber = record.getIssueNumber();
		if (issueNumber != null) {
			reference.setPublicationIssue(issueNumber);
		}

		final String url = record.getUrl();
		if (StringUtils.isNotEmpty(url)) {
			reference.setPublicationWebsite(url);
		}

		// comment is not included in RIS

		return reference;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Scope} to an
	 * EMF {@link metadata.Scope}.
	 */
	public static metadata.Scope convert(Scope deprecatedScope) {

		metadata.Scope scope = MetadataFactory.eINSTANCE.createScope();

		if (StringUtils.isNotEmpty(deprecatedScope.generalComment)) {
			scope.setGeneralComment(deprecatedScope.generalComment);
		}

		if (StringUtils.isNotEmpty(deprecatedScope.temporalInformation)) {
			scope.setTemporalInformation(deprecatedScope.temporalInformation);
		}

		if (deprecatedScope.product != null) {
			metadata.Product product = convert(deprecatedScope.product);
			scope.getProduct().add(product);
		}

		if (deprecatedScope.hazard != null) {
			metadata.Hazard hazard = convert(deprecatedScope.hazard);
			scope.getHazard().add(hazard);
		}

		if (deprecatedScope.populationGroup != null) {
			metadata.PopulationGroup populationGroup = convert(deprecatedScope.populationGroup);
			scope.getPopulationGroup().add(populationGroup);
		}

		SpatialInformation spatialInformation = MetadataFactory.eINSTANCE.createSpatialInformation();

		if (deprecatedScope.region != null && !deprecatedScope.region.isEmpty()) {
			spatialInformation.getRegion().addAll(toStringObjectList(deprecatedScope.region));
		}

		if (deprecatedScope.country != null && !deprecatedScope.country.isEmpty()) {
			spatialInformation.getCountry().addAll(toStringObjectList(deprecatedScope.country));
		}

		return scope;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Product} to an
	 * EMF {@link metadata.Product}.
	 */
	public static metadata.Product convert(Product deprecatedProduct) {

		metadata.Product product = MetadataFactory.eINSTANCE.createProduct();

		if (StringUtils.isNotEmpty(deprecatedProduct.environmentName)) {
			product.setProductName(deprecatedProduct.environmentName);
		}

		if (StringUtils.isNotEmpty(deprecatedProduct.environmentDescription)) {
			product.setProductDescription(deprecatedProduct.environmentDescription);
		}

		if (StringUtils.isNotEmpty(deprecatedProduct.environmentUnit)) {
			product.setProductUnit(deprecatedProduct.environmentUnit);
		}

		if (StringUtils.isNotEmpty(deprecatedProduct.originCountry)) {
			product.setOriginCountry(deprecatedProduct.originCountry);
		}

		if (StringUtils.isNotEmpty(deprecatedProduct.fisheriesArea)) {
			product.setFisheriesArea(deprecatedProduct.fisheriesArea);
		}

		if (deprecatedProduct.productionDate != null) {
			product.setProductionDate(deprecatedProduct.productionDate);
		}

		if (deprecatedProduct.expirationDate != null) {
			product.setExpiryDate(deprecatedProduct.expirationDate);
		}

		if (deprecatedProduct.productionMethod != null && !deprecatedProduct.productionMethod.isEmpty()) {
			String productionMethod = deprecatedProduct.productionMethod.get(0);
			product.setProductionMethod(productionMethod);
		}

		if (deprecatedProduct.packaging != null && !deprecatedProduct.packaging.isEmpty()) {
			String packaging = deprecatedProduct.packaging.get(0);
			product.setPackaging(packaging);
		}

		if (deprecatedProduct.productTreatment != null && !deprecatedProduct.productTreatment.isEmpty()) {
			String productTreatment = deprecatedProduct.productTreatment.get(0);
			product.setProductTreatment(productTreatment);
		}

		if (StringUtils.isNotEmpty(deprecatedProduct.originArea)) {
			product.setOriginArea(deprecatedProduct.originArea);
		}

		return product;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Hazard} to an
	 * EMF {@link metadata.Hazard}.
	 */
	public static metadata.Hazard convert(Hazard deprecatedHazard) {

		metadata.Hazard hazard = MetadataFactory.eINSTANCE.createHazard();

		if (StringUtils.isNotEmpty(deprecatedHazard.hazardType)) {
			hazard.setHazardType(deprecatedHazard.hazardType);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.hazardName)) {
			hazard.setHazardName(deprecatedHazard.hazardName);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.hazardDescription)) {
			hazard.setHazardDescription(deprecatedHazard.hazardDescription);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.hazardUnit)) {
			hazard.setHazardUnit(deprecatedHazard.hazardUnit);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.adverseEffect)) {
			hazard.setAdverseEffect(deprecatedHazard.adverseEffect);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.sourceOfContamination)) {
			hazard.setSourceOfContamination(deprecatedHazard.sourceOfContamination);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.bmd)) {
			hazard.setBenchmarkDose(deprecatedHazard.bmd);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.mrl)) {
			hazard.setMaximumResidueLimit(deprecatedHazard.mrl);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.noael)) {
			hazard.setNoObservedAdverseAffectLevel(deprecatedHazard.noael);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.aoel)) {
			hazard.setAcceptableOperatorExposureLevel(deprecatedHazard.aoel);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.ard)) {
			hazard.setAcuteReferenceDose(deprecatedHazard.ard);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.adi)) {
			hazard.setAcceptableDailyIntake(deprecatedHazard.adi);
		}

		if (StringUtils.isNotEmpty(deprecatedHazard.hazardIndSum)) {
			hazard.setHazardIndSum(deprecatedHazard.hazardIndSum);
		}

		return hazard;
	}

	/**
	 * Convert deprecated RAKIP
	 * {@link de.bund.bfr.knime.fsklab.rakip.PopulationGroup} to an EMF
	 * {@link metadata.PopulationGroup}.
	 */
	public static metadata.PopulationGroup convert(PopulationGroup deprecatedPopulationGroup) {

		metadata.PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();

		if (StringUtils.isNotEmpty(deprecatedPopulationGroup.populationName)) {
			populationGroup.setPopulationName(deprecatedPopulationGroup.populationName);
		}

		if (StringUtils.isNotEmpty(deprecatedPopulationGroup.targetPopulation)) {
			populationGroup.setTargetPopulation(deprecatedPopulationGroup.targetPopulation);
		}

		if (deprecatedPopulationGroup.populationSpan != null && !deprecatedPopulationGroup.populationSpan.isEmpty()) {
			populationGroup.getPopulationSpan().addAll(toStringObjectList(deprecatedPopulationGroup.populationSpan));
		}

		if (deprecatedPopulationGroup.populationDescription != null
				&& !deprecatedPopulationGroup.populationDescription.isEmpty()) {
			populationGroup.getPopulationDescription()
					.addAll(toStringObjectList(deprecatedPopulationGroup.populationSpan));
		}

		if (deprecatedPopulationGroup.populationAge != null && !deprecatedPopulationGroup.populationAge.isEmpty()) {
			populationGroup.getPopulationAge().addAll(toStringObjectList(deprecatedPopulationGroup.populationAge));
		}

		if (StringUtils.isNotEmpty(deprecatedPopulationGroup.populationGender)) {
			populationGroup.setPopulationGender(deprecatedPopulationGroup.populationGender);
		}

		if (deprecatedPopulationGroup.bmi != null && !deprecatedPopulationGroup.bmi.isEmpty()) {
			populationGroup.getBmi().addAll(toStringObjectList(deprecatedPopulationGroup.bmi));
		}

		if (deprecatedPopulationGroup.specialDietGroups != null
				&& !deprecatedPopulationGroup.specialDietGroups.isEmpty()) {
			populationGroup.getSpecialDietGroups().addAll(populationGroup.getSpecialDietGroups());
		}

		if (deprecatedPopulationGroup.patternConsumption != null
				&& !deprecatedPopulationGroup.patternConsumption.isEmpty()) {
			populationGroup.getPatternConsumption().addAll(populationGroup.getPatternConsumption());
		}

		if (deprecatedPopulationGroup.region != null && !deprecatedPopulationGroup.region.isEmpty()) {
			populationGroup.getRegion().addAll(toStringObjectList(deprecatedPopulationGroup.region));
		}

		if (deprecatedPopulationGroup.country != null && !deprecatedPopulationGroup.country.isEmpty()) {
			populationGroup.getCountry().addAll(toStringObjectList(deprecatedPopulationGroup.country));
		}

		if (deprecatedPopulationGroup.populationRiskFactor != null
				&& !deprecatedPopulationGroup.populationRiskFactor.isEmpty()) {
			populationGroup.getPopulationRiskFactor()
					.addAll(toStringObjectList(deprecatedPopulationGroup.populationRiskFactor));
		}

		if (deprecatedPopulationGroup.season != null && !deprecatedPopulationGroup.season.isEmpty()) {
			populationGroup.getSeason().addAll(toStringObjectList(deprecatedPopulationGroup.season));
		}

		return populationGroup;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Study} to an
	 * EMF {@link metadata.Study}.
	 */
	public static metadata.Study convert(Study deprecatedStudy) {

		metadata.Study study = MetadataFactory.eINSTANCE.createStudy();

		if (StringUtils.isNotEmpty(deprecatedStudy.id)) {
			study.setStudyIdentifier(deprecatedStudy.id);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.title)) {
			study.setStudyTitle(deprecatedStudy.title);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.description)) {
			study.setStudyDescription(deprecatedStudy.description);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.designType)) {
			study.setStudyDesignType(deprecatedStudy.designType);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.measurementType)) {
			study.setStudyAssayMeasurementType(deprecatedStudy.measurementType);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.technologyType)) {
			study.setStudyAssayTechnologyType(deprecatedStudy.technologyType);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.technologyPlatform)) {
			study.setStudyAssayTechnologyPlatform(deprecatedStudy.technologyPlatform);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.accreditationProcedure)) {
			study.setAccreditationProcedureForTheAssayTechnology(deprecatedStudy.accreditationProcedure);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.protocolName)) {
			study.setStudyProtocolName(deprecatedStudy.protocolName);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.protocolType)) {
			study.setStudyProtocolType(deprecatedStudy.protocolType);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.protocolDescription)) {
			study.setStudyProtocolDescription(deprecatedStudy.protocolDescription);
		}

		if (deprecatedStudy.protocolUri != null) {
			study.setStudyProtocolURI(deprecatedStudy.protocolUri);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.protocolVersion)) {
			study.setStudyProtocolVersion(deprecatedStudy.protocolVersion);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.parametersName)) {
			study.setStudyProtocolParametersName(deprecatedStudy.parametersName);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.componentsName)) {
			study.setStudyProtocolComponentsName(deprecatedStudy.componentsName);
		}

		if (StringUtils.isNotEmpty(deprecatedStudy.componentsType)) {
			study.setStudyProtocolComponentsType(deprecatedStudy.componentsType);
		}

		return study;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.StudySample}
	 * to an EMF {@link metadata.StudySample}.
	 */
	public static metadata.StudySample convert(StudySample deprecatedStudySample) {

		metadata.StudySample studySample = MetadataFactory.eINSTANCE.createStudySample();

		if (StringUtils.isNotEmpty(deprecatedStudySample.sample)) {
			studySample.setSampleName(deprecatedStudySample.sample);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.collectionProtocol)) {
			studySample.setProtocolOfSampleCollection(deprecatedStudySample.collectionProtocol);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingStrategy)) {
			studySample.setSamplingStrategy(deprecatedStudySample.samplingStrategy);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingProgramType)) {
			studySample.setTypeOfSamplingProgram(deprecatedStudySample.samplingProgramType);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingMethod)) {
			studySample.setSamplingMethod(deprecatedStudySample.samplingMethod);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingPlan)) {
			studySample.setSamplingPlan(deprecatedStudySample.samplingPlan);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingWeight)) {
			studySample.setSamplingWeight(deprecatedStudySample.samplingWeight);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingSize)) {
			studySample.setSamplingSize(deprecatedStudySample.samplingSize);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.lotSizeUnit)) {
			studySample.setLotSizeUnit(deprecatedStudySample.lotSizeUnit);
		}

		if (StringUtils.isNotEmpty(deprecatedStudySample.samplingPoint)) {
			studySample.setSamplingPoint(deprecatedStudySample.samplingPoint);
		}

		return studySample;
	}

	/**
	 * Convert deprecated RAKIP
	 * {@link de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod} to an EMF
	 * {@link metadata.DietaryAssessmentMethod}.
	 */
	public static metadata.DietaryAssessmentMethod convert(DietaryAssessmentMethod deprecatedDietaryAssessmentMethod) {

		metadata.DietaryAssessmentMethod dietaryAssessmentMethod = MetadataFactory.eINSTANCE
				.createDietaryAssessmentMethod();

		if (StringUtils.isNotEmpty(deprecatedDietaryAssessmentMethod.collectionTool)) {
			dietaryAssessmentMethod.setCollectionTool(deprecatedDietaryAssessmentMethod.collectionTool);
		}

		dietaryAssessmentMethod
				.setNumberOfNonConsecutiveOneDay(deprecatedDietaryAssessmentMethod.numberOfNonConsecutiveOneDay);

		if (StringUtils.isNotEmpty(deprecatedDietaryAssessmentMethod.softwareTool)) {
			dietaryAssessmentMethod.setSoftwareTool(deprecatedDietaryAssessmentMethod.softwareTool);
		}

		if (deprecatedDietaryAssessmentMethod.numberOfFoodItems != null
				&& !deprecatedDietaryAssessmentMethod.numberOfFoodItems.isEmpty()) {
			String numberOfFoodItems = deprecatedDietaryAssessmentMethod.numberOfFoodItems.get(0);
			dietaryAssessmentMethod.setNumberOfFoodItems(numberOfFoodItems);
		}

		if (deprecatedDietaryAssessmentMethod.recordTypes != null
				&& !deprecatedDietaryAssessmentMethod.recordTypes.isEmpty()) {
			String recordType = deprecatedDietaryAssessmentMethod.recordTypes.get(0);
			dietaryAssessmentMethod.setRecordTypes(recordType);
		}

		if (deprecatedDietaryAssessmentMethod.foodDescriptors != null
				&& !deprecatedDietaryAssessmentMethod.foodDescriptors.isEmpty()) {
			String foodDescriptor = deprecatedDietaryAssessmentMethod.foodDescriptors.get(0);
			dietaryAssessmentMethod.setFoodDescriptors(foodDescriptor);
		}

		return dietaryAssessmentMethod;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Laboratory} to
	 * an EMF {@link metadata.Laboratory}.
	 */
	public static metadata.Laboratory convert(Laboratory deprecatedLaboratory) {

		metadata.Laboratory laboratory = MetadataFactory.eINSTANCE.createLaboratory();

		if (StringUtils.isNotEmpty(deprecatedLaboratory.accreditation)) {
			StringObject sO = MetadataFactory.eINSTANCE.createStringObject();
			sO.setValue(deprecatedLaboratory.accreditation);
			laboratory.getLaboratoryAccreditation().add(sO);
		}

		if (StringUtils.isNotEmpty(deprecatedLaboratory.name)) {
			laboratory.setLaboratoryName(deprecatedLaboratory.name);
		}

		if (StringUtils.isNotEmpty(deprecatedLaboratory.country)) {
			laboratory.setLaboratoryCountry(deprecatedLaboratory.country);
		}

		return laboratory;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Assay} to an
	 * EMF {@link metadata.Assay}.
	 */
	public static metadata.Assay convert(Assay deprecatedAssay) {

		metadata.Assay assay = MetadataFactory.eINSTANCE.createAssay();

		if (StringUtils.isNotEmpty(deprecatedAssay.name)) {
			assay.setAssayName(deprecatedAssay.name);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.description)) {
			assay.setAssayDescription(deprecatedAssay.description);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.moisturePercentage)) {
			assay.setPercentageOfMoisture(deprecatedAssay.moisturePercentage);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.fatPercentage)) {
			assay.setPercentageOfFat(deprecatedAssay.fatPercentage);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.detectionLimit)) {
			assay.setLimitOfDetection(deprecatedAssay.detectionLimit);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.quantificationLimit)) {
			assay.setLimitOfQuantification(deprecatedAssay.quantificationLimit);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.leftCensoredData)) {
			assay.setLeftCensoredData(deprecatedAssay.leftCensoredData);
		}

		if (StringUtils.isNotEmpty(deprecatedAssay.contaminationRange)) {
			assay.setRangeOfContamination(deprecatedAssay.contaminationRange);
		}

		if (StringUtils.isNotBlank(deprecatedAssay.uncertaintyValue)) {
			assay.setUncertaintyValue(deprecatedAssay.uncertaintyValue);
		}

		return assay;
	}

	/**
	 * Convert deprecated RAKIP
	 * {@link de.bund.bfr.knime.fsklab.rakip.DataBackground} to an EMF
	 * {@link metadata.DataBackground}.
	 */
	public static metadata.DataBackground convert(DataBackground deprecatedDataBackground) {

		metadata.DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();

		if (deprecatedDataBackground.study != null) {
			metadata.Study study = convert(deprecatedDataBackground.study);
			dataBackground.setStudy(study);
		}

		if (deprecatedDataBackground.studySample != null) {
			metadata.StudySample studySample = convert(deprecatedDataBackground.studySample);
			dataBackground.getStudySample().add(studySample);
		}

		if (deprecatedDataBackground.dietaryAssessmentMethod != null) {
			metadata.DietaryAssessmentMethod dietaryAssessmentMethod = convert(
					deprecatedDataBackground.dietaryAssessmentMethod);
			dataBackground.getDietaryAssessmentMethod().add(dietaryAssessmentMethod);
		}

		if (deprecatedDataBackground.laboratory != null) {
			metadata.Laboratory laboratory = convert(deprecatedDataBackground.laboratory);
			dataBackground.getLaboratory().add(laboratory);
		}

		if (deprecatedDataBackground.assay != null) {
			metadata.Assay assay = convert(deprecatedDataBackground.assay);
			dataBackground.getAssay().add(assay);
		}

		return dataBackground;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.ModelMath} to
	 * an EMF {@link metadata.ModelMath}.
	 */
	public static metadata.ModelMath convert(ModelMath deprecatedModelMath) {

		metadata.ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

		// TODO: quality measures

		if (StringUtils.isNotEmpty(deprecatedModelMath.fittingProcedure)) {
			modelMath.setFittingProcedure(deprecatedModelMath.fittingProcedure);
		}

		if (deprecatedModelMath.event != null && !deprecatedModelMath.event.isEmpty()) {
			modelMath.getEvent().addAll(toStringObjectList(deprecatedModelMath.event));
		}

		if (deprecatedModelMath.parameter != null) {
			List<metadata.Parameter> parameters = deprecatedModelMath.parameter.stream().map(RakipUtil::convert)
					.collect(Collectors.toList());
			modelMath.getParameter().addAll(parameters);
		}

		if (deprecatedModelMath.modelEquation != null) {
			List<metadata.ModelEquation> modelEquations = deprecatedModelMath.modelEquation.stream()
					.map(RakipUtil::convert).collect(Collectors.toList());
			modelMath.getModelEquation().addAll(modelEquations);
		}

		if (deprecatedModelMath.exposure != null) {
			metadata.Exposure exposure = convert(deprecatedModelMath.exposure);
			modelMath.setExposure(exposure);
		}

		return modelMath;
	}

	/**
	 * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Parameter} to
	 * an EMF {@link metadata.Parameter}.
	 */
	public static metadata.Parameter convert(Parameter deprecatedParameter) {

		metadata.Parameter parameter = MetadataFactory.eINSTANCE.createParameter();

		if (StringUtils.isNotEmpty(deprecatedParameter.id)) {
			parameter.setParameterID(deprecatedParameter.id);
		}

		if (deprecatedParameter.classification != null) {

			ParameterClassification parameterClassification;

			switch (deprecatedParameter.classification) {
			case input:
				parameterClassification = ParameterClassification.INPUT;
				break;
			case output:
				parameterClassification = ParameterClassification.OUTPUT;
				break;
			default:
				parameterClassification = ParameterClassification.CONSTANT;
			}

			parameter.setParameterClassification(parameterClassification);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.name)) {
			parameter.setParameterName(deprecatedParameter.name);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.description)) {
			parameter.setParameterDescription(deprecatedParameter.description);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.type)) {
			parameter.setParameterType(deprecatedParameter.type);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.unit)) {
			parameter.setParameterUnit(deprecatedParameter.unit);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.unitCategory)) {
			parameter.setParameterUnitCategory(deprecatedParameter.unitCategory);
		}

		if (deprecatedParameter.dataType != null) {

			ParameterType parameterType;

			switch (deprecatedParameter.dataType) {
			case Integer:
				parameterType = ParameterType.INTEGER;
				break;
			case Double:
				parameterType = ParameterType.DOUBLE;
				break;
			case Number:
				parameterType = ParameterType.NUMBER;
				break;
			case Date:
				parameterType = ParameterType.DATE;
				break;
			case File:
				parameterType = ParameterType.FILE;
				break;
			case Boolean:
				parameterType = ParameterType.BOOLEAN;
				break;
			case VectorOfNumbers:
				parameterType = ParameterType.VECTOR_OF_NUMBERS;
				break;
			case VectorOfStrings:
				parameterType = ParameterType.VECTOR_OF_STRINGS;
				break;
			case MatrixOfNumbers:
				parameterType = ParameterType.MATRIX_OF_NUMBERS;
				break;
			case MatrixOfStrings:
				parameterType = ParameterType.MATRIX_OF_STRINGS;
				break;
			case Object:
				parameterType = ParameterType.OBJECT;
				break;
			default:
				parameterType = ParameterType.OTHER;
			}

			parameter.setParameterDataType(parameterType);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.source)) {
			parameter.setParameterSource(deprecatedParameter.source);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.subject)) {
			parameter.setParameterSubject(deprecatedParameter.subject);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.distribution)) {
			parameter.setParameterDistribution(deprecatedParameter.distribution);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.value)) {
			parameter.setParameterValue(deprecatedParameter.value);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.variabilitySubject)) {
			parameter.setParameterVariabilitySubject(deprecatedParameter.variabilitySubject);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.minValue)) {
			parameter.setParameterValueMin(deprecatedParameter.minValue);
		}

		if (StringUtils.isNotEmpty(deprecatedParameter.maxValue)) {
			parameter.setParameterValueMax(deprecatedParameter.maxValue);
		}

		if (deprecatedParameter.error != null) {
			parameter.setParameterError(deprecatedParameter.error.toString());
		}

		return parameter;
	}

	public static metadata.ModelEquation convert(ModelEquation deprecatedModelEquation) {

		metadata.ModelEquation modelEquation = MetadataFactory.eINSTANCE.createModelEquation();

		if (StringUtils.isNotEmpty(deprecatedModelEquation.equationName)) {
			modelEquation.setModelEquationName(deprecatedModelEquation.equationName);
		}

		if (StringUtils.isNotEmpty(deprecatedModelEquation.equationClass)) {
			modelEquation.setModelEquationClass(deprecatedModelEquation.equationClass);
		}

		if (StringUtils.isNotEmpty(deprecatedModelEquation.equation)) {
			modelEquation.setModelEquation(deprecatedModelEquation.equation);
		}

		// hypothesis of the model is not included in deprecatedModelEquation

		// reference
		if (deprecatedModelEquation.equationReference != null) {
			List<metadata.Reference> references = modelEquation.getReference();
			deprecatedModelEquation.equationReference.stream().map(RakipUtil::convert).forEach(references::add);
		}

		return modelEquation;
	}

	public static metadata.Exposure convert(Exposure deprecatedExposure) {

		metadata.Exposure exposure = MetadataFactory.eINSTANCE.createExposure();

		if (StringUtils.isNotEmpty(deprecatedExposure.treatment)) {
			StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
			stringObject.setValue(deprecatedExposure.treatment);
			exposure.getMethodologicalTreatmentOfLeftCensoredData().add(stringObject);
		}

		if (StringUtils.isNotEmpty(deprecatedExposure.contaminationLevel)) {
			StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
			stringObject.setValue(deprecatedExposure.contaminationLevel);
			exposure.getLevelOfContaminationAfterLeftCensoredDataTreatment().add(stringObject);
		}

		if (StringUtils.isNotEmpty(deprecatedExposure.exposureType)) {
			exposure.setTypeOfExposure(deprecatedExposure.exposureType);
		}

		if (StringUtils.isNotEmpty(deprecatedExposure.scenario)) {
			StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
			stringObject.setValue(deprecatedExposure.scenario);
			exposure.getScenario().add(stringObject);
		}

		if (StringUtils.isNotEmpty(deprecatedExposure.uncertaintyEstimation)) {
			exposure.setUncertaintyEstimation(deprecatedExposure.uncertaintyEstimation);
		}

		return exposure;
	}

	private static List<StringObject> toStringObjectList(List<String> input) {

		List<StringObject> stringObjectList = new ArrayList<>();
		for (String o : input) {
			StringObject sO = MetadataFactory.eINSTANCE.createStringObject();
			sO.setValue(o);
			stringObjectList.add(sO);
		}

		return stringObjectList;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * TODO: replace {@link #convert(GeneralInformation)} with this. Convert
	 * deprecated RAKIP general information to General information of the Generic
	 * model v1.0.4.
	 */
	public static de.bund.bfr.metadata.swagger.GenericModelGeneralInformation convert2(
			GeneralInformation deprecatedGI) {

		de.bund.bfr.metadata.swagger.GenericModelGeneralInformation generalInformation = new de.bund.bfr.metadata.swagger.GenericModelGeneralInformation();

		generalInformation.setName(deprecatedGI.name);
		generalInformation.setSource(deprecatedGI.source);
		generalInformation.setIdentifier(deprecatedGI.identifier);
		generalInformation.setCreationDate(toLocalDate(deprecatedGI.creationDate));

		for (Date date : deprecatedGI.modificationDate) {
			generalInformation.addModificationDateItem(toLocalDate(date));
		}

		generalInformation.rights(deprecatedGI.rights);
		generalInformation.setAvailability(Boolean.toString(deprecatedGI.isAvailable));
		if (deprecatedGI.url != null) {
			generalInformation.setUrl(deprecatedGI.url.toString());
		}
		generalInformation.setFormat(deprecatedGI.format);

		for (Record record : deprecatedGI.reference) {
			generalInformation.addReferenceItem(convert2(record));
		}

		generalInformation.setLanguage(deprecatedGI.language);
		generalInformation.setSoftware(deprecatedGI.software);
		generalInformation.setLanguageWrittenIn(deprecatedGI.languageWrittenIn);

		if (deprecatedGI.modelCategory != null) {
			generalInformation.setModelCategory(convert2(deprecatedGI.modelCategory));
		}

		generalInformation.setStatus(deprecatedGI.status);
		generalInformation.setObjective(deprecatedGI.objective);
		generalInformation.setDescription(deprecatedGI.description);

		return generalInformation;
	}

	/**
	 * TODO: replace {@link #convert(com.gmail.gcolaianni5.jris.bean.Record)} with
	 * this. Convert deprecated RIS record to Reference (1.0.4).
	 */
	public static de.bund.bfr.metadata.swagger.Reference convert2(com.gmail.gcolaianni5.jris.bean.Record record) {

		de.bund.bfr.metadata.swagger.Reference reference = new de.bund.bfr.metadata.swagger.Reference();

		// Is reference description is not included in RIS
		reference.setIsReferenceDescription(false);

		if (record.getType() != null) {
			reference.addPublicationTypeItem(PUBLICATION_TYPE.get(record.getType()));
		}

		// Parse RIS date to LocalDate. Since both use ISO 8601 there is no need to add
		// the date format.
		final String publicationDate = record.getPrimaryDate();
		if (StringUtils.isNotEmpty(publicationDate)) {
			reference.setDate(LocalDate.parse(publicationDate));
		}

		reference.setDoi(record.getDoi());

		List<String> firstAuthors = record.getFirstAuthors();
		if (firstAuthors != null && !firstAuthors.isEmpty()) {
			reference.setAuthorList(String.join(";", firstAuthors));
		}

		reference.setTitle(record.getTitle());
		reference.setAbstract(record.getAbstr());
		reference.setVolume(record.getVolumeNumber());

		if (record.getIssueNumber() != null) {
			reference.setIssue(record.getIssueNumber().toString());
		}

		reference.setWebsite(record.getUrl());

		return reference;
	}

	/**
	 * TODO: replace {@link #convert(ModelCategory)} Convert deprecated
	 * ModelCategory to 1.0.4
	 */
	public static de.bund.bfr.metadata.swagger.ModelCategory convert2(ModelCategory deprecated) {
		de.bund.bfr.metadata.swagger.ModelCategory modelCategory = new de.bund.bfr.metadata.swagger.ModelCategory();

		modelCategory.setModelClass(deprecated.modelClass);
		modelCategory.setModelSubClass(deprecated.modelSubClass);
		modelCategory.setModelClassComment(deprecated.modelClassComment);
		modelCategory.setBasicProcess(deprecated.basicProcess);

		return modelCategory;
	}

	/**
	 * TODO: replace #
	 */
	public static de.bund.bfr.metadata.swagger.GenericModelScope convert2(Scope deprecated) {
		de.bund.bfr.metadata.swagger.GenericModelScope scope = new de.bund.bfr.metadata.swagger.GenericModelScope();

		if (deprecated.product != null) {
			scope.addProductItem(convert2(deprecated.product));
		}

		if (deprecated.hazard != null) {
			scope.addHazardItem(convert2(deprecated.hazard));
		}

		if (deprecated.populationGroup != null) {
			scope.addPopulationGroupItem(convert2(deprecated.populationGroup));
		}

		scope.setGeneralComment(deprecated.generalComment);
		scope.setTemporalInformation(deprecated.temporalInformation);
		// TODO: spatialInformation

		return scope;
	}

	public static de.bund.bfr.metadata.swagger.Product convert2(Product deprecated) {
		de.bund.bfr.metadata.swagger.Product product = new de.bund.bfr.metadata.swagger.Product();

		product.setName(deprecated.environmentName);
		product.setDescription(deprecated.environmentDescription);
		product.setUnit(deprecated.environmentUnit);
		product.setMethod(deprecated.productionMethod);
		product.setPackaging(deprecated.packaging);
		product.setTreatment(deprecated.productTreatment);
		product.setOriginCountry(deprecated.originCountry);
		product.setOriginArea(deprecated.originArea);
		product.setFisheriesArea(deprecated.fisheriesArea);

		if (deprecated.productionDate != null) {
			product.setProductionDate(toLocalDate(deprecated.productionDate));
		}

		if (deprecated.expirationDate != null) {
			product.setExpiryDate(toLocalDate(deprecated.expirationDate));
		}

		return product;
	}

	public static de.bund.bfr.metadata.swagger.Hazard convert2(Hazard deprecated) {
		de.bund.bfr.metadata.swagger.Hazard hazard = new de.bund.bfr.metadata.swagger.Hazard();

		hazard.setType(deprecated.hazardType);
		hazard.setName(deprecated.hazardName);
		hazard.setDescription(deprecated.hazardDescription);
		hazard.setUnit(deprecated.hazardUnit);
		hazard.setAdverseEffect(deprecated.adverseEffect);
		hazard.setSourceOfContamination(deprecated.sourceOfContamination);
		hazard.setBenchmarkDose(deprecated.bmd);
		hazard.setMaximumResidueLimit(deprecated.mrl);
		hazard.setNoObservedAdverseAffectLevel(deprecated.noael);
		hazard.setLowestObservedAdverseAffectLevel(deprecated.loael);
		hazard.setAcceptableOperatorsExposureLevel(deprecated.aoel);
		hazard.setAcuteReferenceDose(deprecated.ard);
		hazard.setAcceptableDailyIntake(deprecated.adi);
		hazard.setIndSum(deprecated.hazardIndSum);

		return hazard;
	}

	public static de.bund.bfr.metadata.swagger.PopulationGroup convert2(PopulationGroup deprecated) {
		de.bund.bfr.metadata.swagger.PopulationGroup pg = new de.bund.bfr.metadata.swagger.PopulationGroup();

		pg.setName(deprecated.populationName);
		pg.setTargetPopulation(deprecated.targetPopulation);
		pg.setPopulationSpan(deprecated.populationSpan);
		pg.setPopulationDescription(deprecated.populationDescription);
		pg.setPopulationAge(deprecated.populationAge);
		pg.setPopulationGender(deprecated.populationGender);
		pg.setBmi(deprecated.bmi);
		pg.setSpecialDietGroups(deprecated.specialDietGroups);
		pg.setPatternConsumption(deprecated.patternConsumption);
		pg.setRegion(deprecated.region);
		pg.setCountry(deprecated.country);
		pg.setPopulationRiskFactor(deprecated.populationRiskFactor);
		pg.setSeason(deprecated.season);

		return pg;
	}

	public static de.bund.bfr.metadata.swagger.GenericModelDataBackground convert2(DataBackground deprecated) {
		de.bund.bfr.metadata.swagger.GenericModelDataBackground db = new de.bund.bfr.metadata.swagger.GenericModelDataBackground();

		if (deprecated.study != null) {
			db.setStudy(convert2(deprecated.study));
		}

		if (deprecated.studySample != null) {
			db.addStudySampleItem(convert2(deprecated.studySample));
		}

		if (deprecated.dietaryAssessmentMethod != null) {
			db.addDietaryAssessmentMethodItem(convert2(deprecated.dietaryAssessmentMethod));
		}

		if (deprecated.laboratory != null) {
			db.addLaboratoryItem(convert2(deprecated.laboratory));
		}

		if (deprecated.assay != null) {
			db.addAssayItem(convert2(deprecated.assay));
		}

		return db;
	}

	public static de.bund.bfr.metadata.swagger.Study convert2(Study deprecated) {
		de.bund.bfr.metadata.swagger.Study study = new de.bund.bfr.metadata.swagger.Study();

		study.setIdentifier(deprecated.id);
		study.setTitle(deprecated.title);
		study.setDescription(deprecated.description);
		study.setDesignType(deprecated.designType);
		study.setAssayMeasurementType(deprecated.measurementType);
		study.setAssayTechnologyType(deprecated.technologyType);
		study.setAssayTechnologyPlatform(deprecated.technologyPlatform);
		study.setAccreditationProcedureForTheAssayTechnology(deprecated.accreditationProcedure);
		study.setProtocolName(deprecated.protocolName);
		study.setProtocolType(deprecated.protocolType);
		study.setProtocolDescription(deprecated.protocolDescription);
		if (deprecated.protocolUri != null) {
			study.setProtocolURI(deprecated.protocolUri.toString());
		}
		study.setProtocolVersion(deprecated.protocolVersion);
		study.setProtocolParametersName(deprecated.parametersName);
		study.setProtocolComponentsName(deprecated.componentsName);
		study.setProtocolComponentsType(deprecated.componentsType);

		return study;
	}

	public static de.bund.bfr.metadata.swagger.StudySample convert2(StudySample deprecated) {
		de.bund.bfr.metadata.swagger.StudySample studySample = new de.bund.bfr.metadata.swagger.StudySample();
		studySample.setSampleName(deprecated.sample);
		studySample.setProtocolOfSampleCollection(deprecated.collectionProtocol);
		studySample.setSamplingStrategy(deprecated.samplingStrategy);
		studySample.setTypeOfSamplingProgram(deprecated.samplingProgramType);
		studySample.setSamplingMethod(deprecated.samplingMethod);
		studySample.setSamplingPlan(deprecated.samplingPlan);
		studySample.setSamplingWeight(deprecated.samplingWeight);
		studySample.setSamplingSize(deprecated.samplingSize);
		studySample.setLotSizeUnit(deprecated.lotSizeUnit);
		studySample.samplingPoint(deprecated.samplingPoint);

		return studySample;
	}

	public static de.bund.bfr.metadata.swagger.DietaryAssessmentMethod convert2(DietaryAssessmentMethod deprecated) {
		de.bund.bfr.metadata.swagger.DietaryAssessmentMethod method = new de.bund.bfr.metadata.swagger.DietaryAssessmentMethod();
		method.setCollectionTool(deprecated.collectionTool);
		method.setNumberOfNonConsecutiveOneDay(Integer.toString(deprecated.numberOfNonConsecutiveOneDay));
		method.setSoftwareTool(deprecated.softwareTool);
		method.setNumberOfFoodItems(deprecated.numberOfFoodItems);
		method.setRecordTypes(deprecated.recordTypes);
		method.setFoodDescriptors(deprecated.foodDescriptors);

		return method;
	}

	public static de.bund.bfr.metadata.swagger.Laboratory convert2(Laboratory deprecated) {
		de.bund.bfr.metadata.swagger.Laboratory laboratory = new de.bund.bfr.metadata.swagger.Laboratory();

		if (StringUtils.isNotEmpty(deprecated.accreditation)) {
			laboratory.addAccreditationItem(deprecated.accreditation);
		}

		laboratory.setName(deprecated.name);
		laboratory.country(deprecated.country);

		return laboratory;
	}

	public static de.bund.bfr.metadata.swagger.Assay convert2(Assay deprecated) {
		de.bund.bfr.metadata.swagger.Assay assay = new de.bund.bfr.metadata.swagger.Assay();
		assay.setName(deprecated.name);
		assay.setDescription(deprecated.description);
		assay.setMoisturePercentage(deprecated.moisturePercentage);
		assay.setFatPercentage(deprecated.fatPercentage);
		assay.setDetectionLimit(deprecated.detectionLimit);
		assay.setQuantificationLimit(deprecated.quantificationLimit);
		assay.setLeftCensoredData(deprecated.leftCensoredData);
		assay.setContaminationRange(deprecated.contaminationRange);
		assay.setUncertaintyValue(deprecated.uncertaintyValue);

		return assay;
	}

	public static de.bund.bfr.metadata.swagger.GenericModelModelMath convert2(ModelMath deprecated) {
		de.bund.bfr.metadata.swagger.GenericModelModelMath math = new de.bund.bfr.metadata.swagger.GenericModelModelMath();
		
		deprecated.parameter.stream().map(RakipUtil::convert2).forEach(math::addParameterItem);
		
		de.bund.bfr.metadata.swagger.QualityMeasures measures = new de.bund.bfr.metadata.swagger.QualityMeasures();
		measures.setSSE(new BigDecimal(deprecated.sse));
		measures.setMSE(new BigDecimal(deprecated.mse));
		measures.setRMSE(new BigDecimal(deprecated.rmse));
		measures.setAIC(new BigDecimal(deprecated.aic));
		measures.setBIC(new BigDecimal(deprecated.bic));
		math.addQualityMeasuresItem(measures);
		
		deprecated.modelEquation.stream().map(RakipUtil::convert2).forEach(math::addModelEquationItem);
		math.setFittingProcedure(deprecated.fittingProcedure);
		
		if (deprecated.exposure != null) {
			math.addExposureItem(convert2(deprecated.exposure));
		}
		
		math.setEvent(deprecated.event);

		return math;
	}

	public static de.bund.bfr.metadata.swagger.Parameter convert2(Parameter deprecated) {
		de.bund.bfr.metadata.swagger.Parameter parameter = new de.bund.bfr.metadata.swagger.Parameter();
		parameter.setId(deprecated.id);
		parameter.setClassification(CLASSIF.get(deprecated.classification));
		parameter.setName(deprecated.name);
		parameter.setDescription(deprecated.description);
		parameter.setUnit(deprecated.unit);
		parameter.setUnitCategory(deprecated.unitCategory);
		parameter.setDataType(TYPES.get(deprecated.dataType));
		parameter.setSource(deprecated.source);
		parameter.setSubject(deprecated.subject);
		parameter.setDistribution(deprecated.distribution);
		parameter.setValue(deprecated.value);
		// String reference cannot be converted to Reference object.
		parameter.setVariabilitySubject(deprecated.variabilitySubject);
		parameter.setMinValue(deprecated.minValue);
		parameter.setMaxValue(deprecated.maxValue);

		if (deprecated.error != null) {
			parameter.setError(deprecated.error.toString());
		}

		return parameter;
	}
	
	public static de.bund.bfr.metadata.swagger.ModelEquation convert2(ModelEquation deprecated) {
		de.bund.bfr.metadata.swagger.ModelEquation equation = new de.bund.bfr.metadata.swagger.ModelEquation();
		equation.setName(deprecated.equationName);
		equation.setPropertyClass(deprecated.equationClass);
		deprecated.equationReference.stream().map(RakipUtil::convert2).forEach(equation::addReferenceItem);
		equation.setModelEquation(deprecated.equation);
		
		return equation;
	}
	
	public static de.bund.bfr.metadata.swagger.Exposure convert2(Exposure deprecated) {
		de.bund.bfr.metadata.swagger.Exposure exposure = new de.bund.bfr.metadata.swagger.Exposure();

		if (StringUtils.isNotEmpty(deprecated.treatment)) {
			exposure.addTreatmentItem(deprecated.treatment);
		}

		if (StringUtils.isNotEmpty(deprecated.contaminationLevel)) {
			exposure.addContaminationItem(deprecated.contaminationLevel);
		}

		exposure.setType(deprecated.exposureType);
		
		if (StringUtils.isNotEmpty(deprecated.scenario)) {
			exposure.addScenarioItem(deprecated.scenario);
		}

		exposure.setUncertaintyEstimation(deprecated.uncertaintyEstimation);
		
		return exposure;
	}

	/** Internal map used to convert RIS types to 1.0.4 Reference types. */
	private static Map<Type, PublicationTypeEnum> PUBLICATION_TYPE;
	static {
		PUBLICATION_TYPE = new HashMap<>();

		PUBLICATION_TYPE.put(Type.ABST, PublicationTypeEnum.ABST);
		PUBLICATION_TYPE.put(Type.ADVS, PublicationTypeEnum.ADVS);
		PUBLICATION_TYPE.put(Type.AGGR, PublicationTypeEnum.AGGR);
		PUBLICATION_TYPE.put(Type.ANCIENT, PublicationTypeEnum.ANCIENT);
		PUBLICATION_TYPE.put(Type.ART, PublicationTypeEnum.ART);
		PUBLICATION_TYPE.put(Type.BILL, PublicationTypeEnum.BILL);
		PUBLICATION_TYPE.put(Type.BLOG, PublicationTypeEnum.BLOG);
		PUBLICATION_TYPE.put(Type.BOOK, PublicationTypeEnum.BOOK);
		PUBLICATION_TYPE.put(Type.CASE, PublicationTypeEnum.CASE);
		PUBLICATION_TYPE.put(Type.CHAP, PublicationTypeEnum.CHAP);
		PUBLICATION_TYPE.put(Type.CHART, PublicationTypeEnum.CHART);
		PUBLICATION_TYPE.put(Type.CLSWK, PublicationTypeEnum.CLSWK);
		PUBLICATION_TYPE.put(Type.COMP, PublicationTypeEnum.COMP);
		PUBLICATION_TYPE.put(Type.CONF, PublicationTypeEnum.CONF);
		PUBLICATION_TYPE.put(Type.CPAPER, PublicationTypeEnum.CPAPER);
		PUBLICATION_TYPE.put(Type.CTLG, PublicationTypeEnum.CTLG);
		PUBLICATION_TYPE.put(Type.DATA, PublicationTypeEnum.DATA);
		PUBLICATION_TYPE.put(Type.DBASE, PublicationTypeEnum.DBASE);
		PUBLICATION_TYPE.put(Type.DICT, PublicationTypeEnum.DICT);
		PUBLICATION_TYPE.put(Type.EBOOK, PublicationTypeEnum.EBOOK);
		PUBLICATION_TYPE.put(Type.ECHAP, PublicationTypeEnum.ECHAP);
		PUBLICATION_TYPE.put(Type.EDBOOK, PublicationTypeEnum.EDBOOK);
		PUBLICATION_TYPE.put(Type.EJOUR, PublicationTypeEnum.EJOUR);
		// Typo in PublicationTypeEnum. It should be 'ELEC'
		PUBLICATION_TYPE.put(Type.ELEC, PublicationTypeEnum.ELECT);
		PUBLICATION_TYPE.put(Type.ENCYC, PublicationTypeEnum.ENCYC);
		PUBLICATION_TYPE.put(Type.EQUA, PublicationTypeEnum.EQUA);
		PUBLICATION_TYPE.put(Type.FIGURE, PublicationTypeEnum.FIGURE);
		PUBLICATION_TYPE.put(Type.GEN, PublicationTypeEnum.GEN);
		PUBLICATION_TYPE.put(Type.GOVDOC, PublicationTypeEnum.GOVDOC);
		PUBLICATION_TYPE.put(Type.GRANT, PublicationTypeEnum.GRANT);
		PUBLICATION_TYPE.put(Type.HEAR, PublicationTypeEnum.HEAR);
		PUBLICATION_TYPE.put(Type.ICOMM, PublicationTypeEnum.ICOMM);
		PUBLICATION_TYPE.put(Type.INPR, PublicationTypeEnum.INPR);
		PUBLICATION_TYPE.put(Type.JOUR, PublicationTypeEnum.JOUR);
		PUBLICATION_TYPE.put(Type.JFULL, PublicationTypeEnum.JFULL);
		PUBLICATION_TYPE.put(Type.LEGAL, PublicationTypeEnum.LEGAL);
		PUBLICATION_TYPE.put(Type.MANSCPT, PublicationTypeEnum.MANSCPT);
		PUBLICATION_TYPE.put(Type.MAP, PublicationTypeEnum.MAP);
		PUBLICATION_TYPE.put(Type.MGZN, PublicationTypeEnum.MGZN);
		PUBLICATION_TYPE.put(Type.MPCT, PublicationTypeEnum.MPCT);
		PUBLICATION_TYPE.put(Type.MULTI, PublicationTypeEnum.MULTI);
		PUBLICATION_TYPE.put(Type.MUSIC, PublicationTypeEnum.MUSIC);
		// Typo in PublicationTypeEnum. It should be 'NEWS'
		PUBLICATION_TYPE.put(Type.NEWS, PublicationTypeEnum.NEW);
		PUBLICATION_TYPE.put(Type.PAMP, PublicationTypeEnum.PAMP);
		PUBLICATION_TYPE.put(Type.PAT, PublicationTypeEnum.PAT);
		PUBLICATION_TYPE.put(Type.PCOMM, PublicationTypeEnum.PCOMM);
		PUBLICATION_TYPE.put(Type.RPRT, PublicationTypeEnum.RPRT);
		PUBLICATION_TYPE.put(Type.SER, PublicationTypeEnum.SER);
		PUBLICATION_TYPE.put(Type.SLIDE, PublicationTypeEnum.SLIDE);
		PUBLICATION_TYPE.put(Type.SOUND, PublicationTypeEnum.SOUND);
		PUBLICATION_TYPE.put(Type.STAND, PublicationTypeEnum.STAND);
		PUBLICATION_TYPE.put(Type.STAT, PublicationTypeEnum.STAT);
		PUBLICATION_TYPE.put(Type.THES, PublicationTypeEnum.THES);
		PUBLICATION_TYPE.put(Type.UNPB, PublicationTypeEnum.UNPB);
		PUBLICATION_TYPE.put(Type.VIDEO, PublicationTypeEnum.VIDEO);
	}

	/** Internal map used to convert parameter classification to 1.0.4. */
	private static Map<Parameter.Classification, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum> CLASSIF;
	static {
		CLASSIF = new HashMap<>();

		CLASSIF.put(Parameter.Classification.input, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.INPUT);
		CLASSIF.put(Parameter.Classification.output, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.OUTPUT);
		CLASSIF.put(Parameter.Classification.constant,
				de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.CONSTANT);
	}

	/** Internal map used to convert parameter types to 1.0.4. */
	private static Map<Parameter.DataTypes, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum> TYPES;
	static {
		TYPES = new HashMap<>();

		TYPES.put(Parameter.DataTypes.Integer, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.INTEGER);
		TYPES.put(Parameter.DataTypes.Double, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.DOUBLE);
		TYPES.put(Parameter.DataTypes.Number, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.NUMBER);
		TYPES.put(Parameter.DataTypes.Date, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.DATE);
		TYPES.put(Parameter.DataTypes.File, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.FILE);
		TYPES.put(Parameter.DataTypes.Boolean, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.BOOLEAN);
		TYPES.put(Parameter.DataTypes.VectorOfNumbers, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.VECTOROFNUMBERS);
		TYPES.put(Parameter.DataTypes.VectorOfStrings, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.VECTOROFSTRINGS);
		TYPES.put(Parameter.DataTypes.MatrixOfNumbers, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.MATRIXOFNUMBERS);
		TYPES.put(Parameter.DataTypes.MatrixOfStrings, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.MATRIXOFSTRINGS);
		TYPES.put(Parameter.DataTypes.Other, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
		TYPES.put(Parameter.DataTypes.Object, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
	}

	@SuppressWarnings("deprecation")
	private static LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
