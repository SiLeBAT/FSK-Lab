package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.IndepXml;

@SuppressWarnings("static-method")
public class IndepTest {

	static Indep indep;
	static {
		indep = new Indep();
		indep.name = "Time";
		indep.origname = "Time";
		indep.min = 0.0;
		indep.max = 554.0;
		indep.category = "Time";
		indep.unit = "h";
		indep.description = "time";
	}

	@Test
	public void testConstructors() {
		final Indep indep = new Indep();
		assertThat(indep.name, is(nullValue()));
		assertThat(indep.origname, is(nullValue()));
		assertThat(indep.min, is(nullValue()));
		assertThat(indep.max, is(nullValue()));
		assertThat(indep.category, is(nullValue()));
		assertThat(indep.unit, is(nullValue()));
		assertThat(indep.description, is(nullValue()));
	}

	@Test
	public void testSaveNodeSettings() throws Exception {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		indep.saveToNodeSettings(settings);

		assertThat(settings.getString("name"), equalTo(indep.name));
		assertThat(settings.getString("origname"), equalTo(indep.origname));
		assertThat(settings.getDouble("min"), equalTo(indep.min));
		assertThat(settings.getDouble("max"), equalTo(indep.max));
		assertThat(settings.getString("category"), equalTo(indep.category));
		assertThat(settings.getString("unit"), equalTo(indep.unit));
		assertThat(settings.getString("description"), equalTo(indep.description));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", indep.name);
		settings.addString("origname", indep.origname);
		settings.addDouble("min", indep.min);
		settings.addDouble("max", indep.max);
		settings.addString("category", indep.category);
		settings.addString("unit", indep.unit);
		settings.addString("description", indep.description);

		final Indep obtained = new Indep();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, indep);
	}

	@Test
	public void testToIndep() {
		final IndepXml indepXml = new IndepXml(indep.name, indep.origname, indep.min, indep.max, indep.category, indep.unit, indep.description);
		final Indep obtained = Indep.toIndep(indepXml);
		TestUtils.compare(obtained, indep);
	}

	@Test
	public void testToIndepXml() {
		final IndepXml indepXml = indep.toIndepXml();

		assertThat(indepXml.name, equalTo(indep.name));
		assertThat(indepXml.origName, equalTo(indep.origname));
		assertThat(indepXml.min, equalTo(indep.min));
		assertThat(indepXml.max, equalTo(indep.max));
		assertThat(indepXml.category, equalTo(indep.category));
		assertThat(indepXml.unit, equalTo(indep.unit));
		assertThat(indepXml.description, equalTo(indep.description));
	}
}
