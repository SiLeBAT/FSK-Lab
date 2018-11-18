package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MatrixXml;

public class MatrixTest {

	static int id = 21003;
	static String name = "culture media";
	static String detail = "broth";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	
	@Test
	public void testId() {
		Matrix matrix = new Matrix();
		assertNull(matrix.getId());
		
		matrix.setId(id);
		assertEquals(id, matrix.getId().intValue());
	}
	
	@Test
	public void testName() {
		Matrix matrix = new Matrix();
		assertNull(matrix.getName());
		
		matrix.setName(name);
		assertEquals(name, matrix.getName());
	}
	
	@Test
	public void testDetail() {
		Matrix matrix = new Matrix();
		assertNull(matrix.getDetail());
		
		matrix.setDetail(detail);
		assertEquals(detail, matrix.getDetail());
	}

	@Test
	public void testDbuuid() {
		Matrix matrix = new Matrix();
		assertNull(matrix.getDbuuid());
		
		matrix.setDbuuid(dbuuid);
		assertEquals(dbuuid, matrix.getDbuuid());
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		Matrix matrix = new Matrix();
		matrix.setId(id);
		matrix.setName(name);
		matrix.setDetail(detail);
		matrix.setDbuuid(dbuuid);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		matrix.saveToNodeSettings(settings);
		
		assertEquals(id, settings.getInt(Matrix.ID));
		assertEquals(name, settings.getString(Matrix.NAME));
		assertEquals(detail, settings.getString(Matrix.DETAIL));
		assertEquals(dbuuid, settings.getString(Matrix.DBUUID));
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(Matrix.ID, id);
		settings.addString(Matrix.NAME, name);
		settings.addString(Matrix.DETAIL, detail);
		settings.addString(Matrix.DBUUID, dbuuid);
		
		Matrix matrix = new Matrix();
		matrix.loadFromNodeSettings(settings);
		
		assertEquals(id, matrix.getId().intValue());
		assertEquals(name, matrix.getName());
		assertEquals(detail, matrix.getDetail());
		assertEquals(dbuuid, matrix.getDbuuid());
	}
	
	@Test
	public void testToMatrix() {
		MatrixXml matrixXml = new MatrixXml(id, name, detail, dbuuid);
		Matrix matrix = Matrix.toMatrix(matrixXml);
		
		assertEquals(id, matrix.getId().intValue());
		assertEquals(name, matrix.getName());
		assertEquals(detail, matrix.getDetail());
		assertEquals(dbuuid, matrix.getDbuuid());
	}
	
	@Test
	public void testToMatrixXml() {
		Matrix matrix = new Matrix();
		matrix.setId(id);
		matrix.setName(name);
		matrix.setDetail(detail);
		matrix.setDbuuid(dbuuid);
		MatrixXml matrixXml = matrix.toMatrixXml();

		assertEquals(id, matrixXml.id.intValue());
		assertEquals(name, matrixXml.name);
		assertEquals(detail, matrixXml.detail);
		assertEquals(dbuuid, matrixXml.dbuuid);
	}
}
