package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

public class CatalogModelTest {

	static int id = 77;
	static String name = "Baranyi model (Baranyi and Roberts,1995) - iPMP Full Growth Models Eq 6_outdated";
	static String formula = "Value=Y0+mu_max*Time+ln(exp(-mu_max*Time)+exp(-h0)-exp(-mu_max*Time-h0))-ln(1+((exp(mu_max*(Time+(1/mu_max)*ln(exp(-mu_max*Time)+exp(-h0)-exp(-mu_max*Time-h0))))-1)/(exp(Ymax-Y0))))*((((((Ymax&gt;Y0)*(mu_max&gt;=0))))))";
	static int modelClass = 1;
	static String comment = "some comment";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";

	@Test
	public void testId() {
		CatalogModel catalogModel = new CatalogModel();
		assertNull(catalogModel.getId());

		catalogModel.setId(id);
		assertTrue(id == catalogModel.getId());
	}

	@Test
	public void testName() {
		CatalogModel catalogModel = new CatalogModel();
		assertNull(catalogModel.getName());
		
		catalogModel.setName(name);
		assertEquals(name, catalogModel.getName());
	}

	@Test
	public void testFormula() {
		CatalogModel catalogModel = new CatalogModel();
		assertNull(catalogModel.getFormula());
		
		catalogModel.setFormula(formula);
		assertEquals(formula, catalogModel.getFormula());
	}

	@Test
	public void testModelClass() {
		CatalogModel catalogModel = new CatalogModel();
		assertNull(catalogModel.getModelClass());
		
		catalogModel.setModelClass(modelClass);
		assertTrue(modelClass == catalogModel.getModelClass());
	}

	@Test
	public void testComment() {
		CatalogModel catalogModel = new CatalogModel();
		assertNull(catalogModel.getComment());
		
		catalogModel.setComment(comment);
		assertEquals(comment, catalogModel.getComment());
	}

	@Test
	public void testDbuuid() {
		CatalogModel catalogModel = new CatalogModel();
		assertNull(catalogModel.getDbuuid());
		
		catalogModel.setDbuuid(dbuuid);
		assertEquals(dbuuid, catalogModel.getDbuuid());
	}
	
	@Test
	public void testSaveToNodeSettings() throws Exception {
		CatalogModel catalogModel = new CatalogModel();
		catalogModel.setId(id);
		catalogModel.setName(name);
		catalogModel.setFormula(formula);
		catalogModel.setModelClass(modelClass);
		catalogModel.setComment(comment);
		catalogModel.setDbuuid(dbuuid);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		catalogModel.saveToNodeSettings(settings);
		
		assertTrue(id == settings.getInt(CatalogModel.ID));
		assertEquals(name, settings.getString(CatalogModel.NAME));
		assertEquals(formula, settings.getString(CatalogModel.FORMULA));
		assertTrue(modelClass == settings.getInt(CatalogModel.MODEL_CLASS));
		assertEquals(comment, settings.getString(CatalogModel.COMMENT));
		assertEquals(dbuuid, settings.getString(CatalogModel.DBUUID));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(CatalogModel.ID, id);
		settings.addString(CatalogModel.NAME, name);
		settings.addString(CatalogModel.FORMULA, formula);
		settings.addInt(CatalogModel.MODEL_CLASS, modelClass);
		settings.addString(CatalogModel.COMMENT, comment);
		settings.addString(CatalogModel.DBUUID, dbuuid);
		
		CatalogModel catalogModel = new CatalogModel();
		catalogModel.loadFromNodeSettings(settings);
		
		assertTrue(id == catalogModel.getId());
		assertEquals(name, catalogModel.getName());
		assertEquals(formula, catalogModel.getFormula());
		assertTrue(modelClass == catalogModel.getModelClass());
		assertEquals(comment, catalogModel.getComment());
		assertEquals(dbuuid, catalogModel.getDbuuid());
	}
	
	@Test
	public void testToCatalogModel() {
		CatalogModelXml catalogModelXml = new CatalogModelXml(id, name, formula, modelClass, dbuuid);
		catalogModelXml.comment = comment;
		CatalogModel catalogModel = CatalogModel.toCatalogModel(catalogModelXml);
		
		assertTrue(id == catalogModel.getId());
		assertEquals(name, catalogModel.getName());
		assertEquals(formula, catalogModel.getFormula());
		assertTrue(modelClass == catalogModel.getModelClass());
		assertEquals(comment, catalogModel.getComment());
		assertEquals(dbuuid, catalogModel.getDbuuid());
	}
	
	@Test
	public void testToCatalogModelXml() {
		CatalogModel catalogModel = new CatalogModel();
		catalogModel.setId(id);
		catalogModel.setName(name);
		catalogModel.setFormula(formula);
		catalogModel.setModelClass(modelClass);
		catalogModel.setComment(comment);
		catalogModel.setDbuuid(dbuuid);
		CatalogModelXml catalogModelXml = catalogModel.toCatalogModelXml();
		
		assertTrue(id == catalogModelXml.id);
		assertEquals(name, catalogModelXml.name);
		assertEquals(formula, catalogModelXml.formula);
		assertTrue(modelClass == catalogModelXml.modelClass);
		assertEquals(comment, catalogModelXml.comment);
		assertEquals(dbuuid, catalogModelXml.dbuuid);
	}
}
