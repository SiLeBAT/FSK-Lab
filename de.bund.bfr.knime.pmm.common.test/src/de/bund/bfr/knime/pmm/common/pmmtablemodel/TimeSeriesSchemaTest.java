package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;

public class TimeSeriesSchemaTest {

	@Test
	public void test() {
		
		TimeSeriesSchema schema = new TimeSeriesSchema();
		
		assertEquals("CondID", schema.getName(0));
		assertEquals(KnimeAttribute.TYPE_INT, schema.getType(0));
		
		assertEquals("CombaseID", schema.getName(1));
		assertEquals(KnimeAttribute.TYPE_STRING, schema.getType(1));
		
		assertEquals("Organism", schema.getName(2));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(2));
		
		assertEquals("Matrix", schema.getName(3));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(3));
		
		assertEquals("MD_Data", schema.getName(4));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(4));
		
		assertEquals("Misc", schema.getName(5));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(5));
		
		assertEquals("MD_Info", schema.getName(6));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(6));
		
		assertEquals("MD_Literatur", schema.getName(7));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(7));
		
		assertEquals("MD_DB_UID", schema.getName(8));
		assertEquals(KnimeAttribute.TYPE_STRING, schema.getType(8));
	}
}
