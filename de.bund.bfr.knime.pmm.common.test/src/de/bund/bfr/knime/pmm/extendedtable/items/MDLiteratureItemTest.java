package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Element;
import org.junit.Test;

public class MDLiteratureItemTest {

	@Test
	public void testConstructors() {

		// Test fully parameterized constructor
		MDLiteratureItem item0 = new MDLiteratureItem("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment", 0, "dbuuid");
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
		MDLiteratureItem item1 = new MDLiteratureItem("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment", 0);
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
		MDLiteratureItem item2 = new MDLiteratureItem("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment");
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
		Element element = new Element(MDLiteratureItem.ELEMENT_LITERATURE);
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
		
		MDLiteratureItem item3 = new MDLiteratureItem(element);
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
	public void testToXmlElement() throws Exception {
		MDLiteratureItem item = new MDLiteratureItem("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment", 0, "dbuuid");
		Element element = item.toXmlElement();
		
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
