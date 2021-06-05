package metadata.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.StudySample;

class TestUtils {

	private TestUtils() {
	}

	static void testFirstCreator(Contact firstCreator) {
		assertEquals("Robert", firstCreator.getGivenName());
		assertEquals("Lang", firstCreator.getFamilyName());
		assertEquals("Merry-Go-Round", firstCreator.getOrganization());
		assertEquals("061 770 54 22", firstCreator.getTelephone());
		assertEquals("RobertLang@gustr.com", firstCreator.getEmail());
		assertEquals("Switzerland", firstCreator.getCountry());
		assertEquals("4414", firstCreator.getZipCode());
		assertEquals("Im Sandbüel 38", firstCreator.getStreetAddress());
	}

	static void testFirstAuthor(Contact firstAuthor) {
		assertEquals("Marko", firstAuthor.getGivenName());
		assertEquals("Wurfel", firstAuthor.getFamilyName());
		assertEquals("Weatherill's", firstAuthor.getOrganization());
		assertEquals("02636 67 00 13", firstAuthor.getTelephone());
		assertEquals("MarkoWurfel@gustr.com", firstAuthor.getEmail());
		assertEquals("Germany", firstAuthor.getCountry());
		assertEquals("56651", firstAuthor.getZipCode());
		assertEquals("Wallstrasse 51", firstAuthor.getStreetAddress());
	}
	
	static void testFirstParameter(Parameter firstParameter) {
		assertEquals("DR_Inputs3", firstParameter.getId());
		assertEquals(Parameter.ClassificationEnum.INPUT, firstParameter.getClassification());
		assertEquals("DR_Inputs3.csv", firstParameter.getName());
		assertEquals("[]", firstParameter.getUnit());
		assertEquals("Dimensionless Quantity", firstParameter.getUnitCategory());
		assertEquals(Parameter.DataTypeEnum.FILE, firstParameter.getDataType());
		assertEquals("Boolean", firstParameter.getSource());
		assertEquals("Boolean", firstParameter.getSubject());
		assertEquals("Boolean", firstParameter.getDistribution());
		assertEquals("\"DR_inputs3.csv\"", firstParameter.getValue());
		assertNull(firstParameter.getReference()); // reference is not implemented yet
		assertEquals("a", firstParameter.getVariabilitySubject());
		assertEquals("max0", firstParameter.getMaxValue());
		assertEquals("min0", firstParameter.getMinValue());
		assertEquals("error0", firstParameter.getError());
	}
	
	static void testFirstStudySample(StudySample sample) {
		assertEquals("name", sample.getSampleName());
		assertEquals("collection", sample.getProtocolOfSampleCollection());
		assertEquals("Census", sample.getSamplingStrategy());
		assertEquals("According to 97/747/EC", sample.getSamplingMethod());
		assertEquals("plan", sample.getSamplingPlan());
		assertEquals("a", sample.getSamplingWeight());
		assertEquals("b", sample.getSamplingSize());
		assertEquals("[aw]", sample.getLotSizeUnit());
		assertEquals("Air transport", sample.getSamplingPoint());
	}
	
	static void testFirstHazard(Hazard hazard) {
		assertEquals("Biogenic amines", hazard.getType());
		assertEquals("'Prohexadione (prohexadione (acid) and its salts expressed as prohexadione-calcium)", hazard.getName());
		assertNull(hazard.getDescription());
		assertEquals("[]", hazard.getUnit());
		assertEquals("effect", hazard.getAdverseEffect());
		assertEquals("source", hazard.getSourceOfContamination());
		assertEquals("BMD", hazard.getBenchmarkDose());
		assertEquals("MRL", hazard.getMaximumResidueLimit());
		assertEquals("NOAEL", hazard.getNoObservedAdverseAffectLevel());
		assertEquals("LOAEL", hazard.getLowestObservedAdverseAffectLevel());
		assertEquals("AOEL", hazard.getAcceptableOperatorsExposureLevel());
		assertEquals("ARfD", hazard.getAcuteReferenceDose());
		assertEquals("ADI", hazard.getAcceptableDailyIntake());
		assertNull(hazard.getIndSum());
	}
	
	static void testFirstProduct(Product product) {
		assertEquals("Allspice", product.getName());
		assertEquals("description", product.getDescription());
		assertEquals("[]", product.getUnit());
		assertNull(product.getMethod());
		assertEquals("Aluminium foil - aluminium sheet", product.getPackaging().get(0));
		assertEquals("Canning", product.getTreatment().get(0));
		assertEquals("Afghanistan", product.getOriginCountry());
		assertEquals("A Coruña", product.getOriginArea());
		assertEquals("Adriatic", product.getFisheriesArea());
		assertEquals(LocalDate.of(2017, 11, 30), product.getProductionDate());
		assertEquals(LocalDate.of(2018,  11, 30), product.getExpiryDate());
	}
	
	static void testFirstAssay(Assay assay) {
		assertEquals("name0", assay.getName());
		assertEquals("descr0", assay.getDescription());
		assertEquals("moist0", assay.getMoisturePercentage());
		assertEquals("fat0", assay.getFatPercentage());
		assertEquals("detect0", assay.getDetectionLimit());
		assertEquals("quant0", assay.getQuantificationLimit());
		assertEquals("left0", assay.getLeftCensoredData());
		assertEquals("range0", assay.getContaminationRange());
		assertEquals("uncert0", assay.getUncertaintyValue());
	}
	
	static void testFirstPopulationGroup(PopulationGroup group) {
		assertEquals("infant food", group.getName());
		assertEquals("target", group.getTargetPopulation());
		assertEquals("span", group.getPopulationSpan().get(0));
		assertEquals("description", group.getPopulationDescription().get(0));
		assertEquals("age", group.getPopulationAge().get(0));
		assertEquals("gender", group.getPopulationGender());
		assertEquals("bmi", group.getBmi().get(0));
		assertEquals("consumption", group.getPatternConsumption().get(0));
		assertEquals("A Coruña", group.getRegion().get(0));
		assertEquals("factors", group.getPopulationRiskFactor().get(0));
		assertEquals("Season", group.getSeason().get(0));
	}
	
	static void testFirstReference(Reference reference) {
		assertTrue(reference.isIsReferenceDescription());
		assertNull(reference.getPublicationType());
		assertEquals("2002", reference.getDate());
		assertNull(reference.getPmid());
		assertEquals("10.1136/bmj.324.7334.429/g", reference.getDoi());
		assertEquals("authors", reference.getAuthorList());
		assertEquals("Norman Douglas Wayne", reference.getTitle());
		assertEquals("abstract", reference.getAbstract());
		assertEquals("Published", reference.getStatus());
		assertEquals("www.efsa.europa.eu/efsajournal", reference.getWebsite());
		assertEquals("comment", reference.getComment());
	}
}
