package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MatrixXml;

@SuppressWarnings("static-method")
public class MatrixTest {

	static int id = 21003;
	static String name = "culture media";
	static String detail = "broth";
	static String dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";

	@Test
	public void testConstructor() {
		final Matrix matrix = new Matrix();
		assertNull(matrix.id);
		assertNull(matrix.name);
		assertNull(matrix.detail);
		assertNull(matrix.dbuuid);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final Matrix matrix = new Matrix();
		matrix.id = id;
		matrix.name = name;
		matrix.detail = detail;
		matrix.dbuuid = dbuuid;

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		matrix.saveToNodeSettings(settings);

		assertEquals(id, settings.getInt("id"));
		assertEquals(name, settings.getString("name"));
		assertEquals(detail, settings.getString("detail"));
		assertEquals(dbuuid, settings.getString("dbuuid"));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", id);
		settings.addString("name", name);
		settings.addString("detail", detail);
		settings.addString("dbuuid", dbuuid);

		final Matrix matrix = new Matrix();
		matrix.loadFromNodeSettings(settings);

		assertEquals(id, matrix.id.intValue());
		assertEquals(name, matrix.name);
		assertEquals(detail, matrix.detail);
		assertEquals(dbuuid, matrix.dbuuid);
	}

	@Test
	public void testToMatrix() {
		final MatrixXml matrixXml = new MatrixXml(id, name, detail, dbuuid);
		final Matrix matrix = Matrix.toMatrix(matrixXml);

		assertEquals(id, matrix.id.intValue());
		assertEquals(name, matrix.name);
		assertEquals(detail, matrix.detail);
		assertEquals(dbuuid, matrix.dbuuid);
	}

	@Test
	public void testToMatrixXml() {
		final Matrix matrix = new Matrix();
		matrix.id = id;
		matrix.name = name;
		matrix.detail = detail;
		matrix.dbuuid = dbuuid;
		final MatrixXml matrixXml = matrix.toMatrixXml();

		assertEquals(id, matrixXml.id.intValue());
		assertEquals(name, matrixXml.name);
		assertEquals(detail, matrixXml.detail);
		assertEquals(dbuuid, matrixXml.dbuuid);
	}
}
