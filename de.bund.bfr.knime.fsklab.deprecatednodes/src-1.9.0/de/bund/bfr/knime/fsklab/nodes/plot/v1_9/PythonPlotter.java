package de.bund.bfr.knime.fsklab.nodes.plot.v1_9;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.knime.python2.kernel.PythonKernel;


public class PythonPlotter implements ModelPlotter {
  
  private final PythonKernel kernel;
  
  public PythonPlotter(PythonKernel kernel) {
    this.kernel = kernel;
  }

  @Override
  public void plotPng(File file, String script) throws Exception {

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    // Disable interactive IO with plt.ioff() and save current figure with plt.gcf
    final String wholeScript = String.join("\n", "import matplotlib.pyplot as plt", "import numpy as np",
        "plt.ioff()", script, "plt.gcf().savefig('" + path + "', format='png')");
    kernel.execute(wholeScript);
  }
  
  @Override
  public void plotSvg(File file, String script) throws Exception {

    // Get image path (with proper slashes)
    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

    // Disable interactive IO with plt.ioff() and save current figure with plt.gcf
    final String wholeScript = String.join("\n", "import matplotlib.pyplot as plt", "import numpy as np",
        "plt.ioff()", script, "plt.gcf().savefig('" + path + "', format='svg')");
    kernel.execute(wholeScript);
    kernel.execute("plt.clf()");
  }
}
