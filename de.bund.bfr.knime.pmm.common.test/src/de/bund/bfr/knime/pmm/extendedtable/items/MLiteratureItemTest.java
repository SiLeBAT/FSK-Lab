package de.bund.bfr.knime.pmm.extendedtable.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Element;
import org.junit.Test;

public class MLiteratureItemTest {

	@Test
	public void testConstructors() {

		// Test fully parameterized constructor
		MLiteratureItem item0 = new MLiteratureItem ("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment", 0, "dbuuid");
		assertTrue(0 == item0.getId());
		assertEquals("author", item0.getAuthor());
		assertTrue(0 == item0.getYear());
		assertEquals("journal", item0.getJournal());
		assertEquals("volume", item0.getVolume());
		assertEquals("issue", item0.getIssue());
		assertTrue(0 == item0.getPage());
		assertTrue(0 == item0.getApprovalMode());
		assertEquals("website", item0.getWebsite());
		assertTrue(0 == item0.getType());
		assertEquals("comment", item0.getComment());
		assertEquals("dbuuid", item0.getDbuuid());

		// Test constructor without dbbuid
		MLiteratureItem  item1 = new MLiteratureItem ("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment", 0);
		assertTrue(0 == item1.getId());
		assertEquals("author", item1.getAuthor());
		assertTrue(0 == item1.getYear());
		assertEquals("journal", item1.getJournal());
		assertEquals("volume", item1.getVolume());
		assertEquals("issue", item1.getIssue());
		assertTrue(0 == item1.getPage());
		assertTrue(0 == item1.getApprovalMode());
		assertEquals("website", item1.getWebsite());
		assertTrue(0 == item1.getType());
		assertEquals("comment", item1.getComment());
		assertNull(item1.getDbuuid());

		// Test constructor without dbbuid and id
		MLiteratureItem  item2 = new MLiteratureItem ("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
				0, "website", 0, "comment");
		assertTrue(item2.getId() < 0);
		assertEquals("author", item2.getAuthor());
		assertTrue(0 == item2.getYear());
		assertEquals("journal", item2.getJournal());
		assertEquals("volume", item2.getVolume());
		assertEquals("issue", item2.getIssue());
		assertTrue(0 == item2.getPage());
		assertTrue(0 == item2.getApprovalMode());
		assertEquals("website", item2.getWebsite());
		assertTrue(0 == item2.getType());
		assertEquals("comment", item2.getComment());
		assertNull(item2.getDbuuid());
		
		// Test copy constructor with Element
		Element element = new Element(MLiteratureItem .ELEMENT_LITERATURE);
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
		
		MLiteratureItem  item3 = new MLiteratureItem (element);
		assertTrue(0 == item3.getId());
		assertEquals("author", item3.getAuthor());
		assertTrue(0 == item3.getYear());
		assertEquals("journal", item3.getJournal());
		assertEquals("volume", item3.getVolume());
		assertEquals("issue", item3.getIssue());
		assertTrue(0 == item3.getPage());
		assertTrue(0 == item3.getApprovalMode());
		assertEquals("website", item3.getWebsite());
		assertTrue(0 == item3.getType());
		assertEquals("comment", item3.getComment());
		assertEquals("dbuuid", item3.getDbuuid());
	}

	@Test
	public void testToXmlElement() throws Exception {
		MLiteratureItem  item = new MLiteratureItem ("author", 0, "title", "abstractText", "journal", "volume", "issue", 0,
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
