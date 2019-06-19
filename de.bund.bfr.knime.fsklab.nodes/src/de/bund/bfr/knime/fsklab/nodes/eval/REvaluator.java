package de.bund.bfr.knime.fsklab.nodes.eval;

import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.RController;

public class REvaluator implements Evaluator {

  private RController controller;
  
  public REvaluator(RController controller) {
    this.controller = controller;
  }
  
  @Override
  public void eval(String src) throws RException {
    controller.eval(src, false);
  }
}
