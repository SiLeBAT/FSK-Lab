package de.bund.bfr.knime.pmm.common.generictablemodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.PmmException;

public class KnimeSchemaTest {

	@Test(expected = PmmException.class)
	public void testAddIntAttributeFailure() {

		KnimeSchema schema = new KnimeSchema();
		schema.addIntAttribute(null);
	}

	@Test
	public void testAddIntAttribute() {

		KnimeSchema schema = new KnimeSchema();
		schema.addIntAttribute("attribute");
		assertTrue(schema.containsAtt("attribute"));
	}

	@Test(expected = PmmException.class)
	public void testAddDoubleAttributeFailure() {

		KnimeSchema schema = new KnimeSchema();
		schema.addDoubleAttribute(null);
	}

	@Test
	public void testAddDoubleAttribute() {

		KnimeSchema schema = new KnimeSchema();
		schema.addDoubleAttribute("attribute");
		assertTrue(schema.containsAtt("attribute"));
	}

	@Test(expected = PmmException.class)
	public void testAddStringAttributeFailure() {

		KnimeSchema schema = new KnimeSchema();
		schema.addStringAttribute(null);
	}

	@Test
	public void testAddStringAttribute() {

		KnimeSchema schema = new KnimeSchema();
		schema.addStringAttribute("attribute");
		assertTrue(schema.containsAtt("attribute"));
	}

	@Test(expected = PmmException.class)
	public void testAddXmlAttributeFailure() {

		KnimeSchema schema = new KnimeSchema();
		schema.addXmlAttribute(null);
	}

	@Test
	public void testAddXmlAttribute() {

		KnimeSchema schema = new KnimeSchema();
		schema.addXmlAttribute("attribute");
		assertTrue(schema.containsAtt("attribute"));
	}

	@Test(expected = PmmException.class)
	public void testConstructorNull1() {
		new KnimeSchema(null, new KnimeSchema());
	}

	@Test(expected = PmmException.class)
	public void testConstructorNull2() {
		new KnimeSchema(new KnimeSchema(), null);
	}

	@Test(expected = PmmException.class)
	public void testConstructorDuplicated() {
		KnimeSchema schema = new KnimeSchema();
		schema.addIntAttribute("attribute");

		new KnimeSchema(schema, schema);
	}

	@Test
	public void testConstructor() {
		KnimeSchema schemaA = new KnimeSchema();
		schemaA.addIntAttribute("a");

		KnimeSchema schemaB = new KnimeSchema();
		schemaB.addIntAttribute("b");

		KnimeSchema joinedSchema = new KnimeSchema(schemaA, schemaB);
		assertTrue(joinedSchema.containsAtt("a"));
		assertTrue(joinedSchema.containsAtt("b"));
	}

	@Test
	public void testMerge() throws Exception {
		KnimeSchema schemaA = new KnimeSchema();
		schemaA.addIntAttribute("a");

		KnimeSchema schemaB = new KnimeSchema();
		schemaB.addIntAttribute("b");

		KnimeSchema joinedSchema = KnimeSchema.merge(schemaA, schemaB);
		assertTrue(joinedSchema.containsAtt("a"));
		assertTrue(joinedSchema.containsAtt("b"));
	}

	@Test
	public void testNotContains() {
		KnimeSchema schema = new KnimeSchema();
		assertFalse(schema.containsAtt("attribute"));
	}

	// FIXME: testCreateSpec. Fails when running as test plugin
	/*
	 * @Test public void testCreateSpec() {
	 * 
	 * KnimeSchema schema = new KnimeSchema();
	 * schema.addIntAttribute("integerAttribute");
	 * schema.addDoubleAttribute("doubleAttribute");
	 * schema.addXmlAttribute("xmlAttribute");
	 * schema.addStringAttribute("stringAttribute");
	 * 
	 * DataTableSpec spec = schema.createSpec();
	 * 
	 * DataColumnSpec spec0 = spec.getColumnSpec(0);
	 * assertEquals("integerAttribute", spec0.getName()); assertTrue(IntCell.TYPE ==
	 * spec0.getType()); }
	 */
}
