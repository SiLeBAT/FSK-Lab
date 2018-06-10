package metadata;

import java.util.ResourceBundle;

import de.bund.bfr.knime.fsklab.util.UTF8Control;

/**
 * Wrapper for the metadata tree resource bundle. This class is tested by
 * {@link MetadataTreeBundleTest} to avoid runtime exceptions. The tests should
 * run before making a new build.
 * 
 * @author Miguel de Alba
 */
public class MetadataTreeBundle {

	private final ResourceBundle bundle = ResourceBundle.getBundle("metadatatree", new UTF8Control());

	public String getGeneralInformation() {
		return bundle.getString("GeneralInformation");
	}

	public String getScope() {
		return bundle.getString("Scope");
	}

	public String getDataBackground() {
		return bundle.getString("DataBackground");
	}

	public String getModelMath() {
		return bundle.getString("ModelMath");
	}

	// Contact
	public String getContact_title() {
		return bundle.getString("Contact.title");
	}

	public String getContact_familyName() {
		return bundle.getString("Contact.familyName");
	}

	public String getContact_givenName() {
		return bundle.getString("Contact.givenName");
	}

	public String getContact_email() {
		return bundle.getString("Contact.email");
	}

	public String getContact_telephone() {
		return bundle.getString("Contact.telephone");
	}

	public String getContact_streetAddress() {
		return bundle.getString("Contact.streetAddress");
	}

	public String getContact_country() {
		return bundle.getString("Contact.country");
	}

	public String getContact_city() {
		return bundle.getString("Contact.city");
	}

	public String getContact_zipCode() {
		return bundle.getString("Contact.zipCode");
	}

	public String getContact_region() {
		return bundle.getString("Contact.region");
	}

	public String getContact_timeZone() {
		return bundle.getString("Contact.timeZone");
	}

	public String getContact_gender() {
		return bundle.getString("Contact.gender");
	}

	public String getContact_note() {
		return bundle.getString("Contact.note");
	}

	public String getContact_organization() {
		return bundle.getString("Contact.organization");
	}

	// ModelCategory
	public String getModelCategory_modelClass() {
		return bundle.getString("ModelCategory.modelClass");
	}

	public String getModelCategory_modelSubClass() {
		return bundle.getString("ModelCategory.modelSubClass");
	}

	public String getModelCategory_modelClassComment() {
		return bundle.getString("ModelCategory.modelClassComment");
	}

	public String getModelCategory_basicProcess() {
		return bundle.getString("ModelCategory.basicProcess");
	}

	public String getReference_isReferenceDescription() {
		return bundle.getString("Reference.isReferenceDescription");
	}

	public String getReference_publicationType() {
		return bundle.getString("Reference.publicationType");
	}

	public String getReference_publicationDate() {
		return bundle.getString("Reference.publicationDate");
	}

	public String getReference_pmid() {
		return bundle.getString("Reference.pmid");
	}

	public String getReference_doi() {
		return bundle.getString("Reference.doi");
	}

	public String getReference_authorList() {
		return bundle.getString("Reference.authorList");
	}

	public String getReference_publicationTitle() {
		return bundle.getString("Reference.publicationTitle");
	}

	public String getReference_publicationAbstract() {
		return bundle.getString("Reference.publicationAbstract");
	}

	public String getReference_publicationJournal() {
		return bundle.getString("Reference.publicationJournal");
	}

	public String getReference_publicationVolume() {
		return bundle.getString("Reference.publicationVolume");
	}

	public String getReference_publicationIssue() {
		return bundle.getString("Reference.publicationIssue");
	}

	public String getReference_publicationStatus() {
		return bundle.getString("Reference.publicationStatus");
	}

	public String getReference_publicationWebsite() {
		return bundle.getString("Reference.publicationWebsite");
	}

	public String getReference_comment() {
		return bundle.getString("Reference.comment");
	}

	// GeneralInformation
	public String getGeneralInformation_name() {
		return bundle.getString("GeneralInformation.name");
	}

