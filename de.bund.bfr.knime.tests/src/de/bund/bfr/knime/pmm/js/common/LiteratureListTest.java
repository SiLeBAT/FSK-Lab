package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class LiteratureListTest {

	static Literature literature;

	static {
		literature = new Literature();
		literature.id = LiteratureTest.id;
		literature.author = LiteratureTest.author;
		literature.title = LiteratureTest.title;
		literature.abstractText = LiteratureTest.abstractText;
		literature.year = LiteratureTest.year;
		literature.journal = LiteratureTest.journal;
		literature.volume = LiteratureTest.volume;
		literature.issue = LiteratureTest.issue;
		literature.page = LiteratureTest.page;
		literature.approvalMode = LiteratureTest.approvalMode;
		literature.website = LiteratureTest.website;
		literature.type = 0;
		literature.comment = LiteratureTest.comment;
		literature.dbuuid = LiteratureTest.dbuuid;
	}

	@Test
	public void testLiterature() {
		final LiteratureList list = new LiteratureList();
		assertNull(list.getLiterature());

		final Literature[] references = new Literature[] { literature };
		list.setLiterature(references);

		final Literature expected = references[0]; // expected Literature
		final Literature obtained = list.getLiterature()[0]; // obtained literature

		assertEquals(expected.id, obtained.id);
		assertEquals(expected.author, obtained.author);
		assertEquals(expected.title, obtained.title);
		assertEquals(expected.abstractText, obtained.abstractText);
		assertEquals(expected.year, obtained.year);
		assertEquals(expected.journal, obtained.journal);
		assertEquals(expected.volume, obtained.volume);
		assertEquals(expected.issue, obtained.issue);
		assertEquals(expected.page, obtained.page);
		assertEquals(expected.approvalMode, obtained.approvalMode);
		assertEquals(expected.website, obtained.website);
		assertEquals(expected.type, obtained.type);
		assertEquals(expected.comment, obtained.comment);
		assertEquals(expected.dbuuid, obtained.dbuuid);
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

		assertEquals(expected.id, obtained.id);
		assertEquals(expected.author, obtained.author);
		assertEquals(expected.title, obtained.title);
		assertEquals(expected.abstractText, obtained.abstractText);
		assertEquals(expected.year, obtained.year);
		assertEquals(expected.journal, obtained.journal);
		assertEquals(expected.volume, obtained.volume);
		assertEquals(expected.issue, obtained.issue);
		assertEquals(expected.page, obtained.page);
		assertEquals(expected.approvalMode, obtained.approvalMode);
		assertEquals(expected.website, obtained.website);
		assertEquals(expected.type, obtained.type);
		assertEquals(expected.comment, obtained.comment);
		assertEquals(expected.dbuuid, obtained.dbuuid);
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

		assertEquals(expected.id, obtained.id);
		assertEquals(expected.author, obtained.author);
		assertEquals(expected.title, obtained.title);
		assertEquals(expected.abstractText, obtained.abstractText);
		assertEquals(expected.year, obtained.year);
		assertEquals(expected.journal, obtained.journal);
		assertEquals(expected.volume, obtained.volume);
		assertEquals(expected.issue, obtained.issue);
		assertEquals(expected.page, obtained.page);
		assertEquals(expected.approvalMode, obtained.approvalMode);
		assertEquals(expected.website, obtained.website);
		assertEquals(expected.type, obtained.type);
		assertEquals(expected.comment, obtained.comment);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}
}
