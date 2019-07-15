package metadata;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.Date;

import org.junit.Test;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.ConsumptionModelScope;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DataModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DataModelModelMath;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DoseResponseModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModelScope;
import de.bund.bfr.metadata.swagger.Exposure;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.OtherModelDataBackground;
import de.bund.bfr.metadata.swagger.OtherModelGeneralInformation;
import de.bund.bfr.metadata.swagger.OtherModelModelMath;
import de.bund.bfr.metadata.swagger.OtherModelScope;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.PredictiveModelScope;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.ProcessModelScope;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModelScope;

public class SwaggerUtilTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testConvertGeneralInformation() {

		GenericModelGeneralInformation gi = new GenericModelGeneralInformation();
		{
			metadata.GeneralInformation deprecated = metadata.MetadataFactory.eINSTANCE.createGeneralInformation();

			deprecated.setName("name");
			deprecated.setSource("source");
			deprecated.setIdentifier("identifier");
			deprecated.setCreationDate(new Date(2018, 0, 1));
			deprecated.setRights("rights");
			deprecated.setAvailable(true);
			deprecated.setFormat("format");
			deprecated.setLanguage("language");
			deprecated.setSoftware("software");
			deprecated.setLanguageWrittenIn("languageWrittenIn");
			deprecated.setStatus("status");
			deprecated.setObjective("objective");
			deprecated.setDescription("description");
			deprecated.getModelCategory().add(metadata.MetadataFactory.eINSTANCE.createModelCategory());

			metadata.ModificationDate md = metadata.MetadataFactory.eINSTANCE.createModificationDate();
			md.setValue(new Date(2018, 0, 1));
			deprecated.getModificationdate().add(md);

			deprecated.setAuthor(metadata.MetadataFactory.eINSTANCE.createContact());
			deprecated.getCreators().add(metadata.MetadataFactory.eINSTANCE.createContact());
			deprecated.getReference().add(metadata.MetadataFactory.eINSTANCE.createReference());

			gi = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", gi.getName());
		assertEquals("source", gi.getSource());
		assertEquals("identifier", gi.getIdentifier());
		assertEquals(1, gi.getAuthor().size());
		assertEquals(1, gi.getCreator().size());
		assertEquals(LocalDate.of(2018, 1, 1), gi.getCreationDate());
		assertEquals(LocalDate.of(2018, 1, 1), gi.getModificationDate().get(0));
		assertEquals("rights", gi.getRights());
		assertEquals("true", gi.getAvailability());
		assertNull(gi.getUrl());
		assertEquals("format", gi.getFormat());
		assertEquals(1, gi.getReference().size());
		assertEquals("language", gi.getLanguage());
		assertEquals("software", gi.getSoftware());
		assertEquals("languageWrittenIn", gi.getLanguageWrittenIn());
		assertNotNull(gi.getModelCategory());
		assertEquals("status", gi.getStatus());
		assertEquals("objective", gi.getObjective());
		assertEquals("description", gi.getDescription());
	}

