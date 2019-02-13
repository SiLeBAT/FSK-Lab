package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

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
		LiteratureList list = new LiteratureList();
		assertNull(list.getLiterature());

		Literature[] references = new Literature[] { literature };
		list.setLiterature(references);

		Literature expected = references[0]; // expected Literature
		Literature obtained = list.getLiterature()[0]; // obtained literature

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
		Literature[] references = new Literature[] { literature };
		LiteratureList list = new LiteratureList();
		list.setLiterature(references);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numLiterature"));

		Literature[] obtainedReferences = new Literature[1];
		obtainedReferences[0] = new Literature();
		obtainedReferences[0].loadFromNodeSettings(settings.getNodeSettings("literature" + 0));

		Literature expected = references[0]; // expected literature
		Literature obtained = obtainedReferences[0]; // obtained literature

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
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numLiterature", 1);
		literature.saveToNodeSettings(settings.addNodeSettings("literature" + 0));

		LiteratureList list = new LiteratureList();
		list.loadFromNodeSettings(settings);

		Literature expected = literature; // expected Literature
		Literature obtained = list.getLiterature()[0]; // obtained Literature

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
