package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

import java.util.Arrays;

import org.junit.Test;

@SuppressWarnings("static-method")
public class CategoriesTest {

	@Ignore
	@Test
	public void testGetAllCategories() {
		assertFalse(Categories.getAllCategories().isEmpty());
	}

	@Test
	public void testGetCategory() {
		assertTrue(Categories.getCategory(null) instanceof NoCategory);
		assertTrue(Categories.getCategory("illegal category") instanceof NoCategory);
	}

	@Test
	public void testGetCategoryByUnit() {
		final String phUnit = Categories.getPhUnit();
		assertNotNull(Categories.getCategoryByUnit(phUnit));
	}

	@Test
	public void testGetUnitsFromCategories() {
		assertTrue(Categories.getUnitsFromCategories(null).isEmpty());

		final Category timeCategory = Categories.getTimeCategory();
		assertFalse(Categories.getUnitsFromCategories(Arrays.asList(timeCategory)).isEmpty());
	}

	@Test
	public void testGetTime() {
		assertEquals("Time", Categories.getTime());
	}

	@Test
	public void testGetTimeCategory() {
		assertNotNull(Categories.getTimeCategory());
	}

	@Test
	public void testGetConcentrationCategories() {
		assertFalse(Categories.getConcentrationCategories().isEmpty());
	}

	@Test
	public void testGetTempCategory() {
		assertNotNull(Categories.getTempCategory());
	}

	@Test
	public void testGetPhCategory() {
		assertNotNull(Categories.getPhCategory());
	}

	@Test
	public void testGetAwCategory() {
		assertNotNull(Categories.getAwCategory());
	}

	@Test
	public void testGetPhUnit() {
		assertEquals("[pH]", Categories.getPhUnit());
	}

	@Test
	public void testGetAwUnit() {
		assertEquals("[aw]", Categories.getAwUnit());
	}
}
