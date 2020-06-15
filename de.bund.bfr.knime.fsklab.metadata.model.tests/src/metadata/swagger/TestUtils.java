package metadata.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Parameter;
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
}
