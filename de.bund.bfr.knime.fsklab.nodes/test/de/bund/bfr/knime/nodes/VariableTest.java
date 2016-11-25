package de.bund.bfr.knime.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;

public class VariableTest {

	@Test
	public void test() {
		// Test default values
		Variable v = new Variable();
		assertNull(v.name);
		assertNull(v.unit);
		assertNull(v.min);
		assertNull(v.max);
		assertNull(v.type);
	}
	
	@Test
	public void testToString() {
		Variable v = new Variable();
		v.name = "Prevalence";
		assertEquals("Variable [Prevalence]", v.toString());
	}

	@Test
	public void testEquals() {
		Variable p = new Variable();
		p.name = "Prevalence";
		p.unit = "percent";
		p.value = "50";
		p.min = "10";
		p.max = "100";
		p.type = DataType.numeric;

		// equals with same object should return true
		assertTrue(p.equals(p));

		// equals with null should return false
		assertFalse(p.equals(null));

		// equals with a different class object should return false
		assertFalse(p.equals(new Integer(0)));

		// equals with matching object
		Variable other = new Variable();
		other.name = p.name;
		other.unit = p.unit;
		other.value = p.value;
		other.min = p.min;
		other.max = p.max;
		other.type = p.type;
		assertEquals(p, other);

		// equals with different name
		other.name = "n.iter";
		assertFalse(p.equals(other));
		other.name = p.name;

		// equals with different unit
		other.unit = "no_name";
		assertFalse(p.equals(other));
		other.unit = p.unit;

		// equals with different value
		other.value = "200";
		assertFalse(p.equals(other));
		other.value = p.value;

		// equals with different min
		other.min = "1";
		assertFalse(p.equals(other));
		other.min = p.min;

		// equals with different max
		other.max = "1e8";
		assertFalse(p.equals(other));
		other.max = p.max;

		// equals with different type
		other.type = DataType.integer;
		assertFalse(p.equals(other));
		other.type = p.type;
	}
	
	@Test
	public void testHashCode() throws Exception {
		Variable v1 = new Variable();
		v1.name = "Prevalence";
		v1.unit = "percent";
		v1.value = "50";
		v1.min = "10";
		v1.max = "100";
		v1.type = DataType.numeric;
		
		Variable v2 = new Variable();
		v2.name = v1.name;
		v2.unit = v1.unit;
		v2.value = v1.value;
		v2.min = v1.min;
		v2.max = v1.max;
		v2.type = v1.type;
		
		assertTrue(v1.hashCode() == v2.hashCode());
	}
}
