package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import de.bund.bfr.knime.fsklab.nodes.eval.Evaluator;

public class PythonPlotter implements ModelPlotter {

  @Override
  public void plot(Evaluator evaluator, File file, String script) throws Exception {

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    // Disable interactive IO with plt.ioff() and save current figure with plt.gcf
    final String wholeScript = String.join("\n", "import matplotlib.pyplot as plt", "import numpy as np",
        "plt.ioff()", script, "plt.gcf().savefig('" + path + "')");
    evaluator.eval(wholeScript);
  }
}
