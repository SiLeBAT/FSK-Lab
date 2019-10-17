package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.knime.pmm.common.MatrixXml;
@SuppressWarnings("static-method")
public class MatrixTest {

	static Matrix matrix;
	static {
		matrix = new Matrix();
		matrix.id = 21003;
		matrix.name = "culture media";
		matrix.detail = "broth";
		matrix.dbuuid = "6df109d0-f6b1-409d-a286-0687b1aca001";
	}

	@Test
	public void testConstructor() {
		final Matrix matrix = new Matrix();
		assertThat(matrix.id, is(nullValue()));
		assertThat(matrix.name, is(nullValue()));
		assertThat(matrix.detail, is(nullValue()));
		assertThat(matrix.dbuuid, is(nullValue()));
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		matrix.saveToNodeSettings(settings);

		assertThat(settings.getInt("id"), equalTo(matrix.id));
		assertThat(settings.getString("name"), equalTo(matrix.name));
		assertThat(settings.getString("detail"), equalTo(matrix.detail));
		assertThat(settings.getString("dbuuid"), equalTo(matrix.dbuuid));
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("id", matrix.id);
		settings.addString("name", matrix.name);
		settings.addString("detail", matrix.detail);
		settings.addString("dbuuid", matrix.dbuuid);

		final Matrix obtained = new Matrix();
		obtained.loadFromNodeSettings(settings);
		TestUtils.compare(obtained, matrix);
	}

	@Test
	public void testToMatrix() {
		final MatrixXml matrixXml = new MatrixXml(matrix.id, matrix.name, matrix.detail, matrix.dbuuid);
		final Matrix obtained = Matrix.toMatrix(matrixXml);
		TestUtils.compare(obtained, matrix);
	}

	@Test
	public void testToMatrixXml() {
		final MatrixXml obtained = matrix.toMatrixXml();
		assertThat(obtained.id, equalTo(matrix.id));
		assertThat(obtained.name, equalTo(matrix.name));
		assertThat(obtained.detail, equalTo(matrix.detail));
		assertThat(obtained.dbuuid, equalTo(matrix.dbuuid));
	}
}
