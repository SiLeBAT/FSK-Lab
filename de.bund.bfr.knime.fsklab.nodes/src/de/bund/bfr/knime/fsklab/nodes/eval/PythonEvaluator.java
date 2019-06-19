package de.bund.bfr.knime.fsklab.nodes.eval;

import org.knime.python2.kernel.PythonKernel;

public class PythonEvaluator implements Evaluator {

  private PythonKernel kernel;
  
  public PythonEvaluator(PythonKernel kernel) {
    this.kernel = kernel;
  }
  
  @Override
  public void eval(String src) throws Exception {
    kernel.execute(src);
  }
}