	public String getGeneralInformation_source() {
		return bundle.getString("GeneralInformation.source");
	}

	public String getGeneralInformation_identifier() {
		return bundle.getString("GeneralInformation.identifier");
	}

	public String getGeneralInformation_creationDate() {
		return bundle.getString("GeneralInformation.creationDate");
	}

	public String getGeneralInformation_rights() {
		return bundle.getString("GeneralInformation.rights");
	}

	public String getGeneralInformation_available() {
		return bundle.getString("GeneralInformation.available");
	}

	public String getGeneralInformation_format() {
		return bundle.getString("GeneralInformation.format");
	}

	public String getGeneralInformation_language() {
		return bundle.getString("GeneralInformation.language");
	}

	public String getGeneralInformation_software() {
		return bundle.getString("GeneralInformation.software");
	}

	public String getGeneralInformation_languageWrittenIn() {
		return bundle.getString("GeneralInformation.languageWrittenIn");
	}

	public String getGeneralInformation_status() {
		return bundle.getString("GeneralInformation.status");
	}

	public String getGeneralInformation_objective() {
		return bundle.getString("GeneralInformation.objective");
	}

	public String getGeneralInformation_description() {
		return bundle.getString("GeneralInformation.description");
	}

	public String getGeneralInformation_author() {
		return bundle.getString("GeneralInformation.author");
	}

	public String getGeneralInformation_creators() {
		return bundle.getString("GeneralInformation.creators");
	}

	public String getGeneralInformation_modelCategory() {
		return bundle.getString("GeneralInformation.modelCategory");
	}

	public String getGeneralInformation_references() {
		return bundle.getString("GeneralInformation.references");
	}

	public String getGeneralInformation_modificationDates() {
		return bundle.getString("GeneralInformation.modificationDates");
	}

	// Product
	public String getProduct_productName() {
		return bundle.getString("Product.productName");
	}

	public String getProduct_productDescription() {
		return bundle.getString("Product.productDescription");
	}

	public String getProduct_productUnit() {
		return bundle.getString("Product.productUnit");
	}

	public String getProduct_productionMethod() {
		return bundle.getString("Product.productionMethod");
	}

	public String getProduct_packaging() {
		return bundle.getString("Product.packaging");
	}

	public String getProduct_productTreatment() {
		return bundle.getString("Product.productTreatment");
	}

	public String getProduct_originCountry() {
		return bundle.getString("Product.originCountry");
	}

	public String getProduct_originArea() {
		return bundle.getString("Product.originArea");
	}

	public String getProduct_fisheriesArea() {
		return bundle.getString("Product.fisheriesArea");
	}

	public String getProduct_productionDate() {
		return bundle.getString("Product.productionDate");
	}

	public String getProduct_expiryDate() {
		return bundle.getString("Product.expiryDate");
	}

	// Hazard
	public String getHazard_hazardType() {
		return bundle.getString("Hazard.hazardType");
	}

	public String getHazard_hazardName() {
		return bundle.getString("Hazard.hazardName");
	}

	public String getHazard_hazardDescription() {
		return bundle.getString("Hazard.hazardDescription");
	}

	public String getHazard_hazardUnit() {
		return bundle.getString("Hazard.hazardUnit");
	}

	public String getHazard_adverseEffect() {
		return bundle.getString("Hazard.adverseEffect");
	}

	public String getHazard_sourceOfContamination() {
		return bundle.getString("Hazard.sourceOfContamination");
	}

	public String getHazard_benchmarkDose() {
		return bundle.getString("Hazard.benchmarkDose");
	}

	public String getHazard_maximumResidueLimit() {
		return bundle.getString("Hazard.maximumResidueLimit");
	}

	public String getHazard_noObservedAdverseAffectLevel() {
		return bundle.getString("Hazard.noObservedAdverseAffectLevel");
	}

	public String getHazard_acceptableDailyIntake() {
		return bundle.getString("Hazard.acceptableDailyIntake");
	}

