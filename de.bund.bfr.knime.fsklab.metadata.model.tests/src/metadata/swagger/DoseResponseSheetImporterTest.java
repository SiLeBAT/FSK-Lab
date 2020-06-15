package metadata.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DoseResponseModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModelScope;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;

public class DoseResponseSheetImporterTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() throws Exception {
		Sheet sheet;
		try (InputStream stream = Files.newInputStream(Paths.get("files/annotation_v1.0.4.xlsx"));
				Workbook workbook = WorkbookFactory.create(stream)) {
			sheet = workbook.getSheet("Dose-Response Model");
		}
		DoseResponseModel model = (DoseResponseModel) new DoseResponseSheetImporter().retrieveModel(sheet);
		test(model.getGeneralInformation());
		test(model.getScope());
		test(model.getDataBackground());
		test(model.getModelMath());
	}

	private static void test(DoseResponseModelGeneralInformation information) throws Exception {
		assertEquals("Listeria Monocytogenes (DR of gQMRA)", information.getModelName());
		assertEquals("PUBLISHED SCIENTIFIC STUDIES", information.getSource());
		assertEquals("DR000001", information.getIdentifier());
		assertEquals(6, information.getAuthor().size());
		assertEquals(6, information.getCreator().size());
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

	private static void test(DoseResponseModelScope scope) {
		assertEquals(1, scope.getHazard().size());
		assertEquals(1, scope.getPopulationGroup().size());
		assertNull(scope.getGeneralComment());
		assertNull(scope.getTemporalInformation());
		// TODO: spatial information: String*
	}

	private static void test(PredictiveModelDataBackground background) {
		assertNotNull(background.getStudy());
		assertEquals(3, background.getStudySample().size());
		assertEquals(3, background.getLaboratory().size());
		assertEquals(3, background.getAssay().size());
		
		TestUtils.testFirstStudySample(background.getStudySample().get(0));
	}

	private static void test(DoseResponseModelModelMath math) {
		assertEquals(9, math.getParameter().size());
		assertEquals(1, math.getQualityMeasures().size());
		assertNull(math.getModelEquation());
		assertNull(math.getFittingProcedure());
		assertNull(math.getExposure());
		assertNull(math.getEvent());
		
		TestUtils.testFirstParameter(math.getParameter().get(0));
	}
}
