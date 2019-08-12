package metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.pmfml.ModelClass;

@SuppressWarnings("static-method")
public class LegacyMetadataImporterTest {

	@Test
	public void test() throws Exception {
		Sheet sheet0;

		final Path path = Paths.get("files/Duarte_MetaData.xlsx");
		try (InputStream stream = Files.newInputStream(path)) {
			final Workbook workbook = WorkbookFactory.create(stream);
			sheet0 = workbook.getSheetAt(0);
		}

		final LegacyMetadataImporter importer = new LegacyMetadataImporter();
		final FskMetaData metadata = importer.processSpreadsheet(sheet0);

		assertEquals("Duarte_R", metadata.modelId);
		assertEquals("Duarte_FittingDistributionToMicrobialCounts", metadata.modelName);
		assertEquals("generic_broth", metadata.organism);
		assertNull(metadata.organismDetails);
		assertEquals("lab experiment", metadata.matrix);
		assertEquals("This method fits a zero-inflated Poisson", metadata.matrixDetails);
		assertEquals("Duarte, A.S.R.; Filter, M.", metadata.creator);
		assertEquals("asrd@food.dtu.dk", metadata.referenceDescription);

		final LocalDate creationDate = toLocalDate(metadata.createdDate);
		assertEquals(LocalDate.of(2016, 11, 9), creationDate);

		final LocalDate modificationDate = toLocalDate(metadata.modifiedDate);
		assertEquals(LocalDate.of(2016,  11,  9), modificationDate);

		assertEquals("Public", metadata.rights);
		assertNull(metadata.type);
		assertEquals(ModelClass.UNKNOWN, metadata.subject);
		assertEquals("This method fits a zero-inflated Poisson", metadata.notes);

		assertEquals("expected.temp", metadata.dependentVariable.name);
		assertEquals("log10(CFU)", metadata.dependentVariable.unit);
		assertEquals("0.0", metadata.dependentVariable.min);
		assertEquals("12.0", metadata.dependentVariable.max);

		// Check first independent variable: mu.init
		final Variable muInit = metadata.independentVariables.get(0);
		assertEquals("mu.init", muInit.name);
		assertEquals("log10(CFU)", muInit.unit);
		assertEquals("0", muInit.min);
		assertEquals("1000", muInit.max);

		// Check second independent variable: sd.init
		final Variable sdInit = metadata.independentVariables.get(1);
		assertEquals("sd.init", sdInit.name);
		assertEquals("log10(CFU)", sdInit.unit);
		assertEquals("0", sdInit.min);
		assertEquals("1000", sdInit.max);

		// Check third independent variable: mu.start
		final Variable muStart = metadata.independentVariables.get(2);
		assertEquals("mu.start", muStart.name);
		assertEquals("log10(CFU)", muStart.unit);
		assertEquals("0", muStart.min);
		assertEquals("1000", muStart.max);

		// Check fourth independent variable: sd.start
		final Variable sdStart = metadata.independentVariables.get(3);
		assertEquals("sd.start", sdStart.name);
		assertEquals("log10(CFU)", sdStart.unit);
		assertEquals("0", sdStart.min);
		assertEquals("1000", sdStart.max);

		assertFalse(metadata.hasData);
	}

	private static LocalDate toLocalDate(final Date date) {
		final Instant instant = Instant.ofEpochMilli(date.getTime());
		final LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}
}
