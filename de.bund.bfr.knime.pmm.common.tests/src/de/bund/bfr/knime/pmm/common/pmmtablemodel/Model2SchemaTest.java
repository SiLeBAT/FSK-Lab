package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;

@SuppressWarnings("static-method")
public class Model2SchemaTest {

	@Test
	public void test() {

		final Model2Schema schema = new Model2Schema();

		assertEquals("CatModelSec", schema.getName(0));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(0));

		assertEquals("DependentSec", schema.getName(1));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(1));

		assertEquals("IndependentSec", schema.getName(2));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(2));

		assertEquals("ParameterSec", schema.getName(3));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(3));

		assertEquals("EstModelSec", schema.getName(4));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(4));

		assertEquals("M_LiteraturSec", schema.getName(5));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(5));

		assertEquals("EM_LiteraturSec", schema.getName(6));
		assertEquals(KnimeAttribute.TYPE_XML, schema.getType(6));

		assertEquals("DatabaseWritableSec", schema.getName(7));
		assertEquals(KnimeAttribute.TYPE_INT, schema.getType(7));

		assertEquals("M_DB_UIDSec", schema.getName(8));
		assertEquals(KnimeAttribute.TYPE_STRING, schema.getType(8));

		assertEquals("GlobalModelID", schema.getName(9));
		assertEquals(KnimeAttribute.TYPE_INT, schema.getType(9));
	}
}
