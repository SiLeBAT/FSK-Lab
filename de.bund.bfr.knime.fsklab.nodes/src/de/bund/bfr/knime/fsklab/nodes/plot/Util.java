package de.bund.bfr.knime.fsklab.nodes.plot;

import java.util.List;

public class Util {

  public static ModelPlotter findPlotter(List<String> packages) {
    
    if (packages.contains("ggplot2"))
      return new Ggplot2Plotter();
    
    return new BasePlotter();
  }
}
