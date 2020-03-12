package de.bund.bfr.knime.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("static-method")
public class NameAndDbIdTest {

	@Test
	public void testNameConstructor() {
		NameAndDbId identifier = new NameAndDbId("name");
		assertTrue(identifier.getId() < 0);
		assertEquals("name", identifier.getName());
	}

	@Test
	public void testFullConstructor() throws Exception {
		NameAndDbId identifier = new NameAndDbId("name", 0);
		assertEquals(0, identifier.getId());
		assertEquals("name", identifier.getName());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals() throws Exception {

		// Compare with same object
		NameAndDbId identifier = new NameAndDbId("name");
		assertTrue(identifier.equals(identifier));

		// Compare with null
		assertFalse(identifier.equals(null));

		// Compare with other class
		assertFalse(identifier.equals("a string"));

		// Compare with identifier with different id
		assertFalse(identifier.equals(new NameAndDbId("name", 2)));

		// Compare with identifier with same id
		assertTrue(identifier.equals(new NameAndDbId("name", identifier.getId())));

		// Compare null name with non-null name (same id)
		assertFalse(new NameAndDbId(null, identifier.getId()).equals(identifier));

		// Compare null name with null name (same id)
		assertTrue(new NameAndDbId(null, identifier.getId()).equals(new NameAndDbId(null, identifier.getId())));

		// Compare non-null name with different name (same id)
		assertFalse(identifier.equals(new NameAndDbId("other", identifier.getId())));
	}
	
	@Test
	public void testHashCode() throws Exception {
		NameAndDbId oneIdentifier = new NameAndDbId("name", 0);
		NameAndDbId anotherIdentifier = new NameAndDbId("name", 0);
		assertEquals(oneIdentifier.hashCode(), anotherIdentifier.hashCode());
	}
}
