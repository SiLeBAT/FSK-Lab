package de.bund.bfr.knime.fsklab.nodes.plot;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.bund.bfr.knime.fsklab.r.client.RController;

@Ignore
public class BasePlotterTest {
	
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testPlotPng() throws Exception {
		
		File file = testFolder.newFile("plot.png");
		
		try (RController controller = new RController()) {
			BasePlotter plotter = new BasePlotter(controller);
			String script = "hist(airquality$Temp)";
			plotter.plotPng(file, script);
		}
		
		// Check that the plot.png file was generated and is not empty
		assertTrue(file.exists());
		assertTrue(file.length() > 0);
	}
	
	@Test
	public void testPlotSvg() throws Exception {
		
		File file = testFolder.newFile("plot.svg");
		
		try (RController controller = new RController()) {
			BasePlotter plotter = new BasePlotter(controller);
			String script = "hist(airquality$Temp)";
			plotter.plotSvg(file, script);
		}
		
		// Check that the plot.png file was generated and is not empty
		assertTrue(file.exists());
		assertTrue(file.length() > 0);		
	}
}
