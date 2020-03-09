package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

@SuppressWarnings("static-method")
public class PmmXmlDocTest {

	@Test
	public void testEmptyConstructor() {
		PmmXmlDoc xmlDoc = new PmmXmlDoc();
		assertTrue(xmlDoc.getElementSet().isEmpty());
		assertNull(xmlDoc.get(0));
	}

	@Test
	public void testCopyConstructor() {
		AgentXml agent = new AgentXml();
		PmmXmlDoc xmlDoc = new PmmXmlDoc(agent);
		assertTrue(agent.id == ((AgentXml) xmlDoc.get(0)).id);
	}

	@Test
	public void testStringConstructor() throws Exception {

		// ParametricModel.ELEMENT_PARAMETRICMODEL
		ParametricModel parametricModel = new ParametricModel();
		PmmXmlDoc parametricModelDoc = new PmmXmlDoc(new PmmXmlDoc(parametricModel).toXmlString());
		assertTrue(parametricModel.modelId == ((ParametricModel) parametricModelDoc.get(0)).modelId);

		// MiscXml.ELEMENT_MISC
		MiscXml misc = new MiscXml(0, "name", "description", 0.0, Collections.emptyList(), "unit", "dbuuid");
		PmmXmlDoc miscDoc = new PmmXmlDoc(new PmmXmlDoc(misc).toXmlString());
		assertTrue(misc.id == ((MiscXml) miscDoc.get(0)).id);

		// ParamXml.ELEMENT_PARAM
		ParamXml param = new ParamXml("name", false, 0.0);
		PmmXmlDoc paramDoc = new PmmXmlDoc(new PmmXmlDoc(param).toXmlString());
		assertEquals(param.name, ((ParamXml) paramDoc.get(0)).name);

		// IndepXml.ELEMENT_INDEP
		IndepXml indep = new IndepXml("name", 0.0, 1.0);
		PmmXmlDoc indepDoc = new PmmXmlDoc(new PmmXmlDoc(indep).toXmlString());
		assertEquals(indep.name, ((IndepXml) indepDoc.get(0)).name);

		// DepXml.ELEMENT_DEPENDENT
		DepXml dep = new DepXml("name");
		PmmXmlDoc depDoc = new PmmXmlDoc(new PmmXmlDoc(dep).toXmlString());
		assertEquals(dep.name, ((DepXml) depDoc.get(0)).name);

		// TimeSeriesXml.ELEMENT_TIMESERIES
		TimeSeriesXml timeSeries = new TimeSeriesXml("name", 0.0, "timeUnit", 0.0, "concentrationUnit", 0.0, 0);
		PmmXmlDoc timeSeriesDoc = new PmmXmlDoc(new PmmXmlDoc(timeSeries).toXmlString());
		assertEquals(timeSeries.name, ((TimeSeriesXml) timeSeriesDoc.get(0)).name);

		// MdInfoXml.ELEMENT_MDINFO
		MdInfoXml mdInfo = new MdInfoXml(0, "name", "comment", 0, false);
		PmmXmlDoc mdInfoDoc = new PmmXmlDoc(new PmmXmlDoc(mdInfo).toXmlString());
		assertEquals(mdInfo.id, ((MdInfoXml) mdInfoDoc.get(0)).id);

		// LiteratureItem.ELEMENT_LITERATURE
		LiteratureItem literatureItem = new LiteratureItem("author", 0, "title", "abstractText", "journal", "volume",
				"issue", 0, 0, "website", 0, "comment");
		PmmXmlDoc literatureDoc = new PmmXmlDoc(new PmmXmlDoc(literatureItem).toXmlString());
		assertEquals(literatureItem.id, ((LiteratureItem) literatureDoc.get(0)).id);

		// "MLiteratureItem"
		de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem mLiteratureItem = new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(
				de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem.Type.M, "author", 0, "title", "abstractText",
				"journal", "volume", "issue", 0, 0, "website", 0, "comment");
		literatureDoc = new PmmXmlDoc(new PmmXmlDoc(mLiteratureItem).toXmlString());
		assertEquals(mLiteratureItem.id,
				((de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem) literatureDoc.get(0)).id);

		// "MDLiteratureItem"
		de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem mdLiteratureItem = new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(
				de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem.Type.MD, "author", 0, "title", "abstractText",
				"journal", "volume", "issue", 0, 0, "website", 0, "comment");
		literatureDoc = new PmmXmlDoc(new PmmXmlDoc(mdLiteratureItem).toXmlString());
		assertEquals(mdLiteratureItem.id,
				((de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem) literatureDoc.get(0)).id);

		// "EstimatedModelLiterature"
		de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem emLiteratureItem = new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(
				de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem.Type.EM, "author", 0, "title", "abstractText",
				"journal", "volume", "issue", 0, 0, "website", 0, "comment");
		literatureDoc = new PmmXmlDoc(new PmmXmlDoc(emLiteratureItem).toXmlString());
		assertEquals(emLiteratureItem.id,
				((de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem) literatureDoc.get(0)).id);

		// CatalogModelXml.ELEMENT_CATALOGMODEL
		CatalogModelXml catalog = new CatalogModelXml(0, "name", "formula", 0);
		PmmXmlDoc catalogDoc = new PmmXmlDoc(new PmmXmlDoc(catalog).toXmlString());
		assertEquals(catalog.id, ((CatalogModelXml) catalogDoc.get(0)).id);

		// EstModelXml.ELEMENT_ESTMODEL
		EstModelXml estModel = new EstModelXml(0, "name", .0, .0, .0, .0, .0, 0, false, 1);
		PmmXmlDoc estModelDoc = new PmmXmlDoc(new PmmXmlDoc(estModel).toXmlString());
		assertEquals(estModel.id, ((EstModelXml) estModelDoc.get(0)).id);

		// AgentXml.ELEMENT_AGENT
		AgentXml agent = new AgentXml();
		PmmXmlDoc agentDoc = new PmmXmlDoc(new PmmXmlDoc(agent).toXmlString());
		assertEquals(agent.id, ((AgentXml) agentDoc.get(0)).id);

		// "mdAgent"
		de.bund.bfr.knime.pmm.extendedtable.items.AgentXml mdAgent = new de.bund.bfr.knime.pmm.extendedtable.items.AgentXml(
				de.bund.bfr.knime.pmm.extendedtable.items.AgentXml.Type.MD);
		PmmXmlDoc mdAgentDoc = new PmmXmlDoc(new PmmXmlDoc(mdAgent).toXmlString());
		assertEquals(mdAgent.id, ((de.bund.bfr.knime.pmm.extendedtable.items.AgentXml) mdAgentDoc.get(0)).id);

		// "model1Agent"
		de.bund.bfr.knime.pmm.extendedtable.items.AgentXml model1Agent = new de.bund.bfr.knime.pmm.extendedtable.items.AgentXml(
				de.bund.bfr.knime.pmm.extendedtable.items.AgentXml.Type.Model1);
		PmmXmlDoc model1AgentDoc = new PmmXmlDoc(new PmmXmlDoc(model1Agent).toXmlString());
		assertEquals(model1Agent.id, ((de.bund.bfr.knime.pmm.extendedtable.items.AgentXml) model1AgentDoc.get(0)).id);

		// "model2Agent"
		de.bund.bfr.knime.pmm.extendedtable.items.AgentXml model2Agent = new de.bund.bfr.knime.pmm.extendedtable.items.AgentXml(
				de.bund.bfr.knime.pmm.extendedtable.items.AgentXml.Type.Model2);
		PmmXmlDoc model2AgentDoc = new PmmXmlDoc(new PmmXmlDoc(model2Agent).toXmlString());
		assertEquals(model2Agent.id, ((de.bund.bfr.knime.pmm.extendedtable.items.AgentXml) model2AgentDoc.get(0)).id);
		
		// MatrixXml.ELEMENT_MATRIX
		MatrixXml matrix = new MatrixXml();
		PmmXmlDoc matrixDoc = new PmmXmlDoc(new PmmXmlDoc(matrix).toXmlString());
		assertEquals(matrix.id, ((MatrixXml) matrixDoc.get(0)).id);
		
		// "mdMatrix"
		de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml mdMatrix = new de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml(
				de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml.Type.MD);
		PmmXmlDoc mdMatrixDoc = new PmmXmlDoc(new PmmXmlDoc(mdMatrix).toXmlString());
		assertEquals(mdMatrix.id, ((de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml) mdMatrixDoc.get(0)).id);
		
		// "model1Matrix"
		de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml model1Matrix = new de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml(
				de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml.Type.Model1);
		PmmXmlDoc model1MatrixDoc = new PmmXmlDoc(new PmmXmlDoc(model1Matrix).toXmlString());
		assertEquals(model1Matrix.id, ((de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml) model1MatrixDoc.get(0)).id);
		
		// "model2Matrix"
		de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml model2Matrix = new de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml(
				de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml.Type.Model2);
		PmmXmlDoc model2MatrixDoc = new PmmXmlDoc(new PmmXmlDoc(model2Matrix).toXmlString());
		assertEquals(model2Matrix.id, ((de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml) model2MatrixDoc.get(0)).id);
		
		// PmmTimeSeries.ELEMENT_TIMESERIES
		PmmTimeSeries pmmTimeSeries = new PmmTimeSeries(1);
		timeSeriesDoc = new PmmXmlDoc(new PmmXmlDoc(pmmTimeSeries).toXmlString());
		assertEquals(pmmTimeSeries.getCondId(), ((PmmTimeSeries) timeSeriesDoc.get(0)).getCondId());
	}
}
