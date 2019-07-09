package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.QualityMeasures;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

public class RAKIPSheetImporter2Test {

	private static Sheet sheet;
	private static RAKIPSheetImporter2 importer;

	@BeforeClass
	public static void setup() throws Exception {
		Workbook workbook = WorkbookFactory.create(new File("files/QMRA_Listeria.xlsx"));
		sheet = workbook.getSheetAt(0);
		importer = new RAKIPSheetImporter2();
	}

	@Test
	public void testGeneralInformation() {
		GenericModelGeneralInformation information = importer.retrieveGeneralInformation(sheet);
		assertEquals("Listeria Monocytogenes (DR of gQMRA)", information.getName());
		assertEquals("PUBLISHED SCIENTIFIC STUDIES", information.getSource());
		assertEquals("DR000001", information.getIdentifier());
		assertEquals(1, information.getAuthor().size());
		assertEquals(1, information.getCreator().size());
		assertEquals(LocalDate.of(2018, 3, 30), information.getCreationDate());
		assertNull(information.getModificationDate()); // Not set
		assertEquals("CC0", information.getRights());
		assertEquals("Open access", information.getAvailability());
		assertEquals("http://onlinelibrary.wiley.com/doi/10.2903/sp.efsa.2017.EN-1252/abstract", information.getUrl());
		assertEquals(".fskx", information.getFormat());
		assertEquals(1, information.getReference().size());
		assertEquals("English", information.getLanguage());
		assertEquals("FSK-Lab", information.getSoftware());
		assertEquals("R 3", information.getLanguageWrittenIn());
		assertNotNull(information.getModelCategory());
		assertEquals("Uncurated", information.getStatus());
		assertNull(information.getObjective()); // Not set
		assertNull(information.getDescription()); // Not set
	}

	@Test
	public void testScope() {
		GenericModelScope scope = importer.retrieveScope(sheet);
		assertEquals(12, scope.getProduct().size());
		assertEquals(1, scope.getHazard().size());
		assertEquals(1, scope.getPopulationGroup().size());
		assertNull(scope.getGeneralComment());
		assertNull(scope.getTemporalInformation());
		// TODO: spatial information: String*
	}

	@Test
	public void testDataBackground() {
		GenericModelDataBackground background = importer.retrieveBackground(sheet);
		assertNotNull(background.getStudy());
		assertEquals(3, background.getStudySample().size());
		assertEquals(3, background.getDietaryAssessmentMethod().size());
		assertEquals(3, background.getLaboratory().size());
		assertEquals(3, background.getAssay().size());
	}

	@Test
	public void testModelMath() {
		GenericModelModelMath math = importer.retrieveModelMath(sheet);
		assertEquals(9, math.getParameter().size());
		assertEquals(1, math.getQualityMeasures().size());
		assertNull(math.getModelEquation());
		assertNull(math.getFittingProcedure());
		assertNull(math.getExposure());
		assertNull(math.getEvent());
	}

	@Test
	public void testCreator() throws Exception {

		Contact contact = importer.retrieveCreator(sheet.getRow(3));
		assertNull(contact.getTitle());
		assertEquals("Mesa Varona", contact.getFamilyName());
		assertNull(contact.getGivenName());
		assertEquals("octavio.mesa-varona@bfr.bund.de", contact.getEmail());
		assertNull(contact.getTelephone());
		assertEquals("Alt-Marienfelde 17-21", contact.getStreetAddress());
		assertEquals("Germany", contact.getCountry());
		assertNull(contact.getZipCode());
		assertNull(contact.getRegion());
		assertEquals("BfR", contact.getOrganization());
	}

	@Test
	public void testAuthor() throws Exception {

		Contact contact = importer.retrieveAuthor(sheet.getRow(3));
		assertEquals("Prof", contact.getTitle());
		assertEquals("Mosley", contact.getFamilyName());
		assertEquals("Steve", contact.getGivenName());
		assertEquals("mosley@nyu.org", contact.getEmail());
		assertEquals("080 12345566", contact.getTelephone());
		assertEquals("Berliner Strasse 2", contact.getStreetAddress());
		assertEquals("Saint Vincent and the Grenadines", contact.getCountry());
		assertEquals("12345", contact.getZipCode());
		assertEquals("Greater New Yorker Area", contact.getRegion());
		assertEquals("NYU", contact.getOrganization());
	}

