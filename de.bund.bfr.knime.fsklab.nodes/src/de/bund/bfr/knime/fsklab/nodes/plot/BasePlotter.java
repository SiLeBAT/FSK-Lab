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
        Platform.isMac() ? "library('Cairo'); options(device='png', bitmapType='cairo')"
            : "options(device='png')";
    controller.eval(configCmd, false);

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    final String pngCommand = "png('" + path + "')";
    controller.eval(pngCommand, false);
    controller.eval(script, false);
    controller.eval("dev.off()", false);
  }
}
