package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DoseResponseModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModelScope;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;

public class SwaggerDoseResponseSheetImporterTest {

	private static Sheet sheet;
	private static SwaggerDoseResponseSheetImporter importer;

	@BeforeClass
	public static void setup() throws Exception {
		sheet = SwaggerUtilTest.workbook.getSheet("Dose-Response Model");
		importer = new SwaggerDoseResponseSheetImporter();
	}

	@Test
	public void testGeneralInformation() throws Exception {
		DoseResponseModelGeneralInformation information = importer.retrieveGeneralInformation(sheet);
		assertEquals("Listeria Monocytogenes (DR of gQMRA)", information.getModelName());
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
		DoseResponseModelScope scope = importer.retrieveScope(sheet);
		assertEquals(1, scope.getHazard().size());
		assertEquals(1, scope.getPopulationGroup().size());
		assertNull(scope.getGeneralComment());
		assertNull(scope.getTemporalInformation());
		// TODO: spatial information: String*
	}

	@Test
	public void testDataBackground() {
		PredictiveModelDataBackground background = importer.retrieveBackground(sheet);
		assertNotNull(background.getStudy());
		assertEquals(3, background.getStudySample().size());
		assertEquals(3, background.getLaboratory().size());
		assertEquals(3, background.getAssay().size());
	}

	@Test
	public void testModelMath() {
		DoseResponseModelModelMath math = importer.retrieveModelMath(sheet);
		assertEquals(9, math.getParameter().size());
		assertEquals(1, math.getQualityMeasures().size());
		assertNull(math.getModelEquation());
		assertNull(math.getFittingProcedure());
		assertNull(math.getExposure());
		assertNull(math.getEvent());
	}
}