package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
		assertTrue(id == modelInfo.getId());
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
		assertTrue(qualityScore == modelInfo.getQualityScore());
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

		assertTrue(id == settings.getInt(MdInfo.ID));
		assertEquals(name, settings.getString(MdInfo.NAME));
		assertTrue(qualityScore == settings.getInt(MdInfo.QUALITYSCORE));
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

		assertTrue(id == modelInfo.getId());
		assertEquals(name, modelInfo.getName());
		assertEquals(comment, modelInfo.getComment());
		assertTrue(qualityScore == modelInfo.getQualityScore());
		assertEquals(checked, modelInfo.getChecked());
	}

	@Test
	public void testToMdInfo() {
		MdInfoXml mdInfoXml = new MdInfoXml(id, name, comment, qualityScore, checked);
		MdInfo mdInfo = MdInfo.toMdInfo(mdInfoXml);

		assertTrue(id == mdInfo.getId());
		assertEquals(name, mdInfo.getName());
		assertEquals(comment, mdInfo.getComment());
		assertTrue(qualityScore == mdInfo.getQualityScore());
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

		assertTrue(id == mdInfoXml.getId());
		assertEquals(name, mdInfoXml.getName());
		assertEquals(comment, mdInfoXml.getComment());
		assertTrue(qualityScore == mdInfoXml.getQualityScore());
		assertEquals(checked, mdInfoXml.getChecked());
	}
}
