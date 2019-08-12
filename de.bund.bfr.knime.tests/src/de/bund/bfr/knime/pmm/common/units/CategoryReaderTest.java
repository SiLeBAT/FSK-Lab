package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

@SuppressWarnings("static-method")
public class CategoryReaderTest {

	@Test
	public void test() {
		assertFalse(CategoryReader.getInstance().getMap().isEmpty());
	}
}
