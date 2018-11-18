package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

public class MdInfoTest {

	static int id = 99;
	static String name = "neunundneunzig";
	static String comment = "comment";
	static int qualityScore = 9;
	static boolean checked = false;

	@Test
	public void testId() {
		MdInfo modelInfo = new MdInfo();
		assertNull(modelInfo.getId());

		modelInfo.setId(id);
		assertEquals(id, modelInfo.getId().intValue());
	}

	@Test
	public void testName() {
		MdInfo modelInfo = new MdInfo();
		assertNull(modelInfo.getName());

		modelInfo.setName(name);
		assertEquals(name, modelInfo.getName());
	}

	@Test
	public void testComment() {
		MdInfo modelInfo = new MdInfo();
		assertNull(modelInfo.getComment());

		modelInfo.setComment(comment);
		assertEquals(comment, modelInfo.getComment());
	}

	@Test
	public void testQualityScore() {
		MdInfo modelInfo = new MdInfo();
		assertNull(modelInfo.getQualityScore());

		modelInfo.setQualityScore(qualityScore);
		assertEquals(qualityScore, modelInfo.getQualityScore().intValue());
	}

	@Test
	public void testChecked() {
		MdInfo modelInfo = new MdInfo();
		assertNull(modelInfo.getChecked());

		modelInfo.setChecked(checked);
		assertEquals(checked, modelInfo.getChecked());
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		MdInfo modelInfo = new MdInfo();
		modelInfo.setId(id);
		modelInfo.setName(name);
		modelInfo.setComment(comment);
		modelInfo.setQualityScore(qualityScore);
		modelInfo.setChecked(checked);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		modelInfo.saveToNodeSettings(settings);

		assertEquals(id, settings.getInt(MdInfo.ID));
		assertEquals(name, settings.getString(MdInfo.NAME));
		assertEquals(qualityScore, settings.getInt(MdInfo.QUALITYSCORE));
		assertEquals(checked, settings.getBoolean(MdInfo.CHECKED));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(MdInfo.ID, id);
		settings.addString(MdInfo.NAME, name);
		settings.addString(MdInfo.COMMENT, comment);
		settings.addInt(MdInfo.QUALITYSCORE, qualityScore);
		settings.addBoolean(MdInfo.CHECKED, checked);

		MdInfo modelInfo = new MdInfo();
		modelInfo.loadFromNodeSettings(settings);

		assertEquals(id, modelInfo.getId().intValue());
		assertEquals(name, modelInfo.getName());
		assertEquals(comment, modelInfo.getComment());
		assertEquals(qualityScore, modelInfo.getQualityScore().intValue());
		assertEquals(checked, modelInfo.getChecked());
	}

	@Test
	public void testToMdInfo() {
		MdInfoXml mdInfoXml = new MdInfoXml(id, name, comment, qualityScore, checked);
		MdInfo mdInfo = MdInfo.toMdInfo(mdInfoXml);

		assertEquals(id, mdInfo.getId().intValue());
		assertEquals(name, mdInfo.getName());
		assertEquals(comment, mdInfo.getComment());
		assertEquals(qualityScore, mdInfo.getQualityScore().intValue());
		assertEquals(checked, mdInfo.getChecked());
	}

	@Test
	public void testToMdInfoXml() {
		MdInfo modelInfo = new MdInfo();
		modelInfo.setId(id);
		modelInfo.setName(name);
		modelInfo.setComment(comment);
		modelInfo.setQualityScore(qualityScore);
		modelInfo.setChecked(checked);
		MdInfoXml mdInfoXml = modelInfo.toMdInfoXml();

		assertEquals(id, mdInfoXml.id.intValue());
		assertEquals(name, mdInfoXml.name);
		assertEquals(comment, mdInfoXml.comment);
		assertEquals(qualityScore, mdInfoXml.qualityScore.intValue());
		assertEquals(checked, mdInfoXml.checked);
	}
}
