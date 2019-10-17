package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class MiscListTest {

	static Misc misc = MiscTest.misc;

	@Test
	public void testMiscs() {
		final MiscList list = new MiscList();
		assertThat(list.getMiscs(), is(nullValue()));

		list.setMiscs(new Misc[] { misc });

		final Misc expected = misc;  // expected Misc
		final Misc obtained = list.getMiscs()[0];  // obtained Misc
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final MiscList list = new MiscList();
		list.setMiscs(new Misc[] { misc });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertThat(settings.getInt("numMiscs"), is(1));

		final Misc expected = misc;  // expected Misc
		final Misc obtained = new Misc();  // obtained Misc
		obtained.loadFromNodeSettings(settings.getNodeSettings("miscs0"));

		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numMiscs", 1);
		misc.saveToNodeSettings(settings.addNodeSettings("miscs0"));

		final MiscList list = new MiscList();
		list.loadFromNodeSettings(settings);

		final Misc expected = misc;  // expected Misc
		final Misc obtained = list.getMiscs()[0];  // obtained Misc
		TestUtils.compare(obtained, expected);
	}
}
