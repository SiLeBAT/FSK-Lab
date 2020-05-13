package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.EstModelXml;

@SuppressWarnings("static-method")
public class EstModelTest {

	static EstModel estModel;
	static {
		estModel = new EstModel();
		estModel.id = 67;
		estModel.name = "An estimated model";
		estModel.sse = .7866184782407727;
		estModel.rms = 0.3454675346276258;
		estModel.r2 = 0.9960322874417514;
		estModel.aic = -32.9771352876857;
		estModel.bic = -34.994206193469736;
		estModel.dof = 5;
		estModel.qualityScore = 3;
		estModel.checked = false;
		estModel.comment = "A improvised comment";
		estModel.dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	}

	@Test
	public void testConstructor() {
		final EstModel estModel = new EstModel();
		assertThat(estModel.id, is(nullValue()));
		assertThat(estModel.name, is(nullValue()));
		assertThat(estModel.sse, is(nullValue()));
		assertThat(estModel.rms, is(nullValue()));
		assertThat(estModel.r2, is(nullValue()));
		assertThat(estModel.aic, is(nullValue()));
		assertThat(estModel.bic, is(nullValue()));
		assertThat(estModel.dof, is(nullValue()));
		assertThat(estModel.qualityScore, is(nullValue()));
		assertThat(estModel.checked, is(nullValue()));
		assertThat(estModel.comment, is(nullValue()));
		assertThat(estModel.dbuuid, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		estModel.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(estModel.id));
		assertThat(settings.getString("name"), equalTo(estModel.name));
		assertThat(settings.getDouble("sse"), equalTo(estModel.sse));
		assertThat(settings.getDouble("rms"), equalTo(estModel.rms));
		assertThat(settings.getDouble("r2"), equalTo(estModel.r2));
		assertThat(settings.getDouble("aic"), equalTo(estModel.aic));
		assertThat(settings.getDouble("bic"), equalTo(estModel.bic));
		assertThat(settings.getInt("dof"), equalTo(estModel.dof));
		assertThat(settings.getInt("qualityScore"), equalTo(estModel.qualityScore));
		assertThat(settings.getBoolean("checked"), equalTo(estModel.checked));
		assertThat(settings.getString("comment"), equalTo(estModel.comment));
		assertThat(settings.getString("dbuuid"), equalTo(estModel.dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", estModel.id);
		settings.addString("name", estModel.name);
		settings.addDouble("sse", estModel.sse);
		settings.addDouble("rms", estModel.rms);
		settings.addDouble("r2", estModel.r2);
		settings.addDouble("aic", estModel.aic);
		settings.addDouble("bic", estModel.bic);
		settings.addInt("dof", estModel.dof);
		settings.addInt("qualityScore", estModel.qualityScore);
		settings.addBoolean("checked", estModel.checked);
		settings.addString("comment", estModel.comment);
		settings.addString("dbuuid", estModel.dbuuid);

		final EstModel obtained = new EstModel();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, estModel);
	}

	@Test
	public void testToEstModel() {
		final EstModelXml estModelXml = new EstModelXml(estModel.id, estModel.name, estModel.sse, estModel.rms,
				estModel.r2, estModel.aic, estModel.bic, estModel.dof, estModel.checked, estModel.qualityScore,
				estModel.dbuuid);
		estModelXml.comment = estModel.comment;

		final EstModel obtained = EstModel.toEstModel(estModelXml);
		TestUtils.compare(obtained, estModel);
	}

	@Test
	public void testToEstModelXml() {
		final EstModelXml estModelXml = estModel.toEstModelXml();

		assertThat(estModelXml.id, equalTo(estModel.id));
		assertThat(estModelXml.name, equalTo(estModel.name));
		assertThat(estModelXml.sse, equalTo(estModel.sse));
		assertThat(estModelXml.rms, equalTo(estModel.rms));
		assertThat(estModelXml.r2, equalTo(estModel.r2));
		assertThat(estModelXml.aic, equalTo(estModel.aic));
		assertThat(estModelXml.bic, equalTo(estModel.bic));
		assertThat(estModelXml.dof, equalTo(estModel.dof));
		assertThat(estModelXml.qualityScore, equalTo(estModel.qualityScore));
		assertThat(estModelXml.checked, equalTo(estModel.checked));
		assertThat(estModelXml.comment, equalTo(estModel.comment));
		assertThat(estModelXml.dbuuid, equalTo(estModel.dbuuid));
	}
}
