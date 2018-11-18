package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		assertEquals(id, catalogModel.getId().intValue());
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
		assertEquals(modelClass, catalogModel.getModelClass().intValue());
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
		
		assertEquals(id, settings.getInt("id"));
		assertEquals(name, settings.getString("name"));
		assertEquals(formula, settings.getString("formula"));
		assertEquals(modelClass, settings.getInt("modelClass"));
		assertEquals(comment, settings.getString("comment"));
		assertEquals(dbuuid, settings.getString("dbuuid"));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("name", name);
		settings.addString("formula", formula);
		settings.addInt("modelClass", modelClass);
		settings.addString("comment", comment);
		settings.addString("dbuuid", dbuuid);
		
		CatalogModel catalogModel = new CatalogModel();
		catalogModel.loadFromNodeSettings(settings);
		
		assertEquals(id, catalogModel.getId().intValue());
		assertEquals(name, catalogModel.getName());
		assertEquals(formula, catalogModel.getFormula());
		assertEquals(modelClass, catalogModel.getModelClass().intValue());
		assertEquals(comment, catalogModel.getComment());
		assertEquals(dbuuid, catalogModel.getDbuuid());
	}
	
	@Test
	public void testToCatalogModel() {
		CatalogModelXml catalogModelXml = new CatalogModelXml(id, name, formula, modelClass, dbuuid);
		catalogModelXml.comment = comment;
		CatalogModel catalogModel = CatalogModel.toCatalogModel(catalogModelXml);
		
		assertEquals(id, catalogModel.getId().intValue());
		assertEquals(name, catalogModel.getName());
		assertEquals(formula, catalogModel.getFormula());
		assertEquals(modelClass, catalogModel.getModelClass().intValue());
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
		
		assertEquals(id, catalogModelXml.id.intValue());
		assertEquals(name, catalogModelXml.name);
		assertEquals(formula, catalogModelXml.formula);
		assertEquals(modelClass, catalogModelXml.modelClass.intValue());
		assertEquals(comment, catalogModelXml.comment);
		assertEquals(dbuuid, catalogModelXml.dbuuid);
	}
}
