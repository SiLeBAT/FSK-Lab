package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import javax.json.JsonObject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("static-method")
public class RAKIPSheetImporterTest {
	
	private static XSSFSheet sheet;
	
	private static RAKIPSheetImporter importer;

	@BeforeClass
	public static void setup() throws InvalidFormatException, IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook(new File("files/QMRA_Listeria.xlsx"))) {
			sheet = workbook.getSheetAt(0);
		}
		
		importer = new RAKIPSheetImporter();
	}

	@Test
	public void testGeneralInformation() throws IOException, InvalidFormatException {

		GeneralInformation generalInformation = importer.retrieveGeneralInformation(sheet);

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		assertEquals("Listeria Monocytogenes (DR of gQMRA)", generalInformation.getName());
		assertEquals("PUBLISHED SCIENTIFIC STUDIES", generalInformation.getSource());
		assertEquals("DR000001", generalInformation.getIdentifier());
		assertEquals("CC0", generalInformation.getRights());
		// TODO: availability is boolean in EMF model. Need to change its type to
		// string.
		// assertEquals("Open access", generalInformation.ava)

		// TODO: url is missing in EMF model.
		// assertEquals("", generalInformation.getURL());

		assertEquals(".fskx", generalInformation.getFormat());

		assertEquals("English", generalInformation.getLanguage());
		assertEquals("FSK-Lab", generalInformation.getSoftware());
		assertEquals("R 3", generalInformation.getLanguageWrittenIn());
		assertEquals("Uncurated", generalInformation.getStatus());
		assertFalse(generalInformation.eIsSet(pkg.getGeneralInformation_Objective()));
		assertFalse(generalInformation.eIsSet(pkg.getGeneralInformation_Description()));
	}

	@Test
	public void testCreator() throws IOException, InvalidFormatException {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		Contact contact = importer.retrieveContact(sheet.getRow(3));
		assertFalse(contact.eIsSet(pkg.getContact_Title()));
		assertEquals("Mesa Varona", contact.getFamilyName());
		assertFalse(contact.eIsSet(pkg.getContact_GivenName()));
		assertEquals("octavio.mesa-varona@bfr.bund.de", contact.getEmail());
		assertFalse(contact.eIsSet(pkg.getContact_Telephone()));
		assertEquals("Alt-Marienfelde 17-21", contact.getStreetAddress());
		assertEquals("Germany", contact.getCountry());
		assertEquals("Berlin", contact.getCity());
		assertFalse(contact.eIsSet(pkg.getContact_ZipCode()));
		assertFalse(contact.eIsSet(pkg.getContact_Region()));
		assertEquals("BfR", contact.getOrganization());
	}

	@Test
	public void testAuthor() throws IOException, InvalidFormatException {

		Contact contact = importer.retrieveAuthor(sheet.getRow(3));
		assertEquals("Prof", contact.getTitle());
		assertEquals("Mosley", contact.getFamilyName());
		assertEquals("Steve", contact.getGivenName());
		assertEquals("mosley@nyu.org", contact.getEmail());
		assertEquals("080 12345566", contact.getTelephone());
		assertEquals("Berliner Strasse 2", contact.getStreetAddress());
		assertEquals("Saint Vincent and the Grenadines", contact.getCountry());
		assertEquals("Berlin", contact.getCity());
		assertEquals("12345", contact.getZipCode());
		assertEquals("Greater New Yorker Area", contact.getRegion());
		assertEquals("NYU", contact.getOrganization());
	}

	@Test
	public void testReference() throws IOException, InvalidFormatException {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		Reference reference = importer.retrieveReference(sheet.getRow(14));

		assertTrue(reference.isIsReferenceDescription());
		assertEquals(PublicationType.RPRT, reference.getPublicationType());

		// 6th December 2017
		assertEquals(new Date(2017, 11, 6), reference.getPublicationDate());

		assertFalse(reference.eIsSet(pkg.getReference_Pmid()));
		assertEquals("10.2903/j.efsa.2018.5134", reference.getDoi());
		assertEquals("authors", reference.getAuthorList());
		assertEquals("Listeria monocytogenes contamination of ready-to-eat\n"
				+ " foods and the risk for human health in the EU", reference.getPublicationTitle());
		assertEquals("abstract", reference.getPublicationAbstract());
		// TODO: journal/volume/issue
		assertEquals("Published", reference.getPublicationStatus());
		assertEquals("www.efsa.europa.eu/efsajournal", reference.getPublicationWebsite());
		assertEquals("comment", reference.getComment());
	}

	@Test
	public void testModelCategory() throws IOException, InvalidFormatException {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		ModelCategory modelCategory = importer.retrieveModelCategory(sheet);
		assertEquals("Dose-response model", modelCategory.getModelClass());
		assertFalse(modelCategory.eIsSet(pkg.getModelCategory_ModelSubClass()));
		assertFalse(modelCategory.eIsSet(pkg.getModelCategory_ModelClassComment()));
		assertFalse(modelCategory.eIsSet(pkg.getModelCategory_BasicProcess()));
	}

	@Test
	public void testProduct() throws IOException, InvalidFormatException {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		// Test product at row 39
		{
			Product product39 = importer.retrieveProduct(sheet.getRow(38));
			assertEquals("Allspice", product39.getProductName());
			assertEquals("description", product39.getProductDescription());
			assertEquals("[]", product39.getProductUnit());
			assertFalse(product39.eIsSet(pkg.getProduct_ProductionMethod()));
			assertEquals("Aluminium foil - aluminium sheet", product39.getPackaging());
			assertEquals("Canning", product39.getProductTreatment());
			assertEquals("Afghanistan", product39.getOriginCountry());
			assertEquals("A Coruña", product39.getOriginArea());
			assertEquals("Adriatic", product39.getFisheriesArea());
			// 30th November 2017
			assertEquals(new Date(2017, 10, 30), product39.getProductionDate());
			// 30th November 2018
			assertEquals(new Date(2018, 10, 30), product39.getExpiryDate());
		}

		// Test product at row 40
		{
			Product product40 = importer.retrieveProduct(sheet.getRow(39));
			assertEquals("Almonds", product40.getProductName());
			assertEquals("almonds", product40.getProductDescription());
			assertEquals("[aw]", product40.getProductUnit());
			assertEquals("Farmed Domestic or cultivated", product40.getProductionMethod());
			assertFalse(product40.eIsSet(pkg.getProduct_Packaging()));
			assertEquals("Churning", product40.getProductTreatment());
			assertEquals("Aland Islands", product40.getOriginCountry());
			assertEquals("Aachen, Kreis", product40.getOriginArea());
			assertEquals("Aegean", product40.getFisheriesArea());
			// 30th November 2017
			assertEquals(new Date(2017, 10, 30), product40.getProductionDate());
			// 30th November 2018
			assertEquals(new Date(2018, 10, 30), product40.getExpiryDate());
		}

		// Test product at row 41
		{
			Product product41 = importer.retrieveProduct(sheet.getRow(40));
			assertEquals("American persimmon (Virginia kaki)", product41.getProductName());
			assertEquals("american", product41.getProductDescription());
			assertEquals("[Fluorescence]", product41.getProductUnit());
			assertEquals("Free range production", product41.getProductionMethod());
			assertEquals("Blister (film)", product41.getPackaging());
			assertEquals("Concentration", product41.getProductTreatment());
			assertEquals("Albania", product41.getOriginCountry());
			assertEquals("Aachen, Kreisfreie Stadt", product41.getOriginArea());
			assertEquals("Amazon", product41.getFisheriesArea());
			// 30th November 2017
			assertEquals(new Date(2017, 10, 30), product41.getProductionDate());
			// 30th November 2018
			assertEquals(new Date(2018, 10, 30), product41.getExpiryDate());
		}
	}

	@Test
	public void testHazard() throws IOException, InvalidFormatException {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		// Test hazard at row 39
		{
			Hazard hazard39 = importer.retrieveHazard(sheet.getRow(38));
			assertEquals("Biogenic amines", hazard39.getHazardType());
			assertEquals("'Prohexadione (prohexadione (acid) and its salts expressed as prohexadione-calcium)",
					hazard39.getHazardName());
			assertFalse(hazard39.eIsSet(pkg.getHazard_HazardDescription()));
			assertEquals("[]", hazard39.getHazardUnit());
			assertEquals("effect", hazard39.getAdverseEffect());
			assertEquals("source", hazard39.getSourceOfContamination());
			assertEquals("BMD", hazard39.getBenchmarkDose());
			assertEquals("MRL", hazard39.getMaximumResidueLimit());
			assertEquals("NOAEL", hazard39.getNoObservedAdverseAffectLevel());
			assertEquals("LOAEL", hazard39.getLowestObservedAdverseAffectLevel());
			assertEquals("AOEL", hazard39.getAcceptableOperatorExposureLevel());
			assertEquals("ARfD", hazard39.getAcuteReferenceDose());
			assertEquals("ADI", hazard39.getAcceptableDailyIntake());
		}
	}

	@Test
	public void testPopulationGroup() throws IOException, InvalidFormatException {

		// Test population group at row 39
		{
			PopulationGroup populationGroup39 = importer.retrievePopulationGroup(sheet.getRow(38));
			assertEquals("Other", populationGroup39.getPopulationName());
			assertEquals("target", populationGroup39.getTargetPopulation());
			assertEquals("span", populationGroup39.getPopulationSpan().get(0).getValue());
			assertEquals("description", populationGroup39.getPopulationDescription().get(0).getValue());
			assertEquals("age", populationGroup39.getPopulationAge().get(0).getValue());
			assertEquals("gender", populationGroup39.getPopulationGender());
			assertEquals("bmi", populationGroup39.getBmi().get(0).getValue());
			assertEquals("group", populationGroup39.getSpecialDietGroups().get(0).getValue());
			assertEquals("consumption", populationGroup39.getPatternConsumption().get(0).getValue());
			assertEquals("A Coruña", populationGroup39.getRegion().get(0).getValue());
			assertEquals("Afghanistan", populationGroup39.getCountry().get(0).getValue());
			assertEquals("factors", populationGroup39.getPopulationRiskFactor().get(0).getValue());
			assertEquals("Season", populationGroup39.getSeason().get(0).getValue());
		}
	}

	@Test
	public void testStudy() throws IOException, InvalidFormatException {

		Study study = importer.retrieveStudy(sheet);
		assertEquals("identifier", study.getStudyIdentifier());
		assertEquals("Listeria monocytogenes generic Quantitative Microbiological Risk Assessment (gQMRA) model",
				study.getStudyTitle());
		assertEquals("description", study.getStudyDescription());
		assertEquals("design type", study.getStudyDesignType());
		assertEquals("type", study.getStudyAssayMeasurementType());
		assertEquals("AAS", study.getStudyAssayTechnologyType());
		assertEquals("platform", study.getStudyAssayTechnologyPlatform());
		assertEquals("Internally validated", study.getAccreditationProcedureForTheAssayTechnology());
		assertEquals("name", study.getStudyProtocolName());
		assertEquals("type", study.getStudyProtocolType());
		assertEquals("description", study.getStudyDescription());
		assertEquals(URI.create("uri"), study.getStudyProtocolURI());
		assertEquals("version", study.getStudyProtocolVersion());
		assertEquals("name", study.getStudyProtocolParametersName());
		assertEquals("name", study.getStudyProtocolComponentsName());
		assertEquals("type", study.getStudyProtocolComponentsType());
	}

	@Test
	public void testStudySample() throws IOException, InvalidFormatException {

		// Test study sample from row 97
		StudySample studySample97 = importer.retrieveStudySample(sheet.getRow(96));
		assertEquals("name", studySample97.getSampleName());
		assertEquals("collection", studySample97.getProtocolOfSampleCollection());
		assertEquals("Census", studySample97.getSamplingStrategy());
		assertEquals("Clinical investigations", studySample97.getTypeOfSamplingProgram());
		assertEquals("According to 97/747/EC", studySample97.getSamplingMethod());
		assertEquals("plan", studySample97.getSamplingPlan());
		assertEquals("a", studySample97.getSamplingWeight());
		assertEquals("b", studySample97.getSamplingSize());
		assertEquals("[aw]", studySample97.getLotSizeUnit());
		assertEquals("Air transport", studySample97.getSamplingPoint());

		// Test study sample from row 98
		StudySample studySample98 = importer.retrieveStudySample(sheet.getRow(97));
		assertEquals("name2", studySample98.getSampleName());
		assertEquals("collection3", studySample98.getProtocolOfSampleCollection());
		assertEquals("Convenient sampling", studySample98.getSamplingStrategy());
		assertEquals("Control and eradication programmes", studySample98.getTypeOfSamplingProgram());
		assertEquals("According to Dir. 2002/63/EC", studySample98.getSamplingMethod());
		assertEquals("plan2", studySample98.getSamplingPlan());
		assertEquals("2.0", studySample98.getSamplingWeight());
		assertEquals("c", studySample98.getSamplingSize());
		assertEquals("[Fluorescence]", studySample98.getLotSizeUnit());
		assertEquals("Aquaculture", studySample98.getSamplingPoint());

		// Test study sample from row 99
		StudySample studySample99 = importer.retrieveStudySample(sheet.getRow(98));
		assertEquals("name3", studySample99.getSampleName());
		assertEquals("collection4", studySample99.getProtocolOfSampleCollection());
		assertEquals("Not specified", studySample99.getSamplingStrategy());
		assertEquals("Diet study", studySample99.getTypeOfSamplingProgram());
		assertEquals("According to Reg 152/2009", studySample99.getSamplingMethod());
		assertEquals("gg", studySample99.getSamplingPlan());
		assertEquals("bb", studySample99.getSamplingWeight());
		assertEquals("d", studySample99.getSamplingSize());
		assertEquals("FNU", studySample99.getLotSizeUnit());
		assertEquals("Bee hives", studySample99.getSamplingPoint());
	}

	@Test
	public void testDietaryAssessmentMethod() throws IOException, InvalidFormatException {

		// Test DietaryAssessmentMethod from row 104
		DietaryAssessmentMethod method104 = importer.retrieveDietaryAssessmentMethod(sheet.getRow(103));
		assertEquals("24-hour recall interview", method104.getCollectionTool());
		assertEquals(0, method104.getNumberOfNonConsecutiveOneDay());
		assertEquals("a", method104.getSoftwareTool());
		assertEquals("a", method104.getNumberOfFoodItems());
		assertEquals("f", method104.getRecordTypes());
		assertEquals("(Beet) Sugar", method104.getFoodDescriptors());

		// Test DietaryAssessmentMethod from row 105
		DietaryAssessmentMethod method105 = importer.retrieveDietaryAssessmentMethod(sheet.getRow(104));
		assertEquals("eating outside questionnaire", method105.getCollectionTool());
		assertEquals(1, method105.getNumberOfNonConsecutiveOneDay());
		assertEquals("b", method105.getSoftwareTool());
		assertEquals("b", method105.getNumberOfFoodItems());
		assertEquals("e", method105.getRecordTypes());
		assertEquals("(Beet) Sugar", method105.getFoodDescriptors());

		// Test DietaryAssessmentMethod from row 106
		// food diaries 2 c c d (Beet) Sugar
		DietaryAssessmentMethod method106 = importer.retrieveDietaryAssessmentMethod(sheet.getRow(105));
		assertEquals("food diaries", method106.getCollectionTool());
		assertEquals(2, method106.getNumberOfNonConsecutiveOneDay());
		assertEquals("c", method106.getSoftwareTool());
		assertEquals("c", method106.getNumberOfFoodItems());
		assertEquals("d", method106.getRecordTypes());
		assertEquals("(Beet) Sugar", method106.getFoodDescriptors());
	}

	@Test
	public void testLaboratory() throws IOException, InvalidFormatException {

		// Test Laboratory at row 111
		Laboratory laboratory111 = importer.retrieveLaboratory(sheet.getRow(110));
		assertEquals("Accredited", laboratory111.getLaboratoryAccreditation().get(0).getValue());
		assertEquals("a", laboratory111.getLaboratoryName());
		assertEquals("Afghanistan", laboratory111.getLaboratoryCountry());

		// Test Laboratory at row 112
		Laboratory laboratory112 = importer.retrieveLaboratory(sheet.getRow(111));
		assertEquals("None", laboratory112.getLaboratoryAccreditation().get(0).getValue());
		assertEquals("b", laboratory112.getLaboratoryName());
		assertEquals("Aland Islands", laboratory112.getLaboratoryCountry());

		// Test Laboratory at row 113
		Laboratory laboratory113 = importer.retrieveLaboratory(sheet.getRow(112));
		assertEquals("Other", laboratory113.getLaboratoryAccreditation().get(0).getValue());
		assertEquals("c", laboratory113.getLaboratoryName());
		assertEquals("Albania", laboratory113.getLaboratoryCountry());
	}

	@Test
	public void testAssay() throws IOException, InvalidFormatException {

		// Test assay at row 118
		Assay assay118 = importer.retrieveAssay(sheet.getRow(117));
		assertEquals("name0", assay118.getAssayName());
		assertEquals("descr0", assay118.getAssayDescription());
		assertEquals("moist0", assay118.getPercentageOfMoisture());
		assertEquals("fat0", assay118.getPercentageOfFat());
		assertEquals("detect0", assay118.getLimitOfDetection());
		assertEquals("quant0", assay118.getLimitOfQuantification());
		assertEquals("range0", assay118.getRangeOfContamination());
		assertEquals("uncert0", assay118.getUncertaintyValue());

		// Test assay at row 119
		Assay assay119 = importer.retrieveAssay(sheet.getRow(118));
		assertEquals("name1", assay119.getAssayName());
		assertEquals("descr1", assay119.getAssayDescription());
		assertEquals("moist1", assay119.getPercentageOfMoisture());
		assertEquals("fat1", assay119.getPercentageOfFat());
		assertEquals("detect1", assay119.getLimitOfDetection());
		assertEquals("quant1", assay119.getLimitOfQuantification());
		assertEquals("range1", assay119.getRangeOfContamination());
		assertEquals("uncert1", assay119.getUncertaintyValue());

		// Test assay at row 120
		Assay assay120 = importer.retrieveAssay(sheet.getRow(119));
		assertEquals("name2", assay120.getAssayName());
		assertEquals("descr2", assay120.getAssayDescription());
		assertEquals("moist2", assay120.getPercentageOfMoisture());
		assertEquals("fat2", assay120.getPercentageOfFat());
		assertEquals("detect2", assay120.getLimitOfDetection());
		assertEquals("quant2", assay120.getLimitOfQuantification());
		assertEquals("range2", assay120.getRangeOfContamination());
		assertEquals("uncert2", assay120.getUncertaintyValue());
	}

	@Test
	public void testParameter() throws IOException, InvalidFormatException {

		// Check parameter at row 133
		Parameter param133 = importer.retrieveParameter(sheet.getRow(132));

		assertEquals("DR_Inputs3", param133.getParameterID());
		assertEquals(ParameterClassification.INPUT, param133.getParameterClassification());
		assertEquals("DR_Inputs3.csv", param133.getParameterName());
		assertEquals("DR values, relative risk", param133.getParameterDescription());
		assertEquals("[]", param133.getParameterUnit());
		assertEquals("Dimensionless Quantity", param133.getParameterUnitCategory());
		assertEquals(ParameterType.FILE, param133.getParameterDataType());
		assertEquals("Boolean", param133.getParameterSource());
		assertEquals("Boolean", param133.getParameterSubject());
		assertEquals("Boolean", param133.getParameterDistribution());
		assertEquals(
				"C:\\Users\\mesa\\Desktop\\Listeria monocitogenes(KJ) QMRA\\listeria project\\model\\DR\\DR_inputs3.csv",
				param133.getParameterValue());
		// reference
		assertEquals("a", param133.getParameterVariabilitySubject());
		assertEquals("max0", param133.getParameterValueMax());
		assertEquals("min0", param133.getParameterValueMin());
		assertEquals("error0", param133.getParameterError());
	}

	@Test
	public void testQualityMeasures() throws IOException, InvalidFormatException {

		JsonObject measures = importer.retrieveQualityMeasures(sheet);

		assertEquals(0.1, measures.getJsonNumber("SSE").doubleValue(), .0);
		assertEquals(0.2, measures.getJsonNumber("MSE").doubleValue(), .0);
		assertEquals(0.3, measures.getJsonNumber("RMSE").doubleValue(), .0);
		assertEquals(0.4, measures.getJsonNumber("Rsquared").doubleValue(), .0);
		assertEquals(0.5, measures.getJsonNumber("AIC").doubleValue(), .0);
		assertEquals(0.6, measures.getJsonNumber("BIC").doubleValue(), .0);
	}
}
