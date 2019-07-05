package metadata;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;

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
		assertEquals(PublicationTypeEnum.RPRT, reference.getPublicationType().get(0));
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

	@Ignore
	@Test
	public void testConvertScope() {
		// TODO: check product
		// TODO: hazard
		// TODO: populationGroup
		// TODO: generalComment
		// TODO: temporalInformation
		// TODO: spatialInformation
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
		
		PopulationGroup pg = new PopulationGroup();
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

	private static StringObject createStringObject(String string) {
		StringObject so = metadata.MetadataFactory.eINSTANCE.createStringObject();
		so.setValue(string);

		return so;
	}
}
