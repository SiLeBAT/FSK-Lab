package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

@SuppressWarnings("static-method")
public class CatalogModelTest {

	private static CatalogModel catalogModel;
	static {
		catalogModel = new CatalogModel();
		catalogModel.id = 77;
		catalogModel.name = "Baranyi model (Baranyi and Roberts,1995) - iPMP Full Growth Models Eq 6_outdated";
		catalogModel.formula = "Value=Y0+mu_max*Time+ln(exp(-mu_max*Time)+exp(-h0)-exp(-mu_max*Time-h0))-ln(1+((exp(mu_max*(Time+(1/mu_max)*ln(exp(-mu_max*Time)+exp(-h0)-exp(-mu_max*Time-h0))))-1)/(exp(Ymax-Y0))))*((((((Ymax&gt;Y0)*(mu_max&gt;=0))))))";
		catalogModel.modelClass = 1;
		catalogModel.comment = "some comment";
		catalogModel.dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	}

	@Test
	public void testConstructor() {
		final CatalogModel catalogModel = new CatalogModel();
		assertThat(catalogModel.id, is(nullValue()));
		assertThat(catalogModel.name, is(nullValue()));
		assertThat(catalogModel.formula, is(nullValue()));
		assertThat(catalogModel.modelClass, is(nullValue()));
		assertThat(catalogModel.comment, is(nullValue()));
		assertThat(catalogModel.dbuuid, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		catalogModel.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(catalogModel.id));
		assertThat(settings.getString("name"), equalTo(catalogModel.name));
		assertThat(settings.getString("formula"), equalTo(catalogModel.formula));
		assertThat(settings.getInt("modelClass"), equalTo(catalogModel.modelClass));
		assertThat(settings.getString("comment"), equalTo(catalogModel.comment));
		assertThat(settings.getString("dbuuid"), equalTo(catalogModel.dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", catalogModel.id);
		settings.addString("name", catalogModel.name);
		settings.addString("formula", catalogModel.formula);
		settings.addInt("modelClass", catalogModel.modelClass);
		settings.addString("comment", catalogModel.comment);
		settings.addString("dbuuid", catalogModel.dbuuid);

		final CatalogModel obtained = new CatalogModel();
		obtained.loadFromNodeSettings(settings);

		TestUtils.compare(obtained, catalogModel);
	}

	@Test
	public void testToCatalogModel() {
		final CatalogModelXml catalogModelXml = new CatalogModelXml(catalogModel.id, catalogModel.name,
				catalogModel.formula, catalogModel.modelClass, catalogModel.dbuuid);
		catalogModelXml.comment = catalogModel.comment;
		final CatalogModel obtained = CatalogModel.toCatalogModel(catalogModelXml);

		TestUtils.compare(obtained, catalogModel);
	}

	@Test
	public void testToCatalogModelXml() {
		final CatalogModelXml catalogModelXml = catalogModel.toCatalogModelXml();

		assertThat(catalogModelXml.id, equalTo(catalogModel.id));
		assertThat(catalogModelXml.name, equalTo(catalogModel.name));
		assertThat(catalogModelXml.formula, equalTo(catalogModel.formula));
		assertThat(catalogModelXml.modelClass, equalTo(catalogModel.modelClass));
		assertThat(catalogModelXml.comment, equalTo(catalogModel.comment));
		assertThat(catalogModelXml.dbuuid, equalTo(catalogModel.dbuuid));
	}
}
