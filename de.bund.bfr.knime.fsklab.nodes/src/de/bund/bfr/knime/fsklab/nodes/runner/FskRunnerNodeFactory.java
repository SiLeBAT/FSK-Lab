package de.bund.bfr.knime.fsklab.nodes.runner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

public class FskRunnerNodeFactory extends NodeFactory<FskRunnerNodeModel> {

  @Override
  public FskRunnerNodeModel createNodeModel() {
    return new FskRunnerNodeModel();
  }

  @Override
  protected int getNrNodeViews() {
    return 1;
  }

  @Override
  public NodeView<FskRunnerNodeModel> createNodeView(int viewIndex,
      FskRunnerNodeModel nodeModel) {
    return new FskRunnerNodeView(nodeModel);
  }

  @Override
  protected boolean hasDialog() {
    return true;
  }

  @Override
  protected NodeDialogPane createNodeDialogPane() {
    return new DefaultNodeSettingsPane();
  }
}
