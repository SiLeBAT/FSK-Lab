package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;

public class Model1SchemaTest {

	@Test
	public void test() {
		
		Model1Schema schema = new Model1Schema();
		
		assertEquals("CatModel", schema.getName(0));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(0));
		
		assertEquals("Dependent", schema.getName(1));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(1));
		
		assertEquals("Independent", schema.getName(2));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(2));
		
		assertEquals("Parameter", schema.getName(3));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(3));
		
		assertEquals("EstModel", schema.getName(4));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(4));
		
		assertEquals("M_Literatur", schema.getName(5));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(5));
		
		assertEquals("EM_Literatur", schema.getName(6));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(6));
		
		assertEquals("DatabaseWritable", schema.getName(7));
		assertEquals(KnimeAttribute.TYPE_INT, schema.getType(7));
		
		assertEquals("M_DB_UID", schema.getName(8));
		assertEquals(KnimeAttribute.TYPE_STRING, schema.getType(8));
	}
}
