package metadata;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.emfjson.jackson.module.EMFModule;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.Exposure;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

// FIXME: Fix Jackson issues on KNIME 4.4
@Ignore("Broken on KNIME 4.4")
@SuppressWarnings("static-method")
public class EmfMetadataModuleTest {

	private static ObjectMapper MAPPER;

	private static metadata.Contact OLD_CONTACT;
	private static metadata.Reference OLD_REFERENCE;
	private static metadata.ModelCategory OLD_CATEGORY;
	private static metadata.Product OLD_PRODUCT;
	private static metadata.Hazard OLD_HAZARD;
	private static metadata.PopulationGroup OLD_GROUP;
	private static metadata.Assay OLD_ASSAY;
	private static metadata.Laboratory OLD_LABORATORY;
	private static metadata.StudySample OLD_SAMPLE;
	private static metadata.DietaryAssessmentMethod OLD_METHOD;
	private static metadata.Study OLD_STUDY;
	private static metadata.Parameter OLD_PARAMETER;
	private static metadata.ModelEquation OLD_EQUATION;
	private static metadata.Exposure OLD_EXPOSURE;

	@BeforeClass
	public static void setUp() {
		MAPPER = EMFModule.setupDefaultMapper().registerModule(new ThreeTenModule())
				.registerModule(new EmfMetadataModule());

		OLD_CONTACT = metadata.MetadataFactory.eINSTANCE.createContact();
		OLD_CONTACT.setTitle("title");
		OLD_CONTACT.setFamilyName("familyName");
		OLD_CONTACT.setGivenName("givenName");
		OLD_CONTACT.setEmail("john@doe.com");
		OLD_CONTACT.setTelephone("0123456789");
		OLD_CONTACT.setStreetAddress("streetAddress");
		OLD_CONTACT.setCountry("country");
		OLD_CONTACT.setCity("city");
		OLD_CONTACT.setZipCode("12345");
		OLD_CONTACT.setRegion("region");
		OLD_CONTACT.setTimeZone("timeZone");
		OLD_CONTACT.setGender("gender");
		OLD_CONTACT.setNote("note");
		OLD_CONTACT.setOrganization("organization");

		OLD_REFERENCE = metadata.MetadataFactory.eINSTANCE.createReference();
		OLD_REFERENCE.setIsReferenceDescription(true);
		OLD_REFERENCE.setPublicationType(metadata.PublicationType.RPRT);
		OLD_REFERENCE.setPublicationDate(createDate(2018, 0, 1));
		OLD_REFERENCE.setPmid("pmid");
		OLD_REFERENCE.setDoi("10.2903/j.efsa.2018.5134");
		OLD_REFERENCE.setAuthorList("Miguel");
		OLD_REFERENCE.setPublicationTitle("Listeria monocytogenes");
		OLD_REFERENCE.setPublicationAbstract("abstract");
		OLD_REFERENCE.setPublicationJournal("journal");
		OLD_REFERENCE.setPublicationVolume(7);
		OLD_REFERENCE.setPublicationIssue(1);
		OLD_REFERENCE.setPublicationStatus("Published");
		OLD_REFERENCE.setPublicationWebsite("www.efsa.europa.eu");

		OLD_CATEGORY = metadata.MetadataFactory.eINSTANCE.createModelCategory();
		OLD_CATEGORY.setModelClass("modelClass");
		OLD_CATEGORY.getModelSubClass().add(createStringObject("subClass"));
		OLD_CATEGORY.setModelClassComment("classComment");
		OLD_CATEGORY.setBasicProcess("basicProcess");

		OLD_PRODUCT = metadata.MetadataFactory.eINSTANCE.createProduct();
		OLD_PRODUCT.setProductName("name");
		OLD_PRODUCT.setProductDescription("description");
		OLD_PRODUCT.setProductUnit("unit");
		OLD_PRODUCT.setProductionMethod("method");
		OLD_PRODUCT.setPackaging("packaging");
		OLD_PRODUCT.setProductTreatment("treatment");
		OLD_PRODUCT.setOriginCountry("originCountry");
		OLD_PRODUCT.setOriginArea("originArea");
		OLD_PRODUCT.setFisheriesArea("fisheriesArea");
		OLD_PRODUCT.setProductionDate(createDate(2018, 0, 1));
		OLD_PRODUCT.setExpiryDate(createDate(2019, 0, 1));

		OLD_HAZARD = metadata.MetadataFactory.eINSTANCE.createHazard();
		OLD_HAZARD.setHazardType("type");
		OLD_HAZARD.setHazardName("name");
		OLD_HAZARD.setHazardDescription("description");
		OLD_HAZARD.setHazardUnit("unit");
		OLD_HAZARD.setAdverseEffect("adverseEffect");
		OLD_HAZARD.setSourceOfContamination("sourceOfContamination");
		OLD_HAZARD.setBenchmarkDose("benchmarkDose");
		OLD_HAZARD.setMaximumResidueLimit("maximumResidueLimit");
		OLD_HAZARD.setNoObservedAdverseAffectLevel("level");
		OLD_HAZARD.setLowestObservedAdverseAffectLevel("level");
		OLD_HAZARD.setAcceptableOperatorExposureLevel("level");
		OLD_HAZARD.setAcuteReferenceDose("dose");
		OLD_HAZARD.setAcceptableDailyIntake("intake");
		OLD_HAZARD.setHazardIndSum("sum");

		OLD_GROUP = metadata.MetadataFactory.eINSTANCE.createPopulationGroup();
		OLD_GROUP.setPopulationName("name");
		OLD_GROUP.setTargetPopulation("population");
		OLD_GROUP.getPopulationSpan().add(createStringObject("span"));
		OLD_GROUP.getPopulationDescription().add(createStringObject("description"));
		OLD_GROUP.getBmi().add(createStringObject("bmi"));
		OLD_GROUP.getSpecialDietGroups().add(createStringObject("group"));
		OLD_GROUP.getRegion().add(createStringObject("region"));
		OLD_GROUP.getCountry().add(createStringObject("country"));
		OLD_GROUP.getPopulationRiskFactor().add(createStringObject("factor"));
		OLD_GROUP.getSeason().add(createStringObject("season"));
		OLD_GROUP.setPopulationGender("gender");
		OLD_GROUP.getPatternConsumption().add(createStringObject("consumption"));
		OLD_GROUP.getPopulationAge().add(createStringObject("age"));

		OLD_ASSAY = metadata.MetadataFactory.eINSTANCE.createAssay();
		OLD_ASSAY.setAssayName("name");
		OLD_ASSAY.setAssayDescription("description");
		OLD_ASSAY.setPercentageOfMoisture("0.5");
		OLD_ASSAY.setPercentageOfFat("0.3");
		OLD_ASSAY.setLimitOfDetection("detection");
		OLD_ASSAY.setLimitOfQuantification("quantification");
		OLD_ASSAY.setLeftCensoredData("data");
		OLD_ASSAY.setRangeOfContamination("contamination");
		OLD_ASSAY.setUncertaintyValue("value");

		OLD_LABORATORY = metadata.MetadataFactory.eINSTANCE.createLaboratory();
		OLD_LABORATORY.setLaboratoryName("name");
		OLD_LABORATORY.setLaboratoryCountry("country");
		OLD_LABORATORY.getLaboratoryAccreditation().add(createStringObject("accreditation"));

		OLD_SAMPLE = metadata.MetadataFactory.eINSTANCE.createStudySample();
		OLD_SAMPLE.setSampleName("name");
		OLD_SAMPLE.setProtocolOfSampleCollection("collection");
		OLD_SAMPLE.setSamplingStrategy("strategy");
		OLD_SAMPLE.setTypeOfSamplingProgram("program");
		OLD_SAMPLE.setSamplingMethod("method");
		OLD_SAMPLE.setSamplingPlan("plan");
		OLD_SAMPLE.setSamplingWeight("weight");
		OLD_SAMPLE.setSamplingSize("size");
		OLD_SAMPLE.setLotSizeUnit("unit");
		OLD_SAMPLE.setSamplingPoint("point");

		OLD_METHOD = metadata.MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();
		OLD_METHOD.setCollectionTool("tool");
		OLD_METHOD.setNumberOfNonConsecutiveOneDay(7);
		OLD_METHOD.setSoftwareTool("tool");
		OLD_METHOD.setNumberOfFoodItems("items");
		OLD_METHOD.setRecordTypes("types");
		OLD_METHOD.setFoodDescriptors("descriptors");

		OLD_STUDY = metadata.MetadataFactory.eINSTANCE.createStudy();
		OLD_STUDY.setStudyIdentifier("identifier");
		OLD_STUDY.setStudyTitle("title");
		OLD_STUDY.setStudyDescription("description");
		OLD_STUDY.setStudyDesignType("type");
		OLD_STUDY.setStudyAssayMeasurementType("type");
		OLD_STUDY.setStudyAssayTechnologyType("type");
		OLD_STUDY.setStudyAssayTechnologyPlatform("platform");
		OLD_STUDY.setAccreditationProcedureForTheAssayTechnology("technology");
		OLD_STUDY.setStudyProtocolName("name");
		OLD_STUDY.setStudyProtocolType("type");
		OLD_STUDY.setStudyProtocolDescription("description");
		OLD_STUDY.setStudyProtocolURI(URI.create("https://bfr.bund.de"));
		OLD_STUDY.setStudyProtocolVersion("1.0");
		OLD_STUDY.setStudyProtocolParametersName("name");
		OLD_STUDY.setStudyProtocolComponentsName("name");
		OLD_STUDY.setStudyProtocolComponentsType("type");
		
		OLD_PARAMETER = metadata.MetadataFactory.eINSTANCE.createParameter();
		OLD_PARAMETER.setParameterID("id");
		OLD_PARAMETER.setParameterClassification(metadata.ParameterClassification.CONSTANT);
		OLD_PARAMETER.setParameterName("name");
		OLD_PARAMETER.setParameterDescription("description");
		OLD_PARAMETER.setParameterType("type");
		OLD_PARAMETER.setParameterUnit("unit");
		OLD_PARAMETER.setParameterUnitCategory("unitCategory");
		OLD_PARAMETER.setParameterDataType(metadata.ParameterType.BOOLEAN);
		OLD_PARAMETER.setParameterSource("source");
		OLD_PARAMETER.setParameterSubject("subject");
		OLD_PARAMETER.setParameterDistribution("distribution");
		OLD_PARAMETER.setParameterValue("value");
		OLD_PARAMETER.setParameterVariabilitySubject("subject");
		OLD_PARAMETER.setParameterValueMin("false");
		OLD_PARAMETER.setParameterValueMax("true");
		OLD_PARAMETER.setParameterError("2.718");
		OLD_PARAMETER.setReference(OLD_REFERENCE);
		
		OLD_EQUATION = metadata.MetadataFactory.eINSTANCE.createModelEquation();
		OLD_EQUATION.setModelEquationName("name");
		OLD_EQUATION.setModelEquationClass("class");
		OLD_EQUATION.setModelEquation("equation");
		OLD_EQUATION.getReference().add(metadata.MetadataFactory.eINSTANCE.createReference());
		OLD_EQUATION.getHypothesisOfTheModel().add(createStringObject("hypothesis"));
		
		OLD_EXPOSURE = metadata.MetadataFactory.eINSTANCE.createExposure();
		OLD_EXPOSURE.setTypeOfExposure("exposure");
		OLD_EXPOSURE.setUncertaintyEstimation("estimation");
		OLD_EXPOSURE.getMethodologicalTreatmentOfLeftCensoredData().add(createStringObject("data"));
		OLD_EXPOSURE.getLevelOfContaminationAfterLeftCensoredDataTreatment().add(createStringObject("treatment"));
		OLD_EXPOSURE.getScenario().add(createStringObject("scenario"));
	}

