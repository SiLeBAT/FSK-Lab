package de.bund.bfr.knime.fsklab.metadata.model.tests;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TestUtils {

	public static LocalDate toLocalDate(final Date date) {
		final Instant instant = Instant.ofEpochMilli(date.getTime());
		return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
