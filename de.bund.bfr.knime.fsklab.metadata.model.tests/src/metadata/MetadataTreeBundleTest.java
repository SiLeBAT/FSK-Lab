package metadata;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

@SuppressWarnings("static-method")
public class MetadataTreeBundleTest {

	@Test
	public void test() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getGeneralInformation().isEmpty());
		assertFalse(bundle.getScope().isEmpty());
		assertFalse(bundle.getDataBackground().isEmpty());
		assertFalse(bundle.getModelMath().isEmpty());
	}

	@Test
	public void testContact() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getContact_title().isEmpty());
		assertFalse(bundle.getContact_familyName().isEmpty());
		assertFalse(bundle.getContact_givenName().isEmpty());
		assertFalse(bundle.getContact_email().isEmpty());
		assertFalse(bundle.getContact_telephone().isEmpty());
		assertFalse(bundle.getContact_streetAddress().isEmpty());
		assertFalse(bundle.getContact_country().isEmpty());
		assertFalse(bundle.getContact_city().isEmpty());
		assertFalse(bundle.getContact_zipCode().isEmpty());
		assertFalse(bundle.getContact_region().isEmpty());
		assertFalse(bundle.getContact_timeZone().isEmpty());
		assertFalse(bundle.getContact_gender().isEmpty());
		assertFalse(bundle.getContact_note().isEmpty());
		assertFalse(bundle.getContact_organization().isEmpty());
	}

	@Test
	public void testModelCategory() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getModelCategory_modelClass().isEmpty());
		assertFalse(bundle.getModelCategory_modelSubClass().isEmpty());
		assertFalse(bundle.getModelCategory_modelClassComment().isEmpty());
		assertFalse(bundle.getModelCategory_basicProcess().isEmpty());
	}

	@Test
	public void testReference() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getReference_isReferenceDescription().isEmpty());
		assertFalse(bundle.getReference_publicationType().isEmpty());
		assertFalse(bundle.getReference_publicationDate().isEmpty());
		assertFalse(bundle.getReference_pmid().isEmpty());
		assertFalse(bundle.getReference_doi().isEmpty());
		assertFalse(bundle.getReference_authorList().isEmpty());
		assertFalse(bundle.getReference_publicationTitle().isEmpty());
		assertFalse(bundle.getReference_publicationAbstract().isEmpty());
		assertFalse(bundle.getReference_publicationJournal().isEmpty());
		assertFalse(bundle.getReference_publicationIssue().isEmpty());
		assertFalse(bundle.getReference_publicationStatus().isEmpty());
		assertFalse(bundle.getReference_publicationWebsite().isEmpty());
		assertFalse(bundle.getReference_comment().isEmpty());
	}

	@Test
	public void testGeneralInformation() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getGeneralInformation_name().isEmpty());
		assertFalse(bundle.getGeneralInformation_source().isEmpty());
		assertFalse(bundle.getGeneralInformation_identifier().isEmpty());
		assertFalse(bundle.getGeneralInformation_creationDate().isEmpty());
		assertFalse(bundle.getGeneralInformation_rights().isEmpty());
		assertFalse(bundle.getGeneralInformation_available().isEmpty());
		assertFalse(bundle.getGeneralInformation_format().isEmpty());
		assertFalse(bundle.getGeneralInformation_language().isEmpty());
		assertFalse(bundle.getGeneralInformation_software().isEmpty());
		assertFalse(bundle.getGeneralInformation_languageWrittenIn().isEmpty());
		assertFalse(bundle.getGeneralInformation_status().isEmpty());
		assertFalse(bundle.getGeneralInformation_objective().isEmpty());
		assertFalse(bundle.getGeneralInformation_description().isEmpty());
		assertFalse(bundle.getGeneralInformation_author().isEmpty());
		assertFalse(bundle.getGeneralInformation_creators().isEmpty());
		assertFalse(bundle.getGeneralInformation_modelCategory().isEmpty());
		assertFalse(bundle.getGeneralInformation_references().isEmpty());
		assertFalse(bundle.getGeneralInformation_modificationDates().isEmpty());
	}

	@Test
	public void testProduct() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getProduct_productName().isEmpty());
		assertFalse(bundle.getProduct_productDescription().isEmpty());
		assertFalse(bundle.getProduct_productUnit().isEmpty());
		assertFalse(bundle.getProduct_productionMethod().isEmpty());
		assertFalse(bundle.getProduct_packaging().isEmpty());
		assertFalse(bundle.getProduct_productTreatment().isEmpty());
		assertFalse(bundle.getProduct_originCountry().isEmpty());
		assertFalse(bundle.getProduct_originArea().isEmpty());
		assertFalse(bundle.getProduct_fisheriesArea().isEmpty());
		assertFalse(bundle.getProduct_productionDate().isEmpty());
		assertFalse(bundle.getProduct_expiryDate().isEmpty());
	}

	@Test
	public void testHazard() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getHazard_hazardType().isEmpty());
		assertFalse(bundle.getHazard_hazardName().isEmpty());
		assertFalse(bundle.getHazard_hazardDescription().isEmpty());
		assertFalse(bundle.getHazard_hazardUnit().isEmpty());
		assertFalse(bundle.getHazard_adverseEffect().isEmpty());
		assertFalse(bundle.getHazard_sourceOfContamination().isEmpty());
		assertFalse(bundle.getHazard_benchmarkDose().isEmpty());
		assertFalse(bundle.getHazard_maximumResidueLimit().isEmpty());
		assertFalse(bundle.getHazard_noObservedAdverseAffectLevel().isEmpty());
		assertFalse(bundle.getHazard_acceptableDailyIntake().isEmpty());
		assertFalse(bundle.getHazard_acuteReferenceDose().isEmpty());
		assertFalse(bundle.getHazard_hazardIndSum().isEmpty());
	}

	@Test
	public void testPopulationGroup() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getPopulationGroup_populationName().isEmpty());
		assertFalse(bundle.getPopulationGroup_targetPopulation().isEmpty());
		assertFalse(bundle.getPopulationGroup_populationSpan().isEmpty());
		assertFalse(bundle.getPopulationGroup_populationDescription().isEmpty());
		assertFalse(bundle.getPopulationGroup_populationAge().isEmpty());
		assertFalse(bundle.getPopulationGroup_populationGender().isEmpty());
		assertFalse(bundle.getPopulationGroup_bmi().isEmpty());
		assertFalse(bundle.getPopulationGroup_specialDietGroups().isEmpty());
		assertFalse(bundle.getPopulationGroup_patternConsumption().isEmpty());
		assertFalse(bundle.getPopulationGroup_region().isEmpty());
		assertFalse(bundle.getPopulationGroup_country().isEmpty());
		assertFalse(bundle.getPopulationGroup_populationRiskFactor().isEmpty());
		assertFalse(bundle.getPopulationGroup_season().isEmpty());
	}

	@Test
	public void testSpatialInformation() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getSpatialInformation_region().isEmpty());
		assertFalse(bundle.getSpatialInformation_country().isEmpty());
	}

	@Test
	public void testScope() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getScope_generalComment().isEmpty());
		assertFalse(bundle.getScope_temporalInformation().isEmpty());
		assertFalse(bundle.getScope_product().isEmpty());
		assertFalse(bundle.getScope_hazard().isEmpty());
		assertFalse(bundle.getScope_populationGroup().isEmpty());
		assertFalse(bundle.getScope_spatialInformation().isEmpty());
	}

	@Test
	public void testStudy() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getStudy_studyIdentifier().isEmpty());
		assertFalse(bundle.getStudy_studyTitle().isEmpty());
		assertFalse(bundle.getStudy_studyDescription().isEmpty());
		assertFalse(bundle.getStudy_studyDesignType().isEmpty());
		assertFalse(bundle.getStudy_studyAssayMeasurementType().isEmpty());
		assertFalse(bundle.getStudy_studyAssayTechnologyType().isEmpty());
		assertFalse(bundle.getStudy_studyAssayTechnologyPlatform().isEmpty());
		assertFalse(bundle.getStudy_accreditationProcedureForTheAssayTechnology().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolName().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolType().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolDescription().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolURI().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolVersion().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolComponentsName().isEmpty());
		assertFalse(bundle.getStudy_studyProtocolComponentsType().isEmpty());
	}

	@Test
	public void testStudySample() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getStudySample_sampleName().isEmpty());
		assertFalse(bundle.getStudySample_protocolOfSampleCollection().isEmpty());
		assertFalse(bundle.getStudySample_samplingStrategy().isEmpty());
		assertFalse(bundle.getStudySample_typeOfSamplingProgram().isEmpty());
		assertFalse(bundle.getStudySample_samplingMethod().isEmpty());
		assertFalse(bundle.getStudySample_samplingPlan().isEmpty());
		assertFalse(bundle.getStudySample_samplingWeight().isEmpty());
		assertFalse(bundle.getStudySample_samplingSize().isEmpty());
		assertFalse(bundle.getStudySample_lotSizeUnit().isEmpty());
		assertFalse(bundle.getStudySample_samplingPoint().isEmpty());
	}

	@Test
	public void testDietaryAssessment() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getDietaryAssessmentMethod_collectionTool().isEmpty());
		assertFalse(bundle.getDietaryAssessmentMethod_numberOfNonConsecutiveOneDay().isEmpty());
		assertFalse(bundle.getDietaryAssessmentMethod_softwareTool().isEmpty());
		assertFalse(bundle.getDietaryAssessmentMethod_numberOfFoodItems().isEmpty());
		assertFalse(bundle.getDietaryAssessmentMethod_recordTypes().isEmpty());
		assertFalse(bundle.getDietaryAssessmentMethod_foodDescriptors().isEmpty());
	}

	@Test
	public void testLaboratory() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getLaboratory_laboratoryAccreditation().isEmpty());
		assertFalse(bundle.getLaboratory_laboratoryName().isEmpty());
		assertFalse(bundle.getLaboratory_laboratoryCountry().isEmpty());
	}

	@Test
	public void testAssay() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getAssay_assayName().isEmpty());
		assertFalse(bundle.getAssay_assayDescription().isEmpty());
		assertFalse(bundle.getAssay_percentageOfMoisture().isEmpty());
		assertFalse(bundle.getAssay_percentageOfFat().isEmpty());
		assertFalse(bundle.getAssay_limitOfDetection().isEmpty());
		assertFalse(bundle.getAssay_limitOfQuantification().isEmpty());
		assertFalse(bundle.getAssay_leftCensoredData().isEmpty());
		assertFalse(bundle.getAssay_rangeOfContamination().isEmpty());
		assertFalse(bundle.getAssay_uncertaintyValue().isEmpty());
	}

	@Test
	public void testDataBackground() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getDataBackground_study().isEmpty());
		assertFalse(bundle.getDataBackground_studySample().isEmpty());
		assertFalse(bundle.getDataBackground_dietaryAssessmentMethod().isEmpty());
		assertFalse(bundle.getDataBackground_laboratory().isEmpty());
		assertFalse(bundle.getDataBackground_assay().isEmpty());
	}

	@Test
	public void testParameter() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getParameter_parameterId().isEmpty());
		assertFalse(bundle.getParameter_parameterClassification().isEmpty());
		assertFalse(bundle.getParameter_parameterName().isEmpty());
		assertFalse(bundle.getParameter_parameterDescription().isEmpty());
		assertFalse(bundle.getParameter_parameterType().isEmpty());
		assertFalse(bundle.getParameter_parameterUnit().isEmpty());
		assertFalse(bundle.getParameter_parameterUnitCategory().isEmpty());
		assertFalse(bundle.getParameter_parameterDataType().isEmpty());
		assertFalse(bundle.getParameter_parameterSource().isEmpty());
		assertFalse(bundle.getParameter_parameterSubject().isEmpty());
		assertFalse(bundle.getParameter_parameterDistribution().isEmpty());
		assertFalse(bundle.getParameter_parameterValue().isEmpty());
		assertFalse(bundle.getParameter_parameterVariabilitySubject().isEmpty());
		assertFalse(bundle.getParameter_parameterValueMin().isEmpty());
		assertFalse(bundle.getParameter_parameterValueMax().isEmpty());
		assertFalse(bundle.getParameter_parameterError().isEmpty());
	}

	@Test
	public void testModelEquation() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getModelEquation_modelEquationName().isEmpty());
		assertFalse(bundle.getModelEquation_modelEquationClass().isEmpty());
		assertFalse(bundle.getModelEquation_modelEquation().isEmpty());
		assertFalse(bundle.getModelEquation_hypothesisOfTheModel().isEmpty());
	}

	@Test
	public void testExposure() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getExposure_methodologicalTreatmentOfLeftCensoredData().isEmpty());
		assertFalse(bundle.getExposure_levelOfContaminationAfterLeftCensoredDataTreatment().isEmpty());
		assertFalse(bundle.getExposure_typeOfExposure().isEmpty());
		assertFalse(bundle.getExposure_scenario().isEmpty());
		assertFalse(bundle.getExposure_uncertaintyEstimation().isEmpty());
	}

	@Test
	public void testModelMath() {
		MetadataTreeBundle bundle = new MetadataTreeBundle();

		assertFalse(bundle.getModelMath_qualityMeasures().isEmpty());
		assertFalse(bundle.getModelMath_fittingProcedure().isEmpty());
		assertFalse(bundle.getModelMath_event().isEmpty());
		assertFalse(bundle.getModelMath_parameter().isEmpty());
		assertFalse(bundle.getModelMath_modelEquation().isEmpty());
		assertFalse(bundle.getModelMath_exposure().isEmpty());
	}
}
