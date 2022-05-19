package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import com.sun.jna.Platform;
import de.bund.bfr.knime.fsklab.r.client.RController;

/**
 * Plotter for the R package ggplot2.
 * 
 * @see https://cran.r-project.org/web/packages/ggplot2/index.html
 * @author Miguel de Alba, BfR
 */
public class Ggplot2Plotter implements ModelPlotter {

  private final RController controller;

  public Ggplot2Plotter(RController controller) {
    this.controller = controller;
  }

  @Override
  public void plotPng(File file, String script) throws Exception {
    
	  createPlot(file, script, "png");

  }

  @Override
  public void plotSvg(File file, String script) throws Exception {
	  controller.eval("require(ggplot2)", false);
	  createPlot(file, script, "svg");
    
  }
  
  private void createPlot(File file, String script, String format) throws Exception{

	  String configCmd =
			  Platform.isMac() ? "library(Cairo); options(device='png', bitmapType='cairo')"
					  : "options(device='png')";
	  // Get image path (with proper slashes)
	  final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());


	  controller.eval(configCmd,false);

	  controller.eval(format + "('" + path + "')", false);
	  controller.eval(script, false);
	  //controller.eval("ggsave('" + path + "')", false);// removed size width=4, height=4    
	  controller.eval("dev.off()", false);

	  // if image is empty, try with print(last_plot())
	  // this happens in rserve, if a plot is not explicitly printed
	  // (e.g. when the ggplot function is stored in a variable
	  // however, the last_plot() really only prints the very last plot
	  // thus omitting any previous ones, therefore this zig-zagging
	  if(file.length() < 1000 && !script.trim().isEmpty()) {
		  controller.eval(format + "('" + path + "')", false);
		  controller.eval(script, false);
		  controller.eval("print(last_plot());dev.off()", false);
	  }
  }
}
