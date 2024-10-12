package de.bund.bfr.knime.fsklab.nodes.plot;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.bund.bfr.knime.fsklab.r.client.RController;

@Ignore
public class Ggplot2PlotterTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testPlotPng() throws Exception {

		final File file = testFolder.newFile("plot.png");

		try (RController controller = new RController(false)) {

			// Install ggplot2 if missing
			LibRegistry.instance().install(Arrays.asList("ggplot2"));

			final Ggplot2Plotter plotter = new Ggplot2Plotter(controller);
			final String script = "library(ggplot2);"
					+ "ggplot(mtcars, aes(factor(cyl), mpg)) + geom_violin(aes(fill = cyl))";
			plotter.plotPng(file, script);
		}

		// Check that the plot.png file was generated and is not empty
		assertTrue(file.exists());
		assertTrue(file.length() > 0);
	}

	@Test
	public void testPlotSvg() throws Exception {

		final File file = testFolder.newFile("plot.svg");

		try (RController controller = new RController(false)) {

			// Install ggplot2 if missing
			LibRegistry.instance().install(Arrays.asList("ggplot2"));

			final Ggplot2Plotter plotter = new Ggplot2Plotter(controller);
			final String script = "library(ggplot2);"
					+ "ggplot(mtcars, aes(factor(cyl), mpg)) + geom_violin(aes(fill = cyl))";
			plotter.plotSvg(file, script);
		}

		// Check that the plot.png file was generated and is not empty
		assertTrue(file.exists());
		assertTrue(file.length() > 0);
	}
}
