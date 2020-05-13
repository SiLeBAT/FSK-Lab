package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class IndepListTest {

	private static Indep indep = IndepTest.indep;

	@Test
	public void testIndeps() {
		final IndepList list = new IndepList();
		assertNull(list.getIndeps());

		final Indep[] indeps = new Indep[] { indep };
		list.setIndeps(indeps);

		final Indep expected = indeps[0]; // expected Indep
		final Indep obtained = list.getIndeps()[0]; // obtained Indep
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final Indep[] indeps = new Indep[] { indep };
		final IndepList list = new IndepList();
		list.setIndeps(indeps);

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		final Indep[] obtainedIndeps = new Indep[1];
		obtainedIndeps[0] = new Indep();
		obtainedIndeps[0].loadFromNodeSettings(settings.getNodeSettings("indeps" + 0));

		final Indep expected = indeps[0];  // expected Indep
		final Indep obtained = obtainedIndeps[0];  // obtained Indep
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numIndeps", 1);
		indep.saveToNodeSettings(settings.addNodeSettings("indeps" + 0));

		final IndepList list = new IndepList();
		list.loadFromNodeSettings(settings);

		final Indep expected = indep;  // expected Indep
		final Indep obtained = list.getIndeps()[0];  // obtained Indep
		TestUtils.compare(obtained, expected);
	}
}
