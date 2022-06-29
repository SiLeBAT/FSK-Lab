package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import com.sun.jna.Platform;
import de.bund.bfr.knime.fsklab.r.client.RController;

/**
 * Plotter for base R plots.
 * 
 * @author Miguel de Alba, BfR
 */
public class BasePlotter implements ModelPlotter {

  private final RController controller;

  public BasePlotter(RController controller) {
    this.controller = controller;
  }

  @Override
  public void plotPng(File file, String script) throws Exception {

    // Initialize necessary R stuff to plot
    String configCmd =
        Platform.isMac() ? "library(Cairo); options(device='png', bitmapType='cairo')"
            : "options(device='png')";
    controller.eval(configCmd, false);

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    final String pngCommand = "png('" + path + "')";
    controller.eval(pngCommand, false);
    controller.eval(script, false);
    controller.eval("dev.off()", false);
    // if image is empty, try with print(last_plot())
    // this happens in rserve, if a plot is not explicitly printed
    // (e.g. when the ggplot function is stored in a variable
    // however, the last_plot() really only prints the very last plot
    // thus omitting any previous ones, therefore this zig-zagging
    if(file.length() < 1000) {
    	controller.eval("png('" + path + "')", false);
    	controller.eval(script, false);
    	controller.eval("print(last_plot());dev.off()", false);
    }
  }

  @Override
  public void plotSvg(File file, String script) throws Exception {

    // Initialize necessary R stuff to plot
    String configCmd =
        Platform.isMac() ? "library(Cairo); options(device='png', bitmapType='cairo')"
            : "options(device='png')";

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    //final String wholeScript =
    //    String.join("\n", configCmd, "svg('" + path + "')", script, "dev.off()");
    final String wholeScript =String.join("\n", configCmd, "Cairo(file='" + path + "',type='svg',dpi=72)", script, "dev.off()");
    controller.eval(wholeScript, false);
    // if image is empty, try with print(last_plot())
    // this happens in rserve, if a plot is not explicitly printed
    // (e.g. when the ggplot function is stored in a variable
    // however, the last_plot() really only prints the very last plot
    // thus omitting any previous ones, therefore this zig-zagging
    if(file.length() < 1000 && !script.isBlank()) {
    	controller.eval("svg('" + path + "')", false);
    	controller.eval(script, false);
    	controller.eval("print(last_plot());dev.off()", false);
    }
  }
}
