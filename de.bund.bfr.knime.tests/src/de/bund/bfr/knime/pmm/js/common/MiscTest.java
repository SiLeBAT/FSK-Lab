package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
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
		misc.setId(-1);
		misc.setName("Temperature");
		misc.setDescription("Temperature");
		misc.setValue(10.0);
		misc.setCategories(new String[] { "Temperature" });
		misc.setUnit("°C");
		misc.setOrigUnit("°C");
		misc.setDbuuid("6df109d0-f6b1-409d-a286-0687b1aca001");
	}

	@Test
	public void testId() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getId(), is(nullValue()));

		aMisc.setId(misc.getId());
		assertThat(aMisc.getId(), equalTo(misc.getId()));
	}

	@Test
	public void testName() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getName(), is(nullValue()));

		aMisc.setName(misc.getName());
		assertThat(aMisc.getName(), equalTo(misc.getName()));
	}

	@Test
	public void testDescription() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getDescription(), is(nullValue()));

		aMisc.setDescription(misc.getDescription());
		assertThat(aMisc.getDescription(), equalTo(misc.getDescription()));
	}

	@Test
	public void testValue() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getValue(), is(nullValue()));

		aMisc.setValue(misc.getValue());
		assertThat(aMisc.getValue(), equalTo(misc.getValue()));
	}

	@Test
	public void testCategories() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getCategories(), is(nullValue()));

		aMisc.setCategories(misc.getCategories());
		assertThat(aMisc.getCategories(), arrayContaining(misc.getCategories()));
	}

	@Test
	public void testUnit() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getUnit(), is(nullValue()));

		aMisc.setUnit(misc.getUnit());
		assertThat(aMisc.getUnit(), equalTo(misc.getUnit()));
	}

	@Test
	public void testOrigUnit() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getOrigUnit(), is(nullValue()));

		aMisc.setOrigUnit(misc.getOrigUnit());
		assertThat(aMisc.getOrigUnit(), equalTo(misc.getOrigUnit()));
	}

	@Test
	public void testDbuuid() {
		final Misc aMisc = new Misc();
		assertThat(aMisc.getDbuuid(), is(nullValue()));

		aMisc.setDbuuid(misc.getDbuuid());
		assertThat(aMisc.getDbuuid(), equalTo(misc.getDbuuid()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		misc.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(misc.getId()));
		assertThat(settings.getString("name"), equalTo(misc.getName()));
		assertThat(settings.getString("description"), equalTo(misc.getDescription()));
		assertThat(settings.getDouble("value"), equalTo(misc.getValue()));
		assertThat(settings.getString("unit"), equalTo(misc.getUnit()));
		assertThat(settings.getString("origUnit"), equalTo(misc.getOrigUnit()));
		assertThat(settings.getString("dbuuid"), equalTo(misc.getDbuuid()));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", misc.getId());
		settings.addString("name", misc.getName());
		settings.addString("description", misc.getDescription());
		settings.addDouble("value", misc.getValue());
		settings.addStringArray("category", misc.getCategories());
		settings.addString("unit", misc.getUnit());
		settings.addString("origUnit", misc.getOrigUnit());
		settings.addString("dbuuid", misc.getDbuuid());

		final Misc obtained = new Misc();
		obtained.loadFromNodeSettings(settings);
		compare(obtained, misc);
	}

	@Test
	public void testToMisc() {
		final MiscXml miscXml = new MiscXml(misc.getId(), misc.getName(), misc.getDescription(), misc.getValue(),
				Arrays.asList(misc.getCategories()), misc.getUnit(), misc.getOrigUnit(), misc.getDbuuid());
		final Misc obtained = Misc.toMisc(miscXml);
		compare(obtained, misc);
	}

	@Test
	public void testToMiscXml() {
		final MiscXml obtained = misc.toMiscXml();
		assertThat(obtained.id, equalTo(misc.getId()));
		assertThat(obtained.name, equalTo(misc.getName()));
		assertThat(obtained.description, equalTo(misc.getDescription()));
		assertThat(obtained.value, equalTo(misc.getValue()));
		assertThat(obtained.categories, contains(misc.getCategories()));
		assertThat(obtained.unit, equalTo(misc.getUnit()));
		assertThat(obtained.origUnit, equalTo(misc.getOrigUnit()));
		assertThat(obtained.dbuuid, equalTo(misc.getDbuuid()));
	}

	private static void compare(Misc obtained, Misc expected) {
		assertThat(obtained.getId(), equalTo(expected.getId()));
		assertThat(obtained.getName(), equalTo(expected.getName()));
		assertThat(obtained.getDescription(), equalTo(expected.getDescription()));
		assertThat(obtained.getValue(), equalTo(expected.getValue()));
		assertThat(obtained.getCategories(), arrayContaining(expected.getCategories()));
		assertThat(obtained.getUnit(), equalTo(expected.getUnit()));
		assertThat(obtained.getOrigUnit(), equalTo(expected.getOrigUnit()));
		assertThat(obtained.getDbuuid(), equalTo(expected.getDbuuid()));
	}
}