	public String getHazard_acuteReferenceDose() {
		return bundle.getString("Hazard.acuteReferenceDose");
	}

	public String getHazard_hazardIndSum() {
		return bundle.getString("Hazard.hazardIndSum");
	}

	// PopulationGroup
	public String getPopulationGroup_populationName() {
		return bundle.getString("PopulationGroup.populationName");
	}

	public String getPopulationGroup_targetPopulation() {
		return bundle.getString("PopulationGroup.targetPopulation");
	}

	public String getPopulationGroup_populationSpan() {
		return bundle.getString("PopulationGroup.populationSpan");
	}

	public String getPopulationGroup_populationDescription() {
		return bundle.getString("PopulationGroup.populationDescription");
	}

	public String getPopulationGroup_populationAge() {
		return bundle.getString("PopulationGroup.populationAge");
	}

	public String getPopulationGroup_populationGender() {
		return bundle.getString("PopulationGroup.populationGender");
	}

	public String getPopulationGroup_bmi() {
		return bundle.getString("PopulationGroup.bmi");
	}

	public String getPopulationGroup_specialDietGroups() {
		return bundle.getString("PopulationGroup.specialDietGroups");
	}

	public String getPopulationGroup_patternConsumption() {
		return bundle.getString("PopulationGroup.patternConsumption");
	}

	public String getPopulationGroup_region() {
		return bundle.getString("PopulationGroup.region");
	}

	public String getPopulationGroup_country() {
		return bundle.getString("PopulationGroup.country");
	}

	public String getPopulationGroup_populationRiskFactor() {
		return bundle.getString("PopulationGroup.populationRiskFactor");
	}

	public String getPopulationGroup_season() {
		return bundle.getString("PopulationGroup.season");
	}

	// Spatial information
	public String getSpatialInformation_region() {
		return bundle.getString("SpatialInformation.region");
	}

	public String getSpatialInformation_country() {
		return bundle.getString("SpatialInformation.country");
	}

	// Scope
	public String getScope_generalComment() {
		return bundle.getString("Scope.generalComment");
	}

	public String getScope_temporalInformation() {
		return bundle.getString("Scope.temporalInformation");
	}

	public String getScope_product() {
		return bundle.getString("Scope.product");
	}

	public String getScope_hazard() {
		return bundle.getString("Scope.hazard");
	}

	public String getScope_populationGroup() {
		return bundle.getString("Scope.populationGroup");
	}

	public String getScope_spatialInformation() {
		return bundle.getString("Scope.spatialInformation");
	}

	// Study
	public String getStudy_studyIdentifier() {
		return bundle.getString("Study.studyIdentifier");
	}

	public String getStudy_studyTitle() {
		return bundle.getString("Study.studyTitle");
	}

	public String getStudy_studyDescription() {
		return bundle.getString("Study.studyDescription");
	}

	public String getStudy_studyDesignType() {
		return bundle.getString("Study.studyDesignType");
	}

	public String getStudy_studyAssayMeasurementType() {
		return bundle.getString("Study.studyAssayMeasurementType");
	}

	public String getStudy_studyAssayTechnologyType() {
		return bundle.getString("Study.studyAssayTechnologyType");
	}

	public String getStudy_studyAssayTechnologyPlatform() {
		return bundle.getString("Study.studyAssayTechnologyPlatform");
	}

	public String getStudy_accreditationProcedureForTheAssayTechnology() {
		return bundle.getString("Study.accreditationProcedureForTheAssayTechnology");
	}

	public String getStudy_studyProtocolName() {
		return bundle.getString("Study.studyProtocolName");
	}

	public String getStudy_studyProtocolType() {
		return bundle.getString("Study.studyProtocolType");
	}

	public String getStudy_studyProtocolDescription() {
		return bundle.getString("Study.studyProtocolDescription");
	}

	public String getStudy_studyProtocolURI() {
		return bundle.getString("Study.studyProtocolURI");
	}

