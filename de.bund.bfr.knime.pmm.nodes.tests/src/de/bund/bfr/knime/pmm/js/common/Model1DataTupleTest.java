package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class Model1DataTupleTest {

	static Model1DataTuple tuple;
	static {
		tuple = new Model1DataTuple();
		tuple.condId = 0;
		tuple.globalModelId = 0;
		tuple.combaseId = "combaseId";
		tuple.agent = AgentTest.agent;
		tuple.matrix = MatrixTest.matrix;
		tuple.timeSeriesList.setTimeSeries(new TimeSeries[] { TimeSeriesTest.timeSeries });
		tuple.miscList.setMiscs(new Misc[] { MiscTest.misc } );
		tuple.mdInfo = MdInfoTest.mdInfo;
		tuple.litMd.setLiterature(new Literature[] { LiteratureTest.literature });
		tuple.dbuuid = "dbuuid";
		tuple.catModel = CatalogModelTest.catalogModel;
		tuple.catModelSec = CatalogModelTest.catalogModel;
		tuple.estModel = EstModelTest.estModel;
		tuple.dep = DepTest.dep;
		tuple.params.setParams(new Param[] { ParamTest.param } );
		tuple.indeps.setIndeps(new Indep[] { IndepTest.indep });
		tuple.indepsSec.setIndeps(new Indep[] { IndepTest.indep } );
		tuple.mLit.setLiterature(new Literature[] { LiteratureTest.literature });
		tuple.emLit.setLiterature(new Literature[] { LiteratureTest.literature });
		tuple.databaseWritable = false;
	}

	@Test
	public void testConstructor() {
		final Model1DataTuple aTuple = new Model1DataTuple();
		assertThat(aTuple.condId, is(nullValue()));
		assertThat(aTuple.globalModelId, is(nullValue()));
		assertThat(aTuple.combaseId, is(nullValue()));
		assertThat(aTuple.agent, is(notNullValue()));
		assertThat(aTuple.matrix, is(notNullValue()));
		assertThat(aTuple.timeSeriesList, is(notNullValue()));
		assertThat(aTuple.miscList, is(notNullValue()));
		assertThat(aTuple.mdInfo, is(notNullValue()));
		assertThat(aTuple.litMd, is(notNullValue()));
		assertThat(aTuple.dbuuid, is(nullValue()));
		assertThat(aTuple.catModel, is(notNullValue()));
		assertThat(aTuple.catModelSec, is(notNullValue()));
		assertThat(aTuple.estModel, is(notNullValue()));
		assertThat(aTuple.dep, is(notNullValue()));
		assertThat(aTuple.params, is(notNullValue()));
		assertThat(aTuple.paramsSec, is(notNullValue()));
		assertThat(aTuple.indeps, is(notNullValue()));
		assertThat(aTuple.indepsSec, is(notNullValue()));
		assertThat(aTuple.mLit, is(notNullValue()));
		assertThat(aTuple.emLit, is(notNullValue()));
		assertThat(aTuple.databaseWritable, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		tuple.saveToNodeSettings(settings);

		// Fails with InvalidSettingsException if a config is missing.
		settings.getInt("CondID");
		settings.getString("CombaseID");
		settings.getNodeSettings("Misc");
		settings.getNodeSettings("Organism");
		settings.getNodeSettings("Matrix");
		settings.getNodeSettings("MD_Data");
		settings.getNodeSettings("MD_Info");
		settings.getNodeSettings("MD_Literatur");
		settings.getString("M_DB_UID");
		settings.getNodeSettings("CatModel");
		settings.getNodeSettings("CatModelSec");
		settings.getNodeSettings("EstModel");
		settings.getNodeSettings("Dependent");
		settings.getNodeSettings("Parameter");
		settings.getNodeSettings("Independent");
		settings.getNodeSettings("IndependentSec");
		settings.getNodeSettings("M_Literatur");
		settings.getNodeSettings("EM_Literatur");
		settings.getBoolean("DatabaseWritable");
	}

	@Test
	public void testLoadFromNodeSettings() throws Exception {
		// Create node settings
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("CondID", tuple.condId);
		settings.addString("CombaseID", tuple.combaseId);
		tuple.miscList.saveToNodeSettings(settings.addNodeSettings("Misc"));
		tuple.agent.saveToNodeSettings(settings.addNodeSettings("Organism"));
		tuple.matrix.saveToNodeSettings(settings.addNodeSettings("Matrix"));
		tuple.timeSeriesList.saveToNodeSettings(settings.addNodeSettings("MD_Data"));
		tuple.mdInfo.saveToNodeSettings(settings.addNodeSettings("MD_Info"));
		tuple.litMd.saveToNodeSettings(settings.addNodeSettings("MD_Literatur"));
		settings.addString("M_DB_UID", tuple.dbuuid);
		tuple.catModel.saveToNodeSettings(settings.addNodeSettings("CatModel"));
		tuple.catModelSec.saveToNodeSettings(settings.addNodeSettings("CatModelSec"));
		tuple.estModel.saveToNodeSettings(settings.addNodeSettings("EstModel"));
		tuple.dep.saveToNodeSettings(settings.addNodeSettings("Dependent"));
		tuple.params.saveToNodeSettings(settings.addNodeSettings("Parameter"));
		tuple.indeps.saveToNodeSettings(settings.addNodeSettings("Independent"));
		tuple.indepsSec.saveToNodeSettings(settings.addNodeSettings("IndependentSec"));
		tuple.mLit.saveToNodeSettings(settings.addNodeSettings("M_Literatur"));
		tuple.emLit.saveToNodeSettings(settings.addNodeSettings("EM_Literatur"));
		settings.addBoolean("DatabaseWritable", tuple.databaseWritable);

		// Obtain tuple from settings
		final Model1DataTuple obtained = new Model1DataTuple();
		obtained.loadFromNodeSettings(settings);

		TestUtils.compare(obtained, tuple);
	}
}
