package de.bund.bfr.knime.pmm.common.generictablemodel;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;

public class KnimeTupleTest {
	
	private static KnimeSchema schema;
	
	@BeforeClass
	public static void setUp() {
		schema = new KnimeSchema();
		schema.addIntAttribute("Entero");
		schema.addDoubleAttribute("Real");
		schema.addStringAttribute("Cadena");
		schema.addXmlAttribute("XML");
	}

	@Test
	public void testConstructor() throws Exception {
		KnimeTuple tuple = new KnimeTuple(schema);
		assertEquals(4, tuple.size());
		assertTrue(tuple.getCell(0).isMissing());
		assertTrue(tuple.getCell(1).isMissing());
		assertTrue(tuple.getCell(2).isMissing());
		
		
		KnimeSchema otherSchema = new KnimeSchema();
		otherSchema.addIntAttribute("int");
		KnimeTuple otherTuple = new KnimeTuple(otherSchema);
		
		try {
			new KnimeTuple(schema, tuple, otherTuple);
			fail("Expected PmmException");
		} catch (PmmException e) {}

		try {
			new KnimeTuple(schema, otherTuple, tuple);
			fail("Expected PmmException");
		} catch (PmmException e) {}
		
		new KnimeTuple(schema, tuple, tuple);
	}

	/**
	 * Test {@link KnimeTuple#getSchema()} and
	 * {@link KnimeTuple#setSchema(KnimeSchema)}
	 */
	@Test
	public void testSchema() {

		KnimeTuple tuple = new KnimeTuple(new KnimeSchema());
		assertEquals(0, tuple.getNumCells());

		KnimeSchema realSchema = new KnimeSchema();
		realSchema.addIntAttribute("Column");
		tuple.setSchema(realSchema);
		assertEquals(1, tuple.getNumCells());
	}

	/** Test {@link KnimeTuple#setSchema(KnimeSchema)} */
	@Test(expected = PmmException.class)
	public void testSchemaException() {
		KnimeTuple tuple = new KnimeTuple(new KnimeSchema());
		tuple.setSchema(null); // throw PmmException
	}

	@Test
	public void testGetIndex() {
		KnimeTuple tuple = new KnimeTuple(schema);
		assertEquals(0, tuple.getIndex("Entero"));
		assertEquals(1, tuple.getIndex("Real"));
		assertEquals(2, tuple.getIndex("Cadena"));
	}

	@Test
	public void testGetName() {
		KnimeTuple tuple = new KnimeTuple(schema);
		assertEquals("Entero", tuple.getName(0));
		assertEquals("Real", tuple.getName(1));
		assertEquals("Cadena", tuple.getName(2));
	}

	@Test
	public void testGetInt() {
		KnimeTuple tuple = new KnimeTuple(schema);

		// Set values
		tuple.setValue("Entero", 2);
		tuple.setValue("Real", 3.14);
		tuple.setValue("Cadena", "Hola mundo");
		tuple.setValue("XML", new PmmXmlDoc());

		// Test getter
		assertEquals(2, tuple.getInt("Entero").intValue());

		try {
			tuple.getInt("Real"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
		
		try {
			tuple.getInt("Cadena"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
		
		try {
			tuple.getInt("XML"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
	}
	
	@Test
	public void testGetDouble() {
		KnimeTuple tuple = new KnimeTuple(schema);
		
		// Set values
		tuple.setValue("Entero", 2);
		tuple.setValue("Real", 3.14);
		tuple.setValue("Cadena", "Hola mundo");
		tuple.setValue("XML", new PmmXmlDoc());
		
		// Test getter
		assertEquals(2.0, tuple.getDouble("Entero"), .0);
		assertEquals(3.14, tuple.getDouble("Real"), .0);
		
		try {
			tuple.getDouble("Cadena"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
		
		try {
			tuple.getDouble("XML"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
	}
	
	@Test
	public void testGetString() {
		KnimeTuple tuple = new KnimeTuple(schema);
		
		// Set values
		tuple.setValue("Entero", 2);
		tuple.setValue("Real", 3.14);
		tuple.setValue("Cadena", "Hola mundo");
		tuple.setValue("XML", new PmmXmlDoc());
		
		// Test getter
		assertEquals("2", tuple.getString("Entero"));
		assertEquals("3.14", tuple.getString("Real"));
		assertEquals("Hola mundo", tuple.getString("Cadena"));
		
		try {
			tuple.getString("XML"); // throws ClassCastException
			fail("Expected ClassCastException");
		} catch (ClassCastException e) {
		}
	}
	
	@Test
	public void testGetPmmXml() {
		KnimeTuple tuple = new KnimeTuple(schema);
		
		// Set values
		tuple.setValue("Entero", 2);
		tuple.setValue("Real", 3.14);
		tuple.setValue("Cadena", "Hola mundo");
		tuple.setValue("XML", new PmmXmlDoc());
		
		// Test getter
		try {
			tuple.getPmmXml("Entero"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
		
		try {
			tuple.getPmmXml("Real"); // throws PmmException
			fail("Expected PmmException");
		} catch (PmmException e) {
		}
		
		try {
			tuple.getPmmXml("Cadena"); // throws PmmException
			fail("Expected ClassCastException");
		} catch (PmmException e) {
		}
		
		tuple.getPmmXml("XML");
	}
}
