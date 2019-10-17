package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

@SuppressWarnings("static-method")
public class MdInfoTest {

	static MdInfo mdInfo;
	static {
		mdInfo = new MdInfo();
		mdInfo.id = 99;
		mdInfo.name = "neunundneunzig";
		mdInfo.comment = "comment";
		mdInfo.qualityScore = 9;
		mdInfo.checked = false;
	}

	@Test
	public void testConstructor() {
		final MdInfo modelInfo = new MdInfo();
		assertThat(modelInfo.id, is(nullValue()));
		assertThat(modelInfo.name, is(nullValue()));
		assertThat(modelInfo.comment, is(nullValue()));
		assertThat(modelInfo.qualityScore, is(nullValue()));
		assertThat(modelInfo.checked, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		mdInfo.saveToNodeSettings(settings);

		assertThat(settings.getInt("ID"), equalTo(mdInfo.id));
		assertThat(settings.getString("Name"), equalTo(mdInfo.name));
		assertThat(settings.getString("Comment"), equalTo(mdInfo.comment));
		assertThat(settings.getInt("QualityScore"), equalTo(mdInfo.qualityScore));
		assertThat(settings.getBoolean("Checked"), equalTo(mdInfo.checked));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("ID", mdInfo.id);
		settings.addString("Name", mdInfo.name);
		settings.addString("Comment", mdInfo.comment);
		settings.addInt("QualityScore", mdInfo.qualityScore);
		settings.addBoolean("Checked", mdInfo.checked);

		final MdInfo obtained = new MdInfo();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, mdInfo);
	}

	@Test
	public void testToMdInfo() {
		final MdInfoXml mdInfoXml = new MdInfoXml(mdInfo.id, mdInfo.name, mdInfo.comment, mdInfo.qualityScore, mdInfo.checked);
		final MdInfo obtained = MdInfo.toMdInfo(mdInfoXml);
		TestUtils.compare(obtained, mdInfo);
	}

	@Test
	public void testToMdInfoXml() {
		final MdInfoXml mdInfoXml = mdInfo.toMdInfoXml();
		assertThat(mdInfoXml.id, equalTo(mdInfo.id));
		assertThat(mdInfoXml.name, equalTo(mdInfo.name));
		assertThat(mdInfoXml.comment, equalTo(mdInfo.comment));
		assertThat(mdInfoXml.qualityScore, equalTo(mdInfo.qualityScore));
		assertThat(mdInfoXml.checked, equalTo(mdInfo.checked));
	}
}
