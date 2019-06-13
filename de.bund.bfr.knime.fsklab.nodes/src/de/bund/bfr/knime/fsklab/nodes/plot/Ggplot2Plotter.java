package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import de.bund.bfr.knime.fsklab.r.client.RController;

/**
 * Plotter for the R package ggplot2.
 * 
 * @see https://cran.r-project.org/web/packages/ggplot2/index.html
 * @author Miguel de Alba, BfR
 */
public class Ggplot2Plotter implements ModelPlotter {

  @Override
  public void plot(RController controller, File file, String script) throws Exception {

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());
        
    controller.eval("png('" + path + "')", false);
    controller.eval(script, false);
    controller.eval("print(last_plot()); dev.off()", false);
  }
}