	@Test
	public void testContact() {

		Contact contact = EmfMetadataModule.convertContact(MAPPER.valueToTree(OLD_CONTACT));

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
	public void testEmptyContact() {
		
		metadata.Contact emptyOldContact = metadata.MetadataFactory.eINSTANCE.createContact();
		Contact contact = EmfMetadataModule.convertContact(MAPPER.valueToTree(emptyOldContact));
		
		assertNull(contact.getTitle());
		assertNull(contact.getFamilyName());
		assertNull(contact.getGivenName());
		assertNull(contact.getEmail());
		assertNull(contact.getTelephone());
		assertNull(contact.getStreetAddress());
		assertNull(contact.getCountry());
		assertNull(contact.getZipCode());
		assertNull(contact.getRegion());
		assertNull(contact.getTimeZone());
		assertNull(contact.getGender());
		assertNull(contact.getNote());
		assertNull(contact.getOrganization());
	}

	@Test
	public void testReference() {

		Reference reference = EmfMetadataModule.convertReference(MAPPER.valueToTree(OLD_REFERENCE));

		assertTrue(reference.isIsReferenceDescription());
		assertEquals(PublicationTypeEnum.RPRT, reference.getPublicationType());
		assertEquals("2018", reference.getDate());
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
	public void testEmptyReference() {
		
		metadata.Reference emptyOldReference = metadata.MetadataFactory.eINSTANCE.createReference();
		Reference reference = EmfMetadataModule.convertReference(MAPPER.valueToTree(emptyOldReference));
		
		assertFalse(reference.isIsReferenceDescription());
		assertNull(reference.getPublicationType());
		assertNull(reference.getDate());
		assertNull(reference.getPmid());
		assertNull(reference.getDoi());
		assertNull(reference.getTitle());
		assertNull(reference.getAbstract());
		assertNull(reference.getAbstract());
		assertNull(reference.getVolume());
		assertNull(reference.getIssue());
		assertNull(reference.getStatus());
		assertNull(reference.getWebsite());
	}

	@Test
	public void testModelCategory() {

		ModelCategory category = EmfMetadataModule.convertModelCategory(MAPPER.valueToTree(OLD_CATEGORY));

		assertEquals("modelClass", category.getModelClass());
		assertEquals("subClass", category.getModelSubClass().get(0));
		assertEquals("classComment", category.getModelClassComment());
		assertEquals("basicProcess", category.getBasicProcess().get(0));
	}
	
	@Test
	public void testEmptyModelCategory() {
		
		metadata.ModelCategory emptyOldCategory = metadata.MetadataFactory.eINSTANCE.createModelCategory();
		ModelCategory category = EmfMetadataModule.convertModelCategory(MAPPER.valueToTree(emptyOldCategory));
		
		assertNull(category.getModelClass());
		assertNull(category.getModelSubClass());
		assertNull(category.getModelClassComment());
		assertNull(category.getBasicProcess());
	}

	@Test
	public void testGeneralInformation() throws IOException {

		Copier copier = new Copier();

		metadata.GeneralInformation oldInformation = metadata.MetadataFactory.eINSTANCE.createGeneralInformation();
		oldInformation.setName("name");
		oldInformation.setSource("source");
		oldInformation.setIdentifier("identifier");
		oldInformation.setCreationDate(createDate(2018, 0, 1));
		oldInformation.setRights("rights");
		oldInformation.setAvailable(true);
		oldInformation.setFormat("format");
		oldInformation.setLanguage("language");
		oldInformation.setSoftware("software");
		oldInformation.setLanguageWrittenIn("languageWrittenIn");
		oldInformation.setStatus("status");
		oldInformation.setObjective("objective");
		oldInformation.setDescription("description");
		oldInformation.getModelCategory().add(OLD_CATEGORY);

		final metadata.ModificationDate md = metadata.MetadataFactory.eINSTANCE.createModificationDate();
		md.setValue(createDate(2018, 0, 1));
		oldInformation.getModificationdate().add(md);

		metadata.Contact author = (metadata.Contact) copier.copy(OLD_CONTACT);
		author.setGivenName("John");
		author.setFamilyName("Doe");
		oldInformation.setAuthor(author);

		metadata.Contact creator = (metadata.Contact) copier.copy(OLD_CONTACT);
		creator.setGivenName("Jane");
		creator.setFamilyName("Doe");
		oldInformation.getCreators().add(creator);

		oldInformation.getReference().add(OLD_REFERENCE);

		String jsonString = MAPPER.writeValueAsString(oldInformation);
		GenericModelGeneralInformation information = MAPPER.readValue(jsonString, GenericModelGeneralInformation.class);

		assertEquals("name", information.getName());
		assertEquals("source", information.getSource());
		assertEquals("identifier", information.getIdentifier());
		assertEquals(1, information.getAuthor().size());
		assertEquals(1, information.getCreator().size());
		assertEquals(LocalDate.of(2018, 1, 1), information.getCreationDate());

		// FIXME: modification date is not saved fully to JSON
//		assertEquals(LocalDate.of(2018, 1, 1), information.getModificationDate().get(0));

		assertEquals("rights", information.getRights());
		assertEquals("true", information.getAvailability());
		assertNull(information.getUrl());
		assertEquals("format", information.getFormat());
		assertEquals(1, information.getReference().size());
		assertEquals("language", information.getLanguage());
		assertEquals("software", information.getSoftware());
		assertEquals("languageWrittenIn", information.getLanguageWrittenIn());

		assertNotNull(information.getModelCategory());

		assertEquals("status", information.getStatus());
		assertEquals("objective", information.getObjective());
		assertEquals("description", information.getDescription());
	}

	@Test
	public void testScope() throws IOException {

		final metadata.Scope oldScope = metadata.MetadataFactory.eINSTANCE.createScope();
		oldScope.getProduct().add(OLD_PRODUCT);
		oldScope.getHazard().add(OLD_HAZARD);
		oldScope.getPopulationGroup().add(OLD_GROUP);
		oldScope.setGeneralComment("comment");
		oldScope.setTemporalInformation("information");
		oldScope.setSpatialInformation(metadata.MetadataFactory.eINSTANCE.createSpatialInformation());

		String jsonString = MAPPER.writeValueAsString(oldScope);
		GenericModelScope scope = MAPPER.readValue(jsonString, GenericModelScope.class);

		assertEquals(1, scope.getProduct().size());
		assertEquals(1, scope.getHazard().size());
		assertEquals(1, scope.getPopulationGroup().size());
		assertEquals("comment", scope.getGeneralComment());
		assertEquals("information", scope.getTemporalInformation());
	}

	@Test
	public void testProduct() {

		Product product = EmfMetadataModule.convertProduct(MAPPER.valueToTree(OLD_PRODUCT));

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
	public void testEmptyProduct() {
		
		metadata.Product emptyOldProduct = metadata.MetadataFactory.eINSTANCE.createProduct();
		Product product = EmfMetadataModule.convertProduct(MAPPER.valueToTree(emptyOldProduct));
		
		assertNull(product.getName());
		assertNull(product.getDescription());
		assertNull(product.getUnit());
		assertNull(product.getMethod());
		assertNull(product.getPackaging());
		assertNull(product.getTreatment());
		assertNull(product.getOriginCountry());
		assertNull(product.getOriginArea());
		assertNull(product.getFisheriesArea());
		assertNull(product.getProductionDate());
		assertNull(product.getExpiryDate());
	}
	
	@Test
	public void testHazard() {

		Hazard hazard = EmfMetadataModule.convertHazard(MAPPER.valueToTree(OLD_HAZARD));

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
	public void testEmptyHazard() {
		
		metadata.Hazard emptyOldHazard = metadata.MetadataFactory.eINSTANCE.createHazard();
		Hazard hazard = EmfMetadataModule.convertHazard(MAPPER.valueToTree(emptyOldHazard));
		
		assertNull(hazard.getType());
		assertNull(hazard.getName());
		assertNull(hazard.getDescription());
		assertNull(hazard.getUnit());
		assertNull(hazard.getAdverseEffect());
		assertNull(hazard.getSourceOfContamination());
		assertNull(hazard.getBenchmarkDose());
		assertNull(hazard.getMaximumResidueLimit());
		assertNull(hazard.getNoObservedAdverseAffectLevel());
		assertNull(hazard.getLowestObservedAdverseAffectLevel());
		assertNull(hazard.getAcceptableOperatorsExposureLevel());
		assertNull(hazard.getAcuteReferenceDose());
		assertNull(hazard.getAcceptableDailyIntake());
		assertNull(hazard.getIndSum());
	}

	@Test
	public void testPopulationGroup() {

		PopulationGroup group = EmfMetadataModule.convertPopulationGroup(MAPPER.valueToTree(OLD_GROUP));

		assertEquals("name", group.getName());
		assertEquals("population", group.getTargetPopulation());
		assertEquals("span", group.getPopulationSpan().get(0));
		assertEquals("description", group.getPopulationDescription().get(0));
		assertEquals("age", group.getPopulationAge().get(0));
		assertEquals("gender", group.getPopulationGender());
		assertEquals("bmi", group.getBmi().get(0));
		assertEquals("group", group.getSpecialDietGroups().get(0));
		assertEquals("consumption", group.getPatternConsumption().get(0));
		assertEquals("region", group.getRegion().get(0));
		assertEquals("country", group.getCountry().get(0));
		assertEquals("factor", group.getPopulationRiskFactor().get(0));
		assertEquals("season", group.getSeason().get(0));
	}
	
	@Test
	public void testEmptyPopulationGroup() {
		
		metadata.PopulationGroup emptyOldGroup = metadata.MetadataFactory.eINSTANCE.createPopulationGroup();
		PopulationGroup group = EmfMetadataModule.convertPopulationGroup(MAPPER.valueToTree(emptyOldGroup));
		
		assertNull(group.getName());
		assertNull(group.getTargetPopulation());
		assertNull(group.getPopulationSpan());
		assertNull(group.getPopulationDescription());
		assertNull(group.getPopulationAge());
		assertNull(group.getPopulationGender());
		assertNull(group.getBmi());
		assertNull(group.getSpecialDietGroups());
		assertNull(group.getPatternConsumption());
		assertNull(group.getRegion());
		assertNull(group.getCountry());
		assertNull(group.getPopulationRiskFactor());
		assertNull(group.getSeason());
	}

	@Test
	public void testAssay() {

		Assay assay = EmfMetadataModule.convertAssay(MAPPER.valueToTree(OLD_ASSAY));

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
	public void testEmptyAssay() {
		
		metadata.Assay emptyOldAssay = metadata.MetadataFactory.eINSTANCE.createAssay();
		Assay assay = EmfMetadataModule.convertAssay(MAPPER.valueToTree(emptyOldAssay));
		
		assertNull(assay.getName());
		assertNull(assay.getDescription());
		assertNull(assay.getMoisturePercentage());
		assertNull(assay.getFatPercentage());
		assertNull(assay.getDetectionLimit());
		assertNull(assay.getQuantificationLimit());
		assertNull(assay.getLeftCensoredData());
		assertNull(assay.getContaminationRange());
		assertNull(assay.getUncertaintyValue());
	}

	@Test
	public void testLaboratory() {

		Laboratory laboratory = EmfMetadataModule.convertLaboratory(MAPPER.valueToTree(OLD_LABORATORY));

		assertEquals("name", laboratory.getName());
		assertEquals("country", laboratory.getCountry());
		assertEquals("accreditation", laboratory.getAccreditation().get(0));
	}
	
	@Test
	public void testEmptyLaboratory() {
		
		metadata.Laboratory emptyOldLaboratory = metadata.MetadataFactory.eINSTANCE.createLaboratory();
		Laboratory laboratory = EmfMetadataModule.convertLaboratory(MAPPER.valueToTree(emptyOldLaboratory));
		
		assertNull(laboratory.getName());
		assertNull(laboratory.getCountry());
		assertTrue(laboratory.getAccreditation().isEmpty());
	}

	@Test
	public void testStudySample() {

		StudySample sample = EmfMetadataModule.convertStudySample(MAPPER.valueToTree(OLD_SAMPLE));

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
	public void testEmptyStudySample() {
		
		metadata.StudySample emptyOldSample = metadata.MetadataFactory.eINSTANCE.createStudySample();
		StudySample sample = EmfMetadataModule.convertStudySample(MAPPER.valueToTree(emptyOldSample));
		
		assertNull(sample.getSampleName());
		assertNull(sample.getProtocolOfSampleCollection());
		assertNull(sample.getSamplingStrategy());
		assertNull(sample.getTypeOfSamplingProgram());
		assertNull(sample.getSamplingMethod());
		assertNull(sample.getSamplingPlan());
		assertNull(sample.getSamplingWeight());
		assertNull(sample.getSamplingSize());
		assertNull(sample.getLotSizeUnit());
		assertNull(sample.getSamplingPoint());
	}

	@Test
	public void testDietaryAssessmentMethod() {

		DietaryAssessmentMethod method = EmfMetadataModule
				.convertDietaryAssessmentMethod(MAPPER.valueToTree(OLD_METHOD));

		assertEquals("tool", method.getCollectionTool());
		assertEquals("7", method.getNumberOfNonConsecutiveOneDay());
		assertEquals("tool", method.getSoftwareTool());
		assertEquals("items", method.getNumberOfFoodItems().get(0));
		assertEquals("types", method.getRecordTypes().get(0));
		assertEquals("descriptors", method.getFoodDescriptors().get(0));
	}
	
	@Test
	public void testEmptyDietaryAssessmentMethod() {
		
		metadata.DietaryAssessmentMethod emptyOldMethod = metadata.MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();
		DietaryAssessmentMethod method = EmfMetadataModule.convertDietaryAssessmentMethod(MAPPER.valueToTree(emptyOldMethod));
		
		assertNull(method.getCollectionTool());
		assertNull(method.getNumberOfNonConsecutiveOneDay());
		assertNull(method.getSoftwareTool());
		assertTrue(method.getNumberOfFoodItems().isEmpty());
		assertTrue(method.getRecordTypes().isEmpty());
		assertTrue(method.getFoodDescriptors().isEmpty());
	}

	@Test
	public void testStudy() {

		Study study = EmfMetadataModule.convertStudy(MAPPER.valueToTree(OLD_STUDY));

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
	public void testEmptyStudy() {
		
		metadata.Study emptyOldStudy = metadata.MetadataFactory.eINSTANCE.createStudy();
		Study study = EmfMetadataModule.convertStudy(MAPPER.valueToTree(emptyOldStudy));
		
		assertNull(study.getIdentifier());
		assertNull(study.getTitle());
		assertNull(study.getDescription());
		assertNull(study.getDesignType());
		assertNull(study.getAssayMeasurementType());
		assertNull(study.getAssayTechnologyType());
		assertNull(study.getAssayTechnologyPlatform());
		assertNull(study.getAccreditationProcedureForTheAssayTechnology());
		assertNull(study.getProtocolName());
		assertNull(study.getProtocolType());
		assertNull(study.getProtocolDescription());
		assertNull(study.getProtocolURI());
		assertNull(study.getProtocolVersion());
		assertNull(study.getProtocolParametersName());
		assertNull(study.getProtocolComponentsName());
		assertNull(study.getProtocolComponentsType());
	}

	@Test
	public void testDataBackground() throws IOException {

		final metadata.DataBackground oldBackground = metadata.MetadataFactory.eINSTANCE.createDataBackground();
		oldBackground.setStudy(OLD_STUDY);
		oldBackground.getStudySample().add(OLD_SAMPLE);
		oldBackground.getDietaryAssessmentMethod().add(OLD_METHOD);
		oldBackground.getLaboratory().add(OLD_LABORATORY);
		oldBackground.getAssay().add(OLD_ASSAY);

		String jsonString = MAPPER.writeValueAsString(oldBackground);
		GenericModelDataBackground background = MAPPER.readValue(jsonString, GenericModelDataBackground.class);

		assertNotNull(background.getStudy());
		assertEquals(1, background.getStudySample().size());
		assertEquals(1, background.getDietaryAssessmentMethod().size());
		assertEquals(1, background.getLaboratory().size());
		assertEquals(1, background.getAssay().size());
	}
	
	@Test
	public void testParameter() {
		
		Parameter parameter = EmfMetadataModule.convertParameter(MAPPER.valueToTree(OLD_PARAMETER));

		assertEquals("id", parameter.getId());
		assertEquals(Parameter.ClassificationEnum.CONSTANT, parameter.getClassification());
		assertEquals("name", parameter.getName());
		assertEquals("description", parameter.getDescription());
		assertEquals("unit", parameter.getUnit());
		assertEquals("unitCategory", parameter.getUnitCategory());
		assertEquals(Parameter.DataTypeEnum.BOOLEAN, parameter.getDataType());
		assertEquals("source", parameter.getSource());
		assertEquals("subject", parameter.getSubject());
		assertEquals("distribution", parameter.getDistribution());
		assertEquals("value", parameter.getValue());
		assertEquals("subject", parameter.getVariabilitySubject());
		assertEquals("false", parameter.getMinValue());
		assertEquals("true", parameter.getMaxValue());
		assertEquals("2.718", parameter.getError());
		assertNotNull(parameter.getReference());
	}
	
	@Test
	public void testEmptyParameter() {
		
		metadata.Parameter emptyOldParameter = metadata.MetadataFactory.eINSTANCE.createParameter();
		Parameter parameter = EmfMetadataModule.convertParameter(MAPPER.valueToTree(emptyOldParameter));
		
		assertNull(parameter.getId());
		assertNull(parameter.getClassification());
		assertNull(parameter.getName());
		assertNull(parameter.getDescription());
		assertNull(parameter.getUnit());
		assertNull(parameter.getUnitCategory());
		assertNull(parameter.getDataType());
		assertNull(parameter.getSource());
		assertNull(parameter.getSubject());
		assertNull(parameter.getDistribution());
		assertNull(parameter.getValue());
		assertNull(parameter.getSubject());
		assertNull(parameter.getMinValue());
		assertNull(parameter.getMaxValue());
		assertNull(parameter.getError());
		assertNull(parameter.getReference());
	}
	
	@Test
	public void testModelEquation() {

		ModelEquation equation = EmfMetadataModule.convertModelEquation(MAPPER.valueToTree(OLD_EQUATION));

		assertEquals("name", equation.getName());
		assertEquals("class", equation.getModelEquationClass());
		assertEquals("equation", equation.getModelEquation());
		assertEquals(1, equation.getReference().size());
		assertEquals("hypothesis", equation.getModelHypothesis().get(0));
	}
	
	@Test
	public void testEmptyModelEquation() {
		
		metadata.ModelEquation emptyOldEquation = metadata.MetadataFactory.eINSTANCE.createModelEquation();
		ModelEquation equation = EmfMetadataModule.convertModelEquation(MAPPER.valueToTree(emptyOldEquation));
		
		assertNull(equation.getName());
		assertNull(equation.getModelEquationClass());
		assertNull(equation.getModelEquation());
		assertNull(equation.getReference());
		assertNull(equation.getModelHypothesis());
	}
	
	@Test
	public void testExposure() {
		
		Exposure exposure = EmfMetadataModule.convertExposure(MAPPER.valueToTree(OLD_EXPOSURE));

		assertEquals("exposure", exposure.getType());
		assertEquals("estimation", exposure.getUncertaintyEstimation());
		assertEquals("data", exposure.getTreatment().get(0));
		assertEquals("treatment", exposure.getContamination().get(0));
		assertEquals("scenario", exposure.getScenario().get(0));
	}
	
	@Test
	public void testEmptyExposure() {
		
		metadata.Exposure emptyOldExposure = metadata.MetadataFactory.eINSTANCE.createExposure();
		Exposure exposure = EmfMetadataModule.convertExposure(MAPPER.valueToTree(emptyOldExposure));
		
		assertNull(exposure.getType());
		assertNull(exposure.getUncertaintyEstimation());
		assertNull(exposure.getTreatment());
		assertNull(exposure.getContamination());
		assertNull(exposure.getScenario());
	}
	
	@Test
	public void testModelMath() throws IOException {
		
		final metadata.ModelMath oldMath = metadata.MetadataFactory.eINSTANCE.createModelMath();
		oldMath.setFittingProcedure("procedure");
		oldMath.getParameter().add(OLD_PARAMETER);
		oldMath.getModelEquation().add(OLD_EQUATION);
		oldMath.setExposure(OLD_EXPOSURE);
		oldMath.getEvent().add(createStringObject("event"));
		
		String jsonString = MAPPER.writeValueAsString(oldMath);
		GenericModelModelMath math = MAPPER.readValue(jsonString, GenericModelModelMath.class);

		assertEquals(1, math.getParameter().size());
		assertNull(math.getQualityMeasures());
		assertEquals(1, math.getModelEquation().size());
		assertEquals("procedure", math.getFittingProcedure());
		assertEquals(1, math.getExposure().size());
		assertEquals("event", math.getEvent().get(0));
	}

	private static Date createDate(int year, int month, int date) {
		return new Date(year - 1900, month, date);
	}

	private static StringObject createStringObject(String string) {
		final StringObject so = metadata.MetadataFactory.eINSTANCE.createStringObject();
		so.setValue(string);

		return so;
	}
}
