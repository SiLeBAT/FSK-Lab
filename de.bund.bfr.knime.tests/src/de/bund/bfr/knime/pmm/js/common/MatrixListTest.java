package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class MatrixListTest {

	static Matrix matrix;
	
	static {
		matrix = new Matrix();
		matrix.id = MatrixTest.id;
		matrix.name = MatrixTest.name;
		matrix.detail = MatrixTest.detail;
		matrix.dbuuid = MatrixTest.dbuuid;
	}
	
	@Test
	public void testMatrices() {
		MatrixList list = new MatrixList();
		assertNull(list.getMatrices());
		
		Matrix[] matrices = new Matrix[] { matrix };
		list.setMatrices(matrices);
		
		Matrix expected = matrix;  // expected Matrix
		Matrix obtained = list.getMatrices()[0];  // obtained Matrix
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		MatrixList list = new MatrixList();
		list.setMatrices(new Matrix[] { matrix });
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);
		
		assertEquals(1, settings.getInt("numMatrices"));
		
		Matrix expected = matrix;  // expected Matrix
		Matrix obtained = new Matrix();  // obtained Matrix
		obtained.loadFromNodeSettings(settings.getNodeSettings("matrices" + 0));
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numMatrices", 1);
		matrix.saveToNodeSettings(settings.addNodeSettings("matrices" + 0));
		
		MatrixList list = new MatrixList();
		list.loadFromNodeSettings(settings);
		
		Matrix expected = matrix;  // expected Matrix
		Matrix obtained = list.getMatrices()[0];  // obtained Matrix
		
		assertEquals(expected.id, obtained.id);
		assertEquals(expected.name, obtained.name);
		assertEquals(expected.detail, obtained.detail);
		assertEquals(expected.dbuuid, obtained.dbuuid);
	}
}
