package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Element;
import org.junit.Test;

import de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem.Type;

@SuppressWarnings("static-method")
public class LiteratureItemTest {

	@Test
	public void testConstructors() {

		// Test fully parameterized constructor
		final LiteratureItem item0 = new LiteratureItem(Type.M, "author", 0, "title", "abstractText", "journal", "volume",
				"issue", 0, 0, "website", 0, "comment", 0, "dbuuid");
		assertTrue(0 == item0.id);
		assertEquals("author", item0.author);
		assertTrue(0 == item0.year);
		assertEquals("journal", item0.journal);
		assertEquals("volume", item0.volume);
		assertEquals("issue", item0.issue);
		assertTrue(0 == item0.page);
		assertTrue(0 == item0.approvalMode);
		assertEquals("website", item0.website);
		assertTrue(0 == item0.type);
		assertEquals("comment", item0.comment);
		assertEquals("dbuuid", item0.dbuuid);

		// Test constructor without dbbuid
		final LiteratureItem item1 = new LiteratureItem(Type.M, "author", 0, "title", "abstractText", "journal", "volume",
				"issue", 0, 0, "website", 0, "comment", 0);
		assertTrue(0 == item1.id);
		assertEquals("author", item1.author);
		assertTrue(0 == item1.year);
		assertEquals("journal", item1.journal);
		assertEquals("volume", item1.volume);
		assertEquals("issue", item1.issue);
		assertTrue(0 == item1.page);
		assertTrue(0 == item1.approvalMode);
		assertEquals("website", item1.website);
		assertTrue(0 == item1.type);
		assertEquals("comment", item1.comment);
		assertNull(item1.dbuuid);

		// Test constructor without dbbuid and id
		final LiteratureItem item2 = new LiteratureItem(Type.M, "author", 0, "title", "abstractText", "journal", "volume",
				"issue", 0, 0, "website", 0, "comment");
		assertTrue(item2.id < 0);
		assertEquals("author", item2.author);
		assertTrue(0 == item2.year);
		assertEquals("journal", item2.journal);
		assertEquals("volume", item2.volume);
		assertEquals("issue", item2.issue);
		assertTrue(0 == item2.page);
		assertTrue(0 == item2.approvalMode);
		assertEquals("website", item2.website);
		assertTrue(0 == item2.type);
		assertEquals("comment", item2.comment);
		assertNull(item2.dbuuid);

		// Test copy constructor with Element
		final Element element = new Element("MLiteratureItem");
		element.setAttribute("id", "0");
		element.setAttribute("author", "author");
		element.setAttribute("year", "0");
		element.setAttribute("journal", "journal");
		element.setAttribute("volume", "volume");
		element.setAttribute("issue", "issue");
		element.setAttribute("page", "0");
		element.setAttribute("approvalMode", "0");
		element.setAttribute("website", "website");
		element.setAttribute("type", "0");
		element.setAttribute("comment", "comment");
		element.setAttribute("dbuuid", "dbuuid");

		final LiteratureItem item3 = new LiteratureItem(element);
		assertTrue(0 == item3.id);
		assertEquals("author", item3.author);
		assertTrue(0 == item3.year);
		assertEquals("journal", item3.journal);
		assertEquals("volume", item3.volume);
		assertEquals("issue", item3.issue);
		assertTrue(0 == item3.page);
		assertTrue(0 == item3.approvalMode);
		assertEquals("website", item3.website);
		assertTrue(0 == item3.type);
		assertEquals("comment", item3.comment);
		assertEquals("dbuuid", item3.dbuuid);
	}

	@Test
	public void testElementConstructorEstimatedModel() {
		new LiteratureItem(new Element("EstimatedModelLiterature"));
	}

	@Test
	public void testElementConstructorMD() {
		new LiteratureItem(new Element("MDLiteratureItem"));
	}

	@Test
	public void testElementConstructorM() {
		new LiteratureItem(new Element("MLiteratureItem"));
	}

	@Test(expected = RuntimeException.class)
	public void testElementConstructorOther() {
		new LiteratureItem(new Element("other"));
	}

	@Test
	public void testGetElementName() {
		assertEquals("EstimatedModelLiterature",
				new LiteratureItem(new Element("EstimatedModelLiterature")).getElementName());
		assertEquals("MDLiteratureItem", new LiteratureItem(new Element("MDLiteratureItem")).getElementName());
		assertEquals("MLiteratureItem", new LiteratureItem(new Element("MLiteratureItem")).getElementName());
	}

	@Test
	public void testToXmlElement() throws Exception {
		final LiteratureItem item = new LiteratureItem(Type.M, "author", 0, "title", "abstractText", "journal", "volume",
				"issue", 0, 0, "website", 0, "comment", 0, "dbuuid");
		final Element element = item.toXmlElement();

		assertTrue(0 == element.getAttribute("id").getIntValue());
		assertEquals("author", element.getAttributeValue("author"));
		assertTrue(0 == element.getAttribute("year").getIntValue());
		assertEquals("journal", element.getAttributeValue("journal"));
		assertEquals("volume", element.getAttributeValue("volume"));
		assertEquals("issue", element.getAttributeValue("issue"));
		assertTrue(0 == element.getAttribute("page").getIntValue());
		assertTrue(0 == element.getAttribute("approvalMode").getIntValue());
		assertEquals("website", element.getAttributeValue("website"));
		assertTrue(0 == element.getAttribute("type").getIntValue());
		assertEquals("comment", element.getAttributeValue("comment"));
		assertEquals("dbuuid", element.getAttributeValue("dbuuid"));
	}
}