	public String getStudy_studyProtocolVersion() {
		return bundle.getString("Study.studyProtocolVersion");
	}

	public String getStudy_studyProtocolComponentsName() {
		return bundle.getString("Study.studyProtocolComponentsName");
	}

	public String getStudy_studyProtocolComponentsType() {
		return bundle.getString("Study.studyProtocolComponentsType");
	}

	// Study sample
	public String getStudySample_sampleName() {
		return bundle.getString("StudySample.sampleName");
	}

	public String getStudySample_protocolOfSampleCollection() {
		return bundle.getString("StudySample.protocolOfSampleCollection");
	}

	public String getStudySample_samplingStrategy() {
		return bundle.getString("StudySample.samplingStrategy");
	}

	public String getStudySample_typeOfSamplingProgram() {
		return bundle.getString("StudySample.typeOfSamplingProgram");
	}

	public String getStudySample_samplingMethod() {
		return bundle.getString("StudySample.samplingMethod");
	}

	public String getStudySample_samplingPlan() {
		return bundle.getString("StudySample.samplingPlan");
	}

	public String getStudySample_samplingWeight() {
		return bundle.getString("StudySample.samplingWeight");
	}

	public String getStudySample_samplingSize() {
		return bundle.getString("StudySample.samplingSize");
	}

	public String getStudySample_lotSizeUnit() {
		return bundle.getString("StudySample.lotSizeUnit");
	}

	public String getStudySample_samplingPoint() {
		return bundle.getString("StudySample.samplingPoint");
	}

	// Dietary assessment method
	public String getDietaryAssessmentMethod_collectionTool() {
		return bundle.getString("DietaryAssessmentMethod.collectionTool");
	}

	public String getDietaryAssessmentMethod_numberOfNonConsecutiveOneDay() {
		return bundle.getString("DietaryAssessmentMethod.numberOfNonConsecutiveOneDay");
	}

	public String getDietaryAssessmentMethod_softwareTool() {
		return bundle.getString("DietaryAssessmentMethod.softwareTool");
	}

	public String getDietaryAssessmentMethod_numberOfFoodItems() {
		return bundle.getString("DietaryAssessmentMethod.numberOfFoodItems");
	}

	public String getDietaryAssessmentMethod_recordTypes() {
		return bundle.getString("DietaryAssessmentMethod.recordTypes");
	}

	public String getDietaryAssessmentMethod_foodDescriptors() {
		return bundle.getString("DietaryAssessmentMethod.foodDescriptors");
	}

	// Laboratory
	public String getLaboratory_laboratoryAccreditation() {
		return bundle.getString("Laboratory.laboratoryAccreditation");
	}

	public String getLaboratory_laboratoryName() {
		return bundle.getString("Laboratory.laboratoryName");
	}

	public String getLaboratory_laboratoryCountry() {
		return bundle.getString("Laboratory.laboratoryCountry");
	}

	// Assay
	public String getAssay_assayName() {
		return bundle.getString("Assay.assayName");
	}

	public String getAssay_assayDescription() {
		return bundle.getString("Assay.assayDescription");
	}

	public String getAssay_percentageOfMoisture() {
		return bundle.getString("Assay.percentageOfMoisture");
	}

	public String getAssay_percentageOfFat() {
		return bundle.getString("Assay.percentageOfFat");
	}

	public String getAssay_limitOfDetection() {
		return bundle.getString("Assay.limitOfDetection");
	}

	public String getAssay_limitOfQuantification() {
		return bundle.getString("Assay.limitOfQuantification");
	}

	public String getAssay_leftCensoredData() {
		return bundle.getString("Assay.leftCensoredData");
	}

	public String getAssay_rangeOfContamination() {
		return bundle.getString("Assay.rangeOfContamination");
	}

	public String getAssay_uncertaintyValue() {
		return bundle.getString("Assay.uncertaintyValue");
	}

	// DataBackground
	public String getDataBackground_study() {
		return bundle.getString("DataBackground.study");
	}

