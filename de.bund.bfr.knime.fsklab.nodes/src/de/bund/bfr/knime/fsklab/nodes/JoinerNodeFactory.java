// TODO: Add copyright notice
package de.bund.bfr.knime.fsklab.nodes;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeView;

public class JoinerNodeFactory extends NodeFactory<NodeModel> {

  @Override
  protected int getNrNodeViews() {
    return 0;
  }

  @Override
  protected boolean hasDialog() {
    return true;
  }

  @Override
  protected NodeDialogPane createNodeDialogPane() {
    return new JoinerNodeDialog();
  }

  @Override
  public NodeModel createNodeModel() {
    return new JoinerNodeModel();
  }

  @Override
  public NodeView<NodeModel> createNodeView(int viewIndex, NodeModel nodeModel) {
    return null;
  }
}
