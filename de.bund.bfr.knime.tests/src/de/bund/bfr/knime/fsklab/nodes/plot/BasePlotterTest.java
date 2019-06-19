package de.bund.bfr.knime.fsklab.nodes.plot;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.bund.bfr.knime.fsklab.r.client.RController;

public class BasePlotterTest {
	
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testPlot() throws Exception {
		
		File file = testFolder.newFile("plot.png");
		
		try (RController controller = new RController()) {
			BasePlotter plotter = new BasePlotter(controller);
			String script = "hist(airquality$Temp)";
			plotter.plot(file, script);
		}
		
		// Check that the plot.png file was generated and is not empty
		assertTrue(file.exists());
		assertTrue(file.length() > 0);
	}
}
