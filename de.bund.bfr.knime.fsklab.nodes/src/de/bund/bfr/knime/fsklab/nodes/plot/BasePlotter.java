package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import com.sun.jna.Platform;
import de.bund.bfr.knime.fsklab.nodes.eval.Evaluator;

/**
 * Plotter for base R plots.
 * 
 * @author Miguel de Alba, BfR
 */
public class BasePlotter implements ModelPlotter {

  @Override
  public void plot(Evaluator evaluator, File file, String script) throws Exception {

    // Initialize necessary R stuff to plot
    String configCmd =
        Platform.isMac() ? "library('Cairo'); options(device='png', bitmapType='cairo')"
            : "options(device='png')";
    evaluator.eval(configCmd);

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    final String pngCommand = "png('" + path + "')";
    evaluator.eval(pngCommand);
    evaluator.eval(script);
    evaluator.eval("dev.off()");
  }
}
