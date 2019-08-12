package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Product;

@SuppressWarnings("static-method")
public class PreRakipSheetImporterTest {

	private static Sheet sheet;
	private static PreRakipSheetImporter importer;

	@BeforeClass
	public static void setup() throws Exception {
		final Workbook workbook = WorkbookFactory.create(new File("files/Duarte_MetaData.xlsx"));
		sheet = workbook.getSheetAt(0);
		importer = new PreRakipSheetImporter();
	}

	@Test
	public void testGeneralInformation() {
		final GenericModelGeneralInformation information = importer.retrieveGeneralInformation(sheet);

		assertEquals("Duarte_FittingDistributionToMicrobialCounts", information.getName());
		assertEquals("Duarte_R", information.getIdentifier());
		assertEquals(LocalDate.of(2016, 11, 9), information.getCreationDate());
		assertEquals("Public", information.getRights());
		assertNull(information.getAvailability());
		assertNull(information.getFormat());
		assertEquals(LocalDate.of(2016, 11, 9), information.getModificationDate().get(0));
	}


	@Test
	public void testScope() {

		final GenericModelScope scope = importer.retrieveScope(sheet);

		final Hazard hazard = scope.getHazard().get(0);
		assertEquals("generic_broth", hazard.getName());
		assertNull(hazard.getDescription());

		final Product product = scope.getProduct().get(0);
		assertEquals("lab experiment", product.getName());
		assertEquals("This method fits a zero-inflated Poisson", product.getDescription());
	}

	@Test
	public void testModelMath() {

		final GenericModelModelMath math = importer.retrieveModelMath(sheet);

		// 1st parameter: expected.temp
		final Parameter expectedTemp = math.getParameter().get(0);
		assertEquals("expected.temp", expectedTemp.getId());
		assertEquals("log10(CFU)", expectedTemp.getUnit());
		assertEquals("0.0", expectedTemp.getMinValue());
		assertEquals("12.0", expectedTemp.getMaxValue());

		// 2nd parameter: mu.init
		final Parameter muInit = math.getParameter().get(1);
		assertEquals("mu.init", muInit.getId());
		assertEquals("log10(CFU)", muInit.getUnit());
		assertEquals("0", muInit.getMinValue());
		assertEquals("1000", muInit.getMaxValue());

		// 3rd parameter: sd.init
		final Parameter sdInit = math.getParameter().get(2);
		assertEquals("sd.init", sdInit.getId());
		assertEquals("log10(CFU)", sdInit.getUnit());
		assertEquals("0", sdInit.getMinValue());
		assertEquals("1000", sdInit.getMaxValue());

		// 4th parameter: mu.start
		final Parameter muStart = math.getParameter().get(3);
		assertEquals("mu.start", muStart.getId());
		assertEquals("log10(CFU)", muStart.getUnit());
		assertEquals("0", muStart.getMinValue());
		assertEquals("1000", muStart.getMaxValue());

		// 5th parameter: sd.start
		final Parameter sdStart = math.getParameter().get(4);
		assertEquals("sd.start", sdStart.getId());
		assertEquals("log10(CFU)", sdStart.getUnit());
		assertEquals("0", sdStart.getMinValue());
		assertEquals("1000", sdStart.getMaxValue());
	}
}
