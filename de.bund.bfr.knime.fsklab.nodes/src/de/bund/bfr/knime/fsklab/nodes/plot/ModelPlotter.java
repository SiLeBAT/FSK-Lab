package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;

public interface ModelPlotter {

  /**
   * @param file
   * @param script
   * @throws Exception
   */
  public void plotPng(File file, String script) throws Exception;
}
