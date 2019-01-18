/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParametricModelTest {

	@Test
	public void testEmptyConstructor() {
		ParametricModel model = new ParametricModel();

		assertTrue(Double.isNaN(model.getRss()));
		assertTrue(Double.isNaN(model.getRsquared()));
		assertTrue(Double.isNaN(model.getRms()));
		assertTrue(Double.isNaN(model.getAic()));
		assertTrue(Double.isNaN(model.getBic()));
		assertNull(model.isChecked());
		assertNull(model.getQualityScore());
		assertNull(model.getComment());
		assertTrue(model.getModelId() < 0);
		assertTrue(model.getEstModelId() < 0);
		assertTrue(model.getGlobalModelId() < 0);
		assertTrue(model.getEstModelLit().size() == 0);
		assertTrue(model.getModelLit().size() == 0);
		assertTrue(model.getIndependent().size() == 0);
		assertTrue(model.getParameter().size() == 0);
	}

	/**
	 * Test constructor with modelName, formula, depXml, level, modelId and
	 * estModelId.
	 */
	@Test
	public void testConstructor2() {

		DepXml dep = new DepXml("dep");

		ParametricModel model = new ParametricModel("modelName", "formula", dep, 0, 1, 2);

		assertEquals("modelName", model.getModelName());
		assertEquals("formula", model.getFormula());
		assertEquals("dep", model.getDepXml().name);
		assertEquals(0, model.getLevel());
		assertEquals(1, model.getModelId());
		assertEquals(2, model.getEstModelId());

		assertTrue(Double.isNaN(model.getRss()));
		assertTrue(Double.isNaN(model.getRsquared()));
		assertTrue(Double.isNaN(model.getRms()));
		assertTrue(Double.isNaN(model.getAic()));
		assertTrue(Double.isNaN(model.getBic()));
		assertNull(model.isChecked());
		assertNull(model.getQualityScore());
		assertNull(model.getComment());
		assertTrue(model.getGlobalModelId() < 0);
		assertTrue(model.getEstModelLit().size() == 0);
		assertTrue(model.getModelLit().size() == 0);
		assertTrue(model.getIndependent().size() == 0);
		assertTrue(model.getParameter().size() == 0);
	}

	@Test
	public void testGlobalModelId() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns a random negative int to globalModelId
		assertTrue(model.getGlobalModelId() < 0);

		// Check change
		model.setGlobalModelId(1);
		assertEquals(1, model.getGlobalModelId().intValue());
	}

	@Test
	public void testWarning() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns empty string to warning
		assertTrue(model.getWarning().isEmpty());

		// Check change
		model.setWarning("Some warning");
		assertEquals("Some warning", model.getWarning());
	}

	@Test
	public void testComment() throws Exception {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to comment
		assertNull(model.getComment());

		// Check change
		model.setComment("Some comment");
		assertEquals("Some comment", model.getComment());
	}

	@Test
	public void testQualityScore() throws Exception {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to qualityScore
		assertNull(model.getQualityScore());

		// Check change
		model.setQualityScore(9001);
		// Must be over 9000!!
		assertEquals(9001, model.getQualityScore().intValue());
	}

	@Test
	public void testModelId() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns a random negative number to modelId
		assertTrue(model.getModelId() < 0);

		// Check change
		model.setModelId(0);
		assertEquals(0, model.getModelId());
	}

	@Test
	public void testModelName() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to modelName
		assertNull(model.getModelName());

		// Check change
		model.setModelName("Some name");
		assertEquals("Some name", model.getModelName());
	}

	@Test
	public void testModelClass() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to modelClass
		assertNull(model.getModelClass());

		// Check change
		model.setModelClass(0);
		assertEquals(0, model.getModelClass().intValue());
	}

	@Test
	public void testMDbUuid() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to m_dbuuid
		assertNull(model.getMDbUuid());

		// Check change
		model.setMDbUuid("id");
		assertEquals("id", model.getMDbUuid());
	}

	@Test
	public void testEMDbUuid() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to m_dbuuid
		assertNull(model.getEMDbUuid());

		// Check change
		model.setEMDbUuid("id");
		assertEquals("id", model.getEMDbUuid());
	}

	@Test
	public void testCondId() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns 0 to condId
		assertEquals(0, model.getCondId());

		// Check change
		model.setCondId(7);
		assertEquals(7, model.getCondId());
	}

	@Test
	public void testAic() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns Double#NAN to aic
		assertTrue(Double.isNaN(model.getAic()));
		assertFalse(model.hasAic());

		// Change AIC
		model.setAic(0.5);
		assertEquals(0.5, model.getAic(), .0);
		assertTrue(model.hasAic());

		// Unset AIC
		model.setAic(null);
		assertTrue(Double.isNaN(model.getAic()));
		assertFalse(model.hasAic());
	}

	@Test
	public void testBic() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns Double#NAN to aic
		assertTrue(Double.isNaN(model.getBic()));
		assertFalse(model.hasBic());

		// Change Bic
		model.setBic(0.5);
		assertEquals(0.5, model.getBic(), .0);
		assertTrue(model.hasBic());

		// Unset Bic
		model.setBic(null);
		assertTrue(Double.isNaN(model.getBic()));
		assertFalse(model.hasBic());
	}

	@Test
	public void testRMS() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns Double#NAN to aic
		assertTrue(Double.isNaN(model.getRms()));
		assertFalse(model.hasRms());

		// Change Rms
		model.setRms(0.5);
		assertEquals(0.5, model.getRms(), .0);
		assertTrue(model.hasRms());

		// Unset Rms
		model.setRms(null);
		assertTrue(Double.isNaN(model.getRms()));
		assertFalse(model.hasRms());
	}

	@Test(expected = PmmException.class)
	public void testSetRmsInfinite() {
		ParametricModel model = new ParametricModel();
		model.setRms(Double.POSITIVE_INFINITY);
	}

	@Test(expected = PmmException.class)
	public void testSetRmsNegative() {
		ParametricModel model = new ParametricModel();
		model.setRms(-1.0);
	}

	@Test
	public void testChecked() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to isChecked
		assertNull(model.isChecked());

		// Check change
		model.setChecked(true);
		assertTrue(model.isChecked());
	}

	@Test
	public void testFittedModelName() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns null to fittedModelName
		assertNull(model.getFittedModelName());

		// Check change
		model.setFittedModelName("name");
		assertEquals("name", model.getFittedModelName());
	}

	@Test
	public void testRss() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns Double.NAN to rss
		assertTrue(Double.isNaN(model.getRss()));

		// Check change
		model.setRss(0.5);
		assertEquals(0.5, model.getRss(), .0);
	}

	@Test
	public void testRssNull() {
		ParametricModel model = new ParametricModel();
		model.setRss(null);
		assertTrue(Double.isNaN(model.getRss()));
	}

	@Test(expected = PmmException.class)
	public void testRssInfinite() {
		ParametricModel model = new ParametricModel();
		model.setRss(Double.POSITIVE_INFINITY);
	}

	@Test(expected = PmmException.class)
	public void testRssNegative() {
		ParametricModel model = new ParametricModel();
		model.setRss(-1.0);
	}

	@Test
	public void testRSquared() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns Double.NaN
		assertTrue(Double.isNaN(model.getRsquared()));

		// Check change
		model.setRsquared(0.7);
		assertEquals(0.7, model.getRsquared(), .0);

		// Null is replaced with Double.NaN
		model.setRsquared(null);
		assertTrue(Double.isNaN(model.getRsquared()));
	}

	@Test(expected = PmmException.class)
	public void testSetRsquaredException() {
		ParametricModel model = new ParametricModel();
		model.setRsquared(2.0);
	}

	@Test
	public void testEstModelId() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns a random negative integer
		assertTrue(model.getEstModelId() < 0);

		// Check change
		model.setEstModelId(0);
		assertEquals(0, model.getEstModelId());
	}

	@Test
	public void testIndependent() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns empty PmmXmlDoc
		assertEquals(0, model.getIndependent().size());

		// Check setIndependent
		model.setIndependent(null);
		assertNull(model.getIndependent());
	}

	@Test
	public void testSetters() {
		ParametricModel model = new ParametricModel();

		// When the parameter does not exist the setters do nothing.
		model.setIndepDescription("aloha", "description");
		model.setIndepMin("aloha", 0.0);
		model.setIndepMax("aloha", 1.0);
		model.setIndepUnit("aloha", "unit");
		model.setIndepCategory("aloha", "category");
		
		// and the getters return null
		assertNull(model.getIndepDescription("aloha"));
		assertNull(model.getIndepMin("aloha"));
		assertNull(model.getIndepMax("aloha"));
		assertNull(model.getIndepUnit("aloha"));
		assertNull(model.getIndepCategory("aloha"));

		// Check with existing parameter
		model.addIndepVar("aloha");

		model.setIndepDescription("aloha", "description");
		assertEquals("description", model.getIndepDescription("aloha"));

		model.setIndepMin("aloha", 0.0);
		assertEquals(0.0, model.getIndepMin("aloha"), .0);

		model.setIndepMax("aloha", 1.0);
		assertEquals(1.0, model.getIndepMax("aloha"), .0);

		model.setIndepUnit("aloha", "unit");
		assertEquals("unit", model.getIndepUnit("aloha"));
		
		model.setIndepCategory("aloha", "category");
		assertEquals("category", model.getIndepCategory("aloha"));
	}
}
