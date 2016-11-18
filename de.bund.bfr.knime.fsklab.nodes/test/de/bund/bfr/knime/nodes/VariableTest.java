package de.bund.bfr.knime.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.Variable;

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

}