	public String getDataBackground_studySample() {
		return bundle.getString("DataBackground.studySample");
	}

	public String getDataBackground_dietaryAssessmentMethod() {
		return bundle.getString("DataBackground.dietaryAssessmentMethod");
	}

	public String getDataBackground_laboratory() {
		return bundle.getString("DataBackground.laboratory");
	}

	public String getDataBackground_assay() {
		return bundle.getString("DataBackground.assay");
	}

	// Parameter
	public String getParameter_parameterId() {
		return bundle.getString("Parameter.parameterId");
	}

	public String getParameter_parameterClassification() {
		return bundle.getString("Parameter.parameterClassification");
	}

	public String getParameter_parameterName() {
		return bundle.getString("Parameter.parameterName");
	}

	public String getParameter_parameterDescription() {
		return bundle.getString("Parameter.parameterDescription");
	}

	public String getParameter_parameterType() {
		return bundle.getString("Parameter.parameterType");
	}

	public String getParameter_parameterUnit() {
		return bundle.getString("Parameter.parameterUnit");
	}

	public String getParameter_parameterUnitCategory() {
		return bundle.getString("Parameter.parameterUnitCategory");
	}

	public String getParameter_parameterDataType() {
		return bundle.getString("Parameter.parameterDataType");
	}

	public String getParameter_parameterSource() {
		return bundle.getString("Parameter.parameterSource");
	}

	public String getParameter_parameterSubject() {
		return bundle.getString("Parameter.parameterSubject");
	}

	public String getParameter_parameterDistribution() {
		return bundle.getString("Parameter.parameterDistribution");
	}

	public String getParameter_parameterValue() {
		return bundle.getString("Parameter.parameterValue");
	}

	public String getParameter_parameterVariabilitySubject() {
		return bundle.getString("Parameter.parameterVariabilitySubject");
	}

	public String getParameter_parameterValueMin() {
		return bundle.getString("Parameter.parameterValueMin");
	}

	public String getParameter_parameterValueMax() {
		return bundle.getString("Parameter.parameterValueMax");
	}

	public String getParameter_parameterError() {
		return bundle.getString("Parameter.parameterError");
	}

	// Model equation
	public String getModelEquation_modelEquationName() {
		return bundle.getString("ModelEquation.modelEquationName");
	}

	public String getModelEquation_modelEquationClass() {
		return bundle.getString("ModelEquation.modelEquationClass");
	}

	public String getModelEquation_modelEquation() {
		return bundle.getString("ModelEquation.modelEquation");
	}

	public String getModelEquation_hypothesisOfTheModel() {
		return bundle.getString("ModelEquation.hypothesisOfTheModel");
	}

	// Exposure
	public String getExposure_methodologicalTreatmentOfLeftCensoredData() {
		return bundle.getString("Exposure.methodologicalTreatmentOfLeftCensoredData");
	}

	public String getExposure_levelOfContaminationAfterLeftCensoredDataTreatment() {
		return bundle.getString("Exposure.levelOfContaminationAfterLeftCensoredDataTreatment");
	}

	public String getExposure_typeOfExposure() {
		return bundle.getString("Exposure.typeOfExposure");
	}

	public String getExposure_scenario() {
		return bundle.getString("Exposure.scenario");
	}

	public String getExposure_uncertaintyEstimation() {
		return bundle.getString("Exposure.uncertaintyEstimation");
	}

	// ModelMath
	public String getModelMath_qualityMeasures() {
		return bundle.getString("ModelMath.qualityMeasures");
	}

	public String getModelMath_fittingProcedure() {
		return bundle.getString("ModelMath.fittingProcedure");
	}

	public String getModelMath_event() {
		return bundle.getString("ModelMath.event");
	}

	public String getModelMath_parameter() {
		return bundle.getString("ModelMath.parameter");
	}

	public String getModelMath_modelEquation() {
		return bundle.getString("ModelMath.modelEquation");
	}

	public String getModelMath_exposure() {
		return bundle.getString("ModelMath.exposure");
	}
}
