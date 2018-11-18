package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.EstModelXml;

public class EstModelTest {

	static int id = 67;
	static String name = "An estimated model";
	static double sse = .7866184782407727;
	static double rms = 0.3454675346276258;
	static double r2 = 0.9960322874417514;
	static double aic = -32.9771352876857;
	static double bic = -34.994206193469736;
	static int dof = 5;
	static int qualityScore = 3;
	static boolean checked = false;
	static String comment = "A improvised comment";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";

	@Test
	public void testId() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getId());

		estModel.setId(id);
		assertEquals(id, estModel.getId().intValue());
	}

	@Test
	public void testName() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getName());

		estModel.setName(name);
		assertEquals(name, estModel.getName());
	}

	@Test
	public void testSse() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getSse());

		estModel.setSse(sse);
		assertEquals(sse, estModel.getSse(), 0.0);
	}

	@Test
	public void testRms() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getRms());

		estModel.setRms(rms);
		assertEquals(rms, estModel.getRms(), 0.0);
	}

	@Test
	public void testR2() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getR2());

		estModel.setR2(r2);
		assertEquals(r2, estModel.getR2(), 0.0);
	}

	@Test
	public void testAic() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getAIC());

		estModel.setAIC(aic);
		assertEquals(aic, estModel.getAIC(), 0.0);
	}

	@Test
	public void testBic() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getBIC());

		estModel.setBIC(bic);
		assertEquals(bic, estModel.getBIC(), 0.0);
	}

	@Test
	public void testDof() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getDof());

		estModel.setDof(dof);
		assertEquals(dof, estModel.getDof().intValue());
	}

	@Test
	public void testQualityScore() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getQualityScore());

		estModel.setQualityScore(qualityScore);
		assertEquals(qualityScore, estModel.getQualityScore().intValue());
	}

	@Test
	public void testChecked() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getChecked());
		
		estModel.setChecked(checked);
		assertEquals(checked, estModel.getChecked());
	}

	@Test
	public void testComment() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getComment());
		
		estModel.setComment(comment);
		assertEquals(comment, estModel.getComment());
	}

	@Test
	public void testDbuuid() {
		EstModel estModel = new EstModel();
		assertNull(estModel.getDbuuid());
		
		estModel.setDbuuid(dbuuid);
		assertEquals(dbuuid, estModel.getDbuuid());
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		EstModel estModel = new EstModel();
		estModel.setId(id);
		estModel.setName(name);
		estModel.setSse(sse);
		estModel.setRms(rms);
		estModel.setR2(r2);
		estModel.setAIC(aic);
		estModel.setBIC(bic);
		estModel.setDof(dof);
		estModel.setQualityScore(qualityScore);
		estModel.setChecked(checked);
		estModel.setComment(comment);
		estModel.setDbuuid(dbuuid);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		estModel.saveToNodeSettings(settings);
		
		assertEquals(id, settings.getInt("id"));
		assertEquals(name, settings.getString("name"));
		assertEquals(sse, settings.getDouble("sse"), 0.0);
		assertEquals(rms, settings.getDouble("rms"), 0.0);
		assertEquals(r2, settings.getDouble("r2"), 0.0);
		assertEquals(aic, settings.getDouble("aic"), 0.0);
		assertEquals(bic, settings.getDouble("bic"), 0.0);
		assertEquals(dof, settings.getInt("dof"));
		assertEquals(qualityScore, settings.getInt("qualityScore"));
		assertEquals(checked, settings.getBoolean("checked"));
		assertEquals(comment, settings.getString("comment"));
		assertEquals(dbuuid, settings.getString("dbuuid"));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("name", name);
		settings.addDouble("sse", sse);
		settings.addDouble("rms", rms);
		settings.addDouble("r2", r2);
		settings.addDouble("aic", aic);
		settings.addDouble("bic", bic);
		settings.addInt("dof", dof);
		settings.addInt("qualityScore", qualityScore);
		settings.addBoolean("checked", checked);
		settings.addString("comment", comment);
		settings.addString("dbuuid", dbuuid);
		
		EstModel estModel = new EstModel();
		estModel.loadFromNodeSettings(settings);
		
		assertEquals(id, estModel.getId().intValue());
		assertEquals(name, estModel.getName());
		assertEquals(sse, estModel.getSse(), 0.0);
		assertEquals(rms, estModel.getRms(), 0.0);
		assertEquals(r2, estModel.getR2(), 0.0);
		assertEquals(aic, estModel.getAIC(), 0.0);
		assertEquals(bic, estModel.getBIC(), 0.0);
		assertEquals(dof, estModel.getDof().intValue());
		assertEquals(qualityScore, estModel.getQualityScore().intValue());
		assertEquals(checked, estModel.getChecked());
		assertEquals(comment, estModel.getComment());
		assertEquals(dbuuid, estModel.getDbuuid());
	}
	
	@Test
	public void testToEstModel() {
		EstModelXml estModelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof, checked, qualityScore, dbuuid);
		estModelXml.comment = comment;
		
		EstModel estModel = EstModel.toEstModel(estModelXml);
		assertEquals(id, estModel.getId().intValue());
		assertEquals(name, estModel.getName());
		assertEquals(sse, estModel.getSse(), 0.0);
		assertEquals(rms, estModel.getRms(), 0.0);
		assertEquals(r2, estModel.getR2(), 0.0);
		assertEquals(aic, estModel.getAIC(), 0.0);
		assertEquals(bic, estModel.getBIC(), 0.0);
		assertEquals(dof, estModel.getDof().intValue());
		assertEquals(qualityScore, estModel.getQualityScore().intValue());
		assertEquals(checked, estModel.getChecked());
		assertEquals(comment, estModel.getComment());
		assertEquals(dbuuid, estModel.getDbuuid());
	}
	
	@Test
	public void testToEstModelXml() {
		EstModel estModel = new EstModel();
		estModel.setId(id);
		estModel.setName(name);
		estModel.setSse(sse);
		estModel.setRms(rms);
		estModel.setR2(r2);
		estModel.setAIC(aic);
		estModel.setBIC(bic);
		estModel.setDof(dof);
		estModel.setQualityScore(qualityScore);
		estModel.setChecked(checked);
		estModel.setComment(comment);
		estModel.setDbuuid(dbuuid);
		EstModelXml estModelXml = estModel.toEstModelXml();
		
		assertEquals(id, estModelXml.id.intValue());
		assertEquals(name, estModelXml.name);
		assertEquals(sse, estModelXml.sse, 0.0);
		assertEquals(rms, estModelXml.rms, 0.0);
		assertEquals(r2, estModelXml.r2, 0.0);
		assertEquals(aic, estModelXml.aic, 0.0);
		assertEquals(bic, estModelXml.bic, 0.0);
		assertEquals(dof, estModelXml.dof.intValue());
		assertEquals(qualityScore, estModelXml.qualityScore.intValue());
		assertEquals(checked, estModelXml.checked);
		assertEquals(comment, estModelXml.comment);
		assertEquals(dbuuid, estModelXml.dbuuid);
	}
}