	@Test
	public void testReference() throws Exception {
		Reference reference = importer.retrieveReference(sheet.getRow(14));

		assertTrue(reference.isIsReferenceDescription());
		assertEquals(PublicationTypeEnum.RPRT, reference.getPublicationType().get(0));
		assertEquals(LocalDate.of(2017, 12, 6), reference.getDate());
		assertNull(reference.getPmid());
		assertEquals("10.2903/j.efsa.2018.5134", reference.getDoi());
		assertEquals("authors", reference.getAuthorList());
		assertEquals("Listeria monocytogenes contamination of ready-to-eat\n"
				+ " foods and the risk for human health in the EU", reference.getTitle());
		assertEquals("abstract", reference.getAbstract());
		// TODO: journal
		// TODO: volume
		// TODO: issue
		assertEquals("Published", reference.getStatus());
		assertEquals("www.efsa.europa.eu/efsajournal", reference.getWebsite());
		assertEquals("comment", reference.getComment());
	}

	@Test
	public void testModelCategory() throws Exception {
		ModelCategory modelCategory = importer.retrieveModelCategory(sheet);
		assertEquals("Dose-response model", modelCategory.getModelClass());
		assertNull(modelCategory.getModelSubClass());
		assertNull(modelCategory.getModelClassComment());
		assertNull(modelCategory.getBasicProcess());
	}

	@Test
	public void testProduct() throws Exception {

		// Test product at row 39
		{
			Product product39 = importer.retrieveProduct(sheet.getRow(38));
			assertEquals("Allspice", product39.getName());
			assertEquals("description", product39.getDescription());
			assertEquals("[]", product39.getUnit());
			assertNull(product39.getMethod());
			assertEquals("Aluminium foil - aluminium sheet", product39.getPackaging().get(0));
			assertEquals("Canning", product39.getTreatment().get(0));
			assertEquals("Afghanistan", product39.getOriginCountry());
			assertEquals("A Coruña", product39.getOriginArea());
			assertEquals("Adriatic", product39.getFisheriesArea());
			assertEquals(LocalDate.of(2017, 10, 30), product39.getProductionDate());
			assertEquals(LocalDate.of(2018, 10, 30), product39.getExpiryDate());
		}

		// Test product at row 40
		{
			Product product40 = importer.retrieveProduct(sheet.getRow(39));
			assertEquals("Almonds", product40.getName());
			assertEquals("almonds", product40.getDescription());
			assertEquals("[aw]", product40.getUnit());
			assertEquals("Farmed Domestic or cultivated", product40.getMethod().get(0));
			assertNull(product40.getPackaging());
			assertEquals("Churning", product40.getTreatment().get(0));
			assertEquals("Aland Islands", product40.getOriginCountry());
			assertEquals("Aachen, Kreis", product40.getOriginArea());
			assertEquals("Aegean", product40.getFisheriesArea());
			assertEquals(LocalDate.of(2017, 10, 30), product40.getProductionDate());
			assertEquals(LocalDate.of(2018, 10, 30), product40.getExpiryDate());
		}

		// Test product at row 41
		{
			Product product41 = importer.retrieveProduct(sheet.getRow(40));
			assertEquals("American persimmon (Virginia kaki)", product41.getName());
			assertEquals("american", product41.getDescription());
			assertEquals("[Fluorescence]", product41.getUnit());
			assertEquals("Free range production", product41.getMethod().get(0));
			assertEquals("Blister (film)", product41.getPackaging().get(0));
			assertEquals("Concentration", product41.getTreatment().get(0));
			assertEquals("Albania", product41.getOriginCountry());
			assertEquals("Aachen, Kreisfreie Stadt", product41.getOriginArea());
			assertEquals("Amazon", product41.getFisheriesArea());
			assertEquals(LocalDate.of(2017, 10, 30), product41.getProductionDate());
			assertEquals(LocalDate.of(2018, 10, 30), product41.getExpiryDate());
		}
	}

	@Test
	public void testHazard() throws Exception {
		// Test hazard at row 39
		{
			Hazard hazard39 = importer.retrieveHazard(sheet.getRow(38));
			assertEquals("Biogenic amines", hazard39.getType());
			assertEquals("'Prohexadione (prohexadione (acid) and its salts expressed as prohexadione-calcium)",
					hazard39.getName());
			assertNull(hazard39.getDescription());
			assertEquals("[]", hazard39.getUnit());
			assertEquals("effect", hazard39.getAdverseEffect());
			assertEquals("source", hazard39.getSourceOfContamination());
			assertEquals("BMD", hazard39.getBenchmarkDose());
			assertEquals("MRL", hazard39.getMaximumResidueLimit());
			assertEquals("NOAEL", hazard39.getNoObservedAdverseAffectLevel());
			assertEquals("LOAEL", hazard39.getLowestObservedAdverseAffectLevel());
			assertEquals("AOEL", hazard39.getAcceptableOperatorsExposureLevel());
			assertEquals("ARfD", hazard39.getAcuteReferenceDose());
			assertEquals("ADI", hazard39.getAcceptableDailyIntake());
			assertNull(hazard39.getIndSum());
		}
	}

