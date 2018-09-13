package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class LiteratureListTest {

	static Literature literature;

	static {
		literature = new Literature();
		literature.setId(LiteratureTest.id);
		literature.setAuthor(LiteratureTest.author);
		literature.setTitle(LiteratureTest.title);
		literature.setAbstractText(LiteratureTest.abstractText);
		literature.setYear(LiteratureTest.year);
		literature.setJournal(LiteratureTest.journal);
		literature.setVolume(LiteratureTest.volume);
		literature.setIssue(LiteratureTest.issue);
		literature.setPage(LiteratureTest.page);
		literature.setApprovalMode(LiteratureTest.approvalMode);
		literature.setWebsite(LiteratureTest.website);
		literature.setType(0);
		literature.setComment(LiteratureTest.comment);
		literature.setDbuuid(LiteratureTest.dbuuid);
	}

	@Test
	public void testLiterature() {
		LiteratureList list = new LiteratureList();
		assertNull(list.getLiterature());

		Literature[] references = new Literature[] { literature };
		list.setLiterature(references);

		Literature expected = references[0]; // expected Literature
		Literature obtained = list.getLiterature()[0]; // obtained literature

		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getAuthor(), obtained.getAuthor());
		assertEquals(expected.getTitle(), obtained.getTitle());
		assertEquals(expected.getAbstractText(), obtained.getAbstractText());
		assertEquals(expected.getYear(), obtained.getYear());
		assertEquals(expected.getJournal(), obtained.getJournal());
		assertEquals(expected.getVolume(), obtained.getVolume());
		assertEquals(expected.getIssue(), obtained.getIssue());
		assertEquals(expected.getPage(), obtained.getPage());
		assertEquals(expected.getApprovalMode(), obtained.getApprovalMode());
		assertEquals(expected.getWebsite(), obtained.getWebsite());
		assertEquals(expected.getType(), obtained.getType());
		assertEquals(expected.getComment(), obtained.getComment());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Literature[] references = new Literature[] { literature };
		LiteratureList list = new LiteratureList();
		list.setLiterature(references);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt(LiteratureList.NUM_LITERATURE));

		Literature[] obtainedReferences = new Literature[1];
		obtainedReferences[0] = new Literature();
		obtainedReferences[0].loadFromNodeSettings(settings.getNodeSettings(LiteratureList.LITERATURE + 0));

		Literature expected = references[0]; // expected literature
		Literature obtained = obtainedReferences[0]; // obtained literature

		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getAuthor(), obtained.getAuthor());
		assertEquals(expected.getTitle(), obtained.getTitle());
		assertEquals(expected.getAbstractText(), obtained.getAbstractText());
		assertEquals(expected.getYear(), obtained.getYear());
		assertEquals(expected.getJournal(), obtained.getJournal());
		assertEquals(expected.getVolume(), obtained.getVolume());
		assertEquals(expected.getIssue(), obtained.getIssue());
		assertEquals(expected.getPage(), obtained.getPage());
		assertEquals(expected.getApprovalMode(), obtained.getApprovalMode());
		assertEquals(expected.getWebsite(), obtained.getWebsite());
		assertEquals(expected.getType(), obtained.getType());
		assertEquals(expected.getComment(), obtained.getComment());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(LiteratureList.NUM_LITERATURE, 1);
		literature.saveToNodeSettings(settings.addNodeSettings(LiteratureList.LITERATURE + 0));

		LiteratureList list = new LiteratureList();
		list.loadFromNodeSettings(settings);

		Literature expected = literature; // expected Literature
		Literature obtained = list.getLiterature()[0]; // obtained Literature

		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getAuthor(), obtained.getAuthor());
		assertEquals(expected.getTitle(), obtained.getTitle());
		assertEquals(expected.getAbstractText(), obtained.getAbstractText());
		assertEquals(expected.getYear(), obtained.getYear());
		assertEquals(expected.getJournal(), obtained.getJournal());
		assertEquals(expected.getVolume(), obtained.getVolume());
		assertEquals(expected.getIssue(), obtained.getIssue());
		assertEquals(expected.getPage(), obtained.getPage());
		assertEquals(expected.getApprovalMode(), obtained.getApprovalMode());
		assertEquals(expected.getWebsite(), obtained.getWebsite());
		assertEquals(expected.getType(), obtained.getType());
		assertEquals(expected.getComment(), obtained.getComment());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}
}
