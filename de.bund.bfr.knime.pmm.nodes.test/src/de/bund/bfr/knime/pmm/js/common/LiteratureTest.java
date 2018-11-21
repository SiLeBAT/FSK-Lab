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
	public void testConstructor() {
		Literature literature = new Literature();
		assertNull(literature.id);
		assertNull(literature.author);
		assertNull(literature.title);
		assertNull(literature.abstractText);
		assertNull(literature.year);
		assertNull(literature.journal);
		assertNull(literature.volume);
		assertNull(literature.page);
		assertNull(literature.approvalMode);
		assertNull(literature.website);
		assertNull(literature.type);
		assertNull(literature.comment);
		assertNull(literature.dbuuid);
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		Literature literature = new Literature();
		literature.id = id;
		literature.author = author;
		literature.title = title;
		literature.abstractText = abstractText;
		literature.year = year;
		literature.journal = journal;
		literature.volume = volume;
		literature.issue = issue;
		literature.page = page;
		literature.approvalMode = approvalMode;
		literature.website = website;
		literature.type = 0;
		literature.comment = comment;
		literature.dbuuid = dbuuid;

		NodeSettings settings = new NodeSettings("irrelevantKey");
		literature.saveToNodeSettings(settings);

		assertEquals(id, settings.getInt("id"));
		assertEquals(author, settings.getString("author"));
		assertEquals(title, settings.getString("title"));
		assertEquals(abstractText, settings.getString("abstract"));
		assertEquals(year, settings.getInt("year"));
		assertEquals(journal, settings.getString("journal"));
		assertEquals(volume, settings.getString("volume"));
		assertEquals(issue, settings.getString("issue"));
		assertEquals(page, settings.getInt("page"));
		assertEquals(approvalMode, settings.getInt("approvalMode"));
		assertEquals(website, settings.getString("website"));
		assertEquals(type, settings.getInt("type"));
		assertEquals(comment, settings.getString("comment"));
		assertEquals(dbuuid, settings.getString("dbuuid"));
	}

	@Test
	public void testLoadFromNodeSettings() {

		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("author", author);
		settings.addString("title", title);
		settings.addString("abstract", abstractText);
		settings.addInt("year", year);
		settings.addString("journal", journal);
		settings.addString("volume", volume);
		settings.addString("issue", issue);
		settings.addInt("page", page);
		settings.addInt("approvalMode", approvalMode);
		settings.addString("website", website);
		settings.addInt("type", type);
		settings.addString("comment", comment);
		settings.addString("dbuuid", dbuuid);

		Literature literature = new Literature();
		literature.loadFromNodeSettings(settings);

		assertEquals(id, literature.id.intValue());
		assertEquals(author, literature.author);
		assertEquals(title, literature.title);
		assertEquals(abstractText, literature.abstractText);
		assertEquals(year, literature.year.intValue());
		assertEquals(journal, literature.journal);
		assertEquals(volume, literature.volume);
		assertEquals(issue, literature.issue);
		assertEquals(page, literature.page.intValue());
		assertEquals(approvalMode, literature.approvalMode.intValue());
		assertEquals(website, literature.website);
		assertEquals(type, literature.type.intValue());
		assertEquals(comment, literature.comment);
		assertEquals(dbuuid, literature.dbuuid);
	}
	
	@Test
	public void testToLiterature() {
		LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type, comment, id, dbuuid);
		Literature literature = Literature.toLiterature(literatureItem);
		
		assertEquals(id, literature.id.intValue());
		assertEquals(author, literature.author);
		assertEquals(title, literature.title);
		assertEquals(abstractText, literature.abstractText);
		assertEquals(year, literature.year.intValue());
		assertEquals(journal, literature.journal);
		assertEquals(volume, literature.volume);
		assertEquals(issue, literature.issue);
		assertEquals(page, literature.page.intValue());
		assertEquals(approvalMode, literature.approvalMode.intValue());
		assertEquals(website, literature.website);
		assertEquals(type, literature.type.intValue());
		assertEquals(comment, literature.comment);
		assertEquals(dbuuid, literature.dbuuid);
	}
	
	@Test
	public void testToLiteratureItem() {
		Literature literature = new Literature();
		literature.id = id;
		literature.author = author;
		literature.title = title;
		literature.abstractText = abstractText;
		literature.year = year;
		literature.journal = journal;
		literature.volume = volume;
		literature.issue = issue;
		literature.page = page;
		literature.approvalMode = approvalMode;
		literature.website = website;
		literature.type = 0;
		literature.comment = comment;
		literature.dbuuid = dbuuid;
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