	@Test
	public void testConvertModelCategory() {
		ModelCategory modelCategory;
		{
			metadata.ModelCategory deprecated = metadata.MetadataFactory.eINSTANCE.createModelCategory();
			deprecated.setModelClass("modelClass");
			deprecated.getModelSubClass().add(createStringObject("subClass"));
			deprecated.setModelClassComment("classComment");
			deprecated.setBasicProcess("basicProcess");

			modelCategory = SwaggerUtil.convert(deprecated);
		}

		assertEquals("modelClass", modelCategory.getModelClass());
		assertEquals("subClass", modelCategory.getModelSubClass().get(0));
		assertEquals("classComment", modelCategory.getModelClassComment());
		assertEquals("basicProcess", modelCategory.getBasicProcess().get(0));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testConvertReference() {

		Reference reference;
		{
			metadata.Reference deprecated = metadata.MetadataFactory.eINSTANCE.createReference();
			deprecated.setIsReferenceDescription(false);
			deprecated.setPublicationType(metadata.PublicationType.RPRT);
			deprecated.setPublicationDate(new Date(2018, 0, 1));
			deprecated.setPmid("pmid");
			deprecated.setDoi("10.2903/j.efsa.2018.5134");
			deprecated.setAuthorList("Miguel");
			deprecated.setPublicationTitle("Listeria monocytogenes");
			deprecated.setPublicationAbstract("abstract");
			deprecated.setPublicationJournal("journal");
			deprecated.setPublicationVolume(7);
			deprecated.setPublicationIssue(1);
			deprecated.setPublicationStatus("Published");
			deprecated.setPublicationWebsite("www.efsa.europa.eu");

			reference = SwaggerUtil.convert(deprecated);
		}

		assertFalse(reference.isIsReferenceDescription());
		assertEquals(PublicationTypeEnum.RPRT, reference.getPublicationType());
		assertEquals(LocalDate.of(2018, 1, 1), reference.getDate());
		assertEquals("pmid", reference.getPmid());
		assertEquals("10.2903/j.efsa.2018.5134", reference.getDoi());
		assertEquals("Listeria monocytogenes", reference.getTitle());
		assertEquals("abstract", reference.getAbstract());
		assertEquals("7", reference.getVolume());
		assertEquals("1", reference.getIssue());
		assertEquals("Published", reference.getStatus());
		assertEquals("www.efsa.europa.eu", reference.getWebsite());
	}

	@Test
	public void testConvertContact() {

		Contact contact = new Contact();
		{
			metadata.Contact deprecated = metadata.MetadataFactory.eINSTANCE.createContact();
			deprecated.setTitle("title");
			deprecated.setFamilyName("familyName");
			deprecated.setGivenName("givenName");
			deprecated.setEmail("john@doe.com");
			deprecated.setTelephone("0123456789");
			deprecated.setStreetAddress("streetAddress");
			deprecated.setCountry("country");
			deprecated.setCity("city");
			deprecated.setZipCode("12345");
			deprecated.setRegion("region");
			deprecated.setTimeZone("timeZone");
			deprecated.setGender("gender");
			deprecated.setNote("note");
			deprecated.setOrganization("organization");

			contact = SwaggerUtil.convert(deprecated);
		}

		assertEquals("title", contact.getTitle());
		assertEquals("familyName", contact.getFamilyName());
		assertEquals("givenName", contact.getGivenName());
		assertEquals("john@doe.com", contact.getEmail());
		assertEquals("0123456789", contact.getTelephone());
		assertEquals("streetAddress", contact.getStreetAddress());
		assertEquals("country", contact.getCountry());
		assertEquals("12345", contact.getZipCode());
		assertEquals("region", contact.getRegion());
		assertEquals("timeZone", contact.getTimeZone());
		assertEquals("gender", contact.getGender());
		assertEquals("note", contact.getNote());
		assertEquals("organization", contact.getOrganization());
	}

	@Test
	public void testConvertParameter() {

		Parameter param;
		{
			metadata.Parameter deprecated = metadata.MetadataFactory.eINSTANCE.createParameter();
			deprecated.setParameterID("id");
			deprecated.setParameterClassification(metadata.ParameterClassification.CONSTANT);
			deprecated.setParameterName("name");
			deprecated.setParameterDescription("description");
			deprecated.setParameterType("type");
			deprecated.setParameterUnit("unit");
			deprecated.setParameterUnitCategory("unitCategory");
			deprecated.setParameterDataType(metadata.ParameterType.BOOLEAN);
			deprecated.setParameterSource("source");
			deprecated.setParameterSubject("subject");
			deprecated.setParameterDistribution("distribution");
			deprecated.setParameterValue("value");
			deprecated.setParameterVariabilitySubject("subject");
			deprecated.setParameterValueMin("false");
			deprecated.setParameterValueMax("true");
			deprecated.setParameterError("2.718");
			deprecated.setReference(metadata.MetadataFactory.eINSTANCE.createReference());

			param = SwaggerUtil.convert(deprecated);
		}

		assertEquals("id", param.getId());
		assertEquals(Parameter.ClassificationEnum.CONSTANT, param.getClassification());
		assertEquals("name", param.getName());
		assertEquals("description", param.getDescription());
		assertEquals("unit", param.getUnit());
		assertEquals("unitCategory", param.getUnitCategory());
		assertEquals(Parameter.DataTypeEnum.BOOLEAN, param.getDataType());
		assertEquals("source", param.getSource());
		assertEquals("subject", param.getSubject());
		assertEquals("distribution", param.getDistribution());
		assertEquals("value", param.getValue());
		assertEquals("subject", param.getVariabilitySubject());
		assertEquals("false", param.getMinValue());
		assertEquals("true", param.getMaxValue());
		assertEquals("2.718", param.getError());
		assertNotNull(param.getReference());
	}

	@Test
	public void testConvertScope() {

		GenericModelScope scope = new GenericModelScope();
		{
			metadata.Scope deprecated = metadata.MetadataFactory.eINSTANCE.createScope();
			deprecated.getProduct().add(metadata.MetadataFactory.eINSTANCE.createProduct());
			deprecated.getHazard().add(metadata.MetadataFactory.eINSTANCE.createHazard());
			deprecated.getPopulationGroup().add(metadata.MetadataFactory.eINSTANCE.createPopulationGroup());
			deprecated.setGeneralComment("comment");
			deprecated.setTemporalInformation("information");
			deprecated.setSpatialInformation(metadata.MetadataFactory.eINSTANCE.createSpatialInformation());

			scope = SwaggerUtil.convert(deprecated);
		}

		assertEquals(1, scope.getProduct().size());
		assertEquals(1, scope.getHazard().size());
		assertEquals(1, scope.getPopulationGroup().size());
		assertEquals("comment", scope.getGeneralComment());
		assertEquals("information", scope.getTemporalInformation());
		// FIXME: there is an error in spatial information
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testConvertProduct() {

		Product product;
		{
			metadata.Product deprecated = metadata.MetadataFactory.eINSTANCE.createProduct();
			deprecated.setProductName("name");
			deprecated.setProductDescription("description");
			deprecated.setProductUnit("unit");
			deprecated.setProductionMethod("method");
			deprecated.setPackaging("packaging");
			deprecated.setProductTreatment("treatment");
			deprecated.setOriginCountry("originCountry");
			deprecated.setOriginArea("originArea");
			deprecated.setFisheriesArea("fisheriesArea");
			deprecated.setProductionDate(new Date(2018, 0, 1));
			deprecated.setExpiryDate(new Date(2019, 0, 1));

			product = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", product.getName());
		assertEquals("description", product.getDescription());
		assertEquals("unit", product.getUnit());
		assertEquals("method", product.getMethod().get(0));
		assertEquals("packaging", product.getPackaging().get(0));
		assertEquals("treatment", product.getTreatment().get(0));
		assertEquals("originCountry", product.getOriginCountry());
		assertEquals("originArea", product.getOriginArea());
		assertEquals("fisheriesArea", product.getFisheriesArea());
		assertEquals(LocalDate.of(2018, 1, 1), product.getProductionDate());
		assertEquals(LocalDate.of(2019, 1, 1), product.getExpiryDate());
	}

	@Test
	public void testConvertHazard() {

		Hazard hazard;
		{
			metadata.Hazard deprecated = metadata.MetadataFactory.eINSTANCE.createHazard();
			deprecated.setHazardType("type");
			deprecated.setHazardName("name");
			deprecated.setHazardDescription("description");
			deprecated.setHazardUnit("unit");
			deprecated.setAdverseEffect("adverseEffect");
			deprecated.setSourceOfContamination("sourceOfContamination");
			deprecated.setBenchmarkDose("benchmarkDose");
			deprecated.setMaximumResidueLimit("maximumResidueLimit");
			deprecated.setNoObservedAdverseAffectLevel("level");
			deprecated.setLowestObservedAdverseAffectLevel("level");
			deprecated.setAcceptableOperatorExposureLevel("level");
			deprecated.setAcuteReferenceDose("dose");
			deprecated.setAcceptableDailyIntake("intake");
			deprecated.setHazardIndSum("sum");

			hazard = SwaggerUtil.convert(deprecated);
		}

		assertEquals("type", hazard.getType());
		assertEquals("name", hazard.getName());
		assertEquals("description", hazard.getDescription());
		assertEquals("unit", hazard.getUnit());
		assertEquals("adverseEffect", hazard.getAdverseEffect());
		assertEquals("sourceOfContamination", hazard.getSourceOfContamination());
		assertEquals("benchmarkDose", hazard.getBenchmarkDose());
		assertEquals("maximumResidueLimit", hazard.getMaximumResidueLimit());
		assertEquals("level", hazard.getNoObservedAdverseAffectLevel());
		assertEquals("level", hazard.getLowestObservedAdverseAffectLevel());
		assertEquals("level", hazard.getAcceptableOperatorsExposureLevel());
		assertEquals("dose", hazard.getAcuteReferenceDose());
		assertEquals("intake", hazard.getAcceptableDailyIntake());
		assertEquals("sum", hazard.getIndSum());
	}

	@Test
	public void testConvertPopulationGroup() {

		PopulationGroup pg;
		{
			metadata.PopulationGroup deprecated = metadata.MetadataFactory.eINSTANCE.createPopulationGroup();
			deprecated.setPopulationName("name");
			deprecated.setTargetPopulation("population");
			deprecated.getPopulationSpan().add(createStringObject("span"));
			deprecated.getPopulationDescription().add(createStringObject("description"));
			deprecated.getBmi().add(createStringObject("bmi"));
			deprecated.getSpecialDietGroups().add(createStringObject("group"));
			deprecated.getRegion().add(createStringObject("region"));
			deprecated.getCountry().add(createStringObject("country"));
			deprecated.getPopulationRiskFactor().add(createStringObject("factor"));
			deprecated.getSeason().add(createStringObject("season"));
			deprecated.setPopulationGender("gender");
			deprecated.getPatternConsumption().add(createStringObject("consumption"));
			deprecated.getPopulationAge().add(createStringObject("age"));

			pg = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", pg.getName());
		assertEquals("population", pg.getTargetPopulation());
		assertEquals("span", pg.getPopulationSpan().get(0));
		assertEquals("description", pg.getPopulationDescription().get(0));
		assertEquals("age", pg.getPopulationAge().get(0));
		assertEquals("gender", pg.getPopulationGender());
		assertEquals("bmi", pg.getBmi().get(0));
		assertEquals("group", pg.getSpecialDietGroups().get(0));
		assertEquals("consumption", pg.getPatternConsumption().get(0));
		assertEquals("region", pg.getRegion().get(0));
		assertEquals("country", pg.getCountry().get(0));
		assertEquals("factor", pg.getPopulationRiskFactor().get(0));
		assertEquals("season", pg.getSeason().get(0));
	}

	@Test
	public void testConvertAssay() {

		Assay assay;
		{
			metadata.Assay deprecated = metadata.MetadataFactory.eINSTANCE.createAssay();
			deprecated.setAssayName("name");
			deprecated.setAssayDescription("description");
			deprecated.setPercentageOfMoisture("0.5");
			deprecated.setPercentageOfFat("0.3");
			deprecated.setLimitOfDetection("detection");
			deprecated.setLimitOfQuantification("quantification");
			deprecated.setLeftCensoredData("data");
			deprecated.setRangeOfContamination("contamination");
			deprecated.setUncertaintyValue("value");

			assay = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", assay.getName());
		assertEquals("description", assay.getDescription());
		assertEquals("0.5", assay.getMoisturePercentage());
		assertEquals("0.3", assay.getFatPercentage());
		assertEquals("detection", assay.getDetectionLimit());
		assertEquals("quantification", assay.getQuantificationLimit());
		assertEquals("data", assay.getLeftCensoredData());
		assertEquals("contamination", assay.getContaminationRange());
		assertEquals("value", assay.getUncertaintyValue());
	}

	@Test
	public void testConvertDietaryAssessmentMethod() {

		DietaryAssessmentMethod method;
		{
			metadata.DietaryAssessmentMethod deprecated = metadata.MetadataFactory.eINSTANCE
					.createDietaryAssessmentMethod();
			deprecated.setCollectionTool("tool");
			deprecated.setNumberOfNonConsecutiveOneDay(0);
			deprecated.setSoftwareTool("tool");
			deprecated.setNumberOfFoodItems("items");
			deprecated.setRecordTypes("types");
			deprecated.setFoodDescriptors("descriptors");

			method = SwaggerUtil.convert(deprecated);
		}

		assertEquals("tool", method.getCollectionTool());
		assertEquals("0", method.getNumberOfNonConsecutiveOneDay());
		assertEquals("tool", method.getSoftwareTool());
		assertEquals("items", method.getNumberOfFoodItems().get(0));
		assertEquals("types", method.getRecordTypes().get(0));
		assertEquals("descriptors", method.getFoodDescriptors().get(0));
	}

	@Test
	public void testConvertStudy() {

		Study study;
		{
			metadata.Study deprecated = metadata.MetadataFactory.eINSTANCE.createStudy();
			deprecated.setStudyIdentifier("identifier");
			deprecated.setStudyTitle("title");
			deprecated.setStudyDescription("description");
			deprecated.setStudyDesignType("type");
			deprecated.setStudyAssayMeasurementType("type");
			deprecated.setStudyAssayTechnologyType("type");
			deprecated.setStudyAssayTechnologyPlatform("platform");
			deprecated.setAccreditationProcedureForTheAssayTechnology("technology");
			deprecated.setStudyProtocolName("name");
			deprecated.setStudyProtocolType("type");
			deprecated.setStudyProtocolDescription("description");
			deprecated.setStudyProtocolURI(URI.create("https://bfr.bund.de"));
			deprecated.setStudyProtocolVersion("1.0");
			deprecated.setStudyProtocolParametersName("name");
			deprecated.setStudyProtocolComponentsName("name");
			deprecated.setStudyProtocolComponentsType("type");

			study = SwaggerUtil.convert(deprecated);
		}

		assertEquals("identifier", study.getIdentifier());
		assertEquals("title", study.getTitle());
		assertEquals("description", study.getDescription());
		assertEquals("type", study.getDesignType());
		assertEquals("type", study.getAssayMeasurementType());
		assertEquals("type", study.getAssayTechnologyType());
		assertEquals("platform", study.getAssayTechnologyPlatform());
		assertEquals("technology", study.getAccreditationProcedureForTheAssayTechnology());
		assertEquals("name", study.getProtocolName());
		assertEquals("type", study.getProtocolType());
		assertEquals("description", study.getProtocolDescription());
		assertEquals("https://bfr.bund.de", study.getProtocolURI());
		assertEquals("1.0", study.getProtocolVersion());
		assertEquals("name", study.getProtocolParametersName());
		assertEquals("name", study.getProtocolComponentsName());
		assertEquals("type", study.getProtocolComponentsType());
	}

	@Test
	public void testConvertStudySample() {

		StudySample sample;
		{
			metadata.StudySample deprecated = metadata.MetadataFactory.eINSTANCE.createStudySample();
			deprecated.setSampleName("name");
			deprecated.setProtocolOfSampleCollection("collection");
			deprecated.setSamplingStrategy("strategy");
			deprecated.setTypeOfSamplingProgram("program");
			deprecated.setSamplingMethod("method");
			deprecated.setSamplingPlan("plan");
			deprecated.setSamplingWeight("weight");
			deprecated.setSamplingSize("size");
			deprecated.setLotSizeUnit("unit");
			deprecated.setSamplingPoint("point");

			sample = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", sample.getSampleName());
		assertEquals("collection", sample.getProtocolOfSampleCollection());
		assertEquals("strategy", sample.getSamplingStrategy());
		assertEquals("program", sample.getTypeOfSamplingProgram());
		assertEquals("method", sample.getSamplingMethod());
		assertEquals("plan", sample.getSamplingPlan());
		assertEquals("weight", sample.getSamplingWeight());
		assertEquals("size", sample.getSamplingSize());
		assertEquals("unit", sample.getLotSizeUnit());
		assertEquals("point", sample.getSamplingPoint());
	}

	@Test
	public void testConvertLaboratory() {

		Laboratory laboratory;
		{
			metadata.Laboratory deprecated = metadata.MetadataFactory.eINSTANCE.createLaboratory();
			deprecated.setLaboratoryName("name");
			deprecated.setLaboratoryCountry("country");
			deprecated.getLaboratoryAccreditation().add(createStringObject("accreditation"));

			laboratory = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", laboratory.getName());
		assertEquals("country", laboratory.getCountry());
		assertEquals("accreditation", laboratory.getAccreditation().get(0));
	}

	@Test
	public void testConvertDataBackground() {

		GenericModelDataBackground background;
		{
			metadata.DataBackground deprecated = metadata.MetadataFactory.eINSTANCE.createDataBackground();
			deprecated.setStudy(metadata.MetadataFactory.eINSTANCE.createStudy());
			deprecated.getStudySample().add(metadata.MetadataFactory.eINSTANCE.createStudySample());
			deprecated.getDietaryAssessmentMethod()
					.add(metadata.MetadataFactory.eINSTANCE.createDietaryAssessmentMethod());
			deprecated.getLaboratory().add(metadata.MetadataFactory.eINSTANCE.createLaboratory());
			deprecated.getAssay().add(metadata.MetadataFactory.eINSTANCE.createAssay());

			background = SwaggerUtil.convert(deprecated);
		}

		assertNotNull(background.getStudy());
		assertEquals(1, background.getStudySample().size());
		assertEquals(1, background.getDietaryAssessmentMethod().size());
		assertEquals(1, background.getLaboratory().size());
		assertEquals(1, background.getAssay().size());
	}

	@Test
	public void testConvertModelEquation() {

		ModelEquation equation;
		{
			metadata.ModelEquation deprecated = metadata.MetadataFactory.eINSTANCE.createModelEquation();
			deprecated.setModelEquationName("name");
			deprecated.setModelEquationClass("class");
			deprecated.setModelEquation("equation");
			deprecated.getReference().add(metadata.MetadataFactory.eINSTANCE.createReference());
			deprecated.getHypothesisOfTheModel().add(createStringObject("hypothesis"));

			equation = SwaggerUtil.convert(deprecated);
		}

		assertEquals("name", equation.getName());
		assertEquals("class", equation.getPropertyClass());
		assertEquals("equation", equation.getModelEquation());
		assertEquals(1, equation.getReference().size());
		assertEquals("hypothesis", equation.getModelHypothesis().get(0));
	}

	@Test
	public void testConvertExposure() {

		Exposure exposure;
		{
			metadata.Exposure deprecated = metadata.MetadataFactory.eINSTANCE.createExposure();
			deprecated.setTypeOfExposure("exposure");
			deprecated.setUncertaintyEstimation("estimation");
			deprecated.getMethodologicalTreatmentOfLeftCensoredData().add(createStringObject("data"));
			deprecated.getLevelOfContaminationAfterLeftCensoredDataTreatment().add(createStringObject("treatment"));
			deprecated.getScenario().add(createStringObject("scenario"));

			exposure = SwaggerUtil.convert(deprecated);
		}

		assertEquals("exposure", exposure.getType());
		assertEquals("estimation", exposure.getUncertaintyEstimation());
		assertEquals("data", exposure.getTreatment().get(0));
		assertEquals("treatment", exposure.getContamination().get(0));
		assertEquals("scenario", exposure.getScenario().get(0));
	}

	@Test
	public void testConvertModelMath() {

		GenericModelModelMath math;
		{
			metadata.ModelMath deprecated = metadata.MetadataFactory.eINSTANCE.createModelMath();
			deprecated.setFittingProcedure("procedure");
			deprecated.getParameter().add(metadata.MetadataFactory.eINSTANCE.createParameter());
			deprecated.getModelEquation().add(metadata.MetadataFactory.eINSTANCE.createModelEquation());
			deprecated.setExposure(metadata.MetadataFactory.eINSTANCE.createExposure());
			deprecated.getEvent().add(createStringObject("event"));

			math = SwaggerUtil.convert(deprecated);
		}

		assertEquals(1, math.getParameter().size());
		assertNull(math.getQualityMeasures());
		assertEquals(1, math.getModelEquation().size());
		assertEquals("procedure", math.getFittingProcedure());
		assertEquals(1, math.getExposure().size());
		assertEquals("event", math.getEvent().get(0));
	}

	private static StringObject createStringObject(String string) {
		StringObject so = metadata.MetadataFactory.eINSTANCE.createStringObject();
		so.setValue(string);

		return so;
	}

	@Test
	public void testGetGeneralInformation() {

		assertThat(SwaggerUtil.getGeneralInformation(createGenericModel()), instanceOf(GenericModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createDataModel()), instanceOf(DataModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createPredictiveModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createOtherModel()), instanceOf(OtherModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createExposureModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createToxicologicalModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createDoseResponseModel()), instanceOf(DoseResponseModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createProcessModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createConsumptionModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createRiskModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createQraModel()), instanceOf(PredictiveModelGeneralInformation.class));
	}

	@Test
	public void testGetScope() {
		assertThat(SwaggerUtil.getScope(createGenericModel()), instanceOf(GenericModelScope.class));
		assertThat(SwaggerUtil.getScope(createDataModel()), instanceOf(GenericModelScope.class));
		assertThat(SwaggerUtil.getScope(createPredictiveModel()), instanceOf(PredictiveModelScope.class));
		assertThat(SwaggerUtil.getScope(createOtherModel()), instanceOf(OtherModelScope.class));
		assertThat(SwaggerUtil.getScope(createExposureModel()), instanceOf(ExposureModelScope.class));
		assertThat(SwaggerUtil.getScope(createToxicologicalModel()), instanceOf(ToxicologicalModelScope.class));
		assertThat(SwaggerUtil.getScope(createDoseResponseModel()), instanceOf(DoseResponseModelScope.class));
		assertThat(SwaggerUtil.getScope(createProcessModel()), instanceOf(ProcessModelScope.class));
		assertThat(SwaggerUtil.getScope(createConsumptionModel()), instanceOf(ConsumptionModelScope.class));
		assertThat(SwaggerUtil.getScope(createRiskModel()),  instanceOf(ExposureModelScope.class));
		assertThat(SwaggerUtil.getScope(createQraModel()), instanceOf(ExposureModelScope.class));
	}

	@Test
	public void testGetDataBackground() {
		assertThat(SwaggerUtil.getDataBackground(createGenericModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createDataModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createPredictiveModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createOtherModel()), instanceOf(OtherModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createExposureModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createToxicologicalModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createDoseResponseModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createProcessModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createConsumptionModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createRiskModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createQraModel()), instanceOf(GenericModelDataBackground.class));
	}

	@Test
	public void testGetModelMath() {
		assertThat(SwaggerUtil.getModelMath(createGenericModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createDataModel()), instanceOf(DataModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createPredictiveModel()), instanceOf(PredictiveModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createOtherModel()), instanceOf(OtherModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createExposureModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createToxicologicalModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createDoseResponseModel()), instanceOf(DoseResponseModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createProcessModel()), instanceOf(PredictiveModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createConsumptionModel()), instanceOf(PredictiveModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createRiskModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createQraModel()), instanceOf(GenericModelModelMath.class));
	}
	
	@Test
	public void testGetLanguageWrittenIn() {
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createGenericModel()));
		assertNull(SwaggerUtil.getLanguageWrittenIn(createDataModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createPredictiveModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createOtherModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createExposureModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createToxicologicalModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createDoseResponseModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createProcessModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createConsumptionModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createRiskModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createQraModel()));
	}
	
	@Test
	public void testGetModelName() {
		assertEquals("name", SwaggerUtil.getModelName(createGenericModel()));
		assertEquals("name", SwaggerUtil.getModelName(createDataModel()));
		assertEquals("name", SwaggerUtil.getModelName(createPredictiveModel()));
		assertEquals("name", SwaggerUtil.getModelName(createOtherModel()));
		assertEquals("name", SwaggerUtil.getModelName(createExposureModel()));
		assertEquals("name", SwaggerUtil.getModelName(createToxicologicalModel()));
		assertEquals("name", SwaggerUtil.getModelName(createDoseResponseModel()));
		assertEquals("name", SwaggerUtil.getModelName(createProcessModel()));
		assertEquals("name", SwaggerUtil.getModelName(createConsumptionModel()));
		assertEquals("name", SwaggerUtil.getModelName(createRiskModel()));
		assertEquals("name", SwaggerUtil.getModelName(createQraModel()));
	}

	private static GenericModel createGenericModel() {
		GenericModel model = new GenericModel();
		model.setModelType("genericModel");
		
		GenericModelGeneralInformation information = new GenericModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		
		model.setGeneralInformation(information);
		model.setScope(new GenericModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());

		return model;
	}

	private static DataModel createDataModel() {
		DataModel model = new DataModel();
		model.setModelType("dataModel");

		DataModelGeneralInformation information = new DataModelGeneralInformation();
		information.setName("name");
		model.setGeneralInformation(information);

		model.setScope(new GenericModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new DataModelModelMath());
		
		return model;
	}
	
	private static PredictiveModel createPredictiveModel() {
		PredictiveModel model = new PredictiveModel();
		model.setModelType("predictiveModel");
		
		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new PredictiveModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new PredictiveModelModelMath());
		
		return model;
	}
	
	private static OtherModel createOtherModel() {
		OtherModel model = new OtherModel();
		model.setModelType("otherModel");
		
		OtherModelGeneralInformation information = new OtherModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new OtherModelScope());
		model.setDataBackground(new OtherModelDataBackground());
		model.setModelMath(new OtherModelModelMath());
		
		return model;
	}
	
	private static ExposureModel createExposureModel() {
		ExposureModel model = new ExposureModel();
		model.setModelType("exposureModel");

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ExposureModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());
		
		return model;
	}
	
	private static ToxicologicalModel createToxicologicalModel() {
		ToxicologicalModel model = new ToxicologicalModel();
		model.setModelType("toxicologicalModel");

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ToxicologicalModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new GenericModelModelMath());
		
		return model;
	}
	
	private static DoseResponseModel createDoseResponseModel() {
		DoseResponseModel model = new DoseResponseModel();
		model.setModelType("doseResponseModel");

		DoseResponseModelGeneralInformation information = new DoseResponseModelGeneralInformation();
		information.setModelName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new DoseResponseModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new DoseResponseModelModelMath());
		
		return model;
	}

	private static ProcessModel createProcessModel() {
		ProcessModel model = new ProcessModel();
		model.setModelType("processModel");

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ProcessModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new PredictiveModelModelMath());
		
		return model;
	}

	private static ConsumptionModel createConsumptionModel() {
		ConsumptionModel model = new ConsumptionModel();
		model.setModelType("consumptionModel");
		
		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);
		
		model.setScope(new ConsumptionModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new PredictiveModelModelMath());
		
		return model;
	}

	private static RiskModel createRiskModel() {
		RiskModel model = new RiskModel();
		model.setModelType("riskModel");

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ExposureModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());
		
		return model;
	}

	private static QraModel createQraModel() {
		QraModel model = new QraModel();
		model.setModelType("qraModel");

		PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ExposureModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());
		
		return model;
	}
}
