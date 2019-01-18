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
		assertNull(model.isChecked);
		assertNull(model.qualityScore);
		assertNull(model.comment);
		assertTrue(model.modelId < 0);
		assertTrue(model.getEstModelId() < 0);
		assertTrue(model.globalModelId < 0);
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
		assertEquals(1, model.modelId);
		assertEquals(2, model.getEstModelId());

		assertTrue(model.warning.isEmpty());
		assertTrue(Double.isNaN(model.getRss()));
		assertTrue(Double.isNaN(model.getRsquared()));
		assertTrue(Double.isNaN(model.getRms()));
		assertTrue(Double.isNaN(model.getAic()));
		assertTrue(Double.isNaN(model.getBic()));
		assertNull(model.isChecked);
		assertNull(model.qualityScore);
		assertNull(model.comment);
		assertTrue(model.globalModelId < 0);
		assertTrue(model.getEstModelLit().size() == 0);
		assertTrue(model.getModelLit().size() == 0);
		assertTrue(model.getIndependent().size() == 0);
		assertTrue(model.getParameter().size() == 0);
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
	public void testDependent() {
		ParametricModel model = new ParametricModel();
		
		// Empty constructor assigns null
		assertNull(model.getDepXml());
		
		// When the dep is not set the getters return null
		assertNull(model.getDepVar());
		assertNull(model.getDepDescription());
		assertNull(model.getDepCategory());
		assertNull(model.getDepUnit());
		
		// Setters ignore null
		model.setDepVar(null);
		assertNull(model.getDepVar());
		
		model.setDepDescription(null);
		assertNull(model.getDepDescription());
		
		model.setDepCategory(null);
		assertNull(model.getDepCategory());
		
		model.setDepUnit(null);
		assertNull(model.getDepUnit());
		
		model.setDepXml(new DepXml("lalala"));
		assertEquals("lalala", model.getDepXml().name);
		
		model.setDepVar("Atreus");
		assertEquals("Atreus", model.getDepVar());
		
		model.setDepDescription("Boy");
		assertEquals("Boy", model.getDepDescription());
		
		model.setDepCategory("Nordic god");
		assertEquals("Nordic god", model.getDepCategory());
		
		model.setDepUnit("Half Kratos");
		assertEquals("Half Kratos", model.getDepUnit());
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
	public void testIndependentSetters() {
		ParametricModel model = new ParametricModel();
		model.addIndepVar("indep");

		// Add a non-independent object. Getters/setters must ignore it.
		model.getIndependent().add(new MatrixXml());

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
		model.addIndepVar("odin");

		model.setIndepDescription("odin", "description");
		assertEquals("description", model.getIndepDescription("odin"));

		model.setIndepMin("odin", 0.0);
		assertEquals(0.0, model.getIndepMin("odin"), .0);

		model.setIndepMax("odin", 1.0);
		assertEquals(1.0, model.getIndepMax("odin"), .0);

		model.setIndepUnit("odin", "unit");
		assertEquals("unit", model.getIndepUnit("odin"));

		model.setIndepCategory("odin", "category");
		assertEquals("category", model.getIndepCategory("odin"));
		
		assertFalse(model.containsIndep("aloha"));
		assertTrue(model.containsIndep("odin"));
	}

	@Test
	public void testParameter() {
		ParametricModel model = new ParametricModel();

		// Empty constructor assigns empty PmmXmlDoc
		assertEquals(0, model.getParameter().size());

		// Check setParameter
		model.setParameter(null);
		assertNull(model.getParameter());
	}

	@Test
	public void testParameterSetters() {
		ParametricModel model = new ParametricModel();
		model.addParam("param", false);

		// Add a non-parameter object. Getters/setters must ignore it.
		model.getParameter().add(new AgentXml());

		// When the parameter does not exist the setters do nothing
		model.setParamDescription("aloha", "description");
		model.setParamMin("aloha", 0.0);
		model.setParamMax("aloha", 1.0);
		model.setParamUnit("aloha", "unit");
		model.setParamCategory("aloha", "category");
		
		// and the getters return null
		assertNull(model.getParamDescription("aloha"));
		assertNull(model.getParamMin("aloha"));
		assertNull(model.getParamMax("aloha"));
		assertNull(model.getParamUnit("aloha"));
		assertNull(model.getParamCategory("aloha"));
		assertNull(model.getParamIsStart("aloha"));
		assertNull(model.getParamValue("aloha"));
		assertNull(model.getParamError("aloha"));
		assertNull(model.getParamP("aloha"));
		assertNull(model.getParamT("aloha"));

		// Check with existing parameter
		model.addParam("Baldur", true);

		model.setParamDescription("Baldur", "description");
		assertEquals("description", model.getParamDescription("Baldur"));

		model.setParamMin("Baldur", 0.0);
		assertEquals(0.0, model.getParamMin("Baldur"), .0);

		model.setParamMax("Baldur", 1.0);
		assertEquals(1.0, model.getParamMax("Baldur"), .0);

		model.setParamUnit("Baldur", "unit");
		assertEquals("unit", model.getParamUnit("Baldur"));

		model.setParamCategory("Baldur", "category");
		assertEquals("category", model.getParamCategory("Baldur"));
		
		model.setParamIsStart("Baldur", false);
		assertFalse(model.getParamIsStart("Baldur"));
		
		model.setParamValue("Baldur", 1.0);
		assertEquals(1.0, model.getParamValue("Baldur"), .0);
		
		model.setParamError("Baldur", 0.5);
		assertEquals(0.5, model.getParamError("Baldur"), .0);
		
		assertNull(model.getParamP("Baldur"));
		assertNull(model.getParamT("Baldur"));
		
		assertFalse(model.containsParam("aloha"));
		assertTrue(model.containsParam("Baldur"));
	}
}