	@Test
	public void testPopulationGroup() throws Exception {
		// Test population group at row 39
		{
			PopulationGroup populationGroup39 = importer.retrievePopulationGroup(sheet.getRow(38));
			assertEquals("Other", populationGroup39.getName());
			assertEquals("target", populationGroup39.getTargetPopulation());
			assertEquals("span", populationGroup39.getPopulationSpan().get(0));
			assertEquals("description", populationGroup39.getPopulationDescription().get(0));
			assertEquals("age", populationGroup39.getPopulationAge().get(0));
			assertEquals("gender", populationGroup39.getPopulationGender());
			assertEquals("bmi", populationGroup39.getBmi().get(0));
			assertEquals("group", populationGroup39.getSpecialDietGroups().get(0));
			assertEquals("consumption", populationGroup39.getPatternConsumption().get(0));
			assertEquals("A Coruña", populationGroup39.getRegion().get(0));
			assertEquals("Afghanistan", populationGroup39.getCountry().get(0));
			assertEquals("factors", populationGroup39.getPopulationRiskFactor().get(0));
			assertEquals("Season", populationGroup39.getSeason().get(0));
		}
	}

	@Test
	public void testStudy() throws Exception {
		Study study = importer.retrieveStudy(sheet);
		assertEquals("identifier", study.getIdentifier());
		assertEquals("Listeria monocytogenes generic Quantitative Microbiological Risk Assessment (gQMRA) model",
				study.getTitle());
		assertEquals("description", study.getDescription());
		assertEquals("design type", study.getDesignType());
		assertEquals("type", study.getAssayMeasurementType());
		assertEquals("AAS", study.getAssayTechnologyType());
		assertEquals("platform", study.getAssayTechnologyPlatform());
		assertEquals("Internally validated", study.getAccreditationProcedureForTheAssayTechnology());
		assertEquals("name", study.getProtocolName());
		assertEquals("type", study.getProtocolType());
		assertEquals("description", study.getProtocolDescription());
		assertEquals("uri", study.getProtocolURI());
		assertEquals("version", study.getProtocolVersion());
		assertEquals("name", study.getProtocolParametersName());
		assertEquals("name", study.getProtocolComponentsName());
		assertEquals("type", study.getProtocolComponentsType());
	}

