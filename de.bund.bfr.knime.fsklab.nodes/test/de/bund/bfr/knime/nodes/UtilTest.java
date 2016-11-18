package de.bund.bfr.knime.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.Util;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;

public class UtilTest {

	@Test
	public void test() {
		assertEquals(DataType.array, Util.getValueType("c(0, 1, 2, 3)"));
		assertEquals(DataType.numeric, Util.getValueType(Double.toString(Math.PI)));
		assertEquals(DataType.integer, Util.getValueType("0"));
		assertEquals(DataType.character, Util.getValueType("Hola caracola"));
	}
}
