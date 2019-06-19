package de.bund.bfr.knime.fsklab.nodes.plot;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.knime.python2.kernel.PythonKernel;
import org.knime.python2.kernel.PythonKernelOptions;

import de.bund.bfr.knime.fsklab.nodes.eval.Evaluator;
import de.bund.bfr.knime.fsklab.nodes.eval.PythonEvaluator;

@Ignore
public class PythonPlotterTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testPlot() throws Exception {

		File file = testFolder.newFile("plot.png");

		try (PythonKernel kernel = new PythonKernel(new PythonKernelOptions())) {
			Evaluator evaluator = new PythonEvaluator(kernel);
			PythonPlotter plotter = new PythonPlotter();
			String script = "import matplotlib.pyplot as plt\n" + "import numpy as np\n"
					+ "t = np.arange(0.0, 2.0, 0.01)\n" + "s = 1 + np.sin(2 * np.pi * t)\n"
					+ "fig, ax = plt.subplots()\n" + "ax.plot(t, s)\n"
					+ "ax.set(xlabel='time (s)', ylabel='voltage (mV)',\n"
					+ "       title='About as simple as it gets, folks')\n" + "ax.grid()";
			plotter.plot(evaluator, file, script);
		}
		
		// Check that the plot.png file was generated and is not empty
		assertTrue(file.exists());
		assertTrue(file.length() > 0);
	}
}
