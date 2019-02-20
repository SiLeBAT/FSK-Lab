package de.bund.bfr.knime.pmm.common.units;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CategoriesTest {

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
		String phUnit = Categories.getPhUnit();
		assertNotNull(Categories.getCategoryByUnit(phUnit));
	}
	
	@Test
	public void testGetUnitsFromCategories() {
		assertTrue(Categories.getUnitsFromCategories(null).isEmpty());
		
		Category timeCategory = Categories.getTimeCategory();
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