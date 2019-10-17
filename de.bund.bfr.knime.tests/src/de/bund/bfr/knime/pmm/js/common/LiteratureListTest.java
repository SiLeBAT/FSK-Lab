package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class LiteratureListTest {

	private static Literature literature = LiteratureTest.literature;

	@Test
	public void testLiterature() {
		final LiteratureList list = new LiteratureList();
		assertNull(list.getLiterature());

		final Literature[] references = new Literature[] { literature };
		list.setLiterature(references);

		final Literature expected = references[0]; // expected Literature
		final Literature obtained = list.getLiterature()[0]; // obtained literature
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final Literature[] references = new Literature[] { literature };
		final LiteratureList list = new LiteratureList();
		list.setLiterature(references);

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numLiterature"));

		final Literature[] obtainedReferences = new Literature[1];
		obtainedReferences[0] = new Literature();
		obtainedReferences[0].loadFromNodeSettings(settings.getNodeSettings("literature" + 0));

		final Literature expected = references[0]; // expected literature
		final Literature obtained = obtainedReferences[0]; // obtained literature
		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numLiterature", 1);
		literature.saveToNodeSettings(settings.addNodeSettings("literature" + 0));

		final LiteratureList list = new LiteratureList();
		list.loadFromNodeSettings(settings);

		final Literature expected = literature; // expected Literature
		final Literature obtained = list.getLiterature()[0]; // obtained Literature
		TestUtils.compare(obtained, expected);
	}
}
