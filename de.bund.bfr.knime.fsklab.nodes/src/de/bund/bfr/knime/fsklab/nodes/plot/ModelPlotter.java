package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;
import de.bund.bfr.knime.fsklab.r.client.RController;

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
  public void plot(RController controller, File file, String script) throws Exception;
}
