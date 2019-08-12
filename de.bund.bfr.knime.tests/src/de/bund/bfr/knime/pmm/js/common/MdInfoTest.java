package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

@SuppressWarnings("static-method")
public class MdInfoTest {

	static int id = 99;
	static String name = "neunundneunzig";
	static String comment = "comment";
	static int qualityScore = 9;
	static boolean checked = false;

	@Test
	public void testConstructor() {
		final MdInfo modelInfo = new MdInfo();
		assertNull(modelInfo.id);
		assertNull(modelInfo.name);
		assertNull(modelInfo.comment);
		assertNull(modelInfo.qualityScore);
		assertNull(modelInfo.checked);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final MdInfo modelInfo = new MdInfo();
		modelInfo.id = id;
		modelInfo.name = name;
		modelInfo.comment = comment;
		modelInfo.qualityScore = qualityScore;
		modelInfo.checked = checked;

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		modelInfo.saveToNodeSettings(settings);

		assertEquals(id, settings.getInt("ID"));
		assertEquals(name, settings.getString("Name"));
		assertEquals(comment, settings.getString("Comment"));
		assertEquals(qualityScore, settings.getInt("QualityScore"));
		assertEquals(checked, settings.getBoolean("Checked"));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("ID", id);
		settings.addString("Name", name);
		settings.addString("Comment", comment);
		settings.addInt("QualityScore", qualityScore);
		settings.addBoolean("Checked", checked);

		final MdInfo modelInfo = new MdInfo();
		modelInfo.loadFromNodeSettings(settings);

		assertEquals(id, modelInfo.id.intValue());
		assertEquals(name, modelInfo.name);
		assertEquals(comment, modelInfo.comment);
		assertEquals(qualityScore, modelInfo.qualityScore.intValue());
		assertEquals(checked, modelInfo.checked);
	}

	@Test
	public void testToMdInfo() {
		final MdInfoXml mdInfoXml = new MdInfoXml(id, name, comment, qualityScore, checked);
		final MdInfo mdInfo = MdInfo.toMdInfo(mdInfoXml);

		assertEquals(id, mdInfo.id.intValue());
		assertEquals(name, mdInfo.name);
		assertEquals(comment, mdInfo.comment);
		assertEquals(qualityScore, mdInfo.qualityScore.intValue());
		assertEquals(checked, mdInfo.checked);
	}

	@Test
	public void testToMdInfoXml() {
		final MdInfo modelInfo = new MdInfo();
		modelInfo.id = id;
		modelInfo.name = name;
		modelInfo.comment = comment;
		modelInfo.qualityScore = qualityScore;
		modelInfo.checked = checked;
		final MdInfoXml mdInfoXml = modelInfo.toMdInfoXml();

		assertEquals(id, mdInfoXml.id.intValue());
		assertEquals(name, mdInfoXml.name);
		assertEquals(comment, mdInfoXml.comment);
		assertEquals(qualityScore, mdInfoXml.qualityScore.intValue());
		assertEquals(checked, mdInfoXml.checked);
	}
}
