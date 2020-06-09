package metadata.swagger;

import static org.junit.Assert.assertEquals;

import de.bund.bfr.metadata.swagger.Contact;

class TestUtils {

	private TestUtils() {
	}

	static void testFirstCreator(Contact firstCreator) {
		assertEquals("Robert", firstCreator.getGivenName());
		assertEquals("Lang", firstCreator.getFamilyName());
		assertEquals("Merry-Go-Round", firstCreator.getOrganization());
		assertEquals("061 770 54 22", firstCreator.getTelephone());
		assertEquals("RobertLang@gustr.com", firstCreator.getEmail());
		assertEquals("Switzerland", firstCreator.getCountry());
		assertEquals("4414", firstCreator.getZipCode());
		assertEquals("Im Sandbüel 38", firstCreator.getStreetAddress());
	}

	static void testFirstAuthor(Contact firstAuthor) {
		assertEquals("Marko", firstAuthor.getGivenName());
		assertEquals("Wurfel", firstAuthor.getFamilyName());
		assertEquals("Weatherill's", firstAuthor.getOrganization());
		assertEquals("02636 67 00 13", firstAuthor.getTelephone());
		assertEquals("MarkoWurfel@gustr.com", firstAuthor.getEmail());
		assertEquals("Germany", firstAuthor.getCountry());
		assertEquals("56651", firstAuthor.getZipCode());
		assertEquals("Wallstrasse 51", firstAuthor.getStreetAddress());
	}
}
