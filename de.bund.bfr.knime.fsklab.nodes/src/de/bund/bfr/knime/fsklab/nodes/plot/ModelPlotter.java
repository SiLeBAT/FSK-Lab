package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import de.bund.bfr.knime.fsklab.nodes.eval.Evaluator;

public interface ModelPlotter {

  /**
   * 
   * @param controller
   * @param file
   * @param script
   * @param width Width in pixels
   * @param height Width in pixels
   * @throws Exception
   */
  public void plot(Evaluator evaluator, File file, String script) throws Exception;
}
