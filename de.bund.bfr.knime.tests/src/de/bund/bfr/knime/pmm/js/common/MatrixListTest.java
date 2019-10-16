package de.bund.bfr.knime.pmm.js.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class MatrixListTest {

	static Matrix matrix = MatrixTest.matrix;

	@Test
	public void testMatrices() {
		final MatrixList list = new MatrixList();
		assertThat(list.getMatrices(), is(nullValue()));

		final Matrix[] matrices = new Matrix[] { matrix };
		list.setMatrices(matrices);

		final Matrix expected = matrix;  // expected Matrix
		final Matrix obtained = list.getMatrices()[0];  // obtained Matrix
		compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final MatrixList list = new MatrixList();
		list.setMatrices(new Matrix[] { matrix });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numMatrices"));

		final Matrix expected = matrix;  // expected Matrix
		final Matrix obtained = new Matrix();  // obtained Matrix
		obtained.loadFromNodeSettings(settings.getNodeSettings("matrices" + 0));

		compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numMatrices", 1);
		matrix.saveToNodeSettings(settings.addNodeSettings("matrices" + 0));

		final MatrixList list = new MatrixList();
		list.loadFromNodeSettings(settings);

		final Matrix expected = matrix;  // expected Matrix
		final Matrix obtained = list.getMatrices()[0];  // obtained Matrix
		compare(obtained, expected);
	}

	private static void compare(Matrix obtained, Matrix expected) {
		assertThat(obtained.id, equalTo(expected.id));
		assertThat(obtained.name, equalTo(expected.name));
		assertThat(obtained.detail, equalTo(expected.detail));
		assertThat(obtained.dbuuid, equalTo(expected.dbuuid));
	}
}
