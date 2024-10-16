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

import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.ProcessModelScope;

public class ProcessModelSheetImporterTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() throws Exception {
		Sheet sheet;
		try (InputStream stream = Files.newInputStream(Paths.get("files/annotation_v1.0.4.xlsx"));
				Workbook workbook = WorkbookFactory.create(stream)) {
			sheet = workbook.getSheet("Process Model");
		}

		ProcessModel model = (ProcessModel) new ProcessModelSheetImporter().retrieveModel(sheet);
		test(model.getGeneralInformation());
		test(model.getScope());
		test(model.getDataBackground());
		test(model.getModelMath());
	}

	private static void test(PredictiveModelGeneralInformation information) throws Exception {
		assertEquals("Listeria Monocytogenes (DR of gQMRA)", information.getName());
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
		assertEquals(3, information.getReference().size());
		assertEquals("English", information.getLanguage());
		assertEquals("FSK-Lab", information.getSoftware());
		assertEquals("R 3", information.getLanguageWrittenIn());
		assertNotNull(information.getModelCategory());
		assertEquals("Uncurated", information.getStatus());
		assertEquals("Objective", information.getObjective());
		assertEquals("Description", information.getDescription());
		
		TestUtils.testFirstCreator(information.getCreator().get(0));
		TestUtils.testFirstAuthor(information.getAuthor().get(0));
		TestUtils.testFirstReference(information.getReference().get(0));
	}

	private static void test(ProcessModelScope scope) {
		assertEquals(10, scope.getProduct().size());
		assertEquals(1, scope.getHazard().size());
		assertNull(scope.getGeneralComment());
		assertNull(scope.getTemporalInformation());
		// TODO: spatial information: String*
		
		TestUtils.testFirstProduct(scope.getProduct().get(0));
		TestUtils.testFirstHazard(scope.getHazard().get(0));
	}

	private static void test(PredictiveModelDataBackground background) {
		assertNotNull(background.getStudy());
		assertEquals(3, background.getStudySample().size());
		assertEquals(3, background.getLaboratory().size());
		assertEquals(3, background.getAssay().size());

		TestUtils.testFirstAssay(background.getAssay().get(0));
	}

	private static void test(PredictiveModelModelMath math) {
		assertEquals(9, math.getParameter().size());
		assertEquals(1, math.getQualityMeasures().size());
		assertNull(math.getModelEquation());
		assertNull(math.getFittingProcedure());
		assertNull(math.getEvent());

		TestUtils.testFirstParameter(math.getParameter().get(0));
	}
}
