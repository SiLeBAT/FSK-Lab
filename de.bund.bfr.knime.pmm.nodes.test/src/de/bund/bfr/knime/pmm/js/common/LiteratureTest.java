package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class LiteratureTest {

	static int id = 56;
	static String author = "Baranyi, J.";
	static String title = "A dynamic approach to predicting bacterial growth in food";
	static String abstractText = "A new member ...";
	static int year = 1994;
	static String journal = "International Journal of Food Microbiology";
	static String volume = "23";
	static String issue = "3";
	static int page = 277;
	static int approvalMode = 1;
	static String website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
	static int type = 0;
	static String comment = "improvised comment";
	static String dbuuid = "385a7e4a-0dfa-11e6-9f1a-0030846f8413";

	@Test
	public void testId() {
		Literature literature = new Literature();
		assertNull(literature.getId());

		literature.setId(id);
		assertEquals(id, literature.getId().intValue());
	}

	@Test
	public void testAuthor() {
		Literature literature = new Literature();
		assertNull(literature.getAuthor());

		literature.setAuthor(author);
		assertEquals(author, literature.getAuthor());
	}

	@Test
	public void testTitle() {
		Literature literature = new Literature();
		assertNull(literature.getTitle());

		literature.setTitle(title);
		assertEquals(title, literature.getTitle());
	}

	@Test
	public void testAbstractText() {
		Literature literature = new Literature();
		assertNull(literature.getAbstractText());

		literature.setAbstractText(abstractText);
		assertEquals(abstractText, literature.getAbstractText());
	}

	@Test
	public void testYear() {
		Literature literature = new Literature();
		assertNull(literature.getYear());

		literature.setYear(year);
		assertEquals(year, literature.getYear().intValue());
	}

	@Test
	public void testJournal() {
		Literature literature = new Literature();
		assertNull(literature.getJournal());

		literature.setJournal(journal);
		assertEquals(journal, literature.getJournal());
	}

	@Test
	public void testVolume() {
		Literature literature = new Literature();
		assertNull(literature.getVolume());

		literature.setVolume(volume);
		assertEquals(volume, literature.getVolume());
	}

	@Test
	public void testPage() {
		Literature literature = new Literature();
		assertNull(literature.getPage());

		literature.setPage(page);
		assertEquals(page, literature.getPage().intValue());
	}

	@Test
	public void testApprovalMode() {
		Literature literature = new Literature();
		assertNull(literature.getApprovalMode());

		literature.setApprovalMode(approvalMode);
		assertEquals(approvalMode, literature.getApprovalMode().intValue());
	}

	@Test
	public void testWebsite() {
		Literature literature = new Literature();
		assertNull(literature.getWebsite());

		literature.setWebsite(website);
		assertEquals(website, literature.getWebsite());
	}

	@Test
	public void testType() {
		Literature literature = new Literature();
		assertNull(literature.getType());

		literature.setType(type);
		assertEquals(type, literature.getType().intValue());
	}

	@Test
	public void testComment() {
		Literature literature = new Literature();
		assertNull(literature.getComment());

		literature.setComment(comment);
		assertEquals(comment, literature.getComment());
	}

	@Test
	public void testDbuuid() {
		Literature literature = new Literature();
		assertNull(literature.getDbuuid());

		literature.setDbuuid(dbuuid);
		assertEquals(dbuuid, literature.getDbuuid());
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		Literature literature = new Literature();
		literature.setId(id);
		literature.setAuthor(author);
		literature.setTitle(title);
		literature.setAbstractText(abstractText);
		literature.setYear(year);
		literature.setJournal(journal);
		literature.setVolume(volume);
		literature.setIssue(issue);
		literature.setPage(page);
		literature.setApprovalMode(approvalMode);
		literature.setWebsite(website);
		literature.setType(type);
		literature.setComment(comment);
		literature.setDbuuid(dbuuid);

		NodeSettings settings = new NodeSettings("irrelevantKey");
		literature.saveToNodeSettings(settings);

		assertEquals(id, settings.getInt(Literature.ID));
		assertEquals(author, settings.getString(Literature.AUTHOR));
		assertEquals(title, settings.getString(Literature.TITLE));
		assertEquals(abstractText, settings.getString(Literature.ABSTRACT));
		assertEquals(year, settings.getInt(Literature.YEAR));
		assertEquals(journal, settings.getString(Literature.JOURNAL));
		assertEquals(volume, settings.getString(Literature.VOLUME));
		assertEquals(issue, settings.getString(Literature.ISSUE));
		assertEquals(page, settings.getInt(Literature.PAGE));
		assertEquals(approvalMode, settings.getInt(Literature.APPROVAL_MODE));
		assertEquals(website, settings.getString(Literature.WEBSITE));
		assertEquals(type, settings.getInt(Literature.TYPE));
		assertEquals(comment, settings.getString(Literature.COMMENT));
		assertEquals(dbuuid, settings.getString(Literature.DBUUID));
	}

	@Test
	public void testLoadFromNodeSettings() {

		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(Literature.ID, id);
		settings.addString(Literature.AUTHOR, author);
		settings.addString(Literature.TITLE, title);
		settings.addString(Literature.ABSTRACT, abstractText);
		settings.addInt(Literature.YEAR, year);
		settings.addString(Literature.JOURNAL, journal);
		settings.addString(Literature.VOLUME, volume);
		settings.addString(Literature.ISSUE, issue);
		settings.addInt(Literature.PAGE, page);
		settings.addInt(Literature.APPROVAL_MODE, approvalMode);
		settings.addString(Literature.WEBSITE, website);
		settings.addInt(Literature.TYPE, type);
		settings.addString(Literature.COMMENT, comment);
		settings.addString(Literature.DBUUID, dbuuid);

		Literature literature = new Literature();
		literature.loadFromNodeSettings(settings);

		assertEquals(id, literature.getId().intValue());
		assertEquals(author, literature.getAuthor());
		assertEquals(title, literature.getTitle());
		assertEquals(abstractText, literature.getAbstractText());
		assertEquals(year, literature.getYear().intValue());
		assertEquals(journal, literature.getJournal());
		assertEquals(volume, literature.getVolume());
		assertEquals(issue, literature.getIssue());
		assertEquals(page, literature.getPage().intValue());
		assertEquals(approvalMode, literature.getApprovalMode().intValue());
		assertEquals(website, literature.getWebsite());
		assertEquals(type, literature.getType().intValue());
		assertEquals(comment, literature.getComment());
		assertEquals(dbuuid, literature.getDbuuid());
	}
	
	@Test
	public void testToLiterature() {
		LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type, comment, id, dbuuid);
		Literature literature = Literature.toLiterature(literatureItem);
		
		assertEquals(id, literature.getId().intValue());
		assertEquals(author, literature.getAuthor());
		assertEquals(title, literature.getTitle());
		assertEquals(abstractText, literature.getAbstractText());
		assertEquals(year, literature.getYear().intValue());
		assertEquals(journal, literature.getJournal());
		assertEquals(volume, literature.getVolume());
		assertEquals(issue, literature.getIssue());
		assertEquals(page, literature.getPage().intValue());
		assertEquals(approvalMode, literature.getApprovalMode().intValue());
		assertEquals(website, literature.getWebsite());
		assertEquals(type, literature.getType().intValue());
		assertEquals(comment, literature.getComment());
		assertEquals(dbuuid, literature.getDbuuid());
	}
	
	@Test
	public void testToLiteratureItem() {
		Literature literature = new Literature();
		literature.setId(id);
		literature.setAuthor(author);
		literature.setTitle(title);
		literature.setAbstractText(abstractText);
		literature.setYear(year);
		literature.setJournal(journal);
		literature.setVolume(volume);
		literature.setIssue(issue);
		literature.setPage(page);
		literature.setApprovalMode(approvalMode);
		literature.setWebsite(website);
		literature.setType(type);
		literature.setComment(comment);
		literature.setDbuuid(dbuuid);
		LiteratureItem literatureItem = literature.toLiteratureItem();

		assertEquals(id, literatureItem.id.intValue());
		assertEquals(author, literatureItem.author);
		assertEquals(title, literatureItem.title);
		assertEquals(abstractText, literatureItem.abstractText);
		assertEquals(year, literatureItem.year.intValue());
		assertEquals(journal, literatureItem.journal);
		assertEquals(volume, literatureItem.volume);
		assertEquals(issue, literatureItem.issue);
		assertEquals(page, literatureItem.page.intValue());
		assertEquals(approvalMode, literatureItem.approvalMode.intValue());
		assertEquals(website, literatureItem.website);
		assertEquals(type, literatureItem.type.intValue());
		assertEquals(comment, literatureItem.comment);
		assertEquals(dbuuid, literatureItem.dbuuid);
	}
}