	@Test
	public void testStudySample() throws Exception {

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
	public void testDietaryAssessmentMethod() throws Exception {

		// Test DietaryAssessmentMethod from row 104
		DietaryAssessmentMethod method104 = importer.retrieveDietaryAssessmentMethod(sheet.getRow(103));
		assertEquals("24-hour recall interview", method104.getCollectionTool());
		assertEquals("0.0", method104.getNumberOfNonConsecutiveOneDay());
		assertEquals("a", method104.getSoftwareTool());
		assertEquals("a", method104.getNumberOfFoodItems().get(0));
		assertEquals("f", method104.getRecordTypes().get(0));
		assertEquals("(Beet) Sugar", method104.getFoodDescriptors().get(0));

		// Test DietaryAssessmentMethod from row 105
		DietaryAssessmentMethod method105 = importer.retrieveDietaryAssessmentMethod(sheet.getRow(104));
		assertEquals("eating outside questionnaire", method105.getCollectionTool());
		assertEquals("1.0", method105.getNumberOfNonConsecutiveOneDay());
		assertEquals("b", method105.getSoftwareTool());
		assertEquals("b", method105.getNumberOfFoodItems().get(0));
		assertEquals("e", method105.getRecordTypes().get(0));
		assertEquals("(Beet) Sugar", method105.getFoodDescriptors().get(0));

		// Test DietaryAssessmentMethod from row 106
		DietaryAssessmentMethod method106 = importer.retrieveDietaryAssessmentMethod(sheet.getRow(105));
		assertEquals("food diaries", method106.getCollectionTool());
		assertEquals("2.0", method106.getNumberOfNonConsecutiveOneDay());
		assertEquals("c", method106.getSoftwareTool());
		assertEquals("c", method106.getNumberOfFoodItems().get(0));
		assertEquals("d", method106.getRecordTypes().get(0));
		assertEquals("(Beet) Sugar", method106.getFoodDescriptors().get(0));
	}

	@Test
	public void testLaboratory() throws Exception {

		// Test Laboratory at row 111
		Laboratory laboratory111 = importer.retrieveLaboratory(sheet.getRow(110));
		assertEquals("Accredited", laboratory111.getAccreditation().get(0));
		assertEquals("a", laboratory111.getName());
		assertEquals("Afghanistan", laboratory111.getCountry());

		// Test Laboratory at row 112
		Laboratory laboratory112 = importer.retrieveLaboratory(sheet.getRow(111));
		assertEquals("None", laboratory112.getAccreditation().get(0));
		assertEquals("b", laboratory112.getName());
		assertEquals("Aland Islands", laboratory112.getCountry());

		// Test Laboratory at row 113
		Laboratory laboratory113 = importer.retrieveLaboratory(sheet.getRow(112));
		assertEquals("Other", laboratory113.getAccreditation().get(0));
		assertEquals("c", laboratory113.getName());
		assertEquals("Albania", laboratory113.getCountry());
	}

	@Test
	public void testAssay() throws Exception {

		// Test assay at row 118
		Assay assay118 = importer.retrieveAssay(sheet.getRow(117));
		assertEquals("name0", assay118.getName());
		assertEquals("descr0", assay118.getDescription());
		assertEquals("moist0", assay118.getMoisturePercentage());
		assertEquals("fat0", assay118.getFatPercentage());
		assertEquals("detect0", assay118.getDetectionLimit());
		assertEquals("quant0", assay118.getQuantificationLimit());
		assertEquals("range0", assay118.getContaminationRange());
		assertEquals("uncert0", assay118.getUncertaintyValue());

		// Test assay at row 119
		Assay assay119 = importer.retrieveAssay(sheet.getRow(118));
		assertEquals("name1", assay119.getName());
		assertEquals("descr1", assay119.getDescription());
		assertEquals("moist1", assay119.getMoisturePercentage());
		assertEquals("fat1", assay119.getFatPercentage());
		assertEquals("detect1", assay119.getDetectionLimit());
		assertEquals("quant1", assay119.getQuantificationLimit());
		assertEquals("range1", assay119.getContaminationRange());
		assertEquals("uncert1", assay119.getUncertaintyValue());

		// Test assay at row 120
		Assay assay120 = importer.retrieveAssay(sheet.getRow(119));
		assertEquals("name2", assay120.getName());
		assertEquals("descr2", assay120.getDescription());
		assertEquals("moist2", assay120.getMoisturePercentage());
		assertEquals("fat2", assay120.getFatPercentage());
		assertEquals("detect2", assay120.getDetectionLimit());
		assertEquals("quant2", assay120.getQuantificationLimit());
		assertEquals("range2", assay120.getContaminationRange());
		assertEquals("uncert2", assay120.getUncertaintyValue());
	}

	@Test
	public void testParameter() throws Exception {

		// Check parameter at row 133
		Parameter param133 = importer.retrieveParameter(sheet.getRow(132));

		assertEquals("DR_Inputs3", param133.getId());
		assertEquals(ClassificationEnum.INPUT, param133.getClassification());
		assertEquals("DR_Inputs3.csv", param133.getName());
		assertEquals("DR values, relative risk", param133.getDescription());
		assertEquals("[]", param133.getUnit());
		assertEquals("Dimensionless Quantity", param133.getUnitCategory());
		assertEquals(DataTypeEnum.FILE, param133.getDataType());
		assertEquals("Boolean", param133.getSource());
		assertEquals("Boolean", param133.getSubject());
		assertEquals("Boolean", param133.getDistribution());
		assertEquals(
				"C:\\Users\\mesa\\Desktop\\Listeria monocitogenes(KJ) QMRA\\listeria project\\model\\DR\\DR_inputs3.csv",
				param133.getValue());
		// reference
		assertEquals("a", param133.getVariabilitySubject());
		assertEquals("max0", param133.getMaxValue());
		assertEquals("min0", param133.getMinValue());
		assertEquals("error0", param133.getError());
	}
	
	@Test
	public void testQualityMeasures() throws Exception {

		QualityMeasures measures = importer.retrieveQualityMeasures(sheet);
		assertEquals(0.1, measures.getSSE().doubleValue(), .0);
		assertEquals(0.2, measures.getMSE().doubleValue(), .0);
		assertEquals(0.3, measures.getRMSE().doubleValue(), .0);
		assertEquals(0.4, measures.getRsquared().doubleValue(), .0);
		assertEquals(0.5, measures.getAIC().doubleValue(), .0);
		assertEquals(0.6, measures.getBIC().doubleValue(), .0);
	}
}
