package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MiscXml;

@SuppressWarnings("static-method")
public class MiscTest {

	static Misc misc;
	static {
		misc = new Misc();
		misc.id = -1;
		misc.name = "Temperature";
		misc.description = "Temperature";
		misc.value = 10.0;
		misc.categories = new String[] { "Temperature " };
		misc.unit = "°C";
		misc.origUnit = "°C";
		misc.dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	}

	@Test
	public void testConstructor() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.id, is(nullValue()));
		assertThat(aMisc.name, is(nullValue()));
		assertThat(aMisc.description, is(nullValue()));
		assertThat(aMisc.value, is(nullValue()));
		assertThat(aMisc.categories, is(nullValue()));
		assertThat(aMisc.unit, is(nullValue()));
		assertThat(aMisc.origUnit, is(nullValue()));
		assertThat(aMisc.dbuuid, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		misc.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(misc.id));
		assertThat(settings.getString("name"), equalTo(misc.name));
		assertThat(settings.getString("description"), equalTo(misc.description));
		assertThat(settings.getDouble("value"), equalTo(misc.value));
		assertThat(settings.getString("unit"), equalTo(misc.unit));
		assertThat(settings.getString("origUnit"), equalTo(misc.origUnit));
		assertThat(settings.getString("dbuuid"), equalTo(misc.dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", misc.id);
		settings.addString("name", misc.name);
		settings.addString("description", misc.description);
		settings.addDouble("value", misc.value);
		settings.addStringArray("category", misc.categories);
		settings.addString("unit", misc.unit);
		settings.addString("origUnit", misc.origUnit);
		settings.addString("dbuuid", misc.dbuuid);

		final Misc obtained = new Misc();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, misc);
	}

	@Test
	public void testToMisc() {
		final MiscXml miscXml = new MiscXml(misc.id, misc.name, misc.description, misc.value,
				Arrays.asList(misc.categories), misc.unit, misc.origUnit, misc.dbuuid);
		final Misc obtained = Misc.toMisc(miscXml);
		TestUtils.compare(obtained, misc);
	}

	@Test
	public void testToMiscXml() {
		final MiscXml obtained = misc.toMiscXml();
		assertThat(obtained.id, equalTo(misc.id));
		assertThat(obtained.name, equalTo(misc.name));
		assertThat(obtained.description, equalTo(misc.description));
		assertThat(obtained.value, equalTo(misc.value));
		assertThat(obtained.categories, contains(misc.categories));
		assertThat(obtained.unit, equalTo(misc.unit));
		assertThat(obtained.origUnit, equalTo(misc.origUnit));
		assertThat(obtained.dbuuid, equalTo(misc.dbuuid));
	}
}
