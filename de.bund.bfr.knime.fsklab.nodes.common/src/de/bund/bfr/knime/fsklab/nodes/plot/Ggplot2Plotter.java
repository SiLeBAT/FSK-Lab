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
    
    // Initialize necessary R stuff to plot
    String configCmd =
        Platform.isMac() ? "library('Cairo'); options(device='png', bitmapType='cairo')"
            : "options(device='png')";
    controller.eval(configCmd, false);

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    controller.eval("png('" + path + "')", false);
    controller.eval(script, false);
    controller.eval("print(last_plot()); dev.off()", false);
  }

  @Override
  public void plotSvg(File file, String script) throws Exception {

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    final String wholeScript = script + "\nggsave('" + path + "')";
    controller.eval(wholeScript, false);
  }
}
