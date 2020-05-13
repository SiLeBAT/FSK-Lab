package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

@SuppressWarnings("static-method")
public class LiteratureTest {

	static Literature literature;
	static {
		literature = new Literature();
		literature.id = 56;
		literature.author = "Baranyi, J.";
		literature.title = "A dynamic approach to predicting bacterial growth in food";
		literature.abstractText = "A new member ...";
		literature.year = 1994;
		literature.journal = "International Journal of Food Microbiology";
		literature.volume = "23";
		literature.issue = "3";
		literature.page = 277;
		literature.approvalMode = 1;
		literature.website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
		literature.type = 0;
		literature.comment = "improvised comment";
		literature.dbuuid = "385a7e4a-0dfa-11e6-9f1a-0030846f8413";
	}

	@Test
	public void testConstructor() {
		final Literature literature = new Literature();
		assertThat(literature.id, is(nullValue()));
		assertThat(literature.author, is(nullValue()));
		assertThat(literature.title, is(nullValue()));
		assertThat(literature.abstractText, is(nullValue()));
		assertThat(literature.year, is(nullValue()));
		assertThat(literature.journal, is(nullValue()));
		assertThat(literature.volume, is(nullValue()));
		assertThat(literature.page, is(nullValue()));
		assertThat(literature.approvalMode, is(nullValue()));
		assertThat(literature.website, is(nullValue()));
		assertThat(literature.type, is(nullValue()));
		assertThat(literature.comment, is(nullValue()));
		assertThat(literature.dbuuid, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		literature.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(literature.id));
		assertThat(settings.getString("author"), equalTo(literature.author));
		assertThat(settings.getString("title"), equalTo(literature.title));
		assertThat(settings.getString("abstract"), equalTo(literature.abstractText));
		assertThat(settings.getInt("year"), equalTo(literature.year));
		assertThat(settings.getString("journal"), equalTo(literature.journal));
		assertThat(settings.getString("volume"), equalTo(literature.volume));
		assertThat(settings.getString("issue"), equalTo(literature.issue));
		assertThat(settings.getInt("page"), equalTo(literature.page));
		assertThat(settings.getInt("approvalMode"), equalTo(literature.approvalMode));
		assertThat(settings.getString("website"), equalTo(literature.website));
		assertThat(settings.getInt("type"), equalTo(literature.type));
		assertThat(settings.getString("comment"), equalTo(literature.comment));
		assertThat(settings.getString("dbuuid"), equalTo(literature.dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", literature.id);
		settings.addString("author", literature.author);
		settings.addString("title", literature.title);
		settings.addString("abstract", literature.abstractText);
		settings.addInt("year", literature.year);
		settings.addString("journal", literature.journal);
		settings.addString("volume", literature.volume);
		settings.addString("issue", literature.issue);
		settings.addInt("page", literature.page);
		settings.addInt("approvalMode", literature.approvalMode);
		settings.addString("website", literature.website);
		settings.addInt("type", literature.type);
		settings.addString("comment", literature.comment);
		settings.addString("dbuuid", literature.dbuuid);

		final Literature obtained = new Literature();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, literature);
	}

	@Test
	public void testToLiterature() {
		final LiteratureItem literatureItem = new LiteratureItem(literature.author, literature.year, literature.title,
				literature.abstractText, literature.journal, literature.volume, literature.issue, literature.page,
				literature.approvalMode, literature.website, literature.type, literature.comment, literature.id,
				literature.dbuuid);
		final Literature obtained = Literature.toLiterature(literatureItem);
		TestUtils.compare(obtained, literature);
	}

	@Test
	public void testToLiteratureItem() {
		final LiteratureItem literatureItem = literature.toLiteratureItem();
		assertThat(literatureItem.id, equalTo(literature.id));
		assertThat(literatureItem.author, equalTo(literature.author));
		assertThat(literatureItem.title, equalTo(literature.title));
		assertThat(literatureItem.abstractText, equalTo(literature.abstractText));
		assertThat(literatureItem.year, equalTo(literature.year));
		assertThat(literatureItem.journal, equalTo(literature.journal));
		assertThat(literatureItem.volume, equalTo(literature.volume));
		assertThat(literatureItem.issue, equalTo(literature.issue));
		assertThat(literatureItem.page, equalTo(literature.page));
		assertThat(literatureItem.approvalMode, equalTo(literature.approvalMode));
		assertThat(literatureItem.website, equalTo(literature.website));
		assertThat(literatureItem.type, equalTo(literature.type));
		assertThat(literatureItem.comment, equalTo(literature.comment));
		assertThat(literatureItem.dbuuid, equalTo(literature.dbuuid));
	}
}
