package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.DepXml;

@SuppressWarnings("static-method")
public class DepTest {

	static Dep dep;
	static {
		dep = new Dep();
		dep.name = "Value";
		dep.origname = "Value";
		dep.min = 0.0;
		dep.max = 10.0;
		dep.category = "Number Content (count/mass)";
		dep.unit = "ln(count/g)";
		dep.description = "bacterial population at time t -ln() transformed";
	}

	@Test
	public void testConstructor() {
		final Dep dep = new Dep();
		assertThat(dep.name, is(nullValue()));
		assertThat(dep.origname, is(nullValue()));
		assertThat(dep.min, is(nullValue()));
		assertThat(dep.max, is(nullValue()));
		assertThat(dep.category, is(nullValue()));
		assertThat(dep.unit, is(nullValue()));
		assertThat(dep.description, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		dep.saveToNodeSettings(settings);

		assertThat(settings.getString("name"), equalTo(dep.name));
		assertThat(settings.getString("origname"), equalTo(dep.origname));
		assertThat(settings.getDouble("min"), equalTo(dep.min));
		assertThat(settings.getDouble("max"), equalTo(dep.max));
		assertThat(settings.getString("category"), equalTo(dep.category));
		assertThat(settings.getString("unit"), equalTo(dep.unit));
		assertThat(settings.getString("description"), equalTo(dep.description));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString("name", dep.name);
		settings.addString("origname", dep.origname);
		settings.addDouble("min", dep.min);
		settings.addDouble("max", dep.max);
		settings.addString("category", dep.category);
		settings.addString("unit", dep.unit);
		settings.addString("description", dep.description);

		final Dep obtained = new Dep();
		obtained.loadFromNodeSettings(settings);

		compare(obtained, dep);
	}

	@Test
	public void testToDep() {
		final DepXml depXml = new DepXml(dep.name, dep.origname, dep.category, dep.unit, dep.description);
		depXml.min = dep.min;
		depXml.max = dep.max;

		final Dep obtained = Dep.toDep(depXml);
		compare(obtained, dep);
	}

	@Test
	public void testToDepXml() {
		final DepXml depXml = dep.toDepXml();
		assertThat(depXml.name, equalTo(dep.name));
		assertThat(depXml.origName, equalTo(dep.origname));
		assertThat(depXml.min, equalTo(dep.min));
		assertThat(depXml.max, equalTo(dep.max));
		assertThat(depXml.category, equalTo(dep.category));
		assertThat(depXml.unit, equalTo(dep.unit));
		assertThat(depXml.description, equalTo(dep.description));
	}

	private static void compare(Dep obtained, Dep expected) {
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.origname, equalTo(expected.origname));
		assertThat(obtained.min, equalTo(expected.min));
		assertThat(obtained.max, equalTo(expected.max));
		assertThat(obtained.category, equalTo(expected.category));
		assertThat(obtained.unit, equalTo(expected.unit));
		assertThat(obtained.description, equalTo(expected.description));
	}
}
