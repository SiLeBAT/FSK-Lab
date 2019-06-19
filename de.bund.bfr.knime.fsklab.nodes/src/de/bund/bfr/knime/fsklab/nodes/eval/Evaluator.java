package de.bund.bfr.knime.fsklab.nodes.eval;

/**
 * Evaluates code and do not return any output.
 * 
 * @author Miguel de Alba, BfR
 */
public interface Evaluator {

  public void eval(String src) throws Exception;
}
