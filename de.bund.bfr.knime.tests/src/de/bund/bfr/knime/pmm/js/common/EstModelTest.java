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
	public void testConstructor() {
		EstModel estModel = new EstModel();
		assertNull(estModel.id);
		assertNull(estModel.name);
		assertNull(estModel.sse);
		assertNull(estModel.rms);
		assertNull(estModel.r2);
		assertNull(estModel.aic);
		assertNull(estModel.bic);
		assertNull(estModel.dof);
		assertNull(estModel.qualityScore);
		assertNull(estModel.checked);
		assertNull(estModel.comment);
		assertNull(estModel.dbuuid);
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		EstModel estModel = new EstModel();
		estModel.id = id;
		estModel.name = name;
		estModel.sse = sse;
		estModel.rms = rms;
		estModel.r2 = r2;
		estModel.aic = aic;
		estModel.bic = bic;
		estModel.dof = dof;
		estModel.qualityScore = qualityScore;
		estModel.checked = checked;
		estModel.comment = comment;
		estModel.dbuuid = dbuuid;
		
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
		
		assertEquals(id, estModel.id.intValue());
		assertEquals(name, estModel.name);
		assertEquals(sse, estModel.sse, 0.0);
		assertEquals(rms, estModel.rms, 0.0);
		assertEquals(r2, estModel.r2, 0.0);
		assertEquals(aic, estModel.aic, 0.0);
		assertEquals(bic, estModel.bic, 0.0);
		assertEquals(dof, estModel.dof.intValue());
		assertEquals(qualityScore, estModel.qualityScore.intValue());
		assertEquals(checked, estModel.checked);
		assertEquals(comment, estModel.comment);
		assertEquals(dbuuid, estModel.dbuuid);
	}
	
	@Test
	public void testToEstModel() {
		EstModelXml estModelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof, checked, qualityScore, dbuuid);
		estModelXml.comment = comment;
		
		EstModel estModel = EstModel.toEstModel(estModelXml);
		assertEquals(id, estModel.id.intValue());
		assertEquals(name, estModel.name);
		assertEquals(sse, estModel.sse, 0.0);
		assertEquals(rms, estModel.rms, 0.0);
		assertEquals(r2, estModel.r2, 0.0);
		assertEquals(aic, estModel.aic, 0.0);
		assertEquals(bic, estModel.bic, 0.0);
		assertEquals(dof, estModel.dof.intValue());
		assertEquals(qualityScore, estModel.qualityScore.intValue());
		assertEquals(checked, estModel.checked);
		assertEquals(comment, estModel.comment);
		assertEquals(dbuuid, estModel.dbuuid);
	}
	
	@Test
	public void testToEstModelXml() {
		EstModel estModel = new EstModel();
		estModel.id = id;
		estModel.name = name;
		estModel.sse = sse;
		estModel.rms = rms;
		estModel.r2 = r2;
		estModel.aic = aic;
		estModel.bic = bic;
		estModel.dof = dof;
		estModel.qualityScore = qualityScore;
		estModel.checked = checked;
		estModel.comment = comment;
		estModel.dbuuid = dbuuid;
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
