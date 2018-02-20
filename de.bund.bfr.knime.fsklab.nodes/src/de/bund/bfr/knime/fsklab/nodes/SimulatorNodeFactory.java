package de.bund.bfr.knime.fsklab.nodes;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class SimulatorNodeFactory extends NodeFactory<SimulatorNodeModel> {

  @Override
  public SimulatorNodeModel createNodeModel() {
    return new SimulatorNodeModel();
  }

  @Override
  protected int getNrNodeViews() {
    return 0;
  }

  @Override
  public NodeView<SimulatorNodeModel> createNodeView(int viewIndex, SimulatorNodeModel nodeModel) {
    return null;
  }

  @Override
  protected boolean hasDialog() {
    return true;
  }

  @Override
  public NodeDialogPane createNodeDialogPane() {
    return new SimulatorNodeDialog();
  }
}
